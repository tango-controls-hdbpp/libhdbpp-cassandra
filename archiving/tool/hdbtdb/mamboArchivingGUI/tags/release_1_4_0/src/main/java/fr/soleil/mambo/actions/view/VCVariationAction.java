//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCVariationAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationAction.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: VCVariationAction.java,v $
// Revision 1.2  2006/02/01 14:05:23  ounsy
// minor changes (small date bug corrected)
//
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.dialogs.VCVariationDialog;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.models.VCVariationRankingTableModel;
import fr.soleil.mambo.models.VCVariationResultTableModel;


public class VCVariationAction extends AbstractAction
{
    /**
     * @param name
     */
    public VCVariationAction ( String name )
    {
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        if (ViewConfiguration.getSelectedViewConfiguration() == null) return;
        if ( ViewConfiguration.getSelectedViewConfiguration().getData().isDynamicDateRange() )
        {
            Timestamp[] range = ViewConfiguration.getSelectedViewConfiguration().getData().getDynamicStartAndEndDates();
            ViewConfiguration.getSelectedViewConfiguration().getData().setStartDate( range[ 0 ] );
            ViewConfiguration.getSelectedViewConfiguration().getData().setEndDate( range[ 1 ] );
        }
        VCVariationResultTableModel.getInstance().update();
        VCVariationRankingTableModel.getInstance().update();
        VCVariationDialog.getInstance( true ).setVisible(true);
    }

}
