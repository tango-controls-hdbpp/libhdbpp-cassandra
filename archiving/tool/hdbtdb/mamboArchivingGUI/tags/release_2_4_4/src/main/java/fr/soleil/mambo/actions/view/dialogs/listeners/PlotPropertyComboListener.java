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

/**
 * This is the {@link ActionListener} that listens to the Axis choice combobox and the spectrum view
 * type combobox in the {@link AttributesPlotPropertiesPanel}
 * 
 * @author SOLEIL
 * 
 */
public class PlotPropertyComboListener implements ActionListener {

    private Vector<?> attributesSelected;
    private VCEditDialog editDialog;
    private ViewConfigurationBean viewConfigurationBean;
    private final static int TYPE_AXIS = 0;
    private final static int TYPE_SPECTRUM_VIEW = 1;

    public PlotPropertyComboListener() {
        super();
        attributesSelected = null;
    }

    public ViewConfigurationBean getViewConfigurationBean() {
        return viewConfigurationBean;
    }

    public void setViewConfigurationBean(ViewConfigurationBean viewConfigurationBean) {
        this.viewConfigurationBean = viewConfigurationBean;
    }

    public VCEditDialog getEditDialog() {
        return editDialog;
    }

    public void setEditDialog(VCEditDialog editDialog) {
        this.editDialog = editDialog;
    }

    public void actionPerformed(ActionEvent e) {
        int type = -1;
        if (editDialog != null) {
            if ((e.getSource() == editDialog.getAttributesPlotPropertiesTab().getPropertiesPanel()
                    .getAxisChoiceCombo())
                    || (e.getSource() == editDialog.getExpressionTab().getAxisChoiceCombo())) {
                type = TYPE_AXIS;
            }
            else if ((e.getSource() == editDialog.getAttributesPlotPropertiesTab()
                    .getPropertiesPanel().getSpectrumViewTypeCombo())) {
                type = TYPE_SPECTRUM_VIEW;
            }
            if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 2) {
                attributesSelected = editDialog.getAttributesPlotPropertiesTab()
                        .getVcAttributesPropertiesTree().getListOfAttributesToSet();
            }
            if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 3) {
                attributesSelected = editDialog.getExpressionTab().getExpressionTree()
                        .getSelectedAttributes();
            }
            saveSelectedChoice(type);
        }
    }

    public void saveSelectedChoice(int type) {
        if (attributesSelected != null) {
            if (editDialog.getVcCustomTabbedPane().getSelectedIndex() == 2) {
                // the attributes to set properties to
                AttributesPlotPropertiesPanel panel = editDialog.getAttributesPlotPropertiesTab()
                        .getPropertiesPanel();

                // the VC before modification
                ViewConfiguration currentVC = viewConfigurationBean.getEditingViewConfiguration();
                if (currentVC != null) {
                    ViewConfigurationAttributes currentVCAttributes = currentVC.getAttributes();

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
                                currAttribPlotProp.getViewType(), (type == TYPE_AXIS ? panel
                                        .getAxisChoice() : currAttribPlotProp.getAxisChoice()),
                                currAttribPlotProp.getBar(), currAttribPlotProp.getCurve(),
                                currAttribPlotProp.getMarker(), currAttribPlotProp.getTransform(),
                                currAttribPlotProp.getInterpolation(), currAttribPlotProp
                                        .getSmoothing(), currAttribPlotProp.getMath(),
                                (type == TYPE_SPECTRUM_VIEW ? panel.getSpectrumViewTypeChoice()
                                        : currAttribPlotProp.getSpectrumViewType()),
                                currAttribPlotProp.isHidden());

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
                Enumeration<?> enumeration = attributesSelected.elements();
                while (enumeration.hasMoreElements()) {
                    ExpressionAttribute next = (ExpressionAttribute) enumeration.nextElement();
                    ViewConfigurationAttributePlotProperties properties = next.getProperties();
                    switch (type) {
                        case TYPE_AXIS:
                            properties.setAxisChoice(editDialog.getExpressionTab().getAxisChoice());
                            break;
                    }
                    properties = null;
                    next = null;
                }
            }
        }
    }
}
