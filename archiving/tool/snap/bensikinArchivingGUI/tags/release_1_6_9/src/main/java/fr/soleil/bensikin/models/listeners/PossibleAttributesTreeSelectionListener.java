//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/listeners/PossibleAttributesTreeSelectionListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PossibleAttributesTreeModelSelectionListener.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: PossibleAttributesTreeSelectionListener.java,v $
// Revision 1.2  2005/12/14 16:45:30  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.models.listeners;

import java.util.ArrayList;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.components.context.detail.PossibleAttributesTree;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.datasources.tango.ITangoManager;
import fr.soleil.bensikin.datasources.tango.TangoManagerFactory;
import fr.soleil.bensikin.models.PossibleAttributesTreeModel;

/**
 * Listens to selection events on the PossibleAttributesTree instance.
 * <UL>
 * <LI>If the event is on any node other than a Member node, nothing happens
 * <LI>If the event is on a Member node which attributes are already loaded,
 * nothing happens
 * <LI>Otherwise, that is in the case of a yet unloaded Member node, the
 * attributes are loaded, and the tree model refreshed
 * </UL>
 * 
 * @author CLAISSE
 */
public class PossibleAttributesTreeSelectionListener implements
		TreeSelectionListener {
	private static DefaultMutableTreeNode currentNode;
	private static PossibleAttributesTreeModel model;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent event) {
		PossibleAttributesTree tree = (PossibleAttributesTree) event
				.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		model = (PossibleAttributesTreeModel) tree.getModel();
		if (node == null) {
			return;
		}
		if (!node.isLeaf()) {
			return;
		}

		currentNode = node;
		query(node.toString(), node.getLevel());
	}

	/**
	 * Called whenever a Member node's attributes needs to be loaded. Uses the
	 * current implementation of <code>ITangoManager</code> to load them.
	 * 
	 * @param node
	 *            The String representation of the member node to load
	 *            attributes for
	 * @param level
	 *            The node level
	 */
	private void query(String node, int level) {
		if (level == 3) {
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) currentNode
					.getParent());
			DefaultMutableTreeNode grdparent = (DefaultMutableTreeNode) parent
					.getParent();

			String device_name = grdparent.toString()
					+ GUIUtilities.TANGO_DELIM + parent.toString()
					+ GUIUtilities.TANGO_DELIM + currentNode.toString();

			ITangoManager manager = TangoManagerFactory.getCurrentImpl();
			ArrayList attribute_list = manager.dbGetAttributeList(device_name);

			if (attribute_list != null) {
				for (int i = 0; i < attribute_list.size(); i++) {
					DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode(
							(String) attribute_list.get(i));
					currentNode.add(attributeNode);

					TreeNode[] path = new TreeNode[5];
					path[0] = (DefaultMutableTreeNode) model.getRoot();
					path[1] = grdparent;
					path[2] = parent;
					path[3] = currentNode;
					path[4] = attributeNode;

					ContextAttribute attribute = new ContextAttribute(
							(String) attribute_list.get(i));
					attribute.setCompleteName(grdparent.toString()
							+ GUIUtilities.TANGO_DELIM + parent.toString()
							+ GUIUtilities.TANGO_DELIM + currentNode.toString()
							+ GUIUtilities.TANGO_DELIM
							+ attributeNode.toString());
					attribute.setDevice(device_name);
					attribute.setDomain(grdparent.toString());
					attribute.setFamily(parent.toString());
					attribute.setMember(currentNode.toString());

					model.addAttribute(path, attribute);
				}
			}
		}
	}
}
