// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/Snapshot.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Snapshot.
// (Garda Laure) - 22 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.17 $
//
// $Log: Snapshot.java,v $
// Revision 1.17 2007/10/15 13:17:42 soleilarc
// Author: XP
// Mantis bug ID: 6695
// Comment: For the setEquipments method, change the Exception exception into a
// SnapshotingException exception, after the throws key-word.
//
// Revision 1.16 2007/08/24 12:52:56 ounsy
// minor changes
//
// Revision 1.15 2006/11/29 10:01:10 ounsy
// minor changes
//
// Revision 1.14 2006/04/13 12:37:15 ounsy
// minor changes
//
// Revision 1.13 2006/04/13 12:27:20 ounsy
// cleaned the code
//
// Revision 1.12 2006/03/29 10:23:13 ounsy
// small modification
//
// Revision 1.11 2006/03/20 15:49:40 ounsy
// removed useless logs
//
// Revision 1.10 2006/02/23 10:06:16 ounsy
// Bug corrected : when 1 attribute is modified, the other ones are not set
// modified
//
// Revision 1.9 2006/02/15 09:19:49 ounsy
// minor changes : uncomment to debug
//
// Revision 1.8 2006/01/13 13:25:45 ounsy
// File replacement warning
//
// Revision 1.7 2005/12/14 16:35:21 ounsy
// added methods necessary for the new Word-like file management
//
// Revision 1.6 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:37 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.data.snapshot;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.components.renderers.SnapshotDetailRenderer;
import fr.soleil.bensikin.components.snapshot.SnapshotFileFilter;
import fr.soleil.bensikin.components.snapshot.list.SnapshotListTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailComparePanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.data.snapshot.manager.ISnapshotManager;
import fr.soleil.bensikin.datasources.snapdb.SnapManagerFactory;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * A pivotal class of Bensikin, this class is the upper level modelisation of a
 * snapshot.
 * <p/>
 * A Snapshot object has two main attributes: - a SnapshotData attribute
 * representing all non-attribute-dependant information - a SnapshotAttributes
 * attribute representing all attribute-dependant information and a reference to
 * the context of this snapshot.
 * <p/>
 * The Context class also saves static references to the currently opened
 * contexts, and the selected context.
 * 
 * @author CLAISSE
 */
public class Snapshot {

    private Context                            context;
    private SnapshotData                       snapshotData;
    private SnapshotAttributes                 snapshotAttributes                = null;

    private static Hashtable<String, Snapshot> selectedSnapshots                 = new Hashtable<String, Snapshot>();
    private static Hashtable<String, Snapshot> openedSnapshots                   = new Hashtable<String, Snapshot>();

    private static String                      snapshotDefaultComment;

    private static Snapshot                    firstSnapshotOfComparison;
    private static Snapshot                    secondSnapshotOfComparison;
    private static String                      firstSnapshotOfComparisonTitle;
    private static String                      secondSnapshotOfComparisonTitle;

    /**
     * The XML tag name used in saving/loading
     */
    public static final String                 XML_TAG                           = "Snapshot";
    private boolean                            isModified;

    public static final String                 BENSIKIN_AUTOMATIC_INSERT_COMMENT = "BENSIKIN_AUTOMATIC_SNAPSHOT";

    private boolean                            loadable                          = false;

    /**
     * Sets the snapshotData and context attributes
     * 
     * @param _context
     * @param _snapshotData
     */
    public Snapshot(Context _context, SnapshotData _snapshotData) {
        this.snapshotData = _snapshotData;
        this.context = _context;
    }

    /**
     * Default constructor. The snapshotData and snapshotAttributes attributes
     * are null.
     */
    public Snapshot() {

    }

    /**
     * @param saveLocation
     */
    public void setPath(String path) {
        if (this.snapshotData != null) {
            this.snapshotData.setPath(path);
        }

    }

    public String getPath() {
        if (this.snapshotData != null) {
            return this.snapshotData.getPath();
        } else {
            return null;
        }
    }

    /**
     * Looks up in database the referenced attributes for this snapshot. Once
     * the attributes are found ,they are converted to the SnasphotAttributes
     * attribute.
     * 
     * @throws SnapshotingException
     */
    public void loadAttributes() throws SnapshotingException {
        if ((getSnapshotAttributes() == null)
                || (getSnapshotAttributes().getSnapshotAttributes() == null)
                || (getSnapshotAttributes().getSnapshotAttributes().length == 0)) {
            ISnapManager source = SnapManagerFactory.getCurrentImpl();
            SnapShotLight light = this.getSnapshotData().getAsSnapShotLight();

            SnapAttributeExtract[] sae = source.findSnapshotAttributes(light);

            SnapshotAttributes attrs = new SnapshotAttributes(this);
            int numberOfAttributes = sae.length;
            SnapshotAttribute[] rows = new SnapshotAttribute[numberOfAttributes];

            for (int i = 0; i < numberOfAttributes; i++) {
                SnapAttributeExtract currentSnapAttributeExtract = sae[i];

                rows[i] = new SnapshotAttribute(currentSnapAttributeExtract,
                        attrs);
            }
            attrs.setSnapshotAttributes(rows);

            this.setSnapshotAttributes(attrs);
        }
    }

    /**
     * Updates the comment field of a Snapshot.
     * 
     * @param comment
     *            the new value of comment.
     * @throws Exception
     */
    public void updateComment(String comment) throws SnapshotingException {
        this.getSnapshotData().setComment(comment);

        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        SnapShotLight light = this.getSnapshotData().getAsSnapShotLight();

        try {
            source.updateCommentOfSnapshot(light, comment);
        } catch (SnapshotingException e) {
            e.printStackTrace();
            throw e;
        }

        // --auto refresh the comment in the snapshots list
        SnapshotListTableModel modelToRefresh = SnapshotListTableModel
                .getInstance();
        modelToRefresh.refreshComment(this.getSnapshotData());
    }

    /**
     * Sets write values of equipments, using the values of this snapshot for
     * each attribute.
     * 
     * @throws Exception
     */
    public void setEquipments() throws SnapshotingException {

        // System.out.println("******bensikin Snapshot.setEquipments ()");
        // System.out.println("******Snapshot Set Equipments test0");
        SnapShot transformedSnap = this.toSnapShot();
        // System.out.println("******Snapshot Set Equipments test1");
        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        // System.out.println("******Snapshot Set Equipments test2");

        try {
            source.setEquipmentsWithSnapshot(transformedSnap);
        } catch (SnapshotingException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Sets equipment command input, using the values of this snapshot for each
     * attribute.
     * 
     * @param option
     * @param cmd_name
     * @throws Exception
     */
    public String setEquipmentWithCommand(String cmd_name, String option)
            throws SnapshotingException {
        if (cmd_name == null || option == null) {
            throw new SnapshotingException();
        }
        SnapShot transformedSnap = this.toSnapShot();
        ISnapManager source = SnapManagerFactory.getCurrentImpl();

        return source.setEquipmentsWithCommand(cmd_name, option,
                transformedSnap);
    }

    /**
     * Adds this snapshot to the current list of opened snapshots.
     * 
     * @param snapshot
     *            The Snapshot to add
     */
    public static void addOpenedSnapshot(Snapshot snapshot) {
        int id = snapshot.getSnapshotData().getId();
        openedSnapshots.put(String.valueOf(id), snapshot);
    }

    /**
     * Remove the snapshot with this id from the current list of opened
     * snapshots.
     * 
     * @param id
     *            The id of the Snapshot to remove
     */
    public static void removeOpenedSnapshot(int id) {
        openedSnapshots.remove(String.valueOf(id));
    }

    /**
     * Adds this snapshot to the current list of opened snapshots.
     * 
     * @param snapshot
     *            The Snapshot to add
     */
    public static void addSelectedSnapshot(Snapshot snapshot) {
        int id = snapshot.getSnapshotData().getId();
        selectedSnapshots.put(String.valueOf(id), snapshot);
    }

    /**
     * Remove the snapshot with this id from the current list of selected
     * snapshots.
     * 
     * @param id
     *            The id of the Snapshot to remove
     */
    public static void removeSelectedSnapshot(int id) {
        selectedSnapshots.remove(String.valueOf(id));
    }

    /**
     * Resets the list of selected snapshots.
     */
    public static void removeAllSelectedSnapshots() {
        selectedSnapshots = new Hashtable<String, Snapshot>();
    }

    /**
     * Empties all references to opened and selected snapshots, and resets the
     * snapshot displays, list and detail, accordingly.
     */
    public static void reset(boolean hasToRemoveSelectedSnapshots,
            boolean resetContextFields) {
        SnapshotListTable table = SnapshotListTable.getInstance();
        table.setModel(SnapshotListTableModel.forceReset());

        openedSnapshots = new Hashtable<String, Snapshot>();

        if (resetContextFields) {
            ContextDataPanel dataPanel = ContextDataPanel.getInstance();
            dataPanel.resetFields();
        }

        if (hasToRemoveSelectedSnapshots) {
            SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane
                    .getInstance();
            tabbedPane.closeAllTabs();

            selectedSnapshots = new Hashtable<String, Snapshot>();
        }

        SnapshotDetailComparePanel comparePanel = SnapshotDetailComparePanel
                .getInstance();
        comparePanel.reset();
        Snapshot.setFirstSnapshotOfComparison(null);
        Snapshot.setSecondSnapshotOfComparison(null);
        Snapshot.setFirstSnapshotOfComparisonTitle(null);
        Snapshot.setSecondSnapshotOfComparisonTitle(null);
    }

    /**
     * Looks up in database a particular Snapshot, defined by its id. Loads only
     * its ContextData.
     * 
     * @param id
     *            The key of the snapshot
     * @return The snapshot with this id
     * @throws SnapshotingException
     */
    public static Snapshot findSnapshotById(int id) throws SnapshotingException {
        ISnapManager snapManager = SnapManagerFactory.getCurrentImpl();

        Condition condition = new Condition(GlobalConst.TAB_SNAP[0], "=",
                String.valueOf(id));
        Criterions searchCriterions = new Criterions();
        searchCriterions.addCondition(condition);

        SnapShotLight[] snapshots = snapManager.findSnapshots(searchCriterions);
        SnapshotData snapshotData = new SnapshotData(snapshots[0]);

        Snapshot ret = new Snapshot(Context.getSelectedContext(), snapshotData);

        return ret;
    }

    /**
     * Converts to a SnapShot object
     * 
     * @return The conversion result
     */
    private SnapShot toSnapShot() {
        Context _context = this.getContext();
        if (_context == null) {
            _context = new Context();
        }

        ContextData contData = _context.getContextData();
        SnapshotData snapData = this.getSnapshotData();
        SnapshotAttributes snapAttrs = this.getSnapshotAttributes();

        /*
         * **** filtering attributes depending which ones user wants **** * ****
         * to use to set equipment ****
         */
        int nbAttrs = 0;
        SnapshotAttributes filteredSnapAttrs = new SnapshotAttributes(this);
        SnapshotAttribute[] attributeList = snapAttrs.getSnapshotAttributes();
        for (int i = 0; i < attributeList.length; i++) {
            SnapshotAttributeWriteValue value = attributeList[i]
                    .getWriteValue();
            if (value != null) {
                if (value.isSettable()) {
                    nbAttrs++;
                }
            } else {
                nbAttrs++;
            }
        }
        SnapshotAttribute[] filteredAttributeList = new SnapshotAttribute[nbAttrs];
        int nbAttrs2 = 0;
        for (int i = 0; i < attributeList.length; i++) {
            SnapshotAttributeWriteValue value = attributeList[i]
                    .getWriteValue();
            // System.out.println("*****Name : " +
            // attributeList[i].getAttribute_complete_name());
            if (value != null) {
                /*
                 * if (value.getDataFormat() == AttrDataFormat._SPECTRUM){
                 * System.out.println(attributeList[ i
                 * ].getAttribute_complete_name());
                 * System.out.println("write value not null : " +
                 * value.getSpectrumValue().getClass()); }
                 */
                if (value.isSettable()) {
                    // System.out.println("write value settable");
                    filteredAttributeList[nbAttrs2] = attributeList[i];
                    nbAttrs2++;
                }
            } else {
                // System.out.println("write value null");
                filteredAttributeList[nbAttrs2] = attributeList[i];
                nbAttrs2++;
            }
        }
        filteredSnapAttrs.setSnapshotAttributes(filteredAttributeList);
        /*          **** end filtering **** */

        // ArrayList transformedAttrs = snapAttrs.toArrayList();
        ArrayList<SnapAttributeExtract> transformedAttrs = filteredSnapAttrs
                .toArrayList();

        String[] argin = new String[3];
        argin[0] = String.valueOf(contData.getId());
        argin[1] = String.valueOf(snapData.getId());
        argin[2] = String.valueOf(snapData.getTime());

        SnapShot ret = new SnapShot(argin);

        ret.setAttribute_List(transformedAttrs);

        return ret;
    }

    /**
     * Resets the list of opened snapshots
     */
    public static void removeAllOpenedSnapshots() {
        openedSnapshots = new Hashtable<String, Snapshot>();
    }

    /**
     * Returns a XML representation of the snapshot.
     * 
     * @return a XML representation of the snapshot
     */
    public String toString() {
        String ret = "";

        XMLLine openingLine = new XMLLine(Snapshot.XML_TAG,
                XMLLine.OPENING_TAG_CATEGORY);
        XMLLine closingLine = new XMLLine(Snapshot.XML_TAG,
                XMLLine.CLOSING_TAG_CATEGORY);

        int id = snapshotData.getId();
        Timestamp time = snapshotData.getTime();
        String comment = snapshotData.getComment();
        String path = snapshotData.getPath();
        String isModified = "" + this.isModified();

        openingLine.setAttribute(SnapshotData.ID_PROPERTY_XML_TAG, String
                .valueOf(id));
        openingLine.setAttribute(SnapshotData.IS_MODIFIED_PROPERTY_XML_TAG,
                isModified);
        if (time != null) {
            openingLine.setAttribute(SnapshotData.TIME_PROPERTY_XML_TAG, time
                    .toString());
        }
        if (comment != null) {
            openingLine.setAttribute(SnapshotData.COMMENT_PROPERTY_XML_TAG,
                    comment.toString());
        }
        if (path != null) {
            openingLine.setAttribute(SnapshotData.PATH_PROPERTY_XML_TAG, path);
        }

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if (this.snapshotAttributes != null) {
            ret += this.snapshotAttributes.toString();
        }

        ret += closingLine.toString();

        return ret;
    }

    /**
     * Returns a CSV representation of the snapshot.
     * 
     * @return a CSV representation of the snapshot
     */
    public String toUserFriendlyString() {
        String ret = "";

        if (this.snapshotAttributes != null) {
            ret += this.snapshotAttributes.toUserFriendlyString();
        }

        return ret;
    }

    /**
     * @return Returns the firstSnapshotOfComparison.
     */
    public static Snapshot getFirstSnapshotOfComparison() {
        return firstSnapshotOfComparison;
    }

    /**
     * @param _firstSnapshotOfComparison
     *            The firstSnapshotOfComparison to set.
     */
    public static void setFirstSnapshotOfComparison(
            Snapshot _firstSnapshotOfComparison) {
        firstSnapshotOfComparison = _firstSnapshotOfComparison;
    }

    /**
     * @return Returns the secondSnapshotOfComparison.
     */
    public static Snapshot getSecondSnapshotOfComparison() {
        return secondSnapshotOfComparison;
    }

    /**
     * @param _secondSnapshotOfComparison
     *            The secondSnapshotOfComparison to set.
     */
    public static void setSecondSnapshotOfComparison(
            Snapshot _secondSnapshotOfComparison) {
        secondSnapshotOfComparison = _secondSnapshotOfComparison;
    }

    /**
     * Tests whether both snapshots have the same attributes
     * 
     * @param snapshot
     *            The Snapshot to compare to
     * @return True if both snapshots have the same attributes (including a null
     *         list)
     */
    public boolean hasSameAttributesAs(Snapshot snapshot) {
        SnapshotAttributes snapshotAttributes1 = this.getSnapshotAttributes();
        SnapshotAttributes snapshotAttributes2 = snapshot
                .getSnapshotAttributes();

        if (snapshotAttributes1 == null) {
            return snapshotAttributes2 == null;
        }

        return snapshotAttributes1.hasSameAttributesAs(snapshotAttributes2);
    }

    /**
     * Sets the modified state of a Snapshot
     * 
     * @param b
     *            True if the Snapshot has been modified
     */
    public void setModified(boolean b) {
        this.isModified = b;

        // Bug : forced all attributes to be "modified", what we do not want
        /*
         * SnapshotAttributes snapshotAttributes1 =
         * this.getSnapshotAttributes(); if ( snapshotAttributes1 == null ) {
         * return; } snapshotAttributes1.setModified( b );
         */
    }

    /**
     * @return Returns the firstSnapshotOfComparisonTitle.
     */
    public static String getFirstSnapshotOfComparisonTitle() {
        return firstSnapshotOfComparisonTitle;
    }

    /**
     * @param firstSnapshotOfComparisonTitle
     *            The firstSnapshotOfComparisonTitle to set.
     */
    public static void setFirstSnapshotOfComparisonTitle(
            String firstSnapshotOfComparisonTitle) {
        Snapshot.firstSnapshotOfComparisonTitle = firstSnapshotOfComparisonTitle;
    }

    /**
     * @return Returns the secondSnapshotOfComparisonTitle.
     */
    public static String getSecondSnapshotOfComparisonTitle() {
        return secondSnapshotOfComparisonTitle;
    }

    /**
     * @param secondSnapshotOfComparisonTitle
     *            The secondSnapshotOfComparisonTitle to set.
     */
    public static void setSecondSnapshotOfComparisonTitle(
            String secondSnapshotOfComparisonTitle) {
        Snapshot.secondSnapshotOfComparisonTitle = secondSnapshotOfComparisonTitle;
    }

    /**
     * @return Returns the openedSnapshots.
     */
    public static Hashtable<String, Snapshot> getOpenedSnapshots() {
        return openedSnapshots;
    }

    /**
     * @param openedSnapshots
     *            The openedSnapshots to set.
     */
    public static void setOpenedSnapshots(
            Hashtable<String, Snapshot> openedSnapshots) {
        Snapshot.openedSnapshots = openedSnapshots;
    }

    /**
     * @return Returns the selectedSnapshots.
     */
    public static Hashtable<String, Snapshot> getSelectedSnapshots() {
        return selectedSnapshots;
    }

    /**
     * @param selectedSnapshots
     *            The selectedSnapshots to set.
     */
    public static void setSelectedSnapshots(
            Hashtable<String, Snapshot> selectedSnapshots) {
        Snapshot.selectedSnapshots = selectedSnapshots;
    }

    /**
     * @return Returns the snapshotAttributes.
     */
    public SnapshotAttributes getSnapshotAttributes() {
        return snapshotAttributes;
    }

    /**
     * @param snapshotAttributes
     *            The snapshotAttributes to set.
     */
    public void setSnapshotAttributes(SnapshotAttributes snapshotAttributes) {
        this.snapshotAttributes = snapshotAttributes;
    }

    /**
     * @return Returns the snapshotData.
     */
    public SnapshotData getSnapshotData() {
        return snapshotData;
    }

    /**
     * @param snapshotData
     *            The snapshotData to set.
     */
    public void setSnapshotData(SnapshotData snapshotData) {
        this.snapshotData = snapshotData;
    }

    /**
     * @return Returns the snapshotDefaultComment.
     */
    public static String getSnapshotDefaultComment() {
        return snapshotDefaultComment;
    }

    /**
     * @param snapshotDefaultComment
     *            The snapshotDefaultComment to set.
     */
    public static void setSnapshotDefaultComment(String snapshotDefaultComment) {
        Snapshot.snapshotDefaultComment = snapshotDefaultComment;
    }

    /**
     * @return Returns the context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * @param context
     *            The context to set.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @param manager
     * @param b
     */
    public void save(ISnapshotManager manager, boolean saveAs) {
        String pathToUse = getPathToUse(manager, saveAs);
        if (pathToUse == null) {
            return;
        }
        // System.out.println (
        // "Context/save/pathToUse/"+pathToUse+"/saveAs/"+saveAs+"/" );
        manager.setNonDefaultSaveLocation(pathToUse);
        ILogger logger = LoggerFactory.getCurrentImpl();
        try {
            this.setPath(pathToUse);
            this.setModified(false);
            // manager.saveArchivingConfiguration( this , null );
            manager.saveSnapshot(this, null);
            Snapshot.removeModifiedMarkers();

            String msg = Messages.getLogMessage("SAVE_SNAPSHOT_ACTION_OK");
            logger.trace(fr.soleil.bensikin.logs.ILogger.LEVEL_DEBUG, msg);
        } catch (Exception e) {
            String msg = Messages.getLogMessage("SAVE_SNAPSHOT_ACTION_KO");
            logger.trace(fr.soleil.bensikin.logs.ILogger.LEVEL_ERROR, msg);
            logger.trace(fr.soleil.bensikin.logs.ILogger.LEVEL_ERROR, e);

            this.setPath(null);
            this.setModified(true);

            return;
        }
    }

    private String getPathToUse(ISnapshotManager manager, boolean saveAs) {
        String pathToUse = this.getPath();
        // System.out.println (
        // "Snapshot/getPathToUse/pathToUse/"+pathToUse+"/saveAs/"+saveAs+"/" );
        JFileChooser chooser = new JFileChooser();
        SnapshotFileFilter ACfilter = new SnapshotFileFilter();
        chooser.addChoosableFileFilter(ACfilter);

        if (pathToUse != null) {
            if (saveAs) {
                chooser.setCurrentDirectory(new File(pathToUse));
            } else {
                return pathToUse;
            }
        } else {
            chooser.setCurrentDirectory(new File(manager
                    .getDefaultSaveLocation()));
        }

        int returnVal = chooser.showSaveDialog(BensikinFrame.getInstance());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            String path = f.getAbsolutePath();

            if (f != null) {
                String extension = SnapshotFileFilter.getExtension(f);
                String expectedExtension = ACfilter.getExtension();

                if (extension == null
                        || !extension.equalsIgnoreCase(expectedExtension)) {
                    path += ".";
                    path += expectedExtension;
                }
            }
            if (f.exists()) {
                int choice = JOptionPane
                        .showConfirmDialog(
                                BensikinFrame.getInstance(),
                                Messages
                                        .getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"),
                                Messages
                                        .getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.OK_OPTION) {
                    return null;
                }
            }
            manager.setNonDefaultSaveLocation(path);
            return path;
        } else {
            return null;
        }

    }

    /**
     * @return Returns the isModified.
     */
    public boolean isModified() {
        return isModified;
    }

    /**
     * If the snapshot is a file snapshot, removes the "modified" mark (if there
     * is one)
     */
    private static void removeModifiedMarkers() {
        SnapshotDetailTabbedPane snapshotDetailTabbedPane = SnapshotDetailTabbedPane
                .getInstance();
        if (snapshotDetailTabbedPane == null) {
            return;
        }

        SnapshotDetailTabbedPaneContent selectedContent = snapshotDetailTabbedPane
                .getSelectedSnapshotDetailTabbedPaneContent();
        if (selectedContent == null) {
            return;
        }

        if (selectedContent.isFileContent()) {
            selectedContent.setModified(false);
        }

    }

    /**
     * @param sn1
     * @return
     * @throws Exception
     */
    public static Snapshot getCurrentStateSnapshot(Snapshot sn1)
            throws Exception {
        ISnapManager source = SnapManagerFactory.getCurrentImpl();
        SnapContext snap = sn1.getAsSnapContext();

        SnapShot savedSnapshot = source.launchSnapshot(snap);
        int id = savedSnapshot.getId_snap();

        Snapshot toUpdate = Snapshot.findSnapshotById(id);
        toUpdate.loadAttributes();

        toUpdate.updateComment(Snapshot.BENSIKIN_AUTOMATIC_INSERT_COMMENT);

        return toUpdate;
    }

    /**
     * @return
     * @throws Exception
     */
    private SnapContext getAsSnapContext() throws Exception {
        String author = Context.BENSIKIN_AUTOMATIC_INSERT_COMMENT;
        String name = Context.BENSIKIN_AUTOMATIC_INSERT_COMMENT;
        String reason = Context.BENSIKIN_AUTOMATIC_INSERT_COMMENT;
        String description = Context.BENSIKIN_AUTOMATIC_INSERT_COMMENT;

        SnapContext _context = null;
        if (this.context != null) {
            _context = this.context.getAsSnapContext();
            _context.setAttributeList(this.getAsAttributeList());
        } else {
            _context = new SnapContext(author, name, -1, null, reason,
                    description);
            _context.setCreation_date(new java.sql.Date(System
                    .currentTimeMillis()));
            _context.setAttributeList(this.getAsAttributeList());

            Context ctx = Context.save(_context);
            _context = ctx.getAsSnapContext();
        }

        return _context;
    }

    /**
     * @return
     */
    private ArrayList<SnapAttributeLight> getAsAttributeList() {
        ArrayList<SnapAttributeLight> attributeList = new ArrayList<SnapAttributeLight>();

        SnapshotAttribute[] attrs = this.getSnapshotAttributes()
                .getSnapshotAttributes();
        if (attrs == null) {
            return attributeList;
        }

        for (int i = 0; i < attrs.length; i++) {
            SnapshotAttribute next = attrs[i];
            String attribute_complete_name = next.getAttribute_complete_name();

            SnapAttributeLight currentAttr = new SnapAttributeLight(
                    attribute_complete_name);
            attributeList.add(currentAttr);
        }

        return attributeList;
    }

    public StringBuffer snapshotInfoToUserFriendlyStringBuffer(
            StringBuffer infoBuffer) {
        if (infoBuffer == null) {
            infoBuffer = new StringBuffer();
        }
        int columnCount = 0;
        SnapshotOptions snapshotOptions = Options.getInstance()
                .getSnapshotOptions();
        if (snapshotOptions.isCSVID()) {
            columnCount++;
        }
        if (snapshotOptions.isCSVTime()) {
            columnCount++;
        }
        if (snapshotOptions.isCSVContextID()) {
            columnCount++;
        }
        if (columnCount > 0) {
            int i = 0;
            if (snapshotOptions.isCSVID()) {
                infoBuffer.append(Messages
                        .getMessage("SNAPSHOT_DETAIL_ID_TITLE"));
                i++;
                if (i < columnCount) {
                    infoBuffer.append(snapshotOptions.getCSVSeparator());
                }
            }
            if (snapshotOptions.isCSVTime()) {
                infoBuffer.append(Messages
                        .getMessage("SNAPSHOT_DETAIL_TIME_TITLE"));
                i++;
                if (i < columnCount) {
                    infoBuffer.append(snapshotOptions.getCSVSeparator());
                }
            }
            if (snapshotOptions.isCSVContextID()) {
                infoBuffer.append(Messages
                        .getMessage("SNAPSHOT_DETAIL_CONTEXT_TITLE"));
                i++;
                if (i < columnCount) {
                    infoBuffer.append(snapshotOptions.getCSVSeparator());
                }
            }
            infoBuffer.append('\n');
            i = 0;
            if (snapshotOptions.isCSVID()) {
                if (snapshotData != null) {
                    infoBuffer.append(snapshotData.getId());
                }
                i++;
                if (i < columnCount) {
                    infoBuffer.append(snapshotOptions.getCSVSeparator());
                }
            }
            if (snapshotOptions.isCSVTime()) {
                String timeText = "";
                if (snapshotData != null && snapshotData.getTime() != null) {
                    timeText = snapshotData.getTime().toString();
                    if (timeText == null) {
                        timeText = "";
                    } else {
                        timeText = timeText.trim();
                    }
                }
                infoBuffer.append(timeText);
                timeText = null;
                i++;
                if (i < columnCount) {
                    infoBuffer.append(snapshotOptions.getCSVSeparator());
                }
            }
            if (snapshotOptions.isCSVContextID()) {
                if ((context != null) && (context.getContextData() != null)) {
                    infoBuffer.append(context.getContextData().getId());
                }
                i++;
                if (i < columnCount) {
                    infoBuffer.append(snapshotOptions.getCSVSeparator());
                }
            }
        }
        snapshotOptions = null;
        return infoBuffer;
    }

    public StringBuffer snapshotAttributesToUserFriendlyStringBuffer(
            StringBuffer attrBuffer) {
        if (attrBuffer == null) {
            attrBuffer = new StringBuffer();
        }
        int columnCount = 1;
        SnapshotOptions snapshotOptions = Options.getInstance()
                .getSnapshotOptions();
        if (snapshotOptions.isCSVRead()) {
            columnCount++;
        }
        if (snapshotOptions.isCSVWrite()) {
            columnCount++;
        }
        if (snapshotOptions.isCSVDelta()) {
            columnCount++;
        }
        int i = 0;
        attrBuffer.append(Messages.getMessage("SNAPSHOT_DETAIL_COLUMNS_NAME"));
        i++;
        if (i < columnCount) {
            attrBuffer.append(snapshotOptions.getCSVSeparator());
        }
        if (snapshotOptions.isCSVWrite()) {
            attrBuffer.append(Messages.getMessage("SNAPSHOT_DETAIL_COLUMNS_W"));
            i++;
            if (i < columnCount) {
                attrBuffer.append(snapshotOptions.getCSVSeparator());
            }
        }
        if (snapshotOptions.isCSVRead()) {
            attrBuffer.append(Messages.getMessage("SNAPSHOT_DETAIL_COLUMNS_R"));
            i++;
            if (i < columnCount) {
                attrBuffer.append(snapshotOptions.getCSVSeparator());
            }
        }
        if (snapshotOptions.isCSVDelta()) {
            attrBuffer.append(Messages
                    .getMessage("SNAPSHOT_DETAIL_COLUMNS_DELTA"));
            i++;
            if (i < columnCount) {
                attrBuffer.append(snapshotOptions.getCSVSeparator());
            }
        }
        if ((snapshotAttributes != null)
                && (snapshotAttributes.getSnapshotAttributes() != null)) {
            for (int attrIndex = 0; attrIndex < snapshotAttributes
                    .getSnapshotAttributes().length; attrIndex++) {
                attrBuffer.append('\n');
                i = 0;
                SnapshotAttribute attr = snapshotAttributes
                        .getSnapshotAttributes()[attrIndex];
                attrBuffer.append(attr.getAttribute_complete_name());
                i++;
                if (i < columnCount) {
                    attrBuffer.append(snapshotOptions.getCSVSeparator());
                }
                if (snapshotOptions.isCSVWrite()) {
                    attrBuffer.append(SnapshotDetailRenderer.getTextFor(attr
                            .getWriteValue()));
                    i++;
                    if (i < columnCount) {
                        attrBuffer.append(snapshotOptions.getCSVSeparator());
                    }
                }
                if (snapshotOptions.isCSVRead()) {
                    attrBuffer.append(SnapshotDetailRenderer.getTextFor(attr
                            .getReadValue()));
                    i++;
                    if (i < columnCount) {
                        attrBuffer.append(snapshotOptions.getCSVSeparator());
                    }
                }
                if (snapshotOptions.isCSVDelta()) {
                    attrBuffer.append(SnapshotDetailRenderer.getTextFor(attr
                            .getDeltaValue()));
                    i++;
                    if (i < columnCount) {
                        attrBuffer.append(snapshotOptions.getCSVSeparator());
                    }
                }
            }
        }
        snapshotOptions = null;
        return attrBuffer;
    }

    public boolean isLoadable() {
        return loadable;
    }

    public void setLoadable(boolean loadable) {
        this.loadable = loadable;
    }
}
