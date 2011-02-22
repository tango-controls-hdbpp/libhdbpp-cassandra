// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/options/ReadWriteOptionBook.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ReadWriteOptionBook.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: ReadWriteOptionBook.java,v $
// Revision 1.5 2007/02/01 14:14:09 pierrejoseph
// XmlHelper reorg
//
// Revision 1.4 2006/05/16 12:04:08 ounsy
// minor changes
//
// Revision 1.3 2005/12/15 11:45:02 ounsy
// avoiding a null pointer exception
//
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
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
package fr.soleil.mambo.options;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ReadWriteOptionBook {

	protected Hashtable content;
	protected String tagName;

	public static final String OPTION_TAG = "option";
	public static final String NAME_TAG = "name";
	public static final String VALUE_TAG = "value";

	/**
	 * @param _tagName
	 */
	protected ReadWriteOptionBook(String _tagName) {
		content = new Hashtable();
		this.tagName = _tagName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ret = "";

		XMLLine openingLine = new XMLLine(tagName, XMLLine.OPENING_TAG_CATEGORY);
		XMLLine closingLine = new XMLLine(tagName, XMLLine.CLOSING_TAG_CATEGORY);

		ret += openingLine;
		ret += GUIUtilities.CRLF;
		Enumeration enumeration = content.keys();
		while (enumeration.hasMoreElements()) {
			String nextOptionKey = (String) enumeration.nextElement();
			String nextOptionValue = (String) content.get(nextOptionKey);

			XMLLine nextLine = new XMLLine(OPTION_TAG,
					XMLLine.EMPTY_TAG_CATEGORY);
			nextLine.setAttribute(NAME_TAG, nextOptionKey);
			nextLine.setAttribute(VALUE_TAG, nextOptionValue);

			ret += nextLine.toString();
			ret += GUIUtilities.CRLF;
		}
		ret += closingLine;

		return ret;
	}

	/**
	 * @param key
	 * @param value
	 *            8 juil. 2005
	 */
	public void putOption(String key, String value) {
		content.put(key, value);
	}

	/**
	 * @param key
	 * @return 8 juil. 2005
	 */
	public String getOption(String key) {
		String ret = (String) content.get(key);
		return ret;
	}

	/**
	 * @param options
	 *            5 juil. 2005
	 */
	public void build(Vector options) {
		if (options == null) {
			return;
		}
		Enumeration e = options.elements();
		while (e.hasMoreElements()) {
			Hashtable nextOption = (Hashtable) e.nextElement();
			String nextName = (String) nextOption
					.get(ReadWriteOptionBook.NAME_TAG);
			String nextValue = (String) nextOption
					.get(ReadWriteOptionBook.VALUE_TAG);
			// System.out.println (
			// "Options/loadOptions/nextName/"+nextName+"/nextValue/"+nextValue+"/"
			// );

			this.putOption(nextName, nextValue);
		}

	}

	/**
	 * @return Returns the content.
	 */
	public Hashtable getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(Hashtable content) {
		this.content = content;
	}
}
