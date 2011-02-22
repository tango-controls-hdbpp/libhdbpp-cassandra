/*	Synchrotron Soleil 
 *  
 *   File          :  AttributeQualityRegister.java
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
 *   Log: AttributeQualityRegister.java,v 
 *
 */
 /*
 * Created on 12 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities;

import java.util.Hashtable;
import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttrQualityWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;

/**
 * Manages the qualities of the read attributes.
 * Extracts the read attributes qualities and stores them into a Map.
 * If the quality extraction fails, the attribute will be considered invalid (its quality will be AttrQuality.ATTR_INVALID)
 * @author CLAISSE 
 */
public class AttributeQualityRegister implements IAttributeQualityRegister 
{
    /**
     * An attribute complete name-->quality mapping
     */
    protected Map<String, AttrQualityWrapper> qualities;
    
    /**
     * 
     */
    public AttributeQualityRegister ()
    {
        this.qualities = new Hashtable<String, AttrQualityWrapper> ();    
    }
    
    public void registerAttributeQuality(String deviceName, DeviceAttributeWrapper attribute)
    {
        AttrQualityWrapper quality = null;
        String attributeName = null;
        
        try 
        {
            attributeName = attribute.getName ();
            quality = new AttrQualityWrapper ( attribute.getQuality () );
        } 
        catch (Throwable e) 
        {
            quality = new AttrQualityWrapper ( AttrQualityWrapper.ATTR_INVALID );
        }
            
        this.setQuality ( deviceName + "/" + attributeName , quality );
    }
    
    /**
     * Stores the quality of the specified attribute
     * @param attributeName The attribute's complete name
     * @param quality The attribute's quality
     */
    private void setQuality ( String attributeName , AttrQualityWrapper quality )
    {
        synchronized ( this )
        {
            this.qualities.put ( attributeName , quality );
        }
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.attributes.read.quality.IAttributeQualityReader#getQualities()
     */
    public synchronized Map<String, AttrQualityWrapper> getQualities() 
    {
        return qualities;
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.attributes.read.quality.IAttributeQualityReader#getQuality(java.lang.String)
     */
    public synchronized AttrQualityWrapper getQuality(String attributeName) 
    {
        return qualities.get ( attributeName );
    }
}
