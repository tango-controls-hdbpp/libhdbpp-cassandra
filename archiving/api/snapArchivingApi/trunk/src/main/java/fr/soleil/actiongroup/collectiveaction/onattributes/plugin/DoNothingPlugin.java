/*	Synchrotron Soleil 
 *  
 *   File          :  DoNothingPluginAction.java
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
 *   Log: DoNothingPluginAction.java,v 
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
 * A do nothing implementation
 * @author CLAISSE 
 */
public class DoNothingPlugin implements Plugin 
{
    public void execute ( String deviceName , DeviceAttributeWrapper attribute ) 
    {
 
    }

    public void execute(PluginContext context, String deviceName, DeviceAttributeWrapper attribute) throws Exception {
        // TODO Auto-generated method stub
        
    }
}
