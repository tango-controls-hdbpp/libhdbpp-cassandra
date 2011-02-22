// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/CompareSnapshotsAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AddSnapshotToCompareAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: CompareSnapshotsAction.java,v $
// Revision 1.4 2006/06/28 12:45:47 ounsy
// minor changes
//
// Revision 1.3 2006/04/10 08:47:14 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2005/12/14 16:08:41 ounsy
// permitted the case secondSnapshot==null for comparison with current
// machine state
//
// Revision 1.1 2005/12/14 14:07:18 ounsy
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
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailComparePanel;
import fr.soleil.bensikin.containers.sub.dialogs.SnapshotCompareDialog;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;

/**
 * Launches the comparison between two precedently selected snapshots.
 * <UL>
 * <LI>Gets the static references to both snapshots in Snapshot; if one is null,
 * does nothing
 * <LI>Opens a SnapshotCompareDialog dialog for those 2 snapshots
 * </UL>
 * 
 * @author CLAISSE
 */
public class CompareSnapshotsAction extends BensikinAction {

    private static final long                serialVersionUID = 6475934675904572797L;
    private static CompareSnapshotsAction    instance         = null;
    private ArrayList<SnapshotCompareDialog> openedDialogs;

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @param name
     *            The action's name
     * @return The instance
     */
    public static CompareSnapshotsAction getInstance(String name) {
        if (instance == null) {
            instance = new CompareSnapshotsAction(name);
        }

        return instance;
    }

    /**
     * Returns the existing instance
     * 
     * @return The existing instance
     */
    public static CompareSnapshotsAction getInstance() {
        return instance;
    }

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action name
     */
    private CompareSnapshotsAction(String name) {
        super();
        this.putValue(Action.NAME, name);
        openedDialogs = new ArrayList<SnapshotCompareDialog>();
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        // SnapshotDetailComparePanel comparePanel =
        SnapshotDetailComparePanel.getInstance();

        try {
            Snapshot sn1 = Snapshot.getFirstSnapshotOfComparison();
            Snapshot sn2 = Snapshot.getSecondSnapshotOfComparison();

            if (sn1 == null) {
                return;
            }
            if (sn2 == null) {
                sn2 = Snapshot.getCurrentStateSnapshot(sn1);
                /*
                 * System.out.println (
                 * "-------------------CURRENT------------------" );
                 * System.out.println ( sn2.toString() ); System.out.println (
                 * "-------------------CURRENT------------------" );
                 */
                StringBuffer buffer = new StringBuffer(
                        Messages
                                .getMessage("SNAPSHOT_DETAIL_COMPARE_CURRENT_STATE_SN_NAME"));
                buffer.append('(');
                buffer.append(Snapshot.getFirstSnapshotOfComparisonTitle());
                buffer.append(')');
                Snapshot.setSecondSnapshotOfComparisonTitle(buffer.toString());
                buffer = null;
            }

            final SnapshotCompareDialog snapshotCompareDialog = new SnapshotCompareDialog(
                    sn1, sn2);
            snapshotCompareDialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    openedDialogs.remove(snapshotCompareDialog);
                }

            });
            openedDialogs.add(snapshotCompareDialog);

            snapshotCompareDialog.pack();
            snapshotCompareDialog.setLocationRelativeTo(BensikinFrame
                    .getInstance());
            snapshotCompareDialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SnapshotCompareDialog> getOpenedDialogs() {
        return openedDialogs;
    }
}
