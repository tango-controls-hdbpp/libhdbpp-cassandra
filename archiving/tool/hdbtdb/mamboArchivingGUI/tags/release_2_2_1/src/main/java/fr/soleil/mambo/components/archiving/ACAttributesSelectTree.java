// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributesSelectTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACAttributesSelectTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ACAttributesSelectTree.java,v $
// Revision 1.4 2006/08/23 10:02:00 ounsy
// getInstance(treemodel) allways updates model
//
// Revision 1.3 2006/07/18 10:23:39 ounsy
// Less time consuming by setting tree expanding on demand only
//
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

import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.ACTreeRenderer;
import fr.soleil.mambo.models.ACTreeModel;

public class ACAttributesSelectTree extends AttributesTree {

	/**
	 * @param newModel
	 */
	private ACAttributesSelectTree(ACTreeModel newModel) {
		super(newModel);
		// this.addTreeSelectionListener ( new ACAttributesTreeSelectionListener
		// () );
		this.setCellRenderer(new ACTreeRenderer());

		this.setExpandsSelectedPaths(true);
		this.setScrollsOnExpand(true);
		this.setShowsRootHandles(true);
		this.setToggleClickCount(1);
	}

	private static ACAttributesSelectTree instance = null;

	/**
	 * @param newModel
	 * @return 8 juil. 2005
	 */
	public static ACAttributesSelectTree getInstance(ACTreeModel newModel) {
		if (instance == null) {
			instance = new ACAttributesSelectTree(newModel);
		} else
			instance.setModel(newModel);

		return instance;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static ACAttributesSelectTree getInstance() {
		return instance;
	}

	public void setExpandedState(TreePath path, boolean state) {
		super.setExpandedState(path, state);
	}
}
