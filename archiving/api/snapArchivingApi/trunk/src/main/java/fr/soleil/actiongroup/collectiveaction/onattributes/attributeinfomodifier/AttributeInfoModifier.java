/*	Synchrotron Soleil 
 *  
 *   File          :  AttributeInfoModifier.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  15 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: AttributeInfoModifier.java,v 
 *
 */
 /*
 * Created on 15 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;

/**
 * Modifies an attribute's info properties
 * @author CLAISSE 
 */
public interface AttributeInfoModifier 
{
    /**
     * @param in The AttributeInfo to modify
     * @return The modified AttributeInfo
     */
    public AttributeInfoWrapper modify ( AttributeInfoWrapper in );
    /**
     * @return The name of the AttributeInfo property to modify
     */
    public String getAttributeInfoPropertyName ();
    /**
     * @return The new value of the AttributeInfo property to modify
     */
    public String getAttributeInfoPropertyValue ();
}
