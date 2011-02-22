//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/RemoveSelectedACAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  RemoveSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: RemoveSelectedACAttributesAction.java,v $
// Revision 1.4  2006/08/23 10:01:32  ounsy
// some optimizations with less tree model reloading
//
// Revision 1.3  2006/08/07 13:03:07  ounsy
// trees and lists sort
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
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.ACAttributesPropertiesTree;
import fr.soleil.mambo.components.archiving.ACAttributesRecapTree;
import fr.soleil.mambo.components.archiving.ACAttributesSelectTree;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.models.ACAttributesTreeModel;

public class RemoveSelectedACAttributesAction extends AbstractAction {

	/**
	 * @param name
	 */
	public RemoveSelectedACAttributesAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		ACAttributesSelectTree rightTree = ACAttributesSelectTree.getInstance();
		Vector listToRemove = rightTree
				.getListOfAttributesTreePathUnderSelectedNodes(true);
		if (listToRemove == null) {
			return;
		}

		ACAttributesTreeModel model = (ACAttributesTreeModel) rightTree
				.getModel();
		if (listToRemove.size() != 0) {
			model.removeSelectedAttributes(listToRemove);

			rightTree.revalidate();
			rightTree.updateUI();
			rightTree.repaint();

			TreeMap attrs = model.getAttributes();
			ArchivingConfiguration currentArchivingConfiguration = ArchivingConfiguration
					.getCurrentArchivingConfiguration();
			currentArchivingConfiguration.getAttributes()
					.removeAttributesNotInList(attrs);

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
