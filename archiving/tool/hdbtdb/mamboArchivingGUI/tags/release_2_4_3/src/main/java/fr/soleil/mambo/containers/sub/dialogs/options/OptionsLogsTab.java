//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsLogsTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsLogsTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: OptionsLogsTab.java,v $
// Revision 1.4  2006/10/04 09:57:29  ounsy
// minor changes
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:13  chinkumo
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

//import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.components.OperatorsList;
import fr.soleil.mambo.tools.Messages;

public class OptionsLogsTab extends JPanel {
	private static OptionsLogsTab instance = null;

	private OperatorsList comboBox;

	/**
	 * @return 8 juil. 2005
	 */
	public static OptionsLogsTab getInstance() {
		if (instance == null) {
			instance = new OptionsLogsTab();
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
	private OptionsLogsTab() {
		this.initComponents();
		this.initLayout();
	}

	private void initLayout() {
		Box logLevelPanel = new Box(BoxLayout.X_AXIS);
		logLevelPanel.add(comboBox);
		logLevelPanel.add(Box.createHorizontalGlue());

		String msg = Messages.getMessage("DIALOGS_OPTIONS_LOGS_LEVEL");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getTitleFont());
		CompoundBorder cb = BorderFactory.createCompoundBorder(tb,
				BorderFactory.createEmptyBorder(2, 4, 4, 4));
		logLevelPanel.setBorder(cb);

		// ---

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.add(logLevelPanel);
		this.add(Box.createVerticalGlue());
	}

	/**
	 * 7 juil. 2005
	 */
	private void initComponents() {
		comboBox = new OperatorsList(OperatorsList.LOGS_TYPE);
	}

	/**
	 * @return 8 juil. 2005
	 */
	public String getLogLevel() {
		return (String) comboBox.getSelectedItem();
	}

	public OperatorsList getComboBox() {
		return comboBox;
	}
}
