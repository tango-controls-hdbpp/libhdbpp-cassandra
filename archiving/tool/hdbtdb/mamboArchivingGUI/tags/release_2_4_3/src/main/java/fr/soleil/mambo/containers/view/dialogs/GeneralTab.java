// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/GeneralTab.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GeneralTab.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.14 $
//
// $Log: GeneralTab.java,v $
// Revision 1.14 2008/04/09 10:45:46 achouri
// put date in differente class.
//
// Revision 1.13 2007/12/12 17:49:51 pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.12 2007/10/30 17:52:41 soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment : Define the method initializeHistoricCheck, and call it in the
// method initComponents to put the state of the JcheckBox historicCheck.
//
// Revision 1.11 2007/01/22 15:53:27 ounsy
// the sampling factor default display is now "1"
//
// Revision 1.10 2007/01/10 10:42:38 ounsy
// corrected the display bug that occurred with complicated VC names
//
// Revision 1.9 2006/12/07 16:45:39 ounsy
// removed keeping period
//
// Revision 1.8 2006/12/06 15:12:22 ounsy
// added parametrisable sampling
//
// Revision 1.7 2006/11/06 09:28:05 ounsy
// icons reorganization
//
// Revision 1.6 2006/10/17 14:32:13 ounsy
// "sampling" -> "averaging"
//
// Revision 1.5 2006/09/05 14:11:24 ounsy
// updated for sampling compatibility
//
// Revision 1.4 2006/07/28 10:07:12 ounsy
// icons moved to "icons" package
//
// Revision 1.3 2005/12/15 11:31:49 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.listeners.HistoricCheckboxListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.SamplingTypeComboBox;
import fr.soleil.mambo.tools.Messages;

public class GeneralTab extends JPanel {

    private static final long serialVersionUID = 7389271456444643000L;
    private final static ImageIcon checkedYesIcon = new ImageIcon(Mambo.class
            .getResource("icons/checked_yes.gif"));
    private final static ImageIcon checkedNoIcon = new ImageIcon(Mambo.class
            .getResource("icons/checked_no.gif"));
    private final static ImageIcon checkedPressedIcon = new ImageIcon(Mambo.class
            .getResource("icons/checked_pressed.gif"));

    private JLabel creationDateLabel;
    private JLabel creationDateValue;
    private JLabel lastUpdateDateLabel;
    private JLabel lastUpdateDateValue;

    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel historicLabel;
    private JCheckBox historicCheck;

    private JPanel dataSubPanel;

    private JPanel vcIdentificationPanel;
    private JPanel averagingOptionsPanel;

    private JTextField samplingTypeFactorField;
    private JLabel samplingTypeLabel;
    private SamplingTypeComboBox samplingTypeComboBox;
    private DateRangeBox dateRangeBox;

    private ChartGeneralTabbedPane chartGeneralTabbedPane;

    public GeneralTab(ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {
        initComponents(viewConfigurationBean, editDialog);
        addComponents();
        setLayout();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        GridBagConstraints vcIdentificationPanelConstraints = new GridBagConstraints();
        vcIdentificationPanelConstraints.fill = GridBagConstraints.BOTH;
        vcIdentificationPanelConstraints.gridx = 0;
        vcIdentificationPanelConstraints.gridy = 0;
        vcIdentificationPanelConstraints.weightx = 1;
        vcIdentificationPanelConstraints.weighty = 0.33;
        dataSubPanel.add(vcIdentificationPanel, vcIdentificationPanelConstraints);

        GridBagConstraints dateRangeBoxConstraints = new GridBagConstraints();
        dateRangeBoxConstraints.fill = GridBagConstraints.BOTH;
        dateRangeBoxConstraints.gridx = 0;
        dateRangeBoxConstraints.gridy = 1;
        dateRangeBoxConstraints.weightx = 1;
        dateRangeBoxConstraints.weighty = 0.34;
        dataSubPanel.add(dateRangeBox, dateRangeBoxConstraints);

        GridBagConstraints averagingOptionsPanelConstraints = new GridBagConstraints();
        averagingOptionsPanelConstraints.fill = GridBagConstraints.BOTH;
        averagingOptionsPanelConstraints.gridx = 0;
        averagingOptionsPanelConstraints.gridy = 2;
        averagingOptionsPanelConstraints.weightx = 1;
        averagingOptionsPanelConstraints.weighty = 0.33;
        dataSubPanel.add(averagingOptionsPanel, averagingOptionsPanelConstraints);

        this.add(dataSubPanel);
        JPanel chartPropertiesPanel = new JPanel();
        chartPropertiesPanel.add(chartGeneralTabbedPane);
        this.add(chartPropertiesPanel);
    }

    private void initializeHistoricCheck() {
        // if HDB is not available
        if (Mambo.isHdbAvailable() == false) {
            historicCheck.setSelected(false);
            historicCheck.setEnabled(false);
        }
        // else if the file mambo.properties doesn't exist or the syntax inside
        // is wrong
        else {
            // Behaviour by default:
            historicCheck.setSelected(true);
        }
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents(ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {

        String msg = "";

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_CREATION_DATE");
        creationDateLabel = new JLabel(msg);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_LAST_UPDATE_DATE");
        lastUpdateDateLabel = new JLabel(msg);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_HISTORIC");
        historicLabel = new JLabel(msg);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_NAME");
        nameLabel = new JLabel(msg);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_AVERAGING_TYPE");
        samplingTypeLabel = new JLabel(msg);

        creationDateValue = new JLabel();
        lastUpdateDateValue = new JLabel();

        nameField = new JTextField();
        nameField.setMargin(new Insets(2, 1, 2, 1));

        samplingTypeComboBox = new SamplingTypeComboBox();
        samplingTypeFactorField = new JTextField();
        samplingTypeFactorField.setMargin(new Insets(2, 1, 2, 1));

        historicCheck = new JCheckBox();
        historicCheck.setIcon(checkedNoIcon);
        historicCheck.setSelectedIcon(checkedYesIcon);
        historicCheck.setPressedIcon(checkedPressedIcon);
        historicCheck.addActionListener(new HistoricCheckboxListener(viewConfigurationBean,
                editDialog));
        initializeHistoricCheck();

        dataSubPanel = new JPanel();
        dataSubPanel.setLayout(new GridBagLayout());
        this.initBoxes();

        dateRangeBox.getDateRangeComboBoxListener()
                .itemStateChanged(
                        new ItemEvent(dateRangeBox.getDateRangeComboBox(),
                                ItemEvent.ITEM_STATE_CHANGED, dateRangeBox
                                        .getDateRangeComboBoxListener().getLast1h(),
                                ItemEvent.SELECTED));

        chartGeneralTabbedPane = new ChartGeneralTabbedPane();
    }

    private void initBoxes() {
        String msg;
        TitledBorder tb;

        // ---------------------------
        vcIdentificationPanel = new JPanel();
        vcIdentificationPanel.setLayout(new GridBagLayout());

        GridBagConstraints historicLabelConstraints = new GridBagConstraints();
        historicLabelConstraints.fill = GridBagConstraints.NONE;
        historicLabelConstraints.gridx = 0;
        historicLabelConstraints.gridy = 0;
        historicLabelConstraints.weightx = 0;
        historicLabelConstraints.weighty = 0;
        historicLabelConstraints.anchor = GridBagConstraints.LINE_START;
        historicLabelConstraints.insets = new Insets(5, 5, 5, 0);
        vcIdentificationPanel.add(historicLabel, historicLabelConstraints);

        GridBagConstraints historicCheckConstraints = new GridBagConstraints();
        historicCheckConstraints.fill = GridBagConstraints.NONE;
        historicCheckConstraints.gridx = 1;
        historicCheckConstraints.gridy = 0;
        historicCheckConstraints.weightx = 1;
        historicCheckConstraints.weighty = 0;
        historicCheckConstraints.anchor = GridBagConstraints.WEST;
        historicCheckConstraints.insets = new Insets(5, 5, 5, 0);
        vcIdentificationPanel.add(historicCheck, historicCheckConstraints);

        GridBagConstraints creationDateLabelConstraints = new GridBagConstraints();
        creationDateLabelConstraints.fill = GridBagConstraints.NONE;
        creationDateLabelConstraints.gridx = 0;
        creationDateLabelConstraints.gridy = 1;
        creationDateLabelConstraints.weightx = 0;
        creationDateLabelConstraints.weighty = 0;
        creationDateLabelConstraints.anchor = GridBagConstraints.LINE_START;
        creationDateLabelConstraints.insets = new Insets(0, 5, 5, 0);
        vcIdentificationPanel.add(creationDateLabel, creationDateLabelConstraints);

        GridBagConstraints creationDateValueConstraints = new GridBagConstraints();
        creationDateValueConstraints.fill = GridBagConstraints.NONE;
        creationDateValueConstraints.gridx = 1;
        creationDateValueConstraints.gridy = 1;
        creationDateValueConstraints.weightx = 1;
        creationDateValueConstraints.weighty = 0;
        creationDateValueConstraints.anchor = GridBagConstraints.WEST;
        creationDateValueConstraints.insets = new Insets(0, 5, 5, 0);
        vcIdentificationPanel.add(creationDateValue, creationDateValueConstraints);

        GridBagConstraints lastUpdateDateLabelConstraints = new GridBagConstraints();
        lastUpdateDateLabelConstraints.fill = GridBagConstraints.NONE;
        lastUpdateDateLabelConstraints.gridx = 0;
        lastUpdateDateLabelConstraints.gridy = 2;
        lastUpdateDateLabelConstraints.weightx = 0;
        lastUpdateDateLabelConstraints.weighty = 0;
        lastUpdateDateLabelConstraints.anchor = GridBagConstraints.LINE_START;
        lastUpdateDateLabelConstraints.insets = new Insets(0, 5, 5, 0);
        vcIdentificationPanel.add(lastUpdateDateLabel, lastUpdateDateLabelConstraints);

        GridBagConstraints lastUpdateDateValueConstraints = new GridBagConstraints();
        lastUpdateDateValueConstraints.fill = GridBagConstraints.NONE;
        lastUpdateDateValueConstraints.gridx = 1;
        lastUpdateDateValueConstraints.gridy = 2;
        lastUpdateDateValueConstraints.weightx = 1;
        lastUpdateDateValueConstraints.weighty = 0;
        lastUpdateDateValueConstraints.anchor = GridBagConstraints.WEST;
        lastUpdateDateValueConstraints.insets = new Insets(0, 5, 5, 0);
        vcIdentificationPanel.add(lastUpdateDateValue, lastUpdateDateValueConstraints);

        GridBagConstraints nameLabelConstraints = new GridBagConstraints();
        nameLabelConstraints.fill = GridBagConstraints.NONE;
        nameLabelConstraints.gridx = 0;
        nameLabelConstraints.gridy = 3;
        nameLabelConstraints.weightx = 0;
        nameLabelConstraints.weighty = 0;
        nameLabelConstraints.anchor = GridBagConstraints.LINE_START;
        nameLabelConstraints.insets = new Insets(0, 5, 5, 0);
        vcIdentificationPanel.add(nameLabel, nameLabelConstraints);

        GridBagConstraints nameFieldConstraints = new GridBagConstraints();
        nameFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameFieldConstraints.gridx = 1;
        nameFieldConstraints.gridy = 3;
        nameFieldConstraints.weightx = 1;
        nameFieldConstraints.weighty = 0;
        nameFieldConstraints.anchor = GridBagConstraints.WEST;
        nameFieldConstraints.insets = new Insets(0, 5, 5, 0);
        vcIdentificationPanel.add(nameField, nameFieldConstraints);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_BOXES_VC_IDENTIFICATION");
        tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg, TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.TOP, GUIUtilities.getTitleFont());
        vcIdentificationPanel.setBorder(tb);
        // ---------------------------

        // ---------------------------
        dateRangeBox = new DateRangeBox();

        // ---------------------------

        // ---------------------------
        averagingOptionsPanel = new JPanel();
        averagingOptionsPanel.setLayout(new GridBagLayout());

        GridBagConstraints samplingTypeLabelConstraints = new GridBagConstraints();
        samplingTypeLabelConstraints.fill = GridBagConstraints.NONE;
        samplingTypeLabelConstraints.gridx = 0;
        samplingTypeLabelConstraints.gridy = 0;
        samplingTypeLabelConstraints.weightx = 0;
        samplingTypeLabelConstraints.weighty = 0;
        samplingTypeLabelConstraints.anchor = GridBagConstraints.LINE_START;
        samplingTypeLabelConstraints.insets = new Insets(5, 5, 5, 0);
        averagingOptionsPanel.add(samplingTypeLabel, samplingTypeLabelConstraints);

        GridBagConstraints samplingTypeFactorFieldConstraints = new GridBagConstraints();
        samplingTypeFactorFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        samplingTypeFactorFieldConstraints.gridx = 1;
        samplingTypeFactorFieldConstraints.gridy = 0;
        samplingTypeFactorFieldConstraints.weightx = 0.4;
        samplingTypeFactorFieldConstraints.weighty = 0;
        samplingTypeFactorFieldConstraints.insets = new Insets(5, 5, 5, 0);
        averagingOptionsPanel.add(samplingTypeFactorField, samplingTypeFactorFieldConstraints);

        GridBagConstraints samplingTypeComboBoxConstraints = new GridBagConstraints();
        samplingTypeComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        samplingTypeComboBoxConstraints.gridx = 2;
        samplingTypeComboBoxConstraints.gridy = 0;
        samplingTypeComboBoxConstraints.weightx = 0.6;
        samplingTypeComboBoxConstraints.weighty = 0;
        samplingTypeComboBoxConstraints.insets = new Insets(5, 5, 5, 0);
        averagingOptionsPanel.add(samplingTypeComboBox, samplingTypeComboBoxConstraints);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_BOXES_VC_AVERAGING_OPTIONS");
        tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg, TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.TOP, GUIUtilities.getTitleFont());
        averagingOptionsPanel.setBorder(tb);
    }

    public String getName() {
        return nameField.getText();
    }

    public void setName(String _name) {
        nameField.setText(_name);
    }

    public void setCreationDate(Timestamp creationDate) {
        if (creationDate != null) {
            creationDateValue.setText(creationDate.toString());
        }
        else {
            creationDateValue.setText("");
        }
    }

    public SamplingType getSamplingType() {
        SamplingType ret = (SamplingType) samplingTypeComboBox.getSelectedItem();
        short factor = 0;

        try {
            String factor_s = samplingTypeFactorField.getText();
            factor = Short.parseShort(factor_s);
        }
        catch (Exception e) {

        }

        if (factor > 0) {
            ret.setHasAdditionalFiltering(true);
            ret.setAdditionalFilteringFactor(factor);
        }
        else {
            ret.setHasAdditionalFiltering(false);
        }

        return ret;
    }

    public void setSamplingType(SamplingType _samplingType) {
        samplingTypeComboBox.setSelectedSamplingType(_samplingType);
        if (_samplingType.hasAdditionalFiltering()) {
            String factor = "" + _samplingType.getAdditionalFilteringFactor();
            samplingTypeFactorField.setText(factor);
        }
        else {
            // samplingTypeFactorField.setText ( "" );
            samplingTypeFactorField.setText("1");
        }
    }

    public void setDateRange(String range) {
        dateRangeBox.setDateRangeComboBoxSelectedItem(range);

    }

    public boolean isDynamicDateRange() {
        return dateRangeBox.getDynamicDateRangeCheckBox().isSelected();
    }

    public void setDynamicDateRange(boolean dynamic) {
        if (dynamic != isDynamicDateRange()) {
            dateRangeBox.setDynamicDateRangeDoClick();
        }
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        if (lastUpdateDate != null) {
            lastUpdateDateValue.setText(lastUpdateDate.toString());
        }
        else {
            lastUpdateDateValue.setText("");
        }
    }

    public void setStartDate(Timestamp startDate) {
        if (startDate != null) {
            dateRangeBox.setStartDate(startDate.toString());
        }
        else {
            dateRangeBox.setStartDate("");
            dateRangeBox.setDateRangeComboBoxSelectedItem("---");
        }
    }

    public void setEndDate(Timestamp endDate) {
        if (endDate != null) {
            dateRangeBox.setEndDate(endDate.toString());
        }
        else {
            dateRangeBox.setEndDate("");
            dateRangeBox.setDateRangeComboBoxSelectedItem("---");
        }
    }

    /**
     * @param b 25 ao�t 2005
     */
    public void setHistoric(boolean b) {
        historicCheck.setSelected(b);
    }

    /**
     * @param b 25 ao�t 2005
     */
    public boolean isHistoric() {
        return historicCheck.isSelected();
    }

    public DateRangeBox getDateRangeBox() {
        return dateRangeBox;

    }

    public Timestamp[] getDynamicStartAndEndDates() {
        return dateRangeBox.getDynamicStartAndEndDates();
    }

    public ChartGeneralTabbedPane getChartGeneralTabbedPane() {
        return chartGeneralTabbedPane;
    }

}
