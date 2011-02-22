/*	Synchrotron Soleil 
 *  
 *   File          :  DeviceAttributesWrapper.java
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
 *   Log: DeviceAttributesWrapper.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.Tango.AttrQuality;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.TangoApi.DeviceAttribute;

public class DeviceAttributeWrapper 
{
    private DeviceAttribute attribute;

    /**
     * @param attributes
     */
    public DeviceAttributeWrapper(DeviceAttribute attributes) {
        this.attribute = attributes;
    }

    /**
     * @return the attributes
     */
    public DeviceAttribute getAttribute()
    {
        return this.attribute;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttribute(DeviceAttribute attributes) {
        this.attribute = attributes;
    }

    public String getName() throws DevFailed 
    {
        return this.attribute.getName();
    }

    public int getDimX() throws DevFailed 
    {
        return this.attribute.getDimX ();
    }

    public int getDimY() throws DevFailed 
    {
        return this.attribute.getDimY ();
    }

    public int getType() throws DevFailed 
    {
        return this.attribute.getType ();
    }

    public double[] extractDoubleArray() throws DevFailed 
    {
        return this.attribute.extractDoubleArray ();
    }

    public short[] extractShortArray() throws DevFailed 
    {
        return this.attribute.extractShortArray ();
    }

    public int[] extractLongArray() throws DevFailed 
    {
        return this.attribute.extractLongArray ();
    }

    public float[] extractFloatArray() throws DevFailed 
    {
        return this.attribute.extractFloatArray ();
    }

    public boolean[] extractBooleanArray() throws DevFailed 
    {
        return this.attribute.extractBooleanArray ();
    }

    public String[] extractStringArray() throws DevFailed 
    {
        return this.attribute.extractStringArray ();
    }

    public short extractShort() throws DevFailed 
    {
        return this.attribute.extractShort ();
    }

    public AttrQuality getQuality() throws DevFailed 
    {
        return this.attribute.getQuality();
    } 
    
    public DevState extractState () throws DevFailed 
    {
        return attribute.extractState();   
    }
}
