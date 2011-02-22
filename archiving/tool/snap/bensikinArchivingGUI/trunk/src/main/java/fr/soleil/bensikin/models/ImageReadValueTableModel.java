package fr.soleil.bensikin.models;

import javax.swing.table.DefaultTableModel;

import fr.esrf.TangoDs.TangoConst;

public class ImageReadValueTableModel extends DefaultTableModel {
	protected Object value;
	protected int data_type;

	public ImageReadValueTableModel(Object value, int data_type) {
		super();
		this.value = value;
		this.data_type = data_type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (value != null) {
			switch (data_type) {
			case TangoConst.Tango_DEV_CHAR:
			case TangoConst.Tango_DEV_UCHAR:
				if (((Byte[][]) value).length != 0
						&& ((Byte[][]) value)[0].length != 0) {
					return ((Byte[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				if (((Short[][]) value).length != 0
						&& ((Short[][]) value)[0].length != 0) {
					return ((Short[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				if (((Integer[][]) value).length != 0
						&& ((Integer[][]) value)[0].length != 0) {
					return ((Integer[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (((Float[][]) value).length != 0
						&& ((Float[][]) value)[0].length != 0) {
					return ((Float[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (((Double[][]) value).length != 0
						&& ((Double[][]) value)[0].length != 0) {
					return ((Double[][]) value).length;
				}
				break;
			default: // nothing to do
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (value != null) {
			switch (data_type) {
			case TangoConst.Tango_DEV_CHAR:
			case TangoConst.Tango_DEV_UCHAR:
				if (((Byte[][]) value).length != 0
						&& ((Byte[][]) value)[0].length != 0) {
					return ((Byte[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				if (((Short[][]) value).length != 0
						&& ((Short[][]) value)[0].length != 0) {
					return ((Short[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				if (((Integer[][]) value).length != 0
						&& ((Integer[][]) value)[0].length != 0) {
					return ((Integer[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (((Float[][]) value).length != 0
						&& ((Float[][]) value)[0].length != 0) {
					return ((Float[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (((Double[][]) value).length != 0
						&& ((Double[][]) value)[0].length != 0) {
					return ((Double[][]) value)[0].length;
				}
				break;
			default: // nothing to do
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		return Integer.toString(columnIndex + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (value != null) {
			switch (data_type) {
			case TangoConst.Tango_DEV_CHAR:
			case TangoConst.Tango_DEV_UCHAR:
				return ((Byte[][]) value)[columnIndex][rowIndex];
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				return ((Short[][]) value)[columnIndex][rowIndex];
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				return ((Integer[][]) value)[columnIndex][rowIndex];
			case TangoConst.Tango_DEV_FLOAT:
				return ((Float[][]) value)[columnIndex][rowIndex];
			case TangoConst.Tango_DEV_DOUBLE:
				return ((Double[][]) value)[columnIndex][rowIndex];
			default: // nothing to do
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
