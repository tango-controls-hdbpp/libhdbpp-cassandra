//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/manager/XMLOptionsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLOptionsManager.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.7 $
//
// $Log: XMLOptionsManager.java,v $
// Revision 1.7  2007/08/30 14:01:51  pierrejoseph
// * java 1.5 programming
//
// Revision 1.6  2006/06/28 12:53:46  ounsy
// minor changes
//
// Revision 1.5  2005/12/14 16:46:46  ounsy
// added methods necessary for alternate attribute selection
//
// Revision 1.4  2005/11/29 18:25:08  chinkumo
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
package fr.soleil.bensikin.options.manager;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.ContextOptions;
import fr.soleil.bensikin.options.sub.DisplayOptions;
import fr.soleil.bensikin.options.sub.LogsOptions;
import fr.soleil.bensikin.options.sub.PrintOptions;
import fr.soleil.bensikin.options.sub.SaveOptions;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.options.sub.WordlistOptions;
import fr.soleil.bensikin.tools.BensikinWarning;
import fr.soleil.bensikin.xml.XMLUtils;

/**
 * An XML implementation.
 * 
 * @author CLAISSE
 */
public class XMLOptionsManager implements IOptionsManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.options.manager.IOptionsManager#saveOptions(bensikin
	 * .bensikin.options.Options, java.lang.String)
	 */
	public void saveOptions(Options options, String optionsResourceLocation)
			throws Exception {
		XMLUtils.save(options.toString(), optionsResourceLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.options.manager.IOptionsManager#loadOptions(java.lang
	 * .String)
	 */
	public Options loadOptions(String optionsResourceLocation) throws Exception {
		try {
			Options ret = Options.getInstance();

			Hashtable<String, Vector<Hashtable<String, String>>> optionsHt = loadOptionsIntoHash(optionsResourceLocation);

			// START BUILDING SUB OPTIONS
			Vector<Hashtable<String, String>> options;

			options = optionsHt.get(DisplayOptions.XML_TAG);
			DisplayOptions displayOptions = new DisplayOptions();
			displayOptions.build(options);

			options = optionsHt.get(LogsOptions.XML_TAG);
			LogsOptions logsOptions = new LogsOptions();
			logsOptions.build(options);

			options = optionsHt.get(PrintOptions.XML_TAG);
			PrintOptions printOptions = new PrintOptions();
			printOptions.build(options);

			options = optionsHt.get(SaveOptions.XML_TAG);
			SaveOptions saveOptions = new SaveOptions();
			saveOptions.build(options);

			options = optionsHt.get(WordlistOptions.XML_TAG);
			WordlistOptions wordlistOptions = new WordlistOptions();
			wordlistOptions.build(options);

			options = optionsHt.get(SnapshotOptions.XML_TAG);
			SnapshotOptions snapshotOptions = new SnapshotOptions();
			snapshotOptions.build(options);

			options = optionsHt.get(ContextOptions.XML_TAG);
			ContextOptions contextOptions = new ContextOptions();
			contextOptions.build(options);

			// END BUILDING SUB OPTIONS

			// START BUILDING OPTIONS
			ret.setDisplayOptions(displayOptions);
			ret.setLogsOptions(logsOptions);
			ret.setPrintOptions(printOptions);
			ret.setSaveOptions(saveOptions);
			ret.setWordlistOptions(wordlistOptions);
			ret.seSnapshotOptions(snapshotOptions);
			ret.setContextOptions(contextOptions);
			// END BUILDING OPTIONS

			return ret;
		} catch (Exception e) {
			BensikinWarning bensikinWarning = new BensikinWarning(e
					.getMessage());
			bensikinWarning.setStackTrace(e.getStackTrace());
			throw bensikinWarning;
		}
	}

	/**
	 * Loads the History into a Options, given the path of the XML file.
	 * 
	 * @param optionsResourceLocation
	 *            The path of the XML file
	 * @return A Hashtable which keys are each of the options books' XML_TAG and
	 *         which values are those subclasses of ReadWriteOptionBook
	 * @throws Exception
	 */
	private Hashtable<String, Vector<Hashtable<String, String>>> loadOptionsIntoHash(
			String optionsResourceLocation) throws Exception {
		File file = new File(optionsResourceLocation);
		Node rootNode = XMLUtils.getRootNode(file);

		Hashtable<String, Vector<Hashtable<String, String>>> retour = loadOptionsIntoHashFromRoot(rootNode);

		return retour;
	}

	/**
	 * Loads the Options into a Hashtable, given the root node of the XML file.
	 * 
	 * @param rootNode
	 *            The DOM root of the XML file
	 * @return A Hashtable which keys are each of the options books' XML_TAG and
	 *         which values are those subclasses of ReadWriteOptionBook
	 * @throws Exception
	 */
	private Hashtable<String, Vector<Hashtable<String, String>>> loadOptionsIntoHashFromRoot(
			Node rootNode) {
		Hashtable<String, Vector<Hashtable<String, String>>> options = null;

		if (rootNode.hasChildNodes()) {
			NodeList bookNodes = rootNode.getChildNodes();
			options = new Hashtable<String, Vector<Hashtable<String, String>>>();

			for (int i = 0; i < bookNodes.getLength(); i++)
			// as many loops as there are options sub-blocks in the saved file
			// (which is normally five: display, print, logs, save ,and
			// wordlist)
			{
				Node currentBookNode = bookNodes.item(i);

				if (XMLUtils.isAFakeNode(currentBookNode)) {
					continue;
				}

				String currentBookType = currentBookNode.getNodeName().trim();
				Vector<Hashtable<String, String>> currentBook = loadOptionBook(currentBookNode);
				options.put(currentBookType, currentBook);
			}
		}

		return options;
	}

	/**
	 * Loads the list of options for the current options book node.
	 * 
	 * @param currentBookNode
	 *            The current book node, representing a particular option book's
	 *            node (eg. a LogsOptions.XML_TAG="logs" node)
	 * @return A Vector containing one Hashtable for each options book
	 */
	private Vector<Hashtable<String, String>> loadOptionBook(
			Node currentBookNode) {
		Vector<Hashtable<String, String>> book = new Vector<Hashtable<String, String>>();

		if (currentBookNode.hasChildNodes()) {
			NodeList contextChapterNodes = currentBookNode.getChildNodes();

			for (int i = 0; i < contextChapterNodes.getLength(); i++)
			// as many loops as there are options in the current options
			// sub-block
			{
				Node currentOptionChapterNode = contextChapterNodes.item(i);

				if (XMLUtils.isAFakeNode(currentOptionChapterNode)) {
					continue;
				}

				// String currentOptionChapterType =
				// currentOptionChapterNode.getNodeName().trim();

				Hashtable<String, String> currentOptionChapter = null;
				try {
					currentOptionChapter = XMLUtils
							.loadAttributes(currentOptionChapterNode);
				} catch (Exception e) {
					e.printStackTrace();
				}

				book.add(currentOptionChapter);
			}
		}

		return book;
	}
}
