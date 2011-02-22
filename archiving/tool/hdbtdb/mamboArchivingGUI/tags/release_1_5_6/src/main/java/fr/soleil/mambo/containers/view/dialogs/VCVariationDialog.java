//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCVariationDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationDialog.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCVariationDialog.java,v $
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/02/01 14:09:13  ounsy
// minor changes
//
// Revision 1.2  2005/12/15 11:32:40  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:45  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;

import fr.soleil.mambo.components.view.VCVariationRankingTable;
import fr.soleil.mambo.components.view.VCVariationResultTable;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.VCVariationPanel;
import fr.soleil.mambo.tools.Messages;


public class VCVariationDialog extends JDialog
{
    //private Dimension dim = new Dimension( 750 , 700 );

    private static VCVariationDialog instance = null;

    /**
     * @param
     * @return 8 juil. 2005
     */
    public static VCVariationDialog getInstance ( boolean forceReload )
    {
        if ( instance == null || forceReload )
        {
            instance = new VCVariationDialog();
        }

        return instance;
    }

    /**
     * 
     */
    private VCVariationDialog ()
    {
        super( MamboFrame.getInstance() , Messages.getMessage( "DIALOGS_VARIATION_TITLE" ) , true );

        initComponents();
        addComponents();
        Dimension size = new Dimension();
        size.width = VCVariationPanel.getInstance( false ).getPreferredSize().width + 50;
        size.height = 400;
        setSize( size );
        int mainWidth = MamboFrame.getInstance().getWidth();
        int width = getWidth();
        setLocation( mainWidth - width - 10 , 200 );

    }

    /**
     * 5 juil. 2005
     */
    private void initComponents ()
    {

    }

    /**
     * 15 juin 2005
     */
    /*private void initLayout ()
    {

    }*/

    /**
     * 8 juil. 2005
     */
    private void addComponents ()
    {
        VCVariationPanel vpanel = VCVariationPanel.getInstance( true );
        this.getContentPane().add( vpanel );
        VCVariationResultTable.getInstance( false ).repaint();
        VCVariationRankingTable.getInstance( false ).repaint();
    }

}
