package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailPrintTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailPrintPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.containers.sub.dialogs.SnapshotDetailPrintDialog;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;

public class SnapshotDetailPrintChooseAction extends BensikinAction {

	public SnapshotDetailPrintChooseAction(String _name) {
		super(_name);
		this.putValue(Action.NAME, _name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String[] options = {
				Messages.getMessage("SNAPSHOT_DETAIL_PRINT_CHOOSE_NORMAL"),
				Messages.getMessage("SNAPSHOT_DETAIL_PRINT_CHOOSE_TEXT"),
				Messages.getMessage("SNAPSHOT_DETAIL_PRINT_CHOOSE_CANCEL") };
		int result = JOptionPane.showOptionDialog(BensikinFrame.getInstance(),
				Messages.getMessage("SNAPSHOT_DETAIL_PRINT_CHOOSE_DETAIL"),
				Messages.getMessage("SNAPSHOT_DETAIL_PRINT_CHOOSE_TITLE"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
		switch (result) {
		case JOptionPane.YES_OPTION:
			showPrintDialog(SnapshotDetailPrintPanel.MODE_TABLE);
			break;
		case JOptionPane.NO_OPTION:
			showPrintDialog(SnapshotDetailPrintPanel.MODE_TEXT);
			break;
		default:
			return;
		}
	}

	private void showPrintDialog(int mode) {
		SnapshotDetailTabbedPane pane = SnapshotDetailTabbedPane.getInstance();
		if (pane != null) {
			SnapshotDetailTabbedPaneContent content = pane
					.getSelectedSnapshotDetailTabbedPaneContent();
			if (content != null) {
				Snapshot snapshot = content.getSnapshot();
				SnapshotDetailPrintTable table = new SnapshotDetailPrintTable(
						snapshot);
				new SnapshotDetailPrintDialog(table, mode).setVisible(true);
			}
		}
	}

}
