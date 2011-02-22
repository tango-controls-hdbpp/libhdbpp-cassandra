/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.ValidConnection;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public abstract class AbstractOracleConnection extends DBConnection {

    private static final String TNSNAME_PROPERTY                 = "DbTnsNames";
    private static final String ONSCONF_PROPERTY                 = "DbONSConf";
    private static final String MIN_POOL_SIZE_PROPERTY           = "DbMinPoolSize";
    private static final String MAX_POOL_SIZE_PROPERTY           = "DbMaxPoolSize";
    private static final String INACTIVITY_TIMEOUT_SIZE_PROPERTY = "DbCnxInactivityTimeout";
    private static final short  DEFAULT_MIN_POOL_SIZE            = 1;
    private static final short  DEFAULT_MAX_POOL_SIZE            = 10;
    // private static final short DEFAULT_INACTIVITY_TIMEOUT = 60;

    protected PoolDataSource      poolDS                           = null;

    // private OracleDataSource dataSource = null;

    public AbstractOracleConnection(int type) {
        super();
        setDriver(ConfigConst.DRIVER_ORACLE);
        setDbType(type);
    }

    public AbstractOracleConnection(int type, String host, String name,
            String schema, String user, String pass) {
        super(host, name, schema, user, pass);
        setDriver(ConfigConst.DRIVER_ORACLE);
        setDbType(type);

    }

    /************************* Begin Abstract methods ******************/
    /**
     * @return the ClassDevice content
     */
    protected abstract String getClassDeviceName();

    protected abstract String getDbName();

    /************************* End Abstract methods ******************/

    /**
     * <b>Description : </b> Gets the current database's autocommit <br>
     *
     * @return True if database is in autocommit mode
     */
    public boolean getAutoCommit() {
        return ConfigConst.AUTO_COMMIT_ORACLE;
    }

    /**
     * @return the Tnsname content
     */
    private String constructURLString(boolean rac) throws ArchivingException {
        StringBuffer url = new StringBuffer(ConfigConst.DRIVER_ORACLE);
        url.append(":@");
        if (rac) {
            String tnsNameVal = GetConf.readStringInDB(getClassDeviceName(),
                    TNSNAME_PROPERTY);
            if (tnsNameVal.isEmpty()) {
                String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
                String reason = "Failed while executing AbstractOracleConnection.constructURLString() method...";
                String desc = "The " + TNSNAME_PROPERTY
                        + " property mustn't be empty";
                throw new ArchivingException(message, reason,
                        ErrSeverity.PANIC, desc, "");
            }
            url.append(tnsNameVal);
        } else {
            url.append(getHost() + ":" + ConfigConst.ORACLE_PORT + ":"
                    + getName());
        }
        return url.toString();
    }

    /**
     * @return the ONS configuration value content
     */
    private String constructONSString() throws ArchivingException {
        String onsConfVal = GetConf.readStringInDB(getClassDeviceName(),
                ONSCONF_PROPERTY);
        if (onsConfVal.isEmpty()) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
            String reason = "Failed while executing AbstractOracleConnection.constructONSString() method...";
            String desc = "The " + ONSCONF_PROPERTY
                    + " property mustn't be empty";
            throw new ArchivingException(message, reason, ErrSeverity.PANIC,
                    desc, "");
        }
        return "nodes=" + onsConfVal;
    }

    /*
     * public void connect(boolean rac) throws ArchivingException { // Load the
     * driver try {
     *
     * Class.forName("oracle.jdbc.driver.OracleDriver"); // pilote Oracle String
     * url = rac ? "jdbc:oracle:oci8:@" + getName() : ConfigConst.DRIVER_ORACLE
     * + ":@" + getHost() + ":" + ConfigConst.ORACLE_PORT + ":" + getName(); //
     * pilote // Oracle; String driver = rac ? "oracle.jdbc.driver.OracleDriver"
     * : ConfigConst.DRIVER_ORACLE; setDriver(driver); //
     * setM_dataSource(PoolConnectionFactory.getInstance(getDbType()) //
     * .getPoolInstance(url, getUser(), getPasswd())); if (m_dataSource == null)
     * { Connection conn = null; try { String minPoolSize, maxPoolSize; String
     * inactivityTimeout; String classDevice;
     *
     * // Used to test the Cnx, otherwise the cnx pb will be visible at the
     * first GetConnection method call conn = DriverManager.getConnection(url,
     * getUser(), getPasswd());
     *
     * classDevice = getClassDeviceName();
     *
     * minPoolSize = GetConf.readStringInDB(classDevice,
     * MIN_POOL_SIZE_PROPERTY); maxPoolSize =
     * GetConf.readStringInDB(classDevice, MAX_POOL_SIZE_PROPERTY);
     * inactivityTimeout = GetConf.readStringInDB(classDevice,
     * INACTIVITY_TIMEOUT_SIZE_PROPERTY);
     *
     * System.out.println("minPoolSize = " + minPoolSize + " maxPoolSize = " +
     * maxPoolSize + " inactivityTimeout = " + inactivityTimeout);
     *
     * m_dataSource = new OracleDataSource();
     * ((OracleDataSource)m_dataSource).setURL(url);
     * ((OracleDataSource)m_dataSource).setUser(getUser());
     * ((OracleDataSource)m_dataSource).setPassword(getPasswd());
     *
     * ((OracleDataSource)m_dataSource).setConnectionCachingEnabled(true);
     * ((OracleDataSource)m_dataSource).setConnectionCacheName(getDbName() + "_"
     * + getUser()); Properties cacheProps = new Properties(); // connection
     * cacheProps.setProperty("InitialLimit", minPoolSize); // "1");
     * cacheProps.setProperty("ConnectionWaitTimeout", "1");
     * cacheProps.setProperty("ValidateConnection", "true");
     * cacheProps.setProperty("MinLimit", minPoolSize); // "1");
     * cacheProps.setProperty("MaxLimit", maxPoolSize); // "8");
     *
     * cacheProps.setProperty("InactivityTimeout", inactivityTimeout); //
     * "7200"); cacheProps.setProperty("AbandonedConnectionTimeout", "900"); //
     * seconds cacheProps.setProperty("MaxStatementLimit", "5"); // for one
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * ((OracleDataSource)m_dataSource).setConnectionCacheProperties(cacheProps);
     *
     * } catch (SQLException e) { String message =
     * GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "CONNECTION FAILED !";
     * String reason =
     * "Failed while executing AbstractOracleConnection.connect() method...";
     * String desc =
     * "Failed while connecting to the Oracle archiving database with url = " +
     * url; throw new ArchivingException(message, reason, ErrSeverity.PANIC,
     * desc, "", e);
     *
     * }finally{ closeConnection(conn); conn = null; } } } catch
     * (ClassNotFoundException e) { String message =
     * GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.DRIVER_MISSING;
     * String reason =
     * "Failed while executing AbstractOracleConnection.connect() method...";
     * String desc = "No Oracle driver available..., please check !"; throw new
     * ArchivingException(message, reason, ErrSeverity.PANIC, desc, "", e); } }
     */
    // The call to the alterSession method implies the getConnection()
    // overwrite.
    // public Connection getConnection() {
    // if (m_dataSource == null)
    // return null;
    // try {
    // Connection conn = m_dataSource.getConnection();
    // alterSession(conn);
    // return conn;
    //
    // } catch (SQLException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // return null;
    // } catch (ArchivingException e) {
    // return null;
    // }
    //
    // }
    /**
     * <b>Description : </b> Allows to connect to the <I>HDB/TDB</I> database
     * when of <I>Oracle</I> type.
     *
     * @throws ArchivingException
     */
    public void connect(boolean rac) throws ArchivingException {

        String url = "";
        String ons = "";
        int minPoolSize;
        int maxPoolSize;
        int inactivityTimeout;
        String classDevice;
        String sreadConf;

        url = constructURLString(rac);
        if (rac) {
            ons = constructONSString();
        }
        classDevice = getClassDeviceName();
        try {
            sreadConf = GetConf.readStringInDB(classDevice,
                    MIN_POOL_SIZE_PROPERTY);
            minPoolSize = Integer.parseInt(sreadConf);

            sreadConf = GetConf.readStringInDB(classDevice,
                    MAX_POOL_SIZE_PROPERTY);
            maxPoolSize = Integer.parseInt(sreadConf);

            sreadConf = GetConf.readStringInDB(classDevice,
                    INACTIVITY_TIMEOUT_SIZE_PROPERTY);
            inactivityTimeout = Integer.parseInt(sreadConf);

            // minPoolSize = GetConf.readShortInDB(classDevice,
            // MIN_POOL_SIZE_PROPERTY, DEFAULT_MIN_POOL_SIZE);
            // maxPoolSize = GetConf.readShortInDB(classDevice,
            // MAX_POOL_SIZE_PROPERTY, DEFAULT_MAX_POOL_SIZE);
            // inactivityTimeout = GetConf.readShortInDB(classDevice,
            // INACTIVITY_TIMEOUT_SIZE_PROPERTY,
            // DEFAULT_INACTIVITY_TIMEOUT);

            if (minPoolSize >= maxPoolSize) {
                // utilisation des valeurs par d�faut
                minPoolSize = DEFAULT_MIN_POOL_SIZE;
                maxPoolSize = DEFAULT_MAX_POOL_SIZE;
            }
            System.out.println("minPoolSize = " + minPoolSize
                    + " maxPoolSize = " + maxPoolSize + " inactivityTimeout = "
                    + inactivityTimeout);
            System.out.println("url = " + url + " ons = " + ons);

            poolDS = PoolDataSourceFactory.getPoolDataSource();
            poolDS
                    .setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
            poolDS.setURL(url);
            poolDS.setUser(getUser());
            poolDS.setPassword(getPasswd());
            poolDS.setConnectionPoolName(getDbName() + "_UCP_POOL");

            poolDS.setConnectionWaitTimeout(10);
            poolDS.setInactiveConnectionTimeout(inactivityTimeout);
            poolDS.setInitialPoolSize(minPoolSize);
            poolDS.setMinPoolSize(minPoolSize);
            poolDS.setMaxPoolSize(maxPoolSize);

            //Harvest
            poolDS.setConnectionHarvestTriggerCount(maxPoolSize / 2);
            poolDS.setConnectionHarvestMaxCount(maxPoolSize / 2);


            poolDS.setMaxStatements(10);
            // Lib�rer les connexions invalides
            poolDS.setValidateConnectionOnBorrow(true);

            if (rac) {
                poolDS.setONSConfiguration(ons);
                poolDS.setFastConnectionFailoverEnabled(true);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                    + "Oracle Connection Failed";
            String reason = "Failed while executing AbstractOracleConnection.connect() method...";
            String desc = "url =  " + url + "\n ons = " + ons;
            throw new ArchivingException(message, reason, ErrSeverity.PANIC,
                    desc, "", e);
        } catch (Exception e) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                    + "Unknown Exception";
            String reason = "Failed while executing AbstractOracleConnection.connect() method...";
            String desc = "Except : " + e.toString();
            throw new ArchivingException(message, reason, ErrSeverity.PANIC,
                    desc, "", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection
     * #getConnection()
     */
    public Connection getConnection() {

        if (poolDS == null) {
            return null;
        }
        // try {
        // Connection conn = poolDS.getConnection();
        // alterSession(conn);
        // return conn;
        //
        // } catch (SQLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // return null;
        // } catch (ArchivingException e) {
        // return null;
        // }
        boolean retry = false;
        Connection conn = null;
        int nbTry = 1;
        do {
            try {
                conn = poolDS.getConnection();
                alterSession(conn);
                retry = false;
            } catch (SQLException e) {
                e.printStackTrace();
                if (conn != null) {
                    ValidConnection vconn = (ValidConnection) conn;
                    try {
                        if (!vconn.isValid()) {
                            System.out.println("SetInvalid a connexion");
                            vconn.setInvalid();
                            retry = true;
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } finally {
                        nbTry++;
                    }
                } else {
                    retry = true;
                    nbTry++;
                }
            } catch (ArchivingException e) {
                return null;
            }
        } while (retry && nbTry <= 3);

        if (nbTry > 3) {
            return null;
        }

        return conn;
    }

    private void DisplayCacheInfo() {
        // Display pooling cache info
        // OracleConnectionCacheManager occm =
        // OracleConnectionCacheManager.getConnectionCacheManagerInstance();
        // // getM_dataSource().getConnection();
        // System.out.println("------------------------------------------------------------------");
        // System.out.println("Cache name: " +
        // ((OracleDataSource)getM_dataSource()).getConnectionCacheName());
        // System.out.println("Active connections : " +
        // occm.getNumberOfActiveConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
        // System.out.println("Connections available: " +
        // occm.getNumberOfAvailableConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
        // System.out.println("------------------------------------------------------------------");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection
     * #closeConnection(com.mysql.jdbc.Connection)
     */
    public void closeConnection(Connection conn) {
        // TODO Auto-generated method stub
        try {
            if (conn != null) {
                conn.close();
                // conn = null; inutile
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method is used when connecting an Oracle database. It tunes the
     * connection to the database.
     *
     * @throws ArchivingException
     */
    protected void alterSession(Connection conn) throws ArchivingException {
        if (conn == null)
            return;
        Statement stmt = null;
        String sqlStr1, sqlStr2, sqlStr3;
        sqlStr1 = "alter session set NLS_NUMERIC_CHARACTERS = \". \"";
        sqlStr2 = "alter session set NLS_TIMESTAMP_FORMAT = 'DD-MM-YYYY HH24:MI:SS.FF'";
        sqlStr3 = "alter session set NLS_DATE_FORMAT = 'DD-MM-YYYY HH24:MI:SS'";
        try {
            // conn = getConnection();
            stmt = conn.createStatement();
            lastStatement = stmt;
            stmt.executeQuery(sqlStr1);
            stmt.executeQuery(sqlStr2);
            stmt.executeQuery(sqlStr3);
        } catch (SQLException e) {
            String message = DbUtilsFactory.getInstance(getDbType())
                    .getCommunicationFailureMessage(e);

            String reason = GlobalConst.STATEMENT_FAILURE;
            String desc = "Failed while executing OracleConnection.alterSession() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN,
                    desc, this.getClass().getName(), e);
        } finally {
            ConnectionCommands.close(stmt);
            stmt = null;
            // try {
            //
            // ConnectionCommands.close(stmt);
            // // closeConnection(conn);
            // } catch (SQLException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }
    }
}
