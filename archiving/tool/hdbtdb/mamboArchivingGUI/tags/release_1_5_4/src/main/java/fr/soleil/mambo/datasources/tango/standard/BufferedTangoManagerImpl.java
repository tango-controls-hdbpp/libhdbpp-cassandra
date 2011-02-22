/*	Synchrotron Soleil 
 *  
 *   File          :  BufferedTangoManagerImpl.java
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
 *   Log: BufferedTangoManagerImpl.java,v 
 *
 */
 /*
 * Created on 10 mai 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.tango.standard;

import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;

public class BufferedTangoManagerImpl extends BasicTangoManagerImpl 
{
    private Hashtable buffer;
    private static final int MAX_BUFFER_SIZE = 20; 
    
    public BufferedTangoManagerImpl() 
    {
        super();
        this.buffer = new Hashtable ( MAX_BUFFER_SIZE );
        
        //System.out.println ( "CLA/new BufferedTangoManagerImpl!!!!!!!!!!!!!" );
    }

    public Vector loadDomains ( Criterions searchCriterions, boolean forceReload )
    {
        if ( searchCriterions == null )
        {
            Vector newResult = super.loadDomains ( searchCriterions, forceReload );
            return newResult;   
        }
        
        Hashtable conditionsHT = searchCriterions.getConditionsHT ();
        if ( conditionsHT == null )
        {
            Vector newResult = super.loadDomains ( searchCriterions, forceReload );
            return newResult;   
        }
        
        Vector bufferisedResult = (Vector) this.buffer.get ( conditionsHT );
        if ( bufferisedResult == null || forceReload )
        {
            Vector newResult = super.loadDomains ( searchCriterions, forceReload );
            this.bufferResult ( searchCriterions , newResult );
            return newResult;   
        }
        
        return bufferisedResult;
    }

    private void bufferResult(Criterions searchCriterions, Vector newResult) 
    {
        if ( searchCriterions == null || newResult == null )
        {
            return;
        }
        
        int currentBufferSize = this.buffer.size ();
        if ( currentBufferSize >= MAX_BUFFER_SIZE )
        {
            this.buffer = new Hashtable ( MAX_BUFFER_SIZE );
        }
        
        try
        {
            this.buffer.put ( searchCriterions , newResult );
        }
        catch ( Throwable t ) //Catching possible OutOfMemoryError
        {
            ILogger logger = LoggerFactory.getCurrentImpl();
            logger.trace( ILogger.LEVEL_ERROR , t );
        }
    }
}
