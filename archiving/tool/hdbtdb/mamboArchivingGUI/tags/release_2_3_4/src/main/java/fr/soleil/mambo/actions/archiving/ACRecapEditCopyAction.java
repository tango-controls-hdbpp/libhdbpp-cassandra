/*	Synchrotron Soleil 
 *  
 *   File          :  ACRecapEditCopyAction.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  8 déc. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ACRecapEditCopyAction.java,v 
 *
 */
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.archiving.dialogs.ACRecapEditCopyDialog;

/**
 * 
 * @author SOLEIL
 */
public class ACRecapEditCopyAction extends AbstractAction {
	private static ACRecapEditCopyAction instance = null;

	/**
	 * @return
	 */
	public static ACRecapEditCopyAction getInstance(String name) {
		if (instance == null) {
			instance = new ACRecapEditCopyAction(name);
		}

		return instance;
	}

	public static ACRecapEditCopyAction getInstance() {
		return instance;
	}

	/**
	 * @param name
	 */
	private ACRecapEditCopyAction(String name) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		new ACRecapEditCopyDialog().setVisible(true);
	}
}
