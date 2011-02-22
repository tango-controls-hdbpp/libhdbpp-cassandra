//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeDetailTdbDTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailTdbDTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailTdbDTable.java,v $
// Revision 1.2  2005/12/15 10:59:01  ounsy
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

import javax.swing.JTable;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.components.renderers.ACAttributeDetailTableRenderer;
import fr.soleil.mambo.models.ACAttributeDetailTdbDTableModel;

public class ACAttributeDetailTdbDTable extends JTable {
	private static ACAttributeDetailTdbDTable instance = null;

	public static ACAttributeDetailTdbDTable getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailTdbDTable();
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	private ACAttributeDetailTdbDTable() {
		super(ACAttributeDetailTdbDTableModel.getInstance());
		setAutoCreateColumnsFromModel(true);
		setDefaultRenderer(Object.class, new ACAttributeDetailTableRenderer());
		setEnabled(false);
	}

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
		return false;
	}

}
