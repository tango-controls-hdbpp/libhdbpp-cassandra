//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/manager/XMLHistoryManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLHistoryManager.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: XMLHistoryManager.java,v $
// Revision 1.6  2006/06/28 12:51:18  ounsy
// minor changes
//
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

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.bensikin.history.History;
import fr.soleil.bensikin.history.context.ContextHistory;
import fr.soleil.bensikin.history.context.OpenedContextRef;
import fr.soleil.bensikin.history.context.SelectedContextRef;
import fr.soleil.bensikin.history.snapshot.OpenedSnapshotRef;
import fr.soleil.bensikin.history.snapshot.SelectedSnapshotRef;
import fr.soleil.bensikin.history.snapshot.SnapshotHistory;
import fr.soleil.bensikin.tools.BensikinWarning;
import fr.soleil.bensikin.xml.XMLUtils;


/**
 * An XML implementation. Saves and loads history to/from XML files.
 *
 * @author CLAISSE
 */
public class XMLHistoryManager implements IHistoryManager
{
	/* (non-Javadoc)
	 * @see bensikin.bensikin.history.manager.IHistoryManager#saveHistory(bensikin.bensikin.history.History, java.lang.String)
	 */
	public void saveHistory(History history , String historyResourceLocation) throws Exception
	{
		XMLUtils.save(history.toString() , historyResourceLocation);
	}


	/* (non-Javadoc)
	 * @see bensikin.bensikin.history.manager.IHistoryManager#loadHistory(java.lang.String)
	 */
	public History loadHistory(String historyResourceLocation) throws Exception
	{
		try
		{
			Hashtable historyHt = loadHistoryIntoHash(historyResourceLocation);

			//BEGIN CONTEXTS
			//BEGIN OPEN BOOKS
			Hashtable contextsBook = ( Hashtable ) historyHt.get(History.CONTEXTS_KEY);
			//END OPEN BOOKS

			//BEGIN OPEN CHAPTERS
			Hashtable selectedContextChapter = ( Hashtable ) contextsBook.get(History.SELECTED_CONTEXT_KEY);
			Vector openedContextsChapter = ( Vector ) contextsBook.get(History.OPENED_CONTEXTS_KEY);
			//END OPEN CHAPTERS

			String id_s;
			SelectedContextRef selectedcontext = null;

			if ( selectedContextChapter != null )
			{
				id_s = ( String ) selectedContextChapter.get(History.ID_KEY);
				int id = Integer.parseInt(id_s);
				selectedcontext = new SelectedContextRef(id);
			}
			//BEGIN OPEN PAGES
			OpenedContextRef[] openedContexts = new OpenedContextRef[ openedContextsChapter.size() ];
			Enumeration enumer = openedContextsChapter.elements();
			int i = 0;
			while ( enumer.hasMoreElements() )
			{
				Hashtable nextPage = ( Hashtable ) enumer.nextElement();

				id_s = ( String ) nextPage.get(History.ID_KEY);
				int id = Integer.parseInt(id_s);

				OpenedContextRef openedcontext = new OpenedContextRef(id);
				openedContexts[ i ] = openedcontext;
				i++;
			}
			//END OPEN PAGES
			ContextHistory contextHistory = new ContextHistory(selectedcontext , openedContexts);
			//END CONTEXTS

			//BEGIN SNAPSHOTS
			//BEGIN OPEN BOOKS
			Hashtable snapshotsBook = ( Hashtable ) historyHt.get(History.SNAPSHOTS_KEY);
			//END OPEN BOOKS

			//BEGIN OPEN CHAPTERS
			//Hashtable selectedSnapshotChapter = (Hashtable) snapshotsBook.get ( History.SELECTED_SNAPSHOT_KEY );
			Vector selectedSnapshotsChapter = ( Vector ) snapshotsBook.get(History.SELECTED_SNAPSHOTS_KEY);
			Vector openedSnapshotsChapter = ( Vector ) snapshotsBook.get(History.OPENED_SNAPSHOTS_KEY);
			//END OPEN CHAPTERS

			//BEGIN OPEN PAGES
			OpenedSnapshotRef[] openedSnapshots = new OpenedSnapshotRef[ openedSnapshotsChapter.size() ];
			Enumeration enum1 = openedSnapshotsChapter.elements();
			int i1 = 0;
			while ( enum1.hasMoreElements() )
			{
				Hashtable nextPage = ( Hashtable ) enum1.nextElement();

				id_s = ( String ) nextPage.get(History.ID_KEY);
				int id = Integer.parseInt(id_s);

				OpenedSnapshotRef openedsnapshot = new OpenedSnapshotRef(id);
				openedSnapshots[ i1 ] = openedsnapshot;
				i1++;
			}

			SelectedSnapshotRef[] selectedSnapshots = new SelectedSnapshotRef[ selectedSnapshotsChapter.size() ];
			Enumeration enum2 = selectedSnapshotsChapter.elements();
			int i2 = 0;
			while ( enum2.hasMoreElements() )
			{
				Hashtable nextPage = ( Hashtable ) enum2.nextElement();

				id_s = ( String ) nextPage.get(History.ID_KEY);
				int id = Integer.parseInt(id_s);

				SelectedSnapshotRef selectedsnapshot = new SelectedSnapshotRef(id);
				selectedSnapshots[ i2 ] = selectedsnapshot;
				i2++;
			}
			//END OPEN PAGES
			SnapshotHistory snapshotHistory = new SnapshotHistory(selectedSnapshots , openedSnapshots);
			//END SNAPSHOTS

			return new History(snapshotHistory , contextHistory);

		}
		catch ( Exception e )
		{
			BensikinWarning bensikinWarning = new BensikinWarning(e.getMessage());
			bensikinWarning.setStackTrace(e.getStackTrace());
			throw bensikinWarning;
			//throw e;
		}
	}

	/**
	 * Loads the History into a Hashtable, given the path of the XML file.
	 *
	 * @param path_xml The path of the XML file
	 * @return A Hashtable which keys are History.CONTEXTS_KEY and History.SNAPSHOTS_KEY, and which values
	 *         are respectively Context/Snapshot Hashtables
	 * @throws Exception
	 */
	private static Hashtable loadHistoryIntoHash(String path_xml) throws Exception
	{
		File file = new File(path_xml);
		Node rootNode = XMLUtils.getRootNode(file);

		Hashtable retour = loadHistoryIntoHashFromRoot(rootNode);

		return retour;
	}

	/**
	 * Loads the History into a Hashtable, given the root node of the XML file.
	 *
	 * @param rootNode The DOM root of the XML file
	 * @return A Hashtable which keys are History.CONTEXTS_KEY and History.SNAPSHOTS_KEY, and which values
	 *         are respectively Context/Snapshot Hashtables
	 * @throws Exception
	 */
	private static Hashtable loadHistoryIntoHashFromRoot(Node rootNode) throws Exception
	{
		Hashtable history = null;

		if ( rootNode.hasChildNodes() )
		{
			NodeList bookNodes = rootNode.getChildNodes();
			history = new Hashtable();

			for ( int i = 0 ; i < bookNodes.getLength() ; i++ )
			        //as many loops as there are history parts in the saved file
			        //(which is normally three: contexts, snapshots, and options)
			{
				Node currentBookNode = bookNodes.item(i);

				if ( XMLUtils.isAFakeNode(currentBookNode) )
				{
					continue;
				}

				String currentBookType = currentBookNode.getNodeName().trim();
				Hashtable currentBook;

				if ( History.CONTEXTS_KEY.equals(currentBookType) )
				{
					currentBook = loadContextsBook(currentBookNode);
					history.put(History.CONTEXTS_KEY , currentBook);
				}
				else if ( History.SNAPSHOTS_KEY.equals(currentBookType) )
				{
					currentBook = loadSnapshotsBook(currentBookNode);
					history.put(History.SNAPSHOTS_KEY , currentBook);
				}
				/*else if ( History.OPTIONS_KEY.equals ( currentBookType ) )
				{
				    currentBook = loadOptionsBook ( currentBookNode );
				    history.put ( History.OPTIONS_KEY ,  currentBook );
				}*/
			}
		}
		return history;
	}

	/**
	 * Loads the Contexts history into a Hashtable, given the DOM node of the XML file contexts part.
	 *
	 * @param contextsNode The DOM node of the XML file contexts part
	 * @return A Hashtable which keys are History.SELECTED_CONTEXT_KEY and History.OPENED_CONTEXTS_KEY, and which values
	 *         are respectively selected/opened Contexts Hashtables
	 * @throws Exception
	 */
	private static Hashtable loadContextsBook(Node contextsNode)
	{
		Hashtable book = new Hashtable();

		if ( contextsNode.hasChildNodes() )
		{
			NodeList contextChapterNodes = contextsNode.getChildNodes();

			for ( int i = 0 ; i < contextChapterNodes.getLength() ; i++ )
			        //as many loops as there are contexts types, ie. selected or opened, in the current context
			        //(which is normally three: contexts, snapshots, and options)
			{
				Node currentContextChapterNode = contextChapterNodes.item(i);

				if ( XMLUtils.isAFakeNode(currentContextChapterNode) )
				{
					continue;
				}

				String currentContextChapterType = currentContextChapterNode.getNodeName().trim();
				if ( History.SELECTED_CONTEXT_KEY.equals(currentContextChapterType) )
				{
					Hashtable currentChapter = null;
					try
					{
						currentChapter = XMLUtils.loadAttributes(currentContextChapterNode);
						if ( currentChapter != null )
						{
							book.put(History.SELECTED_CONTEXT_KEY , currentChapter);
						}

					}
					catch ( Exception e )
					{
						e.printStackTrace();
					}

				}
				else if ( History.OPENED_CONTEXTS_KEY.equals(currentContextChapterType) )
				{
					Vector currentChapter = loadOpenedContextsPages(currentContextChapterNode);
					book.put(History.OPENED_CONTEXTS_KEY , currentChapter);
				}
			}
		}

		return book;
	}

	/**
	 * Loads the opened Contexts history into a Vector, given the DOM node of the XML file opened contexts part.
	 *
	 * @param openedContextsNode The DOM node of the XML file opened contexts part
	 * @return A Vector which elements are opened Contexts Hashtables
	 * @throws Exception
	 */
	private static Vector loadOpenedContextsPages(Node openedContextsNode)
	{
		Vector pages = new Vector();

		if ( openedContextsNode.hasChildNodes() )
		{
			NodeList openedContextNodes = openedContextsNode.getChildNodes();

			for ( int i = 0 ; i < openedContextNodes.getLength() ; i++ )
			{
				Node currentOpenedContextNode = openedContextNodes.item(i);

				if ( XMLUtils.isAFakeNode(currentOpenedContextNode) )
				{
					continue;
				}

				String currentOpenedContextType = currentOpenedContextNode.getNodeName().trim();
				Hashtable currentPage = null;
				if ( History.OPENED_CONTEXT_KEY.equals(currentOpenedContextType) )
				{
					try
					{
						currentPage = XMLUtils.loadAttributes(currentOpenedContextNode);
					}
					catch ( Exception e )
					{
						e.printStackTrace();
					}
					pages.add(currentPage);
				}
			}
		}

		return pages;
	}

	/**
	 * Loads the Snapshots history into a Hashtable, given the DOM node of the XML file snapshots part.
	 *
	 * @param snapshotsNode The DOM node of the XML file snapshots part
	 * @return A Hashtable which keys are History.SELECTED_SNAPSHOTS_KEY and History.OPENED_SNAPSHOTS_KEY, and which values
	 *         are respectively selected/opened Snapshots Hashtables
	 * @throws Exception
	 */
	private static Hashtable loadSnapshotsBook(Node snapshotsNode)
	{
		Hashtable book = new Hashtable();

		if ( snapshotsNode.hasChildNodes() )
		{
			NodeList snapshotChapterNodes = snapshotsNode.getChildNodes();

			for ( int i = 0 ; i < snapshotChapterNodes.getLength() ; i++ )
			        //as many loops as there are snapshots types, ie. selected or opened, in the current snapshot
			{
				Node currentSnapshotChapterNode = snapshotChapterNodes.item(i);

				if ( XMLUtils.isAFakeNode(currentSnapshotChapterNode) )
				{
					continue;
				}

				String currentSnapshotChapterType = currentSnapshotChapterNode.getNodeName().trim();

				if ( History.SELECTED_SNAPSHOTS_KEY.equals(currentSnapshotChapterType) )
				{
					Vector currentChapter = loadSelectedSnapshotsPages(currentSnapshotChapterNode);
					book.put(History.SELECTED_SNAPSHOTS_KEY , currentChapter);
				}
				else if ( History.OPENED_SNAPSHOTS_KEY.equals(currentSnapshotChapterType) )
				{
					Vector currentChapter = loadOpenedSnapshotsPages(currentSnapshotChapterNode);
					book.put(History.OPENED_SNAPSHOTS_KEY , currentChapter);
				}
			}
		}

		return book;
	}


	/**
	 * Loads the opened Snapshots history into a Vector, given the DOM node of the XML file opened snapshots part.
	 *
	 * @param openedSnapshotsNode The DOM node of the XML file opened snapshots part
	 * @return A Vector which elements are opened Snapshots Hashtables
	 * @throws Exception
	 */
	private static Vector loadOpenedSnapshotsPages(Node openedSnapshotsNode)
	{
		Vector pages = new Vector();

		if ( openedSnapshotsNode.hasChildNodes() )
		{
			NodeList openedSnapshotNodes = openedSnapshotsNode.getChildNodes();

			for ( int i = 0 ; i < openedSnapshotNodes.getLength() ; i++ )
			{
				Node currentOpenedSnapshotNode = openedSnapshotNodes.item(i);

				if ( XMLUtils.isAFakeNode(currentOpenedSnapshotNode) )
				{
					continue;
				}

				String currentOpenedSnapshotType = currentOpenedSnapshotNode.getNodeName().trim();
				Hashtable currentPage = null;

				if ( History.OPENED_SNAPSHOT_KEY.equals(currentOpenedSnapshotType) )
				{
					try
					{
						currentPage = XMLUtils.loadAttributes(currentOpenedSnapshotNode);
					}
					catch ( Exception e )
					{
						e.printStackTrace();
					}
					pages.add(currentPage);
				}
			}
		}

		return pages;
	}

	/**
	 * Loads the selected Snapshots history into a Vector, given the DOM node of the XML file selected snapshots part.
	 *
	 * @param selectedSnapshotsNode The DOM node of the XML file selected snapshots part
	 * @return A Vector which elements are selected Snapshots Hashtables
	 * @throws Exception
	 */
	private static Vector loadSelectedSnapshotsPages(Node selectedSnapshotsNode)
	{
		Vector pages = new Vector();

		if ( selectedSnapshotsNode.hasChildNodes() )
		{
			NodeList selectedSnapshotNodes = selectedSnapshotsNode.getChildNodes();

			for ( int i = 0 ; i < selectedSnapshotNodes.getLength() ; i++ )
			{
				Node currentSelectedSnapshotNode = selectedSnapshotNodes.item(i);

				if ( XMLUtils.isAFakeNode(currentSelectedSnapshotNode) )
				{
					continue;
				}

				String currentSelectedSnapshotType = currentSelectedSnapshotNode.getNodeName().trim();
				Hashtable currentPage = null;

				if ( History.SELECTED_SNAPSHOT_KEY.equals(currentSelectedSnapshotType) )
				{
					try
					{
						currentPage = XMLUtils.loadAttributes(currentSelectedSnapshotNode);
					}
					catch ( Exception e )
					{
						e.printStackTrace();
					}
					pages.add(currentPage);
				}
			}
		}

		return pages;
	}
}
