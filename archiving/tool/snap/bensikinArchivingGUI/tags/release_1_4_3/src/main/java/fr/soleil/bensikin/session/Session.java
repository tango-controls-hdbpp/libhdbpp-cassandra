/*	Synchrotron Soleil 
 *  
 *   File          :  Session.java
 *  
 *   Project       :  bensikin
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
 *   Log: Session.java,v 
 *
 */
package fr.soleil.bensikin.session;

/**
 * Contains the basic informations for a session
 * @author SOLEIL
 */
public class Session {

    private String directory;
    private static Session instance;

    private Session(String dir) {
        directory = dir;
    }

    public static Session getInstance(String dir) {
        if (instance == null) {
            instance = new Session(dir);
        }
        return instance;
    }

    public static Session getInstance() {
        return instance;
    }

    public String getDirectory() {
        return directory;
    }

}
