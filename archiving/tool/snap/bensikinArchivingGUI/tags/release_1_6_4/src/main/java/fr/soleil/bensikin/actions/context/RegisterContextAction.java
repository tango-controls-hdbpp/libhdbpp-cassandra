// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/RegisterContextAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class RegisterContextAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.8 $
//
// $Log: RegisterContextAction.java,v $
// Revision 1.8 2007/08/30 16:16:14 pierrejoseph
// girardot : avoiding WaitingDialog to stay opened
//
// Revision 1.7 2007/08/24 14:18:07 ounsy
// WaitingDialog added (Mantis bug 3912)
//
// Revision 1.6 2007/08/24 14:05:20 ounsy
// bug correction with context printing as text
//
// Revision 1.5 2007/08/24 12:52:56 ounsy
// minor changes
//
// Revision 1.4 2007/08/23 15:39:18 ounsy
// Registering context resets Snapshot list (Mantis bug 6144)
//
// Revision 1.3 2007/08/23 12:57:22 ounsy
// minor changes
//
// Revision 1.2 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
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

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.containers.sub.dialogs.WaitingDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;

/**
 * Registers a new context; a singleton class.
 * <UL>
 * <LI>Builds a SnapContext object to save.
 * <LI>Saves it through Context.save
 * <LI>Logs the action's success or failure
 * <LI>Displays the saved context (which now has filled id and time fields)
 * </UL>
 * 
 * @author CLAISSE
 */
public class RegisterContextAction extends BensikinAction {

    private static final long serialVersionUID = -3809122686425225675L;
    private static RegisterContextAction instance = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static RegisterContextAction getInstance(String name) {
        if (instance == null) {
            instance = new RegisterContextAction(name);
        }

        return instance;
    }

    /**
     * Returns the existing instance
     * 
     * @return The existing instance
     */
    public static RegisterContextAction getInstance() {
        return instance;
    }

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action's name
     */
    private RegisterContextAction(String name) {
        this.putValue(Action.NAME, name);
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        WaitingDialog.changeFirstMessage(Messages
                .getMessage("DIALOGS_WAITING_SAVING_TITLE"));
        WaitingDialog.openInstance();
        ContextDataPanel contextDataPanel = ContextDataPanel.getInstance();

        String name = contextDataPanel.getNameField().getText();
        String author = contextDataPanel.getAuthorNameField().getText();
        String reason = contextDataPanel.getReasonField().getText();
        String description = contextDataPanel.getDescriptionField().getText();

        ContextAttributesTreeModel model = ContextAttributesTreeModel
                .getInstance(false);

        Context savedContext = null;
        ILogger logger = LoggerFactory.getCurrentImpl();
        try {
            SnapContext toSave = Context.fillSnapContext(name, author, reason,
                    description, model);
            if (toSave == null) {
                return;
                // this happens when the user didn't fill the context data or
                // didn't choose attributes
            }
            savedContext = Context.save(toSave);

            String msg = Messages.getLogMessage("REGISTER_CONTEXT_ACTION_OK");
            logger.trace(ILogger.LEVEL_INFO, msg);
        } catch (Throwable e) {
            String msg = Messages.getLogMessage("REGISTER_CONTEXT_ACTION_KO");
            logger.trace(ILogger.LEVEL_ERROR, msg);
            logger.trace(ILogger.LEVEL_ERROR, e);
            WaitingDialog.closeInstance();
            return;
        }

        Context.setSelectedContext(savedContext);
        savedContext.push();
        ContextActionPanel.getInstance().allowPrint(true);
        Snapshot.reset(false, false);
        ContextAttributesTree.getInstance().expandAll(true);
        WaitingDialog.closeInstance();
    }

}
