package fr.soleil.mambo.actions.view.listeners;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.actions.view.dialogs.listeners.PlotPropertyComboListener;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.tools.PopupUtils;

public class ExpressionTreeListener implements TreeSelectionListener {

    private final ExpressionTab expressionTab;

    public ExpressionTreeListener(final ExpressionTab expressionTab) {
	super();
	this.expressionTab = expressionTab;
    }

    @Override
    public void valueChanged(final TreeSelectionEvent event) {
	expressionTab.getExpressionTree().saveCurrentSelection();
	treeSelectionSave();
	try {
	    treeSelectionChange();
	} catch (final ArchivingException e) {
	    PopupUtils.showOptionErrorDialog(null, e.getMessage());
	    e.printStackTrace();
	} catch (final SQLException e) {
	    PopupUtils.showOptionErrorDialog(null, e.getMessage());
	    e.printStackTrace();
	}
    }

    public void treeSelectionSave() {

	final ExpressionTree tree = expressionTab.getExpressionTree();
	final Vector<ExpressionAttribute> attributes = tree.getLastListOfAttributesToSet();
	if (attributes == null || attributes.size() > 1) {
	    return;// nothing to set
	}

	// AttributesPlotPropertiesPanel panel = expressionTab
	// .getPropertiesPanel();
	// PlotProperties plotProperties = panel.getPlotProperties();

	final PlotProperties plotProperties = expressionTab.getPlotProperties();

	final boolean hidden = expressionTab.isHidden();

	final Enumeration<ExpressionAttribute> enumeration = attributes.elements();
	while (enumeration.hasMoreElements()) {
	    ExpressionAttribute next = enumeration.nextElement();
	    ViewConfigurationAttributePlotProperties properties = next.getProperties();
	    properties.setViewType(plotProperties.getViewType());
	    properties.setBar(plotProperties.getBar());
	    properties.setCurve(plotProperties.getCurve());
	    properties.setMarker(plotProperties.getMarker());
	    properties.setTransform(plotProperties.getTransform());
	    properties.setInterpolation(plotProperties.getInterpolation());
	    properties.setSmoothing(plotProperties.getSmoothing());
	    properties.setMath(plotProperties.getMath());
	    properties.setHidden(hidden);
	    next.setName(plotProperties.getCurve().getName());
	    properties = null;
	    next = null;
	}

    }

    public void treeSelectionChange() throws ArchivingException, SQLException {

	final ExpressionTree tree = expressionTab.getExpressionTree();
	final Vector<ExpressionAttribute> attributes = tree.getSelectedAttributes();

	if (attributes == null || attributes.size() == 0) {
	    expressionTab.setEnabledPropertiesPanel(false);
	    expressionTab.getApplyAction().setEnabled(false);
	    expressionTab.setEnableAddButton(true);
	    expressionTab.setEnabledEditing(true);
	    return;
	}

	expressionTab.setEnabledPropertiesPanel(!(attributes.size() >= 2));

	final DefaultComboBoxModel axisChoiceComboModel = expressionTab.getAxisChoiceComboModel();
	final JComboBox axisChoiceCombo = expressionTab.getAxisChoiceCombo();
	final PlotPropertyComboListener axisChoiceComboListener = expressionTab.getPlotPropertyChoiceComboListener();
	final JTextField factorField = expressionTab.getFactorField();

	if (attributes.size() == 1) {// Select one attribut
	    expressionTab.setEnabledEditing(true);
	    axisChoiceCombo.removeActionListener(axisChoiceComboListener);
	    if (axisChoiceComboModel.getSize() == 4) {
		axisChoiceComboModel.removeElement("---");
	    }
	    axisChoiceCombo.addActionListener(axisChoiceComboListener);
	    expressionTab.setParameters(expressionTab.getExpressionTree().getSelectedAttribute());
	}

	if (attributes.size() >= 2) {// Multi-Selection
	    axisChoiceCombo.removeActionListener(axisChoiceComboListener);

	    if (axisChoiceComboModel.getSize() == 3) {
		axisChoiceComboModel.insertElementAt("---", 0);
	    }
	    axisChoiceComboModel.setSelectedItem("---");
	    axisChoiceCombo.addActionListener(axisChoiceComboListener);

	    factorField.setText("");
	    expressionTab.setEnabledPropertiesPanel(false);
	    expressionTab.setEnabledEditing(false);
	}

    }
}
