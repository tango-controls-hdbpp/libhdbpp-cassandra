// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/OptionsDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OptionsDialog.
// (Claisse Laurent) - 16 juin 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.2  2009/12/17 10:50:38  pierrejoseph
// CheckStyle:  Organize imports / Format
//
// Revision 1.1  2009/10/21 11:33:31  soleilarc
// * Rapha�l GIRARDOT changes: CancelAction really cancels something
//
// Revision 1.8 2007/08/22 14:47:24 ounsy
// new print system
//
// Revision 1.7 2006/06/28 12:48:39 ounsy
// minor changes
//
// Revision 1.6 2005/12/14 16:27:56 ounsy
// added Context tab
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.sub.dialogs.options;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.ValidateOptionsAction;
import fr.soleil.bensikin.actions.listeners.TabbedPaneListener;
import fr.soleil.bensikin.components.BensikinToolbar;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.sub.dialogs.CancelableDialog;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.tools.Messages;

/**
 * A JDialog where the users sets the application's options
 * 
 * @author CLAISSE
 */
public class OptionsDialog extends CancelableDialog {

	private Dimension dim = new Dimension(450, 600);

	// private OptionsDisplayTab displayTab;
	private OptionsPrintTab printTab;
	private OptionsLogsTab logsTab;
	private OptionsSaveOptionsTab saveTab;
	// private OptionsWordlistTab wordlistTab;
	private OptionsSnapshotTab snapshotTab;
	private OptionsContextTab contextTab;

	private JPanel myPanel;
	private JButton okButton;
	private JButton cancelButton;
	private JTabbedPane jTabbedPane;

	private static OptionsDialog menuDialogInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static OptionsDialog getInstance() {
		if (menuDialogInstance == null) {
			menuDialogInstance = new OptionsDialog();
		}

		return menuDialogInstance;
	}

	/**
	 * Forces the current instance to null. Useful for immediate refresh when
	 * the user changed the look and feel.
	 */
	public static void resetInstance() {
		menuDialogInstance = null;
	}

	/**
	 * Builds the dialog.
	 */
	private OptionsDialog() {
		super(BensikinFrame.getInstance(), Messages
				.getMessage("DIALOGS_OPTIONS_TITLE"), true);

		this.setSizeAndLocation();
		this.initComponents();
		this.addComponents();
		this.initLayout();
	}

	/**
	 * Sets the size and location of the dialog.
	 */
	private void setSizeAndLocation() {
		this.setSize(dim);
		this.setLocationRelativeTo(BensikinToolbar.getInstance());
	}

	/**
	 * Inits the dialog's components.
	 */
	private void initComponents() {
		String msgOk = Messages.getMessage("DIALOGS_OPTIONS_OK");
		String msgCancel = Messages.getMessage("DIALOGS_OPTIONS_CANCEL");

		// displayTab = OptionsDisplayTab.getInstance();
		printTab = OptionsPrintTab.getInstance();
		logsTab = OptionsLogsTab.getInstance();
		saveTab = OptionsSaveOptionsTab.getInstance();
		// wordlistTab = OptionsWordlistTab.getInstance();
		snapshotTab = OptionsSnapshotTab.getInstance();
		contextTab = OptionsContextTab.getInstance();

		okButton = new JButton(new ValidateOptionsAction(msgOk));
		cancelButton = new JButton(new CancelAction(msgCancel, this));
	}

	/**
	 * Inits the dialog's layout.
	 */
	private void initLayout() {
		Insets buttonInsets = new Insets(5, 5, 5, 5);
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints okConstraints = new GridBagConstraints();
		okConstraints.fill = GridBagConstraints.NONE;
		okConstraints.gridx = 0;
		okConstraints.gridy = 0;
		okConstraints.weightx = 0;
		okConstraints.weighty = 0;
		okConstraints.insets = buttonInsets;
		okConstraints.anchor = GridBagConstraints.WEST;
		buttonPanel.add(okButton, okConstraints);
		GridBagConstraints glueConstraints = new GridBagConstraints();
		glueConstraints.fill = GridBagConstraints.HORIZONTAL;
		glueConstraints.gridx = 1;
		glueConstraints.gridy = 0;
		glueConstraints.weightx = 1;
		glueConstraints.weighty = 0;
		glueConstraints.anchor = GridBagConstraints.EAST;
		buttonPanel.add(Box.createGlue(), glueConstraints);
		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.fill = GridBagConstraints.NONE;
		cancelConstraints.gridx = 2;
		cancelConstraints.gridy = 0;
		cancelConstraints.weightx = 0;
		cancelConstraints.weighty = 0;
		cancelConstraints.insets = buttonInsets;
		cancelConstraints.anchor = GridBagConstraints.EAST;
		buttonPanel.add(cancelButton, cancelConstraints);

		GridBagConstraints tabbedPaneConstraints = new GridBagConstraints();
		tabbedPaneConstraints.fill = GridBagConstraints.BOTH;
		tabbedPaneConstraints.gridx = 0;
		tabbedPaneConstraints.gridy = 0;
		tabbedPaneConstraints.weightx = 1;
		tabbedPaneConstraints.weighty = 1;
		myPanel.add(jTabbedPane, tabbedPaneConstraints);
		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 1;
		buttonConstraints.weightx = 1;
		buttonConstraints.weighty = 0;
		myPanel.add(buttonPanel, buttonConstraints);
		buttonPanel.revalidate();
	}

	/**
	 * Adds the initialized components to the dialog.
	 */
	private void addComponents() {
		myPanel = new JPanel(new GridBagLayout());
		this.getContentPane().add(myPanel);

		jTabbedPane = new JTabbedPane();
		jTabbedPane.setMinimumSize(dim);
		jTabbedPane.setPreferredSize(dim);

		// String msgDisplayTab = Messages.getMessage(
		// "DIALOGS_OPTIONS_DISPLAY_TITLE" );
		String msgPrint = Messages.getMessage("DIALOGS_OPTIONS_PRINT_TITLE");
		String msgLogs = Messages.getMessage("DIALOGS_OPTIONS_LOGS_TITLE");
		String msgSave = Messages.getMessage("DIALOGS_OPTIONS_SAVE_TITLE");
		// String msgWordList = Messages.getMessage(
		// "DIALOGS_OPTIONS_WORDLIST_TITLE" );
		String msgSnapshotList = Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_TITLE");
		String msgContextList = Messages
				.getMessage("DIALOGS_OPTIONS_CONTEXT_TITLE");

		// jTabbedPane.addTab( msgDisplayTab , displayTab );
		jTabbedPane.addTab(msgLogs, logsTab);
		jTabbedPane.addTab(msgSave, saveTab);
		jTabbedPane.addTab(msgSnapshotList, snapshotTab);
		jTabbedPane.addTab(msgContextList, contextTab);
		jTabbedPane.addTab(msgPrint, printTab);
		jTabbedPane.addChangeListener(new TabbedPaneListener());
	}

	@Override
	public void cancel() {
		try {
			Options.getInstance().push();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
