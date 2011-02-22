//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ContextPanel.java,v $
// Revision 1.8  2006/01/12 13:53:22  ounsy
// minor changes
//
// Revision 1.7  2006/01/12 10:27:48  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:22:43  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;


/**
 * Contains the context half of the application, ie. a list of contexts (ContextListPanel)
 * and details about the current context (ContextDetailPanel).
 *
 * @author CLAISSE
 */
public class ContextPanel extends JPanel
{
    private static final int INITIAL_DETAIL_SPLIT_POSITION_HIRES = 180;
    private static final int INITIAL_DETAIL_SPLIT_POSITION_LORES = 100;
    private static final int INITIAL_DETAIL_SPLIT_SIZE = 8;
    private int initialDetailSplitPosition;

    private static ContextPanel contextPanelInstance = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static ContextPanel getInstance ()
    {
        if ( contextPanelInstance == null )
        {
            contextPanelInstance = new ContextPanel();
        }

        return contextPanelInstance;
    }

    /**
     * Builds the panel
     */
    private ContextPanel ()
    {
        boolean _hires = Bensikin.isHires();
        initialDetailSplitPosition = _hires ? INITIAL_DETAIL_SPLIT_POSITION_HIRES : INITIAL_DETAIL_SPLIT_POSITION_LORES;

        //JScrollPane detailScrollPane = new JScrollPane( ContextDetailPanel.getInstance() );

        JSplitPane detailSplit = new JSplitPane( JSplitPane.VERTICAL_SPLIT , false );
        detailSplit.setOneTouchExpandable( true );
        detailSplit.setDividerLocation( initialDetailSplitPosition );
        detailSplit.setDividerSize(INITIAL_DETAIL_SPLIT_SIZE);

        detailSplit.setTopComponent( ContextListPanel.getInstance() );
        //detailSplit.setBottomComponent( detailScrollPane );
        detailSplit.setBottomComponent( ContextDetailPanel.getInstance() );

        this.setLayout( new GridLayout( 1 , 0 ) );
        this.add( detailSplit );

        String msg = Messages.getMessage( "CONTEXT_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        this.setBorder( tb );

        GUIUtilities.setObjectBackground( this , GUIUtilities.CONTEXT_COLOR );
    }
}
