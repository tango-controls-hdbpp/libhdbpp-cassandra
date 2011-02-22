//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/SaveSelectedSnapshotAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SaveSelectedACAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.4 $
//
//$Log: SaveSelectedSnapshotAction.java,v $
//Revision 1.4  2006/04/10 08:47:14  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.3  2006/01/13 13:24:44  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:09:31  ounsy
//new Word-like file management
//
//Revision 1.1  2005/12/14 14:07:18  ounsy
//first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
//under "bensikin.bensikin" and removing the same from their former locations
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
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.manager.ISnapshotManager;
import fr.soleil.bensikin.data.snapshot.manager.SnapshotManagerFactory;
import fr.soleil.bensikin.tools.Messages;

/**
 * Saves the current snapshot in a file
 * <UL>
 * <LI>If the action's snapshot attribute hasn't been filled yet, gets the
 * current snapshot from the selected tab; if it's null too, does nothing
 * <LI>Opens a file chooser dialog specialised in snapshot saving, gets the path
 * to save to from it (if non-default)
 * <LI>Uses the application's ISnapshotManager to save the context
 * <LI>If the saved snapshot was a file snapshot, removes the "modified" mark
 * (if there was one)
 * <LI>Logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class SaveSelectedSnapshotAction extends BensikinAction {
	private boolean isSaveAs;
	private Snapshot snapshot;

	/**
	 * Standard action constructor that sets the action's name, plus sets the
	 * isDefault attribute and the reference to the <code>Snapshot</code> to
	 * save on <code>actionPerformed</code>
	 * 
	 * @param name
	 *            The action name
	 * @param _snapshot
	 *            The snapshot to save on <code>actionPerformed</code>
	 * @param _isDefault
	 *            True if the save action is a quick save to the working
	 *            directory and default file, false otherwise
	 */
	public SaveSelectedSnapshotAction(String name, Snapshot _snapshot,
			boolean _isSaveAs) {
		super(name);
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);

		this.isSaveAs = _isSaveAs;
		this.snapshot = _snapshot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		ISnapshotManager manager = SnapshotManagerFactory.getCurrentImpl();

		if (snapshot == null) {
			SnapshotDetailTabbedPane snapshotDetailTabbedPane = SnapshotDetailTabbedPane
					.getInstance();
			SnapshotDetailTabbedPaneContent selectedContent = snapshotDetailTabbedPane
					.getSelectedSnapshotDetailTabbedPaneContent();

			if (selectedContent != null) {
				snapshot = selectedContent.getSnapshot();
			}

			if (snapshot == null) {
				JOptionPane
						.showMessageDialog(
								BensikinFrame.getInstance(),
								Messages
										.getMessage("DIALOGS_POPUP_SNAPSHOT_NO_SNAPSHOT"),
								Messages
										.getMessage("DIALOGS_POPUP_SNAPSHOT_WARNING"),
								JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		snapshot.save(manager, this.isSaveAs);

		/*
		 * if ( !this.isDefault ) { //open file chooser String location =
		 * manager.getSaveLocation(); if ( location == null || "".equals(
		 * location.trim() ) ) { location = manager.getDefaultSaveLocation(); }
		 * JFileChooser chooser = new JFileChooser();
		 * chooser.setCurrentDirectory( new File( location ).getParentFile() );
		 * SnapshotFileFilter filter = new SnapshotFileFilter();
		 * chooser.addChoosableFileFilter( filter );
		 * 
		 * String title = Messages.getMessage(
		 * "DIALOGS_FILE_CHOOSER_SAVE_SNAPSHOT_TITLE" ); chooser.setDialogTitle(
		 * title );
		 * 
		 * int returnVal = chooser.showSaveDialog( BensikinFrame.getInstance()
		 * ); if ( returnVal == JFileChooser.APPROVE_OPTION ) { File f =
		 * chooser.getSelectedFile(); if ( f != null ) { String path =
		 * f.getAbsolutePath(); String extension = DataFileFilter.getExtension(
		 * f ); String expectedExtension = filter.getExtension();
		 * 
		 * if ( extension == null || !extension.equalsIgnoreCase(
		 * expectedExtension ) ) { path += "."; path += expectedExtension; } if
		 * ( f.exists() ) { int ok = JOptionPane.showConfirmDialog(
		 * BensikinFrame.getInstance() , Messages.getMessage(
		 * "DIALOGS_FILE_CHOOSER_FILE_EXISTS" ) , Messages.getMessage(
		 * "DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE" ) ,
		 * JOptionPane.YES_NO_OPTION ); if ( ok == JOptionPane.OK_OPTION ) {
		 * manager.setSaveLocation( path ); ILogger logger =
		 * LoggerFactory.getCurrentImpl(); try { manager.saveSnapshot( snapshot
		 * , null );
		 * 
		 * removeModifiedMarkers( snapshot );
		 * 
		 * String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_OK" );
		 * logger.trace( ILogger.LEVEL_DEBUG , msg ); } catch ( Exception e ) {
		 * String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_KO" );
		 * logger.trace( ILogger.LEVEL_ERROR , msg ); logger.trace(
		 * ILogger.LEVEL_ERROR , e ); return; } } } else {
		 * manager.setSaveLocation( path ); ILogger logger =
		 * LoggerFactory.getCurrentImpl(); try { manager.saveSnapshot( snapshot
		 * , null );
		 * 
		 * removeModifiedMarkers( snapshot );
		 * 
		 * String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_OK" );
		 * logger.trace( ILogger.LEVEL_DEBUG , msg ); } catch ( Exception e ) {
		 * String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_KO" );
		 * logger.trace( ILogger.LEVEL_ERROR , msg ); logger.trace(
		 * ILogger.LEVEL_ERROR , e ); return; } } } } } else {
		 * manager.setSaveLocation( null ); ILogger logger =
		 * LoggerFactory.getCurrentImpl(); try { System.out.println(
		 * "---------SNAPSHOT TO BE SAVED--------------" ); System.out.println(
		 * snapshot.toString() ); System.out.println(
		 * "---------SNAPSHOT TO BE SAVED--------------" );
		 * manager.saveSnapshot( snapshot , null );
		 * 
		 * removeModifiedMarkers( snapshot );
		 * 
		 * String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_OK" );
		 * logger.trace( ILogger.LEVEL_DEBUG , msg ); } catch ( Exception e ) {
		 * String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_KO" );
		 * logger.trace( ILogger.LEVEL_ERROR , msg ); logger.trace(
		 * ILogger.LEVEL_ERROR , e ); return; } }
		 */

	}

}
