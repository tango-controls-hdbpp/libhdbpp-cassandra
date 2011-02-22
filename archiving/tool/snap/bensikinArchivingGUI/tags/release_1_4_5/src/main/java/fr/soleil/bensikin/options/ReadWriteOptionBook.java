//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/ReadWriteOptionBook.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ReadWriteOptionBook.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: ReadWriteOptionBook.java,v $
// Revision 1.6  2007/08/30 14:01:51  pierrejoseph
// * java 1.5 programming
//
// Revision 1.5  2006/06/28 12:53:46  ounsy
// minor changes
//
// Revision 1.4  2005/11/29 18:25:27  chinkumo
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
package fr.soleil.bensikin.options;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;


/**
 * The mother class of all Options "books", that is subsets of the application's Options (eg. Snapshots Options is an option book ).
 * It implements the save/load operations, which are common to all Options books.
 *
 * @author CLAISSE
 */
public class ReadWriteOptionBook
{
	/**
	 * A Hashtable containing the name/value couples representing this book's properties
	 */
	protected Hashtable<String, String> content;

	/**
	 * The XML tag that will be used for this book
	 */
	protected String tagName;

	/**
	 * The XML tag name used for each of the this book's options
	 */
	public static final String XML_TAG = "option";

	/**
	 * The XML tag name used for the "name" property
	 */
	public static final String NAME_TAG = "name";

	/**
	 * The XML tag name used for the "value" property
	 */
	public static final String VALUE_TAG = "value";

	/**
	 * Initializes with an empty property set.
	 *
	 * @param _tagName The XML tag name, different for each type of Options book.
	 */
	protected ReadWriteOptionBook(String _tagName)
	{
		content = new Hashtable<String, String>();
		this.tagName = _tagName;
	}

	/**
	 * Returns a XML representation of the options book
	 *
	 * @return a XML representation of the options book
	 */
	public String toString()
	{
		String ret = "";

		XMLLine openingLine = new XMLLine(tagName , XMLLine.OPENING_TAG_CATEGORY);
		XMLLine closingLine = new XMLLine(tagName , XMLLine.CLOSING_TAG_CATEGORY);

		ret += openingLine;
		ret += GUIUtilities.CRLF;
		Enumeration<String> enumer = content.keys();
		while ( enumer.hasMoreElements() )
		{
			String nextOptionKey = enumer.nextElement();
			String nextOptionValue = content.get(nextOptionKey);

			XMLLine nextLine = new XMLLine(XML_TAG , XMLLine.EMPTY_TAG_CATEGORY);
			nextLine.setAttribute(NAME_TAG , nextOptionKey);
			nextLine.setAttribute(VALUE_TAG , nextOptionValue);

			ret += nextLine.toString();
			ret += GUIUtilities.CRLF;
		}
		ret += closingLine;

		return ret;
	}

	/**
	 * Adds an option to the book
	 *
	 * @param key   The option's name
	 * @param value The option's value
	 */
	public void putOption(String key , String value)
	{
		content.put(key , value);
	}

	/**
	 * Gets an option from the book
	 *
	 * @param key The option's name
	 * @return The option's value
	 */
	public String getOption(String key)
	{
		String ret = content.get(key);
		return ret;
	}

	/**
	 * Builds its content Hashtable, using the given options list.
	 *
	 * @param options The Vector containing all name/value couples. Each couple is a Hashtable of size 2.
	 */
	public void build(Vector<Hashtable<String, String>> options)
	{
		Enumeration<Hashtable<String, String>> e = options.elements();
		while ( e.hasMoreElements() )
		{
			Hashtable<String, String> nextOption = e.nextElement();
			String nextName = nextOption.get(ReadWriteOptionBook.NAME_TAG);
			String nextValue = nextOption.get(ReadWriteOptionBook.VALUE_TAG);

			this.putOption(nextName , nextValue);
		}

	}
}
