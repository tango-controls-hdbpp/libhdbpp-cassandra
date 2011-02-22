/*	Synchrotron Soleil 
 *  
 *   File          :  Im1Val.java
 *  
 *   Project       :  javaapi
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  7 mars 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: Im1Val.java,v 
 *
 */
 /*
 * Created on 7 mars 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.snapArchivingApi.SnapshotingApi.persistence.spring.dto;

import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance.AnyAttribute;
import fr.soleil.snapArchivingApi.SnapshotingApi.persistence.context.SnapshotPersistenceContext;

public class Im1Val extends ImVal
{
    private String value;
    
    public Im1Val ()
    {
       
    }

    public Im1Val(AnyAttribute attribute, SnapshotPersistenceContext context) 
    {
        super ( attribute , context ) ;
    }
    
    /**
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
