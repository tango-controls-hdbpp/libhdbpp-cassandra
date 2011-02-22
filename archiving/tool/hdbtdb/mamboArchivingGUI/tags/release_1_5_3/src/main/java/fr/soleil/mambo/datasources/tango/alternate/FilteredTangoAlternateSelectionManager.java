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
package fr.soleil.mambo.datasources.tango.alternate;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;

public class FilteredTangoAlternateSelectionManager extends AbstractTangoAlternateSelectionManager implements ITangoAlternateSelectionManager 
{
    private ITangoAlternateSelectionManager wrappedTasm;
    
    FilteredTangoAlternateSelectionManager ( ITangoAlternateSelectionManager _tasm ) 
    {
        this.wrappedTasm = _tasm;
    }

    public Domain[] loadDomains(String domainsRegExp) throws ArchivingException 
    {
        return this.wrappedTasm.loadDomains ( domainsRegExp );
    }

    public Domain[] loadDomains() throws ArchivingException 
    {
        return this.wrappedTasm.loadDomains ();
    }

    public MamboDeviceClass[] loadDeviceClasses(Domain domain) throws ArchivingException 
    {
        return this.wrappedTasm.loadDeviceClasses(domain);
    }

    public ArchivingConfigurationAttribute[] loadACAttributes(Domain domain, MamboDeviceClass deviceClass) throws ArchivingException 
    {
        return this.wrappedTasm.loadACAttributes(domain,deviceClass);
    }

    public ArchivingConfigurationAttribute[] loadACAttributes(MamboDeviceClass deviceClass, ArchivingConfigurationAttribute brother) 
    {
        return this.wrappedTasm.loadACAttributes(deviceClass,brother);
    }
}
