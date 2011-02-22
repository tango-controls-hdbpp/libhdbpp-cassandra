/*	Synchrotron Soleil 
 *  
 *   File          :  DummySesionManager.java
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
 *   Log: DummySesionManager.java,v 
 *
 */
package fr.soleil.mambo.session.manager;

import java.io.IOException;

/**
 * 
 * @author SOLEIL
 */
public class DummySesionManager implements ISessionManager {
    
    public DummySesionManager() {
        
    }

    /* (non-Javadoc)
     * @see mambo.session.manager.ISessionManager#lockDirectory()
     */
    public boolean lockDirectory() throws IOException {
        return false;
    }

    /* (non-Javadoc)
     * @see mambo.session.manager.ISessionManager#quit()
     */
    public void clearLock() {
        System.exit(0);
    }

}
