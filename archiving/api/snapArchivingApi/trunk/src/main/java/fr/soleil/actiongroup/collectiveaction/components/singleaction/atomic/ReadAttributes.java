package fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.MinimalistActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * Reads attributes from a device. Notifies its listener on completion. 
 * @author CLAISSE 
 */
public class ReadAttributes extends AtomicIndividualAction 
{
    /**
     * The attributes to read
     */
    private String[] attributesToRead;
    
    /**
     * @param _listener The listener to notify on read completion
     * @param _target The device to read from
     * @param _attributesToRead The attributes to read
     */
    public ReadAttributes ( MinimalistActionListener _listener , Target _target , String[] _attributesToRead )
    {
        super ( _listener , _target );
        this.attributesToRead = _attributesToRead;
    }

    @Override
    protected ActionResult executeAtomicAction() throws Throwable 
    {
        long before = System.currentTimeMillis ();
        DeviceAttributeWrapper [] response = this.target.read_attribute ( this.attributesToRead );
        long readTime = System.currentTimeMillis () - before;
        
        return new ActionResult ( response , readTime );
    }
}