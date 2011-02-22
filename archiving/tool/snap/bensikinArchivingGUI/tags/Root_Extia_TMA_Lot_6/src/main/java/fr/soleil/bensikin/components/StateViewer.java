/*	Synchrotron Soleil 
 *  
 *   File          :  StateViewer.java
 *  
 *   Project       :  Bensikin_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  16 mars 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: StateViewer.java,v 
 *
 */
package fr.soleil.bensikin.components;

import java.awt.Color;

import javax.swing.JLabel;

import fr.esrf.Tango.DevState;
import fr.esrf.tangoatk.core.IDevice;
import fr.esrf.tangoatk.widget.util.ATKConstant;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoStateTranslation;

public class StateViewer extends JLabel {

	public StateViewer(int stateCode) {
		super();
		this.setText(TangoStateTranslation.getStrStateValue(stateCode));
		this.setToolTipText(this.getText());
		Color color = null;
		color = ATKConstant.getColor4State(this.getText());
		if (color != null) {
			this.setBackground(color);
			this.setOpaque(true);
		} else {
			this.setOpaque(false);
		}
	}
}
