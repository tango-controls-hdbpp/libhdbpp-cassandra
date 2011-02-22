//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsVCTab.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.5 $
//
//$Log: OptionsVCTab.java,v $
//Revision 1.5  2007/05/10 14:48:16  ounsy
//possibility to change "no value" String in chart data file (default is "*") through vc option
//
//Revision 1.4  2006/08/31 08:45:19  ounsy
//forceExport is now false on defaults
//
//Revision 1.3  2006/07/13 12:50:50  ounsy
//added optionnal doForceTdbExport
//
//Revision 1.2  2006/05/19 15:05:29  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:28:13  chinkumo
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.VCOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class OptionsVCTab extends JPanel
{
    private static OptionsVCTab instance = null;

    private JRadioButton displayWriteYes;
    private JRadioButton displayWriteNo;
    private ButtonGroup writeButtonGroup;

    private JRadioButton displayReadYes;
    private JRadioButton displayReadNo;
    private ButtonGroup readButtonGroup;
    
    private JRadioButton forceTdbExportYes;
    private JRadioButton forceTdbExportNo;
    private ButtonGroup forceTdbExportButtonGroup;

    private JLabel stackDepthLabel;
    private JTextField stackDepthField;

    private Box writeBox;
    private Box readBox;
    private Box forceTdbExportBox;
    private Box emptyBox;
    private JPanel miscPanel;

    private JPanel chartPanel;
    private JLabel noValueLabel;
    private JComboBox noValueBox;
    protected final static String[]  noValueStringChoice = {"*",".","-","+","#","X","!"};
    //private Box miscBox;


    /**
     * @return 8 juil. 2005
     */
    public static OptionsVCTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsVCTab();
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

    private void initLayout ()
    {
        this.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid
                ( this ,
                  5 , 1 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true );
    }


    /**
     *
     */
    private OptionsVCTab ()
    {

        this.initComponents();
        this.addComponents();
        this.initLayout();
    }


    /**
     *
     */
    private void addComponents ()
    {
        this.add( readBox );
        this.add( writeBox );
        this.add( forceTdbExportBox );
        this.add( miscPanel );
        this.add( chartPanel );


        this.add( emptyBox );
    }

    /**
     *
     */
    private void initComponents ()
    {
        forceTdbExportButtonGroup = new ButtonGroup();

        String msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_FORCE_TDB_EXPORT_YES" );
        forceTdbExportYes = new JRadioButton( msg , true );
        forceTdbExportYes.setActionCommand( String.valueOf( VCOptions.FORCE_TDB_EXPORT_YES ) );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_FORCE_TDB_EXPORT_NO" );
        forceTdbExportNo = new JRadioButton( msg , false );
        forceTdbExportNo.setActionCommand( String.valueOf( VCOptions.FORCE_TDB_EXPORT_NO ) );
        forceTdbExportNo.setSelected ( true );
        
        forceTdbExportButtonGroup.add( forceTdbExportYes );
        forceTdbExportButtonGroup.add( forceTdbExportNo );

        forceTdbExportBox = new Box( BoxLayout.Y_AXIS );
        forceTdbExportBox.add( forceTdbExportYes );
        forceTdbExportBox.add( Box.createVerticalStrut( 5 ) );
        //box.add ( Box.createVerticalGlue () );
        forceTdbExportBox.add( forceTdbExportNo );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_FORCE_TDB_EXPORT_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        forceTdbExportBox.setBorder( tb );
        
        //-----------
        writeButtonGroup = new ButtonGroup();

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_DISPLAY_WRITE_YES" );
        displayWriteYes = new JRadioButton( msg , true );
        displayWriteYes.setActionCommand( String.valueOf( VCOptions.DISPLAY_WRITE_YES ) );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_DISPLAY_WRITE_NO" );
        displayWriteNo = new JRadioButton( msg , false );
        displayWriteNo.setActionCommand( String.valueOf( VCOptions.DISPLAY_WRITE_NO ) );

        writeButtonGroup.add( displayWriteYes );
        writeButtonGroup.add( displayWriteNo );

        writeBox = new Box( BoxLayout.Y_AXIS );
        writeBox.add( displayWriteYes );
        writeBox.add( Box.createVerticalStrut( 5 ) );
        //box.add ( Box.createVerticalGlue () );
        writeBox.add( displayWriteNo );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_DISPLAY_WRITE_BORDER" );
        tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        writeBox.setBorder( tb );


        readButtonGroup = new ButtonGroup();

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_DISPLAY_READ_YES" );
        displayReadYes = new JRadioButton( msg , true );
        displayReadYes.setActionCommand( String.valueOf( VCOptions.DISPLAY_READ_YES ) );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_DISPLAY_READ_NO" );
        displayReadNo = new JRadioButton( msg , false );
        displayReadNo.setActionCommand( String.valueOf( VCOptions.DISPLAY_READ_NO ) );

        readButtonGroup.add( displayReadYes );
        readButtonGroup.add( displayReadNo );

        readBox = new Box( BoxLayout.Y_AXIS );
        readBox.add( displayReadYes );
        readBox.add( Box.createVerticalStrut( 5 ) );
        //box.add ( Box.createVerticalGlue () );
        readBox.add( displayReadNo );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_DISPLAY_READ_BORDER" );
        TitledBorder tb2 = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        readBox.setBorder( tb2 );

        emptyBox = new Box( BoxLayout.Y_AXIS );
        emptyBox.add( Box.createVerticalGlue() );


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

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_CHART_NO_VALUE_STRING" );
        noValueLabel = new JLabel( msg );
        noValueBox = new JComboBox();
        noValueBox.setMaximumSize( new Dimension( 40 , 20 ) );
        noValueBox.setPreferredSize( new Dimension( 40 , 20 ) );
        for (int i = 0; i < noValueStringChoice.length; i++) {
            noValueBox.addItem( noValueStringChoice[i] );
        }

        chartPanel = new JPanel();

        chartPanel.add( noValueLabel );
        chartPanel.add( noValueBox );

        chartPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid
                ( chartPanel ,
                  1 , 2 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_VC_CHART_BORDER" );
        TitledBorder tb4 = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        chartPanel.setBorder( tb4 );
    }

    /**
     * @return 8 juil. 2005
     */
    public ButtonGroup getWriteButtonGroup ()
    {
        return writeButtonGroup;
    }


    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectDisplayWrite ( int displayCase )
    {
        switch ( displayCase )
        {
            case VCOptions.DISPLAY_WRITE_YES:
                displayWriteYes.setSelected( true );
                break;

            case VCOptions.DISPLAY_WRITE_NO:
                displayWriteNo.setSelected( true );
                break;
        }
    }


    /**
     * @return Returns the displayWriteNo.
     */
    public JRadioButton getDisplayWriteNo ()
    {
        return displayWriteNo;
    }

    /**
     * @return Returns the displayWriteYes.
     */
    public JRadioButton getDisplayWriteYes ()
    {
        return displayWriteYes;
    }

    /**
     * @return 8 juil. 2005
     */
    public ButtonGroup getReadButtonGroup ()
    {
        return readButtonGroup;
    }


    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectDisplayRead ( int displayCase )
    {
        switch ( displayCase )
        {
            case VCOptions.DISPLAY_READ_YES:
                displayReadYes.setSelected( true );
                break;

            case VCOptions.DISPLAY_READ_NO:
                displayReadNo.setSelected( true );
                break;
        }
    }
    
    public void selectForceTdbExport ( int displayCase )
    {
        //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/displayCase/"+displayCase );
        switch ( displayCase )
        {
            case VCOptions.FORCE_TDB_EXPORT_YES:
                //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/0" );
                forceTdbExportYes.setSelected( true );
                break;

            case VCOptions.FORCE_TDB_EXPORT_NO:
                //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/1" );
                forceTdbExportNo.setSelected( true );
                break;
                
            default:
                //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/2" );
                forceTdbExportNo.setSelected( true );
            break;
        }
    }

    

    /**
     * @return Returns the displayReadNo.
     */
    public JRadioButton getDisplayReadNo ()
    {
        return displayReadNo;
    }

    /**
     * @return Returns the displayReadYes.
     */
    public JRadioButton getDisplayReadYes ()
    {
        return displayReadYes;
    }

    /**
     * @return
     */
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
     * @return Returns the forceTdbExportButtonGroup.
     */
    public ButtonGroup getForceTdbExportButtonGroup() {
        return this.forceTdbExportButtonGroup;
    }

    public void setNoValueString (String noValueString) {
        for (int i = 0; i < noValueBox.getItemCount(); i++) {
            if ( noValueBox.getItemAt(i).equals(noValueString ) ) {
                noValueBox.setSelectedIndex(i);
                return;
            }
        }
    }

    public String getNoValueString() {
        return noValueBox.getSelectedItem().toString();
    }
}
