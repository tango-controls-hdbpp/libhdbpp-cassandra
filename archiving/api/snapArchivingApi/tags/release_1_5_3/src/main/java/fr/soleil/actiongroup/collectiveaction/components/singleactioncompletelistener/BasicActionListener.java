/*  Synchrotron Soleil 
 *  
 *   File          :  DeviceReaderListenerImpl.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  8 déc. 06 
 *  
 *   Revision:                      Author:  
 *   Date:                          State:  
 *  
 *   Log: DeviceReaderListenerImpl.java,v 
 *
 */
 /*
 * Created on 8 déc. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

/**
 * A listener that counts down when he's notified of an actionCompleted event.
 * @author CLAISSE 
 */
public class BasicActionListener implements ActionListener 
{
    /**
     * True if and only if at least one individual task has failed 
     */
    private boolean hasFailed = false;
    
    /**
     * Used to keep track of the group task progress
     */
    private CountDownLatch latch;
    
    /**
     * @param _latch The countdown
     */
    public BasicActionListener ( CountDownLatch _latch )
    {
        this.latch = _latch;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.CLA.listener.IDeviceReaderListener#notifyReadFailureForDevice(java.lang.String)
     */
    public void actionFailed (String targetName , ActionResult actionResult , Throwable e ) 
    {
        //e.printStackTrace();
        
        synchronized ( this )
        {
            this.hasFailed = true;
        }
    }

    public void actionSucceeded(String targetName, ActionResult actionResult) 
    {
        //DO NOTHING
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.CLA.listener.IDeviceReaderListener#notifyReadComplete()
     */
    public void actionCompleted () 
    {
        synchronized ( this.latch )
        {
            this.latch.countDown ();    
        }
    }
    
    /**
     * @return the hasFailed
     */
    public synchronized boolean hasBeenNotifiedOfFailedActions() 
    {
        return this.hasFailed;
    }

    public Map<String, String> getMessages() 
    {
        //DO NOTHING
        return null;
    }
}
