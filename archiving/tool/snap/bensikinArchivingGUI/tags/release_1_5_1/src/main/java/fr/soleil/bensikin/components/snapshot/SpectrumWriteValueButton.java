/*	Synchrotron Soleil 
 *  
 *   File          :  SpectrumWriteValueButton.java
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
 *   Log: SpectrumWriteValueButton.java,v 
 *
 */
package fr.soleil.bensikin.components.snapshot;

import javax.swing.JLabel;
import javax.swing.JTable;

import fr.esrf.Tango.AttrDataFormat;
import fr.soleil.bensikin.containers.sub.dialogs.SpectrumWriteValueDialog;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.tools.Messages;

/**
 * 
 * @author SOLEIL
 */
public class SpectrumWriteValueButton extends JLabel {
    private SnapshotAttributeValue spectrumAttribute;
	private String dialogTitle;
	private JTable table;
	int row,col;
	
	public SpectrumWriteValueButton ( String name, SnapshotAttributeValue value, JTable _table, int _row, int _col )
	{
	    super (Messages.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_VIEW"), JLabel.CENTER);
	    table = _table;
	    row = _row;
	    col = _col;
	    //this.setOpaque(false);
	    switch (value.getDataFormat()) {
	        case AttrDataFormat._SPECTRUM:
	            spectrumAttribute = value;
	            break;
	        default : 
	            spectrumAttribute = null;
	    }
	    dialogTitle = name;
	    setEnabled(spectrumAttribute != null);
	}

	public void actionPerformed() {
	    SpectrumWriteValueDialog.getInstance(true, dialogTitle, spectrumAttribute).setVisible(true);
	    if ( !SpectrumWriteValueDialog.getInstance().isCanceled() )
	    {
	        spectrumAttribute.setSpectrumValue(SpectrumWriteValueDialog.getInstance().getValues());
	        table.setValueAt(spectrumAttribute.getSpectrumValue(), row, col);
	    }
	}

}
