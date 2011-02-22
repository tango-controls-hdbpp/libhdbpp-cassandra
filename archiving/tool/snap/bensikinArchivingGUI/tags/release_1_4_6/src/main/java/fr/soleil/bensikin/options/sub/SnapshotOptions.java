//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/SnapshotOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MiscOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: SnapshotOptions.java,v $
// Revision 1.2  2005/12/14 16:47:26  ounsy
// added methods necessary for direct clipboard edition
//
// Revision 1.1  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.bensikin.options.sub;

import javax.swing.ButtonModel;

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsSnapshotTab;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;


/**
 * The snapshots options of the application.
 * Contains:
 * <UL>
 * <LI> The auto comment options (on/off, and value)
 * <LI> Whether snapshots comparisons should show the read, write, and delta values of snapshots
 * <LI> Whether a "difference" snapshot, made by substracting the two, should be added to the comparison.
 * </UL>
 *
 * @author CLAISSE
 */
public class SnapshotOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
    //--CSV separator
    /**
     * The snapshot separator for CSV extraction
     */
    public static final String SNAPSHOT_CSV_SEPARATOR = "SNAPSHOT_CSV_SEPARATOR";
    
    public static final String SNAPSHOT_CSV_SEPARATOR_SEMICOLON = ";";
    public static final String SNAPSHOT_CSV_SEPARATOR_PIPE = "|";
    public static final String SNAPSHOT_CSV_SEPARATOR_TAB = "\t";
    
    //--auto comment    
    /**
     * The snapshot auto-commenting property name
     */
    public static final String SNAPSHOT_AUTO_COMMENT = "SNAPSHOT_AUTO_COMMENT";
    /**
     * Auto-comment new snapshots
     */
    public static final int SNAPSHOT_AUTO_COMMENT_YES = 1;
    /**
     * Don't auto-comment new snapshots
     */
    public static final int SNAPSHOT_AUTO_COMMENT_NO = 0;
    /**
     * The snapshot auto-comment value property name (unused if auto-commenting is disabled)
     */
    public static final String SNAPSHOT_DEFAULT_COMMENT = "SNAPSHOT_DEFAULT_COMMENT";

    //--snapshots comparison
    /**
     * The snapshot comparison's "read value display" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_READ = "SNAPSHOT_COMPARE_SHOW_READ";
    /**
     * Display read values in snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_READ_YES = 1;
    /**
     * Don't display read values in snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_READ_NO = 0;
    /**
     * The snapshot comparison's "write value display" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_WRITE = "SNAPSHOT_COMPARE_SHOW_WRITE";
    /**
     * Display write values in snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_WRITE_YES = 1;
    /**
     * Don't display write values in snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_WRITE_NO = 0;
    /**
     * The snapshot comparison's "delta value display" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_DELTA = "SNAPSHOT_COMPARE_SHOW_DELTA";
    /**
     * Display delta values in snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_DELTA_YES = 1;
    /**
     * Don't display delta values in snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_DELTA_NO = 0;
    /**
     * The snapshot comparison's "add a difference snapshot" property name
     */
    public static final String SNAPSHOT_COMPARE_SHOW_DIFF = "SNAPSHOT_COMPARE_SHOW_DIFF";
    /**
     * Add a "difference snapshot" to snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_DIFF_YES = 1;
    /**
     * Don't add a "difference snapshot" to snapshot comparisons
     */
    public static final int SNAPSHOT_COMPARE_SHOW_DIFF_NO = 0;

    /**
     * The XML tag name used in saving/loading
     */
    public static final String XML_TAG = "snapshot";

    private boolean showRead = true;
    private boolean showWrite = true;
    private boolean showDelta = true;
    private boolean showDiff = true;
    
    private String separator = SNAPSHOT_CSV_SEPARATOR_SEMICOLON;


    /**
     * Default constructor
     */
    public SnapshotOptions ()
    {
        super( XML_TAG );
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog ()
    {
        //CSV separator
        OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
        String separator = snapshotTab.getSeparator ();
        this.content.put( SNAPSHOT_CSV_SEPARATOR , separator );
        //--auto-comment
        
        ButtonModel selectedModel = snapshotTab.getButtonGroup().getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();
        this.content.put( SNAPSHOT_AUTO_COMMENT , selectedActionCommand );

        String snapshotDefaultComment = snapshotTab.getSnapshotDefaultComment();
        this.content.put( SNAPSHOT_DEFAULT_COMMENT , snapshotDefaultComment );

        //----show options
        String showRead = snapshotTab.hasShowRead();
        this.content.put( SNAPSHOT_COMPARE_SHOW_READ , showRead );
        if ( String.valueOf( SNAPSHOT_COMPARE_SHOW_READ_YES ).equals( showRead ) )
        {
            this.showRead = true;
        }
        else
        {
            this.showRead = false;
        }


        String showWrite = snapshotTab.hasShowWrite();
        this.content.put( SNAPSHOT_COMPARE_SHOW_WRITE , showWrite );
        if ( String.valueOf( SNAPSHOT_COMPARE_SHOW_WRITE_YES ).equals( showWrite ) )
        {
            this.showWrite = true;
        }
        else
        {
            this.showWrite = false;
        }


        String showDelta = snapshotTab.hasShowDelta();
        this.content.put( SNAPSHOT_COMPARE_SHOW_DELTA , showDelta );
        if ( String.valueOf( SNAPSHOT_COMPARE_SHOW_DELTA_YES ).equals( showDelta ) )
        {
            this.showDelta = true;
        }
        else
        {
            this.showDelta = false;
        }

        String showDiff = snapshotTab.hasShowDiff();
        this.content.put( SNAPSHOT_COMPARE_SHOW_DIFF , showDiff );
        if ( String.valueOf( SNAPSHOT_COMPARE_SHOW_DIFF_YES ).equals( showDiff ) )
        {
            this.showDiff = true;
        }
        else
        {
            this.showDiff = false;
        }
    }


    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    public void push ()
    {
        /*String separator = snapshotTab.getSeparator ();
        this.content.put( SNAPSHOT_CSV_SEPARATOR , separator );*/
        
        String val_s = ( String ) this.content.get( SNAPSHOT_CSV_SEPARATOR );
        if ( val_s != null )
        {
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.setSeparator ( val_s );
            
            this.setSeparator ( val_s );
        }
        
        val_s = ( String ) this.content.get( SNAPSHOT_AUTO_COMMENT );
        if ( val_s != null )
        {
            int val = Integer.parseInt( val_s );
           // LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl();
            switch ( val )
            {
                case SNAPSHOT_AUTO_COMMENT_YES:
                    String snapshotDefaultComment = ( String ) this.content.get( SNAPSHOT_DEFAULT_COMMENT );
                    Snapshot.setSnapshotDefaultComment( snapshotDefaultComment );
                    OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
                    snapshotTab.setSnapshotDefaultComment( snapshotDefaultComment );
                    break;

                case SNAPSHOT_AUTO_COMMENT_NO:
                    Snapshot.setSnapshotDefaultComment( null );
                    break;
            }
        }

        val_s = ( String ) this.content.get( SNAPSHOT_COMPARE_SHOW_READ );
        if ( val_s != null )
        {
            int val = Integer.parseInt( val_s );
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowReadButton( val );

            if ( val == SNAPSHOT_COMPARE_SHOW_READ_YES )
            {
                this.showRead = true;
            }
            else
            {
                this.showRead = false;
            }
        }

        val_s = ( String ) this.content.get( SNAPSHOT_COMPARE_SHOW_WRITE );
        if ( val_s != null )
        {
            int val = Integer.parseInt( val_s );
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowWriteButton( val );

            if ( val == SNAPSHOT_COMPARE_SHOW_WRITE_YES )
            {
                this.showWrite = true;
            }
            else
            {
                this.showWrite = false;
            }

            val_s = ( String ) this.content.get( SNAPSHOT_COMPARE_SHOW_DELTA );
        }

        if ( val_s != null )
        {
            int val = Integer.parseInt( val_s );
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowDeltaButton( val );

            if ( val == SNAPSHOT_COMPARE_SHOW_DELTA_YES )
            {
                this.showDelta = true;
            }
            else
            {
                this.showDelta = false;
            }

            val_s = ( String ) this.content.get( SNAPSHOT_COMPARE_SHOW_DIFF );
        }

        if ( val_s != null )
        {
            int val = Integer.parseInt( val_s );
            OptionsSnapshotTab snapshotTab = OptionsSnapshotTab.getInstance();
            snapshotTab.selectShowDiffButton( val );

            if ( val == SNAPSHOT_COMPARE_SHOW_DIFF_YES )
            {
                this.showDiff = true;
            }
            else
            {
                this.showDiff = false;
            }
        }
    }

    /**
     * @return Returns the showDelta.
     */
    public boolean isShowDelta ()
    {
        return showDelta;
    }

    /**
     * @param showDelta The showDelta to set.
     */
    public void setShowDelta ( boolean showDelta )
    {
        this.showDelta = showDelta;
    }

    /**
     * @return Returns the showDiff.
     */
    public boolean isShowDiff ()
    {
        return showDiff;
    }

    /**
     * @param showDiff The showDiff to set.
     */
    public void setShowDiff ( boolean showDiff )
    {
        this.showDiff = showDiff;
    }

    /**
     * @return Returns the showRead.
     */
    public boolean isShowRead ()
    {
        return showRead;
    }

    /**
     * @param showRead The showRead to set.
     */
    public void setShowRead ( boolean showRead )
    {
        this.showRead = showRead;
    }

    /**
     * @return Returns the showWrite.
     */
    public boolean isShowWrite ()
    {
        return showWrite;
    }

    /**
     * @param showWrite The showWrite to set.
     */
    public void setShowWrite ( boolean showWrite )
    {
        this.showWrite = showWrite;
    }
    /**
     * @return Returns the separator.
     */
    public String getSeparator() {
        return separator;
    }
    /**
     * @param separator The separator to set.
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
