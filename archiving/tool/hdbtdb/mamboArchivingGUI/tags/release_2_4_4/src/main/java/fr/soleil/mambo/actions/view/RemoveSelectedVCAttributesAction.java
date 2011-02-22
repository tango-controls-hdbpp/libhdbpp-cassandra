// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/RemoveSelectedVCAttributesAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class RemoveSelectedVCAttributesAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: RemoveSelectedVCAttributesAction.java,v $
// Revision 1.4 2006/11/30 13:59:49 ounsy
// calling repainting methods on the trees to avoid the occasional bug when
// using the remove action on the tree's root
//
// Revision 1.3 2006/08/07 13:03:07 ounsy
// trees and lists sort
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class RemoveSelectedVCAttributesAction extends AbstractAction {

	private static final long serialVersionUID = 1120091539622825833L;
	private ViewConfigurationBean viewConfigurationBean;
	private VCEditDialog editDialog;

	/**
	 * @param name
	 */
	public RemoveSelectedVCAttributesAction(String name,
			ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {
		this.putValue(Action.NAME, name);
		this.viewConfigurationBean = viewConfigurationBean;
		this.editDialog = editDialog;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ViewConfiguration currentViewConfiguration = viewConfigurationBean
				.getEditingViewConfiguration();
		if (currentViewConfiguration != null) {
			VCAttributesSelectTree rightTree = editDialog.getAttributesTab()
					.getSelectedAttributesTree();
			Vector<TreePath> listToRemove = rightTree
					.getListOfAttributesTreePathUnderSelectedNodes(true);
			if (listToRemove != null) {
				VCAttributesTreeModel model = viewConfigurationBean
						.getEditingModel();
				if (listToRemove.size() != 0) {
					model.removeSelectedAttributes(listToRemove);
					model.reload();

					rightTree.revalidate();
					rightTree.updateUI();
					rightTree.repaint();

					if (currentViewConfiguration.getAttributes() != null) {
						currentViewConfiguration.getAttributes()
								.removeAttributesNotInList(
										model.getAttributes());
					}

					rightTree.expandAll(true);
					VCAttributesPropertiesTree propTree = editDialog
							.getAttributesPlotPropertiesTab()
							.getVcAttributesPropertiesTree();
					if (propTree != null) {
						propTree.expandAll(true);
						propTree.revalidate();
						propTree.updateUI();
						propTree.repaint();
					}
				}
			}
		}
	}
}
