package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;


/**
 * A listener, notified by each individual action thread of the result of the group operation on an individual target.
 * After the information from all devices has been accumulated, it is used to get the global information. 
 * @author CLAISSE 
 */
public interface MinimalistActionListener 
{
    /**
     * An individual task has been completed
     */
    public void actionCompleted ();
    
    /**
     * An individual task has succeeded
     * @param targetName The name of the individual target
     * @param actionResult The task's result
     */
    public void actionSucceeded ( String targetName , ActionResult actionResult );
    
    /**
     * An individual task has failed
     * @param targetName The name of the individual target
     * @param exception The exception that made the task fail
     */
    public void actionFailed(String targetName , ActionResult actionResult , Throwable exception );
    
    /**
     * Returns true if at least one individual task has failed. 
     * @return True if at least one individual task has failed
     */
    public boolean hasBeenNotifiedOfFailedActions();
}
