//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/OpenOptionsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenOptionsAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OpenOptionsAction.java,v $
// Revision 1.3  2006/10/13 12:45:54  ounsy
// OptionsDialog moved to mambo.containers.sub.dialogs.options instead of mambo.containers.sub.dialogs
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.sub.dialogs.options.OptionsDialog;

public class OpenOptionsAction extends AbstractAction {
	/**
	 * @param name
	 */
	public OpenOptionsAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		OptionsDialog menuDialog = OptionsDialog.getInstance();
		menuDialog.pack();
		menuDialog.repaint();
		menuDialog.setVisible(true);
	}

}
