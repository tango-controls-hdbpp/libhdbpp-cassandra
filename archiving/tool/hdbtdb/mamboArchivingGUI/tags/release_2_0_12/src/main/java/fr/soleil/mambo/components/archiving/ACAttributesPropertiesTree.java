// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributesPropertiesTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACAttributesPropertiesTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ACAttributesPropertiesTree.java,v $
// Revision 1.5 2006/08/23 10:02:00 ounsy
// getInstance(treemodel) allways updates model
//
// Revision 1.4 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.3 2006/05/16 09:36:11 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.archiving;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.actions.archiving.listeners.ACAttributesPropertiesTreeSelectionListener;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.ACTreeRenderer;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.models.ACTreeModel;
import fr.soleil.mambo.models.AttributesTreeModel;

public class ACAttributesPropertiesTree extends AttributesTree {

	/**
	 * @param newModel
	 */
	private ACAttributesPropertiesTree(ACTreeModel newModel) {
		super(newModel);
		this
				.addTreeSelectionListener(new ACAttributesPropertiesTreeSelectionListener());
		this.setCellRenderer(new ACTreeRenderer());

		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
		this.setToggleClickCount(1);
	}

	private static ACAttributesPropertiesTree instance = null;

	/**
	 * @param newModel
	 * @return 8 juil. 2005
	 */
	public static ACAttributesPropertiesTree getInstance(ACTreeModel newModel) {
		if (instance == null) {
			instance = new ACAttributesPropertiesTree(newModel);
		} else
			instance.setModel(newModel);

		return instance;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static ACAttributesPropertiesTree getInstance() {
		return instance;
	}

	public Vector getListOfAttributesToSet() {
		TreePath[] selectedPath = this.getSelectionPaths();
		if (selectedPath == null || selectedPath.length == 0) {
			return null;
		}

		Vector attributes = new Vector();
		for (int i = 0; i < selectedPath.length; i++) {
			TreePath currentSelectedTreePath = selectedPath[i];
			DefaultMutableTreeNode currentSelectedNode = (DefaultMutableTreeNode) currentSelectedTreePath
					.getLastPathComponent();
			// String name = ( String ) currentSelectedNode.getUserObject();

			Enumeration enumeration = currentSelectedNode.preorderEnumeration();
			while (enumeration.hasMoreElements()) {
				DefaultMutableTreeNode currentTraversedNode = (DefaultMutableTreeNode) enumeration
						.nextElement();

				if (currentTraversedNode.getLevel() == AttributesTreeModel.CONTEXT_TREE_DEPTH - 1) {
					TreeNode[] path = currentTraversedNode.getPath();
					// String completeName =
					// AttributesTreeModel.translatePathIntoCompleteName ( path
					// );// to do:declare as protected again
					String completeName = AttributesTreeModel
							.translatePathIntoKey(path);// to do:declare as
					// protected again

					ArchivingConfigurationAttribute attr = new ArchivingConfigurationAttribute();
					attr.setCompleteName(completeName);
					attributes.add(attr);
				}
			}
		}

		return attributes;
	}
}
