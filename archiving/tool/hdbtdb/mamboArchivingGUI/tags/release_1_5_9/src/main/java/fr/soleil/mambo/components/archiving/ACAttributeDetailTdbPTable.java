//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributeDetailTdbPTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailTdbPTable.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailTdbPTable.java,v $
// Revision 1.2  2005/12/15 10:59:15  ounsy
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
import fr.soleil.mambo.models.ACAttributeDetailTdbPTableModel;
import fr.soleil.mambo.tools.GUIUtilities;


public class ACAttributeDetailTdbPTable extends JTable
{
    private static ACAttributeDetailTdbPTable instance = null;

    public static ACAttributeDetailTdbPTable getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACAttributeDetailTdbPTable();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.ARCHIVING_COLOR );
        }

        return instance;
    }

    private ACAttributeDetailTdbPTable ()
    {
        super( ACAttributeDetailTdbPTableModel.getInstance() );
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
