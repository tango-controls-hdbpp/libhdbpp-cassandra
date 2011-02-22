// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/SnapshotListTableListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotListTableListener.
// (Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotListTableListener.java,v $
// Revision 1.6 2007/08/24 15:18:58 ounsy
// table popup menu added (Mantis bug 6285)
//
// Revision 1.5 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:34 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.actions.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.soleil.bensikin.components.snapshot.list.SnapshotListTable;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.tools.Messages;

/**
 * Listens to double clicks on the snapshots list table body. When a row is clicked, the selected
 * snapshot is added to the snapshots selection.
 * <UL>
 * <LI>Checks the click is not a single click, if it is does nothing
 * <LI>Gets the index of the clicked row
 * <LI>Gets the SnapshotData at this row index, and build a Snapshot with it
 * <LI>Loads all attributes for this Snapshot
 * <LI>Logs the action's success or failure
 * <LI>Displays the loaded snapshot
 * </UL>
 * 
 * @author CLAISSE
 */
public class SnapshotListTableListener extends MouseAdapter implements ActionListener {

    private JPopupMenu tableMenu;
    private JMenuItem selectItem;

    public SnapshotListTableListener() {
        super();
        tableMenu = new JPopupMenu();
        selectItem = new JMenuItem(Messages.getMessage("SNAPSHOT_LIST_VIEW_DETAILS"));
        selectItem.addActionListener(this);
        tableMenu.add(selectItem);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() > 1) {
            selectSnapshot();
        }
        else {
            if (event.getButton() == MouseEvent.BUTTON3 || event.getButton() == MouseEvent.BUTTON2) {
                SnapshotListTable table = SnapshotListTable.getInstance();
                table.clearSelection();
                table.addRowSelectionInterval(table.rowAtPoint(event.getPoint()), table
                        .rowAtPoint(event.getPoint()));
                tableMenu.show(SnapshotListTable.getInstance(), event.getX(), event.getY());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectSnapshot();
    }

    private void selectSnapshot() {
        SnapshotListTable source = SnapshotListTable.getInstance();
        int row = source.getSelectedRow();

        SnapshotListTableModel sourceModel = (SnapshotListTableModel) source.getModel();
        SnapshotData selectedSnapshotData = sourceModel.getSnapshotDataAtRow(row);

        Snapshot selectedSnapshot = new Snapshot(selectedSnapshotData);
        selectedSnapshotData.setSnapshot(selectedSnapshot);
        Snapshot.addSelectedSnapshot(selectedSnapshot);
        selectedSnapshot.setLoadable(true);

        SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane.getInstance();
        tabbedPane.addSnapshotDetail(selectedSnapshot);
    }

}
