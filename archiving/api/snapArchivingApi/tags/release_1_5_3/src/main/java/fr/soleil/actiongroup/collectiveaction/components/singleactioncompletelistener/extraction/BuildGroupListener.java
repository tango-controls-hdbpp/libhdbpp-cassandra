package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponse;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;

/**
 * A listener that is notified when a State group command has been completed for an individual device.
 * @author CLAISSE 
 */
public interface BuildGroupListener extends ActionListener 
{
    public CollectiveResponse getGroupResponse ();
}
