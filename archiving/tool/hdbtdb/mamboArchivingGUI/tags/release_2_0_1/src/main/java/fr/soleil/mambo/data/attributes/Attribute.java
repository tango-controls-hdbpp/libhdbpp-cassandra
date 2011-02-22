//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/attributes/Attribute.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Attribute.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: Attribute.java,v $
// Revision 1.6  2007/09/17 07:02:58  ounsy
// for Mambo Web classes implements java.io.serializable.
//
// Revision 1.5  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.4  2006/10/19 12:41:24  ounsy
// corrected setCompleteName() to display a correct device name
//
// Revision 1.3  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
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

import java.util.StringTokenizer;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.GUIUtilities;


public class Attribute implements java.io.Serializable
{
    private String name;
    private String completeName;
    private String domainName;
    private String familyName;
    private String memberName;
    private String deviceName;
    
    private Attributes attributes;
    private Member member;

    /**
     * @param _contextAttributes
     */
    public Attribute ( Attributes _contextAttributes )
    {
        this.attributes = _contextAttributes;
    }

    public Attribute ()
    {

    }

    public TreePath getTreePath ( String rootName )
    {
        try
        {
            //TreePath(Object[] path)

            StringTokenizer st = new StringTokenizer( this.completeName , "/" );
            String[] path = new String[ 5 ];
            //String [] path = new String [ 4 ];

            String domain = st.nextToken();
            String family = st.nextToken();
            String member = st.nextToken();
            String name = st.nextToken();

            path[ 0 ] = rootName;
            path[ 1 ] = domain;
            path[ 2 ] = family;
            path[ 3 ] = member;
            path[ 4 ] = name;
            /*path [ 0 ] = domain;
            path [ 1 ] = family;
            path [ 2 ] = member;
            path [ 3 ] = name;*/

            TreePath ret = new TreePath( path );
            return ret;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 8 juil. 2005
     */
    public Attributes getContextAttributes ()
    {
        return attributes;
    }

    /**
     * @param contextAttribute 8 juil. 2005
     */
    public void setContextAttributes ( Attributes contextAttribute )
    {
        this.attributes = contextAttribute;
    }

    /**
     * @param _name
     */
    public Attribute ( String _name )
    {
        name = _name;
    }

    /**
     * @param path
     */
    public Attribute ( TreeNode[] path )
    {
        String _domain = ( String ) path[ 1 ].toString();
        String _family = ( String ) path[ 2 ].toString();
        String _member = ( String ) path[ 3 ].toString();
        String _name = ( String ) path[ 4 ].toString();

        this.domainName = _domain;
        this.familyName = _family;
        this.memberName = _member;
        this.deviceName = _member;
        this.name = _name;
        this.completeName = _domain + GUIUtilities.TANGO_DELIM + _family + GUIUtilities.TANGO_DELIM + _member + GUIUtilities.TANGO_DELIM + _name;
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
    public String getCompleteName ()
    {
        return completeName;
    }

    /**
     * @param completeName 8 juil. 2005
     */
    public void setCompleteName ( String completeName )
    {
        this.completeName = completeName;

        if ( completeName != null )
        {
            StringTokenizer st = new StringTokenizer( completeName , GUIUtilities.TANGO_DELIM );
            String domain_s = st.nextToken();
            String family_s = st.nextToken();
            String member_s = st.nextToken();
            String attr_s = st.nextToken();

            this.setDomain( domain_s );
            this.setFamily( family_s );
            this.setMember( member_s );
            //this.setDevice( member_s );
            this.setDevice( domain_s + GUIUtilities.TANGO_DELIM + family_s + GUIUtilities.TANGO_DELIM + member_s );
            this.setName( attr_s );
        }

    }

    /**
     * @return 8 juil. 2005
     */
    public String getDeviceName ()
    {
        return deviceName;
    }

    /**
     * @param device 8 juil. 2005
     */
    public void setDevice ( String device )
    {
        this.deviceName = device;
    }

    /**
     * @return 8 juil. 2005
     */
    public String getDomainName ()
    {
        return domainName;
    }

    /**
     * @param domain 8 juil. 2005
     */
    public void setDomain ( String domain )
    {
        this.domainName = domain;
    }

    /**
     * @return 8 juil. 2005
     */
    public String getFamilyName ()
    {
        return familyName;
    }

    /**
     * @param family 8 juil. 2005
     */
    public void setFamily ( String family )
    {
        this.familyName = family;
    }

    /**
     * @return 8 juil. 2005
     */
    public String getMemberName ()
    {
        return memberName;
    }

    /**
     * @param member 8 juil. 2005
     */
    public void setMember ( String member )
    {
        this.memberName = member;
    }

    public boolean isArchived ( boolean historic ) throws Exception
    {
    	boolean res = false;
        try
        {
        	IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
        	res = manager.isArchived( this.getCompleteName() , historic );
        }
        catch(Exception e)
        {
        	
        }
        return res; 
        
    }

    public String toString ()
    {
        String ret = "            " + this.getName();

        return ret;
    }

    public String getDescription ()
    {
        return "                " + this.getName();
    }

    /**
     * @return the member
     */
    public Member getMember() {
        return this.member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(Member member) {
        this.member = member;
    }
}
