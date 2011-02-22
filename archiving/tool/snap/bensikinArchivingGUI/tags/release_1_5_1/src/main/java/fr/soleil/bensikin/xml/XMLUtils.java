//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/xml/XMLUtils.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLUtils.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: XMLUtils.java,v $
// Revision 1.4  2007/08/30 14:01:51  pierrejoseph
// * java 1.5 programming
//
// Revision 1.3  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:42  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.xml;

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

import fr.soleil.bensikin.tools.GUIUtilities;


public class XMLUtils
{
	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

	/**
	 * Returns the XML root node of an XML file.
	 *
	 * @param file The file we want to get the root of
	 * @return The root node
	 * @throws Exception
	 */
	public static Node getRootNode(File file) throws Exception
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// should we add dtd validating?
			factory.setValidating(false);
			factory.setIgnoringComments(true);

			DocumentBuilder builder = factory.newDocumentBuilder();

			//builder.setErrorHandler ( error );
			Document doc = builder.parse(file);

			return doc.getDocumentElement();
		}
		catch ( Exception e )
		{
			throw e;
		}
	}

	/**
	 * Tests whether a Node is really a node in the hierarchical meaning.
	 *
	 * @param nodeToTest The node to test
	 * @return True if the node wasn't a node in the hierarchical meaning
	 */
	public static boolean isAFakeNode(Node nodeToTest)
	{
		int typeNode = nodeToTest.getNodeType();
		return ( typeNode != Node.ELEMENT_NODE );
	}

	/**
	 * Tests whether a Node has real child nodes, eg. child nodes that aren't fake according to isAFakeNode.
	 *
	 * @param nodeToTest The node to test
	 * @return True if the node has real child nodes
	 */
	public static boolean hasRealChildNodes(Node nodeToTest)
	{
		NodeList potentialChilds = nodeToTest.getChildNodes();
		for ( int i = 0 ; i < potentialChilds.getLength() ; i++ )
		{
			Node nextNode = potentialChilds.item(i);
			if ( !isAFakeNode(nextNode) )
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Saves a String content to a file using a BufferedWriter.
	 * Adds an XML file header.
	 *
	 * @param string                    The content to save
	 * @param favoritesResourceLocation The location to save to
	 * @throws Exception
	 */
	public static void save(String string , String favoritesResourceLocation) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(favoritesResourceLocation));

		GUIUtilities.write(bw , XMLUtils.XML_HEADER , true);
		GUIUtilities.write(bw , string , false);

		bw.close();
	}

	/**
	 * Loads the XML attributes of a XML node into a Hashtable
	 *
	 * @param DOMnode The node to load
	 * @return The attributes Hashtable
	 * @throws Exception
	 */
	public static Hashtable<String, String> loadAttributes(Node DOMnode) throws Exception
	{
		Hashtable<String, String> retour = null;

		if ( DOMnode.hasAttributes() )
		{
			retour = new Hashtable<String, String>();
			NamedNodeMap listAtts = DOMnode.getAttributes();

			for ( int i = 0 ; i < listAtts.getLength() ; i++ )
			{
				String nomAtt = listAtts.item(i).getNodeName().trim();
				String valueAtt = listAtts.item(i).getNodeValue().trim();

				retour.put(nomAtt , valueAtt);
			}
		}

		return retour;
	}

	/**
	 * Returns the value of a XML node, ie. the value contained between the opening and the closing tag for this node
	 *
	 * @param DOMnode The node to get the value from
	 * @return The trimmed value
	 */
	public static String getNodeValue(Node DOMnode)
	{
		String ret = null;

		NodeList potentialChilds = DOMnode.getChildNodes();
		for ( int i = 0 ; i < potentialChilds.getLength() ; i++ )
		{
			Node nextNode = potentialChilds.item(i);
			int typeNode = nextNode.getNodeType();

			if ( typeNode == Node.TEXT_NODE )
			{
				ret = nextNode.getNodeValue();
				break;
			}
		}

		if ( ret != null )
		{
			ret = ret.trim();
		}

		return ret;
	}

	/**
	 * @param ret
	 * @return
	 */
	public static String replaceXMLChars(String ret)
	{
		if ( ret == null )
		{
			return ret;
		}

		String[] AMP = {"&", "&amp;"};
		String[] LEFT_ARROW = {"<", "&lt;"};
		String[] RIGHT_ARROW = {">", "&gt;"};
		String[] DOUBLE_QUOTE = {"\"", "&quot;"};
		String[] APOS = {"'", "&apos;"};

		ret = GUIUtilities.replace(ret , AMP[ 0 ] , AMP[ 1 ]);
		ret = GUIUtilities.replace(ret , LEFT_ARROW[ 0 ] , LEFT_ARROW[ 1 ]);
		ret = GUIUtilities.replace(ret , RIGHT_ARROW[ 0 ] , RIGHT_ARROW[ 1 ]);
		ret = GUIUtilities.replace(ret , DOUBLE_QUOTE[ 0 ] , DOUBLE_QUOTE[ 1 ]);
		ret = GUIUtilities.replace(ret , APOS[ 0 ] , APOS[ 1 ]);

		return ret;
	}
}
