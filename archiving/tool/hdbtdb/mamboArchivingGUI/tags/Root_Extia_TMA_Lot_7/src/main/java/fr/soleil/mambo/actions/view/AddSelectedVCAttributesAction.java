// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/AddSelectedVCAttributesAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AddSelectedVCAttributesAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: AddSelectedVCAttributesAction.java,v $
// Revision 1.4 2006/12/06 10:15:16 ounsy
// minor changes
//
// Revision 1.3 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.5 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.4 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class AddSelectedVCAttributesAction extends AbstractAction {

	private static final long serialVersionUID = -5268580903604795617L;
	private ViewConfigurationBean viewConfigurationBean;
	private VCAttributesSelectTree selectTree;
	private VCAttributesPropertiesTree propertiesTree;
	private VCPossibleAttributesTree possibleAttributesTree;

	/**
	 * @param name
	 */
	public AddSelectedVCAttributesAction(String name,
			ViewConfigurationBean viewConfigurationBean,
			VCAttributesSelectTree selectTree,
			VCAttributesPropertiesTree propertiesTree,
			VCPossibleAttributesTree possibleAttributesTree) {
		super();
		this.putValue(Action.NAME, name);
		this.viewConfigurationBean = viewConfigurationBean;
		this.selectTree = selectTree;
		this.propertiesTree = propertiesTree;
		this.possibleAttributesTree = possibleAttributesTree;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (viewConfigurationBean != null) {
			Vector<TreePath> listToAdd = possibleAttributesTree
					.getListOfAttributesTreePathUnderSelectedNodes(false);
			if (listToAdd != null && listToAdd.size() != 0) {
				VCAttributesTreeModel model = viewConfigurationBean
						.getEditingModel();
				model.setTree(selectTree);
				try {
					model.addSelectedAttibutes(listToAdd);
					model.reload();

					selectTree.expandAll(true);
					if (propertiesTree != null) {
						propertiesTree.expandAll(true);
					}
					TreeMap<String, ViewConfigurationAttribute> attrs = model
							.getAttributes();
					ViewConfiguration currentViewConfiguration = viewConfigurationBean
							.getEditingViewConfiguration();
					if (currentViewConfiguration != null) {
						currentViewConfiguration.getAttributes().addAttributes(
								attrs);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
