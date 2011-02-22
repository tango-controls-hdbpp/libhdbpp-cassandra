/*	Synchrotron Soleil 
 *  
 *   File          :  ViewCopyAction.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  8 mars 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ViewCopyAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.mambo.containers.view.dialogs.ViewImageTableDialog;

public class ViewImageTableAction extends AbstractAction {
	private ImageData imageData;
	private String displayFormat;

	/**
	 * @param name
	 */
	public ViewImageTableAction(String name, ImageData _imageData,
			String _displayFormat) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);
		this.imageData = _imageData;
		this.displayFormat = _displayFormat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		ViewImageTableDialog dialog = new ViewImageTableDialog(this.imageData,
				displayFormat);

		dialog.pack();
		dialog.setVisible(true);
	}
}
