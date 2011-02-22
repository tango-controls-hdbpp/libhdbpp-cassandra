/*	Synchrotron Soleil 
 *  
 *   File          :  MultiThreadedPreBufferingEventListener.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  22 nov. 06 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: MultiThreadedPreBufferingEventListener.java,v 
 *
 */
 /*
 * Created on 22 nov. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.actions.listeners;

import fr.soleil.mambo.containers.archiving.dialogs.AttributesTabAlternate;

public class MultiThreadedPreBufferingEventListener extends BasicPreBufferingEventListener 
{
    private int currentSteps = 0;
    
    public synchronized void stepDone(int step, int totalSteps) 
    {
        currentSteps++;
        
        System.out.println ( "MultiThreadedPreBufferingEventListener/stepDone/step/"+currentSteps+"/totalSteps/"+totalSteps );
        AttributesTabAlternate.setBufferingStatus ( currentSteps , totalSteps );
    }

    public String getName() 
    {
        return "Mambo MultiThreadedPreBufferingEventListener";
    }
}
