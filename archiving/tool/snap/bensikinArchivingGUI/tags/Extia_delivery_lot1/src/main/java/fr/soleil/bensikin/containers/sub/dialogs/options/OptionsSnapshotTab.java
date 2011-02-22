// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsSnapshotTab.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OptionsMiscTab.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OptionsSnapshotTab.java,v $
// Revision 1.3 2006/06/28 12:49:44 ounsy
// minor changes
//
// Revision 1.2 2005/12/14 16:30:08 ounsy
// added the CSV separator option
//
// Revision 1.1 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:37 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.sub.dialogs.options;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.components.snapshot.SnapshotCompareOptionTable;
import fr.soleil.bensikin.models.StringArrayTableModel;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;

/**
 * The snapshots tab of OptionsDialog, used to set the snapshot specific options
 * of the application.
 * 
 * @author CLAISSE
 */
public class OptionsSnapshotTab extends JPanel {

	private static final long serialVersionUID = -6092900478346970343L;

	private static OptionsSnapshotTab instance = null;

	// ---
	private Box autoCommentBox;

	private JRadioButton autoSnapshotCommentYes;
	private JRadioButton autoSnapshotCommentNo;
	private JTextField defaultSnapshotComment;
	private ButtonGroup buttonGroup;

	// ------ Snapshot Comparison
	private JPanel showPanel;

	private JLabel showReadLabel;
	private JRadioButton showReadYes;
	private JRadioButton showReadNo;
	private ButtonGroup showReadGroup;

	private JLabel showWriteLabel;
	private JRadioButton showWriteYes;
	private JRadioButton showWriteNo;
	private ButtonGroup showWriteGroup;

	private JLabel showDeltaLabel;
	private JRadioButton showDeltaYes;
	private JRadioButton showDeltaNo;
	private ButtonGroup showDeltaGroup;

	private JLabel showDiffLabel;
	private JRadioButton showDiffYes;
	private JRadioButton showDiffNo;
	private ButtonGroup showDiffGroup;

	private JLabel comparisonColumnSortLabel;
	private SnapshotCompareOptionTable comparisonColumnSorter;
	private JScrollPane comparisonColumnSorterScrollPane;

	// ------ CSV Export
	private JPanel exportPanel;
	private JLabel separatorLabel;
	private JComboBox separatorComboBox;

	private JLabel csvIDLabel;
	private JRadioButton csvIDYes;
	private JRadioButton csvIDNo;
	private ButtonGroup csvIDGroup;

	private JLabel csvTimeLabel;
	private JRadioButton csvTimeYes;
	private JRadioButton csvTimeNo;
	private ButtonGroup csvTimeGroup;

	private JLabel csvContextIDLabel;
	private JRadioButton csvContextIDYes;
	private JRadioButton csvContextIDNo;
	private ButtonGroup csvContextIDGroup;

	private JLabel csvReadLabel;
	private JRadioButton csvReadYes;
	private JRadioButton csvReadNo;
	private ButtonGroup csvReadGroup;

	private JLabel csvWriteLabel;
	private JRadioButton csvWriteYes;
	private JRadioButton csvWriteNo;
	private ButtonGroup csvWriteGroup;

	private JLabel csvDeltaLabel;
	private JRadioButton csvDeltaYes;
	private JRadioButton csvDeltaNo;
	private ButtonGroup csvDeltaGroup;

	// ------ Time Filter
	private JPanel timeFilterPanel;
	private JLabel timeFilterLabel;
	private JComboBox timeFilterComboBox;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static OptionsSnapshotTab getInstance() {
		if (instance == null) {
			instance = new OptionsSnapshotTab();
		}

		return instance;
	}

	/**
	 * Builds the tab.
	 */
	private OptionsSnapshotTab() {
		this.initComponents();
		this.initLayout();
		this.addComponents();
	}

	/**
	 * Inits the tab's layout.
	 */
	private void initLayout() {
		this.setLayout(new GridBagLayout());
	}

	/**
	 * Adds the initialized components to the tab.
	 */
	private void addComponents() {

		JScrollPane showPanelScrollPane = new JScrollPane(showPanel);
		showPanelScrollPane.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED), Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_BORDER"),
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getOptionsTitleFont()));
		showPanelScrollPane.setPreferredSize(showPanelScrollPane
				.getPreferredSize());

		JScrollPane exportPanelScrollPane = new JScrollPane(exportPanel);
		exportPanelScrollPane.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED), Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_EXPORT"),
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getOptionsTitleFont()));
		exportPanelScrollPane.setPreferredSize(exportPanel.getPreferredSize());

		Insets gapInsets = new Insets(20, 0, 0, 0);

		GridBagConstraints autoCommentBoxConstraints = new GridBagConstraints();
		autoCommentBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
		autoCommentBoxConstraints.gridx = 0;
		autoCommentBoxConstraints.gridy = 0;
		autoCommentBoxConstraints.weightx = 1;
		autoCommentBoxConstraints.weighty = 0;
		this.add(autoCommentBox, autoCommentBoxConstraints);

		GridBagConstraints timeFilterPanelConstraints = new GridBagConstraints();
		timeFilterPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeFilterPanelConstraints.gridx = 0;
		timeFilterPanelConstraints.gridy = 1;
		timeFilterPanelConstraints.weightx = 1;
		timeFilterPanelConstraints.weighty = 0;
		timeFilterPanelConstraints.insets = gapInsets;
		this.add(timeFilterPanel, timeFilterPanelConstraints);

		GridBagConstraints showPanelConstraints = new GridBagConstraints();
		showPanelConstraints.fill = GridBagConstraints.BOTH;
		showPanelConstraints.gridx = 0;
		showPanelConstraints.gridy = 2;
		showPanelConstraints.weightx = 1;
		showPanelConstraints.weighty = 0.5;
		showPanelConstraints.insets = gapInsets;
		this.add(showPanelScrollPane, showPanelConstraints);

		GridBagConstraints exportPanelConstraints = new GridBagConstraints();
		exportPanelConstraints.fill = GridBagConstraints.BOTH;
		exportPanelConstraints.gridx = 0;
		exportPanelConstraints.gridy = 3;
		exportPanelConstraints.weightx = 1;
		exportPanelConstraints.weighty = 0.5;
		exportPanelConstraints.insets = gapInsets;
		this.add(exportPanelScrollPane, exportPanelConstraints);
	}

	/**
	 * Inits the tab's components.
	 */
	private void initComponents() {
		buttonGroup = new ButtonGroup();

		String msg = Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_HAS_AUTO_SNAPSHOT_COMMENT_YES");
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_HAS_AUTO_SNAPSHOT_COMMENT_YES");
		autoSnapshotCommentYes = new JRadioButton(msg, true);
		autoSnapshotCommentYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_AUTO_COMMENT_YES));
		msg = Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_HAS_AUTO_SNAPSHOT_COMMENT_NO");
		autoSnapshotCommentNo = new JRadioButton(msg, false);
		autoSnapshotCommentNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_AUTO_COMMENT_NO));
		buttonGroup.add(autoSnapshotCommentYes);
		buttonGroup.add(autoSnapshotCommentNo);

		msg = Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_DEFAULT_SNAPSHOT_COMMENT");
		JLabel defaultCommentLabel = new JLabel(msg);
		defaultSnapshotComment = new JTextField();
		defaultSnapshotComment.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				20));
		Box defaultCommentBox = new Box(BoxLayout.X_AXIS);
		defaultCommentBox.add(defaultCommentLabel);
		defaultCommentBox.add(Box.createHorizontalStrut(5));
		defaultCommentBox.add(defaultSnapshotComment);

		autoCommentBox = new Box(BoxLayout.Y_AXIS);
		autoCommentBox.add(autoSnapshotCommentYes);
		autoCommentBox.add(Box.createVerticalStrut(5));
		autoCommentBox.add(autoSnapshotCommentNo);
		autoCommentBox.add(Box.createVerticalStrut(10));
		autoCommentBox.add(defaultCommentBox);

		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_AUTO_COMMENT");
		TitledBorder tb = new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED), msg, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.TOP, GUIUtilities.getOptionsTitleFont());
		autoCommentBox.setBorder(tb);

		initTimeFilterPanel();

		initCSVPanel();

		initShowPanel();
	}

	private void initCSVPanel() {
		exportPanel = new JPanel(new GridBagLayout());

		String msg = Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_SEPARATOR");
		separatorLabel = new JLabel(msg);
		separatorComboBox = new JComboBox();
		separatorComboBox.addItem(";");
		separatorComboBox.addItem("TAB");
		separatorComboBox.addItem("|");
		separatorComboBox.setPreferredSize(new Dimension(100, 20));
		separatorComboBox.setMaximumSize(new Dimension(100, 20));

		Box separatorBox = new Box(BoxLayout.X_AXIS);
		separatorBox.add(separatorLabel);
		separatorBox.add(Box.createHorizontalStrut(5));
		separatorBox.add(separatorComboBox);
		separatorBox.add(Box.createHorizontalGlue());
		GridBagConstraints separatorBoxConstraints = new GridBagConstraints();
		separatorBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
		separatorBoxConstraints.gridx = 0;
		separatorBoxConstraints.gridy = 0;
		separatorBoxConstraints.weightx = 1;
		separatorBoxConstraints.weighty = 0;
		separatorBoxConstraints.gridwidth = GridBagConstraints.REMAINDER;
		exportPanel.add(separatorBox, separatorBoxConstraints);

		JSeparator separator = new JSeparator();
		GridBagConstraints separatorConstraints = new GridBagConstraints();
		separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
		separatorConstraints.gridx = 0;
		separatorConstraints.gridy = 1;
		separatorConstraints.weightx = 1;
		separatorConstraints.weighty = 0;
		separatorConstraints.insets = new Insets(5, 0, 5, 0);
		separatorConstraints.gridwidth = GridBagConstraints.REMAINDER;
		exportPanel.add(separator, separatorConstraints);

		Insets titleGap = new Insets(5, 0, 0, 5);
		Insets yesGap = new Insets(5, 0, 0, 0);
		Insets noGap = new Insets(5, 10, 0, 0);

		double weigth = 0.5;

		csvIDGroup = new ButtonGroup();
		csvIDLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_ID"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_YES");
		csvIDYes = new JRadioButton(msg, true);
		csvIDYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_ID_YES));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_NO");
		csvIDNo = new JRadioButton(msg, false);
		csvIDNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_READ_NO));
		csvIDGroup.add(csvIDYes);
		csvIDGroup.add(csvIDNo);
		GridBagConstraints idLabelConstraints = new GridBagConstraints();
		idLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		idLabelConstraints.gridx = 0;
		idLabelConstraints.gridy = 2;
		idLabelConstraints.weightx = 0;
		idLabelConstraints.weighty = 0;
		idLabelConstraints.insets = titleGap;
		idLabelConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvIDLabel, idLabelConstraints);
		GridBagConstraints idYesConstraints = new GridBagConstraints();
		idYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		idYesConstraints.gridx = 1;
		idYesConstraints.gridy = 2;
		idYesConstraints.weightx = weigth;
		idYesConstraints.weighty = 0;
		idYesConstraints.insets = yesGap;
		idYesConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvIDYes, idYesConstraints);
		GridBagConstraints idNoConstraints = new GridBagConstraints();
		idNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		idNoConstraints.gridx = 2;
		idNoConstraints.gridy = 2;
		idNoConstraints.weightx = weigth;
		idNoConstraints.weighty = 0;
		idNoConstraints.insets = noGap;
		idNoConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvIDNo, idNoConstraints);

		csvTimeGroup = new ButtonGroup();
		csvTimeLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_TIME"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_YES");
		csvTimeYes = new JRadioButton(msg, true);
		csvTimeYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_TIME_YES));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_NO");
		csvTimeNo = new JRadioButton(msg, false);
		csvTimeNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_READ_NO));
		csvTimeGroup.add(csvTimeYes);
		csvTimeGroup.add(csvTimeNo);
		GridBagConstraints timeLabelConstraints = new GridBagConstraints();
		timeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeLabelConstraints.gridx = 0;
		timeLabelConstraints.gridy = 3;
		timeLabelConstraints.weightx = 0;
		timeLabelConstraints.weighty = 0;
		timeLabelConstraints.insets = titleGap;
		timeLabelConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvTimeLabel, timeLabelConstraints);
		GridBagConstraints timeYesConstraints = new GridBagConstraints();
		timeYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeYesConstraints.gridx = 1;
		timeYesConstraints.gridy = 3;
		timeYesConstraints.weightx = weigth;
		timeYesConstraints.weighty = 0;
		timeYesConstraints.insets = yesGap;
		timeYesConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvTimeYes, timeYesConstraints);
		GridBagConstraints timeNoConstraints = new GridBagConstraints();
		timeNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeNoConstraints.gridx = 2;
		timeNoConstraints.gridy = 3;
		timeNoConstraints.weightx = weigth;
		timeNoConstraints.weighty = 0;
		timeNoConstraints.insets = noGap;
		timeNoConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvTimeNo, timeNoConstraints);

		csvContextIDGroup = new ButtonGroup();
		csvContextIDLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_CONTEXT_ID"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_YES");
		csvContextIDYes = new JRadioButton(msg, true);
		csvContextIDYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_CONTEXT_ID_YES));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_NO");
		csvContextIDNo = new JRadioButton(msg, false);
		csvContextIDNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_READ_NO));
		csvContextIDGroup.add(csvContextIDYes);
		csvContextIDGroup.add(csvContextIDNo);
		GridBagConstraints contextIDLabelConstraints = new GridBagConstraints();
		contextIDLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		contextIDLabelConstraints.gridx = 0;
		contextIDLabelConstraints.gridy = 4;
		contextIDLabelConstraints.weightx = 0;
		contextIDLabelConstraints.weighty = 0;
		contextIDLabelConstraints.insets = titleGap;
		contextIDLabelConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvContextIDLabel, contextIDLabelConstraints);
		GridBagConstraints contextIDYesConstraints = new GridBagConstraints();
		contextIDYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		contextIDYesConstraints.gridx = 1;
		contextIDYesConstraints.gridy = 4;
		contextIDYesConstraints.weightx = weigth;
		contextIDYesConstraints.weighty = 0;
		contextIDYesConstraints.insets = yesGap;
		contextIDYesConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvContextIDYes, contextIDYesConstraints);
		GridBagConstraints contextIDNoConstraints = new GridBagConstraints();
		contextIDNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		contextIDNoConstraints.gridx = 2;
		contextIDNoConstraints.gridy = 4;
		contextIDNoConstraints.weightx = weigth;
		contextIDNoConstraints.weighty = 0;
		contextIDNoConstraints.insets = noGap;
		contextIDNoConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvContextIDNo, contextIDNoConstraints);

		csvReadGroup = new ButtonGroup();
		csvReadLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_READ"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_YES");
		csvReadYes = new JRadioButton(msg, true);
		csvReadYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_READ_YES));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_NO");
		csvReadNo = new JRadioButton(msg, false);
		csvReadNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_READ_NO));
		csvReadGroup.add(csvReadYes);
		csvReadGroup.add(csvReadNo);
		GridBagConstraints readLabelConstraints = new GridBagConstraints();
		readLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		readLabelConstraints.gridx = 0;
		readLabelConstraints.gridy = 5;
		readLabelConstraints.weightx = 0;
		readLabelConstraints.weighty = 0;
		readLabelConstraints.insets = titleGap;
		readLabelConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvReadLabel, readLabelConstraints);
		GridBagConstraints readYesConstraints = new GridBagConstraints();
		readYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		readYesConstraints.gridx = 1;
		readYesConstraints.gridy = 5;
		readYesConstraints.weightx = weigth;
		readYesConstraints.weighty = 0;
		readYesConstraints.insets = yesGap;
		readYesConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvReadYes, readYesConstraints);
		GridBagConstraints readNoConstraints = new GridBagConstraints();
		readNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		readNoConstraints.gridx = 2;
		readNoConstraints.gridy = 5;
		readNoConstraints.weightx = weigth;
		readNoConstraints.weighty = 0;
		readNoConstraints.insets = noGap;
		readNoConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvReadNo, readNoConstraints);

		csvWriteGroup = new ButtonGroup();
		csvWriteLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_WRITE"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_YES");
		csvWriteYes = new JRadioButton(msg, true);
		csvWriteYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_WRITE_YES));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_NO");
		csvWriteNo = new JRadioButton(msg, false);
		csvWriteNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_WRITE_NO));
		csvWriteGroup.add(csvWriteYes);
		csvWriteGroup.add(csvWriteNo);
		GridBagConstraints writeLabelConstraints = new GridBagConstraints();
		writeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		writeLabelConstraints.gridx = 0;
		writeLabelConstraints.gridy = 6;
		writeLabelConstraints.weightx = 0;
		writeLabelConstraints.weighty = 0;
		writeLabelConstraints.insets = titleGap;
		writeLabelConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvWriteLabel, writeLabelConstraints);
		GridBagConstraints writeYesConstraints = new GridBagConstraints();
		writeYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		writeYesConstraints.gridx = 1;
		writeYesConstraints.gridy = 6;
		writeYesConstraints.weightx = weigth;
		writeYesConstraints.weighty = 0;
		writeYesConstraints.insets = yesGap;
		writeYesConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvWriteYes, writeYesConstraints);
		GridBagConstraints writeNoConstraints = new GridBagConstraints();
		writeNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		writeNoConstraints.gridx = 2;
		writeNoConstraints.gridy = 6;
		writeNoConstraints.weightx = weigth;
		writeNoConstraints.weighty = 0;
		writeNoConstraints.insets = noGap;
		writeNoConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvWriteNo, writeNoConstraints);

		csvDeltaGroup = new ButtonGroup();
		csvDeltaLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_DELTA"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_YES");
		csvDeltaYes = new JRadioButton(msg, true);
		csvDeltaYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_DELTA_YES));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_CSV_NO");
		csvDeltaNo = new JRadioButton(msg, false);
		csvDeltaNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_CSV_DELTA_NO));
		csvDeltaGroup.add(csvDeltaYes);
		csvDeltaGroup.add(csvDeltaNo);
		GridBagConstraints deltaLabelConstraints = new GridBagConstraints();
		deltaLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		deltaLabelConstraints.gridx = 0;
		deltaLabelConstraints.gridy = 7;
		deltaLabelConstraints.weightx = 0;
		deltaLabelConstraints.weighty = 0;
		deltaLabelConstraints.insets = titleGap;
		deltaLabelConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvDeltaLabel, deltaLabelConstraints);
		GridBagConstraints deltaYesConstraints = new GridBagConstraints();
		deltaYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		deltaYesConstraints.gridx = 1;
		deltaYesConstraints.gridy = 7;
		deltaYesConstraints.weightx = weigth;
		deltaYesConstraints.weighty = 0;
		deltaYesConstraints.insets = yesGap;
		deltaYesConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvDeltaYes, deltaYesConstraints);
		GridBagConstraints deltaNoConstraints = new GridBagConstraints();
		deltaNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		deltaNoConstraints.gridx = 2;
		deltaNoConstraints.gridy = 7;
		deltaNoConstraints.weightx = weigth;
		deltaNoConstraints.weighty = 0;
		deltaNoConstraints.insets = noGap;
		deltaNoConstraints.anchor = GridBagConstraints.WEST;
		exportPanel.add(csvDeltaNo, deltaNoConstraints);
	}

	/**
	 * Inits the "show" sub panel.
	 */
	private void initShowPanel() {
		showPanel = new JPanel(new GridBagLayout());

		Insets titleGap = new Insets(5, 0, 0, 5);
		Insets yesGap = new Insets(5, 0, 0, 0);
		Insets noGap = new Insets(5, 10, 0, 0);
		double weigth = 0.5;

		showReadGroup = new ButtonGroup();
		showReadLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_READ"));
		String msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES");
		showReadYes = new JRadioButton(msg, true);
		showReadYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_YES));
		showReadYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addRead();
			}

		});
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO");
		showReadNo = new JRadioButton(msg, false);
		showReadNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_NO));
		showReadNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeRead();
			}

		});
		showReadGroup.add(showReadYes);
		showReadGroup.add(showReadNo);
		GridBagConstraints readLabelConstraints = new GridBagConstraints();
		readLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		readLabelConstraints.gridx = 0;
		readLabelConstraints.gridy = 0;
		readLabelConstraints.weightx = 0;
		readLabelConstraints.weighty = 0;
		readLabelConstraints.insets = titleGap;
		readLabelConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showReadLabel, readLabelConstraints);
		GridBagConstraints readYesConstraints = new GridBagConstraints();
		readYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		readYesConstraints.gridx = 1;
		readYesConstraints.gridy = 0;
		readYesConstraints.weightx = weigth;
		readYesConstraints.weighty = 0;
		readYesConstraints.insets = yesGap;
		readYesConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showReadYes, readYesConstraints);
		GridBagConstraints readNoConstraints = new GridBagConstraints();
		readNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		readNoConstraints.gridx = 2;
		readNoConstraints.gridy = 0;
		readNoConstraints.weightx = weigth;
		readNoConstraints.weighty = 0;
		readNoConstraints.insets = noGap;
		readNoConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showReadNo, readNoConstraints);

		showWriteGroup = new ButtonGroup();
		showWriteLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_WRITE"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES");
		showWriteYes = new JRadioButton(msg, true);
		showWriteYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_YES));
		showWriteYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addWrite();
			}

		});
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO");
		showWriteNo = new JRadioButton(msg, false);
		showWriteNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_NO));
		showWriteNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeWrite();
			}

		});
		showWriteGroup.add(showWriteYes);
		showWriteGroup.add(showWriteNo);
		GridBagConstraints writeLabelConstraints = new GridBagConstraints();
		writeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		writeLabelConstraints.gridx = 0;
		writeLabelConstraints.gridy = 1;
		writeLabelConstraints.weightx = 0;
		writeLabelConstraints.weighty = 0;
		writeLabelConstraints.insets = titleGap;
		writeLabelConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showWriteLabel, writeLabelConstraints);
		GridBagConstraints writeYesConstraints = new GridBagConstraints();
		writeYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		writeYesConstraints.gridx = 1;
		writeYesConstraints.gridy = 1;
		writeYesConstraints.weightx = weigth;
		writeYesConstraints.weighty = 0;
		writeYesConstraints.insets = yesGap;
		writeYesConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showWriteYes, writeYesConstraints);
		GridBagConstraints writeNoConstraints = new GridBagConstraints();
		writeNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		writeNoConstraints.gridx = 2;
		writeNoConstraints.gridy = 1;
		writeNoConstraints.weightx = weigth;
		writeNoConstraints.weighty = 0;
		writeNoConstraints.insets = noGap;
		writeNoConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showWriteNo, writeNoConstraints);

		showDeltaGroup = new ButtonGroup();
		showDeltaLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_DELTA"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES");
		showDeltaYes = new JRadioButton(msg, true);
		showDeltaYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_YES));
		showDeltaYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addDelta();
			}

		});
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO");
		showDeltaNo = new JRadioButton(msg, false);
		showDeltaNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_NO));
		showDeltaNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeDelta();
			}

		});
		showDeltaGroup.add(showDeltaYes);
		showDeltaGroup.add(showDeltaNo);
		GridBagConstraints deltaLabelConstraints = new GridBagConstraints();
		deltaLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		deltaLabelConstraints.gridx = 0;
		deltaLabelConstraints.gridy = 2;
		deltaLabelConstraints.weightx = 0;
		deltaLabelConstraints.weighty = 0;
		deltaLabelConstraints.insets = titleGap;
		deltaLabelConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showDeltaLabel, deltaLabelConstraints);
		GridBagConstraints deltaYesConstraints = new GridBagConstraints();
		deltaYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		deltaYesConstraints.gridx = 1;
		deltaYesConstraints.gridy = 2;
		deltaYesConstraints.weightx = weigth;
		deltaYesConstraints.weighty = 0;
		deltaYesConstraints.insets = yesGap;
		deltaYesConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showDeltaYes, deltaYesConstraints);
		GridBagConstraints deltaNoConstraints = new GridBagConstraints();
		deltaNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		deltaNoConstraints.gridx = 2;
		deltaNoConstraints.gridy = 2;
		deltaNoConstraints.weightx = weigth;
		deltaNoConstraints.weighty = 0;
		deltaNoConstraints.insets = noGap;
		deltaNoConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showDeltaNo, deltaNoConstraints);

		JSeparator separator1 = new JSeparator();
		GridBagConstraints separator1Constraints = new GridBagConstraints();
		separator1Constraints.fill = GridBagConstraints.HORIZONTAL;
		separator1Constraints.gridx = 0;
		separator1Constraints.gridy = 3;
		separator1Constraints.weightx = 1;
		separator1Constraints.weighty = 0;
		separator1Constraints.insets = new Insets(5, 0, 5, 0);
		separator1Constraints.gridwidth = GridBagConstraints.REMAINDER;
		showPanel.add(separator1, separator1Constraints);

		showDiffGroup = new ButtonGroup();
		showDiffLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_DIFF"));
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_YES");
		showDiffYes = new JRadioButton(msg, true);
		showDiffYes.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_YES));
		showDiffYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addDiff();
			}

		});
		msg = Messages.getMessage("DIALOGS_OPTIONS_SNAPSHOT_SHOW_NO");
		showDiffNo = new JRadioButton(msg, false);
		showDiffNo.setActionCommand(String
				.valueOf(SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_NO));
		showDiffNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeDiff();
			}

		});
		showDiffGroup.add(showDiffYes);
		showDiffGroup.add(showDiffNo);
		GridBagConstraints diffLabelConstraints = new GridBagConstraints();
		diffLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		diffLabelConstraints.gridx = 0;
		diffLabelConstraints.gridy = 4;
		diffLabelConstraints.weightx = 0;
		diffLabelConstraints.weighty = 0;
		diffLabelConstraints.insets = new Insets(0, 0, 0, titleGap.right);
		diffLabelConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showDiffLabel, diffLabelConstraints);
		GridBagConstraints diffYesConstraints = new GridBagConstraints();
		diffYesConstraints.fill = GridBagConstraints.HORIZONTAL;
		diffYesConstraints.gridx = 1;
		diffYesConstraints.gridy = 4;
		diffYesConstraints.weightx = weigth;
		diffYesConstraints.weighty = 0;
		diffYesConstraints.insets = new Insets(0, 0, 0, 0);
		diffYesConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showDiffYes, diffYesConstraints);
		GridBagConstraints diffNoConstraints = new GridBagConstraints();
		diffNoConstraints.fill = GridBagConstraints.HORIZONTAL;
		diffNoConstraints.gridx = 2;
		diffNoConstraints.gridy = 4;
		diffNoConstraints.weightx = weigth;
		diffNoConstraints.weighty = 0;
		diffNoConstraints.insets = new Insets(0, noGap.left, 0, 0);
		diffNoConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(showDiffNo, diffNoConstraints);

		JSeparator separator2 = new JSeparator();
		GridBagConstraints separator2Constraints = new GridBagConstraints();
		separator2Constraints.fill = GridBagConstraints.HORIZONTAL;
		separator2Constraints.gridx = 0;
		separator2Constraints.gridy = 5;
		separator2Constraints.weightx = 1;
		separator2Constraints.weighty = 0;
		separator2Constraints.insets = new Insets(5, 0, 5, 0);
		separator2Constraints.gridwidth = GridBagConstraints.REMAINDER;
		showPanel.add(separator2, separator2Constraints);

		comparisonColumnSortLabel = new JLabel(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_ORDER"));
		int[] defaultArray = SnapshotOptions.DEFAULT_COMPARE_COLUMN_ORDER;
		String[] toSet = new String[defaultArray.length];
		for (int i = 0; i < defaultArray.length; i++) {
			toSet[i] = columnIdToString(defaultArray[i]);
		}
		comparisonColumnSorter = new SnapshotCompareOptionTable(
				new StringArrayTableModel(toSet));
		comparisonColumnSorterScrollPane = new JScrollPane(
				comparisonColumnSorter);
		comparisonColumnSorterScrollPane
				.setPreferredSize(new Dimension(50, 35));
		GridBagConstraints comparisonColumnSortLabelConstraints = new GridBagConstraints();
		comparisonColumnSortLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		comparisonColumnSortLabelConstraints.gridx = 0;
		comparisonColumnSortLabelConstraints.gridy = 6;
		comparisonColumnSortLabelConstraints.weightx = 0;
		comparisonColumnSortLabelConstraints.weighty = 0;
		comparisonColumnSortLabelConstraints.insets = new Insets(0, 0, 0,
				titleGap.right);
		comparisonColumnSortLabelConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(comparisonColumnSortLabel,
				comparisonColumnSortLabelConstraints);
		GridBagConstraints comparisonColumnSorterConstraints = new GridBagConstraints();
		comparisonColumnSorterConstraints.fill = GridBagConstraints.HORIZONTAL;
		comparisonColumnSorterConstraints.gridx = 1;
		comparisonColumnSorterConstraints.gridy = 6;
		comparisonColumnSorterConstraints.weightx = 1;
		comparisonColumnSorterConstraints.weighty = 0;
		comparisonColumnSorterConstraints.gridwidth = GridBagConstraints.REMAINDER;
		comparisonColumnSorterConstraints.insets = new Insets(0, 0, 0,
				titleGap.right);
		comparisonColumnSorterConstraints.anchor = GridBagConstraints.WEST;
		showPanel.add(comparisonColumnSorterScrollPane,
				comparisonColumnSorterConstraints);
	}

	private void initTimeFilterPanel() {
		timeFilterPanel = new JPanel(new GridBagLayout());

		timeFilterLabel = new JLabel(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER_TYPE"),
				JLabel.RIGHT);
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;
		labelConstraints.weightx = 0;
		labelConstraints.weighty = 0;
		timeFilterPanel.add(timeFilterLabel, labelConstraints);

		timeFilterComboBox = new JComboBox();
		timeFilterComboBox.addItem(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER_DAY"));
		timeFilterComboBox.addItem(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER_WEEK"));
		timeFilterComboBox.addItem(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER_DAY-7"));
		timeFilterComboBox.addItem(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER_MONTH"));
		timeFilterComboBox.addItem(Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER_DAY-30"));
		GridBagConstraints comboBoxConstraints = new GridBagConstraints();
		comboBoxConstraints.fill = GridBagConstraints.NONE;
		comboBoxConstraints.gridx = 1;
		comboBoxConstraints.gridy = 0;
		comboBoxConstraints.weightx = 0;
		comboBoxConstraints.weighty = 0;
		timeFilterPanel.add(timeFilterComboBox, comboBoxConstraints);

		GridBagConstraints glueConstraints = new GridBagConstraints();
		glueConstraints.fill = GridBagConstraints.HORIZONTAL;
		glueConstraints.gridx = 2;
		glueConstraints.gridy = 0;
		glueConstraints.weightx = 1;
		glueConstraints.weighty = 0;
		timeFilterPanel.add(Box.createGlue(), glueConstraints);

		timeFilterPanel.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED), Messages
				.getMessage("DIALOGS_OPTIONS_SNAPSHOT_LIST_FILTER"),
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getOptionsTitleFont()));
	}

	/**
	 * @return The buttonGroup attribute, containing the autoSnapshotCommentYes
	 *         and autoSnapshotCommentNo JRadioButtons
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * Selects a auto comment JRadioButton, depending on the autoComment
	 * parameter value
	 * 
	 * @param autoComment
	 *            Has to be either SNAPSHOT_AUTO_COMMENT_YES or
	 *            SNAPSHOT_AUTO_COMMENT_NO, otherwise a IllegalArgumentException
	 *            is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectAutoCommentButton(int autoComment)
			throws IllegalArgumentException {
		switch (autoComment) {
		case SnapshotOptions.SNAPSHOT_AUTO_COMMENT_YES:
			autoSnapshotCommentYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_AUTO_COMMENT_NO:
			autoSnapshotCommentNo.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a show read JRadioButton, depending on the showRead parameter
	 * value
	 * 
	 * @param showRead
	 *            Has to be either SNAPSHOT_COMPARE_SHOW_READ_YES or
	 *            SNAPSHOT_COMPARE_SHOW_READ_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectShowReadButton(int showRead)
			throws IllegalArgumentException {
		switch (showRead) {
		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_YES:
			showReadYes.setSelected(true);
			addRead();
			break;

		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_READ_NO:
			showReadNo.setSelected(true);
			removeRead();
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a show write JRadioButton, depending on the showWrite parameter
	 * value
	 * 
	 * @param showWrite
	 *            Has to be either SNAPSHOT_COMPARE_SHOW_WRITE_YES or
	 *            SNAPSHOT_COMPARE_SHOW_WRITE_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectShowWriteButton(int showWrite)
			throws IllegalArgumentException {
		switch (showWrite) {
		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_YES:
			showWriteYes.setSelected(true);
			addWrite();
			break;

		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_WRITE_NO:
			showWriteNo.setSelected(true);
			removeWrite();
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a show delta JRadioButton, depending on the showDelta parameter
	 * value
	 * 
	 * @param showDelta
	 *            Has to be either SNAPSHOT_COMPARE_SHOW_DELTA_YES or
	 *            SNAPSHOT_COMPARE_SHOW_DELTA_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectShowDeltaButton(int showDelta)
			throws IllegalArgumentException {
		switch (showDelta) {
		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_YES:
			showDeltaYes.setSelected(true);
			addDelta();
			break;

		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DELTA_NO:
			showDeltaNo.setSelected(true);
			removeDelta();
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a show diff JRadioButton, depending on the showDiff parameter
	 * value
	 * 
	 * @param showDiff
	 *            Has to be either SNAPSHOT_COMPARE_SHOW_DIFF_YES or
	 *            SNAPSHOT_COMPARE_SHOW_DIFF_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectShowDiffButton(int showDiff)
			throws IllegalArgumentException {
		switch (showDiff) {
		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_YES:
			showDiffYes.setSelected(true);
			addDiff();
			break;

		case SnapshotOptions.SNAPSHOT_COMPARE_SHOW_DIFF_NO:
			showDiffNo.setSelected(true);
			removeDiff();
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a csv id JRadioButton, depending on the csvID parameter value
	 * 
	 * @param csvID
	 *            Has to be either SNAPSHOT_CSV_ID_YES or SNAPSHOT_CSV_ID_NO,
	 *            otherwise a IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectCSVIDButton(int csvID) throws IllegalArgumentException {
		switch (csvID) {
		case SnapshotOptions.SNAPSHOT_CSV_ID_YES:
			csvIDYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_CSV_ID_NO:
			csvIDNo.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a csv time JRadioButton, depending on the csvTime parameter value
	 * 
	 * @param csvTime
	 *            Has to be either SNAPSHOT_CSV_TIME_YES or
	 *            SNAPSHOT_CSV_TIME_NO, otherwise a IllegalArgumentException is
	 *            thrown
	 * @throws IllegalArgumentException
	 */
	public void selectCSVTimeButton(int csvTime)
			throws IllegalArgumentException {
		switch (csvTime) {
		case SnapshotOptions.SNAPSHOT_CSV_TIME_YES:
			csvTimeYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_CSV_TIME_NO:
			csvTimeNo.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a csv contextID JRadioButton, depending on the csvContextID
	 * parameter value
	 * 
	 * @param csvContextID
	 *            Has to be either SNAPSHOT_CSV_CONTEXT_ID_YES or
	 *            SNAPSHOT_CSV_CONTEXT_ID_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
	public void selectCSVContextIDButton(int csvContextID)
			throws IllegalArgumentException {
		switch (csvContextID) {
		case SnapshotOptions.SNAPSHOT_CSV_CONTEXT_ID_YES:
			csvContextIDYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_CSV_CONTEXT_ID_NO:
			csvContextIDNo.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a csv read JRadioButton, depending on the csvRead parameter value
	 * 
	 * @param csvRead
	 *            Has to be either SNAPSHOT_CSV_READ_YES or
	 *            SNAPSHOT_CSV_READ_NO, otherwise a IllegalArgumentException is
	 *            thrown
	 * @throws IllegalArgumentException
	 */
	public void selectCSVReadButton(int csvRead)
			throws IllegalArgumentException {
		switch (csvRead) {
		case SnapshotOptions.SNAPSHOT_CSV_READ_YES:
			csvReadYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_CSV_READ_NO:
			csvReadNo.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a csv write JRadioButton, depending on the csvWrite parameter
	 * value
	 * 
	 * @param csvWrite
	 *            Has to be either SNAPSHOT_CSV_WRITE_YES or
	 *            SNAPSHOT_CSV_WRITE_NO, otherwise a IllegalArgumentException is
	 *            thrown
	 * @throws IllegalArgumentException
	 */
	public void selectCSVWriteButton(int csvWrite)
			throws IllegalArgumentException {
		switch (csvWrite) {
		case SnapshotOptions.SNAPSHOT_CSV_WRITE_YES:
			csvWriteYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_CSV_WRITE_NO:
			csvWriteNo.setSelected(true);
			break;

		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Selects a csv delta JRadioButton, depending on the csvDelta parameter
	 * value
	 * 
	 * @param csvDelta
	 *            Has to be either SNAPSHOT_CSV_DELTA_YES or
	 *            SNAPSHOT_CSV_DELTA_NO, otherwise a IllegalArgumentException is
	 *            thrown
	 * @throws IllegalArgumentException
	 */
	public void selectCSVDeltaButton(int csvDelta)
			throws IllegalArgumentException {
		switch (csvDelta) {
		case SnapshotOptions.SNAPSHOT_CSV_DELTA_YES:
			csvDeltaYes.setSelected(true);
			break;

		case SnapshotOptions.SNAPSHOT_CSV_DELTA_NO:
			csvDeltaNo.setSelected(true);
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
	public String getSnapshotDefaultComment() {
		return this.defaultSnapshotComment.getText();
	}

	/**
	 * Sets the default comment.
	 * 
	 * @param _comment
	 *            The default comment
	 */
	public void setSnapshotDefaultComment(String _comment) {
		this.defaultSnapshotComment.setText(_comment);
	}

	/**
	 * Returns true if show read has been selected.
	 * 
	 * @return True if show read has been selected
	 */
	public String hasShowRead() {
		ButtonModel selectedModel = showReadGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if show write has been selected.
	 * 
	 * @return True if show write has been selected
	 */
	public String hasShowWrite() {
		ButtonModel selectedModel = showWriteGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if show delta has been selected.
	 * 
	 * @return True if show delta has been selected
	 */
	public String hasShowDelta() {
		ButtonModel selectedModel = showDeltaGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if show diff has been selected.
	 * 
	 * @return True if show diff has been selected
	 */
	public String hasShowDiff() {
		ButtonModel selectedModel = showDiffGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if csv id has been selected.
	 * 
	 * @return True if csv id has been selected
	 */
	public String hasCSVID() {
		ButtonModel selectedModel = csvIDGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if csv time has been selected.
	 * 
	 * @return True if csv time has been selected
	 */
	public String hasCSVTime() {
		ButtonModel selectedModel = csvTimeGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if csv context ID has been selected.
	 * 
	 * @return True if csv context ID has been selected
	 */
	public String hasCSVContextID() {
		ButtonModel selectedModel = csvContextIDGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if csv read has been selected.
	 * 
	 * @return True if csv read has been selected
	 */
	public String hasCSVRead() {
		ButtonModel selectedModel = csvReadGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if csv write has been selected.
	 * 
	 * @return True if csv write has been selected
	 */
	public String hasCSVWrite() {
		ButtonModel selectedModel = csvWriteGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * Returns true if csv delta has been selected.
	 * 
	 * @return True if csv delta has been selected
	 */
	public String hasCSVDelta() {
		ButtonModel selectedModel = csvDeltaGroup.getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		return selectedActionCommand;
	}

	/**
	 * @return
	 */
	public String getSeparator() {
		switch (this.separatorComboBox.getSelectedIndex()) {
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
	public void setSeparator(String val_s) {
		// System.out.println ( "setSeparator" );

		int idx;

		if (val_s == null) {
			idx = 0;
		} else if (val_s
				.equals(SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_SEMICOLON)) {
			idx = 0;
		} else if (val_s.equals(SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_TAB)) {
			idx = 1;
		} else if (val_s.equals(SnapshotOptions.SNAPSHOT_CSV_SEPARATOR_PIPE)) {
			idx = 2;
		} else if (val_s.trim().equals("")) {
			idx = 1;
		} else {
			idx = 0;
		}

		this.separatorComboBox.setSelectedIndex(idx);
	}

	public int getSelectedTimeFilter() {
		return timeFilterComboBox.getSelectedIndex();
	}

	public void setSelectedTimeFilter(int filter) {
		timeFilterComboBox.setSelectedIndex(filter);
	}

	public void setCompareColumnOrder(int[] order) {
		if (order != null) {
			String[] names = new String[order.length];
			for (int i = 0; i < order.length; i++) {
				names[i] = columnIdToString(order[i]);
			}
			comparisonColumnSorter.setModel(new StringArrayTableModel(names));
			if (showReadYes.isSelected()) {
				addRead();
			} else {
				removeRead();
			}
			if (showWriteYes.isSelected()) {
				addWrite();
			} else {
				removeWrite();
			}
			if (showDeltaYes.isSelected()) {
				addDelta();
			} else {
				removeDelta();
			}
			if (showDiffYes.isSelected()) {
				addDiff();
			} else {
				removeDiff();
			}
		}
	}

	public int[] getCompareColumnOrder() {
		String[] names = comparisonColumnSorter.getNameModelAsArray();
		int[] result = new int[names.length];
		for (int i = 0; i < names.length; i++) {
			result[i] = stringToColumnId(names[i]);
		}
		return result;
	}

	private String columnIdToString(int id) {
		switch (id) {
		case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_R:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_R");
		case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_W:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_W");
		case SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_DELTA:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_DELTA");
		case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_R:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_R");
		case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_W:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_W");
		case SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_DELTA:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_DELTA");
		case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_R:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R");
		case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_W:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W");
		case SnapshotOptions.SNAPSHOT_COMPARE_DIFF_DELTA:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA");
		default:
			return Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_NAME");
		}
	}

	private int stringToColumnId(String value) {
		if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_R").equals(
				value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_R;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_W")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_W;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_DELTA")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_SNAP1_DELTA;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_R")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_R;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_W")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_W;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_DELTA")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_SNAP2_DELTA;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_DIFF_R;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_DIFF_W;
		} else if (Messages.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA")
				.equals(value)) {
			return SnapshotOptions.SNAPSHOT_COMPARE_DIFF_DELTA;
		} else {
			return SnapshotOptions.SNAPSHOT_COMPARE_COLUMNS_NAME;
		}
	}

	private void addRead() {
		if (showDiffYes.isSelected()) {
			comparisonColumnSorter.addName(Messages
					.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R"));
		}
		comparisonColumnSorter.addName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_R"));
		comparisonColumnSorter.addName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_R"));
	}

	private void removeRead() {
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_R"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_R"));
	}

	private void addWrite() {
		if (showDiffYes.isSelected()) {
			comparisonColumnSorter.addName(Messages
					.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W"));
		}
		comparisonColumnSorter.addName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_W"));
		comparisonColumnSorter.addName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_W"));
	}

	private void removeWrite() {
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_W"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_W"));
	}

	private void addDelta() {
		if (showDiffYes.isSelected()) {
			comparisonColumnSorter.addName(Messages
					.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA"));
		}
		comparisonColumnSorter.addName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_DELTA"));
		comparisonColumnSorter.addName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_DELTA"));
	}

	private void removeDelta() {
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP1_DELTA"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_SNAP2_DELTA"));
	}

	private void addDiff() {
		if (showReadYes.isSelected()) {
			comparisonColumnSorter.addName(Messages
					.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R"));
		}
		if (showWriteYes.isSelected()) {
			comparisonColumnSorter.addName(Messages
					.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W"));
		}
		if (showDeltaYes.isSelected()) {
			comparisonColumnSorter.addName(Messages
					.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA"));
		}
	}

	private void removeDiff() {
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_R"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_W"));
		comparisonColumnSorter.removeName(Messages
				.getMessage("SNAPSHOT_COMPARE_COLUMNS_DIFF_DELTA"));
	}

}
