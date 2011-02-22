// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/LoadSnapshotAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class LoadACAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: LoadSnapshotAction.java,v $
// Revision 1.3 2006/04/10 08:47:14 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2005/12/14 16:09:11 ounsy
// new Word-like file management
//
// Revision 1.1 2005/12/14 14:07:18 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Action;
import javax.swing.JFileChooser;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.snapshot.SnapshotFileFilter;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.manager.ISnapshotManager;
import fr.soleil.bensikin.data.snapshot.manager.SnapshotManagerFactory;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.tools.Messages;

/**
 * Loads a snapshot from a file. This action can be of 2 type: default (quick
 * load from the default directory and file), or non-default (load from the
 * user-defined directory and file, with prepositionning on the default
 * directory).
 * <UL>
 * <LI>if the load isn't a quick load,opens a file chooser dialog specialised in
 * snapshot loading, gets the path to load from from it.
 * <LI>uses the application's ISnapshotManager to load the snapshot.
 * <LI>displays the loaded snapshot.
 * <LI>logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class LoadSnapshotAction extends BensikinAction {

	private boolean isDefault;

	/**
	 * Standard action constructor that sets the action's name, plus sets the
	 * isDefault attribute.
	 * 
	 * @param name
	 *            The action name
	 * @param _isDefault
	 */
	public LoadSnapshotAction(String name, boolean _isDefault) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);

		this.isDefault = _isDefault;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		ISnapshotManager manager = SnapshotManagerFactory.getCurrentImpl();

		if (!this.isDefault) {
			// open file chooser
			JFileChooser chooser = new JFileChooser();
			String location = manager.getSaveLocation();
			if (location == null || "".equals(location.trim())) {
				chooser.setCurrentDirectory(new File(manager
						.getDefaultSaveLocation()));
			} else {
				chooser.setCurrentDirectory(new File(location).getParentFile());
			}
			SnapshotFileFilter filter = new SnapshotFileFilter();
			chooser.addChoosableFileFilter(filter);
			String title = Messages
					.getMessage("DIALOGS_FILE_CHOOSER_LOAD_SNAPSHOT_TITLE");
			chooser.setDialogTitle(title);

			int returnVal = chooser.showOpenDialog(BensikinFrame.getInstance());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				String path = f.getAbsolutePath();

				if (f != null) {
					String extension = SnapshotFileFilter.getExtension(f);
					String expectedExtension = filter.getExtension();

					if (extension == null
							|| !extension.equalsIgnoreCase(expectedExtension)) {
						path += ".";
						path += expectedExtension;
					}
				}
				manager.setNonDefaultSaveLocation(path);
			} else {
				return;
			}
		} else {
			manager.setNonDefaultSaveLocation(null);
		}

		ILogger logger = LoggerFactory.getCurrentImpl();

		try {
			Snapshot selectedSnapshot = manager.loadSnapshot();

			SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane
					.getInstance();
			String fileName = manager.getFileName();
			tabbedPane.addFileSnapshotDetail(selectedSnapshot, fileName);

			String msg = Messages.getLogMessage("LOAD_SNAPSHOT_ACTION_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (FileNotFoundException fnfe) {
			String msg = Messages.getLogMessage("LOAD_SNAPSHOT_ACTION_WARNING");
			logger.trace(ILogger.LEVEL_WARNING, msg);
			logger.trace(ILogger.LEVEL_WARNING, fnfe);
			return;
		} catch (Exception e) {
			String msg = Messages.getLogMessage("LOAD_SNAPSHOT_ACTION_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}
}
