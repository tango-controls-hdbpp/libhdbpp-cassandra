package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;


public class VCSetBalancingFactorAttributesGroupProperties extends AbstractAction
{
    protected boolean expression;

	public VCSetBalancingFactorAttributesGroupProperties ( String name, boolean _expression )
    {
        putValue( Action.NAME , name );
        putValue( Action.SHORT_DESCRIPTION , name );
        this.expression = _expression;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {      
        if (expression)
        {
            ExpressionTree tree = ExpressionTree.getInstance();
            Vector attributes = tree.getSelectedAttributes();
            if ( attributes == null )
            {
                return;//nothing to set
            }

            AttributesPlotPropertiesPanel panel = ExpressionTab.getInstance().getPropertiesPanel();
            double factor = panel.getFactor();
            Enumeration enumeration = attributes.elements();     
            while ( enumeration.hasMoreElements() )
            { 
                ExpressionAttribute next = (ExpressionAttribute)enumeration.nextElement();
                next.setFactor(factor);
                next = null;
            }
        }
        else
        {
            //the attributes to set properties to
            VCAttributesPropertiesTree tree = VCAttributesPropertiesTree.getInstance();
            Vector attributes = tree.getListOfAttributesToSet();
            if ( attributes == null )
            {
                return;//nothing to set
            }

            // Retrieve the factor value
            AttributesPlotPropertiesPanel panel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();
            double factor = panel.getFactor();
            
            // the currentVC before modification
            ViewConfiguration currentVC = ViewConfiguration.getCurrentViewConfiguration();
            ViewConfigurationAttributes currentVCAttributes = currentVC.getAttributes();
            
            Enumeration enumeration = attributes.elements();
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

            tree.setCellRenderer( new VCTreeRenderer() );
            //ViewConfigurationAttributeProperties.resetCurrentProperties();
        }
    }
}
