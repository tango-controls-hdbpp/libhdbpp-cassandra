/*
 * Created on Jun 28, 2006
 *
 */
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.archiving.ACAttributesSelectTree;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.tools.Messages;

/**
 * @author GIRARDOT
 */
public class ACAttributesSelectTreeExpandSelectedAction extends AbstractAction {

	public ACAttributesSelectTreeExpandSelectedAction() {
		super();
		this.putValue(Action.NAME, Messages
				.getMessage("ARCHIVING_ACTION_EXPAND_SELECTED_SELECT_BUTTON"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		TreePath path = ACAttributesSelectTree.getInstance().getSelectionPath();
		if (path != null) {
			WaitingDialog.openInstance();
			try {
				ACAttributesSelectTree.getInstance().expandAll(path, true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			WaitingDialog.closeInstance();
			path = null;
		}
	}

}
