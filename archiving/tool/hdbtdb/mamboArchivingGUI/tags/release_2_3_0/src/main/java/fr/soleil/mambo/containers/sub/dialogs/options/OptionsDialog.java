//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsDialog.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.2  2007/05/10 14:48:16  ounsy
// possibility to change "no value" String in chart data file (default is "*") through vc option
//
// Revision 1.1  2006/10/13 12:45:54  ounsy
// OptionsDialog moved to mambo.containers.sub.dialogs.options instead of mambo.containers.sub.dialogs
//
// Revision 1.5  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.4  2006/02/27 09:34:58  ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.3  2005/12/15 11:27:57  ounsy
// "copy table to clipboard" management
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.sub.dialogs.options;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.CancelAction;
import fr.soleil.mambo.actions.ValidateOptionsAction;
import fr.soleil.mambo.actions.listeners.TabbedPaneListener;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.Messages;

public class OptionsDialog extends JDialog {
	private Dimension dim = new Dimension(300, 400);

	// private OptionsDisplayTab displayTab;
	// private OptionsPrintTab printTab;
	private OptionsLogsTab logsTab;
	private OptionsSaveOptionsTab saveTab;
	// private OptionsWordlistTab wordlistTab;
	private OptionsGeneralTab miscTab;
	private OptionsACTab acTab;
	private OptionsVCTab vcTab;

	private JPanel myPanel;
	private JButton okButton;
	private JButton cancelButton;
	private JTabbedPane jTabbedPane;

	private static OptionsDialog menuDialogInstance = null;

	/**
	 * @return 8 juil. 2005
	 */
	public static OptionsDialog getInstance() {
		if (menuDialogInstance == null) {
			menuDialogInstance = new OptionsDialog();
		}

		return menuDialogInstance;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static void resetInstance() {
		if (menuDialogInstance != null) {
			menuDialogInstance.repaint();
		}

		menuDialogInstance = null;

		OptionsDisplayTab.resetInstance();
		OptionsLogsTab.resetInstance();
		OptionsSaveOptionsTab.resetInstance();
		OptionsACTab.resetInstance();
		OptionsVCTab.resetInstance();
	}

	/**
     * 
     */
	private OptionsDialog() {
		super(MamboFrame.getInstance(), Messages
				.getMessage("DIALOGS_OPTIONS_TITLE"), true);

		this.setSizeAndLocation();

		this.initComponents();
		this.addComponents();
		this.initLayout();
	}

	/**
	 * 13 sept. 2005
	 */
	private void setSizeAndLocation() {
		// Dimension dim = new Dimension( 700 , 700 );
		this.setLocation(100, 100);
	}

	/**
	 * 5 juil. 2005
	 */
	private void initComponents() {
		String msgOk = Messages.getMessage("DIALOGS_OPTIONS_OK");
		String msgCancel = Messages.getMessage("DIALOGS_OPTIONS_CANCEL");

		// displayTab = OptionsDisplayTab.getInstance();
		// printTab = OptionsPrintTab.getInstance();
		logsTab = OptionsLogsTab.getInstance();
		saveTab = OptionsSaveOptionsTab.getInstance();
		// wordlistTab = OptionsWordlistTab.getInstance();
		acTab = OptionsACTab.getInstance();
		vcTab = OptionsVCTab.getInstance();
		miscTab = OptionsGeneralTab.getInstance();

		okButton = new JButton(new ValidateOptionsAction(msgOk));
		cancelButton = new JButton(new CancelAction(msgCancel, this));

		okButton.setPreferredSize(new Dimension(20, 30));
		cancelButton.setPreferredSize(new Dimension(20, 30));

		// this.setDoubleBuffered ( true );
	}

	/**
	 * 15 juin 2005
	 */
	private void initLayout() {
		Box mainBox = new Box(BoxLayout.Y_AXIS);
		mainBox.add(jTabbedPane);
		this.repaint();

		mainBox.add(Box.createVerticalStrut(5));

		Box buttonsBox = new Box(BoxLayout.X_AXIS);
		buttonsBox.add(okButton);
		buttonsBox.add(cancelButton);

		mainBox.add(buttonsBox);

		myPanel.add(mainBox);
	}

	/**
	 * 8 juil. 2005
	 */
	private void addComponents() {
		myPanel = new JPanel();
		myPanel.setLayout(new GridLayout());
		this.getContentPane().add(myPanel);

		jTabbedPane = new JTabbedPane();
		jTabbedPane.setIgnoreRepaint(false);
		jTabbedPane.setMinimumSize(dim);
		jTabbedPane.setDoubleBuffered(true);
		// jTabbedPane.setPreferredSize ( dim );

		// String msgDisplayTab = Messages.getMessage(
		// "DIALOGS_OPTIONS_DISPLAY_TITLE" );
		// String msgPrint = Messages.getMessage( "DIALOGS_OPTIONS_PRINT_TITLE"
		// );
		String msgLogs = Messages.getMessage("DIALOGS_OPTIONS_LOGS_TITLE");
		String msgSave = Messages.getMessage("DIALOGS_OPTIONS_SAVE_TITLE");
		// String msgWordList = Messages.getMessage(
		// "DIALOGS_OPTIONS_WORDLIST_TITLE" );
		String msgMiscList = Messages
				.getMessage("DIALOGS_OPTIONS_GENERAL_TITLE");
		String msgAc = Messages.getMessage("DIALOGS_OPTIONS_AC_TITLE");
		String msgVc = Messages.getMessage("DIALOGS_OPTIONS_VC_TITLE");

		jTabbedPane.addTab(msgLogs, logsTab);
		jTabbedPane.addTab(msgSave, saveTab);
		if (Mambo.hasACs()) {
			jTabbedPane.addTab(msgAc, acTab);
		}
		jTabbedPane.addTab(msgVc, vcTab);
		jTabbedPane.addTab(msgMiscList, miscTab);

		jTabbedPane.addChangeListener(new TabbedPaneListener());
	}
}
