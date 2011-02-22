//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/SaveSelectedContextAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SaveSelectedACAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.5 $
//
//$Log: SaveSelectedContextAction.java,v $
//Revision 1.5  2006/04/10 08:46:54  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.4  2006/03/14 13:06:18  ounsy
//removed useless logs
//
//Revision 1.3  2006/01/13 13:24:27  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:04:34  ounsy
//new Word-like file management
//
//Revision 1.1  2005/12/14 14:07:17  ounsy
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
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.manager.ContextManagerFactory;
import fr.soleil.bensikin.data.context.manager.IContextManager;

/**
 * Saves the current context in a file. This action can be of 2 type: default
 * (quick save to the default directory and file), or non-default (save to the
 * user-defined directory and file, with prepositionning on the default
 * directory).
 * <UL>
 * <LI>Builds the current context from the display area
 * <LI>Opens a file chooser dialog specialised in context saving, gets the path
 * to save to from it (if non-default)
 * <LI>Uses the application's IContextManager to save the context
 * <LI>Logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class SaveSelectedContextAction extends BensikinAction {
	private boolean isSaveAs;

	/**
	 * Standard action constructor that sets the action's name, plus sets the
	 * isDefault attribute.
	 * 
	 * @param name
	 *            The action name
	 * @param _isDefault
	 *            True if the save action is a quick save to the working
	 *            directory and default file, false otherwise
	 */
	public SaveSelectedContextAction(String name, boolean _isSaveAs) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);

		this.isSaveAs = _isSaveAs;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		// ----------------Building the context to be saved
		Context selectedContext = Context.buildContextToBeSaved();

		if (selectedContext == null) {
			return;
		}

		IContextManager manager = ContextManagerFactory.getCurrentImpl();
		selectedContext.save(manager, this.isSaveAs);

		// System.out.println (
		// "SaveSelectedContextAction/selectedContext/"+selectedContext.getPath()+"/"
		// );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	/*
	 * public void actionPerformed ( ActionEvent actionEvent ) {
	 * //----------------Building the context to be saved Context
	 * selectedContext = Context.buildContextToBeSaved ();
	 * 
	 * if ( selectedContext == null ) { return; }
	 * 
	 * IContextManager manager = ContextManagerFactory.getCurrentImpl(); if (
	 * !this.isDefault ) { //open file chooser String location =
	 * manager.getSaveLocation(); if ( location == null || "".equals(
	 * location.trim() ) ) { location = manager.getDefaultSaveLocation(); }
	 * JFileChooser chooser = new JFileChooser(); chooser.setCurrentDirectory(
	 * new File( location ).getParentFile() ); ContextFileFilter filter = new
	 * ContextFileFilter(); chooser.addChoosableFileFilter( filter );
	 * 
	 * String title = Messages.getMessage(
	 * "DIALOGS_FILE_CHOOSER_SAVE_CONTEXT_TITLE" ); chooser.setDialogTitle(
	 * title );
	 * 
	 * int returnVal = chooser.showSaveDialog( BensikinFrame.getInstance() ); if
	 * ( returnVal == JFileChooser.APPROVE_OPTION ) { File f =
	 * chooser.getSelectedFile(); if ( f != null ) { String path =
	 * f.getAbsolutePath(); String extension = DataFileFilter.getExtension( f );
	 * String expectedExtension = filter.getExtension();
	 * 
	 * if ( extension == null || !extension.equalsIgnoreCase( expectedExtension
	 * ) ) { path += "."; path += expectedExtension; } if ( f.exists() ) { int
	 * ok = JOptionPane.showConfirmDialog( BensikinFrame.getInstance() ,
	 * Messages.getMessage( "DIALOGS_FILE_CHOOSER_FILE_EXISTS" ) ,
	 * Messages.getMessage( "DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE" ) ,
	 * JOptionPane.YES_NO_OPTION ); if ( ok == JOptionPane.OK_OPTION ) {
	 * manager.setSaveLocation( path ); ILogger logger =
	 * LoggerFactory.getCurrentImpl(); try { manager.saveContext(
	 * selectedContext , null );
	 * 
	 * String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_OK" );
	 * logger.trace( ILogger.LEVEL_DEBUG , msg ); } catch ( Exception e ) {
	 * String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_KO" );
	 * logger.trace( ILogger.LEVEL_ERROR , msg ); logger.trace(
	 * ILogger.LEVEL_ERROR , e ); return; } } } else { manager.setSaveLocation(
	 * path ); ILogger logger = LoggerFactory.getCurrentImpl(); try {
	 * manager.saveContext( selectedContext , null );
	 * 
	 * String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_OK" );
	 * logger.trace( ILogger.LEVEL_DEBUG , msg ); } catch ( Exception e ) {
	 * String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_KO" );
	 * logger.trace( ILogger.LEVEL_ERROR , msg ); logger.trace(
	 * ILogger.LEVEL_ERROR , e ); return; } } } } } else {
	 * manager.setSaveLocation( null ); ILogger logger =
	 * LoggerFactory.getCurrentImpl(); try { manager.saveContext(
	 * selectedContext , null );
	 * 
	 * String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_OK" );
	 * logger.trace( ILogger.LEVEL_DEBUG , msg ); } catch ( Exception e ) {
	 * String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_KO" );
	 * logger.trace( ILogger.LEVEL_ERROR , msg ); logger.trace(
	 * ILogger.LEVEL_ERROR , e ); return; } } }
	 */
}
