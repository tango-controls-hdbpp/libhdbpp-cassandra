//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/tango/DummyTangoAlternateSelectionManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyTangoAlternateSelectionManagerImpl.
//						(Claisse Laurent) - oct. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: DummyTangoAlternateSelectionManagerImpl.java,v $
// Revision 1.5  2007/11/09 16:20:33  pierrejoseph
// Suppression of unuseful interface inheritance
//
// Revision 1.4  2007/03/15 14:26:34  ounsy
// corrected the table mode add bug and added domains buffer
//
// Revision 1.2  2006/11/23 10:04:31  ounsy
// refactroring
//
// Revision 1.1  2006/09/20 12:47:32  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.4  2006/09/14 11:29:29  ounsy
// complete buffering refactoring
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2006/02/24 12:22:26  ounsy
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

public class DummyTangoAlternateSelectionManagerImpl implements ITangoAlternateSelectionManager
{

    public ContextAttribute[] loadACAttributes(Domain domain, BensikinDeviceClass deviceClass) throws SnapshotingException {
        // TODO Auto-generated method stub
        return null;
    }

    public ContextAttribute[] loadACAttributes(BensikinDeviceClass deviceClass, ContextAttribute brother) {
        // TODO Auto-generated method stub
        return null;
    }

    public BensikinDeviceClass[] loadDeviceClasses(Domain domain) throws SnapshotingException {
        // TODO Auto-generated method stub
        return null;
    }

    public BensikinDeviceClass[] loadDeviceClasses(Domain[] domains) throws SnapshotingException {
        // TODO Auto-generated method stub
        return null;
    }

    public Domain[] loadDomains(String domainsRegExp) throws SnapshotingException {
        // TODO Auto-generated method stub
        return null;
    }

    public Domain[] loadDomains() throws SnapshotingException {
        // TODO Auto-generated method stub
        return null;
    }


    

}
