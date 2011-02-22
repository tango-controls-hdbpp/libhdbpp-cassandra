package fr.soleil.bensikin.datasources.tango;

import java.util.HashMap;
import java.util.Map;

import fr.soleil.bensikin.actions.listeners.BasicPreBufferingEventListener;
import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class BufferedTangoAlternateSelectionManager extends FilteredTangoAlternateSelectionManager
{
    private Domain[] domainsBuffer;
    private Map domainsRegExpToDomainsBuffer;
    private Map domainToDeviceClassesBuffer;
    private Map domainAndDeviceClassToAttributeBuffer;
    //private Map deviceClassAndBrotherToAttributeBuffer;
    
    private static final int MAX_BUFFER_SIZE = 10;
    
    private IPreBufferingEventListener preBufferingEventListener;
    
    BufferedTangoAlternateSelectionManager ( ITangoAlternateSelectionManager _tasm ) 
    {
        super ( _tasm );
        
        this.domainsBuffer = new Domain [0];
        this.domainsRegExpToDomainsBuffer = new HashMap ();
        this.domainToDeviceClassesBuffer = new HashMap ();
        this.domainAndDeviceClassToAttributeBuffer = new HashMap ();
        //this.deviceClassAndBrotherToAttributeBuffer = new HashMap ();
        
        //this.register ( new DoNothingPreBufferingEventListener () );
        this.register ( new BasicPreBufferingEventListener () );
        
        this.doPreBuffering ();
    }
    
    public Domain[] loadDomains(String domainsRegExp) throws SnapshotingException 
    {
        synchronized ( this.domainsRegExpToDomainsBuffer )//Other threads have to wait for the completion of the buffering, so that the loading from database isn't done twice  
        {
            Domain[] bufferedDomains = (Domain[]) this.domainsRegExpToDomainsBuffer.get ( domainsRegExp );
            
            if ( bufferedDomains == null || bufferedDomains.length == 0 )
            {
                bufferedDomains = super.loadDomains(domainsRegExp);
                this.bufferResult ( domainsRegExpToDomainsBuffer , MAX_BUFFER_SIZE , domainsRegExp , bufferedDomains );
            }
            
            return bufferedDomains;
        }
    }

    public Domain[] loadDomains() throws SnapshotingException 
    {
        synchronized ( this.domainsBuffer )//Other threads have to wait for the completion of the buffering, so that the loading from database isn't done twice  
        {
            Domain[] domains = this.domainsBuffer;
            
            if ( domains == null || domains.length == 0 )
            {
                domains = super.loadDomains ();
                this.domainsBuffer = domains;
            }
            
            return domains;
        }
    }

    public BensikinDeviceClass[] loadDeviceClasses(Domain domain) throws SnapshotingException 
    {
        String key = domain.getName ();
        
        synchronized ( domainToDeviceClassesBuffer )//Other threads have to wait for the completion of the buffering, so that the loading from database isn't done twice  
        {
            BensikinDeviceClass[] deviceClasses = (BensikinDeviceClass[]) this.domainToDeviceClassesBuffer.get ( key );
            
            if ( deviceClasses == null )
            {
                //System.out.println ( "BufferedTangoAlternateSelectionManager/loadDeviceClasses(Domain="+domain+")/NOT BUFFERED" );
                deviceClasses = super.loadDeviceClasses ( domain );
                
                this.bufferResult ( domainToDeviceClassesBuffer , MAX_BUFFER_SIZE , key , deviceClasses ); 
            }
            else
            {
                //System.out.println ( "BufferedTangoAlternateSelectionManager/loadDeviceClasses(Domain="+domain+")/BUFFERED" );
            }
            
            return deviceClasses;
        }
    }

    public ContextAttribute[] loadACAttributes(Domain domain, BensikinDeviceClass deviceClass) throws SnapshotingException 
    {
        String key = domain.getName () + "__|__" + deviceClass.getName ();
        
        synchronized ( this.domainAndDeviceClassToAttributeBuffer )//Other threads have to wait for the completion of the buffering, so that the loading from database isn't done twice  
        {
            ContextAttribute[] attributes = (ContextAttribute[]) this.domainAndDeviceClassToAttributeBuffer.get ( key );
            
            if ( attributes == null )
            {
                //System.out.println ( "BufferedTangoAlternateSelectionManager/loadACAttributes Dom+dC/NOT BUFFERED" );
                attributes = super.loadACAttributes ( domain , deviceClass );
                this.bufferResult ( domainAndDeviceClassToAttributeBuffer , MAX_BUFFER_SIZE , key , attributes );
            }
            else
            {
                //System.out.println ( "BufferedTangoAlternateSelectionManager/loadACAttributes Dom+dC/BUFFERED" );
            }
            
            return attributes;
        }
    }

    /*
     *Removed : The attribute name isn't a sufficient buffering key  
     * public ArchivingConfigurationAttribute[] loadACAttributes(MamboDeviceClass deviceClass, ArchivingConfigurationAttribute brother) 
    {
        System.out.println ( "BufferedTangoAlternateSelectionManager/loadACAttributes dC+brother/deviceClass.getName ()|"+deviceClass.getName ()+"|brother.getCompleteName()|"+brother.getCompleteName() );
        String key = deviceClass.getName () + "__|__" + brother.getCompleteName();
        ArchivingConfigurationAttribute[] attributes = (ArchivingConfigurationAttribute[]) this.deviceClassAndBrotherToAttributeBuffer.get ( key );
        
        if ( attributes == null )
        {
            System.out.println ( "BufferedTangoAlternateSelectionManager/loadACAttributes dC+brother NOT BUFFERED" );
            attributes = super.loadACAttributes ( deviceClass , brother );
            this.bufferResult ( deviceClassAndBrotherToAttributeBuffer , MAX_BUFFER_SIZE , key , attributes );
        }
        else
        {
            System.out.println ( "BufferedTangoAlternateSelectionManager/loadACAttributes dC+brother BUFFERED" );
        }
        
        return attributes;
    }*/
    
    protected void bufferResult ( Map buffer , int maxSize , Object key , Object newValue ) 
    {
        if ( key == null || newValue == null )
        {
            return;
        }
        
        try
        {
            buffer.put ( key , newValue );
        }
        catch ( Throwable t ) //Catching possible OutOfMemoryError
        {
            t.printStackTrace ();
            
            ILogger logger = LoggerFactory.getCurrentImpl();
            logger.trace( ILogger.LEVEL_ERROR , t );
        }
    }

    protected void doPreBuffering ()
    {
        try 
        {
            Domain[] doms = this.loadDomains ();
            if ( doms == null )
            {
                return;
            }
            int totalSteps = doms.length;
            
            for ( int i = 0 ; i < totalSteps ; i ++ )
            {
                this.loadDeviceClasses(doms [ i ]);
                this.notifyStepDone ( i + 1 , totalSteps );
            }
        } 
        catch (SnapshotingException e) 
        {
            e.printStackTrace();
        }
        finally
        {
            this.notifyAllDone ();
        }
    }
    
    public void register(IPreBufferingEventListener source) 
    {
        this.setPreBufferingEventListener ( source );
    }

    private void setPreBufferingEventListener(IPreBufferingEventListener source) 
    {
        this.preBufferingEventListener = source;
    }

    public void unregister(IPreBufferingEventListener source) 
    {
        this.setPreBufferingEventListener ( null );
    }

    public void notifyAllDone() 
    {
        this.preBufferingEventListener.allDone ();
    }

    public void notifyStepDone(int step, int totalSteps) 
    {
        this.preBufferingEventListener.stepDone ( step , totalSteps );
    }
}
