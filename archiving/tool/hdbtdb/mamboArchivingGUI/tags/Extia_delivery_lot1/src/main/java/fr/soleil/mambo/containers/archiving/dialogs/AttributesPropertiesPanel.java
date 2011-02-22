//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/dialogs/AttributesPropertiesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesPropertiesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.23 $
//
// $Log: AttributesPropertiesPanel.java,v $
// Revision 1.23  2007/02/01 14:16:53  pierrejoseph
// Export Period is sometimes forced to 30 min.
//
// Revision 1.22  2007/01/17 09:34:52  ounsy
// added the missing TDB difference period field
//
// Revision 1.21  2006/12/08 07:34:42  pierrejoseph
// The periodic mode check box is no more disabled
//
// Revision 1.20  2006/12/07 16:45:39  ounsy
// removed keeping period
//
// Revision 1.19  2006/12/07 15:06:58  ounsy
// the modeP checkbox is no longer editable
//
// Revision 1.18  2006/10/19 12:38:35  ounsy
// added a display for the selected attribute's dedicated archiver
//
// Revision 1.17  2006/10/06 16:02:31  ounsy
// minor changes (L&F)
//
// Revision 1.16  2006/10/05 15:36:04  ounsy
// added a setCurrentlySelectedAttribute() method
//
// Revision 1.15  2006/09/05 14:11:03  ounsy
// updated for sampling compatibility
//
// Revision 1.14  2006/07/27 12:46:40  ounsy
// corrected verifyEachMode() for absolute and relative modes
//
// Revision 1.13  2006/06/20 16:03:00  ounsy
// avoids the bug of hdb/tdb information not transfered to attributes properties panel
//
// Revision 1.12  2006/06/15 15:38:29  ounsy
// added a sub-panel for archiver selection
//
// Revision 1.11  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.10  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.9  2006/05/16 12:49:41  ounsy
// modified imports
//
// Revision 1.8  2006/05/16 09:37:09  ounsy
// minor changes
//
// Revision 1.7  2006/03/27 14:06:43  ounsy
// added a global mode control before setting properties
//
// Revision 1.6  2006/03/20 10:35:24  ounsy
// removed useless logs
//
// Revision 1.5  2006/02/24 12:26:10  ounsy
// modified for HDB/TDB separation
//
// Revision 1.3  2005/12/15 11:25:24  ounsy
// period formating
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
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
package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.TdbSpec;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.archiving.SetAttributesGroupProperties;
import fr.soleil.mambo.components.archiving.ACTdbExportPeriodComboBox;
import fr.soleil.mambo.components.archiving.ArchiversComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class AttributesPropertiesPanel extends JPanel {
	private static AttributesPropertiesPanel instance = null;

	// Archiver choice objects
	private ArchiverChoiceDefaultCheckBox archiverChoiceDefaultCheckBox;
	private ArchiversComboBox archiverChoiceRunningComboBox;
	private ArchiverChoiceNameTextField archiverChoiceNameTextField;
	private JTextField currentArchiverTextField;
	private JTextField dedicatedArchiverTextField;
	// Archiver choice objects

	// HDB objects
	private JPanel HDBPanel, HDBPeriodPanel, HDBModePanel;
	private JLabel HDBLabel;
	private JLabel HDBperiodLabel;
	private JTextField HDBperiodField;
	private JLabel HDBmodesLabel;
	private JCheckBox HDBPeriodicalCheck;
	private JCheckBox HDBAbsoluteCheck;
	private JCheckBox HDBRelativeCheck;
	private JCheckBox HDBThresholdCheck;
	private JCheckBox HDBDifferenceCheck;
	// /////////////////////////
	private JCheckBox HDBAbsoluteDLCheck;
	private JCheckBox HDBRelativeDLCheck;
	// private JLabel HDBdifferencePrecisionLabel;
	// private JTextField HDBdifferencePrecisionField;
	// HDB objects

	// TDB objects
	private JPanel TDBPanel, TDBPeriodPanel, TDBModePanel;
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
	// /////////////////////////
	private JCheckBox TDBAbsoluteDLCheck;
	private JCheckBox TDBRelativeDLCheck;
	// private JLabel TDBdifferencePrecisionLabel;
	// private JTextField TDBdifferencePrecisionField;

	// TDB objects

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

	// general objects
	private JButton setButton;
	private JPanel centerPanel;

	// general objects

	public static final String DEFAULT_HDB_PERIOD_VALUE = "60000";

	public static final String DEFAULT_TDB_PERIOD_VALUE = "1000";

	private long[] keepingPeriodsTable;
	// private long[] exportPeriodsTable;
	// private Hashtable reverseExportPeriodsTable = new Hashtable();
	private Hashtable reverseKeepingPeriodsTable = new Hashtable();

	private boolean isHistoric;

	private Mode lastMode;

	private final static Dimension classicMaxDimension = new Dimension(
			Integer.MAX_VALUE, 21);
	private final static Dimension classicPrefDimension = new Dimension(60, 21);
	private final static Dimension classicFixedDimension = new Dimension(90, 21);

	/**
	 * @param b
	 * @return 8 juil. 2005
	 */
	public static AttributesPropertiesPanel getInstance(boolean isHistoric) {
		if (instance == null || instance.isHistoric() != isHistoric) {
			instance = new AttributesPropertiesPanel(isHistoric);
		}

		return instance;
	}

	public static AttributesPropertiesPanel getInstance() {
		return instance;
	}

	/**
     * 
     */
	public static void resetInstance() {
		instance = null;
	}

	/**
     * 
     */
	private AttributesPropertiesPanel(boolean isHistoric) {
		this.isHistoric = isHistoric;

		this.initComponents(isHistoric);
		this.addComponents();
		this.initLayout();

		ArchivingConfigurationAttributeProperties.resetCurrentProperties();
	}

	/**
	 * 19 juil. 2005
	 */
	private void initLayout() {
		this.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, 0,
				0, 0, 0, true);
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		this.add(centerPanel);
		this.add(Box.createVerticalGlue());
		JPanel setPanel = new JPanel();
		this.add(setButton);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents(boolean isHistoric) {
		// System.out.println ( "CLA/4/isHistoric/"+isHistoric );
		// initKeepingPeriodsTable();
		// initExportPeriodsTable();

		String msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_SET");
		setButton = new JButton(new SetAttributesGroupProperties(msg));
		setButton.setMaximumSize(classicMaxDimension);
		setButton.setPreferredSize(classicPrefDimension);

		initCenterPanel(isHistoric);
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
	private void initCenterPanel(boolean isHistoric) {
		this.initCenterComponents(isHistoric);
		centerPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(centerPanel, centerPanel
				.getComponentCount(), 1, 0, 0, 0, 0, true);

		ArchivingConfigurationAttributeProperties currentProperties = new ArchivingConfigurationAttributeProperties();
		ArchivingConfigurationAttributeProperties
				.setCurrentProperties(currentProperties);
	}

	private class ArchiverChoiceDefaultCheckBoxActionListener implements
			ActionListener {
		public ArchiverChoiceDefaultCheckBoxActionListener() {

		}

		public void actionPerformed(ActionEvent e) {
			boolean isDefault = AttributesPropertiesPanel.this.archiverChoiceDefaultCheckBox
					.isSelected();
			// System.out.println (
			// "CLA/ArchiverChoiceDefaultCheckBoxActionListener/isDefault/"+isDefault
			// );
			AttributesPropertiesPanel.this.setArchiverDefault(isDefault);
		}
	}

	private void initCenterComponents(boolean isHistoric) {
		centerPanel = new JPanel();

		archiverChoiceDefaultCheckBox = new ArchiverChoiceDefaultCheckBox();
		archiverChoiceRunningComboBox = new ArchiversComboBox(this.isHistoric);
		archiverChoiceNameTextField = new ArchiverChoiceNameTextField(" ");
		archiverChoiceNameTextField.setEditable(false);

		currentArchiverTextField = new JTextField(" ");
		currentArchiverTextField.setPreferredSize(classicPrefDimension);
		currentArchiverTextField.setMaximumSize(classicMaxDimension);
		currentArchiverTextField.setEditable(false);

		dedicatedArchiverTextField = new JTextField(" ");
		dedicatedArchiverTextField.setPreferredSize(classicPrefDimension);
		dedicatedArchiverTextField.setMaximumSize(classicMaxDimension);
		dedicatedArchiverTextField.setEditable(false);

		boolean initialConditions = true;
		archiverChoiceDefaultCheckBox.setSelected(initialConditions);
		this.setArchiverDefault(initialConditions);

		String msg = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_MODES");
		HDBmodesLabel = new JLabel(msg);
		HDBmodesLabel.setFont(HDBmodesLabel.getFont().deriveFont(Font.ITALIC));
		HDBmodesLabel.setPreferredSize(classicPrefDimension);
		HDBmodesLabel.setMaximumSize(classicMaxDimension);
		TDBmodesLabel = new JLabel(msg);
		TDBmodesLabel.setFont(TDBmodesLabel.getFont().deriveFont(Font.ITALIC));
		TDBmodesLabel.setPreferredSize(classicPrefDimension);
		TDBmodesLabel.setMaximumSize(classicMaxDimension);

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

		HDBperiodField.setMaximumSize(classicFixedDimension);
		HDBperiodField.setPreferredSize(classicFixedDimension);
		TDBperiodField.setMaximumSize(classicFixedDimension);
		TDBperiodField.setPreferredSize(classicFixedDimension);

		/*
		 * TDBkeepingPeriodComboBox.setMaximumSize( classicFixedDimension );
		 * TDBkeepingPeriodComboBox.setPreferredSize( classicFixedDimension );
		 */
		TDBexportPeriodComboBox.setMaximumSize(classicFixedDimension);
		TDBexportPeriodComboBox.setPreferredSize(classicFixedDimension);

		HDBPeriodicalCheck = new JCheckBox();
		// HDBPeriodicalCheck.setEnabled ( false );
		HDBAbsoluteCheck = new JCheckBox();
		HDBRelativeCheck = new JCheckBox();
		HDBThresholdCheck = new JCheckBox();
		HDBDifferenceCheck = new JCheckBox();

		TDBPeriodicalCheck = new JCheckBox();
		// TDBPeriodicalCheck.setEnabled ( false );
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
		HDBabsolutePeriodField.setMaximumSize(classicMaxDimension);
		HDBabsolutePeriodField.setPreferredSize(classicPrefDimension);
		TDBabsolutePeriodField.setMaximumSize(classicMaxDimension);
		TDBabsolutePeriodField.setPreferredSize(classicPrefDimension);
		HDBabsoluteLowerField.setMaximumSize(classicMaxDimension);
		HDBabsoluteLowerField.setPreferredSize(classicPrefDimension);
		TDBabsoluteLowerField.setMaximumSize(classicMaxDimension);
		TDBabsoluteLowerField.setPreferredSize(classicPrefDimension);
		HDBabsoluteUpperField.setMaximumSize(classicMaxDimension);
		HDBabsoluteUpperField.setPreferredSize(classicPrefDimension);
		TDBabsoluteUpperField.setMaximumSize(classicMaxDimension);
		TDBabsoluteUpperField.setPreferredSize(classicPrefDimension);

		HDBAbsoluteDLCheck = new JCheckBox("slow drift");
		TDBAbsoluteDLCheck = new JCheckBox("slow drift");

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
		HDBrelativePeriodField.setMaximumSize(classicMaxDimension);
		HDBrelativePeriodField.setPreferredSize(classicPrefDimension);
		TDBrelativePeriodField.setMaximumSize(classicMaxDimension);
		TDBrelativePeriodField.setPreferredSize(classicPrefDimension);
		HDBrelativeLowerField.setMaximumSize(classicMaxDimension);
		HDBrelativeLowerField.setPreferredSize(classicPrefDimension);
		TDBrelativeLowerField.setMaximumSize(classicMaxDimension);
		TDBrelativeLowerField.setPreferredSize(classicPrefDimension);
		HDBrelativeUpperField.setMaximumSize(classicMaxDimension);
		HDBrelativeUpperField.setPreferredSize(classicPrefDimension);
		TDBrelativeUpperField.setMaximumSize(classicMaxDimension);
		TDBrelativeUpperField.setPreferredSize(classicPrefDimension);
		HDBRelativeDLCheck = new JCheckBox("slow drift");
		TDBRelativeDLCheck = new JCheckBox("slow drift");

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
		HDBthresholdPeriodField.setMaximumSize(classicMaxDimension);
		HDBthresholdPeriodField.setPreferredSize(classicPrefDimension);
		TDBthresholdPeriodField.setMaximumSize(classicMaxDimension);
		TDBthresholdPeriodField.setPreferredSize(classicPrefDimension);
		HDBthresholdLowerField.setMaximumSize(classicMaxDimension);
		HDBthresholdLowerField.setPreferredSize(classicPrefDimension);
		TDBthresholdLowerField.setMaximumSize(classicMaxDimension);
		TDBthresholdLowerField.setPreferredSize(classicPrefDimension);
		HDBthresholdUpperField.setMaximumSize(classicMaxDimension);
		HDBthresholdUpperField.setPreferredSize(classicPrefDimension);
		TDBthresholdUpperField.setMaximumSize(classicMaxDimension);
		TDBthresholdUpperField.setPreferredSize(classicPrefDimension);

		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_DIFFERENCE_PERIOD_HDB");
		HDBdifferencePeriodLabel = new JLabel(msg);
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_DIFFERENCE_PERIOD_TDB");
		TDBdifferencePeriodLabel = new JLabel(msg);

		HDBdifferencePeriodField = new JTextField();
		TDBdifferencePeriodField = new JTextField();
		HDBdifferencePeriodField.setMaximumSize(classicMaxDimension);
		HDBdifferencePeriodField.setPreferredSize(classicPrefDimension);
		TDBdifferencePeriodField.setMaximumSize(classicMaxDimension);
		TDBdifferencePeriodField.setPreferredSize(classicPrefDimension);
		// HDBdifferencePrecisionLabel = new JLabel("Precision value: ");
		// HDBdifferencePrecisionField = new JTextField("0");

		// TDBdifferencePrecisionLabel = new JLabel("Precision value: ");
		// TDBdifferencePrecisionField = new JTextField("0");

		initCenterSubPanel(isHistoric);
	}

	/**
	 * 20 juil. 2005
	 */
	private void initCenterSubPanel(final boolean isHistoric) {
		Dimension size = TDBexportPeriodComboBox.getPreferredSize();
		size.height = 20;

		// System.out.println ( "CLA/5/isHistoric/"+isHistoric );

		if (isHistoric) {
			// System.out.println ( "CLA/6 HDB" );
			HDBPanel = new JPanel();
			HDBPanel.setLayout(new SpringLayout());

			HDBPeriodPanel = new JPanel();
			HDBPeriodPanel.setLayout(new SpringLayout());

			HDBModePanel = new JPanel();
			HDBModePanel.setLayout(new SpringLayout());

			HDBperiodField.setPreferredSize(size);

			HDBPeriodPanel.add(HDBperiodLabel);
			HDBPeriodPanel.add(HDBperiodField);
			HDBPeriodPanel.add(Box.createHorizontalGlue());

			// --------------------

			JPanel panelM = new JPanel();
			JPanel panelP = new JPanel();
			JPanel panelA = new JPanel();
			JPanel panelR = new JPanel();
			JPanel panelT = new JPanel();
			JPanel panelD = new JPanel();
			HDBModePanel.add(panelM);
			HDBModePanel.add(panelP);
			HDBModePanel.add(panelA);
			HDBModePanel.add(panelR);
			HDBModePanel.add(panelT);
			HDBModePanel.add(panelD);
			SpringUtilities.makeCompactGrid(HDBModePanel, 6, 1, 3, 3, 3, 5,
					true);

			panelM.setLayout(new SpringLayout());
			panelM.add(HDBmodesLabel);
			SpringUtilities.makeCompactGrid(panelM, 1, 1, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			// Periodic Mode
			panelP.setBorder(BorderFactory.createEtchedBorder());
			panelP.setLayout(new SpringLayout());
			panelP.add(HDBPeriodicalLabel);
			panelP.add(HDBPeriodicalCheck);
			SpringUtilities.makeCompactGrid(panelP, 1, 2, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			// Absolute Mode

			panelA.setBorder(BorderFactory.createEtchedBorder());
			panelA.add(HDBAbsoluteLabel);
			panelA.add(HDBAbsoluteCheck);
			panelA.add(Box.createHorizontalGlue());
			panelA.add(Box.createHorizontalGlue());
			panelA.add(Box.createHorizontalGlue());

			panelA.add(HDBabsolutePeriodLabel);
			panelA.add(HDBabsolutePeriodField);
			panelA.add(Box.createHorizontalGlue());

			panelA.add(HDBAbsoluteDLCheck);
			panelA.add(Box.createHorizontalGlue());

			panelA.add(HDBabsoluteLowerLabel);
			panelA.add(HDBabsoluteLowerField);
			panelA.add(Box.createHorizontalGlue());
			panelA.add(HDBabsoluteUpperLabel);
			panelA.add(HDBabsoluteUpperField);
			panelA.setLayout(new SpringLayout());
			SpringUtilities.makeCompactGrid(panelA, 3, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			setAbsoluteModeParameters(HDBAbsoluteCheck.isSelected(), isHistoric);
			HDBAbsoluteCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					setAbsoluteModeParameters(HDBAbsoluteCheck.isSelected(),
							isHistoric);
				}

			});

			// Relative Mode
			panelR.setLayout(new SpringLayout());

			panelR.setBorder(BorderFactory.createEtchedBorder());

			panelR.add(HDBRelativeLabel);
			panelR.add(HDBRelativeCheck);
			panelR.add(Box.createHorizontalGlue());
			panelR.add(Box.createHorizontalGlue());
			panelR.add(Box.createHorizontalGlue());

			panelR.add(HDBrelativePeriodLabel);
			panelR.add(HDBrelativePeriodField);
			panelR.add(Box.createHorizontalGlue());
			panelR.add(HDBRelativeDLCheck);
			panelR.add(Box.createHorizontalGlue());

			panelR.add(HDBrelativeLowerLabel);
			panelR.add(HDBrelativeLowerField);
			panelR.add(Box.createHorizontalGlue());
			panelR.add(HDBrelativeUpperLabel);
			panelR.add(HDBrelativeUpperField);
			SpringUtilities.makeCompactGrid(panelR, 3, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			setRelativeModeParameters(HDBThresholdCheck.isSelected(),
					isHistoric);
			HDBRelativeCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					setRelativeModeParameters(HDBRelativeCheck.isSelected(),
							isHistoric);
					// TODO Auto-generated method stub
				}

			});

			// Threshhold Mode
			panelT.setLayout(new SpringLayout());
			panelT.setBorder(BorderFactory.createEtchedBorder());

			panelT.add(HDBThresholdLabel);
			panelT.add(HDBThresholdCheck);
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());

			panelT.add(HDBthresholdPeriodLabel);
			panelT.add(HDBthresholdPeriodField);
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());

			panelT.add(HDBthresholdLowerLabel);
			panelT.add(HDBthresholdLowerField);
			panelT.add(Box.createHorizontalGlue());
			panelT.add(HDBthresholdUpperLabel);
			panelT.add(HDBthresholdUpperField);
			SpringUtilities.makeCompactGrid(panelT, 3, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			setThresholdModeParameters(HDBThresholdCheck.isSelected(),
					isHistoric);
			HDBThresholdCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					setThresholdModeParameters(HDBThresholdCheck.isSelected(),
							isHistoric);
				}

			});

			// Difference Mode
			panelD.setLayout(new SpringLayout());
			panelD.setBorder(BorderFactory.createEtchedBorder());

			panelD.add(HDBDifferenceLabel);
			panelD.add(HDBDifferenceCheck);
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());

			panelD.add(HDBdifferencePeriodLabel);
			panelD.add(HDBdifferencePeriodField);
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());

			SpringUtilities.makeCompactGrid(panelD, 2, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);
			setDifferenceModeParameters(HDBDifferenceCheck.isSelected(),
					isHistoric);
			HDBDifferenceCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					setDifferenceModeParameters(
							HDBDifferenceCheck.isSelected(), isHistoric);
				}

			});

			// ----------------------------

			HDBPanel.add(HDBPeriodPanel);
			HDBPanel.add(Box.createVerticalGlue());
			HDBPanel.add(HDBModePanel);

			// Lay out the panels.
			SpringUtilities.makeCompactGrid(HDBPeriodPanel, 1, HDBPeriodPanel
					.getComponentCount(), // rows, cols
					0, 0, // initX, initY
					0, 5, // xPad, yPad
					true);
			SpringUtilities.makeCompactGrid(HDBPanel, HDBPanel
					.getComponentCount(), 1, // rows, cols
					0, 0, // initX, initY
					0, 0, // xPad, yPad
					true);

			centerPanel.add(HDBPanel);
		} else {
			TDBPanel = new JPanel();
			TDBPanel.setLayout(new SpringLayout());

			TDBPeriodPanel = new JPanel();
			TDBPeriodPanel.setLayout(new SpringLayout());

			TDBModePanel = new JPanel();
			TDBModePanel.setLayout(new SpringLayout());

			TDBPeriodPanel.add(TDBperiodLabel);
			TDBPeriodPanel.add(TDBperiodField);
			TDBPeriodPanel.add(Box.createHorizontalGlue());

			// --------------------

			JPanel panelM = new JPanel();
			JPanel panelP = new JPanel();
			JPanel panelA = new JPanel();
			JPanel panelR = new JPanel();
			JPanel panelT = new JPanel();
			JPanel panelD = new JPanel();
			TDBModePanel.add(panelM);
			TDBModePanel.add(panelP);
			TDBModePanel.add(panelA);
			TDBModePanel.add(panelR);
			TDBModePanel.add(panelT);
			TDBModePanel.add(panelD);
			SpringUtilities.makeCompactGrid(TDBModePanel, 6, 1, 3, 3, 3, 5,
					true);

			panelM.setLayout(new SpringLayout());
			panelM.add(TDBmodesLabel);
			SpringUtilities.makeCompactGrid(panelM, 1, 1, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			// Periodic Mode
			panelP.setBorder(BorderFactory.createEtchedBorder());
			panelP.setLayout(new SpringLayout());
			panelP.add(TDBPeriodicalLabel);
			panelP.add(TDBPeriodicalCheck);
			SpringUtilities.makeCompactGrid(panelP, 1, 2, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			// Absolute Mode

			panelA.setBorder(BorderFactory.createEtchedBorder());
			panelA.add(TDBAbsoluteLabel);
			panelA.add(TDBAbsoluteCheck);
			panelA.add(Box.createHorizontalGlue());
			panelA.add(Box.createHorizontalGlue());
			panelA.add(Box.createHorizontalGlue());

			panelA.add(TDBabsolutePeriodLabel);
			panelA.add(TDBabsolutePeriodField);
			panelA.add(Box.createHorizontalGlue());

			panelA.add(TDBAbsoluteDLCheck);
			panelA.add(Box.createHorizontalGlue());

			panelA.add(TDBabsoluteLowerLabel);
			panelA.add(TDBabsoluteLowerField);
			panelA.add(Box.createHorizontalGlue());
			panelA.add(TDBabsoluteUpperLabel);
			panelA.add(TDBabsoluteUpperField);
			panelA.setLayout(new SpringLayout());
			SpringUtilities.makeCompactGrid(panelA, 3, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			setAbsoluteModeParameters(TDBAbsoluteCheck.isSelected(), isHistoric);
			TDBAbsoluteCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					setAbsoluteModeParameters(TDBAbsoluteCheck.isSelected(),
							isHistoric);
				}

			});

			// Relative Mode
			panelR.setLayout(new SpringLayout());

			panelR.setBorder(BorderFactory.createEtchedBorder());

			panelR.add(TDBRelativeLabel);
			panelR.add(TDBRelativeCheck);
			panelR.add(Box.createHorizontalGlue());
			panelR.add(Box.createHorizontalGlue());
			panelR.add(Box.createHorizontalGlue());

			panelR.add(TDBrelativePeriodLabel);
			panelR.add(TDBrelativePeriodField);
			panelR.add(Box.createHorizontalGlue());
			panelR.add(TDBRelativeDLCheck);
			panelR.add(Box.createHorizontalGlue());

			panelR.add(TDBrelativeLowerLabel);
			panelR.add(TDBrelativeLowerField);
			panelR.add(Box.createHorizontalGlue());
			panelR.add(TDBrelativeUpperLabel);
			panelR.add(TDBrelativeUpperField);
			SpringUtilities.makeCompactGrid(panelR, 3, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			setRelativeModeParameters(TDBThresholdCheck.isSelected(),
					isHistoric);
			TDBRelativeCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					setRelativeModeParameters(TDBRelativeCheck.isSelected(),
							isHistoric);
					// TODO Auto-generated method stub
				}

			});

			// Threshhold Mode
			panelT.setLayout(new SpringLayout());
			panelT.setBorder(BorderFactory.createEtchedBorder());

			panelT.add(TDBThresholdLabel);
			panelT.add(TDBThresholdCheck);
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());

			panelT.add(TDBthresholdPeriodLabel);
			panelT.add(TDBthresholdPeriodField);
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());
			panelT.add(Box.createHorizontalGlue());

			panelT.add(TDBthresholdLowerLabel);
			panelT.add(TDBthresholdLowerField);
			panelT.add(Box.createHorizontalGlue());
			panelT.add(TDBthresholdUpperLabel);
			panelT.add(TDBthresholdUpperField);
			SpringUtilities.makeCompactGrid(panelT, 3, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);

			setThresholdModeParameters(TDBThresholdCheck.isSelected(),
					isHistoric);
			TDBThresholdCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					setThresholdModeParameters(TDBThresholdCheck.isSelected(),
							isHistoric);
				}

			});

			// Difference Mode
			panelD.setLayout(new SpringLayout());
			panelD.setBorder(BorderFactory.createEtchedBorder());

			panelD.add(TDBDifferenceLabel);
			panelD.add(TDBDifferenceCheck);
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());

			panelD.add(TDBdifferencePeriodLabel);
			panelD.add(TDBdifferencePeriodField);
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());
			panelD.add(Box.createHorizontalGlue());

			// panelD.add( TDBdifferencePrecisionLabel );
			// panelD.add( TDBdifferencePrecisionField );
			// panelD.add( Box.createHorizontalGlue() );
			// panelD.add( Box.createHorizontalGlue() );
			// panelD.add( Box.createHorizontalGlue() );
			SpringUtilities.makeCompactGrid(panelD, 2, 5, // rows, cols
					3, 3,// initX, initY
					3, 5,// xPad, yPad
					true);
			setDifferenceModeParameters(TDBDifferenceCheck.isSelected(),
					isHistoric);
			TDBDifferenceCheck.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					setDifferenceModeParameters(
							TDBDifferenceCheck.isSelected(), isHistoric);
				}

			});

			// ----------------------------
			// -------------------------

			TDBPanel.add(TDBPeriodPanel);
			TDBPanel.add(Box.createVerticalGlue());
			TDBPanel.add(TDBModePanel);

			// Lay out the panels.
			SpringUtilities.makeCompactGrid(TDBPeriodPanel, 1, TDBPeriodPanel
					.getComponentCount(), // rows, cols
					0, 0, // initX, initY
					0, 5, // xPad, yPad
					true);
			SpringUtilities.makeCompactGrid(TDBPanel, TDBPanel
					.getComponentCount(), 1, // rows, cols
					0, 0, // initX, initY
					0, 0, // xPad, yPad
					true);

			centerPanel.add(TDBPanel);
		}

		JPanel warningPanel = this.getWarningPanel();
		JPanel archiverChoicePanel = this.getArchiverChoicePanel();

		centerPanel.add(Box.createVerticalGlue());
		centerPanel.add(warningPanel);

		if (Mambo.canChooseArchivers()) {
			centerPanel.add(Box.createVerticalGlue());
			centerPanel.add(archiverChoicePanel);
		}
	}

	private void setDifferenceModeParameters(boolean b, boolean historic) {
		// TODO Auto-generated method stub
		if (historic) {
			HDBdifferencePeriodLabel.setEnabled(b);
			HDBdifferencePeriodField.setEnabled(b);
			// HDBdifferencePrecisionLabel.setEnabled(b);
			// HDBdifferencePrecisionField.setEnabled(b);
		} else {
			TDBdifferencePeriodLabel.setEnabled(b);
			TDBdifferencePeriodField.setEnabled(b);
			// TDBdifferencePrecisionLabel.setEnabled(b);
			// TDBdifferencePrecisionField.setEnabled(b);

		}
	}

	private void setThresholdModeParameters(boolean b, boolean historic) {
		// TODO Auto-generated method stub
		if (historic) {
			HDBthresholdPeriodLabel.setEnabled(b);
			HDBthresholdPeriodField.setEnabled(b);
			HDBthresholdUpperField.setEnabled(b);
			HDBthresholdUpperLabel.setEnabled(b);
			HDBthresholdLowerLabel.setEnabled(b);
			HDBthresholdLowerField.setEnabled(b);
		} else {
			TDBthresholdPeriodLabel.setEnabled(b);
			TDBthresholdPeriodField.setEnabled(b);
			TDBthresholdUpperField.setEnabled(b);
			TDBthresholdUpperLabel.setEnabled(b);
			TDBthresholdLowerLabel.setEnabled(b);
			TDBthresholdLowerField.setEnabled(b);

		}
	}

	private void setRelativeModeParameters(boolean b, boolean historic) {
		// TODO Auto-generated method stub
		if (historic) {
			HDBrelativePeriodLabel.setEnabled(b);
			HDBrelativePeriodField.setEnabled(b);
			HDBRelativeDLCheck.setEnabled(b);
			HDBrelativeLowerLabel.setEnabled(b);
			HDBrelativeLowerLabel.setEnabled(b);
			HDBrelativeUpperLabel.setEnabled(b);
			HDBrelativeUpperField.setEnabled(b);
		} else {
			TDBrelativePeriodLabel.setEnabled(b);
			TDBrelativePeriodField.setEnabled(b);
			TDBRelativeDLCheck.setEnabled(b);
			TDBrelativeLowerLabel.setEnabled(b);
			TDBrelativeLowerLabel.setEnabled(b);
			TDBrelativeUpperLabel.setEnabled(b);
			TDBrelativeUpperField.setEnabled(b);

		}
	}

	private void setAbsoluteModeParameters(boolean b, boolean historic) {
		// TODO Auto-generated method stub
		if (historic) {
			HDBabsolutePeriodLabel.setEnabled(b);
			HDBabsolutePeriodField.setEnabled(b);
			HDBAbsoluteDLCheck.setEnabled(b);
			HDBabsoluteLowerLabel.setEnabled(b);
			HDBabsoluteLowerField.setEnabled(b);
			HDBabsoluteUpperLabel.setEnabled(b);
			HDBabsoluteUpperField.setEnabled(b);
		} else {
			TDBabsolutePeriodLabel.setEnabled(b);
			TDBabsolutePeriodField.setEnabled(b);
			TDBAbsoluteDLCheck.setEnabled(b);
			TDBabsoluteLowerLabel.setEnabled(b);
			TDBabsoluteLowerField.setEnabled(b);
			TDBabsoluteUpperLabel.setEnabled(b);
			TDBabsoluteUpperField.setEnabled(b);

		}
	}

	/**
	 * @return
	 */
	private JPanel getWarningPanel() {
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
		SpringUtilities.makeCompactGrid(warningPanel, 1, warningPanel
				.getComponentCount(), // rows, cols
				0, 0, // initX, initY
				0, 0, // xPad, yPad
				true);
		return warningPanel;
	}

	private JPanel getArchiverChoicePanel() {
		JPanel archiverChoicePanel = new JPanel();

		String dedicatedArchiverLabelMessage = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_DEDICATED_ARCHIVER");
		JLabel dedicatedArchiverLabel = new JLabel(
				dedicatedArchiverLabelMessage);
		String currentArchiverLabelMessage = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_CURRENT_ARCHIVER");
		JLabel currentArchiverLabel = new JLabel(currentArchiverLabelMessage);
		String archiverDefaultLabelMessage = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_ARCHIVER_DEFAULT");
		JLabel archiverDefaultLabel = new JLabel(archiverDefaultLabelMessage);
		String archiverRunningLabelMessage = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_ARCHIVER_RUNNING");
		JLabel archiverRunningLabel = new JLabel(archiverRunningLabelMessage);
		String archiverNameLabelMessage = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_ARCHIVER_NAME");
		JLabel archiverNameLabel = new JLabel(archiverNameLabelMessage);

		archiverChoicePanel.add(dedicatedArchiverLabel);
		archiverChoicePanel.add(dedicatedArchiverTextField);
		archiverChoicePanel.add(currentArchiverLabel);
		archiverChoicePanel.add(currentArchiverTextField);
		archiverChoicePanel.add(archiverDefaultLabel);
		archiverChoicePanel.add(archiverChoiceDefaultCheckBox);
		archiverChoicePanel.add(archiverRunningLabel);
		archiverChoicePanel.add(archiverChoiceRunningComboBox);
		archiverChoicePanel.add(archiverNameLabel);
		archiverChoicePanel.add(archiverChoiceNameTextField);

		String archiverTitleLabelMessage = Messages
				.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_ARCHIVER_TITLE");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED),
				archiverTitleLabelMessage, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.TOP, GUIUtilities.getTitleFont());

		archiverChoicePanel.setBorder(tb);

		archiverChoicePanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(archiverChoicePanel, 5, 2, // rows, cols
				0, 0, // initX, initY
				0, 5, // xPad, yPad
				true);

		return archiverChoicePanel;
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
			// System.out.println ( "getTDBperiod/s/"+s );
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
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * @param period
	 *            25 juil. 2005
	 */
	public void setTDBPeriod(int period) {
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
		TDBThresholdCheck.setSelected(false);
		TDBDifferenceCheck.setSelected(false);

		HDBPeriodicalCheck.setSelected(false);
		HDBAbsoluteCheck.setSelected(false);
		HDBThresholdCheck.setSelected(false);
		HDBDifferenceCheck.setSelected(false);
	}

	public boolean verifyValues() {
		boolean firstTests = verifyThatSelectedModesAreCoherent()
				&& verifyEachMode();
		if (firstTests) {
			try {
				this.getLastMode().checkMode(this.isHistoric);
				return true;
			} catch (ArchivingException e) {
				String title = Messages
						.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_MODES_TITLE");
				String msgGeneral = Messages
						.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_MODES_MESSAGE");
				String msgSpecific = e.getLastExceptionMessage();
				String msg = msgGeneral + GUIUtilities.CRLF + msgSpecific;

				JOptionPane.showMessageDialog(null, msg, title,
						JOptionPane.ERROR_MESSAGE);

				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @return
	 */
	private boolean verifyEachMode() {
		Mode mode = new Mode();

		if (this.isHistoric) {
			if (HDBPeriodicalCheck.isSelected()) {
				String s = HDBperiodField.getText();
				try {
					Integer.parseInt(s);
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_PERIODIC_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_PERIODIC_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModePeriode modeP = new ModePeriode();
				modeP.setPeriod(this.getHDBperiod());
				mode.setModeP(modeP);
			}

			if (HDBDifferenceCheck.isSelected()) {
				String s = HDBdifferencePeriodField.getText();
				try {
					Integer.parseInt(s);
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_DIFFERENCE_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_DIFFERENCE_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeDifference modeD = new ModeDifference();
				modeD.setPeriod(this.getHDBDifferencePeriod());
				// modeD.setVal_precision(Integer.parseInt(this.getHDBDifferencePrecision()));
				mode.setModeD(modeD);
			}

			if (HDBAbsoluteCheck.isSelected()) {
				String s = HDBabsolutePeriodField.getText();
				String s1 = HDBabsoluteLowerField.getText();
				String s2 = HDBabsoluteUpperField.getText();
				double d1;
				double d2;

				try {
					Integer.parseInt(s);
					d1 = Math.abs(Double.parseDouble(s1));
					// d1 = -1.0 * Math.abs ( d1 );
					d2 = Math.abs(Double.parseDouble(s2));
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_ABSOLUTE_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_ABSOLUTE_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);
					return false;
				}

				ModeAbsolu modeA = new ModeAbsolu();
				modeA.setPeriod(this.getHDBAbsolutePeriod());
				modeA.setValInf(d1);
				modeA.setValSup(d2);
				modeA.setSlow_drift(this.isHDBAbsoluteDLCheck());
				mode.setModeA(modeA);
			}

			if (HDBRelativeCheck.isSelected()) {
				String s = HDBrelativePeriodField.getText();
				String s1 = HDBrelativeLowerField.getText();
				String s2 = HDBrelativeUpperField.getText();
				double d1;
				double d2;

				try {
					Integer.parseInt(s);
					d1 = Double.parseDouble(s1);
					d1 = Math.abs(d1);
					d2 = Double.parseDouble(s2);
					d2 = Math.abs(d2);
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_RELATIVE_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_RELATIVE_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeRelatif modeR = new ModeRelatif();
				modeR.setPeriod(this.getHDBRelativePeriod());
				modeR.setPercentInf(d1);
				modeR.setPercentSup(d2);
				modeR.setSlow_drift(this.isHDBRelativeDLCheck());
				mode.setModeR(modeR);
			}

			if (HDBThresholdCheck.isSelected()) {
				String s = HDBthresholdPeriodField.getText();
				String s1 = HDBthresholdLowerField.getText();
				String s2 = HDBthresholdUpperField.getText();
				try {
					Integer.parseInt(s);
					double d1 = Double.parseDouble(s1);
					double d2 = Double.parseDouble(s2);
					if (d1 >= d2) {
						throw new IllegalStateException();
					}
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_THRESHOLD_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_THRESHOLD_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeSeuil modeS = new ModeSeuil();
				modeS.setPeriod(this.getHDBThresholdPeriod());
				modeS.setThresholdInf(Double.parseDouble(this
						.getHDBThresholdLower()));
				modeS.setThresholdSup(Double.parseDouble(this
						.getHDBThresholdUpper()));
				mode.setModeT(modeS);
			}
		} else {
			if (TDBPeriodicalCheck.isSelected()) {
				String s = TDBperiodField.getText();
				try {
					Integer.parseInt(s);
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_PERIODIC_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_PERIODIC_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModePeriode modeP = new ModePeriode();
				modeP.setPeriod(this.getTDBperiod());
				mode.setModeP(modeP);

				long exportPeriod = this.getExportPeriod();
				long keepingPeriod = this.getKeepingPeriod();
				TdbSpec tdbSpec = new TdbSpec(exportPeriod, keepingPeriod);
				mode.setTdbSpec(tdbSpec);
			}

			if (TDBDifferenceCheck.isSelected()) {
				String s = TDBdifferencePeriodField.getText();
				try {
					Integer.parseInt(s);
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_DIFFERENCE_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_DIFFERENCE_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeDifference modeD = new ModeDifference();
				modeD.setPeriod(this.getTDBDifferencePeriod());
				// modeD.setVal_precision(Integer.parseInt(this.getTDBDifferencePrecision()));
				mode.setModeD(modeD);
			}

			if (TDBAbsoluteCheck.isSelected()) {
				String s = TDBabsolutePeriodField.getText();
				String s1 = TDBabsoluteLowerField.getText();
				String s2 = TDBabsoluteUpperField.getText();
				try {
					Integer.parseInt(s);
					Double.parseDouble(s1);
					Double.parseDouble(s2);
				} catch (Exception e) {
					e.printStackTrace();
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_ABSOLUTE_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_ABSOLUTE_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeAbsolu modeA = new ModeAbsolu();
				modeA.setPeriod(this.getTDBAbsolutePeriod());
				modeA.setValInf(Math.abs(Double.parseDouble(this
						.getTDBAbsoluteLower())));
				modeA.setValSup(Math.abs(Double.parseDouble(this
						.getTDBAbsoluteUpper())));
				modeA.setSlow_drift(this.isTDBAbsoluteDLCheck());
				mode.setModeA(modeA);
			}

			if (TDBRelativeCheck.isSelected()) {
				String s = TDBrelativePeriodField.getText();
				String s1 = TDBrelativeLowerField.getText();
				String s2 = TDBrelativeUpperField.getText();
				try {
					Integer.parseInt(s);
					Double.parseDouble(s1);
					Double.parseDouble(s2);
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_RELATIVE_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_RELATIVE_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeRelatif modeR = new ModeRelatif();
				modeR.setPeriod(this.getTDBRelativePeriod());
				modeR.setPercentInf(Double.parseDouble(this
						.getTDBRelativeLower()));
				modeR.setPercentSup(Double.parseDouble(this
						.getTDBRelativeUpper()));
				modeR.setSlow_drift(this.isTDBRelativeDLCheck());
				mode.setModeR(modeR);
			}

			if (TDBThresholdCheck.isSelected()) {
				String s = TDBthresholdPeriodField.getText();
				String s1 = TDBthresholdLowerField.getText();
				String s2 = TDBthresholdUpperField.getText();
				try {
					Integer.parseInt(s);
					double d1 = Double.parseDouble(s1);
					double d2 = Double.parseDouble(s2);
					if (d1 >= d2) {
						throw new IllegalStateException();
					}
				} catch (Exception e) {
					String title = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_THRESHOLD_MODE_TITLE");
					String msg = Messages
							.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_TDB_THRESHOLD_MODE_MESSAGE");
					JOptionPane.showMessageDialog(null, msg, title,
							JOptionPane.ERROR_MESSAGE);

					return false;
				}

				ModeSeuil modeS = new ModeSeuil();
				modeS.setPeriod(this.getTDBThresholdPeriod());
				modeS.setThresholdInf(Double.parseDouble(this
						.getTDBThresholdLower()));
				modeS.setThresholdSup(Double.parseDouble(this
						.getTDBThresholdUpper()));
				mode.setModeT(modeS);
			}
		}

		this.setLastMode(mode);
		return true;
	}

	/**
	 * @param mode
	 */
	private void setLastMode(Mode mode) {
		this.lastMode = mode;
	}

	/**
	 * @return
	 */
	public boolean verifyThatSelectedModesAreCoherent() {
		if (this.isHistoric) {
			boolean hasHDBNonPeriodicMode = HDBAbsoluteCheck.isSelected()
					|| HDBThresholdCheck.isSelected()
					|| HDBDifferenceCheck.isSelected();
			boolean hasHDBPeriodicMode = HDBPeriodicalCheck.isSelected();

			if (hasHDBNonPeriodicMode && !hasHDBPeriodicMode) {
				String title = Messages
						.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INCOHERENT_HDB_MODES_TITLE");
				String msg = Messages
						.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INCOHERENT_HDB_MODES_MESSAGE");
				JOptionPane.showMessageDialog(null, msg, title,
						JOptionPane.ERROR_MESSAGE);

				return false;
			}
		} else {
			boolean hasTDBNonPeriodicMode = TDBAbsoluteCheck.isSelected()
					|| TDBThresholdCheck.isSelected()
					|| TDBDifferenceCheck.isSelected();
			boolean hasTDBPeriodicMode = TDBPeriodicalCheck.isSelected();

			if (hasTDBNonPeriodicMode && !hasTDBPeriodicMode) {
				String title = Messages
						.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INCOHERENT_TDB_MODES_TITLE");
				String msg = Messages
						.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INCOHERENT_TDB_MODES_MESSAGE");
				JOptionPane.showMessageDialog(null, msg, title,
						JOptionPane.ERROR_MESSAGE);

				return false;
			}
		}

		return true;
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

	/************** getters ******************/
	public boolean isHDBAbsoluteDLCheck() {
		return HDBAbsoluteDLCheck.isSelected();
	}

	public boolean isHDBRelativeDLCheck() {
		return HDBRelativeDLCheck.isSelected();
	}

	// public String getHDBDifferencePrecision ()
	// {
	// return HDBdifferencePrecisionField.getText();
	// }

	/******************* setters ******************/

	public void setHDBAbsoluteDLCheck(boolean check) {
		HDBAbsoluteDLCheck.setSelected(check);
	}

	public void setHDBRelativeDLCheck(boolean check) {
		HDBRelativeDLCheck.setSelected(check);
	}

	// public void setHDBDifferencePrecision (String val)
	// {
	// HDBdifferencePrecisionField.setText(val);
	// }

	/***************************************/

	/************** TDB getters ******************/
	public boolean isTDBAbsoluteDLCheck() {
		return TDBAbsoluteDLCheck.isSelected();
	}

	public boolean isTDBRelativeDLCheck() {
		return TDBRelativeDLCheck.isSelected();
	}

	// public String getTDBDifferencePrecision ()
	// {
	// return TDBdifferencePrecisionField.getText();
	// }

	/******************* TDB setters ******************/

	public void setTDBAbsoluteDLCheck(boolean check) {
		TDBAbsoluteDLCheck.setSelected(check);
	}

	public void setTDBRelativeDLCheck(boolean check) {
		TDBRelativeDLCheck.setSelected(check);
	}

	// public void setTDBDifferencePrecision (String val)
	// {
	// TDBdifferencePrecisionField.setText(val);
	// }
	//
	/***************************************/

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
		try {
			double d = Double.parseDouble(val);
			d = Math.abs(d);
			val = String.valueOf(d);
		} catch (Exception e) {
			// do nothing
		}

		HDBabsoluteLowerField.setText(val);
	}

	public void setHDBRelativeLower(String val) {
		try {
			double d = Double.parseDouble(val);
			d = Math.abs(d);
			val = String.valueOf(d);
		} catch (Exception e) {
			// do nothing
		}

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

	/*
     * 
     */
	// public int getHDBDifferencePrecisionValue ()
	// {
	// try
	// {
	// String s = HDBdifferencePrecisionField.getText();
	// int i;
	// try {
	// i = Integer.parseInt( s );
	// if (i<0) {
	// i = 0;
	// }
	// else if (i > Integer.MAX_VALUE/1000) {
	// i = Integer.MAX_VALUE/1000;
	// }
	// }
	// catch (NumberFormatException n) {
	// double d;
	// try {
	// d = Double.parseDouble(s);
	// if (d > 0) {
	// i = Integer.MAX_VALUE/1000;
	// }
	// else {
	// i = 0;
	// }
	// }
	// catch (Exception e) {
	// throw n;
	// }
	// }
	// i*=1000;
	// return i;
	// }
	// catch ( Exception e )
	// {
	// return 0;
	// }
	// }
	/*
     * 
     */
	// public int getTDBDifferencePrecisionValue ()
	// {
	// try
	// {
	// String s = TDBdifferencePrecisionField.getText();
	// int i;
	// try {
	// i = Integer.parseInt( s );
	// if (i<0) {
	// i = 0;
	// }
	// else if (i > Integer.MAX_VALUE/1000) {
	// i = Integer.MAX_VALUE/1000;
	// }
	// }
	// catch (NumberFormatException n) {
	// double d;
	// try {
	// d = Double.parseDouble(s);
	// if (d > 0) {
	// i = Integer.MAX_VALUE/1000;
	// }
	// else {
	// i = 0;
	// }
	// }
	// catch (Exception e) {
	// throw n;
	// }
	// }
	// i*=1000;
	// return i;
	// }
	// catch ( Exception e )
	// {
	// return 0;
	// }
	// }
	/*
 * 
 */

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
		// System.out.println ( "setHDBAbsolutePeriod/period/"+period+"/" );
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
	 * @return Returns the lastMode.
	 */
	public Mode getLastMode() {
		return lastMode;
	}

	public void setArchiverDefault(boolean isDefault) {
		// System.out.println ( "CLA/setArchiverDefault/isDefault/"+isDefault );

		if (isDefault) {
			this.archiverChoiceNameTextField.setText(ArchiversComboBox.DEFAULT);
		} else {
			this.archiverChoiceNameTextField.setText("");
		}

		this.archiverChoiceRunningComboBox.selectDefault();

		// this.archiverChoiceNameTextField.setEnabled ( ! isDefault );
		this.archiverChoiceRunningComboBox.setEnabled(!isDefault);
	}

	private class ArchiverChoiceDefaultCheckBox extends JCheckBox {
		public ArchiverChoiceDefaultCheckBox() {
			super();
			this
					.addActionListener(new ArchiverChoiceDefaultCheckBoxActionListener());
		}
	}

	public void setArchiverChoiceText(String newArchiver) {
		this.archiverChoiceNameTextField.setText(newArchiver);
	}

	public String getDedicatedArchiver() {
		String rawValue = this.archiverChoiceNameTextField.getText();
		if (rawValue == null || rawValue.equals("")
				|| rawValue.equals(ArchiversComboBox.DEFAULT)) {
			return "";
		}

		return this.archiverChoiceNameTextField.getText();
	}

	public void pushDefaultArchiver(String dedicatedArchiver) {
		boolean archiverDefaultIsEmpty = dedicatedArchiver == null
				|| dedicatedArchiver.equals("")
				|| dedicatedArchiver.equals(ArchiversComboBox.DEFAULT);
		// System.out.println (
		// "/pushDefaultArchiver/pushDefaultArchiver/dedicatedArchiver|"+dedicatedArchiver+"|archiverDefaultIsEmpty|"+archiverDefaultIsEmpty
		// );

		this.archiverChoiceDefaultCheckBox.setSelected(archiverDefaultIsEmpty);
		this.archiverChoiceRunningComboBox.selectArchiver(dedicatedArchiver);
		this.setArchiverDefault(archiverDefaultIsEmpty);
		this.setArchiverChoiceText(dedicatedArchiver);
	}

	/**
	 * @return Returns the isHistoric.
	 */
	public boolean isHistoric() {
		return this.isHistoric;
	}

	public void pushCurrentArchiver(Archiver currentArchiver,
			String attributeCompleteName) {
		String name = currentArchiver == null ? "" : currentArchiver.getName();
		// System.out.println (
		// "AttributesPropertiesPanel/pushCurrentArchiver/currentArchiver|"+
		// name+"|");
		this.currentArchiverTextField.setText(name);

		Color color = Color.BLACK;
		if (currentArchiver != null) {
			color = currentArchiver.getAssociationColor(attributeCompleteName);
		}
		this.currentArchiverTextField.setForeground(color);

		this.archiverChoiceRunningComboBox
				.setCurrentlySelectedAttribute(attributeCompleteName);
	}

	public void pushDedicatedArchiver(String attributeCompleteName) {
		// System.out.println (
		// "CLA/pushDedicatedArchiver/attributeCompleteName|"+attributeCompleteName
		// );
		String text = "";
		Hashtable attributesToDedicatedArchiver = Archiver
				.getAttributesToDedicatedArchiver(this.isHistoric);
		if (attributesToDedicatedArchiver != null
				&& attributeCompleteName != null) {
			// System.out.println ( "CLA/pushDedicatedArchiver/1" );
			Archiver dedicatedArch = (Archiver) attributesToDedicatedArchiver
					.get(attributeCompleteName);
			if (dedicatedArch != null) {
				text = dedicatedArch.getName();
				String dedicatedStatusDescription = dedicatedArch.isExported() ? " (EXPORTED)"
						: " (NOT EXPORTED)";
				text += dedicatedStatusDescription;
				// System.out.println ( "CLA/pushDedicatedArchiver/2/text|"+text
				// );
			}
			/*
			 * else { System.out.println ( "CLA/pushDedicatedArchiver/3" ); }
			 */
		}

		this.dedicatedArchiverTextField.setText(text);
	}

	public void setCurrentlySelectedAttribute(String attributeCompleteName) {
		this.archiverChoiceRunningComboBox
				.setCurrentlySelectedAttribute(attributeCompleteName);
	}

	private class ArchiverChoiceNameTextField extends JTextField {
		public ArchiverChoiceNameTextField(String initialValue) {
			super(initialValue);

			this.setMaximumSize(classicMaxDimension);
		}

		public void setText(String archiver) {
			super.setText(archiver);
		}

		public void setColorForAttribute(String completeName) {
			String archiver = this.getText();

			if (archiver == null || archiver.equals("")
					|| archiver.equals(ArchiversComboBox.DEFAULT)) {
				// do nothing
				return;
			} else {
				try {
					Archiver arch = new Archiver(archiver,
							AttributesPropertiesPanel.this.isHistoric());
					Color color = arch.getAssociationColor(completeName);
					super.setForeground(color);
				} catch (Exception e) {
					// do nothing
					e.printStackTrace();
				}
			}
		}
	}

	public void pushChoiceArchiverColor(String completeName) {
		if (this.archiverChoiceNameTextField == null) {
			return;
		}

		this.archiverChoiceNameTextField.setColorForAttribute(completeName);
	}

}