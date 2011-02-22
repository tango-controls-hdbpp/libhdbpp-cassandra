//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACRecapTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACRecapTable.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ACRecapTable.java,v $
// Revision 1.5  2006/08/09 10:35:27  ounsy
// Time formating in assessments + differences between database and archiving configuration highlighted
//
// Revision 1.4  2006/05/16 09:36:11  ounsy
// minor changes
//
// Revision 1.3  2006/04/10 08:59:18  ounsy
// optimisation on loading an archiving assessment
//
// Revision 1.2  2005/12/15 11:14:58  ounsy
// "copy table to clipboard" management
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

import fr.soleil.mambo.components.MamboFormatableTable;
import fr.soleil.mambo.components.renderers.ACRecapTableRenderer;

public class ACRecapTable extends MamboFormatableTable
{

    public ACRecapTable ()
    {
        //super( new ACRecapTableModel() );
        super ();
        /*ACRecapTableModel model = new ACRecapTableModel ();
        super.setModel ( model );*/
        
        
        setAutoCreateColumnsFromModel( true );
        setDefaultRenderer( Object.class , new ACRecapTableRenderer() );
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
