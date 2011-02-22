//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/tango/standard/BasicTangoManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TangoManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: BasicTangoManagerImpl.java,v $
// Revision 1.2  2006/10/19 12:45:17  ounsy
// minor changes
//
// Revision 1.1  2006/09/20 12:47:45  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.6  2006/09/14 11:29:29  ounsy
// complete buffering refactoring
//
// Revision 1.5  2006/07/18 10:29:25  ounsy
// possibility to reload tango attributes
//
// Revision 1.4  2006/06/15 15:41:58  ounsy
// added a findArchivers method
//
// Revision 1.3  2006/05/29 15:46:35  ounsy
// minor changes
//
// Revision 1.2  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.1  2006/05/16 07:52:20  ounsy
// renamed from RealTangoManagerImpl
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.datasources.tango.standard;

import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.Database;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Condition;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.HdbArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.TdbArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.Family;
import fr.soleil.mambo.data.attributes.Member;
import fr.soleil.mambo.tools.GUIUtilities;

public class BasicTangoManagerImpl implements ITangoManager
{
    public BasicTangoManagerImpl ()
    {
        //System.out.println ( "CLA/new BasicTangoManagerImpl!!!!!!!!!!!!!" );   
    }
    
    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoManager#loadDomains(java.lang.String)
     */
    public Vector loadDomains ( Criterions searchCriterions, boolean forceReload )
    {
        Vector ret = new Vector();
        Database database;

        String domain_criterions = GUIUtilities.TANGO_JOKER;
        String family_criterions = GUIUtilities.TANGO_JOKER;
        String member_criterions = GUIUtilities.TANGO_JOKER;
        //String attribute_criterions = GUIUtilities.TANGO_JOKER;

        if ( searchCriterions != null )
        {
            if ( searchCriterions.getConditions( "domain" ) != null )
            {
                Condition domainCondition = searchCriterions.getConditions( "domain" )[ 0 ];
                domain_criterions = domainCondition.getValue();    
            }
            
            if ( searchCriterions.getConditions( "family" ) != null )
            {
                Condition familyCondition = searchCriterions.getConditions( "family" )[ 0 ];
                family_criterions = familyCondition.getValue();    
            }
            
            if ( searchCriterions.getConditions( "member" ) != null )
            {
                Condition memberCondition = searchCriterions.getConditions( "member" )[ 0 ];
                member_criterions = memberCondition.getValue(); 
            }
        }

        try
        {
            database = new Database();

            String domains[] = database.get_device_domain( domain_criterions + "/" + GUIUtilities.TANGO_JOKER );

            int numberOfDomains = domains.length;
            for ( int domainsIndex = 0 ; domainsIndex < numberOfDomains ; domainsIndex++ )
            {
                String currentDomainName = domains[ domainsIndex ];
                Domain currentDomain = new Domain( currentDomainName );

                String[] families = database.get_device_family( currentDomainName + "/" + family_criterions + "/" + GUIUtilities.TANGO_JOKER );
                int numberOfFamilies = families.length;
                for ( int familiesIndex = 0 ; familiesIndex < numberOfFamilies ; familiesIndex++ )
                {
                    String currentFamilyName = families[ familiesIndex ];
                    Family currentFamily = new Family( currentFamilyName );

                    String[] members = database.get_device_member( currentDomainName + "/" + currentFamilyName + "/" + member_criterions );

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    public Archiver [] findArchivers ( boolean historic ) throws ArchivingException 
    {
        String [] list = historic? HdbArchivingManagerApiRef.m_ExportedArchiverList:TdbArchivingManagerApiRef.m_ExportedArchiverList;
        if ( list == null || list.length == 0 )
        {
            return null;    
        }
        
        int numberOfArchivers = list.length;
        Archiver [] ret = new Archiver [ numberOfArchivers ];
        for ( int i = 0 ; i < numberOfArchivers ; i ++ )
        {
            String nextName = list [ i ];
            ret [ i ] = new Archiver ( nextName , historic );
        }
        
        return ret;    
    }    
}
