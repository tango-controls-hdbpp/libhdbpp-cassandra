//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeRealDetailTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeRealDetailTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeRealDetailTable.java,v $
// Revision 1.2  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:24  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.archiving;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import fr.soleil.mambo.components.renderers.ACAttributeDetailTableRenderer;
import fr.soleil.mambo.models.ACAttributeRealDetailTableModel;

public class ACAttributeRealDetailTable extends JTable {
	private static ACAttributeRealDetailTable instance = null;

	/**
	 * @return 8 juil. 2005
	 */
	public static ACAttributeRealDetailTable getInstance() {
		if (instance == null) {
			instance = new ACAttributeRealDetailTable();
			instance.setBackground(new Color(210, 190, 190));
		}

		return instance;
	}

	/**
	 * @param snapshot
	 */
	private ACAttributeRealDetailTable() {
		super(ACAttributeRealDetailTableModel.getInstance());
		this.setAutoCreateColumnsFromModel(true);
		this.setTableHeader(new JTableHeader());
		this.setShowGrid(false);

		/*
		 * JButton button = new JButton (); Color bg = button.getBackground ();
		 * this.setBackground ( bg );
		 */
		this.setDefaultRenderer(Object.class,
				new ACAttributeDetailTableRenderer());

		// this.grayOutFirstColumn ();
	}

	/**
	 * 14 juin 2005
	 */
	/*
	 * private void grayOutFirstColumn () { String firstColumnIdentifier =
	 * this.getColumnName( 0 ); TableColumn firstColumn = this.getColumn(
	 * firstColumnIdentifier );
	 * 
	 * //TableCellRenderer firstColumnCellRenderer = this.getTableHeader
	 * ().getDefaultRenderer (); //firstColumn.setCellRenderer(
	 * firstColumnCellRenderer );
	 * 
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#isColumnSelected(int)
	 */
	public boolean isColumnSelected(int column) {
		if (column == 0) {
			return false;
		}

		return super.isColumnSelected(column);
	}

}
