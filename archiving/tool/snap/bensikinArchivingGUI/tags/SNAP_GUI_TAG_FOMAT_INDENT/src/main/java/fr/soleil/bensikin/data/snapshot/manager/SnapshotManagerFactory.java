//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/manager/SnapshotManagerFactory.java,v $
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
//$Log: SnapshotManagerFactory.java,v $
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
package fr.soleil.bensikin.data.snapshot.manager;

/**
 * A factory used to instantiate ISnapshotManager of 2 types: DUMMY_IMPL_TYPE or
 * XML_IMPL_TYPE
 * 
 * @author CLAISSE
 */
public class SnapshotManagerFactory {
	/**
	 * Code for a dummy implementation that does nothing
	 */
	public static final int DUMMY_IMPL_TYPE = 1;

	/**
	 * Code for an XML implementation that saves/loads to/from XML files
	 */
	public static final int XML_IMPL_TYPE = 2;

	private static ISnapshotManager currentImpl = null;

	/**
	 * Instantiates and returns an implementation the
	 * <code>ISnapshotManager<code> described by <code>typeOfImpl<code>
	 * 
	 * @param typeOfImpl
	 *            The type of implementation of ISnapshotManager
	 * @return An implementation of ISnapshotManager
	 * @throws IllegalArgumentException
	 *             If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE,
	 *             XML_IMPL_TYPE)
	 */
	public static ISnapshotManager getImpl(int typeOfImpl) {
		switch (typeOfImpl) {
		case DUMMY_IMPL_TYPE:
			currentImpl = new DummySnapshotManagerImpl();
			break;

		case XML_IMPL_TYPE:
			currentImpl = new XMLSnapshotManagerImpl();
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
	 * @return The current implementation as precedently instantiated 28 juin
	 *         2005
	 */
	public static ISnapshotManager getCurrentImpl() {
		return currentImpl;
	}
}
