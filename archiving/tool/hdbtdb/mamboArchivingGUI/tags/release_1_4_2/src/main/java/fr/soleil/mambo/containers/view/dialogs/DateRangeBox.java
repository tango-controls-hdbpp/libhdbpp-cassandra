//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfiguration.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.DynamicDateRangeCheckBoxAction;
import fr.soleil.mambo.actions.view.listeners.DateRangeComboBoxListener;
import fr.soleil.mambo.containers.view.ViewAttributesTreePanel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class DateRangeBox extends JPanel 
{
//	private static DateRangeBox instance = null;

	private   JLabel     startDateLabel;
	private   JLabel     endDateLabel;
	private   JTextField startDateField;
	private   JTextField endDateField;
	private   JLabel     dynamicDateRangeLabel;
	private   JCheckBox  dynamicDateRangeCheckBox;
//	private   boolean    dynamicDateRange;
	private   JLabel     dateRangeLabel;
	private   JComboBox  dateRangeComboBox;
//	private   DateRange  dateRange;
	
    protected DateRangeComboBoxListener dateRangeComboBoxListener;
	private JPanel datePanel;
	private JPanel checkBoxPanel;
	private Container parentComponente; 
	
//    public static DateRangeBox getInstance ()
//    {
//        if ( instance == null )
//        {
//            instance = new DateRangeBox();
//        }
//
//        return instance;
//    }

	public DateRangeBox( Container _parentComponente ) {
		super();

		this.parentComponente = _parentComponente;
        initComponents();
        addComponents();
        setLayout();
	}

	
	private void setLayout() {
		
		if( parentComponente instanceof ViewAttributesTreePanel ){
	    this.setLayout( new SpringLayout() );
	    SpringUtilities.makeCompactGrid( this ,
	                                     2, 1 , //rows, cols
	                                     6 , 6 , //initX, initY
	                                     6 , 6 ,
	                                     true );//xPad, yPad
		}else{
		    this.setLayout( new SpringLayout() );
		    SpringUtilities.makeCompactGrid( this ,
		                                     4 , 2, //rows, cols
		                                     6 , 6 , //initX, initY
		                                     6 , 6 ,
		                                     true );//xPad, yPad
			
		}
	}

	private void initComponents() 
	{
		datePanel = new JPanel();
		checkBoxPanel = new JPanel();
        
        ImageIcon checkedYesIcon = new ImageIcon( Mambo.class.getResource( "icons/checked_yes.gif" ) );
        ImageIcon checkedNoIcon = new ImageIcon( Mambo.class.getResource( "icons/checked_no.gif" ) );
        ImageIcon checkedPressedIcon = new ImageIcon( Mambo.class.getResource( "icons/checked_pressed.gif" ) );

        String msg = "";
	
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_START_DATE" );
        startDateLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_END_DATE" );
        endDateLabel = new JLabel( msg );

        startDateField = new JTextField();
        startDateField.setMinimumSize( new Dimension( 180 , 25 ) );
        startDateField.setMaximumSize( new Dimension( 180 , 25 ) );
        startDateField.setText("");
        endDateField = new JTextField();
        endDateField.setMinimumSize( new Dimension( 180 , 25 ) );
        endDateField.setMaximumSize( new Dimension( 180 , 25 ) );
        endDateField.setText("");
        
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE" );
        dateRangeLabel = new JLabel( msg );
        dateRangeComboBox = new JComboBox();
        dateRangeComboBoxListener = new DateRangeComboBoxListener( this );
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
//        dateRangeComboBox.addItemListener( dateRangeComboBoxListener );
        dateRangeComboBox.addActionListener( dateRangeComboBoxListener );
        
	    msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_BOXES_VC_DATA_RANGE" );
        TitledBorder tb;
	    tb = BorderFactory.createTitledBorder
	            ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
	              msg ,
	              TitledBorder.DEFAULT_JUSTIFICATION ,
	              TitledBorder.TOP ,
	              GUIUtilities.getTitleFont() );
	    this.setBorder( tb );

        dynamicDateRangeLabel = new JLabel( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DYNAMIC_DATE_RANGE" ) );

        dynamicDateRangeCheckBox = new JCheckBox();
        dynamicDateRangeCheckBox.setIcon( checkedNoIcon );
        dynamicDateRangeCheckBox.setSelectedIcon( checkedYesIcon );
        dynamicDateRangeCheckBox.setPressedIcon( checkedPressedIcon );
        dynamicDateRangeCheckBox.setSelected( false );
        dynamicDateRangeCheckBox.addActionListener( new DynamicDateRangeCheckBoxAction(this) );
        
//        dateRange = DateRange.getInstance();
//        dateRange.ajouterObservateur( this );
//        dateRange.setStartDate( startDateField.getText() , this );
//        dateRange.setEndDate( endDateField.getText() , this );
	}

	private void addComponents() {
		
		if( parentComponente instanceof ViewAttributesTreePanel ){

			datePanel.add( dateRangeLabel );
			datePanel.add( dateRangeComboBox );
			datePanel.add( startDateLabel );
			datePanel.add( startDateField );
			datePanel.add( endDateLabel );
			datePanel.add( endDateField );
			checkBoxPanel.add( dynamicDateRangeLabel );
			checkBoxPanel.add( dynamicDateRangeCheckBox );
		    
			datePanel.setLayout(new SpringLayout());
			SpringUtilities.makeCompactGrid( datePanel ,
					3, 2 ,
					0 , 0 ,
					0 , 0 , true );      
			
			checkBoxPanel.setLayout( new SpringLayout() );
			SpringUtilities.makeCompactGrid( checkBoxPanel ,
	                1, 2 , //rows, cols
	                0 , 0 , //initX, initY
	                6 , 1 ,
	                true );       //xPad, yPad

			this.add( datePanel );
		    this.add( checkBoxPanel );
			GUIUtilities.setObjectBackground(datePanel    ,GUIUtilities.VIEW_COLOR);
			GUIUtilities.setObjectBackground(checkBoxPanel,GUIUtilities.VIEW_COLOR);
			
		}else{
			
			this.add( dateRangeLabel );
			this.add( dateRangeComboBox );
			this.add( startDateLabel );
			this.add( startDateField );
			this.add( endDateLabel );
			this.add( endDateField );

			this.add( dynamicDateRangeLabel );
			this.add( dynamicDateRangeCheckBox );
		    
		}
	}

	public void update( DateRangeBox dateRangeBox ) {
        if(dateRangeBox instanceof DateRangeBox)
        {       
        	DateRangeBox g = ( DateRangeBox ) dateRangeBox;
        	startDateField.setText( g.getStartDateField().getText() );
        	endDateField.setText( g.getEndDateField().getText() );
        	startDateField.setEnabled( g.getStartDateField().isEnabled() );
        	endDateField.setEnabled( g.getEndDateField().isEnabled() );
        	dynamicDateRangeCheckBox.setSelected( g.getDynamicDateRangeCheckBox().isSelected() );
//        	dateRangeComboBox.setSelectedIndex( g.getDateRangeComboBoxSelectedIndex() );
        	
        	dateRangeComboBox.removeActionListener( dateRangeComboBoxListener );
        	dateRangeComboBox.setSelectedItem( g.getDateRangeComboBox().getSelectedItem() );
        	dateRangeComboBox.addActionListener( dateRangeComboBoxListener );
        }      
	}


    public void setStartAndEndDatesFieldsEnabled ( boolean enabled )
    {
    	startDateField.setEnabled( enabled );
    	endDateField.setEnabled( enabled );
    }
	
	public void setDateRangeComboBox( JComboBox dateRangeComboBox ) {
		this.dateRangeComboBox = dateRangeComboBox;

	}

	public JComboBox getDateRangeComboBox() {
		return dateRangeComboBox;
	}

	public void setDateRangeComboBoxSelectedItem( String range ) {
		
		dateRangeComboBox.removeActionListener( dateRangeComboBoxListener );
		dateRangeComboBox.setSelectedItem( range );
		dateRangeComboBox.addActionListener( dateRangeComboBoxListener );
    			
//		this.dateRange.setComboBoxSelectedItem( range , this );
	}
	
	public void setDateRangeComboBoxListener( DateRangeComboBoxListener dateRangeComboBoxListener ) {
		this.dateRangeComboBoxListener = dateRangeComboBoxListener;
//		dateRange.notifierObservateurs();
	}

	public DateRangeComboBoxListener getDateRangeComboBoxListener() {
		return dateRangeComboBoxListener;
	}

	public JCheckBox getDynamicDateRangeCheckBox() {
		return dynamicDateRangeCheckBox;
	}

	public void setStartDateField( JTextField startDateField ) {
		this.startDateField = startDateField;
//		dateRange.notifierObservateurs();
	}

	public JTextField getStartDateField() {
		return startDateField;
	}

	public void setEndDateField( JTextField endDateField ) {
		this.endDateField = endDateField;
//		dateRange.notifierObservateurs();
	}

	public JTextField getEndDateField() {
		return endDateField;
//		dateRange.notifierObservateurs();
	}

	public void setDynamicDateRangeCheckBox( JCheckBox dynamicDateRangeCheckBox ) {
		this.dynamicDateRangeCheckBox = dynamicDateRangeCheckBox;
//		dateRange.notifierObservateurs();
	}

	public void setStartDate( String startDate ) {
//		this.dateRange.setStartDate( startDate , this );
		this.startDateField.setText( startDate );
	}

	public void setEndDate( String endDate ) {
//		this.dateRange.setEndDate( endDate , this );
		this.endDateField.setText( endDate );
	}

	public void setDynamicDateRangeDoClick() {
		dynamicDateRangeCheckBox.doClick();
//		dateRange.setDynamicDateRangeCheckBoxSelected(dynamicDateRangeCheckBox.isSelected(), this );		
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

	public boolean isDynamicDateRange() {
		// TODO Auto-generated method stub
		return dynamicDateRangeCheckBox.isSelected();
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
    




	
	
//	public DateRange getDateRange() {
//		return dateRange;
//	}
	

	
}
