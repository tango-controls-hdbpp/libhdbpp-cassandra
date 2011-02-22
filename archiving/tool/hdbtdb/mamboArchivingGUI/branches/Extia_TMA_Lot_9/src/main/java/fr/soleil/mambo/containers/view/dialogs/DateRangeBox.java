// +======================================================================
// $Source:
// /cvsroot/tango-cs/archiving/tool/hdbtdb/mamboArchivingGUI/src/main/java/fr/soleil/mambo/containers/view/dialogs/DateRangeBox.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewConfiguration.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author$
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
import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import fr.soleil.mambo.actions.view.DynamicDateRangeCheckBoxAction;
import fr.soleil.mambo.actions.view.listeners.DateRangeComboBoxInnerListener;
import fr.soleil.mambo.actions.view.listeners.IDateRangeBoxListener;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.event.DateRangeBoxEvent;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class DateRangeBox extends JPanel {

    private static final long                               serialVersionUID = 4350134103690734098L;
    private JLabel                                          startDateLabel;
    private JLabel                                          endDateLabel;
    private JTextField                                      startDateField;
    private JTextField                                      endDateField;
    private JLabel                                          dynamicDateRangeLabel;
    private JCheckBox                                       dynamicDateRangeCheckBox;
    private JLabel                                          dateRangeLabel;
    private JComboBox                                       dateRangeComboBox;
    private ArrayList<WeakReference<IDateRangeBoxListener>> listeners;

    protected DateRangeComboBoxInnerListener                dateRangeComboBoxListener;

    public DateRangeBox() {
        super(new GridBagLayout());

        listeners = new ArrayList<WeakReference<IDateRangeBoxListener>>();

        initComponents();
        addComponents();
    }

    private void initComponents() {
        String msg = "";

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_START_DATE");
        startDateLabel = new JLabel(msg);

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_END_DATE");
        endDateLabel = new JLabel(msg);

        DocumentListener dateListener = new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireDateRangeBoxEvent();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                fireDateRangeBoxEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fireDateRangeBoxEvent();
            }

        };

        msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE");
        dateRangeLabel = new JLabel(msg);
        dateRangeComboBox = new JComboBox();
        dateRangeComboBoxListener = new DateRangeComboBoxInnerListener(this);
        msg = dateRangeComboBoxListener.getDefaultSelection();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast1h();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast4h();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast8h();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast1d();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast3d();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast7d();
        dateRangeComboBox.addItem(msg);
        msg = dateRangeComboBoxListener.getLast30d();
        dateRangeComboBox.addItem(msg);
        dateRangeComboBox.addActionListener(dateRangeComboBoxListener);

        startDateField = new JTextField();
        startDateField.setText("");
        startDateField.getDocument().addDocumentListener(dateListener);
        startDateField.setPreferredSize(dateRangeComboBox.getPreferredSize());
        endDateField = new JTextField();
        endDateField.setText("");
        endDateField.getDocument().addDocumentListener(dateListener);
        endDateField.setPreferredSize(dateRangeComboBox.getPreferredSize());

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_GENERAL_BOXES_VC_DATA_RANGE");
        TitledBorder tb;
        tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
                GUIUtilities.getTitleFont());
        this.setBorder(tb);

        dynamicDateRangeLabel = new JLabel(Messages
                .getMessage("DIALOGS_EDIT_VC_GENERAL_DYNAMIC_DATE_RANGE"));

        dynamicDateRangeCheckBox = new JCheckBox();
        dynamicDateRangeCheckBox.setSelected(false);
        dynamicDateRangeCheckBox
                .addActionListener(new DynamicDateRangeCheckBoxAction(this));
    }

    private void addComponents() {

        Insets defaultInsets = new Insets(5, 5, 5, 5);

        GridBagConstraints dateRangeLabelConstraints = new GridBagConstraints();
        dateRangeLabelConstraints.fill = GridBagConstraints.BOTH;
        dateRangeLabelConstraints.gridx = 0;
        dateRangeLabelConstraints.gridy = 0;
        dateRangeLabelConstraints.weightx = 0;
        dateRangeLabelConstraints.weighty = 0;
        dateRangeLabelConstraints.insets = defaultInsets;
        GridBagConstraints dateRangeComboBoxConstraints = new GridBagConstraints();
        dateRangeComboBoxConstraints.fill = GridBagConstraints.BOTH;
        dateRangeComboBoxConstraints.gridx = 1;
        dateRangeComboBoxConstraints.gridy = 0;
        dateRangeComboBoxConstraints.weightx = 1;
        dateRangeComboBoxConstraints.weighty = 0;
        dateRangeComboBoxConstraints.insets = defaultInsets;

        GridBagConstraints startDateLabelConstraints = new GridBagConstraints();
        startDateLabelConstraints.fill = GridBagConstraints.BOTH;
        startDateLabelConstraints.gridx = 0;
        startDateLabelConstraints.gridy = 1;
        startDateLabelConstraints.weightx = 0;
        startDateLabelConstraints.weighty = 0;
        startDateLabelConstraints.insets = defaultInsets;
        GridBagConstraints startDateFieldConstraints = new GridBagConstraints();
        startDateFieldConstraints.fill = GridBagConstraints.BOTH;
        startDateFieldConstraints.gridx = 1;
        startDateFieldConstraints.gridy = 1;
        startDateFieldConstraints.weightx = 1;
        startDateFieldConstraints.weighty = 0;
        startDateFieldConstraints.insets = defaultInsets;

        GridBagConstraints endDateLabelConstraints = new GridBagConstraints();
        endDateLabelConstraints.fill = GridBagConstraints.BOTH;
        endDateLabelConstraints.gridx = 0;
        endDateLabelConstraints.gridy = 2;
        endDateLabelConstraints.weightx = 0;
        endDateLabelConstraints.insets = defaultInsets;
        GridBagConstraints endDateFieldConstraints = new GridBagConstraints();
        endDateFieldConstraints.fill = GridBagConstraints.BOTH;
        endDateFieldConstraints.gridx = 1;
        endDateFieldConstraints.gridy = 2;
        endDateFieldConstraints.weightx = 1;
        endDateLabelConstraints.weighty = 0;
        endDateFieldConstraints.weighty = 0;
        endDateFieldConstraints.insets = defaultInsets;

        GridBagConstraints dynamicDateRangeLabelConstraints = new GridBagConstraints();
        dynamicDateRangeLabelConstraints.fill = GridBagConstraints.BOTH;
        dynamicDateRangeLabelConstraints.gridx = 0;
        dynamicDateRangeLabelConstraints.weightx = 0;
        dynamicDateRangeLabelConstraints.weighty = 0;
        dynamicDateRangeLabelConstraints.insets = defaultInsets;
        GridBagConstraints dynamicDateRangeCheckBoxConstraints = new GridBagConstraints();
        dynamicDateRangeCheckBoxConstraints.fill = GridBagConstraints.BOTH;
        dynamicDateRangeCheckBoxConstraints.gridx = 1;
        dynamicDateRangeCheckBoxConstraints.weightx = 1;
        dynamicDateRangeCheckBoxConstraints.weighty = 0;
        dynamicDateRangeCheckBoxConstraints.insets = defaultInsets;

        dynamicDateRangeLabelConstraints.gridy = 3;
        dynamicDateRangeCheckBoxConstraints.gridy = 3;

        this.add(dateRangeLabel, dateRangeLabelConstraints);
        this.add(dateRangeComboBox, dateRangeComboBoxConstraints);
        this.add(startDateLabel, startDateLabelConstraints);
        this.add(startDateField, startDateFieldConstraints);
        this.add(endDateLabel, endDateLabelConstraints);
        this.add(endDateField, endDateFieldConstraints);
        this.add(dynamicDateRangeLabel, dynamicDateRangeLabelConstraints);
        this.add(dynamicDateRangeCheckBox, dynamicDateRangeCheckBoxConstraints);
    }

    public void update(DateRangeBox dateRangeBox) {
        if (dateRangeBox != null) {
            startDateField.setText(dateRangeBox.getStartDateField().getText()
                    .trim());
            startDateField.setCaretPosition(0);
            endDateField.setText(dateRangeBox.getEndDateField().getText()
                    .trim());
            endDateField.setCaretPosition(0);
            startDateField.setEnabled(dateRangeBox.getStartDateField()
                    .isEnabled());
            endDateField.setEnabled(dateRangeBox.getEndDateField().isEnabled());
            dynamicDateRangeCheckBox.setSelected(dateRangeBox
                    .getDynamicDateRangeCheckBox().isSelected());

            dateRangeComboBox.removeActionListener(dateRangeComboBoxListener);
            dateRangeComboBox.setSelectedItem(dateRangeBox
                    .getDateRangeComboBox().getSelectedItem());
            dateRangeComboBox.addActionListener(dateRangeComboBoxListener);
        }
    }

    public void update(ViewConfigurationData data) {
        if (data == null) {
            startDateField.setText("");
            endDateField.setText("");
            startDateField.setEnabled(false);
            endDateField.setEnabled(false);
            dynamicDateRangeCheckBox.setSelected(false);

            dateRangeComboBox.removeActionListener(dateRangeComboBoxListener);
            dateRangeComboBox.setSelectedItem(dateRangeComboBoxListener
                    .getDefaultSelection());
            dateRangeComboBox.addActionListener(dateRangeComboBoxListener);
        } else {
            Timestamp startDate, endDate;
            boolean dynamicDateRange = data.isDynamicDateRange();
            dynamicDateRangeCheckBox.setSelected(dynamicDateRange);
            String dateRange = data.getDateRange();
            if (dateRange == null) {
                dateRange = dateRangeComboBoxListener.getDefaultSelection();
            }
            dateRangeComboBox.removeActionListener(dateRangeComboBoxListener);
            dateRangeComboBox.setSelectedItem(dateRange);
            dateRangeComboBox.addActionListener(dateRangeComboBoxListener);
            if (dynamicDateRange) {
                Timestamp[] dynamic = data.getDynamicStartAndEndDates();
                startDate = dynamic[0];
                endDate = dynamic[1];
            } else {
                startDate = data.getStartDate();
                endDate = data.getEndDate();
            }
            if (startDate == null) {
                startDateField.setText("");
            } else {
                startDateField.setText(startDate.toString());
                startDateField.setCaretPosition(0);
            }
            if (endDate == null) {
                endDateField.setText("");
            } else {
                endDateField.setText(endDate.toString());
                endDateField.setCaretPosition(0);
            }
            startDateField.setEnabled(!dynamicDateRange);
            endDateField.setEnabled(!dynamicDateRange);
        }
    }

    public void setStartAndEndDatesFieldsEnabled(boolean enabled) {
        startDateField.setEnabled(enabled);
        endDateField.setEnabled(enabled);
        fireDateRangeBoxEvent();
    }

    public void setDateRangeComboBox(JComboBox dateRangeComboBox) {
        this.dateRangeComboBox = dateRangeComboBox;

    }

    public JComboBox getDateRangeComboBox() {
        return dateRangeComboBox;
    }

    public void setDateRangeComboBoxSelectedItem(String range) {
        dateRangeComboBox.removeActionListener(dateRangeComboBoxListener);
        dateRangeComboBox.setSelectedItem(range);
        dateRangeComboBox.addActionListener(dateRangeComboBoxListener);
        fireDateRangeBoxEvent();
    }

    public void setDateRangeComboBoxListener(
            DateRangeComboBoxInnerListener dateRangeComboBoxListener) {
        this.dateRangeComboBoxListener = dateRangeComboBoxListener;
    }

    public DateRangeComboBoxInnerListener getDateRangeComboBoxListener() {
        return dateRangeComboBoxListener;
    }

    public JCheckBox getDynamicDateRangeCheckBox() {
        return dynamicDateRangeCheckBox;
    }

    public void setStartDateField(JTextField startDateField) {
        this.startDateField = startDateField;
    }

    public JTextField getStartDateField() {
        return startDateField;
    }

    public void setEndDateField(JTextField endDateField) {
        this.endDateField = endDateField;
    }

    public JTextField getEndDateField() {
        return endDateField;
    }

    public void setDynamicDateRangeCheckBox(JCheckBox dynamicDateRangeCheckBox) {
        this.dynamicDateRangeCheckBox = dynamicDateRangeCheckBox;
    }

    public void setStartDate(String startDate) {
        startDateField.setText(startDate);
        startDateField.setCaretPosition(0);
    }

    public void setEndDate(String endDate) {
        endDateField.setText(endDate);
        endDateField.setCaretPosition(0);
    }

    public void setDynamicDateRangeDoClick() {
        dynamicDateRangeCheckBox.doClick();
    }

    public Timestamp[] getDynamicStartAndEndDates() {
        Timestamp[] startAndEnd = new Timestamp[2];
        long now = System.currentTimeMillis();
        Timestamp end = new Timestamp(now);
        startAndEnd[1] = end;
        long delta = 0;
        long hour = 3600000;
        long day = 86400000;
        String range = (String) dateRangeComboBox.getSelectedItem();
        if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1H")
                .equals(range)) {
            delta = 1 * hour;
        } else if (Messages.getMessage(
                "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_4H").equals(range)) {
            delta = 4 * hour;
        } else if (Messages.getMessage(
                "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_8H").equals(range)) {
            delta = 8 * hour;
        } else if (Messages.getMessage(
                "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1D").equals(range)) {
            delta = 1 * day;
        } else if (Messages.getMessage(
                "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_3D").equals(range)) {
            delta = 3 * day;
        } else if (Messages.getMessage(
                "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_7D").equals(range)) {
            delta = 7 * day;
        } else if (Messages.getMessage(
                "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_30D").equals(range)) {
            delta = 30 * day;
        }
        Timestamp start = new Timestamp(now - delta);
        startAndEnd[0] = start;
        return startAndEnd;
    }

    public boolean isDynamicDateRange() {
        return dynamicDateRangeCheckBox.isSelected();
    }

    public Timestamp getStartDate() throws IllegalArgumentException {
        Timestamp ret = null;
        try {
            String s = startDateField.getText();
            ret = Timestamp.valueOf(s);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return ret;
    }

    public Timestamp getEndDate() throws IllegalArgumentException {
        Timestamp ret = null;
        try {
            String s = endDateField.getText();
            ret = Timestamp.valueOf(s);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    public String getDateRange() {
        String ret = "";
        ret = (String) dateRangeComboBox.getSelectedItem();
        return ret;
    }

    public void addDateRangeBoxListener(IDateRangeBoxListener listener) {
        if (listener != null) {
            synchronized (listeners) {
                boolean canAdd = true;
                for (int i = 0; i < listeners.size(); i++) {
                    WeakReference<IDateRangeBoxListener> soft = listeners
                            .get(i);
                    IDateRangeBoxListener temp = soft.get();
                    if (listener.equals(temp)) {
                        canAdd = false;
                    }
                    temp = null;
                    soft = null;
                }
                if (canAdd) {
                    listeners.add(new WeakReference<IDateRangeBoxListener>(
                            listener));
                }
            }
        }
    }

    public void removeDateRangeBoxListener(IDateRangeBoxListener listener) {
        if (listener != null) {
            synchronized (listeners) {
                ArrayList<WeakReference<IDateRangeBoxListener>> toRemove = new ArrayList<WeakReference<IDateRangeBoxListener>>();
                for (int i = 0; i < listeners.size(); i++) {
                    WeakReference<IDateRangeBoxListener> soft = listeners
                            .get(i);
                    IDateRangeBoxListener temp = soft.get();
                    if ((temp == null) || listener.equals(temp)) {
                        toRemove.add(soft);
                    }
                }
                listeners.removeAll(toRemove);
                toRemove.clear();
                toRemove = null;
            }
        }
    }

    public void removeAllDateRangeBoxListeners() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    private void fireDateRangeBoxEvent() {
        synchronized (listeners) {
            boolean canUpdate = true;
            try {
                getStartDate();
                getEndDate();
            } catch (IllegalArgumentException e) {
                canUpdate = false;
            }
            if (canUpdate) {
                DateRangeBoxEvent event = new DateRangeBoxEvent(this);
                for (int i = 0; i < listeners.size(); i++) {
                    WeakReference<IDateRangeBoxListener> soft = listeners
                            .get(i);
                    IDateRangeBoxListener temp = soft.get();
                    if (temp != null) {
                        temp.dateRangeBoxChanged(event);
                    }
                    temp = null;
                    soft = null;
                }
            }
        }
    }

}
