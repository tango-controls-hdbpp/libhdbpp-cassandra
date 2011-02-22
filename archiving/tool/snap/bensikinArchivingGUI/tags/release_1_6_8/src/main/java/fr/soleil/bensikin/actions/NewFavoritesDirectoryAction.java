//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/NewFavoritesDirectoryAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NewFileAction.
//						(Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: NewFavoritesDirectoryAction.java,v $
// Revision 1.2  2006/04/10 08:46:41  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2006/03/21 11:27:09  ounsy
// creation for favorites
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
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.containers.sub.dialogs.open.AddFavoriteContextDialog;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.favorites.FavoritesContextSubMenu;

/**
 * An action used to add a new subdirectory to the context favorites tree. Does
 * nothing if the selected path or the subdirectory name are empty.
 * 
 * @author CLAISSE
 */
public class NewFavoritesDirectoryAction extends BensikinAction {

	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public NewFavoritesDirectoryAction(String name) {
		putValue(Action.NAME, name);
		putValue(Action.SHORT_DESCRIPTION, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		AddFavoriteContextDialog dialog = AddFavoriteContextDialog
				.getInstance();
		String newFileName = dialog.getNewFileName();
		if (newFileName == null || newFileName.trim().equals("")) {
			return;
		}

		TreePath selectedTreePath = dialog.getSelectedTreePath();
		if (selectedTreePath == null) {
			return;
		}

		// AFTER
		Favorites favorites = Favorites.getInstance();
		FavoritesContextSubMenu treeMenu = favorites.getContextSubMenu();

		treeMenu.addDirectory(selectedTreePath, newFileName);
	}

}
