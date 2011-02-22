/*	Synchrotron Soleil 
 *  
 *   File          :  PluginContext.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  6 mars 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: PluginContext.java,v 
 *
 */
 /*
 * Created on 6 mars 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context;

public class PluginContext 
{
    public PluginContext ()
    {
        
    }
    
    private PersistenceContext persistenceContext;

    /**
     * @return the persistenceContext
     */
    public PersistenceContext getPersistenceContext() {
        return this.persistenceContext;
    }

    /**
     * @param persistenceContext the persistenceContext to set
     */
    public void setPersistenceContext(PersistenceContext persistenceContext) {
        this.persistenceContext = persistenceContext;
    }
    
    public boolean requiresPersistence ()
    {
        return true;
    }
}
