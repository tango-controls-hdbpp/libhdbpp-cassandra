//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/OpenOptionsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenOptionsAction.
//						(Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: OpenOptionsAction.java,v $
// Revision 1.6  2006/04/10 08:46:41  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsDialog;

/**
 * An action opening the "Options" popup
 * 
 * @author CLAISSE
 */
public class OpenOptionsAction extends BensikinAction {
	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public OpenOptionsAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		OptionsDialog menuDialog = OptionsDialog.getInstance();
		menuDialog.pack();
		menuDialog.setVisible(true);
	}

}
