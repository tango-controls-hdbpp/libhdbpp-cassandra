package fr.soleil.mambo.actions.view.dialogs.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;

public class AxisChoiceComboListener implements ActionListener {

    private Vector<?>             attributesSelected;
    private VCCustomTabbedPane    tabbedPane;
    private VCEditDialog          editDialog;
    private ViewConfigurationBean viewConfigurationBean;

    public AxisChoiceComboListener(ViewConfigurationBean viewConfigurationBean,
            VCEditDialog editDialog) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        this.editDialog = editDialog;

        attributesSelected = null;

        tabbedPane = new VCCustomTabbedPane(editDialog);
    }

    public void actionPerformed(ActionEvent e) {
        if (tabbedPane.getSelectedIndex() == 2) {
            attributesSelected = editDialog.getAttributesPlotPropertiesTab()
                    .getVcAttributesPropertiesTree().getListOfAttributesToSet();
        }
        if (tabbedPane.getSelectedIndex() == 3) {
            attributesSelected = editDialog.getExpressionTab()
                    .getExpressionTree().getSelectedAttributes();
        }
        saveAxisChoice();
    }

    public void saveAxisChoice() {
        if (attributesSelected == null) {
            return;// nothing to set
        }

        if (tabbedPane.getSelectedIndex() == 2) {
            // the attributes to set properties to
            AttributesPlotPropertiesPanel panel = editDialog
                    .getAttributesPlotPropertiesTab().getPropertiesPanel();
            int axisChoice = panel.getAxisChoice();
            boolean hidden = panel.isHidden();

            // the VC before modification
            ViewConfiguration currentVC = viewConfigurationBean
                    .getEditingViewConfiguration();
            if (currentVC != null) {
                ViewConfigurationAttributes currentVCAttributes = currentVC
                        .getAttributes();

                if (attributesSelected.size() > 1) {
                    if (axisChoice == 0) {
                        // selected "---" value with multi selection
                        return;
                    }
                    if ((axisChoice > 0) && (axisChoice < 4)) {
                        axisChoice--;
                    }
                }

                // Treatment is done on all the selected attributes
                Enumeration<?> enumeration = attributesSelected.elements();
                while (enumeration.hasMoreElements()) {
                    // This element (next) has no properties so the current
                    // properties must be retrieve to avoid
                    // the lost of the current properties values
                    ViewConfigurationAttribute next = (ViewConfigurationAttribute) enumeration
                            .nextElement();

                    ViewConfigurationAttribute currArribute = (ViewConfigurationAttribute) currentVCAttributes
                            .getAttribute(next.getCompleteName());
                    ViewConfigurationAttributePlotProperties currAttribPlotProp = currArribute
                            .getProperties().getPlotProperties();

                    // create a new property object for the next element
                    ViewConfigurationAttributePlotProperties nextPlotProperties = new ViewConfigurationAttributePlotProperties(
                            currAttribPlotProp.getViewType(),
                            axisChoice, // New set value
                            currAttribPlotProp.getBar(), currAttribPlotProp
                                    .getCurve(),
                            currAttribPlotProp.getMarker(), currAttribPlotProp
                                    .getTransform(), hidden);

                    ViewConfigurationAttributeProperties nextProperties = new ViewConfigurationAttributeProperties();
                    nextProperties.setPlotProperties(nextPlotProperties);
                    next.setProperties(nextProperties);

                    // Retrieve the factor value
                    next.setFactor(currArribute.getFactor());

                    currentVCAttributes.addAttribute(next);
                }
            }
        }

        if (tabbedPane.getSelectedIndex() == 3) {

            AttributesPlotPropertiesPanel panel = editDialog.getExpressionTab()
                    .getPropertiesPanel();
            int axisChoice = panel.getAxisChoice();
            if (attributesSelected.size() > 1) {
                if (axisChoice == 0) {// selcted "---" value with multi
                    // selection
                    return;
                }
                if ((axisChoice > 0) && (axisChoice < 4)) {
                    axisChoice--;
                }
            }

            Enumeration<?> enumeration = attributesSelected.elements();
            while (enumeration.hasMoreElements()) {
                ExpressionAttribute next = (ExpressionAttribute) enumeration
                        .nextElement();
                ViewConfigurationAttributePlotProperties properties = next
                        .getProperties();
                properties.setAxisChoice(axisChoice);
                properties = null;
                next = null;
            }
            //
            // ExpressionTree.getInstance().addTreeSelectionListener(
            // ExpressionTreeListener.getInstance());
        }

    }

}
