//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/manager/ContextManagerFactory.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ArchivingConfigurationManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.1 $
//
//$Log: ContextManagerFactory.java,v $
//Revision 1.1  2005/12/14 14:07:18  ounsy
//first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
//under "bensikin.bensikin" and removing the same from their former locations
//
//Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.context.manager;

/**
 * A factory used to instantiate IContextManager of 2 types: DUMMY_IMPL_TYPE or
 * XML_IMPL_TYPE
 * 
 * @author CLAISSE
 */
public class ContextManagerFactory {
	/**
	 * Code for a dummy implementation that does nothing
	 */
	public static final int DUMMY_IMPL_TYPE = 1;

	/**
	 * Code for an XML implementation that saves/loads to/from XML files
	 */
	public static final int XML_IMPL_TYPE = 2;

	private static IContextManager currentImpl = null;

	/**
	 * Instantiates and returns an implementation the
	 * <code>IContextManager<code> described by <code>typeOfImpl<code>
	 * 
	 * @param typeOfImpl
	 *            The type of implementation of IContextManager
	 * @return An implementation of IContextManager
	 * @throws IllegalArgumentException
	 *             If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE,
	 *             XML_IMPL_TYPE)
	 */
	public static IContextManager getImpl(int typeOfImpl)
			throws IllegalArgumentException {
		switch (typeOfImpl) {
		case DUMMY_IMPL_TYPE:
			currentImpl = new DummyContextManagerImpl();
			break;

		case XML_IMPL_TYPE:
			currentImpl = new XMLContextManagerImpl();
			break;

		default:
			throw new IllegalArgumentException(
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
	public static IContextManager getCurrentImpl() {
		return currentImpl;
	}
}
