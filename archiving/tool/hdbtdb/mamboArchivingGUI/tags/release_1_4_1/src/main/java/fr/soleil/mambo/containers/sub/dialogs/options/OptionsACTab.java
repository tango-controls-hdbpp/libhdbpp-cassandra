//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsACTab.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: OptionsACTab.java,v $
//Revision 1.1  2005/11/29 18:28:12  chinkumo
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
package fr.soleil.mambo.containers.sub.dialogs.options;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class OptionsACTab extends JPanel
{
    private static OptionsACTab instance = null;

    private JRadioButton treeSelectionMode;
    private JRadioButton tableSelectionMode;
    private ButtonGroup buttonGroup;

    private Box box;
    private ACTabDefaultPanel defaultPanel;
    private JPanel miscPanel;

    private JLabel stackDepthLabel;
    private JTextField stackDepthField;

    /**
     * @return 8 juil. 2005
     */
    public static OptionsACTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsACTab();
        }

        return instance;
    }

    /**
     * @return 8 juil. 2005
     */
    public static void resetInstance ()
    {
        if ( instance != null )
        {
            instance.repaint();
        }

        instance = null;

        ACTabDefaultPanel.resetInstance();
    }

    private void initLayout ()
    {
        this.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid( this ,
                                         3 , 1 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad
    }


    /**
     *
     */
    private OptionsACTab ()
    {

        this.initComponents();
        this.addComponents();
        this.initLayout();

        //this.setIgnoreRepaint ( false );
    }


    /**
     *
     */
    private void addComponents ()
    {
        this.add( box );
        this.add( defaultPanel );
        this.add( miscPanel );
    }

    /**
     *
     */
    private void initComponents ()
    {
        buttonGroup = new ButtonGroup();

        String msg = Messages.getMessage( "DIALOGS_OPTIONS_AC_SELECTION_TREE" );
        treeSelectionMode = new JRadioButton( msg , true );
        treeSelectionMode.setActionCommand( String.valueOf( ACOptions.SELECTION_MODE_TREE ) );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_AC_SELECTION_TABLE" );
        tableSelectionMode = new JRadioButton( msg , false );
        tableSelectionMode.setActionCommand( String.valueOf( ACOptions.SELECTION_MODE_TABLE ) );

        buttonGroup.add( treeSelectionMode );
        buttonGroup.add( tableSelectionMode );

        box = new Box( BoxLayout.Y_AXIS );
        box.add( treeSelectionMode );
        box.add( Box.createVerticalStrut( 5 ) );
        box.add( Box.createVerticalGlue() );
        box.add( tableSelectionMode );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_AC_SELECTION_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        box.setBorder( tb );

        //---------------
        defaultPanel = ACTabDefaultPanel.getInstance();
        msg = Messages.getMessage( "DIALOGS_OPTIONS_AC_DEFAULT_BORDER" );
        TitledBorder tb2 = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        defaultPanel.setBorder( tb2 );

        //---------------
        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_MISC_STACK_DEPTH" );
        stackDepthLabel = new JLabel( msg );
        stackDepthField = new JTextField();
        stackDepthField.setMaximumSize( new Dimension( 50 , 20 ) );
        stackDepthField.setPreferredSize( new Dimension( 50 , 20 ) );

        miscPanel = new JPanel();

        miscPanel.add( stackDepthLabel );
        miscPanel.add( stackDepthField );
        miscPanel.add( Box.createHorizontalGlue() );

        miscPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid
                ( miscPanel ,
                  1 , 3 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_MISC_BORDER" );
        TitledBorder tb3 = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        miscPanel.setBorder( tb3 );
    }

    /**
     * @return 8 juil. 2005
     */
    public ButtonGroup getButtonGroup ()
    {
        return buttonGroup;
    }

    public String getStackDepth ()
    {
        return stackDepthField.getText();
    }

    /**
     * @param stackDepth_s
     */
    public void setStackDepth ( String stackDepth_s )
    {
        stackDepthField.setText( stackDepth_s );
    }

    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectPlafButton ( int plaf )
    {
        switch ( plaf )
        {
            case ACOptions.SELECTION_MODE_TREE:
                treeSelectionMode.setSelected( true );
                break;

            case ACOptions.SELECTION_MODE_TABLE:
                tableSelectionMode.setSelected( true );
                break;
        }
    }

    /**
     * @return Returns the tableSelectionMode.
     */
    public JRadioButton getTableSelectionMode ()
    {
        return tableSelectionMode;
    }

    /**
     * @return Returns the treeSelectionMode.
     */
    public JRadioButton getTreeSelectionMode ()
    {
        return treeSelectionMode;
    }

    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectIsAlternate ( boolean _isAlternate )
    {
        if ( _isAlternate )
        {
            tableSelectionMode.setSelected( true );
        }
        else
        {
            treeSelectionMode.setSelected( true );
        }
    }
}
