// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/SnapshotCompareTablePrintModel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailTableModel.
// (Claisse Laurent) - 30 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.6 $
//
// $Log: SnapshotCompareTablePrintModel.java,v $
// Revision 1.6 2007/10/29 14:40:58 soleilarc
// Author: XP
// Mantis bug ID: 5629
// Comment :
// Add a titlesDisplayed data : true if one must insert the header into the
// first row for printing, false otherwise. Add the getter/setter of
// titlesDisplayed.
// In the getRowCount method, replace the condition �ret != 0� by
// titlesDisplayed.
// In the getValueAt method, one display the headers in the first row only for
// printing.
// In the getSnapshotComparisonAtRow method, there is a comparison between the 2
// snapshots, but if the current row contains the header.
// Delete the getColumnName method. This one will be inherited from
// SnapshotCompareTableModel.
// In the getSnapshotComparisons method, one does the comparisons row by row,
// according to the value of the titlesDisplayed data.
//
// Revision 1.5 2007/07/03 08:41:09 ounsy
// minor changes
//
// Revision 1.4 2007/06/29 09:18:58 ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.3 2007/03/26 08:07:53 ounsy
// *** empty log message ***
//
// Revision 1.2 2005/12/14 16:43:46 ounsy
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

import java.util.Vector;

import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotComparison;

/**
 * Extends SnapshotCompareTableModel to get around a print bug that prevents
 * columns titles to be printed. The chosen solution is to make the titles row
 * the first of the model rows.
 * 
 * @author CLAISSE
 */
public class SnapshotCompareTablePrintModel extends SnapshotCompareTableModel {

	private static final long serialVersionUID = -1344493453916528778L;
	private boolean titlesDisplayed = false;

	public SnapshotCompareTablePrintModel(Snapshot snapshot1,
			String snapshot1Title, Snapshot snapshot2, String snapshot2Title) {
		super(snapshot1, snapshot1Title, snapshot2, snapshot2Title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		int ret = super.getRowCount();
		if (titlesDisplayed) {
			// +1 for the titles row
			ret++;
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == -1) {
			System.out
					.println("!!!!!!!!SnapshotCompareTablePrintModel/getValueAt/rowIndex/"
							+ rowIndex + "/columnIndex/" + columnIndex);
			columnIndex = 0;
		}

		if (titlesDisplayed) {
			if (rowIndex == 0) {
				// The first row is the title row
				return super.getColumnName(columnIndex);
			}

			rowIndex -= 1;
		}
		// All other rows are the standard rows
		return super.getValueAt(rowIndex, columnIndex);
	}

	public SnapshotComparison getSnapshotComparisonAtRow(int rowIndex) {
		try {
			if (titlesDisplayed) {
				if (rowIndex == 0) {
					// The first row is the title row
					return null;
				}

				rowIndex -= 1;
			}
			// All other rows are the standard rows
			return super.getSnapshotComparisonAtRow(rowIndex);
		} catch (Exception e) {
			// shouldn't happen, but since it is only used by a renderer, we
			// don't want a render error to cause a crash
			return null;
		}
	}

	protected Vector<SnapshotComparison> getSnapshotComparisons() {
		Vector<SnapshotComparison> v = new Vector<SnapshotComparison>();
		if (titlesDisplayed) {
			for (int i = 0; i < rows.length; i++) {
				v.add(this.getSnapshotComparisonAtRow(i + 1));
			}
		} else {
			for (int i = 0; i < rows.length; i++) {
				v.add(this.getSnapshotComparisonAtRow(i));
			}
		}
		return v;
	}

	/**
	 * @return the titlesDisplayed
	 */
	public boolean isTitlesDisplayed() {
		return titlesDisplayed;
	}

	/**
	 * @param titlesDisplayed
	 *            the titlesDisplayed to set
	 */
	public void setTitlesDisplayed(boolean titlesDisplayed) {
		if (titlesDisplayed != this.titlesDisplayed) {
			this.titlesDisplayed = titlesDisplayed;
			if (titlesDisplayed) {
				fireTableRowsInserted(0, 0);
			} else {
				fireTableRowsDeleted(0, 0);
			}
		}
	}

}
