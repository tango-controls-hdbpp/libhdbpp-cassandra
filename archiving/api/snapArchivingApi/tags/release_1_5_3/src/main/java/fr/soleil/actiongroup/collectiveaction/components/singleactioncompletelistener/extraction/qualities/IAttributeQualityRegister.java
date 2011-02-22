/*	Synchrotron Soleil 
 *  
 *   File          :  IAttributeQualityRegister.java
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
 *   Log: IAttributeQualityRegister.java,v 
 *
 */
 /*
 * Created on 12 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;

/**
 * Manages the qualities of the read attributes.
 * @author CLAISSE 
 */
public interface IAttributeQualityRegister extends Qualities
{
    /**
     * Stores the quality of a read attribute
     * @param deviceName The device the attribute was read from
     * @param attribute The read attribute
     */
    public void registerAttributeQuality ( String deviceName, DeviceAttributeWrapper attribute );
}
