package fr.soleil.mambo.actions.view.listeners;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;

public class ExpressionTreeListener implements TreeSelectionListener
{
    private static ExpressionTreeListener instance = null;

    public static ExpressionTreeListener getInstance ()
    {
        if (instance == null)
        {
            instance = new ExpressionTreeListener();
        }
        return instance;
    }

    protected ExpressionTreeListener ()
    {
        super();
    }

    public void valueChanged (TreeSelectionEvent e)
    {
        ExpressionTab.getInstance().setParameters(ExpressionTree.getInstance().getSelectedAttribute());
    }
}
