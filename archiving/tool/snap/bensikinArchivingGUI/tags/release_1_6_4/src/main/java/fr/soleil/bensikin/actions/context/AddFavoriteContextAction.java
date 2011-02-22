// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/AddFavoriteContextAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AddFavoriteContextAction.
// (Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: AddFavoriteContextAction.java,v $
// Revision 1.3 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2006/03/27 14:02:06 ounsy
// favorites contexts now have a label
//
// Revision 1.1 2005/12/14 14:07:17 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2 2005/08/22 11:58:33 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.sub.dialogs.open.AddFavoriteContextDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.favorites.FavoritesContextSubMenu;

/**
 * Adds the currently selected context to the selected path of contexts
 * favorites:
 * <UL>
 * <LI>gets the currently selected context, if null do nothing.
 * <LI>gets the favorites path to add it to from the currently opened
 * AddFavoriteContextDialog dialog.
 * <LI>adds the context reference to the FavoritesContextSubMenu instance.
 * <LI>refreshes the application's menu bar with the completed favorites menu.
 * <LI>closes the dialog
 * </UL>
 * 
 * @author CLAISSE
 */
public class AddFavoriteContextAction extends BensikinAction {

    private static final long serialVersionUID = 1030513086611821493L;

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action name
     */
    public AddFavoriteContextAction(String name) {
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

        Context context = Context.getSelectedContext();
        AddFavoriteContextDialog dialog = AddFavoriteContextDialog
                .getInstance();
        if (context == null) {
            dialog.setVisible(false);
            return;
        }
        String id = String.valueOf(context.getContextId());
        String label = dialog.getFavoriteLabel();

        TreePath selectedTreePath = dialog.getSelectedTreePath();
        // FavoritesContextSubMenu treeMenu =
        // FavoritesContextSubMenu.getInstance ();
        Favorites favorites = Favorites.getInstance();
        FavoritesContextSubMenu treeMenu = favorites.getContextSubMenu();

        try {
            treeMenu.addContext(selectedTreePath, id, label);
        } catch (IllegalArgumentException iae) {
            return;
        }

        favorites.setContextSubMenu(treeMenu);

        BensikinFrame frame = BensikinFrame.getInstance();
        BensikinMenuBar menuBar = (BensikinMenuBar) frame.getJMenuBar();

        frame.remove(menuBar);
        menuBar.setFavorites_contexts(treeMenu.getMenuRoot());
        frame.setJMenuBar(menuBar);

        dialog.setVisible(false);
    }

}
