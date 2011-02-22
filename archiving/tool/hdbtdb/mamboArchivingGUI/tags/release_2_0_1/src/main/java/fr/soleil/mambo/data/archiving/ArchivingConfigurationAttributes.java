//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttributes.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationAttributes.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.12 $
//
// $Log: ArchivingConfigurationAttributes.java,v $
// Revision 1.12  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.11  2006/11/07 14:34:19  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.10  2006/08/23 10:02:46  ounsy
// avoided a concurrent modification exception
//
// Revision 1.9  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.8  2006/07/24 07:37:51  ounsy
// image support with partial loading
//
// Revision 1.7  2006/07/18 10:26:42  ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.6  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.5  2006/05/16 12:05:28  ounsy
// minor changes
//
// Revision 1.4  2006/04/10 09:15:00  ounsy
// optimisation on loading an AC
//
// Revision 1.3  2005/12/15 11:34:39  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.archiving;

import java.text.Collator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import fr.soleil.mambo.data.attributes.Attributes;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.Family;
import fr.soleil.mambo.data.attributes.Member;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.models.AttributesSelectTableModel;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.GUIUtilities;

public class ArchivingConfigurationAttributes extends Attributes
{
    //private 
    private TreeMap attributes;
    //private ArchivingConfigurationAttribute[] list;

    private ArchivingConfiguration archivingConfiguration;

    public ArchivingConfigurationAttributes ()
    {
        attributes = new TreeMap(Collator.getInstance(Locale.FRENCH));
    }

    /**
     * @param _attributes
     */
    public ArchivingConfigurationAttributes ( ArchivingConfigurationAttribute[] _attributes )
    {
        super( _attributes );

        if ( _attributes != null )
        {
            for ( int i = 0 ; i < _attributes.length ; i++ )
            {
                if ( _attributes[ i ] != null )
                {
                    _attributes[ i ].setGroup( this );
                }
            }
        }
    }

    public boolean equals ( ArchivingConfigurationAttributes this2 )
    {
        return this.attributes.size() == this2.attributes.size();
    }

    public void addAttribute ( ArchivingConfigurationAttribute _attribute )
    {
        _attribute.setGroup( this );
        String completeName = _attribute.getCompleteName();
        if ( completeName.endsWith( "/" ) )
        {
            completeName = completeName.substring( 0 , completeName.length() - 1 );
        }
        attributes.put( completeName , _attribute );
    }

    public ArchivingConfigurationAttribute getAttribute ( String completeName )
    {
        return ( ArchivingConfigurationAttribute ) attributes.get( completeName );
    }


    public String toString ()
    {
        String ret = "";

        if ( attributes != null )
        {
            Set keySet = attributes.keySet();
            Iterator keyIterator = keySet.iterator();
            int i = 0;
            while ( keyIterator.hasNext() )
            {
                String nextKey = ( String ) keyIterator.next();
                ArchivingConfigurationAttribute nextValue = ( ArchivingConfigurationAttribute ) attributes.get( nextKey );

                ret += nextValue.toString();
                if ( i < attributes.size() - 1 )
                {
                    ret += GUIUtilities.CRLF;
                }

                i++;
            }
        }

        return ret;
    }


    /**
     * @return Returns the archivingConfiguration.
     */
    public ArchivingConfiguration getArchivingConfiguration ()
    {
        return archivingConfiguration;
    }

    /**
     * @param archivingConfiguration The archivingConfiguration to set.
     */
    public void setArchivingConfiguration ( ArchivingConfiguration archivingConfiguration )
    {
        this.archivingConfiguration = archivingConfiguration;
    }

    /**
     * @return Returns the attributes.
     */
    public TreeMap getAttributes ()
    {
        return attributes;
    }

    public ArchivingConfigurationAttribute[] getAttributesList ()
    {
        ArchivingConfigurationAttribute[] ret = new ArchivingConfigurationAttribute[ attributes.size() ];
        Set keySet = attributes.keySet();
        Iterator keyIterator = keySet.iterator();
        int i = 0;
        while ( keyIterator.hasNext() )
        {
            String nextKey = ( String ) keyIterator.next();
            ArchivingConfigurationAttribute nextValue = ( ArchivingConfigurationAttribute ) attributes.get( nextKey );
            ret[ i ] = nextValue;
            i++;
        }
        return ret;
    }

    /**
     * 30 août 2005
     */
    

     /*public void push ()
     //ORIGINAL PUSH METHOD
    {
        Hashtable attributesHash = this.getAttributes();
        if ( attributesHash == null )
        {
            return;
        }

        Enumeration enum = attributesHash.keys();
        Vector _domains = new Vector();

        ArchivingConfigurationAttribute[] list = new ArchivingConfigurationAttribute[ attributesHash.size() ];
        int i = 0;

        while ( enum.hasMoreElements() )
        {
            String completeName = ( String ) enum.nextElement();
            ArchivingConfigurationAttribute next = ( ArchivingConfigurationAttribute ) attributesHash.get( completeName );
            String domain_s = next.getDomain();
            String family_s = next.getFamily();
            String member_s = next.getMember();
            String attr_s = next.getName();
            String cmpltNam = next.getCompleteName();

            ArchivingConfigurationAttribute attr = new ArchivingConfigurationAttribute();
            attr.setName( attr_s );
            attr.setCompleteName( completeName );
            attr.setDevice( member_s );
            attr.setDomain( domain_s );
            attr.setFamily( family_s );

            list[ i ] = attr;

            Domain domain = new Domain( domain_s );
            Family family = new Family( family_s );
            Member member = new Member( member_s );

            Domain dom = Domain.hasDomain( _domains , domain_s );
            if ( dom != null )
            {
                Family fam = dom.hasFamily( family_s );
                if ( fam != null )
                {
                    Member mem = fam.hasMember( member_s );
                    if ( mem != null )
                    {
                        mem.addAttribute( attr );
                        fam.addMember( mem );
                        dom.addFamily( fam );
                        //_domains.add ( dom );
                        _domains = Domain.addDomain( _domains , dom );
                    }
                    else
                    {
                        member.addAttribute( attr );
                        fam.addMember( member );
                        dom.addFamily( fam );
                        //_domains.add ( dom );
                        _domains = Domain.addDomain( _domains , dom );
                    }
                }
                else
                {
                    member.addAttribute( attr );
                    family.addMember( member );
                    dom.addFamily( family );
                    //_domains.add ( dom );
                    _domains = Domain.addDomain( _domains , dom );
                }
            }
            else
            {
                member.addAttribute( attr );
                family.addMember( member );
                domain.addFamily( family );
                //_domains.add ( domain );
                _domains = Domain.addDomain( _domains , domain );
            }

            i++;
        }
        ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
        model.build( _domains );
        model.reload();

        //----------
        ACAttributesRecapTree recapTree = ACAttributesRecapTree.getInstance();
        if ( recapTree != null )
        {
            recapTree.expandAll( true );
        }
        ACAttributesPropertiesTree propTree = ACAttributesPropertiesTree.getInstance();
        if ( propTree != null )
        {
            propTree.expandAll( true );
        }
        //----------

        AttributesSelectTableModel tableModel = AttributesSelectTableModel.getInstance();
        tableModel.setRows( list );
    }*/

    public void push ()
    {
        if ( this.getAttributes () == null )
        {
            return;
        }
        
        Options options = Options.getInstance ();
        ACOptions acOptions = options.getAcOptions ();
        if ( acOptions.isAlternateSelectionMode () )
        {
	        ArchivingConfigurationAttribute[] list = this.getListCLA ( this.getAttributes () );
	        this.reloadTableModelCLA ( list );
            //this.reloadTableModelCLA ( this.getList () );
        }
        else
        {
            Vector _domains = this.getDomainsCLA ( this.getAttributes () );
            this.reloadTreeModelCLA ( _domains );
        }
        
        //this.expandTreesCLA ();
    }
    
    /*
    private void expandTreesCLA() 
    {
//      ----------
        ACAttributesRecapTree recapTree = ACAttributesRecapTree.getInstance();
        if ( recapTree != null )
        {
            recapTree.expandAll( true );
        }
        ACAttributesPropertiesTree propTree = ACAttributesPropertiesTree.getInstance();
        if ( propTree != null )
        {
            propTree.expandAll( true );
        }
        //----------
    
    }*/

    /**
     * @param list
     */
    private void reloadTableModelCLA(ArchivingConfigurationAttribute[] list) 
    {
        AttributesSelectTableModel tableModel = AttributesSelectTableModel.getInstance();
        tableModel.setRows( list );    
    }

    /**
     * @param _domains
     */
    private void reloadTreeModelCLA(Vector _domains) 
    {
        ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
        model.build( _domains );
        model.reload();
    }

    /**
     * @param attributes2
     * @return
     */
    private Vector getDomainsCLA(TreeMap attributesHash) 
    {
        Set keySet = attributesHash.keySet();
        Iterator keyIterator = keySet.iterator();
        Vector _domains = new Vector();
        ArchivingConfigurationAttribute[] _list = new ArchivingConfigurationAttribute [ attributesHash.size() ];
        int i = 0;
        
        while ( keyIterator.hasNext() )
        {
            String completeName = ( String ) keyIterator.next();
            ArchivingConfigurationAttribute next = ( ArchivingConfigurationAttribute ) attributesHash.get( completeName );
            _list [ i ] = next;
            i++;
            
            String domain_s = next.getDomainName();
            String family_s = next.getFamilyName();
            String member_s = next.getMemberName();
            //String attr_s = next.getName();
            //String cmpltNam = next.getCompleteName();

            Domain domain = new Domain( domain_s );
            Family family = new Family( family_s );
            Member member = new Member( member_s );

            Domain dom = Domain.hasDomain( _domains , domain_s );
            if ( dom != null )
            {
                Family fam = dom.getFamily( family_s );
                if ( fam != null )
                {
                    Member mem = fam.getMember( member_s );
                    if ( mem != null )
                    {
                        mem.addAttribute( next );
                        fam.addMember( mem );
                        dom.addFamily( fam );
                        //_domains.add ( dom );
                        _domains = Domain.addDomain( _domains , dom );
                    }
                    else
                    {
                        member.addAttribute( next );
                        fam.addMember( member );
                        dom.addFamily( fam );
                        //_domains.add ( dom );
                        _domains = Domain.addDomain( _domains , dom );
                    }
                }
                else
                {
                    member.addAttribute( next );
                    family.addMember( member );
                    dom.addFamily( family );
                    //_domains.add ( dom );
                    _domains = Domain.addDomain( _domains , dom );
                }
            }
            else
            {
                member.addAttribute( next );
                family.addMember( member );
                domain.addFamily( family );
                //_domains.add ( domain );
                _domains = Domain.addDomain( _domains , domain );
            }
        }
        
        //this.setList ( _list );
        return _domains;
    }

    /**
     * @return
     */
    private ArchivingConfigurationAttribute[] getListCLA ( TreeMap attributesHash ) 
    {
        ArchivingConfigurationAttribute[] list = new ArchivingConfigurationAttribute[ attributesHash.size() ];
        int i = 0;
        Set keySet = attributesHash.keySet();
        Iterator keyIterator = keySet.iterator();
        
        while ( keyIterator.hasNext() )
        {
            String completeName = ( String ) keyIterator.next();
            ArchivingConfigurationAttribute next = ( ArchivingConfigurationAttribute ) attributesHash.get( completeName );
            /*String domain_s = next.getDomain();
            String family_s = next.getFamily();
            String member_s = next.getMember();
            String attr_s = next.getName();
            String cmpltNam = next.getCompleteName();

            ArchivingConfigurationAttribute attr = new ArchivingConfigurationAttribute();
            attr.setName( attr_s );
            attr.setCompleteName( completeName );
            attr.setDevice( member_s );
            attr.setDomain( domain_s );
            attr.setFamily( family_s );

            list[ i ] = attr;*/
            list[ i ] = next;

            i++;
        }
        return list;
    }

    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes ( TreeMap attributes )
    {
        this.attributes = attributes;
    }

    /**
     * @param attrs 1 sept. 2005
     */
    public void removeAttributesNotInList ( TreeMap attrs )
    {
        /*Enumeration enum1 = attrs.keys ();
        while ( enum1.hasMoreElements () )
        {
            String next = (String) enum1.nextElement ();
            System.out.println ( "removeAttributesNotInList/next 1|"+next+"|" );
        }
        //---
          */
        Set keySet = this.attributes.keySet();
        Set toRemoveSet = new HashSet();
        Iterator keyIterator = keySet.iterator();
        while ( keyIterator.hasNext() )
        {
            String next = ( String ) keyIterator.next();
            //System.out.println ( "removeAttributesNotInList/next|"+next+"|" );
            if ( !attrs.containsKey( next ) )
            {
                //  System.out.println ( "remove !!!!!" );
                //this.attributes.remove( next );
                toRemoveSet.add(next);
            }
        }
        Iterator toRemoveIterator = toRemoveSet.iterator();
        while (toRemoveIterator.hasNext())
        {
            this.attributes.remove( toRemoveIterator.next() );
        }

        //while
    }

    /**
     * @return 9 sept. 2005
     */
    public void controlValues () throws ArchivingConfigurationException
    {
        ArchivingConfigurationAttribute[] list = this.getAttributesList();
        if ( list == null || list.length == 0 )
        {
            throw new ArchivingConfigurationException( null , ArchivingConfiguration.EMPTY_ATTRIBUTES );
        }

        int lg = list.length;
        int ret = -1;
        for ( int i = 0 ; i < lg ; i++ )
        {
            ArchivingConfigurationAttribute attr = list[ i ];
            attr.controlValues();
            if ( ret != -1 )
            {
                break;
            }
        }

    }

    /**
     * @return
     */
    public ArchivingConfigurationAttributes cloneAttrs() 
    {
        ArchivingConfigurationAttributes ret = new ArchivingConfigurationAttributes ();
        
        ret.setAttributes ( (TreeMap) this.getAttributes().clone() );
        
        return ret;
    }
    

    /**
     * @return
     */
    /*public boolean containsNonSetAttributes() 
    {
        if ( attributes == null )
        {
            return false;
        }
        
        Enumeration enum = attributes.keys ();
        while ( enum.hasMoreElements () )
        {
            String nextKey = (String) enum.nextElement ();
            ArchivingConfigurationAttribute nextValue = (ArchivingConfigurationAttribute) attributes.get ( nextKey );
            
            if ( nextValue.isEmpty () )
            {
                return true;
            }
        }
        return false;
    }*/
    /**
     * @return Returns the list.
     */
    /*private ArchivingConfigurationAttribute[] getList() {
        return list;
    }*/
    /**
     * @param list The list to set.
     */
    /*private void setList(ArchivingConfigurationAttribute[] list) {
        this.list = list;
    }*/
}
