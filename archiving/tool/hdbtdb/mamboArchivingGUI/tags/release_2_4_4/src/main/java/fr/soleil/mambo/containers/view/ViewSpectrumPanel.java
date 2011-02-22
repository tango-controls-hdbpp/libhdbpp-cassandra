/*
 * Synchrotron Soleil File : ViewSpectrumPanel.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 24 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumPanel.java,v
 */
package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.dao.util.DefaultDataArrayDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCViewSpectrumChartAction;
import fr.soleil.mambo.actions.view.VCViewSpectrumFilterAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.editors.AttributesSelectTableEditor;
import fr.soleil.mambo.components.renderers.ViewSpectrumTableRenderer;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewSpectrumTableModel;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * @author SOLEIL
 */
public class ViewSpectrumPanel extends AbstractViewSpectrumPanel {

    private static final long serialVersionUID = -8895787942729175801L;
    private JButton viewButton;
    private JButton allRead;
    private JButton allWrite;
    private JButton noneRead;
    private JButton noneWrite;
    private ViewSpectrumTableModel model;
    private JTable choiceTable;
    private JScrollPane choiceTableScrollPane;
    private JPanel buttonPanel;
    private final static ImageIcon viewIcon = new ImageIcon(Mambo.class
            .getResource("icons/View.gif"));
    private int writable;
    private boolean cleaning = false;
    private final static Dimension refSize = new Dimension(50, 50);

    public ViewSpectrumPanel(ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super(theSpectrum, viewConfigurationBean);
    }

    @Override
    protected void initComponents() {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning)
            return;

        if (splitedData == null || ((splitedData[0] == null) && (splitedData[1] == null))) {
            writable = ViewSpectrumTableModel.UNKNOWN;
            model = new ViewSpectrumTableModel(null, null);
            choiceTableScrollPane = null;
        }
        else {
            viewButton = new JButton();
            viewButton.setMargin(new Insets(0, 0, 0, 0));
            viewButton.setIcon(viewIcon);
            GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
            viewButton.addActionListener(new VCViewSpectrumChartAction(this));
            viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON") + " " + getName());
            viewButton.setEnabled(true);

            int viewType = getViewSpectrumType();

            String[] readNames = null;
            String[] writeNames = null;

            if (splitedData[0] != null) {
                allRead = new JButton(new VCViewSpectrumFilterAction(Messages
                        .getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL")
                        + " " + Messages.getMessage("VIEW_SPECTRUM_READ"), this, true, true));
                allRead.setMargin(new Insets(0, 0, 0, 0));
                allRead.setEnabled(true);

                noneRead = new JButton(new VCViewSpectrumFilterAction(Messages
                        .getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE")
                        + " " + Messages.getMessage("VIEW_SPECTRUM_READ"), this, false, true));
                noneRead.setMargin(new Insets(0, 0, 0, 0));
                noneRead.setEnabled(true);

                readNames = generateNames(splitedData[0], viewType, Messages
                        .getMessage("VIEW_SPECTRUM_READ"));

                writable = ViewSpectrumTableModel.R;
            }
            if (splitedData[1] != null) {
                allWrite = new JButton(new VCViewSpectrumFilterAction(Messages
                        .getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL")
                        + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"), this, true, false));
                allWrite.setMargin(new Insets(0, 0, 0, 0));
                allWrite.setEnabled(true);

                noneWrite = new JButton(new VCViewSpectrumFilterAction(Messages
                        .getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE")
                        + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"), this, false, false));
                noneWrite.setMargin(new Insets(0, 0, 0, 0));
                noneWrite.setEnabled(true);

                writeNames = generateNames(splitedData[1], viewType, Messages
                        .getMessage("VIEW_SPECTRUM_WRITE"));

                writable = ViewSpectrumTableModel.W;

            }
            if ((splitedData[0] != null) && (splitedData[1] != null)) {
                writable = ViewSpectrumTableModel.RW;
            }
            model = new ViewSpectrumTableModel(readNames, writeNames);
            model.setViewSpectrumType(viewType);

            buttonPanel = new JPanel();
            GUIUtilities.setObjectBackground(buttonPanel, GUIUtilities.VIEW_COLOR);
            buttonPanel.setLayout(new SpringLayout());

            switch (writable) {
                case ViewSpectrumTableModel.R:
                    buttonPanel.add(allRead);
                    buttonPanel.add(noneRead);
                    SpringUtilities.makeCompactGrid(buttonPanel, 2, 1, 5, 5, 5, 5, true);
                    break;
                case ViewSpectrumTableModel.W:
                    buttonPanel.add(allWrite);
                    buttonPanel.add(noneWrite);
                    SpringUtilities.makeCompactGrid(buttonPanel, 2, 1, 5, 5, 5, 5, true);
                    break;
                case ViewSpectrumTableModel.RW:
                    buttonPanel.add(allRead);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(allWrite);
                    buttonPanel.add(noneRead);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(noneWrite);
                    SpringUtilities.makeCompactGrid(buttonPanel, 2, 3, 5, 5, 5, 5, true);
                    break;
            }
            choiceTable = new JTable(model);
            choiceTable.setMinimumSize(new Dimension(110, 200));
            choiceTable.setDefaultRenderer(Object.class, new ViewSpectrumTableRenderer());
            choiceTable.setDefaultEditor(Object.class, new AttributesSelectTableEditor());
            GUIUtilities.setObjectBackground(choiceTable, GUIUtilities.VIEW_COLOR);
            choiceTableScrollPane = new JScrollPane(choiceTable);
            choiceTableScrollPane.setPreferredSize(refSize);
            choiceTableScrollPane.setMinimumSize(refSize);
            GUIUtilities.setObjectBackground(choiceTableScrollPane, GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(choiceTableScrollPane.getViewport(),
                    GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(choiceTableScrollPane.getHorizontalScrollBar(),
                    GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(choiceTableScrollPane.getVerticalScrollBar(),
                    GUIUtilities.VIEW_COLOR);
        }
    }

    private String[] generateNames(DbData data, int viewType, String toEnd) {
        if (data == null) {
            return null;
        }
        else {
            String[] names;
            switch (viewType) {
                case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                    names = new String[data.getMax_x()];
                    for (int i = 0; i < names.length; i++) {
                        names[i] = Integer.toString(i + 1) + " " + toEnd;
                    }
                    break;
                case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                    names = new String[data.getData_timed() == null ? 0
                            : data.getData_timed().length];
                    for (int i = 0; i < names.length; i++) {
                        Long time = data.getData_timed()[i].time;
                        if (time == null) {
                            names[i] = "";
                        }
                        else {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(data.getData_timed()[i].time.longValue());
                            names[i] = dateFormat.format(calendar.getTime()) + " " + toEnd;
                            calendar = null;
                        }
                    }
                    break;
                default:
                    names = null;
                    break;
            }
            return names;
        }
    }

    @Override
    protected void addComponents() {
        if (splitedData == null || ((splitedData[0] == null) && (splitedData[1] == null))) {
            JLabel nameLabel = new JLabel(spectrum.getCompleteName() + ":");
            GUIUtilities.setObjectBackground(nameLabel, GUIUtilities.VIEW_COLOR);

            GridBagConstraints readLabelConstraints = new GridBagConstraints();
            readLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
            readLabelConstraints.gridx = 0;
            readLabelConstraints.gridy = 1;
            readLabelConstraints.weightx = 1;
            readLabelConstraints.weighty = 0;
            readLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(nameLabel, readLabelConstraints);

            JLabel noDataLabel = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA"));
            noDataLabel.setVerticalAlignment(SwingConstants.NORTH);
            GUIUtilities.setObjectBackground(noDataLabel, GUIUtilities.VIEW_COLOR);
            noDataLabel.setForeground(Color.RED);
            GridBagConstraints noDataLabelConstraints = new GridBagConstraints();
            noDataLabelConstraints.fill = GridBagConstraints.BOTH;
            noDataLabelConstraints.gridx = 0;
            noDataLabelConstraints.gridy = 2;
            noDataLabelConstraints.weightx = 1;
            noDataLabelConstraints.weighty = 1;
            noDataLabelConstraints.anchor = GridBagConstraints.NORTH;
            noDataLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(noDataLabel, noDataLabelConstraints);
        }
        else {
            if (buttonPanel == null)
                return;
            GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
            buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
            buttonPanelConstraints.gridx = 0;
            buttonPanelConstraints.gridy = 1;
            buttonPanelConstraints.weightx = 1;
            buttonPanelConstraints.weighty = 0;
            buttonPanelConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(buttonPanel, buttonPanelConstraints);

            if (choiceTableScrollPane == null)
                return;
            GridBagConstraints choiceTableConstraints = new GridBagConstraints();
            choiceTableConstraints.fill = GridBagConstraints.BOTH;
            choiceTableConstraints.gridx = 0;
            choiceTableConstraints.gridy = 2;
            choiceTableConstraints.weightx = 1;
            choiceTableConstraints.weighty = 1;
            choiceTableConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(choiceTableScrollPane, choiceTableConstraints);

            if (viewButton == null)
                return;
            GridBagConstraints viewButtonConstraints = new GridBagConstraints();
            viewButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
            viewButtonConstraints.gridx = 0;
            viewButtonConstraints.gridy = 3;
            viewButtonConstraints.weightx = 0.5;
            viewButtonConstraints.weighty = 0;
            add(viewButton, viewButtonConstraints);

            if (saveButton == null)
                return;
            GridBagConstraints saveButtonConstraints = new GridBagConstraints();
            saveButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
            saveButtonConstraints.gridx = 1;
            saveButtonConstraints.gridy = 3;
            saveButtonConstraints.weightx = 0.5;
            saveButtonConstraints.weighty = 0;
            saveButtonConstraints.insets = new Insets(0, 20, 0, 0);
            add(saveButton, saveButtonConstraints);
            viewButton.setPreferredSize(saveButton.getPreferredSize());
        }
    }

    @Override
    protected void initBorder() {
        String msg = getFullName();
        TitledBorder tb = new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP);
        this.setBorder(tb);
    }

    public DbData[] getSplitedData() {
        return splitedData;
    }

    public ViewSpectrumTableModel getModel() {
        return model;
    }

    public DefaultDataArrayDAO getFilteredSpectrumDAO() {
        if (splitedData == null) {
            return null;
        }
        else {
            int dataType = TangoConst.Tango_DEV_VOID;
            int readBooleanColumn = -1;
            int readNameColumn = -1;
            int writeBooleanColumn = -1;
            int writeNameColumn = -1;
            if (splitedData[0] == null) {
                dataType = splitedData[1].getData_type();
                writeBooleanColumn = 0;
                writeNameColumn = 1;
            }
            else {
                dataType = splitedData[0].getData_type();
                readBooleanColumn = 0;
                readNameColumn = 1;
                writeBooleanColumn = 3;
                writeNameColumn = 2;
            }
            switch (dataType) {
                case TangoConst.Tango_DEV_SHORT:
                case TangoConst.Tango_DEV_USHORT:
                case TangoConst.Tango_DEV_LONG:
                case TangoConst.Tango_DEV_ULONG:
                case TangoConst.Tango_DEV_FLOAT:
                case TangoConst.Tango_DEV_DOUBLE:
                    List<DataArray> dataArrayList = new ArrayList<DataArray>();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        IExtractingManager extractingManager = ExtractingManagerFactory
                                .getCurrentImpl();
                        if (extractingManager != null) {
                            if (extractingManager.isShowRead()) {
                                DataArray readDataArray = extractDataArray(row, readNameColumn,
                                        readBooleanColumn, getViewSpectrumType(), splitedData[0]);
                                if (readDataArray != null) {
                                    dataArrayList.add(readDataArray);
                                }
                                readDataArray = null;
                            }

                            if (extractingManager.isShowWrite()) {
                                DataArray writeDataArray = extractDataArray(row, writeNameColumn,
                                        writeBooleanColumn, getViewSpectrumType(), splitedData[1]);
                                if (writeDataArray != null) {
                                    dataArrayList.add(writeDataArray);
                                }
                                writeDataArray = null;
                            }
                        }
                    }
                    DefaultDataArrayDAO dao = new DefaultDataArrayDAO();
                    dao.setData(dataArrayList);
                    dataArrayList = null;
                    return dao;
                default:
                    return null;
            }
        }
    }

    private DataArray extractDataArray(int row, int nameColumn, int booleanColumn, int viewType,
            DbData data) {
        if ((data != null) && (data.getData_timed() != null) && (row > -1)
                && (row < model.getRowCount()) && (nameColumn > -1)
                && (nameColumn < model.getColumnCount()) && (booleanColumn > -1)
                && (booleanColumn < model.getColumnCount())) {
            DataArray dataArray = null;
            Boolean selected = null;
            if (model.getValueAt(row, booleanColumn) instanceof Boolean) {
                selected = (Boolean) model.getValueAt(row, booleanColumn);
            }
            if (selected == null) {
                selected = new Boolean(false);
            }
            if (selected.booleanValue()) {
                switch (viewType) {
                    case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                        // Index spectrum : each DataArray is the history of an index in the
                        // spectrum
                        dataArray = new DataArray();
                        dataArray.setId("" + model.getValueAt(row, nameColumn));
                        for (int timeIndex = 0; timeIndex < data.getData_timed().length; timeIndex++) {
                            if ((data.getData_timed()[timeIndex].value != null)
                                    && row < data.getData_timed()[timeIndex].value.length) {
                                dataArray.add(data.getData_timed()[timeIndex].time, (Number) data
                                        .getData_timed()[timeIndex].value[row]);
                            }
                        }
                        break;
                    case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                    case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                        // Time spectrum : each DataArray is a full spectrum at a particular time
                        dataArray = new DataArray();
                        dataArray.setId("" + model.getValueAt(row, nameColumn));
                        if (row < data.getData_timed().length) {
                            for (int index = 0; index < data.getMax_x(); index++) {
                                Number y;
                                if (data.getData_timed()[row].value != null
                                        && index < data.getData_timed()[row].value.length
                                        && data.getData_timed()[row].value[index] != null) {
                                    y = (Number) data.getData_timed()[row].value[index];
                                }
                                else {
                                    y = new Double(Double.NaN);
                                }
                                dataArray.add(new Integer(index), y);
                                y = null;
                            }
                        }
                        break;
                    default:
                        // Invalid view type
                        dataArray = null;
                        break;
                }
            }
            else {
                // Value is not selected
                dataArray = null;
            }
            return dataArray;
        }
        else {
            // Invalid parameters
            return null;
        }
    }

    @Override
    public int getViewSpectrumType() {
        return spectrum.getProperties().getPlotProperties().getSpectrumViewType();
    }

    public void setAllReadSelected(boolean selected) {
        disposeTable();
        if (choiceTable == null || choiceTable.getRowCount() == 0
                || writable == ViewSpectrumTableModel.UNKNOWN
                || writable == ViewSpectrumTableModel.W) {
            return;
        }
        for (int i = 0; i < ((ViewSpectrumTableModel) choiceTable.getModel()).getReadLength(); i++) {
            choiceTable.getModel().setValueAt(new Boolean(selected), i, 0);
        }
    }

    public void setAllWriteSelected(boolean selected) {
        disposeTable();
        if (choiceTable == null || choiceTable.getRowCount() == 0
                || writable == ViewSpectrumTableModel.UNKNOWN
                || writable == ViewSpectrumTableModel.R) {
            return;
        }
        if (writable == ViewSpectrumTableModel.W) {
            for (int i = 0; i < choiceTable.getRowCount(); i++) {
                choiceTable.getModel().setValueAt(new Boolean(selected), i, 0);
            }
        }
        else { // RW
            for (int i = 0; i < ((ViewSpectrumTableModel) choiceTable.getModel()).getWriteLength(); i++) {
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
                choiceTable.getModel().setValueAt(new Boolean(((JCheckBox) comp).isSelected()),
                        row, col);
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
        splitedData = null;
        loadingLabel = null;
        viewConfigurationBean = null;
    }

    @Override
    public void lightClean() {
        splitedData = null;
        loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        removeAll();
        addLoadingLabel();
    }

    @Override
    public boolean isCleaning() {
        return cleaning;
    }

    public void enableViewButton(boolean enable, boolean updateText) {
        if (viewButton != null) {
            viewButton.setEnabled(enable);
            if (updateText) {
                if (enable) {
                    viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON") + " "
                            + getName());
                }
                else {
                    viewButton.setText(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA") + " "
                            + getName());
                }
            }
        }
    }

    @Override
    protected List<Integer> getReadFilter() {
        disposeTable();
        if (model == null) {
            return null;
        }
        else {
            return model.getSelectedRead();
        }
    }

    @Override
    protected List<Integer> getWriteFilter() {
        disposeTable();
        if (model == null) {
            return null;
        }
        else {
            return model.getSelectedWrite();
        }
    }

}
