//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/AttributesTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesTree.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: AttributesTree.java,v $
// Revision 1.5  2006/06/28 12:27:37  ounsy
// expandAll ( TreePath parent , boolean expand ) becomes public
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 09:35:45  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.models.AttributesTreeModel;


public class AttributesTree extends JTree
{
    /**
     * @param newModel
     */
    protected AttributesTree ( TreeModel newModel )
    {
        super( newModel );
        //this.addTreeWillExpandListener( new TreeWillExpandListenerImpl () );
        //this.addTreeExpansionListener ( new TreeExpansionListenerImpl () );
    }


    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll ( boolean expand )
    {
        TreeNode root = ( TreeNode ) this.getModel().getRoot();

        // Traverse tree from root
        expandAll( new TreePath( root ) , expand );
        this.treeDidChange();
    }

    public void collapsePath ( TreePath path )
    {
        int count = path.getPathCount();

        if ( count == 1 )
        {
            this.expandAll( false );
        }
        else
        {
            super.collapsePath( path );
        }
    }

    public void expandAll ( TreePath parent , boolean expand )
    {
        // Traverse children
        TreeNode node = ( TreeNode ) parent.getLastPathComponent();
        if ( node.getChildCount() >= 0 )
        {
            java.util.Enumeration e = node.children();
            while ( e.hasMoreElements() )
            {
                TreeNode n = ( TreeNode ) e.nextElement();
                TreePath path = parent.pathByAddingChild( n );
                this.expandAll( path , expand );
            }
            e = null;
        }

        // Expansion or collapse must be done bottom-up
        if ( expand )
        {
            super.expandPath( parent );
        }
        else
        {
            super.collapsePath( parent );
        }
    }

    public void expandAll1Level ( boolean expand )
    {
        TreeNode root = ( TreeNode ) this.getModel().getRoot();

        // Traverse tree from root
        //expandAll( new TreePath(root), expand);
        TreePath parent = new TreePath( root );

        TreeNode node = ( TreeNode ) parent.getLastPathComponent();
        if ( node.getChildCount() >= 0 )
        {
            for ( java.util.Enumeration e = node.children() ; e.hasMoreElements() ; )
            {
                TreeNode n = ( TreeNode ) e.nextElement();
                //TreePath path = 
                parent.pathByAddingChild( n );

                //Expansion or collapse must be done bottom-up
                if ( expand )
                {
                    super.expandPath( parent );
                }
                else
                {
                    super.collapsePath( parent );
                }
                //-------
            }
        }

        this.treeDidChange();
    }

    /**
     * Returns a list of all the tree attributes that are under a currently selected node.
     * Warning, since attributes trees are only loaded up to member level until each member is clicked, the list will be empty if no member's attributes list has been loaded yet.
     *
     * @param remove If true, removes each of the found nodes from the tree
     * @return A Vector containing TreePath objects, each representing the path to one of the attributes under one
     *         of the selected nodes.
     */
    public Vector getListOfAttributesTreePathUnderSelectedNodes ( boolean remove )
    {
        TreePath[] selectedPath = this.getSelectionPaths();
        if ( selectedPath == null || selectedPath.length == 0 )
        {
            return null;
        }

        Vector attributes = new Vector();
        for ( int i = 0 ; i < selectedPath.length ; i++ )
                //as many loops as there are selected nodes
        {
            TreePath currentSelectedTreePath = selectedPath[ i ];
            DefaultMutableTreeNode currentSelectedNode = ( DefaultMutableTreeNode ) currentSelectedTreePath.getLastPathComponent();
            //String name = ( String ) currentSelectedNode.getUserObject();

            Enumeration enumeration = currentSelectedNode.preorderEnumeration();
            while ( enumeration.hasMoreElements() )
                    //for each selected nodes, we loop up all its attribute-level nodes
            {
                DefaultMutableTreeNode currentTraversedNode = ( DefaultMutableTreeNode ) enumeration.nextElement();

                if ( currentTraversedNode.getLevel() == AttributesTreeModel.CONTEXT_TREE_DEPTH - 1 )
                {
                    TreeNode[] path = currentTraversedNode.getPath();
                    TreePath toAdd = new TreePath( path );
                    attributes.add( toAdd );
                }
            }

            if ( remove )
            {
                currentSelectedNode.removeFromParent();
            }
        }

        return attributes;
    }

}
