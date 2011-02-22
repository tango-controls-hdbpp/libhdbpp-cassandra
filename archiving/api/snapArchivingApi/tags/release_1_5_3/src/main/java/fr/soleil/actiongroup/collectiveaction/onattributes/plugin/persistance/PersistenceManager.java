/*	Synchrotron Soleil 
 *  
 *   File          :  IAttributeStoringManager.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  17 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IAttributeStoringManager.java,v 
 *
 */
 /*
 * Created on 17 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance;

import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PersistenceContext;


/**
 * Makes extracted attributes persistent. 
 * @author CLAISSE 
 */
public interface PersistenceManager 
{
    public void store ( AnyAttribute attribute, PersistenceContext persistenceContext ) throws Exception;
}
