/*	Synchrotron Soleil 
 *  
 *   File          :  ff.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  25 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ff.java,v 
 *
 */
 /*
 * Created on 25 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.plugin;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PluginContext;

/**
 * An action that will be executed for each attribute read from a device.
 * @author CLAISSE 
 */
public interface Plugin 
{
    /**
     * @param context 
     * @param deviceName The device the attribute was read from
     * @param attribute The attribute
     * @throws Exception The action failed
     */
    public void execute ( PluginContext context, String deviceName, DeviceAttributeWrapper attribute ) throws Exception;
}
