// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/SelectFavoriteContextAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SelectFavoriteContextAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: SelectFavoriteContextAction.java,v $
// Revision 1.4 2007/08/24 14:05:20 ounsy
// bug correction with context printing as text
//
// Revision 1.3 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2006/03/27 14:02:06 ounsy
// favorites contexts now have a label
//
// Revision 1.1 2005/12/14 14:07:17 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2 2005/08/22 11:58:34 chinkumo
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

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * Makes the selected (in favorites) context the current context.
 * <UL>
 * <LI>Loads the context with the selected id via Context.findContext
 * <LI>Displays this context
 * <LI>Logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class SelectFavoriteContextAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = 6340699525247766654L;
    private final String id;

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action name, which is also the context's id
     */
    public SelectFavoriteContextAction(final String id, final String name) {
	putValue(Action.NAME, name);
	this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent arg0) {

	try {
	    final String id = this.id;
	    final Context selectedContext = Context.findContext(id);
	    Context.setSelectedContext(selectedContext);
	    selectedContext.push();
	    ContextActionPanel.getInstance().allowPrint(true);

	    final String msg = Messages.getLogMessage("LOAD_FAVORITE_CONTEXT_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("LOAD_FAVORITE_CONTEXT_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}
    }
}
