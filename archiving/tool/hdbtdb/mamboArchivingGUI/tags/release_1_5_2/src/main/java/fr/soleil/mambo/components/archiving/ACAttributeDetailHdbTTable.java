//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeDetailHdbTTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbTTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailHdbTTable.java,v $
// Revision 1.2  2005/12/15 10:58:39  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:24  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.archiving;

import javax.swing.JTable;

import fr.soleil.mambo.components.renderers.ACAttributeDetailTableRenderer;
import fr.soleil.mambo.models.ACAttributeDetailHdbTTableModel;
import fr.soleil.mambo.tools.GUIUtilities;


public class ACAttributeDetailHdbTTable extends JTable
{
    private static ACAttributeDetailHdbTTable instance = null;

    public static ACAttributeDetailHdbTTable getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACAttributeDetailHdbTTable();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.ARCHIVING_COLOR );
        }

        return instance;
    }

    private ACAttributeDetailHdbTTable ()
    {
        super( ACAttributeDetailHdbTTableModel.getInstance() );
        setAutoCreateColumnsFromModel( true );
        setDefaultRenderer( Object.class , new ACAttributeDetailTableRenderer() );
        setEnabled(false);
    }

    /* (non-Javadoc)
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    public boolean isCellEditable ( int row , int column )
    {
        return false;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JTable#isColumnSelected(int)
     */
    public boolean isColumnSelected(int column) {
        return false;
    }

}
