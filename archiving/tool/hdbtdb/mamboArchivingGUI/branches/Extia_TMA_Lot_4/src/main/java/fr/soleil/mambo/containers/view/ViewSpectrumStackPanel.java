package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.comete.dao.util.DefaultStackDataArrayDAO;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCViewSpectrumStackChartAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewSpectrumTableModel;
import fr.soleil.mambo.tools.ATKToCometUtilities;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * @author awo
 */
public class ViewSpectrumStackPanel extends MamboCleanablePanel {

    private static final long          serialVersionUID = -8895787942729175801L;

    private ViewConfigurationAttribute spectrum;
    private JButton                    viewButton;
    private JLDataView[]               readData;
    private JLDataView[]               writeData;
    private JLabel                     loadingLabel;
    private JPanel                     curveChoicePanel;
    private GridBagConstraints         gbc;
    private JCheckBox                  readCheckBox;
    private JCheckBox                  writeCheckBox;
    private final static ImageIcon     viewIcon         = new ImageIcon(
                                                                Mambo.class
                                                                        .getResource("icons/View.gif"));
    int                                writable;
    private boolean                    cleaning         = false;
    private ViewConfigurationBean      viewConfigurationBean;

    private ArrayList<String>          timeList         = new ArrayList<String>();

    public ViewSpectrumStackPanel(ViewConfigurationAttribute theSpectrum,
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
        viewButton.addActionListener(new VCViewSpectrumStackChartAction(this,
                viewConfigurationBean));
        if (readData.length == 0 && writeData.length == 0) {
            writable = ViewSpectrumTableModel.UNKNOWN;
            viewButton.setText(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA")
                    + " " + getName());
            viewButton.setEnabled(false);
        } else {
            viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON")
                    + " " + getName());
            viewButton.setEnabled(true);
            if (readData.length > 0) {
                if (writeData.length > 0) {
                    writable = ViewSpectrumTableModel.RW;
                } else {
                    writable = ViewSpectrumTableModel.R;
                }
            } else {
                writable = ViewSpectrumTableModel.W;
            }
        }
        viewButton.setIcon(viewIcon);
        GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
        // If we have both read and write values, we let the user choose
        // If only read or only write values exist, then we don't show radio
        // buttons, and the method returning the dao will display the non-null
        // dataview in chart
        buildCurveChoicePanel();
    }

    private void buildCurveChoicePanel() {
        curveChoicePanel = new JPanel();
        curveChoicePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        readCheckBox = new JCheckBox(Messages
                .getMessage("VIEW_SPECTRUM_STACK_READ"));
        GUIUtilities.setObjectBackground(readCheckBox, GUIUtilities.VIEW_COLOR);
        writeCheckBox = new JCheckBox(Messages
                .getMessage("VIEW_SPECTRUM_STACK_WRITE"));
        GUIUtilities
                .setObjectBackground(writeCheckBox, GUIUtilities.VIEW_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        curveChoicePanel.add(readCheckBox, gbc);
        gbc.gridx = 1;
        curveChoicePanel.add(writeCheckBox, gbc);
        GUIUtilities.setObjectBackground(curveChoicePanel,
                GUIUtilities.VIEW_COLOR);
        if (!isNullOrEmpty(readData)) {
            if (!isNullOrEmpty(writeData)) {
                // read and write not null
                readCheckBox.setSelected(true);
                writeCheckBox.setSelected(true);
                readCheckBox.setEnabled(true);
                writeCheckBox.setEnabled(true);
            } else {
                // read only
                readCheckBox.setSelected(true);
                writeCheckBox.setSelected(false);
                readCheckBox.setEnabled(false);
                writeCheckBox.setEnabled(false);
            }
        } else {
            if (!isNullOrEmpty(writeData)) {
                // write only
                readCheckBox.setSelected(false);
                writeCheckBox.setSelected(true);
                readCheckBox.setEnabled(false);
                writeCheckBox.setEnabled(false);
            } else {
                // read and write null, not very interesting...
                readCheckBox.setSelected(true);
                writeCheckBox.setSelected(false);
                readCheckBox.setEnabled(true);
                writeCheckBox.setEnabled(true);
            }
        }
    }

    private boolean isNullOrEmpty(JLDataView[] dv) {
        if (dv == null) {
            return true;
        } else
            return dv.length == 0;
    }

    private void addComponents() {
        if (viewButton == null)
            return;
        this.add(curveChoicePanel);
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
            // calling the appropriate method, which is with time mode
            table = extractingManager.retrieveDataHash(param,
                    selectedViewConfigurationData.isHistoric(),
                    selectedViewConfigurationData.getSamplingType(), true);
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

    public DefaultStackDataArrayDAO getFilteredSpectrumStackDAO() {
        timeList.clear();
        // "reference" is a workaround to get dates as long as Mambo is ATK
        // dependant, and extracting manager returns JLDataViews.
        JLDataView[] reference = new JLDataView[0];
        DefaultStackDataArrayDAO dao = null;
        if (readCheckBox.isSelected()) {
            if (writeCheckBox.isSelected()) {
                dao = ATKToCometUtilities.createSpectrumStackDAO(readData,
                        writeData);
                if (readData == null) {
                    if (writeData != null) {
                        reference = writeData;
                    }
                } else {
                    if ((writeData == null)
                            || (readData.length >= writeData.length)) {
                        reference = readData;
                    } else {
                        reference = writeData;
                    }
                }
            } else {
                reference = readData;
                dao = ATKToCometUtilities.createSpectrumStackDAO(reference,
                        null);
            }
        } else if (writeCheckBox.isSelected()) {
            reference = writeData;
            dao = ATKToCometUtilities.createSpectrumStackDAO(null, reference);
        }
        // Workaround to get date list
        for (int i = 0; i < reference.length; i++) {
            timeList.add(reference[i].getName().replace(
                    Messages.getMessage("VIEW_SPECTRUM_READ"), "").replace(
                    Messages.getMessage("VIEW_SPECTRUM_WRITE"), "").trim());
        }
        return dao;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public String getFullName() {
        if (spectrum == null) {
            return "";
        } else {
            return spectrum.getCompleteName();
        }
    }

    public void clean() {
        cleaning = true;
        removeAll();
        viewButton = null;
        curveChoicePanel = null;
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
