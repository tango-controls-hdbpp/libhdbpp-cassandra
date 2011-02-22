package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.sub.dialogs.open.AddFavoriteContextDialog;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.favorites.FavoritesContextSubMenu;

/**
 * An action used to add a new subdirectory to the context favorites tree. Does
 * nothing if the selected path or the subdirectory name are empty.
 * 
 * @author CLAISSE
 */
public class RemoveFavoriteContextItemAction extends BensikinAction {

	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public RemoveFavoriteContextItemAction(String name) {
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
		TreePath selectedTreePath = dialog.getSelectedTreePath();
		if (selectedTreePath == null) {
			return;
		}

		Favorites favorites = Favorites.getInstance();
		FavoritesContextSubMenu treeMenu = favorites.getContextSubMenu();

		treeMenu.removeItem(selectedTreePath);
		favorites.setContextSubMenu(treeMenu);

		BensikinFrame frame = BensikinFrame.getInstance();
		BensikinMenuBar menuBar = (BensikinMenuBar) frame.getJMenuBar();

		frame.remove(menuBar);
		menuBar.setFavorites_contexts(treeMenu.getMenuRoot());
		frame.setJMenuBar(menuBar);
	}

}
