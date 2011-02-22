//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/OpenEditCommentAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenEditCommentAction.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OpenEditCommentAction.java,v $
// Revision 1.2  2006/04/10 08:47:14  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
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
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.sub.dialogs.UpdateCommentDialog;

/**
 * An action opening the "Edit comment" popup for a given snapshot.
 * 
 * @author CLAISSE
 */
public class OpenEditCommentAction extends BensikinAction {
	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public OpenEditCommentAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		UpdateCommentDialog updateCommentDialog = new UpdateCommentDialog();

		updateCommentDialog.pack();
		updateCommentDialog.setVisible(true);
	}

}
