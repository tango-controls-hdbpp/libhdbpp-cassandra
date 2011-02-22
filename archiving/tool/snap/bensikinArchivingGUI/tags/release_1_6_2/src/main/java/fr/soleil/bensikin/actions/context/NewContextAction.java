// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/NewContextAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class NewContextAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: NewContextAction.java,v $
// Revision 1.5 2007/08/24 14:05:20 ounsy
// bug correction with context printing as text
//
// Revision 1.4 2007/08/24 12:52:56 ounsy
// minor changes
//
// Revision 1.3 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2005/12/14 16:03:36 ounsy
// minor changes
//
// Revision 1.1 2005/12/14 14:07:17 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2 2005/08/22 11:58:33 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.context.ContextAttributesPanel;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.tango.util.entity.ui.model.AttributesSelectTableModel;

/**
 * Starts over with a new, void context
 * <UL>
 * <LI>Resets the current context's display and enables input for a new one.
 * <LI>Resets all snapshots displays
 * </UL>
 * 
 * @author CLAISSE
 */
public class NewContextAction extends BensikinAction {

	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public NewContextAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/**
	 * Standard action constructor that sets the action's name and icon.
	 * 
	 * @param name
	 *            The action name
	 * @param icon
	 */
	public NewContextAction(String name, Icon icon) {
		super(name, icon);
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// reset the context data fields and enable input
		ContextDataPanel dataPanel = ContextDataPanel.getInstance();
		dataPanel.enableInput(true);

		// reset the attributes tree and enable input
		ContextAttributesTree tree = ContextAttributesTree.getInstance();
		ContextAttributesTreeModel model = ContextAttributesTreeModel
				.getInstance(true);
		model.setTree(tree);
		tree.setModel(model);
		model.removeAll();

		AttributesSelectTableModel attributesSelectTableModel = ContextDetailPanel
				.getInstance().getAttributeTableSelectionBean()
				.getSelectionPanel().getAttributesSelectTable().getModel();
		attributesSelectTableModel.reset();

		ContextAttributesPanel attrPanel = ContextAttributesPanel.getInstance();
		attrPanel.enableButtons();

		Snapshot.reset(false, true);
		BensikinMenuBar.getInstance().resetRegisterItem();
		ContextActionPanel.getInstance().resetRegisterButton();
		ContextActionPanel.getInstance().allowPrint(false);
	}

}
