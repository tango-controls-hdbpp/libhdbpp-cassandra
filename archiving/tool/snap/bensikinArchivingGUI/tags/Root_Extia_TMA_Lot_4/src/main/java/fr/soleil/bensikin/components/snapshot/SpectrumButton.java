/*	Synchrotron Soleil 
 *  
 *   File          :  SpectrumButton.java
 *  
 *   Project       :  Bensikin_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  3 févr. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SpectrumButton.java,v 
 *
 */
package fr.soleil.bensikin.components.snapshot;

import javax.swing.JLabel;

import fr.esrf.Tango.AttrDataFormat;
import fr.soleil.bensikin.containers.sub.dialogs.SpectrumAttributeDialog;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.tools.Messages;

/**
 * 
 * @author SOLEIL
 */
public class SpectrumButton extends JLabel {
	private SnapshotAttributeValue spectrumAttribute;
	private String dialogTitle;

	public SpectrumButton(String name, SnapshotAttributeValue value) {
		super(Messages.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_VIEW"),
				JLabel.CENTER);
		// this.setOpaque(false);
		switch (value.getDataFormat()) {
		case AttrDataFormat._SPECTRUM:
			spectrumAttribute = value;
			break;
		default:
			spectrumAttribute = null;
		}
		dialogTitle = name;
		setEnabled(spectrumAttribute != null);
	}

	public void actionPerformed() {
		new SpectrumAttributeDialog(dialogTitle, spectrumAttribute)
				.setVisible(true);
	}
}
