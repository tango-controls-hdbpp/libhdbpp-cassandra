/*
 * Synchrotron Soleil File : ViewSpectrumTableModel.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 31 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumTableModel.java,v
 */
package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;

import fr.soleil.mambo.tools.Messages;

/**
 * @author SOLEIL
 */
public class ViewSpectrumTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -2590232244788145159L;
	public final static int R = 0;
	public final static int W = 1;
	public final static int RW = 2;
	public final static int UNKNOWN = 3;

	private boolean[] viewable;
	private int writable;
	private int readDataLength;

	public ViewSpectrumTableModel(int readlength, int writeLength,
			int dataWritable) {
		super();
		writable = dataWritable;
		readDataLength = readlength;
		viewable = new boolean[readlength + writeLength];
		for (int i = 0; i < viewable.length; i++) {
			viewable[i] = true;
		}
		writable = dataWritable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (viewable == null) {
			return 0;
		}
		if (writable == RW) {
			if ((viewable.length - readDataLength) > readDataLength) {
				return viewable.length - readDataLength;
			}
			return readDataLength;
		}
		return viewable.length;
	}

	public int getSelectedReadRowCount() {
		if (viewable == null || writable == W || writable == UNKNOWN) {
			return 0;
		}
		int count;
		if (writable == R) {
			count = viewable.length;
			for (int i = 0; i < viewable.length; i++) {
				if (!viewable[i]) {
					count--;
				}
			}
		} else {// RW
			count = readDataLength;
			for (int i = 0; i < readDataLength; i++) {
				if (!viewable[i]) {
					count--;
				}
			}
		}
		return count;
	}

	public int getSelectedWriteRowCount() {
		if (viewable == null || writable == R || writable == UNKNOWN) {
			return 0;
		}
		int count;
		if (writable == W) {
			count = viewable.length;
			for (int i = 0; i < viewable.length; i++) {
				if (!viewable[i]) {
					count--;
				}
			}
		} else {// RW
			count = viewable.length - readDataLength;
			for (int i = readDataLength; i < viewable.length; i++) {
				if (!viewable[i]) {
					count--;
				}
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (writable == RW) {
			return 4;
		}
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (aValue instanceof Boolean) {
			if (columnIndex == 0) {
				viewable[rowIndex] = ((Boolean) aValue).booleanValue();
			} else if (columnIndex == 3) {
				viewable[rowIndex + readDataLength] = ((Boolean) aValue)
						.booleanValue();
			}
			fireTableDataChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (rowIndex < readDataLength) {
				return new Boolean(viewable[rowIndex]);
			} else
				return "";
		case 1:
			switch (writable) {
			case R:
			case RW:
				if (rowIndex < readDataLength) {
					return (rowIndex + 1) + " "
							+ Messages.getMessage("VIEW_SPECTRUM_READ");
				} else
					return "";
			case W:
				if (rowIndex < viewable.length - readDataLength) {
					return (rowIndex + 1) + " "
							+ Messages.getMessage("VIEW_SPECTRUM_WRITE");
				} else
					return "";
			case UNKNOWN:
				return "" + (rowIndex + 1);
			default:
				return null;
			}
		case 2:
			if (rowIndex < viewable.length - readDataLength) {
				return (rowIndex + 1) + " "
						+ Messages.getMessage("VIEW_SPECTRUM_WRITE");
			} else
				return "";
		case 3:
			if (rowIndex < viewable.length - readDataLength) {
				return new Boolean(viewable[rowIndex + readDataLength]);
			} else
				return "";
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			if (rowIndex < readDataLength) {
				return true;
			}
			return false;
		}
		if (columnIndex == 3) {
			if (rowIndex < viewable.length - readDataLength) {
				return true;
			}
			return false;
		}
		return false;
	}

	public String getColumnName(int column) {
		String ret = "";
		switch (column) {
		case 0:
			ret = Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT");
			switch (writable) {
			case R:
			case RW:
				ret += " " + Messages.getMessage("VIEW_SPECTRUM_READ");
				break;
			case W:
				ret += " " + Messages.getMessage("VIEW_SPECTRUM_WRITE");
				break;
			case UNKNOWN:
				break;
			default:
				return null;
			}
			break;
		case 1:
			ret = Messages.getMessage("VIEW_SPECTRUM_SUBPART");
			switch (writable) {
			case R:
			case RW:
				ret += " " + Messages.getMessage("VIEW_SPECTRUM_READ");
				break;
			case W:
				ret += " " + Messages.getMessage("VIEW_SPECTRUM_WRITE");
				break;
			case UNKNOWN:
				break;
			default:
				return null;
			}
			break;
		case 2:
			ret = Messages.getMessage("VIEW_SPECTRUM_SUBPART") + " "
					+ Messages.getMessage("VIEW_SPECTRUM_WRITE");
			break;
		case 3:
			ret = Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT") + " "
					+ Messages.getMessage("VIEW_SPECTRUM_WRITE");
			break;
		}
		return ret;
	}

	public int getReadLength() {
		return readDataLength;
	}

	public int getWriteLength() {
		return viewable.length - readDataLength;
	}

}
