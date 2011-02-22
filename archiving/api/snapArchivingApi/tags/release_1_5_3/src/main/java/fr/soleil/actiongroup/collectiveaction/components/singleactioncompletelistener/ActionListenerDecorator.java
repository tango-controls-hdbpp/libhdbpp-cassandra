/*	Synchrotron Soleil 
 *  
 *   File          :  ActionResultListenerDecorator.java
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
 *   Log: ActionResultListenerDecorator.java,v 
 *
 */
 /*
 * Created on 7 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

public class ActionListenerDecorator implements ActionListener 
{
    private ActionListener decorator;
    
    public ActionListenerDecorator ( ActionListener _decorator )
    {
        this.decorator = _decorator;
    }
    
    public synchronized void actionCompleted() 
    {
        this.decorator.actionCompleted ();
    }

    public synchronized void actionFailed(String targetName, ActionResult actionResult,Throwable exception) 
    {
        this.decorator.actionFailed(targetName, actionResult, exception);
    }

    public synchronized void actionSucceeded(String targetName, ActionResult actionResult) 
    {
        this.decorator.actionSucceeded(targetName, actionResult);
    }

    public synchronized boolean hasBeenNotifiedOfFailedActions() 
    {
        return this.decorator.hasBeenNotifiedOfFailedActions();
    }

    public synchronized Map<String, String> getMessages() 
    {
        return this.decorator.getMessages();
    }
}
