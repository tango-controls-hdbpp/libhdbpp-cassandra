//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/OpenAddFavoriteContextAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenAddFavoriteContextAction.
//						(Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OpenAddFavoriteContextAction.java,v $
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
import fr.soleil.bensikin.containers.sub.dialogs.open.AddFavoriteContextDialog;
import fr.soleil.bensikin.data.context.Context;


/**
 * Opens a popup where the user can add the current context to his favorites
 * <UL>
 * <LI>Gets the current context; if null does nothing
 * <LI>Opens a AddFavoriteContextDialog dialog
 * </UL>
 *
 * @author CLAISSE
 */
public class OpenAddFavoriteContextAction extends BensikinAction
{
	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	public OpenAddFavoriteContextAction(String name)
	{
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent)
	{
		Context selectedContext = Context.getSelectedContext();
		if ( selectedContext == null )
		{
			return;
		}

		AddFavoriteContextDialog dialog = AddFavoriteContextDialog.getInstance();
		dialog.pack();
		dialog.setVisible(true);
	}

}
