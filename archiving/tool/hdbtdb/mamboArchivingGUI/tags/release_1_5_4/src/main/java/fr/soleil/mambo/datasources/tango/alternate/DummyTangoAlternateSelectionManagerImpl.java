//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/tango/alternate/DummyTangoAlternateSelectionManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyTangoAlternateSelectionManagerImpl.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: DummyTangoAlternateSelectionManagerImpl.java,v $
// Revision 1.2  2006/11/23 10:04:31  ounsy
// refactroring
//
// Revision 1.1  2006/09/20 12:47:32  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.4  2006/09/14 11:29:29  ounsy
// complete buffering refactoring
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2006/02/24 12:22:26  ounsy
// added a loadDomains ( String domainsRegExp ) method
//
// Revision 1.1  2005/11/29 18:28:12  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.datasources.tango.alternate;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;
import fr.soleil.mambo.tools.GUIUtilities;

public class DummyTangoAlternateSelectionManagerImpl implements ITangoAlternateSelectionManager
{


    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDeviceClasses(mambo.data.attributes.Domain)
     */
    public MamboDeviceClass[] loadDeviceClasses ( Domain domain )
    {
        MamboDeviceClass[] ret = new MamboDeviceClass[ 5 ];
        MamboDeviceClass current;


        current = new MamboDeviceClass( "DeviceClass #1" );
        ret[ 0 ] = current;

        current = new MamboDeviceClass( "DeviceClass #2" );
        ret[ 1 ] = current;

        current = new MamboDeviceClass( "DeviceClass #3" );
        ret[ 2 ] = current;

        current = new MamboDeviceClass( "DeviceClass #4" );
        ret[ 3 ] = current;

        current = new MamboDeviceClass( "DeviceClass #5" );
        ret[ 4 ] = current;

        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.DeviceClass)
     */
    public ArchivingConfigurationAttribute[] loadACAttributes ( Domain domain , MamboDeviceClass deviceClass )
    {
        ArchivingConfigurationAttribute[] ret = new ArchivingConfigurationAttribute[ 3 ];
        ArchivingConfigurationAttribute current;
        String domainName = domain.getName();
        //String deviceClassName = deviceClass.getName();
        String[] attributeNames = {"short_scalar", "short_scalar_ro", "double_scalar"};
        String[] familyNames = {"tangotest", "tangotest", "tangotest"};

        for ( int i = 0 ; i < 3 ; i++ )
        {
            String deviceName = "2";
            current = new ArchivingConfigurationAttribute();
            current.setDomain( domainName );
            current.setFamily( familyNames[ i ] );
            current.setDevice( deviceName );
            current.setMember( deviceName );
            current.setName( attributeNames[ i ] );
            current.setCompleteName
                    ( domainName + GUIUtilities.TANGO_DELIM +
                      familyNames[ i ] + GUIUtilities.TANGO_DELIM +
                      deviceName + GUIUtilities.TANGO_DELIM +
                      attributeNames[ i ] );

            ret[ i ] = current;
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains()
     */
    public Domain[] loadDomains ()
    {
        Domain[] ret = new Domain[ 1 ];
        Domain current;

        current = new Domain( "tango" );
        ret[ 0 ] = current;

        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.MamboDeviceClass, mambo.data.archiving.ArchivingConfigurationAttribute)
     */
    public ArchivingConfigurationAttribute[] loadACAttributes ( MamboDeviceClass deviceClass , ArchivingConfigurationAttribute brother )
    {
        ArchivingConfigurationAttribute[] ret = new ArchivingConfigurationAttribute[ 3 ];
        ArchivingConfigurationAttribute current;
        //String domainName = domain.getName ();
        String domainName = "tango";
        //String deviceClassName = deviceClass.getName();
        String[] attributeNames = {"short_scalar", "short_scalar_ro", "double_scalar"};
        String[] familyNames = {"tangotest", "tangotest", "tangotest"};

        for ( int i = 0 ; i < 3 ; i++ )
        {
            String deviceName = "2";
            current = new ArchivingConfigurationAttribute();
            current.setDomain( domainName );
            current.setFamily( familyNames[ i ] );
            current.setDevice( deviceName );
            current.setMember( deviceName );
            current.setName( attributeNames[ i ] );
            current.setCompleteName
                    ( domainName + GUIUtilities.TANGO_DELIM +
                      familyNames[ i ] + GUIUtilities.TANGO_DELIM +
                      deviceName + GUIUtilities.TANGO_DELIM +
                      attributeNames[ i ] );

            ret[ i ] = current;
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains(java.lang.String)
     */
    public Domain[] loadDomains(String domainsRegExp) throws ArchivingException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDeviceClasses(mambo.data.attributes.Domain[])
     */
    public MamboDeviceClass[] loadDeviceClasses(Domain[] domains) throws ArchivingException {
        // TODO Auto-generated method stub
        return null;
    }

    public MamboDeviceClass[] loadDeviceClasses(String domainsRegExp) throws ArchivingException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCanceled(boolean b) {
        // TODO Auto-generated method stub
        
    }

}
