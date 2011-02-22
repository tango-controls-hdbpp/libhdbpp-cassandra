// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/AddSelectedContextAttributesAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class
// AddSelectedContextAttributesAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: AddSelectedContextAttributesAction.java,v $
// Revision 1.5 2007/08/24 14:04:53 ounsy
// bug correction with context printing as text
//
// Revision 1.4 2007/08/23 12:57:22 ounsy
// minor changes
//
// Revision 1.3 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2005/12/14 16:01:27 ounsy
// attributes added to the tree are automatically added to the attributes select
// table
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
import java.util.Vector;

import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.detail.PossibleAttributesTree;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * Adds the selected attributes (or attributes that are under selected nodes) to
 * the current context.
 * <UL>
 * <LI>Gets the list of attributes that are under one of the selected tree
 * nodes; if that list is empty, do nothing.
 * <LI>Adds those attributes to the current ContextAttributesTreeModel instance.
 * <LI>Logs the action's success or failure.
 * </UL>
 * 
 * @author CLAISSE
 */
public class AddSelectedContextAttributesAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = -5872593224821475505L;

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action name
     */
    public AddSelectedContextAttributesAction(final String name) {
	putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
	final PossibleAttributesTree leftTree = PossibleAttributesTree.getInstance();
	final Vector<TreePath> listToAdd = leftTree
		.getListOfAttributesTreePathUnderSelectedNodes(false);

	if (listToAdd.size() != 0) {
	    final ContextAttributesTreeModel model = ContextAttributesTreeModel.getInstance(false);

	    try {
		// ArrayList<TreePath> paths = model.addSelectedAttributes(
		// listToAdd, true);
		model.addSelectedAttributes(listToAdd, true);
		model.reload();
		if (ContextAttributesTree.getInstance() != null) {
		    // for (int i = 0; i < paths.size(); i++) {
		    // ContextAttributesTree.getInstance().makeVisible(
		    // paths.get(i));
		    // }
		    ContextAttributesTree.getInstance().expandAll(true);
		}
		// paths.clear();
		// paths = null;

		final String msg = Messages
			.getLogMessage("ADD_SELECTED_CONTEXT_ATTRIBUTES_ACTION_OK");
		logger.trace(ILogger.LEVEL_DEBUG, msg);

	    } catch (final Exception e) {
		final String msg = Messages
			.getLogMessage("ADD_SELECTED_CONTEXT_ATTRIBUTES_ACTION_KO");
		logger.trace(ILogger.LEVEL_CRITIC, msg);
		logger.trace(ILogger.LEVEL_CRITIC, e);
		return;
	    }
	    ContextActionPanel.getInstance().allowPrint(false);
	}
    }
}
