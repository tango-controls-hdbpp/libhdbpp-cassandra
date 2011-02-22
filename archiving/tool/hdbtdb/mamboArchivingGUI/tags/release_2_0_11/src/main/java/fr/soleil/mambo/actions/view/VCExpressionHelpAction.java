// +======================================================================
// $Source$
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCExpressionHelpAction.
// (Girardot Rapha�l) - 7 nov. 2006
//
// $Author$
//
// $Revision:
//
// $Log$
// Revision 1.1 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.dialogs.ExpressionEditDialog;
import fr.soleil.mambo.containers.view.dialogs.ExpressionHelpDialog;
import fr.soleil.mambo.tools.Messages;

public class VCExpressionHelpAction extends AbstractAction {

    private static final long    serialVersionUID = -2343019539858326380L;
    private ExpressionHelpDialog helpDialog;

    public VCExpressionHelpAction(ExpressionEditDialog expressionEditDialog) {
        super();
        this.putValue(Action.NAME, "?");
        this.putValue(Action.SHORT_DESCRIPTION, Messages
                .getMessage("EXPRESSION_SUPPORT_TITLE"));
        helpDialog = new ExpressionHelpDialog(expressionEditDialog);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        helpDialog.setVisible(true);
    }

}
