//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/manager/XMLOptionsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLOptionsManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: XMLOptionsManager.java,v $
// Revision 1.5  2007/02/01 14:14:09  pierrejoseph
// XmlHelper reorg
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2005/12/15 11:45:39  ounsy
// "copy table to clipboard" management
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options.manager;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.options.sub.DisplayOptions;
import fr.soleil.mambo.options.sub.GeneralOptions;
import fr.soleil.mambo.options.sub.LogsOptions;
import fr.soleil.mambo.options.sub.PrintOptions;
import fr.soleil.mambo.options.sub.SaveOptions;
import fr.soleil.mambo.options.sub.VCOptions;
import fr.soleil.mambo.options.sub.WordlistOptions;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;

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
		Options ret = Options.getInstance();

		// optionsResourceLocation += "_sav"; //to drop
		Hashtable optionsHt = loadOptionsIntoHash(optionsResourceLocation);

		// START BUILDING SUB OPTIONS
		Vector options;

		options = (Vector) optionsHt.get(DisplayOptions.KEY);
		DisplayOptions displayOptions = new DisplayOptions();
		displayOptions.build(options);

		options = (Vector) optionsHt.get(LogsOptions.KEY);
		LogsOptions logsOptions = new LogsOptions();
		logsOptions.build(options);

		options = (Vector) optionsHt.get(PrintOptions.KEY);
		PrintOptions printOptions = new PrintOptions();
		printOptions.build(options);

		options = (Vector) optionsHt.get(SaveOptions.KEY);
		SaveOptions saveOptions = new SaveOptions();
		saveOptions.build(options);

		options = (Vector) optionsHt.get(WordlistOptions.KEY);
		WordlistOptions wordlistOptions = new WordlistOptions();
		wordlistOptions.build(options);

		options = (Vector) optionsHt.get(GeneralOptions.KEY);
		GeneralOptions generalOptions = new GeneralOptions();
		generalOptions.build(options);

		options = (Vector) optionsHt.get(ACOptions.KEY);
		ACOptions acOptions = new ACOptions();
		if (options != null) {
			acOptions.build(options);
		}

		options = (Vector) optionsHt.get(VCOptions.KEY);
		VCOptions vcOptions = new VCOptions();
		if (options != null) {
			vcOptions.build(options);
		}

		// END BUILDING SUB OPTIONS

		// START BUILDING OPTIONS
		ret.setDisplayOptions(displayOptions);
		ret.setLogsOptions(logsOptions);
		ret.setPrintOptions(printOptions);
		ret.setSaveOptions(saveOptions);
		ret.setWordlistOptions(wordlistOptions);
		ret.setGeneralOptions(generalOptions);
		ret.setAcOptions(acOptions);
		ret.setVcOptions(vcOptions);
		// END BUILDING OPTIONS

		return ret;

	}

	/**
	 * @param optionsResourceLocation
	 * @return
	 * @throws Exception
	 *             8 juil. 2005
	 */
	private Hashtable loadOptionsIntoHash(String optionsResourceLocation)
			throws Exception {
		File file = new File(optionsResourceLocation);
		Node rootNode = XMLUtils.getRootNode(file);

		Hashtable retour = loadOptionsIntoHashFromRoot(rootNode);

		return retour;
	}

	/**
	 * @param rootNode
	 * @return 5 juil. 2005
	 */
	private Hashtable loadOptionsIntoHashFromRoot(Node rootNode) {
		Hashtable options = null;

		if (rootNode.hasChildNodes()) {
			NodeList bookNodes = rootNode.getChildNodes();
			options = new Hashtable();

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
				// System.out.println (
				// "loadOptionsIntoHashFromRoot/currentBookType/"+currentBookType+"/"
				// );
				Vector currentBook = loadOptionBook(currentBookNode);
				options.put(currentBookType, currentBook);
			}
		}
		return options;
	}

	/**
	 * @param currentBookNode
	 * @return 5 juil. 2005
	 */
	private Vector loadOptionBook(Node currentBookNode) {
		Vector book = new Vector();

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
				// currentOptionChapterNode.getNodeName ().trim ();
				// System.out.println (
				// "loadOptionBook/currentOptionChapterType/"+currentOptionChapterType+"/"
				// );

				Hashtable currentOptionChapter = null;
				try {
					currentOptionChapter = XMLUtils
							.loadAttributes(currentOptionChapterNode);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// book.put ( currentOptionChapterType , currentOptionChapter );
				book.add(currentOptionChapter);
				// XMLOption option = new XMLOption ( );

			}
		}

		return book;
	}

}
