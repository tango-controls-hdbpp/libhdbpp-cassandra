// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/VCAttributesPropertiesTreeSelectionListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class
// VCAttributesPropertiesTreeSelectionListener.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCAttributesPropertiesTreeSelectionListener.java,v $
// Revision 1.4 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.3 2006/05/19 15:03:24 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
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
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
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

public class VCAttributesPropertiesTreeSelectionListener implements
        TreeSelectionListener {

    private ViewConfigurationBean viewConfigurationBean;
    private VCEditDialog          editDialog;

    public VCAttributesPropertiesTreeSelectionListener(
            ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        this.editDialog = editDialog;
    }

    @Override
    public void valueChanged(TreeSelectionEvent event) {
        editDialog.getAttributesPlotPropertiesTab()
                .getVcAttributesPropertiesTree().saveLastSelectionPath();
        treeSelectionAttributeSave();
        treeSelectionAttributesPush();

    }

    public void treeSelectionAttributeSave() {

        VCAttributesPropertiesTree tree = editDialog
                .getAttributesPlotPropertiesTab()
                .getVcAttributesPropertiesTree();
        Vector<ViewConfigurationAttribute> attributes = tree
                .getLastListOfAttributesToSet();
        if (attributes == null || (attributes.size() > 1)) {
            return;// nothing to set
        }

        AttributesPlotPropertiesPanel panel = editDialog
                .getAttributesPlotPropertiesTab().getPropertiesPanel();
        int viewType = panel.getViewType();
        Bar bar = panel.getBar();
        Curve curve = panel.getCurve();
        Marker marker = panel.getMarker();
        Polynomial2OrderTransform transform = panel.getTransform();
        boolean hidden = panel.isHidden();

        // the VC before modification
        ViewConfiguration currentVC = viewConfigurationBean
                .getEditingViewConfiguration();
        if (currentVC != null) {
            ViewConfigurationAttributes currentVCAttributes = currentVC
                    .getAttributes();

            Enumeration<ViewConfigurationAttribute> enumeration = attributes
                    .elements();
            while (enumeration.hasMoreElements()) {
                // This element (next) has neither factor nor axis values so the
                // current
                // values must be retrieved to avoid their loss
                ViewConfigurationAttribute next = enumeration.nextElement();
                ViewConfigurationAttribute currArribute = currentVCAttributes
                        .getAttribute(next.getCompleteName());

                // the properties to set
                ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
                ViewConfigurationAttributePlotProperties currentPlotProperties = currentProperties
                        .getPlotProperties();
                if (!(viewType == currentPlotProperties.getViewType()))
                    currentPlotProperties.setViewType(viewType);
                if (!bar.equals(currentPlotProperties.getBar()))
                    currentPlotProperties.setBar(bar);
                if (!curve.equals(currentPlotProperties.getCurve()))
                    currentPlotProperties.setCurve(curve);
                if (!marker.equals(currentPlotProperties.getMarker()))
                    currentPlotProperties.setMarker(marker);
                if (!transform.equals(currentPlotProperties.getTransform()))
                    currentPlotProperties.setTransform(transform);
                if (!(hidden == currentPlotProperties.isHidden()))
                    currentPlotProperties.setHidden(hidden);

                next.setProperties(currentProperties);

                // Retrieve the factor value
                next.setFactor(currArribute.getFactor());
                next.getProperties().getPlotProperties().setAxisChoice(
                        currArribute.getProperties().getPlotProperties()
                                .getAxisChoice());
                currentVCAttributes.addAttribute(next);
            }
        }

        ViewConfigurationAttributeProperties.resetCurrentProperties();

    }

    public void treeSelectionAttributesPush() {

        VCAttributesPropertiesTree tree = editDialog
                .getAttributesPlotPropertiesTab()
                .getVcAttributesPropertiesTree();
        Vector<ViewConfigurationAttribute> attributes = tree
                .getListOfAttributesToSet();

        if (attributes == null || (attributes.size() == 0)) {
            return;// nothing to set
        }

        AttributesPlotPropertiesPanel panel = editDialog
                .getAttributesPlotPropertiesTab().getPropertiesPanel();
        panel.setEnabled(!(attributes.size() >= 2));

        DefaultComboBoxModel axisChoiceComboModel = panel
                .getDefaultComboBoxModel();
        JComboBox axisChoiceCombo = panel.getAxisChoiceCombo();
        AxisChoiceComboListener axisChoiceComboListener = panel
                .getAxisChoiceComboListener();
        JTextField factorField = panel.getFactorField();

        if (attributes.size() == 1) {// Select one attribut

            // le listener (AxisChoiceComboListener) du combobox est enlev� pour
            // qu'il ne se d�clanche pas, et il est rajouter juste apres
            axisChoiceCombo.removeActionListener(axisChoiceComboListener);
            if (axisChoiceComboModel.getSize() == 4)
                axisChoiceComboModel.removeElement("---");
            axisChoiceCombo.addActionListener(axisChoiceComboListener);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                    .getLastSelectedPathComponent();

            TreeNode[] path = node.getPath();
            String completeName = AttributesTreeModel
                    .translatePathIntoKey(path);

            ViewConfiguration currentViewConfiguration = viewConfigurationBean
                    .getEditingViewConfiguration();
            if (currentViewConfiguration != null) {
                ViewConfigurationAttribute selectedAttribute = currentViewConfiguration
                        .getAttributes().getAttribute(completeName);

                ViewConfigurationAttributePlotProperties plotProperties = new ViewConfigurationAttributePlotProperties();

                if (selectedAttribute != null) {
                    ViewConfigurationAttributeProperties properties = selectedAttribute
                            .getProperties();

                    plotProperties = properties.getPlotProperties();
                    editDialog.getAttributesPlotPropertiesTab()
                            .getPropertiesPanel().setFactor(
                                    selectedAttribute.getFactor());
                }
                viewConfigurationBean
                        .pushAttributesPlotProperties(plotProperties);
            }
        }

        if (attributes.size() >= 2) {// Multi-Selection

            // le listener (AxisChoiceComboListener) du combobox est enlev� pour
            // qu'il ne se d�clanche pas, et il est rajouter juste apres
            axisChoiceCombo.removeActionListener(axisChoiceComboListener);
            if (axisChoiceComboModel.getSize() == 3)
                axisChoiceComboModel.insertElementAt("---", 0);
            axisChoiceComboModel.setSelectedItem("---");
            axisChoiceCombo.addActionListener(axisChoiceComboListener);

            factorField.setText("");
        }
    }

}
