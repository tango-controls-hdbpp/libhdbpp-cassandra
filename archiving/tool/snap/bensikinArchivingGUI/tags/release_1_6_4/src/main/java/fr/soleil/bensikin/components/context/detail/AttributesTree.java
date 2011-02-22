// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/detail/AttributesTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AttributesTree.
// (Claisse Laurent) - 23 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: AttributesTree.java,v $
// Revision 1.7 2006/11/29 09:57:50 ounsy
// minor changes
//
// Revision 1.6 2006/03/27 14:03:16 ounsy
// now extends BensikinTree instead of JTree
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:34 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.components.context.detail;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.components.BensikinTree;
import fr.soleil.bensikin.components.renderers.BensikinTreeCellRenderer;
import fr.soleil.bensikin.models.AttributesTreeModel;

/**
 * The mother class of all trees of the application. Implements a method to get
 * the current tree attributes selection.
 * 
 * @author CLAISSE
 */
public class AttributesTree extends BensikinTree {
    private static final long serialVersionUID = 1104696426381821108L;

    /**
     * Standard tree building from a model, plus sets the following options:
     * -setExpandsSelectedPaths ( true ) -->expands selected paths
     * -setScrollsOnExpand ( true ) -->scrolls if necessary on expansion
     * -setShowsRootHandles ( true ) --> shows the root handle even if empty
     * -setToggleClickCount ( 1 ) -->one click tree expansion/collapse
     * 
     * @param newModel
     *            The model
     */
    protected AttributesTree(TreeModel newModel) {
        super(newModel);

        this.setExpandsSelectedPaths(true);
        this.setScrollsOnExpand(true);
        this.setShowsRootHandles(true);
        this.setToggleClickCount(1);
        this.setCellRenderer(new BensikinTreeCellRenderer());
    }

    /**
     * Returns a list of all the tree attributes that are under a currently
     * selected node. Warning, since attributes trees are only loaded up to
     * member level until each member is clicked, the list will be empty if no
     * member's attributes list has been loaded yet.
     * 
     * @param remove
     *            If true, removes each of the found nodes from the tree
     * @return A Vector containing TreePath objects, each representing the path
     *         to one of the attributes under one of the selected nodes.
     */
    public Vector<TreePath> getListOfAttributesTreePathUnderSelectedNodes(
            boolean remove) {
        TreePath[] selectedPath = this.getSelectionPaths();
        if (selectedPath == null || selectedPath.length == 0) {
            return null;
        }

        Vector<TreePath> attributes = new Vector<TreePath>();
        for (int i = 0; i < selectedPath.length; i++)
        // as many loops as there are selected nodes
        {
            TreePath currentSelectedTreePath = selectedPath[i];
            DefaultMutableTreeNode currentSelectedNode = (DefaultMutableTreeNode) currentSelectedTreePath
                    .getLastPathComponent();
            // String name = ( String ) currentSelectedNode.getUserObject();

            Enumeration<?> enumer = currentSelectedNode.preorderEnumeration();
            while (enumer.hasMoreElements())
            // for each selected nodes, we loop up all its attribute-level nodes
            {
                DefaultMutableTreeNode currentTraversedNode = (DefaultMutableTreeNode) enumer
                        .nextElement();

                if (currentTraversedNode.getLevel() == AttributesTreeModel.CONTEXT_TREE_DEPTH - 1) {
                    TreeNode[] path = currentTraversedNode.getPath();
                    TreePath toAdd = new TreePath(path);
                    attributes.add(toAdd);
                }
            }

            if (remove) {
                currentSelectedNode.removeFromParent();
            }
        }

        return attributes;
    }
}
