// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/datasources/db/archiving/BasicArchivingManager.java,v
// $

// Project: Tango Archiving Service

// Description: Java source code for the class DBManagerImpl.
// (Claisse Laurent) - 5 juil. 2005

// $Author: ounsy $

// $Revision: 1.6 $

// $Log: BasicArchivingManager.java,v $
// Revision 1.6 2007/03/05 16:27:33 ounsy
// non-static DataBase

// Revision 1.5 2006/11/20 09:37:41 ounsy
// all the methodss now throw only ArchivingExceptions

// Revision 1.4 2006/10/19 12:42:21 ounsy
// minor changes

// Revision 1.3 2006/09/27 13:38:21 ounsy
// now uses DbConnectionManager to manage DB connecion

// Revision 1.2 2006/09/26 15:54:08 ounsy
// added timeOut management in stopArchiving

// Revision 1.1 2006/09/22 09:32:19 ounsy
// moved from mambo.datasources.db

// Revision 1.5 2006/06/20 16:03:56 ounsy
// When an attribute of an archiving configuration fails to stop archiving, it
// does not avoid the other ones to stop

// Revision 1.4 2006/06/15 15:40:27 ounsy
// minor changes

// Revision 1.3 2006/03/10 15:49:38 ounsy
// stop only hdb or only tdb

// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message

// Revision 1.1.2.4 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...

// Revision 1.1.2.3 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...

// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !

// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX

// -======================================================================
package fr.soleil.mambo.datasources.db.archiving;

import java.util.ArrayList;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.TDBDataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.datasources.db.DbConnectionManager;
import fr.soleil.mambo.tools.Messages;

public class BasicArchivingManager extends DbConnectionManager implements
        IArchivingManager {

    protected final static String errorSeparator = "\n*----------------------------*\n";

    @Override
    public void startArchiving(ArchivingConfiguration AC)
            throws ArchivingException {
    }

    @Override
    public void stopArchiving(ArchivingConfiguration ac)
            throws ArchivingException {
        if (ac != null) {
            boolean historic = ac.isHistoric();
            IArchivingManagerApiRef manager;
            String dbName;
            if (historic) {
                manager = hdbManager;
                dbName = "HDB";
            } else {
                manager = tdbManager;
                dbName = "TDB";
            }
            int exceptionCounter = 1;
            String exceptionMessage = "";
            openConnection();
            ArchivingConfigurationAttributes acAttributes = ac.getAttributes();
            ArchivingConfigurationAttribute[] attributesList = acAttributes
                    .getAttributesList();
            int nbOfAttributes = attributesList.length;
            ArrayList<String> attributeListFinal = new ArrayList<String>();
            ArchivingException toThrow = new ArchivingException();

            for (int i = 0; i < nbOfAttributes; i++) {
                ArchivingConfigurationAttribute next = attributesList[i];
                String nextName = next.getCompleteName();
                try {
                    if (manager != null && manager.isArchived(nextName)) {
                        attributeListFinal.add(nextName);
                    }
                } catch (ArchivingException e1) {
                    exceptionMessage = errorSeparator;
                    exceptionMessage += Messages
                            .getMessage("LOGS_ERROR_NUMBER")
                            + exceptionCounter++ + "\n";
                    exceptionMessage += Messages
                            .getMessage("LOGS_ATTRIBUTE_IN_ERROR")
                            + " " + nextName + "\n";
                    exceptionMessage += Messages
                            .getMessage("LOGS_CONCERNED_DATABASE")
                            + " " + dbName + "\n";
                    exceptionMessage += e1.getMessage();
                    toThrow.addStack(exceptionMessage, e1);
                }
            }// end for (int i = 0; i < nbOfAttributes; i++)

            try {
                if (manager != null && !attributeListFinal.isEmpty()) {
                    String[] attributeListFinal_s = new String[0];
                    attributeListFinal_s = attributeListFinal
                            .toArray(attributeListFinal_s);
                    manager.ArchivingStop(attributeListFinal_s);
                }
            } catch (ArchivingException e1) {
                exceptionMessage = errorSeparator;
                exceptionMessage += Messages.getMessage("LOGS_ERROR_NUMBER")
                        + exceptionCounter++ + "\n";
                exceptionMessage += Messages
                        .getMessage("LOGS_CONCERNED_DATABASE")
                        + " " + dbName + "\n";
                exceptionMessage += e1.getMessage();

                toThrow.addStack(exceptionMessage, e1);
            }

            if (exceptionCounter > 1) {
                throw toThrow;
            }
        } // end if (ac != null)
    }

    @Override
    public Mode getArchivingMode(String completeName, boolean historic)
            throws ArchivingException {
        Mode mode = null;
        try {
            mode = this.getArchivingManagerApiInstance(historic)
                    .GetArchivingMode(completeName);
        } catch (Exception e) {
            // XXX AW: It is a bad way programming. However, if this
            // printStackTrace is removed, a lot of NullPointerException are
            // displayed at Mambo start. It could be a good idea to search why.

            // e.printStackTrace();
        }
        return mode;
    }

    @Override
    public boolean isArchived(String completeName, boolean historic)
            throws ArchivingException {
        boolean ret = false;
        ret = this.getArchivingManagerApiInstance(historic).isArchived(
                completeName);
        return ret;
    }

    public Archiver getCurrentArchiverForAttribute(String completeName,
            boolean _historic) throws ArchivingException {
        openConnection();
        DataBaseManager database = getDataBaseApi(_historic);

        String archiverName = database
                .getMode()
                .getArchiverForAttributeEvenIfTheStopDateIsNotNull(completeName);
        if (archiverName == null || archiverName.equals("")) {
            return null;
        } else {
            Archiver ret = new Archiver(archiverName, _historic);
            return ret;
        }
    }

    public void exportData2Tdb(String attributeName, String endDate)
            throws ArchivingException {
        try {
            ((TDBDataBaseManager) tdbManager.getDataBase()).getTdbExport()
                    .ExportData2Tdb(attributeName, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
