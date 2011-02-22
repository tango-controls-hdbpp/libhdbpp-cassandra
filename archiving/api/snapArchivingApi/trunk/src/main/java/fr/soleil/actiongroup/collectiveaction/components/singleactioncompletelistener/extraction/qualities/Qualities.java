/*	Synchrotron Soleil 
 *  
 *   File          :  IAttributeQualityReader.java
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
 *   Log: IAttributeQualityReader.java,v 
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

/**
 * Gets the qualities of the read attributes
 * @author CLAISSE 
 */
public interface Qualities 
{
    /**
     * @return A Map which keys are the read attributes names, and which values are the read attributes values
     */
    public Map<String, AttrQualityWrapper> getQualities ();
    
    /**
     * @param attributeName A read attribute's name
     * @return The specified read attribute's quality
     */
    public AttrQualityWrapper getQuality ( String attributeName );
}
