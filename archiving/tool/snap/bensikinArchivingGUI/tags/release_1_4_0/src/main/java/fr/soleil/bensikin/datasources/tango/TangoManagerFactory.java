//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/tango/TangoManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TangoManagerFactory.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: TangoManagerFactory.java,v $
// Revision 1.2  2006/11/29 10:02:17  ounsy
// package refactoring
//
// Revision 1.1  2005/12/14 16:56:05  ounsy
// has been renamed
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
package fr.soleil.bensikin.datasources.tango;

import fr.soleil.bensikin.impl.DummyTangoManagerImpl;
import fr.soleil.bensikin.impl.TangoManagerImpl;


/**
 * A factory used to instantiate ITangoManager of 2 types: DUMMY_IMPL_TYPE or REAL_IMPL_TYPE (Tango access)
 *
 * @author CLAISSE
 */
public class TangoManagerFactory
{
    /**
     * Code for a dummy implementation that returns hard-coded values
     */
    public static final int DUMMY_IMPL_TYPE = 1;

    /**
     * Code for a real implementation that looks up Tango attributes
     */
    public static final int REAL_IMPL_TYPE = 2;

    private static ITangoManager currentImpl = null;

    /**
     * Instantiates and returns an implementation the <code>ITangoManager<code> described by <code>typeOfImpl<code>
     *
     * @param typeOfImpl The type of implementation of ITangoManager
     * @return An implementation of ITangoManager
     * @throws IllegalArgumentException If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE, REAL_IMPL_TYPE)
     */
    public static ITangoManager getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummyTangoManagerImpl();
                break;

            case REAL_IMPL_TYPE:
                currentImpl = new TangoManagerImpl();
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
    public static ITangoManager getCurrentImpl ()
    {
        return currentImpl;
    }

}
