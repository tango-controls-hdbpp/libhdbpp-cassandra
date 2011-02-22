/*
 * Created on Jun 28, 2006
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.tools.Messages;

/**
 * @author GIRARDOT
 */
public class VCAttributesPropertiesTreeExpandSelectedAction extends
        AbstractAction {

    private static final long          serialVersionUID = 2578757319014583855L;
    private VCAttributesPropertiesTree vcAttributesPropertiesTree;

    public VCAttributesPropertiesTreeExpandSelectedAction(
            VCAttributesPropertiesTree vcAttributesPropertiesTree) {
        super();
        this.putValue(Action.NAME, Messages
                .getMessage("VIEW_ACTION_EXPAND_SELECTED_PROPERTIES_BUTTON"));
        this.vcAttributesPropertiesTree = vcAttributesPropertiesTree;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        TreePath path = vcAttributesPropertiesTree.getSelectionPath();
        if (path != null) {
            WaitingDialog.openInstance();
            try {
                vcAttributesPropertiesTree.expandAll(path, true);
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
            WaitingDialog.closeInstance();
            path = null;
        }
    }

}
