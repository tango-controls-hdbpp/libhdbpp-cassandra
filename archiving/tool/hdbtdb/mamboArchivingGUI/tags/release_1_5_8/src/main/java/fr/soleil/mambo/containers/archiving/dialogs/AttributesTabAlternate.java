//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/dialogs/AttributesTabAlternate.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AttributesTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.8 $
//
//$Log: AttributesTabAlternate.java,v $
//Revision 1.8  2007/03/15 14:28:03  ounsy
//minor changes
//
//Revision 1.7  2006/10/06 16:02:31  ounsy
//minor changes (L&F)
//
//Revision 1.6  2006/09/22 14:52:49  ounsy
//added a little display of the alternate selection buffering status
//
//Revision 1.5  2006/05/19 13:45:13  ounsy
//minor changes
//
//Revision 1.4  2006/05/16 12:49:41  ounsy
//modified imports
//
//Revision 1.3  2006/03/15 16:10:28  ounsy
//minor changes
//
//Revision 1.2  2006/02/24 12:25:38  ounsy
//modified for HDB/TDB separation
//
//Revision 1.1  2005/11/29 18:27:56  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.actions.archiving.AddSelectedACAttributeAlternateAction;
import fr.soleil.mambo.actions.archiving.AlternateSelectionAction;
import fr.soleil.mambo.actions.archiving.DomainsFilterAlternateAction;
import fr.soleil.mambo.components.archiving.AttributesSelectComboBox;
import fr.soleil.mambo.components.archiving.AttributesSelectTable;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.models.AttributesSelectTableModel;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class AttributesTabAlternate extends JPanel
{
    private static AttributesTabAlternate instance = null;

    //---------LEFT HALF
    JPanel topHalf;

    private JLabel domainsLabel;
    private JTextField domainsField;
    private JButton domainsButton;
    private JLabel domainLabel;
    private JLabel deviceClassLabel;
    private JLabel attributeLabel;
    private JButton okButton;
    private JLabel selectionLabel;
    private JButton selectReverseButton;
    private JButton selectAllButton;
    private JButton selectNoneButton;
    private Box selectBox;

    private JLabel bufferingStatusLabel;
    private String bufferingStatusLabelMsg;
    private static int step;
    private static int totalSteps;
    private static boolean bufferingWasCompleteBeforeThisWasInstantiated = false;
    
    private AttributesSelectComboBox domainComboBox;
    private AttributesSelectComboBox deviceClassComboBox;
    private AttributesSelectComboBox attributeComboBox;
    //---------LEFT HALF

    //---------RIGHT HALF
    JScrollPane scrollpane;
    //---------RIGHT HALF

    /**
     * @return 8 juil. 2005
     */
    public static AttributesTabAlternate getInstance ()
    {
        if ( instance == null )
        {
            instance = new AttributesTabAlternate();
        }

        return instance;
    }
    
    /**
     * @return 8 juil. 2005
     */
    public static AttributesTabAlternate getCurrentInstance ()
    {
        return instance;
    }

    /**
     *
     */
    private AttributesTabAlternate ()
    {
        this.initComponents();
        this.addComponents();
        this.initLayout();
        
        if ( AttributesTabAlternate.bufferingWasCompleteBeforeThisWasInstantiated )
        {
            this.doSetBufferingStatus ( AttributesTabAlternate.step , AttributesTabAlternate.totalSteps );
        }
    }

    /**
     * 19 juil. 2005
     */
    private void initLayout ()
    {
        this.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                this,
                this.getComponentCount(), 1, // rows, cols
                0, 10, // initX, initY
                0, 10, // Xpad, Ypad
                true  // components same size
        );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        addComponentsTop();
        this.add( topHalf );
        this.add( scrollpane );
    }

    /**
     * 
     */
    private void addComponentsTop ()
    {
        topHalf = new JPanel();
        Dimension emptyBoxDimension = new Dimension( 40 , 20 );
        
        //START LINE 0
        topHalf.add( domainsLabel );
        topHalf.add( domainsField );
        topHalf.add( domainsButton );
        topHalf.add( bufferingStatusLabel );
        topHalf.add( Box.createHorizontalGlue () );
        //START LINE 0
        
        //START LINE 1
        topHalf.add( domainLabel );
        topHalf.add( domainComboBox );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createHorizontalGlue () );
        topHalf.add( Box.createHorizontalGlue () );
        //START LINE 1

        //START LINE 2
        topHalf.add( deviceClassLabel );
        topHalf.add( deviceClassComboBox );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createHorizontalGlue () );
        topHalf.add( Box.createHorizontalGlue () );
        //END LINE 2

        //START LINE 3
        topHalf.add( attributeLabel );
        topHalf.add( attributeComboBox );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createHorizontalGlue () );
        topHalf.add( Box.createHorizontalGlue () );
        //END LINE 3

        //START LINE 4
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( okButton );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createHorizontalGlue () );
        topHalf.add( Box.createHorizontalGlue () );
        //END LINE 4

        //START LINE 5
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createHorizontalGlue () );
        topHalf.add( Box.createHorizontalGlue () );
        //END LINE 5

        //START LINE 6
        topHalf.add( selectionLabel );
        topHalf.add( selectBox );
        topHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        topHalf.add( Box.createHorizontalGlue () );
        topHalf.add( Box.createHorizontalGlue () );
        //END LINE 6

        topHalf.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid
                ( topHalf ,
                  7 , 5 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true );

        topHalf.setMaximumSize( new Dimension( Integer.MAX_VALUE , 100 ) );
        //GUIUtilities.addDebugBorderToPanel ( topHalf, true , Color.RED , 3 );
    }
    

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        initComponentsTop();
        initComponentsBottom();

    }

    private void initComponentsBottom ()
    {
        ArchivingConfiguration currentArchivingConfiguration = ArchivingConfiguration.getCurrentArchivingConfiguration();
        ArchivingConfigurationAttributes attributes = currentArchivingConfiguration.getAttributes();
        ArchivingConfigurationAttribute[] rows = attributes.getAttributesList();

        AttributesSelectTableModel tableModel = AttributesSelectTableModel.getInstance();
        tableModel.setRows( rows );

        scrollpane = new JScrollPane( AttributesSelectTable.getInstance() );
        scrollpane.setMinimumSize(new Dimension(50, 100));
        scrollpane.setPreferredSize(new Dimension(50, 100));
        scrollpane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
    }

    public static void setBufferingStatus( int _step , int _totalSteps ) 
    {
        if ( instance == null )
        {
            AttributesTabAlternate.bufferingWasCompleteBeforeThisWasInstantiated = true;
            AttributesTabAlternate.step = _step;
            AttributesTabAlternate.totalSteps = _totalSteps;
        }
        else
        {
            instance.doSetBufferingStatus ( _step , _totalSteps );
        }
    }
    
    private void doSetBufferingStatus( int _step , int _totalSteps ) 
    {
        String text = "(" + bufferingStatusLabelMsg + _step + "/" + _totalSteps + ")";
        if ( bufferingStatusLabel != null )
        {
            bufferingStatusLabel.setText ( text );
            if ( _step == _totalSteps )
            {
                bufferingStatusLabel.setForeground ( Color.GREEN.darker() );
            }
        }
    }
    
    private void initComponentsTop ()
    {
        topHalf = new JPanel();
        String msg;
        
        bufferingStatusLabelMsg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_BUFFERING_STATUS" )+ " :";
        bufferingStatusLabel = new JLabel ( bufferingStatusLabelMsg );
        Font bufferingStatusLabelFont = new Font( null , Font.ITALIC , 10 );
        bufferingStatusLabel.setFont ( bufferingStatusLabelFont );
        bufferingStatusLabel.setForeground ( Color.RED );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_DOMAINS" ) + " :";
        domainsLabel = new JLabel( msg );
        
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_DOMAIN" ) + " :";
        domainLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_DEVICE_CLASS" ) + " :";
        deviceClassLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_ATTRIBUTE" ) + " :";
        attributeLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_OK" );
        okButton = new JButton( new AddSelectedACAttributeAlternateAction( msg ) );

        domainsField = new JTextField ();
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_DOMAINS_REGEXP" );
        domainsButton  = new JButton( new DomainsFilterAlternateAction( msg ) );
        
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_SELECTION" ) + " :";
        selectionLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_SELECT_REVERSE_BUTTON" );
        selectReverseButton = new JButton( new AlternateSelectionAction( msg , AlternateSelectionAction.SELECT_REVERSE_TYPE ) );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_SELECT_NONE_BUTTON" );
        selectNoneButton = new JButton( new AlternateSelectionAction( msg , AlternateSelectionAction.SELECT_NONE_TYPE ) );

        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_SELECT_ALL_BUTTON" );
        selectAllButton = new JButton( new AlternateSelectionAction( msg , AlternateSelectionAction.SELECT_ALL_TYPE ) );

        selectBox = new Box( BoxLayout.X_AXIS );
        selectBox.add( selectReverseButton );
        selectBox.add( Box.createHorizontalStrut( 5 ) );
        selectBox.add( selectAllButton );
        selectBox.add( Box.createHorizontalStrut( 5 ) );
        selectBox.add( selectNoneButton );

        domainComboBox = new AttributesSelectComboBox( AttributesSelectComboBox.DOMAIN_TYPE );
        deviceClassComboBox = new AttributesSelectComboBox( AttributesSelectComboBox.DEVICE_CLASS_TYPE );
        attributeComboBox = new AttributesSelectComboBox( AttributesSelectComboBox.ATTRIBUTE_TYPE );
    }

    /**
     * @return Returns the attributeComboBox.
     */
    public AttributesSelectComboBox getAttributeComboBox ()
    {
        return attributeComboBox;
    }

    /**
     * @return Returns the deviceClassComboBox.
     */
    public AttributesSelectComboBox getDeviceClassComboBox ()
    {
        return deviceClassComboBox;
    }

    /**
     * @return Returns the domainComboBox.
     */
    public AttributesSelectComboBox getDomainComboBox ()
    {
        return domainComboBox;
    }
    
    public String getDomainsRegExp ()
    {
        return this.domainsField.getText (); 
    }
}
