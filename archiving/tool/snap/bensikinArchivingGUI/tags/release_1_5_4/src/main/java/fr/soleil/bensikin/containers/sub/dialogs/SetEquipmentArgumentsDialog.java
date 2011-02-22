package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.snapshot.ValidateSetEquipmentwithCommandAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * A small JDialog to update the comment field of a snapshot. The current value
 * is displayed and can be changed by the user
 * 
 * @author CLAISSE
 */
public class SetEquipmentArgumentsDialog extends CancelableDialog {

	private Dimension dim = new Dimension(300, 300);
	private JPanel myPanel;

	private JLabel commandLabel, optionLabel;
	private JTextField commandText;
	private JComboBox optionsComboList;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Builds the dialog.
	 */
	public SetEquipmentArgumentsDialog() {
		super(BensikinFrame.getInstance(), Messages
				.getMessage("DIALOGS_SET_EQUIPMENTS_WITH_COMMAND"), true);

		this.initComponents();
		this.addComponents();
		this.initLayout();

		this.setSize(dim);
		this.setLocation(400, 400);
	}

	/**
	 * Inits the dialog's components. The comment's value is initialized with
	 * the snapshot's current comment.
	 */
	private void initComponents() {
		SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane
				.getInstance();
		// SnapshotDetailTabbedPaneContent content = (
		// SnapshotDetailTabbedPaneContent ) tabbedPane.getSelectedComponent();
		Snapshot snapshotToUse = ((SnapshotDetailTabbedPaneContent) tabbedPane
				.getSelectedComponent()).getSnapshot();

		commandText = new JTextField();
		commandText.setPreferredSize(new Dimension(200, 20));
		String previousComment = snapshotToUse.getSnapshotData().getComment();
		commandText.setText(previousComment);

		String msgComment = Messages
				.getMessage("DIALOGS_SET_EQUIPMENTS_WITH_COMMAND_CMD_LABEL");
		commandLabel = new JLabel(msgComment, JLabel.TRAILING);

		String msgOption = Messages
				.getMessage("DIALOGS_SET_EQUIPMENTS_WITH_COMMAND_CMD_OPTIONS");
		optionLabel = new JLabel(msgOption, JLabel.TRAILING);

		optionsComboList = new JComboBox();
		optionsComboList.setPreferredSize(new Dimension(200, 20));
		optionsComboList.addItem("STORED_READ_VALUE");
		optionsComboList.addItem("STORED_WRITE_VALUE");

		String msg = Messages
				.getMessage("DIALOGS_SET_EQUIPMENTS_WITH_COMMAND_VALIDATE");
		okButton = new JButton(new ValidateSetEquipmentwithCommandAction(this,
				msg));
		okButton.setPreferredSize(new Dimension(60, 20));

		msg = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_WITH_COMMAND_CANCEL");
		cancelButton = new JButton(new CancelAction(msg, this));
		cancelButton.setPreferredSize(new Dimension(75, 20));
	}

	/**
	 * Inits the dialog's layout.
	 */
	private void initLayout() {
		myPanel.setLayout(new SpringLayout());

		SpringUtilities.makeCompactGrid(myPanel, 3, 4, // rows, cols
				6, 6, // initX, initY
				6, 6, // xPad, yPad
				true);
	}

	/**
	 * Adds the initialized components to the dialog.
	 */
	private void addComponents() {
		Dimension emptyBoxDimension = new Dimension(40, 20);

		myPanel = new JPanel();
		this.getContentPane().add(myPanel);

		// Create and populate the panel.

		// START ROW 1
		myPanel.add(commandLabel);
		myPanel.add(commandText);
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		// END ROW 1

		// START ROW 2
		myPanel.add(optionLabel);
		myPanel.add(optionsComboList);
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		// END ROW 2

		// START ROW 3
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(okButton);
		myPanel.add(cancelButton);
		// END ROW 3
	}

	/**
	 * Returns the command name.
	 */
	public String getCommandName() {

		return commandText == null ? null : commandText.getText();
	}

	/**
	 * @return the selected option
	 */
	public String getOption() {
		return optionsComboList == null ? null : (String) optionsComboList
				.getSelectedItem();
	}

	@Override
	public void cancel() {
		// nothing particular to do
	}
}
