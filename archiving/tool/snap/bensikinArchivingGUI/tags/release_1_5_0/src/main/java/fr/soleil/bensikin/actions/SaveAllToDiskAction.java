//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/SaveAllToDiskAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SaveAllToDiskAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.3 $
//
//$Log: SaveAllToDiskAction.java,v $
//Revision 1.3  2006/04/10 08:46:41  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.2  2005/12/14 15:59:37  ounsy
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
import javax.swing.ImageIcon;

import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.manager.ContextManagerFactory;
import fr.soleil.bensikin.data.context.manager.IContextManager;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.manager.ISnapshotManager;
import fr.soleil.bensikin.data.snapshot.manager.SnapshotManagerFactory;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.tools.Messages;



/**
 * An action that does a quick save on the currently selected context and snapshot.
 *
 * @author CLAISSE
 */
public class SaveAllToDiskAction extends BensikinAction
{
    /**
     * Standard action constructor that sets the action's name and icon.
     *
     * @param name The action name
     * @param icon The action icon
     */
    public SaveAllToDiskAction ( String name , ImageIcon icon )
    {
        super( name , icon );
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        ILogger logger = LoggerFactory.getCurrentImpl();

        IContextManager archManager = ContextManagerFactory.getCurrentImpl();
        //archManager.setSaveLocation( null );//if this.isDefault

        //   ----------------Building th context to be saved
        Context selectedContext = Context.buildContextToBeSaved ();

        //----------------Building th context to be saved
        if ( selectedContext != null )
        {
            try
            {
                archManager.saveContext( selectedContext , null );

                String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_OK" );
                logger.trace( ILogger.LEVEL_DEBUG , msg );

                //selectedArchivingConfiguration.push ();
            }
            catch ( Exception e )
            {
                String msg = Messages.getLogMessage( "SAVE_CONTEXT_ACTION_KO" );
                logger.trace( ILogger.LEVEL_ERROR , msg );
                logger.trace( ILogger.LEVEL_ERROR , e );
            }
        }

        ISnapshotManager viewManager = SnapshotManagerFactory.getCurrentImpl();
        //viewManager.setSaveLocation( null );//if this.isDefault

        SnapshotDetailTabbedPane snapshotDetailTabbedPane = SnapshotDetailTabbedPane.getInstance();
        SnapshotDetailTabbedPaneContent selectedContent = snapshotDetailTabbedPane.getSelectedSnapshotDetailTabbedPaneContent();
        Snapshot snapshot = null;
        if ( selectedContent != null )
        {
            snapshot = selectedContent.getSnapshot();
        }
        if ( snapshot != null )
        {
            try
            {
                viewManager.saveSnapshot( snapshot , null );

                String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_OK" );
                logger.trace( ILogger.LEVEL_DEBUG , msg );

                //selectedViewConfiguration.push ();
            }
            catch ( Exception e )
            {
                String msg = Messages.getLogMessage( "SAVE_SNAPSHOT_ACTION_KO" );
                logger.trace( ILogger.LEVEL_ERROR , msg );
                logger.trace( ILogger.LEVEL_ERROR , e );
            }
        }
    }

}
