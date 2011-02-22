/*	Synchrotron Soleil 
 *  
 *   File          :  ISessionManager.java
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
 *   Log: ISessionManager.java,v 
 *
 */
package fr.soleil.bensikin.session.manager;

import java.io.IOException;

/**
 * Interface of session manager.
 * @author SOLEIL
 */
public interface ISessionManager {
    /**
     * Puts a lock on the working directory and initializes the Session
     * @return true if the directory is successfully locked, false
     * otherwise, which means that another Mambo application locked the directory.
     * @throws IOException can happen when trying to create the lock file
     */
    public boolean lockDirectory() throws IOException;

    /**
     * Does everything necessary to properly finish the session before exiting
     */
    public void clearLock();
}
