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
//$Revision: 1.14 $
//
//$Log: GeneralTab.java,v $
//Revision 1.14  2008/04/09 10:45:46  achouri
//put date in differente class.
//
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.Mambo;
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
    private JLabel lastUpdateDateLabel;
    private JLabel lastUpdateDateValue;

    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel historicLabel;
    private JCheckBox historicCheck;

    private JPanel dataSubPanel;

    private JPanel vcIdentificationBox; 
    private JPanel samplingOptionsBox;
    
    private JTextField samplingTypeFactorField;
    private JLabel samplingTypeLabel;
    private SamplingTypeComboBox samplingTypeComboBox;
    private DateRangeBox dateRangeBox;
    
    /**
     * @return 8 juil. 2005
     */
    public static GeneralTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new GeneralTab();
            instance.dateRangeBox.getDateRangeComboBoxListener().itemStateChanged( 
            		new ItemEvent( instance.dateRangeBox.getDateRangeComboBox() , 
            		ItemEvent.ITEM_STATE_CHANGED , 
            		instance.dateRangeBox.getDateRangeComboBoxListener().getLast1h() , 
            		ItemEvent.SELECTED ) );
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
        this.add( ChartGeneralTabbedPane.getInstance() );

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

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_HISTORIC" );
        historicLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_NAME" );
        nameLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_AVERAGING_TYPE" );
        samplingTypeLabel = new JLabel( msg );
        
        creationDateValue = new JLabel();
        lastUpdateDateValue = new JLabel();

        nameField = new JTextField();

        samplingTypeComboBox = new SamplingTypeComboBox ();
        samplingTypeComboBox.setMaximumSize( new Dimension( 150 , 25 ) );
        samplingTypeFactorField = new JTextField ();
        
        historicCheck = new JCheckBox();
        historicCheck.setIcon( checkedNoIcon );
        historicCheck.setSelectedIcon( checkedYesIcon );
        historicCheck.setPressedIcon( checkedPressedIcon );
        historicCheck.addActionListener( new HistoricCheckboxListener() );
        initializeHistoricCheck();

        dataSubPanel = new JPanel();

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
        dateRangeBox = new DateRangeBox( this );
        
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
    	dateRangeBox.setDateRangeComboBoxSelectedItem( range );

    }

    public boolean isDynamicDateRange ()
    {
        return dateRangeBox.getDynamicDateRangeCheckBox().isSelected();
    }

    public void setDynamicDateRange ( boolean dynamic )
    {
        if ( dynamic != isDynamicDateRange() )
        {
        	dateRangeBox.setDynamicDateRangeDoClick();
        }
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
        	dateRangeBox.setStartDate( startDate.toString() );
        }
        else
        {
        	dateRangeBox.setStartDate( "" );
        	dateRangeBox.setDateRangeComboBoxSelectedItem("---");
        }
    }

    public void setEndDate ( Timestamp endDate )
    {
        if ( endDate != null )
        {
        	dateRangeBox.setEndDate( endDate.toString() );
        }
        else
        {
        	dateRangeBox.setEndDate( "" );
        	dateRangeBox.setDateRangeComboBoxSelectedItem("---");
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

	public DateRangeBox getDateRangeBox() {
		return dateRangeBox;
		
	}

	public Timestamp[] getDynamicStartAndEndDates() {
		return dateRangeBox.getDynamicStartAndEndDates();
	}


}
