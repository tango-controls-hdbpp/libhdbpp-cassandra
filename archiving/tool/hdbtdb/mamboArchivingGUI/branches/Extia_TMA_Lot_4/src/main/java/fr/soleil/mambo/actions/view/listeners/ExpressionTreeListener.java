package fr.soleil.mambo.actions.view.listeners;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import fr.soleil.mambo.actions.view.dialogs.listeners.AxisChoiceComboListener;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.ExpressionTab;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;

public class ExpressionTreeListener implements TreeSelectionListener {

	private ExpressionTab expressionTab;

	public ExpressionTreeListener(ExpressionTab expressionTab) {
		super();
		this.expressionTab = expressionTab;
	}

	public void valueChanged(TreeSelectionEvent event) {
		expressionTab.getExpressionTree().saveCurrentSelection();
		treeSelectionSave();
		treeSelectionChange();
	}

	public void treeSelectionSave() {

		ExpressionTree tree = expressionTab.getExpressionTree();
		Vector<ExpressionAttribute> attributes = tree
				.getLastListOfAttributesToSet();
		if (attributes == null || (attributes.size() > 1)) {
			return;// nothing to set
		}

		AttributesPlotPropertiesPanel panel = expressionTab
				.getPropertiesPanel();
		int viewType = panel.getViewType();
		Bar bar = panel.getBar();
		Curve curve = panel.getCurve();
		Marker marker = panel.getMarker();
		Polynomial2OrderTransform transform = panel.getTransform();
		boolean hidden = panel.isHidden();

		Enumeration<ExpressionAttribute> enumeration = attributes.elements();
		while (enumeration.hasMoreElements()) {
			ExpressionAttribute next = enumeration.nextElement();
			ViewConfigurationAttributePlotProperties properties = next
					.getProperties();
			properties.setViewType(viewType);
			properties.setBar(bar);
			properties.setCurve(curve);
			properties.setMarker(marker);
			properties.setTransform(transform);
			properties.setHidden(hidden);
			properties = null;
			next = null;
		}

	}

	public void treeSelectionChange() {

		ExpressionTree tree = expressionTab.getExpressionTree();
		Vector<ExpressionAttribute> attributes = tree.getSelectedAttributes();

		if (attributes == null || (attributes.size() == 0)) {
			return;// nothing to set
		}

		AttributesPlotPropertiesPanel panel = expressionTab
				.getPropertiesPanel();
		panel.setEnabled(!(attributes.size() >= 2));

		DefaultComboBoxModel axisChoiceComboModel = panel
				.getAxisChoiceComboModel();
		JComboBox axisChoiceCombo = panel.getAxisChoiceCombo();
		AxisChoiceComboListener axisChoiceComboListener = panel
				.getAxisChoiceComboListener();
		JTextField factorField = panel.getFactorField();

		if (attributes.size() == 1) {// Select one attribut
			axisChoiceCombo.removeActionListener(axisChoiceComboListener);
			if (axisChoiceComboModel.getSize() == 4)
				axisChoiceComboModel.removeElement("---");
			axisChoiceCombo.addActionListener(axisChoiceComboListener);
			expressionTab.setParameters(expressionTab.getExpressionTree()
					.getSelectedAttribute());
		}

		if (attributes.size() >= 2) {// Multi-Selection

			axisChoiceCombo.removeActionListener(axisChoiceComboListener);
			if (axisChoiceComboModel.getSize() == 3)
				axisChoiceComboModel.insertElementAt("---", 0);
			axisChoiceComboModel.setSelectedItem("---");
			axisChoiceCombo.addActionListener(axisChoiceComboListener);

			factorField.setText("");
		}

	}
}
