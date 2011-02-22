package fr.soleil.mambo.actions.view.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;

public class ExpressionNameFieldListener implements DocumentListener {

    private ExpressionTab expressionTab;

    public ExpressionNameFieldListener(ExpressionTab expressionTab) {
        super();
        this.expressionTab = expressionTab;
    }

    private void textChanged(DocumentEvent arg0) {
        if (arg0 != null) {
            Document doc = arg0.getDocument();
            if (doc != null && doc.getLength() > 0) {
                try {
                    if (!"".equals(doc.getText(0, doc.getLength()).trim())) {
                        if (expressionTab.getExpressionTree()
                                .getSelectedAttribute() != null) {
                            expressionTab.getApplyAction().setEnabled(true);
                        }
                    }
                }
                catch (BadLocationException e) {
                    // trace here but should never happen.
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
        // nothing to do
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        textChanged(arg0);
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        textChanged(arg0);
    }

}
