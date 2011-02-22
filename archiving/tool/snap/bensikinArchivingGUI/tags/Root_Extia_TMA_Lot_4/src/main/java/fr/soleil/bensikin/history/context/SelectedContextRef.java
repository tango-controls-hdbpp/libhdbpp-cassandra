//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/context/SelectedContextRef.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SelectedContextRef.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: SelectedContextRef.java,v $
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
 * The daughter class of ContextRef for selected contexts.
 * 
 * @author CLAISSE
 */
public class SelectedContextRef extends ContextRef {
	private String TAG_NAME = "selectedContext";

	/**
	 * Same as mother constructor
	 * 
	 * @param _id
	 *            The selected context id
	 */
	public SelectedContextRef(int _id) {
		super(_id);
	}

	/**
	 * Uses the id of <code>selectedContext</code> as the reference.
	 * 
	 * @param selectedContext
	 *            The referenced selected context
	 */
	public SelectedContextRef(Context selectedContext) {
		super(selectedContext.getContextData().getId());
	}

	/**
	 * Returns a XML representation of the selected context reference
	 * 
	 * @return a XML representation of the selected context reference
	 */
	public String toString() {
		return super.toString(TAG_NAME);
	}

}
