package fr.soleil.bensikin.components.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import fr.soleil.bensikin.data.snapshot.SnapshotComparison;
import fr.soleil.bensikin.tools.Messages;

public class SnapshotCompareOptionTableRenderer extends
		DefaultTableCellRenderer {

	private static final long serialVersionUID = -3693062707810114091L;
	private TableCellRenderer renderer;

	public SnapshotCompareOptionTableRenderer() {
		super();
		renderer = new JTable().getTableHeader().getDefaultRenderer();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = renderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_R").equals(
				value)
				|| Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_W")
						.equals(value)
				|| Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_DELTA")
						.equals(value)) {
			comp.setBackground(SnapshotComparison.getFirstSnapshotColor());
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_R")
				.equals(value)
				|| Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_W")
						.equals(value)
				|| Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_DELTA")
						.equals(value)) {
			comp.setBackground(SnapshotComparison.getSecondSnapshotColor());
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R")
				.equals(value)
				|| Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W")
						.equals(value)
				|| Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA")
						.equals(value)) {
			comp.setBackground(SnapshotComparison.getDiffSnapshotColor());
		}
		return comp;
	}

}
