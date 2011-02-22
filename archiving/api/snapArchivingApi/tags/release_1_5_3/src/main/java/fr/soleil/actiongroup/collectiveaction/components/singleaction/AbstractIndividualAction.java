package fr.soleil.actiongroup.collectiveaction.components.singleaction;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.MinimalistActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevFailedWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * A Runnable that models the group action's execution on a single target.
 * Keeps a DeviceProxy reference on the target to execute the action.
 * Notifies its listener on completion. 
 * @author CLAISSE 
 */
public abstract class AbstractIndividualAction implements IndividualAction 
{
    /**
     * The device on which the action will be executed
     */
    protected Target target;
    
    /**
     * The action completion listener
     */
    protected MinimalistActionListener listener;
    
    /**
     * Set up a new individual action.
     * @param _listener The listener to notify on action completion
     * @param _target The device to call the action on
     */
    public AbstractIndividualAction ( MinimalistActionListener _listener , Target _target )
    {
        this.target = _target;
        this.listener = _listener;
    }
    
    public void setTimeout ( int timeout ) //throws DevFailedWrapper
    {
        try 
        {
            this.target.set_timeout_millis ( timeout );
        } 
        catch (Exception e) //TO DO 
        {
            //e.printStackTrace();
            DevFailedWrapper wrapper = new DevFailedWrapper ();
            wrapper.initCause(e);
            //throw wrapper;
        }
    }
}