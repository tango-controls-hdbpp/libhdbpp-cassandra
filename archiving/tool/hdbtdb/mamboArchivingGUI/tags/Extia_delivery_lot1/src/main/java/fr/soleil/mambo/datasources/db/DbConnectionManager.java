package fr.soleil.mambo.datasources.db;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;

public class DbConnectionManager {

    // protected static DataBaseApi database;
    protected static boolean isReady = false;
    protected static IArchivingManagerApiRef hdbManager, tdbManager;
    private final static String              HDB = "HDB";
    private final static String              TDB = "TDB";

    public DbConnectionManager() {
        // System.out.println ( "DbConnectionManager/new/manager/"+manager );
    }

    public void openConnection() throws ArchivingException {
        if (!isReady) {
            ILogger logger = LoggerFactory.getCurrentImpl();

            // ArchivingManagerApi.ArchivingConfigure( HDBuser , HDBpassword ,
            // TDBuser , TDBpassword );
            // CLA 30/10/06 AN EXCEPTION IS NO LONGER THROWN, AS BOTH DATABASES
            // ARE INITIALIZED INDEPENDANTLY
            ArchivingException hdbFailure = null;
            ArchivingException tdbFailure = null;

            // If HDB is not available it is not useful to connect to this DB
            try {
                // check HDB connection parameters
                if (Mambo.isHdbAvailable()) {
                    connectDataBase(true);
                }
            }
            catch (ArchivingException ae) {
                hdbManager = null;
                System.out
                        .println("DbConnectionManager/openConnection/failed to connect to HDB!");
                ae.printStackTrace();

                logger.trace(ILogger.LEVEL_ERROR, "Failed to connect to HDB");
                logger.trace(ILogger.LEVEL_ERROR, ae);

                hdbFailure = ae;
            }

            try {
                // check TDB connection parameters
                connectDataBase(false);
            }
            catch (ArchivingException ae) {
                System.out
                        .println("DbConnectionManager/openConnection/failed to connect to TDB!");
                ae.printStackTrace();

                logger.trace(ILogger.LEVEL_ERROR, "Failed to connect to TDB");
                logger.trace(ILogger.LEVEL_ERROR, ae);

                tdbFailure = ae;
            }

            if (hdbFailure != null) {
                throw hdbFailure;
            }

            if (tdbFailure != null) {
                throw tdbFailure;
            }

            isReady = true;
        }
    }

    // centralized method for database connection
    private void connectDataBase(boolean historic) throws ArchivingException {
        IArchivingManagerApiRef manager = null;
        String prefix;
        String user;
        String password;
        String deviceClass;
        if (historic) {
            manager = ArchivingManagerApiRefFactory
                    .getInstance(ConfigConst.HDB);
            hdbManager = manager;
            prefix = HDB;
            user = Mambo.getHDBuser();
            password = Mambo.getHDBpassword();
            deviceClass = ConfigConst.HDB_CLASS_DEVICE;
        }
        else {
            manager = ArchivingManagerApiRefFactory
                    .getInstance(ConfigConst.TDB);
            tdbManager = manager;
            prefix = TDB;
            user = Mambo.getTDBuser();
            password = Mambo.getTDBpassword();
            deviceClass = ConfigConst.TDB_CLASS_DEVICE;
        }

        String dbHost = extractSystemProperty(prefix + "_HOST");
        if ("".equals(dbHost)) {
            dbHost = GetConf.readStringInDB(deviceClass, "DbHost");
        }

        String dbName = extractSystemProperty(prefix + "_NAME");
        if ("".equals(dbName)) {
            dbName = GetConf.readStringInDB(deviceClass, "DbName");
        }

        String racConnection = extractSystemProperty(prefix + "_RAC");
        boolean isRac = "true".equalsIgnoreCase(racConnection);
        if ("".equals(racConnection)) {
            isRac = GetConf.readBooleanInDB(deviceClass, "RacConnection");
        }
        racConnection = null;

        if (Mambo.canChooseArchivers()) {
            manager.ArchivingConfigure(user, password);
        }
        else {
            manager.ArchivingConfigureWithoutArchiverListInit(dbHost, dbName,
                    user, password, isRac);
        }
    }

    // Returns the expected System property, or an empty string if it does not
    // exist
    private String extractSystemProperty(String property) {
        return System.getProperty(property) == null ? "" : System.getProperty(
                property).trim();
    }

    /*
     * (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#getDataBaseApi(boolean)
     */
    public DataBaseManager getDataBaseApi(boolean _historic)
            throws ArchivingException {
        DataBaseManager database = null;
        try {
            openConnection();
            database = getArchivingManagerApiInstance(_historic).getDataBase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return database;
    }

    public IArchivingManagerApiRef getArchivingManagerApiInstance(
            boolean historic) {
        return historic ? hdbManager : tdbManager;
    }
}
