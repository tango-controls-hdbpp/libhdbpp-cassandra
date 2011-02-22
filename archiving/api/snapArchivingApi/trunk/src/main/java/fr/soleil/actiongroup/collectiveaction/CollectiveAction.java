/*	Synchrotron Soleil 
 *  
 *   File          :  IBasicTangoGroup.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  24 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IBasicTangoGroup.java,v 
 *
 */
 /*
 * Created on 24 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction;


/**
 * Models a group action that can be launched by a single call.
 * @author CLAISSE 
 */
public interface CollectiveAction
{
    /**
     * Launches the execution of the group action
     */
    public boolean execute ();
    
    /**
     * Sets the timeout for the execution of the group action on an individual device 
     * @param timeout The timeout
     */
    public void setIndividualTimeout ( int timeout );
}
