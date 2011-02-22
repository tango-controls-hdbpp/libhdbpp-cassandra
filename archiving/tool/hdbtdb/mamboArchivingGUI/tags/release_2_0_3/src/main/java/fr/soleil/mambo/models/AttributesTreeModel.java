//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/AttributesTreeModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesTreeModel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: AttributesTreeModel.java,v $
// Revision 1.8  2006/11/29 09:27:40  ounsy
// added a sort on domains in build()
//
// Revision 1.7  2006/11/20 09:38:18  ounsy
// minor changes
//
// Revision 1.6  2006/11/09 16:22:53  ounsy
// corrected an attributes ordering bug
//
// Revision 1.5  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.4  2006/11/07 14:34:28  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.3  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.2  2005/11/29 18:27:08  chinkumo
// no message
//
// Revision 1.1.2.4  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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
package fr.soleil.mambo.models;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import fr.soleil.mambo.comparators.MamboAttributesComparator;
import fr.soleil.mambo.comparators.MamboDomainsComparator;
import fr.soleil.mambo.comparators.MamboFamiliesComparator;
import fr.soleil.mambo.comparators.MamboMembersComparator;
import fr.soleil.mambo.data.attributes.Attribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.Family;
import fr.soleil.mambo.data.attributes.Member;



public class AttributesTreeModel extends DefaultTreeModel
{
    protected Vector domains;
    protected TreeMap attributesHT;

    private static String DEFAULT_ROOT_NAME = "ROOT";
    public static final int CONTEXT_TREE_DEPTH = 5;

    /**
     * @return 8 juil. 2005
     */
    public static DefaultMutableTreeNode findRootInSystemProperties ()
    {
        String rootName = System.getProperty( "TANGO_HOST" );

        if ( rootName == null )
        {
            rootName = DEFAULT_ROOT_NAME;
        }

        return new DefaultMutableTreeNode( rootName );
    }

    /**
     * 
     */
    public AttributesTreeModel ()
    {
        super( findRootInSystemProperties() );
        attributesHT = new TreeMap(Collator.getInstance(Locale.FRENCH));
    }


    /**
     * @param _domains 8 juil. 2005
     */
    public void build ( Vector _domains )
    {
        attributesHT = new TreeMap(Collator.getInstance(Locale.FRENCH));
        domains = _domains;
        
        if ( _domains == null )
        {
            return;
        }
        Collections.sort( _domains, new MamboDomainsComparator() );
        
        Enumeration enumDomains = _domains.elements();
        while ( enumDomains.hasMoreElements() )
        {
            //START CURRENT DOMAIN
            Domain domain = ( Domain ) enumDomains.nextElement();
            DefaultMutableTreeNode domainNode = new DefaultMutableTreeNode( domain.getName() );
         
            ( ( DefaultMutableTreeNode ) ( this.getRoot() ) ).add( domainNode );

            List familiesToSort = new Vector ();
            familiesToSort.addAll ( domain.getFamilies().values() );
            Collections.sort( familiesToSort, new MamboFamiliesComparator() );
            
            Iterator enumFamilies = familiesToSort.iterator ();
            while ( enumFamilies.hasNext() )
            {
                //START CURRENT FAMILY
                Family family = ( Family ) enumFamilies.next();
                DefaultMutableTreeNode familyNode = new DefaultMutableTreeNode( family.getName() );
                domainNode.add( familyNode );

                List membersToSort = new Vector ();
                membersToSort.addAll ( family.getMembers().values() );
                Collections.sort( membersToSort, new MamboMembersComparator() );

                Iterator enumMembers = membersToSort.iterator ();
                while ( enumMembers.hasNext() )
                {
                    //START CURRENT MEMBER
                    Member member = ( Member ) enumMembers.next();
                    DefaultMutableTreeNode memberNode = new DefaultMutableTreeNode( member.getName() );
                    familyNode.add( memberNode );

                    Collection coll = member.getAttributes().values();
                    List attributesToSort = new Vector ();
                    attributesToSort.addAll ( coll );
                    Collections.sort( attributesToSort, new MamboAttributesComparator() );
                    
                    Iterator enumAttributes = attributesToSort.iterator();
                    while ( enumAttributes.hasNext () )
                    {
                        //START CURRENT ATTRIBUTE
                        Attribute attribute = ( Attribute ) enumAttributes.next ();
                        //--
                        attribute.setCompleteName( domain.getName() + "/" + family.getName() + "/" + member.getName() + "/" + attribute.getName() );
                        attribute.setDomain( domain.getName() );
                        attribute.setFamily( family.getName() );
                        attribute.setMember( member.getName() );
                        attribute.setDevice( member.getName() );
                        //--
                        DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode( attribute.getName() );
                        //System.out.println ( "AttributesTreeModel/build/attribute.getName()/"+attribute.getName()+"/" );
                        memberNode.add( attributeNode );
                        this.addAttribute( attributeNode.getPath() , attribute );
                        //END CURRENT ATTRIBUTE
                    }
                    
                    //END CURRENT MEMBER
                }
                //END CURRENT FAMILY
            }
            //END CURRENT DOMAIN
        }
        //System.out.println ( "AttributesTreeModel/build/attributesHT.size()/"+attributesHT.size() );
    }

    /**
     * @param attribute 29 juin 2005
     */
    public void addAttribute ( TreeNode[] path , Attribute attribute )
    {
        attributesHT.put( translatePathIntoKey( path ) , attribute );
    }

    /**
     * @return 8 juil. 2005
     */
    protected TreeMap getAttributes ()
    {
        return attributesHT;
    }

    /**
     * @return 8 juil. 2005
     */
    public Collection getTreeAttributes ()
    {
        return attributesHT.values();
    }

    /**
     * @param path
     * @return 8 juil. 2005
     */
    public static String translatePathIntoKey ( TreeNode[] path )
    {
        String ret = "";

        for ( int i = 1 ; i < path.length ; i++ )
        {
            ret += path[ i ].toString();
            if ( i < path.length - 1 )
            {
                ret += "/";
            }
        }

        return ret;
    }

    public void removeAll ()
    {
        /*
modified 14/09/05
         * DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getRoot ();
	    int numberOfRootChildren = root.getChildCount ();
	    
	    for ( int i = 0 ; i < numberOfRootChildren ; i ++ )
	    {
	        if ( root.getChildCount () == 0 )
	        {
	            break;
	        }
	        
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt ( i );
	        
	        try
	        {
	            this.removeNodeFromParent( node );    
	        }
	        catch ( Exception e )
	        {
	            //do nothing if already empty
	    }
	    	
	        
	    }*/

        //super.domains = new Vector ();
        DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) this.getRoot();
        String name = ( String ) root.getUserObject();
        this.attributesHT = new TreeMap();
        this.setRoot( new DefaultMutableTreeNode( name ) );
    }

    public void traceHtAttr ()
    {
        /*Hashtable htAttr = this.getAttributes ();
        int length = ( htAttr == null ) ? 0 :  htAttr.size ();
        //System.out.println ( "ACAttributesTreeModel/traceHtAttr/START/length/"+length+"/" );
       
        Enumeration enum = htAttr.keys ();
        System.out.println ( "ACAttributesTreeModel/traceHtAttr-------------------------IN------" );
        while ( enum.hasMoreElements () )
        {
            Object nextKey = enum.nextElement ();
            System.out.println ( "ACAttributesTreeModel/traceHtAttr/nextKey|"+nextKey.toString()+"|" );
        }
        System.out.println ( "ACAttributesTreeModel/traceHtAttr-------------------------OUT------" );*/
    }

    /**
     * @param string 14 sept. 2005
     */
    public void removeFromDomains ( String string )
    {
        //System.out.println ( "removeFromDomains/string/"+string+"/" );
        if ( string == null || string.equals( "" ) )
        {
            return;
        }
        try
        {
            StringTokenizer st = new StringTokenizer( string , "/" );
            String dom_s = st.nextToken();
            String fam_s = st.nextToken();
            String mem_s = st.nextToken();
            String attr_s = st.nextToken();
            //System.out.println ( "removeFromDomains/dom_s/"+dom_s+"/fam_s/"+fam_s+"/mem_s/"+mem_s+"/attr_s/"+attr_s+"/" );

            Domain dom = Domain.hasDomain( this.domains , dom_s );
            Family fam = dom.getFamily( fam_s );
            Member mem = fam.getMember( mem_s );
            mem.removeAttribute( attr_s );

            //System.out.println ( "removeFromDomains---------------");
            //Domain.traceDomains ( this.domains );
            //System.out.println ( "removeFromDomains---------------");
        }
        catch ( Exception e )
        {
            // do nothing
        }
    }

    /**
     * @param _attribute 14 sept. 2005
     */
    public void addToDomains ( Attribute _attribute )
    {
        String dom_s = _attribute.getDomainName();
        String fam_s = _attribute.getFamilyName();
        String mem_s = _attribute.getMemberName();
        String attr_s = _attribute.getName();
        //System.out.println ( "addToDomains/dom_s/"+dom_s+"/fam_s/"+fam_s+"/mem_s/"+mem_s+"/attr_s/"+attr_s+"/" );
        
        Domain dom = Domain.hasDomain( this.domains , dom_s );
        if ( dom == null )
        {
            dom = new Domain( dom_s );
            Family fam = new Family( fam_s );
            Member mem = new Member( mem_s );

            mem.addAttribute( _attribute );
            fam.addMember( mem );
            dom.addFamily( fam );

            this.domains.add( dom );
        }
        else
        {
            Family fam = dom.getFamily( fam_s );
            if ( fam == null )
            {
                fam = new Family( fam_s );
                Member mem = new Member( mem_s );

                mem.addAttribute( _attribute );
                fam.addMember( mem );
                dom.addFamily( fam );
            }
            else
            {
                Member mem = fam.getMember( mem_s );
                if ( mem == null )
                {
                    mem = new Member( mem_s );

                    mem.addAttribute( _attribute );
                    fam.addMember( mem );
                }
                else
                {
                    Attribute att = mem.getAttribute( attr_s );
                    if ( att == null )
                    {
                        mem.addAttribute( _attribute );
                    }
                    else
                    {
                        //nothing to do
                    }
                }
            }
        }
        
        //System.out.println ( "addToDomains---------------");
        //Domain.traceDomains ( this.domains );
        //System.out.println ( "addToDomains---------------");
    }
}
