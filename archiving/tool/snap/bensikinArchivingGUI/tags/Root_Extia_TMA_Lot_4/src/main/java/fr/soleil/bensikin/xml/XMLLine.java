//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/xml/XMLLine.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLLine.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: XMLLine.java,v $
// Revision 1.4  2006/03/21 11:25:50  ounsy
// added a getItemName method
//
// Revision 1.3  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:42  chinkumo
// First commit
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.xml;

import java.util.Enumeration;
import java.util.Hashtable;

import fr.soleil.bensikin.history.History;

/**
 * An object representation of an XML line. The line can either be an opening
 * line <tag>, a closing line </tag>, or an empty line <tag />. The XML
 * attributes can be set one by one, then a call to toString gives the String
 * representation.
 * 
 * @author CLAISSE
 */
public class XMLLine {
	private String name = null;
	private Hashtable attributes = null;
	private int tagCategory = 0;

	private static final String OPEN_TAG = "<";
	private static final String CLOSE_TAG = ">";
	private static final String EMPTY_TAG = " /";
	private static final String SPACE = " ";
	private static final String EQUALS = "=";
	private static final String QUOTE = "\"";
	private static final String CLOSING_TAG = "</";

	public static final int EMPTY_TAG_CATEGORY = 0;
	public static final int OPENING_TAG_CATEGORY = 1;
	public static final int CLOSING_TAG_CATEGORY = 2;

	/**
	 * Not used
	 */
	private XMLLine() {

	}

	/**
	 * Builds a XMLLine with this name. By default the line is an empty line.
	 * 
	 * @param _name
	 *            The tag name
	 */
	public XMLLine(String _name) {
		name = _name;
		attributes = new Hashtable();
		tagCategory = EMPTY_TAG_CATEGORY;
	}

	/**
	 * Builds a XMLLine with this name.
	 * 
	 * @param _name
	 *            The tag name
	 * @param _tagCategory
	 *            The line category
	 * @throws IllegalArgumentException
	 *             If _tagCategory is not in (EMPTY_TAG_CATEGORY,
	 *             OPENING_TAG_CATEGORY, CLOSING_TAG_CATEGORY)
	 */
	public XMLLine(String _name, int _tagCategory)
			throws IllegalArgumentException {
		name = _name;
		attributes = new Hashtable();
		tagCategory = _tagCategory;

		if (_tagCategory != EMPTY_TAG_CATEGORY
				&& _tagCategory != OPENING_TAG_CATEGORY
				&& _tagCategory != CLOSING_TAG_CATEGORY) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Builds a XMLLine with this name, and pre-filled attributes. By default
	 * the line is an empty line.
	 * 
	 * @param _name
	 * @param _attributes
	 */
	public XMLLine(String _name, Hashtable _attributes) {
		name = _name;
		attributes = _attributes;
		tagCategory = EMPTY_TAG_CATEGORY;
	}

	/**
	 * Adds an XML attribute to the XML line.
	 * 
	 * @param key
	 *            The attribute name
	 * @param value
	 *            The attribute value
	 */
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	/**
	 * Returns the line attribute with the name key, null if it doesn't exist.
	 * 
	 * @param key
	 *            The attribute's name
	 * @return The attribute's value
	 */
	public String getAttribute(String key) {
		if (attributes == null) {
			return null;
		}

		return (String) attributes.get(key);
	}

	/**
	 * Calls setAttribute ( History.ID_KEY , id )
	 * 
	 * @param id
	 *            The id value
	 */
	public void setId(String id) {
		setAttribute(History.ID_KEY, id);
	}

	/**
	 * Calls getAttribute ( History.ID_KEY )
	 * 
	 * @return The id value
	 */
	public String getId() {
		return getAttribute(History.ID_KEY);
	}

	/**
	 * Calls setAttribute ( History.LABEL_KEY , id )
	 * 
	 * @param id
	 *            The id value
	 */
	public void setLabel(String id) {
		setAttribute(History.LABEL_KEY, id);
	}

	/**
	 * Calls getAttribute ( History.LABEL_KEY )
	 * 
	 * @return The id value
	 */
	public String getLabel() {
		return getAttribute(History.LABEL_KEY);
	}

	public String getItemName() {
		String id = this.getId();
		String label = this.getLabel();

		String itemName;
		if (label != null && !label.trim().equals("")) {
			itemName = label + " (" + id + ")";
		} else {
			itemName = id;
		}

		return itemName;
	}

	/**
	 * Returns a XML representation of the line
	 * 
	 * @return a XML representation of the line
	 */
	public String toString() {
		String retour = null;
		switch (tagCategory) {
		case CLOSING_TAG_CATEGORY:
			retour = CLOSING_TAG + name + CLOSE_TAG;
			break;

		case OPENING_TAG_CATEGORY:
			retour = OPEN_TAG + name;

			if (attributes != null) {
				Enumeration e = attributes.keys();
				while (e.hasMoreElements()) {
					String clef = (String) e.nextElement();
					String valeur = (String) attributes.get(clef);

					retour += SPACE;
					retour += clef;
					retour += EQUALS;
					retour += QUOTE;
					retour += XMLUtils.replaceXMLChars(valeur);
					retour += QUOTE;
				}
			}

			retour += CLOSE_TAG;
			break;

		case EMPTY_TAG_CATEGORY:
			retour = OPEN_TAG + name;

			if (attributes != null) {
				Enumeration e = attributes.keys();
				while (e.hasMoreElements()) {
					String clef = (String) e.nextElement();
					String valeur = (String) attributes.get(clef);

					retour += SPACE;
					retour += clef;
					retour += EQUALS;
					retour += QUOTE;
					retour += XMLUtils.replaceXMLChars(valeur);
					retour += QUOTE;
				}
			}

			retour += EMPTY_TAG;
			retour += CLOSE_TAG;
			break;

		default:
			// dsdsd
			break;
		}

		return retour;
	}

	/**
	 * Returns the name of the line tag.
	 * 
	 * @return The name of the line tag
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the attributes Hashtable.
	 * 
	 * @return The attributes Hashtable
	 */
	public Hashtable getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes Hashtable.
	 * 
	 * @param attributes
	 *            The new value
	 */
	public void setAttributes(Hashtable attributes) {
		this.attributes = attributes;
	}
}
