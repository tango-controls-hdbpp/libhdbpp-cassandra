/*	Synchrotron Soleil 
 *  
 *   File          :  IndividualAttributesResponse.java
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
 *   Log: IndividualAttributesResponse.java,v 
 *
 */
 /*
 * Created on 1 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.response;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;

public class IndividualAttributesResponseImpl extends IndividualResponseImpl implements IndividualAttributesResponse 
{
    private DeviceAttributeWrapper[] data;
                    
    public IndividualAttributesResponseImpl(String _deviceName) 
    {
        super ( _deviceName );
    }

    public DeviceAttributeWrapper[] get_data() 
    {
        return this.data;
    }

    public void setAttributes(DeviceAttributeWrapper[] attributes) 
    {
        this.data = attributes;
    }    
}
