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

import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.tools.Messages;

public class VCExpressionAddAction extends AbstractAction {

	private static final long serialVersionUID = -7489756470187025771L;
	private ExpressionTab expressionTab;

	public VCExpressionAddAction(ExpressionTab expressionTab) {
		super();
		super.putValue(Action.NAME, Messages.getMessage("EXPRESSION_ADD"));
		super.putValue(Action.SHORT_DESCRIPTION, Messages
				.getMessage("EXPRESSION_ADD"));
		this.expressionTab = expressionTab;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		expressionTab.addExpression();
	}

}
