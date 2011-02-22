package fr.soleil.bensikin.datasources.tango;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.soleil.bensikin.components.BensikinAlternateEntitiesComparator;
import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class OrderedTangoAlternateSelectionManager extends FilteredTangoAlternateSelectionManager  
{
    private BensikinAlternateEntitiesComparator comparator;
    
    OrderedTangoAlternateSelectionManager ( ITangoAlternateSelectionManager _tasm ) 
    {
        super ( _tasm );
        
        this.comparator = new BensikinAlternateEntitiesComparator ();
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDeviceClasses(mambo.data.attributes.Domain)
     */
    public BensikinDeviceClass[] loadDeviceClasses ( Domain domain ) throws SnapshotingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadDeviceClasses( Domain domain )" );
        BensikinDeviceClass[] unorderedValues = super.loadDeviceClasses ( domain );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        BensikinDeviceClass [] ret = (BensikinDeviceClass[]) list.toArray ( unorderedValues );
        return ret;
    }
    
    public BensikinDeviceClass[] loadDeviceClasses ( Domain [] domains ) throws SnapshotingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadDeviceClasses( Domain [] domains )" );
        BensikinDeviceClass[] unorderedValues = super.loadDeviceClasses ( domains );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        BensikinDeviceClass [] ret = (BensikinDeviceClass[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.DeviceClass)
     */
    public ContextAttribute[] loadACAttributes ( Domain domain , BensikinDeviceClass deviceClass ) throws SnapshotingException
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadACAttributes( Domain domain , MamboDeviceClass deviceClass )" );
        ContextAttribute[] unorderedValues = super.loadACAttributes ( domain , deviceClass );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        ContextAttribute [] ret = (ContextAttribute[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains()
     */
    public Domain[] loadDomains () throws SnapshotingException
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
    public ContextAttribute[] loadACAttributes ( BensikinDeviceClass deviceClass , ContextAttribute brother )
    {
        //System.out.println ( "CLA/OrderedTangoAlternateSelectionManagerImpl/loadACAttributes( MamboDeviceClass deviceClass , ArchivingConfigurationAttribute brother)" );
        ContextAttribute[] unorderedValues = super.loadACAttributes ( deviceClass , brother );
        if ( unorderedValues == null || unorderedValues.length == 0 )
        {
            return null;
        }
        
        List list = Arrays.asList ( unorderedValues );
        Collections.sort ( list , this.comparator );
        
        ContextAttribute [] ret = (ContextAttribute[]) list.toArray ( unorderedValues );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains(java.lang.String)
     */
    public Domain[] loadDomains(String domainsRegExp) throws SnapshotingException 
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
