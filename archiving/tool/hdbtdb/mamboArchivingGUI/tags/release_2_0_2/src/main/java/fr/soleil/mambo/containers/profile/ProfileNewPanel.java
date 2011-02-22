//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/profile/ProfileNewPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileNewPanel.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileNewPanel.java,v $
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.profile.NewProfileBrowseAction;
import fr.soleil.mambo.actions.profile.NewProfileCancelAction;
import fr.soleil.mambo.actions.profile.NewProfileValidateAction;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


/**
 * @author SOLEIL
 */
public class ProfileNewPanel extends JPanel
{

    private static ProfileNewPanel instance;

    private JLabel nameLabel , pathLabel;
    private JTextField nameField , pathField;
    private JButton browseButton , okButton , cancelButton;
    private ImageIcon browseIcon = new ImageIcon( Mambo.class.getResource( "icons/browse.gif" ) );
    private ImageIcon cancelIcon = new ImageIcon( Mambo.class.getResource( "icons/cancel.gif" ) );
    private ImageIcon okIcon = new ImageIcon( Mambo.class.getResource( "icons/validate.gif" ) );

    public static ProfileNewPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ProfileNewPanel();
        }
        return instance;
    }

    private ProfileNewPanel ()
    {
        nameLabel = new JLabel( Messages.getMessage( "PROFILE_NAME" ) );
        nameField = new JTextField();
        pathLabel = new JLabel( Messages.getMessage( "PROFILE_PATH" ) );
        pathField = new JTextField();
        browseButton = new JButton();
        browseButton.setAction( new NewProfileBrowseAction( Messages.getMessage( "PROFILE_BROWSE" ) ) );
        browseButton.setIcon( browseIcon );
        browseButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( browseButton , GUIUtilities.PROFILE_COLOR );
        okButton = new JButton();
        okButton.setAction( new NewProfileValidateAction( Messages.getMessage( "PROFILE_OK" ) ) );
        okButton.setIcon( okIcon );
        okButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( okButton , GUIUtilities.PROFILE_COLOR );
        cancelButton = new JButton();
        cancelButton.setAction( new NewProfileCancelAction( Messages.getMessage( "PROFILE_CANCEL" ) ) );
        cancelButton.setIcon( cancelIcon );
        cancelButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        GUIUtilities.setObjectBackground( cancelButton , GUIUtilities.PROFILE_COLOR );
        this.setLayout( new SpringLayout() );
        
        //Line 1
        this.add( nameLabel );
        this.add( nameField );
        this.add( Box.createRigidArea( new Dimension( 20 , 20 ) ) );
        
        //Line 2
        this.add( pathLabel );
        this.add( pathField );
        this.add( browseButton );
        
        //Line 3
        this.add( cancelButton );
        this.add( Box.createRigidArea( new Dimension( 20 , 20 ) ) );
        this.add( okButton );

        SpringUtilities.makeCompactGrid
                ( this ,
                  3 , 3 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true ); //every component same size
        GUIUtilities.setObjectBackground( this , GUIUtilities.PROFILE_COLOR );
    }

    /**
     * Gets the Path entered for the new profile
     *
     * @return The Path entered for the new profile
     */
    public String getPath ()
    {
        return pathField.getText();
    }

    /**
     * Sets the text of the "path" TextField
     */
    public void setPath ( String path )
    {
        pathField.setText( path );
    }

    /**
     * Gets the Name entered for the new profile
     *
     * @return The Name entered for the new profile
     */
    public String getName ()
    {
        return nameField.getText();
    }

}
