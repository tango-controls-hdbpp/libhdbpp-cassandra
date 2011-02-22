/*	Synchrotron Soleil 
 *  
 *   File          :  StateTableRenderer.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  9 mars 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: StateTableRenderer.java,v 
 *
 */
package fr.soleil.mambo.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fr.esrf.Tango.DevState;
import fr.esrf.tangoatk.core.IDevice;
import fr.esrf.tangoatk.widget.util.ATKConstant;
import fr.soleil.mambo.tools.Messages;

public class StateTableRenderer implements TableCellRenderer
{

    public StateTableRenderer ()
    {
        super();
    }

    public Component getTableCellRendererComponent ( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
    {
        String val = Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA");
        boolean needsColor = false;
        if (value != null)
        {
            if (value instanceof Integer)
            {
            	needsColor = true;
                int intVal = ( (Integer)value ).intValue();
                switch (intVal)
                {
                    case DevState._ALARM:
                        val = IDevice.ALARM;
                        break;
                    case DevState._CLOSE:
                        val = IDevice.CLOSE;
                        break;
                    case DevState._DISABLE:
                        val = IDevice.DISABLE;
                        break;
                    case DevState._EXTRACT:
                        val = IDevice.EXTRACT;
                        break;
                    case DevState._FAULT:
                        val = IDevice.FAULT;
                        break;
                    case DevState._INIT:
                        val = IDevice.INIT;
                        break;
                    case DevState._INSERT:
                        val = IDevice.INSERT;
                        break;
                    case DevState._MOVING:
                        val = IDevice.MOVING;
                        break;
                    case DevState._OFF:
                        val = IDevice.OFF;
                        break;
                    case DevState._ON:
                        val = IDevice.ON;
                        break;
                    case DevState._OPEN:
                        val = IDevice.OPEN;
                        break;
                    case DevState._RUNNING:
                        val = IDevice.RUNNING;
                        break;
                    case DevState._STANDBY:
                        val = IDevice.STANDBY;
                        break;
                    case DevState._UNKNOWN:
                    default:
                        val = IDevice.UNKNOWN;
                }
            }
            else
            {
                val = value.toString();
                needsColor = false;
            }
        }
        JLabel label = new JLabel(val);
        label.setToolTipText(val);
        if (needsColor)
        {
            Color color = null;
            color = ATKConstant.getColor4State(val);
            if (color != null)
            {
                label.setBackground(color);
                label.setOpaque(true);
            }
            else
            {
                label.setOpaque(false);
            }
        }
        else
        {
            label.setOpaque(false);
        }
        return label;
    }

}
