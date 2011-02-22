//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/context/OpenedContextRef.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenedContextRef.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: OpenedContextRef.java,v $
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
package fr.soleil.bensikin.history.context;

import fr.soleil.bensikin.data.context.Context;

/**
 * The daughter class of ContextRef for opened contexts.
 * 
 * @author CLAISSE
 */
public class OpenedContextRef extends ContextRef {
	private String TAG_NAME = "openedContext";

	/**
	 * Same as mother constructor
	 * 
	 * @param _id
	 *            The opened context id
	 */
	public OpenedContextRef(int _id) {
		super(_id);
	}

	/**
	 * Uses the id of <code>context</code> as the reference.
	 * 
	 * @param context
	 *            The referenced opened context
	 */
	public OpenedContextRef(Context context) {
		super(context.getContextData().getId());
	}

	/**
	 * Returns a XML representation of the opened context reference
	 * 
	 * @return a XML representation of the opened context reference
	 */
	public String toString() {
		return super.toString(TAG_NAME);
	}
}
