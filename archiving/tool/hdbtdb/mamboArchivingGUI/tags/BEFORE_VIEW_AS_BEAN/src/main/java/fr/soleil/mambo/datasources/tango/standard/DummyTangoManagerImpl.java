//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/tango/standard/DummyTangoManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyTangoManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: DummyTangoManagerImpl.java,v $
// Revision 1.1  2006/09/20 12:47:45  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.5  2006/09/14 11:29:29  ounsy
// complete buffering refactoring
//
// Revision 1.4  2006/07/18 10:29:25  ounsy
// possibility to reload tango attributes
//
// Revision 1.3  2006/06/15 15:41:58  ounsy
// added a findArchivers method
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
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
package fr.soleil.mambo.datasources.tango.standard;

import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.tango.util.entity.data.Attribute;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;


public class DummyTangoManagerImpl implements ITangoManager
{
    /* (non-Javadoc)
     * @see mambo.datasources.tango.ITangoManager#loadDomains(java.lang.String, boolean)
     */
    public Vector<Domain> loadDomains ( Criterions searchCriterions, boolean forceReload )
    {
        /*Properties prop = System.getProperties ();
        Enumeration enum = prop.keys ();
        GUIUtilities.trace ( 9 , "-------------------------------------------------------------" );
        while ( enum.hasMoreElements () )
        {
            String nextKey = (String) enum.nextElement ();
            String nextValue = (String) prop.get ( nextKey );
            GUIUtilities.trace ( 9 , nextKey + " = " + nextValue );
        }
        GUIUtilities.trace ( 9 , "-------------------------------------------------------------" );*/

        Vector<Domain> val = new Vector<Domain>();

        Domain archiving = new Domain( "archiving" );
        Domain dserver = new Domain( "dserver" );
        Domain sys = new Domain( "sys" );
        Domain tango = new Domain( "tango" );

        val.add( archiving );
        val.add( dserver );
        val.add( sys );
        val.add( tango );

        Family tangotest = new Family( "tangotest" );
        tango.addFamily( tangotest );

        Member n1 = new Member( "1" );
        tangotest.addMember( n1 );

        Attribute float_scalar = new Attribute( "float_scalar" );
        Attribute short_scalar_ro = new Attribute( "short_scalar_ro" );
        Attribute short_scalar = new Attribute( "short_scalar" );
        Attribute long_scalar = new Attribute( "long_scalar" );
        Attribute double_scalar = new Attribute( "double_scalar" );
        Attribute string_scalar = new Attribute( "string_scalar" );
        Attribute float_spectrum = new Attribute( "float_spectrum" );
        Attribute short_spectrum = new Attribute( "short_spectrum" );
        Attribute short_image = new Attribute( "short_image" );
        Attribute float_image = new Attribute( "float_image" );

        n1.addAttribute( float_scalar );
        n1.addAttribute( short_scalar_ro );
        n1.addAttribute( short_scalar );
        n1.addAttribute( long_scalar );
        n1.addAttribute( double_scalar );
        n1.addAttribute( string_scalar );
        n1.addAttribute( float_spectrum );
        n1.addAttribute( short_spectrum );
        n1.addAttribute( short_image );
        n1.addAttribute( float_image );

        return val;
    }

    public Archiver[] findArchivers(boolean historic) {
        // TODO Auto-generated method stub
        return null;
    }

    public void loadDServers() {
        // TODO Auto-generated method stub
        
    }

    /*public Hashtable getDedicatedAttributesToArchiver(boolean historic, boolean refreshArchivers) throws ArchivingException {
        // TODO Auto-generated method stub
        return null;
    }*/

}
