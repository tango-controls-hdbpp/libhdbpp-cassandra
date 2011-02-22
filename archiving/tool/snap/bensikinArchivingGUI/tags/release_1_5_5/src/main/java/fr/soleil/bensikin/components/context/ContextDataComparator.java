// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/ContextDataComparator.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ContextDataComparator.
// (Claisse Laurent) - 13 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ContextDataComparator.java,v $
// Revision 1.2 2006/04/13 12:21:41 ounsy
// added a sort on the snapshot detail table
//
// Revision 1.1 2005/11/29 18:25:27 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:32 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.components.context;

import fr.soleil.bensikin.components.BensikinComparator;
import fr.soleil.bensikin.data.context.ContextData;

/**
 * Implements a Comparator on ContextData objects, useful to sort the contexts
 * list. The comparison can be on any one of the ContextData fields, depending
 * on the type specified on constructing the comparator.
 * 
 * @author CLAISSE
 */
public class ContextDataComparator extends BensikinComparator<ContextData> {

	/**
	 * The comparison will be on the ContextData's id field
	 */
	public static final int COMPARE_ID = 0;

	/**
	 * The comparison will be on the ContextData's creation date field
	 */
	public static final int COMPARE_TIME = 1;

	/**
	 * The comparison will be on the ContextData's name field
	 */
	public static final int COMPARE_NAME = 2;

	/**
	 * The comparison will be on the ContextData's author field
	 */
	public static final int COMPARE_AUTHOR = 3;

	/**
	 * The comparison will be on the ContextData's reason field
	 */
	public static final int COMPARE_REASON = 4;

	/**
	 * The comparison will be on the ContextData's description field
	 */
	public static final int COMPARE_DESCRIPTION = 5;

	/**
	 * Builds a comparator on the desired ContextData field
	 * 
	 * @param _fieldToCompare
	 *            The field on which the comparison will be done
	 * @throws IllegalArgumentException
	 *             If _fieldToCompare isn't in (COMPARE_ID, COMPARE_TIME
	 *             COMPARE_NAME, COMPARE_AUTHOR, COMPARE_REASON,
	 *             COMPARE_DESCRIPTION)
	 */
	public ContextDataComparator(int _fieldToCompare)
			throws IllegalArgumentException {
		super(_fieldToCompare);
		if (_fieldToCompare != COMPARE_ID && _fieldToCompare != COMPARE_TIME
				&& _fieldToCompare != COMPARE_NAME
				&& _fieldToCompare != COMPARE_AUTHOR
				&& _fieldToCompare != COMPARE_REASON
				&& _fieldToCompare != COMPARE_DESCRIPTION) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int compare(ContextData data1, ContextData data2) {

		int ret = 0;

		switch (fieldToCompare) {
		case COMPARE_ID:
			ret = data1.getId() - data2.getId();
			break;

		case COMPARE_TIME:
			java.sql.Date date1 = data1.getCreation_date();
			java.sql.Date date2 = data2.getCreation_date();
			ret = date1.compareTo(date2);
			break;

		case COMPARE_NAME:
			String name1 = data1.getName();
			String name2 = data2.getName();
			ret = super.compareStrings(name1, name2);
			break;

		case COMPARE_AUTHOR:
			String author1 = data1.getAuthor_name();
			String author2 = data2.getAuthor_name();
			ret = super.compareStrings(author1, author2);
			break;

		case COMPARE_REASON:
			String reason1 = data1.getReason();
			String reason2 = data2.getReason();
			ret = super.compareStrings(reason1, reason2);
			break;

		case COMPARE_DESCRIPTION:
			String description1 = data1.getDescription();
			String description2 = data2.getDescription();
			ret = super.compareStrings(description1, description2);
			break;
		}

		return ret;
	}
}
