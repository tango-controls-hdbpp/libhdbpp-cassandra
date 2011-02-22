package fr.soleil.mambo.actions.view.dialogs.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;

public class FactorFieldListener implements ActionListener {

	private Vector<?> attributesSelected;
	private VCAttributesPropertiesTree attributesPropertiesTree;
	private ExpressionTree expressionTree;
	private VCCustomTabbedPane tabbedPane;
	private ViewConfigurationBean viewConfigurationBean;
	private VCEditDialog editDialog;

	public FactorFieldListener(ViewConfigurationBean viewConfigurationBean,
			VCEditDialog editDialog) {
		super();
		this.viewConfigurationBean = viewConfigurationBean;
		this.editDialog = editDialog;
		attributesSelected = null;
		attributesPropertiesTree = null;
		expressionTree = null;
		tabbedPane = editDialog.getVcCustomTabbedPane();

	}

	public void actionPerformed(ActionEvent e) {

		if (tabbedPane.getSelectedIndex() == 2) {
			attributesPropertiesTree = editDialog
					.getAttributesPlotPropertiesTab()
					.getVcAttributesPropertiesTree();
			attributesSelected = attributesPropertiesTree
					.getListOfAttributesToSet();
		}
		if (tabbedPane.getSelectedIndex() == 3) {
			expressionTree = editDialog.getExpressionTab().getExpressionTree();
			attributesSelected = expressionTree.getSelectedAttributes();
		}

		saveValue();
	}

	public void saveValue() {

		if (attributesSelected == null) {
			return;// nothing to set
		}

		if (tabbedPane.getSelectedIndex() == 2) {
			// Retrieve the factor value
			AttributesPlotPropertiesPanel panel = editDialog
					.getAttributesPlotPropertiesTab().getPropertiesPanel();
			double factor = panel.getFactor();

			// the currentVC before modification
			ViewConfiguration currentVC = viewConfigurationBean
					.getEditingViewConfiguration();
			if (currentVC != null) {
				ViewConfigurationAttributes currentVCAttributes = currentVC
						.getAttributes();

				Enumeration<?> enumeration = attributesSelected.elements();
				while (enumeration.hasMoreElements()) {
					// This element (next) has no properties so the current
					// properties must be retrieve to avoid
					// the lost of the current properties values
					ViewConfigurationAttribute next = (ViewConfigurationAttribute) enumeration
							.nextElement();

					ViewConfigurationAttribute currArribute = (ViewConfigurationAttribute) currentVCAttributes
							.getAttribute(next.getCompleteName());
					next.setProperties(currArribute.getProperties());

					// Set the new factor value
					next.setFactor(factor);

					currentVCAttributes.addAttribute(next);
				}

				panel.setFactor(factor);
			}
		}

		if (tabbedPane.getSelectedIndex() == 3) {

			AttributesPlotPropertiesPanel panel = editDialog.getExpressionTab()
					.getPropertiesPanel();
			double factor = panel.getFactor();
			Enumeration<?> enumeration = attributesSelected.elements();
			while (enumeration.hasMoreElements()) {
				ExpressionAttribute next = (ExpressionAttribute) enumeration
						.nextElement();
				next.setFactor(factor);
				next = null;
			}
			panel.setFactor(factor);
		}
	}

}
