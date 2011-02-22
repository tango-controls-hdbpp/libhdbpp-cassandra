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

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.components.archiving.ACPossibleAttributesTree;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesTab;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.mambo.models.ACPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.Messages;

public class MatchPossibleAttributesAction extends AbstractAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    /**
     * 
     */
    private static final long serialVersionUID = -213885952805354775L;

    /**
     * @param name
     */
    public MatchPossibleAttributesAction(final String name) {
	putValue(Action.NAME, name);

	setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent arg0) {
	final AttributesTab attributesTab = AttributesTab.getInstance();
	final String pattern = attributesTab.getLeftRegexp();
	final Criterions searchCriterions = MatchACAttributesAction
		.getAttributesSearchCriterions(pattern);

	try {
	    final ACPossibleAttributesTree tree = ACPossibleAttributesTree.getInstance();
	    final ACPossibleAttributesTreeModel model = (ACPossibleAttributesTreeModel) tree
		    .getModel();

	    final ITangoManager source = TangoManagerFactory.getCurrentImpl();

	    final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();
	    model.setRoot(root);

	    model.build(source, searchCriterions);

	    // Notify that the model has been reset
	    model.reload(root);

	    tree.setModel(model);

	    // Expand 1 level
	    tree.expandAll1Level(true);

	    // Uncomment this to expand all nodes from the resulting tree
	    // tree.expandAll( true );
	    final String msg = Messages.getLogMessage("LOAD_POSSIBLE_ATTRIBUTES_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("LOAD_POSSIBLE_ATTRIBUTES_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}

    }
}
