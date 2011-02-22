//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/FavoritesManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  FavoritesManagerFactory.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: FavoritesManagerFactory.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:38  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.favorites;

/**
 * A factory used to instantiate IFavoritesManager of 2 types: DUMMY_IMPL_TYPE or XML_IMPL_TYPE
 *
 * @author CLAISSE
 */
public class FavoritesManagerFactory
{
	/**
	 * Code for a dummy implementation that does nothing
	 */
	public static final int DUMMY_IMPL_TYPE = 1;

	/**
	 * Code for an XML implementation that saves/loads to/from XML files
	 */
	public static final int XML_IMPL_TYPE = 2;

	private static IFavoritesManager currentImpl = null;

	/**
	 * Instantiates and returns an implementation the <code>IFavoritesManager<code> described by <code>typeOfImpl<code>
	 *
	 * @param typeOfImpl The type of implementation of IFavoritesManager
	 * @return An implementation of IFavoritesManager
	 * @throws IllegalArgumentException If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE, XML_IMPL_TYPE)
	 */
	public static IFavoritesManager getImpl(int typeOfImpl)
	{
		switch ( typeOfImpl )
		{
			case DUMMY_IMPL_TYPE:
				currentImpl = new DummyFavoritesManager();
				break;

			case XML_IMPL_TYPE:
				currentImpl = new XMLFavoritesManager();
				break;

			default:
				throw new IllegalStateException("Expected either DUMMY_IMPL_TYPE (1) or XML_IMPL_TYPE (2), got " + typeOfImpl + " instead.");
		}

		return currentImpl;
	}

	/**
	 * Returns the current implementation as precedently instantiated.
	 *
	 * @return The current implementation as precedently instantiated
	 *         28 juin 2005
	 */
	public static IFavoritesManager getCurrentImpl()
	{
		return currentImpl;
	}
}
