//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ACBackAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OpenACEditDialogAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: ACBackAction.java,v $
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

import fr.soleil.mambo.components.archiving.ACCustomTabbedPane;



public class ACBackAction extends AbstractAction
{
    private static ACBackAction instance = null;

    /**
     * @return
     */
    public static ACBackAction getInstance ( String name )
    {
        if ( instance == null )
        {
            instance = new ACBackAction( name );
        }

        return instance;
    }

    public static ACBackAction getInstance ()
    {
        return instance;
    }

    /**
     * @param name
     */
    private ACBackAction ( String name )
    {
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        ACCustomTabbedPane tabbedPane = ACCustomTabbedPane.getInstance();

        int oldValue = tabbedPane.getSelectedIndex();
        int newValue = oldValue - 1;

        tabbedPane.setEnabledAt( newValue , true );
        tabbedPane.setSelectedIndex( newValue );

        switch ( newValue )
        {
            case 0:
                tabbedPane.setEnabledAt( 1 , false );
                tabbedPane.setEnabledAt( 2 , false );
                break;

            case 1:
                tabbedPane.setEnabledAt( 0 , false );
                tabbedPane.setEnabledAt( 2 , false );
                break;

            default :
                throw new IllegalStateException();
        }
    }

}
