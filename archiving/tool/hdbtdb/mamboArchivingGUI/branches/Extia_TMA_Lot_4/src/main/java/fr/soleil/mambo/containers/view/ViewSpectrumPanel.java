/*
 * Synchrotron Soleil File : ViewSpectrumPanel.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 24 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumPanel.java,v
 */
package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.comete.dao.util.DefaultDataArrayDAO;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCViewSpectrumChartAction;
import fr.soleil.mambo.actions.view.VCViewSpectrumFilterAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.editors.AttributesSelectTableEditor;
import fr.soleil.mambo.components.renderers.ViewSpectrumTableRenderer;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewSpectrumTableModel;
import fr.soleil.mambo.tools.ATKToCometUtilities;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * @author SOLEIL
 */
public class ViewSpectrumPanel extends MamboCleanablePanel {

    private static final long          serialVersionUID = -8895787942729175801L;
    private ViewConfigurationAttribute spectrum;
    private JButton                    viewButton, allRead, allWrite, noneRead,
            noneWrite;
    private JLDataView[]               readData;
    private JLDataView[]               writeData;
    private ViewSpectrumTableModel     model;
    private JTable                     choiceTable;
    private JScrollPane                choiceTableScrollPane;
    private JPanel                     buttonPanel;
    private JLabel                     loadingLabel;
    private final static ImageIcon     viewIcon         = new ImageIcon(
                                                                Mambo.class
                                                                        .getResource("icons/View.gif"));
    int                                writable;
    private boolean                    cleaning         = false;
    private ViewConfigurationBean      viewConfigurationBean;
    private final static Dimension     refSize          = new Dimension(50, 50);

    public ViewSpectrumPanel(ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        readData = null;
        writeData = null;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        this.spectrum = theSpectrum;
        if (spectrum == null
                || viewConfigurationBean.getViewConfiguration() == null
                || !spectrum.isSpectrum(viewConfigurationBean
                        .getViewConfiguration().getData().isHistoric()))
            return;
        loadingLabel = new JLabel(Messages
                .getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        add(loadingLabel);
        initLayout();
        initBorder();
    }

    private void initComponents() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning)
            return;
        viewButton = new JButton();
        viewButton.setMargin(new Insets(0, 0, 0, 0));
        viewButton.addActionListener(new VCViewSpectrumChartAction(this));
        allRead = new JButton(new VCViewSpectrumFilterAction(Messages
                .getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL")
                + " " + Messages.getMessage("VIEW_SPECTRUM_READ"), this, true,
                true));
        allRead.setMargin(new Insets(0, 0, 0, 0));
        noneRead = new JButton(new VCViewSpectrumFilterAction(Messages
                .getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE")
                + " " + Messages.getMessage("VIEW_SPECTRUM_READ"), this, false,
                true));
        noneRead.setMargin(new Insets(0, 0, 0, 0));
        allWrite = new JButton(new VCViewSpectrumFilterAction(Messages
                .getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL")
                + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"), this, true,
                false));
        allWrite.setMargin(new Insets(0, 0, 0, 0));
        noneWrite = new JButton(new VCViewSpectrumFilterAction(Messages
                .getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE")
                + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"), this,
                false, false));
        noneWrite.setMargin(new Insets(0, 0, 0, 0));
        if (((readData == null) || (readData.length == 0))
                && ((writeData == null) || (writeData.length == 0))) {
            writable = ViewSpectrumTableModel.UNKNOWN;
            viewButton.setText(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA")
                    + " " + getName());
            viewButton.setEnabled(false);
            model = new ViewSpectrumTableModel(null, null);
            int viewType = spectrum.getProperties().getPlotProperties()
                    .getSpectrumViewType();
            model.setViewSpectrumType(viewType);
            allRead.setEnabled(false);
            noneRead.setEnabled(false);
            allWrite.setEnabled(false);
            noneWrite.setEnabled(false);
        } else {
            viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON")
                    + " " + getName());
            viewButton.setEnabled(true);
            if ((readData != null) && (readData.length > 0)) {
                allRead.setEnabled(true);
                noneRead.setEnabled(true);
                if ((writeData != null) && (writeData.length > 0)) {
                    allWrite.setEnabled(true);
                    noneWrite.setEnabled(true);
                    writable = ViewSpectrumTableModel.RW;
                } else {
                    allWrite.setEnabled(false);
                    noneWrite.setEnabled(false);
                    writable = ViewSpectrumTableModel.R;
                }
            } else {
                allWrite.setEnabled(true);
                noneWrite.setEnabled(true);
                allRead.setEnabled(false);
                noneRead.setEnabled(false);
                writable = ViewSpectrumTableModel.W;
            }
            int readLength = 0;
            if (readData != null) {
                readLength = readData.length;
            }
            String[] readNames = (readData == null ? null
                    : new String[readLength]);
            for (int i = 0; i < readLength; i++) {
                readNames[i] = readData[i].getName();
            }
            int writeLength = 0;
            if (writeData != null) {
                writeLength = writeData.length;
            }
            String[] writeNames = (writeData == null ? null
                    : new String[writeLength]);
            for (int i = 0; i < writeLength; i++) {
                writeNames[i] = writeData[i].getName();
            }
            model = new ViewSpectrumTableModel(readNames, writeNames);
            int viewType = spectrum.getProperties().getPlotProperties()
                    .getSpectrumViewType();
            model.setViewSpectrumType(viewType);
        }
        buttonPanel = new JPanel();
        GUIUtilities.setObjectBackground(buttonPanel, GUIUtilities.VIEW_COLOR);
        buttonPanel.setLayout(new SpringLayout());
        switch (writable) {
            case ViewSpectrumTableModel.R:
                buttonPanel.add(allRead);
                buttonPanel.add(noneRead);
                SpringUtilities.makeCompactGrid(buttonPanel, 2, 1, 5, 5, 5, 5,
                        true);
                break;
            case ViewSpectrumTableModel.W:
                buttonPanel.add(allWrite);
                buttonPanel.add(noneWrite);
                SpringUtilities.makeCompactGrid(buttonPanel, 2, 1, 5, 5, 5, 5,
                        true);
                break;
            case ViewSpectrumTableModel.RW:
                buttonPanel.add(allRead);
                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(allWrite);
                buttonPanel.add(noneRead);
                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(noneWrite);
                SpringUtilities.makeCompactGrid(buttonPanel, 2, 3, 5, 5, 5, 5,
                        true);
                break;
            case ViewSpectrumTableModel.UNKNOWN:
                break;
        }
        viewButton.setIcon(viewIcon);
        GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
        choiceTable = new JTable(model);
        choiceTable.setMinimumSize(new Dimension(110, 200));
        choiceTable.setDefaultRenderer(Object.class,
                new ViewSpectrumTableRenderer());
        choiceTable.setDefaultEditor(Object.class,
                new AttributesSelectTableEditor());
        GUIUtilities.setObjectBackground(choiceTable, GUIUtilities.VIEW_COLOR);
        choiceTableScrollPane = new JScrollPane(choiceTable);
        choiceTableScrollPane.setPreferredSize(refSize);
        choiceTableScrollPane.setMinimumSize(refSize);
        GUIUtilities.setObjectBackground(choiceTableScrollPane,
                GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(choiceTableScrollPane.getViewport(),
                GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(choiceTableScrollPane
                .getHorizontalScrollBar(), GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(choiceTableScrollPane
                .getVerticalScrollBar(), GUIUtilities.VIEW_COLOR);
    }

    private void addComponents() {
        if (buttonPanel == null)
            return;
        this.add(buttonPanel);
        if (choiceTableScrollPane == null)
            return;
        this.add(choiceTableScrollPane);
        if (viewButton == null)
            return;
        this.add(viewButton);
    }

    public void initLayout() {
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, 5,
                5, 5, 5, true);
    }

    private void initBorder() {
        String msg = getFullName();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP);
        Border border = (Border) (tb);
        this.setBorder(border);
    }

    private void initData() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (cleaning)
            return;
        ViewConfiguration selectedViewConfiguration = viewConfigurationBean
                .getViewConfiguration();
        if (selectedViewConfiguration == null) {
            return;
        }
        ViewConfigurationData selectedViewConfigurationData = selectedViewConfiguration
                .getData();

        String[] param = new String[3];
        param[0] = getFullName();
        Timestamp start = selectedViewConfigurationData.getStartDate();
        Timestamp end = selectedViewConfigurationData.getEndDate();
        String startDate = "";
        String endDate = "";
        try {
            startDate = extractingManager.timeToDateSGBD(start.getTime());
            endDate = extractingManager.timeToDateSGBD(end.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            return;
        }

        param[1] = startDate;
        param[2] = endDate;
        Hashtable<String, Object> table;
        try {
            // calling the appropriate method
            int viewType = spectrum.getProperties().getPlotProperties()
                    .getSpectrumViewType();
            table = null;
            if (viewType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDICE) {
                // FIXME: use only one method, and put false as last argument
                table = extractingManager.retrieveDataHash(param,
                        selectedViewConfigurationData.isHistoric(),
                        selectedViewConfigurationData.getSamplingType());
            } else if (viewType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME) {
                table = extractingManager.retrieveDataHash(param,
                        selectedViewConfigurationData.isHistoric(),
                        selectedViewConfigurationData.getSamplingType(), true);
            }
            if (cleaning)
                return;
        } catch (Exception e) {
            if (cleaning)
                return;
            e.printStackTrace();
            LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            return;
        }

        if (table == null) {
            // System.out.println("table null");
            readData = new JLDataView[0];
            writeData = new JLDataView[0];
            return;
        }
        if (table.get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY) != null) {
            readData = (JLDataView[]) table
                    .get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY);
        } else {
            readData = new JLDataView[0];
        }
        if (table.get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY) != null) {
            writeData = (JLDataView[]) table
                    .get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY);
        } else {
            writeData = new JLDataView[0];
        }
        table.clear();
        table = null;
    }

    public String getName() {
        if (spectrum == null) {
            return "";
        } else {
            return spectrum.getName();
        }
    }

    public DefaultDataArrayDAO getFilteredSpectrumDAO() {
        return ATKToCometUtilities.createSpectrumDAO(getSelectedReadView(),
                getSelectedWriteView());
    }

    public int getViewSpectrumType() {
        return spectrum.getProperties().getPlotProperties()
                .getSpectrumViewType();
    }

    private JLDataView[] getSelectedReadView() {
        if (model == null) {
            return null;
        }
        if ((readData == null) || (readData.length == 0)) {
            return null;
        } else {
            ArrayList<JLDataView> selectionList = new ArrayList<JLDataView>();
            JLDataView[] selection;
            for (int i = 0; i < readData.length; i++) {
                Boolean val = new Boolean(false);
                if (model.getValueAt(i, 0) instanceof Boolean) {
                    val = (Boolean) model.getValueAt(i, 0);
                }
                if (val.booleanValue()) {
                    selectionList.add(readData[i]);
                }
            }
            selection = selectionList.toArray(new JLDataView[selectionList
                    .size()]);
            selectionList.clear();
            selectionList = null;
            return selection;
        }
    }

    private JLDataView[] getSelectedWriteView() {
        if (model == null) {
            return null;
        }
        if ((writeData == null) || (writeData.length == 0)) {
            return null;
        } else {
            ArrayList<JLDataView> selectionList = new ArrayList<JLDataView>();
            JLDataView[] selection;
            int column = 0;
            if ((readData != null) && (readData.length > 0)) {
                column = 3;
            }
            for (int i = 0; i < writeData.length; i++) {
                Boolean val = new Boolean(false);
                if (model.getValueAt(i, column) instanceof Boolean) {
                    val = (Boolean) model.getValueAt(i, column);
                }
                if (val.booleanValue()) {
                    selectionList.add(readData[i]);
                }
            }
            selection = selectionList.toArray(new JLDataView[selectionList
                    .size()]);
            selectionList.clear();
            selectionList = null;
            return selection;
        }
    }

    public String getFullName() {
        if (spectrum == null) {
            return "";
        } else {
            return spectrum.getCompleteName();
        }
    }

    public void setAllReadSelected(boolean selected) {
        // System.out.println("writable :" + writable);
        if (choiceTable == null || choiceTable.getRowCount() == 0
                || writable == ViewSpectrumTableModel.UNKNOWN
                || writable == ViewSpectrumTableModel.W) {
            return;
        }
        for (int i = 0; i < ((ViewSpectrumTableModel) choiceTable.getModel())
                .getReadLength(); i++) {
            choiceTable.getModel().setValueAt(new Boolean(selected), i, 0);
        }
    }

    public void setAllWriteSelected(boolean selected) {
        // System.out.println("writable :" + writable);
        if (choiceTable == null || choiceTable.getRowCount() == 0
                || writable == ViewSpectrumTableModel.UNKNOWN
                || writable == ViewSpectrumTableModel.R) {
            return;
        }
        if (writable == ViewSpectrumTableModel.W) {
            for (int i = 0; i < choiceTable.getRowCount(); i++) {
                choiceTable.getModel().setValueAt(new Boolean(selected), i, 0);
            }
        } else { // RW
            for (int i = 0; i < ((ViewSpectrumTableModel) choiceTable
                    .getModel()).getWriteLength(); i++) {
                choiceTable.getModel().setValueAt(new Boolean(selected), i, 3);
            }
        }
    }

    public void disposeTable() {
        if (choiceTable == null || choiceTable.getRowCount() == 0) {
            return;
        }
        if (choiceTable.isEditing()) {
            int row = choiceTable.getEditingRow();
            int col = choiceTable.getEditingColumn();
            Component comp = choiceTable.getEditorComponent();
            if (comp != null && comp instanceof JCheckBox) {
                choiceTable.getModel().setValueAt(
                        new Boolean(((JCheckBox) comp).isSelected()), row, col);
            }
            choiceTable.getDefaultEditor(Object.class).cancelCellEditing();
        }
    }

    public void clean() {
        cleaning = true;
        removeAll();
        if (buttonPanel != null) {
            buttonPanel.removeAll();
            buttonPanel = null;
        }
        viewButton = null;
        allRead = null;
        allWrite = null;
        noneRead = null;
        noneWrite = null;
        choiceTable = null;
        choiceTableScrollPane = null;
        model = null;
        spectrum = null;
        if (readData != null) {
            for (int i = 0; i < readData.length; i++) {
                readData[i] = null;
            }
            readData = null;
        }
        if (writeData != null) {
            for (int i = 0; i < writeData.length; i++) {
                writeData[i] = null;
            }
            writeData = null;
        }
        loadingLabel = null;
        viewConfigurationBean = null;
    }

    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        try {
            if (cleaning)
                return;
            initData();
            if (cleaning)
                return;
            initComponents();
            if (cleaning)
                return;
            addComponents();
            if (cleaning)
                return;
            initLayout();
            if (cleaning)
                return;
            if (loadingLabel != null) {
                loadingLabel.setText(Messages
                        .getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setToolTipText(Messages
                        .getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setForeground(Color.GREEN);
            }
            if (extractingManager.isCanceled() || cleaning)
                return;
            updateUI();
            repaint();

        } catch (java.lang.OutOfMemoryError oome) {
            if (extractingManager.isCanceled() || cleaning)
                return;
            outOfMemoryErrorManagement();
            return;
        } catch (Exception e) {
            if (extractingManager.isCanceled())
                return;
            else
                e.printStackTrace();
        }
    }

    @Override
    public void lightClean() {
        readData = null;
        writeData = null;
        loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        removeAll();
        add(loadingLabel);
    }

}
