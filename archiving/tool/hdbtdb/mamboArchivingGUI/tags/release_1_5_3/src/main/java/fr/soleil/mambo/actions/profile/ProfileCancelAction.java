//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/profile/ProfileCancelAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileCancelAction.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileCancelAction.java,v $
// Revision 1.1  2005/12/15 09:21:07  ounsy
// First Commit including profile management
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.profile;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * @author SOLEIL
 */
public class ProfileCancelAction extends AbstractAction
{

    /**
     * Standard action constructor that sets the action's name
     *
     * @param name The name of the action
     */
    public ProfileCancelAction ( String name )
    {
        super( name );
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        System.exit( 0 );
    }

}
