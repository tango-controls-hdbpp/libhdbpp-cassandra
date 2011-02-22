//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/RemoveSelectedContextAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  RemoveSelectedContextAttributesAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: RemoveSelectedContextAttributesAction.java,v $
// Revision 1.3  2007/08/24 14:05:20  ounsy
// bug correction with context printing as text
//
// Revision 1.2  2006/04/10 08:46:54  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/12/14 14:07:17  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;

/**
 * Removes the selected attributes (or attributes that are under selected nodes)
 * from the current context.
 * <UL>
 * <LI>Gets the list of attributes that are under one of the selected tree
 * nodes; if that list is empty, do nothing.
 * <LI>Removes those attributes to the current ContextAttributesTreeModel
 * instance.
 * </UL>
 * 
 * @author CLAISSE
 */
public class RemoveSelectedContextAttributesAction extends BensikinAction {
	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public RemoveSelectedContextAttributesAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		ContextAttributesTree rightTree = ContextAttributesTree.getInstance();
		Vector listToRemove = rightTree
				.getListOfAttributesTreePathUnderSelectedNodes(true);

		ContextAttributesTreeModel model = (ContextAttributesTreeModel) rightTree
				.getModel();
		if (listToRemove.size() != 0) {
			model.removeSelectedAttributes(listToRemove);
			model.reload();
		}
		ContextActionPanel.getInstance().allowPrint(false);
	}
}
