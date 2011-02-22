//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/VCVariationResultPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationResultPanel.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: VCVariationResultPanel.java,v $
// Revision 1.1  2005/11/29 18:27:07  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.mambo.components.view.VCVariationResultTable;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class VCVariationResultPanel extends JPanel
{
    private static VCVariationResultPanel instance = null;

    /**
     * @return 8 juil. 2005
     */
    public static VCVariationResultPanel getInstance ( boolean forceReset )
    {
        if ( instance == null || forceReset )
        {
            instance = new VCVariationResultPanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.VIEW_COLOR );
        }

        return instance;
    }

    public VCVariationResultPanel ()
    {
        super();
        JLabel label = new JLabel( Messages.getMessage( "DIALOGS_VARIATION_RESULT" ) ,
                                   JLabel.CENTER );
        label.getPreferredSize().height += 10;
        setLayout( new SpringLayout() );
        add( label );
        add( VCVariationResultTable.getInstance( true ).getTableHeader() );
        VCVariationResultTable.getInstance( false ).setRowHeight( VCVariationResultTable.getInstance( false ).getRowHeight() + 2 );
        add( VCVariationResultTable.getInstance( false ) );
        SpringUtilities.makeCompactGrid( this ,
                                         3 , 1 , //rows, cols
                                         6 , 0 , //initX, initY
                                         6 , 0 , //xPad, yPad
                                         true  //same size for everyone
        );
    }

}
