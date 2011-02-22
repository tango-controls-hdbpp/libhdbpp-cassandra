/*	Synchrotron Soleil 
 *  
 *   File          :  IGroupResponsesList.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  1 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IGroupResponsesList.java,v 
 *
 */
 /*
 * Created on 1 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.response;

import java.util.Collection;


public interface CollectiveResponse 
{
    public void addIndividualResponse ( IndividualResponse individualResponse );
    
    public Collection <IndividualResponse> getIndividualResponses ();
    
    public IndividualResponse getIndividualResponse ( String deviceName );
    
    public boolean hasFailed ();
    
    public boolean isFailureDueToATimeout ();
}
