package fr.soleil.bensikin.components;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
* The mother class of all trees of the application. Implements a method to get the current tree attributes selection.
*
* @author CLAISSE
*/
public class BensikinTree extends JTree
{
	protected BensikinTree()
	{

	}

	/**
     * @param newModel
     */
	protected BensikinTree(TreeModel newModel) 
    {
        super ( newModel );
    }

    // If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandAll(boolean expand)
	{
		TreeNode root = ( TreeNode ) this.getModel().getRoot();

		// Traverse tree from root
		expandAll(new TreePath(root) , expand);
		this.treeDidChange();
	}

	public void collapsePath(TreePath path)
	{
		int count = path.getPathCount();

		if ( count == 1 )
		{
			this.expandAll(false);
		}
		else
		{
			super.collapsePath(path);
		}
	}

	private void expandAll(TreePath parent , boolean expand)
	{
		// Traverse children
		TreeNode node = ( TreeNode ) parent.getLastPathComponent();
		if ( node.getChildCount() >= 0 )
		{
			for ( java.util.Enumeration e = node.children() ; e.hasMoreElements() ; )
			{
				TreeNode n = ( TreeNode ) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				this.expandAll(path , expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if ( expand )
		{
			super.expandPath(parent);
		}
		else
		{
			super.collapsePath(parent);
		}
	}

	public void expandAll1Level(boolean expand)
	{
		TreeNode root = ( TreeNode ) this.getModel().getRoot();

		// Traverse tree from root
		//expandAll( new TreePath(root), expand);
		TreePath parent = new TreePath(root);

		TreeNode node = ( TreeNode ) parent.getLastPathComponent();
		if ( node.getChildCount() >= 0 )
		{
			for ( java.util.Enumeration e = node.children() ; e.hasMoreElements() ; )
			{
				TreeNode n = ( TreeNode ) e.nextElement();
				//TreePath path = 
                parent.pathByAddingChild(n);

				//Expansion or collapse must be done bottom-up
				if ( expand )
				{
					super.expandPath(parent);
				}
				else
				{
					super.collapsePath(parent);
				}
				//-------
			}
		}

		this.treeDidChange();
	}
}
