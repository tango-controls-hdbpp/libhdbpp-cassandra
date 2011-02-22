//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCNextAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OpenVCEditDialogAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: VCNextAction.java,v $
//Revision 1.2  2007/01/11 14:05:46  ounsy
//Math Expressions Management (warning ! requires atk 2.7.0 or greater)
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.view.VCCustomTabbedPane;



public class VCNextAction extends AbstractAction
{
    private static VCNextAction instance = null;

    /**
     * @return
     */
    public static VCNextAction getInstance ( String name )
    {
        if ( instance == null )
        {
            instance = new VCNextAction( name );
        }

        return instance;
    }

    public static VCNextAction getInstance ()
    {
        return instance;
    }

    /**
     * @param name
     */
    private VCNextAction ( String name )
    {
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        VCCustomTabbedPane tabbedPane = VCCustomTabbedPane.getInstance();

        int oldValue = tabbedPane.getSelectedIndex();
        int newValue = oldValue + 1;

        tabbedPane.setEnabledAt( newValue , true );
        tabbedPane.setSelectedIndex( newValue );

        switch ( newValue )
        {
            case 1:
                tabbedPane.setEnabledAt( 0 , false );
                tabbedPane.setEnabledAt( 2 , false );
                tabbedPane.setEnabledAt( 3 , false );
                break;

            case 2:
                tabbedPane.setEnabledAt( 0 , false );
                tabbedPane.setEnabledAt( 1 , false );
                tabbedPane.setEnabledAt( 3 , false );
                break;

            case 3:
                tabbedPane.setEnabledAt( 0 , false );
                tabbedPane.setEnabledAt( 1 , false );
                tabbedPane.setEnabledAt( 2 , false );
                break;

            default :
                throw new IllegalStateException();
        }
    }
}
