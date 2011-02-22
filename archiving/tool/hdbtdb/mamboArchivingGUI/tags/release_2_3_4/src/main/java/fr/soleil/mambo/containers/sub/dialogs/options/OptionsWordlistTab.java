//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsWordlistTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsWordlistTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OptionsWordlistTab.java,v $
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:13  chinkumo
// no message
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import fr.soleil.mambo.options.sub.WordlistOptions;

public class OptionsWordlistTab extends JPanel {
	private static OptionsWordlistTab instance = null;

	JRadioButton soleilWordlist;
	JRadioButton tangoWordlist;
	JRadioButton customWordlist1;
	ButtonGroup buttonGroup;

	/**
	 * @return 8 juil. 2005
	 */
	public static OptionsWordlistTab getInstance() {
		if (instance == null) {
			instance = new OptionsWordlistTab();
		}

		return instance;
	}

	/**
     * 
     */
	private OptionsWordlistTab() {
		buttonGroup = new ButtonGroup();

		// String msg = Messages.getMessage( "DIALOGS_OPTIONS_WORDLIST_SOLEIL"
		// );
		soleilWordlist = new JRadioButton("Soleil wordlist", true);
		soleilWordlist.setActionCommand(String
				.valueOf(WordlistOptions.WORDLIST_SOLEIL));

		// msg = Messages.getMessage( "DIALOGS_OPTIONS_WORDLIST_TANGO" );
		tangoWordlist = new JRadioButton("Tango wordlist", true);
		tangoWordlist.setActionCommand(String
				.valueOf(WordlistOptions.WORDLIST_TANGO));

		// msg = Messages.getMessage( "DIALOGS_OPTIONS_WORDLIST_CUSTOM" );
		customWordlist1 = new JRadioButton("Custom wordlist 1", true);
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
	 * @return 8 juil. 2005
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public JRadioButton getCustomWordlist1() {
		return customWordlist1;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public JRadioButton getSoleilWordlist() {
		return soleilWordlist;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public JRadioButton getTangoWordlist() {
		return tangoWordlist;
	}

	/**
	 * @param val
	 *            20 juil. 2005
	 */
	public void selectWordlistButton(int val) {
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
		}
	}
}
