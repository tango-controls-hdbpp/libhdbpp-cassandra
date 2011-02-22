//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/OpenSearchContextsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenSearchContextsAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OpenSearchContextsAction.java,v $
// Revision 1.2  2006/04/10 08:46:54  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/12/14 14:07:17  ounsy
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
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.sub.dialogs.open.SearchContextsInDBDialog;


/**
 * Opens a popup where the user can choose filtering criterions to look up contexts
 * <UL>
 * <LI>Opens a SearchContextsInDBDialog dialog
 * </UL>
 *
 * @author CLAISSE
 */
public class OpenSearchContextsAction extends BensikinAction
{

	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	public OpenSearchContextsAction(String name)
	{
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		SearchContextsInDBDialog searchContextsInDBDialog = SearchContextsInDBDialog.getInstance();

		searchContextsInDBDialog.pack();
		searchContextsInDBDialog.setVisible(true);
	}

}
