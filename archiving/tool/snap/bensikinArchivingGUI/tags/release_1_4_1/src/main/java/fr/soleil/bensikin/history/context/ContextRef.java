//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/context/ContextRef.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextRef.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: ContextRef.java,v $
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

import fr.soleil.bensikin.history.ObjectRef;

/**
 * The daughter class of ObjectRef for context.
 * Generic for selected or opened contexts.
 *
 * @author CLAISSE
 */
public class ContextRef extends ObjectRef
{
	/**
	 * Same as mother constructor
	 *
	 * @param _id The context id
	 */
	protected ContextRef(int _id)
	{
		super(_id);
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.history.ObjectRef#toString(java.lang.String)
	 */
	protected String toString(String tagName)
	{
		return super.toString(tagName);
	}
}
