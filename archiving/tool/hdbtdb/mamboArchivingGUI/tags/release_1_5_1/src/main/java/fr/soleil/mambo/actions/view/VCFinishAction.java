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
// $Revision: 1.8 $
//
// $Log: VCFinishAction.java,v $
// Revision 1.8  2008/04/09 10:45:46  achouri
// update principal windows
//
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

import fr.soleil.mambo.actions.view.listeners.ExpressionTreeListener;
import fr.soleil.mambo.actions.view.listeners.VCAttributesPropertiesTreeSelectionListener;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCAttributesRecapTree;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.view.ViewActionPanel;
import fr.soleil.mambo.containers.view.ViewAttributesPanel;
import fr.soleil.mambo.containers.view.ViewAttributesTreePanel;
import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
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
    public static VCFinishAction getInstance ( String name , DateRangeBox dateRangeBox )
    {
        if ( instance == null )
        {
            instance = new VCFinishAction( name , dateRangeBox );
        }

        return instance;
    }

    public static VCFinishAction getInstance ()
    {
        return instance;
    }

	private DateRangeBox dateRangeBox;

    /**
     * @param name
     */
    private VCFinishAction ( String name , DateRangeBox dateRangeBox )
    {
    	this.dateRangeBox = dateRangeBox;
        putValue( Action.NAME , name );
        putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
    	//Save parameters of last attribut 
    	VCCustomTabbedPane tabbedPane = VCCustomTabbedPane.getInstance();
        int oldValue = tabbedPane.getSelectedIndex();
        if( oldValue == 2 ){
        	VCAttributesPropertiesTree.getInstance().saveLastSelectionPath();
        	VCAttributesPropertiesTreeSelectionListener.getInstance().treeSelectionAttributeSave();
        }
        if( oldValue == 3 ){
        	ExpressionTree.getInstance().saveCurrenteSelection();
        	ExpressionTreeListener.getInstance().treeSelectionSave();
        }

    	if ( !verifyVC( dateRangeBox ) )
        {
            return;
        }

    	//--setting the generic plot data
        ChartGeneralTabbedPane generalTabbedPane = ChartGeneralTabbedPane.getInstance();
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
        
//        newData.upDateVCData( dateRangeBox );
        
        //--setting the start and end dates and historic parameter
//      GeneralTab generalTab = GeneralTab.getInstance();
      boolean dynamic = dateRangeBox.isDynamicDateRange();
      if ( dynamic )
      {
          Timestamp[] range = dateRangeBox.getDynamicStartAndEndDates();
          newData.setStartDate( range[ 0 ] );
          newData.setEndDate( range[ 1 ] );
      }
      else
      {
      	newData.setStartDate( dateRangeBox.getStartDate() );
      	newData.setEndDate( dateRangeBox.getEndDate() );
      }
      newData.setDynamicDateRange( dynamic );
      newData.setDateRange( dateRangeBox.getDateRange() );
      //--setting the start and end dates
        GeneralTab generalTab = GeneralTab.getInstance();
        newData.setHistoric( generalTab.isHistoric() );
        newData.setName( generalTab.getName() );
        newData.setSamplingType( generalTab.getSamplingType() );

        currentVC.setData( newData );
        currentVC.setModified( true );
        
        //currentVC.push();
        if( VCEditDialog.getInstance().isNewVC() ){
        	ViewConfiguration.setSelectedViewConfiguration( currentVC );
        }

        ViewConfiguration vc = ViewConfiguration.getSelectedViewConfiguration();
        if (vc == null) {
            vc = new ViewConfiguration();
            ViewConfiguration.setSelectedViewConfigurationAndPush(vc);
        }
        vc.setData ( currentVC.getData() );
        vc.setAttributes ( currentVC.getAttributes() );
        vc.setExpressions ( currentVC.getExpressions() );
        vc.setModified( true );
        vc.push ();

        VCEditDialog dialog = VCEditDialog.getInstance();
        dialog.setVisible( false );
        
        //
        VCAttributesRecapTree recapTree     = VCAttributesRecapTree.getInstance();
        VCAttributesPropertiesTree propTree = VCAttributesPropertiesTree.getInstance();
        if ( recapTree != null ) recapTree.openExpandedPath();
        if ( propTree  != null ) propTree.openExpandedPath();

        ViewAttributesTreePanel.getInstance().getDateRangeBox().update(dateRangeBox);
        ViewAttributesPanel.getInstance().requestToRefresh();//clean Graphe and put border button Refresh to RED

                
    }

    /**
     * @return
     */
    public boolean verifyVC ( DateRangeBox dateRangeBox )
    {
        return ViewConfigurationData.verifyDates(dateRangeBox) && verifyAttributesAreSet();
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


}
