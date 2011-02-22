// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/xmlhelpers/XMLUtils.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class XMLUtils.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.1 $
//
// $Log: XMLUtils.java,v $
// Revision 1.1 2007/02/01 14:07:17 pierrejoseph
// getAttributesToDedicatedArchiver
//
// Revision 1.5 2006/11/22 10:44:14 ounsy
// corrected a NullPointer bug in loadAttributes()
//
// Revision 1.4 2006/09/22 14:52:23 ounsy
// minor changes
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:28:26 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.tools.xmlhelpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.tools.MamboWarning;

public class XMLUtils {

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 *             8 juil. 2005
	 */
	public static Node getRootNode(File file) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			// should we add dtd validating?
			factory.setValidating(false);
			factory.setIgnoringComments(true);

			DocumentBuilder builder = factory.newDocumentBuilder();

			// builder.setErrorHandler ( error );
			Document doc = builder.parse(file);

			return doc.getDocumentElement();
		} catch (Exception e) {
			MamboWarning mamboWarning = new MamboWarning(e.getMessage());
			mamboWarning.setStackTrace(e.getStackTrace());
			throw mamboWarning;
			// throw e;
		}
	}

	/**
	 * @param noeudATester
	 * @return 8 juil. 2005
	 */
	public static boolean isAFakeNode(Node noeudATester) {
		int typeNode = noeudATester.getNodeType();

		return (typeNode != Node.ELEMENT_NODE);

	}

	/**
	 * @param nodeToTest
	 * @return 8 juil. 2005
	 */
	public static boolean hasRealChildNodes(Node nodeToTest) {
		boolean ret = false;

		NodeList potentialChilds = nodeToTest.getChildNodes();
		for (int i = 0; i < potentialChilds.getLength(); i++) {
			Node nextNode = potentialChilds.item(i);
			if (!isAFakeNode(nextNode)) {
				return true;
			}
		}

		return ret;
	}

	/**
	 * @param string
	 * @param favoritesResourceLocation
	 *            12 juil. 2005
	 * @throws Exception
	 */
	public static void save(String string, String favoritesResourceLocation)
			throws Exception {
		// System.out.println("XMLUtils/save/favoritesResourceLocation|"+favoritesResourceLocation+"|");
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				favoritesResourceLocation));

		XMLUtils.write(bw, XMLUtils.XML_HEADER, true);
		XMLUtils.write(bw, string, false);

		bw.close();
	}

	/**
	 * @param noeudDOM
	 * @return
	 * @throws Exception
	 *             8 juil. 2005
	 */
	public static Hashtable<String, String> loadAttributes(Node DOMnode)
			throws Exception {
		Hashtable<String, String> retour = new Hashtable<String, String>();
		if (DOMnode.hasAttributes()) {
			NamedNodeMap listAtts = DOMnode.getAttributes();

			for (int i = 0; i < listAtts.getLength(); i++) {
				String nomAtt = listAtts.item(i).getNodeName().trim();
				String valueAtt = listAtts.item(i).getNodeValue().trim();
				retour.put(nomAtt, valueAtt);
			}
		}

		return retour;
	}

	/**
	 * @param ret
	 * @return
	 */
	public static String replaceXMLChars(String ret) {
		if (ret == null) {
			return ret;
		}

		String[] AMP = { "&", "&amp;" };
		String[] LEFT_ARROW = { "<", "&lt;" };
		String[] RIGHT_ARROW = { ">", "&gt;" };
		String[] DOUBLE_QUOTE = { "\"", "&quot;" };
		String[] APOS = { "'", "&apos;" };

		ret = GUIUtilities.replace(ret, AMP[0], AMP[1]);
		ret = GUIUtilities.replace(ret, LEFT_ARROW[0], LEFT_ARROW[1]);
		ret = GUIUtilities.replace(ret, RIGHT_ARROW[0], RIGHT_ARROW[1]);
		ret = GUIUtilities.replace(ret, DOUBLE_QUOTE[0], DOUBLE_QUOTE[1]);
		ret = GUIUtilities.replace(ret, APOS[0], APOS[1]);

		return ret;
	}

	/**
	 * @param bw
	 * @param s
	 * @param hasNewLine
	 * @throws Exception
	 *             8 juil. 2005
	 */
	public static void write(BufferedWriter bw, String s, boolean hasNewLine)
			throws Exception {
		bw.write(s, 0, s.length());
		if (hasNewLine) {
			bw.newLine();
		}
	}
}
