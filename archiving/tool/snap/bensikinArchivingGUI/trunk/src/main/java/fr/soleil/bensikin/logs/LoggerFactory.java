//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/logs/LoggerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LoggerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: LoggerFactory.java,v $
// Revision 1.3  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.logs;

/**
 * A factory used to instantiate IHistoryManager of 1 types: DEFAULT_TYPE
 * 
 * @author CLAISSE
 */
public class LoggerFactory {
	/**
	 * Code for the default (and so far only) implementation
	 */
	public static final int DEFAULT_TYPE = 1;

	private static ILogger currentImpl = null;

	/**
	 * Instantiates and returns an implementation the
	 * <code>ILogger<code> described by <code>typeOfImpl<code>
	 * 
	 * @param typeOfImpl
	 *            The type of implementation of ILogger
	 * @return An implementation of ILogger
	 * @throws IllegalArgumentException
	 *             If <code>typeOfImpl<code> isn't in (DEFAULT_LIFE_CYCLE) (Only
	 *             one implementation so far)
	 */
	public static ILogger getImpl(int typeOfImpl) {
		switch (typeOfImpl) {
		case DEFAULT_TYPE:
			currentImpl = new DefaultLogger();
			break;

		default:
			throw new IllegalStateException(
					"Expected either DEFAULT_TYPE (1), got " + typeOfImpl
							+ " instead.");
		}

		return currentImpl;
	}

	/**
	 * Returns the current implementation as precedently instantiated.
	 * 
	 * @return The current implementation as precedently instantiated 28 juin
	 *         2005
	 */
	public static ILogger getCurrentImpl() {
		return currentImpl;
	}

}
