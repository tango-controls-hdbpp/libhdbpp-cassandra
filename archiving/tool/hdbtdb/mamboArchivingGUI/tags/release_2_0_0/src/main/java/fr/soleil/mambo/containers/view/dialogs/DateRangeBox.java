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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.actions.view.DynamicDateRangeCheckBoxAction;
import fr.soleil.mambo.actions.view.listeners.DateRangeComboBoxListener;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class DateRangeBox extends JPanel
{

	private   JLabel     startDateLabel;
	private   JLabel     endDateLabel;
	private   JTextField startDateField;
	private   JTextField endDateField;
	private   JLabel     dynamicDateRangeLabel;
	private   JCheckBox  dynamicDateRangeCheckBox;
	private   JLabel     dateRangeLabel;
	private   JComboBox  dateRangeComboBox;

    protected DateRangeComboBoxListener dateRangeComboBoxListener;

	public DateRangeBox( Container _parentComponente ) {
		super( new GridBagLayout() );

        initComponents();
        addComponents();
	}

	private void initComponents()
	{
        String msg = "";

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_START_DATE" );
        startDateLabel = new JLabel( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_END_DATE" );
        endDateLabel = new JLabel( msg );

        startDateField = new JTextField();
//        startDateField.setMinimumSize( new Dimension( 180 , 25 ) );
//        startDateField.setMaximumSize( new Dimension( 180 , 25 ) );
        startDateField.setText("");
        endDateField = new JTextField();
//        endDateField.setMinimumSize( new Dimension( 180 , 25 ) );
//        endDateField.setMaximumSize( new Dimension( 180 , 25 ) );
        endDateField.setText("");

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE" );
        dateRangeLabel = new JLabel( msg );
        dateRangeComboBox = new JComboBox();
        dateRangeComboBoxListener = new DateRangeComboBoxListener( this );
//        dateRangeComboBox.setMaximumSize( new Dimension( 180 , 25 ) );
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
        dynamicDateRangeCheckBox.setSelected( false );
        dynamicDateRangeCheckBox.addActionListener( new DynamicDateRangeCheckBoxAction(this) );

//        dateRange = DateRange.getInstance();
//        dateRange.ajouterObservateur( this );
//        dateRange.setStartDate( startDateField.getText() , this );
//        dateRange.setEndDate( endDateField.getText() , this );
	}

	private void addComponents() {

	    Insets defaultInsets = new Insets(5,5,5,5);

	    GridBagConstraints dateRangeLabelConstraints = new GridBagConstraints();
	    dateRangeLabelConstraints.fill = GridBagConstraints.BOTH;
	    dateRangeLabelConstraints.gridx = 0;
	    dateRangeLabelConstraints.gridy = 0;
	    dateRangeLabelConstraints.weightx = 0;
        dateRangeLabelConstraints.weighty = 0;
	    dateRangeLabelConstraints.insets = defaultInsets;
        GridBagConstraints dateRangeComboBoxConstraints = new GridBagConstraints();
        dateRangeComboBoxConstraints.fill = GridBagConstraints.BOTH;
        dateRangeComboBoxConstraints.gridx = 1;
        dateRangeComboBoxConstraints.gridy = 0;
        dateRangeComboBoxConstraints.weightx = 1;
        dateRangeComboBoxConstraints.weighty = 0;
        dateRangeComboBoxConstraints.insets = defaultInsets;

        GridBagConstraints startDateLabelConstraints = new GridBagConstraints();
        startDateLabelConstraints.fill = GridBagConstraints.BOTH;
        startDateLabelConstraints.gridx = 0;
        startDateLabelConstraints.gridy = 1;
        startDateLabelConstraints.weightx = 0;
        startDateLabelConstraints.weighty = 0;
        startDateLabelConstraints.insets = defaultInsets;
        GridBagConstraints startDateFieldConstraints = new GridBagConstraints();
        startDateFieldConstraints.fill = GridBagConstraints.BOTH;
        startDateFieldConstraints.gridx = 1;
        startDateFieldConstraints.gridy = 1;
        startDateFieldConstraints.weightx = 1;
        startDateFieldConstraints.weighty = 0;
        startDateFieldConstraints.insets = defaultInsets;

        GridBagConstraints endDateLabelConstraints = new GridBagConstraints();
        endDateLabelConstraints.fill = GridBagConstraints.BOTH;
        endDateLabelConstraints.gridx = 0;
        endDateLabelConstraints.gridy = 2;
        endDateLabelConstraints.weightx = 0;
        endDateLabelConstraints.insets = defaultInsets;
        GridBagConstraints endDateFieldConstraints = new GridBagConstraints();
        endDateFieldConstraints.fill = GridBagConstraints.BOTH;
        endDateFieldConstraints.gridx = 1;
        endDateFieldConstraints.gridy = 2;
        endDateFieldConstraints.weightx = 1;
        endDateLabelConstraints.weighty = 0;
        endDateFieldConstraints.weighty = 0;
        endDateFieldConstraints.insets = defaultInsets;

        GridBagConstraints dynamicDateRangeLabelConstraints = new GridBagConstraints();
        dynamicDateRangeLabelConstraints.fill = GridBagConstraints.BOTH;
        dynamicDateRangeLabelConstraints.gridx = 0;
        dynamicDateRangeLabelConstraints.weightx = 0;
        dynamicDateRangeLabelConstraints.weighty = 0;
        dynamicDateRangeLabelConstraints.insets = defaultInsets;
        GridBagConstraints dynamicDateRangeCheckBoxConstraints = new GridBagConstraints();
        dynamicDateRangeCheckBoxConstraints.fill = GridBagConstraints.BOTH;
        dynamicDateRangeCheckBoxConstraints.gridx = 1;
        dynamicDateRangeCheckBoxConstraints.weightx = 1;
        dynamicDateRangeCheckBoxConstraints.weighty = 0;
        dynamicDateRangeCheckBoxConstraints.insets = defaultInsets;

        dynamicDateRangeLabelConstraints.gridy = 3;
        dynamicDateRangeCheckBoxConstraints.gridy = 3;

        this.add(dateRangeLabel, dateRangeLabelConstraints);
        this.add(dateRangeComboBox, dateRangeComboBoxConstraints);
        this.add(startDateLabel, startDateLabelConstraints);
        this.add(startDateField, startDateFieldConstraints);
        this.add(endDateLabel, endDateLabelConstraints);
        this.add(endDateField, endDateFieldConstraints);
        this.add(dynamicDateRangeLabel, dynamicDateRangeLabelConstraints);
        this.add(dynamicDateRangeCheckBox, dynamicDateRangeCheckBoxConstraints);
	}

	public void update( DateRangeBox dateRangeBox ) {
        if(dateRangeBox instanceof DateRangeBox)
        {
        	DateRangeBox g = dateRangeBox;
        	startDateField.setText( g.getStartDateField().getText().trim() );
        	endDateField.setText( g.getEndDateField().getText().trim() );
        	startDateField.setEnabled( g.getStartDateField().isEnabled() );
        	endDateField.setEnabled( g.getEndDateField().isEnabled() );
        	dynamicDateRangeCheckBox.setSelected( g.getDynamicDateRangeCheckBox().isSelected() );

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
		startDateField.setText( startDate );
	}

	public void setEndDate( String endDate ) {
//		this.dateRange.setEndDate( endDate , this );
		endDateField.setText( endDate );
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

}
