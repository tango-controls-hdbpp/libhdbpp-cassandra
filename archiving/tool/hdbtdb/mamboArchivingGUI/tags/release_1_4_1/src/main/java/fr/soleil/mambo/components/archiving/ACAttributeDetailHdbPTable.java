//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeDetailHdbPTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbPTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailHdbPTable.java,v $
// Revision 1.2  2005/12/15 10:58:16  ounsy
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
import fr.soleil.mambo.models.ACAttributeDetailHdbPTableModel;
import fr.soleil.mambo.tools.GUIUtilities;


public class ACAttributeDetailHdbPTable extends JTable
{
    private static ACAttributeDetailHdbPTable instance = null;

    public static ACAttributeDetailHdbPTable getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACAttributeDetailHdbPTable();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.ARCHIVING_COLOR );
        }

        return instance;
    }

    private ACAttributeDetailHdbPTable ()
    {
        super( ACAttributeDetailHdbPTableModel.getInstance() );
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
