//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/list/ContextListTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextListTable.
//						(Claisse Laurent) - 17 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: ContextListTable.java,v $
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.context.list;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import fr.soleil.bensikin.actions.listeners.ContextTableHeaderListener;
import fr.soleil.bensikin.actions.listeners.ContextTableListener;
import fr.soleil.bensikin.models.ContextListTableModel;

/**
 * A singleton class containing the current list of contexts. The table's cells
 * are not editable. A ContextTableListener is added that listens to line
 * selection events, and a ContextTableHeaderListener is added that listens to
 * column double-clicks to sort them.
 * 
 * @author CLAISSE
 */
public class ContextListTable extends JTable {
	private static ContextListTable contextListTableInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static ContextListTable getInstance() {
		if (contextListTableInstance == null) {
			contextListTableInstance = new ContextListTable();
		}

		return contextListTableInstance;
	}

	/**
	 * Default constructor.
	 * <UL>
	 * <LI>Instantiates its table model
	 * <LI>Adds a selection listener on its table body (ContextTableListener)
	 * <LI>Adds a sort request listener on its table header
	 * (ContextTableHeaderListener)
	 * <LI>Sets its columns sizes and row height
	 * <LI>Disables the columns auto resize mode
	 * </UL>
	 */
	private ContextListTable() {
		super(ContextListTableModel.getInstance());

		this.addMouseListener(new ContextTableListener());
		JTableHeader header = this.getTableHeader();
		header.addMouseListener(new ContextTableHeaderListener());

		this.getColumn("ID").setPreferredWidth(30);
		this.getColumn("Time").setPreferredWidth(70);
		this.getColumn("Author").setPreferredWidth(95);
		this.getColumn("Name").setPreferredWidth(92);
		this.getColumn("Reason").setPreferredWidth(100);
		this.getColumn("Description").setPreferredWidth(100);

		this.setRowHeight(20);

		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
