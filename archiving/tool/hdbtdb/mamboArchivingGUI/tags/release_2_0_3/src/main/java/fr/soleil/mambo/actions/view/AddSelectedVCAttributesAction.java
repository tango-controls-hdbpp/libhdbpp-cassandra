//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/AddSelectedVCAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AddSelectedVCAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: AddSelectedVCAttributesAction.java,v $
// Revision 1.4  2006/12/06 10:15:16  ounsy
// minor changes
//
// Revision 1.3  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.5  2005/09/26 07:52:25  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.4  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCAttributesRecapTree;
import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.models.VCAttributesTreeModel;


public class AddSelectedVCAttributesAction extends AbstractAction
{
    /**
     * @param name
     */
    public AddSelectedVCAttributesAction ( String name )
    {
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        VCPossibleAttributesTree leftTree = VCPossibleAttributesTree.getInstance();

        Vector listToAdd = leftTree.getListOfAttributesTreePathUnderSelectedNodes( false );
        if ( listToAdd != null && listToAdd.size() != 0 )
        {
            VCAttributesSelectTree rightTree = VCAttributesSelectTree.getInstance();
            VCAttributesTreeModel model = ( VCAttributesTreeModel ) rightTree.getModel();
            //--
            model.setTree( rightTree );
            //--
            try
            {
                model.addSelectedAttibutes( listToAdd );
                model.reload();

                rightTree.expandAll( true );
                VCAttributesPropertiesTree propTree = VCAttributesPropertiesTree.getInstance();
                if ( propTree != null )
                {
                    propTree.expandAll( true );
                }
                VCAttributesRecapTree recapTree = VCAttributesRecapTree.getInstance();
                if ( recapTree != null )
                {
                    recapTree.expandAll( true );
                }
                
                //-------
                
                TreeMap attrs = model.getAttributes();
                ViewConfiguration currentViewConfiguration = ViewConfiguration.getCurrentViewConfiguration();
                currentViewConfiguration.getAttributes().addAttributes( attrs );
                ViewConfiguration.setCurrentViewConfiguration( currentViewConfiguration );
                
                //------
                
                /*for ( int i = 0 ; i < selectedPath.length ; i++ )
		        {
		            if ( selectedPath [ i ].getPathCount () == AttributesTreeModel.CONTEXT_TREE_DEPTH )
		            {
		                String s = selectedPath [ i ].toString();
			            TreePath parent = selectedPath [ i ].getParentPath ();
		                
			            String[] test_s = {"HDB"};
			            TreePath testPath = new TreePath (test_s);
			            rightTree.collapsePath ( testPath );
			            
			            rightTree.makeVisible ( selectedPath [ i ] );
		            }
		        }*/
            }
            catch ( Exception e )
            {
                return;
            }
        }
        /*TreePath [] selectedPath = leftTree.getSelectionPaths ();
        Vector validSelected = null;
        if ( selectedPath != null )
        {
            try
            {
	            validSelected = new Vector ();
	            VCAttributesSelectTree rightTree = VCAttributesSelectTree.getInstance ();
	            
	            for ( int i = 0 ; i < selectedPath.length ; i++ )
		        {
		            String s = selectedPath [ i ].toString();
		            if ( selectedPath [ i ].getPathCount () == AttributesTreeModel.CONTEXT_TREE_DEPTH )
		            {
		                validSelected.add ( selectedPath [ i ] );
		            }
		        }
	            
	            if ( validSelected.size () != 0 )
	            {
	                VCAttributesTreeModel model = (VCAttributesTreeModel) rightTree.getModel ();
	                //-------
	                model.setTree ( rightTree );
	                //--------------
	                model.addSelectedAttibutes ( validSelected ); 
	                
	                //-------
	                Hashtable attrs = model.getAttributes ();
	                ViewConfiguration currentViewConfiguration = ViewConfiguration.getCurrentViewConfiguration ();
	                currentViewConfiguration.getAttributes().addAttributes ( attrs );
	                ViewConfiguration.setCurrentViewConfiguration ( currentViewConfiguration );
	                //------
	                
	            }
	            
	            for ( int i = 0 ; i < selectedPath.length ; i++ )
		        {
		            if ( selectedPath [ i ].getPathCount () == AttributesTreeModel.CONTEXT_TREE_DEPTH )
		            {
		                String s = selectedPath [ i ].toString();
			            TreePath parent = selectedPath [ i ].getParentPath ();
		                
			            String[] test_s = {"HDB"};
			            TreePath testPath = new TreePath (test_s);
			            rightTree.collapsePath ( testPath );
			            
			            rightTree.makeVisible ( selectedPath [ i ] );
		            }
		            }
	            
            }
            catch ( Exception e )
            {
                e.printStackTrace ();
            }
        }*/
    }
}
