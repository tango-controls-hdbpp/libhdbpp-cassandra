// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/renderers/SnapshotCompareRenderer.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailRenderer.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.5 $
//
// $Log: SnapshotCompareRenderer.java,v $
// Revision 1.5 2007/10/29 14:47:09 soleilarc
// Author: XP
// Mantis bug ID: 5629
// Comment : In the getTableCellRendererComponent method, one apply the renderer
// of the header of the table to the first row if the first row contains the
// header (before printing).
//
// Revision 1.4 2007/06/29 09:18:58 ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.3 2007/03/26 08:07:53 ounsy
// *** empty log message ***
//
// Revision 1.2 2005/12/14 16:16:13 ounsy
// modifications to add asymmetrical snapshots comparisons
//
// Revision 1.1 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:35 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fr.soleil.bensikin.data.snapshot.SnapshotComparison;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;

/**
 * A cell renderer used for SnapshotCompareTable. It does the same thing as its
 * father class SnapshotDetailRenderer but for one difference, in how it
 * displays the first row and column as if they were table headers.
 * (SnapshotCompareTable's first line is filled with columns title to go around
 * a printing bug)
 * 
 * @author CLAISSE
 */
public class SnapshotCompareRenderer extends SnapshotDetailRenderer {

	private Map<String, Color> colorForName;

	public SnapshotCompareRenderer() {
		this.colorForName = new HashMap<String, Color>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component ret = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (ret == null) {
			return null;
		}
		SnapshotCompareTablePrintModel model = null;
		if (table != null
				&& table.getModel() instanceof SnapshotCompareTablePrintModel) {
			model = (SnapshotCompareTablePrintModel) table.getModel();
		}
		if (model != null) {
			if (row == 0 && model.isTitlesDisplayed()) {
				TableCellRenderer renderer = table.getTableHeader()
						.getDefaultRenderer();
				ret = renderer.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);

				int mcol = table.convertColumnIndexToModel(column);
				Color color = this.getColumnColor(mcol, model);
				if (color != null) {
					ret.setBackground(color);
				}

				if (isSelected) {
					ret.setBackground(Color.GRAY);
					ret.setForeground(Color.WHITE);
				}

				ret.repaint();
			} else {
				int comparisonType = -1;
				SnapshotComparison snapshotComparison = model
						.getSnapshotComparisonAtRow(row);
				if (snapshotComparison != null) {
					comparisonType = snapshotComparison.getComparisonType();
				}

				if (column == 0) {
					switch (comparisonType) {
					case SnapshotComparison.ATTR_BELONGS_TO_SN1_ONLY:
						ret.setBackground(SnapshotComparison
								.getFirstSnapshotColor());
						break;

					case SnapshotComparison.ATTR_BELONGS_TO_SN2_ONLY:
						ret.setBackground(SnapshotComparison
								.getSecondSnapshotColor());
						break;

					case SnapshotComparison.ATTR_BELONGS_TO_BOTH:
						ret.setBackground(SnapshotComparison
								.getDiffSnapshotColor());
						break;

					default:
						// do nothing
					}
				}
			}
		}
		model = null;
		return ret;
	}

	private Color getColumnColor(int column,
			SnapshotCompareTablePrintModel tableModel) {
		Color ret = null;
		int comparisonType = tableModel.getSnapshotType(column);
		switch (comparisonType) {
		case SnapshotComparison.SNAPSHOT_TYPE_1:
			ret = SnapshotComparison.getFirstSnapshotColor();
			break;

		case SnapshotComparison.SNAPSHOT_TYPE_2:
			ret = SnapshotComparison.getSecondSnapshotColor();
			break;

		case SnapshotComparison.SNAPSHOT_TYPE_DIFF:
			ret = SnapshotComparison.getDiffSnapshotColor();
			break;

		default:
			ret = null;
		}

		return ret;
	}
}
