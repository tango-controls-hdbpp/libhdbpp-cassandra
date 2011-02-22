//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/profile/ProfileSelectionPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionPanel.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ProfileSelectionPanel.java,v $
// Revision 1.4  2007/08/27 14:22:30  pierrejoseph
// MultiSession display suppression at the connection step
//
// Revision 1.3  2006/06/08 09:18:46  ounsy
// "show profile path" option added
//
// Revision 1.2  2005/12/14 16:23:53  ounsy
// added multi sessions management
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.profile;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.soleil.bensikin.containers.profile.ProfileSelectionActionPanel;
import fr.soleil.bensikin.containers.profile.ProfileSelectionListPanel;
import fr.soleil.bensikin.containers.profile.ProfileSelectionTitlePanel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;



/**
 * @author SOLEIL
 */
public class ProfileSelectionPanel extends JPanel
{

   // private JCheckBox multiSession;
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
