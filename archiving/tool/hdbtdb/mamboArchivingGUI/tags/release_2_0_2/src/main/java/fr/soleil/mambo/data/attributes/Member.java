//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/attributes/Member.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Member.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: Member.java,v $
// Revision 1.7  2007/09/17 07:02:58  ounsy
// for Mambo Web classes implements java.io.serializable.
//
// Revision 1.6  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.5  2006/11/07 14:34:19  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import fr.soleil.mambo.tools.GUIUtilities;



public class Member implements java.io.Serializable
{
    private String name;
    private Map attributes;
    private Family family;

    /**
     * @param _name
     */
    public Member ( String _name )
    {
        attributes = new Hashtable();
        this.name = _name;
    }

    /**
     * @param attribute 8 juil. 2005
     */
    public void addAttribute ( Attribute attribute )
    {
        attribute.setMember ( this );
        attributes.put( attribute.getName () , attribute );
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
    public Map getAttributes ()
    {
        return attributes;
    }
    
    public Attribute getAttribute ( String attrName )
    {
        return (Attribute) this.attributes.get( attrName );
    }

    public String toString ()
    {
        String ret = "        " + this.getName();

        if ( this.attributes == null )
        {
            return ret;
        }

        Iterator enumeration = this.attributes.values().iterator();
        while ( enumeration.hasNext () )
        {
            ret += GUIUtilities.CRLF;
            Attribute att = ( Attribute ) enumeration.next();
            ret += att.toString();
        }

        return ret;
    }

    /**
     * @param attr_s 14 sept. 2005
     */
    public void removeAttribute ( String attrToRemove )
    {
        this.attributes.remove( attrToRemove );
    }

    public String getDescription ()
    {
        //return "            " + this.getName();
        StringBuffer buff = new StringBuffer ();
        buff.append ( "            " + this.getName() );
        buff.append ( GUIUtilities.CRLF );
        Iterator keysIterator = attributes.keySet ().iterator ();
        while ( keysIterator.hasNext () )
        {
            String nextKey = (String) keysIterator.next ();
            Attribute nextAttribute = (Attribute) attributes.get ( nextKey );
            buff.append ( nextAttribute.getDescription () );
            //buff.append (  "                " + nextKey );
            buff.append ( GUIUtilities.CRLF );
        }
        return buff.toString ();
    }

    /**
     * @return the family
     */
    public Family getFamily() {
        return this.family;
    }

    /**
     * @param family the family to set
     */
    public void setFamily(Family family) {
        this.family = family;
    }
}
