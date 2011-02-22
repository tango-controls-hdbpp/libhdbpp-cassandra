// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/LaunchSnapshotAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class LaunchSnapshotAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.6 $
//
// $Log: LaunchSnapshotAction.java,v $
// Revision 1.6 2007/10/04 08:29:18 soleilarc
// Author: XP
// Mantis bug ID: 6594
// Comment: Add the block of the catch clause for the SnapshotingException
// exceptions.
//
// Revision 1.5 2007/09/25 14:54:01 pierrejoseph
// 6594 : Close the WaitingDialog event if an Exception occured
//
// Revision 1.4 2007/08/30 16:13:33 pierrejoseph
// girardot : avoiding WaitingDialog to stay opened
//
// Revision 1.3 2007/08/24 14:18:07 ounsy
// WaitingDialog added (Mantis bug 3912)
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

import fr.soleil.bensikin.containers.sub.dialogs.WaitingDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * Launches a snapshot on the current context.
 * <UL>
 * <LI>gets the currently selected context, if null do nothing
 * <LI>launches the snapshot
 * <LI>logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class LaunchSnapshotAction extends AbsRefreshAction {

    private static final long           serialVersionUID = 8157115330594820825L;
    private static LaunchSnapshotAction instance         = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static LaunchSnapshotAction getInstance(String name) {
        if (instance == null) {
            instance = new LaunchSnapshotAction(name);
        }

        return instance;
    }

    /**
     * Returns the current instance.
     * 
     * @return The current instance
     */
    public static LaunchSnapshotAction getInstance() {
        return instance;
    }

    /**
     * @param name
     *            The action name
     */
    private LaunchSnapshotAction(String name) {
        this.putValue(Action.NAME, name);
        this.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        WaitingDialog.changeFirstMessage(Messages
                .getMessage("DIALOGS_WAITING_LAUNCHING_TITLE"));
        WaitingDialog.openInstance();
        Context selectedContext = Context.getSelectedContext();

        ILogger logger = LoggerFactory.getCurrentImpl();
        try {
            selectedContext.launchSnapshot();

            String msg = Messages.getLogMessage("LAUNCH_SNAPSHOT_ACTION_OK");
            logger.trace(ILogger.LEVEL_INFO, msg);

        }
        catch (SnapshotingException snapEx) {
            snapEx.printStackTrace();

            if (snapEx.computeIsDueToATimeOut()) {
                // Print a Bensikin log message because of the timeout
                String msg = Messages.getLogMessage("LAUNCH_SNAPSHOT_TIMEOUT");
                logger.trace(ILogger.LEVEL_INFO, msg);

                // To ending the SnapArchiver work needs some time more.
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    logger.trace(ILogger.LEVEL_ERROR, e);
                }

                refreshSnapList();
            }
            else {
                String msg = Messages
                        .getLogMessage("LAUNCH_SNAPSHOT_ACTION_KO");
                logger.trace(ILogger.LEVEL_ERROR, msg);
                logger.trace(ILogger.LEVEL_ERROR, snapEx);
            }

        }
        catch (Throwable t) {
            String msg = Messages.getLogMessage("LAUNCH_SNAPSHOT_ACTION_KO");
            logger.trace(ILogger.LEVEL_ERROR, msg);
            logger.trace(ILogger.LEVEL_ERROR, t);
        }
        finally {
            WaitingDialog.closeInstance();
        }
    }
}
