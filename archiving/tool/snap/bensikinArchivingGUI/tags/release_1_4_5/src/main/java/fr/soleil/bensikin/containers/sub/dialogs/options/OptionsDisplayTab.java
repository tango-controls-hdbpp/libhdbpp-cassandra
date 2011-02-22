//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsDisplayTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: OptionsDisplayTab.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import fr.soleil.bensikin.options.sub.DisplayOptions;
import fr.soleil.bensikin.tools.Messages;


/**
 * The display tab of OptionsDialog, used to set the look and feel of the application.
 *
 * @author CLAISSE
 */
public class OptionsDisplayTab extends JPanel
{
	private static OptionsDisplayTab instance = null;

	private JRadioButton winPLAF;
	private JRadioButton javaPLAF;
	private ButtonGroup buttonGroup;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static OptionsDisplayTab getInstance()
	{
		if ( instance == null )
		{
			instance = new OptionsDisplayTab();
		}

		return instance;
	}

	/**
	 * Builds the tab.
	 */
	private OptionsDisplayTab()
	{
		buttonGroup = new ButtonGroup();

		String msg = Messages.getMessage("DIALOGS_OPTIONS_DISPLAY_WINDOWS");
		winPLAF = new JRadioButton(msg , true);
		winPLAF.setActionCommand(String.valueOf(DisplayOptions.PLAF_WIN));
		msg = Messages.getMessage("DIALOGS_OPTIONS_DISPLAY_JAVA");
		javaPLAF = new JRadioButton(msg , false);
		javaPLAF.setActionCommand(String.valueOf(DisplayOptions.PLAF_JAVA));
		buttonGroup.add(winPLAF);
		buttonGroup.add(javaPLAF);

		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(winPLAF);
		box.add(Box.createVerticalStrut(5));
		box.add(Box.createVerticalGlue());
		box.add(javaPLAF);

		this.add(box);
	}


	/**
	 * @return The buttonGroup attribute, containing the JAVA and Windows JRadioButtons
	 */
	public ButtonGroup getButtonGroup()
	{
		return buttonGroup;
	}

	/**
	 * @return The JAVA JRadioButton
	 */
	public JRadioButton getJavaPLAF()
	{
		return javaPLAF;
	}

	/**
	 * @return The Windows JRadioButton
	 */
	public JRadioButton getWinPLAF()
	{
		return winPLAF;
	}


	/**
	 * Selects a look and feel JRadioButton, depending on the plaf parameter value
	 *
	 * @param plaf Has to be either PLAF_WIN or PLAF_JAVA, otherwise a IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectPlafButton(int plaf) throws IllegalArgumentException
	{
		switch ( plaf )
		{
			case DisplayOptions.PLAF_WIN:
				winPLAF.setSelected(true);
				break;

			case DisplayOptions.PLAF_JAVA:
				javaPLAF.setSelected(true);
				break;

			default:
				throw new IllegalArgumentException();
		}
	}
}
