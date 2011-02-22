//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingGeneralPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingGeneralPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ArchivingGeneralPanel.java,v $
// Revision 1.8  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.7  2006/11/06 09:28:05  ounsy
// icons reorganization
//
// Revision 1.6  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.5  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.4  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 12:49:41  ounsy
// modified imports
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.archiving;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.archiving.CloseSelectedACAction;
import fr.soleil.mambo.actions.archiving.OpenACEditDialogAction;
import fr.soleil.mambo.components.MamboActivableButton;
import fr.soleil.mambo.components.archiving.LimitedACStack;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;



public class ArchivingGeneralPanel extends JPanel
{
//MULTI-CONF
    private static ArchivingGeneralPanel instance = null;

    private JLabel nameLabel;
    private JLabel nameField;
    private JLabel creationDateLabel;
    private JLabel lastUpdateDateLabel;
    private JLabel creationDateField;
    private JLabel lastUpdateDateField;
    private JLabel pathField;
    private JButton closeButton;
    private JButton newButton;
    private OpenedACComboBox stack;

    OpenACEditDialogAction newAction;
    private final static ImageIcon newIcon = new ImageIcon( Mambo.class.getResource( "icons/New.gif" ) );
    private static boolean isAlternateSelectionMode;

    /**
     * @return 8 juil. 2005
     */
    public static ArchivingGeneralPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ArchivingGeneralPanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.ARCHIVING_COLOR );
        }

        return instance;
    }

    /**
     *
     */
    private ArchivingGeneralPanel ()
    {
        super();

        initComponents();
        addComponents();
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        Box topBox = new Box( BoxLayout.X_AXIS );
        topBox.add( newButton );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( stack );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( closeButton );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( pathField );
        topBox.add( Box.createHorizontalGlue() );

        Box bottomBox = new Box( BoxLayout.X_AXIS );
        bottomBox.add( nameLabel );
        bottomBox.add( Box.createHorizontalStrut( 5 ) );
        bottomBox.add( nameField );
        bottomBox.add( Box.createHorizontalStrut( 10 ) );
        bottomBox.add( creationDateLabel );
        bottomBox.add( Box.createHorizontalStrut( 5 ) );
        bottomBox.add( creationDateField );
        bottomBox.add( Box.createHorizontalStrut( 10 ) );
        bottomBox.add( lastUpdateDateLabel );
        bottomBox.add( Box.createHorizontalStrut( 5 ) );
        bottomBox.add( lastUpdateDateField );
        bottomBox.add( Box.createHorizontalGlue() );

        this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
        this.add( topBox );
        this.add( Box.createVerticalStrut( 5 ) );
        this.add( bottomBox );

        String msg = Messages.getMessage( "ARCHIVING_GENERAL_BORDER" );
        TitledBorder titledBorder = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(titledBorder, BorderFactory.createEmptyBorder(1, 5, 2, 5));
        this.setBorder( compoundBorder );

//        this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 150 ) );
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        String msg = "";

        msg = Messages.getMessage( "ARCHIVING_GENERAL_NAME" );
        nameLabel = new JLabel( msg );
        Font newFont = nameLabel.getFont().deriveFont( Font.ITALIC );
        nameLabel.setFont( newFont );

        msg = Messages.getMessage( "ARCHIVING_GENERAL_CREATION_DATE" );
        creationDateLabel = new JLabel( msg );
        creationDateLabel.setFont( newFont );

        msg = Messages.getMessage( "ARCHIVING_GENERAL_LAST_UPDATE_DATE" );
        lastUpdateDateLabel = new JLabel( msg );
        lastUpdateDateLabel.setFont( newFont );

        creationDateField = new JLabel();
        lastUpdateDateField = new JLabel();
        nameField = new JLabel();
        pathField = new JLabel();

        msg = Messages.getMessage( "ARCHIVING_ACTION_NEW_BUTTON" );
        newAction = new OpenACEditDialogAction( msg , true , isAlternateSelectionMode );
        newButton = new MamboActivableButton( newAction , newIcon , newIcon );
        GUIUtilities.setObjectBackground( newButton , GUIUtilities.ARCHIVING_COLOR );

        stack = OpenedACComboBox.getInstance( new LimitedACStack() );
        /*stack.setPreferredSize ( new Dimension ( 300 , 20 ) );
        stack.setMaximumSize ( new Dimension ( 300 , 20 ) );*/

        closeButton = new JButton( CloseSelectedACAction.getInstance( "" ) );
        ImageIcon newActionIcon = new ImageIcon( Mambo.class.getResource( "icons/remove.gif" ) );
        closeButton.setIcon( newActionIcon );
        closeButton.setPreferredSize( new Dimension( 22 , 20 ) );
    }

    public void setCreationDate ( Timestamp creationDate )
    {
        if ( creationDate != null )
        {
            creationDateField.setText( creationDate.toString() );
        }
        else
        {
            creationDateField.setText( "" );
        }
    }

    public void setLastUpdateDate ( Timestamp lastUpdateDate )
    {
        if ( lastUpdateDate != null )
        {
            lastUpdateDateField.setText( lastUpdateDate.toString() );
        }
        else
        {
            lastUpdateDateField.setText( "" );
        }
    }

    @Override
	public void setName ( String name )
    {
        if ( name != null )
        {
            nameField.setText( name );
        }
        else
        {
            nameField.setText( "" );
        }
    }

    /**
     * @param path
     */
    public void setPath ( String path )
    {
        if ( path != null )
        {
            pathField.setText( "(" + path + ")" );
        }
        else
        {
            pathField.setText( "" );
        }
    }

}
