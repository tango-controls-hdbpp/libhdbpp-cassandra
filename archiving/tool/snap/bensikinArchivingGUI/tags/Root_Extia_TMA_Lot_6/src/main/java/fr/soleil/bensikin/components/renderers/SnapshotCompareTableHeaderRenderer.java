package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import fr.soleil.bensikin.data.snapshot.SnapshotComparison;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;

public class SnapshotCompareTableHeaderRenderer extends
		DefaultTableCellRenderer {

	private static final long serialVersionUID = -2336430257393345663L;
	private TableCellRenderer renderer;

	public SnapshotCompareTableHeaderRenderer() {
		super();
		renderer = new SortedTableHeaderRenderer();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component ret = renderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		int mcol = table.convertColumnIndexToModel(column);
		SnapshotCompareTablePrintModel tableModel = null;
		if (table != null
				&& table.getModel() instanceof SnapshotCompareTablePrintModel) {
			tableModel = (SnapshotCompareTablePrintModel) table.getModel();
		}
		Color color = this.getColumnColor(mcol, tableModel);
		tableModel = null;
		if (color != null) {
			ret.setBackground(color);
		}

		if (isSelected) {
			ret.setBackground(Color.GRAY);
			ret.setForeground(Color.WHITE);
		}

		ret.repaint();

		return ret;
	}

	private Color getColumnColor(int column,
			SnapshotCompareTablePrintModel tableModel) {
		Color ret = null;
		if (tableModel != null) {
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
		}
		return ret;
	}
}
