//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsSnapshotTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsMiscTab.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OptionsSnapshotTab.java,v $
// Revision 1.3  2006/06/28 12:49:44  ounsy
// minor changes
//
// Revision 1.2  2005/12/14 16:30:08  ounsy
// added the CSV separator option
//
// Revision 1.1  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:37  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.sub.dialogs.options;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


/**
 * The snapshots tab of OptionsDialog, used to set the snapshot specific options of the application.
 *
 * @author CLAISSE
 */
public class OptionsSnapshotTab extends JPanel
{
    private static OptionsSnapshotTab instance = null;

    JLabel separatorLabel;
    JComboBox separatorComboBox;
    Box miscBox;

    //---
    Box autoCommentBox;
    JPanel showPanel;

    JRadioButton autoSnapshotCommentYes;
    JRadioButton autoSnapshotCommentNo;
    JTextField defaultSnapshotComment;
    ButtonGroup buttonGroup;

    //------
    JLabel showReadLabel;
    JRadioButton showReadYes;
    JRadioButton showReadNo;
    ButtonGroup showReadGroup;

    JLabel showWriteLabel;
    JRadioButton showWriteYes;
    JRadioButton showWriteNo;
    ButtonGroup showWriteGroup;

    JLabel showDeltaLabel;
    JRadioButton showDeltaYes;
    JRadioButton showDeltaNo;
    ButtonGroup showDeltaGroup;

    JLabel showDiffLabel;
    JRadioButton showDiffYes;
    JRadioButton showDiffNo;
    ButtonGroup showDiffGroup;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static OptionsSnapshotTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsSnapshotTab();
        }

        return instance;
    }

    /**
     * Builds the tab.
     */
    private OptionsSnapshotTab ()
    {
        this.initComponents();
        this.initLayout();
        this.addComponents();
    }


    /**
     * Inits the tab's layout.
     */
    private void initLayout ()
    {
        //Dimension emptyBoxDimension = new Dimension( 20 , 2 );
        showPanel = new JPanel();

        //Create and populate the panel.

        //START ROW 1
        showPanel.add( showReadLabel );
        showPanel.add( showReadYes );
        showPanel.add( showReadNo );
        //END ROW 1

        //START ROW 2
        showPanel.add( showWriteLabel );
        showPanel.add( showWriteYes );
        showPanel.add( showWriteNo );
        //END ROW 2

        //START ROW 3
        showPanel.add( showDeltaLabel );
        showPanel.add( showDeltaYes );
        showPanel.add( showDeltaNo );
        //END ROW 3

        //START ROW 4
        showPanel.add( new JSeparator() );
        showPanel.add( new JSeparator() );
        showPanel.add( new JSeparator() );
        //END ROW 4

        //START ROW 5
        showPanel.add( showDiffLabel );
        showPanel.add( showDiffYes );
        showPanel.add( showDiffNo );
        //END ROW 5

        showPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid
                ( showPanel ,
                  5 , 3 , //rows, cols
                  6 , 6 , //initX, initY
                  6 , 6 , //xPad, yPad
                  true );

        String msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getOptionsTitleFont() );
        showPanel.setBorder( tb );

        this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
    }

    /**
     * Adds the initialized components to the tab.
     */
    private void addComponents ()
    {
        Box showPanelContainer = new Box( BoxLayout.Y_AXIS );
        showPanelContainer.add( showPanel );

        this.add( autoCommentBox );
        this.add( Box.createVerticalStrut( 20 ) );
        this.add( showPanelContainer );
        this.add( Box.createVerticalStrut( 20 ) );
        this.add( miscBox );
        this.add( Box.createVerticalGlue() );
    }

    /**
     * Inits the tab's components.
     */
    private void initComponents ()
    {
        buttonGroup = new ButtonGroup();

        String msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_HAS_AUTO_SNAPSHOT_COMMENT_YES" );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_HAS_AUTO_SNAPSHOT_COMMENT_YES" );
        autoSnapshotCommentYes = new JRadioButton( msg , true );
        autoSnapshotCommentYes.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_AUTO_COMMENT_YES ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_HAS_AUTO_SNAPSHOT_COMMENT_NO" );
        autoSnapshotCommentNo = new JRadioButton( msg , false );
        autoSnapshotCommentNo.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_AUTO_COMMENT_NO ) );
        buttonGroup.add( autoSnapshotCommentYes );
        buttonGroup.add( autoSnapshotCommentNo );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_DEFAULT_SNAPSHOT_COMMENT" );
        JLabel defaultCommentLabel = new JLabel( msg );
        defaultSnapshotComment = new JTextField();
        defaultSnapshotComment.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
        Box defaultCommentBox = new Box( BoxLayout.X_AXIS );
        defaultCommentBox.add( defaultCommentLabel );
        defaultCommentBox.add( Box.createHorizontalStrut( 5 ) );
        defaultCommentBox.add( defaultSnapshotComment );

        autoCommentBox = new Box( BoxLayout.Y_AXIS );
        autoCommentBox.add( autoSnapshotCommentYes );
        autoCommentBox.add( Box.createVerticalStrut( 5 ) );
        autoCommentBox.add( autoSnapshotCommentNo );
        autoCommentBox.add( Box.createVerticalStrut( 10 ) );
        autoCommentBox.add( defaultCommentBox );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_AUTO_COMMENT" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getOptionsTitleFont() );
        autoCommentBox.setBorder( tb );
        
        //-------------
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_CSV_SEPARATOR" );
        separatorLabel = new JLabel ( msg );
        separatorComboBox = new JComboBox ();
        separatorComboBox.addItem( ";" );
        separatorComboBox.addItem( "TAB" );
        separatorComboBox.addItem( "|" );
        separatorComboBox.setPreferredSize( new Dimension( 100 , 20 ) );
        separatorComboBox.setMaximumSize( new Dimension( 100 , 20 ) );
        
        miscBox = new Box( BoxLayout.X_AXIS );
        miscBox.add ( separatorLabel );
        miscBox.add( Box.createHorizontalStrut ( 5 ) );
        miscBox.add ( separatorComboBox );
        miscBox.add( Box.createHorizontalGlue () );
        
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_MISC" );
        TitledBorder tb2 = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getOptionsTitleFont() );
        miscBox.setBorder( tb2 );
        //----------

        initShowPanel();
    }

    /**
     * Inits the "show" sub panel.
     */
    private void initShowPanel ()
    {
        showPanel = new JPanel();

        showReadGroup = new ButtonGroup();
        showReadLabel = new JLabel( Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_READ" ) );
        String msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES" );
        showReadYes = new JRadioButton( msg , true );
        showReadYes.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_YES ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO" );
        showReadNo = new JRadioButton( msg , false );
        showReadNo.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_NO ) );
        showReadGroup.add( showReadYes );
        showReadGroup.add( showReadNo );

        showWriteGroup = new ButtonGroup();
        showWriteLabel = new JLabel( Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_WRITE" ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES" );
        showWriteYes = new JRadioButton( msg , true );
        showWriteYes.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_YES ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO" );
        showWriteNo = new JRadioButton( msg , false );
        showWriteNo.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_NO ) );
        showWriteGroup.add( showWriteYes );
        showWriteGroup.add( showWriteNo );

        showDeltaGroup = new ButtonGroup();
        showDeltaLabel = new JLabel( Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_DELTA" ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES" );
        showDeltaYes = new JRadioButton( msg , true );
        showDeltaYes.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_YES ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO" );
        showDeltaNo = new JRadioButton( msg , false );
        showDeltaNo.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_NO ) );
        showDeltaGroup.add( showDeltaYes );
        showDeltaGroup.add( showDeltaNo );

        showDiffGroup = new ButtonGroup();
        showDiffLabel = new JLabel( Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_DIFF" ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES" );
        showDiffYes = new JRadioButton( msg , true );
        showDiffYes.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_YES ) );
        msg = Messages.getMessage( "DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO" );
        showDiffNo = new JRadioButton( msg , false );
        showDiffNo.setActionCommand( String.valueOf( SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_NO ) );
        showDiffGroup.add( showDiffYes );
        showDiffGroup.add( showDiffNo );
    }

    /**
     * @return The buttonGroup attribute, containing the autoSnapshotCommentYes and autoSnapshotCommentNo JRadioButtons
     */
    public ButtonGroup getButtonGroup ()
    {
        return buttonGroup;
    }

    /**
     * Selects a auto comment JRadioButton, depending on the autoComment parameter value
     *
     * @param autoComment Has to be either SNAPSHOT_AUTO_COMMENT_YES or SNAPSHOT_AUTO_COMMENT_NO, otherwise a IllegalArgumentException is thrown
     * @throws IllegalArgumentException
     */
    public void selectAutoCommentButton ( int autoComment ) throws IllegalArgumentException
    {
        switch ( autoComment )
        {
            case SnapshotOptions.SNAPSHOT_AUTO_COMMENT_YES:
                autoSnapshotCommentYes.setSelected( true );
                break;

            case SnapshotOptions.SNAPSHOT_AUTO_COMMENT_NO:
                autoSnapshotCommentNo.setSelected( true );
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Selects a show read JRadioButton, depending on the showRead parameter value
     *
     * @param showRead Has to be either SNAPSHOT_COMPARE_SHOW_READ_YES or SNAPSHOT_COMPARE_SHOW_READ_NO, otherwise a IllegalArgumentException is thrown
     * @throws IllegalArgumentException
     */
    public void selectShowReadButton ( int showRead ) throws IllegalArgumentException
    {
        switch ( showRead )
        {
            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_YES:
                showReadYes.setSelected( true );
                break;

            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_NO:
                showReadNo.setSelected( true );
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Selects a show write JRadioButton, depending on the showWrite parameter value
     *
     * @param showWrite Has to be either SNAPSHOT_COMPARE_SHOW_WRITE_YES or SNAPSHOT_COMPARE_SHOW_WRITE_NO, otherwise a IllegalArgumentException is thrown
     * @throws IllegalArgumentException
     */
    public void selectShowWriteButton ( int showWrite ) throws IllegalArgumentException
    {
        switch ( showWrite )
        {
            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_YES:
                showWriteYes.setSelected( true );
                break;

            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_NO:
                showWriteNo.setSelected( true );
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Selects a show delta JRadioButton, depending on the showDelta parameter value
     *
     * @param showDelta Has to be either SNAPSHOT_COMPARE_SHOW_DELTA_YES or SNAPSHOT_COMPARE_SHOW_DELTA_NO, otherwise a IllegalArgumentException is thrown
     * @throws IllegalArgumentException
     */
    public void selectShowDeltaButton ( int showDelta ) throws IllegalArgumentException
    {
        switch ( showDelta )
        {
            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_YES:
                showDeltaYes.setSelected( true );
                break;

            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_NO:
                showDeltaNo.setSelected( true );
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Selects a show diff JRadioButton, depending on the showDiff parameter value
     *
     * @param showDiff Has to be either SNAPSHOT_COMPARE_SHOW_DIFF_YES or SNAPSHOT_COMPARE_SHOW_DIFF_NO, otherwise a IllegalArgumentException is thrown
     * @throws IllegalArgumentException
     */
    public void selectShowDiffButton ( int showDiff ) throws IllegalArgumentException
    {
        switch ( showDiff )
        {
            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_YES:
                showDiffYes.setSelected( true );
                break;

            case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_NO:
                showDiffNo.setSelected( true );
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Returns The default comment.
     *
     * @return The default comment
     */
    public String getSnapshotDefaultComment ()
    {
        return this.defaultSnapshotComment.getText();
    }


    /**
     * Sets the default comment.
     *
     * @param _comment The default comment
     */
    public void setSnapshotDefaultComment ( String _comment )
    {
        this.defaultSnapshotComment.setText( _comment );
    }

    /**
     * Returns true if show read has been selected.
     *
     * @return True if show read has been selected
     */
    public String hasShowRead ()
    {
        ButtonModel selectedModel = showReadGroup.getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();

        return selectedActionCommand;
    }

    /**
     * Returns true if show write has been selected.
     *
     * @return True if show write has been selected
     */
    public String hasShowWrite ()
    {
        ButtonModel selectedModel = showWriteGroup.getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();

        return selectedActionCommand;
    }

    /**
     * Returns true if show delta has been selected.
     *
     * @return True if show delta has been selected
     */
    public String hasShowDelta ()
    {
        ButtonModel selectedModel = showDeltaGroup.getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();

        return selectedActionCommand;
    }

    /**
     * Returns true if show diff has been selected.
     *
     * @return True if show diff has been selected
     */
    public String hasShowDiff ()
    {
        ButtonModel selectedModel = showDiffGroup.getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();

        return selectedActionCommand;
    }

    /**
     * @return
     */
    public String getSeparator() 
    {
        switch ( this.separatorComboBox.getSelectedIndex () )
        {
        	case 0:
        	   return SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_SEMICOLON; 
        	   
        	case 1:
         	   return SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_TAB;
         	   
        	case 2:
         	   return SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_PIPE;
         	   
        	default:
         	   return SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_SEMICOLON;
        	   
        }
    }

    /**
     * @param val_s
     */
    public void setSeparator(String val_s) 
    {
        //System.out.println ( "setSeparator" );
        
        int idx;
        
        if ( val_s == null )
        {
            idx = 0; 
        }
        else if ( val_s.equals ( SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_SEMICOLON ) )
        {
            idx = 0; 
        }
        else if ( val_s.equals ( SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_TAB ) )
        {
            idx = 1; 
        }
        else if ( val_s.equals ( SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_PIPE ) )
        {
            idx = 2; 
        }
        else if ( val_s.trim ().equals ( "" ) )
        {
            idx = 1; 
        }
        else
        {
            idx = 0; 
        }
        
        this.separatorComboBox.setSelectedIndex ( idx );
    }
}
