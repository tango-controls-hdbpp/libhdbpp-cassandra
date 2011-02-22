package fr.soleil.mambo.components.renderers;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import fr.soleil.mambo.Mambo;

public class ExpressionTreeRenderer extends DefaultTreeCellRenderer
{
    private static ExpressionTreeRenderer instance = null;
    protected final static ImageIcon expressionIcon = new ImageIcon(Mambo.class.getResource("icons/expression.png"));

    public static ExpressionTreeRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new ExpressionTreeRenderer();
        }
        return instance;
    }

    protected ExpressionTreeRenderer ()
    {
        super();
    }

    public Component getTreeCellRendererComponent ( JTree tree ,
            Object value ,
            boolean sel ,
            boolean expanded ,
            boolean leaf ,
            int row ,
            boolean hasFocus )
    {
        Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (comp instanceof JLabel && !((DefaultMutableTreeNode)value).isRoot())
        {
            ((JLabel)comp).setIcon(expressionIcon);
        }
        return comp;
    }

}
