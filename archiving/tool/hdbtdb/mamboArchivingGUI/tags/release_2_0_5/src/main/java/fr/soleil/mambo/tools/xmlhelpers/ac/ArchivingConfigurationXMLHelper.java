//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/xmlhelpers/ac/ArchivingConfigurationXMLHelper.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationXMLHelper.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.1 $
//
// $Log: ArchivingConfigurationXMLHelper.java,v $
// Revision 1.1  2007/02/01 14:12:19  pierrejoseph
// Export Period is sometimes forced to 30 min.
// XmlHelper reorg
//
// Revision 1.10  2006/12/07 16:45:39  ounsy
// removed keeping period
//
// Revision 1.9  2006/11/24 13:20:18  ounsy
// corrected a small bug
//
// Revision 1.8  2006/10/05 15:35:14  ounsy
// uncommented the dedicatedArchiver part
//
// Revision 1.7  2006/07/18 10:31:43  ounsy
// minor changes
//
// Revision 1.6  2006/06/30 08:30:57  ounsy
// corrected loadCurretnAttributeModes (dedicated archivers were partially implemented)
//
// Revision 1.5  2006/06/28 14:16:36  ounsy
// null dedicated archiver protection
//
// Revision 1.4  2006/06/15 15:42:52  ounsy
// added support for dedicate archivers definition
//
// Revision 1.3  2006/02/24 12:23:56  ounsy
// modified for HDB/TDB separation
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.tools.xmlhelpers.ac;

import org.w3c.dom.Node;

import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public class ArchivingConfigurationXMLHelper extends ArchivingConfigurationXMLHelperStandard implements IArchivingConfigurationXMLHelper
{

    public ArchivingConfiguration loadArchivingConfigurationIntoHash ( String location ) throws Exception
    {
    	return super.loadArchivingConfigurationIntoHash ( location );
    }

    public ArchivingConfiguration loadArchivingConfigurationIntoHashFromRoot ( Node rootNode ) throws Exception
    {
    	return super.loadArchivingConfigurationIntoHashFromRoot ( rootNode );
    }
}
