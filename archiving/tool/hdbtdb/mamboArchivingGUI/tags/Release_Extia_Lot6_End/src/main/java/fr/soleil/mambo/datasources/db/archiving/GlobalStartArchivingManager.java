// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/datasources/db/archiving/GlobalStartArchivingManager.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DBManagerImplWithGlobalStartCall.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: GlobalStartArchivingManager.java,v $
// Revision 1.9 2007/03/05 16:27:33 ounsy
// non-static DataBase
//
// Revision 1.8 2007/01/05 12:57:30 pierrejoseph
// Modification of the ArchivingMessConfig object creation
//
// Revision 1.7 2006/12/08 16:07:07 ounsy
// forcing keeping period = 3 days (to be compatible with MySQL)
//
// Revision 1.6 2006/12/08 14:54:57 pierrejoseph
// minor changes
//
// Revision 1.5 2006/12/07 16:45:39 ounsy
// removed keeping period
//
// Revision 1.4 2006/11/22 10:43:47 ounsy
// minor changes
//
// Revision 1.3 2006/11/20 09:37:41 ounsy
// all the methodss now throw only ArchivingExceptions
//
// Revision 1.2 2006/09/26 15:08:04 ounsy
// minor changes
//
// Revision 1.1 2006/09/22 09:32:19 ounsy
// moved from mambo.datasources.db
//
// Revision 1.5 2006/06/15 15:41:21 ounsy
// added support for dedicate archivers definition
//
// Revision 1.4 2006/04/10 09:13:47 ounsy
// removed useless logs
//
// Revision 1.3 2006/02/24 12:21:37 ounsy
// corrected a bug where an incorrect Periodic mode wasn't reported
//
// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.datasources.db.archiving;

import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingMessConfig;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributesArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;

public class GlobalStartArchivingManager extends BasicArchivingManager {

    @Override
    public void startArchiving(ArchivingConfiguration ac)
            throws ArchivingException {
        if (!isReady) {
            openConnection();
        }
        if (ac != null) {
            boolean historic = ac.isHistoric();
            IArchivingManagerApiRef manager;
            String dbType;
            if (historic) {
                manager = hdbManager;
                dbType = "historical";
            } else {
                manager = tdbManager;
                dbType = "temporary";
            }
            ArchivingConfigurationAttributes ACAttributes = ac.getAttributes();
            ArchivingConfigurationAttribute[] attributesList = ACAttributes
                    .getAttributesList();
            int nbOfAttributes = attributesList.length;

            ArchivingMessConfig archivingMessConfig = null;
            for (int i = 0; i < nbOfAttributes; i++) {
                ArchivingConfigurationAttribute next = attributesList[i];
                ArchivingConfigurationAttributeProperties nextProperties = next
                        .getProperties();
                ArchivingConfigurationAttributeDBProperties nextDBProperties;
                if (historic) {
                    nextDBProperties = nextProperties.getHDBProperties();
                } else {
                    nextDBProperties = nextProperties.getTDBProperties();
                }
                String nextDBDedicatedArchiver = nextDBProperties
                        .getDedicatedArchiver();
                if (!nextDBProperties.isEmpty()) {
                    if (archivingMessConfig == null) {
                        archivingMessConfig = ArchivingMessConfig
                                .basicObjectCreation();
                    }

                    ArchivingConfigurationMode[] DBModes = nextDBProperties
                            .getModes();

                    AttributeLightMode attributeLightMode = new AttributeLightMode();
                    attributeLightMode.setAttribute_complete_name(next
                            .getCompleteName());
                    Mode mode;
                    if (historic) {
                        mode = ArchivingConfigurationMode
                                .buildCompleteHDBMode(DBModes);
                    } else {
                        mode = ArchivingConfigurationMode
                                .buildCompleteTDBMode(
                                        DBModes,
                                        ((ArchivingConfigurationAttributeTDBProperties) nextDBProperties)
                                                .getExportPeriod(), 259200000);
                    }
                    attributeLightMode.setMode(mode);

                    archivingMessConfig.add(attributeLightMode);
                    archivingMessConfig.setDeviceInChargeForAttribute(
                            attributeLightMode, nextDBDedicatedArchiver);
                } // end if (!nextDBProperties.isEmpty())
            } // end for (int i = 0; i < nbOfAttributes; i++)

            AttributesArchivingException archivingException = new AttributesArchivingException();
            boolean needsThrow = false;
            if (archivingMessConfig != null) {
                archivingMessConfig.setGrouped(false);
                try {
                    manager.ArchivingStart(archivingMessConfig);
                } catch (AttributesArchivingException e) {
                    e.printStackTrace();
                    archivingException.addStack("Failed when launching "
                            + dbType + " archiving", e);
                    // We transfer all bad attributes from the caught exception
                    // to the new forged exception
                    archivingException.getFaultingAttributes().addAll(
                            e.getFaultingAttributes());
                    needsThrow = true;
                }
            } // end if (archivingMessConfig != null)
            if (needsThrow) {
                throw archivingException;
            }
        } // end if (ac != null)
    }

}
