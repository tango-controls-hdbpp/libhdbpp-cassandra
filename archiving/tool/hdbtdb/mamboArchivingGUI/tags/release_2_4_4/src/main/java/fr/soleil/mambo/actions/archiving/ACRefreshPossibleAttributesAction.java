/*
 * Created on Jun 27, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.components.archiving.ACPossibleAttributesTree;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.mambo.models.ACPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.Messages;

/**
 * @author operateur
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ACRefreshPossibleAttributesAction extends AbstractAction {

    private static ACRefreshPossibleAttributesAction instance;

    final static ILogger logger = GUILoggerFactory.getLogger();

    private final static String title = Messages.getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_UPDATE_KO");
    private String msgLog;
    private String msg;

    public static ACRefreshPossibleAttributesAction getInstance() {
	if (instance == null) {
	    instance = new ACRefreshPossibleAttributesAction();
	}
	return instance;
    }

    private ACRefreshPossibleAttributesAction() {
	super();
	super
		.putValue(Action.NAME, Messages
			.getMessage("ARCHIVING_ACTION_REFRESH_POSSIBLE_BUTTON"));
    }

    public void actionPerformed(final ActionEvent arg0) {
	WaitingDialog.openInstance();

	try {
	    final ITangoManager source = TangoManagerFactory.getCurrentImpl();
	    source.loadDomains(new Criterions(), true);
	} catch (final Exception e) {
	    msgLog = Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_TANGO_ATTRIBUTES_KO");
	    msg = Messages.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_UPDATE_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msgLog);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    JOptionPane.showMessageDialog(ACEditDialog.getInstance(), msg, title,
		    JOptionPane.ERROR_MESSAGE);
	    msg = null;
	    msgLog = null;
	    WaitingDialog.closeInstance();
	    return;
	} catch (final Throwable t) {
	    t.printStackTrace();
	    WaitingDialog.closeInstance();
	    return;
	}
	try {
	    ACPossibleAttributesTree.getInstance().setModel(
		    ACPossibleAttributesTreeModel.forceGetInstance());
	    ACPossibleAttributesTree.getInstance().repaint();
	} catch (final Throwable t) {
	    t.printStackTrace();
	}
	WaitingDialog.closeInstance();
    }

}
