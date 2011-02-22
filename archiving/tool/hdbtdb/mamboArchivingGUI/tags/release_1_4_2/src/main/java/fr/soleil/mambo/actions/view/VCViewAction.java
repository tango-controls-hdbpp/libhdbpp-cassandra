//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCViewAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCViewAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.21 $
//
// $Log: VCViewAction.java,v $
// Revision 1.21  2007/08/24 12:51:43  ounsy
// WaitingDialog should allways close now
//
// Revision 1.20  2007/03/05 16:27:33  ounsy
// non-static DataBase
//
// Revision 1.19  2006/12/06 12:35:13  ounsy
// better error and waiting dialog management
//
// Revision 1.18  2006/10/04 10:00:06  ounsy
// modified the prepareLegends() method to something  cleaner
//
// Revision 1.17  2006/09/05 14:01:04  ounsy
// updated for sampling compatibility
//
// Revision 1.16  2006/08/31 12:18:14  ounsy
// informations about forcing export only in case of tdb
//
// Revision 1.15  2006/08/30 12:24:02  ounsy
// minor changes
//
// Revision 1.14  2006/08/29 14:04:06  ounsy
// waiting dialog becomes static
//
// Revision 1.13  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.12  2006/07/18 10:22:28  ounsy
// minor changes
//
// Revision 1.11  2006/07/13 12:50:33  ounsy
// added optionnal doForceTdbExport
//
// Revision 1.10  2006/05/16 12:06:16  ounsy
// minor changes
//
// Revision 1.9  2006/05/16 09:35:20  ounsy
// minor changes
//
// Revision 1.8  2006/05/15 09:24:56  ounsy
// waiting dialog added
//
// Revision 1.7  2006/05/05 14:59:14  ounsy
// minor changes
//
// Revision 1.6  2006/05/05 14:36:41  ounsy
// "View" forces export to TDB
//
// Revision 1.5  2006/04/05 13:42:09  ounsy
// optimization
//
// Revision 1.4  2006/02/01 14:06:00  ounsy
// minor changes (small date bug corrected)
//
// Revision 1.3  2005/12/15 10:54:04  ounsy
// avoiding a null pointer exception + minor changes
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.dialogs.VCViewDialog;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.VCOptions;
import fr.soleil.mambo.tools.Messages;


public class VCViewAction extends AbstractAction
{
    /**
     * @param name
     */
    public VCViewAction ( String name )
    {
        putValue( Action.NAME , name );
        putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        System.gc();
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration();
        if (selectedVC == null)
        {
            return;
        }
        WaitingDialog.openInstance();
        try
        {
            
            //Options options = Options.
            VCOptions vcOptions = Options.getInstance ().getVcOptions ();
            boolean doForceTdbExport = vcOptions.isDoForceTdbExport (); 
            ILogger logger = LoggerFactory.getCurrentImpl();
            
            if ( selectedVC.getData() != null
                    && !selectedVC.getData().isHistoric() )
            {
                if ( doForceTdbExport )
                {
                    if ( selectedVC.getAttributes() != null )
                    {
                        logger.trace( ILogger.LEVEL_INFO, Messages.getMessage( "VIEW_ACTION_EXPORT" ) );
                        TreeMap attrs = selectedVC.getAttributes().getAttributes();
                        Set keySet = attrs.keySet();
                        Iterator keyIterator = keySet.iterator();
                        while (keyIterator.hasNext())
                        {
                            Object obj = keyIterator.next();
                            if ( obj instanceof String )
                            {
                                String attributeName = (String) obj;
                                try
                                {
                                    //this.manager.ExportData2Tdb( attributeName );
                                    IArchivingManager archivingManager = ArchivingManagerFactory.getCurrentImpl ();
                                    archivingManager.exportData2Tdb ( attributeName );
                                }
                                catch (ArchivingException e)
                                {
                                    String msg = Messages.getMessage( "VIEW_ACTION_EXPORT_WARNING" ) + attributeName;
                                    logger.trace( ILogger.LEVEL_WARNING, msg );
                                    logger.trace( ILogger.LEVEL_WARNING, e );
                                }
                            }
                        }
                    }
                }
                else
                {
                    logger.trace( ILogger.LEVEL_INFO, Messages.getMessage( "VIEW_ACTION_NO_EXPORT" ) );
                }
            }
            prepareLegends();
            //-------------------
            VCViewDialog.getInstance( true ).pack();
            WaitingDialog.closeInstance();
            VCViewDialog.getInstance( false ).setVisible( true );
            VCViewDialog.clearInstance();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            WaitingDialog.closeInstance();
        }
    }

    private void prepareLegends ()
    {
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration();
        selectedVC.refreshContent ();
    }
    
    /**
     * 6 sept. 2005
     */
    /*private void prepareLegends ()
    {
        //i don't know why but this makes the curve legends appear correctly..

        GeneralTabbedPane generalTabbedPane = GeneralTabbedPane.getInstance();
        GeneralChartProperties generalChartProperties = generalTabbedPane.getGeneralChartProperties();
        YAxis y1Axis = generalTabbedPane.getY1Axis();
        YAxis y2Axis = generalTabbedPane.getY2Axis();
        ViewConfigurationData newData = new ViewConfigurationData( generalChartProperties , y1Axis , y2Axis );

        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration();
        ViewConfigurationData oldData = selectedVC.getData();
        newData.setCreationDate( oldData.getCreationDate() );
        newData.setPath( oldData.getPath() );

        newData.setLastUpdateDate( GUIUtilities.now() );

        //--do not remove
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
        //--do not remove
    }*/

}
