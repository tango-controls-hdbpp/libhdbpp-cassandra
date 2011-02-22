// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/ContextAttributesTreeModel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ContextAttributesTreeModel.
// (Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: ContextAttributesTreeModel.java,v $
// Revision 1.9 2007/08/24 14:09:12 ounsy
// Context attributes ordering (Mantis bug 3912)
//
// Revision 1.8 2007/08/23 13:00:21 ounsy
// minor changes
//
// Revision 1.7 2006/06/28 12:53:20 ounsy
// minor changes
//
// Revision 1.6 2006/01/12 12:59:11 ounsy
// minor changes
//
// Revision 1.5 2005/12/14 16:42:30 ounsy
// added methods necessary for alternate attribute selection
//
// Revision 1.4 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:40 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.comparators.BensikinToStringObjectComparator;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.detail.PossibleAttributesTree;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.tango.util.entity.ui.model.AttributesSelectTableModel;

/**
 * A daughter of AttributesTreeModel representing the attributes of the current
 * context.
 * <UL>
 * <LI>Attributes can be chosen from the Tango attributes list and added to this
 * model
 * <LI>Attributes can be removed from this model
 * </UL>
 * 
 * @author CLAISSE
 */
public class ContextAttributesTreeModel extends AttributesTreeModel {

	private static final long serialVersionUID = -5210432063278811659L;
	private ContextAttributesTree contextAttributesTreeInstance = null;
	private static ContextAttributesTreeModel instance = null;

	/**
	 * Calls mother constructor
	 */
	private ContextAttributesTreeModel() {
		super();
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 */
	public static ContextAttributesTreeModel getInstance(boolean force) {
		if (instance == null || force) {
			instance = new ContextAttributesTreeModel();
		}

		return instance;
	}

	/**
	 * Sets the tree reference of this model. Has to be used if one wishes to
	 * automatically make each new added attribute visible.
	 * 
	 * @param tree
	 *            The tree this is a model of.
	 */
	public void setTree(ContextAttributesTree tree) {
		this.contextAttributesTreeInstance = tree;
	}

	/**
	 * Adds the following list of attributes.
	 * 
	 * @param validSelected
	 *            A list of TreePath references to attributes. All TreePath that
	 *            point to a non-attribute node will be ignored.
	 */
	public void addSelectedAttributes(Vector<TreePath> validSelected,
			boolean updateTableToo) {
		AttributesSelectTableModel attributesSelectTableModel = ContextDetailPanel
				.getInstance().getAttributeTableSelectionBean()
				.getSelectionPanel().getAttributesSelectTable().getModel();

		TreeMap<String, ContextAttribute> possibleAttr = null;
		try {
			possibleAttr = ((AttributesTreeModel) PossibleAttributesTree
					.getInstance().getModel()).getAttributes();
		} catch (Exception e) {
			// nothing to do
			possibleAttr = null;
		}

		TreeMap<String, ContextAttribute> htAttr = this.getAttributes();

		Enumeration<TreePath> enumer = validSelected.elements();
		while (enumer.hasMoreElements()) {
			TreePath aPath = (TreePath) enumer.nextElement();

			// START CURRENT SELECTED PATH
			DefaultMutableTreeNode currentTreeObject = null;
			for (int depth = 0; depth < CONTEXT_TREE_DEPTH; depth++) {
				Object currentPathObject = aPath.getPathComponent(depth);

				if (depth == 0) {
					currentTreeObject = (DefaultMutableTreeNode) this.getRoot();
					continue;
				}

				int numOfChildren = this.getChildCount(currentTreeObject);
				Vector<String> vect = new Vector<String>();
				for (int childIndex = 0; childIndex < numOfChildren; childIndex++) {
					Object childAt = this.getChild(currentTreeObject,
							childIndex);
					vect.add(childAt.toString());
				}

				if (vect.contains(currentPathObject.toString())) {
					int pos = vect.indexOf(currentPathObject.toString());
					currentTreeObject = (DefaultMutableTreeNode) this.getChild(
							currentTreeObject, pos);
				} else {
					vect.add(currentPathObject.toString());
					Collections.sort(vect,
							new BensikinToStringObjectComparator());
					DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(
							currentPathObject.toString());
					int index = vect.indexOf(currentPathObject.toString());
					this.insertNodeInto(newChild, currentTreeObject, index);

					if (depth == CONTEXT_TREE_DEPTH - 1) {
						TreeNode[] path = newChild.getPath();

						ContextAttribute attribute = (possibleAttr == null) ? null
								: (ContextAttribute) possibleAttr
										.get(translatePathIntoKey(path));
						if (attribute == null) {
							attribute = new ContextAttribute(path);
						}

						htAttr.put(translatePathIntoKey(path), attribute);

						// ----------------
						if (contextAttributesTreeInstance != null) {
							TreePath treePath = new TreePath(path);
							contextAttributesTreeInstance.makeVisible(treePath);
						}
						// ----------------
					}

					currentTreeObject = newChild;
				}
			}
		}
		super.attributesHT = htAttr;
		if (updateTableToo) {
			Collection<ContextAttribute> values = getAttributes().values();
			ContextAttribute[] attrArray = values
					.toArray(new ContextAttribute[values.size()]);
			attributesSelectTableModel.setRows(attrArray);
		}
	}

	public void addSelectedAttributes(ContextAttribute[] attributesToAdd,
			boolean updateTableToo) {
		if (attributesToAdd != null) {
			AttributesSelectTableModel attributesSelectTableModel = ContextDetailPanel
					.getInstance().getAttributeTableSelectionBean()
					.getSelectionPanel().getAttributesSelectTable().getModel();

			TreeMap<String, ContextAttribute> htAttr = this.getAttributes();

			for (int attrIndex = 0; attrIndex < attributesToAdd.length; attrIndex++) {
				ContextAttribute attribute = attributesToAdd[attrIndex];
				if (attribute != null) {
					TreePath aPath = attribute.getTreePath();

					// START CURRENT SELECTED PATH
					DefaultMutableTreeNode currentTreeObject = null;
					for (int depth = 0; depth < CONTEXT_TREE_DEPTH; depth++) {
						Object currentPathObject = aPath
								.getPathComponent(depth);

						if (depth == 0) {
							currentTreeObject = (DefaultMutableTreeNode) this
									.getRoot();
							continue;
						}

						int numOfChildren = this
								.getChildCount(currentTreeObject);
						Vector<String> vect = new Vector<String>();
						for (int childIndex = 0; childIndex < numOfChildren; childIndex++) {
							Object childAt = this.getChild(currentTreeObject,
									childIndex);
							vect.add(childAt.toString());
						}

						if (vect.contains(currentPathObject.toString())) {
							int pos = vect
									.indexOf(currentPathObject.toString());
							currentTreeObject = (DefaultMutableTreeNode) this
									.getChild(currentTreeObject, pos);
						} else {
							vect.add(currentPathObject.toString());
							Collections.sort(vect,
									new BensikinToStringObjectComparator());
							DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(
									currentPathObject.toString());
							int index = vect.indexOf(currentPathObject
									.toString());
							this.insertNodeInto(newChild, currentTreeObject,
									index);

							if (depth == CONTEXT_TREE_DEPTH - 1) {
								TreeNode[] path = newChild.getPath();

								htAttr.put(translatePathIntoKey(path),
										attribute);

								// ----------------
								if (contextAttributesTreeInstance != null) {
									TreePath treePath = new TreePath(path);
									contextAttributesTreeInstance
											.makeVisible(treePath);
								}
								// ----------------
							}

							currentTreeObject = newChild;
						}
					}
				}
			}

			super.attributesHT = htAttr;
			if (updateTableToo) {
				Collection<ContextAttribute> values = getAttributes().values();
				ContextAttribute[] attrArray = values
						.toArray(new ContextAttribute[values.size()]);
				attributesSelectTableModel.setRows(attrArray);
			}
		}
	}

	/**
	 * Removes a list of attributes from the model.
	 * 
	 * @param validSelected
	 *            A list of TreePath references to the attributes that are to be
	 *            removed
	 */
	public void removeSelectedAttributes(Vector<TreePath> validSelected) {
		AttributesSelectTableModel attributesSelectTableModel = ContextDetailPanel
				.getInstance().getAttributeTableSelectionBean()
				.getSelectionPanel().getAttributesSelectTable().getModel();

		Enumeration<TreePath> enumer = validSelected.elements();
		while (enumer.hasMoreElements()) {
			TreePath aPath = enumer.nextElement();
			MutableTreeNode node = (MutableTreeNode) aPath
					.getLastPathComponent();

			String key = aPath.getPathComponent(1) + GUIUtilities.TANGO_DELIM
					+ aPath.getPathComponent(2) + GUIUtilities.TANGO_DELIM
					+ aPath.getPathComponent(3) + GUIUtilities.TANGO_DELIM
					+ aPath.getPathComponent(4);

			/*
			 * ContextAttribute contextAttribute = this.getAttribute ( key );
			 * attributesSelectTableModel.r
			 */
			// to do global remove
			this.getAttributes().remove(key);
			try {
				this.removeNodeFromParent(node);
			} catch (Exception e) {
				// do nothing
			}
		}

		Collection<ContextAttribute> values = getAttributes().values();
		ContextAttribute[] attrArray = values
				.toArray(new ContextAttribute[values.size()]);

		attributesSelectTableModel.setRows(attrArray);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.models.AttributesTreeModel#getTreeAttributes()
	 */
	public Collection<String> getTreeAttributes() {
		return super.getTreeAttributes();
	}

	public void removeAll() {
		super.removeAll();
	}

	/**
	 * @param validSelected
	 *            29 juin 2005
	 */
	public void removeSelectedAttributes2(Vector<TreePath> validSelected) {
		Enumeration<TreePath> enumer = validSelected.elements();
		while (enumer.hasMoreElements()) {
			TreePath aPath = enumer.nextElement();

			DefaultMutableTreeNode currentTreeObject = null;
			for (int depth = 0; depth < CONTEXT_TREE_DEPTH; depth++) {
				String userObjectRef = (String) aPath.getPathComponent(depth);

				if (depth == 0) {
					currentTreeObject = (DefaultMutableTreeNode) this.getRoot();
					continue;
				}

				int numOfChildren = this.getChildCount(currentTreeObject);
				// Vector vect = new Vector();
				for (int childIndex = 0; childIndex < numOfChildren; childIndex++) {
					DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) this
							.getChild(currentTreeObject, childIndex);
					String userObjectAt = (String) childAt.getUserObject();

					if (userObjectRef.equals(userObjectAt)) {
						currentTreeObject = childAt;
						break;
					}
				}
			}

			TreeNode[] path = currentTreeObject.getPath();
			this.removeNodeFromParent(currentTreeObject);

			// System.out.println ( "path"+path.length );

			Iterator<String> it = this.getAttributes().keySet().iterator();
			while (it.hasNext()) {
				/* String next = ( String ) */it.next();
				// System.out.println ( "xxx/next 1|"+next+"|" );
			}

			// System.out.println ( "before/"+this.getAttributes
			// ().size()+"/translatePathIntoKey ( path )|"+translatePathIntoKey
			// ( path )+"|" );
			this.getAttributes().remove(translatePathIntoKey(path));
			// System.out.println ( "after/"+this.getAttributes ().size() );
		}
	}
}
