//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/SnapshotListTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotListTableModel.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotListTableModel.java,v $
// Revision 1.6  2006/06/28 12:53:20  ounsy
// minor changes
//
// Revision 1.5  2006/04/13 12:28:00  ounsy
// organized imports
//
// Revision 1.4  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.models;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import fr.soleil.bensikin.components.snapshot.list.SnapshotDataComparator;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.models.listeners.SnapshotTableModelListener;
import fr.soleil.bensikin.tools.Messages;

/**
 * The table model used by SnapshotListTable, this model lists the current list
 * of snapshots. Its rows are SnapshotData objects. A singleton class.
 * 
 * @author CLAISSE
 */
public class SnapshotListTableModel extends DefaultTableModel {
	private SnapshotData[] rows;
	private String[] columnsNames;

	private static SnapshotListTableModel instance = null;

	private int idSort = SnapshotDataComparator.NO_SORT;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static SnapshotListTableModel getInstance() {
		if (instance == null) {
			instance = new SnapshotListTableModel();
		}

		return instance;
	}

	/**
	 * Forces a new instantiation, used to reset the model.
	 * 
	 * @return The new instance
	 */
	public static SnapshotListTableModel forceReset() {
		instance = new SnapshotListTableModel();
		return instance;
	}

	/**
	 * Returns the SnapshotData located at row <code>rowIndex</code>.
	 * 
	 * @param rowIndex
	 *            The specified row
	 * @return The SnapshotData located at row <code>rowIndex</code>
	 */
	public SnapshotData getSnapshotDataAtRow(int rowIndex) {
		return rows[rowIndex];
	}

	/**
	 * Removes all rows which indexes are found in <code>indexesToRemove</code>.
	 * 
	 * @param indexesToRemove
	 *            The list of rows to remove
	 */
	public void removeRows(int[] indexesToRemove) {
		int numberOfLinesToRemove = indexesToRemove.length;
		SnapshotData[] newRows = new SnapshotData[rows.length
				- numberOfLinesToRemove];

		Vector idsToRemoveList = new Vector(numberOfLinesToRemove);
		for (int i = 0; i < numberOfLinesToRemove; i++) {
			int idOfLineToRemove = this
					.getSnapshotDataAtRow(indexesToRemove[i]).getId();
			idsToRemoveList.add(new Integer(idOfLineToRemove));
		}

		int j = 0;
		for (int i = 0; i < rows.length; i++) {
			Integer idOfCurrentLine = new Integer(this.getSnapshotDataAtRow(i)
					.getId());
			if (!idsToRemoveList.contains(idOfCurrentLine)) {
				newRows[j] = rows[i];
				j++;
			} else {
				Snapshot.removeOpenedSnapshot(idOfCurrentLine.intValue());
			}
		}

		rows = newRows;

		for (int i = 0; i < numberOfLinesToRemove; i++) {
			this.fireTableRowsDeleted(indexesToRemove[i], indexesToRemove[i]);
		}
	}

	/**
	 * Initializes the columns titles, and adds a SnapshotTableModelListener to
	 * itself.
	 */
	private SnapshotListTableModel() {
		String msgId = Messages.getMessage("SNAPSHOT_LIST_COLUMNS_ID");
		String msgTime = Messages.getMessage("SNAPSHOT_LIST_COLUMNS_TIME");
		String msgComment = Messages
				.getMessage("SNAPSHOT_LIST_COLUMNS_COMMENT");

		columnsNames = new String[this.getColumnCount()];
		columnsNames[0] = msgId;
		columnsNames[1] = msgTime;
		columnsNames[2] = msgComment;

		rows = new SnapshotData[0];

		this.addTableModelListener(new SnapshotTableModelListener());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 3;
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
		Object value = null;

		switch (columnIndex) {
		case 0:
			value = new Integer(rows[rowIndex].getId());
			break;

		case 1:
			value = rows[rowIndex].getTime();
			break;

		case 2:
			value = rows[rowIndex].getComment();
			break;

		default:
			return null;
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		return columnsNames[columnIndex];
	}

	/**
	 * Changes the current model's list of snapshots, and refreshes the list.
	 * 
	 * @param snapshots
	 *            The model's new list of snapshots
	 */
	public void updateList(Snapshot[] snapshots) {
		int linesNumber = snapshots.length;
		rows = new SnapshotData[linesNumber];
		for (int i = 0; i < linesNumber; i++) {
			SnapshotData line = snapshots[i].getSnapshotData();
			rows[i] = line;
		}

		this.fireTableRowsInserted(0, linesNumber - 1);
	}

	/**
	 * Adds a line at the end of the current model for the specified snapshot,
	 * and refreshes the list.
	 * 
	 * @param snapshot
	 *            The snapshot to add to the current list
	 */
	public void updateList(Snapshot snapshot) {
		// int linesNumber = 1;
		SnapshotData[] before = this.rows;
		rows = new SnapshotData[before.length + 1];

		for (int i = 0; i < before.length; i++) {
			rows[i] = before[i];
		}
		rows[before.length] = snapshot.getSnapshotData();

		this.fireTableRowsInserted(before.length, before.length);
	}

	/**
	 * Changes the current model's list of snapshots, and refreshes the list.
	 * 
	 * @param _rows
	 *            The model's new list of snapshots data
	 */
	public void setRows(SnapshotData[] _rows) {
		this.rows = _rows;
		this.fireTableRowsInserted(0, _rows.length - 1);
	}

	/**
	 * Removes all rows.
	 */
	public void removeAllRows() {
		int firstRemoved = 0;
		int lastRemoved = rows.length - 1;
		this.setRows(new SnapshotData[0]);

		Snapshot.removeAllOpenedSnapshots();

		if (lastRemoved >= firstRemoved) {
			this.fireTableRowsDeleted(firstRemoved, lastRemoved);
		}
	}

	/**
	 * Refreshes the comment for the specified Snapshot.
	 * 
	 * @param snapshotData
	 *            The Snapshot to refresh, with its modified comment field
	 */
	public void refreshComment(SnapshotData snapshotData) {
		if (rows == null) {
			return;
		}

		int idOfRowToRefresh = snapshotData.getId();
		String commentToRefresh = snapshotData.getComment();

		for (int i = 0; i < rows.length; i++) {
			int currentId = rows[i].getId();
			if (currentId == idOfRowToRefresh) {
				rows[i].setComment(commentToRefresh);
				this.fireTableRowsUpdated(i, i);
				break;
			}
		}
	}

	/**
	 * Sorts the table's lines relative to the specified column. If the the
	 * table is already sorted relative to this column, reverses the sort.
	 * 
	 * @param clickedColumnIndex
	 *            The index of the column to sort the lines by
	 */
	public void sort(int clickedColumnIndex) {
		switch (clickedColumnIndex) {
		case 0:
			sortByColumn(SnapshotDataComparator.COMPARE_ID);
			break;

		case 1:
			sortByColumn(SnapshotDataComparator.COMPARE_TIME);
			break;

		case 2:
			sortByColumn(SnapshotDataComparator.COMPARE_COMMENT);
			break;
		}
	}

	/**
	 * Sorts the table's lines relative to the specified field. If the the table
	 * is already sorted relative to this column, reverses the sort.
	 * 
	 * @param compareCase
	 *            The type of field to sort the lines by
	 */
	private void sortByColumn(int compareCase) {
		int newSortType = SnapshotDataComparator.getNewSortType(this.idSort);

		Vector v = new Vector();
		for (int i = 0; i < rows.length; i++) {
			v.add(this.getSnapshotDataAtRow(i));
		}

		Collections.sort(v, new SnapshotDataComparator(compareCase));
		if (newSortType == SnapshotDataComparator.SORT_DOWN) {
			Collections.reverse(v);
		}

		SnapshotData[] newRows = new SnapshotData[rows.length];
		Enumeration enumer = v.elements();
		int i = 0;
		while (enumer.hasMoreElements()) {
			newRows[i] = (SnapshotData) enumer.nextElement();
			i++;
		}

		this.rows = newRows;
		this.fireTableDataChanged();

		this.idSort = newSortType;
	}
}
