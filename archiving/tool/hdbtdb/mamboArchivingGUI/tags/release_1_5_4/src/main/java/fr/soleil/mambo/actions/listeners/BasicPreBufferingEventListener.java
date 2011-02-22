/*	Synchrotron Soleil 
 *  
 *   File          :  PreBufferingEventListener.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  20 sept. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: PreBufferingEventListener.java,v 
 *
 */
 /*
 * Created on 20 sept. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.actions.listeners;

import fr.soleil.mambo.containers.archiving.dialogs.AttributesTabAlternate;
import fr.soleil.mambo.datasources.tango.alternate.IPreBufferingEventListener;

public class BasicPreBufferingEventListener implements IPreBufferingEventListener 
{
    public BasicPreBufferingEventListener() 
    {
        super();
    }

    public void allDone() 
    {
        System.out.println ( "PreBufferingEventListener/allDone" );
        //ILogger logger = LoggerFactory.getCurrentImpl ();
        //String msg = "Buffering of table selection complete";
        //logger.trace ( ILogger.LEVEL_INFO , msg );
    }

    public void stepDone(int step, int totalSteps) 
    {
        System.out.println ( "PreBufferingEventListener/stepDone/step/"+step+"/totalSteps/"+totalSteps );
        AttributesTabAlternate.setBufferingStatus ( step , totalSteps );
    }

    public String getName() 
    {
        return "Mambo PreBufferingEventListener";
    }
}
