//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/profile/ProfileDeleteAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileDeleteAction.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ProfileDeleteAction.java,v $
// Revision 1.2  2005/12/15 10:48:44  ounsy
// Profile classes changed package from mambo.containers.sub.dialogs to mambo.containers.sub.dialogs.profile
//
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
import javax.swing.JOptionPane;

import fr.soleil.mambo.containers.profile.ProfileSelectionListPanel;
import fr.soleil.mambo.containers.sub.dialogs.profile.ProfileSelectionDialog;
import fr.soleil.mambo.profile.manager.ProfileManagerFactory;
import fr.soleil.mambo.tools.Messages;


/**
 * @author SOLEIL
 */
public class ProfileDeleteAction extends AbstractAction
{

    private static ProfileDeleteAction instance;

    /**
     * Returns the currently used ProfileDeleteAction, and defines it when necessary
     *
     * @param name the name of the ProfileDeleteAction in case of first call
     * @return The currently used ProfileDeleteAction, and defines it when necessary
     */
    public static ProfileDeleteAction getInstance ( String name )
    {
        if ( instance == null )
        {
            instance = new ProfileDeleteAction( name );
        }
        return instance;
    }

    /**
     * Returns the currently used ProfileDeleteAction
     *
     * @return The currently used ProfileDeleteAction
     */
    public static ProfileDeleteAction getInstance ()
    {
        return instance;
    }

    /**
     * Standard action constructor that sets the action's name
     *
     * @param name The name of the action
     */
    private ProfileDeleteAction ( String name )
    {
        super( name );
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        if ( validateChoice() )
        {
            ProfileManagerFactory.getCurrentImpl().deleteProfile( ProfileSelectionListPanel.getInstance().getSelectedProfile() );
            ProfileSelectionListPanel.getInstance().reload();
        }
        else
        {
            JOptionPane.showMessageDialog( ProfileSelectionDialog.getInstance() ,
                                           Messages.getMessage( "PROFILE_ERROR_NO_PROFILE" ) ,
                                           Messages.getMessage( "PROFILE_ERROR_TITLE" ) ,
                                           JOptionPane.ERROR_MESSAGE );
        }
    }

    private boolean validateChoice ()
    {
        ProfileSelectionListPanel panel = ProfileSelectionListPanel.getInstance();
        return ( panel.getSelectedProfile() != -1 );
    }

}
