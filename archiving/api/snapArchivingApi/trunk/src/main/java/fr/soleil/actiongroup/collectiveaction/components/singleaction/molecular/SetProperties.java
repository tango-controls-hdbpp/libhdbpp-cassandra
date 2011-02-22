package fr.soleil.actiongroup.collectiveaction.components.singleaction.molecular;
import java.util.Iterator;
import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.singleaction.AbstractIndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DbDatumWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * Creates or updates device properties. Notifies its listener on completion. 
 * @author CLAISSE 
 */
public class SetProperties extends AbstractIndividualAction 
{
    /**
     * A property name --> value mapping 
     */
    private Map<String,DbDatumWrapper> propertiesToSet;
    
    /**
     * @param _listener The listener to notify on completion
     * @param _target The device to read from
     * @param _propertiesToSet The device properties to update
     */
    public SetProperties ( ActionListener _listener , Target _target, Map<String,DbDatumWrapper> _propertiesToSet )
    {
        super ( _listener , _target );
        this.propertiesToSet = _propertiesToSet;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() 
    {
        Iterator<String> names = propertiesToSet.keySet ().iterator ();
        while ( names.hasNext () )
        {
            String nextPropertyName = names.next ().trim ();
            DbDatumWrapper nextPropertyValue = propertiesToSet.get ( nextPropertyName );
            ActionResult res = new ActionResult ( nextPropertyName , nextPropertyValue );
            
            try 
            {
                this.target.put_property ( nextPropertyName , nextPropertyValue );
                this.listener.actionSucceeded( target.get_name () + "/" + nextPropertyName , res );
            } 
            catch (Throwable t) 
            {
                this.listener.actionFailed ( target.get_name () + "/" + nextPropertyName , res , t );
            }   
        }
        
        this.listener.actionCompleted ();
    }
}