/*	Synchrotron Soleil 
 *  
 *   File          :  PersistenceContext.java
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
 *   Log: PersistenceContext.java,v 
 *
 */
 /*
 * Created on 6 mars 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context;

import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance.PersistenceManager;

public class PersistenceContext
{
    private PersistenceManager manager;

    /**
     * @return the manager
     */
    public PersistenceManager getManager() {
        return this.manager;
    }

    /**
     * @param manager the manager to set
     */
    public void setManager(PersistenceManager manager) {
        this.manager = manager;
    }
    
    
}
