//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/ACTabDefaultPanel.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AttributesPropertiesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: pierrejoseph $
//
//$Revision: 1.9 $
//
//$Log: ACTabDefaultPanel.java,v $
//Revision 1.9  2007/02/01 14:16:53  pierrejoseph
//Export Period is sometimes forced to 30 min.
//
//Revision 1.8  2006/12/07 16:45:39  ounsy
//removed keeping period
//
//Revision 1.7  2006/12/07 15:06:58  ounsy
//the modeP checkbox is no longer editable
//
//Revision 1.6  2006/10/05 08:57:17  ounsy
//LoadACDefaults and SaveACDefaults moved to mambo.actions.archiving instead of mambo.actions
//
//Revision 1.5  2006/05/19 15:05:29  ounsy
//minor changes
//
//Revision 1.4  2006/05/16 11:58:12  ounsy
//minor changes
//
//Revision 1.3  2006/02/24 12:19:32  ounsy
//small modifications
//
//Revision 1.2  2005/12/15 11:28:33  ounsy
//period formating
//
//Revision 1.1  2005/11/29 18:28:13  chinkumo
//no message
//
//Revision 1.1.2.3  2005/09/19 08:00:22  chinkumo
//Miscellaneous changes...
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.actions.archiving.LoadACDefaults;
import fr.soleil.mambo.actions.archiving.SaveACDefaults;
import fr.soleil.mambo.components.archiving.ACTdbExportPeriodComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ACTabDefaultPanel extends JPanel {
	private static ACTabDefaultPanel instance = null;

	// HDB objects
	private JPanel HDBPanel;
	private JLabel HDBLabel;
	private JLabel HDBperiodLabel;
	private JTextField HDBperiodField;
	private JLabel HDBmodesLabel;
	private JCheckBox HDBPeriodicalCheck;
	private JCheckBox HDBAbsoluteCheck;
	private JCheckBox HDBRelativeCheck;
	private JCheckBox HDBThresholdCheck;
	private JCheckBox HDBDifferenceCheck;
	// HDB objects
	// /////////////////////////
	private JCheckBox HDBAbsoluteDLCheck;
	private JCheckBox HDBRelativeDLCheck;

	// TDB objects
	private JPanel TDBPanel;
	private JLabel TDBLabel;
	private JLabel TDBperiodLabel;
	private JTextField TDBperiodField;
	// private JLabel TDBkeepingPeriodLabel;
	// private JComboBox TDBkeepingPeriodComboBox;

	private JLabel TDBexportPeriodLabel;
	// private JComboBox TDBexportPeriodComboBox;
	private ACTdbExportPeriodComboBox TDBexportPeriodComboBox;

	private JLabel TDBmodesLabel;
	private JCheckBox TDBPeriodicalCheck;
	private JCheckBox TDBAbsoluteCheck;
	private JCheckBox TDBRelativeCheck;
	private JCheckBox TDBThresholdCheck;
	private JCheckBox TDBDifferenceCheck;
	// TDB objects
	// /////////////////////////
	private JCheckBox TDBAbsoluteDLCheck;
	private JCheckBox TDBRelativeDLCheck;

	// general objects
	private JButton saveDefaultsButton;
	private JButton loadDefaultsButton;
	private JButton restoreDefaultsButton;
	private JPanel centerPanel;
	private Box buttonsBox;
	// general objects

	public static final String DEFAULT_HDB_PERIOD_VALUE = "60000";

	public static final String DEFAULT_TDB_PERIOD_VALUE = "1000";

	/*
	 * private long[] exportPeriodsTable; private Hashtable
	 * reverseExportPeriodsTable = new Hashtable();
	 */

	private long[] keepingPeriodsTable;
	private Hashtable reverseKeepingPeriodsTable = new Hashtable();

	private JLabel HDBPeriodicalLabel;
	private JLabel HDBAbsoluteLabel;
	private JLabel HDBRelativeLabel;
	private JLabel HDBThresholdLabel;
	private JLabel HDBDifferenceLabel;

	private JLabel TDBPeriodicalLabel;
	private JLabel TDBAbsoluteLabel;
	private JLabel TDBRelativeLabel;
	private JLabel TDBThresholdLabel;
	private JLabel TDBDifferenceLabel;

	private JTextField HDBabsolutePeriodField;

	private JTextField TDBabsolutePeriodField;

	private JTextField HDBabsoluteLowerField;

	private JTextField TDBabsoluteLowerField;

	private JTextField HDBabsoluteUpperField;

	private JTextField TDBabsoluteUpperField;

	private JTextField HDBrelativePeriodField;

	private JTextField TDBrelativePeriodField;

	private JTextField HDBrelativeLowerField;

	private JTextField TDBrelativeLowerField;

	private JTextField HDBrelativeUpperField;

	private JTextField TDBrelativeUpperField;

	private JTextField HDBthresholdPeriodField;

	private JTextField TDBthresholdPeriodField;

	private JTextField HDBthresholdLowerField;

	private JTextField TDBthresholdLowerField;

	private JTextField HDBthresholdUpperField;

	private JTextField TDBthresholdUpperField;

	private JTextField HDBdifferencePeriodField;

	private JTextField TDBdifferencePeriodField;

	private JLabel HDBabsolutePeriodLabel;

	private JLabel TDBabsolutePeriodLabel;

	private JLabel HDBabsoluteLowerLabel;

	private JLabel TDBabsoluteLowerLabel;

	private JLabel HDBabsoluteUpperLabel;

	private JLabel TDBabsoluteUpperLabel;

	private JLabel HDBrelativePeriodLabel;

	private JLabel TDBrelativePeriodLabel;

	private JLabel HDBrelativeLowerLabel;

	private JLabel TDBrelativeLowerLabel;

	private JLabel HDBrelativeUpperLabel;

	private JLabel TDBrelativeUpperLabel;

	private JLabel HDBthresholdPeriodLabel;

	private JLabel TDBthresholdPeriodLabel;

	private JLabel HDBthresholdLowerLabel;

	private JLabel TDBthresholdLowerLabel;

	private JLabel HDBthresholdUpperLabel;

	private JLabel TDBthresholdUpperLabel;

	private JLabel HDBdifferencePeriodLabel;

	private JLabel TDBdifferencePeriodLabel;

	/**
	 * @return 8 juil. 2005
	 */
	public static ACTabDefaultPanel getInstance() {
		if (instance == null) {
			instance = new ACTabDefaultPanel();
		}

		return instance;
	}

	public static void resetInstance() {
		if (instance != null) {
			instance.repaint();
		}

		instance = null;
	}

	/**
     *
     */
	private ACTabDefaultPanel() {
		// IACDefaultsManager manager =
		// ACDefaultsManagerFactory.getCurrentImpl();
		// defaultExportValue =
		// Long.parseLong(manager.getACDefaultsPropertyValue("TDB_EXPORT_PERIOD"));

		this.initComponents();
		this.initLayout();
		this.addComponents();

		ArchivingConfigurationAttributeProperties currentProperties = new ArchivingConfigurationAttributeProperties();
		ArchivingConfigurationAttributeProperties
				.setCurrentProperties(currentProperties);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initLayout() {
		this.setLayout(new BorderLayout());
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		this.add(centerPanel, BorderLayout.CENTER);
		// this.add ( setButton , BorderLayout.PAGE_END );
		this.add(buttonsBox, BorderLayout.PAGE_END);

	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		// initKeepingPeriodsTable();
		// initExportPeriodsTable();
		initButtonsBox();

		String msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_MODES");
		HDBmodesLabel = new JLabel(msg);
		HDBmodesLabel.setFont(HDBmodesLabel.getFont().deriveFont(Font.ITALIC));
		TDBmodesLabel = new JLabel(msg);
		TDBmodesLabel.setFont(TDBmodesLabel.getFont().deriveFont(Font.ITALIC));

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_PERIODICAL_MODE");
		HDBPeriodicalLabel = new JLabel(msg);
		TDBPeriodicalLabel = new JLabel(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_ABSOLUTE_MODE");
		HDBAbsoluteLabel = new JLabel(msg);
		TDBAbsoluteLabel = new JLabel(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_RELATIVE_MODE");
		HDBRelativeLabel = new JLabel(msg);
		TDBRelativeLabel = new JLabel(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_THRESHOLD_MODE");
		HDBThresholdLabel = new JLabel(msg);
		TDBThresholdLabel = new JLabel(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_DIFFERENCE_MODE");
		HDBDifferenceLabel = new JLabel(msg);
		TDBDifferenceLabel = new JLabel(msg);

		msg = Messages.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_HDB");
		HDBLabel = new JLabel(msg);
		HDBLabel.setFont(HDBLabel.getFont().deriveFont(Font.BOLD));
		msg = Messages.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_TDB");
		TDBLabel = new JLabel(msg);
		TDBLabel.setFont(TDBLabel.getFont().deriveFont(Font.BOLD));

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_PERIOD_HDB");
		HDBperiodLabel = new JLabel(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_PERIOD_TDB");
		TDBperiodLabel = new JLabel(msg);

		/*
		 * msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD" );
		 * TDBkeepingPeriodLabel = new JLabel( msg );
		 */

		msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD");
		TDBexportPeriodLabel = new JLabel(msg);

		TDBexportPeriodComboBox = new ACTdbExportPeriodComboBox();
		// set this parameter not visible, its inserted as tdbarchiver class
		// property
		TDBexportPeriodLabel.setVisible(false);
		TDBexportPeriodComboBox.setVisible(false);

		/*
		 * TDBexportPeriodComboBox = new JComboBox(); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_5MN" );
		 * TDBexportPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_10MN" );
		 * TDBexportPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_30MN" );
		 * TDBexportPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_1H" );
		 * TDBexportPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_2H" );
		 * TDBexportPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_4H" );
		 * TDBexportPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_6H" );
		 * TDBexportPeriodComboBox.addItem( msg );
		 * TDBexportPeriodComboBox.setSelectedIndex( 1 );
		 */

		/*
		 * TDBkeepingPeriodComboBox = new JComboBox(); msg =
		 * Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_1H" );
		 * TDBkeepingPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_2H" );
		 * TDBkeepingPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_4H" );
		 * TDBkeepingPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_6H" );
		 * TDBkeepingPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_8H" );
		 * TDBkeepingPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_1D" );
		 * TDBkeepingPeriodComboBox.addItem( msg ); msg = Messages.getMessage(
		 * "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_KEEPING_PERIOD_3D" );
		 * TDBkeepingPeriodComboBox.addItem( msg );
		 * TDBkeepingPeriodComboBox.setSelectedIndex( 2 );
		 */

		HDBperiodField = new JTextField();
		TDBperiodField = new JTextField();

		HDBperiodField.setMaximumSize(new Dimension(60, 20));
		TDBperiodField.setMaximumSize(new Dimension(60, 20));

		/* TDBkeepingPeriodComboBox.setMaximumSize( new Dimension( 60 , 20 ) ); */
		TDBexportPeriodComboBox.setMaximumSize(new Dimension(60, 20));

		this.setHDBPeriod(Integer.parseInt(DEFAULT_HDB_PERIOD_VALUE));
		TDBperiodField.setText(DEFAULT_TDB_PERIOD_VALUE);

		HDBPeriodicalCheck = new JCheckBox();
		HDBPeriodicalCheck.setEnabled(false);
		HDBAbsoluteCheck = new JCheckBox();
		HDBRelativeCheck = new JCheckBox();
		HDBThresholdCheck = new JCheckBox();
		HDBDifferenceCheck = new JCheckBox();

		TDBPeriodicalCheck = new JCheckBox();
		TDBPeriodicalCheck.setEnabled(false);
		TDBAbsoluteCheck = new JCheckBox();
		TDBRelativeCheck = new JCheckBox();
		TDBThresholdCheck = new JCheckBox();
		TDBDifferenceCheck = new JCheckBox();

		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ABSOLUTE_PERIOD_HDB");
		HDBabsolutePeriodLabel = new JLabel(msg);
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ABSOLUTE_PERIOD_TDB");
		TDBabsolutePeriodLabel = new JLabel(msg);
		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ABSOLUTE_LOWER");
		HDBabsoluteLowerLabel = new JLabel(msg);
		TDBabsoluteLowerLabel = new JLabel(msg);
		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ABSOLUTE_UPPER");
		HDBabsoluteUpperLabel = new JLabel(msg);
		TDBabsoluteUpperLabel = new JLabel(msg);

		HDBabsolutePeriodField = new JTextField();
		TDBabsolutePeriodField = new JTextField();
		HDBabsoluteLowerField = new JTextField();
		TDBabsoluteLowerField = new JTextField();
		HDBabsoluteUpperField = new JTextField();
		TDBabsoluteUpperField = new JTextField();
		HDBAbsoluteDLCheck = new JCheckBox("Slow drift");
		TDBAbsoluteDLCheck = new JCheckBox("Slow drift");

		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_RELATIVE_PERIOD_HDB");
		HDBrelativePeriodLabel = new JLabel(msg);
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_RELATIVE_PERIOD_TDB");
		TDBrelativePeriodLabel = new JLabel(msg);
		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_RELATIVE_LOWER");
		HDBrelativeLowerLabel = new JLabel(msg);
		TDBrelativeLowerLabel = new JLabel(msg);
		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_RELATIVE_UPPER");
		HDBrelativeUpperLabel = new JLabel(msg);
		TDBrelativeUpperLabel = new JLabel(msg);

		HDBrelativePeriodField = new JTextField();
		TDBrelativePeriodField = new JTextField();
		HDBrelativeLowerField = new JTextField();
		TDBrelativeLowerField = new JTextField();
		HDBrelativeUpperField = new JTextField();
		TDBrelativeUpperField = new JTextField();
		// adding slow drift check box
		HDBRelativeDLCheck = new JCheckBox("Slow drift");
		TDBRelativeDLCheck = new JCheckBox("Slow drift");

		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_THRESHOLD_PERIOD_HDB");
		HDBthresholdPeriodLabel = new JLabel(msg);
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_THRESHOLD_PERIOD_TDB");
		TDBthresholdPeriodLabel = new JLabel(msg);
		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_THRESHOLD_LOWER");
		HDBthresholdLowerLabel = new JLabel(msg);
		TDBthresholdLowerLabel = new JLabel(msg);
		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_THRESHOLD_UPPER");
		HDBthresholdUpperLabel = new JLabel(msg);
		TDBthresholdUpperLabel = new JLabel(msg);

		HDBthresholdPeriodField = new JTextField();
		TDBthresholdPeriodField = new JTextField();
		HDBthresholdLowerField = new JTextField();
		TDBthresholdLowerField = new JTextField();
		HDBthresholdUpperField = new JTextField();
		TDBthresholdUpperField = new JTextField();

		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_DIFFERENCE_PERIOD_HDB");
		HDBdifferencePeriodLabel = new JLabel(msg);
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_DIFFERENCE_PERIOD_TDB");
		TDBdifferencePeriodLabel = new JLabel(msg);

		HDBdifferencePeriodField = new JTextField();
		TDBdifferencePeriodField = new JTextField();

		initCenterPanel();
	}

	/**
     *
     */
	private void initButtonsBox() {
		String msg;

		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ACTIONS_SAVE");
		saveDefaultsButton = new JButton(new SaveACDefaults(msg));

		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ACTIONS_LOAD");
		loadDefaultsButton = new JButton(new LoadACDefaults(msg, false));

		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_ACTIONS_RESTORE");
		restoreDefaultsButton = new JButton(new LoadACDefaults(msg, true));

		buttonsBox = new Box(BoxLayout.X_AXIS);
		buttonsBox.add(Box.createHorizontalGlue());
		buttonsBox.add(saveDefaultsButton);
		buttonsBox.add(Box.createHorizontalStrut(5));
		buttonsBox.add(loadDefaultsButton);
		buttonsBox.add(Box.createHorizontalStrut(5));
		buttonsBox.add(restoreDefaultsButton);
		buttonsBox.add(Box.createHorizontalGlue());
	}

	/**
	 * 6 sept. 2005
	 */
	private void initKeepingPeriodsTable() {
		keepingPeriodsTable = new long[8];
		reverseKeepingPeriodsTable = new Hashtable(8);

		long millisecondsInOneHour = 60000 * 60;
		keepingPeriodsTable[0] = 1 * millisecondsInOneHour;
		keepingPeriodsTable[1] = 2 * millisecondsInOneHour;
		keepingPeriodsTable[2] = 4 * millisecondsInOneHour;
		keepingPeriodsTable[3] = 6 * millisecondsInOneHour;
		keepingPeriodsTable[4] = 8 * millisecondsInOneHour;
		keepingPeriodsTable[5] = 12 * millisecondsInOneHour;
		keepingPeriodsTable[6] = 1 * 24 * millisecondsInOneHour;
		keepingPeriodsTable[7] = 3 * 24 * millisecondsInOneHour;

		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[0]),
				new Integer(0));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[1]),
				new Integer(1));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[2]),
				new Integer(2));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[3]),
				new Integer(3));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[4]),
				new Integer(4));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[5]),
				new Integer(5));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[6]),
				new Integer(6));
		reverseKeepingPeriodsTable.put(new Long(keepingPeriodsTable[7]),
				new Integer(7));
	}

	/**
	 * 6 sept. 2005
	 */
	/*
	 * private void initExportPeriodsTable () { exportPeriodsTable = new long[ 7
	 * ]; reverseExportPeriodsTable = new Hashtable( 7 );
	 * 
	 * long millisecondsInOneMinute = 60000; exportPeriodsTable[ 0 ] = 5 *
	 * millisecondsInOneMinute; exportPeriodsTable[ 1 ] = 10 *
	 * millisecondsInOneMinute; exportPeriodsTable[ 2 ] = 30 *
	 * millisecondsInOneMinute; exportPeriodsTable[ 3 ] = 1 * 60 *
	 * millisecondsInOneMinute; exportPeriodsTable[ 4 ] = 2 * 60 *
	 * millisecondsInOneMinute; exportPeriodsTable[ 5 ] = 4 * 60 *
	 * millisecondsInOneMinute; exportPeriodsTable[ 6 ] = 6 * 60 *
	 * millisecondsInOneMinute;
	 * 
	 * reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 0 ] ) , new
	 * Integer( 0 ) ); reverseExportPeriodsTable.put( new Long(
	 * exportPeriodsTable[ 1 ] ) , new Integer( 1 ) );
	 * reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 2 ] ) , new
	 * Integer( 2 ) ); reverseExportPeriodsTable.put( new Long(
	 * exportPeriodsTable[ 3 ] ) , new Integer( 3 ) );
	 * reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 4 ] ) , new
	 * Integer( 4 ) ); reverseExportPeriodsTable.put( new Long(
	 * exportPeriodsTable[ 5 ] ) , new Integer( 5 ) );
	 * reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 6 ] ) , new
	 * Integer( 6 ) ); }
	 */

	/**
	 * 20 juil. 2005
	 */
	private void initCenterPanel() {
		centerPanel = new JPanel();
		HDBPanel = new JPanel();
		HDBPanel.setLayout(new SpringLayout());
		TDBPanel = new JPanel();
		TDBPanel.setLayout(new SpringLayout());

		Dimension emptyBoxDimension = new Dimension(40, 20);
		/*
		 * //---------------- centerPanel.add( HDBLabel ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * centerPanel.add( HDBperiodLabel ); centerPanel.add( HDBperiodField );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * //--------------------
		 * 
		 * centerPanel.add( HDBmodesLabel ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * centerPanel.add( HDBPeriodicalLabel ); centerPanel.add(
		 * HDBPeriodicalCheck ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) );
		 * 
		 * centerPanel.add( HDBAbsoluteLabel ); centerPanel.add(
		 * HDBAbsoluteCheck ); centerPanel.add( HDBabsolutePeriodLabel );
		 * centerPanel.add( HDBabsolutePeriodField ); centerPanel.add(
		 * HDBabsoluteLowerLabel ); centerPanel.add( HDBabsoluteLowerField );
		 * centerPanel.add( HDBabsoluteUpperLabel ); centerPanel.add(
		 * HDBabsoluteUpperField );
		 * 
		 * centerPanel.add( HDBRelativeLabel ); centerPanel.add(
		 * HDBRelativeCheck ); centerPanel.add( HDBrelativePeriodLabel );
		 * centerPanel.add( HDBrelativePeriodField ); centerPanel.add(
		 * HDBrelativeLowerLabel ); centerPanel.add( HDBrelativeLowerField );
		 * centerPanel.add( HDBrelativeUpperLabel ); centerPanel.add(
		 * HDBrelativeUpperField );
		 * 
		 * centerPanel.add( HDBThresholdLabel ); centerPanel.add(
		 * HDBThresholdCheck ); centerPanel.add( HDBthresholdPeriodLabel );
		 * centerPanel.add( HDBthresholdPeriodField ); centerPanel.add(
		 * HDBthresholdLowerLabel ); centerPanel.add( HDBthresholdLowerField );
		 * centerPanel.add( HDBthresholdUpperLabel ); centerPanel.add(
		 * HDBthresholdUpperField );
		 * 
		 * centerPanel.add( HDBDifferenceLabel ); centerPanel.add(
		 * HDBDifferenceCheck ); centerPanel.add( HDBdifferencePeriodLabel );
		 * centerPanel.add( HDBdifferencePeriodField ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) );
		 * //----------------------------
		 * 
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * //--------------------------- centerPanel.add( TDBLabel );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * centerPanel.add( TDBperiodLabel ); centerPanel.add( TDBperiodField );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * centerPanel.add( TDBexportPeriodLabel ); centerPanel.add(
		 * TDBexportPeriodComboBox ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) );
		 * 
		 * centerPanel.add( TDBkeepingPeriodLabel ); centerPanel.add(
		 * TDBkeepingPeriodComboBox ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) );
		 * 
		 * //------------------------- centerPanel.add( TDBmodesLabel );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * centerPanel.add( TDBPeriodicalLabel ); centerPanel.add(
		 * TDBPeriodicalCheck ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); centerPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) );
		 * 
		 * centerPanel.add( TDBAbsoluteLabel ); centerPanel.add(
		 * TDBAbsoluteCheck ); centerPanel.add( TDBabsolutePeriodLabel );
		 * centerPanel.add( TDBabsolutePeriodField ); centerPanel.add(
		 * TDBabsoluteLowerLabel ); centerPanel.add( TDBabsoluteLowerField );
		 * centerPanel.add( TDBabsoluteUpperLabel ); centerPanel.add(
		 * TDBabsoluteUpperField );
		 * 
		 * centerPanel.add( TDBRelativeLabel ); centerPanel.add(
		 * TDBRelativeCheck ); centerPanel.add( TDBrelativePeriodLabel );
		 * centerPanel.add( TDBrelativePeriodField ); centerPanel.add(
		 * TDBrelativeLowerLabel ); centerPanel.add( TDBrelativeLowerField );
		 * centerPanel.add( TDBrelativeUpperLabel ); centerPanel.add(
		 * TDBrelativeUpperField );
		 * 
		 * centerPanel.add( TDBThresholdLabel ); centerPanel.add(
		 * TDBThresholdCheck ); centerPanel.add( TDBthresholdPeriodLabel );
		 * centerPanel.add( TDBthresholdPeriodField ); centerPanel.add(
		 * TDBthresholdLowerLabel ); centerPanel.add( TDBthresholdLowerField );
		 * centerPanel.add( TDBthresholdUpperLabel ); centerPanel.add(
		 * TDBthresholdUpperField );
		 * 
		 * centerPanel.add( TDBDifferenceLabel ); centerPanel.add(
		 * TDBDifferenceCheck ); centerPanel.add( TDBdifferencePeriodLabel );
		 * centerPanel.add( TDBdifferencePeriodField ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) ); centerPanel.add(
		 * Box.createRigidArea( emptyBoxDimension ) );
		 * 
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * centerPanel.add( Box.createRigidArea( emptyBoxDimension ) );
		 * //-------------------------
		 * 
		 * 
		 * 
		 * centerPanel.setLayout( new SpringLayout() ); //Lay out the panel.
		 * SpringUtilities.makeCompactGrid( centerPanel , 20 , 8 , //rows, cols
		 * 6 , 6 , //initX, initY 6 , 6 , true ); //xPad, yPad
		 */
		Dimension size = TDBexportPeriodComboBox.getPreferredSize();
		size.height = 20;
		HDBperiodField.setPreferredSize(size);
		HDBPanel.add(HDBLabel);
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		HDBPanel.add(HDBperiodLabel);
		HDBPanel.add(HDBperiodField);
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		// --------------------

		HDBPanel.add(HDBmodesLabel);
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		HDBPanel.add(HDBPeriodicalLabel);
		HDBPanel.add(HDBPeriodicalCheck);
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		HDBPanel.add(HDBAbsoluteLabel);
		HDBPanel.add(HDBAbsoluteCheck);
		HDBPanel.add(HDBabsolutePeriodLabel);
		HDBPanel.add(HDBabsolutePeriodField);
		HDBPanel.add(HDBabsoluteLowerLabel);
		HDBPanel.add(HDBabsoluteLowerField);
		HDBPanel.add(HDBabsoluteUpperLabel);
		HDBPanel.add(HDBabsoluteUpperField);
		HDBPanel.add(HDBAbsoluteDLCheck);

		HDBPanel.add(HDBRelativeLabel);
		HDBPanel.add(HDBRelativeCheck);
		HDBPanel.add(HDBrelativePeriodLabel);
		HDBPanel.add(HDBrelativePeriodField);
		HDBPanel.add(HDBrelativeLowerLabel);
		HDBPanel.add(HDBrelativeLowerField);
		HDBPanel.add(HDBrelativeUpperLabel);
		HDBPanel.add(HDBrelativeUpperField);
		HDBPanel.add(HDBRelativeDLCheck);

		HDBPanel.add(HDBThresholdLabel);
		HDBPanel.add(HDBThresholdCheck);
		HDBPanel.add(HDBthresholdPeriodLabel);
		HDBPanel.add(HDBthresholdPeriodField);
		HDBPanel.add(HDBthresholdLowerLabel);
		HDBPanel.add(HDBthresholdLowerField);
		HDBPanel.add(HDBthresholdUpperLabel);
		HDBPanel.add(HDBthresholdUpperField);
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		HDBPanel.add(HDBDifferenceLabel);
		HDBPanel.add(HDBDifferenceCheck);
		HDBPanel.add(HDBdifferencePeriodLabel);
		HDBPanel.add(HDBdifferencePeriodField);
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		HDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		// ----------------------------

		// ---------------------------
		TDBPanel.add(TDBLabel);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		TDBPanel.add(TDBperiodLabel);
		TDBPanel.add(TDBperiodField);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		TDBPanel.add(TDBexportPeriodLabel);
		TDBPanel.add(TDBexportPeriodComboBox);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		/*
		 * TDBPanel.add( TDBkeepingPeriodLabel ); TDBPanel.add(
		 * TDBkeepingPeriodComboBox ); TDBPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); TDBPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); TDBPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); TDBPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); TDBPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) ); TDBPanel.add( Box.createRigidArea(
		 * emptyBoxDimension ) );
		 */

		// -------------------------
		TDBPanel.add(TDBmodesLabel);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		TDBPanel.add(TDBPeriodicalLabel);
		TDBPanel.add(TDBPeriodicalCheck);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		TDBPanel.add(TDBAbsoluteLabel);
		TDBPanel.add(TDBAbsoluteCheck);
		TDBPanel.add(TDBabsolutePeriodLabel);
		TDBPanel.add(TDBabsolutePeriodField);
		TDBPanel.add(TDBabsoluteLowerLabel);
		TDBPanel.add(TDBabsoluteLowerField);
		TDBPanel.add(TDBabsoluteUpperLabel);
		TDBPanel.add(TDBabsoluteUpperField);
		TDBPanel.add(TDBAbsoluteDLCheck);

		TDBPanel.add(TDBRelativeLabel);
		TDBPanel.add(TDBRelativeCheck);
		TDBPanel.add(TDBrelativePeriodLabel);
		TDBPanel.add(TDBrelativePeriodField);
		TDBPanel.add(TDBrelativeLowerLabel);
		TDBPanel.add(TDBrelativeLowerField);
		TDBPanel.add(TDBrelativeUpperLabel);
		TDBPanel.add(TDBrelativeUpperField);
		TDBPanel.add(TDBRelativeDLCheck);

		TDBPanel.add(TDBThresholdLabel);
		TDBPanel.add(TDBThresholdCheck);
		TDBPanel.add(TDBthresholdPeriodLabel);
		TDBPanel.add(TDBthresholdPeriodField);
		TDBPanel.add(TDBthresholdLowerLabel);
		TDBPanel.add(TDBthresholdLowerField);
		TDBPanel.add(TDBthresholdUpperLabel);
		TDBPanel.add(TDBthresholdUpperField);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		TDBPanel.add(TDBDifferenceLabel);
		TDBPanel.add(TDBDifferenceCheck);
		TDBPanel.add(TDBdifferencePeriodLabel);
		TDBPanel.add(TDBdifferencePeriodField);
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));

		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		TDBPanel.add(Box.createRigidArea(emptyBoxDimension));
		// -------------------------

		centerPanel.add(HDBPanel);

		JPanel warningPanel = new JPanel();
		warningPanel.setLayout(new SpringLayout());
		String warning = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_PERIOD_WARNING1");
		warning += Integer.MAX_VALUE + "ms (=" + Integer.MAX_VALUE / 1000
				+ "s). ";
		warning += Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_PERIOD_WARNING2");
		JLabel warningLabel = new JLabel(warning);
		Font font = new Font("Arial", Font.BOLD, 10);
		warningLabel.setForeground(Color.RED);
		JLabel label = new JLabel("*");
		label.setFont(font);
		warningPanel.add(label);
		warningPanel.add(warningLabel);
		SpringUtilities.makeCompactGrid(warningPanel, 1, 2, // rows, cols
				6, 6, // initX, initY
				6, 6, true); // xPad, yPad
		centerPanel.add(warningPanel);

		centerPanel.add(TDBPanel);

		centerPanel.setLayout(new SpringLayout());
		// Lay out the panel.
		SpringUtilities.makeCompactGrid(centerPanel, 3, 1, // rows, cols
				0, 0, // initX, initY
				0, 0, true); // xPad, yPad
		// Lay out the panel.
		SpringUtilities.makeCompactGrid(HDBPanel, 8, 9, // rows, cols
				6, 6, // initX, initY
				6, 6, true); // xPad, yPad
		// Lay out the panel.
		SpringUtilities.makeCompactGrid(TDBPanel, 10, 9, // rows, cols
				6, 6, // initX, initY
				6, 6, true); // xPad, yPad
	}

	/**
	 * @return 22 juil. 2005
	 */
	public long getExportPeriod() {
		return TDBexportPeriodComboBox.getExportPeriod();
		/*
		 * int selected = TDBexportPeriodComboBox.getSelectedIndex(); return
		 * exportPeriodsTable[ selected ];
		 */
	}

	/**
	 * 
	 * @deprecated
	 */
	public long getKeepingPeriod() {
		/*
		 * int selected = TDBkeepingPeriodComboBox.getSelectedIndex(); return
		 * keepingPeriodsTable[ selected ];
		 */
		return 0;
	}

	/**
	 * @param historic
	 * @param type_a
	 *            22 juil. 2005
	 */
	public void doSelectMode(boolean _historic, int _type) {
		JCheckBox checkBoxToSelect = null;

		switch (_type) {
		case ArchivingConfigurationMode.TYPE_A:
			if (_historic) {
				checkBoxToSelect = HDBAbsoluteCheck;
			} else {
				checkBoxToSelect = TDBAbsoluteCheck;
			}
			break;

		case ArchivingConfigurationMode.TYPE_D:
			if (_historic) {
				checkBoxToSelect = HDBDifferenceCheck;
			} else {
				checkBoxToSelect = TDBDifferenceCheck;
			}
			break;

		case ArchivingConfigurationMode.TYPE_P:
			if (_historic) {
				checkBoxToSelect = HDBPeriodicalCheck;
			} else {
				checkBoxToSelect = TDBPeriodicalCheck;
			}
			break;

		case ArchivingConfigurationMode.TYPE_T:
			if (_historic) {
				checkBoxToSelect = HDBThresholdCheck;
			} else {
				checkBoxToSelect = TDBThresholdCheck;
			}
			break;

		case ArchivingConfigurationMode.TYPE_R:
			if (_historic) {
				checkBoxToSelect = HDBRelativeCheck;
			} else {
				checkBoxToSelect = TDBRelativeCheck;
			}
			break;
		}

		checkBoxToSelect.setSelected(true);
	}

	/**
	 * @param modesTypes
	 *            25 juil. 2005
	 */
	public void setSelectedModes(boolean _historic, Vector modesTypes) {
		if (_historic) {
			HDBRelativeCheck.setSelected(false);
			HDBPeriodicalCheck.setSelected(false);
			HDBAbsoluteCheck.setSelected(false);
			HDBDifferenceCheck.setSelected(false);
			HDBThresholdCheck.setSelected(false);
		} else {
			TDBRelativeCheck.setSelected(false);
			TDBPeriodicalCheck.setSelected(false);
			TDBAbsoluteCheck.setSelected(false);
			TDBDifferenceCheck.setSelected(false);
			TDBThresholdCheck.setSelected(false);
		}

		if (modesTypes == null) {
			return;
		}

		Enumeration enumeration = modesTypes.elements();
		while (enumeration.hasMoreElements()) {
			Integer _nextModeType = (Integer) enumeration.nextElement();
			int nextModeType = _nextModeType.intValue();
			// System.out.println(
			// "AttributesPropertiesPanel/setSelectedModes/nextModeType/"+nextModeType+"/"
			// );
			doSelectMode(_historic, nextModeType);
		}

	}

	// ------------up/low getters
	public String getHDBAbsoluteUpper() {
		return HDBabsoluteUpperField.getText();
	}

	public String getHDBRelativeUpper() {
		return HDBrelativeUpperField.getText();
	}

	public String getHDBThresholdUpper() {
		return HDBthresholdUpperField.getText();
	}

	public String getHDBAbsoluteLower() {
		return HDBabsoluteLowerField.getText();
	}

	public String getHDBRelativeLower() {
		return HDBrelativeLowerField.getText();
	}

	public String getHDBThresholdLower() {
		return HDBthresholdLowerField.getText();
	}

	public String getTDBAbsoluteUpper() {
		return TDBabsoluteUpperField.getText();
	}

	public String getTDBRelativeUpper() {
		return TDBrelativeUpperField.getText();
	}

	public String getTDBThresholdUpper() {
		return TDBthresholdUpperField.getText();
	}

	public String getTDBAbsoluteLower() {
		return TDBabsoluteLowerField.getText();
	}

	public String getTDBRelativeLower() {
		return TDBrelativeLowerField.getText();
	}

	public String getTDBThresholdLower() {
		return TDBthresholdLowerField.getText();
	}

	// ------------
	// -----up/low getters
	public void setHDBAbsoluteUpper(String val) {
		HDBabsoluteUpperField.setText(val);
	}

	public void setHDBRelativeUpper(String val) {
		HDBrelativeUpperField.setText(val);
	}

	public void setHDBThresholdUpper(String val) {
		HDBthresholdUpperField.setText(val);
	}

	public void setHDBAbsoluteLower(String val) {
		HDBabsoluteLowerField.setText(val);
	}

	public void setHDBRelativeLower(String val) {
		HDBrelativeLowerField.setText(val);
	}

	public void setHDBThresholdLower(String val) {
		HDBthresholdLowerField.setText(val);
	}

	public void setTDBAbsoluteUpper(String val) {
		TDBabsoluteUpperField.setText(val);
	}

	public void setTDBRelativeUpper(String val) {
		TDBrelativeUpperField.setText(val);
	}

	public void setTDBThresholdUpper(String val) {
		TDBthresholdUpperField.setText(val);
	}

	public void setTDBAbsoluteLower(String val) {
		TDBabsoluteLowerField.setText(val);
	}

	public void setTDBRelativeLower(String val) {
		TDBrelativeLowerField.setText(val);
	}

	public void setTDBThresholdLower(String val) {
		TDBthresholdLowerField.setText(val);
	}

	// -----

	/**
	 * @param period
	 *            25 juil. 2005
	 */
	public void setHDBPeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period / 1000);
		this.HDBperiodField.setText(text);
	}

	public int getHDBperiod() {
		try {
			String s = HDBperiodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				} else if (i > Integer.MAX_VALUE / 1000) {
					i = Integer.MAX_VALUE / 1000;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE / 1000;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			i *= 1000;
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getTDBperiod() {
		try {
			String s = TDBperiodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getHDBAbsolutePeriod() {
		try {
			String s = HDBabsolutePeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				} else if (i > Integer.MAX_VALUE / 1000) {
					i = Integer.MAX_VALUE / 1000;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE / 1000;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			i *= 1000;
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getHDBRelativePeriod() {
		try {
			String s = HDBrelativePeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				} else if (i > Integer.MAX_VALUE / 1000) {
					i = Integer.MAX_VALUE / 1000;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE / 1000;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			i *= 1000;
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getHDBThresholdPeriod() {
		try {
			String s = HDBthresholdPeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				} else if (i > Integer.MAX_VALUE / 1000) {
					i = Integer.MAX_VALUE / 1000;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE / 1000;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			i *= 1000;
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getHDBDifferencePeriod() {
		try {
			String s = HDBdifferencePeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				} else if (i > Integer.MAX_VALUE / 1000) {
					i = Integer.MAX_VALUE / 1000;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE / 1000;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			i *= 1000;
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getTDBAbsolutePeriod() {
		try {
			String s = TDBabsolutePeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getTDBRelativePeriod() {
		try {
			String s = TDBrelativePeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getTDBThresholdPeriod() {
		try {
			String s = TDBthresholdPeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public int getTDBDifferencePeriod() {
		try {
			String s = TDBdifferencePeriodField.getText();
			int i;
			try {
				i = Integer.parseInt(s);
				if (i < 0) {
					i = 0;
				}
			} catch (NumberFormatException n) {
				double d;
				try {
					d = Double.parseDouble(s);
					if (d > 0) {
						i = Integer.MAX_VALUE;
					} else {
						i = 0;
					}
				} catch (Exception e) {
					throw n;
				}
			}
			return i;
		} catch (Exception e) {
			return -1;
		}
	}

	public void setHDBAbsolutePeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period / 1000);
		this.HDBabsolutePeriodField.setText(text);
	}

	public void setHDBRelativePeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period / 1000);
		this.HDBrelativePeriodField.setText(text);
	}

	public void setHDBThresholdPeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period / 1000);
		this.HDBthresholdPeriodField.setText(text);
	}

	public void setHDBDifferencePeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period / 1000);
		this.HDBdifferencePeriodField.setText(text);
	}

	public void setTDBAbsolutePeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period);
		this.TDBabsolutePeriodField.setText(text);
	}

	public void setTDBRelativePeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period);
		this.TDBrelativePeriodField.setText(text);
	}

	public void setTDBThresholdPeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period);
		this.TDBthresholdPeriodField.setText(text);
	}

	public void setTDBDifferencePeriod(int period) {
		String text = period == -1 ? "" : String.valueOf(period);
		this.TDBdifferencePeriodField.setText(text);
	}

	/**
	 * @param period
	 *            25 juil. 2005
	 */
	public void setTDBPeriod(int period) {
		// System.out.println ( "ACTab/setTDBPeriod/TDB_PERIOD/"+s );

		String text = period == -1 ? "" : String.valueOf(period);
		this.TDBperiodField.setText(text);
	}

	/**
	 * @param period
	 *            25 juil. 2005
	 */
	public void setExportPeriod(long period) {
		TDBexportPeriodComboBox.setExportPeriod(period);

		/*
		 * int idx = 1; //default 10 minutes //System.out.println (
		 * "setExportPeriod/period/"+period+"/" ); try { Integer key = ( Integer
		 * ) reverseExportPeriodsTable.get( new Long( period ) ); if ( key ==
		 * null ) { idx = 1; } else { idx = key.intValue(); } } catch (
		 * Exception e ) { e.printStackTrace(); }
		 * this.TDBexportPeriodComboBox.setSelectedIndex( idx );
		 */
	}

	/**
	 * @param reverseExportPeriodsTable2
	 *            6 sept. 2005
	 */
	/*
	 * private void traceHT ( Hashtable ht ) { Enumeration enumeration =
	 * ht.keys(); while ( enumeration.hasMoreElements() ) { Long key = ( Long )
	 * enumeration.nextElement(); //System.out.println ( "key/"+key+"/" ); }
	 * 
	 * }
	 */

	/**
	 * @deprecated
	 */
	public void setKeepingPeriod(long period) {
		/*
		 * int idx = 2; //default 4 hours
		 * 
		 * try { Integer key = ( Integer ) reverseKeepingPeriodsTable.get( new
		 * Long( period ) ); if ( key == null ) { idx = 2; } else { idx =
		 * key.intValue(); } } catch ( Exception e ) { e.printStackTrace(); }
		 * this.TDBkeepingPeriodComboBox.setSelectedIndex( idx );
		 */

	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeHDB_A() {
		return HDBAbsoluteCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeHDB_P() {
		return HDBPeriodicalCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeHDB_R() {
		return HDBRelativeCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeHDB_T() {
		return HDBThresholdCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeHDB_D() {
		return HDBDifferenceCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeTDB_A() {
		return TDBAbsoluteCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeTDB_P() {
		return TDBPeriodicalCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeTDB_R() {
		return TDBRelativeCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeTDB_T() {
		return TDBThresholdCheck.isSelected();
	}

	/**
	 * @return 7 sept. 2005
	 */
	public boolean hasModeTDB_D() {
		return TDBDifferenceCheck.isSelected();
	}

	/**
	 * 13 sept. 2005
	 */
	public void doUnselectAllModes() {
		TDBPeriodicalCheck.setSelected(false);
		TDBAbsoluteCheck.setSelected(false);
		TDBRelativeCheck.setSelected(false);
		TDBThresholdCheck.setSelected(false);
		TDBDifferenceCheck.setSelected(false);

		HDBPeriodicalCheck.setSelected(false);
		HDBAbsoluteCheck.setSelected(false);
		HDBRelativeCheck.setSelected(false);
		HDBThresholdCheck.setSelected(false);
		HDBDifferenceCheck.setSelected(false);
	}

	public boolean getHDBAbsoluteDLCheck() {
		// TODO Auto-generated method stub
		return HDBAbsoluteDLCheck.isSelected();
	}

	public boolean getHDBRelativeDLCheck() {
		// TODO Auto-generated method stub
		return HDBRelativeDLCheck.isSelected();
	}

	public boolean getTDBAbsoluteDLCheck() {
		// TODO Auto-generated method stub
		return TDBAbsoluteDLCheck.isSelected();
	}

	public boolean getTDBRelativeDLCheck() {
		// TODO Auto-generated method stub
		return TDBRelativeDLCheck.isSelected();
	}

	public void setHDBAbsoluteDLCheck(boolean b) {
		// TODO Auto-generated method stub
		HDBAbsoluteDLCheck.setSelected(b);
	}

	public void setHDBRelativeDLCheck(boolean b) {
		// TODO Auto-generated method stub
		HDBRelativeDLCheck.setSelected(b);
	}

	public void setTDBAbsoluteDLCheck(boolean b) {
		// TODO Auto-generated method stub
		TDBAbsoluteDLCheck.setSelected(b);
	}

	public void setTDBRelativeDLCheck(boolean b) {
		// TODO Auto-generated method stub
		TDBRelativeDLCheck.setSelected(b);
	}

}
