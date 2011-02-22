package fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.AbstractIndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.MinimalistActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * Reads attributes from a device. Notifies its listener on completion. 
 * @author CLAISSE 
 */
public abstract class AtomicIndividualAction extends AbstractIndividualAction 
{
    /**
     * @param _listener The listener to notify on read completion
     * @param _target The device to read from
     * @param _attributesToRead The attributes to read
     */
    public AtomicIndividualAction ( MinimalistActionListener _listener , Target _target )
    {
        super ( _listener , _target );
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() 
    {
        try 
        {
            ActionResult result = this.executeAtomicAction ();
            this.listener.actionSucceeded ( target.get_name () , result );
        } 
        catch (Throwable t) 
        {
            this.listener.actionFailed ( target.get_name () , null , t );
        }
        finally
        {
            this.listener.actionCompleted (); //Latch countdown in the finally clause to avoid deadlocks in case of read failure
        }
    }

    protected abstract ActionResult executeAtomicAction () throws Throwable;
}