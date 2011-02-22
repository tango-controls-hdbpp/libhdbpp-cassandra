//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/XMLFavoritesManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLFavoritesManager.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: XMLFavoritesManager.java,v $
// Revision 1.6  2006/03/27 14:04:34  ounsy
// favorites contexts now have a label
//
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.TreeMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.bensikin.xml.XMLUtils;


/**
 * An XML implementation.
 *
 * @author CLAISSE
 */
public class XMLFavoritesManager implements IFavoritesManager
{
	/* (non-Javadoc)
	 * @see bensikin.bensikin.favorites.IFavoritesManager#saveFavorites(bensikin.bensikin.favorites.Favorites, java.lang.String)
	 */
	public void saveFavorites(Favorites history , String favoritesResourceLocation) throws Exception
	{
		XMLUtils.save(history.toString() , favoritesResourceLocation);
	}


	/**
	 * Loads a Favorites given its file location.
	 *
	 * @param favoritesResourceLocation The complete path to the favorites file to load
	 * @return The Favorites object built from its XML representation
	 * @throws Exception
	 */
	public Favorites loadFavorites(String favoritesResourceLocation) throws Exception
	{
		Hashtable favoritesHt = loadFavoritesIntoHash(favoritesResourceLocation);

		//BEGIN OPEN BOOKS
		FavoritesContextSubMenu contextSubMenu = new FavoritesContextSubMenu(FavoritesContextSubMenu.XML_TAG);
		if ( favoritesHt != null )
		{
			TreeMap contextsBook = ( TreeMap ) favoritesHt.get(FavoritesContextSubMenu.XML_TAG);
			contextSubMenu.build(contextsBook);
		}
		else
		{
			contextSubMenu.build(null);
		}
		//END OPEN BOOKS

		Favorites favorites = Favorites.getInstance();
		favorites.setContextSubMenu(contextSubMenu);

		return favorites;
	}


	/**
	 * Loads a Favorites into a Hashtable given its file location.
	 *
	 * @param location The complete path to the favorites file to load
	 * @return The Hashtable built from its XML representation
	 * @throws Exception
	 */
	private Hashtable loadFavoritesIntoHash(String location) throws Exception
	{
		File file = null;
		try
		{
			file = new File(location);
			if ( file == null || !file.canRead() )
			{
				throw new FileNotFoundException();
			}
		}
		catch ( FileNotFoundException e )
		{
			//if there is no favorites file yet,we return null to have an empty menu
			return null;
		}

		Node rootNode = XMLUtils.getRootNode(file);

		Hashtable ret = loadFavoritesIntoHashFromRoot(rootNode);

		return ret;
	}

	/**
	 * Loads a Favorites given its XML root node.
	 *
	 * @param rootNode The file's root node
	 * @return The Context Hashtable built from its XML representation
	 * @throws Exception
	 */
	private Hashtable loadFavoritesIntoHashFromRoot(Node rootNode) throws Exception
	{
		try
		{
			Hashtable favorites = null;
			if ( rootNode.hasChildNodes() )
			{
				NodeList bookNodes = rootNode.getChildNodes();
				favorites = new Hashtable();

				for ( int i = 0 ; i < bookNodes.getLength() ; i++ )
				        //as many loops as there are favorites parts in the saved file
				        //(which is normally three: contexts, snapshots, and options)
				{
					Node currentBookNode = bookNodes.item(i);

					if ( XMLUtils.isAFakeNode(currentBookNode) )
					{
						continue;
					}

					String currentBookType = currentBookNode.getNodeName().trim();
					TreeMap currentBook;
					if ( FavoritesContextSubMenu.XML_TAG.equals(currentBookType) )
					{
						currentBook = loadContextsBook(currentBookNode);
						favorites.put(FavoritesContextSubMenu.XML_TAG , currentBook);
					}
				}
			}
			return favorites;
		}
		catch ( Exception e )
		{
			throw e;
		}
	}


	/**
	 * Loads the context part of the favorites
	 *
	 * @param currentBookNode The context node
	 * @return A TreeMap containing the context part of the favorites
	 * @throws Exception
	 */
	private TreeMap loadContextsBook(Node currentBookNode) throws Exception
	{
		TreeMap ret = new TreeMap();
		ret = getChapter(ret , currentBookNode);

		return ret;
	}

	/**
	 * Loads recursively the favorite contexts nodes into a TreeMap.
	 *
	 * @param mapIn              The current step of TreeMap building
	 * @param currentChapterNode The current node
	 * @return A TreeMap containing the favorites structure for the current book
	 */
	private TreeMap getChapter(TreeMap mapIn , Node currentChapterNode) throws Exception
	{
		NodeList bookNodes = currentChapterNode.getChildNodes();
		for ( int i = 0 ; i < bookNodes.getLength() ; i++ )
		{
			Node nextNode = bookNodes.item(i);
			if ( XMLUtils.isAFakeNode(nextNode) )
			{
				continue;
			}

			String nextNodeName = nextNode.getNodeName();

			if ( XMLUtils.hasRealChildNodes(nextNode) )
			{
				TreeMap next = getChapter(new TreeMap() , nextNode);
				mapIn.put(nextNodeName , next);
			}
			else
			{
				Hashtable attributes = XMLUtils.loadAttributes(nextNode);

				if ( attributes == null )
				{
					mapIn.put(nextNodeName , new TreeMap());
				}
				else
				{
					XMLLine line = new XMLLine(nextNodeName , XMLLine.EMPTY_TAG_CATEGORY);
					line.setAttributes(attributes);
					//WARNING!!	
					//mapIn.put(line.getId() , line);
					mapIn.put(line.getItemName() , line);
				}
			}
		}

		return mapIn;
	}
}
