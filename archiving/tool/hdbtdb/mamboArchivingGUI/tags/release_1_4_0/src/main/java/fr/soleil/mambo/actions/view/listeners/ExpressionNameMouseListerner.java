package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fr.soleil.mambo.containers.view.dialogs.ExpressionNamesChooser;

public class ExpressionNameMouseListerner extends MouseAdapter
{
    private int reference;

    public ExpressionNameMouseListerner (int _reference)
    {
        super();
        this.reference = _reference;
    }

    public void mouseClicked (MouseEvent e)
    {
        ExpressionNamesChooser.getInstance().setNameIndex(reference);
        ExpressionNamesChooser.getInstance().setVisible(true);
    }
}
