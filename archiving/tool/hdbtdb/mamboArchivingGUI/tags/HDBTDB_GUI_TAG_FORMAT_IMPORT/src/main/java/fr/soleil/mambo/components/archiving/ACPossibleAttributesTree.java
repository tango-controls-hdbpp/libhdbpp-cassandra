// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACPossibleAttributesTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACPossibleAttributesTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ACPossibleAttributesTree.java,v $
// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.archiving;

import fr.soleil.mambo.actions.archiving.listeners.PossibleAttributesTreeSelectionListener;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.ACTreeRenderer;
import fr.soleil.mambo.models.ACTreeModel;

public class ACPossibleAttributesTree extends AttributesTree {

	/**
	 * @param newModel
	 */
	private ACPossibleAttributesTree(ACTreeModel newModel) {
		super(newModel);
		this
				.addTreeSelectionListener(new PossibleAttributesTreeSelectionListener());
		this.setCellRenderer(new ACTreeRenderer());

		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
		this.setToggleClickCount(1);
	}

	private static ACPossibleAttributesTree possibleAttributesTreeInstance = null;

	/**
	 * @param newModel
	 * @return 8 juil. 2005
	 */
	public static ACPossibleAttributesTree getInstance(ACTreeModel newModel) {
		if (possibleAttributesTreeInstance == null) {
			possibleAttributesTreeInstance = new ACPossibleAttributesTree(
					newModel);
		}

		return possibleAttributesTreeInstance;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static ACPossibleAttributesTree getInstance() {
		return possibleAttributesTreeInstance;
	}

}
