//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/xmlhelpers/XMLLine.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLLine.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.1 $
//
// $Log: XMLLine.java,v $
// Revision 1.1  2007/02/01 14:07:17  pierrejoseph
// getAttributesToDedicatedArchiver
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.tools.xmlhelpers;

import java.util.Enumeration;
import java.util.Hashtable;


public class XMLLine
{
    String name = null;
    Hashtable attributes = null;
    int tagCategory = 0;

    private static final String OPEN_TAG = "<";
    private static final String CLOSE_TAG = ">";
    private static final String EMPTY_TAG = " /";
    private static final String SPACE = " ";
    private static final String EQUALS = "=";
    private static final String QUOTE = "\"";
    private static final String CLOSING_TAG = "</";

    public static final int EMPTY_TAG_CATEGORY = 0;
    public static final int OPENING_TAG_CATEGORY = 1;
    public static final int CLOSING_TAG_CATEGORY = 2;

    /**
     *
     */
    private XMLLine ()
    {

    }

    /**
     * @param _name
     */
    public XMLLine ( String _name )
    {
        name = _name;
        attributes = new Hashtable();
        tagCategory = EMPTY_TAG_CATEGORY;
    }

    /**
     * @param _name
     * @param _tagCategory
     */
    public XMLLine ( String _name , int _tagCategory )
    {
        name = _name;
        attributes = new Hashtable();
        tagCategory = _tagCategory;
    }

    /**
     * @param _name
     * @param _attributes
     */
    public XMLLine ( String _name , Hashtable _attributes )
    {
        name = _name;
        attributes = _attributes;
        tagCategory = EMPTY_TAG_CATEGORY;
    }

    /**
     * Ajoute un attribut a la ligne XML
     *
     * @param clef Le name de l'attribut.
     * @param clef La valeur de l'attribut.
     * @author CLA
     * @version 21/07/2004
     */
    public void setAttribute ( String clef , String valeur )
    {
        attributes.put( clef , valeur );
    }

    /**
     * @param clef
     * @return 8 juil. 2005
     */
    public String getAttribute ( String clef )
    {
        if ( attributes == null )
        {
            return null;
        }

        return ( String ) attributes.get( clef );
    }

    /*public void setId ( String id )
    {
        setAttribute ( History.ID_KEY , id );
    }*/

    /**
     * @return 8 juil. 2005
     */
    /*public String getId ()
    {
        return getAttribute ( History.ID_KEY );
    }*/

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        String retour = null;
        switch ( tagCategory )
        {
            case CLOSING_TAG_CATEGORY:
                retour = CLOSING_TAG + name + CLOSE_TAG;
                break;

            case OPENING_TAG_CATEGORY:
                retour = OPEN_TAG + name;

                if ( attributes != null )
                {
                    Enumeration e = attributes.keys();
                    while ( e.hasMoreElements() )
                    {
                        String clef = ( String ) e.nextElement();
                        String valeur = ( String ) attributes.get( clef );

                        retour += SPACE;
                        retour += clef;
                        retour += EQUALS;
                        retour += QUOTE;
                        //retour += valeur;
                        retour += XMLUtils.replaceXMLChars( valeur );
                        retour += QUOTE;
                    }
                }

                retour += CLOSE_TAG;
                break;

            case EMPTY_TAG_CATEGORY:
                retour = OPEN_TAG + name;

                if ( attributes != null )
                {
                    Enumeration e = attributes.keys();
                    while ( e.hasMoreElements() )
                    {
                        String clef = ( String ) e.nextElement();
                        String valeur = ( String ) attributes.get( clef );

                        retour += SPACE;
                        retour += clef;
                        retour += EQUALS;
                        retour += QUOTE;
//						retour += valeur;
                        retour += XMLUtils.replaceXMLChars( valeur );
                        retour += QUOTE;
                    }
                }

                retour += EMPTY_TAG;
                retour += CLOSE_TAG;
                break;

            default :
                //dsdsd
                break;
        }

        return retour;
    }


    /**
     * @return 8 juil. 2005
     */
    public String getName ()
    {
        return name;
    }

    /**
     * @return 8 juil. 2005
     */
    public Hashtable getAttributes ()
    {
        return attributes;
    }

    /**
     * @param attributes 8 juil. 2005
     */
    public void setAttributes ( Hashtable attributes )
    {
        this.attributes = attributes;
    }

    /**
     * @param display_duration_property_xml_tag
     *
     * @param string 24 août 2005
     */
    public void setAttributeIfNotNull ( String s , String string )
    {
        if ( string != null )
        {
            setAttribute( s , string );
        }
    }
}
