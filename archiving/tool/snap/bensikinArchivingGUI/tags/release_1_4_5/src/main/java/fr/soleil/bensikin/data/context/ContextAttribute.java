//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/ContextAttribute.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextAttribute.
//						(Garda Laure) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ContextAttribute.java,v $
// Revision 1.8  2007/08/23 15:28:48  ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.7  2007/03/15 14:26:35  ounsy
// corrected the table mode add bug and added domains buffer
//
// Revision 1.6  2005/12/14 16:33:07  ounsy
// added methods necessary for alternate attribute selection
//
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:37  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.context;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;


/**
 * Represents an attribute attached to a context
 *
 * @author CLAISSE
 */
public class ContextAttribute
{
    private String name;
    private String completeName;
    private String domain;
    private String family;
    private String member;
    private String device;

    private ContextAttributes contextAttributes;
    
    private boolean isSelected = true;
    private boolean isNew = false;
    
    private String deviceClass;

    /**
     * The XML tag name used in saving/loading
     */
    public static final String XML_TAG = "Attribute";

    /**
     * The XML tag name used for the "complete name" property
     */
    public static final String COMPLETE_NAME_PROPERTY_XML_TAG = "CompleteName";

    /**
     * The XML tag name used for the "name" property
     */
    public static final String NAME_PROPERTY_XML_TAG = "Name";

    /**
     * The XML tag name used for the "domain" property
     */
    public static final String DOMAIN_PROPERTY_XML_TAG = "Domain";

    /**
     * The XML tag name used for the "family" property
     */
    public static final String FAMILY_PROPERTY_XML_TAG = "Family";

    /**
     * The XML tag name used for the "member" property
     */
    public static final String MEMBER_PROPERTY_XML_TAG = "Member";

    /**
     * The XML tag name used for the "device" property
     */
    public static final String DEVICE_PROPERTY_XML_TAG = "Device";

    /**
     * Builds a ContextAttribute with a reference to its ContextAttributes group.
     *
     * @param _contextAttributes The group this attributes belongs to
     */
    public ContextAttribute ( ContextAttributes _contextAttributes )
    {
        this.contextAttributes = _contextAttributes;
    }

    /**
     * Builds a ContextAttribute of name <code>_name</code>.
     *
     * @param _name
     */
    public ContextAttribute ( String _name )
    {
        name = _name;
    }

    /**
     * Default constructor
     */
    public ContextAttribute ()
    {
    }

    /**
     * @param path
     */
    public ContextAttribute ( TreeNode[] path )
    {
        String _domain = ( String ) path[ 1 ].toString();
        String _family = ( String ) path[ 2 ].toString();
        String _member = ( String ) path[ 3 ].toString();
        String _name = ( String ) path[ 4 ].toString();

        this.domain = _domain;
        this.family = _family;
        this.member = _member;
        //this.device = _member;
        this.name = _name;
        
        this.device = _domain + GUIUtilities.TANGO_DELIM + _family + GUIUtilities.TANGO_DELIM + _member;
        this.completeName = _domain + GUIUtilities.TANGO_DELIM + _family + GUIUtilities.TANGO_DELIM + _member + GUIUtilities.TANGO_DELIM + _name;
    }
    
    /**
     * Returns a XML representation of the attribute.
     *
     * @return a XML representation of the attribute
     */
    public String toString ()
    {
        String ret = "";

        XMLLine emptyLine = new XMLLine( ContextAttribute.XML_TAG , XMLLine.EMPTY_TAG_CATEGORY );

        if ( this.completeName != null )
        {
            emptyLine.setAttribute( ContextAttribute.COMPLETE_NAME_PROPERTY_XML_TAG , this.completeName );
        }
        if ( this.name != null )
        {
            emptyLine.setAttribute( ContextAttribute.NAME_PROPERTY_XML_TAG , this.name );
        }
        if ( this.domain != null )
        {
            emptyLine.setAttribute( ContextAttribute.DOMAIN_PROPERTY_XML_TAG , this.domain );
        }
        if ( this.family != null )
        {
            emptyLine.setAttribute( ContextAttribute.FAMILY_PROPERTY_XML_TAG , this.family );
        }
        if ( this.member != null )
        {
            emptyLine.setAttribute( ContextAttribute.MEMBER_PROPERTY_XML_TAG , this.member );
        }
        if ( this.device != null )
        {
            emptyLine.setAttribute( ContextAttribute.DEVICE_PROPERTY_XML_TAG , this.device );
        }

        ret += emptyLine.toString();
        ret += GUIUtilities.CRLF;

        return ret;
    }

    public void appendUserFriendlyString(StringBuffer buffer) {
        if (buffer != null) {
            buffer.append(completeName);
        }
    }

    /**
     * @return Returns the completeName.
     */
    public String getCompleteName ()
    {
        return completeName;
    }

    /**
     * @param completeName The completeName to set.
     */
    public void setCompleteName ( String completeName )
    {
        this.completeName = completeName;
    }

    /**
     * @return Returns the contextAttributes.
     */
    public ContextAttributes getContextAttributes ()
    {
        return contextAttributes;
    }

    /**
     * @param contextAttributes The contextAttributes to set.
     */
    public void setContextAttributes ( ContextAttributes contextAttributes )
    {
        this.contextAttributes = contextAttributes;
    }

    /**
     * @return Returns the device.
     */
    public String getDevice ()
    {
        //return device;
        this.device = this.domain + GUIUtilities.TANGO_DELIM + this.family + GUIUtilities.TANGO_DELIM + this.member;
        return device;
    }

    /**
     * @param device The device to set.
     */
    public void setDevice ( String device )
    {
        this.device = device;
    }

    /**
     * @return Returns the domain.
     */
    public String getDomain ()
    {
        return domain;
    }

    /**
     * @param domain The domain to set.
     */
    public void setDomain ( String domain )
    {
        this.domain = domain;
    }

    /**
     * @return Returns the family.
     */
    public String getFamily ()
    {
        return family;
    }

    /**
     * @param family The family to set.
     */
    public void setFamily ( String family )
    {
        this.family = family;
    }

    /**
     * @return Returns the member.
     */
    public String getMember ()
    {
        return member;
    }

    /**
     * @param member The member to set.
     */
    public void setMember ( String member )
    {
        this.member = member;
    }

    /**
     * @return Returns the name.
     */
    public String getName ()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName ( String name )
    {
        this.name = name;
    }
    /**
     * @return Returns the isNew.
     */
    public boolean isNew() {
        return isNew;
    }
    /**
     * @param isNew The isNew to set.
     */
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
    /**
     * @return Returns the isSelected.
     */
    public boolean isSelected() {
        return isSelected;
    }
    /**
     * @param isSelected The isSelected to set.
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    
    /**
     * 
     */
    public void reverseSelection ()
    {
        this.isSelected = !this.isSelected;
    }
    
    /**
     * @return
     */
    public TreePath getTreePath ()
    {

        String[] pathRef = new String[ 5 ];
        pathRef[ 0 ] = "";
        pathRef[ 1 ] = this.getDomain();
        pathRef[ 2 ] = this.getFamily();
        pathRef[ 3 ] = this.getMember();
        pathRef[ 4 ] = this.getName();
        
        TreePath aPath = new TreePath( pathRef );
        return aPath;
    }

    /**
     * @return the deviceClass
     */
    public String getDeviceClass() {
        return this.deviceClass;
    }

    /**
     * @param deviceClass the deviceClass to set
     */
    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }
}
