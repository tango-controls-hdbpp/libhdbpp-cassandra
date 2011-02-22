package fr.soleil.mambo.actions.view.dialogs.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Enumeration;
import java.util.Vector;

import fr.soleil.mambo.actions.view.listeners.ExpressionTreeListener;
import fr.soleil.mambo.actions.view.listeners.VCAttributesPropertiesTreeSelectionListener;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;

public class AxisChoiceComboListener implements ActionListener  {

	Vector attributesSelected;
	VCAttributesPropertiesTree attributesPropertiesTree;
	ExpressionTree expressionTree;
	VCCustomTabbedPane tabbedPane;
	
	public AxisChoiceComboListener() {
		super();
		attributesSelected = null;
		attributesPropertiesTree = null;
		expressionTree = null;
		tabbedPane = VCCustomTabbedPane.getInstance();
	}

	public void actionPerformed(ActionEvent e) {
		if( tabbedPane.getSelectedIndex() == 2 ){
			attributesPropertiesTree = VCAttributesPropertiesTree.getInstance();
			attributesSelected = attributesPropertiesTree.getListOfAttributesToSet();
	    }
		if( tabbedPane.getSelectedIndex() == 3 ){
			expressionTree = ExpressionTree.getInstance();
			attributesSelected = expressionTree.getSelectedAttributes();
		}
		saveAxisChoice();

	}

	public void saveAxisChoice(){
		if ( attributesSelected == null )
        {
            return;//nothing to set
        }
		
		if( tabbedPane.getSelectedIndex() == 2 ){
			//the attributes to set properties to
	        AttributesPlotPropertiesPanel panel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();
	        int axisChoice = panel.getAxisChoice();
	        boolean hidden = panel.isHidden();
	        
	        //the VC before modification
	        ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
	        ViewConfigurationAttributes currentVCAttributes = currentVC.getAttributes();

            if( attributesSelected.size() > 1 ){
    	        if( axisChoice == 0 ){// selcted "---" value with multi selection
    	        	return;
    	        }
    	        if( ( axisChoice > 0 ) && ( axisChoice < 4 ) ){
            		axisChoice--;
    	        }
	        }
	        
            // Treatment is done on all the selected attributes
	        Enumeration enumeration = attributesSelected.elements();     
	        while ( enumeration.hasMoreElements() )
	        { 
	            // This element (next) has no properties so the current properties must be retrieve to avoid 
	            // the lost of the current properties values
	            ViewConfigurationAttribute next = ( ViewConfigurationAttribute ) enumeration.nextElement();
	            
	            ViewConfigurationAttribute currArribute = (ViewConfigurationAttribute) currentVCAttributes.getAttribute (next.getCompleteName());
	            ViewConfigurationAttributePlotProperties currAttribPlotProp = currArribute.getProperties().getPlotProperties();
	            
	            // create a new property object for the next element
	            ViewConfigurationAttributePlotProperties nextPlotProperties = 
	                new ViewConfigurationAttributePlotProperties( currAttribPlotProp.getViewType() ,
	                        axisChoice , // New set value 
	                        currAttribPlotProp.getBar() , currAttribPlotProp.getCurve() , 
	                        currAttribPlotProp.getMarker() , currAttribPlotProp.getTransform(), hidden );
	        
	            ViewConfigurationAttributeProperties nextProperties = new ViewConfigurationAttributeProperties();
	            nextProperties.setPlotProperties( nextPlotProperties );
	            next.setProperties(nextProperties);
	            
	            // Retrieve the factor value
	            next.setFactor( currArribute.getFactor() );

	            currentVCAttributes.addAttribute( next );
	        }

//	        attributesPropertiesTree.setCellRenderer( new VCTreeRenderer() );
	        //ViewConfigurationAttributeProperties.resetCurrentProperties();
		}
		
		if( tabbedPane.getSelectedIndex() == 3 ){
            
            AttributesPlotPropertiesPanel panel = ExpressionTab.getInstance().getPropertiesPanel();
            int axisChoice = panel.getAxisChoice();
            if( attributesSelected.size() > 1 ){
    	        if( axisChoice == 0 ){// selcted "---" value with multi selection
    	        	return;
    	        }
    	        if( ( axisChoice > 0 ) && ( axisChoice < 4 ) ){
            		axisChoice--;
    	        }
	        }
	        

            Enumeration enumeration = attributesSelected.elements();     
	            while ( enumeration.hasMoreElements() )
	            { 
	                ExpressionAttribute next = (ExpressionAttribute)enumeration.nextElement();
	                ViewConfigurationAttributePlotProperties properties = next.getProperties();
	                properties.setAxisChoice(axisChoice);
	                properties= null;
	                next = null;
	            }
            
            ExpressionTree.getInstance().addTreeSelectionListener( ExpressionTreeListener.getInstance() );
//            ExpressionTab.getInstance().setParameters(ExpressionTree.getInstance().getSelectedAttribute());
//            ExpressionTreeListener.getInstance().treeSelectionChange();
		}

	}

}
