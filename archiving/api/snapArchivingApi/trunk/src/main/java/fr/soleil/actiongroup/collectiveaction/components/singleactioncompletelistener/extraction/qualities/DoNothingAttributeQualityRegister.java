/*	Synchrotron Soleil 
 *  
 *   File          :  aa.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  12 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: aa.java,v 
 *
 */
 /*
 * Created on 12 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttrQualityWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;

/**
 * A do-nothing implementation 
 * @author CLAISSE 
 */
public class DoNothingAttributeQualityRegister implements IAttributeQualityRegister 
{
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.attributes.read.quality.IAttributeQualityReader#getQualities()
     */
    public Map<String, AttrQualityWrapper> getQualities() 
    {
        return null;
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.attributes.read.quality.IAttributeQualityReader#getQuality(java.lang.String)
     */
    public AttrQualityWrapper getQuality(String attributeName) 
    {
        return null;
    }

    public void registerAttributeQuality(String deviceName, DeviceAttributeWrapper attribute) 
    {
    
    }
}
