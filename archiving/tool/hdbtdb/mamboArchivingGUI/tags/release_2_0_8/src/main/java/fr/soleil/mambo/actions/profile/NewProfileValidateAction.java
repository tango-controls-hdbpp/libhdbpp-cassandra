//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/profile/NewProfileValidateAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NewProfileValidateAction.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: NewProfileValidateAction.java,v $
// Revision 1.2  2005/12/15 10:48:28  ounsy
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
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.mambo.containers.profile.ProfileNewPanel;
import fr.soleil.mambo.containers.profile.ProfileSelectionListPanel;
import fr.soleil.mambo.containers.sub.dialogs.profile.ProfileNewDialog;
import fr.soleil.mambo.containers.sub.dialogs.profile.ProfileSelectionDialog;
import fr.soleil.mambo.profile.manager.ProfileManagerFactory;
import fr.soleil.mambo.tools.Messages;


/**
 * @author SOLEIL
 */
public class NewProfileValidateAction extends AbstractAction
{

    /**
     * Standard action constructor that sets the action's name
     *
     * @param name The name of the action
     */
    public NewProfileValidateAction ( String name )
    {
        super( name );
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        String name = ProfileNewPanel.getInstance().getName();
        String path = ProfileNewPanel.getInstance().getPath();
        if ( validate( name , path ) )
        {
            ProfileManagerFactory.getCurrentImpl().addProfile( name , path );
            ProfileSelectionListPanel.getInstance().reload();
            ProfileNewDialog.getInstance().setVisible( false );
        }
        else
        {
            JOptionPane.showMessageDialog( ProfileSelectionDialog.getInstance() ,
                                           Messages.getMessage( "PROFILE_ERROR_NEW" ) ,
                                           Messages.getMessage( "PROFILE_ERROR_TITLE" ) ,
                                           JOptionPane.ERROR_MESSAGE );
        }
    }

    private boolean validate ( String name , String path )
    {
        if ( ( !"".equals( name.trim() ) ) && ( !"".equals( path.trim() ) ) )
        {
            File pathFile = new File( path.trim() );
            return ( pathFile.exists() && pathFile.isDirectory() );
        }
        return false;
    }

}
