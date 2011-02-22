/*	Synchrotron Soleil 
 *  
 *   File          :  Domains.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  7 nov. 06 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: Domains.java,v 
 *
 */
 /*
 * Created on 7 nov. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.data.attributes;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import fr.soleil.mambo.tools.GUIUtilities;


public class Domains implements java.io.Serializable 
{
    private Map domains;
    public Domains ()
    {
        this.domains = new Hashtable ();
    }
    
    public void addAttribute ( String domainName , String familyName , String memberName , String attributeName )
    {
        //System.out.println ( "Domains/addAttribute/domainName|"+domainName+"|familyName|"+familyName+"|memberName|"+memberName+"|attributeName|"+attributeName+"|" );
        Domain domain = this.getDomain ( domainName );
        if ( domain == null )
        {
            domain = new Domain ( domainName );
            this.addDomain ( domain );
        }
        
        domain.addAttribute ( familyName , memberName , attributeName );
    }

    public Vector getList() 
    {
        Vector ret = new Vector ();
        ret.addAll ( domains.values () );
        return ret;
    }
    
    public Domain getDomain ( String domainName )
    {
        return (Domain) domains.get ( domainName );
    }
    
    public void addDomain ( Domain domain )
    {
        domains.put ( domain.getName() , domain );
    }
    
    public String getDescription ()
    {
        StringBuffer buff = new StringBuffer ();
        buff.append ( "    VVVVVVVVVVVVV Domains VVVVVVVVVVVVVVVV" );
        buff.append ( GUIUtilities.CRLF );
        Iterator keysIterator = domains.keySet ().iterator ();
        while ( keysIterator.hasNext () )
        {
            String nextKey = (String) keysIterator.next ();
            Domain nextDomain = (Domain) domains.get ( nextKey );
            buff.append ( nextDomain.getDescription () );
            buff.append ( GUIUtilities.CRLF );
        }
        buff.append ( "    ^^^^^^^^^^^^^ Domains ^^^^^^^^^^^^^^^^" );
        return buff.toString ();
    }
}
