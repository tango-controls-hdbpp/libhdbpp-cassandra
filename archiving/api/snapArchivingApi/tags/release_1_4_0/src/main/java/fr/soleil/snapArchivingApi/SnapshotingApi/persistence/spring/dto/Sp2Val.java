/*	Synchrotron Soleil 
 *  
 *   File          :  Sp2Val.java
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
 *   Log: Sp2Val.java,v 
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

public class Sp2Val extends SpVal
{
    private String readValue;
    private String writeValue;
    
    public Sp2Val ()
    {
       
    }
    
    public Sp2Val(AnyAttribute attribute, SnapshotPersistenceContext context) 
    {
        super ( attribute , context ) ;
        this.readValue = attribute.getConvertedStringAggreggatedValues ( true);
        this.writeValue = attribute.getConvertedStringAggreggatedValues ( false);
    }

    /**
     * @return the readValue
     */
    public String getReadValue() {
        return this.readValue;
    }

    /**
     * @param readValue the readValue to set
     */
    public void setReadValue(String readValue) {
        this.readValue = readValue;
    }

    /**
     * @return the writeValue
     */
    public String getWriteValue() {
        return this.writeValue;
    }

    /**
     * @param writeValue the writeValue to set
     */
    public void setWriteValue(String writeValue) {
        this.writeValue = writeValue;
    }
}
