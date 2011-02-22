/*	Synchrotron Soleil 
 *  
 *   File          :  DevErrorWrapper.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  9 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: DevErrorWrapper.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.Tango.DevError;

public class DevErrorWrapper 
{
    private DevError devError;

    /**
     * @param devError
     */
    public DevErrorWrapper(DevError devError) {
        super();
        this.devError = devError;
    }

    /**
     * @return the devError
     */
    public DevError getDevError() {
        return this.devError;
    }

    /**
     * @param devError the devError to set
     */
    public void setDevError(DevError devError) {
        this.devError = devError;
    }

    public static DevErrorWrapper[] fillWrapper(DevError[] errors) 
    {
        DevErrorWrapper [] ret = new DevErrorWrapper [ errors.length ];
        for ( int i = 0 ; i < errors.length ; i ++ )
        {
            ret [ i ] = new DevErrorWrapper ( errors [ i ] );
        }
        return ret;
    }
}
