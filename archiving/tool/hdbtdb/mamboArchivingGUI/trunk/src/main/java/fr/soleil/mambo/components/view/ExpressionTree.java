package fr.soleil.mambo.components.view;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.actions.view.listeners.ExpressionTreeListener;
import fr.soleil.mambo.components.renderers.ExpressionTreeRenderer;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.models.ExpressionTreeModel;

public class ExpressionTree extends JTree {

    private static final long serialVersionUID = -4290063632376565247L;

    private TreePath[] lastSelectionsPath;
    private TreePath[] currentSelectionsPath;
    private final ExpressionTreeListener expressionTreeListener;

    public ExpressionTree(final ExpressionTab expressionTab) {
	super();
	setModel(new ExpressionTreeModel());
	setCellRenderer(ExpressionTreeRenderer.getInstance());
	expressionTreeListener = new ExpressionTreeListener(expressionTab);
	addTreeSelectionListener(expressionTreeListener);
	lastSelectionsPath = null;
	currentSelectionsPath = null;
    }

    public Vector<ExpressionAttribute> getLastListOfAttributesToSet() {
	final TreePath[] selectedPath = lastSelectionsPath;
	return treePathToVector(selectedPath);
    }

    private Vector<ExpressionAttribute> treePathToVector(final TreePath[] selectedPath) {
	if (selectedPath == null || selectedPath.length == 0) {
	    return null;
	}

	final Vector<ExpressionAttribute> attributes = new Vector<ExpressionAttribute>();
	for (final TreePath currentSelectedTreePath : selectedPath) {
	    if (currentSelectedTreePath.getPathCount() == 1) {
		continue;
	    }
	    final DefaultMutableTreeNode currentSelectedNode = (DefaultMutableTreeNode) currentSelectedTreePath
		    .getLastPathComponent();

	    if (!currentSelectedNode.isRoot()) {
		final ExpressionAttribute attr = getModel().getExpressionAttributes().get(
			currentSelectedNode.getUserObject());
		if (attr != null) {
		    attributes.add(attr);
		}
	    }
	}

	return attributes;
    }

    public Vector<ExpressionAttribute> getSelectedAttributes() {
	saveCurrentSelection();
	return treePathToVector(currentSelectionsPath);
    }

    public ExpressionAttribute getSelectedAttribute() {
	TreePath selectedPath = getSelectionPath();
	if (selectedPath == null || selectedPath.getPathCount() == 1) {
	    return null;
	}
	ExpressionAttribute attr = null;
	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
	if (!selectedNode.isRoot()) {
	    final String attributeName = (String) selectedNode.getUserObject();
	    attr = getModel().getExpressionAttribute(attributeName);
	}
	selectedNode = null;
	selectedPath = null;
	return attr;
    }

    public ExpressionAttribute expressionTab() {
	TreePath selectedPath = getSelectionPath();
	if (selectedPath == null || selectedPath.getPathCount() == 1) {
	    return null;
	}
	ExpressionAttribute attr = null;
	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
	if (!selectedNode.isRoot()) {
	    final String attributeName = (String) selectedNode.getUserObject();
	    attr = getModel().getExpressionAttribute(attributeName);
	}
	selectedNode = null;
	selectedPath = null;
	return attr;
    }

    public void saveCurrentSelection() {
	lastSelectionsPath = currentSelectionsPath;
	currentSelectionsPath = getSelectionPaths();
    }

    public void reloadSelection() {
	setSelectionPaths(currentSelectionsPath);
	try {
	    getExpressionTreeListener().treeSelectionChange();
	} catch (final ArchivingException e) {
	    final JPopupMenu popup = new JPopupMenu();
	    popup.add(e.getLocalizedMessage());
	    popup.setVisible(true);
	    e.printStackTrace();
	} catch (final SQLException e) {
	    final JPopupMenu popup = new JPopupMenu();
	    popup.add(e.getLocalizedMessage());
	    popup.setVisible(true);
	    e.printStackTrace();
	}
    }

    @Override
    public void setModel(final TreeModel model) {
	if (model instanceof ExpressionTreeModel) {
	    super.setModel(model);
	}
    }

    @Override
    public ExpressionTreeModel getModel() {
	return (ExpressionTreeModel) super.getModel();
    }

    public ExpressionTreeListener getExpressionTreeListener() {
	return expressionTreeListener;
    }

}
