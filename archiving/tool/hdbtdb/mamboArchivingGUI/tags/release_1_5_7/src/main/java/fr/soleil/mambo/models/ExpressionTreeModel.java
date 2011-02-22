package fr.soleil.mambo.models;

import java.text.Collator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.tools.Messages;

public class ExpressionTreeModel extends DefaultTreeModel
{

    private static ExpressionTreeModel instance = null;
    protected TreeMap<String, ExpressionAttribute> expressionAttributes;
    protected DefaultMutableTreeNode rootNode;

    public static ExpressionTreeModel getInstance()
    {
        if (instance == null)
        {
            instance = new ExpressionTreeModel();
        }
        return instance;
    }

    protected ExpressionTreeModel ()
    {
        super(new DefaultMutableTreeNode( Messages.getMessage("EXPRESSION_ATTRIBUTES") ));
        rootNode = (DefaultMutableTreeNode)this.getRoot();
        setRoot(rootNode);
        expressionAttributes = new TreeMap<String, ExpressionAttribute>(Collator.getInstance(Locale.FRENCH));
    }

    public boolean addAttribute(ExpressionAttribute attribute)
    {
        if (expressionAttributes.containsKey(attribute.getName()))
        {
            return false;
        }
        else
        {
            expressionAttributes.put(attribute.getName(), attribute);
            this.build();
            return true;
        }
    }

    public ExpressionAttribute removeAttribute(String attributeName)
    {
        DefaultMutableTreeNode node = null;
        for (int i = 0; i < rootNode.getChildCount(); i++)
        {
            if ( attributeName.equals( ((DefaultMutableTreeNode)rootNode.getChildAt(i)).getUserObject() ) )
            {
                node = (DefaultMutableTreeNode)rootNode.getChildAt(i);
                break;
            }
        }
        if (node != null) this.removeNodeFromParent(node);
        node = null;
        this.reload();
        return expressionAttributes.remove(attributeName);
    }

    public boolean updateAttribute(String formerName, ExpressionAttribute attribute)
    {
        if (expressionAttributes.containsKey(formerName))
        {
            if (attribute.getName().equals(formerName) || !expressionAttributes.containsKey(attribute.getName()) )
            {
                removeAttribute(formerName);
                return addAttribute(attribute);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public TreeMap<String, ExpressionAttribute> getExpressionAttributes ()
    {
        return expressionAttributes;
    }

    public void setExpressionAttributes (TreeMap expressionAttributes)
    {
        this.expressionAttributes = expressionAttributes;
        this.build();
    }

    public ExpressionAttribute getExpressionAttribute (String attributeName)
    {
        return expressionAttributes.get(attributeName);
    }

    protected void build()
    {
        rootNode.removeAllChildren();
        Set<String> keySet = expressionAttributes.keySet();
        Vector<String> vect = new Vector<String>();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext())
        {
            String key = keyIterator.next();
            vect.add(key);
            int index = vect.indexOf( key );
            this.insertNodeInto( new DefaultMutableTreeNode(key) , rootNode , index );
        }
        vect.clear();
        vect = null;
        keyIterator = null;
        keySet = null;
        this.reload();
    }
}
