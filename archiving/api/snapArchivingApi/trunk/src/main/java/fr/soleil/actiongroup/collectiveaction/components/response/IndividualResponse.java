/*	Synchrotron Soleil 
 *  
 *   File          :  dd.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  1 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: dd.java,v 
 *
 */
 /*
 * Created on 1 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.response;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevErrorWrapper;

public interface IndividualResponse 
{
    public boolean has_failed ();

    public String dev_name();

    public DevErrorWrapper[] get_err_stack ();

    public boolean has_timeout (); 
    
    public void set_err_stack ( DevErrorWrapper[] _err_stack ); 
    
    public void set_failed ( boolean _has_failed ); 
    
    public void set_timeout ( boolean _has_timeout ); 
}
