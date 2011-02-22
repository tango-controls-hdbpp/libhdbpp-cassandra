//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/detail/PossibleAttributesTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PossibleAttributesTree.
//						(Claisse Laurent) - 23 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: PossibleAttributesTree.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
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

import javax.swing.tree.TreeModel;

import fr.soleil.bensikin.models.listeners.PossibleAttributesTreeSelectionListener;


/**
 * A singleton class representing the Tango attributes that it is possible to add to the current context.
 *
 * @author CLAISSE
 */
public class PossibleAttributesTree extends AttributesTree
{
	private static PossibleAttributesTree possibleAttributesTreeInstance = null;

	/**
	 * @param newModel The tree's model
	 */
	private PossibleAttributesTree(TreeModel newModel)
	{
		super(newModel);
		this.addTreeSelectionListener(new PossibleAttributesTreeSelectionListener());
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @param newModel
	 * @return The instance
	 */
	public static PossibleAttributesTree getInstance(TreeModel newModel)
	{
		if ( possibleAttributesTreeInstance == null )
		{
			possibleAttributesTreeInstance = new PossibleAttributesTree(newModel);
		}

		return possibleAttributesTreeInstance;
	}

	/**
	 * Used to get the already instantiated singleton
	 *
	 * @return The instance
	 */
	public static PossibleAttributesTree getInstance()
	{
		return possibleAttributesTreeInstance;
	}
}
