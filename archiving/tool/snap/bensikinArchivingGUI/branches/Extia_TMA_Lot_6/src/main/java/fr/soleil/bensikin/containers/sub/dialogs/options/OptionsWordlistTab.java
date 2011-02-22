//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsWordlistTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsWordlistTab.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: OptionsWordlistTab.java,v $
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import fr.soleil.bensikin.options.sub.WordlistOptions;
import fr.soleil.bensikin.tools.Messages;

/**
 * The wordlist tab of OptionsDialog, used to set the wordlist of the
 * application. Not used yet.
 * 
 * @author CLAISSE
 */
public class OptionsWordlistTab extends JPanel {
	private static OptionsWordlistTab instance = null;

	JRadioButton soleilWordlist;
	JRadioButton tangoWordlist;
	JRadioButton customWordlist1;
	ButtonGroup buttonGroup;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static OptionsWordlistTab getInstance() {
		if (instance == null) {
			instance = new OptionsWordlistTab();
		}

		return instance;
	}

	/**
	 * Builds the tab.
	 */
	private OptionsWordlistTab() {
		buttonGroup = new ButtonGroup();

		String msg = Messages.getMessage("DIALOGS_OPTIONS_WORDLIST_SOLEIL");
		soleilWordlist = new JRadioButton(msg, true);
		soleilWordlist.setActionCommand(String
				.valueOf(WordlistOptions.WORDLIST_SOLEIL));

		msg = Messages.getMessage("DIALOGS_OPTIONS_WORDLIST_TANGO");
		tangoWordlist = new JRadioButton(msg, true);
		tangoWordlist.setActionCommand(String
				.valueOf(WordlistOptions.WORDLIST_TANGO));

		msg = Messages.getMessage("DIALOGS_OPTIONS_WORDLIST_CUSTOM");
		customWordlist1 = new JRadioButton(msg, true);
		customWordlist1.setActionCommand(String
				.valueOf(WordlistOptions.WORDLIST_CUSTOM));

		buttonGroup.add(soleilWordlist);
		buttonGroup.add(tangoWordlist);
		buttonGroup.add(customWordlist1);

		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(soleilWordlist);
		box.add(Box.createVerticalStrut(5));
		box.add(Box.createVerticalGlue());
		box.add(tangoWordlist);
		box.add(Box.createVerticalStrut(5));
		box.add(Box.createVerticalGlue());
		box.add(customWordlist1);

		this.add(box);
	}

	/**
	 * Selects a wordlist JRadioButton, depending on the val parameter value
	 * 
	 * @param val
	 *            Has to be either WORDLIST_SOLEIL, WORDLIST_TANGO,or
	 *            SNAPSHOT_COMPARE_SHOW_DIFF_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectWordlistButton(int val) throws IllegalArgumentException {
		switch (val) {
		case WordlistOptions.WORDLIST_SOLEIL:
			soleilWordlist.setSelected(true);
			break;

		case WordlistOptions.WORDLIST_TANGO:
			tangoWordlist.setSelected(true);
			break;

		case WordlistOptions.WORDLIST_CUSTOM:
			customWordlist1.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @return Returns the buttonGroup.
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @param buttonGroup
	 *            The buttonGroup to set.
	 */
	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	/**
	 * @return Returns the customWordlist1.
	 */
	public JRadioButton getCustomWordlist1() {
		return customWordlist1;
	}

	/**
	 * @param customWordlist1
	 *            The customWordlist1 to set.
	 */
	public void setCustomWordlist1(JRadioButton customWordlist1) {
		this.customWordlist1 = customWordlist1;
	}

	/**
	 * @return Returns the soleilWordlist.
	 */
	public JRadioButton getSoleilWordlist() {
		return soleilWordlist;
	}

	/**
	 * @param soleilWordlist
	 *            The soleilWordlist to set.
	 */
	public void setSoleilWordlist(JRadioButton soleilWordlist) {
		this.soleilWordlist = soleilWordlist;
	}

	/**
	 * @return Returns the tangoWordlist.
	 */
	public JRadioButton getTangoWordlist() {
		return tangoWordlist;
	}

	/**
	 * @param tangoWordlist
	 *            The tangoWordlist to set.
	 */
	public void setTangoWordlist(JRadioButton tangoWordlist) {
		this.tangoWordlist = tangoWordlist;
	}
}
