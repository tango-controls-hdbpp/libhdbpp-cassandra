//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/IFavoritesManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IFavoritesManager.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: IFavoritesManager.java,v $
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
 * Defines save/load operations to a file representation of a Favorites
 *
 * @author CLAISSE
 */
public interface IFavoritesManager
{

	/**
	 * Saves a Favorites to the desired location.
	 *
	 * @param favorites                 The Favorites object to save
	 * @param favoritesResourceLocation The save location
	 * @throws Exception
	 */
	public void saveFavorites(Favorites favorites , String favoritesResourceLocation) throws Exception;

	/**
	 * Loads a Favorites from the desired location.
	 *
	 * @param favoritesResourceLocation The load location
	 * @return The loaded Favorites
	 * @throws Exception
	 */
	public Favorites loadFavorites(String favoritesResourceLocation) throws Exception;
}
