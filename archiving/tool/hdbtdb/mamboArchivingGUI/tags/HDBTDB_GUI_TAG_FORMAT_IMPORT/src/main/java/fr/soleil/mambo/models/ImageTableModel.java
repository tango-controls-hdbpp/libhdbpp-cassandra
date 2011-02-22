//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ImageTableModel.
//                      (GIRARDOT Raphaël) - 10 juil. 2006
//
// $Author$
//
// $Revision: 
//
// copyleft :   Synchrotron SOLEIL
//                  L'Orme des Merisiers
//                  Saint-Aubin - BP 48
//                  91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;

import fr.esrf.TangoDs.TangoConst;

/**
 * @author GIRARDOT
 * 
 */
public class ImageTableModel extends DefaultTableModel {
	protected Object value;
	protected int data_type;

	public ImageTableModel(Object value, int data_type) {
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
				if (((byte[][]) value).length != 0
						&& ((byte[][]) value)[0].length != 0) {
					return ((byte[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				if (((short[][]) value).length != 0
						&& ((short[][]) value)[0].length != 0) {
					return ((short[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				if (((int[][]) value).length != 0
						&& ((int[][]) value)[0].length != 0) {
					return ((int[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (((float[][]) value).length != 0
						&& ((float[][]) value)[0].length != 0) {
					return ((float[][]) value).length;
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (((double[][]) value).length != 0
						&& ((double[][]) value)[0].length != 0) {
					return ((double[][]) value).length;
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
				if (((byte[][]) value).length != 0
						&& ((byte[][]) value)[0].length != 0) {
					return ((byte[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				if (((short[][]) value).length != 0
						&& ((short[][]) value)[0].length != 0) {
					return ((short[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				if (((int[][]) value).length != 0
						&& ((int[][]) value)[0].length != 0) {
					return ((int[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (((float[][]) value).length != 0
						&& ((float[][]) value)[0].length != 0) {
					return ((float[][]) value)[0].length;
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (((double[][]) value).length != 0
						&& ((double[][]) value)[0].length != 0) {
					return ((double[][]) value)[0].length;
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
				return new Byte(((byte[][]) value)[columnIndex][rowIndex]);
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				return new Short(((short[][]) value)[columnIndex][rowIndex]);
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				return new Integer(((int[][]) value)[columnIndex][rowIndex]);
			case TangoConst.Tango_DEV_FLOAT:
				return new Float(((float[][]) value)[columnIndex][rowIndex]);
			case TangoConst.Tango_DEV_DOUBLE:
				return new Double(((double[][]) value)[columnIndex][rowIndex]);
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
