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
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public abstract class AbstractOracleConnection extends DBConnection {

    private static final String ALTER_SESSION_SET_NLS_DATE_FORMAT_DD_MM_YYYY_HH24_MI_SS = "alter session set NLS_DATE_FORMAT = 'DD-MM-YYYY HH24:MI:SS'";
    private static final String ALTER_SESSION_SET_NLS_TIMESTAMP_FORMAT_DD_MM_YYYY_HH24_MI_SS_FF = "alter session set NLS_TIMESTAMP_FORMAT = 'DD-MM-YYYY HH24:MI:SS.FF'";
    private static final String ALTER_SESSION_SET_NLS_NUMERIC_CHARACTERS = "alter session set NLS_NUMERIC_CHARACTERS = \". \"";
    private static final String TNSNAME_PROPERTY = "DbTnsNames";
    private static final String ONSCONF_PROPERTY = "DbONSConf";
    private static final String MIN_POOL_SIZE_PROPERTY = "DbMinPoolSize";
    private static final String MAX_POOL_SIZE_PROPERTY = "DbMaxPoolSize";
    private static final String INACTIVITY_TIMEOUT_SIZE_PROPERTY = "DbCnxInactivityTimeout";
    private static final short DEFAULT_MIN_POOL_SIZE = 1;
    private static final short DEFAULT_MAX_POOL_SIZE = 10;
    // private static final short DEFAULT_INACTIVITY_TIMEOUT = 60;

    protected PoolDataSource poolDS;

    public AbstractOracleConnection(final int type) {
	super();
	setDriver(ConfigConst.DRIVER_ORACLE);
	setDbType(type);
    }

    public AbstractOracleConnection(final int type, final String host, final String name,
	    final String schema, final String user, final String pass) {
	super(host, name, schema, user, pass);
	setDriver(ConfigConst.DRIVER_ORACLE);
	setDbType(type);

    }

    /**
     * @return the ClassDevice content
     */
    protected abstract String getClassDeviceName();

    protected abstract String getDbName();

    // /**
    // * <b>Description : </b> Gets the current database's autocommit <br>
    // *
    // * @return True if database is in autocommit mode
    // */
    // public boolean getAutoCommit() {
    // return ConfigConst.AUTO_COMMIT_ORACLE;
    // }
    /**
     * @return the Tnsname content
     */
    private String constructURLString(final boolean rac) throws ArchivingException {
	final StringBuffer url = new StringBuffer(ConfigConst.DRIVER_ORACLE);
	url.append(":@");
	if (rac) {
	    final String tnsNameVal = GetConf
		    .readStringInDB(getClassDeviceName(), TNSNAME_PROPERTY);
	    if (tnsNameVal.isEmpty()) {
		final String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
		final String reason = "Failed while executing AbstractOracleConnection.constructURLString() method...";
		final String desc = "The " + TNSNAME_PROPERTY + " property mustn't be empty";
		throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "");
	    }
	    url.append(tnsNameVal);
	} else {
	    url.append(getHost() + ":" + ConfigConst.ORACLE_PORT + ":" + getName());
	}
	return url.toString();
    }

    /**
     * @return the ONS configuration value content
     */
    private String constructONSString() throws ArchivingException {
	final String onsConfVal = GetConf.readStringInDB(getClassDeviceName(), ONSCONF_PROPERTY);
	if (onsConfVal.isEmpty()) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
	    final String reason = "Failed while executing AbstractOracleConnection.constructONSString() method...";
	    final String desc = "The " + ONSCONF_PROPERTY + " property mustn't be empty";
	    throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "");
	}
	return "nodes=" + onsConfVal;
    }

    /**
     * <b>Description : </b> Allows to connect to the <I>HDB/TDB</I> database
     * when of <I>Oracle</I> type.
     * 
     * @throws ArchivingException
     */
    @Override
    public void connect() throws ArchivingException {

	final boolean rac = isRacConnection();
	final String url = constructURLString(rac);
	String ons = "";
	if (rac) {
	    ons = constructONSString();
	}
	try {
	    int minPoolSize = getConf(MIN_POOL_SIZE_PROPERTY, DEFAULT_MIN_POOL_SIZE);
	    int maxPoolSize = getConf(MAX_POOL_SIZE_PROPERTY, DEFAULT_MAX_POOL_SIZE);
	    final int inactivityTimeout = getConf(INACTIVITY_TIMEOUT_SIZE_PROPERTY, 1);
	    if (minPoolSize >= maxPoolSize) {
		minPoolSize = DEFAULT_MIN_POOL_SIZE;
		maxPoolSize = DEFAULT_MAX_POOL_SIZE;
	    }

	    System.out.println("minPoolSize = " + minPoolSize + " maxPoolSize = " + maxPoolSize
		    + " inactivityTimeout = " + inactivityTimeout);

	    // final UniversalConnectionPoolManager mgr =
	    // UniversalConnectionPoolManagerImpl
	    // .getUniversalConnectionPoolManager();
	    //
	    // mgr.setLogLevel(Level.FINEST);

	    poolDS = PoolDataSourceFactory.getPoolDataSource();
	    poolDS.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
	    poolDS.setURL(url);
	    poolDS.setUser(getUser());
	    poolDS.setPassword(getPasswd());
	    poolDS.setConnectionPoolName(getDbName() + "_UCP_POOL");
	    poolDS.setConnectionWaitTimeout(10);
	    poolDS.setInactiveConnectionTimeout(inactivityTimeout);
	    poolDS.setInitialPoolSize(minPoolSize);
	    poolDS.setMinPoolSize(minPoolSize);
	    poolDS.setMaxPoolSize(maxPoolSize);
	    poolDS.setMaxStatements(10);
	    poolDS.setValidateConnectionOnBorrow(true);
	    if (isHarvestable()) {
		poolDS.setConnectionHarvestTriggerCount(maxPoolSize / 2);
		poolDS.setConnectionHarvestMaxCount(maxPoolSize / 2);
	    }
	    if (rac) {
		System.out.println("USE RAC - url = " + url + " ons = " + ons);
		poolDS.setONSConfiguration(ons);
		poolDS.setFastConnectionFailoverEnabled(true);
	    }
	} catch (final SQLException e) {
	    System.err.println("SQLException during AbstractOracleConnection.connect()");
	    e.printStackTrace();
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
		    + "Oracle Connection Failed";
	    final String reason = "Failed while executing AbstractOracleConnection.connect() method...";
	    final String desc = "url =  " + url + "\n ons = " + ons;
	    throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	}
    }

    private int getConf(final String propName, final int defaultValue) throws ArchivingException {
	int result = defaultValue;
	final String sreadConf = GetConf.readStringInDB(getClassDeviceName(), propName);
	try {
	    result = Integer.parseInt(sreadConf);
	} catch (final NumberFormatException e) {
	    // ignore
	}
	return result;
    }

    @Override
    public Connection getConnection() throws ArchivingException {

	if (poolDS == null) {
	    throw new ArchivingException("Oracle pool is null", "Oracle pool is not initialized",
		    ErrSeverity.PANIC, "Oracle pool is not initialized", this.getClass().getName()
			    + ".getConnection");
	}

	int nbTry = 1;
	Connection conn = null;
	SQLException occuredError = null;
	do {
	    try {
		// System.out.println("CREATE CONNECTION "
		// + poolDS.getAvailableConnectionsCount());
		// System.out.println(" borrowed: " +
		// poolDS.getBorrowedConnectionsCount());
		conn = poolDS.getConnection();
		if (!isAutoCommit()) {
		    // With harvestable connections some of them can be in
		    // auto_commit=false
		    conn.setAutoCommit(true);
		}
		alterSession(conn);
		break;
	    } catch (final SQLException e) {
		System.err.println("ALARM : GetConnection ERROR");
		e.printStackTrace();
		occuredError = e;
		if (conn != null) {
		    final ValidConnection vconn = (ValidConnection) conn;
		    try {
			if (!vconn.isValid()) {
			    System.err.println("SetInvalid a connexion");
			    vconn.setInvalid();
			    closeConnection(conn);
			}
		    } catch (final SQLException e1) {
			occuredError = e1;
			e1.printStackTrace();
		    }
		    nbTry++;
		}
	    }
	} while (nbTry <= 3);

	if (nbTry > 3) {
	    closeConnection(conn);
	    throw new ArchivingException("Failed to build connection",
		    "Oracle pool is not able to connect to DB", ErrSeverity.PANIC,
		    "Oracle  pool is not  able to connect to DB", this.getClass().getName()
			    + ".getConnection", occuredError);
	}
	return conn;
    }

    // private void DisplayCacheInfo() {
    // Display pooling cache info
    // final OracleConnectionCacheManager occm =
    // OracleConnectionCacheManager
    // .getConnectionCacheManagerInstance();
    // getM_dataSource().getConnection();
    // System.out.println("------------------------------------------------------------------");
    // System.out.println("Cache name: " +
    // ((OracleDataSource)getM_dataSource()).getConnectionCacheName());
    // System.out.println("Active connections : " +
    // occm.getNumberOfActiveConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
    // System.out.println("Connections available: " +
    // occm.getNumberOfAvailableConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
    // System.out.println("------------------------------------------------------------------");
    // }

    @Override
    public void closeConnection(final Connection conn) {
	try {
	    if (conn != null && !conn.isClosed()) {
		conn.close();
	    }
	} catch (final SQLException e) {
	    // ignore
	    e.printStackTrace();
	}
    }

    /**
     * This method is used when connecting an Oracle database. It tunes the
     * connection to the database.
     * 
     * @throws SQLException
     */
    private void alterSession(final Connection conn) throws SQLException {
	if (conn != null) {
	    Statement stmt = null;
	    try {
		stmt = conn.createStatement();
		lastStatement = stmt;
		stmt.executeQuery(ALTER_SESSION_SET_NLS_NUMERIC_CHARACTERS);
		stmt.executeQuery(ALTER_SESSION_SET_NLS_TIMESTAMP_FORMAT_DD_MM_YYYY_HH24_MI_SS_FF);
		stmt.executeQuery(ALTER_SESSION_SET_NLS_DATE_FORMAT_DD_MM_YYYY_HH24_MI_SS);
	    } finally {
		if (stmt != null && !stmt.isClosed()) {
		    stmt.close();
		}
	    }
	}
    }
}
