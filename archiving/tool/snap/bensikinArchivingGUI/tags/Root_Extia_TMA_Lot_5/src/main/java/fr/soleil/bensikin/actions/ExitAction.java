//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/ExitAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ExitAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: ExitAction.java,v $
//Revision 1.2  2006/04/10 08:46:41  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/11/29 18:25:08  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.Action;

import fr.soleil.bensikin.lifecycle.LifeCycleManager;
import fr.soleil.bensikin.lifecycle.LifeCycleManagerFactory;

/**
 * An action used to shut down Bensikin.
 * 
 * @author CLAISSE
 */
public class ExitAction extends BensikinAction {
	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
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
		// System.out.println("ExitAction/actionPerformed");

		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getCurrentImpl();
		lifeCycleManager.applicationClosed(new Hashtable());
	}
}
