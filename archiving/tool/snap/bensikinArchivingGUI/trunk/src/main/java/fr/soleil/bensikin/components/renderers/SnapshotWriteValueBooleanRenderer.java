package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import fr.soleil.bensikin.components.BensikinBooleanViewer;

public class SnapshotWriteValueBooleanRenderer extends
		BensikinTableCellRenderer {
	public SnapshotWriteValueBooleanRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof String) {
			if ("true".equalsIgnoreCase(value.toString().trim())
					|| "false".equalsIgnoreCase(value.toString().trim())) {
				BensikinBooleanViewer viewer = new BensikinBooleanViewer(
						new Boolean("true".equalsIgnoreCase(value.toString()
								.trim())));
				viewer.setOpaque(true);
				viewer.setBackground(Color.WHITE);
				return viewer;
			} else
				return super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);
		} else
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
	}
}
