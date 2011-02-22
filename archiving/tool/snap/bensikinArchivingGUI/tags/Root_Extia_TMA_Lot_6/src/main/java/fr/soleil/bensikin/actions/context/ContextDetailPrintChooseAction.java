package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.sub.dialogs.ContextDetailPrintDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.tools.Messages;

public class ContextDetailPrintChooseAction extends BensikinAction {

	public ContextDetailPrintChooseAction(String _name) {
		super(_name);
		this.putValue(Action.NAME, _name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String[] options = {
				Messages.getMessage("CONTEXT_DETAIL_PRINT_CHOOSE_NORMAL"),
				Messages.getMessage("CONTEXT_DETAIL_PRINT_CHOOSE_TEXT"),
				Messages.getMessage("CONTEXT_DETAIL_PRINT_CHOOSE_CANCEL") };
		int result = JOptionPane.showOptionDialog(BensikinFrame.getInstance(),
				Messages.getMessage("CONTEXT_DETAIL_PRINT_CHOOSE_DETAIL"),
				Messages.getMessage("CONTEXT_DETAIL_PRINT_CHOOSE_TITLE"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
		switch (result) {
		case JOptionPane.YES_OPTION:
			showPrintDialog(false);
			break;
		case JOptionPane.NO_OPTION:
			showPrintDialog(true);
			break;
		default:
			return;
		}
	}

	private void showPrintDialog(boolean modeText) {
		Context context = Context.getSelectedContext();
		if (context != null) {
			new ContextDetailPrintDialog(context, modeText).setVisible(true);
		}
	}

}
