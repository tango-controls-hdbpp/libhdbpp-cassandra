//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewGeneralPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewGeneralPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: ViewGeneralPanel.java,v $
// Revision 1.9  2008/04/09 18:00:00  achouri
// add new button
//
// Revision 1.8  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.7  2006/11/06 09:28:05  ounsy
// icons reorganization
//
// Revision 1.6  2006/08/31 12:19:32  ounsy
// informations about forcing export only in case of tdb
//
// Revision 1.5  2006/08/31 09:53:54  ounsy
// User always knows whether data export will be forced
//
// Revision 1.4  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
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
package fr.soleil.mambo.containers.view;

import java.awt.Color;
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
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.CloseSelectedVCAction;
import fr.soleil.mambo.actions.view.OpenVCEditDialogAction;
import fr.soleil.mambo.components.MamboActivableButton;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class ViewGeneralPanel extends JPanel
{
//  MULTI-CONF
    private static ViewGeneralPanel instance = null;

    private JButton newButton;
    private JLabel nameLabel;
    private JLabel nameField;
    private JLabel creationDateLabel;
    private JLabel lastUpdateDateLabel;
    private JLabel creationDateField;
    private JLabel lastUpdateDateField;
    private JLabel forceExportLabel;
    private JLabel pathField;
    private JButton closeButton;
    private OpenedVCComboBox stack;
    private Font forceExportFont = new Font("Arial",Font.BOLD,12);

    private final static ImageIcon newIcon = new ImageIcon( Mambo.class.getResource( "icons/New.gif" ) );

    /**
     * @return 8 juil. 2005
     */
    public static ViewGeneralPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ViewGeneralPanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.VIEW_COLOR );
        }

        return instance;
    }

    /**
     * 
     */
    private ViewGeneralPanel ()
    {
        super();

        initComponents();
        addComponents();
        setLayout();
        initBorder();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout ()
    {
        this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 150 ) );
        //this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                this,
                this.getComponentCount(), 1,
                0, 0,
                0, 0,
                true
        );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        Box topBox = new Box( BoxLayout.X_AXIS );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( newButton );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( stack );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( closeButton );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( pathField );
        topBox.add( Box.createHorizontalGlue() );

        Box bottomBox = new Box( BoxLayout.X_AXIS );
        bottomBox.add( Box.createHorizontalStrut( 5 ) );
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

        this.add( topBox );
        this.add( Box.createVerticalStrut( 5 ) );
        this.add( bottomBox );
        this.add( Box.createVerticalStrut( 5 ) );
        this.add( forceExportLabel );
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        String msg = "";

        msg = Messages.getMessage( "VIEW_ACTION_NEW_BUTTON" );
        OpenVCEditDialogAction newAction = new OpenVCEditDialogAction( msg , true );
        newButton = new MamboActivableButton( newAction , newIcon , newIcon );
        GUIUtilities.setObjectBackground( newButton , GUIUtilities.VIEW_COLOR );

        msg = Messages.getMessage( "VIEW_GENERAL_NAME" );
        nameLabel = new JLabel( msg );
        Font newFont = nameLabel.getFont().deriveFont( Font.ITALIC );
        nameLabel.setFont( newFont );

        msg = Messages.getMessage( "VIEW_GENERAL_CREATION_DATE" );
        creationDateLabel = new JLabel( msg );
        creationDateLabel.setFont( newFont );

        msg = Messages.getMessage( "VIEW_GENERAL_LAST_UPDATE_DATE" );
        lastUpdateDateLabel = new JLabel( msg );
        lastUpdateDateLabel.setFont( newFont );

        forceExportLabel = new JLabel();
        forceExportLabel.setFont(forceExportFont);
        updateForceExport();

        creationDateField = new JLabel();
        lastUpdateDateField = new JLabel();
        nameField = new JLabel();
        pathField = new JLabel();

        stack = OpenedVCComboBox.getInstance( new LimitedVCStack() );

        closeButton = new JButton( CloseSelectedVCAction.getInstance( "" ) );
        ImageIcon newActionIcon = new ImageIcon( Mambo.class.getResource( "icons/remove.gif" ) );
        closeButton.setIcon( newActionIcon );
        closeButton.setPreferredSize( new Dimension( 22 , 20 ) );
    }

    public void updateForceExport()
    {
        ViewConfiguration vc = ViewConfiguration.getSelectedViewConfiguration();
        if (vc != null && vc.getData() != null && !vc.getData().isHistoric())
        {
            String msg;
            if (Options.getInstance().getVcOptions().isDoForceTdbExport())
            {
                msg = Messages.getMessage( "VIEW_ACTION_IS_EXPORT" );
                forceExportLabel.setForeground(new Color(150,0,0));
            }
            else
            {
                msg = Messages.getMessage( "VIEW_ACTION_IS_NOT_EXPORT" );
                forceExportLabel.setForeground(new Color(0,0,150));
            }
            forceExportLabel.setText(msg);
            forceExportLabel.setToolTipText(msg);
            msg = null;
            forceExportLabel.setVisible(true);
        }
        else
        {
            forceExportLabel.setVisible(false);
        }
        setLayout();
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

    /**
     * 19 juil. 2005
     */
    private void initBorder ()
    {
        String msg = Messages.getMessage( "VIEW_GENERAL_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );

    }


}
