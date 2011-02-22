package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener;

import java.util.Map;

/**
 * Defines the error management interface used to share error messages between a group and its action result listener, and between the group and its client.
 * @author CLAISSE 
 */
public interface Messages 
{
    /**
     * @return A map which keys are the failed action's target, and which values are the correseponding error messages.
     */
    public Map<String, String> getMessages ();
}
