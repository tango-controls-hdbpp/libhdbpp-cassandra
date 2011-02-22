//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  FieldFormatException.
//						(Claisse Laurent) - 11 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.5  2005/11/29 18:25:13  chinkumo
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
package fr.soleil.archiving.gui.exceptions;

/**
 * An exception launched when the filtering parameters on contexts or snapshots
 * are incorrect.
 * 
 * @author CLAISSE
 */
public class FieldFormatException extends Exception {
	/**
	 * During snapshots filtering, the id field was invalid
	 */
	public static final int FILTER_SNAPSHOTS_ID = 101;
	/**
	 * During snapshots filtering, the start time field was invalid
	 */
	public static final int FILTER_SNAPSHOTS_START_TIME = 102;
	/**
	 * During snapshots filtering, the end time field was invalid
	 */
	public static final int FILTER_SNAPSHOTS_END_TIME = 103;

	/**
	 * During contexts filtering, the id field was invalid
	 */
	public static final int SEARCH_CONTEXTS_ID = 101;
	/**
	 * During contexts filtering, the start time field was invalid
	 */
	public static final int SEARCH_CONTEXTS_START_TIME = 102;
	/**
	 * During contexts filtering, the start time field was invalid
	 */
	public static final int SEARCH_CONTEXTS_END_TIME = 103;

	private int code;

	/**
	 * @param _code
	 *            The Exception's code
	 */
	public FieldFormatException(int _code) {
		super();
		this.code = _code;
	}

	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
