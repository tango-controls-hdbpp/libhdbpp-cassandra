// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/SnapshotOptions.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class MiscOptions.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: SnapshotOptions.java,v $
// Revision 1.2 2005/12/14 16:47:26 ounsy
// added methods necessary for direct clipboard edition
//
// Revision 1.1 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:41 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================

package fr.soleil.bensikin.options.sub;

import java.util.ArrayList;

import javax.swing.ButtonModel;

import fr.soleil.bensikin.actions.snapshot.CompareSnapshotsAction;
import fr.soleil.bensikin.containers.sub.dialogs.SnapshotCompareDialog;
import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsSnapshotTab;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;

/**
 * The snapshots options of the application. Contains:
 * <UL>
 * <LI>The auto comment options (on/off, and value)
 * <LI>Whether snapshots comparisons should show the read, write, and delta
 * values of snapshots
 * <LI>Whether a "difference" snapshot, made by substracting the two, should be
 * added to the comparison.
 * </UL>
 * 
 * @author CLAISSE
 */
public class SnapshotOptions extends ReadWriteOptionBook implements
        PushPullOptionBook {

    /**
     * The XML tag name used in saving/loading
     */
    public static final String XML_TAG                          = "snapshot";

    // --auto comment
    /**
     * The snapshot auto-commenting property name
     */
    public static final String SNAPSHOT_AUTO_COMMENT            = "SNAPSHOT_AUTO_COMMENT";
    /**
     * Auto-comment new snapshots
     */
    public static final int    SNAPSHOT_AUTO_COMMENT_YES        = 1;
    /**
     * Don't auto-comment new snapshots
     */
    public static final int    SNAPSHOT_AUTO_COMMENT_NO         = 0;
    /**
     * The snapshot auto-comment value property name (unused if auto-commenting
     * is disabled)
     */
    public static final String SNAPSHOT_DEFAULT_COMMENT         = "SNAPSHOT_DEFAULT_COMMENT";

    // --CSV export
    /**
     * The snapshot separator for CSV extraction
     */
    public static final String SNAPSHOT_CSV_SEPARATOR           = "SNAPSHOT_CSV_SEPARATOR";
    public static final String SNAPSHOT_CSV_SEPARATOR_SEMICOLON = ";";
    public static final String SNAPSHOT_CSV_SEPARATOR_PIPE      = "|";
    public static final String SNAPSHOT_CSV_SEPARATOR_TAB       = "\t";
    /**
     * The CSV export's "id display" property name
     */
    public static final String SNAPSHOT_CSV_ID                  = "SNAPSHOT_CSV_ID";
    /**
     * Display id in CSV exports
     */
    public static final int    SNAPSHOT_CSV_ID_YES              = 1;
    /**
     * Don't display id in CSV exports
     */
    public static final int    SNAPSHOT_CSV_ID_NO               = 0;
    /**
     * The CSV export's "time display" property name
     */
    public static final String SNAPSHOT_CSV_TIME                = "SNAPSHOT_CSV_TIME";
    /**
     * Display time in CSV exports
     */
    public static final int    SNAPSHOT_CSV_TIME_YES            = 1;
    /**
     * Don't display time in CSV exports
     */
    public static final int    SNAPSHOT_CSV_TIME_NO             = 0;
    /**
     * The CSV export's "linked context id display" property name
     */
    public static final String SNAPSHOT_CSV_CONTEXT_ID          = "SNAPSHOT_CSV_CONTEXT_ID";
    /**
     * Display linked context id in CSV exports
     */
    public static final int    SNAPSHOT_CSV_CONTEXT_ID_YES      = 1;
    /**
     * Don't display linked context id in CSV exports
     */
    public static final int    SNAPSHOT_CSV_CONTEXT_ID_NO       = 0;
    /**
     * The CSV export's "read value display" property name
     */
    public static final String SNAPSHOT_CSV_READ                = "SNAPSHOT_CSV_READ";
    /**
     * Display read values in CSV exports
     */
    public static final int    SNAPSHOT_CSV_READ_YES            = 1;
    /**
     * Don't display read values in CSV exports
     */
    public static final int    SNAPSHOT_CSV_READ_NO             = 0;
    /**
     * The CSV export's "write value display" property name
     */
    public static final String SNAPSHOT_CSV_WRITE               = "SNAPSHOT_CSV_WRITE";
    /**
     * Display write values in CSV exports
     */
    public static final int    SNAPSHOT_CSV_WRITE_YES           = 1;
    /**
     * Don't display write values in CSV exports
     */
    public static final int    SNAPSHOT_CSV_WRITE_NO            = 0;
    /**
     * The CSV export's "delta value display" property name
     */
    public static final String SNAPSHOT_CSV_DELTA               = "SNAPSHOT_CSV_DELTA";
    /**
     * Display delta values in CSV exports
     */
    public static final int    SNAPSHOT_CSV_DELTA_YES           = 1;
    /**
     * Don't display delta values in CSV exports
     */
    public static final int    SNAPSHOT_CSV_DELTA_NO            = 0;

    private boolean            csvID                            = true;
    private boolean            csvTime                          = true;
    private boolean            csvContextID                     = true;
    private boolean            csvRead                          = true;
    private boolean            csvWrite                         = true;
    private boolean            csvDelta                         = true;
    private String             csvSeparator                     = SNAPSHOT_CSV_SEPARATOR_SEMICOLON;

    // --snapshots comparison
    /**
     * The snapshot comparison's "read value display" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_READ       = "SNAPSHOT_COMPARE_SHOW_READ";
    /**
     * Display read values in snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_READ_YES   = 1;
    /**
     * Don't display read values in snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_READ_NO    = 0;
    /**
     * The snapshot comparison's "write value display" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_WRITE      = "SNAPSHOT_COMPARE_SHOW_WRITE";
    /**
     * Display write values in snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_WRITE_YES  = 1;
    /**
     * Don't display write values in snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_WRITE_NO   = 0;
    /**
     * The snapshot comparison's "delta value display" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_DELTA      = "SNAPSHOT_COMPARE_SHOW_DELTA";
    /**
     * Display delta values in snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_DELTA_YES  = 1;
    /**
     * Don't display delta values in snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_DELTA_NO   = 0;
    /**
     * The snapshot comparison's "add a difference snapshot" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_DIFF       = "SNAPSHOT_COMPARE_SHOW_DIFF";
    /**
     * Add a "difference snapshot" to snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_DIFF_YES   = 1;
    /**
     * Don't add a "difference snapshot" to snapshot comparisons
     */
    public static final int    SNAPSHOT_COMPARE_SHOW_DIFF_NO    = 0;

    private boolean            showRead                         = true;
    private boolean            showWrite                        = true;
    private boolean            showDelta                        = true;
    private boolean            showDiff                         = true;

    public final static int    SNAPSHOT_COMPARE_COLUMNS_NAME    = 0;
    public final static int    SNAPSHOT_COMPARE_SNAP1_R         = 1;
    public final static int    SNAPSHOT_COMPARE_SNAP1_W         = 2;
    public final static int    SNAPSHOT_COMPARE_SNAP1_DELTA     = 3;
    public final static int    SNAPSHOT_COMPARE_SNAP2_R         = 4;
    public final static int    SNAPSHOT_COMPARE_SNAP2_W         = 5;
    public final static int    SNAPSHOT_COMPARE_SNAP2_DELTA     = 6;
    public final static int    SNAPSHOT_COMPARE_DIFF_R          = 7;
    public final static int    SNAPSHOT_COMPARE_DIFF_W          = 8;
    public final static int    SNAPSHOT_COMPARE_DIFF_DELTA      = 9;

    public final static int[]  DEFAULT_COMPARE_COLUMN_ORDER     = {
            SNAPSHOT_COMPARE_COLUMNS_NAME, SNAPSHOT_COMPARE_DIFF_R,
            SNAPSHOT_COMPARE_DIFF_W, SNAPSHOT_COMPARE_DIFF_DELTA,
            SNAPSHOT_COMPARE_SNAP1_R, SNAPSHOT_COMPARE_SNAP1_W,
            SNAPSHOT_COMPARE_SNAP1_DELTA, SNAPSHOT_COMPARE_SNAP2_R,
            SNAPSHOT_COMPARE_SNAP2_W, SNAPSHOT_COMPARE_SNAP2_DELTA };

    private int[]              compareColumnOrder;
    /**
     * The snapshot comparison's "column order" property name
     */
    public final static String SNAPSHOT_COMPARE_COLUMN_ORDER    = "SNAPSHOT_COMPARE_COLUMN_ORDER";

    // --Date Filter
    /**
     * The snapshot comparison's "read value display" property name
     */
    public static final String SNAPSHOT_TIME_FILTER             = "SNAPSHOT_TIME_FILTER";
    public final static int    FILTER_DAY                       = 0;
    public final static int    FILTER_WEEK                      = 1;
    public final static int    FILTER_DAY7                      = 2;
    public final static int    FILTER_MONTH                     = 3;
    public final static int    FILTER_DAY30                     = 4;
    private int                timeFilter                       = FILTER_DAY;

    /**
     * Default constructor
     */
    public SnapshotOptions() {
        super(XML_TAG);
    }

    /*
     * (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog() {
        // CSV separator
        OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
        String separator = snapshotTab.getSeparator();
        this.content.put(SNAPSHOT_CSV_SEPARATOR, separator);
        // --auto-comment

        ButtonModel selectedModel = snapshotTab.getButtonGroup().getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();
        this.content.put(SNAPSHOT_AUTO_COMMENT, selectedActionCommand);

        String snapshotDefaultComment = snapshotTab.getSnapshotDefaultComment();
        this.content.put(SNAPSHOT_DEFAULT_COMMENT, snapshotDefaultComment);

        // ----show options
        String showRead = snapshotTab.hasShowRead();
        this.content.put(SNAPSHOT_COMPARE_SHOW_READ, showRead);
        if (String.valueOf(SNAPSHOT_COMPARE_SHOW_READ_YES).equals(showRead)) {
            this.showRead = true;
        }
        else {
            this.showRead = false;
        }

        String showWrite = snapshotTab.hasShowWrite();
        this.content.put(SNAPSHOT_COMPARE_SHOW_WRITE, showWrite);
        if (String.valueOf(SNAPSHOT_COMPARE_SHOW_WRITE_YES).equals(showWrite)) {
            this.showWrite = true;
        }
        else {
            this.showWrite = false;
        }

        String showDelta = snapshotTab.hasShowDelta();
        this.content.put(SNAPSHOT_COMPARE_SHOW_DELTA, showDelta);
        if (String.valueOf(SNAPSHOT_COMPARE_SHOW_DELTA_YES).equals(showDelta)) {
            this.showDelta = true;
        }
        else {
            this.showDelta = false;
        }

        String showDiff = snapshotTab.hasShowDiff();
        this.content.put(SNAPSHOT_COMPARE_SHOW_DIFF, showDiff);
        if (String.valueOf(SNAPSHOT_COMPARE_SHOW_DIFF_YES).equals(showDiff)) {
            this.showDiff = true;
        }
        else {
            this.showDiff = false;
        }

        String order = intArrayToString(snapshotTab.getCompareColumnOrder(),
                intArrayToString(DEFAULT_COMPARE_COLUMN_ORDER, null));
        System.out.println("order: " + order);
        this.content.put(SNAPSHOT_COMPARE_COLUMN_ORDER, order);
        // ----csv options
        String csvID = snapshotTab.hasCSVID();
        this.content.put(SNAPSHOT_CSV_ID, csvID);
        if (String.valueOf(SNAPSHOT_CSV_ID_YES).equals(csvID)) {
            this.csvID = true;
        }
        else {
            this.csvID = false;
        }

        String csvTime = snapshotTab.hasCSVTime();
        this.content.put(SNAPSHOT_CSV_TIME, csvTime);
        if (String.valueOf(SNAPSHOT_CSV_TIME_YES).equals(csvTime)) {
            this.csvTime = true;
        }
        else {
            this.csvTime = false;
        }

        String csvContextID = snapshotTab.hasCSVContextID();
        this.content.put(SNAPSHOT_CSV_CONTEXT_ID, csvContextID);
        if (String.valueOf(SNAPSHOT_CSV_CONTEXT_ID_YES).equals(csvContextID)) {
            this.csvContextID = true;
        }
        else {
            this.csvContextID = false;
        }

        String csvRead = snapshotTab.hasCSVRead();
        this.content.put(SNAPSHOT_CSV_READ, csvRead);
        if (String.valueOf(SNAPSHOT_CSV_READ_YES).equals(csvRead)) {
            this.csvRead = true;
        }
        else {
            this.csvRead = false;
        }

        String csvWrite = snapshotTab.hasCSVWrite();
        this.content.put(SNAPSHOT_CSV_WRITE, csvWrite);
        if (String.valueOf(SNAPSHOT_CSV_WRITE_YES).equals(csvWrite)) {
            this.csvWrite = true;
        }
        else {
            this.csvWrite = false;
        }

        String csvDelta = snapshotTab.hasCSVDelta();
        this.content.put(SNAPSHOT_CSV_DELTA, csvDelta);
        if (String.valueOf(SNAPSHOT_CSV_DELTA_YES).equals(csvDelta)) {
            this.csvDelta = true;
        }
        else {
            this.csvDelta = false;
        }
        int selectedTimeFilter = snapshotTab.getSelectedTimeFilter();
        this.content.put(SNAPSHOT_TIME_FILTER, Integer
                .toString(selectedTimeFilter));
        this.timeFilter = selectedTimeFilter;

        // finally: update ui
        updateUI();
    }

    /*
     * (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    public void push() {
        /*
         * String separator = snapshotTab.getSeparator (); this.content.put(
         * SNAPSHOT_CSV_SEPARATOR , separator );
         */

        String val_s = (String) this.content.get(SNAPSHOT_CSV_SEPARATOR);
        if (val_s != null) {
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.setSeparator(val_s);

            this.setCSVSeparator(val_s);
        }

        val_s = (String) this.content.get(SNAPSHOT_AUTO_COMMENT);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            // LifeCycleManager lifeCycleManager =
            // LifeCycleManagerFactory.getCurrentImpl();
            switch (val) {
                case SNAPSHOT_AUTO_COMMENT_YES:
                    String snapshotDefaultComment = (String) this.content
                            .get(SNAPSHOT_DEFAULT_COMMENT);
                    Snapshot.setSnapshotDefaultComment(snapshotDefaultComment);
                    OptionsSnapshotTab snapshotTab = OptionsSnapshotTab
                            .getInstance();
                    snapshotTab
                            .setSnapshotDefaultComment(snapshotDefaultComment);
                    break;

                case SNAPSHOT_AUTO_COMMENT_NO:
                    Snapshot.setSnapshotDefaultComment(null);
                    break;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_COMPARE_SHOW_READ);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowReadButton(val);

            if (val == SNAPSHOT_COMPARE_SHOW_READ_YES) {
                this.showRead = true;
            }
            else {
                this.showRead = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_COMPARE_SHOW_WRITE);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowWriteButton(val);

            if (val == SNAPSHOT_COMPARE_SHOW_WRITE_YES) {
                this.showWrite = true;
            }
            else {
                this.showWrite = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_COMPARE_SHOW_DELTA);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowDeltaButton(val);

            if (val == SNAPSHOT_COMPARE_SHOW_DELTA_YES) {
                this.showDelta = true;
            }
            else {
                this.showDelta = false;
            }

            val_s = (String) this.content.get(SNAPSHOT_COMPARE_SHOW_DIFF);
        }

        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowDiffButton(val);

            if (val == SNAPSHOT_COMPARE_SHOW_DIFF_YES) {
                this.showDiff = true;
            }
            else {
                this.showDiff = false;
            }

        }

        val_s = (String) this.content.get(SNAPSHOT_COMPARE_COLUMN_ORDER);
        int[] valArray = stringToIntArray(val_s, -1,
                DEFAULT_COMPARE_COLUMN_ORDER);
        OptionsSnapshotTab.getInstance().setCompareColumnOrder(valArray);
        // Workaround to be sure to have a column order compatible with columns
        // visibility (needed because this functionality was added later, and
        // some saved options may not have this value saved, which means the
        // array may be too long according to columns visibility)
        this.compareColumnOrder = OptionsSnapshotTab.getInstance()
                .getCompareColumnOrder();
        this.content.put(SNAPSHOT_COMPARE_COLUMN_ORDER, intArrayToString(
                compareColumnOrder, ""));
        valArray = null;

        val_s = (String) this.content.get(SNAPSHOT_CSV_ID);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectCSVIDButton(val);

            if (val == SNAPSHOT_CSV_ID_YES) {
                this.csvID = true;
            }
            else {
                this.csvID = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_CSV_TIME);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectCSVTimeButton(val);

            if (val == SNAPSHOT_CSV_TIME_YES) {
                this.csvTime = true;
            }
            else {
                this.csvTime = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_CSV_CONTEXT_ID);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectCSVContextIDButton(val);

            if (val == SNAPSHOT_CSV_CONTEXT_ID_YES) {
                this.csvContextID = true;
            }
            else {
                this.csvContextID = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_CSV_READ);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectCSVReadButton(val);

            if (val == SNAPSHOT_CSV_READ_YES) {
                this.csvRead = true;
            }
            else {
                this.csvRead = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_CSV_WRITE);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectCSVWriteButton(val);

            if (val == SNAPSHOT_CSV_WRITE_YES) {
                this.csvWrite = true;
            }
            else {
                this.csvWrite = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_CSV_DELTA);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectCSVDeltaButton(val);

            if (val == SNAPSHOT_CSV_DELTA_YES) {
                this.csvDelta = true;
            }
            else {
                this.csvDelta = false;
            }
        }

        val_s = (String) this.content.get(SNAPSHOT_TIME_FILTER);
        if (val_s != null) {
            int val = Integer.parseInt(val_s);
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.setSelectedTimeFilter(val);
            this.timeFilter = val;
        }

        // finally: update ui
        updateUI();
    }

    /**
     * @return Returns the showDiff.
     */
    public boolean isShowDiff() {
        return showDiff;
    }

    /**
     * @param showDiff
     *            The showDiff to set.
     */
    public void setShowDiff(boolean showDiff) {
        this.showDiff = showDiff;
    }

    /**
     * @return Returns the showRead.
     */
    public boolean isShowRead() {
        return showRead;
    }

    /**
     * @param showRead
     *            The showRead to set.
     */
    public void setShowRead(boolean showRead) {
        this.showRead = showRead;
    }

    /**
     * @return Returns the showWrite.
     */
    public boolean isShowWrite() {
        return showWrite;
    }

    /**
     * @param showWrite
     *            The showWrite to set.
     */
    public void setShowWrite(boolean showWrite) {
        this.showWrite = showWrite;
    }

    /**
     * @return Returns the showDelta.
     */
    public boolean isShowDelta() {
        return showDelta;
    }

    public int[] getCompareColumnOrder() {
        return compareColumnOrder;
    }

    public void setCompareColumnOrder(int[] columnOrder) {
        this.compareColumnOrder = columnOrder;
    }

    /**
     * @return Returns the csvID.
     */
    public boolean isCSVID() {
        return csvID;
    }

    /**
     * @param csvID
     *            The csvID to set.
     */
    public void setCSVID(boolean csvID) {
        this.csvID = csvID;
    }

    /**
     * @return Returns the csvTime.
     */
    public boolean isCSVTime() {
        return csvTime;
    }

    /**
     * @param csvTime
     *            The csvTime to set.
     */
    public void setCSVTime(boolean csvTime) {
        this.csvTime = csvTime;
    }

    /**
     * @return Returns the csvContextID.
     */
    public boolean isCSVContextID() {
        return csvContextID;
    }

    /**
     * @param csvContextID
     *            The csvContextID to set.
     */
    public void setCSVContextID(boolean csvContextID) {
        this.csvContextID = csvContextID;
    }

    /**
     * @return Returns the csvRead.
     */
    public boolean isCSVRead() {
        return csvRead;
    }

    /**
     * @param csvRead
     *            The csvRead to set.
     */
    public void setCSVRead(boolean csvRead) {
        this.csvRead = csvRead;
    }

    /**
     * @return Returns the csvWrite.
     */
    public boolean isCSVWrite() {
        return csvWrite;
    }

    /**
     * @param csvWrite
     *            The csvWrite to set.
     */
    public void setCSVWrite(boolean csvWrite) {
        this.csvWrite = csvWrite;
    }

    /**
     * @return Returns the csvDelta.
     */
    public boolean isCSVDelta() {
        return csvDelta;
    }

    /**
     * @param csvDelta
     *            The csvDelta to set.
     */
    public void setCSVDelta(boolean csvDelta) {
        this.csvDelta = csvDelta;
    }

    /**
     * @return Returns the CSV separator.
     */
    public String getCSVSeparator() {
        return csvSeparator;
    }

    /**
     * @param separator
     *            The CSV separator to set.
     */
    public void setCSVSeparator(String separator) {
        this.csvSeparator = separator;
    }

    public int getTimeFilter() {
        return timeFilter;
    }

    public void setTimeFilter(int timeFilter) {
        this.timeFilter = timeFilter;
    }

    private void updateUI() {
        // Update snapshot compare tables contents
        ArrayList<SnapshotCompareDialog> dialogs = new ArrayList<SnapshotCompareDialog>();
        if (CompareSnapshotsAction.getInstance() != null) {
            dialogs.addAll(CompareSnapshotsAction.getInstance()
                    .getOpenedDialogs());
        }
        for (int i = 0; i < dialogs.size(); i++) {
            if (dialogs.get(i).getCompareTable() != null
                    && dialogs.get(i).getCompareTable().getModel() instanceof SnapshotCompareTablePrintModel) {
                ((SnapshotCompareTablePrintModel) dialogs.get(i)
                        .getCompareTable().getModel()).build();
            }
        }
        dialogs.clear();
        dialogs = null;
    }

    private String intArrayToString(int[] array, String defaultResult) {
        if (array == null) {
            return defaultResult;
        }
        else {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    buffer.append(';');
                }
                buffer.append(Integer.toString(array[i]));
            }
            return buffer.toString();
        }
    }

    private int[] stringToIntArray(String toArray, int defaultValue,
            int[] defaultResult) {
        if (toArray == null || "".equals(toArray.trim())) {
            return defaultResult;
        }
        else {
            String[] array = toArray.split(";");
            int[] result = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                try {
                    result[i] = Integer.parseInt(array[i]);
                }
                catch (Exception e) {
                    result[i] = defaultValue;
                }
            }
            array = null;
            return result;
        }
    }

}
