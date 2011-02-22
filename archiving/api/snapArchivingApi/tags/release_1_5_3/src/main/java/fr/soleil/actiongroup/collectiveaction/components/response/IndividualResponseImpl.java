/*	Synchrotron Soleil 
 *  
 *   File          :  IndividualResponse.java
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
 *   Log: IndividualResponse.java,v 
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

public class IndividualResponseImpl implements IndividualResponse 
{
    protected String deviceName;
    protected DevErrorWrapper[] err_stack;  
    protected boolean has_failed;  
    protected boolean has_timeout;  
    
    public IndividualResponseImpl ( String _deviceName ) 
    {
        this.deviceName = _deviceName;
    }
    
    public String dev_name() 
    {
        return this.deviceName;
    }

    public DevErrorWrapper[] get_err_stack() 
    {
        return this.err_stack;
    }

    public boolean has_failed() 
    {
        return this.has_failed;
    }

    public boolean has_timeout() 
    {
        return this.has_timeout;
    }

    public void set_err_stack ( DevErrorWrapper[] _err_stack ) 
    {
        this.err_stack = _err_stack;
    }

    public void set_failed ( boolean _has_failed ) 
    {
        this.has_failed = _has_failed;
    }

    public void set_timeout ( boolean _has_timeout ) 
    {
        this.has_timeout = _has_timeout;
    }
}
