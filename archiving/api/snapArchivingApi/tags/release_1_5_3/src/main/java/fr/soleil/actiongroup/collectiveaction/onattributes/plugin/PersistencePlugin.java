/*	Synchrotron Soleil 
 *  
 *   File          :  PersistencePluginAction.java
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
 *   Log: PersistencePluginAction.java,v 
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
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PersistenceContext;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PluginContext;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance.AnyAttribute;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance.PersistenceManager;

/**
 * An implementation of IPluginAction where the action consists of:
 * <UL> 
 * <LI> extracting the attribute's type and value
 * <LI> storing the attribute
 * </UL>
 * The persistence management is delegated to the chosen IPersistenceManager implementation
 * @author CLAISSE 
 */
public class PersistencePlugin implements Plugin
{
    public PersistencePlugin ()
    {

    }
    
    public void execute ( PluginContext context, String deviceName, DeviceAttributeWrapper attribute ) throws Exception 
    {
        AnyAttribute anyAttribute = new AnyAttribute ( deviceName , attribute );
        
        PersistenceContext persistenceContext = context.getPersistenceContext ();
        PersistenceManager manager = persistenceContext.getManager();
        manager.store ( anyAttribute , persistenceContext );
    }
}
