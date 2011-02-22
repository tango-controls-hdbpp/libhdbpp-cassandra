package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;

import fr.soleil.bensikin.containers.sub.dialogs.ImageWriteAttibuteDialog;

public class SnapImageSetAction extends AbstractAction {

	private JTable table;
	private int row;
	private ImageWriteAttibuteDialog dialog;

	public SnapImageSetAction(String name, JTable _table,
			ImageWriteAttibuteDialog _dialog, int _row) {
		this.putValue(Action.NAME, name);
		dialog = _dialog;
		row = _row;
		table = _table;
	}

	public void actionPerformed(ActionEvent e) {
		table.getModel().setValueAt(dialog.getValue(), row, 1);
		dialog.setVisible(false);
	}
}
