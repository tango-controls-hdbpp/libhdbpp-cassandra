/*	Synchrotron Soleil 
 *  
 *   File          :  AttrQualityWrapper.java
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
 *   Log: AttrQualityWrapper.java,v 
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

public class AttrQualityWrapper 
{
    public static final AttrQuality ATTR_INVALID = AttrQuality.ATTR_INVALID;
    
    private AttrQuality attrQuality;

    public AttrQualityWrapper(AttrQuality attrQuality) 
    {
        this.attrQuality = attrQuality;
    }
    
    /**
     * @return the attrQuality
     */
    public AttrQuality getAttrQuality() {
        return this.attrQuality;
    }

    /**
     * @param attrQuality the attrQuality to set
     */
    public void setAttrQuality(AttrQuality attrQuality) {
        this.attrQuality = attrQuality;
    }
}
