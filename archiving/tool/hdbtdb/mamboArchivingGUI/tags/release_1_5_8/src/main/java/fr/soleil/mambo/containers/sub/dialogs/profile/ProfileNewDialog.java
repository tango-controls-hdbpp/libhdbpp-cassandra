//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/profile/ProfileNewDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileNewDialog.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileNewDialog.java,v $
// Revision 1.1  2005/12/15 11:29:40  ounsy
// Profile classes changed package from mambo.containers.sub.dialogs to mambo.containers.sub.dialogs.profile
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.sub.dialogs.profile;

import java.awt.Dimension;

import javax.swing.JDialog;

import fr.soleil.mambo.containers.profile.ProfileNewPanel;
import fr.soleil.mambo.tools.Messages;


/**
 * JDialog for new profile editing
 *
 * @author SOLEIL
 */
public class ProfileNewDialog extends JDialog
{

    private static ProfileNewDialog instance;

    public static ProfileNewDialog getInstance ()
    {
        if ( instance == null )
        {
            instance = new ProfileNewDialog();
        }
        return instance;
    }

    private ProfileNewDialog ()
    {
        super( ProfileSelectionDialog.getInstance() ,
               Messages.getMessage( "PROFILE_TITLE" )
               + " : "
               + Messages.getMessage( "PROFILE_NEW" ) ,
               true );
        this.setContentPane( ProfileNewPanel.getInstance() );
        this.setSize( new Dimension( 300 , 120 ) );
    }

}