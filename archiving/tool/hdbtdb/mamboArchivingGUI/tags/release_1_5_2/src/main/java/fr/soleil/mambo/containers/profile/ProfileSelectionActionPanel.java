//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/profile/ProfileSelectionActionPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionActionPanel.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileSelectionActionPanel.java,v $
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

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.profile.ProfileCancelAction;
import fr.soleil.mambo.actions.profile.ProfileDeleteAction;
import fr.soleil.mambo.actions.profile.ProfileNewAction;
import fr.soleil.mambo.actions.profile.ProfileReloadAction;
import fr.soleil.mambo.actions.profile.ProfileValidateAction;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


/**
 * @author SOLEIL
 */
public class ProfileSelectionActionPanel extends JPanel
{

    private static ProfileSelectionActionPanel instance;
    private JButton okButton;
    private JButton cancelButton;
    private JButton newButton;
    private JButton deleteButton;
    private JButton reloadButton;
    private ImageIcon validateIcon = new ImageIcon( Mambo.class.getResource( "icons/validate.gif" ) );
    private ImageIcon cancelIcon = new ImageIcon( Mambo.class.getResource( "icons/cancel.gif" ) );
    private ImageIcon newProfileIcon = new ImageIcon( Mambo.class.getResource( "icons/profile_new.gif" ) );
    private ImageIcon deleteProfileIcon = new ImageIcon( Mambo.class.getResource( "icons/profile_delete.gif" ) );
    private ImageIcon reloadIcon = new ImageIcon( Mambo.class.getResource( "icons/update.gif" ) );

    private ProfileSelectionActionPanel ()
    {
        setLayout( new SpringLayout() );
        okButton = new JButton();
        okButton.setAction( ProfileValidateAction.getInstance( Messages.getMessage( "PROFILE_OK" ) ) );
        okButton.setIcon( validateIcon );
        okButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( okButton , GUIUtilities.PROFILE_COLOR );
        newButton = new JButton();
        newButton.setAction( new ProfileNewAction( Messages.getMessage( "PROFILE_NEW" ) ) );
        newButton.setIcon( newProfileIcon );
        newButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( newButton , GUIUtilities.PROFILE_COLOR );
        deleteButton = new JButton();
        deleteButton.setAction( ProfileDeleteAction.getInstance( Messages.getMessage( "PROFILE_DELETE" ) ) );
        deleteButton.setIcon( deleteProfileIcon );
        deleteButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( deleteButton , GUIUtilities.PROFILE_COLOR );
        reloadButton = new JButton();
        reloadButton.setAction( new ProfileReloadAction( Messages.getMessage( "PROFILE_RELOAD" ) ) );
        reloadButton.setIcon( reloadIcon );
        reloadButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( reloadButton , GUIUtilities.PROFILE_COLOR );
        cancelButton = new JButton();
        cancelButton.setAction( new ProfileCancelAction( Messages.getMessage( "PROFILE_CANCEL_AND_EXIT" ) ) );
        cancelButton.setIcon( cancelIcon );
        cancelButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( cancelButton , GUIUtilities.PROFILE_COLOR );
        //Line 1
        this.add( okButton );
        this.add( cancelButton );
        this.add( reloadButton );
        //Line 2
        this.add( newButton );
        this.add( deleteButton );
        this.add( Box.createRigidArea( new Dimension( 10 , 10 ) ) );
        SpringUtilities.makeCompactGrid
                ( this ,
                  2 , 3 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true ); //every component same size
        GUIUtilities.setObjectBackground( this , GUIUtilities.PROFILE_COLOR );
    }

    public static ProfileSelectionActionPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ProfileSelectionActionPanel();
        }
        return instance;
    }

}
