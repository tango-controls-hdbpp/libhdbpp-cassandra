/*	Synchrotron Soleil 
 *  
 *   File          :  CompletelyBufferedAttrDBManagerImpl.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  10 mai 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: CompletelyBufferedAttrDBManagerImpl.java,v 
 *
 */
 /*
 * Created on 10 mai 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.db.attributes;

import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;

public class BufferedFormatsAndDomainsAttributeManager extends BufferedFormatsAttributeManager 
{
    private Hashtable hdbBuffer;
    private Hashtable tdbBuffer;
    private static final int MAX_BUFFER_SIZE_DB = 20; 
    private static final String NULL_CRITERION = "No Criterion"; 
   // private static final String NULL_CONDITION = "No Condition"; 
    
    public BufferedFormatsAndDomainsAttributeManager() 
    {
        super();

        this.hdbBuffer = new Hashtable ( MAX_BUFFER_SIZE_DB );
        this.tdbBuffer = new Hashtable ( MAX_BUFFER_SIZE_DB );
    }
    

    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#loadDomains(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions, boolean, boolean)
     */
    public Vector loadDomains ( Criterions searchCriterions, boolean _historic, boolean forceReload )
    {
        Vector bufferisedResult;
        Hashtable domainsBuffer = _historic ? this.hdbBuffer : this.tdbBuffer;
        
        if ( searchCriterions == null )
        {
            return this.loadDomains(_historic, forceReload);
        }
        else
        {
            Hashtable conditionsHT = searchCriterions.getConditionsHT ();
            if ( conditionsHT == null )
            {
                return this.loadDomains(_historic, forceReload);
            }
            
            bufferisedResult = (Vector) domainsBuffer.get ( conditionsHT );
            if ( bufferisedResult == null || forceReload )
            {
                Vector newResult = super.loadDomains ( searchCriterions , _historic, forceReload );            
                this.bufferResult ( domainsBuffer , MAX_BUFFER_SIZE_DB , searchCriterions , newResult );
                return newResult;   
            }
        }
        
        return bufferisedResult;
    }
    
    public Vector loadDomains ( boolean _historic, boolean forceReload )
    {
        Hashtable domainsBuffer = _historic ? this.hdbBuffer : this.tdbBuffer;
        Vector bufferisedResult = (Vector) domainsBuffer.get ( NULL_CRITERION );
        
        if ( bufferisedResult == null || forceReload )
        {
            Vector newResult = super.loadDomains ( _historic, forceReload );            
            this.bufferResult ( domainsBuffer , MAX_BUFFER_SIZE_DB , NULL_CRITERION , newResult );
            return newResult;   
        }
        else
        {
            return bufferisedResult;    
        }
    }
}
