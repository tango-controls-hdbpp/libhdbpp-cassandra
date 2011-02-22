//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsSaveOptionsTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsSaveOptionsTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OptionsSaveOptionsTab.java,v $
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
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
package fr.soleil.mambo.containers.sub.dialogs.options;

//import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.SaveOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class OptionsSaveOptionsTab extends JPanel
{
    private static OptionsSaveOptionsTab instance = null;

    private JRadioButton yesButton;
    private JRadioButton noButton;
    //private JLabel label;
    private ButtonGroup buttonGroup;

    //private Dimension dim = new Dimension( 300 , 300 );
    //private JPanel myPanel;

    private Box emptyBox;
    private Box saveHistoryPanel;

    /**
     * @return 8 juil. 2005
     */
    public static OptionsSaveOptionsTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsSaveOptionsTab();
        }

        return instance;
    }

    public static void resetInstance ()
    {
        if ( instance != null )
        {
            instance.repaint();
        }

        instance = null;
    }

    /**
     * 
     */
    private OptionsSaveOptionsTab ()
    {

        this.initComponents();
        this.addComponents();
        this.initLayout();


    }

    /**
     * 5 juil. 2005
     */
    private void initLayout ()
    {
        this.setLayout( new SpringLayout() );

        SpringUtilities.makeCompactGrid( this ,
                                         2 , 1 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad

    }

    /**
     * 5 juil. 2005
     */
    /*private void addComponents() 
    {
        Dimension emptyBoxDimension = new Dimension ( 20 , 2 );

        myPanel = new JPanel ();
        this.add ( myPanel );
        
        //Create and populate the panel.
       
        //START ROW 1
        myPanel.add ( Box.createRigidArea ( emptyBoxDimension ) );
        myPanel.add ( Box.createRigidArea ( emptyBoxDimension ) );
        myPanel.add ( yesButton );
        //END ROW 1
        
        //START ROW 2
        myPanel.add ( label );
        myPanel.add ( Box.createRigidArea ( emptyBoxDimension ) );
        myPanel.add ( Box.createRigidArea ( emptyBoxDimension ) );
        //END ROW 2
        
        //START ROW 3
        myPanel.add ( Box.createRigidArea ( emptyBoxDimension ) );
        myPanel.add ( Box.createRigidArea ( emptyBoxDimension ) );
        myPanel.add ( noButton );
        //END ROW 3
        myPanel.setPreferredSize ( new Dimension ( 300 , 60 ) );
        //GUIUtilities.addDebugBorderToPanel ( myPanel , true , Color.RED , 2 );
    }*/
    private void addComponents ()
    {
        this.add( saveHistoryPanel );
        this.add( emptyBox );
    }

    /**
     * 5 juil. 2005
     */
    private void initComponents ()
    {
        buttonGroup = new ButtonGroup();

        //String msgSaveHistoryOnShutdown = Messages.getMessage( "DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN" );
        String msgYes = Messages.getMessage( "DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_YES" );
        String msgNo = Messages.getMessage( "DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_NO" );

        //label = new JLabel( msgSaveHistoryOnShutdown );

        yesButton = new JRadioButton( msgYes , true );
        noButton = new JRadioButton( msgNo , true );

        yesButton.setActionCommand( String.valueOf( SaveOptions.HISTORY_YES ) );
        noButton.setActionCommand( String.valueOf( SaveOptions.HISTORY_NO ) );

        /*yesButton.setPreferredSize ( new Dimension ( 20 , 10 ) );
        noButton.setPreferredSize ( new Dimension ( 20 , 10 ) );*/
        //label.setPreferredSize ( new Dimension ( 20 , 10 ) );

        buttonGroup.add( yesButton );
        buttonGroup.add( noButton );

        emptyBox = new Box( BoxLayout.Y_AXIS );
        emptyBox.add( Box.createVerticalGlue() );

        saveHistoryPanel = new Box( BoxLayout.Y_AXIS );
        saveHistoryPanel.add( yesButton );
        saveHistoryPanel.add( Box.createVerticalStrut( 10 ) );
        saveHistoryPanel.add( noButton );

        String msg = Messages.getMessage( "DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN" );
        TitledBorder tb3 = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        saveHistoryPanel.setBorder( tb3 );

    }

    /**
     * @return 8 juil. 2005
     */
    public ButtonGroup getButtonGroup ()
    {
        return buttonGroup;
    }

    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectHasSaveButton ( int hasSave )
    {
        switch ( hasSave )
        {
            case SaveOptions.HISTORY_YES:
                yesButton.setSelected( true );
                break;

            case SaveOptions.HISTORY_NO:
                noButton.setSelected( true );
                break;
        }
    }
}
