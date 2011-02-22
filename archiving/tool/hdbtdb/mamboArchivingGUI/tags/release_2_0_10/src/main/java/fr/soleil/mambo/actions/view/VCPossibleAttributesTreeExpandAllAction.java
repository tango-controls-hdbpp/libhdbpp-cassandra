/*
 * Created on Jun 28, 2006
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.tools.Messages;

/**
 * @author GIRARDOT
 */
public class VCPossibleAttributesTreeExpandAllAction extends AbstractAction {

    private static final long        serialVersionUID = -1559324051691652613L;
    private VCPossibleAttributesTree possibleAttributesTree;

    public VCPossibleAttributesTreeExpandAllAction(
            VCPossibleAttributesTree possibleAttributesTree) {
        super();
        this.putValue(Action.NAME, Messages
                .getMessage("VIEW_ACTION_EXPAND_ALL_POSSIBLE_BUTTON"));
        this.possibleAttributesTree = possibleAttributesTree;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        WaitingDialog.openInstance();
        try {
            possibleAttributesTree.expandAll(true);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        WaitingDialog.closeInstance();
    }

}
