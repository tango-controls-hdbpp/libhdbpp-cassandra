//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/SaveToDiskAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SaveToDiskAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.4 $
//
//$Log: SaveToDiskAction.java,v $
//Revision 1.4  2006/04/10 08:46:41  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.3  2006/02/15 09:12:43  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:00:04  ounsy
//new Word-like file management
//
//Revision 1.1  2005/11/29 18:25:08  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.manager.ContextManagerFactory;
import fr.soleil.bensikin.data.context.manager.IContextManager;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.manager.ISnapshotManager;
import fr.soleil.bensikin.data.snapshot.manager.SnapshotManagerFactory;
import fr.soleil.bensikin.tools.Messages;

/**
 * An action that does saves the currently selected context or snapshot.
 * 
 * @author CLAISSE
 */
public class SaveToDiskAction extends BensikinAction {
	private int type = NO_PRESET_TYPE;

	/**
	 * The object to save will be set later
	 */
	public static final int NO_PRESET_TYPE = -1;
	/**
	 * The object to print is the current context
	 */
	public static final int CONTEXT_TYPE = 0;
	/**
	 * The object to print is the current snapshot
	 */
	public static final int SNAPSHOT_TYPE = 1;

	/**
	 * Sets the action's name and icon for display, plus its type (none,
	 * context, or snapshot).
	 * 
	 * @param name
	 *            The action name
	 * @param icon
	 *            The action icon
	 * @param _type
	 *            Must be either NO_PRESET_TYPE, CONTEXT_TYPE, or SNAPSHOT_TYPE
	 */
	public SaveToDiskAction(String name, Icon icon, int _type) {
		super(name, icon);
		this.putValue(Action.NAME, name);
		this.type = _type;

		if (_type != NO_PRESET_TYPE && _type != CONTEXT_TYPE
				&& _type != SNAPSHOT_TYPE) {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// System.out.println ( "SaveToDiskAction/actionPerformed" );
		int saveType = -1;

		if (this.type == NO_PRESET_TYPE) {
			String saveLabel = Messages.getMessage("MENU_SAVE");
			String chooseLabel = Messages.getMessage("ACTION_SAVE_CHOOSE");
			String currentContextLabel = Messages
					.getMessage("ACTION_SAVE_CURRENT_CONTEXT");
			String currentSnapshotLabel = Messages
					.getMessage("ACTION_SAVE_CURRENT_SNAPSHOT");

			String[] possibleValues = { currentContextLabel,
					currentSnapshotLabel };
			String selectedValue = (String) JOptionPane.showInputDialog(null,
					chooseLabel, saveLabel, JOptionPane.INFORMATION_MESSAGE,
					null, possibleValues, possibleValues[0]);

			if (selectedValue == null) {
				return;
			}

			if (selectedValue.equals(possibleValues[0])) {
				saveType = CONTEXT_TYPE;
			} else if (selectedValue.equals(possibleValues[1])) {
				saveType = SNAPSHOT_TYPE;
			} else {
				throw new IllegalStateException();
			}
		} else {
			saveType = this.type;
		}

		// ---------------
		switch (saveType) {
		case CONTEXT_TYPE:
			saveContext();
			break;

		case SNAPSHOT_TYPE:
			saveSnapshot();
			break;

		default:

			throw new IllegalStateException();
		}

	}

	/**
	 * <UL>
	 * <LI>Gets the currently focused snapshot; if null do nothing.
	 * <LI>Opens a file chooser dialog specialised in snapshot saving, gets the
	 * path to save to from it.
	 * <LI>Uses the application's ISnapshotManager to save the snapshot.
	 * <LI>Logs the action's success or failure.
	 * </UL>
	 */
	private void saveSnapshot() {
		ISnapshotManager manager = SnapshotManagerFactory.getCurrentImpl();

		SnapshotDetailTabbedPane snapshotDetailTabbedPane = SnapshotDetailTabbedPane
				.getInstance();
		SnapshotDetailTabbedPaneContent selectedContent = snapshotDetailTabbedPane
				.getSelectedSnapshotDetailTabbedPaneContent();
		Snapshot snapshot = null;
		if (selectedContent != null) {
			snapshot = selectedContent.getSnapshot();
		}

		if (snapshot == null) {
			JOptionPane.showMessageDialog(BensikinFrame.getInstance(), Messages
					.getMessage("DIALOGS_POPUP_SNAPSHOT_NO_SNAPSHOT"), Messages
					.getMessage("DIALOGS_POPUP_SNAPSHOT_WARNING"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		snapshot.save(manager, false);
	}

	/**
	 * <UL>
	 * <LI>Builds the current context from the display area.
	 * <LI>Opens a file chooser dialog specialised in context saving, gets the
	 * path to save to from it.
	 * <LI>Uses the application's IContextManager to save the context.
	 * <LI>Logs the action's success or failure.
	 * <UL>
	 */
	private void saveContext() {
		IContextManager manager = ContextManagerFactory.getCurrentImpl();
		Context selectedContext = Context.buildContextToBeSaved();

		selectedContext.save(manager, false);
	}
}
