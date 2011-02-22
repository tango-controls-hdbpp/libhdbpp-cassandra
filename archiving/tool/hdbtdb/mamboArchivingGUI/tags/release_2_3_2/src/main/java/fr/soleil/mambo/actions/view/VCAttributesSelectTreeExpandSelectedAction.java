/*
 * Created on Jun 28, 2006
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.tools.Messages;

/**
 * @author GIRARDOT
 */
public class VCAttributesSelectTreeExpandSelectedAction extends AbstractAction {

	private static final long serialVersionUID = -487475740173180398L;
	private VCAttributesSelectTree vcAttributesSelectTree;

	public VCAttributesSelectTreeExpandSelectedAction(
			VCAttributesSelectTree vcAttributesSelectTree) {
		super();
		this.putValue(Action.NAME, Messages
				.getMessage("VIEW_ACTION_EXPAND_SELECTED_SELECT_BUTTON"));
		this.vcAttributesSelectTree = vcAttributesSelectTree;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TreePath path = vcAttributesSelectTree.getSelectionPath();
		if (path != null) {
			WaitingDialog.openInstance();
			try {
				vcAttributesSelectTree.expandAll(path, true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			WaitingDialog.closeInstance();
			path = null;
		}
	}

}
