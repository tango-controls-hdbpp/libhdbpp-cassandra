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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.soleil.mambo.actions.view.dialogs.listeners.AxisChoiceComboListener;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.models.AttributesTreeModel;



public class VCAttributesPropertiesTreeSelectionListener implements TreeSelectionListener
{
    //private static DefaultMutableTreeNode currentNode;
    //private static VCAttributesTreeModel model;

    private static VCAttributesPropertiesTreeSelectionListener instance;

	public static VCAttributesPropertiesTreeSelectionListener getInstance() {
		if (instance == null){
			instance = new VCAttributesPropertiesTreeSelectionListener();
		}

		return instance;
	}

	/* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged ( TreeSelectionEvent event )
    {
    	VCAttributesPropertiesTree.getInstance().saveLastSelectionPath();
    	treeSelectionAttributeSave();
    	treeSelectionAttributesPush();

    }

    public void treeSelectionAttributeSave() {

    	VCAttributesPropertiesTree tree = VCAttributesPropertiesTree.getInstance();
        Vector attributes = tree.getLastListOfAttributesToSet();
        if ( attributes == null || ( attributes.size() > 1 ) )
        {
            return;//nothing to set
        }

    	AttributesPlotPropertiesPanel panel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();
        int viewType = panel.getViewType();
        Bar bar = panel.getBar();
        Curve curve = panel.getCurve();
        Marker marker = panel.getMarker();
        Polynomial2OrderTransform transform = panel.getTransform();
        boolean hidden = panel.isHidden();


        //the VC before modification
        ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
        ViewConfigurationAttributes currentVCAttributes = currentVC.getAttributes();

        Enumeration enumeration = attributes.elements();
        while ( enumeration.hasMoreElements() )
        {
            // This element (next) has no factor nor axis values so the current values must be retrieve to avoid
            // the lost them
            ViewConfigurationAttribute next = ( ViewConfigurationAttribute ) enumeration.nextElement();
            ViewConfigurationAttribute currArribute = currentVCAttributes.getAttribute (next.getCompleteName());

            //the properties to set
//            ViewConfigurationAttributePlotProperties currentPlotProperties = new ViewConfigurationAttributePlotProperties( viewType ,
////                   Retrieve the axis value
//                    currArribute.getProperties().getPlotProperties().getAxisChoice(),
//                    bar , curve , marker , transform , hidden );
            //ViewConfigurationAttributePlotProperties currentPlotProperties = new ViewConfigurationAttributePlotProperties( viewType , bar , curve , marker , transform );

            ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
            ViewConfigurationAttributePlotProperties currentPlotProperties = currentProperties.getPlotProperties();
            if( !(viewType == currentPlotProperties.getViewType()) )currentPlotProperties.setViewType( viewType );
            if( ! bar.equals( currentPlotProperties.getBar() ) )currentPlotProperties.setBar( bar );
            if( ! curve.equals( currentPlotProperties.getCurve() ) )currentPlotProperties.setCurve( curve );
            if( ! marker.equals( currentPlotProperties.getMarker() ) )currentPlotProperties.setMarker( marker );
            if( ! transform.equals( currentPlotProperties.getTransform() ) )currentPlotProperties.setTransform( transform );
            if( !(hidden == currentPlotProperties.isHidden()) )currentPlotProperties.setHidden( hidden );


//            currentProperties.setPlotProperties( currentPlotProperties );
            next.setProperties( currentProperties );

            // Retrieve the factor value
            next.setFactor( currArribute.getFactor() );
            next.getProperties().getPlotProperties().setAxisChoice( currArribute.getProperties().getPlotProperties().getAxisChoice() );
            currentVCAttributes.addAttribute( next );
        }

//        tree.setCellRenderer( new VCTreeRenderer() );
        ViewConfigurationAttributeProperties.resetCurrentProperties();

	}

	public void treeSelectionAttributesPush(){

    	VCAttributesPropertiesTree tree = VCAttributesPropertiesTree.getInstance();
    	Vector attributes = tree.getListOfAttributesToSet();

    	if ( attributes == null || ( attributes.size() == 0 ) )
        {
            return;//nothing to set
        }

    	AttributesPlotPropertiesPanel panel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();
    	panel.setEnabled( !(attributes.size() >= 2) );

        DefaultComboBoxModel  axisChoiceComboModel = panel.getDefaultComboBoxModel();
        JComboBox axisChoiceCombo = panel.getAxisChoiceCombo();
        AxisChoiceComboListener axisChoiceComboListener = panel.getAxisChoiceComboListener();
        JTextField factorField = panel.getFactorField();

        if( attributes.size() == 1 ){//Select one attribut

        	// le listener (AxisChoiceComboListener) du combobox est enlevé pour qu'il ne se déclanche pas, et il est rajouter juste apres
        	axisChoiceCombo.removeActionListener(  axisChoiceComboListener );
    		if( axisChoiceComboModel.getSize() == 4 )axisChoiceComboModel.removeElement("---");
    		axisChoiceCombo.addActionListener( axisChoiceComboListener );

        	DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) tree.getLastSelectedPathComponent();
            //model = ( VCAttributesTreeModel ) tree.getModel();
//            if ( node == null || node.isLeaf() )
//            {
//                return;
//            }
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
//                System.out.println ("out 2" );
//                return;
                // we will push empty DB Properties, ie. reset the fields
            }else{
            ViewConfigurationAttributeProperties properties = selectedAttribute.getProperties();

            plotProperties = properties.getPlotProperties();
            AttributesPlotPropertiesTab.getInstance().getPropertiesPanel().setFactor( selectedAttribute.getFactor() );
            }
            plotProperties.push();
        }

        if( attributes.size() >= 2 ){//Multi-Selection

        	// le listener (AxisChoiceComboListener) du combobox est enlevé pour qu'il ne se déclanche pas, et il est rajouter juste apres
        	axisChoiceCombo.removeActionListener(  axisChoiceComboListener );
    		if( axisChoiceComboModel.getSize() == 3 )axisChoiceComboModel.insertElementAt("---", 0 );
    		axisChoiceComboModel.setSelectedItem("---");
    		axisChoiceCombo.addActionListener( axisChoiceComboListener );

    		factorField.setText("");
        }
    }

}
