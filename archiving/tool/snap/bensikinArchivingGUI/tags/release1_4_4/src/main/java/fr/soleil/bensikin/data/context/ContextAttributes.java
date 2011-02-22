//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/ContextAttributes.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextAttributes.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: ContextAttributes.java,v $
// Revision 1.12  2008/01/08 16:10:24  pierrejoseph
// Pb with the validate cmd in table mode : correction in the remove method
// - see with RGIR
//
// Revision 1.11  2007/08/24 14:09:13  ounsy
// Context attributes ordering (Mantis bug 3912)
//
// Revision 1.10  2007/08/23 15:28:48  ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.9  2007/02/08 14:45:09  ounsy
// corrected a bug in toString() that left out some attributes
//
// Revision 1.8  2006/11/29 10:00:26  ounsy
// minor changes
//
// Revision 1.7  2006/01/12 12:57:39  ounsy
// traces removed
//
// Revision 1.6  2005/12/14 16:33:27  ounsy
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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.soleil.bensikin.comparators.BensikinAttributesComparator;
import fr.soleil.bensikin.comparators.BensikinDomainsComparator;
import fr.soleil.bensikin.components.context.detail.AttributesSelectTable;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.attributes.Family;
import fr.soleil.bensikin.data.attributes.Member;
import fr.soleil.bensikin.models.AttributesSelectTableModel;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.tools.GUIUtilities;

/**
 * Represents the list of attributes attached to a context
 *
 * @author CLAISSE
 */
public class ContextAttributes
{
    private ContextAttribute[] contextAttributes;
    private Context context;

    /**
     * Builds and returns a Vector containing the Domains spanned by these attributes.
     * Each Domain element of the vector is filled with the Domain/Family/Member structure (cf. bensikin.bensikin.data.attributes).
     *
     * @return A Vector containing the Domains of this ContextAttributes.
     */
    public Vector getHierarchy ()
    {
        if ( this.contextAttributes == null )
        {
            return null;
        }

        int nbAttributes = this.contextAttributes.length;

        //Hashtable domains
        TreeMap htDomains = new TreeMap(Collator.getInstance(Locale.FRENCH));
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            ContextAttribute currentAttribute = this.contextAttributes[ i ];
            String currentDomain = currentAttribute.getDomain();
            htDomains.put( currentDomain , new TreeMap(Collator.getInstance(Locale.FRENCH)) );
        }

        //Hashtable families
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            ContextAttribute currentAttribute = this.contextAttributes[ i ];
            String currentDomain = currentAttribute.getDomain();
            String currentFamily = currentAttribute.getFamily();

            TreeMap htCurrentDomain = ( TreeMap ) htDomains.get( currentDomain );
            htCurrentDomain.put( currentFamily , new TreeMap(Collator.getInstance(Locale.FRENCH)) );
        }

        //Hashtable members
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            ContextAttribute currentAttribute = this.contextAttributes[ i ];
            String currentDomain = currentAttribute.getDomain();
            String currentFamily = currentAttribute.getFamily();
            String currentMember = currentAttribute.getMember();

            TreeMap htCurrentDomain = ( TreeMap ) htDomains.get( currentDomain );
            TreeMap htCurrentFamily = ( TreeMap ) htCurrentDomain.get( currentFamily );

            htCurrentFamily.put( currentMember , new Vector() );
        }

        //Vector attributes
        for ( int i = 0 ; i < nbAttributes ; i++ )
        {
            ContextAttribute currentAttribute = this.contextAttributes[ i ];
            String currentDomain = currentAttribute.getDomain();
            String currentFamily = currentAttribute.getFamily();
            String currentMember = currentAttribute.getMember();
            String currentAttributeName = currentAttribute.getName();

            TreeMap htCurrentDomain = ( TreeMap ) htDomains.get( currentDomain );
            TreeMap htCurrentFamily = ( TreeMap ) htCurrentDomain.get( currentFamily );
            Vector veCurrentMember = ( Vector ) htCurrentFamily.get( currentMember );

            veCurrentMember.add( currentAttributeName );
        }

        Vector result = new Vector();

        Iterator enumDomains = htDomains.keySet().iterator();
        while ( enumDomains.hasNext() )
        {
            String nextDomainName = ( String ) enumDomains.next();
            Domain nextDomain = new Domain( nextDomainName );
            result.add( nextDomain );

            TreeMap htFamilies = ( TreeMap ) htDomains.get( nextDomainName );
            Iterator enumFamilies = htFamilies.keySet().iterator();
            while ( enumFamilies.hasNext() )
            {
                String nextFamilyName = ( String ) enumFamilies.next();
                Family nextFamily = new Family( nextFamilyName );
                nextDomain.addFamily( nextFamily );

                TreeMap htMembers = ( TreeMap ) htFamilies.get( nextFamilyName );
                Iterator enumMembers = htMembers.keySet().iterator();
                while ( enumMembers.hasNext() )
                {
                    String nextMemberName = ( String ) enumMembers.next();
                    Member nextMember = new Member( nextMemberName );
                    nextFamily.addMember( nextMember );

                    Vector vectAttr = ( Vector ) htMembers.get( nextMemberName );
                    Enumeration enumAttr = vectAttr.elements();
                    while ( enumAttr.hasMoreElements() )
                    {
                        String nextAttrName = ( String ) enumAttr.nextElement();
                        ContextAttribute nextAttr = new ContextAttribute( nextAttrName );

                        nextAttr.setDomain( nextDomainName );
                        nextAttr.setMember( nextMemberName );
                        nextAttr.setFamily( nextFamilyName );
                        nextAttr.setDevice( nextMemberName );
                        nextAttr.setCompleteName(
                                nextDomainName + GUIUtilities.TANGO_DELIM
                                  + nextFamilyName + GUIUtilities.TANGO_DELIM
                                  + nextMemberName + GUIUtilities.TANGO_DELIM
                                  + nextAttrName
                        );

                        nextMember.addAttribute( nextAttr );
                    }
                    Vector attrs = nextMember.getAttributes();
                    Collections.sort( attrs, new BensikinAttributesComparator() );
                }
            }
        }

        Collections.sort( result, new BensikinDomainsComparator() );
        result.trimToSize();
        return result;
    }

    /**
     * Builds a ContextAttribute with a reference to its Context.
     *
     * @param _context The Context this attributes belong to
     */
    public ContextAttributes ( Context _context )
    {
        this.context = _context;
    }

    /**
     * @param _context           The Context this attributes belong to
     * @param _contextAttributes The list of attributes
     */
    public ContextAttributes ( Context _context , ContextAttribute[] _contextAttributes )
    {
        this.context = _context;
        this.contextAttributes = _contextAttributes;
    }


    /**
     * Returns a XML representation of the attributes.
     *
     * @return a XML representation of the attributes
     */
    public String toString ()
    {
        String ret = "";
        
        if ( this.contextAttributes != null )
        {
            for ( int i = 0 ; i < this.contextAttributes.length ; i++ )
            {
                ContextAttribute nextValue = this.contextAttributes[ i ];
                ret += nextValue.toString();
                if ( i < contextAttributes.length - 1 )
                {
                    ret += GUIUtilities.CRLF;
                }
            }
        }

        return ret;
    }

    public void appendUserFriendlyString (StringBuffer buffer) {
        if ( buffer != null ) {
            if ( this.contextAttributes != null ) {
                for (int i = 0; i < this.contextAttributes.length; i++) {
                    ContextAttribute nextValue = this.contextAttributes[i];
                    nextValue.appendUserFriendlyString(buffer);
                    if (i < contextAttributes.length - 1) {
                        buffer.append( GUIUtilities.CRLF );
                    }
                }
            }
        }
    }

    /**
     * Visibly make these attributes the current context's attributes
     */
    public void push ()
    {
        ContextDetailPanel contextDetailPanel = ContextDetailPanel.getInstance ();
        boolean isAlternate = contextDetailPanel.isAlternateSelectionMode ();
        
        if ( isAlternate )
        {
            //System.out.println ( "ContextAttributes/push/isAlternate" );
            
            AttributesSelectTableModel tableModel = AttributesSelectTableModel.forceReset ();
            
            tableModel.reset ();
            tableModel.setRows ( this.getContextAttributes () , true );
            
            AttributesSelectTable table = AttributesSelectTable.getInstance ();
            table.setModel ( tableModel );
            tableModel.fireTableDataChanged ();
            
            contextDetailPanel.validate();
            contextDetailPanel.repaint ();
        }
        else
        {
            Vector newAttributes = this.getHierarchy();
            //ContextAttributesTree tree = ContextAttributesTree.getInstance();

            ContextAttributesTreeModel model = ContextAttributesTreeModel.getInstance ( false );
            DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) model.getRoot();
            model.setRoot( new DefaultMutableTreeNode( root.getUserObject() ) );
            model.build( newAttributes );
            model.reload();
        }
    }

    /**
     * @return Returns the context.
     */
    public Context getContext ()
    {
        return context;
    }

    /**
     * @param context The context to set.
     */
    public void setContext ( Context context )
    {
        this.context = context;
    }

    /**
     * @return Returns the contextAttributes.
     */
    public ContextAttribute[] getContextAttributes ()
    {
        return contextAttributes;
    }

    /**
     * @param contextAttributes The contextAttributes to set.
     */
    public void setContextAttributes ( ContextAttribute[] contextAttributes )
    {
        this.contextAttributes = contextAttributes;
    }
    
    public void removeAttributesNotInList ( TreeMap attrs )
    {
        //ContextAttribute[] contextAttributes;
        if ( this.contextAttributes == null || this.contextAttributes.length == 0 )
        {
            return;
        }
        TreeMap ht = new TreeMap(Collator.getInstance(Locale.FRENCH));
        for ( int i = 0 ; i < this.contextAttributes.length ; i ++ )
        {
            ht.put ( this.contextAttributes [ i ].getCompleteName () , this.contextAttributes [ i ] );
        }
        
        //---
        Iterator enumer = ht.keySet().iterator();
        ArrayList toRemove = new ArrayList();
        while ( enumer.hasNext() )
        {
            String next = ( String ) enumer.next();
            if ( !attrs.containsKey( next ) )
            {
            	toRemove.add(next);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
        	ht.remove( toRemove.get(i) );
        }
        toRemove.clear();
        toRemove = null;
        //---

        ContextAttribute[] result = new ContextAttribute [ ht.size () ];
        enumer = ht.keySet().iterator();
        int i = 0;
        while ( enumer.hasNext() )
        {
            String key = ( String ) enumer.next();
            ContextAttribute val = (ContextAttribute) ht.get ( key );
            result [ i ] = val;
            i++;
        }
        
        
        this.contextAttributes = result;
    }
}
