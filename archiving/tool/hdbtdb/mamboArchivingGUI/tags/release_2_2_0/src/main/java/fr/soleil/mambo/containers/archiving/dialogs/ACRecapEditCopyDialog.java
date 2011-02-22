/*	Synchrotron Soleil 
 *  
 *   File          :  ACRecapEditCopyDialog.java
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
 *   Log: ACRecapEditCopyDialog.java,v 
 *
 */
package fr.soleil.mambo.containers.archiving.dialogs;

import javax.swing.JDialog;

import fr.soleil.mambo.containers.archiving.ACRecapEditCopyPanel;

/**
 * 
 * @author SOLEIL
 */
public class ACRecapEditCopyDialog extends JDialog {
	public ACRecapEditCopyDialog() {
		super(ACRecapDialog.getInstance(false), "", true);
		this.setContentPane(new ACRecapEditCopyPanel(this));
		this.setBounds(ACRecapDialog.getInstance(false).getX(), ACRecapDialog
				.getInstance(false).getY(), 500, 300);
	}
}
