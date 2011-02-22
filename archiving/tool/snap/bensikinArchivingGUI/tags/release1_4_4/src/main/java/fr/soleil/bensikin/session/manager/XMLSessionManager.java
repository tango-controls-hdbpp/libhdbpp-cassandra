/*	Synchrotron Soleil 
 *  
 *   File          :  XMLSessionManager.java
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
 *   Log: XMLSessionManager.java,v 
 *
 */
package fr.soleil.bensikin.session.manager;

import java.io.File;
import java.io.IOException;

import fr.soleil.bensikin.containers.sub.dialogs.BensikinErrorDialog;
import fr.soleil.bensikin.session.Session;
import fr.soleil.bensikin.tools.Messages;


/**
 * 
 * @author SOLEIL
 */
public class XMLSessionManager implements ISessionManager {
    File lockFile;
    boolean multiSession;

    XMLSessionManager(String directory, boolean multi) {
        multiSession = multi;
        lockFile = new File(directory + File.separatorChar + ".lock");
    }

    /* (non-Javadoc)
     * @see mambo.session.manager.ISessionManager#lockDirectory(java.lang.String)
     */
    public boolean lockDirectory() throws IOException {
        boolean created = lockFile.createNewFile();
        if (!created) {
            if (multiSession) {
                File directory;
                for (int i = 1; i < Integer.MAX_VALUE; i++) {
                    String path = lockFile.getParent() + File.separatorChar + "session" + i;
                    directory = new File(path);
                    directory.mkdir();// does not create the directory if it already exists
                    lockFile = new File(path + File.separatorChar + ".lock");
                    if (lockFile.createNewFile()) {
                        new BensikinErrorDialog(Messages.getMessage("SESSION_WARNING_TITLE"),Messages.getMessage("SESSION_LOCKED_WARNING")).setVisible ( true );
                        Session.getInstance(path);//validating session
                        return true;
                    }
                    else {
                        lockFile = new File(lockFile.getParent());
                    }
                }
                //Too many users, can not write lock file
                throw new IOException();
            }
            else {
                return false;
            }
        }
        Session.getInstance(lockFile.getParent());//validating session
        return true; 
    }

    /* (non-Javadoc)
     * @see mambo.session.manager.ISessionManager#quit()
     */
    public void clearLock() {
        lockFile.delete();
    }
}
