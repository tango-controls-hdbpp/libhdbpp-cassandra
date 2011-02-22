package fr.soleil.mambo.datasources.tango.alternate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.comparators.MamboAlternateEntitiesComparator;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;

public class OrderedTangoAlternateSelectionManager extends FilteredTangoAlternateSelectionManager implements ITangoAlternateSelectionManager  
{
    private MamboAlternateEntitiesComparator comparator;
    
    OrderedTangoAlternateSelectionManager ( ITangoAlternateSelectionManager _tasm ) 
    {
        super ( _tasm );
        
        this.comparator = new MamboAlternateEntitiesComparator ();
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDeviceClasses(mambo.data.attributes.Domain)
     */
    public MamboDeviceClass[] loadDeviceClasses ( Domain domain ) throws ArchivingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadDeviceClasses( Domain domain )" );
        MamboDeviceClass[] unorderedValues = super.loadDeviceClasses ( domain );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        MamboDeviceClass [] ret = (MamboDeviceClass[]) list.toArray ( unorderedValues );
        return ret;
    }
    
    public MamboDeviceClass[] loadDeviceClasses ( Domain [] domains ) throws ArchivingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadDeviceClasses( Domain [] domains )" );
        MamboDeviceClass[] unorderedValues = super.loadDeviceClasses ( domains );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        MamboDeviceClass [] ret = (MamboDeviceClass[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.DeviceClass)
     */
    public ArchivingConfigurationAttribute[] loadACAttributes ( Domain domain , MamboDeviceClass deviceClass ) throws ArchivingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadACAttributes( Domain domain , MamboDeviceClass deviceClass )" );
        ArchivingConfigurationAttribute[] unorderedValues = super.loadACAttributes ( domain , deviceClass );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        ArchivingConfigurationAttribute [] ret = (ArchivingConfigurationAttribute[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains()
     */
    public Domain[] loadDomains () throws ArchivingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadDomains" );
        Domain[] unorderedValues = super.loadDomains ();
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        Domain [] ret = (Domain[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.MamboDeviceClass, mambo.data.archiving.ArchivingConfigurationAttribute)
     */
    public ArchivingConfigurationAttribute[] loadACAttributes ( MamboDeviceClass deviceClass , ArchivingConfigurationAttribute brother )
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadACAttributes( MamboDeviceClass deviceClass , ArchivingConfigurationAttribute brother)" );
        ArchivingConfigurationAttribute[] unorderedValues = super.loadACAttributes ( deviceClass , brother );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        ArchivingConfigurationAttribute [] ret = (ArchivingConfigurationAttribute[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains(java.lang.String)
     */
    public Domain[] loadDomains(String domainsRegExp) throws ArchivingException 
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadDomains(String domainsRegExp)" );
        Domain[] unorderedValues = super.loadDomains ( domainsRegExp );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        Domain [] ret = (Domain[]) list.toArray ( unorderedValues );
        return ret;
    }
}
