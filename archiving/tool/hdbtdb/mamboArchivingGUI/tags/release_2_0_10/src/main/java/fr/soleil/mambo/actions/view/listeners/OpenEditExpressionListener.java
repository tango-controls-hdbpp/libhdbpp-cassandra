package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;

public class OpenEditExpressionListener extends MouseAdapter {

    private ExpressionTab expressionTab;

    public OpenEditExpressionListener(ExpressionTab expressionTab) {
        super();
        this.expressionTab = expressionTab;
    }

    public void mouseClicked(MouseEvent e) {
        expressionTab.editExpression();
    }
}
