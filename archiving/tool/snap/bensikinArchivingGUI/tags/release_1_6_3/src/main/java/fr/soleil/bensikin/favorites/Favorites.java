//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/Favorites.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Favorites.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: Favorites.java,v $
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

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * The model of the "Favorites" menu. As of today, the only sub-menu of this is
 * the contexts sub-menu. This class is a singleton.
 * 
 * @author CLAISSE
 */
public class Favorites {
	private FavoritesContextSubMenu contextSubMenu = null;
	private static Favorites instance = null;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "favorites";

	/**
	 * Default constructor, does nothing.
	 */
	private Favorites() {

	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static Favorites getInstance() {
		if (instance == null) {
			instance = new Favorites();
		}

		return instance;
	}

	/**
	 * Returns a XML representation of the favorites.
	 * 
	 * @return a XML representation of the favorites
	 */
	public String toString() {
		String ret = "";

		ret += new XMLLine(XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		if (this.contextSubMenu != null) {
			ret += this.contextSubMenu.toString();
			ret += GUIUtilities.CRLF;
		}

		ret += new XMLLine(XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

		return ret;
	}

	/**
	 * @return Returns the contextSubMenu.
	 */
	public FavoritesContextSubMenu getContextSubMenu() {
		return contextSubMenu;
	}

	/**
	 * @param contextSubMenu
	 *            The contextSubMenu to set.
	 */
	public void setContextSubMenu(FavoritesContextSubMenu contextSubMenu) {
		this.contextSubMenu = contextSubMenu;
	}
}
