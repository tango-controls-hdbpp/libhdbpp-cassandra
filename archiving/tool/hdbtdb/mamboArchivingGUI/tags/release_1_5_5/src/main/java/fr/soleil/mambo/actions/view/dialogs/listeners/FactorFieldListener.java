package fr.soleil.mambo.actions.view.dialogs.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;

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
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;

public class FactorFieldListener implements ActionListener  {

	Vector attributesSelected;
	VCAttributesPropertiesTree attributesPropertiesTree;
	ExpressionTree expressionTree;
	VCCustomTabbedPane tabbedPane;

	public FactorFieldListener() {
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
		
		saveValue();
	}
	
	public void saveValue(){
		
		if ( attributesSelected == null )
        {
            return;//nothing to set
        }
		
		if( tabbedPane.getSelectedIndex() == 2 ){
	        // Retrieve the factor value
			AttributesPlotPropertiesPanel panel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();
	        double factor = panel.getFactor();
	        
	        // the currentVC before modification
	        ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
	        ViewConfigurationAttributes currentVCAttributes = currentVC.getAttributes();
	        
	        Enumeration enumeration = attributesSelected.elements();
	        while ( enumeration.hasMoreElements() )
	        {
	            // This element (next) has no properties so the current properties must be retrieve to avoid 
	            // the lost of the current properties values
	            ViewConfigurationAttribute next = ( ViewConfigurationAttribute ) enumeration.nextElement();
	                    
	            ViewConfigurationAttribute currArribute = (ViewConfigurationAttribute) currentVCAttributes.getAttribute (next.getCompleteName());
	            next.setProperties(currArribute.getProperties());
	            
	             // Set the new factor value
	            next.setFactor(factor);
	            
	            currentVCAttributes.addAttribute( next );
	        }

	        panel.setFactor(factor);
//	        attributesPropertiesTree.setCellRenderer( new VCTreeRenderer() );
	        //ViewConfigurationAttributeProperties.resetCurrentProperties();
	        
		}

		if( tabbedPane.getSelectedIndex() == 3 ){

			AttributesPlotPropertiesPanel panel = ExpressionTab.getInstance().getPropertiesPanel();
			double factor = panel.getFactor();
            Enumeration enumeration = attributesSelected.elements();     
            while ( enumeration.hasMoreElements() )
            { 
                ExpressionAttribute next = (ExpressionAttribute)enumeration.nextElement();
                next.setFactor(factor);
                next = null;
            }
            panel.setFactor(factor);
		}
	}

}
