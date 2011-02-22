/*
 * Synchrotron Soleil
 * 
 * File : StateTableRenderer.java
 * 
 * Project : Mambo_CVS
 * 
 * Description :
 * 
 * Author : SOLEIL
 * 
 * Original : 9 mars 2006
 * 
 * Revision: Author: Date: State:
 * 
 * Log: StateTableRenderer.java,v
 */
package fr.soleil.mambo.components.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fr.soleil.comete.viewController.tango.TangoStateBehavior;
import fr.soleil.comete.widget.awt.AwtColorTool;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoStateTranslation;
import fr.soleil.mambo.tools.Messages;

public class StateTableRenderer implements TableCellRenderer {

    public StateTableRenderer() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        String val = Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA");
        boolean needsColor = false;
        if (value != null) {
            if (value instanceof Integer) {
                needsColor = true;
                val = TangoStateTranslation.getStrStateValue(((Integer) value).intValue());
            }
            else {
                val = value.toString();
                needsColor = false;
            }
        }
        JLabel label = new JLabel(val);
        label.setToolTipText(val);
        if (needsColor) {
            CometeColor color = null;
            if (val != null) {
                val = val.trim().toUpperCase();
            }
            // XXX en attendant correction
            new TangoStateBehavior();
            color = TangoStateBehavior.getColor(val);
            if (color != null) {
                label.setBackground(AwtColorTool.getColor(color));
                label.setOpaque(true);
            }
            else {
                label.setOpaque(false);
            }
        }
        else {
            label.setOpaque(false);
        }
        return label;
    }

}
