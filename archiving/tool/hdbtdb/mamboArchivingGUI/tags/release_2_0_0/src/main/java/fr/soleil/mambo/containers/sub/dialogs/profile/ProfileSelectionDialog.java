//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/profile/ProfileSelectionDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionDialog.
//						(Claisse Laurent) - nov. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ProfileSelectionDialog.java,v $
// Revision 1.4  2007/08/27 14:22:30  pierrejoseph
// MultiSession display suppression at the connection step
//
// Revision 1.3  2006/06/08 09:19:42  ounsy
// "show profile path" option added
//
// Revision 1.2  2006/03/10 12:03:25  ounsy
// state and string support
//
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

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import fr.soleil.mambo.containers.profile.ProfileSelectionPanel;
import fr.soleil.mambo.tools.Messages;


/**
 * @author SOLEIL
 */
public class ProfileSelectionDialog extends JDialog
{

    private static ProfileSelectionDialog instance;

    public static ProfileSelectionDialog getInstance ()
    {
        if ( instance == null )
        {
            instance = new ProfileSelectionDialog();
        }
        return instance;
    }

    private ProfileSelectionDialog ()
    {
        super( ( Frame ) null , Messages.getMessage( "PROFILE_TITLE" ) , true );
        this.setContentPane( ProfileSelectionPanel.getInstance() );
//      Size avec Multiselection
		//this.setSize(500 , 230);
		// Size sans MultiSelection
		this.setSize(450 , 200);
        this.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        this.addWindowListener(new KillerWindowListener());
    }

    private class KillerWindowListener extends WindowAdapter {
        public KillerWindowListener() {
            super();
        }
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
        public void windowClosed(WindowEvent e) {
            System.exit(0);
        }
    }
    //this.addWindowListener(new KillerWindowListener());
}
