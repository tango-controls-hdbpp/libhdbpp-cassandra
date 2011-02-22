//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/profile/manager/ProfileManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileManagerFactory.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileManagerFactory.java,v $
// Revision 1.1  2005/12/15 09:21:07  ounsy
// First Commit including profile management
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.profile.manager;

/**
 * A factory used to instantiate IProfileManager of 2 types: DUMMY_IMPL_TYPE or
 * XML_IMPL_TYPE
 *
 * @author SOLEIL
 */
public class ProfileManagerFactory
{

    /**
     * Code for a dummy implementation that does nothing
     */
    public static final int DUMMY_IMPL_TYPE = 1;

    /**
     * Code for an XML implementation that saves/loads to/from XML files
     */
    public static final int XML_IMPL_TYPE = 2;

    private static IProfileManager currentImpl = null;

    private static ProfileManagerFactory instance;

    private static String mainPath;

    /**
     * Constructor
     *
     * @param path The application start folder path
     */
    private ProfileManagerFactory ( String path )
    {
        mainPath = path;
    }

    /**
     * Instantiates and returns an implementation the
     * <code>IProfileManager<code> described by <code>typeOfImpl<code>
     *
     * @param typeOfImpl The type of implementation of IProfileManager
     * @return An implementation of IProfileManager
     * @throws IllegalArgumentException If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE, XML_IMPL_TYPE)
     */
    public static IProfileManager getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummyProfileManager();
                break;

            case XML_IMPL_TYPE:
                currentImpl = new XMLProfileManager( mainPath );
                break;

            default :
                throw new IllegalArgumentException( "Expected either DUMMY_IMPL_TYPE (1) or XML_IMPL_TYPE (2), got "
                                                    + typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * Returns the current implementation as precedently instantiated.
     *
     * @return The current implementation as precedently instantiated
     */
    public static IProfileManager getCurrentImpl ()
    {
        return currentImpl;
    }

    /**
     * Returns the ProfileManagerFactory currently used by the rest of the
     * application
     *
     * @return The ProfileManagerFactory currently used by the rest of the
     *         application
     */
    public static ProfileManagerFactory getInstance ()
    {
        return instance;
    }

    /**
     * The ProfileManagerFactory currently used by the rest of the application,
     * that is initiated on first call of this method
     *
     * @param path The application start folder path
     * @return The ProfileManagerFactory currently used by the rest of the
     *         application, that is initiated on first call of this method
     */
    public static ProfileManagerFactory getInstance ( String path )
    {
        if ( instance == null )
        {
            instance = new ProfileManagerFactory( path );
        }
        return instance;
    }

}