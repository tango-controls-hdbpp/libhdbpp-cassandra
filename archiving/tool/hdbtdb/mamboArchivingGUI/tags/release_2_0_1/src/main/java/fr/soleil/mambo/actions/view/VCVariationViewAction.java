//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCVariationViewAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationViewAction.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationViewAction.java,v $
// Revision 1.5  2006/10/25 08:00:46  ounsy
// replaced calls to show() by calls to setVisible(true)
//
// Revision 1.4  2006/09/05 13:43:21  ounsy
// updated for sampling compatibility
//
// Revision 1.3  2006/02/01 14:05:38  ounsy
// minor changes (small date bug corrected)
//
// Revision 1.2  2005/12/15 10:53:11  ounsy
// table selection for "variations"
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

import fr.soleil.mambo.components.view.VCVariationRankingTable;
import fr.soleil.mambo.components.view.VCVariationResultTable;
import fr.soleil.mambo.containers.view.VCVariationPanel;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.VCVariationViewDialog;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.models.VCVariationRankingTableModel;
import fr.soleil.mambo.models.VCVariationResultTableModel;
import fr.soleil.mambo.tools.GUIUtilities;


public class VCVariationViewAction extends AbstractAction
{
    /**
     * @param name
     */
    public VCVariationViewAction ( String name )
    {
        putValue( Action.NAME , name );
        putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        prepareLegends();
        ViewConfiguration svc = ViewConfiguration.getSelectedViewConfiguration();
        ViewConfiguration vc = new ViewConfiguration();
        vc.setData( svc.getData() );
        ViewConfigurationAttributes vcas = new ViewConfigurationAttributes();
        ViewConfigurationAttributes svcas = svc.getAttributes();
        if (VCVariationPanel.getInstance(false).getWorkingTable()) {
            int[] rows = VCVariationResultTable.getInstance( false ).getSelectedRows();
            for ( int i = 0 ; i < rows.length ; i++ )
            {
                String name = ( String ) VCVariationResultTableModel.getInstance().getValueAt( rows[ i ] , 0 );
                ViewConfigurationAttribute vca = svcas.getAttribute( name );
                if ( vca != null )
                {
                    vcas.addAttribute( vca );
                }
            }
        }
        else {
            int[] rows = VCVariationRankingTable.getInstance( false ).getSelectedRows();
            for ( int i = 0 ; i < rows.length ; i++ )
            {
                String name = ( String ) VCVariationRankingTableModel.getInstance().getValueAt( rows[ i ] , 0 );
                ViewConfigurationAttribute vca = svcas.getAttribute( name );
                if ( vca != null )
                {
                    vcas.addAttribute( vca );
                }
            }
        }
        vc.setAttributes( vcas );
        VCVariationViewDialog dialog = new VCVariationViewDialog( vc );
        dialog.setVisible(true);
    }

    /**
     * 6 sept. 2005
     */
    private void prepareLegends ()
    {
        //i don't know why but this makes the curve legends appear correctly..

        ChartGeneralTabbedPane generalTabbedPane = ChartGeneralTabbedPane.getInstance();
        GeneralChartProperties generalChartProperties = generalTabbedPane.getGeneralChartProperties();
        
        //System.out.println ( "VCViewAction/prepareLegends/generalChartProperties---------------" );
        //System.out.println ( generalChartProperties.toString() );
        //System.out.println ( "VCViewAction/prepareLegends/generalChartProperties---------------" );
        
        YAxis y1Axis = generalTabbedPane.getY1Axis();
        YAxis y2Axis = generalTabbedPane.getY2Axis();

        ViewConfigurationData newData = new ViewConfigurationData( generalChartProperties , y1Axis , y2Axis );
        //ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration ();
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration();
        ViewConfigurationData oldData = selectedVC.getData();
        newData.setCreationDate( oldData.getCreationDate() );
        newData.setPath( oldData.getPath() );

        //--setting the edit dates
        newData.setLastUpdateDate( GUIUtilities.now() );
        //--setting the edit dates

        //--do dnot remove
        boolean dynamic = oldData.isDynamicDateRange();
        if ( dynamic )
        {
            Timestamp[] range = oldData.getDynamicStartAndEndDates();
            newData.setStartDate( range[ 0 ] );
            newData.setEndDate( range[ 1 ] );
        }
        else
        {
            newData.setStartDate( oldData.getStartDate() );
            newData.setEndDate( oldData.getEndDate() );
        }
        newData.setDynamicDateRange( dynamic );
        newData.setDateRange( oldData.getDateRange() );
        newData.setHistoric( oldData.isHistoric() );
        newData.setName( oldData.getName() );
        newData.setSamplingType( oldData.getSamplingType() );

        selectedVC.setData( newData );
        //--do dnot remove
    }

}
