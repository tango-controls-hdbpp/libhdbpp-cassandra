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

public class StateViewer extends JLabel
{

    public StateViewer (int stateCode)
    {
        super();
        switch(stateCode)
        {
            case DevState._ALARM:
                this.setText(IDevice.ALARM);
                break;
            case DevState._CLOSE:
                this.setText(IDevice.CLOSE);
                break;
            case DevState._DISABLE:
                this.setText(IDevice.DISABLE);
                break;
            case DevState._EXTRACT:
                this.setText(IDevice.EXTRACT);
                break;
            case DevState._FAULT:
                this.setText(IDevice.FAULT);
                break;
            case DevState._INIT:
                this.setText(IDevice.INIT);
                break;
            case DevState._INSERT:
                this.setText(IDevice.INSERT);
                break;
            case DevState._MOVING:
                this.setText(IDevice.MOVING);
                break;
            case DevState._OFF:
                this.setText(IDevice.OFF);
                break;
            case DevState._ON:
                this.setText(IDevice.ON);
                break;
            case DevState._OPEN:
                this.setText(IDevice.OPEN);
                break;
            case DevState._RUNNING:
                this.setText(IDevice.RUNNING);
                break;
            case DevState._STANDBY:
                this.setText(IDevice.STANDBY);
                break;
            case DevState._UNKNOWN:
            default:
                this.setText(IDevice.UNKNOWN);
        }
        this.setToolTipText(this.getText());
        Color color = null;
        color = ATKConstant.getColor4State(this.getText());
        if (color != null)
        {
            this.setBackground(color);
            this.setOpaque(true);
        }
        else
        {
            this.setOpaque(false);
        }
    }
}
