/*
 * Created on Jun 28, 2006
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.tools.Messages;


/**
 * @author GIRARDOT
 */
public class VCPossibleAttributesTreeExpandSelectedAction extends AbstractAction {

    public VCPossibleAttributesTreeExpandSelectedAction()
    {
        super();
        this.putValue( Action.NAME , Messages.getMessage("VIEW_ACTION_EXPAND_SELECTED_POSSIBLE_BUTTON") );
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
        TreePath path = VCPossibleAttributesTree.getInstance().getSelectionPath();
        if (path != null)
        {
            WaitingDialog.openInstance();
            try
            {
                VCPossibleAttributesTree.getInstance().expandAll(path, true);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
            WaitingDialog.closeInstance();
            path = null;
        }
    }

}
