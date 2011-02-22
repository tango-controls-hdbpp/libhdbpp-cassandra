//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeDetailHdbDTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbDTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailHdbDTable.java,v $
// Revision 1.2  2005/12/15 10:58:00  ounsy
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

import fr.soleil.mambo.components.renderers.ACAttributeDetailTableRenderer;
import fr.soleil.mambo.models.ACAttributeDetailHdbDTableModel;
import fr.soleil.mambo.tools.GUIUtilities;

public class ACAttributeDetailHdbDTable extends JTable {
	private static ACAttributeDetailHdbDTable instance = null;

	public static ACAttributeDetailHdbDTable getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailHdbDTable();
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	private ACAttributeDetailHdbDTable() {
		super(ACAttributeDetailHdbDTableModel.getInstance());
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
