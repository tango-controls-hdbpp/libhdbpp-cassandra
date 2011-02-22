//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/AlternateSelectionAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  RemoveSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: AlternateSelectionAction.java,v $
//Revision 1.2  2006/05/29 15:45:00  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.AttributesSelectTable;


public class AlternateSelectionAction extends AbstractAction
{
    private int type;

    public static final int SELECT_REVERSE_TYPE = 0;
    public static final int SELECT_ALL_TYPE = 1;
    public static final int SELECT_NONE_TYPE = 2;

    /**
     * @param name
     */
    public AlternateSelectionAction ( String name , int _type )
    {
        super( name );

        if ( _type != SELECT_REVERSE_TYPE && _type != SELECT_ALL_TYPE && _type != SELECT_NONE_TYPE )
        {
            throw new IllegalArgumentException( "Expected either of " + SELECT_REVERSE_TYPE + "," + SELECT_ALL_TYPE + "," + SELECT_NONE_TYPE + " as a parameter. Received " + type + " instead." );
        }

        this.putValue( Action.NAME , name );
        this.type = _type;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent event )
    {
        
        AttributesSelectTable table = AttributesSelectTable.getInstance();
        table.applyChange();
        switch ( this.type )
        {
            case SELECT_REVERSE_TYPE:
                table.reverseSelection();
            break;

            case SELECT_ALL_TYPE:
                table.selectAllOrNone( true );
            break;

            case SELECT_NONE_TYPE:
                table.selectAllOrNone( false );
            break;
        }
    }

}
