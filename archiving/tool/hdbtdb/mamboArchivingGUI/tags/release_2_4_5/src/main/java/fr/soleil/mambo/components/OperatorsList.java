//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/OperatorsList.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OperatorsList.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OperatorsList.java,v $
// Revision 1.3  2006/10/04 09:57:20  ounsy
// minor changes
//
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
package fr.soleil.mambo.components;

import java.awt.Dimension;

import javax.swing.JComboBox;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

public class OperatorsList extends JComboBox {
    public static final String NO_SELECTION = "---";

    // private String[] idOperators = {NO_SELECTION, GlobalConst.OP_EQUALS,
    // GlobalConst.OP_LOWER_THAN_STRICT, GlobalConst.OP_GREATER_THAN_STRICT,
    // GlobalConst.OP_LOWER_THAN, GlobalConst.OP_LOWER_THAN};
    // private String[] afterTimeOperators = {NO_SELECTION,
    // GlobalConst.OP_GREATER_THAN, GlobalConst.OP_GREATER_THAN_STRICT};
    // private String[] beforeTimeOperators = {NO_SELECTION,
    // GlobalConst.OP_LOWER_THAN, GlobalConst.OP_LOWER_THAN_STRICT};
    private final String[] commentOperators = { NO_SELECTION, "Contains", "Starts with",
	    "Ends with" };
    private final String[] sinceOperators = { NO_SELECTION, "Today", "Yesterday", "Last week",
	    "Last month" };
    private final String[] logsOperators = { ILogger.DEBUG, ILogger.INFO, ILogger.WARNING,
	    ILogger.ERROR, ILogger.CRITIC };

    private String[] operators;

    public static final int ID_TYPE = 0;
    public static final int AFTER_TIME_TYPE = 1;
    public static final int BEFORE_TIME_TYPE = 2;
    public static final int COMMENT_TYPE = 3;
    public static final int SINCE_TYPE = 4;
    public static final int LOGS_TYPE = 5;

    /**
     * @param type
     * @throws IllegalStateException
     */
    public OperatorsList(final int type) throws IllegalStateException {
	super();

	switch (type) {
	/*
	 * case ID_TYPE: operators = idOperators; break;
	 * 
	 * case AFTER_TIME_TYPE: operators = afterTimeOperators; break;
	 * 
	 * case BEFORE_TIME_TYPE: operators = beforeTimeOperators; break;
	 */
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
	    throw new IllegalStateException("Expected either of " + ID_TYPE + ","
		    + BEFORE_TIME_TYPE + "," + AFTER_TIME_TYPE + "," + COMMENT_TYPE
		    + " as a parameter. Received " + type + " instead.");
	}

	for (final String operator : operators) {
	    super.addItem(operator);
	}

	setPreferredSize(new Dimension(100, 20));
	setMaximumSize(new Dimension(100, 20));
    }
}
