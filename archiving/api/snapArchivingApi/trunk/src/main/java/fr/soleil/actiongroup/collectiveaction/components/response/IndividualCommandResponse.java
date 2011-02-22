/*	Synchrotron Soleil 
 *  
 *   File          :  IIndividualCommandResponse.java
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
 *   Log: IIndividualCommandResponse.java,v 
 *
 */
 /*
 * Created on 1 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.response;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;

public interface IndividualCommandResponse extends IndividualResponse 
{
    public DeviceDataWrapper get_data (); 
    
    public void setData ( DeviceDataWrapper data );
}
