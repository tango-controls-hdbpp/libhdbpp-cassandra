//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/SetAttributesGroupProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SetAttributesGroupProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: SetAttributesGroupProperties.java,v $
// Revision 1.8  2007/03/20 14:15:09  ounsy
// added the possibility to hide a dataview
//
// Revision 1.7  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.6  2006/09/27 07:00:08  chinkumo
// correction for Mantis 2230
//
// Revision 1.5  2006/09/20 13:03:29  chinkumo
// Mantis 2230: Separated Set addition in the "Attributes plot properties" tab of the New/Modify VC View
//
// Revision 1.4  2006/05/19 15:03:05  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 09:33:31  ounsy
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
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
/*import javax.swing.tree.DefaultMutableTreeNode;

import mambo.components.archiving.ACAttributesPropertiesTree;*/


public class SetAttributesGroupProperties extends AbstractAction
{
    boolean expression;
    /**
     * @param name
     */
    public SetAttributesGroupProperties ( String name, boolean _expression )
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
            int viewType = panel.getViewType();
            Bar bar = panel.getBar();
            Curve curve = panel.getCurve();
            Marker marker = panel.getMarker();
            Polynomial2OrderTransform transform = panel.getTransform();
            boolean hidden = panel.isHidden();

            Enumeration enumeration = attributes.elements();     
            while ( enumeration.hasMoreElements() )
            { 
                ExpressionAttribute next = (ExpressionAttribute)enumeration.nextElement();
                ViewConfigurationAttributePlotProperties properties = next.getProperties();
                properties.setViewType(viewType);
                properties.setBar(bar);
                properties.setCurve(curve);
                properties.setMarker(marker);
                properties.setTransform(transform);
                properties.setHidden(hidden);
                properties= null;
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
                ViewConfigurationAttribute currArribute = (ViewConfigurationAttribute) currentVCAttributes.getAttribute (next.getCompleteName());
                
                //the properties to set
                ViewConfigurationAttributePlotProperties currentPlotProperties = new ViewConfigurationAttributePlotProperties( viewType ,
//                       Retrieve the axis value
                        currArribute.getProperties().getPlotProperties().getAxisChoice(),
                        bar , curve , marker , transform , hidden );
                //ViewConfigurationAttributePlotProperties currentPlotProperties = new ViewConfigurationAttributePlotProperties( viewType , bar , curve , marker , transform );
                ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
                currentProperties.setPlotProperties( currentPlotProperties );
                next.setProperties( currentProperties );
                
                // Retrieve the factor value
                next.setFactor( currArribute.getFactor() );
                            
                currentVCAttributes.addAttribute( next );
            }

            tree.setCellRenderer( new VCTreeRenderer() );
            ViewConfigurationAttributeProperties.resetCurrentProperties();
        }
    }

}
