/*	Synchrotron Soleil 
 *  
 *   File          :  DeviceDataWrapper.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  9 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: DeviceDataWrapper.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.Tango.DevState;
import fr.esrf.TangoApi.DeviceData;

public class DeviceDataWrapper 
{
    private DeviceData deviceData;

    /**
     * @param deviceData
     */
    public DeviceDataWrapper(DeviceData deviceData) 
    {
        this.deviceData = deviceData;
    }

    public DevState extractDevState() 
    {
        return this.deviceData.extractDevState();
    }
    
    public DeviceData getCommandArgument() 
    {
        return this.deviceData;
    }
}
