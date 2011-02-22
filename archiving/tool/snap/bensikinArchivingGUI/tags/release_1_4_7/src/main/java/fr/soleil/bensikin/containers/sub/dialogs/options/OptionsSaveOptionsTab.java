//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsSaveOptionsTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsSaveOptionsTab.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: OptionsSaveOptionsTab.java,v $
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
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.options.sub.SaveOptions;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


/**
 * The save options tab of OptionsDialog, used to set the save on shudown/load on startup cycle of the application.
 *
 * @author CLAISSE
 */
public class OptionsSaveOptionsTab extends JPanel
{
	private static OptionsSaveOptionsTab instance = null;

	private JRadioButton yesButton;
	private JRadioButton noButton;
	private JLabel label;
	private ButtonGroup buttonGroup;

	//private Dimension dim = new Dimension(300 , 300);
	private JPanel myPanel;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static OptionsSaveOptionsTab getInstance()
	{
		if ( instance == null )
		{
			instance = new OptionsSaveOptionsTab();
		}

		return instance;
	}

	/**
	 * Builds the tab.
	 */
	private OptionsSaveOptionsTab()
	{
		this.initComponents();
		this.addComponents();
		this.initLayout();
	}

	/**
	 * Inits the tab's layout.
	 */
	private void initLayout()
	{
		myPanel.setLayout(new SpringLayout());

		SpringUtilities.makeCompactGrid
		        (myPanel ,
		         3 , 3 , //rows, cols
		         6 , 6 , //initX, initY
		         6 , 6 , //xPad, yPad
		         true);
	}

	/**
	 * Adds the initialized components to the tab.
	 */
	private void addComponents()
	{
		Dimension emptyBoxDimension = new Dimension(20 , 2);

		myPanel = new JPanel();
		this.add(myPanel);

		//Create and populate the panel.

		//START ROW 1
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(yesButton);
		//END ROW 1

		//START ROW 2
		myPanel.add(label);
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		//END ROW 2

		//START ROW 3
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(Box.createRigidArea(emptyBoxDimension));
		myPanel.add(noButton);
		//END ROW 3
		myPanel.setPreferredSize(new Dimension(300 , 60));
		//GUIUtilities.addDebugBorderToPanel ( myPanel , true , Color.RED , 2 );
	}

	/**
	 * Inits the tab's components.
	 */
	private void initComponents()
	{
		buttonGroup = new ButtonGroup();

		String msgSaveHistoryOnShutdown = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN");
		String msgYes = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_YES");
		String msgNo = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_NO");

		label = new JLabel(msgSaveHistoryOnShutdown);

		yesButton = new JRadioButton(msgYes , true);
		noButton = new JRadioButton(msgNo , true);

		yesButton.setActionCommand(String.valueOf(SaveOptions.HISTORY_YES));
		noButton.setActionCommand(String.valueOf(SaveOptions.HISTORY_NO));

		yesButton.setPreferredSize(new Dimension(20 , 10));
		noButton.setPreferredSize(new Dimension(20 , 10));
		//label.setPreferredSize ( new Dimension ( 20 , 10 ) );

		buttonGroup.add(yesButton);
		buttonGroup.add(noButton);

	}

	/**
	 * @return The buttonGroup attribute, containing the Save Yes and Save No JRadioButtons
	 */
	public ButtonGroup getButtonGroup()
	{
		return buttonGroup;
	}

	/**
	 * Selects a save JRadioButton, depending on the hasSave parameter value
	 *
	 * @param hasSave Has to be either HISTORY_YES or HISTORY_NO, otherwise a IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectHasSaveButton(int hasSave) throws IllegalArgumentException
	{
		switch ( hasSave )
		{
			case SaveOptions.HISTORY_YES:
				yesButton.setSelected(true);
				break;

			case SaveOptions.HISTORY_NO:
				noButton.setSelected(true);
				break;

			default:
				throw new IllegalArgumentException();
		}
	}
}
