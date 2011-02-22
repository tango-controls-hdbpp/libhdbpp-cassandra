package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.bensikin.actions.snapshot.PrintSnapshotDetailAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailPrintTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailPrintPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.tools.Messages;

public class SnapshotDetailPrintDialog extends JDialog {
	private SnapshotDetailPrintTable table;
	private JPanel mainPanel;
	private int mode;
	private JButton printButton;
	private JButton cancelButton;
	private SnapshotDetailPrintPanel printPanel;

	public SnapshotDetailPrintDialog(SnapshotDetailPrintTable table, int mode) {
		super(BensikinFrame.getInstance(), true);
		this.table = table;
		this.mode = mode;
		initialize();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 300));
		SnapshotDetailTabbedPane pane = SnapshotDetailTabbedPane.getInstance();
		if (pane != null) {
			setLocation(pane.getLocationOnScreen());
		}
	}

	private void initialize() {
		String msg = Messages.getMessage("SNAPSHOT_DETAIL_PRINT_TITLE");
		setTitle(msg);
		printPanel = new SnapshotDetailPrintPanel(table, mode);
		String actionTitle = BensikinFrame.getInstance().getTitle() + " - "
				+ msg;
		printButton = new JButton(new PrintSnapshotDetailAction(msg,
				actionTitle, printPanel));
		actionTitle = null;

		msg = Messages.getMessage("SNAPSHOT_DETAIL_PRINT_CHOOSE_CANCEL");
		cancelButton = new JButton(msg);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SnapshotDetailPrintDialog.this.setVisible(false);
			}
		});

		mainPanel = new JPanel();
		mainPanel.setBackground(printPanel.getBackground());

		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints printPanelConstraints = new GridBagConstraints();
		printPanelConstraints.fill = GridBagConstraints.BOTH;
		printPanelConstraints.gridx = 0;
		printPanelConstraints.gridy = 0;
		printPanelConstraints.weightx = 1;
		printPanelConstraints.weighty = 1;
		printPanelConstraints.gridwidth = GridBagConstraints.REMAINDER;

		GridBagConstraints printButtonConstraints = new GridBagConstraints();
		printButtonConstraints.fill = GridBagConstraints.NONE;
		printButtonConstraints.gridx = 0;
		printButtonConstraints.gridy = 1;
		printButtonConstraints.weightx = 0;
		printButtonConstraints.weighty = 0;

		GridBagConstraints glueConstraints = new GridBagConstraints();
		glueConstraints.fill = GridBagConstraints.HORIZONTAL;
		glueConstraints.gridx = 1;
		glueConstraints.gridy = 1;
		glueConstraints.weightx = 1;
		glueConstraints.weighty = 0;

		GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
		cancelButtonConstraints.fill = GridBagConstraints.NONE;
		cancelButtonConstraints.gridx = 2;
		cancelButtonConstraints.gridy = 1;
		cancelButtonConstraints.weightx = 0;
		cancelButtonConstraints.weighty = 0;

		JScrollPane scrollPane = new JScrollPane(printPanel);
		scrollPane.getViewport().setBackground(printPanel.getBackground());
		mainPanel.add(scrollPane, printPanelConstraints);

		mainPanel.add(printButton, printButtonConstraints);

		mainPanel.add(Box.createGlue(), glueConstraints);

		mainPanel.add(cancelButton, cancelButtonConstraints);

		setContentPane(mainPanel);
	}
}
