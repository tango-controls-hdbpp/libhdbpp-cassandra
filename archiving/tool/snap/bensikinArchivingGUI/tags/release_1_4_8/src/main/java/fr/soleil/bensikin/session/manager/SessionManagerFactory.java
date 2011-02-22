/*	Synchrotron Soleil 
 *  
 *   File          :  SessionManagerFactory.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  8 déc. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SessionManagerFactory.java,v 
 *
 */
package fr.soleil.bensikin.session.manager;

import fr.soleil.bensikin.containers.profile.ProfileSelectionPanel;



/**
 * 
 * @author SOLEIL
 */
public class SessionManagerFactory {

    /**
     * Code for a dummy implementation that does nothing
     */
    public static final int DUMMY_IMPL_TYPE = 1;

    /**
     * Code for an XML implementation that saves/loads to/from XML files
     */
    public static final int XML_IMPL_TYPE = 2;

    private static ISessionManager currentImpl = null;
    private static SessionManagerFactory instance;
    private static String mainPath;
    
    private SessionManagerFactory(String directory) {
        mainPath = directory;
    }

    /**
     * Instantiates and returns an implementation the
     * <code>ISessionManager<code> described by <code>typeOfImpl<code>
     *
     * @param typeOfImpl The type of implementation of ISessionManager
     * @return An implementation of ISessionManager
     * @throws IllegalArgumentException If <code>typeOfImpl<code> isn't in (DUMMY_IMPL_TYPE, XML_IMPL_TYPE)
     */
    public static ISessionManager getImpl ( int typeOfImpl ) {
        switch ( typeOfImpl ) {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummySesionManager();
                break;

            case XML_IMPL_TYPE:
                //currentImpl = new XMLSessionManager( mainPath, ProfileSelectionPanel.getInstance().isMultiSession() );
                currentImpl = new XMLSessionManager( mainPath, false );
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
    public static ISessionManager getCurrentImpl ()
    {
        return currentImpl;
    }

    /**
     * Sets the path used by SessionManagerFactory to initialize any SessionManagerFactory and any ISessionManager
     * @param path the profile working directory path
     */
    public static void setPath(String path) {
        mainPath = path;
    }

    /**
     * Returns the shared SessionManagerFactory and initializes it when
     * necessary. If first call to this method, please call the setPath(String
     * path) method to be able to initialize the shared SessionManagerFactory
     * before calling this method.
     * 
     * @return the shared SessionManagerFactory
     */
    public static SessionManagerFactory getInstance() {
        if (instance==null) {
            instance = new SessionManagerFactory(mainPath);
        }
        return instance;
    }
}
