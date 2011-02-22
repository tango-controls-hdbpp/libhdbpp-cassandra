/*
 * Synchrotron Soleil File : ReadOnlyRightsManager.java Project :
 * bensikinOperator Description : Author : CLAISSE Original : 28 mars 2006
 * Revision: Author: Date: State: Log: ReadOnlyRightsManager.java,v
 */
/*
 * Created on 28 mars 2006 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.actions.roles;

import javax.swing.Action;

import fr.soleil.bensikin.actions.context.AddSelectedContextAttributesAction;
import fr.soleil.bensikin.actions.context.LaunchSnapshotAction;
import fr.soleil.bensikin.actions.context.MatchContextAttributesAction;
import fr.soleil.bensikin.actions.context.MatchPossibleContextAttributesAction;
import fr.soleil.bensikin.actions.context.NewContextAction;
import fr.soleil.bensikin.actions.context.RegisterContextAction;
import fr.soleil.bensikin.actions.context.RemoveSelectedContextAttributesAction;
import fr.soleil.bensikin.actions.context.ValidateAlternateSelectionAction;
import fr.soleil.bensikin.actions.snapshot.EditCommentAction;
import fr.soleil.bensikin.actions.snapshot.OpenEditCommentAction;
import fr.soleil.bensikin.actions.snapshot.SelectAllOrNoneAction;
import fr.soleil.bensikin.actions.snapshot.SetEquipmentsAction;
import fr.soleil.bensikin.actions.snapshot.SnapSpectrumSetAction;
import fr.soleil.bensikin.actions.snapshot.SnapSpectrumSetAllAction;

/**
 * @author CLAISSE
 */
public class ReadOnlyRightsManager implements IRightsManager {

    /**
     * 
     */
    public ReadOnlyRightsManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see
     * bensikin.bensikin.actions.rights.IRightsManager#isGrantedToOperator(javax
     * .swing.AbstractAction)
     */
    // public boolean isGrantedToOperator(BensikinAction action)
    public boolean isGrantedToOperator(Action action) {
        // System.out.println (
        // "CLA/isGrantedToOperator/action/"+action.getClass().toString() );

        if (action instanceof AddSelectedContextAttributesAction) {
            return false;
        }
        if (action instanceof LaunchSnapshotAction) {
            return false;
        }
        if (action instanceof MatchContextAttributesAction) {
            return false;
        }
        if (action instanceof MatchPossibleContextAttributesAction) {
            return false;
        }
        if (action instanceof NewContextAction) {
            return false;
        }
        if (action instanceof RegisterContextAction) {
            return false;
        }
        if (action instanceof RemoveSelectedContextAttributesAction) {
            return false;
        }
        if (action instanceof ValidateAlternateSelectionAction) {
            return false;
        }
        if (action instanceof EditCommentAction) {
            return false;
        }
        if (action instanceof OpenEditCommentAction) {
            return false;
        }
        if (action instanceof SelectAllOrNoneAction) {
            return false;
        }
        if (action instanceof SetEquipmentsAction) {
            return false;
        }
        if (action instanceof SnapSpectrumSetAction) {
            return false;
        }
        if (action instanceof SnapSpectrumSetAllAction) {
            return false;
        }
        return true;
    }

    public void disableUselessFields() {
        // TODO Auto-generated method stub

    }

}
