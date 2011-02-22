/*	Synchrotron Soleil 
 *  
 *   File          :  ImagesListSelectionListener.java
 *  
 *   Project       :  mambo_IMAGES
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  2 mai 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ImagesListSelectionListener.java,v 
 *
 */
/*
 * Created on 2 mai 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.components.view.images;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.mambo.actions.view.ViewImageTableAction;
import fr.soleil.mambo.tools.Messages;

public class ImagesListSelectionListener implements ListSelectionListener {
	private PartialImageDataTableModel model;
	private String viewReadMsg;
	private String displayFormat;

	public ImagesListSelectionListener(PartialImageDataTableModel _model,
			String _displayFormat) {
		super();
		this.model = _model;
		viewReadMsg = Messages.getMessage("VIEW_IMAGE_READ");
		this.displayFormat = _displayFormat;
	}

	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) {
			return;
		}

		ListSelectionModel lsm = (ListSelectionModel) event.getSource();
		if (lsm.isSelectionEmpty()) {
			return;
		} else {
			int selectedRow = lsm.getMinSelectionIndex();
			// System.out.println (
			// "CLA/dqsdqsdsqdqsdq/selectedRow/"+selectedRow );
			ImageData imageData = this.model.getRow(selectedRow);
			ViewImageTableAction viewImageAction = new ViewImageTableAction(
					viewReadMsg, imageData, displayFormat);
			viewImageAction.actionPerformed(null);
		}
	}

}
