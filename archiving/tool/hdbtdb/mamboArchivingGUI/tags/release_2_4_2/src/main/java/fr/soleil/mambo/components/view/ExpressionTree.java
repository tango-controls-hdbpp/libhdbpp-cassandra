package fr.soleil.mambo.components.view;

import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.actions.view.listeners.ExpressionTreeListener;
import fr.soleil.mambo.components.renderers.ExpressionTreeRenderer;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.models.ExpressionTreeModel;

public class ExpressionTree extends JTree {

    private static final long serialVersionUID = -4290063632376565247L;

    private TreePath[] lastSelectionsPath;
    private TreePath[] currentSelectionsPath;
    private ExpressionTreeListener expressionTreeListener;

    public ExpressionTree(ExpressionTab expressionTab) {
        super();
        setModel(new ExpressionTreeModel());
        setCellRenderer(ExpressionTreeRenderer.getInstance());
        expressionTreeListener = new ExpressionTreeListener(expressionTab);
        addTreeSelectionListener(expressionTreeListener);
        lastSelectionsPath = null;
        currentSelectionsPath = null;
    }

    public Vector<ExpressionAttribute> getLastListOfAttributesToSet() {
        TreePath[] selectedPath = lastSelectionsPath;
        return treePathToVector(selectedPath);
    }

    private Vector<ExpressionAttribute> treePathToVector(TreePath[] selectedPath) {
        if (selectedPath == null || selectedPath.length == 0) {
            return null;
        }

        Vector<ExpressionAttribute> attributes = new Vector<ExpressionAttribute>();
        for (int i = 0; i < selectedPath.length; i++) {
            TreePath currentSelectedTreePath = selectedPath[i];
            if (currentSelectedTreePath.getPathCount() == 1)
                continue;
            DefaultMutableTreeNode currentSelectedNode = (DefaultMutableTreeNode) currentSelectedTreePath
                    .getLastPathComponent();

            if (!currentSelectedNode.isRoot()) {
                ExpressionAttribute attr = getModel().getExpressionAttributes().get(
                        currentSelectedNode.getUserObject());
                if (attr != null)
                    attributes.add(attr);
            }
        }

        return attributes;
    }

    public Vector<ExpressionAttribute> getSelectedAttributes() {
        saveCurrentSelection();
        return treePathToVector(currentSelectionsPath);
    }

    public ExpressionAttribute getSelectedAttribute() {
        TreePath selectedPath = this.getSelectionPath();
        if (selectedPath == null || selectedPath.getPathCount() == 1) {
            return null;
        }
        ExpressionAttribute attr = null;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath
                .getLastPathComponent();
        if (!selectedNode.isRoot()) {
            String attributeName = (String) selectedNode.getUserObject();
            attr = getModel().getExpressionAttribute(attributeName);
        }
        selectedNode = null;
        selectedPath = null;
        return attr;
    }

    public ExpressionAttribute expressionTab() {
        TreePath selectedPath = this.getSelectionPath();
        if (selectedPath == null || selectedPath.getPathCount() == 1) {
            return null;
        }
        ExpressionAttribute attr = null;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath
                .getLastPathComponent();
        if (!selectedNode.isRoot()) {
            String attributeName = (String) selectedNode.getUserObject();
            attr = getModel().getExpressionAttribute(attributeName);
        }
        selectedNode = null;
        selectedPath = null;
        return attr;
    }

    public void saveCurrentSelection() {
        lastSelectionsPath = currentSelectionsPath;
        currentSelectionsPath = this.getSelectionPaths();
    }

    public void reloadSelection() {
        setSelectionPaths(currentSelectionsPath);
        getExpressionTreeListener().treeSelectionChange();
    }

    @Override
    public void setModel(TreeModel model) {
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
