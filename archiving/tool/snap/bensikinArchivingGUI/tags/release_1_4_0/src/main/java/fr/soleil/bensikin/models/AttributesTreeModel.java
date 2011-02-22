//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/AttributesTreeModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesTreeModel.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: AttributesTreeModel.java,v $
// Revision 1.6  2007/08/24 14:09:12  ounsy
// Context attributes ordering (Mantis bug 3912)
//
// Revision 1.5  2005/12/14 16:41:59  ounsy
// added a removeAll method to clean the tree
//
// Revision 1.4  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.models;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import fr.soleil.bensikin.comparators.BensikinAttributesComparator;
import fr.soleil.bensikin.comparators.BensikinDomainsComparator;
import fr.soleil.bensikin.comparators.BensikinFamiliesComparator;
import fr.soleil.bensikin.comparators.BensikinMembersComparator;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.attributes.Family;
import fr.soleil.bensikin.data.attributes.Member;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.tools.GUIUtilities;

/**
 * The mother class of all tree models of the application.
 * It implements methods to add/get atributes, and keeps track of the current attributes in a Hashtable, which keys are the attributes paths.
 * A Hashtable
 *
 * @author CLAISSE
 */
public class AttributesTreeModel extends DefaultTreeModel
{
    /**
     * The list of Domains for this model
     */
    protected Vector domains;
    /**
     * The attributes TreeMap. Each key is the String representations of an attribute's path, and each value is the corresponding ContextAttribute.
     */
    protected TreeMap attributesHT;

    private static String DEFAULT_ROOT_NAME = "ROOT";
    public static final int CONTEXT_TREE_DEPTH = 5;

    /**
     * Returns a node representing the TANGO_HOST property in the local system.
     *
     * @return A node representing the TANGO_HOST property in the local system
     */
    private static DefaultMutableTreeNode findRootInSystemProperties ()
    {
        String rootName = System.getProperty( "TANGO_HOST" );

        if ( rootName == null )
        {
            rootName = DEFAULT_ROOT_NAME;
        }

        return new DefaultMutableTreeNode( rootName );
    }

    /**
     * Initializes the root and the attributes Hashtable.
     */
    public AttributesTreeModel ()
    {
        super( findRootInSystemProperties() );
        attributesHT = new TreeMap(Collator.getInstance(Locale.FRENCH));
    }


    /**
     * Builds the model for the given Domains:
     * <UL>
     * <LI> Adds a node for each Domain to the root
     * <LI> For each Domain, adds all of its Family nodes
     * <LI> For each Family, adds all of its Members nodes
     * <LI> For each Member, adds all of its Attributes nodes (provided the attributes have been loaded yet)
     * </UL>
     *
     * @param _domains The Domains to display
     */
    public void build ( Vector _domains )
    {
        attributesHT = new TreeMap(Collator.getInstance(Locale.FRENCH));
        domains = _domains;

        if ( _domains == null )
        {
            return;
        }
        Collections.sort( _domains, new BensikinDomainsComparator() );
        
        Enumeration enumDomains = _domains.elements();
        while ( enumDomains.hasMoreElements() )
        {
            //START CURRENT DOMAIN
            Domain domain = ( Domain ) enumDomains.nextElement();
            DefaultMutableTreeNode domainNode = new DefaultMutableTreeNode( domain.getName() );

            ( ( DefaultMutableTreeNode ) ( this.getRoot() ) ).add( domainNode );

            List familiesToSort = new Vector ();
            familiesToSort.addAll ( domain.getFamilies() );
            Collections.sort( familiesToSort, new BensikinFamiliesComparator() );

            Iterator enumFamilies = familiesToSort.iterator ();
            while ( enumFamilies.hasNext() )
            {
                //START CURRENT FAMILY
                Family family = ( Family ) enumFamilies.next();
                DefaultMutableTreeNode familyNode = new DefaultMutableTreeNode( family.getName() );
                domainNode.add( familyNode );

                List membersToSort = new Vector ();
                membersToSort.addAll ( family.getMembers() );
                Collections.sort( membersToSort, new BensikinMembersComparator() );

                Iterator enumMembers = membersToSort.iterator ();
                while ( enumMembers.hasNext() )
                {
                    //START CURRENT MEMBER
                    Member member = ( Member ) enumMembers.next();
                    DefaultMutableTreeNode memberNode = new DefaultMutableTreeNode( member.getName() );
                    familyNode.add( memberNode );

                    Collection coll = member.getAttributes();
                    List attributesToSort = new Vector ();
                    attributesToSort.addAll ( coll );
                    Collections.sort( attributesToSort, new BensikinAttributesComparator() );

                    Iterator enumAttributes = attributesToSort.iterator();
                    while ( enumAttributes.hasNext () )
                    {
                        //START CURRENT ATTRIBUTE
                        ContextAttribute attribute = ( ContextAttribute ) enumAttributes.next();
                        attribute.setCompleteName( domain.getName() + "/" + family.getName() + "/" + member.getName() + "/" + attribute.getName() );
                        attribute.setDomain( domain.getName() );
                        attribute.setFamily( family.getName() );
                        attribute.setMember( member.getName() );
                        attribute.setDevice( member.getName() );

                        DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode( attribute.getName() );
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
    }

    /**
     * Adds a (String representations of path in tree, attribute) key/value couple in the attributes Hashtable.
     *
     * @param path      The attribute's path in the tree
     * @param attribute The attribute to add
     */
    public void addAttribute ( TreeNode[] path , ContextAttribute attribute )
    {
        attributesHT.put( translatePathIntoKey( path ) , attribute );
    }

    /**
     * Returns the attribute located at a given path in the tree.
     *
     * @param key The String representations of the path of the desired attribute
     * @return The attribute at this path in the tree
     */
    public ContextAttribute getAttribute ( String key )
    {
        return ( ContextAttribute ) attributesHT.get( key );
    }

    /**
     * Returns the attributes Hashtable.
     *
     * @return The attributes Hashtable
     */
    protected TreeMap getAttributes ()
    {
        return attributesHT;
    }

    /**
     * Returns an enumeration on all of the current tree attributes.
     *
     * @return An enumeration on all of the current tree attributes
     */
    public Collection getTreeAttributes ()
    {
        return attributesHT.keySet();
    }

    /**
     * Returns the String representation of the given path, usable as key in the attributes Hashtable.
     *
     * @param path The path to convert
     * @return The String representation of the given path
     */
    public static String translatePathIntoKey ( TreeNode[] path )
    {
        String ret = "";

        for ( int i = 1 ; i < path.length ; i++ )
        {
            ret += path[ i ].toString();
            if ( i < path.length - 1 )
            {
                ret += GUIUtilities.TANGO_DELIM;
            }
        }

        return ret;
    }

    /**
     * Returns the current number of attributes.
     *
     * @return The current number of attributes
     */
    public int size ()
    {
        if ( attributesHT == null )
        {
            return 0;
        }
        else
        {
            return attributesHT.size();
        }
    }
    
    /*public void traceAttributesHT ()
    {
        if ( this.size () == 0 )
        {
            return;
        }
        
        Enumeration enum = attributesHT.keys ();
        System.out.println ( "traceAttributesHT------------------" );
        while ( enum.hasMoreElements () )
        {
            String nextKey = (String) enum.nextElement ();
            System.out.println ( "nextKey|"+nextKey+"|" );
        }
        System.out.println ( "traceAttributesHT------------------" );
    }*/
    
    public void removeAll ()
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getRoot ();
        String name = (String) root.getUserObject ();
	    this.attributesHT = new TreeMap(Collator.getInstance(Locale.FRENCH));
	    this.setRoot ( new DefaultMutableTreeNode ( name ) );
    }
}

