//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/DummyFavoritesManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyFavoritesManager.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: DummyFavoritesManager.java,v $
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

import java.util.TreeMap;

import fr.soleil.bensikin.xml.XMLLine;


/**
 * Dummy implementation.
 *
 * @author CLAISSE
 */
public class DummyFavoritesManager implements IFavoritesManager
{

	/* (non-Javadoc)
	 * @see bensikin.bensikin.favorites.IFavoritesManager#saveFavorites(bensikin.bensikin.favorites.Favorites, java.lang.String)
	 */
	public void saveFavorites(Favorites history ,
	                          String favoritesResourceLocation) throws Exception
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.favorites.IFavoritesManager#loadFavorites(java.lang.String)
	 */
	public Favorites loadFavorites(String favoritesResourceLocation) throws Exception
	{
		Favorites favorites = Favorites.getInstance();

		FavoritesContextSubMenu ret = new FavoritesContextSubMenu(FavoritesContextSubMenu.XML_TAG);
		TreeMap treeMap = new TreeMap();

		XMLLine line1 = new XMLLine("context");
		line1.setAttribute("id" , "111111");

		XMLLine line2 = new XMLLine("context");
		line2.setAttribute("id" , "222222");

		XMLLine line3 = new XMLLine("context");
		line3.setAttribute("id" , "333333");

		XMLLine line4 = new XMLLine("context");
		line4.setAttribute("id" , "444444");

		XMLLine line5 = new XMLLine("context");
		line5.setAttribute("id" , "555555");

		XMLLine line6 = new XMLLine("context");
		line6.setAttribute("id" , "666666");

		XMLLine line7 = new XMLLine("context");
		line7.setAttribute("id" , "777777");

		XMLLine line8 = new XMLLine("context");
		line8.setAttribute("id" , "888888");

		XMLLine line9 = new XMLLine("context");
		line9.setAttribute("id" , "999999");

		TreeMap sous_cas_11 = new TreeMap();
		sous_cas_11.put(line1.getId() , line1);
		sous_cas_11.put(line2.getId() , line2);
		sous_cas_11.put(line3.getId() , line3);

		TreeMap sous_cas_12 = new TreeMap();
		sous_cas_12.put(line4.getId() , line4);

		TreeMap sous_cas_13 = new TreeMap();
		sous_cas_13.put(line5.getId() , line5);

		TreeMap cas_1 = new TreeMap();
		cas_1.put("sous_cas_11" , sous_cas_11);
		cas_1.put("sous_cas_12" , sous_cas_12);
		cas_1.put("sous_cas_13" , sous_cas_13);

		TreeMap cas_2 = new TreeMap();
		cas_2.put(line6.getId() , line6);
		cas_2.put(line7.getId() , line7);
		cas_2.put(line8.getId() , line8);

		treeMap.put("cas_1" , cas_1);
		treeMap.put("cas_2" , cas_2);
		treeMap.put(line9.getId() , line9);

		ret.buildMenu(treeMap);
		ret.buildTree(treeMap);

		favorites.setContextSubMenu(ret);

		return favorites;
	}

}
