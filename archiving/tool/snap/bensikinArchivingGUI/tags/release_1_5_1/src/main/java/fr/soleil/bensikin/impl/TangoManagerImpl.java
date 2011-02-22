//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/impl/TangoManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TangoManagerImpl.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.8 $
//
// $Log: TangoManagerImpl.java,v $
// Revision 1.8  2007/06/14 15:23:15  pierrejoseph
// Catch addition in the loadDomains (NullPointer raised by new Database())
//
// Revision 1.7  2006/11/29 10:02:17  ounsy
// package refactoring
//
// Revision 1.1  2005/12/14 16:56:05  ounsy
// has been renamed
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.impl;

import java.util.ArrayList;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.Database;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.soleil.bensikin.datasources.tango.ITangoManager;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;

/**
 * An implementation that uses the Database API.
 *
 * @author CLAISSE
 */
public class TangoManagerImpl implements ITangoManager
{

    /**
     * Loads Tango attributes into a Vector containing Domain objects:
     * <UL>
     * <LI> Gets the criterions on the respective domain, family, member, and attribute parts from <code>searchCriterions</code>; if one of them is null, it is replaced with a joker
     * <LI> Uses the Database API to get the domains matching the domain criterion
     * <LI> For each such domain, uses the Database API to get the families rattached to this domain and matching the family criterion
     * <LI> For each such family, uses the Database API to get the members rattached to this family and matching the member criterion
     * <LI> The loading stops there, the attributes are not loaded yet lest Tango be overloaded
     * </UL>
     *
     * @param searchCriterions The search criterions
     * @return A Vector containing Domain objects
     */
    public Vector loadDomains ( Criterions searchCriterions )
    {
        Vector ret = new Vector();
        Database database;

        String domain_criterions = GUIUtilities.TANGO_JOKER;
        String family_criterions = GUIUtilities.TANGO_JOKER;
        String member_criterions = GUIUtilities.TANGO_JOKER;
        //String attribute_criterions = GUIUtilities.TANGO_JOKER;

        if ( searchCriterions != null )
        {
            System.out.println( searchCriterions.toString() );

            Condition domainCondition = searchCriterions.getConditions( GlobalConst.TAB_DEF[ 4 ] )[ 0 ];
            domain_criterions = domainCondition.getValue();

            Condition familyCondition = searchCriterions.getConditions( GlobalConst.TAB_DEF[ 5 ] )[ 0 ];
            family_criterions = familyCondition.getValue();

            Condition memberCondition = searchCriterions.getConditions( GlobalConst.TAB_DEF[ 6 ] )[ 0 ];
            member_criterions = memberCondition.getValue();

            //Condition attributeCondition = searchCriterions.getConditions( GlobalConst.TAB_DEF[ 7 ] )[ 0 ];
            //attribute_criterions = attributeCondition.getValue();
        }

        try
        {
        	
        	
            database = new Database();
            
            String domains[] = database.get_device_domain( domain_criterions + GUIUtilities.TANGO_DELIM + GUIUtilities.TANGO_JOKER );

            int numberOfDomains = domains.length;
            for ( int domainsIndex = 0 ; domainsIndex < numberOfDomains ; domainsIndex++ )
            {
                String currentDomainName = domains[ domainsIndex ];
                Domain currentDomain = new Domain( currentDomainName );

                String[] families = database.get_device_family( currentDomainName + GUIUtilities.TANGO_DELIM + family_criterions + GUIUtilities.TANGO_DELIM + GUIUtilities.TANGO_JOKER );
                int numberOfFamilies = families.length;
                for ( int familiesIndex = 0 ; familiesIndex < numberOfFamilies ; familiesIndex++ )
                {
                    String currentFamilyName = families[ familiesIndex ];
                    Family currentFamily = new Family( currentFamilyName );

                    String[] members = database.get_device_member( currentDomainName + GUIUtilities.TANGO_DELIM + currentFamilyName + GUIUtilities.TANGO_DELIM + member_criterions );

                    int numberOfMembers = members.length;
                    for ( int membersIndex = 0 ; membersIndex < numberOfMembers ; membersIndex++ )
                    {
                        String currentMemberName = members[ membersIndex ];
                        Member currentMember = new Member( currentMemberName );
                        currentFamily.addMember( currentMember );
                    }

                    currentDomain.addFamily( currentFamily );
                }
                ret.add( currentDomain );
            }
        }
        catch ( DevFailed e )
        {
            e.printStackTrace(System.out);
        }
        catch(Exception e)
        {
        	e.printStackTrace(System.out);
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.impl.ITangoManager#dbGetAttributeList(java.lang.String)
     */
    public ArrayList dbGetAttributeList ( String device_name )
    {
        ArrayList liste_att = new ArrayList( 32 );

        try
        {
            DeviceProxy deviceProxy = new DeviceProxy( device_name );
            deviceProxy.ping();
            DeviceData device_data_argin = new DeviceData();
            String[] device_data_argout;
            device_data_argin.insert( GUIUtilities.TANGO_JOKER + device_name + GUIUtilities.TANGO_JOKER );
            device_data_argout = deviceProxy.get_attribute_list();

            for ( int i = 0 ; i < device_data_argout.length ; i++ )
            {
                liste_att.add( device_data_argout[ i ] );
            }

            liste_att.trimToSize();
            return liste_att;
        }
        catch ( DevFailed e )
        {
            return null;
        }
    }

}
