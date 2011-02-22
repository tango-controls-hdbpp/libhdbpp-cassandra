//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewAttributesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewAttributesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ViewAttributesPanel.java,v $
// Revision 1.4  2006/10/12 13:22:53  ounsy
// ViewAttributesDetailPanel removed (useless)
//
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
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;



public class ViewAttributesPanel extends JPanel
{
    private static ViewAttributesPanel instance = null;

    private ViewAttributesTreePanel viewAttributesTreePanel;

    /**
     * @return 8 juil. 2005
     */
    public static ViewAttributesPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ViewAttributesPanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.VIEW_COLOR );
        }

        return instance;
    }

    /**
     * 
     */
    private ViewAttributesPanel ()
    {
        super();

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
        this.setLayout( new SpringLayout() );

        //Lay out the panel.
        SpringUtilities.makeCompactGrid( this ,
                                         1 , 1 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 , //xPad, yPad
                                         true );

    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        this.add( viewAttributesTreePanel );
        //this.add ( viewAttributesDetailPanel );
        /*this.add ( new JLabel ( "ViewAttributesPanel, attributes" ) );
        this.add ( new JLabel ( "ViewAttributesPanel, detail" ) );*/
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        viewAttributesTreePanel = ViewAttributesTreePanel.getInstance();
    }

    /**
     * 19 juil. 2005
     */
    private void initBorder ()
    {
        String msg = Messages.getMessage( "VIEW_ATTRIBUTES_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );

    }

}
