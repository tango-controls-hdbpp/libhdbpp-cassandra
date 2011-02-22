/*	Synchrotron Soleil 
 *  
 *   File          :  IMessagesBuilder.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  7 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IMessagesBuilder.java,v 
 *
 */
 /*
 * Created on 7 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

public interface MessageBuilder 
{
    public String getSuccessMessage ( String targetName , ActionResult actionResult );
    public String getFailureMessage ( String targetName , ActionResult actionResult , Throwable e );
}
