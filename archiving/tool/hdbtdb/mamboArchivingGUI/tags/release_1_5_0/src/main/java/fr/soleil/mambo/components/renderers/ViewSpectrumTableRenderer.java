/*	Synchrotron Soleil 
 *  
 *   File          :  ViewSpectrumTableRenderer.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  31 janv. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ViewSpectrumTableRenderer.java,v 
 *
 */
package fr.soleil.mambo.components.renderers;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author SOLEIL
 */
public class ViewSpectrumTableRenderer implements TableCellRenderer {

    public ViewSpectrumTableRenderer() {
        
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) {
            JCheckBox checkbox = new JCheckBox();
            checkbox.setSelected( ((Boolean)value).booleanValue() );
            return checkbox;
        }
        return new JLabel(value.toString());
    }

}
