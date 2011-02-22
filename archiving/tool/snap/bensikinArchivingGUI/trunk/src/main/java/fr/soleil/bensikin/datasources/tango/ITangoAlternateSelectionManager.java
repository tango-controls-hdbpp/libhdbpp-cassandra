//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/tango/ITangoAlternateSelectionManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ITangoAlternateSelectionManager.
//						(Claisse Laurent) - oct. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ITangoAlternateSelectionManager.java,v $
// Revision 1.4  2007/11/09 16:20:33  pierrejoseph
// Suppression of unuseful interface inheritance
//
// Revision 1.3  2007/03/15 14:26:34  ounsy
// corrected the table mode add bug and added domains buffer
//
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
package fr.soleil.bensikin.datasources.tango;

import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public interface ITangoAlternateSelectionManager
{
    public Domain[] loadDomains ( String domainsRegExp ) throws SnapshotingException; 
    
    public Domain[] loadDomains () throws SnapshotingException;

    public BensikinDeviceClass[] loadDeviceClasses ( Domain domain ) throws SnapshotingException;
    
    public BensikinDeviceClass[] loadDeviceClasses(Domain[] domains) throws SnapshotingException;

    public ContextAttribute[] loadACAttributes ( Domain domain , BensikinDeviceClass deviceClass ) throws SnapshotingException;

    public ContextAttribute[] loadACAttributes ( BensikinDeviceClass deviceClass , ContextAttribute brother );
}
