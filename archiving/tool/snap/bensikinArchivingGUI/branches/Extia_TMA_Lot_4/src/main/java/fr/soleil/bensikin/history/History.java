// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/History.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class History.
// (Claisse Laurent) - 8 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.12 $
//
// $Log: History.java,v $
// Revision 1.12 2007/08/24 14:09:55 ounsy
// bug correction with context printing as text
//
// Revision 1.11 2006/11/29 10:02:31 ounsy
// minor changes
//
// Revision 1.10 2006/03/21 11:26:37 ounsy
// added a key for storing a favorite context's user-defined label
//
// Revision 1.9 2006/02/15 09:22:28 ounsy
// avoiding ArrayOutOfBoundsException
//
// Revision 1.8 2006/01/13 13:25:59 ounsy
// avoiding NullPointerException
//
// Revision 1.7 2005/12/14 16:39:44 ounsy
// minor changes
//
// Revision 1.6 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:39 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.history;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.datasources.snapdb.SnapManagerFactory;
import fr.soleil.bensikin.history.context.ContextHistory;
import fr.soleil.bensikin.history.context.OpenedContextRef;
import fr.soleil.bensikin.history.context.SelectedContextRef;
import fr.soleil.bensikin.history.snapshot.OpenedSnapshotRef;
import fr.soleil.bensikin.history.snapshot.SelectedSnapshotRef;
import fr.soleil.bensikin.history.snapshot.SnapshotHistory;
import fr.soleil.bensikin.models.ContextListTableModel;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.*;

/**
 * Contains the application's history, eg. what contexts/snashots were
 * opened/selected at the time of the latest shutdown.
 * 
 * @author CLAISSE
 */
public class History {

    private SnapshotHistory    snapshotHistory;
    private ContextHistory     contextHistory;

    private static History     history;

    /**
     * The XML tag name used in saving/loading
     */
    public static final String XML_TAG                = "history";

    /**
     * The XML tag name for the contexts node
     */
    public static final String CONTEXTS_KEY           = "contexts";

    /**
     * The XML tag name for the selected context node
     */
    public static final String SELECTED_CONTEXT_KEY   = "selectedContext";

    /**
     * The XML tag name for the opened contexts node
     */
    public static final String OPENED_CONTEXTS_KEY    = "openedContexts";

    /**
     * The XML tag name for an opened context node
     */
    public static final String OPENED_CONTEXT_KEY     = "openedContext";

    /**
     * The XML tag name for the snapshots node
     */
    public static final String SNAPSHOTS_KEY          = "snapshots";

    /**
     * The XML tag name for the selected snapshots node
     */
    public static final String SELECTED_SNAPSHOTS_KEY = "selectedSnapshots";

    /**
     * The XML tag name for a selected snapshot node
     */
    public static final String SELECTED_SNAPSHOT_KEY  = "selectedSnapshot";

    /**
     * The XML tag name for the opened snapshots node
     */
    public static final String OPENED_SNAPSHOTS_KEY   = "openedSnapshots";

    /**
     * The XML tag name for an opened snapshot node
     */
    public static final String OPENED_SNAPSHOT_KEY    = "openedSnapshot";

    /**
     * The XML tag name used for the "id" property
     */
    public static final String ID_KEY                 = "id";

    /**
     * The XML tag name used for the "highlighted" property
     */
    public static final String HIGHLIGHTED_KEY        = "highlighted";

    /**
     * The XML tag name used for the "label" property
     */
    public static final String LABEL_KEY              = "label";

    /**
     * Builds directly given the Snapshot history and the Context history.
     * 
     * @param _snapshotHistory
     *            the Snapshot history
     * @param _contextHistory
     *            the Context history
     */
    public History(SnapshotHistory _snapshotHistory,
            ContextHistory _contextHistory) {
        this.snapshotHistory = _snapshotHistory;
        this.contextHistory = _contextHistory;
    }

    /**
     * Loads the opened contexts into a Hashtable.
     * 
     * @return A Hashtable which keys are the opened contexts' ids and which
     *         values are those contexts.
     * @throws Exception
     */
    private Hashtable<String, Context> loadOpenedContextsFromHistory()
            throws SnapshotingException, Exception {
        Hashtable<String, Context> openedContexts = new Hashtable<String, Context>();

        ContextHistory contextHistory = this.getContextHistory();
        OpenedContextRef[] openedContextRefs = contextHistory
                .getOpenedContextRefs();

        ISnapManager snapManager = SnapManagerFactory.getCurrentImpl();

        Criterions searchCriterions;
        for (int i = 0; i < openedContextRefs.length; i++) {
            int id = openedContextRefs[i].getId();
            Condition condition = new Condition(GlobalConst.TAB_CONTEXT[0],
                    "=", String.valueOf(id));

            searchCriterions = new Criterions();
            searchCriterions.addCondition(condition);

            // Condition[] cond = searchCriterions.getConditions(
            // GlobalConst.TAB_CONTEXT[ 0 ] );

            SnapContext[] snapContexts = snapManager
                    .findContexts(searchCriterions);
            // avoiding ArrayIndexOutOfBoundsException
            if (snapContexts.length > 0) {
                ContextData contextData = new ContextData(snapContexts[0]);

                openedContexts
                        .put(String.valueOf(id), new Context(contextData));
            }
        }

        return openedContexts;
    }

    /**
     * Returns the selected Context.
     * 
     * @return The selected Context
     * @throws SnapshotingException
     * @throws Exception
     */
    private Context loadSelectedContext() throws SnapshotingException,
            Exception {
        ContextHistory contextHistory = this.getContextHistory();
        SelectedContextRef selectedContextRef = contextHistory
                .getSelectedContextRef();
        if (selectedContextRef == null) {
            return null;
        }

        ISnapManager snapManager = SnapManagerFactory.getCurrentImpl();

        Condition condition = new Condition(GlobalConst.TAB_CONTEXT[0], "=",
                String.valueOf(selectedContextRef.getId()));
        Criterions searchCriterions = new Criterions();
        searchCriterions.addCondition(condition);

        SnapContext[] snapContexts = snapManager.findContexts(searchCriterions);
        ContextData contextData = null;
        Context context;
        // avoiding NullPointerException
        if (snapContexts == null || snapContexts.length == 0) {
            contextData = new ContextData();
            context = new Context();
        } else {
            contextData = new ContextData(snapContexts[0]);
            context = new Context(contextData);
        }

        context.loadAttributes(null);

        return context;
    }

    /**
     * Builds and return a History object representing the current state of the
     * application
     * 
     * @return The current History
     */
    public static History getCurrentHistory() {
        Context selectedContext = Context.getSelectedContext();

        Hashtable<String, Context> htOpenedContexts = Context
                .getOpenedContexts();
        Context[] openedContexts = new Context[htOpenedContexts.size()];
        Enumeration<String> e = htOpenedContexts.keys();
        int i = 0;
        while (e.hasMoreElements()) {
            Object next = e.nextElement();
            Context c = (Context) htOpenedContexts.get(next);
            openedContexts[i] = c;
            i++;
        }

        ContextHistory contextHistory = new ContextHistory(selectedContext,
                openedContexts);

        // SNAPSHOTS
        Hashtable<String, Snapshot> htOpenedSnapshots = Snapshot
                .getOpenedSnapshots();
        Snapshot[] openedSnapshots = new Snapshot[htOpenedSnapshots.size()];
        Enumeration<String> e1 = htOpenedSnapshots.keys();
        int i1 = 0;
        while (e1.hasMoreElements()) {
            Object next = e1.nextElement();
            Snapshot c = (Snapshot) htOpenedSnapshots.get(next);
            openedSnapshots[i1] = c;
            i1++;
        }

        Hashtable<String, Snapshot> htSelectedSnapshots = Snapshot
                .getSelectedSnapshots();
        Snapshot[] selectedSnapshots = new Snapshot[htSelectedSnapshots.size()];
        Enumeration<String> e2 = htSelectedSnapshots.keys();
        int i2 = 0;
        while (e2.hasMoreElements()) {
            Object next = e2.nextElement();
            Snapshot c = (Snapshot) htSelectedSnapshots.get(next);
            selectedSnapshots[i2] = c;
            i2++;
        }

        SnapshotHistory snapshotHistory = new SnapshotHistory(
                selectedSnapshots, openedSnapshots);

        return new History(snapshotHistory, contextHistory);
    }

    /**
     * Returns a XML representation of the history.
     * 
     * @return a XML representation of the history
     */
    public String toString() {
        String ret = "";

        ret += new XMLLine(History.XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
        ret += GUIUtilities.CRLF;

        if (this.contextHistory != null) {
            ret += this.contextHistory.toString();
            ret += GUIUtilities.CRLF;
        }

        if (this.snapshotHistory != null) {
            ret += this.snapshotHistory.toString();
        }

        ret += new XMLLine(History.XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

        return ret;
    }

    /**
     * Loads the opened snapshots into a Hashtable.
     * 
     * @param selectedContext
     *            The context to which the snapshots are rattached
     * @return A Hashtable which keys are the opened snapshots' ids and which
     *         values are those snapshots.
     * @throws SnapshotingException
     * @throws Exception
     */
    private Hashtable<String, Snapshot> loadOpenedSnapshotsFromHistory(
            Context selectedContext) throws SnapshotingException, Exception {
        Hashtable<String, Snapshot> openedSnapshots = new Hashtable<String, Snapshot>();

        SnapshotHistory snapshotHistory = this.getSnapshotHistory();
        OpenedSnapshotRef[] openedSnapshotRefs = snapshotHistory
                .getOpenedSnapshotRefs();

        ISnapManager snapManager = SnapManagerFactory.getCurrentImpl();

        Criterions searchCriterions;
        for (int i = 0; i < openedSnapshotRefs.length; i++) {
            int id = openedSnapshotRefs[i].getId();
            Condition condition = new Condition(GlobalConst.TAB_SNAP[0], "=",
                    String.valueOf(id));
            searchCriterions = new Criterions();
            searchCriterions.addCondition(condition);

            // Condition[] cond = searchCriterions.getConditions(
            // GlobalConst.TAB_SNAP[ 0 ] );

            SnapShotLight[] snapshots = snapManager
                    .findSnapshots(searchCriterions);
            // avoiding ArrayIndexOutOfBoundsException
            if (snapshots.length > 0) {
                SnapshotData snapshotData = new SnapshotData(snapshots[0]);
                openedSnapshots.put(String.valueOf(id), new Snapshot(
                        selectedContext, snapshotData));
            }
        }

        return openedSnapshots;
    }

    /**
     * Loads the selected snapshots into a Hashtable.
     * 
     * @param selectedContext
     * @return A Hashtable which keys are the selected snapshots' ids and which
     *         values are those snapshots.
     * @throws SnapshotingException
     * @throws Exception
     */
    private Hashtable<String, Snapshot> loadSelectedSnapshotsFromHistory(
            Context selectedContext) throws SnapshotingException, Exception {
        Hashtable<String, Snapshot> selectedSnapshots = new Hashtable<String, Snapshot>();

        SnapshotHistory snapshotHistory = this.getSnapshotHistory();
        SelectedSnapshotRef[] selectedSnapshotRefs = snapshotHistory
                .getSelectedSnapshotRefs();

        ISnapManager snapManager = SnapManagerFactory.getCurrentImpl();

        Criterions searchCriterions;

        for (int i = 0; i < selectedSnapshotRefs.length; i++) {
            int id = selectedSnapshotRefs[i].getId();
            Condition condition = new Condition(GlobalConst.TAB_SNAP[0], "=",
                    String.valueOf(id));
            searchCriterions = new Criterions();
            searchCriterions.addCondition(condition);

            // Condition[] cond = searchCriterions.getConditions(
            // GlobalConst.TAB_SNAP[ 0 ] );

            SnapShotLight[] snapshots = snapManager
                    .findSnapshots(searchCriterions);
            SnapshotData snapshotData;
            // avoiding ArrayOutOfBoundsException
            if (snapshots == null || snapshots.length == 0) {
                snapshotData = new SnapshotData();
            } else {
                snapshotData = new SnapshotData(snapshots[0]);
            }
            Snapshot snapshot = new Snapshot(selectedContext, snapshotData);
            // We don't load attributes here, they will be loaded when
            // setSelectedIndex method will be invoked.
            // We only notify which tab will be fully loaded by setting the
            // highlight flag
            if (selectedSnapshotRefs[i].isHightlighted()) {
                snapshot.getSnapshotData().setHighlighted(true);
            }
            selectedSnapshots.put(String.valueOf(id), snapshot);
        }

        return selectedSnapshots;
    }

    /**
     * Applies the opened/selected state of snapshots/contexts contained in this
     * History to the static references in Context/Snapshot.
     * 
     * @throws SnapshotingException
     * @throws Exception
     */
    public void setOpenedAndSelectedEntities() throws SnapshotingException,
            Exception {
        Hashtable<String, Context> openedContexts = this
                .loadOpenedContextsFromHistory();
        Context.setOpenedContexts(openedContexts);

        Context selectedContext = this.loadSelectedContext();
        if (selectedContext == null) {
            return;
            // we don't load the snapshots because they don't have an associated
            // context
        }
        Context.setSelectedContext(selectedContext);

        Hashtable<String, Snapshot> openedSnapshots = this
                .loadOpenedSnapshotsFromHistory(selectedContext);
        Snapshot.setOpenedSnapshots(openedSnapshots);

        Hashtable<String, Snapshot> selectedSnapshots = this
                .loadSelectedSnapshotsFromHistory(selectedContext);
        Snapshot.setSelectedSnapshots(selectedSnapshots);
    }

    /**
     * Displays on the screen the opened/selected state of snapshots/contexts
     * contained in this History.
     */
    public void push() {
        this.pushOpenedContexts();
        this.pushSelectedContext();
        this.pushOpenedSnapshots();
        this.pushSelectedSnapshots();

        // We focus on the tab that was selected the last time the user closed
        // Bensikin (which is the only one containing highlighted="true" in
        // history.xml)
        SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane
                .getInstance();
        Hashtable<String, SnapshotDetailTabbedPaneContent> tabs = tabbedPane
                .getTabsHashtable();
        String highlighted = null;

        for (String tabId : tabs.keySet()) {
            SnapshotDetailTabbedPaneContent tab = tabs.get(tabId);
            tab.getSnapshot().setLoadable(true);
            if (tab.getSnapshot().getSnapshotData().isHighlighted()) {
                highlighted = tabId;
            }
        }
        if (highlighted == null) {
            // backward compatibility
            int index = tabbedPane.getSelectedIndex();
            if (index > -1) {
                SnapshotDetailTabbedPaneContent tab = (SnapshotDetailTabbedPaneContent) tabbedPane
                        .getComponentAt(index);
                tab.getSnapshot().setLoadable(true);
                tabbedPane.setSelectedIndex(index);
            }
        } else {
            tabbedPane.setSelectedComponent(tabs.get(highlighted));
        }
    }

    /**
     * Displays on screen the selected state of snapshots contained in this
     * History.
     */
    private void pushSelectedSnapshots() {
        Hashtable<String, Snapshot> selectedSnapshots = Snapshot
                .getSelectedSnapshots();
        if (selectedSnapshots == null) {
            return;
        }

        Iterator<Snapshot> it = selectedSnapshots.values().iterator();

        int i = 0;
        SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane
                .getInstance();
        while (it.hasNext()) {
            Snapshot nextSnapshot = it.next();
            tabbedPane.addSnapshotDetail(nextSnapshot);
            i++;
        }
    }

    /**
     * Displays onscreen the opened state of snapshots contained in this
     * History.
     */
    private void pushOpenedSnapshots() {
        Hashtable<String, Snapshot> openedSnapshots = Snapshot
                .getOpenedSnapshots();
        if (openedSnapshots == null) {
            return;
        }

        Iterator<Snapshot> it = openedSnapshots.values().iterator();
        int size = openedSnapshots.values().size();
        SnapshotData[] rows = new SnapshotData[size];
        int i = 0;
        while (it.hasNext()) {
            Snapshot nextSnapshot = it.next();
            rows[i] = nextSnapshot.getSnapshotData();
            i++;
        }

        SnapshotListTableModel model = SnapshotListTableModel.getInstance();
        model.setRows(rows);
    }

    /**
     * Displays onscreen the opened state of contexts contained in this History.
     */
    private void pushOpenedContexts() {
        Hashtable<String, Context> openedContexts = Context.getOpenedContexts();
        if (openedContexts != null) {
            Iterator<Context> it = openedContexts.values().iterator();
            int size = openedContexts.values().size();
            ContextData[] rows = new ContextData[size];
            int i = 0;
            while (it.hasNext()) {
                Context nextContext = it.next();
                rows[i] = nextContext.getContextData();
                i++;
            }
            ContextListTableModel model = ContextListTableModel.getInstance();
            model.setRows(rows);
        }
    }

    /**
     * Displays onscreen the selected state of contexts contained in this
     * History.
     */
    private void pushSelectedContext() {
        Context selectedContext = Context.getSelectedContext();
        if (selectedContext != null) {
            selectedContext.push();
            ContextAttributesTree.getInstance().expandAll(true);
        }
        ContextActionPanel.getInstance().allowPrint(true);
    }

    /**
     * @return Returns the history.
     */
    public static History getHistory() {
        return history;
    }

    /**
     * @param history
     *            The history to set.
     */
    public static void setHistory(History history) {
        History.history = history;
    }

    /**
     * @return Returns the contextHistory.
     */
    public ContextHistory getContextHistory() {
        return contextHistory;
    }

    /**
     * @param contextHistory
     *            The contextHistory to set.
     */
    public void setContextHistory(ContextHistory contextHistory) {
        this.contextHistory = contextHistory;
    }

    /**
     * @return Returns the snapshotHistory.
     */
    public SnapshotHistory getSnapshotHistory() {
        return snapshotHistory;
    }

    /**
     * @param snapshotHistory
     *            The snapshotHistory to set.
     */
    public void setSnapshotHistory(SnapshotHistory snapshotHistory) {
        this.snapshotHistory = snapshotHistory;
    }
}
