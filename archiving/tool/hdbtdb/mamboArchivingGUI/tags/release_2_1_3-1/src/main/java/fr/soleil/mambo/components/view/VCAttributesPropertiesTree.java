// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/VCAttributesPropertiesTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCAttributesPropertiesTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCAttributesPropertiesTree.java,v $
// Revision 1.4 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.3 2006/05/16 09:36:27 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:28:12 chinkumo
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
package fr.soleil.mambo.components.view;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.actions.view.listeners.VCAttributesPropertiesTreeSelectionListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.models.AttributesTreeModel;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class VCAttributesPropertiesTree extends AttributesTree {

	private static final long serialVersionUID = 5536881510994949065L;
	private TreePath[] lastSelectionsPath;
	private TreePath[] currentSelectionsPath;

	private HashMap<Integer, TreePath> expandedRowPropTree;
	private VCAttributesPropertiesTreeSelectionListener vcAttributesPropertiesTreeSelectionListener;

	public VCAttributesPropertiesTree(VCAttributesTreeModel newModel,
			ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {
		super(newModel);
		vcAttributesPropertiesTreeSelectionListener = new VCAttributesPropertiesTreeSelectionListener(
				viewConfigurationBean, editDialog);
		addTreeSelectionListener(vcAttributesPropertiesTreeSelectionListener);
		setCellRenderer(new VCTreeRenderer(viewConfigurationBean, true));

		setExpandsSelectedPaths(true);
		setScrollsOnExpand(true);
		setShowsRootHandles(true);
		setToggleClickCount(1);

		lastSelectionsPath = null;
		currentSelectionsPath = null;
	}

	public Vector<ViewConfigurationAttribute> getListOfAttributesToSet() {
		saveLastSelectionPath();
		return treePathToVector(currentSelectionsPath);

	}

	public Vector<ViewConfigurationAttribute> getLastListOfAttributesToSet() {
		return treePathToVector(lastSelectionsPath);
	}

	private Vector<ViewConfigurationAttribute> treePathToVector(
			TreePath[] selectedPath) {
		if ((selectedPath == null) || (selectedPath.length == 0)) {
			return null;
		}

		Vector<ViewConfigurationAttribute> attributes = new Vector<ViewConfigurationAttribute>();
		for (int i = 0; i < selectedPath.length; i++) {
			TreePath currentSelectedTreePath = selectedPath[i];
			DefaultMutableTreeNode currentSelectedNode = (DefaultMutableTreeNode) currentSelectedTreePath
					.getLastPathComponent();

			Enumeration<?> enumeration = currentSelectedNode
					.preorderEnumeration();
			while (enumeration.hasMoreElements()) {
				DefaultMutableTreeNode currentTraversedNode = (DefaultMutableTreeNode) enumeration
						.nextElement();

				if (currentTraversedNode.getLevel() == AttributesTreeModel.CONTEXT_TREE_DEPTH - 1) {
					TreeNode[] path = currentTraversedNode.getPath();
					String completeName = AttributesTreeModel
							.translatePathIntoKey(path);

					ViewConfigurationAttribute attr = new ViewConfigurationAttribute();
					attr.setCompleteName(completeName);
					attributes.add(attr);
				}
			}
		}

		return attributes;
	}

	public void saveLastSelectionPath() {
		lastSelectionsPath = currentSelectionsPath;
		currentSelectionsPath = this.getSelectionPaths();
	}

	public void saveExpandedPath() {
		expandedRowPropTree = new HashMap<Integer, TreePath>();
		for (int i = 0; i < getRowCount(); i++) {
			if (isExpanded(i)) {
				expandedRowPropTree.put(new Integer(i), getPathForRow(i));
			}
		}
	}

	public void openExpandedPath() {
		try {
			for (int i = 0; i < getRowCount(); i++) {
				Iterator<Integer> it = expandedRowPropTree.keySet().iterator();
				int j;
				while (it.hasNext()) {
					j = it.next().intValue();
					if (getPathForRow(i).toString().equals(
							expandedRowPropTree.get(j).toString())) {
						expandRow(i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VCAttributesPropertiesTreeSelectionListener getVcAttributesPropertiesTreeSelectionListener() {
		return vcAttributesPropertiesTreeSelectionListener;
	}

	@Override
	public void setModel(TreeModel newModel) {
		if (newModel instanceof VCAttributesTreeModel) {
			super.setModel(newModel);
		}
	}

	@Override
	public VCAttributesTreeModel getModel() {
		return (VCAttributesTreeModel) super.getModel();
	}

}
