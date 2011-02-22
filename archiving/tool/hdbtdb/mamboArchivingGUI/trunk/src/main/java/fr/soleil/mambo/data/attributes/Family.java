//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/attributes/Family.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Family.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: Family.java,v $
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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import fr.soleil.mambo.tools.GUIUtilities;



public class Family implements java.io.Serializable
{
    private String name;
    private Map members;
    private Domain domain;

    /**
     * @param _name
     */
    public Family ( String _name )
    {
        members = new Hashtable();
        this.name = _name;
    }

    /**
     * @param member 8 juil. 2005
     */
    public void addMember ( Member member )
    {
        member.setFamily ( this );
        this.members.put(member.getName(), member);
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
    public Map getMembers ()
    {
        return members;
    }

    public Member getMember ( String memberName )
    {
        return (Member) this.members.get( memberName );
    }

    public String toString ()
    {
        String ret = "    " + this.getName();

        if ( this.members == null )
        {
            return ret;
        }

        Iterator enumeration = this.members.values().iterator();
        while ( enumeration.hasNext() )
        {
            ret += GUIUtilities.CRLF;
            Member mem = ( Member ) enumeration.next();
            ret += mem.toString();
        }

        return ret;
    }

    public void addAttribute(String memberName, String attributeName) 
    {
        Member member = this.getMember ( memberName );
        if ( member == null )
        {
            member = new Member ( memberName );
            this.addMember ( member );
        }
        
        member.addAttribute ( new Attribute ( attributeName ) );
    }

    public String getDescription ()
    {
        StringBuffer buff = new StringBuffer ();
        buff.append ( "        " + this.getName() );
        buff.append ( GUIUtilities.CRLF );
        Iterator keysIterator = members.keySet ().iterator ();
        while ( keysIterator.hasNext () )
        {
            String nextKey = (String) keysIterator.next ();
            Member nextMember = (Member) members.get ( nextKey );
            buff.append ( nextMember.getDescription () );
            buff.append ( GUIUtilities.CRLF );
        }
        return buff.toString ();
        //return this.getName();
    }

    /**
     * @return the domain
     */
    public Domain getDomain() {
        return this.domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
