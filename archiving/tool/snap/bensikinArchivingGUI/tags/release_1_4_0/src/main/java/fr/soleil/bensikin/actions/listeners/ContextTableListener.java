//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/ContextTableListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextTableListener.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: ContextTableListener.java,v $
// Revision 1.9  2007/08/24 15:18:58  ounsy
// table popup menu added (Mantis bug 6285)
//
// Revision 1.8  2007/08/24 14:05:46  ounsy
// bug correction with context printing as text
//
// Revision 1.7  2007/08/24 12:53:10  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:06:09  ounsy
// modified the reset call to Snapshot.reset ( false); instead of Snapshot.reset ();
// not to wipe out everything snapshot-related so that the user can compare snapshots
// with different attributes
//
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
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
package fr.soleil.bensikin.actions.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.soleil.bensikin.actions.snapshot.FilterSnapshotsAction;
import fr.soleil.bensikin.components.context.list.ContextListTable;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotFilterPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.ContextListTableModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;

/**
 * Listens to double clicks on the contexts list table body. When a row is clicked, the selected context
 * becomes the current context.
 * <UL>
 * <LI> Checks the click is not a single click, if it is does nothing
 * <LI> Gets the index of the clicked row
 * <LI> Gets the ContextData at this row index, and build a Context with it
 * <LI> Loads all attributes for this Context
 * <LI> Resets all snapshots displays
 * <LI> Logs the action's success or failure
 * <LI> Displays the loaded context
 * </UL>
 *
 * @author CLAISSE
 */
public class ContextTableListener extends MouseAdapter implements ActionListener
{

    private JPopupMenu tableMenu;
    private JMenuItem  selectItem;

    public ContextTableListener() {
        super();
        tableMenu = new JPopupMenu();
        selectItem = new JMenuItem( Messages.getMessage("CONTEXT_LIST_VIEW_DETAILS") );
        selectItem.addActionListener( this );
        tableMenu.add(selectItem);
    }


    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed ( MouseEvent event )
    {
        if ( event.getButton() == MouseEvent.BUTTON1
                && event.getClickCount() > 1 ) { 
            selectContext();
        }
        else {
            if ( event.getButton() == MouseEvent.BUTTON3
                    || event.getButton() == MouseEvent.BUTTON2 ) {
                ContextListTable table = ContextListTable.getInstance();
                table.clearSelection();
                table.addRowSelectionInterval(
                        table.rowAtPoint( event.getPoint() ),
                        table.rowAtPoint( event.getPoint() )
                );
                tableMenu.show(
                        ContextListTable.getInstance(),
                        event.getX(),
                        event.getY()
                );
            }
        }
    }


    public void actionPerformed (ActionEvent e) {
        selectContext();
        tableMenu.setVisible(false);
    }

    private void selectContext() {
        ContextListTable source = ContextListTable.getInstance();
        int row = source.getSelectedRow();
        Criterions pattern = null;

        ContextListTableModel sourceModel = ( ContextListTableModel ) source.getModel();
        ContextData selectedContextData = sourceModel.getContextAtRow( row );

        Context selectedContext = new Context( selectedContextData );
        Context.setSelectedContext( selectedContext );

        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            selectedContext.loadAttributes( pattern );

            Snapshot.reset( false, true );

            //preparing snapshot filter
            Calendar today = Calendar.getInstance();
            today.set( Calendar.HOUR_OF_DAY , 0 );
            today.set( Calendar.MINUTE , 0 );
            today.set( Calendar.SECOND , 0 );
            today.set( Calendar.MILLISECOND , 0 );
            Timestamp dayStart = new Timestamp( today.getTimeInMillis() );
            String dayStartString = dayStart.toString().substring( 0 , dayStart.toString().length() - 2 );
            //starting filter
            SnapshotFilterPanel.getInstance().resetFields();
            SnapshotFilterPanel.getInstance().selectStartTime.setSelectedIndex( 1 );
            SnapshotFilterPanel.getInstance().textStartTime.setText( dayStartString );
            FilterSnapshotsAction.getInstance().actionPerformed( new ActionEvent( new JButton() , ActionEvent.ACTION_PERFORMED , "" ) );
            //end filtering

            String msg = Messages.getLogMessage( "LOAD_CONTEXT_ATTRIBUTES_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "LOAD_CONTEXT_ATTRIBUTES_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }

        selectedContext.push();
        ContextActionPanel.getInstance().allowPrint(true);
    }
}
