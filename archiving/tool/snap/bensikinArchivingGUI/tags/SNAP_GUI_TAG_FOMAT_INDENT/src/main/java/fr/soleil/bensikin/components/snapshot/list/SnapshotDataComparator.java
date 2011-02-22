//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/snapshot/list/SnapshotDataComparator.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ContextDataComparator.
//						(Claisse Laurent) - 13 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.1 $
//
//$Log: SnapshotDataComparator.java,v $
//Revision 1.1  2006/04/13 12:21:09  ounsy
//moved
//
//Revision 1.1  2005/11/29 18:25:13  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:32  chinkumo
//First commit
//
//
//copyleft :		    Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.snapshot.list;

import fr.soleil.bensikin.components.BensikinComparator;
import fr.soleil.bensikin.data.snapshot.SnapshotData;

/**
 * Implements a Comparator on SnapshotData objects, useful to sort the snapshots
 * list. The comparison can be on any one of the SnapshotData fields, depending
 * on the type specified on constructing the comparator. Empty or null values
 * are considered lowest.
 * 
 * @author CLAISSE
 */
public class SnapshotDataComparator extends BensikinComparator {
	/**
	 * The comparison will be on the SnapshotData's id field
	 */
	public static final int COMPARE_ID = 0;

	/**
	 * The comparison will be on the SnapshotData's time field
	 */
	public static final int COMPARE_TIME = 1;

	/**
	 * The comparison will be on the SnapshotData's comment field
	 */
	public static final int COMPARE_COMMENT = 2;

	/**
	 * Builds a comparator on the desired SnapshotData field
	 * 
	 * @param _fieldToCompare
	 *            The field on which the comparison will be done
	 * @throws IllegalArgumentException
	 *             If _fieldToCompare isn't in (COMPARE_ID, COMPARE_TIME,
	 *             COMPARE_COMMENT)
	 */
	public SnapshotDataComparator(int _fieldToCompare)
			throws IllegalArgumentException {
		super(_fieldToCompare);

		if (_fieldToCompare != COMPARE_ID && _fieldToCompare != COMPARE_TIME
				&& _fieldToCompare != COMPARE_COMMENT) {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		SnapshotData data1 = (SnapshotData) arg0;
		SnapshotData data2 = (SnapshotData) arg1;

		int ret = 0;

		switch (fieldToCompare) {
		case COMPARE_ID:
			ret = data1.getId() - data2.getId();
			break;

		case COMPARE_TIME:
			java.sql.Timestamp date1 = data1.getTime();
			java.sql.Timestamp date2 = data2.getTime();
			ret = date1.compareTo(date2);
			break;

		case COMPARE_COMMENT:
			String name1 = data1.getComment();
			String name2 = data2.getComment();
			ret = super.compareStrings(name1, name2);
			break;
		}

		return ret;
	}
}
