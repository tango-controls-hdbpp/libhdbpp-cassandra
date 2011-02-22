package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;

public class OpenEditExpressionListener extends MouseAdapter
{
    public OpenEditExpressionListener ()
    {
        super();
    }

    public void mouseClicked (MouseEvent e)
    {
        ExpressionTab.getInstance().editExpression();
    }
}
