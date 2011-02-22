/*  Synchrotron Soleil 
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
 *   Revision:                      Author:  
 *   Date:                          State:  
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

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.Messages;

/**
 * Models a group action that can be launched by a single call.
 * @author CLAISSE 
 */
public interface CollectiveActionWithMessages extends CollectiveAction, Messages
{
    
}
