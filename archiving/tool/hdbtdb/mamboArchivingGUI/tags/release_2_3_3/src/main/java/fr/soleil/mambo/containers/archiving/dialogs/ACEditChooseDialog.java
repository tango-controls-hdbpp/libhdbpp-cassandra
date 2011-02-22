package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * 
 * @author SOLEIL
 */
public class ACEditChooseDialog extends JDialog {

	private JRadioButton hdb;
	private JRadioButton tdb;
	private static ACEditChooseDialog instance;
	private boolean validated = false;

	private ACEditChooseDialog() {
		super(MamboFrame.getInstance(), Messages
				.getMessage("ARCHIVING_CHOOSE_EDIT_TITLE"), true);
		JPanel panel = new JPanel();
		ButtonGroup dbGroup = new ButtonGroup();
		hdb = new JRadioButton(Messages.getMessage("ARCHIVING_CHOOSE_EDIT_HDB"));
		tdb = new JRadioButton(Messages.getMessage("ARCHIVING_CHOOSE_EDIT_TDB"));
		initializeArchivingChooseEdit(hdb, tdb);

		dbGroup.add(hdb);
		dbGroup.add(tdb);
		JButton okButton = new JButton("ok");
		okButton.setToolTipText("ok");
		okButton.addActionListener(new ACECCloseAction());
		panel.setLayout(new SpringLayout());
		panel.add(hdb);
		panel.add(tdb);
		panel.add(okButton);
		this.setContentPane(panel);
		this.setBounds(0, 400, 300, 130);
		SpringUtilities.makeCompactGrid(panel, 3, 1, // rows, cols
				0, 5, // initX, initY
				0, 5, // xPad, yPad
				true); // every component same size
		GUIUtilities.setObjectBackground(hdb, GUIUtilities.ARCHIVING_COLOR);
		GUIUtilities.setObjectBackground(tdb, GUIUtilities.ARCHIVING_COLOR);
		GUIUtilities.setObjectBackground(panel, GUIUtilities.ARCHIVING_COLOR);
		GUIUtilities.setObjectBackground(this, GUIUtilities.ARCHIVING_COLOR);

		this.setupCloseOperation();
	}

	private void initializeArchivingChooseEdit(JRadioButton hdb,
			JRadioButton tdb) {
		// if HDB is not available
		if (Mambo.isHdbAvailable() == false) {
			hdb.setEnabled(false);
			tdb.setSelected(true);
		}
		// else if the file mambo.properties doesn't exist or the syntax inside
		// is wrong
		else {
			// Behaviour by default:
			hdb.setSelected(true);
			tdb.setSelected(false);
		}
	}

	/**
     * 
     */
	private void setupCloseOperation() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ACEditChooseDialog.getInstance().setValidated(false);
				ACEditChooseDialog.getInstance().setVisible(false);
			}
		});
	}

	public static ACEditChooseDialog getInstance() {
		if (instance == null) {
			instance = new ACEditChooseDialog();
		}
		return instance;
	}

	public boolean isHistoric() {
		return hdb.isSelected();
	}

	/**
	 * @return Returns the validated.
	 */
	public boolean isValidated() {
		return validated;
	}

	/**
	 * @param validated
	 *            The validated to set.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
}

class ACECCloseAction extends AbstractAction {
	public ACECCloseAction() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		ACEditChooseDialog.getInstance().setValidated(true);
		ACEditChooseDialog.getInstance().setVisible(false);
	}
}