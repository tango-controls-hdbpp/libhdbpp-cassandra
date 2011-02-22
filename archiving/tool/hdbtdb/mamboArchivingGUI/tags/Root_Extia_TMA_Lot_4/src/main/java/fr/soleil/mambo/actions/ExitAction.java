//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/ExitAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ExitAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ExitAction.java,v $
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
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.lifecycle.LifeCycleManager;
import fr.soleil.mambo.lifecycle.LifeCycleManagerFactory;

public class ExitAction extends AbstractAction {
	/**
	 * @param name
	 */
	public ExitAction(String name) {
		super(name);
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// System.out.println ( "ExitAction/actionPerformed" );

		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getCurrentImpl();
		lifeCycleManager.applicationClosed(new Hashtable());
	}
}
