//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/ObjectRef.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ObjectRef.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: ObjectRef.java,v $
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
package fr.soleil.bensikin.history;

import java.util.Hashtable;

import fr.soleil.bensikin.xml.XMLLine;

/**
 * A genereric object used to save/load to history. The field used to reference
 * the genereric object is its id.
 * 
 * @author CLAISSE
 */
public class ObjectRef {
	/**
	 * The field used as reference to the favorite object. Coincides with the
	 * object's DB id.
	 */
	protected int id = -1;

	/**
	 * Fills the id field
	 * 
	 * @param _id
	 *            The id field value
	 */
	protected ObjectRef(int _id) {
		this.id = _id;
	}

	/**
	 * Returns a XML representation of the object reference (the id)
	 * 
	 * @param tagName
	 *            The XML tag to use
	 * @return A XML representation of the object reference
	 */
	protected String toString(String tagName) {
		Hashtable attributes = new Hashtable();

		if (id != -1) {
			attributes.put(History.ID_KEY, String.valueOf(this.id));
		}

		XMLLine ret = new XMLLine(tagName, attributes);
		return ret.toString();
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
}
