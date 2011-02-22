// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/SnapshotCompareTableModel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailTableModel.
// (Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: SnapshotCompareTableModel.java,v $
// Revision 1.9 2007/07/04 12:39:26 ounsy
// sort by name by default
//
// Revision 1.8 2007/07/03 09:44:22 ounsy
// sorting ok
//
// Revision 1.7 2007/07/03 08:41:54 ounsy
// SnapshotCompareTable sorting ok
//
// Revision 1.6 2007/06/29 09:18:58 ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.5 2007/06/28 14:39:19 ounsy
// Snapshot Compare Table sorting
//
// Revision 1.4 2007/03/26 08:07:53 ounsy
// *** empty log message ***
//
// Revision 1.3 2006/06/28 12:53:20 ounsy
// minor changes
//
// Revision 1.2 2005/12/14 16:43:28 ounsy
// minor changes
//
// Revision 1.1 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:40 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.models;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotComparisonComparator;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotAttribute;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.data.snapshot.SnapshotComparison;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.Messages;

/**
 * The table model used by SnapshotCompareTable, this model lists the values of
 * the snapshots involved in the comparison, for each of those attributes. Its
 * rows are SnapshotComparison objects. The number of columns depends on the
 * user-defined display options.
 * 
 * @author CLAISSE
 */
public class SnapshotCompareTableModel extends SortedTableModel {

	private static final long serialVersionUID = -8810932900385131562L;

	private Map<Integer, String> colNamesMap;

	/**
	 * The table's rows
	 */
	protected SnapshotComparison[] rows;

	/**
	 * The table's columns names
	 */
	protected static String[] columnsNames;

	/**
	 * The names of the snapshots to be compared
	 */
	protected static String[] snapNames;

	private Snapshot snapshot1, snapshot2;

	/**
	 * Initializes the columns titles, and sets the showXXX attributes.
	 * 
	 * @param snapshot1
	 *            The first snapshot of the comparison
	 * @param snapshot1Title
	 *            The title to associate with the 1st snapshot
	 * @param snapshot2
	 *            The second snapshot of the comparison
	 * @param snapshot2Title
	 *            The title to associate with the 2nd snapshot
	 */
	public SnapshotCompareTableModel(Snapshot snapshot1, String snapshot1Title,
			Snapshot snapshot2, String snapshot2Title) {
		super();
		this.colNamesMap = new Hashtable<Integer, String>();

		this.snapshot1 = snapshot1;
		this.snapshot2 = snapshot2;

		String msgAttr = Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_NAME");
		String msgW = Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_W");
		String msgR = Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_R");
		String msgDelta = Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DELTA");

		columnsNames = new String[4];
		columnsNames[SnapshotComparison.COLUMN_ATTR_NAME_TYPE] = msgAttr;
		columnsNames[SnapshotComparison.COLUMN_READ_TYPE] = msgR;
		columnsNames[SnapshotComparison.COLUMN_WRITE_TYPE] = msgW;
		columnsNames[SnapshotComparison.COLUMN_DELTA_TYPE] = msgDelta;

		snapNames = new String[4];
		snapNames[SnapshotComparison.SNAPSHOT_TYPE_ATTR_NAME] = msgAttr;
		snapNames[SnapshotComparison.SNAPSHOT_TYPE_1] = snapshot1Title;
		snapNames[SnapshotComparison.SNAPSHOT_TYPE_2] = snapshot2Title;
		snapNames[SnapshotComparison.SNAPSHOT_TYPE_DIFF] = snapshot1Title + "-"
				+ snapshot2Title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return Options.getInstance().getSnapshotOptions()
				.getCompareColumnOrder().length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (rows == null) {
			return 0;
		}

		return rows.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		// System.out.println("SnapshotCompareTableModel/getValueAt/START/columnIndex/"+columnIndex);
		if (columnIndex < 0) {
			System.out
					.println("!!!!!!!!SnapshotCompareTableModel/getValueAt/rowIndex/"
							+ rowIndex + "/columnIndex/" + columnIndex);
		}
		int columnType = getColumnType(columnIndex);
		int snapshotType = getSnapshotType(columnIndex);

		SnapshotComparison row = rows[rowIndex];
		if (row == null)
			return null;
		SnapshotAttribute snap = row.getSnapshotAttribute(snapshotType);

		switch (columnType) {
		case SnapshotComparison.COLUMN_ATTR_NAME_TYPE:
			return snap.getAttribute_complete_name();

		case SnapshotComparison.COLUMN_WRITE_TYPE:
			return snap.getWriteValue();

		case SnapshotComparison.COLUMN_READ_TYPE:
			return snap.getReadValue();

		case SnapshotComparison.COLUMN_DELTA_TYPE:
			return snap.getDeltaValue();

		default:
			throw new IllegalStateException();
		}
	}

	/**
	 * Returns the type of snapshot found at the specified index (depending on
	 * the columns display options). Possible values are
	 * <UL>
	 * <LI>SNAPSHOT_TYPE_ATTR_NAME: the specified column is the attributes names
	 * columns
	 * <LI>SNAPSHOT_TYPE_1: the specified column is one of the columns for the
	 * first snapshot of the comparison
	 * <LI>SNAPSHOT_TYPE_2: the specified column is one of the columns for the
	 * first snapshot of the comparison
	 * <LI>SNAPSHOT_TYPE_DIFF: the specified column is one of the columns for
	 * the "difference snapshot" of the comparison
	 * </UL>
	 * 
	 * @param columnIndex
	 *            The specified column index
	 * @return The type of snapshot found at the specified index
	 */
	public int getSnapshotType(int columnIndex) {
		// String columnName = this.getColumnName(columnIndex);

		int[] columnOrder = Options.getInstance().getSnapshotOptions()
				.getCompareColumnOrder();
		if (columnIndex < columnOrder.length) {
			int type = columnOrder[columnIndex];
			switch (type) {
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_R:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_W:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_DELTA:
				columnOrder = null;
				return SnapshotComparison.SNAPSHOT_TYPE_1;

			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_R:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_W:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_DELTA:
				columnOrder = null;
				return SnapshotComparison.SNAPSHOT_TYPE_2;

			case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_R:
			case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_DELTA:
			case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_W:
				return SnapshotComparison.SNAPSHOT_TYPE_DIFF;
			default:
				columnOrder = null;
				return SnapshotComparison.SNAPSHOT_TYPE_ATTR_NAME;
			}
		} else {
			columnOrder = null;
			throw new IllegalStateException();
		}
		// if (columnIndex == 0) {
		// return SnapshotComparison.SNAPSHOT_TYPE_ATTR_NAME;
		// }
		//
		// columnIndex -= 1;
		// int subColumnsCount = getSubColumnsCount();
		// int idx = 1;
		//
		// while (columnIndex >= subColumnsCount) {
		// columnIndex -= subColumnsCount;
		// idx++;
		// }
		//
		// Options option = Options.getInstance();
		// if (!option.getSnapshotOptions().isShowDiff()) {
		// idx++;
		// }
		//
		// //
		// System.out.println("SnapshotCompareTableModel/getSnapshotType/columnIndex/"+columnIndex+"/getSnapshotType/"+idx);
		//
		// return idx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		if (columnIndex < 0) {
			System.out
					.println("!!!!!!!!SnapshotCompareTableModel/getColumnName/START/columnIndex/"
							+ columnIndex);
			// return "";
		}

		int columnType = getColumnType(columnIndex);
		int snapshotType = getSnapshotType(columnIndex);

		String ret = columnsNames[columnType];

		if (snapshotType != SnapshotComparison.SNAPSHOT_TYPE_ATTR_NAME) {
			ret += " (";
			ret += snapNames[snapshotType];
			this.colNamesMap.put(columnIndex, snapNames[snapshotType]);
			ret += ")";
		}

		return ret;
	}

	public String getMappedColName(int columnIndex) {
		return this.colNamesMap.get(columnIndex);
	}

	/**
	 * Returns the type of the column found at the specified index (depending on
	 * the columns display options). Possible values are
	 * <UL>
	 * <LI>COLUMN_ATTR_NAME_TYPE: the specified column is the attributes names
	 * columns
	 * <LI>COLUMN_READ_TYPE: the specified column is a Read Value column
	 * <LI>COLUMN_WRITE_TYPE: the specified column is a Write Value column
	 * <LI>COLUMN_DELTA_TYPE: the specified column is a Delta Value column
	 * </UL>
	 * 
	 * @param columnIndex
	 *            The specified column index
	 * @return The type of the column found at the specified index
	 */
	private int getColumnType(int columnIndex) {
		if (columnIndex == -1) {
			System.out
					.println("!!!!!!!!SnapshotCompareTableModel/getColumnName/START/columnIndex/"
							+ columnIndex);
			// columnIndex = 1;
		}

		int[] columnOrder = Options.getInstance().getSnapshotOptions()
				.getCompareColumnOrder();
		if (columnIndex < columnOrder.length) {
			int type = columnOrder[columnIndex];
			switch (type) {
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_R:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_R:
			case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_R:
				columnOrder = null;
				return SnapshotComparison.COLUMN_READ_TYPE;

			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_W:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_W:
			case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_W:
				columnOrder = null;
				return SnapshotComparison.COLUMN_WRITE_TYPE;

			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_DELTA:
			case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_DELTA:
			case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_DELTA:
				return SnapshotComparison.COLUMN_DELTA_TYPE;
			default:
				columnOrder = null;
				return SnapshotComparison.COLUMN_ATTR_NAME_TYPE;
			}
		} else {
			columnOrder = null;
			throw new IllegalStateException();
		}
		// // if (colu)
		// if (columnIndex == 0) {
		// return SnapshotComparison.COLUMN_ATTR_NAME_TYPE;
		// }
		// columnIndex -= 1;
		// //
		// System.out.println("SnapshotCompareTableModel/getColumnType/R1/columnIndex/"+columnIndex);
		//
		// int subColumnsCount = getSubColumnsCount();
		// //
		// System.out.println("SnapshotCompareTableModel/getColumnType/R2/subColumnsCount/"+subColumnsCount);
		// while (columnIndex >= subColumnsCount) {
		// //
		// System.out.println("SnapshotCompareTableModel/getColumnType/WHILE 1/subColumnsCount/");
		// columnIndex -= subColumnsCount;
		// //
		// System.out.println("SnapshotCompareTableModel/getColumnType/WHILE 2/subColumnsCount/");
		// }
		//
		// switch (columnIndex) {
		// case 0:
		// if (this.showRead) {
		// return SnapshotComparison.COLUMN_READ_TYPE;
		// }
		// if (this.showWrite) {
		// return SnapshotComparison.COLUMN_WRITE_TYPE;
		// }
		// return SnapshotComparison.COLUMN_DELTA_TYPE;
		//
		// case 1:
		// if (this.showRead) {
		// if (this.showWrite) {
		// return SnapshotComparison.COLUMN_WRITE_TYPE;
		// }
		// return SnapshotComparison.COLUMN_DELTA_TYPE;
		// }
		//
		// case 2:
		// return SnapshotComparison.COLUMN_DELTA_TYPE;
		//
		// default:
		// //
		// System.out.println("SnapshotCompareTableModel/getColumnType/columnIndex/"+columnIndex);
		// throw new IllegalStateException();
		// }
	}

	/**
	 * Builds itself with the data provided by <code>snapshot1</code> and
	 * <code>snapshot2</code>.
	 */
	public void build() {
		this.rows = SnapshotComparison.build(snapshot1, snapshot2, Options
				.getInstance().getSnapshotOptions().isShowDiff());
		fireTableStructureChanged();
		sort(0);
	}

	public SnapshotComparison getSnapshotComparisonAtRow(int row) {
		if (this.rows == null) {
			return null;
		}

		return this.rows[row];
	}

	/**
	 * Sorts the table's lines relative to the specified column. If the the
	 * table is already sorted relative to this column, reverses the sort.
	 * 
	 * @param clickedColumnIndex
	 *            The index of the column to sort the lines by
	 */
	public void sort(int clickedColumnIndex) {
		int snapshotType = getSnapshotType(clickedColumnIndex);
		int columnType = getColumnType(clickedColumnIndex);
		if (snapshotType == SnapshotComparison.SNAPSHOT_TYPE_ATTR_NAME) {
			snapshotType = SnapshotComparison.SNAPSHOT_TYPE_1;
			columnType = SnapshotComparison.COLUMN_ATTR_NAME_TYPE;
		}
		sortByColumn(snapshotType, columnType);
		sortedColumn = clickedColumnIndex;
	}

	/**
	 * Sorts the table's lines relative to the specified field. If the the table
	 * is already sorted relative to this column, reverses the sort.
	 * 
	 * @param compareCase
	 *            The type of field to sort the lines by
	 */
	protected void sortByColumn(int snapshotType, int columnType) {
		int compareCase = -1;
		switch (snapshotType) {
		case SnapshotComparison.SNAPSHOT_TYPE_1:
			switch (columnType) {
			case SnapshotComparison.COLUMN_ATTR_NAME_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_FIRST_NAME;
				break;
			case SnapshotComparison.COLUMN_READ_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_FIRST_READ_VALUE;
				break;
			case SnapshotComparison.COLUMN_WRITE_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_FIRST_WRITE_VALUE;
				break;
			case SnapshotComparison.COLUMN_DELTA_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_FIRST_DELTA_VALUE;
				break;
			}
			break;

		case SnapshotComparison.SNAPSHOT_TYPE_2:
			switch (columnType) {
			case SnapshotComparison.COLUMN_ATTR_NAME_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_SECOND_NAME;
				break;
			case SnapshotComparison.COLUMN_READ_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_SECOND_READ_VALUE;
				break;
			case SnapshotComparison.COLUMN_WRITE_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_SECOND_WRITE_VALUE;
				break;
			case SnapshotComparison.COLUMN_DELTA_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_SECOND_DELTA_VALUE;
				break;
			}
			break;

		case SnapshotComparison.SNAPSHOT_TYPE_DIFF:
			switch (columnType) {
			case SnapshotComparison.COLUMN_ATTR_NAME_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_DIFF_NAME;
				break;
			case SnapshotComparison.COLUMN_READ_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_DIFF_READ_VALUE;
				break;
			case SnapshotComparison.COLUMN_WRITE_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_DIFF_WRITE_VALUE;
				break;
			case SnapshotComparison.COLUMN_DELTA_TYPE:
				compareCase = SnapshotComparisonComparator.COMPARE_DIFF_DELTA_VALUE;
				break;
			}
			break;
		case SnapshotComparison.SNAPSHOT_TYPE_ATTR_NAME:
			compareCase = SnapshotComparisonComparator.COMPARE_FIRST_NAME;
			break;

		}
		int newSortType = SnapshotComparisonComparator
				.getNewSortType(this.idSort);

		Vector<SnapshotComparison> v = getSnapshotComparisons();
		SnapshotComparisonComparator comparator = new SnapshotComparisonComparator(
				compareCase);

		if (columnType == SnapshotComparison.COLUMN_ATTR_NAME_TYPE) {
			Collections.sort(v, comparator);
		} else {
			// organize by categories
			Vector<SnapshotComparison> notComparableAttrs = new Vector<SnapshotComparison>();

			Vector<SnapshotComparison> booleanScalarValues = new Vector<SnapshotComparison>();
			Vector<SnapshotComparison> stringScalarValues = new Vector<SnapshotComparison>();
			Vector<SnapshotComparison> numberScalarValues = new Vector<SnapshotComparison>();

			Vector<SnapshotComparison> booleanSpectrumValues = new Vector<SnapshotComparison>();
			Vector<SnapshotComparison> stringSpectrumValues = new Vector<SnapshotComparison>();
			Vector<SnapshotComparison> numberSpectrumValues = new Vector<SnapshotComparison>();

			Vector<SnapshotComparison> booleanImageValues = new Vector<SnapshotComparison>();
			Vector<SnapshotComparison> stringImageValues = new Vector<SnapshotComparison>();
			Vector<SnapshotComparison> numberImageValues = new Vector<SnapshotComparison>();
			// Prepare the collection of null attributes
			int notNull = -1;
			notNull = sortNotComparableAttrFirst(v, snapshotType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(v.get(i));
			}
			int index = notNull;
			while (index > 0) {
				v.remove(0);
				index--;
			}

			for (int i = 0; i < v.size(); i++) {
				SnapshotComparison compar = v.get(i);
				SnapshotAttribute attr = compar
						.getSnapshotAttribute(snapshotType);
				switch (attr.getData_format()) {
				case AttrDataFormat._SCALAR:
					switch (attr.getData_type()) {
					case TangoConst.Tango_DEV_BOOLEAN:
						booleanScalarValues.add(compar);
						break;
					case TangoConst.Tango_DEV_STRING:
						stringScalarValues.add(compar);
						break;
					case TangoConst.Tango_DEV_CHAR:
					case TangoConst.Tango_DEV_UCHAR:
					case TangoConst.Tango_DEV_SHORT:
					case TangoConst.Tango_DEV_USHORT:
					case TangoConst.Tango_DEV_LONG:
					case TangoConst.Tango_DEV_ULONG:
					case TangoConst.Tango_DEV_FLOAT:
					case TangoConst.Tango_DEV_DOUBLE:
						numberScalarValues.add(compar);
						break;
					default:
						notComparableAttrs.add(compar);
						break;
					}
					break;
				case AttrDataFormat._SPECTRUM:
					switch (attr.getData_type()) {
					case TangoConst.Tango_DEV_BOOLEAN:
						booleanSpectrumValues.add(compar);
						break;
					case TangoConst.Tango_DEV_STRING:
						stringSpectrumValues.add(compar);
						break;
					case TangoConst.Tango_DEV_CHAR:
					case TangoConst.Tango_DEV_UCHAR:
					case TangoConst.Tango_DEV_SHORT:
					case TangoConst.Tango_DEV_USHORT:
					case TangoConst.Tango_DEV_LONG:
					case TangoConst.Tango_DEV_ULONG:
					case TangoConst.Tango_DEV_FLOAT:
					case TangoConst.Tango_DEV_DOUBLE:
						numberSpectrumValues.add(compar);
						break;
					default:
						notComparableAttrs.add(compar);
						break;
					}
					break;
				case AttrDataFormat._IMAGE:
					switch (attr.getData_type()) {
					case TangoConst.Tango_DEV_BOOLEAN:
						booleanImageValues.add(compar);
						break;
					case TangoConst.Tango_DEV_STRING:
						stringImageValues.add(compar);
						break;
					case TangoConst.Tango_DEV_CHAR:
					case TangoConst.Tango_DEV_UCHAR:
					case TangoConst.Tango_DEV_SHORT:
					case TangoConst.Tango_DEV_USHORT:
					case TangoConst.Tango_DEV_LONG:
					case TangoConst.Tango_DEV_ULONG:
					case TangoConst.Tango_DEV_FLOAT:
					case TangoConst.Tango_DEV_DOUBLE:
						numberImageValues.add(compar);
						break;
					default:
						notComparableAttrs.add(compar);
						break;
					}
					break;
				default:
					notComparableAttrs.add(compar);
					break;
				}
			}

			// classify scalars
			notNull = sortNotComparableValueFirst(booleanScalarValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(booleanScalarValues.get(i));
			}
			if (booleanScalarValues.size() > 0)
				index = notNull;
			while (index > 0) {
				booleanScalarValues.remove(0);
				index--;
			}

			notNull = sortNotComparableValueFirst(numberScalarValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(numberScalarValues.get(i));
			}
			if (numberScalarValues.size() > 0)
				index = notNull;
			while (index > 0) {
				numberScalarValues.remove(0);
				index--;
			}

			notNull = sortNotComparableValueFirst(stringScalarValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(stringScalarValues.get(i));
			}
			if (stringScalarValues.size() > 0)
				index = notNull;
			while (index > 0) {
				stringScalarValues.remove(0);
				index--;
			}

			// classify spectrums
			notNull = sortNotComparableValueFirst(booleanSpectrumValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(booleanSpectrumValues.get(i));
			}
			index = notNull;
			while (index > 0) {
				booleanSpectrumValues.remove(0);
				index--;
			}

			notNull = sortNotComparableValueFirst(numberSpectrumValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(numberSpectrumValues.get(i));
			}
			index = notNull;
			while (index > 0) {
				numberSpectrumValues.remove(0);
				index--;
			}

			notNull = sortNotComparableValueFirst(stringSpectrumValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(stringSpectrumValues.get(i));
			}
			index = notNull;
			while (index > 0) {
				stringSpectrumValues.remove(0);
				index--;
			}

			// classify images
			notNull = sortNotComparableValueFirst(booleanImageValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(booleanImageValues.get(i));
			}
			index = notNull;
			while (index > 0) {
				booleanImageValues.remove(0);
				index--;
			}

			notNull = sortNotComparableValueFirst(numberImageValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(numberImageValues.get(i));
			}
			index = notNull;
			while (index > 0) {
				numberImageValues.remove(0);
				index--;
			}

			notNull = sortNotComparableValueFirst(stringImageValues,
					snapshotType, columnType);
			for (int i = 0; i < notNull; i++) {
				notComparableAttrs.add(stringImageValues.get(i));
			}
			index = notNull;
			while (index > 0) {
				stringImageValues.remove(0);
				index--;
			}

			// sort scalars
			Collections.sort(booleanScalarValues, comparator);
			Collections.sort(numberScalarValues, comparator);
			Collections.sort(stringScalarValues, comparator);

			// sort spectrums
			Collections.sort(booleanSpectrumValues, comparator);
			Collections.sort(numberSpectrumValues, comparator);
			Collections.sort(stringSpectrumValues, comparator);

			// sort images
			Collections.sort(booleanImageValues, comparator);
			Collections.sort(numberScalarValues, comparator);
			Collections.sort(stringScalarValues, comparator);

			v.clear();
			v.addAll(notComparableAttrs);
			v.addAll(booleanScalarValues);
			v.addAll(numberScalarValues);
			v.addAll(stringScalarValues);
			v.addAll(booleanSpectrumValues);
			v.addAll(numberSpectrumValues);
			v.addAll(stringSpectrumValues);
			v.addAll(booleanImageValues);
			v.addAll(numberImageValues);
			v.addAll(stringImageValues);
		}

		if (newSortType == SnapshotComparisonComparator.SORT_DOWN) {
			Collections.reverse(v);
		}

		SnapshotComparison[] newRows = new SnapshotComparison[rows.length];
		for (int i = 0; i < v.size(); i++) {
			newRows[i] = v.get(i);
		}
		v.clear();
		v = null;

		this.rows = newRows;
		this.fireTableDataChanged();

		this.idSort = newSortType;
	}

	protected Vector<SnapshotComparison> getSnapshotComparisons() {
		Vector<SnapshotComparison> v = new Vector<SnapshotComparison>();
		for (int i = 0; i < rows.length; i++) {
			v.add(this.getSnapshotComparisonAtRow(i));
		}
		return v;
	}

	// Puts null attributes at the begining.
	// Returns the index of the first not null attribute
	protected int sortNotComparableAttrFirst(Vector<SnapshotComparison> comps,
			int comparedAttribute) {
		Vector<SnapshotComparison> sorted = new Vector<SnapshotComparison>();
		int index = 0;
		for (int i = 0; i < comps.size(); i++) {
			SnapshotAttribute attr = comps.get(i).getSnapshotAttribute(
					comparedAttribute);
			if (attr == null
					|| attr.getData_format() == SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT) {
				sorted.add(comps.get(i));
				index++;
			}
		}
		for (int i = 0; i < comps.size(); i++) {
			SnapshotAttribute attr = comps.get(i).getSnapshotAttribute(
					comparedAttribute);
			if (attr != null
					&& attr.getData_format() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT) {
				sorted.add(comps.get(i));
			}
		}
		comps.clear();
		comps.addAll(sorted);
		sorted.clear();
		sorted = null;
		return index;
	}

	// Puts null attribute values at the begining.
	// Returns the index of the first not null attribute value
	protected int sortNotComparableValueFirst(Vector<SnapshotComparison> comps,
			int comparedAttribute, int valueType) {
		Vector<SnapshotComparison> sorted = new Vector<SnapshotComparison>();
		int index = 0;
		for (int i = 0; i < comps.size(); i++) {
			SnapshotAttribute attr = comps.get(i).getSnapshotAttribute(
					comparedAttribute);
			SnapshotAttributeValue value = null;
			switch (valueType) {
			case SnapshotComparison.COLUMN_READ_TYPE:
				value = attr.getReadValue();
				break;
			case SnapshotComparison.COLUMN_WRITE_TYPE:
				value = attr.getWriteValue();
				break;
			case SnapshotComparison.COLUMN_DELTA_TYPE:
				value = attr.getDeltaValue();
				break;
			}
			if ((value.getDataFormat() == SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT || value
					.isNotApplicable())
					|| (value.getScalarValue() == null
							&& value.getSpectrumValue() == null && value
							.getImageValue() == null)) {
				sorted.add(comps.get(i));
				index++;
			}
			attr = null;
			value = null;
		}
		for (int i = 0; i < comps.size(); i++) {
			SnapshotAttribute attr = comps.get(i).getSnapshotAttribute(
					comparedAttribute);
			SnapshotAttributeValue value = null;
			switch (valueType) {
			case SnapshotComparison.COLUMN_READ_TYPE:
				value = attr.getReadValue();
				break;
			case SnapshotComparison.COLUMN_WRITE_TYPE:
				value = attr.getWriteValue();
				break;
			case SnapshotComparison.COLUMN_DELTA_TYPE:
				value = attr.getDeltaValue();
				break;
			}
			if ((value.getDataFormat() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT && !value
					.isNotApplicable())
					&& (value.getScalarValue() != null
							|| value.getSpectrumValue() != null || value
							.getImageValue() != null)) {
				sorted.add(comps.get(i));
			}
			attr = null;
			value = null;
		}
		comps.clear();
		comps.addAll(sorted);
		sorted.clear();
		sorted = null;
		return index;
	}

}
