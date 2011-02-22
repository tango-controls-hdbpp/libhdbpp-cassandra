// +======================================================================
//$Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextAttributesPanelAlternate.java,v
// $
//
//Project: Tango Archiving Service
//
//Description: Java source code for the class AttributesTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.6 $
//
//$Log: ContextAttributesPanelAlternate.java,v $
//Revision 1.6  2007/03/15 14:26:35  ounsy
//corrected the table mode add bug and added domains buffer
//
//Revision 1.5  2006/05/03 13:04:14  ounsy
//modified the limited operator rights
//
//Revision 1.4  2006/02/24 14:13:25  ounsy
//added support for domain *
//
//Revision 1.3  2006/02/15 09:17:53  ounsy
//minor changes
//
//Revision 1.2  2006/01/12 10:27:48  ounsy
//minor changes
//
//Revision 1.1 2005/12/14 16:53:47 ounsy
//added methods necessary for alternate attribute selection
//
//Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
//Second commit !
//
//
//copyleft : Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.context;

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

import fr.soleil.bensikin.actions.context.AddSelectedContextAttributeAlternateAction;
import fr.soleil.bensikin.actions.context.AlternateSelectionAction;
import fr.soleil.bensikin.actions.context.DomainsFilterAlternateAction;
import fr.soleil.bensikin.actions.context.ValidateAlternateSelectionAction;
import fr.soleil.bensikin.components.context.detail.AttributesSelectComboBox;
import fr.soleil.bensikin.components.context.detail.AttributesSelectTable;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.data.context.ContextAttributes;
import fr.soleil.bensikin.models.AttributesSelectTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


public class ContextAttributesPanelAlternate extends JPanel {
    private static ContextAttributesPanelAlternate instance = null;

    //---------LEFT HALF
    JPanel                                         leftHalf;
    
    private JLabel domainsLabel;
    private JTextField domainsField;
    private JButton domainsButton;
    
    private JLabel                                 domainLabel;
    private JLabel                                 deviceClassLabel;
    private JLabel                                 attributeLabel;
    private JButton                                okButton;
    private JLabel                                 selectionLabel;
    private JButton                                selectReverseButton;
    private JButton                                selectAllButton;
    private JButton                                selectNoneButton;
    private JButton                                selectValidateButton;
    private Box                                    selectBox;
    private AttributesSelectComboBox               domainComboBox;
    private AttributesSelectComboBox               deviceClassComboBox;
    private AttributesSelectComboBox               attributeComboBox;

    private JLabel bufferingStatusLabel;
    private String bufferingStatusLabelMsg;
    private static int step;
    private static int totalSteps;
    private static boolean bufferingWasCompleteBeforeThisWasInstantiated = false;
    
    //---------LEFT HALF

    //---------RIGHT HALF
    JPanel                                         rightHalf;
    JScrollPane                                    scrollpane;
    //---------RIGHT HALF
    
    private static boolean isInputEnabled = true;

    /**
     * @return 8 juil. 2005
     */
    public static ContextAttributesPanelAlternate getInstance() {
        if (instance == null) 
        {
            instance = new ContextAttributesPanelAlternate();
            instance.applyEnabled ();
        }

        return instance;
    }

    public void applyEnabled ()
    {
        if ( ! ContextAttributesPanelAlternate.isInputEnabled )
        {
            this.domainsField.setEnabled ( false );
            this.domainComboBox.setEnabled ( false );
            this.deviceClassComboBox.setEnabled ( false );
            this.attributeComboBox.setEnabled ( false );   
        }
    }

    public static void disableInput ()
    {
        ContextAttributesPanelAlternate.isInputEnabled  = false;
        if ( instance != null )
        {
            instance.applyEnabled ();
        }
    }
    
    /**
     *  
     */
    private ContextAttributesPanelAlternate() 
    {
        this.initComponents();
        this.addComponents();
        this.initLayout();
        
        if ( bufferingWasCompleteBeforeThisWasInstantiated )
        {
            this.doSetBufferingStatus ( step , totalSteps );
        }
    }
    
    public static void setBufferingStatus( int _step , int _totalSteps ) 
    {        
        if ( instance == null )
        {
            bufferingWasCompleteBeforeThisWasInstantiated = true;
            step = _step;
            totalSteps = _totalSteps;
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

    /**
     * 19 juil. 2005
     */
    private void initLayout() {
        //this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this, 2, 1, //rows, cols
                0, 0, //initX, initY
                0, 0, true); //xPad, yPad

        GUIUtilities.setObjectBackground(leftHalf, GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(okButton, GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(selectReverseButton,
                GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(selectNoneButton,
                GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(selectValidateButton,
                GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(selectAllButton,
                GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(this, GUIUtilities.CONTEXT_COLOR);
        GUIUtilities
                .setObjectBackground(scrollpane, GUIUtilities.CONTEXT_COLOR);
        GUIUtilities.setObjectBackground(scrollpane.getViewport(),
                GUIUtilities.CONTEXT_COLOR);

        scrollpane.setMinimumSize(new Dimension(
                leftHalf.getPreferredSize().width, 60));
        scrollpane.setPreferredSize(new Dimension(
                leftHalf.getPreferredSize().width, 150));
        //this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 385));
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() 
    {
        addComponentsLeft();
        addComponentsRight();

        this.add(leftHalf);
        this.add(scrollpane);
    }

    /**
     *  
     */
    private void addComponentsRight() 
    {
   
    }

    /**
     *  
     */
    private void addComponentsLeft() {
        leftHalf = new JPanel();
        Dimension emptyBoxDimension = new Dimension(40, 20);

        //START LINE 0
        leftHalf.add( Box.createHorizontalGlue () );
        leftHalf.add( bufferingStatusLabel );
        leftHalf.add( Box.createHorizontalGlue () );
        leftHalf.add( Box.createHorizontalGlue () );
        //START LINE 0
        
        //START LINE 0
        leftHalf.add( domainsLabel );
        leftHalf.add( domainsField );
        leftHalf.add( domainsButton );
        leftHalf.add( Box.createHorizontalGlue () );
        //START LINE 0
        
        //START LINE 1
        leftHalf.add(domainLabel);
        leftHalf.add(domainComboBox);
        leftHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        leftHalf.add( Box.createHorizontalGlue () );
        //START LINE 1

        //START LINE 2
        leftHalf.add(deviceClassLabel);
        leftHalf.add(deviceClassComboBox);
        leftHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        leftHalf.add( Box.createHorizontalGlue () );
        //END LINE 2

        //START LINE 3
        leftHalf.add(attributeLabel);
        leftHalf.add(attributeComboBox);
        leftHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        leftHalf.add( Box.createHorizontalGlue () );
        //END LINE 3

        //START LINE 4
        leftHalf.add(Box.createRigidArea(emptyBoxDimension));
        leftHalf.add(okButton);
        leftHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        leftHalf.add( Box.createHorizontalGlue () );
        //END LINE 4

        
        //START LINE 6
        leftHalf.add(selectionLabel);
        leftHalf.add(selectBox);
        leftHalf.add( Box.createRigidArea( emptyBoxDimension ) );
        leftHalf.add( Box.createHorizontalGlue () );
        //END LINE 6

        leftHalf.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                leftHalf, 
                7, 4, //rows, cols
                0, 0, //initX, initY
                6, 6, //xPad, yPad
                true
        );

        GUIUtilities.addDebugBorderToPanel(this, true, Color.BLACK, 0);

        leftHalf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents() {
        initComponentsLeft();
        initComponentsRight();
    }

    private void initComponentsRight() {

        Context currentContext = Context.getSelectedContext();
        ContextAttributes attributes = currentContext == null ? null
                : currentContext.getContextAttributes();
        ContextAttribute[] rows = attributes == null ? null : attributes
                .getContextAttributes();
        AttributesSelectTableModel tableModel = AttributesSelectTableModel
                .getInstance();
        tableModel.setRows(rows, true);

        scrollpane = new JScrollPane(AttributesSelectTable.getInstance());
    }

    private void initComponentsLeft() 
    {
        bufferingStatusLabelMsg = Messages.getMessage( "CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_BUFFERING_STATUS" )+ " :";
        bufferingStatusLabel = new JLabel ( bufferingStatusLabelMsg );
        Font bufferingStatusLabelFont = new Font( null , Font.ITALIC , 10 );
        bufferingStatusLabel.setFont ( bufferingStatusLabelFont );
        bufferingStatusLabel.setForeground ( Color.RED );
        
        leftHalf = new JPanel();
        String msg;
        
        msg = Messages.getMessage( "CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_DOMAINS" ) 
        		+ " :";
        domainsLabel = new JLabel( msg );
        
        msg = Messages.getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_DOMAIN")
                + " :";
        domainLabel = new JLabel(msg);

        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_DEVICE_CLASS")
                + " :";
        deviceClassLabel = new JLabel(msg);

        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_ATTRIBUTE")
                + " :";
        attributeLabel = new JLabel(msg);

        msg = Messages.getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_OK");
        okButton = new JButton(new AddSelectedContextAttributeAlternateAction(msg));
        okButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        domainsField = new JTextField ();
        msg = Messages.getMessage( "CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_DOMAINS_REGEXP" );
        domainsButton  = new JButton( new DomainsFilterAlternateAction( msg ) );
        Dimension size = new Dimension ( 40 , 25 );
        domainsButton.setMaximumSize ( size );
        domainsButton.setPreferredSize ( size );
        domainsButton.setSize ( size );
        
        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECTION")
                + " :";
        selectionLabel = new JLabel(msg);

        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECT_REVERSE_BUTTON");
        selectReverseButton = new JButton(new AlternateSelectionAction(msg,
                AlternateSelectionAction.SELECT_REVERSE_TYPE));

        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECT_NONE_BUTTON");
        selectNoneButton = new JButton(new AlternateSelectionAction(msg,
                AlternateSelectionAction.SELECT_NONE_TYPE));

        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECT_ALL_BUTTON");
        selectAllButton = new JButton(new AlternateSelectionAction(msg,
                AlternateSelectionAction.SELECT_ALL_TYPE));

        msg = Messages
                .getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECT_VALIDATE_BUTTON");
        selectValidateButton = new JButton(
                new ValidateAlternateSelectionAction(msg));

        selectBox = new Box(BoxLayout.X_AXIS);
        selectBox.add(selectReverseButton);
        selectBox.add(Box.createHorizontalStrut(5));
        selectBox.add(selectAllButton);
        selectBox.add(Box.createHorizontalStrut(5));
        selectBox.add(selectNoneButton);
        selectBox.add(Box.createHorizontalStrut(20));
        selectBox.add(Box.createHorizontalGlue());
        selectBox.add(selectValidateButton);

        domainComboBox = new AttributesSelectComboBox(
                AttributesSelectComboBox.DOMAIN_TYPE);
        deviceClassComboBox = new AttributesSelectComboBox(
                AttributesSelectComboBox.DEVICE_CLASS_TYPE);
        attributeComboBox = new AttributesSelectComboBox(
                AttributesSelectComboBox.ATTRIBUTE_TYPE);

    }

    /**
     * @return Returns the attributeComboBox.
     */
    public AttributesSelectComboBox getAttributeComboBox() {
        return attributeComboBox;
    }

    /**
     * @return Returns the deviceClassComboBox.
     */
    public AttributesSelectComboBox getDeviceClassComboBox() {
        return deviceClassComboBox;
    }

    /**
     * @return Returns the domainComboBox.
     */
    public AttributesSelectComboBox getDomainComboBox() {
        return domainComboBox;
    }
    
    public String getDomainsRegExp ()
    {
        return this.domainsField.getText (); 
    }
}