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
// $Revision: 1.4 $
//
// $Log: ViewPanel.java,v $
// Revision 1.4  2008/04/09 10:45:46  achouri
// remove viewActionPanel
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
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
        super( new GridBagLayout() );
        addComponents();
        initBorder();
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        GridBagConstraints generalConstraints = new GridBagConstraints();
        generalConstraints.fill = GridBagConstraints.BOTH;
        generalConstraints.gridx = 0;
        generalConstraints.gridy = 0;
        generalConstraints.weightx = 1;
        generalConstraints.weighty = 0;
        this.add( ViewGeneralPanel.getInstance(), generalConstraints );

        GridBagConstraints attrConstraints = new GridBagConstraints();
        attrConstraints.fill = GridBagConstraints.BOTH;
        attrConstraints.gridx = 0;
        attrConstraints.gridy = 1;
        attrConstraints.weightx = 1;
        attrConstraints.weighty = 1;
        this.add( ViewAttributesPanel.getInstance(), attrConstraints );
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
