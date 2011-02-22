//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/attributes/Attributes.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Attributes.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: Attributes.java,v $
// Revision 1.4  2007/09/17 07:02:58  ounsy
// for Mambo Web classes implements java.io.serializable.
//
// Revision 1.3  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
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
package fr.soleil.mambo.data.attributes;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class Attributes implements java.io.Serializable
{
    private Attribute[] attributes;
    //private Context context;
    
    public Attributes ()
    {

    }

    /**
     * @return 8 juil. 2005
     */
    public Vector getHierarchy ()
    {
        if ( this.attributes == null )
        {
            return null;
        }

        int nbAttributes = this.attributes.length;

        //Hashtable domains
        Hashtable htDomains = new Hashtable();
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            Attribute currentAttribute = this.attributes[ i ];
            String currentDomain = currentAttribute.getDomainName();
            htDomains.put( currentDomain , new Hashtable() );

            //GUIUtilities.trace ( 9 , "CLA/getHierarchy/currentDomain/"+currentDomain+"/" );
        }

        //Hashtable families
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            Attribute currentAttribute = this.attributes[ i ];
            String currentDomain = currentAttribute.getDomainName();
            String currentFamily = currentAttribute.getFamilyName();

            Hashtable htCurrentDomain = ( Hashtable ) htDomains.get( currentDomain );
            htCurrentDomain.put( currentFamily , new Hashtable() );
            //GUIUtilities.trace ( 9 , "CLA/getHierarchy/currentFamily/"+currentFamily+"/" );
        }

        //Hashtable members
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            Attribute currentAttribute = this.attributes[ i ];
            String currentDomain = currentAttribute.getDomainName();
            String currentFamily = currentAttribute.getFamilyName();
            String currentMember = currentAttribute.getMemberName();

            Hashtable htCurrentDomain = ( Hashtable ) htDomains.get( currentDomain );
            Hashtable htCurrentFamily = ( Hashtable ) htCurrentDomain.get( currentFamily );

            htCurrentFamily.put( currentMember , new Vector() );
            //GUIUtilities.trace ( 9 , "CLA/getHierarchy/currentMember/"+currentMember+"/" );
        }

        //Vector attributes
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            Attribute currentAttribute = this.attributes[ i ];
            String currentDomain = currentAttribute.getDomainName();
            String currentFamily = currentAttribute.getFamilyName();
            String currentMember = currentAttribute.getMemberName();
            String currentAttributeName = currentAttribute.getName();

            Hashtable htCurrentDomain = ( Hashtable ) htDomains.get( currentDomain );
            Hashtable htCurrentFamily = ( Hashtable ) htCurrentDomain.get( currentFamily );
            Vector veCurrentMember = ( Vector ) htCurrentFamily.get( currentMember );

            veCurrentMember.add( currentAttributeName );
            //GUIUtilities.trace ( 9 , "CLA/getHierarchy/currentAttributeName/"+currentAttributeName+"/" );
        }

        Vector result = new Vector();

        Enumeration enumDomains = htDomains.keys();
        while ( enumDomains.hasMoreElements() )
        {
            String nextDomainName = ( String ) enumDomains.nextElement();
            //GUIUtilities.trace ( 9 , "D:" + nextDomainName );
            Domain nextDomain = new Domain( nextDomainName );
            result.add( nextDomain );

            Hashtable htFamilies = ( Hashtable ) htDomains.get( nextDomainName );
            Enumeration enumFamilies = htFamilies.keys();
            while ( enumFamilies.hasMoreElements() )
            {
                String nextFamilyName = ( String ) enumFamilies.nextElement();
                //GUIUtilities.trace ( 9 , "F:    " + nextFamilyName );
                Family nextFamily = new Family( nextFamilyName );
                nextDomain.addFamily( nextFamily );

                Hashtable htMembers = ( Hashtable ) htFamilies.get( nextFamilyName );
                Enumeration enumMembers = htMembers.keys();
                while ( enumMembers.hasMoreElements() )
                {
                    String nextMemberName = ( String ) enumMembers.nextElement();
                    //GUIUtilities.trace ( 9 , "M:        " + nextMemberName );
                    Member nextMember = new Member( nextMemberName );
                    nextFamily.addMember( nextMember );

                    Vector vectAttr = ( Vector ) htMembers.get( nextMemberName );
                    Enumeration enumAttr = vectAttr.elements();
                    while ( enumAttr.hasMoreElements() )
                    {
                        String nextAttrName = ( String ) enumAttr.nextElement();
                        //GUIUtilities.trace ( 9 , "A:            " + nextAttrName );
                        Attribute nextAttr = new Attribute( nextAttrName );
                        nextMember.addAttribute( nextAttr );
                    }
                }
            }
        }

        result.trimToSize();
        return result;
    }
    
    /**
     * @param _context
     */
    /*public Attributes ( Context _context )
    {
        this.context = _context;
    }*/
    
    /**
     * @param _context
     * @param _attributes
     */
    /*public ContextAttributes ( Context _context , ContextAttribute [] _attributes )
    {
        this.context = _context;
        this.attributes = _attributes;
    }*/
    public Attributes ( Attribute[] _attributes )
    {
        this.attributes = _attributes;
    }

    /**
     * @return 8 juil. 2005
     */
    /*public Context getContext() 
    {
        return context;
    }*/


    public Attribute[] getContextAttributes ()
    {
        return attributes;
    }

    /**
     * @param attributes 8 juil. 2005
     */
    public void setContextAttributes ( Attribute[] attributes )
    {
        this.attributes = attributes;
    }
}
