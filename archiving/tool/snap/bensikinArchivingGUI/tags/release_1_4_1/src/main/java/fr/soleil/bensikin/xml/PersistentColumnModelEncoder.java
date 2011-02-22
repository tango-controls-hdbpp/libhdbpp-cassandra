/*	Synchrotron Soleil 
 *  
 *   File          :  PersistentColumnModelEncoder.java
 *  
 *   Project       :  bensikin
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  23 mars 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: PersistentColumnModelEncoder.java,v 
 *
 */
 /*
 * Created on 23 mars 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.xml;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import fr.soleil.bensikin.Bensikin;


public class PersistentColumnModelEncoder extends XMLEncoder
{
    public PersistentColumnModelEncoder() throws FileNotFoundException 
    {
        super ( getOutputStream () );
        
        ExceptionListener exceptionListener = new ExceptionListener ()
        {
            public void exceptionThrown ( Exception e )
            {
                e.printStackTrace();
            }
        };
        this.setExceptionListener(exceptionListener);
    }

    private static OutputStream getOutputStream() throws FileNotFoundException 
    {
        String path = Bensikin.getPathToResources();
        String absp = path + "/beans";
        File f = new File(absp);
        if ( !f.canWrite() )
        {
            f.mkdir();
        }
        absp += "/columnModel" + ".bean";

        OutputStream out = new BufferedOutputStream ( new FileOutputStream ( new File (absp) ) );
        
        return out;
    }

}
