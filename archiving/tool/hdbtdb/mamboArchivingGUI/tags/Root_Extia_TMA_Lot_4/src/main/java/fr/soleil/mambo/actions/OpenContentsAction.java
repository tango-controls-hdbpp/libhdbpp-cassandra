//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/OpenContentsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenContentsAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: OpenContentsAction.java,v $
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

public class OpenContentsAction extends AbstractAction {
	/**
	 * @param name
	 */
	public OpenContentsAction(String name) {
		this.putValue(Action.NAME, name);
		this.setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {

	}

}
