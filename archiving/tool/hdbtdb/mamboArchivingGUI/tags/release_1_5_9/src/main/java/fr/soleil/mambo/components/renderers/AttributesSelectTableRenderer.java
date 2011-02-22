//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/components/renderers/AttributesSelectTableRenderer.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SnapshotDetailRenderer.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.4 $
//
//$Log: AttributesSelectTableRenderer.java,v $
//Revision 1.4  2006/10/19 12:37:36  ounsy
//changed the colors
//
//Revision 1.3  2006/05/19 15:05:29  ounsy
//minor changes
//
//Revision 1.2  2005/12/15 11:17:51  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:27:45  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.models.AttributesSelectTableModel;


/**
* A cell renderer used for SnapshotDetailTable. 
* It paints cells containing "Not applicable" values in grey, and modified values in red. 
* In all other cases, it paints the cell with the default component. 
*/
public class AttributesSelectTableRenderer implements TableCellRenderer 
{	
	 /* (non-Javadoc)
	  * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	  */
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	 {
	     if ( row == -1 )
         {
	         JTableHeader header = new JTableHeader ();
	         return header.getDefaultRenderer ().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         }
	     
	     if ( column == 3 )
	     {
	         JCheckBox ret = new JCheckBox ();
		         
	         Boolean _value = (Boolean) value;
	         ret.setSelected ( _value.booleanValue () );
	         
	         return ret;    
	     }
	     else 
	     {
	         Component ret = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	         
	         AttributesSelectTableModel model = (AttributesSelectTableModel) table.getModel ();
	         ArchivingConfigurationAttribute attr = model.getACAttributeAtRow ( row );
	         
	         if ( attr.isNew () )
	         {
	             Color newColorNotSelected = new Color ( 255 , 220 , 220 );
	             Color newColorSelected = new Color ( 255 , 150 , 150 );
	             
	             Color newColor = isSelected ? newColorSelected : newColorNotSelected;
	             
	             ret.setBackground ( newColor );    
	         }
	         
	         return ret; 
	     }
	 }
}
