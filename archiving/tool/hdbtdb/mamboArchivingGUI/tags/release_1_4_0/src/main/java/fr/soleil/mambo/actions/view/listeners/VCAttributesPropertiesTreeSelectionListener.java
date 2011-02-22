//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/VCAttributesPropertiesTreeSelectionListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCAttributesPropertiesTreeSelectionListener.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCAttributesPropertiesTreeSelectionListener.java,v $
// Revision 1.4  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.3  2006/05/19 15:03:24  ounsy
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
package fr.soleil.mambo.actions.view.listeners;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.models.AttributesTreeModel;



public class VCAttributesPropertiesTreeSelectionListener implements TreeSelectionListener
{
    //private static DefaultMutableTreeNode currentNode;
    //private static VCAttributesTreeModel model;

    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged ( TreeSelectionEvent event )
    {
        VCAttributesPropertiesTree tree = ( VCAttributesPropertiesTree ) event.getSource();
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) tree.getLastSelectedPathComponent();
        //model = ( VCAttributesTreeModel ) tree.getModel();
        if ( node == null || !node.isLeaf() )
        {
            //System.out.println ("out 1" );
            return;
        }

        //currentNode = node;

        TreeNode[] path = node.getPath();
        //String completeName = AttributesTreeModel.translatePathIntoCompleteName ( path );
        String completeName = AttributesTreeModel.translatePathIntoKey( path );

        ViewConfiguration currentViewConfiguration = ViewConfiguration.getCurrentViewConfiguration();
        ViewConfigurationAttribute selectedAttribute = currentViewConfiguration.getAttributes().getAttribute( completeName );
        //GUIUtilities.trace ( 9 , "VCAttributesPropertiesTreeSelectionListener/completeName|" + completeName + "|" );

        ViewConfigurationAttributePlotProperties plotProperties = new ViewConfigurationAttributePlotProperties();
        //ViewConfigurationAttributeTDBProperties TDBProperties = new ViewConfigurationAttributeTDBProperties ();

        if ( selectedAttribute == null )
        {
            //System.out.println ("out 2" );
            //return;
            // we will push empty DB Properties, ie. reset the fields
        }
        else
        {
            ViewConfigurationAttributeProperties properties = selectedAttribute.getProperties();

            plotProperties = properties.getPlotProperties();
            AttributesPlotPropertiesTab.getInstance().getPropertiesPanel().setFactor( selectedAttribute.getFactor() );
            //TDBProperties = properties.getTDBProperties ();
        }

        plotProperties.push();
    }


}
