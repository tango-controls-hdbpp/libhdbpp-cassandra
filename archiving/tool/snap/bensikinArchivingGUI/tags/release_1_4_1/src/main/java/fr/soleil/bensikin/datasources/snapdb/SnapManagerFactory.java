//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/snapdb/SnapManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapManagerFactory.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: SnapManagerFactory.java,v $
// Revision 1.3  2007/03/14 15:49:11  ounsy
// the user/password is no longer hard-coded in the APIs, Bensikin takes two more parameters
//
// Revision 1.2  2006/11/29 10:02:17  ounsy
// package refactoring
//
// Revision 1.1  2005/12/14 16:55:28  ounsy
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
package fr.soleil.bensikin.datasources.snapdb;

import fr.soleil.bensikin.impl.DummySnapManagerImpl;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapManagerApi.SnapManagerImpl;

/**
 * A factory used to instantiate ISnapManager of 2 types: DUMMY_IMPL_TYPE or REAL_IMPL_TYPE (DB access)
 *
 * @author CLAISSE
 */
public class SnapManagerFactory
{
    /**
     * Code for a dummy implementation that returns hard-coded values
     */
    public static final int DUMMY_IMPL_TYPE = 1;

    /**
     * Code for a real implementation that uses Database APIs
     */
    public static final int REAL_IMPL_TYPE = 2;

    private static ISnapManager currentImpl = null;

    private static String user;
    private static String password;

    /**
     * Instantiates and returns an implementation the <code>ISnapManager<code> described by <code>typeOfImpl<code>
     *
     * @param typeOfImpl The type of implementation of ISnapManager
     * @return An implementation of ISnapManager
     * @throws IllegalArgumentException If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE, REAL_IMPL_TYPE)
     */
    public static ISnapManager getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummySnapManagerImpl();
                break;

            case REAL_IMPL_TYPE:
                System.out.println("SnapManagerFactory/getImpl/user/"+user+"/password/"+password);
                currentImpl = new SnapManagerImpl( user , password );
                break;

            default:
                throw new IllegalStateException( "Expected either DUMMY_IMPL_TYPE (1) or REAL_IMPL_TYPE (2), got " + typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * Returns the current implementation as precedently instantiated.
     *
     * @return The current implementation as precedently instantiated
     */
    public static ISnapManager getCurrentImpl ()
    {
        return currentImpl;
    }

    public static void setUser(String _user) 
    {
        user = _user;
    }

    public static void setPassword(String _password) 
    {
        password = _password;    
    }

}
