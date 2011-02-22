//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/impl/DummyTangoManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyTangoManagerImpl.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: DummyTangoManagerImpl.java,v $
// Revision 1.5  2006/11/29 10:02:17  ounsy
// package refactoring
//
// Revision 1.1  2005/12/14 16:56:05  ounsy
// has been renamed
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.impl;

import java.util.ArrayList;
import java.util.Vector;

import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.datasources.tango.ITangoManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;


/**
 * A dummy implementation, returns hard-coded values.
 *
 * @author CLAISSE
 */
public class DummyTangoManagerImpl implements ITangoManager
{
    /* (non-Javadoc)
     * @see bensikin.bensikin.impl.ITangoManager#loadDomains(java.lang.String)
     */
    public Vector loadDomains ( Criterions searchCriterions )
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

        Vector val = new Vector();

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

        ContextAttribute float_scalar = new ContextAttribute( "float_scalar" );
        ContextAttribute short_scalar_ro = new ContextAttribute( "short_scalar_ro" );
        ContextAttribute short_scalar = new ContextAttribute( "short_scalar" );
        ContextAttribute long_scalar = new ContextAttribute( "long_scalar" );
        ContextAttribute double_scalar = new ContextAttribute( "double_scalar" );
        ContextAttribute string_scalar = new ContextAttribute( "string_scalar" );
        ContextAttribute float_spectrum = new ContextAttribute( "float_spectrum" );
        ContextAttribute short_spectrum = new ContextAttribute( "short_spectrum" );
        ContextAttribute short_image = new ContextAttribute( "short_image" );
        ContextAttribute float_image = new ContextAttribute( "float_image" );

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

    /* (non-Javadoc)
     * @see bensikin.bensikin.impl.ITangoManager#dbGetAttributeList(java.lang.String)
     */
    public ArrayList dbGetAttributeList ( String device_name )
    {
        // TODO Auto-generated method stub
        return null;
    }

}
