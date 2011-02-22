//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ViewPanel.java,v $
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;



public class ViewPanel extends JPanel
{
    //private static final int INITIAL_DETAIL_SPLIT_POSITION = 200;

    private static ViewPanel instance = null;

    private ViewGeneralPanel viewGeneralPanel;
    private ViewActionPanel viewActionPanel;
    private ViewAttributesPanel viewAttributesPanel;

    /**
     * @return 8 juil. 2005
     */
    public static ViewPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ViewPanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.VIEW_COLOR );
        }

        return instance;
    }

    /**
     * 
     */
    private ViewPanel ()
    {
        initComponents();
        addComponents();
        setLayout();
        initBorder();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout ()
    {
        this.setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        this.add( viewGeneralPanel );
        this.add( viewAttributesPanel );
        this.add( viewActionPanel );

    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        viewGeneralPanel = ViewGeneralPanel.getInstance();
        viewAttributesPanel = ViewAttributesPanel.getInstance();
        viewActionPanel = ViewActionPanel.getInstance();
    }

    /**
     * 19 juil. 2005
     */
    private void initBorder ()
    {
        String msg = Messages.getMessage( "VIEW_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        Border border = ( Border ) ( tb );
        this.setBorder( border );

    }
}
