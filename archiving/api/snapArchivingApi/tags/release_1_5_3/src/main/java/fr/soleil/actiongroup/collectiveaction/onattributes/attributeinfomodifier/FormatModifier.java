/*	Synchrotron Soleil 
 *  
 *   File          :  AttributeInfoFormatModifier.java
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
 *   Log: AttributeInfoFormatModifier.java,v 
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
 * Modifies the "Format" attribute info property
 * @author CLAISSE 
 */
class FormatModifier extends AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * @param newFormat
     */
    FormatModifier ( String newFormat )
    {
        super ( newFormat );
    }
    
    public AttributeInfoWrapper modify ( AttributeInfoWrapper in ) 
    {
        in.setFormat ( super.newProperty );
        return in;
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier#getAttributeInfoPropertyName()
     */
    public String getAttributeInfoPropertyName() 
    {
        return "FORMAT";
    }
}
