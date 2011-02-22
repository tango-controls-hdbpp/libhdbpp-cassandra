package fr.soleil.bensikin.models;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;


public class SpectrumReadValueTableModel extends SpectrumWriteValueTableModel
{
    public SpectrumReadValueTableModel (int dataType, Object[] spectrumValues)
    {
        super( dataType, spectrumValues );
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex , int columnIndex)
    {
        if(columnIndex == 0)
        {
            return "" + (rowIndex + 1);
        }
        else
        {
            if (values != null && !"NaN".equals(values))
            {
                SnapshotAttributeValue value;
                switch (type)
                {
                    case TangoConst.Tango_DEV_STATE:
                        value = new SnapshotAttributeValue(SnapshotAttributeValue.SCALAR_DATA_FORMAT, type, ((Integer[])values)[rowIndex]);
                        return value;
                    case TangoConst.Tango_DEV_STRING:
                        value = new SnapshotAttributeValue(SnapshotAttributeValue.SCALAR_DATA_FORMAT, type, new String(((String[])values)[rowIndex]));
                        return value;
                    case TangoConst.Tango_DEV_BOOLEAN:
                        value = new SnapshotAttributeValue(SnapshotAttributeValue.SCALAR_DATA_FORMAT, type, ((Boolean[])values)[rowIndex]);
                        return value;
                    case TangoConst.Tango_DEV_UCHAR:
                    case TangoConst.Tango_DEV_CHAR:
                        return "" + ((Byte[])values)[rowIndex];
                    case TangoConst.Tango_DEV_ULONG:
                    case TangoConst.Tango_DEV_LONG:
                        return "" + ((Integer[])values)[rowIndex];
                    case TangoConst.Tango_DEV_USHORT:
                    case TangoConst.Tango_DEV_SHORT:
                        return "" + ((Short[])values)[rowIndex];
                    case TangoConst.Tango_DEV_FLOAT:
                        return "" + ((Float[])values)[rowIndex];
                    case TangoConst.Tango_DEV_DOUBLE:
                        return "" + ((Double[])values)[rowIndex];
                    default:
                        return "";
                }
            }
            else
            {
                return "";
            }
        }
    }
}
