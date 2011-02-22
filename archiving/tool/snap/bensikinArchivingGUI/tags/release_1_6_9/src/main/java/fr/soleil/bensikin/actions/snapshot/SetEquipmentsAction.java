//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/SetEquipmentsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SetEquipmentsAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.5 $
//
// $Log: SetEquipmentsAction.java,v $
// Revision 1.5  2007/10/15 13:15:42  soleilarc
// Author: XP
// Mantis bug ID: 6695
// Comment: In the actionPerformed method, change the Exception exception into a SnapshotingException exception, and complete the catch clause, to display which devices are unreachable.
//
// Revision 1.4  2006/11/29 09:57:00  ounsy
// minor changes
//
// Revision 1.3  2006/04/10 08:47:14  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.2  2006/02/15 09:13:41  ounsy
// minor changes : uncomment to debug
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.esrf.Tango.DevError;
import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * Sets the write values of all Tango attributes contained in the selected
 * snapshot, with the write values of the its attributes.
 * <UL>
 * <LI>Opens a confirmation popup; if the user cancels, does nothing
 * <LI>Gets the selected snapshot
 * <LI>Calls setEquipments on it
 * <LI>Logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class SetEquipmentsAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param _name
     *            The action name
     */
    public SetEquipmentsAction(final String _name) {
	putValue(Action.NAME, _name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent arg0) {
	final String msgTitle = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_CONFIRM_TITLE");
	final String msgConfirm = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_CONFIRM_LABEL");
	final String msgCancel = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_CANCEL");
	final String msgValidate = Messages.getMessage("DIALOGS_SET_EQUIPMENTS_VALIDATE");
	final Object[] options = { msgValidate, msgCancel };

	final int confirm = JOptionPane.showOptionDialog(null, msgConfirm, msgTitle,
		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

	if (confirm != JOptionPane.OK_OPTION) {
	    return;
	}

	final SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane.getInstance();
	// SnapshotDetailTabbedPaneContent content = (
	// SnapshotDetailTabbedPaneContent ) tabbedPane.getSelectedComponent();

	final Snapshot snapshotToUse = ((SnapshotDetailTabbedPaneContent) tabbedPane
		.getSelectedComponent()).getSnapshot();
	// String idToAdd =
	// String.valueOf(snapshotToUse.getSnapshotData().getId());

	try {
	    snapshotToUse.setEquipments();
	    final String msg = Messages.getLogMessage("SET_EQUIPMENTS_ACTION_OK");
	    logger.trace(ILogger.LEVEL_INFO, msg);
	} catch (final SnapshotingException e) {
	    final DevError[] devErrorTab = e.getDevErrorTab();
	    int i = 0;
	    do {
		if (devErrorTab[i].reason.indexOf("method on") > 0) {
		    logger.trace(ILogger.LEVEL_ERROR, devErrorTab[i].reason);
		}
	    } while (++i < devErrorTab.length);

	    final String msg = Messages.getLogMessage("SET_EQUIPMENTS_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}
    }
}
