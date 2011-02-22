//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ArchivingModesComboBox.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingModesComboBox.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ArchivingModesComboBox.java,v $
// Revision 1.2  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.components.archiving;

import java.awt.Dimension;

import javax.swing.JComboBox;

public class ArchivingModesComboBox extends JComboBox {
	public static final String NO_SELECTION = "---";

	// private String [] standardOperators = { NO_SELECTION ,
	// ISnapManager.OP_EQUALS , ISnapManager.OP_LOWER_THAN_STRICT ,
	// ISnapManager.OP_GREATER_THAN_STRICT , ISnapManager.OP_LOWER_THAN ,
	// ISnapManager.OP_LOWER_THAN };
	private String[] standardOperators = { NO_SELECTION, "Periodical",
			"Absolute", "Relative", "Threshold", "On difference",
			"On calculation", "External" };
	private String[] operators;

	public static final int STANDARD_TYPE = 0;

	/**
	 * @param type
	 * @throws IllegalStateException
	 */
	public ArchivingModesComboBox(int type) throws IllegalStateException {
		super();

		switch (type) {
		case STANDARD_TYPE:
			operators = standardOperators;
			break;

		default:
			throw new IllegalStateException("Expected either of "
					+ STANDARD_TYPE + "," + " as a parameter. Received " + type
					+ " instead.");
		}

		for (int i = 0; i < operators.length; i++) {
			super.addItem(operators[i]);
		}

		this.setPreferredSize(new Dimension(100, 20));
		this.setMaximumSize(new Dimension(100, 20));
	}

}
