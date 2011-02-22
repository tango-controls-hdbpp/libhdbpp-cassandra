/*	Synchrotron Soleil 
 *  
 *   File          :  MamboFormatableTable.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  13 déc. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: MamboFormatableTable.java,v 
 *
 */
package fr.soleil.mambo.components;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import fr.soleil.mambo.options.Options;


/**
 * 
 * @author SOLEIL
 */
public class MamboFormatableTable extends JTable {
    
    public MamboFormatableTable() {
        super();
    }

    public MamboFormatableTable(TableModel model) {
        super(model);
    }

    public String toString() {
        String stringValue = "";
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                stringValue += this.getModel().getValueAt(i, j) + Options.getInstance().getGeneralOptions().getSeparator();
            }
            if (this.getColumnCount() > 0)
            {
                stringValue = stringValue.substring(0, stringValue.lastIndexOf(Options.getInstance().getGeneralOptions().getSeparator()));
            }
            stringValue += "\n";
        }
        if ("".equals(stringValue)) {
            return stringValue;
        }
        else {
            return stringValue.substring(0, stringValue.lastIndexOf("\n"));
        }
    }

}
