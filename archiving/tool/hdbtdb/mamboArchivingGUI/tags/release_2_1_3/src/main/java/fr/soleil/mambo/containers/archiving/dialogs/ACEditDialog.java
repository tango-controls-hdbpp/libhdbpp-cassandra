// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/dialogs/ACEditDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACEditDialog.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.14 $
//
// $Log: ACEditDialog.java,v $
// Revision 1.14 2007/05/10 14:48:16 ounsy
// possibility to change "no value" String in chart data file (default is "*")
// through vc option
//
// Revision 1.13 2006/10/06 16:02:31 ounsy
// minor changes (L&F)
//
// Revision 1.12 2006/07/18 10:24:24 ounsy
// minor changes
//
// Revision 1.11 2006/06/20 16:02:46 ounsy
// avoids the bug of hdb/tdb information not transfered to attributes properties
// panel
//
// Revision 1.10 2006/05/29 15:45:49 ounsy
// minor changes
//
// Revision 1.9 2006/05/19 13:45:13 ounsy
// minor changes
//
// Revision 1.8 2006/05/16 14:03:31 ounsy
// corrected a bug in alternate/normal mode selection
//
// Revision 1.7 2006/05/16 12:49:41 ounsy
// modified imports
//
// Revision 1.6 2006/05/16 09:36:51 ounsy
// little optimisation
//
// Revision 1.5 2006/02/24 12:25:26 ounsy
// modified for HDB/TDB separation
//
// Revision 1.4 2006/01/24 12:26:38 ounsy
// Bug of the new AC replacing the former selected AC corrected
//
// Revision 1.3 2005/12/15 11:22:37 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:56 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.mambo.actions.CancelAction;
import fr.soleil.mambo.actions.archiving.ACBackAction;
import fr.soleil.mambo.actions.archiving.ACFinishAction;
import fr.soleil.mambo.actions.archiving.ACNextAction;
import fr.soleil.mambo.components.archiving.ACCustomTabbedPane;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.options.sub.GeneralOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.tango.util.entity.bean.AttributeTableSelectionBean;
import fr.soleil.tango.util.entity.datasource.ITangoEntitiesSelectionManager;

public class ACEditDialog extends JDialog {

	private static final long serialVersionUID = 7389543974419666413L;
	private GeneralTab generalTab;
	private AttributesTab attributesTab;
	private AttributeTableSelectionBean attributeTableSelectionBean;
	private AttributesPropertiesTab attributesPropertiesTab;
	private JScrollPane generalScrollPane;
	private JScrollPane attributesScrollPane;
	private JScrollPane attributesPropertiesScrollPane;

	private JPanel myPanel;
	// private JButton okButton;
	private JButton backButton;
	private JButton nextButton;
	private JButton finishButton;
	private JButton cancelButton;
	private ACCustomTabbedPane jTabbedPane;
	private boolean newAC;

	private String msgPrint;

	private boolean isAlternateSelectionMode;
	private boolean isHistoric;

	private static ACEditDialog instance = null;
	private static ITangoEntitiesSelectionManager defaultTangoManager = GeneralOptions.BUFFERED_TANGO_SELECTION_MANAGER;

	/**
	 * @return 8 juil. 2005
	 */
	public static ACEditDialog getInstance(boolean _isAlternateSelectionMode,
			boolean _isHistoric) {
		if (instance == null
				|| instance.isAlternateSelectionMode() != _isAlternateSelectionMode
				|| instance.isHistoric() != _isHistoric) {
			instance = new ACEditDialog(_isAlternateSelectionMode, _isHistoric);
		}

		return instance;
	}

	public static ACEditDialog getInstance() {
		return instance;
	}

	public static void resetInstance() {
		instance = null;
	}

	private ACEditDialog(boolean _isAlternateSelectionMode, boolean _isHistoric) {
		super(MamboFrame.getInstance(), Messages
				.getMessage("DIALOGS_EDIT_AC_TITLE"), true);
		// First, initialize tangoManagerType and selection mode
		this.isAlternateSelectionMode = false;
		this.isHistoric = _isHistoric;
		attributeTableSelectionBean = new AttributeTableSelectionBean();
		attributeTableSelectionBean.setTangoManager(defaultTangoManager);

		this.initComponents();
		this.addComponents();
		this.initLayout();
		this.setSizeAndLocation();

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new ACEditWindowAdapter(this));
		this.newAC = false;

		// Then, update tangoManagerType and selection mode to their expected
		// value, in order to start the bean if necessary
		setAlternateSelectionMode(_isAlternateSelectionMode);
	}

	public void setNewAC(boolean ac) {
		this.newAC = ac;
	}

	public boolean isNewAC() {
		return newAC;
	}

	private class ACEditWindowAdapter extends WindowAdapter {

		private ACEditDialog toClose;

		public ACEditWindowAdapter(ACEditDialog _toClose) {
			this.toClose = _toClose;
		}

		public void windowClosing(WindowEvent e) {
			CancelAction.performCancel(this.toClose);
		}
	}

	/**
	 * 13 sept. 2005
	 */
	private void setSizeAndLocation() {
		pack();
		int x = MamboFrame.getInstance().getX() + 50;
		if (x < 0)
			x = 0;
		int y = MamboFrame.getInstance().getY() + 100;
		Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		if (y < 0 || y + getHeight() > r.y + r.height)
			y = 0;

		this.setLocation(x, y);
	}

	/**
	 * 5 juil. 2005
	 */
	private void initComponents() {
		String msgBack = Messages.getMessage("DIALOGS_EDIT_AC_BACK");
		String msgNext = Messages.getMessage("DIALOGS_EDIT_AC_NEXT");
		String msgFinish = Messages.getMessage("DIALOGS_EDIT_AC_FINISH");
		String msgCancel = Messages.getMessage("DIALOGS_EDIT_AC_CANCEL");

		generalTab = GeneralTab.getInstance();
		generalScrollPane = new JScrollPane(generalTab);
		generalScrollPane
				.setPreferredSize(GUIUtilities.getEditScrollPaneSize());

		// --------CLA
		if (this.isAlternateSelectionMode) {
			attributesScrollPane = new JScrollPane(attributeTableSelectionBean
					.getSelectionPanel());
		} else {
			attributesTab = AttributesTab.getInstance();
			attributesScrollPane = new JScrollPane(attributesTab);
		}
		attributesScrollPane.setPreferredSize(GUIUtilities
				.getEditScrollPaneSize());
		// --------CLA

		AttributesPropertiesTab.resetInstance();
		attributesPropertiesTab = AttributesPropertiesTab
				.getInstance(this.isHistoric);
		attributesPropertiesScrollPane = new JScrollPane(
				attributesPropertiesTab);
		attributesPropertiesScrollPane.setPreferredSize(GUIUtilities
				.getEditScrollPaneSize());

		backButton = new JButton(ACBackAction.getInstance(msgBack));
		nextButton = new JButton(ACNextAction.getInstance(msgNext));
		finishButton = new JButton(ACFinishAction.getInstance(msgFinish));
		cancelButton = new JButton(new CancelAction(msgCancel, this));

		backButton.setPreferredSize(new Dimension(20, 30));
		nextButton.setPreferredSize(new Dimension(20, 30));
		finishButton.setPreferredSize(new Dimension(20, 30));
		cancelButton.setPreferredSize(new Dimension(20, 30));

	}

	/**
	 * 15 juin 2005
	 */
	private void initLayout() {
		Box mainBox = new Box(BoxLayout.Y_AXIS);
		mainBox.add(jTabbedPane);

		mainBox.add(Box.createVerticalStrut(5));

		Box buttonsBox = new Box(BoxLayout.X_AXIS);
		buttonsBox.add(backButton);
		buttonsBox.add(nextButton);
		buttonsBox.add(finishButton);
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

		JScrollPane scrollPane = new JScrollPane(myPanel);
		this.getContentPane().add(scrollPane);
		this.getContentPane().setLayout(new GridLayout());

		jTabbedPane = ACCustomTabbedPane.getInstance();
		jTabbedPane.removeAll();

		String msgDisplayTab = Messages
				.getMessage("DIALOGS_EDIT_AC_GENERAL_TITLE");
		msgPrint = Messages.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_TITLE");
		String msgLogs = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_TITLE");

		jTabbedPane.addTab(msgDisplayTab, generalScrollPane);
		jTabbedPane.addTab(msgPrint, attributesScrollPane);
		jTabbedPane.addTab(msgLogs, attributesPropertiesScrollPane);

		initBackAndNextStatus();
	}

	/**
     * 
     */
	private void initBackAndNextStatus() {
		jTabbedPane.setSelectedIndex(0);
		jTabbedPane.setEnabledAt(1, false);
		jTabbedPane.setEnabledAt(2, false);
		ACBackAction backAction = ACBackAction.getInstance();
		ACNextAction nextAction = ACNextAction.getInstance();
		ACFinishAction finishAction = ACFinishAction.getInstance();
		backAction.setEnabled(false);
		nextAction.setEnabled(true);
		finishAction.setEnabled(false);
	}

	/**
	 * @return Returns the isAlternateSelectionMode.
	 */
	public boolean isAlternateSelectionMode() {
		return isAlternateSelectionMode;
	}

	/**
	 * @param isAlternateSelectionMode
	 *            The isAlternateSelectionMode to set.
	 */
	public void setAlternateSelectionMode(boolean _isAlternateSelectionMode) {
		this.isAlternateSelectionMode = _isAlternateSelectionMode;
		if (this.isAlternateSelectionMode) {
			attributesScrollPane.setViewportView(attributeTableSelectionBean
					.getSelectionPanel());
		} else {
			if (attributesTab == null) {
				attributesTab = AttributesTab.getInstance();
			}
			attributesScrollPane.setViewportView(attributesTab);
		}
		attributesScrollPane.revalidate();
	}

	public void resetTabbedPane() {
		jTabbedPane.setEnabledAt(0, true);
		this.initBackAndNextStatus();
	}

	/**
	 * @return Returns the isHistoric.
	 */
	public boolean isHistoric() {
		return isHistoric;
	}

	public AttributeTableSelectionBean getAttributeTableSelectionBean() {
		return attributeTableSelectionBean;
	}

	public static void setDefaultTangoManager(
			ITangoEntitiesSelectionManager defaultTangoManager) {
		ACEditDialog.defaultTangoManager = defaultTangoManager;
	}

}
