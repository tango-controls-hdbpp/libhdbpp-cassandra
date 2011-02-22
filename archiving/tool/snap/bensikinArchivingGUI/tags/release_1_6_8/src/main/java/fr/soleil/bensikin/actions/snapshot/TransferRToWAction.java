package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailTable;

public class TransferRToWAction extends BensikinAction {

	private SnapshotDetailTable table;

	public TransferRToWAction(String name, SnapshotDetailTable _table) {
		super(name);
		this.putValue(Action.NAME, name);
		this.table = _table;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// be sure to stop editing
		this.table.applyChange();

		// then, edit
		this.table.transferSelectedReadToWrite();
	}

}
