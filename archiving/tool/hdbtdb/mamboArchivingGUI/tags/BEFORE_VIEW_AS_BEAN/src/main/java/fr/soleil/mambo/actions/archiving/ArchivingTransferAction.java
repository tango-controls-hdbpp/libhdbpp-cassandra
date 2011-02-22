//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ArchivingTransferAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingTransferAction.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ArchivingTransferAction.java,v $
// Revision 1.10  2007/08/24 09:23:00  ounsy
// Color index reset on new VC (Mantis bug 5210 part1)
//
// Revision 1.9  2006/11/14 09:35:05  ounsy
// minor changes
//
// Revision 1.8  2006/08/23 10:01:32  ounsy
// some optimizations with less tree model reloading
//
// Revision 1.7  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.6  2006/07/05 12:58:58  ounsy
// VC : data synchronization management
//
// Revision 1.5  2006/06/20 08:40:27  ounsy
// by default, y1 is visible and autoscaled
//
// Revision 1.4  2006/05/19 14:59:14  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:13:57  ounsy
// modified so that the Ac's isHistoric is automatically used
//
// Revision 1.2  2005/12/15 10:43:17  ounsy
// minor changes
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
package fr.soleil.mambo.actions.archiving;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.Collator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.esrf.tangoatk.widget.util.chart.JLAxis;
import fr.soleil.mambo.components.archiving.ACAttributesRecapTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.tango.util.entity.data.Attribute;


public class ArchivingTransferAction extends AbstractAction
{
    private static ArchivingTransferAction instance;

    public static ArchivingTransferAction getInstance () {
        return instance;
    }

    public static ArchivingTransferAction getInstance (String name) {
        if (instance == null) {
            instance = new ArchivingTransferAction(name);
        }
        return instance;
    }

    private ArchivingTransferAction ( String name )
    {
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        //ArchivingTransferChooseDialog.getInstance().show();
        ArchivingConfiguration selectedAC = ArchivingConfiguration.getSelectedArchivingConfiguration ();
        
        VCAttributesTreeModel vcModel = VCAttributesTreeModel.getInstance();
        ACAttributesTreeModel acModel = ACAttributesTreeModel.getInstance();

        // cleaning VC
        vcModel.removeAll();

        //adding attributes
        TreeMap acTable = acModel.getAttributes();
        Set attrSet = acTable.keySet();
        Iterator it = attrSet.iterator();
        TreeMap vcTable = new TreeMap(Collator.getInstance());
        ChartGeneralTabbedPane generalTabbedPane = ChartGeneralTabbedPane.getInstance();
        GeneralChartProperties generalChartProperties = generalTabbedPane.getGeneralChartProperties();
        AttributesPlotPropertiesTab.getInstance().getPropertiesPanel().reset();
        YAxis y1Axis = generalTabbedPane.getY1Axis();
        y1Axis.setAutoScale(true);
        y1Axis.setVisible(true);
        y1Axis.setColor(Color.BLACK);
        y1Axis.setDrawOpposite(false);
        y1Axis.setShowSubGrid(false);
        y1Axis.setScaleMode(JLAxis.LINEAR_SCALE);
        y1Axis.setTitle("");
        YAxis y2Axis = generalTabbedPane.getY2Axis();
        y2Axis.setAutoScale(true);
        y2Axis.setVisible(false);
        y2Axis.setColor(Color.BLACK);
        y2Axis.setDrawOpposite(false);
        y2Axis.setShowSubGrid(false);
        y2Axis.setScaleMode(JLAxis.LINEAR_SCALE);
        y2Axis.setTitle("");

        ViewConfigurationData newData = new ViewConfigurationData( generalChartProperties , y1Axis , y2Axis );

        int timePrecision = 0;
        if (selectedAC != null)
        {
            ArchivingConfigurationAttribute[] attributes = selectedAC.getAttributes().getAttributesList();
            if (attributes.length > 0)
            {
                if (selectedAC.isHistoric())
                {
                    try
                    {
                        timePrecision = attributes[0].getProperties().getHDBProperties().getMode(ArchivingConfigurationMode.TYPE_P).getMode().getModeP().getPeriod()/2;
                    }
                    catch (Exception e)
                    {
                        timePrecision = 0;
                    }
                }
                else
                {
                    try
                    {
                        timePrecision = attributes[0].getProperties().getTDBProperties().getMode(ArchivingConfigurationMode.TYPE_P).getMode().getModeP().getPeriod()/2;
                    }
                    catch (Exception e)
                    {
                        timePrecision = 0;
                    }
                }
            }
        }
        newData.getGeneralChartProperties().setTimePrecision(timePrecision);
        
        //ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
        ViewConfiguration currentVC = new ViewConfiguration ();
        Timestamp now = GUIUtilities.now();
        Timestamp before = new Timestamp( now.getTime() - 3600000 );
        GeneralTab viewTab = GeneralTab.getInstance();
        viewTab.setDynamicDateRange(true);
        String dateRange = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1H" );
        viewTab.setDateRange(dateRange);
        newData.setCreationDate( now );
        newData.setLastUpdateDate(now);
        newData.setStartDate( before );
        newData.setEndDate( now );
        newData.setHistoric( selectedAC.isHistoric () );
        newData.setDynamicDateRange(true);
        newData.setDateRange(dateRange);
        
        if ( selectedAC != null )
        {
            newData.setName ( selectedAC.getName () );
        }
        
        currentVC.setData( newData );
        //newData.setCreationDate()
        while ( it.hasNext() )
        {
            Object key = it.next();
            ViewConfigurationAttribute vca = new ViewConfigurationAttribute( ( Attribute ) acTable.get( key ) );
            vcTable.put( key , vca );
        }
        currentVC.getAttributes().getAttributes().clear();
        currentVC.getAttributes().addAttributes( vcTable );

        currentVC.push();
        ACAttributesRecapTree recapTree = ACAttributesRecapTree.getInstance();
        if ( recapTree != null )
        {
            recapTree.revalidate();
            recapTree.updateUI();
            recapTree.repaint();
        }
    }

}
