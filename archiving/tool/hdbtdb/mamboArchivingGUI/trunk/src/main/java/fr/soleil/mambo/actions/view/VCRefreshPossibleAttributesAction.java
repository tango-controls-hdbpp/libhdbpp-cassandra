/*
 * Created on Jun 27, 2006 TODO To change the template for this generated file
 * go to Window - Preferences - Java - Code Style - Code Templates
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.tools.Messages;

/**
 * @author operateur TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class VCRefreshPossibleAttributesAction extends AbstractAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = -1469173242668010378L;

    private boolean historic;

    private final static String title = Messages.getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_UPDATE_KO");
    private String msgLog;
    private String msg;
    private final VCEditDialog editDialog;

    public VCRefreshPossibleAttributesAction(final VCEditDialog editDialog) {
	super();
	super.putValue(Action.NAME, Messages.getMessage("VIEW_ACTION_REFRESH_POSSIBLE_BUTTON"));
	this.editDialog = editDialog;
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
	historic = editDialog.getAttributesTab().getPossibleAttributesTree().getModel()
		.isHistoric();
	WaitingDialog.openInstance();

	try {
	    final IAttributeManager source = AttributeManagerFactory.getCurrentImpl();
	    source.loadDomains(null, historic, true);
	} catch (final Throwable e) {
	    if (historic) {
		msgLog = Messages
			.getLogMessage("APPLICATION_WILL_START_BUFFERING_HDB_ATTRIBUTES_KO");
		msg = Messages.getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_UPDATE_HDB_KO");
	    } else {
		msgLog = Messages
			.getLogMessage("APPLICATION_WILL_START_BUFFERING_TDB_ATTRIBUTES_KO");
		msg = Messages.getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_UPDATE_TDB_KO");
	    }
	    logger.trace(ILogger.LEVEL_ERROR, msgLog);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    JOptionPane.showMessageDialog(editDialog, msg, title, JOptionPane.ERROR_MESSAGE);
	    msg = null;
	    msgLog = null;
	    WaitingDialog.closeInstance();
	    return;
	}
	editDialog.getAttributesTab().getPossibleAttributesTree().getModel().setHistoric(historic);
	editDialog.getAttributesTab().getPossibleAttributesTree().repaint();
	WaitingDialog.closeInstance();
    }

}
