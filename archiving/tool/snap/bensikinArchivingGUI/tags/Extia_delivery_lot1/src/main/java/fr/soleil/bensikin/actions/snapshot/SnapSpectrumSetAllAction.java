/*	Synchrotron Soleil 
 *  
 *   File          :  SnapSpectrumSetAllAction.java
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
 *   Log: SnapSpectrumSetAllAction.java,v 
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
public class SnapSpectrumSetAllAction extends BensikinAction {

	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public SnapSpectrumSetAllAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		SpectrumWriteValueDialog.getInstance().setAll();
	}

}
