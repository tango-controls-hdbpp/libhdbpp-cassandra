package fr.soleil.mambo.components.view.images;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class ImageViewTableCellRenderer extends DefaultTableCellRenderer {

	public ImageViewTableCellRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp;
		if (column == 0) {
			JTableHeader header = table.getTableHeader();
			TableCellRenderer renderer = header.getDefaultRenderer();
			comp = renderer.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, 0, 0);
		} else {
			TableCellRenderer renderer = new DefaultTableCellRenderer();
			comp = renderer.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}

		return comp;
	}
}