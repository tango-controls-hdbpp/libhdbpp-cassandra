//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/MatchPossibleAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MatchPossibleAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: MatchPossibleAttributesAction.java,v $
// Revision 1.5  2006/09/22 14:52:23  ounsy
// minor changes
//
// Revision 1.4  2006/09/20 12:49:40  ounsy
// changed imports
//
// Revision 1.3  2006/07/18 10:21:55  ounsy
// Less time consuming by setting tree expanding on demand only
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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.components.archiving.ACPossibleAttributesTree;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesTab;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.models.ACPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class MatchPossibleAttributesAction extends AbstractAction {

	/**
	 * @param name
	 */
	public MatchPossibleAttributesAction(String name) {
		this.putValue(Action.NAME, name);

		this.setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		AttributesTab attributesTab = AttributesTab.getInstance();
		String pattern = attributesTab.getLeftRegexp();
		Criterions searchCriterions = GUIUtilities
				.getAttributesSearchCriterions(pattern);

		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			ACPossibleAttributesTree tree = ACPossibleAttributesTree
					.getInstance();
			ACPossibleAttributesTreeModel model = (ACPossibleAttributesTreeModel) tree
					.getModel();

			ITangoManager source = TangoManagerFactory.getCurrentImpl();

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model
					.getRoot();
			root.removeAllChildren();
			model.setRoot(root);

			model.build(source, searchCriterions);

			tree.setModel(model);
			// tree.expandAll( true );
			String msg = Messages.getLogMessage("LOAD_POSSIBLE_ATTRIBUTES_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (Exception e) {
			String msg = Messages.getLogMessage("LOAD_POSSIBLE_ATTRIBUTES_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}

	}
}
