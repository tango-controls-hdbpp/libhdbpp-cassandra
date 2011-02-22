/*	Synchrotron Soleil 
 *  
 *   File          :  TargetFactory.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  9 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: TargetFactory.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target;

import fr.esrf.TangoApi.DeviceProxy;

public class TargetFactory 
{
    public static Target getTarget ( DeviceProxy _proxy )
    {
        return new TargetImpl ( _proxy );
    }
}
