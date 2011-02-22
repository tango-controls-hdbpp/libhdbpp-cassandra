//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/EditCommentAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  EditCommentAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: EditCommentAction.java,v $
// Revision 1.3  2006/11/29 09:57:00  ounsy
// minor changes
//
// Revision 1.2  2006/04/10 08:47:14  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
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

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.containers.sub.dialogs.UpdateCommentDialog;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.tools.Messages;


/**
 * Updates the selected Snapshot with a new comment.
 * <UL>
 * <LI> Gets the selected Snapshot, and the new comment
 * <LI> Closes the comment editing dialog
 * <LI> Updates the selected Snapshot with this comment via updateComment
 * <LI> Logs the action's success or failure
 * </UL>
 *
 * @author CLAISSE
 */
public class EditCommentAction extends BensikinAction
{
	private UpdateCommentDialog dialogFrom;

	/**
	 * Standard action constructor that sets the action's name, plus initializes the reference to the popup to close on <code>actionPerformed</code>
	 *
	 * @param name        The action name
	 * @param _dialogFrom
	 */
	public EditCommentAction(String name , UpdateCommentDialog _dialogFrom)
	{
		this.putValue(Action.NAME , name);

		this.dialogFrom = _dialogFrom;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane.getInstance();
		//SnapshotDetailTabbedPaneContent content = ( SnapshotDetailTabbedPaneContent ) tabbedPane.getSelectedComponent();
		Snapshot snapshotToUse = ( ( SnapshotDetailTabbedPaneContent ) tabbedPane.getSelectedComponent() ).getSnapshot();
		//String idToComment = String.valueOf(snapshotToUse.getSnapshotData().getId());

		String comment = dialogFrom.getCommentText().getText();

		dialogFrom.setVisible(false);

		ILogger logger = LoggerFactory.getCurrentImpl();
		try
		{
			snapshotToUse.updateComment(comment);

			String msg = Messages.getLogMessage("EDIT_COMMENT_ACTION_OK");
			logger.trace(ILogger.LEVEL_INFO , msg);
		}
		catch ( Exception e )
		{
			String msg = Messages.getLogMessage("EDIT_COMMENT_ACTION_KO");
			logger.trace(ILogger.LEVEL_ERROR , msg);
			logger.trace(ILogger.LEVEL_ERROR , e);
			return;
		}
	}
}
