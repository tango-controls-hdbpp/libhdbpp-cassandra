/*	Synchrotron Soleil 
 *  
 *   File          :  SnapSpectrumCancelAction.java
 *  
 *   Project       :  Bensikin_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  14 févr. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SnapSpectrumCancelAction.java,v 
 *
 */
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.sub.dialogs.SpectrumWriteValueDialog;

/**
 * 
 * @author SOLEIL
 */
public class SnapSpectrumCancelAction extends BensikinAction {

	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public SnapSpectrumCancelAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		SpectrumWriteValueDialog.getInstance().cancel();
	}

}
