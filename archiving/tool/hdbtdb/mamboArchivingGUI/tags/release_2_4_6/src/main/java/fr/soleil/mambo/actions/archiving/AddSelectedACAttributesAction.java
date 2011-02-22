//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/AddSelectedACAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AddSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: AddSelectedACAttributesAction.java,v $
// Revision 1.5  2006/08/23 10:01:32  ounsy
// some optimizations with less tree model reloading
//
// Revision 1.4  2006/05/19 14:58:55  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:15:43  ounsy
// small modifications
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.ACAttributesPropertiesTree;
import fr.soleil.mambo.components.archiving.ACAttributesRecapTree;
import fr.soleil.mambo.components.archiving.ACAttributesSelectTree;
import fr.soleil.mambo.components.archiving.ACPossibleAttributesTree;
import fr.soleil.mambo.models.ACAttributesTreeModel;

public class AddSelectedACAttributesAction extends AbstractAction {
	// private static final String LoggerFactory = null;

	/**
	 * @param name
	 */
	public AddSelectedACAttributesAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		ACPossibleAttributesTree leftTree = ACPossibleAttributesTree
				.getInstance();
		Vector listToAdd = leftTree
				.getListOfAttributesTreePathUnderSelectedNodes(false);

		if (listToAdd != null && listToAdd.size() != 0) {
			ACAttributesSelectTree rightTree = ACAttributesSelectTree
					.getInstance();

			ACAttributesTreeModel model = (ACAttributesTreeModel) rightTree
					.getModel();
			model.addSelectedAttibutes(listToAdd);

			rightTree.revalidate();
			rightTree.updateUI();
			rightTree.repaint();
			rightTree.expandAll(true);

			ACAttributesPropertiesTree propTree = ACAttributesPropertiesTree
					.getInstance();
			if (propTree != null) {
				propTree.revalidate();
				propTree.updateUI();
				propTree.repaint();
			}
			ACAttributesRecapTree recapTree = ACAttributesRecapTree
					.getInstance();
			if (recapTree != null) {
				recapTree.revalidate();
				recapTree.updateUI();
				recapTree.repaint();
			}
		}

	}
}
