//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/CloseSelectedACAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OpenACEditDialogAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: CloseSelectedACAction.java,v $
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.OpenedACComboBox;

public class CloseSelectedACAction extends AbstractAction {
	private static CloseSelectedACAction instance = null;

	/**
	 * @return
	 */
	public static CloseSelectedACAction getInstance(String name) {
		if (instance == null) {
			instance = new CloseSelectedACAction(name);
		}

		return instance;
	}

	public static CloseSelectedACAction getInstance() {
		return instance;
	}

	/**
	 * @param name
	 */
	private CloseSelectedACAction(String name) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		OpenedACComboBox.removeSelectedElement();

	}

}
