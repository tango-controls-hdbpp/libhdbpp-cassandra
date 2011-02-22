/*	Synchrotron Soleil 
 *  
 *   File          :  ActionResult.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  29 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ActionResult.java,v 
 *
 */
 /*
 * Created on 29 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

public class ActionResult 
{
    private long actionTime;
    private DeviceAttributeWrapper[] attributesValue;
    
    private DeviceDataWrapper commandValue;
    
    private String attributePropertyName;
    private String attributePropertyValue;
    
    private String devicePropertyName;
    private DbDatumWrapper devicePropertyValue;
    
    private double newValue;
    
    /**
     * @param actionTime
     * @param attributesValue
     */
    public ActionResult ( DeviceAttributeWrapper[] attributesValue , long actionTime ) 
    {
        super();
        this.actionTime = actionTime;
        this.attributesValue = attributesValue;
    }

    /**
     * @param commandValue
     */
    public ActionResult(DeviceDataWrapper commandValue) 
    {
        this.commandValue = commandValue;
    }

    public ActionResult(String propertyName , String propertyValue ) 
    {
        this.attributePropertyName = propertyName;
        this.attributePropertyValue = propertyValue;
    }

    public ActionResult(String nextPropertyName, DbDatumWrapper nextPropertyValue) 
    {
        this.devicePropertyName = nextPropertyName;
        this.devicePropertyValue = nextPropertyValue;
    }

    /**
     * @return the actionTime
     */
    public long getActionTime() {
        return this.actionTime;
    }

    /**
     * @return the attributePropertyName
     */
    public String getAttributePropertyName() {
        return this.attributePropertyName;
    }

    /**
     * @return the attributePropertyValue
     */
    public String getAttributePropertyValue() {
        return this.attributePropertyValue;
    }

    /**
     * @return the attributesValue
     */
    public DeviceAttributeWrapper[] getAttributesValue() {
        return this.attributesValue;
    }

    /**
     * @return the commandValue
     */
    public DeviceDataWrapper getCommandValue() {
        return this.commandValue;
    }

    /**
     * @return the devicePropertyName
     */
    public String getDevicePropertyName() {
        return this.devicePropertyName;
    }

    /**
     * @return the devicePropertyValue
     */
    public DbDatumWrapper getDevicePropertyValue() {
        return this.devicePropertyValue;
    }

    /**
     * @return the newValue
     */
    public double getNewValue() {
        return this.newValue;
    }

    public void setNewValue(Double newValue) 
    {
        this.newValue = newValue;
    }    
}
