/*	Synchrotron Soleil 
 *  
 *   File          :  StringTableRenderer.java
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
 *   Log: StringTableRenderer.java,v 
 *
 */
package fr.soleil.mambo.components.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fr.soleil.mambo.tools.Messages;

//import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.StringFormater;

public class StringTableRenderer implements TableCellRenderer {

	public StringTableRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String val = Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA");
		if (value != null) {
			val = value.toString();
		}
		JLabel label = new JLabel(val);
		return label;
	}

}
