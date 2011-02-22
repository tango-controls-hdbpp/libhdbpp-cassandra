//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/BensikinMainPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BensikinMainPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: BensikinMainPanel.java,v $
// Revision 1.7  2006/01/10 13:28:30  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:18:31  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.components.BensikinToolbar;
import fr.soleil.bensikin.containers.context.ContextPanel;
import fr.soleil.bensikin.containers.messages.MessagesPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotPanel;


/**
 * The container directly under the top-level container. Contains everything onscreen except for popup
 * dialogs.
 * A singleton class.
 *
 * @author CLAISSE
 */
public class BensikinMainPanel extends JPanel
{

    private static final int INITIAL_MESSAGE_SPLIT_POSITION_HIRES = 840;
    private static final int INITIAL_CONTEXT_SPLIT_POSITION_HIRES = 540;
    private static final int INITIAL_MESSAGE_SPLIT_POSITION_LORES = 560;
    private static final int INITIAL_CONTEXT_SPLIT_POSITION_LORES = 540;

    private int initialMessageSplitPosition;
    private int initialContextSplitPosition;

    private static final int MESSAGE_SPLIT_DIVIDER_SIZE_HIRES = 8;
    private static final int CONTEXT_SPLIT_DIVIDER_SIZE_HIRES = 8;
    private static final int MESSAGE_SPLIT_DIVIDER_SIZE_LORES = 5;
    private static final int CONTEXT_SPLIT_DIVIDER_SIZE_LORES = 5;
    private int messageSplitDividerSize;
    private int contextSplitDividerSize;

    private static final double MESSAGE_SPLIT_RESIZE_WEIGHT = 1.0;
    private static final double CONTEXT_SPLIT_RESIZE_WEIGHT = 0.5;

    private static BensikinMainPanel bensikinMainPanelInstance = null;
    private JScrollPane messageScrollPane;

    /**
     * Instantiates itself if necessary, returns the instance.
     */
    public static BensikinMainPanel getInstance ()
    {
        if ( bensikinMainPanelInstance == null )
        {
            bensikinMainPanelInstance = new BensikinMainPanel();
        }

        return bensikinMainPanelInstance;
    }

    /**
     * Builds the panel.
     */
    private BensikinMainPanel ()
    {

        this.initSplitsPositions();

        //START HORIZONTAL SPLIT
        JSplitPane contextSplit = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , false );

        contextSplit.setDividerSize( contextSplitDividerSize );
        //contextSplit.setDividerSize(15);
        contextSplit.setResizeWeight( CONTEXT_SPLIT_RESIZE_WEIGHT );
        contextSplit.setOneTouchExpandable( true );
        contextSplit.setDividerLocation( initialContextSplitPosition );
        JScrollPane contextScrollPane = new JScrollPane( ContextPanel.getInstance() );
        JScrollPane snapshotScrollPane = new JScrollPane( SnapshotPanel.getInstance() );
        contextSplit.setLeftComponent( contextScrollPane );
        contextSplit.setRightComponent( snapshotScrollPane );
        //END HORIZONTAL SPLIT

        //START VERTICAL SPLIT
        JSplitPane messageSplit = new JSplitPane( JSplitPane.VERTICAL_SPLIT , true );
        messageSplit.setDividerSize( messageSplitDividerSize );
        messageSplit.setResizeWeight( MESSAGE_SPLIT_RESIZE_WEIGHT );
        messageSplit.setOneTouchExpandable( true );
        messageSplit.setDividerLocation( initialMessageSplitPosition );
        messageSplit.setTopComponent( contextSplit );
        messageScrollPane = new JScrollPane( MessagesPanel.getInstance() );
        messageScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        messageSplit.setBottomComponent( messageScrollPane );

        JPanel mainGrid = new JPanel();
        mainGrid.setLayout( new GridLayout( 1 , 0 ) );
        mainGrid.add( messageSplit );
        //GUIUtilities.addDebugBorderToPanel ( mainGrid , true , Color.BLACK , 5 );
        //END VERTICAL SPLIT

        //START MAIN PART
        this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );

        JPanel toolBarPane = new JPanel();
        toolBarPane.setLayout( new GridLayout( 1 , 0 ) );
        toolBarPane.add( BensikinToolbar.getInstance() );
        toolBarPane.setMaximumSize( new Dimension( Integer.MAX_VALUE , 30 ) );

        this.add( toolBarPane );
        this.add( mainGrid );
        //GUIUtilities.addDebugBorderToPanel ( this , true , Color.BLACK , 5 );
        //END MAIN PART
    }

    /**
     * Sets up the Split panes dividers locations, so that everything is usable in low res
     */
    private void initSplitsPositions ()
    {
        if ( Bensikin.isHires() )
        {
            initialMessageSplitPosition = INITIAL_MESSAGE_SPLIT_POSITION_HIRES;
            initialContextSplitPosition = INITIAL_CONTEXT_SPLIT_POSITION_HIRES;

            messageSplitDividerSize = MESSAGE_SPLIT_DIVIDER_SIZE_HIRES;
            contextSplitDividerSize = CONTEXT_SPLIT_DIVIDER_SIZE_HIRES;
        }
        else
        {
            initialMessageSplitPosition = INITIAL_MESSAGE_SPLIT_POSITION_LORES;
            initialContextSplitPosition = INITIAL_CONTEXT_SPLIT_POSITION_LORES;

            messageSplitDividerSize = MESSAGE_SPLIT_DIVIDER_SIZE_LORES;
            contextSplitDividerSize = CONTEXT_SPLIT_DIVIDER_SIZE_LORES;
        }
    }


    /**
     * Scrolls down the log message area to make the latest log visible.
     */
    public void scrollDownToLatestMessage ()
    {
        JViewport viewport = messageScrollPane.getViewport();
        Point point = new Point( 0 , 2000 );
        Dimension dim = new Dimension();
        Rectangle contentRect = new Rectangle( point , dim );
        viewport.scrollRectToVisible( contentRect );
    }

}
