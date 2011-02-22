/*	Synchrotron Soleil 
 *  
 *   File          :  AttributeInfoWrapper.java
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
 *   Log: AttributeInfoWrapper.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.TangoApi.AttributeInfo;

public class AttributeInfoWrapper
{
    private AttributeInfo attributeInfo;

    /**
     * @param attributeInfo
     */
    public AttributeInfoWrapper(AttributeInfo attributeInfo) 
    {
        this.attributeInfo = attributeInfo;
    }

    public void setFormat(String value) 
    {
        this.attributeInfo.format = value;    
    }

    public void setLabel(String value) 
    {
        this.attributeInfo.label = value;    
    }

    public void setMaxAlarm(String value) 
    {
        this.attributeInfo.max_alarm = value;    
    }

    public void setMaxValue(String value) 
    {
        this.attributeInfo.max_value = value;    
    }

    public void setMinAlarm(String value) 
    {
        this.attributeInfo.min_alarm = value;    
    }

    public void setMinValue(String value) 
    {
        this.attributeInfo.min_value = value;    
    }

    public void setUnit(String value) 
    {
        this.attributeInfo.unit = value;    
    }

    /**
     * @return the attributeInfo
     */
    public AttributeInfo getAttributeInfo() {
        return this.attributeInfo;
    }

}
