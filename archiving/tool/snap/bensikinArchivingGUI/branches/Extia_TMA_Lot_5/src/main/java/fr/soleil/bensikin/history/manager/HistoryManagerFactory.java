//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/manager/HistoryManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HistoryManagerFactory.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: HistoryManagerFactory.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.history.manager;

/**
 * A factory used to instantiate IHistoryManager of 2 types: DUMMY_IMPL_TYPE or
 * XML_IMPL_TYPE
 * 
 * @author CLAISSE
 */
public class HistoryManagerFactory {
	/**
	 * Code for a dummy implementation that does nothing
	 */
	public static final int DUMMY_IMPL_TYPE = 1;

	/**
	 * Code for an XML implementation that saves/loads to/from XML files
	 */
	public static final int XML_IMPL_TYPE = 2;

	private static IHistoryManager currentImpl = null;

	/**
	 * Instantiates and returns an implementation the
	 * <code>IHistoryManager<code> described by <code>typeOfImpl<code>
	 * 
	 * @param typeOfImpl
	 *            The type of implementation of IHistoryManager
	 * @return An implementation of IHistoryManager
	 * @throws IllegalArgumentException
	 *             If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE,
	 *             XML_IMPL_TYPE)
	 */
	public static IHistoryManager getImpl(int typeOfImpl) {
		switch (typeOfImpl) {
		case DUMMY_IMPL_TYPE:
			currentImpl = new DummyHistoryManager();
			break;

		case XML_IMPL_TYPE:
			currentImpl = new XMLHistoryManager();
			break;

		default:
			throw new IllegalStateException(
					"Expected either DUMMY_IMPL_TYPE (1) or XML_IMPL_TYPE (2), got "
							+ typeOfImpl + " instead.");
		}

		return currentImpl;
	}

	/**
	 * Returns the current implementation as precedently instantiated.
	 * 
	 * @return The current implementation as precedently instantiated
	 */
	public static IHistoryManager getCurrentImpl() {
		return currentImpl;
	}
}
