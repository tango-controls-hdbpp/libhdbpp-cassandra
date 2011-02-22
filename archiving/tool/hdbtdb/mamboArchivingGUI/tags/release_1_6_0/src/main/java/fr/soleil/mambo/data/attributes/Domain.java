//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/attributes/Domain.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Domain.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: Domain.java,v $
// Revision 1.6  2007/09/17 07:02:58  ounsy
// for Mambo Web classes implements java.io.serializable.
//
// Revision 1.5  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.4  2006/11/07 14:34:19  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.3  2006/05/16 12:00:33  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
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
package fr.soleil.mambo.data.attributes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import fr.soleil.mambo.tools.GUIUtilities;



public class Domain implements java.io.Serializable
{
    private String name;
    private Map families;

    /**
     * @param _name
     */
    public Domain ( String _name )
    {
        families = new TreeMap();
        this.name = _name;
    }

    /**
     * @param family 8 juil. 2005
     */
    public void addFamily ( Family family )
    {
        family.setDomain ( this );
        families.put( family.getName() , family );
    }

    public static Vector addDomain ( Vector in , Domain domain )
    {
        String domainName = domain.getName();
        Enumeration enumeration = in.elements();
        while ( enumeration.hasMoreElements() )
        {
            Domain dom = ( Domain ) enumeration.nextElement();
            String dom_s = dom.getName();
            if ( dom_s != null && dom_s.equals( domainName ) )
            {
                in.remove( dom );
            }
        }
        in.add( domain );
        return in;
    }


    /**
     * @return 8 juil. 2005
     */
    public String getName ()
    {
        return name;
    }

    /**
     * @param name 8 juil. 2005
     */
    public void setName ( String name )
    {
        this.name = name;
    }

    /**
     * @return 8 juil. 2005
     */
    public Map getFamilies ()
    {
        return families;
    }

    public static Domain hasDomain ( Vector domainsList , String domainName )
    {
        Enumeration enumeration = domainsList.elements();
        while ( enumeration.hasMoreElements() )
        {
            Domain dom = ( Domain ) enumeration.nextElement();
            String dom_s = dom.getName();
            if ( dom_s != null && dom_s.equals( domainName ) )
            {
                return dom;
            }
        }
        return null;
    }

    public Family getFamily ( String familyName )
    {
        return (Family) this.families.get( familyName );
    }

    public String toString ()
    {
        String ret = this.getName();

        if ( this.families == null )
        {
            return ret;
        }

        Iterator enumeration = this.families.values ().iterator ();
        while ( enumeration.hasNext() )
        {
            ret += GUIUtilities.CRLF;
            Family fam = ( Family ) enumeration.next();
            ret += fam.toString();
        }

        return ret;
    }

    public void addAttribute(String familyName, String memberName, String attributeName) 
    {
        Family family = this.getFamily ( familyName );
        if ( family == null )
        {
            family = new Family ( familyName );
            this.addFamily ( family );
        }
        
        family.addAttribute ( memberName , attributeName );
    }

    public String getDescription ()
    {
        //return this.getName();
        StringBuffer buff = new StringBuffer ();
        buff.append ( "    " + this.getName() );
        buff.append ( GUIUtilities.CRLF );
        Iterator keysIterator = families.keySet ().iterator ();
        while ( keysIterator.hasNext () )
        {
            String nextKey = (String) keysIterator.next ();
            Family nextFamily = (Family) families.get ( nextKey );
            buff.append ( nextFamily.getDescription () );
            buff.append ( GUIUtilities.CRLF );
        }
        return buff.toString ();
    }
}
