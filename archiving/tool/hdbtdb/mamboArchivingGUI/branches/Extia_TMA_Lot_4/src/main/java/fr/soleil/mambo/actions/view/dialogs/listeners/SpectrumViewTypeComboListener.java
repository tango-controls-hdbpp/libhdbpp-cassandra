package fr.soleil.mambo.actions.view.dialogs.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;

public class SpectrumViewTypeComboListener implements ActionListener {

    private Vector<?>             attributesSelected;
    private VCEditDialog          editDialog;
    private ViewConfigurationBean viewConfigurationBean;

    public SpectrumViewTypeComboListener(
            ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        this.editDialog = editDialog;

        attributesSelected = null;
    }

    public void actionPerformed(ActionEvent e) {
        if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 2) {
            attributesSelected = editDialog.getAttributesPlotPropertiesTab()
                    .getVcAttributesPropertiesTree().getListOfAttributesToSet();
        }
        if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 3) {
            attributesSelected = editDialog.getExpressionTab()
                    .getExpressionTree().getSelectedAttributes();
        }
        saveSpectrumViewChoice();
    }

    public void saveSpectrumViewChoice() {
        if (attributesSelected != null && (attributesSelected.size() == 1)) {
            if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 2) {
                // the attributes to set properties to
                AttributesPlotPropertiesPanel panel = editDialog
                        .getAttributesPlotPropertiesTab().getPropertiesPanel();
                int spectrumViewChoice = panel.getSpectrumViewTypeChoice();
                boolean hidden = panel.isHidden();

                // the VC before modification
                ViewConfiguration currentVC = viewConfigurationBean
                        .getEditingViewConfiguration();
                if (currentVC != null) {
                    ViewConfigurationAttributes currentVCAttributes = currentVC
                            .getAttributes();

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
                                currAttribPlotProp.getAxisChoice(),
                                spectrumViewChoice,
                                currAttribPlotProp.getBar(), currAttribPlotProp
                                        .getCurve(), currAttribPlotProp
                                        .getMarker(), currAttribPlotProp
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

            if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 3) {

                AttributesPlotPropertiesPanel panel = editDialog
                        .getExpressionTab().getPropertiesPanel();
                int spectrumViewChoice = panel.getSpectrumViewTypeChoice();

                Enumeration<?> enumeration = attributesSelected.elements();
                while (enumeration.hasMoreElements()) {
                    ExpressionAttribute next = (ExpressionAttribute) enumeration
                            .nextElement();
                    ViewConfigurationAttributePlotProperties properties = next
                            .getProperties();
                    properties.setAxisChoice(spectrumViewChoice);
                    properties = null;
                    next = null;
                }
                //
                // ExpressionTree.getInstance().addTreeSelectionListener(
                // ExpressionTreeListener.getInstance());
            }
        }

    }

}
