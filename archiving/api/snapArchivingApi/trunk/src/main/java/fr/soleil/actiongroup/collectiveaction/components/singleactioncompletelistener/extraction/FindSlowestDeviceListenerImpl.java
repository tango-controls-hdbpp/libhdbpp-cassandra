package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListenerDecorator;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

/**
 * A listener for the event "the numeric attributes reading operation from a device is complete".
 * If the operation succeeded, the numeric values are stored. 
 * @author CLAISSE 
 */
public class FindSlowestDeviceListenerImpl extends ActionListenerDecorator implements FindSlowestDeviceListener 
{
    private String slowestDevice;
    private long slowestTime;
    
    public FindSlowestDeviceListenerImpl ( ActionListener _decorator ) 
    {
        super ( _decorator );
    }
   
    public void actionSucceeded ( String deviceName , ActionResult readResult )
    {
        long actionTime = readResult.getActionTime ();
        synchronized ( this )
        {
            if ( actionTime > this.slowestTime )
            {
                this.slowestTime = actionTime;
                this.slowestDevice = deviceName;
            }
        }
        
        super.actionSucceeded ( deviceName, readResult );
    }
    
    public synchronized String getSlowestDevice ()
    {
        return this.slowestDevice;
    }
    
    public synchronized long getSlowestTime ()
    {
        return this.slowestTime;
    }
}
