/*  Synchrotron Soleil 
 *  
 *   File          :  TableDeviceReaderListenerImpl.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  11 d�c. 06 
 *  
 *   Revision:                      Author:  
 *   Date:                          State:  
 *  
 *   Log: TableDeviceReaderListenerImpl.java,v 
 *
 */
 /*
 * Created on 11 d�c. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListenerDecorator;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * A listener that's notified when the attributes to modify are ready for the values setting operation.
 * This implementation effectively performs the values setting operation for each attribute and stores the write operation's result message (success or failure).
 * @author CLAISSE 
 */
public class WriteNumericValueListener extends ActionListenerDecorator implements ActionListener 
{
    /**
     * The numeric attributes new value
     */
    private Double newValue;
    
    /**
     * A device name-->device mapping
     */
    protected Map <String,Target> devices; 
    
    public WriteNumericValueListener ( ActionListener _decorator , Double _newValue , Map <String,Target> _devices ) 
    {
        super ( _decorator );
        this.newValue = _newValue;
        this.devices = _devices;
    }

    public void actionSucceeded ( String deviceName , ActionResult readResult )
    {
        Target device = this.devices.get ( deviceName );
        try
        {
            device.write_attribute ( readResult.getAttributesValue () , this.newValue );
            readResult.setNewValue ( this.newValue );
            super.actionSucceeded ( deviceName , readResult );
        }
        catch ( Exception e )
        {
            super.actionFailed ( deviceName , readResult , e );
        }
    }
}
