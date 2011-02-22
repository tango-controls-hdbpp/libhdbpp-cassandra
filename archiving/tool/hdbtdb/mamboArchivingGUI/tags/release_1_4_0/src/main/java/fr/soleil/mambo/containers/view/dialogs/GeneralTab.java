//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/GeneralTab.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  GeneralTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: pierrejoseph $
//
//$Revision: 1.13 $
//
//$Log: GeneralTab.java,v $
//Revision 1.13  2007/12/12 17:49:51  pierrejoseph
//HdbAvailable is stored in Mambo class.
//
//Revision 1.12  2007/10/30 17:52:41  soleilarc
//Author: XP
//Mantis bug ID: 6961
//Comment : Define the method initializeHistoricCheck, and call it in the method initComponents to put the state of the JcheckBox historicCheck.
//
//Revision 1.11  2007/01/22 15:53:27  ounsy
//the sampling factor default display is now "1"
//
//Revision 1.10  2007/01/10 10:42:38  ounsy
//corrected the display bug that occurred with complicated VC names
//
//Revision 1.9  2006/12/07 16:45:39  ounsy
//removed keeping period
//
//Revision 1.8  2006/12/06 15:12:22  ounsy
//added parametrisable sampling
//
//Revision 1.7  2006/11/06 09:28:05  ounsy
//icons reorganization
//
//Revision 1.6  2006/10/17 14:32:13  ounsy
//"sampling" -> "averaging"
//
//Revision 1.5  2006/09/05 14:11:24  ounsy
//updated for sampling compatibility
//
//Revision 1.4  2006/07/28 10:07:12  ounsy
//icons moved to "icons" package
//
//Revision 1.3  2005/12/15 11:31:49  ounsy
//minor changes
//
//Revision 1.2  2005/11/29 18:27:45  chinkumo
//no message
//
//Revision 1.1.2.4  2005/09/26 07:52:25  chinkumo
//Miscellaneous changes...
//
//Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
//Third commit !
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
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.DynamicDateRangeCheckBoxAction;
import fr.soleil.mambo.actions.view.listeners.DateRangeComboBoxListener;
import fr.soleil.mambo.actions.view.listeners.HistoricCheckboxListener;
import fr.soleil.mambo.components.view.SamplingTypeComboBox;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class GeneralTab extends JPanel
{
    private static GeneralTab instance = null;
    
    private JLabel creationDateLabel;
    private JLabel creationDateValue;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel lastUpdateDateLabel;
    private JLabel lastUpdateDateValue;
    private JTextField startDateField;
    private JTextField endDateField;

    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel dynamicDateRangeLabel;
    private JCheckBox dynamicDateRangeCheckBox;

    private JLabel dateRangeLabel;
    private JComboBox dateRangeComboBox;
    private DateRangeComboBoxListener dateRangeComboBoxListener;

    private JLabel historicLabel;
    private JCheckBox historicCheck;

    private JPanel dataSubPanel;

    private JPanel vcIdentificationBox; 
    private JPanel dateRangeBox;
    private JPanel samplingOptionsBox;
    
    //private JLabel samplingTypeFactorLabel;
    private JTextField samplingTypeFactorField;
    private JLabel samplingTypeLabel;
    private SamplingTypeComboBox samplingTypeComboBox;

    /**
     * @return 8 juil. 2005
     */
    public static GeneralTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new GeneralTab();
            instance.dateRangeComboBoxListener.itemStateChanged( new ItemEvent( instance.dateRangeComboBox , ItemEvent.ITEM_STATE_CHANGED , instance.dateRangeComboBoxListener.getLast1h() , ItemEvent.SELECTED ) );
        }

        return instance;
    }

    /**
     *
     */
    private GeneralTab ()
    {
        initComponents();
        addComponents();
        setLayout();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout ()
    {
        
        dataSubPanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( dataSubPanel ,
                                         3 , 1 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad

        this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        dataSubPanel.add( vcIdentificationBox );
        dataSubPanel.add( dateRangeBox );
        dataSubPanel.add( samplingOptionsBox );
     
        this.add( dataSubPanel );
        this.add( GeneralTabbedPane.getInstance() );

    }
    
    private void initializeHistoricCheck()
    {
        // if HDB is not available
        if ( Mambo.isHdbAvailable() == false ) {
        	historicCheck.setSelected(false);
        	historicCheck.setEnabled(false);
        }
        // else if the file mambo.properties doesn't exist or the syntax inside is wrong
        else {
        	// Behaviour by default:
        	historicCheck.setSelected(true);
        }
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        ImageIcon checkedYesIcon = new ImageIcon( Mambo.class.getResource( "icons/checked_yes.gif" ) );
        ImageIcon checkedNoIcon = new ImageIcon( Mambo.class.getResource( "icons/checked_no.gif" ) );
        ImageIcon checkedPressedIcon = new ImageIcon( Mambo.class.getResource( "icons/checked_pressed.gif" ) );

        String msg = "";

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_CREATION_DATE" );
        creationDateLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_LAST_UPDATE_DATE" );
        lastUpdateDateLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_START_DATE" );
        startDateLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_END_DATE" );
        endDateLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_HISTORIC" );
        historicLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_NAME" );
        nameLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_AVERAGING_TYPE" );
        samplingTypeLabel = new JLabel( msg );
        
        /*msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_AVERAGING_FACTOR" );
        samplingTypeFactorLabel = new JLabel( msg );*/
        
        creationDateValue = new JLabel();
        lastUpdateDateValue = new JLabel();

        nameField = new JTextField();
        /*nameField.setMinimumSize( new Dimension( 180 , 25 ) );
        nameField.setMaximumSize( new Dimension( 180 , 25 ) );*/
        
        startDateField = new JTextField();
        startDateField.setMinimumSize( new Dimension( 180 , 25 ) );
        startDateField.setMaximumSize( new Dimension( 180 , 25 ) );
        endDateField = new JTextField();
        endDateField.setMinimumSize( new Dimension( 180 , 25 ) );
        endDateField.setMaximumSize( new Dimension( 180 , 25 ) );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE" );
        dateRangeLabel = new JLabel( msg );

        dateRangeComboBox = new JComboBox();
        dateRangeComboBoxListener = new DateRangeComboBoxListener();
        dateRangeComboBox.setMaximumSize( new Dimension( 180 , 25 ) );
        msg = dateRangeComboBoxListener.getDefaultSelection();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast1h();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast4h();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast8h();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast1d();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast3d();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast7d();
        dateRangeComboBox.addItem( msg );
        msg = dateRangeComboBoxListener.getLast30d();
        dateRangeComboBox.addItem( msg );
        dateRangeComboBox.addItemListener( dateRangeComboBoxListener );
        
        samplingTypeComboBox = new SamplingTypeComboBox ();
        samplingTypeComboBox.setMaximumSize( new Dimension( 150 , 25 ) );
        samplingTypeFactorField = new JTextField ();
        /*samplingTypeFactorField.setMinimumSize( new Dimension( 80 , 25 ) );
        samplingTypeFactorField.setMaximumSize( new Dimension( 80 , 25 ) );*/
        
        historicCheck = new JCheckBox();
        historicCheck.setIcon( checkedNoIcon );
        historicCheck.setSelectedIcon( checkedYesIcon );
        historicCheck.setPressedIcon( checkedPressedIcon );
        historicCheck.addActionListener( new HistoricCheckboxListener() );
        initializeHistoricCheck();

        dataSubPanel = new JPanel();

        dynamicDateRangeLabel = new JLabel( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DYNAMIC_DATE_RANGE" ) );

        dynamicDateRangeCheckBox = new JCheckBox();
        dynamicDateRangeCheckBox.setIcon( checkedNoIcon );
        dynamicDateRangeCheckBox.setSelectedIcon( checkedYesIcon );
        dynamicDateRangeCheckBox.setPressedIcon( checkedPressedIcon );
        dynamicDateRangeCheckBox.setSelected( false );
        dynamicDateRangeCheckBox.addActionListener( new DynamicDateRangeCheckBoxAction() );
        
        this.initBoxes ();
    }

    private void initBoxes() 
    {
        String msg; 
        TitledBorder tb;

        //---------------------------
        vcIdentificationBox = new JPanel();
        vcIdentificationBox.add( historicLabel );
        vcIdentificationBox.add( historicCheck );
        vcIdentificationBox.add( creationDateLabel );
        vcIdentificationBox.add( creationDateValue );
        vcIdentificationBox.add( lastUpdateDateLabel );
        vcIdentificationBox.add( lastUpdateDateValue );
        vcIdentificationBox.add( nameLabel );
        vcIdentificationBox.add( nameField );
        
        vcIdentificationBox.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid( vcIdentificationBox ,
                                         4 , 2 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad
        
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_BOXES_VC_IDENTIFICATION" );
        tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        vcIdentificationBox.setBorder( tb );
        //---------------------------
        
        //---------------------------
        dateRangeBox = new JPanel();
        dateRangeBox.add( dateRangeLabel );
        dateRangeBox.add( dateRangeComboBox );
        dateRangeBox.add( startDateLabel );
        dateRangeBox.add( startDateField );
        dateRangeBox.add( endDateLabel );
        dateRangeBox.add( endDateField );
        dateRangeBox.add( dynamicDateRangeLabel );
        dateRangeBox.add( dynamicDateRangeCheckBox );
        
        dateRangeBox.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid( dateRangeBox ,
                                         4, 2 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad
        
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_BOXES_VC_DATA_RANGE" );
        tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        dateRangeBox.setBorder( tb );
        //---------------------------
        
        //---------------------------
        samplingOptionsBox = new JPanel();
        samplingOptionsBox.add( samplingTypeLabel );
        samplingOptionsBox.add( samplingTypeFactorField );
        samplingOptionsBox.add( samplingTypeComboBox );
        samplingOptionsBox.add( Box.createHorizontalGlue() );
        
        samplingOptionsBox.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid( samplingOptionsBox ,
                                         1, 4 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad
        
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_BOXES_VC_AVERAGING_OPTIONS" );
        tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        samplingOptionsBox.setBorder( tb );
        //---------------------------
    }

    public String getName ()
    {
        return nameField.getText();
    }

    public void setName ( String _name )
    {
        nameField.setText( _name );
    }

    public void setStartAndEndDatesFieldsEnabled ( boolean enabled )
    {
        startDateField.setEnabled( enabled );
        endDateField.setEnabled( enabled );
    }

    public void setCreationDate ( Timestamp creationDate )
    {
        if ( creationDate != null )
        {
            creationDateValue.setText( creationDate.toString() );
        }
        else
        {
            creationDateValue.setText( "" );
        }
    }

    public Timestamp getStartDate () throws IllegalArgumentException
    {
        Timestamp ret = null;
        try
        {
            String s = startDateField.getText();
            //System.out.println ( "getStartDate/s/"+s+"/" );
            ret = Timestamp.valueOf( s );
        }
        catch ( Exception e )
        {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    public Timestamp getEndDate () throws IllegalArgumentException
    {
        Timestamp ret = null;
        try
        {
            String s = endDateField.getText();
            ret = Timestamp.valueOf( s );
        }
        catch ( Exception e )
        {
            throw new IllegalArgumentException();
        }
        return ret;
    }

    public String getDateRange ()
    {
        String ret = "";
        ret = ( String ) dateRangeComboBox.getSelectedItem();
        return ret;
    }
    
    public SamplingType getSamplingType ()
    {
        SamplingType ret = (SamplingType) samplingTypeComboBox.getSelectedItem ();
        short factor = 0;
        
        try
        {
            String factor_s = samplingTypeFactorField.getText ();
            factor = Short.parseShort ( factor_s );
        }
        catch ( Exception e )
        {
            
        }
        
        if ( factor > 0 )
        {
            ret.setHasAdditionalFiltering ( true );
            ret.setAdditionalFilteringFactor ( factor );
        }
        else
        {
            ret.setHasAdditionalFiltering ( false );
        }
        
        return ret;
    }
    
    public void setSamplingType ( SamplingType _samplingType )
    {
        samplingTypeComboBox.setSelectedSamplingType ( _samplingType );
        if ( _samplingType.hasAdditionalFiltering () )
        {
            String factor = "" + _samplingType.getAdditionalFilteringFactor ();
            samplingTypeFactorField.setText ( factor );
        }
        else
        {
            //samplingTypeFactorField.setText ( "" );
            samplingTypeFactorField.setText ( "1" );
        }
    }
    

    public void setDateRange ( String range )
    {
        dateRangeComboBox.setSelectedItem( range );
    }

    public boolean isDynamicDateRange ()
    {
        return dynamicDateRangeCheckBox.isSelected();
    }

    public void setDynamicDateRange ( boolean dynamic )
    {
        if ( dynamic != isDynamicDateRange() )
        {
            dynamicDateRangeCheckBox.doClick();
        }
    }

    public Timestamp[] getDynamicStartAndEndDates ()
    {
        Timestamp[] startAndEnd = new Timestamp[ 2 ];
        long now = System.currentTimeMillis();
        Timestamp end = new Timestamp( now );
        startAndEnd[ 1 ] = end;
        long delta = 0;
        long hour = 3600000;
        long day = 86400000;
        String range = ( String ) dateRangeComboBox.getSelectedItem();
        if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1H" ).equals( range ) )
        {
            delta = 1 * hour;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_4H" ).equals( range ) )
        {
            delta = 4 * hour;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_8H" ).equals( range ) )
        {
            delta = 8 * hour;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1D" ).equals( range ) )
        {
            delta = 1 * day;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_3D" ).equals( range ) )
        {
            delta = 3 * day;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_7D" ).equals( range ) )
        {
            delta = 7 * day;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_30D" ).equals( range ) )
        {
            delta = 30 * day;
        }
        Timestamp start = new Timestamp( now - delta );
        startAndEnd[ 0 ] = start;
        return startAndEnd;
    }

    public void setLastUpdateDate ( Timestamp lastUpdateDate )
    {
        if ( lastUpdateDate != null )
        {
            lastUpdateDateValue.setText( lastUpdateDate.toString() );
        }
        else
        {
            lastUpdateDateValue.setText( "" );
        }
    }

    public void setStartDate ( Timestamp startDate )
    {
        if ( startDate != null )
        {
            startDateField.setText( startDate.toString() );
        }
        else
        {
            startDateField.setText( "" );
        }
    }

    public void setEndDate ( Timestamp endDate )
    {
        if ( endDate != null )
        {
            endDateField.setText( endDate.toString() );
        }
        else
        {
            endDateField.setText( "" );
        }
    }

    /**
     * @param b 25 août 2005
     */
    public void setHistoric ( boolean b )
    {
        historicCheck.setSelected( b );
    }

    /**
     * @param b 25 août 2005
     */
    public boolean isHistoric ()
    {
        return historicCheck.isSelected();
    }
}
