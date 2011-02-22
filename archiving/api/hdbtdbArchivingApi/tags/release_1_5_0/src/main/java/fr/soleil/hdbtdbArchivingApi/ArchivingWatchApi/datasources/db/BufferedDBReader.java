/*	Synchrotron Soleil 
 *  
 *   File          :  BufferedDBReader.java
 *  
 *   Project       :  javaapi
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  23 oct. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: BufferedDBReader.java,v 
 *
 */
 /*
 * Created on 23 oct. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db;

import java.util.Hashtable;

import fr.esrf.Tango.DevFailed;

public abstract class BufferedDBReader extends DBReaderAdapter 
{
//  Store the writable criteria for each attribut : AttrWriteType._READ_WRITE or AttrWriteType._READ or AttrWriteType._WRITE
    private Hashtable<String,Integer> m_nameToWritableCriterionHash;
//  Store the format of each attribut : AttrDataFormat._SCALAR or AttrDataFormat._SPECTRUM ot AttrDataFormat._IMAGE
    private Hashtable<String,Integer> m_nameToFormatCriterionHash;
    
    public BufferedDBReader() 
    {
        super();
        this.m_nameToWritableCriterionHash = new Hashtable <String,Integer>();
        this.m_nameToFormatCriterionHash = new Hashtable <String,Integer>();
    }
    
    public int getAttWritableCriterion(String completeName ) throws DevFailed
    {
    	Integer writable = (Integer) this.m_nameToWritableCriterionHash.get ( completeName );   	
        if ( writable == null )
        {
        	writable = new Integer ( super.getAttWritableCriterion ( completeName ) );
            this.m_nameToWritableCriterionHash.put ( completeName , writable );
        }
        
        return writable.intValue();    
    }
    
    public int getAttFormatCriterion(String completeName ) throws DevFailed
    {
    	Integer writable = (Integer) this.m_nameToFormatCriterionHash.get ( completeName );   	
        if ( writable == null )
        {
        	writable = new Integer ( super.getAttFormatCriterion ( completeName ) );
            this.m_nameToFormatCriterionHash.put ( completeName , writable );
        }
        
        return writable.intValue();    
    }
}
