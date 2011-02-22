//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/tango/alternate/ITangoAlternateSelectionManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ITangoAlternateSelectionManager.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ITangoAlternateSelectionManager.java,v $
// Revision 1.1  2006/09/20 12:47:32  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.2  2006/02/24 12:22:17  ounsy
// added a loadDomains ( String domainsRegExp ) method
//
// Revision 1.1  2005/11/29 18:28:12  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.datasources.tango.alternate;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;

public interface ITangoAlternateSelectionManager
{
    public Domain[] loadDomains ( String domainsRegExp ) throws ArchivingException; 
    
    public Domain[] loadDomains () throws ArchivingException;

    public MamboDeviceClass[] loadDeviceClasses ( Domain domain ) throws ArchivingException;
    
    public MamboDeviceClass[] loadDeviceClasses(Domain[] domains) throws ArchivingException;

    public ArchivingConfigurationAttribute[] loadACAttributes ( Domain domain , MamboDeviceClass deviceClass ) throws ArchivingException;

    public ArchivingConfigurationAttribute[] loadACAttributes ( MamboDeviceClass deviceClass , ArchivingConfigurationAttribute brother );
}
