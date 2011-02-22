//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeDetailHdbTitleTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbTitleTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailHdbTitleTable.java,v $
// Revision 1.2  2005/12/15 10:58:31  ounsy
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
import fr.soleil.mambo.models.ACAttributeDetailHdbTitleTableModel;

public class ACAttributeDetailHdbTitleTable extends JTable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8405572319311135324L;
	private static ACAttributeDetailHdbTitleTable instance = null;

	public static ACAttributeDetailHdbTitleTable getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailHdbTitleTable();
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	private ACAttributeDetailHdbTitleTable() {
		super(ACAttributeDetailHdbTitleTableModel.getInstance());
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
