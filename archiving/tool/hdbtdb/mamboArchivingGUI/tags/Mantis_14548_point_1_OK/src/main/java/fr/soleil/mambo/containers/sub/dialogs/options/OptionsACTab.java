//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsACTab.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: OptionsACTab.java,v $
//Revision 1.1  2005/11/29 18:28:12  chinkumo
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.Messages;

public class OptionsACTab extends JPanel {
	private static OptionsACTab instance = null;

	private JRadioButton treeSelectionMode;
	private JRadioButton tableSelectionMode;
	private ButtonGroup buttonGroup;

	private ACTabDefaultPanel defaultPanel;

	private JLabel stackDepthLabel;
	private JTextField stackDepthField;

	/**
	 * @return 8 juil. 2005
	 */
	public static OptionsACTab getInstance() {
		if (instance == null) {
			instance = new OptionsACTab();
		}

		return instance;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static void resetInstance() {
		if (instance != null) {
			instance.repaint();
		}

		instance = null;

		ACTabDefaultPanel.resetInstance();
	}

	private void initLayout() {
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		buttonBox.add(treeSelectionMode);
		buttonBox.add(tableSelectionMode);

		Box attributesSelectionBox = new Box(BoxLayout.X_AXIS);
		attributesSelectionBox.add(buttonBox);
		attributesSelectionBox.add(Box.createHorizontalGlue());

		String msg1 = Messages
				.getMessage("DIALOGS_OPTIONS_AC_SELECTION_BORDER");
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg1,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
		CompoundBorder cb1 = BorderFactory.createCompoundBorder(tb1,
				BorderFactory.createEmptyBorder(2, 4, 4, 4));
		attributesSelectionBox.setBorder(cb1);

		// ---

		String msg2 = Messages.getMessage("DIALOGS_OPTIONS_AC_DEFAULT_BORDER");
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg2,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
		CompoundBorder cb2 = BorderFactory.createCompoundBorder(tb2,
				BorderFactory.createEmptyBorder(2, 4, 4, 4));
		defaultPanel.setBorder(cb2);

		// ---

		Box miscBox = new Box(BoxLayout.X_AXIS);
		miscBox.add(stackDepthLabel);
		miscBox.add(Box.createRigidArea(new Dimension(5, 0)));
		miscBox.add(stackDepthField);
		miscBox.add(Box.createHorizontalGlue());

		stackDepthField.setPreferredSize(new Dimension(50, stackDepthField
				.getPreferredSize().height));
		stackDepthField.setMaximumSize(new Dimension(50, stackDepthField
				.getPreferredSize().height));

		String msg3 = Messages.getMessage("DIALOGS_OPTIONS_VC_MISC_BORDER");
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg3,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
		CompoundBorder cb3 = BorderFactory.createCompoundBorder(tb3,
				BorderFactory.createEmptyBorder(2, 4, 4, 4));
		miscBox.setBorder(cb3);

		// ---

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.add(attributesSelectionBox);
		this.add(Box.createVerticalStrut(4));
		this.add(defaultPanel);
		this.add(Box.createVerticalStrut(4));
		this.add(miscBox);
		this.add(Box.createVerticalGlue());
	}

	/**
	 *
	 */
	private OptionsACTab() {
		this.initComponents();
		this.initLayout();
	}

	/**
     *
     */
	private void initComponents() {
		String msg = Messages.getMessage("DIALOGS_OPTIONS_AC_SELECTION_TREE");
		treeSelectionMode = new JRadioButton(msg, true);
		treeSelectionMode.setActionCommand(String
				.valueOf(ACOptions.SELECTION_MODE_TREE));

		msg = Messages.getMessage("DIALOGS_OPTIONS_AC_SELECTION_TABLE");
		tableSelectionMode = new JRadioButton(msg, false);
		tableSelectionMode.setActionCommand(String
				.valueOf(ACOptions.SELECTION_MODE_TABLE));

		buttonGroup = new ButtonGroup();
		buttonGroup.add(treeSelectionMode);
		buttonGroup.add(tableSelectionMode);

		// ---

		defaultPanel = ACTabDefaultPanel.getInstance();

		// ---

		stackDepthField = new JTextField();

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_MISC_STACK_DEPTH");
		stackDepthLabel = new JLabel(msg);
		stackDepthLabel.setLabelFor(stackDepthField);
	}

	/**
	 * @return 8 juil. 2005
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public String getStackDepth() {
		return stackDepthField.getText();
	}

	/**
	 * @param stackDepth_s
	 */
	public void setStackDepth(String stackDepth_s) {
		stackDepthField.setText(stackDepth_s);
	}

	/**
	 * 20 juil. 2005
	 * 
	 * @param plaf
	 */
	public void selectPlafButton(int plaf) {
		switch (plaf) {
		case ACOptions.SELECTION_MODE_TREE:
			treeSelectionMode.setSelected(true);
			break;

		case ACOptions.SELECTION_MODE_TABLE:
			tableSelectionMode.setSelected(true);
			break;
		}
	}

	/**
	 * @return Returns the tableSelectionMode.
	 */
	public JRadioButton getTableSelectionMode() {
		return tableSelectionMode;
	}

	/**
	 * @return Returns the treeSelectionMode.
	 */
	public JRadioButton getTreeSelectionMode() {
		return treeSelectionMode;
	}

	/**
	 * 20 juil. 2005
	 * 
	 * @param plaf
	 */
	public void selectIsAlternate(boolean _isAlternate) {
		if (_isAlternate) {
			tableSelectionMode.setSelected(true);
		} else {
			treeSelectionMode.setSelected(true);
		}
	}
}
