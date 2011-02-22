//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/FavoritesSubMenu.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  FavoritesSubMenu.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: FavoritesSubMenu.java,v $
// Revision 1.6  2006/03/27 14:04:34  ounsy
// favorites contexts now have a label
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:38  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.favorites;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.actions.context.SelectFavoriteContextAction;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * A generic type for all sub-menus of the "Favorites" menu. Implements
 * recursive methods to add:
 * <UL>
 * <LI>A sub-menu
 * <LI>A reference to a favorite object
 * </UL>
 * anywhere in the tree-like structure.
 * 
 * @author CLAISSE
 */
public class FavoritesSubMenu {
	private String menuName;
	/**
	 * A recursive (ie. containing other TreeMaps) list of favorites
	 * directories. A TreeMap is used for automatic alphabetic ordering.
	 */
	protected TreeMap favoritesSubMenuElements;

	private JMenu menuRoot;

	/**
	 * The TreeModel for the tree used to add a sub-menu or a reference
	 */
	protected DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;

	/**
	 * For a contexts sub-menu
	 */
	public static final int CONTEXT_TYPE = 0;

	/**
	 * For a snapshots sub-menu (not used)
	 */
	public static final int SNAPSHOT_TYPE = 1;

	/**
	 * For a configurations sub-menu (not used)
	 */
	public static final int CONFIGURATION_TYPE = 2;

	private int type = -1;

	/**
	 * Builds a sub-menu of type <code>_type</code> and name
	 * <code>_menuName</code>
	 * 
	 * @param _menuName
	 *            The name as it will appear in the favorites menu.
	 * @param _type
	 *            The type of sub-menu
	 * @throws IllegalArgumentException
	 *             If type is not in (CONTEXT_TYPE, SNAPSHOT_TYPE,
	 *             CONFIGURATION_TYPE)
	 */
	protected FavoritesSubMenu(String _menuName, int _type)
			throws IllegalArgumentException {
		this.menuName = _menuName;
		this.type = _type;

		if (_type != CONTEXT_TYPE && _type != SNAPSHOT_TYPE
				&& _type != CONFIGURATION_TYPE) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns the associated tree model.
	 * 
	 * @return The associated tree model
	 */
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * Returns a XML representation of the sub-menu.
	 * 
	 * @return a XML representation of the sub-menu
	 */
	public String toString() {
		String ret = "";
		XMLLine opening = new XMLLine(this.menuName,
				XMLLine.OPENING_TAG_CATEGORY);
		XMLLine closing = new XMLLine(this.menuName,
				XMLLine.CLOSING_TAG_CATEGORY);

		ret += opening;
		ret += GUIUtilities.CRLF;
		ret += getString(favoritesSubMenuElements);
		ret += GUIUtilities.CRLF;
		ret += closing;

		return ret;
	}

	/**
	 * A recursive method used by toString
	 * 
	 * @param favoritesSubMenuElements2
	 *            The current zoom level in the sub-menu tree
	 * @return An XML representation of the current zoom level in the sub-menu
	 *         tree
	 */
	private String getString(TreeMap favoritesSubMenuElements2) {
		String ret = "";

		Iterator it = favoritesSubMenuElements2.keySet().iterator();
		while (it.hasNext()) {
			Object nextKey = it.next();
			Object nextVal = favoritesSubMenuElements2.get(nextKey);

			if (nextVal instanceof TreeMap) {
				XMLLine opening = new XMLLine((String) nextKey,
						XMLLine.OPENING_TAG_CATEGORY);
				XMLLine closing = new XMLLine((String) nextKey,
						XMLLine.CLOSING_TAG_CATEGORY);

				ret += opening.toString();
				ret += GUIUtilities.CRLF;
				ret += getString((TreeMap) nextVal);
				ret += GUIUtilities.CRLF;
				ret += closing.toString();
				ret += GUIUtilities.CRLF;
			} else if (nextVal instanceof XMLLine) {
				ret += ((XMLLine) nextVal).toString();
				ret += GUIUtilities.CRLF;
			}
		}
		return ret;
	}

	/**
	 * Builds the associated JMenu.
	 * 
	 * @param _favoritesSubMenuElements
	 *            The recursive map to build from
	 */
	void buildMenu(TreeMap _favoritesSubMenuElements) {
		this.favoritesSubMenuElements = _favoritesSubMenuElements;

		menuRoot = new JMenu(this.menuName);

		if (_favoritesSubMenuElements != null) {
			menuRoot = getSubMenu(menuRoot, _favoritesSubMenuElements);
		} else {
			this.favoritesSubMenuElements = new TreeMap();
		}

	}

	/**
	 * A recursive method used by buildMenu
	 * 
	 * @param menuIn
	 *            The current level of sub-menu
	 * @param favoritesSubMenuElements2
	 *            The current zoom level in the sub-menu tree
	 * @return The current step of menu building
	 */
	private JMenu getSubMenu(JMenu menuIn, TreeMap favoritesSubMenuElements2) {
		JMenu ret = menuIn;

		Iterator it = favoritesSubMenuElements2.keySet().iterator();
		while (it.hasNext()) {
			Object nextKey = it.next();
			Object nextVal = favoritesSubMenuElements2.get(nextKey);

			if (nextVal instanceof TreeMap) {
				JMenu next = new JMenu((String) nextKey);
				ret.add(getSubMenu(next, (TreeMap) nextVal));
			} else if (nextVal instanceof XMLLine) {
				String id = ((XMLLine) nextVal).getId();
				String itemName = ((XMLLine) nextVal).getItemName();

				switch (this.type) {
				case CONTEXT_TYPE:
					ret.add(new JMenuItem(new SelectFavoriteContextAction(id,
							itemName)));
					break;

				case SNAPSHOT_TYPE:
					// ret.add ( new JMenuItem ( new
					// SelectFavoriteSnapshotAction ( itemName ) ) );
					break;

				case CONFIGURATION_TYPE:
					// ret.add ( new JMenuItem ( new
					// SelectFavoriteConfigurationAction ( itemName ) ) );
					break;
				}
			}
		}

		return ret;
	}

	/**
	 * Builds the associated Tree Model from the given recursive TreeMap
	 * 
	 * @param _favoritesSubMenuElements
	 *            The recursive TreeMap describing the structure
	 */
	void buildTree(TreeMap _favoritesSubMenuElements) {
		root = new DefaultMutableTreeNode(menuName);

		if (_favoritesSubMenuElements != null) {
			root = getNode(root, _favoritesSubMenuElements);
		} else {
			this.favoritesSubMenuElements = new TreeMap();
		}

		this.treeModel = new DefaultTreeModel(root);
	}

	/**
	 * A recursive method used by buildTree
	 * 
	 * @param nodeIn
	 *            The current level of tree
	 * @param favoritesSubMenuElements2
	 *            The current zoom level in the sub-menu tree
	 * @return The current step of tree building
	 */
	private DefaultMutableTreeNode getNode(DefaultMutableTreeNode nodeIn,
			TreeMap favoritesSubMenuElements2) {
		DefaultMutableTreeNode ret = nodeIn;

		Iterator it = favoritesSubMenuElements2.keySet().iterator();
		while (it.hasNext()) {
			Object nextKey = it.next();
			Object nextVal = favoritesSubMenuElements2.get(nextKey);

			if (nextVal instanceof TreeMap) {
				DefaultMutableTreeNode next = new DefaultMutableTreeNode(
						(String) nextKey);
				ret.add(getNode(next, (TreeMap) nextVal));
			} else if (nextVal instanceof XMLLine) {
				// String itemName = ( ( XMLLine ) nextVal ).getId();
				String itemName = ((XMLLine) nextVal).getItemName();
				DefaultMutableTreeNode leafNode = new DefaultMutableTreeNode(
						itemName);
				leafNode.setAllowsChildren(false);
				ret.add(new DefaultMutableTreeNode(leafNode));
			}
		}

		return ret;
	}

	/**
	 * Builds both the associated JMenu and Tree from the given recursive
	 * TreeMap
	 * 
	 * @param contextsBook
	 *            The recursive TreeMap describing the structure
	 */
	void build(TreeMap contextsBook) {
		this.buildMenu(contextsBook);
		this.buildTree(contextsBook);
	}

	/**
	 * A recursive method used by the addContext, addXXX .. methods of daughter
	 * classes to add an object (context,XXX) reference to the existing
	 * structure.
	 * 
	 * @param in
	 *            The TreeMap before addition of the new object reference
	 * @param path
	 *            The path in the recursive TreeMap where this object reference
	 *            will be added
	 * @param id
	 *            The id to use for this object reference
	 * @return The TreeMap after addition of the new object reference
	 */
	protected TreeMap addSubMenu(TreeMap in, String[] path, String id,
			String label) {
		if (path.length == 0) {
			XMLLine line = new XMLLine(Context.XML_TAG);
			line.setId(id);
			line.setLabel(label);

			// in.put(line.getId() , line);
			// WARNING
			in.put(line.getItemName(), line);
		} else {
			String currentPathComponent = path[0];

			String[] nextPath = new String[path.length - 1];

			for (int i = 0; i < path.length - 1; i++) {
				nextPath[i] = path[i + 1];
			}

			TreeMap nextTreeMap = (TreeMap) in.get(currentPathComponent);
			nextTreeMap = addSubMenu(nextTreeMap, nextPath, id, label);
			in.put(currentPathComponent, nextTreeMap);
		}

		return in;
	}

	/**
	 * A recursive method used by addDirectory to add a menu file to the
	 * existing structure.
	 * 
	 * @param in
	 *            The TreeMap before addition of the new sub directory
	 * @param path
	 *            The path in the recursive TreeMap where this sub directory
	 *            will be added
	 * @param nameOfNewDirectory
	 *            The name of the new sub directory
	 * @return The TreeMap after addition of the new sub directory
	 */
	private TreeMap addSubDirectory(TreeMap in, String[] path,
			String nameOfNewDirectory) {
		if (path.length == 0) {
			TreeMap map = new TreeMap();

			in.put(nameOfNewDirectory, map);
		} else {
			String currentPathComponent = path[0];

			String[] nextPath = new String[path.length - 1];

			for (int i = 0; i < path.length - 1; i++) {
				nextPath[i] = path[i + 1];
			}

			TreeMap nextTreeMap = (TreeMap) in.get(currentPathComponent);
			nextTreeMap = addSubDirectory(nextTreeMap, nextPath,
					nameOfNewDirectory);
			in.put(currentPathComponent, nextTreeMap);
		}

		return in;
	}

	/**
	 * Adds a directory to the existing structure.
	 * 
	 * @param selectedTreePath
	 *            The path in the recursive TreeMap where this directory will be
	 *            added
	 * @param nameOfNewDirectory
	 *            The name of the new directory
	 */
	protected void addDirectory(TreePath selectedTreePath,
			String nameOfNewDirectory) {
		if (selectedTreePath == null) {
			TreeMap map = new TreeMap();
			this.favoritesSubMenuElements.put(nameOfNewDirectory, map);
		} else {
			Object[] path_o = selectedTreePath.getPath();
			String[] path = new String[path_o.length - 1];
			for (int i = 0; i < path_o.length - 1; i++) {
				DefaultMutableTreeNode currentPathElement = (DefaultMutableTreeNode) path_o[i + 1];
				String currentPathElementName = (String) currentPathElement
						.getUserObject();
				path[i] = currentPathElementName;
			}

			TreeMap currentFavoritesSubMenuElements = this.favoritesSubMenuElements;

			currentFavoritesSubMenuElements = addSubDirectory(
					currentFavoritesSubMenuElements, path, nameOfNewDirectory);
			this.favoritesSubMenuElements = currentFavoritesSubMenuElements;
		}

		this.build(this.favoritesSubMenuElements);
	}

	/**
	 * Returns the global favorites JMenu
	 * 
	 * @return the global favorites JMenu
	 */
	public JMenu getMenuRoot() {
		return menuRoot;
	}

	/**
	 * @param selectedTreePath
	 */
	// cf. addDirectory
	public void removeItem(TreePath selectedTreePath) {
		if (selectedTreePath == null) {
			// TreeMap map = new TreeMap();
			// this.favoritesSubMenuElements.put(nameOfNewDirectory , map);
		} else {
			Object[] path_o = selectedTreePath.getPath();
			String[] path = new String[path_o.length - 1];
			for (int i = 0; i < path_o.length - 1; i++) {
				DefaultMutableTreeNode currentPathElement = (DefaultMutableTreeNode) path_o[i + 1];
				// System.out.println (
				// "CLA/currentPathElement/"+currentPathElement.getUserObject().getClass().toString()
				// );
				// currentPathElement.g
				// String currentPathElementName = ( String )
				// currentPathElement.getUserObject();
				String currentPathElementName;
				if (currentPathElement.getUserObject().getClass().equals(
						DefaultMutableTreeNode.class)) {
					// removing a leaf
					DefaultMutableTreeNode currentPathElementName_node = (DefaultMutableTreeNode) currentPathElement
							.getUserObject();
					currentPathElementName = (String) currentPathElementName_node
							.getUserObject();
				} else {
					// removing a directory
					currentPathElementName = (String) currentPathElement
							.getUserObject();
				}

				// System.out.println (
				// "CLA/currentPathElementName/"+currentPathElementName );
				path[i] = currentPathElementName;
			}

			TreeMap currentFavoritesSubMenuElements = this.favoritesSubMenuElements;
			/*
			 * System.out.println (
			 * "CLA/currentFavoritesSubMenuElements-------------------------------"
			 * ); trace ( currentFavoritesSubMenuElements ); System.out.println
			 * (
			 * "CLA/currentFavoritesSubMenuElements-------------------------------"
			 * );
			 */
			currentFavoritesSubMenuElements = removeSubItem(
					currentFavoritesSubMenuElements, path);
			this.favoritesSubMenuElements = currentFavoritesSubMenuElements;
		}

		this.build(this.favoritesSubMenuElements);
	}

	private TreeMap removeSubItem(TreeMap in, String[] path) {
		if (path.length == 1) {
			// System.out.println ( "CLA/ path [0]/"+ path [0] );
			// trace ( in );
			in.remove(path[0]);
		} else {
			String currentPathComponent = path[0];

			String[] nextPath = new String[path.length - 1];

			for (int i = 0; i < path.length - 1; i++) {
				nextPath[i] = path[i + 1];
			}

			TreeMap nextTreeMap = (TreeMap) in.get(currentPathComponent);
			nextTreeMap = removeSubItem(nextTreeMap, nextPath);
			in.put(currentPathComponent, nextTreeMap);
		}

		return in;
	}

	/**
	 * @param in
	 */
	public void trace(TreeMap in) {
		Set set = in.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			System.out.println("it.next ()/" + it.next());
		}
	}
}
