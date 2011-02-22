/*	Synchrotron Soleil 
 *  
 *   File          :  GroupResponse.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  1 f�vr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: GroupResponse.java,v 
 *
 */
 /*
 * Created on 1 f�vr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.response;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;


public class CollectiveResponseImpl implements CollectiveResponse 
{
    private Map <String,IndividualResponse> individualResponses;
    private boolean hasFailed;
    private boolean isFailureDueToATimeout;
    
    public CollectiveResponseImpl ()
    {
        this.individualResponses = new Hashtable <String,IndividualResponse> ();
    }

    public synchronized Collection <IndividualResponse> getIndividualResponses ()
    {
        return individualResponses.values ();
    }
    
    public synchronized IndividualResponse getIndividualResponse ( String deviceName )
    {
        return individualResponses.get ( deviceName );
    }

    public synchronized void addIndividualResponse(IndividualResponse individualResponse) 
    {
        individualResponses.put ( individualResponse.dev_name () , individualResponse );
        
        if ( individualResponse.has_failed () )
        {
            this.hasFailed = true;
            
            if ( individualResponse.has_timeout () )
            {
                this.isFailureDueToATimeout = true;
            }
        }
    }

    public synchronized boolean hasFailed() 
    {
        return hasFailed;
    }

    public synchronized boolean isFailureDueToATimeout() 
    {
        return isFailureDueToATimeout;
    }
}
