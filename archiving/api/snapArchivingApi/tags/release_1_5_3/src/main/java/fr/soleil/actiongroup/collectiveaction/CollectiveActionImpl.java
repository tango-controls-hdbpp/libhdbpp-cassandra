/*  Synchrotron Soleil 
 *  
 *   File          :  TangoGroupCLA.java
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
 *   Log: TangoGroupCLA.java,v 
 *
 */
 /*
 * Created on 8 déc. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.SingleActionThreadFactory;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.BasicActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * Models a group action of any kind. 
 
 * Each target (device) is accessed via a DeviceProxy reference.
 * The method that launches the group action's execution is executeGroupAction().
 * 
 * For each target, a new task is submited to the executor service. When it is completed, the task completion listener is notified of the task success or failure. 
 * Thread pooling (using an executor service) drastically reduces the response time when there are many successive tasks.
 * To further reduce the thread creation load, the ExecutorService is a singleton, which can therefore be used by distinct groups. 
 * 
 * This class is abstract:
 * <UL>
 * <LI>The implementation of the individual target task depends on what subclass is used (getTask is abstract)  
 * <LI>The implementation of the individual task completion listener depends on what subclass is used (getTaskCompletionListener is abstract)
 * </UL>
 * 
 * A synchronized countdown is used to wait for the completion of the common task for all devices.
 * The group action is over when 0 is reached. 
 * 
 * @author CLAISSE 
 */
public abstract class CollectiveActionImpl implements CollectiveAction
{
    /**
     * The targets of the group action
     */
    protected Target[] targets = null;
    
    /**
     * The list of attributes to use for each device
     * (unused for tasks not related to attributes)
     */
    protected String[][] attributes = null;
    
    /**
     * The threads pooling service
     */
    protected static ExecutorService executorService;
    
    /**
     * Used to wait for the group action's completion  
     */
    protected CountDownLatch latch;
    
    /**
     * Listens to task completion
     */
    protected ActionListener taskCompletionListener;
    
    /**
     * The timeout for executing the action on one device
     */
    protected int individualTimeout;
    
    /**
     * @param _targets The targets of the group action
     * @param _attributes The list of attributes to use for each device (unused for tasks not related to attributes)
     */
    protected CollectiveActionImpl(Target[] _targets, String[][] _attributes) 
    {
        this.targets = _targets;
        this.attributes = _attributes;
        
        this.setupExecutorService ();
    }
    
    /**
     * Instantiates the executor service singleton
     */
    private void setupExecutorService() 
    {
        if ( executorService == null )
        {
            executorService = Executors.newCachedThreadPool ( new SingleActionThreadFactory () );
        }
    }

    /**
     * Sets up the countdown
     */
    private void initCountDownLatch ()
    {
        this.latch = new CountDownLatch ( this.targets.length );   
    }
    
    /**
     * @param taskCompletionListener The individual task completion listener
     */
    private void submitTaskToDevices ( ActionListener taskCompletionListener )
    {
        for ( int i = 0 ; i < targets.length ; i ++ )
        {
            IndividualAction task = this.getTask ( i , taskCompletionListener );
            
            try
            {
                task.setTimeout ( this.individualTimeout );
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
            
            executorService.submit ( task );
        }   
        
        this.waitForTaskCollectiveCompletion ();
    }
    
    /**
     * Waits for the countdown to reach 0
     */
    private void waitForTaskCollectiveCompletion ()
    {
        try 
        {
            this.latch.await (); //Waits as long as it takes to count [proxies.length] calls to countDown() on latch 
        } 
        catch (InterruptedException e1) 
        {
            e1.printStackTrace(); //Shouldn't happen since the only operations on latch are calls to countDown()  
        }   
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.ICommonActionGroup#executeGroupAction()
     */
    public synchronized boolean execute ()
    {
        this.initCountDownLatch ();
        this.taskCompletionListener = this.getTaskCompletionListener ();
        this.submitTaskToDevices ( this.taskCompletionListener );
        
        return this.taskCompletionListener.hasBeenNotifiedOfFailedActions ();
    }
    
    public synchronized void setIndividualTimeout ( int _individualTimeout )
    {
        this.individualTimeout = _individualTimeout;
    }

    /**
     * @return The individual task completion listener for this group action
     */
    protected abstract ActionListener getTaskCompletionListener ();
    
    /**
     * Builds the individual task defined by the specified target and completion listener 
     * @param targetIndex The device's position in targets
     * @param listener The individual task completion listener
     * @return The corresponding individual task
     */
    protected abstract IndividualAction getTask ( int targetIndex , ActionListener listener );
    
    protected ActionListener getBasicListener ()
    {
        return new BasicActionListener ( this.latch );    
    }
}
