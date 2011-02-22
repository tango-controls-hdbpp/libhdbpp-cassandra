//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/OperatorsList.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OperatorsList.
//						(Claisse Laurent) - 14 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: OperatorsList.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components;

import java.awt.Dimension;

import javax.swing.JComboBox;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;

/**
 * Used to instantiate all JComboBox of the applications.
 * 
 * @author CLAISSE
 */
public class OperatorsList extends JComboBox {
	/**
	 * The default ComboBox element displayed before any selection is made;
	 * common to all types of <code>OperatorsList<code>
	 */
	public static final String NO_SELECTION = "---";

	private String[] idOperators = { NO_SELECTION, GlobalConst.OP_EQUALS,
			GlobalConst.OP_LOWER_THAN_STRICT,
			GlobalConst.OP_GREATER_THAN_STRICT, GlobalConst.OP_LOWER_THAN,
			GlobalConst.OP_GREATER_THAN };
	private String[] afterTimeOperators = { NO_SELECTION,
			GlobalConst.OP_GREATER_THAN, GlobalConst.OP_GREATER_THAN_STRICT };
	private String[] beforeTimeOperators = { NO_SELECTION,
			GlobalConst.OP_LOWER_THAN, GlobalConst.OP_LOWER_THAN_STRICT };
	private String[] commentOperators = { NO_SELECTION, "Contains",
			"Starts with", "Ends with" };
	private String[] sinceOperators = { NO_SELECTION, "Today", "Yesterday",
			"Last week", "Last month" };
	private String[] logsOperators = { ILogger.DEBUG, ILogger.INFO,
			ILogger.WARNING, ILogger.ERROR, ILogger.CRITIC };

	private String[] operators;

	/**
	 * For building a list of operators applying on a number, eg. an ID field
	 */
	public static final int ID_TYPE = 0;

	/**
	 * For building a list of operators applying on a start date/time
	 */
	public static final int AFTER_TIME_TYPE = 1;

	/**
	 * For building a list of operators applying on an end date/time
	 */
	public static final int BEFORE_TIME_TYPE = 2;

	/**
	 * For building a list of operators applying on a String
	 */
	public static final int COMMENT_TYPE = 3;

	/**
	 * For building a list of "since" options
	 */
	public static final int SINCE_TYPE = 4;

	/**
	 * For building a list of log levels options
	 */
	public static final int LOGS_TYPE = 5;

	/**
	 * Build a pre-filled ComboBox of the given type.
	 * 
	 * @param type
	 *            The desired type of ComboBox
	 * @throws IllegalArgumentException
	 *             If type isn't in (ID_TYPE, BEFORE_TIME_TYPE, AFTER_TIME_TYPE,
	 *             COMMENT_TYPE, SINCE_TYPE, or LOGS_TYPE)
	 */
	public OperatorsList(int type) throws IllegalArgumentException {
		super();

		switch (type) {
		case ID_TYPE:
			operators = idOperators;
			break;

		case AFTER_TIME_TYPE:
			operators = afterTimeOperators;
			break;

		case BEFORE_TIME_TYPE:
			operators = beforeTimeOperators;
			break;

		case COMMENT_TYPE:
			operators = commentOperators;
			break;

		case SINCE_TYPE:
			operators = sinceOperators;
			break;

		case LOGS_TYPE:
			operators = logsOperators;
			break;

		default:
			throw new IllegalArgumentException("Expected either of " + ID_TYPE
					+ "," + BEFORE_TIME_TYPE + "," + AFTER_TIME_TYPE + ","
					+ COMMENT_TYPE + " as a parameter. Received " + type
					+ " instead.");
		}

		for (int i = 0; i < operators.length; i++) {
			super.addItem(operators[i]);
		}

		this.setPreferredSize(new Dimension(100, 20));
		this.setMaximumSize(new Dimension(100, 20));
	}
}
