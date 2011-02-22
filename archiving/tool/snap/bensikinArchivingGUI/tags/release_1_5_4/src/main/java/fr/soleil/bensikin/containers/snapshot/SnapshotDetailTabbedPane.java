// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotDetailTabbedPane.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailTabbedPane.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: SnapshotDetailTabbedPane.java,v $
// Revision 1.8 2006/11/29 09:59:22 ounsy
// minor changes
//
// Revision 1.7 2006/10/31 16:54:08 ounsy
// milliseconds and null values management
//
// Revision 1.6 2005/12/14 16:25:39 ounsy
// removed attributes homogeneity control
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.snapshot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * A JTabbedPane containing as many tabs as there are selected snapshots
 * 
 * @author CLAISSE
 */
public class SnapshotDetailTabbedPane extends JTabbedPane {

    private static final long                                  serialVersionUID                 = -5650276831610054664L;

    private static SnapshotDetailTabbedPane                    snapshotDetailTabbedPaneInstance = null;
    private final static ImageIcon                             DOCUMENT_ICON                    = new ImageIcon(
                                                                                                        Bensikin.class
                                                                                                                .getResource("icons/document.gif"));
    private final static Color                                 NOT_LOADED_COLOR                 = Color.GRAY;
    private final static Color                                 LOADED_COLOR                     = Color.BLACK;

    /**
     * A Hashtable which keys are the opened tabs titles, and which values are
     * those tabs (of type SnapshotDetailTabbedPaneContent)
     */
    private Hashtable<String, SnapshotDetailTabbedPaneContent> tabsHashtable;

    public Hashtable<String, SnapshotDetailTabbedPaneContent> getTabsHashtable() {
        return tabsHashtable;
    }

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static SnapshotDetailTabbedPane getInstance() {
        if (snapshotDetailTabbedPaneInstance == null) {
            snapshotDetailTabbedPaneInstance = new SnapshotDetailTabbedPane();
        }

        return snapshotDetailTabbedPaneInstance;
    }

    /**
     * Default constructor. Sets its preferred size, and initializes the tabs
     * Hashtable <code>tabsHashtable</code>
     */
    private SnapshotDetailTabbedPane() {
        super();
        GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
        tabsHashtable = new Hashtable<String, SnapshotDetailTabbedPaneContent>();
        this.setPreferredSize(new Dimension(600, 200));
    }

    /**
     * Returns the currently selected SnapshotDetailTabbedPaneContent tab.
     * 
     * @return The currently selected SnapshotDetailTabbedPaneContent tab
     */
    public SnapshotDetailTabbedPaneContent getSelectedSnapshotDetailTabbedPaneContent() {
        SnapshotDetailTabbedPaneContent ret = (SnapshotDetailTabbedPaneContent) super
                .getSelectedComponent();
        return ret;
    }

    /**
     * Used to add a snapshot from the snapshots list. To add a snapshot loaded
     * from a file use addFileSnapshotDetail instead.
     * <p/>
     * Before adding the snapshot, checks that its attributes list is coherent
     * with the already selected snapshots. If it's not, warns the user and ask
     * for confirmation before 1/ emptying the current list 2/ adding it
     * <p/>
     * If the current selection already contains a snapshot with the same title,
     * the previous tab is replaced with the new one.
     * 
     * @param selectedSnapshot
     *            The Snapshot to add to the selection
     */
    public void addSnapshotDetail(Snapshot selectedSnapshot) {
        if (!this.checkAndWarn(selectedSnapshot)) {
            return;
        }

        int id = selectedSnapshot.getSnapshotData().getId();
        String tabTitle = String.valueOf(id);
        SnapshotDetailTabbedPaneContent newTab = new SnapshotDetailTabbedPaneContent(
                selectedSnapshot);
        GUIUtilities.setObjectBackground(newTab, GUIUtilities.SNAPSHOT_COLOR);

        if (!tabsHashtable.containsKey(String.valueOf(id))) {
            int insertPosition = getInsertPosition(id);
            tabsHashtable.put(tabTitle, newTab);
            this.insertTab(tabTitle, null, newTab, "", insertPosition);
        } else {
            SnapshotDetailTabbedPaneContent toHighlight = (SnapshotDetailTabbedPaneContent) tabsHashtable
                    .get(String.valueOf(id));
            this.remove(toHighlight);
            tabsHashtable.remove(tabTitle);

            int insertPosition = getInsertPosition(id);
            this.insertTab(tabTitle, null, newTab, "", insertPosition);
            tabsHashtable.put(tabTitle, newTab);
        }
        super.setSelectedComponent(newTab);
    }

    /**
     * Returns the tab index where a snapshot of id <code>id</code> should be
     * added. (because we want to have the tabs in increasing ids order)
     * 
     * @param id
     *            The id of the snapshot to add
     * @return The tab index where the snapshot will be added.
     */
    private int getInsertPosition(int id) {
        // If there are no opened tabs yet
        if (tabsHashtable.isEmpty()) {
            return 0;
        }

        Enumeration<String> enumer = tabsHashtable.keys();
        Vector<String> minusSet = new Vector<String>();
        while (enumer.hasMoreElements()) {
            String title = (String) enumer.nextElement();
            int titleId = -1;

            try {
                titleId = Integer.parseInt(title);
            } catch (NumberFormatException e) {
                // exception due to the tab title being a string representing
                // the snapshot's file name
                // do nothing, file snapshots are always first in the list
            }

            if (titleId - id > 0) {
                minusSet.add(String.valueOf(titleId));
                // if the current snapshot has a bigger id than the added
                // snapshot, it is placed after
            }
        }

        if (minusSet.isEmpty()) {
            return tabsHashtable.size();
        }

        String min = (String) Collections.min(minusSet);
        // The new index is the smallest index of all indexes of snapshots that
        // have a bigger id
        SnapshotDetailTabbedPaneContent last = (SnapshotDetailTabbedPaneContent) tabsHashtable
                .get(min);

        this.setSelectedComponent(last);
        return this.getSelectedIndex();
    }

    /**
     * Closes the currently selected tab.
     * <UL>
     * <LI>Finds the currently selected tab
     * <LI>Removes it from the tabs Hashtable <code>tabsHashtable</code>
     * <LI>Removes the corresponding snapshot's from the application's list of
     * opened snapshots, via Snapshot.removeSelectedSnapshot.
     * </UL>
     */
    public void closeTab() {
        SnapshotDetailTabbedPaneContent tabToRemove = (SnapshotDetailTabbedPaneContent) this
                .getSelectedComponent();

        int idx = this.indexOfComponent(tabToRemove);
        String keyToRemove = this.getTitleAt(idx);
        tabsHashtable.remove(keyToRemove);

        this.remove(tabToRemove);

        Snapshot snap = tabToRemove.getSnapshot();
        SnapshotData data = snap.getSnapshotData();
        Snapshot.removeSelectedSnapshot(data.getId());
    }

    /**
     * Closes all tabs.
     * <UL>
     * <LI>Close all tabs
     * <LI>Reinitializes the tabs Hashtable <code>tabsHashtable</code>
     * <LI>Empty the application's list of opened snapshots, via
     * removeAllSelectedSnapshots
     * </UL>
     */
    public void closeAllTabs() {
        this.removeAll();
        tabsHashtable = new Hashtable<String, SnapshotDetailTabbedPaneContent>();
        Snapshot.removeAllSelectedSnapshots();
    }

    /**
     * Used to add a snapshot from a file. To add a snapshot from the snapshots
     * list use addSnapshotDetail instead.
     * <p/>
     * Before adding the snapshot, checks that its attributes list is coherent
     * with the already selected snapshots. If it's not, warns the user and ask
     * for confirmation before 1/ emptying the current list 2/ adding it
     * <p/>
     * If the current selection already contains a snapshot with the same title,
     * the previous tab is replaced with the new one.
     * 
     * @param selectedSnapshot
     *            The Snapshot to add to the selection
     * @param fileName
     *            The file name
     */
    public void addFileSnapshotDetail(Snapshot selectedSnapshot, String fileName) {
        if (!this.checkAndWarn(selectedSnapshot)) {
            return;
        }

        SnapshotDetailTabbedPaneContent newTab = new SnapshotDetailTabbedPaneContent(
                selectedSnapshot);
        newTab.setEditCommentDisabled(true);

        if (!tabsHashtable.containsKey(fileName)) {
            tabsHashtable.put(fileName, newTab);

            this.insertTab(fileName, null, newTab, "", 0);
            this.setSelectedIndex(0);
        } else {
            SnapshotDetailTabbedPaneContent toHighlight = (SnapshotDetailTabbedPaneContent) tabsHashtable
                    .get(fileName);
            this.remove(toHighlight);
            tabsHashtable.remove(fileName);
            int insertPosition = 0;
            this.insertTab(fileName, null, newTab, "", insertPosition);
            tabsHashtable.put(fileName, newTab);

            super.setSelectedComponent(newTab);
        }
        setIconAt(this.getSelectedIndex(), DOCUMENT_ICON);
    }

    /**
     * Checks that selectedSnapshot's attributes list is coherent with the
     * already selected snapshots. If it's not, warns the user and ask for
     * confirmation.
     * 
     * @param selectedSnapshot
     *            The Snapshot to be added to the current selection
     * @return True if the snapshot is coherent, or if the user chooses to
     *         overwrite. False otherwise.
     */

    private boolean checkAndWarn(Snapshot selectedSnapshot) {
        /*
         * boolean areCoherent = this.checkAttributesAreHomogenous(
         * selectedSnapshot ); if ( !areCoherent ) { if (
         * !warnAttributesAreNotHomogenous() ) { return false; } else {
         * this.closeAllTabs(); return true; } }
         */
        return true;
    }

    // /**
    // * Opens a confirmation dialog to overwrite the current selection.
    // *
    // * @return True if user confirmed, false if user cancelled.
    // */
    /*
     * private boolean warnAttributesAreNotHomogenous () { String msgTitle =
     * Messages.getMessage(
     * "ADD_SNAPSHOT_TO_LIST_ATTRIBUTES_NOT_HOMOGENOUS_TITLE" ); String
     * msgConfirm = Messages.getMessage(
     * "ADD_SNAPSHOT_TO_LIST_ATTRIBUTES_NOT_HOMOGENOUS_MESSAGE" ); msgConfirm +=
     * GUIUtilities.CRLF; msgConfirm += Messages.getMessage(
     * "ADD_SNAPSHOT_TO_LIST_ATTRIBUTES_NOT_HOMOGENOUS_MESSAGE2" ); String
     * msgCancel = Messages.getMessage( "DIALOGS_SET_EQUIPMENTS_CANCEL" );
     * String msgValidate = Messages.getMessage(
     * "DIALOGS_SET_EQUIPMENTS_VALIDATE" ); Object[] options = {msgValidate,
     * msgCancel}; int confirm = JOptionPane.showOptionDialog ( null ,
     * msgConfirm , //message msgTitle , //title JOptionPane.DEFAULT_OPTION ,
     * JOptionPane.WARNING_MESSAGE , null , options , options[ 0 ] ); return
     * confirm == JOptionPane.OK_OPTION; }
     */
    // /**
    // * Checks that selectedSnapshot's attributes list is coherent with the
    // already selected snapshots.
    // *
    // * @param selectedSnapshot The Snapshot to be added to the current
    // selection
    // * @return True if the snapshot is coherent, False otherwise.
    // */
    /*
     * private boolean checkAttributesAreHomogenous ( Snapshot selectedSnapshot
     * ) { if ( tabsHashtable == null ) { return false; } if (
     * this.getTabCount() == 0 ) { return true; }
     * SnapshotDetailTabbedPaneContent content = (
     * SnapshotDetailTabbedPaneContent ) this.getComponentAt( 0 );//we only need
     * to check one, since it's all or nothing Snapshot snapshot =
     * content.getSnapshot(); boolean areCoherent =
     * selectedSnapshot.hasSameAttributesAs( snapshot ); return areCoherent; }
     */

    /**
     * Returns the title of the wanted Snapshot.
     * 
     * @param title
     *            The title of the wanted Snapshot
     * @return The Snapshot that has the passed title
     */
    public Snapshot getSnapshotForTitle(String title) {
        if (tabsHashtable == null) {
            return null;
        }

        if (title == null) {
            return null;
        }
        title = title.trim();

        SnapshotDetailTabbedPaneContent snapshotDetailTabbedPaneContent = (SnapshotDetailTabbedPaneContent) tabsHashtable
                .get(title);
        if (snapshotDetailTabbedPaneContent == null) {
            return null;
        }

        return snapshotDetailTabbedPaneContent.getSnapshot();
    }

    /**
     * Sets the enabled state of the EditComment Action for all tabs
     * 
     * @param disabled
     *            True if the action has to be disabled
     */
    public void setEditCommentDisabledForTabs(boolean disabled) {
        if (tabsHashtable == null) {
            return;
        }
        Enumeration<String> enumer = tabsHashtable.keys();
        while (enumer.hasMoreElements()) {
            String title = (String) enumer.nextElement();
            SnapshotDetailTabbedPaneContent snapshotDetailTabbedPaneContent = (SnapshotDetailTabbedPaneContent) tabsHashtable
                    .get(title);

            snapshotDetailTabbedPaneContent.setEditCommentDisabled(disabled);
        }
    }

    /**
     * Overriding method to load the attributes table only when it is displayed
     * 
     * @param index
     *            index of the tab to display
     */
    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        SnapshotDetailTabbedPaneContent content = getSelectedSnapshotDetailTabbedPaneContent();
        if (content != null) {
            Snapshot snap = content.getSnapshot();
            if (snap != null) {
                if (snap.isLoadable()) {
                    try {
                        snap.loadAttributes();
                        content.reload();
                    } catch (SnapshotingException e) {
                        e.printStackTrace();
                    }
                }
            }
            content.revalidate();
            content.repaint();
        }
        updateTabFont(index);
        content = null;
    }

    private void updateTabFont(int tab) {
        if (tab > -1) {
            SnapshotDetailTabbedPaneContent content = (SnapshotDetailTabbedPaneContent) getComponentAt(tab);
            if ((!content.getSnapshot().isLoadable())
                    || (content.getSnapshot().getSnapshotAttributes() == null)
                    || (content.getSnapshot().getSnapshotAttributes()
                            .getSnapshotAttributes() == null)) {
                setForegroundAt(tab, NOT_LOADED_COLOR);
                setToolTipTextAt(tab, getTitleAt(tab) + " "
                        + Messages.getMessage("SNAPSHOT_TITLE_NOT_LOADED"));
            } else {
                setForegroundAt(tab, LOADED_COLOR);
                setToolTipTextAt(tab, getTitleAt(tab));
            }
            content = null;
        }
    }

    @Override
    public void setIconAt(int index, Icon icon) {
        Component comp = getTabComponentAt(index);
        if (comp instanceof JLabel) {
            ((JLabel) comp).setIcon(icon);
        } else {
            super.setIconAt(index, icon);
        }
        comp = null;
    }
}
