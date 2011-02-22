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

import fr.soleil.comete.viewController.tango.TangoStateBehavior;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoStateTranslation;

public class StateViewer extends JLabel {

    private static final long serialVersionUID = 7753625163588396224L;

    public StateViewer(int stateCode) {
        super();
        this.setText(TangoStateTranslation.getStrStateValue(stateCode));
        this.setToolTipText(this.getText());
        Color color = null;
        // XXX en attendant correction dans TangoDAO
        new TangoStateBehavior();
        CometeColor cometeColor = TangoStateBehavior.getColor(this.getText());
        if (cometeColor != null) {
            color = cometeColor.getAwtColor();
        }
        cometeColor = null;
        if (color != null) {
            this.setBackground(color);
            this.setOpaque(true);
        }
        else {
            this.setOpaque(false);
        }
    }
}
