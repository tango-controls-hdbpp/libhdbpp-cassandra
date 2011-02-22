//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/detail/ContextAttributesTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextAttributesTree.
//						(Claisse Laurent) - 13 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: ContextAttributesTree.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.context.detail;

import fr.soleil.bensikin.models.ContextAttributesTreeModel;

/**
 * A singleton class representing the attributes of the current context.
 * 
 * @author CLAISSE
 */
public class ContextAttributesTree extends AttributesTree {
	private static ContextAttributesTree contextAttributesTreeInstance = null;

	/**
	 * @param newModel
	 *            The tree's model
	 */
	private ContextAttributesTree(ContextAttributesTreeModel newModel) {
		super(newModel);
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @param newModel
	 * @return The instance
	 */
	public static ContextAttributesTree getInstance(
			ContextAttributesTreeModel newModel) {
		if (contextAttributesTreeInstance == null) {
			contextAttributesTreeInstance = new ContextAttributesTree(newModel);
			newModel.setTree(contextAttributesTreeInstance);
		}

		return contextAttributesTreeInstance;
	}

	/**
	 * Used to get the already instantiated singleton
	 * 
	 * @return The instance
	 */
	public static ContextAttributesTree getInstance() {
		return contextAttributesTreeInstance;
	}
}
