//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCFinishAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ValidateVCEditAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: VCFinishAction.java,v $
// Revision 1.7  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.6  2006/09/05 13:43:09  ounsy
// updated for sampling compatibility
//
// Revision 1.5  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.4  2006/05/19 15:03:05  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 09:33:31  ounsy
// minor changes
//
// Revision 1.2  2005/12/15 10:50:35  ounsy
// avoiding a null pointer exception
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
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
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.containers.view.dialogs.GeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class VCFinishAction extends AbstractAction
{
    private static VCFinishAction instance = null;

    /**
     * @return
     */
    public static VCFinishAction getInstance ( String name )
    {
        if ( instance == null )
        {
            instance = new VCFinishAction( name );
        }

        return instance;
    }

    public static VCFinishAction getInstance ()
    {
        return instance;
    }

    /**
     * @param name
     */
    private VCFinishAction ( String name )
    {
        putValue( Action.NAME , name );
        putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        //System.out.println ( "ValidateACEditAction/actionPerformed/START" );
        if ( !verifyVC() )
        {
            return;
        }

        //--setting the generic plot data
        GeneralTabbedPane generalTabbedPane = GeneralTabbedPane.getInstance();
        GeneralChartProperties generalChartProperties = generalTabbedPane.getGeneralChartProperties();
        YAxis y1Axis = generalTabbedPane.getY1Axis();
        YAxis y2Axis = generalTabbedPane.getY2Axis();

        ViewConfigurationData newData = new ViewConfigurationData( generalChartProperties , y1Axis , y2Axis );
        //--setting the generic plot data

        ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
        ViewConfigurationData oldData = currentVC.getData();

        //--setting the edit dates
        if ( currentVC.isNew() )
        {
            newData.setCreationDate( GUIUtilities.now() );
            newData.setPath( null );
        }
        else
        {
            newData.setCreationDate( oldData.getCreationDate() );
            newData.setPath( oldData.getPath() );
        }

        newData.setLastUpdateDate( GUIUtilities.now() );
        //--setting the edit dates

        //--setting the start and end dates and historic parameter
        GeneralTab generalTab = GeneralTab.getInstance();
        boolean dynamic = generalTab.isDynamicDateRange();
        if ( dynamic )
        {
            Timestamp[] range = generalTab.getDynamicStartAndEndDates();
            newData.setStartDate( range[ 0 ] );
            newData.setEndDate( range[ 1 ] );
        }
        else
        {
            newData.setStartDate( generalTab.getStartDate() );
            newData.setEndDate( generalTab.getEndDate() );
        }
        newData.setDynamicDateRange( dynamic );
        newData.setDateRange( generalTab.getDateRange() );
        newData.setHistoric( generalTab.isHistoric() );
        newData.setName( generalTab.getName() );
        newData.setSamplingType( generalTab.getSamplingType() );
        //--setting the start and end dates

        currentVC.setData( newData );
        currentVC.setModified( true );
        
        //currentVC.push();
        ViewConfiguration vc = ViewConfiguration.getSelectedViewConfiguration();
        if (vc == null) {
            vc = new ViewConfiguration();
            ViewConfiguration.setSelectedViewConfiguration(vc);
        }
        vc.setData ( currentVC.getData() );
        vc.setAttributes ( currentVC.getAttributes() );
        vc.setExpressions ( currentVC.getExpressions() );
        vc.setModified( true );
        vc.push ();

        VCEditDialog dialog = VCEditDialog.getInstance();
        dialog.setVisible( false );
    }

    /**
     * @return
     */
    private boolean verifyVC ()
    {
        return verifyDates() && verifyAttributesAreSet();
    }

    private boolean verifyAttributesAreSet ()
    {
        //ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();

        if ( containsNonSetAttributes() )
        {
            String msgTitle = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_TITLE" );
            String msgConfirm = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_LABEL_1" );
            msgConfirm += GUIUtilities.CRLF;
            msgConfirm += Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_LABEL_2" );
            String msgCancel = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_CANCEL" );
            String msgValidate = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_VALIDATE" );
            Object[] options = {msgValidate, msgCancel};

            int confirm = JOptionPane.showOptionDialog
                    ( null ,
                      msgConfirm ,
                      msgTitle ,
                      JOptionPane.DEFAULT_OPTION ,
                      JOptionPane.WARNING_MESSAGE ,
                      null ,
                      options ,
                      options[ 0 ] );
            return confirm == JOptionPane.OK_OPTION;
        }
        else
        {
            return true;
        }
    }

    /**
     * @return
     */
    private boolean containsNonSetAttributes ()
    {
        VCAttributesTreeModel model = VCAttributesTreeModel.getInstance();
        TreeMap htAttr = model.getAttributes();
        if ( htAttr == null )
        {
            return false;
        }

        Set keySet = htAttr.keySet();
        Iterator keyIterator = keySet.iterator();
        while ( keyIterator.hasNext() )
        {
            String nextKey = ( String ) keyIterator.next();
            ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) htAttr.get( nextKey );
            String name = nextValue.getCompleteName();
            ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
            if ( !currentVC.containsAttribute( name ) )
            {
                return true;
            }
            nextValue = currentVC.getAttribute( name );

            if ( nextValue.isEmpty() )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return 8 sept. 2005
     */
    private boolean verifyDates ()
    {
        GeneralTab generalTab = GeneralTab.getInstance();

        boolean ok = true;
        String msg = null;
        long startDate_l = 0;
        long endDate_l = 0;
        Timestamp[] range = generalTab.getDynamicStartAndEndDates();

        try
        {
            Timestamp startDate;
            if ( generalTab.isDynamicDateRange() )
            {
                startDate = range[ 0 ];
            }
            else
            {
                startDate = generalTab.getStartDate();
            }
            startDate_l = startDate.getTime();
        }
        catch ( IllegalArgumentException iae )
        {
            ok = false;
            msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_START_DATE" );
        }

        try
        {
            Timestamp endDate;
            if ( generalTab.isDynamicDateRange() )
            {
                endDate = range[ 1 ];
            }
            else
            {
                endDate = generalTab.getEndDate();
            }
            endDate_l = endDate.getTime();
        }
        catch ( IllegalArgumentException iae )
        {
            ok = false;
            msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_END_DATE" );
        }

        if ( ok )
        {
            if ( endDate_l <= startDate_l )
            {
                ok = false;
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_DATE_RANGE" );
            }
        }


        if ( !ok )
        {
            String title = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_TITLE" );
            JOptionPane.showMessageDialog( null , msg , title , JOptionPane.ERROR_MESSAGE );
        }

        return ok;
    }

}
