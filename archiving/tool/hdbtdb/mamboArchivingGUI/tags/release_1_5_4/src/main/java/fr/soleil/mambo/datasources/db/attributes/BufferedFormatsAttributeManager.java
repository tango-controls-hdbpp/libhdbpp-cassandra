/*	Synchrotron Soleil 
 *  
 *   File          :  BufferedAttrDBManagerImpl.java
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
 *   Log: BufferedAttrDBManagerImpl.java,v 
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
import java.util.Map;

import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;


public class BufferedFormatsAttributeManager extends BasicAttributeManager 
{
    private Map isScalarBuffer;
    private Map isSpectrumBuffer;
    private Map isImageBuffer;
    
    private static final int MAX_BUFFER_SIZE_ATTRIBUTES_TYPES = 10000;
    
    private static final String SEPARATOR = "|";
    
    public BufferedFormatsAttributeManager() 
    {
        super();
        
        this.isScalarBuffer = new Hashtable ( MAX_BUFFER_SIZE_ATTRIBUTES_TYPES );
        this.isSpectrumBuffer = new Hashtable ( MAX_BUFFER_SIZE_ATTRIBUTES_TYPES );
        this.isImageBuffer = new Hashtable ( MAX_BUFFER_SIZE_ATTRIBUTES_TYPES );
    }

    public boolean isScalar ( String completeName , boolean _historic )
    {
        String key = completeName + SEPARATOR + _historic;
        Boolean isScalar = (Boolean) this.isScalarBuffer.get ( key );
        
        if ( isScalar == null )
        {
            boolean newResult = super.isScalar ( completeName , _historic );
            this.bufferResult ( isScalarBuffer , MAX_BUFFER_SIZE_ATTRIBUTES_TYPES , key , new Boolean ( newResult ) );
            return newResult;
        }
        else
        {
            return isScalar.booleanValue ();
        }
    }
    
    public boolean isSpectrum ( String completeName , boolean _historic )
    {
        String key = completeName + SEPARATOR + _historic;
        Boolean isSpectrum = (Boolean) this.isSpectrumBuffer.get ( key );
        
        if ( isSpectrum == null )
        {
            boolean newResult = super.isSpectrum ( completeName , _historic );
            this.bufferResult ( isSpectrumBuffer , MAX_BUFFER_SIZE_ATTRIBUTES_TYPES , key , new Boolean ( newResult ) );
            return newResult;
        }
        else
        {
            return isSpectrum.booleanValue ();
        }
    }
    
    public boolean isImage ( String completeName , boolean _historic )
    {
        String key = completeName + SEPARATOR + _historic;
        Boolean isImage = (Boolean) this.isImageBuffer.get ( key );
        
        if ( isImage == null )
        {
            boolean newResult = super.isImage ( completeName , _historic );
            this.bufferResult ( isImageBuffer , MAX_BUFFER_SIZE_ATTRIBUTES_TYPES , key , new Boolean ( newResult ) );
            return newResult;
        }
        else
        {
            return isImage.booleanValue ();
        }
    }

    protected void bufferResult ( Map buffer , int maxSize , Object key , Object newValue ) 
    {
        if ( key == null || newValue == null )
        {
            return;
        }
        
        /*int currentBufferSize = buffer.size ();
        if ( currentBufferSize >= maxSize )
        {
            buffer = new Hashtable ( maxSize );
        }*/
        
        try
        {
            buffer.put ( key , newValue );
        }
        catch ( Throwable t ) //Catching possible OutOfMemoryError
        {
            ILogger logger = LoggerFactory.getCurrentImpl();
            logger.trace( ILogger.LEVEL_ERROR , t );
        }
    }
}
