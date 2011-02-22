/*	Synchrotron Soleil 
 *  
 *   File          :  MamboBasicTableRenderer.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  15 déc. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: MamboBasicTableRenderer.java,v 
 *
 */
package fr.soleil.mambo.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author SOLEIL
 */
public class MamboBasicTableRenderer extends DefaultTableCellRenderer {

	public MamboBasicTableRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if (column != 0) {
			return new DefaultTableCellRenderer()
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
		}
		Component comp = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		JTableHeader header = table.getTableHeader();
		if (header != null) {
			TableCellRenderer renderer = header.getDefaultRenderer();
			if (renderer != null) {
				Component comp2 = renderer.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, 0, 0);
				if (comp2 != null && row >= 0 && column == 0) {
					comp.setBackground(comp2.getBackground());
					comp.setForeground(Color.BLACK);
					if (comp instanceof JComponent
							&& comp2 instanceof JComponent) {
						((JComponent) comp).setBorder(((JComponent) comp2)
								.getBorder());
					}
					if (isSelected) {
						comp.setBackground(Color.GRAY);
						comp.setForeground(Color.WHITE);
					}
				}
			}
		}
		return comp;
	}

}