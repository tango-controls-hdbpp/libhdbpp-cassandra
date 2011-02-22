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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.util.DataArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCViewSpectrumStackChartAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.Messages;

/**
 * @author awo
 */
public class ViewSpectrumStackPanel extends AbstractViewSpectrumPanel {

    private static final long serialVersionUID = -8895787942729175801L;

    private JButton viewButton;
    private JPanel curveChoicePanel;
    private GridBagConstraints gbc;
    private JCheckBox readCheckBox;
    private JCheckBox writeCheckBox;
    private final static ImageIcon viewIcon = new ImageIcon(Mambo.class
            .getResource("icons/View.gif"));
    private boolean cleaning = false;
    private ArrayList<String> timeList = new ArrayList<String>();

    public ViewSpectrumStackPanel(ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super(theSpectrum, viewConfigurationBean);
    }

    @Override
    protected void initComponents() {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();
        if (extractingManager.isCanceled() || cleaning)
            return;
        if ((splitedData == null) || ((splitedData[0] == null) && (splitedData[1] == null))) {
            if (extractingManager.isCanceled() || cleaning)
                return;
            curveChoicePanel = null;
        }
        else {
            if (extractingManager.isCanceled() || cleaning)
                return;
            buildCurveChoicePanel();
        }
    }

    private void buildCurveChoicePanel() {

        viewButton = new JButton();
        viewButton.setMargin(new Insets(0, 0, 0, 0));
        GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
        viewButton.setIcon(viewIcon);
        viewButton
                .addActionListener(new VCViewSpectrumStackChartAction(this, viewConfigurationBean));
        viewButton.setEnabled(true);
        viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON") + " " + getName());
        if (cleaning)
            return;
        curveChoicePanel = new JPanel();
        if (cleaning)
            return;
        curveChoicePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        if (cleaning)
            return;
        readCheckBox = new JCheckBox(Messages.getMessage("VIEW_SPECTRUM_STACK_READ"));
        GUIUtilities.setObjectBackground(readCheckBox, GUIUtilities.VIEW_COLOR);
        readCheckBox.addActionListener(new VCViewSpectrumStackChartAction(this,
                viewConfigurationBean));
        if (cleaning)
            return;
        writeCheckBox = new JCheckBox(Messages.getMessage("VIEW_SPECTRUM_STACK_WRITE"));
        writeCheckBox.addActionListener(new VCViewSpectrumStackChartAction(this,
                viewConfigurationBean));
        GUIUtilities.setObjectBackground(writeCheckBox, GUIUtilities.VIEW_COLOR);
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
        GUIUtilities.setObjectBackground(curveChoicePanel, GUIUtilities.VIEW_COLOR);

        // If we have both read and write values, we let the user choose
        // If only read or only write values exist, then we don't show radio
        // buttons, and the method returning the dao will display the non-null
        // dataview in chart
        readCheckBox.setSelected(true);
        writeCheckBox.setSelected(true);
        readCheckBox.setEnabled(true);
        writeCheckBox.setEnabled(true);
        if (cleaning)
            return;
        if (splitedData[0] == null) {
            readCheckBox.setSelected(false);
            readCheckBox.setEnabled(false);
            writeCheckBox.setEnabled(false);
        }
        if (cleaning)
            return;
        if (splitedData[1] == null) {
            writeCheckBox.setSelected(false);
            readCheckBox.setEnabled(false);
            writeCheckBox.setEnabled(false);
        }
        if (cleaning)
            return;
    }

    @Override
    protected void addComponents() {
        if (splitedData == null || ((splitedData[0] == null) && (splitedData[1] == null))) {
            if (cleaning)
                return;
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
            if (cleaning)
                return;
            GridBagConstraints curveChoicePanelConstraints = new GridBagConstraints();
            curveChoicePanelConstraints.fill = GridBagConstraints.BOTH;
            curveChoicePanelConstraints.gridx = 0;
            curveChoicePanelConstraints.gridy = 1;
            curveChoicePanelConstraints.weightx = 1;
            curveChoicePanelConstraints.weighty = 1;
            curveChoicePanelConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(curveChoicePanel, curveChoicePanelConstraints);
            if (cleaning)
                return;
            GridBagConstraints viewButtonConstraints = new GridBagConstraints();
            viewButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
            viewButtonConstraints.gridx = 0;
            viewButtonConstraints.gridy = 2;
            viewButtonConstraints.weightx = 0.5;
            viewButtonConstraints.weighty = 0;
            add(viewButton, viewButtonConstraints);

            if (saveButton == null)
                return;
            GridBagConstraints saveButtonConstraints = new GridBagConstraints();
            saveButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
            saveButtonConstraints.gridx = 1;
            saveButtonConstraints.gridy = 2;
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

    private DataArray extractDataArray(int timeIndex, DbData data) {
        DataArray dataArray = null;
        if ((data != null) && (data.getData_timed() != null)
                && timeIndex < data.getData_timed().length) {
            dataArray = new DataArray();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(data.getData_timed()[timeIndex].time.longValue());
            dataArray.setId(AbstractViewSpectrumPanel.dateFormat.format(calendar.getTime()));
            for (int index = 0; index < data.getMax_x(); index++) {
                Number y;
                if (data.getData_timed()[timeIndex].value != null
                        && index < data.getData_timed()[timeIndex].value.length
                        && data.getData_timed()[timeIndex].value[index] != null) {
                    y = (Number) data.getData_timed()[timeIndex].value[index];
                }
                else {
                    y = new Double(Double.NaN);
                }
                dataArray.add(new Integer(index), y);
                y = null;
            }
        }
        return dataArray;
    }

    public DbData[] getSplitedData() {
        return splitedData;
    }

    public int getCheckBoxSelected() {
        if (readCheckBox.isSelected() && writeCheckBox.isSelected()) {
            // READ and WRITE selected
            return 3;
        }
        else {
            if (readCheckBox.isSelected()) {
                // Only READ selected
                return 1;
            }
            if (writeCheckBox.isSelected()) {
                // Only Write selected
                return 2;
            }
        }
        // No data selected
        return 0;
    }

    public ArrayList<String> getTimeList() {

        timeList.clear();

        if (splitedData == null) {
            return null;
        }
        else if ((!readCheckBox.isSelected()) && (!writeCheckBox.isSelected())) {
            return null;
        }
        else {
            int readSize = 0;
            int writeSize = 0;
            if (splitedData[0] != null) {
                readSize = splitedData[0].getData_timed().length;
            }
            if (splitedData[1] != null) {
                writeSize = splitedData[1].getData_timed().length;
            }
            if (readCheckBox.isSelected()) {

                if (writeCheckBox.isSelected()) {
                    // both checkboxes are selected
                    int size = Math.max(readSize, writeSize);
                    for (int i = 0; i < size; i++) {
                        DataArray readDataArray = extractDataArray(i, splitedData[0]);
                        boolean filled = false;
                        if (readDataArray != null) {
                            timeList.add(readDataArray.getId());
                        }
                        readDataArray = null;
                        DataArray writeDataArray = extractDataArray(i, splitedData[1]);
                        if (writeDataArray != null) {
                            if (!filled) {
                                timeList.add(writeDataArray.getId());
                            }
                        }
                    }
                }
                else {
                    // only readCheckBox is selected
                    for (int i = 0; i < readSize; i++) {
                        DataArray readDataArray = extractDataArray(i, splitedData[0]);
                        if (readDataArray != null) {
                            if (cleaning)
                                return null;
                            timeList.add(readDataArray.getId());
                        }
                        readDataArray = null;
                    }
                }
            }
            else {
                // only writeCheckBox is selected
                for (int i = 0; i < writeSize; i++) {
                    DataArray writeDataArray = extractDataArray(i, splitedData[1]);
                    if (writeDataArray != null) {

                        timeList.add(writeDataArray.getId());
                    }
                }
            }
        }
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

    public boolean getWriteCheckBox() {
        return writeCheckBox.isSelected();
    }

    public boolean getReadCheckBox() {
        return readCheckBox.isSelected();
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
    public DbData getReadData() {
        if ((readCheckBox != null) && (!readCheckBox.isSelected())) {
            return null;
        }
        return super.getReadData();
    }

    @Override
    public DbData getWriteData() {
        if ((writeCheckBox != null) && (!writeCheckBox.isSelected())) {
            return null;
        }
        return super.getWriteData();
    }

    @Override
    public int getViewSpectrumType() {
        return ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK;
    }
}
