//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/db/attributes/BasicAttributeManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttrDBManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: BasicAttributeManager.java,v $
// Revision 1.12  2007/02/01 14:01:50  pierrejoseph
// getAttributesToDedicatedArchiver is not useful
//
// Revision 1.11  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.10  2006/12/08 11:14:32  ounsy
// bad connection management
//
// Revision 1.9  2006/11/29 10:08:39  ounsy
// minor changes
//
// Revision 1.8  2006/11/20 09:37:55  ounsy
// minor changes
//
// Revision 1.7  2006/11/09 16:22:53  ounsy
// corrected an attributes ordering bug
//
// Revision 1.6  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.5  2006/11/07 14:35:08  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.4  2006/10/19 12:42:48  ounsy
// added getAttributesToDedicatedArchiver ( boolean historic , boolean refreshArchivers )
//
// Revision 1.3  2006/10/04 09:57:43  ounsy
// minor changes
//
// Revision 1.2  2006/09/27 13:38:21  ounsy
// now uses DbConnectionManager to manage DB connecion
//
// Revision 1.1  2006/09/22 09:32:19  ounsy
// moved from mambo.datasources.db
//
// Revision 1.6  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.5  2006/07/24 07:37:51  ounsy
// image support with partial loading
//
// Revision 1.4  2006/06/28 12:30:12  ounsy
// db attributes buffering
//
// Revision 1.3  2006/06/15 15:39:39  ounsy
// Added a getCurrentArchiverForAttribute method
//
// Revision 1.2  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.1  2006/05/16 12:01:24  ounsy
// renamed  from RealAttrDBManagerImpl
//
// Revision 1.5  2006/03/10 12:06:10  ounsy
// state and string support
//
// Revision 1.4  2006/03/07 14:37:00  ounsy
// added a isImage method
//
// Revision 1.3  2006/02/01 14:12:16  ounsy
// spectrum management
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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
package fr.soleil.mambo.datasources.db.attributes;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Condition;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.attributes.Attribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.Domains;
import fr.soleil.mambo.data.attributes.Family;
import fr.soleil.mambo.data.attributes.Member;
import fr.soleil.mambo.datasources.db.DbConnectionManager;
import fr.soleil.mambo.tools.GUIUtilities;

public class BasicAttributeManager extends DbConnectionManager implements IAttributeManager
{
    public boolean isScalar ( String completeName , boolean _historic )
    {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            int format = database.getAttribute().getAttDataFormat( completeName );
            boolean ret = ( format == AttrDataFormat._SCALAR );

            return ret;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return true;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#loadDomains(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions, boolean, boolean)
     */
    public Vector loadDomains ( Criterions searchCriterions , boolean _historic, boolean forceReload )
    {
        boolean loadCompletely = true;
        Vector ret = new Vector();

        String domain_criterions = GUIUtilities.TANGO_JOKER;
        String family_criterions = GUIUtilities.TANGO_JOKER;
        String member_criterions = GUIUtilities.TANGO_JOKER;
        String attribute_criterions = GUIUtilities.TANGO_JOKER;

        if ( searchCriterions != null )
        {
            Condition [] domainConditions = searchCriterions.getConditions( "domain" );
            if ( domainConditions != null )
            {
                Condition domainCondition = domainConditions[ 0 ];
                domain_criterions = domainCondition.getValue();    
            }

            Condition [] familyConditions = searchCriterions.getConditions( "family" );
            if ( familyConditions != null )
            {
                Condition familyCondition = familyConditions[ 0 ];
                family_criterions = familyCondition.getValue();    
            }
            
            Condition [] memberConditions = searchCriterions.getConditions( "member" );
            if ( memberConditions != null )
            {
                Condition memberCondition = memberConditions[ 0 ];
                member_criterions = memberCondition.getValue();    
            }

            Condition [] attributeConditions = searchCriterions.getConditions( "att_name" );
            if ( attributeConditions != null )
            {
                Condition attributeCondition = attributeConditions[ 0 ];
                attribute_criterions = attributeCondition.getValue();    
            }
        }

        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            String domains_s[] = database.getAttribute().getDomains().get_domains_by_criterion( domain_criterions );

            int numberOfDomains = domains_s.length;
            for ( int domainsIndex = 0 ; domainsIndex < numberOfDomains ; domainsIndex++ )
            {
                String currentDomainName = domains_s[ domainsIndex ];
                Domain currentDomain = new Domain( currentDomainName );

                String[] families_s = database.getAttribute().getFamilies().get_families_by_criterion( currentDomainName , family_criterions );

                int numberOfFamilies = families_s.length;
                for ( int familiesIndex = 0 ; familiesIndex < numberOfFamilies ; familiesIndex++ )
                {
                    String currentFamilyName = families_s[ familiesIndex ];
                    Family currentFamily = new Family( currentFamilyName );

                    String[] members_s = database.getAttribute().getMembers().get_members_by_criterion( currentDomainName , currentFamilyName , member_criterions );

                    int numberOfMembers = members_s.length;
                    for ( int membersIndex = 0 ; membersIndex < numberOfMembers ; membersIndex++ )
                    {
                        String currentMemberName = members_s[ membersIndex ];
                        Member currentMember = new Member( currentMemberName );
                        currentFamily.addMember( currentMember );

                        if ( loadCompletely )
                        {
                            String[] attributes_s = database.getAttribute().getNames().getAttributes_by_criterion( currentDomainName , currentFamilyName , currentMemberName , attribute_criterions );

                            int numberOfAttributes = attributes_s.length;
                            for ( int attributesIndex = 0 ; attributesIndex < numberOfAttributes ; attributesIndex++ )
                            {
                                String currentAttributeName = attributes_s[ attributesIndex ];
                                currentAttributeName = parseCompleteName( currentAttributeName );
                                Attribute currentAttribute = new Attribute();
                                currentAttribute.setName( currentAttributeName );
                                currentAttribute.setDomain( currentDomainName );
                                currentAttribute.setFamily( currentFamilyName );
                                currentAttribute.setMember( currentMemberName );
                                currentMember.addAttribute( currentAttribute );
                            }
                        }
                    }
                    currentDomain.addFamily( currentFamily );
                }
                ret.add( currentDomain );
            }
        }
        catch ( ArchivingException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Collections.sort( ret, new MamboDomainsComparator() );
        return ret;
    }

    public Vector loadDomains ( boolean _historic, boolean forceReload )
    {
        //boolean loadCompletely = true;
        Domains domains = new Domains ();
        //String domain_criterions = GUIUtilities.TANGO_JOKER;

        try
        {
            DataBaseManager database = getDataBaseApi( _historic );
            if ( database == null )
            {
                return null;     
            }
            
            String completeNames[] = database.getAttribute().getNames().get_attributes_complete_names();
            if ( completeNames == null )
            {
                return new Vector();
            }
            for ( int i = 0 ; i < completeNames.length ; i ++ )
            {
                String [] dfma = this.parseCompleteNameIntoDFMA(completeNames[i] );
                
                String domainName = dfma [ 0 ];
                String familyName = dfma [ 1 ];
                String memberName = dfma [ 2 ];
                String attributeName = dfma [ 3 ];
                
                domains.addAttribute ( domainName , familyName , memberName , attributeName );
            }
        }
        catch ( ArchivingException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Vector ret = domains.getList ();
        //Collections.sort( ret, new MamboDomainsComparator() );
        return ret;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#getAttributes(java.lang.String, java.lang.String, java.lang.String)
     */
    public ArrayList getAttributes ( String domain , String family , String member , boolean _historic ) throws DevFailed , ArchivingException
    {
        DataBaseManager database = getDataBaseApi( _historic );
        String[] list = database.getAttribute().getNames().getAttributes( domain , family , member );

        if ( list == null )
        {
            return null;
        }

        ArrayList ret = new ArrayList( list.length );
        for ( int i = 0 ; i < list.length ; i++ )
        {
            ret.add( parseCompleteName( list[ i ] ) );
        }

        return ret;
    }

    /**
     * @param string
     * @return 26 ao�t 2005
     */
    private String parseCompleteName ( String completeName )
    {
        StringTokenizer st = new StringTokenizer( completeName , "/" );
        String ret = "";
        while ( true )
        {
            try
            {
                ret = st.nextToken();
            }
            catch ( NoSuchElementException nsee )
            {
                break;
            }
        }
        return ret;
    }
    
    private String[] parseCompleteNameIntoDFMA ( String completeName )
    {
        StringTokenizer st = new StringTokenizer( completeName , "/" );
        String [] ret  = new String [4];
        
        ret [ 0 ] = st.nextToken();
        ret [ 1 ] = st.nextToken();
        ret [ 2 ] = st.nextToken();
        ret [ 3 ] = st.nextToken();
        
        return ret;
    }
    

    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#isSpectrum(java.lang.String, boolean)
     */
    public boolean isSpectrum(String completeName, boolean _historic) {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            int format = database.getAttribute().getAttDataFormat( completeName );
            boolean ret = ( format == AttrDataFormat._SPECTRUM );

            return ret;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#getDataType(java.lang.String, boolean)
     */
    public int getDataType ( String completeName, boolean _historic )
    {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            int type = database.getAttribute().getAttDataType( completeName );

            return type;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return -1;
    }

    public int getDataWritable (String completeName, boolean _historic)
    {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            int writable = database.getAttribute().getAttDataWritable( completeName );

            return writable;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#isSpectrum(java.lang.String, boolean)
     */
    public boolean isImage(String completeName, boolean _historic) 
    {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            int format = database.getAttribute().getAttDataFormat( completeName );
            boolean ret = ( format == AttrDataFormat._IMAGE );

            return ret;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return false;
    }

    
    public String getDisplayFormat (String completeName, boolean _historic)
    {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );

            String format = database.getAttribute().getProperties().getDisplayFormat( completeName );

            return format;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param name
     *            the name of the attribute
     * @param _historic
     *            a boolean to know wheather you check HDB or TDB
     * @return an int representing the format of the attribute. -1 on error
     */
    public int getFormat (String name, boolean _historic)
    {
        try
        {
            DataBaseManager database = getDataBaseApi( _historic );
            return database.getAttribute().getAttDataFormat( name );
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    /*private Hashtable getAttributesToDedicatedArchiver ( boolean historic , boolean refreshArchivers ) throws ArchivingException 
    {
        return ArchivingManagerApi.getAttributesToDedicatedArchiver ( historic , refreshArchivers );
    }*/
}