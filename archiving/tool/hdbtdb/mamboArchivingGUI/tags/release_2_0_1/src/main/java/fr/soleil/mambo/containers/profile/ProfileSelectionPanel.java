//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/profile/ProfileSelectionPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionPanel.
//						(Claisse Laurent) - nov. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ProfileSelectionPanel.java,v $
// Revision 1.4  2007/08/27 14:22:30  pierrejoseph
// MultiSession display suppression at the connection step
//
// Revision 1.3  2006/06/08 09:19:42  ounsy
// "show profile path" option added
//
// Revision 1.2  2005/12/15 11:26:33  ounsy
// multi sessions management
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
package fr.soleil.mambo.containers.profile;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


/**
 * @author SOLEIL
 */
public class ProfileSelectionPanel extends JPanel
{

    //private JCheckBox multiSession;
    private static ProfileSelectionPanel instance;
    private final static Font sessionFont = new Font("Arial", Font.PLAIN, 12) ;

    public static ProfileSelectionPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ProfileSelectionPanel();
        }
        return instance;
    }

    private ProfileSelectionPanel ()
    {
        super();
        /*multiSession = new JCheckBox(Messages.getMessage("SESSION_MULTI_ENABLED"));
        multiSession.setFont(sessionFont);
        multiSession.setForeground(Color.RED);
        multiSession.setOpaque(false);
        multiSession.setSelected(false);
        JPanel multiSessionPanel = new JPanel();
        multiSessionPanel.add(multiSession);
        GUIUtilities.setObjectBackground( multiSessionPanel , GUIUtilities.PROFILE_COLOR );*/
        ProfileSelectionActionPanel.getInstance();
        Box box = new Box( BoxLayout.Y_AXIS );
        box.setBackground( GUIUtilities.getProfileColor() );
        box.add( ProfileSelectionTitlePanel.getInstance() );
        box.add( ProfileSelectionListPanel.getInstance() );
        //box.add(multiSessionPanel);
        box.add(new JLabel());
        box.add( ProfileSelectionActionPanel.getInstance() );
        this.add( box );
        GUIUtilities.setObjectBackground( this , GUIUtilities.PROFILE_COLOR );
    }

    /*public boolean isMultiSession() {
        return multiSession.isSelected();
    }*/
}
