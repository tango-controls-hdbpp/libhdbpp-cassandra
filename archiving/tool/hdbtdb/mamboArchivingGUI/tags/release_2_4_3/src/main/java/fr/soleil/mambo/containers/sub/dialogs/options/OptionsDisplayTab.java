//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsDisplayTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: OptionsDisplayTab.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
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
package fr.soleil.mambo.containers.sub.dialogs.options;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.options.sub.DisplayOptions;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class OptionsDisplayTab extends JPanel {
	private static OptionsDisplayTab instance = null;

	JRadioButton winPLAF;
	JRadioButton javaPLAF;
	ButtonGroup buttonGroup;

	private Box emptyBox;
	private Box plafPanel;

	/**
	 * @return 8 juil. 2005
	 */
	public static OptionsDisplayTab getInstance() {
		if (instance == null) {
			instance = new OptionsDisplayTab();
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
	/*
	 * private OptionsDisplayTab () { buttonGroup = new ButtonGroup ();
	 * 
	 * String msg = Messages.getMessage ( "DIALOGS_OPTIONS_DISPLAY_WINDOWS" );
	 * winPLAF = new JRadioButton ( msg , true ); winPLAF.setActionCommand (
	 * String.valueOf ( DisplayOptions.PLAF_WIN ) ); msg = Messages.getMessage (
	 * "DIALOGS_OPTIONS_DISPLAY_JAVA" ); javaPLAF = new JRadioButton ( msg ,
	 * false ); javaPLAF.setActionCommand ( String.valueOf (
	 * DisplayOptions.PLAF_JAVA ) ); buttonGroup.add( winPLAF );
	 * buttonGroup.add( javaPLAF );
	 * 
	 * Box box = new Box ( BoxLayout.Y_AXIS ); box.add ( winPLAF ); box.add (
	 * Box.createVerticalStrut ( 5 ) ); box.add ( Box.createVerticalGlue () );
	 * box.add ( javaPLAF );
	 * 
	 * this.add ( box ); }
	 */
	private OptionsDisplayTab() {
		this.initComponents();
		this.addComponents();
		this.initLayout();
	}

	private void initLayout() {
		this.setLayout(new SpringLayout());

		SpringUtilities.makeCompactGrid(this, 2, 1, // rows, cols
				6, 6, // initX, initY
				6, 6, // xPad, yPad
				true);

	}

	private void addComponents() {
		this.add(plafPanel);
		this.add(emptyBox);
	}

	/**
     * 
     */
	private void initComponents() {
		buttonGroup = new ButtonGroup();

		String msg = Messages.getMessage("DIALOGS_OPTIONS_DISPLAY_WINDOWS");
		winPLAF = new JRadioButton(msg, true);
		winPLAF.setActionCommand(String.valueOf(DisplayOptions.PLAF_WIN));
		msg = Messages.getMessage("DIALOGS_OPTIONS_DISPLAY_JAVA");
		javaPLAF = new JRadioButton(msg, false);
		javaPLAF.setActionCommand(String.valueOf(DisplayOptions.PLAF_JAVA));
		buttonGroup.add(winPLAF);
		buttonGroup.add(javaPLAF); // TODO Auto-generated method stub

		emptyBox = new Box(BoxLayout.Y_AXIS);
		emptyBox.add(Box.createVerticalGlue());

		plafPanel = new Box(BoxLayout.Y_AXIS);
		plafPanel.add(javaPLAF);
		plafPanel.add(Box.createVerticalStrut(10));
		plafPanel.add(winPLAF);

		msg = Messages.getMessage("DIALOGS_OPTIONS_DISPLAY_BORDER");
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getTitleFont());
		plafPanel.setBorder(tb3);
	}

	/**
	 * @return 8 juil. 2005
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public JRadioButton getJavaPLAF() {
		return javaPLAF;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public JRadioButton getWinPLAF() {
		return winPLAF;
	}

	/**
	 * 20 juil. 2005
	 * 
	 * @param plaf
	 */
	public void selectPlafButton(int plaf) {
		switch (plaf) {
		case DisplayOptions.PLAF_WIN:
			winPLAF.setSelected(true);
			break;

		case DisplayOptions.PLAF_JAVA:
			javaPLAF.setSelected(true);
			break;
		}
	}
}
