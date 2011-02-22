//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsLogsTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsLogsTab.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: OptionsLogsTab.java,v $
// Revision 1.6  2006/06/28 12:49:44  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:37  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.sub.dialogs.options;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * The logs tab of OptionsDialog, used to set the log level of the application.
 * 
 * @author CLAISSE
 */
public class OptionsLogsTab extends JPanel {
	private static OptionsLogsTab instance = null;

	private OperatorsList comboBox;
	private JLabel levelLabel;
	// private Dimension dim = new Dimension(300 , 300);
	private JPanel myPanel;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static OptionsLogsTab getInstance() {
		if (instance == null) {
			instance = new OptionsLogsTab();
		}

		return instance;
	}

	/**
	 * Builds the tab.
	 */
	private OptionsLogsTab() {
		this.initComponents();
		this.addComponents();
		this.initLayout();
	}

	/**
	 * Inits the tab's layout.
	 */
	private void initLayout() {
		myPanel.setLayout(new SpringLayout());

		SpringUtilities.makeCompactGrid(myPanel, 1, 3, // rows, cols
				6, 6, // initX, initY
				6, 6, // xPad, yPad
				true);
	}

	/**
	 * Adds the initialized components to the tab.
	 */
	private void addComponents() {
		Dimension emptyBoxDimension = new Dimension(20, 2);

		myPanel = new JPanel();
		this.add(myPanel);

		// Create and populate the panel.

		// START ROW 1
		myPanel.add(levelLabel);
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(comboBox);
		// END ROW 1

		myPanel.setPreferredSize(new Dimension(300, 60));
	}

	/**
	 * Inits the tab's components.
	 */
	private void initComponents() {
		comboBox = new OperatorsList(OperatorsList.LOGS_TYPE);
		String msgLevelLabel = Messages
				.getMessage("DIALOGS_OPTIONS_LOGS_LEVEL");
		levelLabel = new JLabel(msgLevelLabel);
	}

	/**
	 * @return The selected log level
	 */
	public String getLogLevel() {
		return (String) comboBox.getSelectedItem();
	}

	/**
	 * @return The log level combo box
	 */
	public OperatorsList getComboBox() {
		return comboBox;
	}
}
