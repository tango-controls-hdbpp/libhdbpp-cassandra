package fr.soleil.bensikin.components.context;

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import fr.soleil.bensikin.components.BensikinTree;
import fr.soleil.bensikin.components.renderers.FavoritesTreeCellRenderer;

/**
 * A singleton class representing the Tango attributes that it is possible to
 * add to the current context.
 * 
 * @author CLAISSE
 */
public class FavoritesContextTree extends BensikinTree {

	private static final long serialVersionUID = -4306999161213466137L;
	private static FavoritesContextTree instance = null;

	/**
	 * @param newModel
	 *            The tree's model
	 */
	private FavoritesContextTree() {
		super();

		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel
				.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setSelectionModel(selectionModel);

		this.setCellRenderer(new FavoritesTreeCellRenderer());
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @param newModel
	 * @return The instance
	 */
	public static FavoritesContextTree getInstance() {
		if (instance == null) {
			instance = new FavoritesContextTree();
		}

		return instance;
	}

}
