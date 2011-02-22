/*	Synchrotron Soleil 
 *  
 *   File          :  ss.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  13 sept. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ss.java,v 
 *
 */
 /*
 * Created on 13 sept. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.datasources.tango;

import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class FilteredTangoAlternateSelectionManager extends AbstractTangoAlternateSelectionManager  
{
    private ITangoAlternateSelectionManager wrappedTasm;
    
    FilteredTangoAlternateSelectionManager ( ITangoAlternateSelectionManager _tasm ) 
    {
        this.wrappedTasm = _tasm;
    }

    public Domain[] loadDomains(String domainsRegExp) throws SnapshotingException 
    {
        return this.wrappedTasm.loadDomains ( domainsRegExp );
    }

    public Domain[] loadDomains() throws SnapshotingException 
    {
        return this.wrappedTasm.loadDomains ();
    }

    public BensikinDeviceClass[] loadDeviceClasses(Domain domain) throws SnapshotingException 
    {
        return this.wrappedTasm.loadDeviceClasses(domain);
    }

    public ContextAttribute[] loadACAttributes(Domain domain, BensikinDeviceClass deviceClass) throws SnapshotingException 
    {
        return this.wrappedTasm.loadACAttributes(domain,deviceClass);
    }

    public ContextAttribute[] loadACAttributes(BensikinDeviceClass deviceClass, ContextAttribute brother) 
    {
        return this.wrappedTasm.loadACAttributes(deviceClass,brother);
    }
}
