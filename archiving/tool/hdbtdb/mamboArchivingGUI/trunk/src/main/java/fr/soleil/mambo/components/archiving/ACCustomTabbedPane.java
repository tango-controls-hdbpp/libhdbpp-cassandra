//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACCustomTabbedPane.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACCustomTabbedPane.
//						(Claisse Laurent) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: ACCustomTabbedPane.java,v $
// Revision 1.1  2005/11/29 18:27:24  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.archiving;

import javax.swing.JTabbedPane;

import fr.soleil.mambo.actions.archiving.listeners.ACCustomTabbedPaneListener;

public class ACCustomTabbedPane extends JTabbedPane {
	private static ACCustomTabbedPane instance = null;

	/**
	 * @return 8 juil. 2005
	 */
	public static ACCustomTabbedPane getInstance() {
		if (instance == null) {
			instance = new ACCustomTabbedPane();
		}

		return instance;
	}

	/**
     * 
     */
	private ACCustomTabbedPane() {
		super();
		this
				.addPropertyChangeListener(ACCustomTabbedPaneListener
						.getInstance());
	}

	public void setSelectedIndex(int index) {
		int oldValue = this.getSelectedIndex();
		super.setSelectedIndex(index);
		int newValue = this.getSelectedIndex();

		this.firePropertyChange(ACCustomTabbedPaneListener.SELECTED_PAGE,
				oldValue, newValue);
	}
}
