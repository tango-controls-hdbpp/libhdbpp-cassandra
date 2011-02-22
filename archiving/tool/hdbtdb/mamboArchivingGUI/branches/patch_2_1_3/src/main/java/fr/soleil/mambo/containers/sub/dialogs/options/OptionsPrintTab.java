//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsPrintTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsPrintTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: OptionsPrintTab.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
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

import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionsPrintTab extends JPanel {
	private static OptionsPrintTab instance = null;

	/**
	 * @return 8 juil. 2005
	 */
	public static OptionsPrintTab getInstance() {
		if (instance == null) {
			instance = new OptionsPrintTab();
		}

		return instance;
	}

	public static void resetInstance() {
		if (instance != null) {
			instance.repaint();
		}

		instance = null;
	}

	/**
     * 
     */
	private OptionsPrintTab() {
		this.add(new JLabel("Future prints options"));
	}
}
