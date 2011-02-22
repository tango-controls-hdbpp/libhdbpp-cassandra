package fr.soleil.bensikin.components.context.detail;

import javax.swing.JTable;

import org.jdesktop.swingx.JXTable;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.models.ContextDetailPrintTableModel;

public class ContextDetailPrintTable extends JXTable {

	public ContextDetailPrintTable() {
		super();
		setSortable(false);// for the JXTable not to interact with the sort
		// mechanism already in place
		this.setAutoCreateColumnsFromModel(true);
		this.setRowHeight(20);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.setModel(new ContextDetailPrintTableModel());
	}

	public String toUserFriendlyString() {
		StringBuffer buffer = new StringBuffer();
		int rowCount = getModel().getRowCount();
		int columnCount = getModel().getColumnCount();
		for (int row = 0; row < rowCount; row++) {
			for (int column = 1; column < columnCount; column++) {
				buffer.append(getModel().getValueAt(row, column));
				if (column < columnCount - 1) {
					buffer.append(GUIUtilities.TANGO_DELIM);
				}
			}
			if (row < rowCount - 1) {
				buffer.append(GUIUtilities.CRLF);
			}
		}
		return buffer.toString();
	}

}
