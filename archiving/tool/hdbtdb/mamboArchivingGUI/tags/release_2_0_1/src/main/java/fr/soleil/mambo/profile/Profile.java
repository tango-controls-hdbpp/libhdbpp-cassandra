//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/profile/Profile.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Profile.
//						(Claisse Laurent) - nov. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.2 $
//
// $Log: Profile.java,v $
// Revision 1.2  2007/02/01 14:13:30  pierrejoseph
// XmlHelper reorg
//
// Revision 1.1  2005/12/15 09:21:07  ounsy
// First Commit including profile management
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.profile;

import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

/**
 * This class determines every necessary information for a bensikin profile
 *
 * @author SOLEIL
 */
public class Profile
{

    public final static String PROFILE_TAG = "PROFILE";
    public final static String ID_TAG = "ID";
    public final static String NAME_TAG = "NAME";
    public final static String PATH_TAG = "PATH";

    private int id;
    private String name;
    private String path;

    /**
     * Constructor of a profile
     *
     * @param _id   id of the profile
     * @param _name
     * @param _path
     */
    public Profile ( int _id , String _name , String _path )
    {
        this.id = _id;
        this.name = _name;
        this.path = _path;
    }

    /**
     * Constructor of a dummy profile.
     * Only usefull to make the "new Profile" node in profile selection tree
     */
    public Profile ()
    {
        this.id = -1;
        this.name = Messages.getMessage( "PROFILE_NEW" );
        this.path = "";
    }

    /**
     * @return The id of the profile as an <code>int</code>
     */
    public int getId ()
    {
        return this.id;
    }

    /**
     * @return The name of the profile as <code>String</code>
     */
    public String getName ()
    {
        return this.name;
    }

    /**
     * @return The path of the profile as <code>String</code>
     */
    public String getPath ()
    {
        return this.path;
    }

    public String toString ()
    {
        return this.getName();
    }

    public boolean equals ( Object o )
    {
        if ( o == null )
        {
            return false;
        }
        if ( o instanceof Profile )
        {
            return ( this.getId() == ( ( Profile ) o ).getId() );
        }
        return false;
    }

    /**
     * Returns an xml line
     *
     * @return An xml line. Used to save this profile.
     */
    public String toXMLString ()
    {
        XMLLine line = new XMLLine( PROFILE_TAG , XMLLine.EMPTY_TAG_CATEGORY );
        line.setAttribute( ID_TAG , this.getId() + "" );
        line.setAttribute( NAME_TAG , this.getName() );
        line.setAttribute( PATH_TAG , this.getPath() );
        return line.toString();
    }

}
