/*
 * Synchrotron Soleil File : SpectrumWriteValueTableModel.java Project :
 * Bensikin_CVS Description : Author : SOLEIL Original : 14 f�vr. 2006 Revision:
 * Author: Date: State: Log: SpectrumWriteValueTableModel.java,v
 */
package fr.soleil.bensikin.models;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.tools.Messages;

/**
 * @author SOLEIL
 */
public class SpectrumWriteValueTableModel extends AbstractTableModel {

    protected Object[] values;
    protected int      type;
    protected boolean  canSet = false;

    public SpectrumWriteValueTableModel(int dataType, Object[] spectrumValues) {
        super();
        type = dataType;
        values = spectrumValues;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 2;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        if (values != null && !"NaN".equals(values)) {
            switch (type) {
                case TangoConst.Tango_DEV_STRING:
                    return ((String[]) values).length;
                case TangoConst.Tango_DEV_BOOLEAN:
                    return ((Boolean[]) values).length;
                case TangoConst.Tango_DEV_UCHAR:
                case TangoConst.Tango_DEV_CHAR:
                    return ((Byte[]) values).length;
                case TangoConst.Tango_DEV_STATE:
                case TangoConst.Tango_DEV_ULONG:
                case TangoConst.Tango_DEV_LONG:
                    return ((Integer[]) values).length;
                case TangoConst.Tango_DEV_USHORT:
                case TangoConst.Tango_DEV_SHORT:
                    return ((Short[]) values).length;
                case TangoConst.Tango_DEV_FLOAT:
                    return ((Float[]) values).length;
                case TangoConst.Tango_DEV_DOUBLE:
                    return ((Double[]) values).length;
                default:
                    return 0;
            }
        }
        else {
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return "" + (rowIndex + 1);
        }
        else {
            if (values != null && !"NaN".equals(values)) {
                switch (type) {
                    case TangoConst.Tango_DEV_STRING:
                        return ((String[]) values)[rowIndex];
                    case TangoConst.Tango_DEV_BOOLEAN:
                        return "" + ((Boolean[]) values)[rowIndex];
                    case TangoConst.Tango_DEV_UCHAR:
                    case TangoConst.Tango_DEV_CHAR:
                        return "" + ((Byte[]) values)[rowIndex];
                    case TangoConst.Tango_DEV_STATE:
                    case TangoConst.Tango_DEV_ULONG:
                    case TangoConst.Tango_DEV_LONG:
                        return "" + ((Integer[]) values)[rowIndex];
                    case TangoConst.Tango_DEV_USHORT:
                    case TangoConst.Tango_DEV_SHORT:
                        return "" + ((Short[]) values)[rowIndex];
                    case TangoConst.Tango_DEV_FLOAT:
                        return "" + ((Float[]) values)[rowIndex];
                    case TangoConst.Tango_DEV_DOUBLE:
                        return "" + ((Double[]) values)[rowIndex];
                    default:
                        return "";
                }
            }
            else {
                return "";
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            if (isValidValue(aValue.toString())) {
                if (values != null && !"NaN".equals(values)) {
                    switch (type) {
                        case TangoConst.Tango_DEV_STRING:
                            ((String[]) values)[rowIndex] = aValue.toString();
                            break;
                        case TangoConst.Tango_DEV_BOOLEAN:
                            ((Boolean[]) values)[rowIndex] = new Boolean("true"
                                    .equalsIgnoreCase(aValue.toString()));
                            break;
                        case TangoConst.Tango_DEV_UCHAR:
                        case TangoConst.Tango_DEV_CHAR:
                            ((Byte[]) values)[rowIndex] = new Byte(Double
                                    .valueOf(aValue.toString()).byteValue());
                            break;
                        case TangoConst.Tango_DEV_STATE:
                        case TangoConst.Tango_DEV_ULONG:
                        case TangoConst.Tango_DEV_LONG:
                            ((Integer[]) values)[rowIndex] = new Integer(Double
                                    .valueOf(aValue.toString()).intValue());
                            break;
                        case TangoConst.Tango_DEV_USHORT:
                        case TangoConst.Tango_DEV_SHORT:
                            ((Short[]) values)[rowIndex] = new Short(Double
                                    .valueOf(aValue.toString()).shortValue());
                            break;
                        case TangoConst.Tango_DEV_FLOAT:
                            ((Float[]) values)[rowIndex] = Float.valueOf(aValue
                                    .toString());
                            break;
                        case TangoConst.Tango_DEV_DOUBLE:
                            ((Double[]) values)[rowIndex] = Double
                                    .valueOf(aValue.toString());
                            break;
                    }
                    fireTableDataChanged();
                }
            }
            else {
                String title = Messages
                        .getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_TITLE");
                String msg = Messages
                        .getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_MESSAGE");
                JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg,
                        title, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return Messages.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_INDEX");
        }
        if (columnIndex == 1) {
            return Messages.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_VALUE");
        }
        return "";
    }

    /**
     * Returns the values saved in the table. Use them to set the spectrum value
     * of your AttibuteWriteValue.
     * 
     * @return the values saved in the table
     */
    public Object[] getValues() {
        return values;
    }

    private boolean isValidValue(String val) {
        switch (type) {
            case TangoConst.Tango_DEV_STRING:
                return true;
            case TangoConst.Tango_DEV_BOOLEAN:
                if ("true".equalsIgnoreCase(val.trim())
                        || "false".equalsIgnoreCase(val.trim())) {
                    return true;
                }
                return false;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                try {
                    Byte.parseByte(val);
                    return true;
                }
                catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_STATE:
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                try {
                    Integer.parseInt(val);
                    return true;
                }
                catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                try {
                    Short.parseShort(val);
                    return true;
                }
                catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_FLOAT:
                try {
                    Float.parseFloat(val);
                    return true;
                }
                catch (Exception e) {
                    return false;
                }
            case TangoConst.Tango_DEV_DOUBLE:
                try {
                    Double.parseDouble(val);
                    return true;
                }
                catch (Exception e) {
                    return false;
                }
        }
        return false;
    }

    /**
     * Sets all the elements of the table to a specific value
     * 
     * @param value
     *            the value
     * @return <code>true</code> if the value is valid and every element is set
     *         to this value, <code>false></code> otherwise
     */
    public boolean setAll(String value) {
        if (isValidValue(value)) {
            for (int i = 0; i < this.getRowCount(); i++) {
                this.setValueAt(value, i, 1);
            }
            fireTableDataChanged();
            canSet = true;
        }
        else {
            canSet = false;
        }
        if (!canSet) {
            String title = Messages
                    .getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_TITLE");
            String msg = Messages
                    .getMessage("MODIFY_SNAPSHOT_INVALID_VALUE_MESSAGE");
            JOptionPane.showMessageDialog(null, msg, title,
                    JOptionPane.ERROR_MESSAGE);
        }
        return canSet;
    }

    /**
     * Tells wheather this table values can be used or not
     * 
     * @return <code>true</code> if this table values can be used,
     *         <code>false</code> otherwise
     */
    public boolean isCanSet() {
        return canSet;
    }

    /**
     * Forces the table to set its "canSet" variable to a specific boolean value
     * 
     * @param b
     *            the value of the "canSet" variable
     * @see #isCanSet()
     */
    public void setCanSet(boolean b) {
        canSet = b;
    }

}
