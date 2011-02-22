/*	Synchrotron Soleil 
 *  
 *   File          :  BensikinAction.java
 *  
 *   Project       :  bensikinOperator
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  28 mars 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: BensikinAction.java,v 
 *
 */
/*
 * Created on 28 mars 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.roles.IRightsManager;
import fr.soleil.bensikin.actions.roles.RightsManagerFactory;

/**
 * 
 * @author CLAISSE
 */
public class BensikinAction extends AbstractAction {
	private boolean isGranted;

	/**
     * 
     */
	public BensikinAction() {
		super();
		this.setPermission();
	}

	/**
	 * @param arg0
	 */
	public BensikinAction(String arg0) {
		super(arg0);
		this.setPermission();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BensikinAction(String arg0, Icon arg1) {
		super(arg0, arg1);
		this.setPermission();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// do nothing
	}

	/**
     * 
     */
	private void setPermission() {
		boolean _isGranted = true;
		if (Bensikin.isRestricted()) {
			IRightsManager rightsManager = RightsManagerFactory
					.getCurrentImpl();
			_isGranted = rightsManager.isGrantedToOperator(this);
		}

		this.isGranted = _isGranted;
		super.setEnabled(this.isGranted);
	}

	/**
	 * @return Returns the isGranted.
	 */
	public boolean isGranted() {
		return isGranted;
	}

	public void setEnabled(boolean wouldBeEnabled) {
		if (wouldBeEnabled) {
			if (this.isGranted) {
				super.setEnabled(true);
			}
		} else {
			super.setEnabled(false);
		}
	}
}
