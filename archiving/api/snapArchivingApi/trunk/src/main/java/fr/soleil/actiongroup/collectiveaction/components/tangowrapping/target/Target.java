/*	Synchrotron Soleil 
 *  
 *   File          :  ITarget.java
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
 *   Log: ITarget.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target;

import fr.esrf.Tango.DevFailed;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.CommandInfoWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DbDatumWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;

public interface Target 
{
    public String get_name();
    public void set_timeout_millis ( int timeout ) throws DevFailed;
    
    public DeviceDataWrapper command_inout ( String commandName , DeviceDataWrapper commandParameters ) throws DevFailed;
    public DeviceAttributeWrapper[] read_attribute ( String [] attributesToRead ) throws DevFailed;
    
    public AttributeInfoWrapper get_attribute_info ( String attributeName ) throws DevFailed;
    public void set_attribute_info ( AttributeInfoWrapper wrapper ) throws DevFailed;
    
    
    public void put_property ( String propertyName , DbDatumWrapper propertyValue ) throws DevFailed;
    
    public void write_attribute ( DeviceAttributeWrapper [] attributes , Double newValue ) throws DevFailed;
    
    public CommandInfoWrapper[] command_list_query() throws DevFailed;
}
