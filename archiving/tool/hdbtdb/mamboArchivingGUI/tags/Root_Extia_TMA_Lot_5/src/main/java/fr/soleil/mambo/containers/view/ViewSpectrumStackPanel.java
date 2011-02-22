package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.comete.dao.util.DefaultStackDataArrayDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.util.StackDataArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCViewSpectrumStackChartAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * @author awo
 */
public class ViewSpectrumStackPanel extends AbstractViewSpectrumPanel {

    private static final long      serialVersionUID = -8895787942729175801L;

    private JButton                viewButton;
    private JPanel                 curveChoicePanel;
    private GridBagConstraints     gbc;
    private JCheckBox              readCheckBox;
    private JCheckBox              writeCheckBox;
    private final static ImageIcon viewIcon         = new ImageIcon(
                                                            Mambo.class
                                                                    .getResource("icons/View.gif"));
    private boolean                cleaning         = false;
    private ArrayList<String>      timeList         = new ArrayList<String>();

    public ViewSpectrumStackPanel(ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super(theSpectrum, viewConfigurationBean);
    }

    @Override
    protected void initComponents() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning)
            return;
        viewButton = new JButton();
        if (extractingManager.isCanceled() || cleaning)
            return;
        viewButton.setMargin(new Insets(0, 0, 0, 0));
        if (extractingManager.isCanceled() || cleaning)
            return;
        viewButton.addActionListener(new VCViewSpectrumStackChartAction(this,
                viewConfigurationBean));
        if (extractingManager.isCanceled() || cleaning)
            return;
        if (splitedData == null) {
            if (extractingManager.isCanceled() || cleaning)
                return;
            viewButton.setText(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA")
                    + " " + getName());
            if (extractingManager.isCanceled() || cleaning)
                return;
            viewButton.setEnabled(false);
        } else {
            if (extractingManager.isCanceled() || cleaning)
                return;
            viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON")
                    + " " + getName());
            if (extractingManager.isCanceled() || cleaning)
                return;
            viewButton.setEnabled(true);
        }
        if (extractingManager.isCanceled() || cleaning)
            return;
        viewButton.setIcon(viewIcon);
        GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
        if (extractingManager.isCanceled() || cleaning)
            return;
        // If we have both read and write values, we let the user choose
        // If only read or only write values exist, then we don't show radio
        // buttons, and the method returning the dao will display the non-null
        // dataview in chart
        buildCurveChoicePanel();
    }

    private void buildCurveChoicePanel() {
        if (cleaning)
            return;
        curveChoicePanel = new JPanel();
        if (cleaning)
            return;
        curveChoicePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        if (cleaning)
            return;
        readCheckBox = new JCheckBox(Messages
                .getMessage("VIEW_SPECTRUM_STACK_READ"));
        GUIUtilities.setObjectBackground(readCheckBox, GUIUtilities.VIEW_COLOR);
        if (cleaning)
            return;
        writeCheckBox = new JCheckBox(Messages
                .getMessage("VIEW_SPECTRUM_STACK_WRITE"));
        GUIUtilities
                .setObjectBackground(writeCheckBox, GUIUtilities.VIEW_COLOR);
        if (cleaning)
            return;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        if (cleaning)
            return;
        curveChoicePanel.add(readCheckBox, gbc);
        gbc.gridx = 1;
        if (cleaning)
            return;
        curveChoicePanel.add(writeCheckBox, gbc);
        GUIUtilities.setObjectBackground(curveChoicePanel,
                GUIUtilities.VIEW_COLOR);
        if (splitedData == null) {
            // read and write null, not very interesting...
            if (cleaning)
                return;
            readCheckBox.setSelected(false);
            if (cleaning)
                return;
            writeCheckBox.setSelected(false);
            if (cleaning)
                return;
            readCheckBox.setEnabled(false);
            if (cleaning)
                return;
            writeCheckBox.setEnabled(false);
        } else {
            if (splitedData[0] == null) {
                // write only
                if (cleaning)
                    return;
                readCheckBox.setSelected(false);
                if (cleaning)
                    return;
                writeCheckBox.setSelected(true);
                if (cleaning)
                    return;
                readCheckBox.setEnabled(false);
                if (cleaning)
                    return;
                writeCheckBox.setEnabled(false);
            } else {
                if (splitedData[1] == null) {
                    // read only
                    if (cleaning)
                        return;
                    readCheckBox.setSelected(true);
                    if (cleaning)
                        return;
                    writeCheckBox.setSelected(false);
                    if (cleaning)
                        return;
                    readCheckBox.setEnabled(false);
                    if (cleaning)
                        return;
                    writeCheckBox.setEnabled(false);
                } else {
                    // read and write
                    if (cleaning)
                        return;
                    readCheckBox.setSelected(true);
                    if (cleaning)
                        return;
                    writeCheckBox.setSelected(true);
                    if (cleaning)
                        return;
                    readCheckBox.setEnabled(true);
                    if (cleaning)
                        return;
                    writeCheckBox.setEnabled(true);
                }
            }
        }
    }

    @Override
    protected void addComponents() {
        if (viewButton == null)
            return;
        if (cleaning)
            return;
        GridBagConstraints curveChoicePanelConstraints = new GridBagConstraints();
        curveChoicePanelConstraints.fill = GridBagConstraints.BOTH;
        curveChoicePanelConstraints.gridx = 0;
        curveChoicePanelConstraints.gridy = 1;
        curveChoicePanelConstraints.weightx = 1;
        curveChoicePanelConstraints.weighty = 1;
        add(curveChoicePanel, curveChoicePanelConstraints);
        if (cleaning)
            return;
        GridBagConstraints viewButtonConstraints = new GridBagConstraints();
        viewButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        viewButtonConstraints.gridx = 0;
        viewButtonConstraints.gridy = 2;
        viewButtonConstraints.weightx = 1;
        viewButtonConstraints.weighty = 0;
        add(viewButton, viewButtonConstraints);
    }

    @Override
    protected void initBorder() {
        String msg = getFullName();
        TitledBorder tb = new TitledBorder(new EtchedBorder(
                EtchedBorder.LOWERED), msg, TitledBorder.CENTER,
                TitledBorder.TOP);
        this.setBorder(tb);
    }

    private DataArray extractDataArray(int timeIndex, DbData data) {
        DataArray dataArray = null;
        if ((data != null) && (data.getData_timed() != null)
                && timeIndex < data.getData_timed().length) {
            dataArray = new DataArray();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(data.getData_timed()[timeIndex].time
                    .longValue());
            dataArray.setName(AbstractViewSpectrumPanel.dateFormat
                    .format(calendar.getTime()));
            for (int index = 0; index < data.getMax_x(); index++) {
                Number y;
                if (data.getData_timed()[timeIndex].value != null
                        && index < data.getData_timed()[timeIndex].value.length
                        && data.getData_timed()[timeIndex].value[index] != null) {
                    y = (Number) data.getData_timed()[timeIndex].value[index];
                } else {
                    y = new Double(Double.NaN);
                }
                dataArray.add(new Integer(index), y);
                y = null;
            }
        }
        return dataArray;
    }

    public DefaultStackDataArrayDAO getFilteredSpectrumStackDAO() {
        if (cleaning)
            return null;
        timeList.clear();

        if (splitedData == null) {
            return null;
        } else if ((!readCheckBox.isSelected())
                && (!writeCheckBox.isSelected())) {
            return null;
        } else {
            int readSize = 0;
            int writeSize = 0;
            if (splitedData[0] != null) {
                readSize = splitedData[0].getData_timed().length;
            }
            if (splitedData[1] != null) {
                writeSize = splitedData[1].getData_timed().length;
            }
            StackDataArray data = new StackDataArray();
            if (cleaning)
                return null;
            if (readCheckBox.isSelected()) {
                if (cleaning)
                    return null;
                if (writeCheckBox.isSelected()) {
                    // both checkboxes are selected
                    int size = Math.max(readSize, writeSize);
                    for (int i = 0; i < size; i++) {
                        if (cleaning) {
                            data = null;
                            return null;
                        }
                        ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();
                        DataArray readDataArray = extractDataArray(i,
                                splitedData[0]);
                        boolean filled = false;
                        if (readDataArray != null) {
                            if (cleaning)
                                return null;
                            timeList.add(readDataArray.getName());
                            filled = true;
                            readDataArray
                                    .setName(readDataArray.getName()
                                            + " "
                                            + Messages
                                                    .getMessage("VIEW_SPECTRUM_READ"));
                            dataArrayList.add(readDataArray);
                        }
                        readDataArray = null;
                        if (cleaning) {
                            data = null;
                            dataArrayList = null;
                            return null;
                        }
                        DataArray writeDataArray = extractDataArray(i,
                                splitedData[1]);
                        if (writeDataArray != null) {
                            if (!filled) {
                                timeList.add(writeDataArray.getName());
                            }
                            writeDataArray.setName(writeDataArray.getName()
                                    + " "
                                    + Messages
                                            .getMessage("VIEW_SPECTRUM_WRITE"));
                            dataArrayList.add(writeDataArray);
                        }
                        writeDataArray = null;
                        data.add(dataArrayList);
                        dataArrayList = null;
                    }
                } else {
                    // only readCheckBox is selected
                    for (int i = 0; i < readSize; i++) {
                        if (cleaning) {
                            data = null;
                            return null;
                        }
                        ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();
                        DataArray readDataArray = extractDataArray(i,
                                splitedData[0]);
                        if (readDataArray != null) {
                            if (cleaning)
                                return null;
                            timeList.add(readDataArray.getName());
                            readDataArray
                                    .setName(readDataArray.getName()
                                            + " "
                                            + Messages
                                                    .getMessage("VIEW_SPECTRUM_READ"));
                            dataArrayList.add(readDataArray);
                        }
                        readDataArray = null;
                        data.add(dataArrayList);
                        dataArrayList = null;
                    }
                }
            } else {
                // only writeCheckBox is selected
                for (int i = 0; i < writeSize; i++) {
                    if (cleaning) {
                        data = null;
                        return null;
                    }
                    ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();
                    DataArray writeDataArray = extractDataArray(i,
                            splitedData[1]);
                    if (writeDataArray != null) {
                        if (cleaning)
                            return null;
                        timeList.add(writeDataArray.getName());
                        writeDataArray.setName(writeDataArray.getName() + " "
                                + Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                        dataArrayList.add(writeDataArray);
                    }
                    writeDataArray = null;
                    data.add(dataArrayList);
                    dataArrayList = null;
                }
            }
            DefaultStackDataArrayDAO dao = new DefaultStackDataArrayDAO();
            dao.setData(data);
            return dao;
        }
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void clean() {
        cleaning = true;
        removeAll();
        viewButton = null;
        curveChoicePanel = null;
        spectrum = null;
        splitedData = null;
        loadingLabel = null;
        viewConfigurationBean = null;
        if (timeList != null) {
            timeList.clear();
        }
        timeList = null;
    }

    @Override
    public void lightClean() {
        splitedData = null;
        timeList.clear();
        loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        removeAll();
        addLoadingLabel();
    }

    @Override
    public boolean isCleaning() {
        return cleaning;
    }

}
