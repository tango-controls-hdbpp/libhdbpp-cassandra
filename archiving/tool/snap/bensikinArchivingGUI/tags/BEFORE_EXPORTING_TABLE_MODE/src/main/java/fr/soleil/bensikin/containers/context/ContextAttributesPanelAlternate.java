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

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

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
            domainsField.setEnabled ( false );
            domainComboBox.setEnabled ( false );
            deviceClassComboBox.setEnabled ( false );
            attributeComboBox.setEnabled ( false );
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

        scrollpane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 60));
        scrollpane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));
        //this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 385));
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents()
    {
        addComponentsLeft();
        addComponentsRight();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(leftHalf);
        this.add(Box.createVerticalStrut(7));
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
		GroupLayout layout = new GroupLayout(leftHalf);
		leftHalf.setLayout(layout);

		layout.setAutocreateGaps(true);

		SequentialGroup hGroup = layout.createSequentialGroup();
/*
		hGroup.add(layout.createParallelGroup(GroupLayout.LEADING, false)
				.add(domainsLabel)
				.add(domainLabel)
				.add(deviceClassLabel)
				.add(attributeLabel)
				.add(selectionLabel));
		hGroup.add(layout.createParallelGroup(GroupLayout.LEADING, true)
				.add(bufferingStatusLabel)
				.add(domainsField)
				.add(domainComboBox)
				.add(deviceClassComboBox)
				.add(attributeComboBox)
				.add(okButton)
				.add(layout.createSequentialGroup()
				//il y a un problème de layout incompréhensible avec ce code, si on enlève un bouton ça marche bien...
						.add(selectReverseButton)
						.add(selectAllButton)
						.add(selectNoneButton)
						.addPreferredGap(selectNoneButton, selectValidateButton, LayoutStyle.UNRELATED, true)
						.add(selectValidateButton)));
		hGroup.add(domainsButton);
*/

		hGroup.add(layout.createParallelGroup(GroupLayout.LEADING, false)
				.add(domainsLabel)
				.add(domainLabel)
				.add(deviceClassLabel)
				.add(attributeLabel)
				.add(selectionLabel));
		hGroup.add(layout.createParallelGroup(GroupLayout.LEADING, true)
				.add(layout.createSequentialGroup()
						.add(layout.createParallelGroup(GroupLayout.LEADING, true)
								.add(bufferingStatusLabel)
								.add(domainsField)
								.add(domainComboBox)
								.add(deviceClassComboBox)
								.add(attributeComboBox)
								.add(okButton))
						.add(domainsButton))
				.add(layout.createSequentialGroup()
						.add(selectReverseButton)
						.add(selectAllButton)
						.add(selectNoneButton)
						.addPreferredGap(selectNoneButton, selectValidateButton, LayoutStyle.UNRELATED, true)
						.add(selectValidateButton)));
		layout.setHorizontalGroup(hGroup);


		SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.add(bufferingStatusLabel);
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(domainsLabel)
				.add(domainsField)
				.add(domainsButton));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(domainLabel)
				.add(domainComboBox));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(deviceClassLabel)
				.add(deviceClassComboBox));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(attributeLabel)
				.add(attributeComboBox));
		vGroup.add(okButton);
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(selectionLabel)
				.add(selectReverseButton)
				.add(selectAllButton)
				.add(selectNoneButton)
				.add(selectValidateButton));
		layout.setVerticalGroup(vGroup);
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
//        Dimension size = new Dimension ( 40 , 25 );
//        domainsButton.setMaximumSize ( size );
//        domainsButton.setPreferredSize ( size );
//        domainsButton.setSize ( size );

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
        return domainsField.getText ();
    }
}