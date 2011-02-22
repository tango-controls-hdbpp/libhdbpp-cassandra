//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/tango/BasicTangoAlternateSelectionManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TangoAlternateSelectionManagerImpl.
//                      (Claisse Laurent) - oct. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.2 $
//
// $Log: BasicTangoAlternateSelectionManager.java,v $
// Revision 1.2  2007/11/09 16:20:34  pierrejoseph
// Suppression of unuseful interface inheritance
//
// Revision 1.1  2007/03/15 14:26:34  ounsy
// corrected the table mode add bug and added domains buffer
//
// Revision 1.8  2006/11/29 10:08:39  ounsy
// minor changes
//
// Revision 1.7  2006/11/23 10:04:31  ounsy
// refactroring
//
// Revision 1.6  2006/10/20 13:00:19  ounsy
// minor changes
//
// Revision 1.5  2006/10/19 12:50:35  ounsy
// minor changes
//
// Revision 1.4  2006/10/19 12:49:53  ounsy
// minor changes
//
// Revision 1.3  2006/10/17 14:33:34  ounsy
// minor changes
//
// Revision 1.2  2006/09/27 09:43:55  ounsy
// added a debug "delay" method
//
// Revision 1.1  2006/09/20 12:47:32  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.5  2006/09/19 12:32:37  ounsy
// minor changes
//
// Revision 1.4  2006/09/19 09:48:27  ounsy
// minor changes
//
// Revision 1.3  2006/09/19 07:36:36  ounsy
// minor changes
//
// Revision 1.2  2006/09/14 11:52:20  ounsy
// minor changes
//
// Revision 1.1  2006/09/14 11:29:29  ounsy
// complete buffering refactoring
//
// Revision 1.1  2006/05/17 09:24:20  ounsy
// renamed from TangoAlternateSelectionManagerImpl
//
// Revision 1.6  2006/05/16 12:03:02  ounsy
// minor changes
//
// Revision 1.5  2006/03/20 10:37:11  ounsy
// corrected the "non-unique device classes" bug that happened
// for joker domain* selections
//
// Revision 1.4  2006/03/08 10:11:02  ounsy
// small modification
//
// Revision 1.3  2006/03/07 14:37:59  ounsy
// bug correction in loadDeviceClasses (Domain [])
//
// Revision 1.2  2006/02/24 12:22:26  ounsy
// added a loadDomains ( String domainsRegExp ) method
//
// Revision 1.1  2005/11/29 18:28:12  chinkumo
// no message
//
//
// copyleft :   Synchrotron SOLEIL
//                  L'Orme des Merisiers
//                  Saint-Aubin - BP 48
//                  91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.datasources.tango;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.attributes.Member;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapTangoDbSearch;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class BasicTangoAlternateSelectionManager extends FilteredTangoAlternateSelectionManager
{
    public BasicTangoAlternateSelectionManager() 
    {
        super(null);
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDeviceClasses(mambo.data.attributes.Domain)
     */
    public BensikinDeviceClass[] loadDeviceClasses ( Domain domain ) throws SnapshotingException
    {
        if ( domain == null )
        {
            return null;
        }
        
        //long before = System.currentTimeMillis ();
        //System.out.println ( "BasicTangoAlternateSelectionManagerImpl/loadDeviceClasses/START/|domain|"+domain+"|" );
        //long before = System.currentTimeMillis ();
        //System.out.println ( "      BasicTangoAlternateSelectionManagerImpl|loadDeviceClasses|domain|"+domain+"|" );
        
        String domainName = domain.getName();
        
        Hashtable hashtable = SnapTangoDbSearch.getInstance().getClassAndDevices( domainName );
        if ( hashtable == null )
        {
            return null;
        }
        
        //long middle = System.currentTimeMillis ();
        //long delta1 = middle - before;
        
        BensikinDeviceClass[] ret = new BensikinDeviceClass[ hashtable.size() ];
        int j = 0;
        Enumeration enumeration = hashtable.keys();
        while ( enumeration.hasMoreElements() )
        {
            String nextClass = ( String ) enumeration.nextElement();
            //System.out.println ( "    BasicTangoAlternateSelectionManagerImpl/loadDeviceClasses/1|nextClass|"+nextClass+"|" );
            ret[ j ] = new BensikinDeviceClass( nextClass );

            Vector devices = ( Vector ) hashtable.get( nextClass );
            for ( int i = 0 ; i < devices.size() ; i++ )
            {
                String dev = ( String ) devices.elementAt( i );
                //System.out.println ( "          BasicTangoAlternateSelectionManagerImpl/loadDeviceClasses/2|dev|"+dev+"|" );
                ret[ j ].addDevice( new Member( dev ) );
            }

            j++;
        }

        /*long after = System.currentTimeMillis ();
        long delta2 = after - middle;*/
        
		//delay (domain);
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.DeviceClass)
     */
    public ContextAttribute[] loadACAttributes ( Domain domain , BensikinDeviceClass deviceClass ) throws SnapshotingException
    {
        //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/START" );
        if ( domain == null || deviceClass == null )
        {
            return null;
        }
        String domainName = domain.getName();
        String deviceClassName = deviceClass.getName();

        //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/domainName|"+domainName+"|deviceClassName|"+deviceClassName+"|" );
        Hashtable hashtable = SnapTangoDbSearch.getInstance().getClassAndDevices( domainName );

        Vector devicesList = ( Vector ) hashtable.get( deviceClassName );
        if ( devicesList == null )
        {
            return null;
        }
        //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/1" );
        String[] att = SnapTangoDbSearch.getInstance().getAttributesForClass( deviceClassName , devicesList );
        if ( att == null )
        {
            return null;
        }
        //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/2" );
        ContextAttribute[] ret = new ContextAttribute[ att.length ];
        for ( int i = 0 ; i < att.length ; i++ )
        {
            ret[ i ] = new ContextAttribute();
            //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttribute/att [ i ]|"+ att [ i ]+"|" );
            ret[ i ].setName( att[ i ] );
            ret[ i ].setDeviceClass ( deviceClass.getName () );
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains()
     */
    public Domain[] loadDomains () throws SnapshotingException
    {
    	Vector domains = SnapTangoDbSearch.getInstance().getDomains();
        if ( domains == null )
        {
            return null;
        }

        Domain[] ret = new Domain[ domains.size() ];
        Enumeration enumeration = domains.elements();
        int i = 0;
        while ( enumeration.hasMoreElements() )
        {
            String nextDomainName = ( String ) enumeration.nextElement();
            ret[ i ] = new Domain( nextDomainName );
            i++;
        }
        
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadACAttributes(mambo.data.attributes.Domain, mambo.data.attributes.MamboDeviceClass, mambo.data.archiving.ArchivingConfigurationAttribute)
     */
    public ContextAttribute[] loadACAttributes ( BensikinDeviceClass deviceClass , ContextAttribute brother )
    {
        //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/START" );
        if ( brother == null || deviceClass == null )
        {
            return null;
        }
        //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/deviceClass|"+deviceClass.getName()+"|brother|"+brother.getCompleteName()+"|" );
        
        Vector members = deviceClass.getDevices();
        Enumeration enumeration = members.elements();
        ContextAttribute[] ret = new ContextAttribute[ members.size() ];
        int i = 0;
        while ( enumeration.hasMoreElements() )
        {
            Member nextDevice = ( Member ) enumeration.nextElement();
            String nextAttributePath = nextDevice.getName();
            //System.out.println ( "BasicTangoAlternateSelectionManager/loadACAttributes/nextDevice|"+nextDevice+"|" );
            
            StringTokenizer st = new StringTokenizer( nextAttributePath , GUIUtilities.TANGO_DELIM );
            String domainName = st.nextToken();
            String familyName = st.nextToken();
            String memberName = st.nextToken();

            String attName = brother.getName();
            String completeName = nextAttributePath + GUIUtilities.TANGO_DELIM + attName;

            ret[ i ] = new ContextAttribute();
            ret[ i ].setDomain( domainName );
            ret[ i ].setFamily( familyName );
            ret[ i ].setDevice( memberName );
            ret[ i ].setMember( memberName );
            ret[ i ].setName( attName );
            ret[ i ].setCompleteName( completeName );
            ret[ i ].setDeviceClass ( deviceClass.getName () );

            i++;
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoAlternateSelectionManager#loadDomains(java.lang.String)
     */
    public Domain[] loadDomains(String domainsRegExp) throws SnapshotingException 
    {
        //long before = System.currentTimeMillis ();
        //System.out.println ( "BasicTangoAlternateSelectionManagerImpl|loadDomains|domainsRegExp|"+domainsRegExp+"|" );
    	Vector domains = SnapTangoDbSearch.getInstance().getDomains();
        if ( domains == null )
        {
            return null;
        }
     
        Domain[] ret = new Domain[ domains.size() ];
        Enumeration enumeration = domains.elements();
        int i = 0;
        while ( enumeration.hasMoreElements() )
        {
            String nextDomainName = ( String ) enumeration.nextElement();
            //System.out.println ( "BasicTangoAlternateSelectionManagerImpl|loadDomains|nextDomainName|"+nextDomainName+"|" );
            ret[ i ] = new Domain( nextDomainName );
            i++;
        }
        //long after = System.currentTimeMillis ();
        //long delta = after - before;
        //System.out.println ( "BasicTangoAlternateSelectionManagerImpl|loadDomains|loaded "+i+" domains in " + delta + " milliseconds" );
        
        return ret;
    }
    
    /*private void delay(Domain domain) 
    {
        System.out.println ("CLA/delay/BEFORE/domain|"+domain.getName());
        long duration = 1; //millis
        duration *= 1000; //seconds
        duration *= 60;
        try {
            Thread.sleep ( duration );
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println ("CLA/delay/AFTER/domain|"+domain.getName());    
    }*/
}
