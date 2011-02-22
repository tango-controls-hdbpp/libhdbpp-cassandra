//+======================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoSnapshoting/SnapshotingApi/DataBaseAPI.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DataBaseAPI.
//						(Chinkumo Jean) - Mar 4, 2003
//
// $Author: pierrejoseph $
//
// $Revision: 1.40 $
//
// $Log: DataBaseAPI.java,v $
// Revision 1.40  2007/07/19 13:54:17  pierrejoseph
// Utilisation de resultSet.getInt(1) != 0 au lieu de  resultSet.getBoolean car probl�me face � Oracle
//
// Revision 1.39  2007/06/29 09:20:31  ounsy
// devLong represented as Integer
//
// Revision 1.38  2007/06/26 13:07:51  pierrejoseph
// dataSource usage instead of DriverManager with a MySql Database only : allows a Pool connections usage
//
// Revision 1.37  2007/04/19 13:31:50  ounsy
// corrected extract_SpectrumData_RW_MySQL and the smae for images
//
// Revision 1.36  2007/04/19 13:07:30  chinkumo
// corrected extract_SpectrumData_RO_MySQL
//
// Revision 1.35  2007/04/18 09:15:50  ounsy
// removed trace
//
// Revision 1.34  2007/04/05 10:16:03  ounsy
// corrected a Oracle bug in getAttDefinitionData
//
// Revision 1.33  2007/03/14 15:40:53  ounsy
// added statement closing in finally clauses
//
// Revision 1.32  2007/02/28 09:47:51  ounsy
// now checks the right table
//
// Revision 1.31  2007/01/11 14:37:25  ounsy
// correction of a ClassCastException
//
// Revision 1.30  2007/01/11 14:33:25  ounsy
// correction of a ClassCastException
//
// Revision 1.29  2006/10/31 16:54:24  ounsy
// milliseconds and null values management
//
// Revision 1.28  2006/06/28 12:43:58  ounsy
// image support
//
// Revision 1.27  2006/06/16 08:51:52  ounsy
// ready for images
//
// Revision 1.26  2006/05/12 09:25:19  ounsy
// CLOB_SEPARATOR in GlobalConst
//
// Revision 1.25  2006/05/04 14:33:17  ounsy
// CLOB_SEPARATOR centralized in ConfigConst
//
// Revision 1.24  2006/04/13 12:47:24  ounsy
// new spectrum types support
//
// Revision 1.23  2006/04/11 14:35:26  ounsy
// new spectrum types support
//
// Revision 1.22  2006/03/29 15:06:54  ounsy
// Improved the way cursor handles are closed
// (ResultSet and PreparedStatement objects are now close in a finally clause)
//
// Revision 1.21  2006/03/29 10:24:50  ounsy
// corrected insert_ScalarData in the case of  Longs
//
// Revision 1.20  2006/03/16 15:29:08  ounsy
// String scalar support
//
// Revision 1.19  2006/03/14 13:16:48  ounsy
// removed useless logs
//
// Revision 1.18  2006/03/14 12:33:20  ounsy
// corrected the SNAP/spectrums/RW problem
// about the read and write values having the same length
//
// Revision 1.17  2006/03/07 13:45:03  ounsy
// take care of clob separator
//
// Revision 1.16  2006/03/07 10:04:04  ounsy
// take care of clob separator
//
// Revision 1.15  2006/02/28 17:05:58  chinkumo
// no message
//
// Revision 1.14  2006/02/24 12:06:10  ounsy
// replaced hard-coded "," value to CLOB_SEPARATOR
//
// Revision 1.13  2006/02/17 13:24:19  ounsy
// stable version for spectrum extraction (MySQL/Oracle split)
//
// Revision 1.12  2006/02/17 12:47:23  ounsy
// small logs modification
//
// Revision 1.11  2006/02/17 11:15:35  chinkumo
// no message
//
// Revision 1.10  2006/02/17 10:50:29  ounsy
// splitted the extract_spectrum methods MySQl/Oracle
//
// Revision 1.9  2006/02/17 09:32:35  chinkumo
// Since the structure and the name of some SNAPSHOT database's table changed, this was reported here.
//
// Revision 1.8  2006/02/15 09:04:46  ounsy
// Spectrums Management
//
// Revision 1.7  2005/11/29 17:11:17  chinkumo
// no message
//
// Revision 1.5.2.2  2005/11/15 13:34:38  chinkumo
// no message
//
// Revision 1.5.2.1  2005/09/09 08:43:53  chinkumo
// no message
//
// Revision 1.6  2005/08/29 08:08:52  chinkumo
// In the getContextAssociatedSnapshots method, the preparedStatement's argin parameters (id_context, id_snap) were swapped.
//
// Revision 1.5  2005/08/19 14:04:02  chinkumo
// no message
//
// Revision 1.4.6.1.2.2  2005/08/12 08:08:35  chinkumo
// Unused constants removed.
//
// Revision 1.4.6.1.2.1  2005/08/11 08:37:54  chinkumo
// The 'extract_ScalarData_XX' method was improved : The attributes types are better managed.
//
// Revision 1.4.6.1  2005/08/01 13:49:57  chinkumo
// Several changes carried out for the support of the new graphical application (Bensikin).
//
// Revision 1.4  2005/06/28 09:10:16  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.3  2005/06/24 12:04:46  chinkumo
// crateNewSnap method was renamed into createNewSnap
// crateNewSnapMySQL method was renamed into createNewSnapMySQL
// crateNewSnapOracle method was renamed into createNewSnapOracle
// crateNewSnap method was renamed into createNewSnap
// crateNewSnap method was renamed into createNewSnap
// crateNewSnap method was renamed into createNewSnap.
//
// Revision 1.2  2005/06/14 10:12:18  chinkumo
// Branch (tangORBarchiving_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.1.4.2  2005/05/11 14:41:35  chinkumo
// Some comments were corrected.
//
// Revision 1.1.4.1  2005/04/12 13:53:38  chinkumo
// The management of null values (Double.NaN) was improved.
// Some changes was made in the insertion part to improve its efficiency (String > BufferString).
//
// Revision 1.1  2005/01/26 15:35:38  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 17:39:56  chinkumo
// First commit (new API architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.snapArchivingApi.SnapshotingApi;

/*
 * Import classes
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.ScalarEvent;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeHeavy;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapImageEvent_RO;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapImageEvent_RW;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapScalarEvent_RO;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapScalarEvent_RW;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapScalarEvent_WO;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapSpectrumEvent_RO;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapSpectrumEvent_RW;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * <b>Description : </b> This class hides the loading of drivers, the
 * connection/disconnection with the database, and more generaly, this class
 * hides number of methods to insert, update and query <I>Snap</I>.
 * 
 * @author Jean CHINKUMO
 * @version 1.0
 */
public class DataBaseAPI {
    /*
     * Members
     */
    /**
     * Connection dbatabase type (<I>MySQL</I>, <I>Oracle</I>, ...)
     */
    private static int db_type;
    /*
     * Port number for the connection private static int port;
     */
    /* JDBC driver used for the connection */
    private static String driver;
    /* Database Host adress */
    private static String host;
    /* User's name for the connection */
    private static String user;
    /* User's password for the connection */
    private static String passwd;
    /* database name */
    private static String name;
    /**
     * database'schema' used
     */
    private String schema;
    /* Connection object used */
    private static Connection dbconn;

    private DataSource m_dataSource = null;

    // True if connection and disconnection are made once
    // False if the connection are or made for every request,
    private static boolean autoConnect;

    private static String temporarilyPath;

    private int currentSpectrumDimX;

    /*
     * Contructors
     */

    /**
     * Default constructor.
     */
    public DataBaseAPI() {
    }

    /**
     * Constructor using host name, user name, password and database name.
     * 
     * @param host_name
     *            Identifier (name or IP adress) of the machine which supplies
     *            the service "data base <I>Snap</I>"
     * @param host_name
     *            Identifier (name or IP adress) of the machine which supplies
     *            the service "data base <I>HDB</I>"
     * @param db_name
     *            database name
     * @param db_schema
     *            Name of the database's schema used
     */
    public DataBaseAPI(final String host_name, final String db_name, final String db_schema) {
	setHost(host_name);
	setDbName(db_name);
	if (!db_schema.equals("")) {
	    setDbShema(db_schema);
	} else {
	    setDbShema(db_name);
	}
    }

    /**
     * Constructor using host name, user name, password and database name.
     * 
     * @param host_name
     *            Identifier (name or IP adress) of the machine which supplies
     *            the service data base
     * @param db_name
     *            Name of the data base used
     * @param db_schema
     *            Name of the database's schema used
     * @param user_name
     *            Name to use to connect
     * @param password
     *            Password to use to connect
     */
    public DataBaseAPI(final String host_name, final String db_name, final String db_schema,
	    final String user_name, final String password) {
	this(host_name, db_name, db_schema);
	setUser(user_name);
	setPassword(password);
    }

    /*****************************************************************************
     * 
     * 
     * Generals methods used for the connection
     * 
     * 
     ****************************************************************************/

    // Getter methods
    /**
     * <b>Description : </b> Gets the database name
     * 
     * @return The database name
     */
    public String getDbName() {
	return name;
    }

    /**
     * <b>Description : </b> Gets the database's schema name
     * 
     * @return The database name
     */
    public String getDbSchema() {
	return schema;
    }

    /**
     * <b>Description : </b> Gets the driver's name for the connection
     * 
     * @return The driver used for the connection
     */
    public String getDriver() {
	return driver;
    }

    /**
     * <b>Description : </b> Returns the connected database host identifier.
     * 
     * @return The host where the connection is done
     */
    public String getHost() {
	return host;
    }

    /**
     * <b>Description : </b> Gets the current user's name for the connection
     * 
     * @return The user's name for the connection
     */
    public String getUser() {
	return user;
    }

    /**
     * <b>Description : </b> Gets the current user's password for the connection
     * 
     * @return The user's password for the connection
     */
    public String getPassword() {
	return passwd;
    }

    /**
     * <b>Description : </b> Gets the type of database being used (<I>MySQL</I>,
     * <I>Oracle</I>, ...)
     * 
     * @return The type of database being used
     */
    public int getDb_type() {
	return db_type;
    }

    public static String getTemporarilyPath() {
	return temporarilyPath;
    }

    public static void setTemporarilyPath(final String temporarilyPath) {
	DataBaseAPI.temporarilyPath = temporarilyPath;
    }

    /**
     * <b>Description</b> : Returns misc informations about the database and a
     * set of parameters characterizing the connection. <br>
     * 
     * @return miscellaneous informations about the database and a set of
     *         parameters characterizing the connection.
     * @throws SnapshotingException
     */
    public String getInfo() throws SnapshotingException {
	String msg = null;
	// Then get info from the database
	try {
	    final DatabaseMetaData db_info = dbconn.getMetaData();

	    msg = "\tUser : " + db_info.getUserName() + "\n\tdatabase name : " + name;
	    msg = msg + "\n\tdatabase product : " + db_info.getDatabaseProductVersion();
	    msg = msg + "\n\tURL : " + db_info.getURL();
	    msg = msg + "\n\tDriver name : " + db_info.getDriverName();
	    msg = msg + " Version " + db_info.getDriverVersion();
	    msg = msg + "\n\tdatabase modes : ";
	    if (dbconn.getAutoCommit()) {
		msg = msg + "AUTO COMMIT ; ";
	    } else {
		msg = msg + "MANUAL COMMIT ; ";
	    }

	    if (dbconn.isReadOnly()) {
		msg = msg + "READ ONLY ; ";
	    } else {
		msg = msg + "READ-WRITE ; ";
	    }

	    if (db_info.usesLocalFiles()) {
		msg = msg + "USES LOCAL FILES";
	    } else {
		msg = msg + "DONT USE LOCAL FILES";
	    }

	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }
	    // String message = "";
	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.getInfo() method...";
	    throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	}
	return msg;

    }

    /**
     * <b>Description : </b> Gets the current used connection's object
     * 
     * @return The connection object return when connecting
     */
    public Connection getConnectionObject() {
	if (dbconn == null) {
	    String msg = "Error detected :\n";
	    msg = msg + "---> Class : DataBaseAPI\n" + "---> Method : getConnectionObject\n";
	    msg = msg + "---> Description : Impossible to connect with the database.\n";
	    msg = msg + "                   The connection object returned is NULL ... exiting.";
	    System.out.println(msg);
	    System.exit(-1);
	}
	return dbconn;
    }

    /**
     * <b>Description : </b> Gets the current connection's type <br>
     * 
     * @return True if connection and disconnection are made for every request,
     *         False if the connection and disconnection are made once
     */
    public boolean getAutoConnect() {
	return autoConnect;
    }

    // Setter methods
    /**
     * <b>Description : </b> Sets the database name <br>
     * 
     * @param db_name
     *            The database name
     */
    public void setDbName(final String db_name) {
	name = db_name;
    }

    /**
     * <b>Description : </b> Sets the database name <br>
     * 
     * @param db_shema
     *            The database shema used
     */
    public void setDbShema(final String db_shema) {
	schema = db_shema;
    }

    /**
     * <b>Description : </b> Sets the database type (<I>MySQL</I>,
     * <I>Oracle</I>, ...) <br>
     * 
     * @param db_type
     *            The database name
     */
    public void setDb_type(final int db_type) {
	DataBaseAPI.db_type = db_type;
    }

    /**
     * <b>Description : </b> Specifies a driver for the connection
     * 
     * @param driver_name
     *            The driver's name to use for the connection
     */
    public void setDriver(final String driver_name) {
	driver = driver_name;
    }

    /**
     * <b>Description : </b> Sets the host to connect
     * 
     * @param host_name
     *            The host to connect
     */
    public void setHost(final String host_name) {
	host = host_name;
    }

    /**
     * <b>Description : </b> Sets the current user's name for the connection
     * 
     * @param user_name
     *            The user's name for the connection
     */
    public void setUser(final String user_name) {
	user = user_name;
    }

    /**
     * <b>Description : </b> Sets a password for the connection
     * 
     * @param password
     *            The password for the connection
     */
    public void setPassword(final String password) {
	passwd = password;
    }

    /**
     * <b>Description : </b> Sets the current connection's mode
     * 
     * @param value
     *            <li><I>True</I> : implies that connection and disconnection
     *            are made for every request, <li><I>False</I> : implies that
     *            connection and disconnection are made once
     */
    public void setAutoConnect(final boolean value) {
	autoConnect = value;
    }

    /**
     * ************************************************************************
     * <b>Description</b> : Methods that counts the number of non null rows in
     * an array
     * 
     * @param arr
     *            an array of Strings
     * @throws NullPointerException
     *             **************************************************************
     *             ***********
     */
    public int getArrayCount(final String[] arr) {
	int arrayCount = 0;
	try {
	    for (int i = 0; i < arr.length; i++) {
		if (!arr[i].equals(null)) {
		    arrayCount++;
		}
	    }
	} catch (final NullPointerException e) {
	}
	return arrayCount;
    }

    /**
     * ************************************************************************
     * <b>Description</b> : Sets the auto commit mode to "true" or "false"
     * 
     * @param value
     *            The mode value
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public void setAutoCommit(final boolean value) throws SnapshotingException {
	// Set commit mode to manual
	try {
	    dbconn.setAutoCommit(value);
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : ";
	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.setAutoCommit() method...";
	    throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	}
    }

    /**
     * ************************************************************************
     * <b>Description</b> : Load an instance of the JDBC driver according to the
     * type of database used (db_type)
     * ************************************************************************
     */
    private void loadDriver() {
	// System.out.println("DataBaseAPI.loadDriver");
	// System.out.println("\tdb_type = " + db_type);
	try {
	    switch (db_type) {
	    case ConfigConst.BD_MYSQL:
		Class.forName("org.gjt.mm.mysql.Driver"); // D�finition du
		// pilote MySQL
		break;
	    case ConfigConst.BD_ORACLE:
		Class.forName("oracle.jdbc.driver.OracleDriver"); // pilote
		// Oracle
		break;
	    case ConfigConst.BD_POSTGRESQL: // pilote PostgreSQL
		Class.forName("org.postgresql.Driver");
		break;
	    }
	} catch (final ClassNotFoundException err) {
	    System.err.println("\nPilote DB non trouv�!");
	    System.err.println(err);
	}
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Build the url string and get a connection object
     * 
     * @throws SnapshotingException
     *             **************************************************************
     *             ***********
     */
    private void buildUrlAndConnect() throws SnapshotingException {
	// Gets connected to the database
	String url = "";
	switch (db_type) {
	case ConfigConst.BD_MYSQL:
	    url = ConfigConst.DRIVER_MYSQL + "://" + host + "/" + name; // D�finition
	    // du
	    // pilote
	    // MySQL
	    break;
	case ConfigConst.BD_ORACLE:
	    url = ConfigConst.DRIVER_ORACLE + ":@" + host + ":" + ConfigConst.ORACLE_PORT + ":"
		    + name; // pilote Oracle
	    break;
	}
	System.out.println("DataBaseApi/buildUrlAndConnect/url|" + url);
	// System.out.println("\turl = " + url);
	try {
	    dbconn = DriverManager.getConnection(url, user, passwd);
	    // System.out.println("DataBaseApi/buildUrlAndConnect/url|"+url);
	    dbconn.setAutoCommit(true);
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + "Error initializing the connection...";
	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.buildUrlAndConnect() method...";
	    throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, e.getClass()
		    .getName(), e);
	} catch (final Exception e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + "Error initializing the connection...";
	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.buildUrlAndConnect() method...";
	    throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, e.getClass()
		    .getName(), e);
	}
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Loads the JDBC driver, connects with the database
     * and initializes. Remark: the type of the base (MySQL/Oracle) must
     * beforehand have been specified...
     * 
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public void connect() throws SnapshotingException {
	// Load the driver
	loadDriver();
	// Gets connected to the database
	buildUrlAndConnect();
	// Set connection's default settings
	setAutoCommit(true);
	System.out.println("SnapInfo : \n" + getInfo());
    }

    /**
     * <b>Description : </b> Allows to connect to the database <I>Snap</I>,
     * independently of its type (<I>mySQL</I>/<I>Oracle</I>)
     */
    public void connect_auto() throws SnapshotingException {
	SnapshotingException archivingException1 = null, archivingException2 = null;
	try {
	    // try oracle
	    connect_oracle();
	    System.out.println("DataBaseApi.connect_auto oracle: " + getUser() + "@" + getHost());
	    return;
	} catch (final SnapshotingException e) {
	    // e.printStackTrace();
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + "CONNECTION FAILED !";
	    final String reason = "Failed while executing DataBaseApi.connect_auto() method...";
	    final String desc = "Failed while connecting to the Oracle archiving database";
	    archivingException1 = new SnapshotingException(message, reason, ErrSeverity.PANIC,
		    desc, "", e);
	}
	try {
	    // try MySQL;
	    connect_mysql();
	    System.out.println("DataBaseApi.connect_auto MySQL: " + getUser() + "@" + getHost());
	    return;
	} catch (final SnapshotingException e) {
	    // e.printStackTrace();
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + "CONNECTION FAILED !";
	    final String reason = "Failed while executing DataBaseApi.connect_auto() method...";
	    final String desc = "Failed while connecting to the MySQL archiving database";
	    archivingException2 = new SnapshotingException(message, reason, ErrSeverity.PANIC,
		    desc, "", e);

	    final String _desc = "Failed while connecting to the archiving database";

	    final SnapshotingException archivingException = new SnapshotingException(message,
		    reason, ErrSeverity.PANIC, _desc, "", archivingException1);
	    archivingException.addStack(message, reason, ErrSeverity.PANIC, _desc, "",
		    archivingException2);
	    throw archivingException;
	}
    }

    /**
     * <b>Description : </b> Allows to connect to the <I>Snap</I> database when
     * of <I>mySQL</I> type.
     * 
     * @throws SnapshotingException
     */
    public void connect_mysql() throws SnapshotingException {
	// Load the driver
	try {
	    Class.forName("org.gjt.mm.mysql.Driver"); // D�finition du pilote
	    // MySQL
	    final String url = ConfigConst.DRIVER_MYSQL + "://" + host + "/" + name
		    + "?autoReconnect=true"; // D�finition du pilote MySQL
	    System.out.println("DataBaseApi.connect_mysql - url: " + url);
	    dbconn = DriverManager.getConnection(url, user, passwd);
	    setAutoCommit(true);
	    setDb_type(ConfigConst.BD_MYSQL);
	    setDriver(ConfigConst.DRIVER_MYSQL);

	    final ObjectPool connectionPool = new GenericObjectPool(null);
	    final ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url,
		    user, passwd);
	    final PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
		    connectionFactory, connectionPool, null, null, false, true);
	    m_dataSource = new PoolingDataSource(connectionPool);

	} catch (final ClassNotFoundException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.DRIVER_MISSING;
	    final String reason = "Failed while executing DataBaseApi.connect_mysql() method...";
	    final String desc = "No MySQL driver available..., please check !";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ADB_CONNECTION_FAILURE;
	    final String reason = "Failed while executing DataBaseApi.connect_mysql() method...";
	    final String desc = e.getMessage().indexOf(GlobalConst.NO_HOST_EXCEPTION) != -1 ? "The 'host' property ("
		    + host + ") might be wrong... please check it..."
		    : "The loggin parameters (host, database name,  user, password) seem to be wrong...";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} catch (final Exception e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ADB_CONNECTION_FAILURE;
	    final String reason = "Failed while executing DataBaseApi.connect_mysql() method...";
	    final String desc = e.getMessage().indexOf(GlobalConst.NO_HOST_EXCEPTION) != -1 ? "The 'host' property ("
		    + host + ") might be wrong... please check it..."
		    : "The loggin parameters (host, database name,  user, password) seem to be wrong...";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	}
    }

    /**
     * <b>Description : </b> Allows to connect to the <I>Snap</I> database when
     * of <I>Oracle</I> type.
     * 
     * @throws SnapshotingException
     */
    public void connect_oracle() throws SnapshotingException {
	// Load the driver
	try {
	    Class.forName("oracle.jdbc.driver.OracleDriver"); // pilote Oracle
	    final String url = ConfigConst.DRIVER_ORACLE + ":@" + host + ":"
		    + ConfigConst.ORACLE_PORT + ":" + name; // pilote Oracle
	    dbconn = DriverManager.getConnection(url, user, passwd);
	    setAutoCommit(true);
	    setDb_type(ConfigConst.BD_ORACLE);
	    setDriver(ConfigConst.DRIVER_ORACLE);
	    alterSession();
	} catch (final ClassNotFoundException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.DRIVER_MISSING;
	    final String reason = "Failed while executing DataBaseApi.connect_oracle() method...";
	    final String desc = "No Oracle driver available..., please check !";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ADB_CONNECTION_FAILURE;
	    final String reason = "Failed while executing DataBaseApi.connect_oracle() method...";
	    final String desc = "The loggin parameters (host, database name,  user, password) seem to be wrong...";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	}
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Closes the connection with the database
     * 
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public void close() throws SnapshotingException {
	if (dbconn != null) {
	    try {
		dbconn.close();
	    } catch (final SQLException e) {
		String message;
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		// String message = ConfigConst.SNAPSHOTING_ERROR_PREFIX +
		// " : ";
		final String reason = GlobalConst.STATEMENT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.close() method...";
		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);

	    }
	}
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Commits actions in the database <br>
     * 
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public void commit() throws SnapshotingException {
	try {
	    dbconn.commit();
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.commit() method...";
	    throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);

	}
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Cancels actions in the database <br>
     * 
     * @throws SQLException
     *             **************************************************************
     *             ***********
     */
    public void rollback() throws SQLException {
	dbconn.rollback();
    }

    public void alterSession() throws SnapshotingException {
	Statement stmt;
	String sqlStr1, sqlStr2, sqlStr3;
	sqlStr1 = "alter session set NLS_NUMERIC_CHARACTERS = \". \"";
	sqlStr2 = "alter session set NLS_TIMESTAMP_FORMAT = 'DD-MM-YYYY HH24:MI:SS.FF'";
	sqlStr3 = "alter session set NLS_DATE_FORMAT = 'DD-MM-YYYY HH24:MI:SS'";
	try {
	    stmt = dbconn.createStatement();
	    stmt.executeQuery(sqlStr1);
	    stmt.executeQuery(sqlStr2);
	    stmt.executeQuery(sqlStr3);
	    stmt.close();
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.alterSession() method...";
	    throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	}
    }

    private static void close(final ResultSet rset) throws SQLException {
	if (rset != null) {
	    rset.close();
	}
    }

    /*****************************************************************************
     * 
     * 
     * Generals methods used to query SNAP (SELECT) (Extraction)
     * 
     ****************************************************************************/

    /**
     * ************************************************************************
     * <b>Description : </b> Gets for a specified attribute its ID as defined in
     * SnapDb
     * 
     * @param att_name
     *            The attribute's name
     * @return The <I>SnapDb</I>'s ID that caracterize the given attribute
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public int getAttID(final String att_name) throws SnapshotingException {
	int attributesID = 0;
	Connection conn = null;
	ResultSet rset = null;

	PreparedStatement ps_get_att_id = null;
	final String table_name = getDbSchema() + "." + GlobalConst.TABS[0];
	String selectString = "";
	try {
	    // Preparing statement...
	    selectString = "SELECT " + GlobalConst.TAB_DEF[0] + " FROM " + table_name + " WHERE "
		    + GlobalConst.TAB_DEF[2] + " like ?";
	    if (m_dataSource == null) {
		ps_get_att_id = dbconn.prepareStatement(selectString);
	    } else {
		conn = m_dataSource.getConnection();
		ps_get_att_id = conn.prepareStatement(selectString);
	    }
	    final String field1 = att_name.trim();
	    ps_get_att_id.setString(1, field1);
	    rset = ps_get_att_id.executeQuery();
	    // Gets the result of the query
	    if (rset.next()) {
		attributesID = rset.getInt(1);
		ps_get_att_id.close();
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getAttID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(ps_get_att_id);
	    closeConnection(conn);
	}

	// Returns the total number of signals defined in Snap
	return attributesID;
    }

    /**
     * <b>Description : </b> Checks if the attribute of the given name, is
     * already registered in <I>SnapDb</I> (and more particularly in the table
     * of the definitions).
     * 
     * @param att_name
     *            The name of the attribute to check.
     * @return boolean
     * @throws SnapshotingException
     */
    public boolean isRegistered(final String att_name) throws SnapshotingException {
	final int id = getAttID(att_name.trim());
	if (id != 0) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * <b>Description : </b> Returns the new id for the new context being
     * referenced in database <I>snap</I>.<I>(mySQL only)</I>
     * 
     * @return Returns the new id for the new context being referenced in
     *         database <I>snap</I>.
     * @throws SnapshotingException
     */
    public int getMaxID() throws SnapshotingException {
	Statement stmt = null;
	ResultSet rset = null;
	String query;
	final int new_ID = 0;
	int res = 0;
	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	// Build the query string
	query = "SELECT MAX(" + GlobalConst.TAB_CONTEXT[0] + ") FROM " + getDbSchema() + "."
		+ GlobalConst.TABS[1];

	try {
	    stmt = dbconn.createStatement();
	    rset = stmt.executeQuery(query);
	    // Gets the result of the query
	    if (rset.next()) {
		res = rset.getInt(1);
	    }
	    if (res < new_ID) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.getMaxID() method...";
		final String desc = "The table seem to be empty, please check it...";
		System.out.println(new SnapshotingException(message, reason, ErrSeverity.WARN,
			desc, "").toString());

		res = new_ID;
	    }
	    stmt.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getMaxID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(stmt);
	}

	return res;
    }

    /**
     * This method retrives from the the database, the list of all registered
     * contexts (or 'snap-patterns)
     * 
     * @return The list of all registered contexts (or 'snap-patterns)
     * @throws SnapshotingException
     */
    public ArrayList getAllContext() throws SnapshotingException {
	ArrayList contextList;
	contextList = new ArrayList(512);
	String query = "";
	Statement stmt = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_CONTEXT[0] + ", "
		+ GlobalConst.TAB_CONTEXT[1] + ", " + GlobalConst.TAB_CONTEXT[2] + ", "
		+ GlobalConst.TAB_CONTEXT[3] + ", " + GlobalConst.TAB_CONTEXT[4] + ", "
		+ GlobalConst.TAB_CONTEXT[5];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[1];
	final String clause_1 = " ORDER BY " + GlobalConst.TAB_CONTEXT[0];
	query = "SELECT " + select_field + "  FROM " + table_1 + clause_1;

	ResultSet resultSet = null;
	try {
	    stmt = dbconn.createStatement();
	    resultSet = stmt.executeQuery(query.toString());
	    while (resultSet.next()) {
		final SnapContext snapContext = new SnapContext();
		snapContext.setId(resultSet.getInt(1));
		snapContext.setCreation_date(resultSet.getDate(2));
		snapContext.setName(resultSet.getString(3));
		snapContext.setAuthor_name(resultSet.getString(4));
		snapContext.setReason(resultSet.getString(5));
		snapContext.setDescription(resultSet.getString(6));
		contextList.add(snapContext);
	    }
	    return contextList;
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getAllContext() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(stmt);
	}
    }

    /**
     * This method retrives from the the database, the list of all registered
     * contexts (or 'snap-patterns) which subscribe to the clause and/or have
     * the identifier id_context. Author : Laure Garda
     * 
     * @param clause
     * @param id_context
     * @return a list of registered contexts (or 'snap-patterns)
     * @throws SnapshotingException
     */
    public ArrayList getContext(final String clause, final int id_context)
	    throws SnapshotingException {
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	Statement stmt = null;

	ArrayList contextList;
	contextList = new ArrayList(512);
	String query = "";
	String select_field = "";
	// Selection des champs des contexts a afficher.
	select_field = select_field + GlobalConst.TAB_CONTEXT[0] + ", "
		+ GlobalConst.TAB_CONTEXT[1] + ", " + GlobalConst.TAB_CONTEXT[2] + ", "
		+ GlobalConst.TAB_CONTEXT[3] + ", " + GlobalConst.TAB_CONTEXT[4] + ", "
		+ GlobalConst.TAB_CONTEXT[5];
	// Choix de la table dans laquelle effectuer la requete SQL.
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[1];
	// Affichage des resusltats par ordre croissant d'ID du context.
	final String clause_1 = " ORDER BY " + GlobalConst.TAB_CONTEXT[0];
	query = "SELECT " + select_field + " FROM " + table_1;

	try {
	    if (id_context != -1) {
		// Cas ou l'id_context est donne.

		query = query + clause;

		// System.out.println("query : " + query);

		if (m_dataSource == null) {
		    preparedStatement = dbconn.prepareStatement(query);
		} else {
		    conn = m_dataSource.getConnection();
		    preparedStatement = conn.prepareStatement(query);
		}

		preparedStatement.setInt(1, id_context);
		resultSet = preparedStatement.executeQuery();

		// Gets the result of the query
		while (resultSet.next()) {
		    final SnapContext snapContext = new SnapContext();
		    snapContext.setId(resultSet.getInt(1));
		    snapContext.setCreation_date(resultSet.getDate(2));
		    snapContext.setName(resultSet.getString(3));
		    snapContext.setAuthor_name(resultSet.getString(4));
		    snapContext.setReason(resultSet.getString(5));
		    snapContext.setDescription(resultSet.getString(6));
		    contextList.add(snapContext);
		}
	    } else {
		// cas ou l'id du context n'est pas donne.

		if (clause.equals("")) {
		    query = query + clause_1;
		} else {
		    query = query + clause + clause_1;
		    System.out.println("query : " + query);
		}

		if (m_dataSource == null) {
		    stmt = dbconn.createStatement();
		} else {
		    conn = m_dataSource.getConnection();
		    stmt = conn.createStatement();
		}

		resultSet = stmt.executeQuery(query.toString());
		while (resultSet.next()) {
		    final SnapContext snapContext = new SnapContext();
		    snapContext.setId(resultSet.getInt(1));
		    snapContext.setCreation_date(resultSet.getDate(2));
		    snapContext.setName(resultSet.getString(3));
		    snapContext.setAuthor_name(resultSet.getString(4));
		    snapContext.setReason(resultSet.getString(5));
		    snapContext.setDescription(resultSet.getString(6));
		    contextList.add(snapContext);
		}
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getContext() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeStatement(stmt);
	    closeConnection(conn);
	}

	return contextList;
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Gets all attributs from a given context
     * 
     * @return array of strings
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public String[] get_AttFromContext(final String nom_context) throws SnapshotingException {
	final Vector argout_vect = new Vector();
	String[] argout;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	String query;
	final String select_field = GlobalConst.TAB_DEF[2];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[0];
	final String clause_1 = GlobalConst.TABS[0] + "." + GlobalConst.TAB_DEF[0] + " = "
		+ GlobalConst.TABS[3] + "." + GlobalConst.TAB_LIST[1];
	final String clause_2 = GlobalConst.TABS[3] + "." + GlobalConst.TAB_SNAP[0] + " = "
		+ GlobalConst.TABS[1] + "." + GlobalConst.TAB_CONTEXT[0];
	final String clause_3 = GlobalConst.TABS[1] + "." + GlobalConst.TAB_CONTEXT[2] + " = ?";

	query = "SELECT DISTINCT " + select_field + " FROM " + table_1 + " WHERE " + clause_1
		+ " AND " + clause_2 + " AND " + clause_3;
	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setString(1, nom_context);
	    rset = preparedStatement.executeQuery();

	    // Gets the result of the query
	    while (rset.next()) {
		argout_vect.addElement(rset.getString(1));
	    }

	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ERROR_RET_ATT;
	    final String reason = "Failed while executing DataBaseAPI.get_AttFromContext() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	}
	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the families list
	argout = toStringArray(argout_vect);
	return argout;
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Gets all snapshots from a given context
     * 
     * @return array of strings
     * @throws SnapshotingException
     * @see SnapShotLight
     *      *********************************************************************
     *      ****
     */
    public ArrayList getContextAssociatedSnapshots(final int id_context)
	    throws SnapshotingException {
	final ArrayList associatedSnapshots = new ArrayList();
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	String query;
	final String select_field = GlobalConst.TAB_SNAP[0] + ", " + GlobalConst.TAB_SNAP[2];
	// + ConfigConst.TAB_SNAP[3] //todo ajouter le champ 'commentaire'
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[3];
	final String clause_2 = GlobalConst.TABS[3] + "." + GlobalConst.TAB_SNAP[1] + " = ?";

	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_2;

	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setInt(1, id_context);
	    rset = preparedStatement.executeQuery();

	    // Gets the result of the query
	    while (rset.next()) {
		final SnapShotLight snapShotLight = new SnapShotLight();
		snapShotLight.setId_snap(rset.getInt(1));
		snapShotLight.setSnap_date(rset.getTimestamp(2));
		// snapShotLight.setSnap_comment(rset.getString(3)); // todo
		// commentaire
		associatedSnapshots.add(snapShotLight);
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ERROR_RET_SNAP;
	    final String reason = "Failed while executing DataBaseAPI.getContextAssociatedSnapshots() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	}
	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the names list

	return associatedSnapshots;
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Gets all snapshots from a given context Author :
     * Laure Garda : Renvoi les snapshots associes au contexte passe en
     * parametre qui verifient les conditions passees en parametre.
     * 
     * @return array of strings
     * @throws SnapshotingException
     * @see SnapShotLight
     *      *********************************************************************
     *      ****
     */
    public ArrayList getContextAssociatedSnapshots(final String clause, final int id_context,
	    final int id_snap) throws SnapshotingException {
	final ArrayList associatedSnapshots = new ArrayList();
	// First connect with the database
	/*
	 * if (getAutoConnect()) connect();
	 */
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	Statement stmt = null;

	// Create and execute the SQL query string
	String query;
	final String select_field = GlobalConst.TAB_SNAP[0] + ", " + GlobalConst.TAB_SNAP[2] + ", "
		+ GlobalConst.TAB_SNAP[3] // todo
	// ajouter
	// le
	// champ
	// 'commentaire'
	;
	// recuperation de la table des snapshot.
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[3];
	final String clause_4 = " ORDER BY " + GlobalConst.TAB_SNAP[0];

	query = "SELECT " + select_field + " FROM " + table_1 + clause;
	try {
	    if (id_context != -1 && id_snap != -1) {
		// Cas ou l'id_context et l'id_snap sont donnes.
		/*
		 * String clause_2 = ConfigConst.TABS[2] + "." +
		 * ConfigConst.TAB_SNAP[1] + " = ?"; String clause_3 =
		 * ConfigConst.TABS[2] + "." + ConfigConst.TAB_SNAP[0] + " = ?";
		 * if(clause.equals("")) query = query + " WHERE " + clause_2 +
		 * " AND " + clause_3 + clause_4; else query = query + " AND " +
		 * clause_2 + " AND " + clause_3 + clause_4;
		 */
		query = query + clause_4;
		// System.out.println("DataBaseApi.getContextAssociatedSnapshots/query  : "
		// + query);
		if (m_dataSource == null) {
		    preparedStatement = dbconn.prepareStatement(query);
		} else {
		    conn = m_dataSource.getConnection();
		    preparedStatement = conn.prepareStatement(query);
		}
		preparedStatement.setInt(1, id_snap);
		preparedStatement.setInt(2, id_context);
		rset = preparedStatement.executeQuery();

		// Gets the result of the query
		while (rset.next()) {
		    final SnapShotLight snapShotLight = new SnapShotLight();
		    snapShotLight.setId_snap(rset.getInt(1));
		    snapShotLight.setSnap_date(rset.getTimestamp(2));
		    snapShotLight.setComment(rset.getString(3)); // todo
		    // commentaire
		    associatedSnapshots.add(snapShotLight);
		}
		// preparedStatement.close();

	    } else if (id_context != -1) {
		// Cas ou l'id_context est donne.

		/*
		 * String clause_2 = ConfigConst.TABS[2] + "." +
		 * ConfigConst.TAB_SNAP[1] + " = ?"; if(clause.equals("")) query
		 * = query + " WHERE " + clause_2 + clause_4; else query = query
		 * + " AND " + clause_2 + clause_4;
		 */
		query = query + clause_4;
		// System.out.println("DataBaseApi.getContextAssociatedSnapshots/query  : "
		// + query);

		if (m_dataSource == null) {
		    preparedStatement = dbconn.prepareStatement(query);
		} else {
		    conn = m_dataSource.getConnection();
		    preparedStatement = conn.prepareStatement(query);
		}
		preparedStatement.setInt(1, id_context);
		rset = preparedStatement.executeQuery();

		// Gets the result of the query
		while (rset.next()) {
		    final SnapShotLight snapShotLight = new SnapShotLight();
		    snapShotLight.setId_snap(rset.getInt(1));
		    snapShotLight.setSnap_date(rset.getTimestamp(2));
		    snapShotLight.setComment(rset.getString(3)); // todo
		    // commentaire
		    associatedSnapshots.add(snapShotLight);
		}
		// preparedStatement.close();
	    } else if (id_snap != -1) {
		// Cas ou l'id_snap est donne.
		/*
		 * String clause_2 = ConfigConst.TABS[2] + "." +
		 * ConfigConst.TAB_SNAP[0] + " = ?"; if(clause.equals("")) query
		 * = query + " WHERE " + clause_2 + clause_4; else query = query
		 * + " AND " + clause_2 + clause_4;
		 */
		query = query + clause_4;
		// System.out.println("DataBaseApi.getContextAssociatedSnapshots/query  : "
		// + query);
		if (m_dataSource == null) {
		    preparedStatement = dbconn.prepareStatement(query);
		} else {
		    conn = m_dataSource.getConnection();
		    preparedStatement = conn.prepareStatement(query);
		}
		preparedStatement.setInt(1, id_snap);
		rset = preparedStatement.executeQuery();

		// Gets the result of the query
		while (rset.next()) {
		    final SnapShotLight snapShotLight = new SnapShotLight();
		    snapShotLight.setId_snap(rset.getInt(1));
		    snapShotLight.setSnap_date(rset.getTimestamp(2));
		    snapShotLight.setComment(rset.getString(3)); // todo
		    // commentaire
		    associatedSnapshots.add(snapShotLight);
		}
		// preparedStatement.close();
	    } else {
		// Cas ou ni l'id_context ni l'id_snap ne sont donnes.
		query = query + clause_4;
		// System.out.println("DataBaseApi.getContextAssociatedSnapshots/query  : "
		// + query);

		if (m_dataSource == null) {
		    stmt = dbconn.createStatement();
		} else {
		    conn = m_dataSource.getConnection();
		    stmt = conn.createStatement();
		}

		rset = stmt.executeQuery(query.toString());
		while (rset.next()) {
		    final SnapShotLight snapShotLight = new SnapShotLight();
		    snapShotLight.setId_snap(rset.getInt(1));
		    snapShotLight.setSnap_date(rset.getTimestamp(2));
		    snapShotLight.setComment(rset.getString(3)); // todo
		    // commentaire
		    associatedSnapshots.add(snapShotLight);
		}
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ERROR_RET_SNAP;
	    final String reason = "Failed while executing DataBaseAPI.getContextAssociatedSnapshots() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} catch (final Exception e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ERROR_RET_SNAP;
	    final String reason = "Failed while executing DataBaseAPI.getContextAssociatedSnapshots() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	    closeStatement(stmt);
	    closeConnection(conn);
	}

	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the names list
	return associatedSnapshots;
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Gets number of attributs from a given context
     * 
     * @return array of strings
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public double get_nbAttFromContext(final String nom_context) throws SnapshotingException {
	double result = 0;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;

	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	String query;
	final String select_field = " COUNT(*) ";
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[3];
	final String clause_1 = GlobalConst.TABS[3] + "." + GlobalConst.TAB_SNAP[0] + " = "
		+ GlobalConst.TABS[1] + "." + GlobalConst.TAB_CONTEXT[0];
	final String clause_2 = GlobalConst.TABS[1] + "." + GlobalConst.TAB_CONTEXT[2] + " = ?";

	// the SQL request is : select count(*) from snap.list where
	// list.id_context = context.id_context and context.name = 'nom';
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setString(1, nom_context);
	    rset = preparedStatement.executeQuery();

	    // Gets the result of the query
	    while (rset.next()) {
		result = rset.getDouble(1);
		preparedStatement.close();
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ERROR_RET_ATT;
	    final String reason = "Failed while executing DataBaseAPI.get_nbAttFromContext() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	}

	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the result
	return result;
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Gets the given attribute format
     * 
     * @return the given attribute format
     * @throws SnapshotingException
     *             **************************************************************
     *             *********
     */
    public int get_FormatFromAtt(final String nom_attribut) throws SnapshotingException {
	int result = 0;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	String query;
	final String select_field = GlobalConst.TAB_DEF[9];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[0];
	final String clause_1 = GlobalConst.TAB_DEF[2] + " = ?";

	// the SQL request is : select data_format from AST where name = 'nom';
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1;
	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setString(1, nom_attribut);
	    rset = preparedStatement.executeQuery();

	    // Gets the result of the query
	    while (rset.next()) {
		result = rset.getInt(1);
		preparedStatement.close();
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.ERROR_RET_ATT;
	    final String reason = "Failed while executing DataBaseAPI.get_FormatFromAtt() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	}

	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the result
	return result;
    }

    /**
     * Retrieves the creation time of the given snapshot.
     * 
     * @param snapID
     *            The identifier of the snapshot
     * @return the creation time
     * @throws SnapshotingException
     */
    public Timestamp getSnapRegistrationTime(final int snapID) throws SnapshotingException {
	java.sql.Timestamp time = null;
	ResultSet rset = null;
	PreparedStatement statement = null;
	final String table_name = getDbSchema() + "." + GlobalConst.TABS[3];
	String query = "";
	try { // Preparing statement...
	    query = "SELECT " + GlobalConst.TAB_SNAP[2] + " FROM " + table_name + " WHERE "
		    + GlobalConst.TAB_SNAP[0] + " like ?";
	    statement = dbconn.prepareStatement(query);
	    statement.setInt(1, snapID);
	    rset = statement.executeQuery();
	    // Gets the result of the query
	    if (rset.next()) {
		time = rset.getTimestamp(1);
	    }
	    statement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getSnapRegistrationTime() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(statement);
	}
	return time;
    }

    /**
     * Retrieves the context identifier to wich the given snapshot identifier is
     * associated.
     * 
     * @param snapID
     *            the snapshot identifier
     * @return the associated context identifier
     * @throws SnapshotingException
     */
    public int getContextID(final int snapID) throws SnapshotingException {
	int res = -1;
	ResultSet rset = null;
	PreparedStatement preparedStatement = null;
	Connection conn = null;
	String query;
	final String select_field = GlobalConst.TAB_SNAP[1];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[3];
	final String clause_1 = GlobalConst.TAB_SNAP[0] + " like ?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1;
	try { // Preparing preparedStatement...
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }
	    preparedStatement.setInt(1, snapID);
	    rset = preparedStatement.executeQuery();
	    // Gets the result of the query
	    if (rset.next()) {
		res = rset.getInt(1);
		preparedStatement.close();
	    }
	} catch (final SQLException e) {
	    e.printStackTrace();

	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getContextID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	return res;
    }

    public SnapAttributeExtract getSnapResult(final SnapAttributeLight snapAttributeLight,
	    final int id_snap) {
	final SnapAttributeExtract snapAttributeExtract = new SnapAttributeExtract(
		snapAttributeLight);
	final int attID = snapAttributeExtract.getId_att();
	switch (snapAttributeExtract.getData_format()) {
	case AttrDataFormat._SCALAR:
	    switch (snapAttributeExtract.getWritable()) {
	    case AttrWriteType._READ:
		snapAttributeExtract.setValue(extract_ScalarData_RO(id_snap, attID,
			snapAttributeLight.getData_type()));
		break;
	    case AttrWriteType._READ_WITH_WRITE:
		snapAttributeExtract.setValue(extract_ScalarData_RW(id_snap, attID,
			snapAttributeLight.getData_type()));
		break;
	    case AttrWriteType._WRITE:
		snapAttributeExtract.setValue(extract_ScalarData_WO(id_snap, attID,
			snapAttributeLight.getData_type()));
		break;
	    case AttrWriteType._READ_WRITE:
		snapAttributeExtract.setValue(extract_ScalarData_RW(id_snap, attID,
			snapAttributeLight.getData_type()));
		break;
	    }
	    break;
	case AttrDataFormat._SPECTRUM:
	    switch (snapAttributeExtract.getWritable()) {
	    case AttrWriteType._WRITE:
	    case AttrWriteType._READ:
		snapAttributeExtract.setValue(extract_SpectrumData_RO(id_snap, attID,
			snapAttributeExtract.getData_type()));
		break;
	    case AttrWriteType._READ_WITH_WRITE:
	    case AttrWriteType._READ_WRITE:
		snapAttributeExtract.setValue(extract_SpectrumData_RW(id_snap, attID,
			snapAttributeExtract.getData_type()));
		snapAttributeExtract.setDimX(getCurrentSpectrumDimX());
		break;
	    }
	    break;
	case AttrDataFormat._IMAGE:
	    switch (snapAttributeExtract.getWritable()) {
	    case AttrWriteType._WRITE:
	    case AttrWriteType._READ:
		snapAttributeExtract.setValue(extract_ImageData_RO(id_snap, attID,
			snapAttributeExtract.getData_type()));
		break;
	    case AttrWriteType._READ_WITH_WRITE:
	    case AttrWriteType._READ_WRITE:
		snapAttributeExtract.setValue(extract_ImageData_RW(id_snap, attID,
			snapAttributeExtract.getData_type()));
		break;
	    }
	    break;
	}

	return snapAttributeExtract;
    }

    /**
     * @param snapID
     * @param snapAtt
     * @return
     */
    private Object extract_ScalarData_RO(final int snapID, final int snapAtt, final int data_type) {
	Object value = null;
	String query = "";
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_SC_RO_NUM[2];
	final String table_1 = getDbSchema()
		+ "."
		+ (data_type != TangoConst.Tango_DEV_STRING ? GlobalConst.TABS[8]
			: GlobalConst.TABS[10]);
	final String clause_1 = (data_type != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RO_NUM[0]
		: GlobalConst.TAB_SC_RO_STR[0])
		+ "=?";
	final String clause_2 = (data_type != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RO_NUM[1]
		: GlobalConst.TAB_SC_RO_STR[1])
		+ "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		switch (data_type) {
		case TangoConst.Tango_DEV_STRING:
		    value = resultSet.getString(1);
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_STATE:
		    value = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_UCHAR:
		    value = new Byte(resultSet.getByte(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_LONG:
		    value = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_ULONG:
		    value = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_BOOLEAN:
		    value = new Boolean(resultSet.getInt(1) != 0);
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_SHORT:
		    value = new Short(resultSet.getShort(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_FLOAT:
		    value = new Float(resultSet.getFloat(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_DOUBLE:
		    value = new Double(resultSet.getDouble(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		default:
		    value = new Double(resultSet.getDouble(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		}
	    }
	} catch (final SQLException e) {
	    e.printStackTrace();

	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ScalarData_RO" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	return value;
    }

    public void closeResultSet(final ResultSet resultSet) {
	if (resultSet == null) {
	    return;
	}

	try {
	    resultSet.close();
	} catch (final SQLException e) {
	    e.printStackTrace();

	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.closeResultSet" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage());
	}
    }

    public void closeStatement(final Statement preparedStatement) {
	if (preparedStatement == null) {
	    return;
	}

	try {
	    preparedStatement.close();
	} catch (final SQLException e) {
	    e.printStackTrace();

	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.closeStatement" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage());
	}
    }

    private void closeConnection(final Connection conn) {
	if (conn == null) {
	    return;
	}

	try {
	    conn.close();
	} catch (final SQLException e) {
	    e.printStackTrace();

	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.closeConnection" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage());
	}
    }

    private Object[] extract_ScalarData_RW(final int snapID, final int snapAtt, final int data_type) {
	final Object[] value = new Object[2];
	Object rvalue = null;
	Object wvalue = null;
	String query = "";
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_SC_RW_NUM[2] + ", "
		+ GlobalConst.TAB_SC_RW_NUM[3];
	final String table_1 = getDbSchema()
		+ "."
		+ (data_type != TangoConst.Tango_DEV_STRING ? GlobalConst.TABS[9]
			: GlobalConst.TABS[11]);
	final String clause_1 = (data_type != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RW_NUM[0]
		: GlobalConst.TAB_SC_RW_STR[0])
		+ "=?";
	final String clause_2 = (data_type != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RW_NUM[1]
		: GlobalConst.TAB_SC_RW_STR[1])
		+ "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		switch (data_type) {
		case TangoConst.Tango_DEV_STRING:
		    rvalue = resultSet.getString(1);
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = resultSet.getString(2);
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_STATE:
		    rvalue = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Integer(resultSet.getInt(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_UCHAR:
		    rvalue = new Byte(resultSet.getByte(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Byte(resultSet.getByte(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_LONG:
		    rvalue = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Integer(resultSet.getInt(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_ULONG:
		    rvalue = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Integer(resultSet.getInt(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_BOOLEAN:
		    rvalue = new Boolean((resultSet.getInt(1) != 0));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Boolean((resultSet.getInt(1) != 0));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_SHORT:
		    rvalue = new Short(resultSet.getShort(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Short(resultSet.getShort(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_FLOAT:
		    rvalue = new Float(resultSet.getFloat(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Float(resultSet.getFloat(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		case TangoConst.Tango_DEV_DOUBLE:
		    rvalue = new Double(resultSet.getDouble(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Double(resultSet.getDouble(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		default:
		    rvalue = new Double(resultSet.getDouble(1));
		    if (resultSet.wasNull()) {
			rvalue = null;
		    }
		    wvalue = new Double(resultSet.getDouble(2));
		    if (resultSet.wasNull()) {
			wvalue = null;
		    }
		    break;
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ScalarData_RW" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	value[0] = rvalue;
	value[1] = wvalue;
	return value;
    }

    private Object extract_ScalarData_WO(final int snapID, final int snapAtt, final int data_type) {
	Object value = null;
	String query = "";
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_SC_WO_NUM[2];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[8];
	final String clause_1 = GlobalConst.TAB_SC_WO_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_SC_WO_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		switch (data_type) {
		case TangoConst.Tango_DEV_STRING:
		    value = resultSet.getString(1);
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_STATE:
		    value = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_UCHAR:
		    value = new Byte(resultSet.getByte(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_LONG:
		    value = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_ULONG:
		    value = new Integer(resultSet.getInt(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_BOOLEAN:
		    value = new Boolean(resultSet.getInt(1) != 0);
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_SHORT:
		    value = new Short(resultSet.getShort(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_FLOAT:
		    value = new Float(resultSet.getFloat(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		case TangoConst.Tango_DEV_DOUBLE:
		    value = new Double(resultSet.getDouble(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		default:
		    value = new Double(resultSet.getDouble(1));
		    if (resultSet.wasNull()) {
			value = null;
		    }
		    break;
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ScalarData_WO" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	return value;
    }

    private Object extract_SpectrumData_RO(final int snapID, final int snapAtt, final int data_type) {
	if (db_type == ConfigConst.BD_MYSQL) {
	    return extract_SpectrumData_RO_MySQL(snapID, snapAtt, data_type);
	} else {
	    return extract_SpectrumData_RO_Oracle(snapID, snapAtt, data_type);
	}
    }

    /**
     * @param snapID
     * @param snapAtt
     * @return
     */
    private Object extract_SpectrumData_RO_Oracle(final int snapID, final int snapAtt,
	    final int data_type) {
	String query = "";
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_SP_RO_NUM[3];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[6];
	final String clause_1 = GlobalConst.TAB_SP_RO_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_SP_RO_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;

	String readString = null;

	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);

	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		final Clob readClob = resultSet.getClob(1);
		if (resultSet.wasNull()) {
		    readString = null;
		} else {
		    readString = readClob.getSubString(1, (int) readClob.length());
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_SpectrumData_RO_Oracle" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	return getSpectrumValue(readString, null, data_type, false);
    }

    /**
     * @param snapID
     * @param snapAtt
     * @return
     */
    private Object[] extract_SpectrumData_RO_MySQL(final int snapID, final int snapAtt,
	    final int data_type) {
	String value = "NaN";
	String query = "";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	Connection conn = null;
	final String select_field = GlobalConst.TAB_SP_RO_NUM[3];
	final String select_field2 = "CAST(" + select_field + " AS CHAR)";
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[6];
	final String clause_1 = GlobalConst.TAB_SP_RO_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_SP_RO_NUM[1] + "=?";
	query = "SELECT " + select_field2 + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }
	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		value = resultSet.getString(1);
		if (resultSet.wasNull()) {
		    value = null;
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_SpectrumData_RO_MySQL" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	return getSpectrumValue(value, null, data_type, false);
    }

    private Object[] extract_SpectrumData_RW(final int snapID, final int snapAtt,
	    final int data_type) {
	if (db_type == ConfigConst.BD_MYSQL) {
	    return extract_SpectrumData_RW_MySQL(snapID, snapAtt, data_type);
	} else {
	    return extract_SpectrumData_RW_Oracle(snapID, snapAtt, data_type);
	}
    }

    private Object[] extract_SpectrumData_RW_MySQL(final int snapID, final int snapAtt,
	    final int data_type) {
	String read_value = "NaN";
	String write_value = "NaN";
	String query = "";
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = "CAST(" + GlobalConst.TAB_SP_RW_NUM[3] + " AS CHAR)" + ", " + "CAST("
		+ GlobalConst.TAB_SP_RW_NUM[4] + " AS CHAR)" + ", " + GlobalConst.TAB_SP_RW_NUM[2];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[7];
	final String clause_1 = GlobalConst.TAB_SP_RW_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_SP_RW_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		read_value = resultSet.getString(1);
		if (resultSet.wasNull()) {
		    read_value = null;
		}
		write_value = resultSet.getString(2);
		if (resultSet.wasNull()) {
		    write_value = null;
		}
		final int dim_x = resultSet.getInt(3);
		setCurrentSpectrumDimX(dim_x);
		// System.out.println (
		// "CLA/extract_SpectrumData_RW_MySQL/dim_x/"+dim_x );
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_SpectrumData_RW_MySQL" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	return getSpectrumValue(read_value, write_value, data_type, true);
    }

    private Object[] extract_SpectrumData_RW_Oracle(final int snapID, final int snapAtt,
	    final int data_type) {
	String query = "";
	String readString = null, writeString = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_SP_RW_NUM[3] + ", "
		+ GlobalConst.TAB_SP_RW_NUM[4] + ", " + GlobalConst.TAB_SP_RW_NUM[2];

	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[7];
	final String clause_1 = GlobalConst.TAB_SP_RW_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_SP_RW_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;

	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);

	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		final Clob readClob = resultSet.getClob(1);
		if (resultSet.wasNull()) {
		    readString = null;
		} else {
		    readString = readClob.getSubString(1, (int) readClob.length());
		}

		final Clob writeClob = resultSet.getClob(2);
		if (resultSet.wasNull()) {
		    writeString = null;
		} else {
		    writeString = writeClob.getSubString(1, (int) writeClob.length());
		}

		final int dim_x = resultSet.getInt(3);
		setCurrentSpectrumDimX(dim_x);

		// System.out.println (
		// "CLA/extract_SpectrumData_RW_Oracle/readString/"+readString
		// );
		// System.out.println (
		// "CLA/extract_SpectrumData_RW_Oracle/writeString/"+writeString
		// );
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_SpectrumData_RW_Oracle" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	}

	return getSpectrumValue(readString, writeString, data_type, true);
    }

    private Object extract_ImageData_RO(final int snapID, final int snapAtt, final int data_type) {
	if (db_type == ConfigConst.BD_MYSQL) {
	    return extract_ImageData_RO_MySQL(snapID, snapAtt, data_type);
	} else {
	    return extract_ImageData_RO_Oracle(snapID, snapAtt, data_type);
	}
    }

    private Object[] extract_ImageData_RW(final int snapID, final int snapAtt, final int data_type) {
	if (db_type == ConfigConst.BD_MYSQL) {
	    return extract_ImageData_RW_MySQL(snapID, snapAtt, data_type);
	} else {
	    return extract_ImageData_RW_Oracle(snapID, snapAtt, data_type);
	}
    }

    /**
     * @param snapID
     * @param snapAtt
     * @return
     */
    private Object extract_ImageData_RO_MySQL(final int snapID, final int snapAtt,
	    final int data_type) {
	String value = null;
	String query = "";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_IM_RO_NUM[4];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[4];
	final String clause_1 = GlobalConst.TAB_IM_RO_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_IM_RO_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		value = resultSet.getString(1);
		if (resultSet.wasNull()) {
		    value = null;
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ImageData_RO_MySQL" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	}

	if (value != null) {
	    return getImageValue(value, data_type);
	} else {
	    return null;
	}
    }

    /**
     * @param snapID
     * @param snapAtt
     * @return
     */
    private Object extract_ImageData_RO_Oracle(final int snapID, final int snapAtt,
	    final int data_type) {
	String query = "";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_IM_RO_NUM[3];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[4];
	final String clause_1 = GlobalConst.TAB_IM_RO_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_IM_RO_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;

	String readString = null;

	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);

	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		final Clob readClob = resultSet.getClob(1);
		if (resultSet.wasNull()) {
		    readString = null;
		} else {
		    readString = readClob.getSubString(1, (int) readClob.length());
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ImageData_RO_Oracle" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	}

	if (readString != null) {
	    return getImageValue(readString, data_type);
	} else {
	    return null;
	}
    }

    private Object[] extract_ImageData_RW_MySQL(final int snapID, final int snapAtt,
	    final int data_type) {
	final Object[] imageValueRW = new Object[2];

	String readValue = null;
	String writeValue = null;
	String query = "";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + "CAST(" + GlobalConst.TAB_IM_RW_NUM[4] + " AS CHAR)" + ", "
		+ "CAST(" + GlobalConst.TAB_IM_RW_NUM[5] + " AS CHAR)";
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[5];
	final String clause_1 = GlobalConst.TAB_IM_RW_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_IM_RW_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;
	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);
	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		readValue = resultSet.getString(1);
		if (resultSet.wasNull()) {
		    readValue = null;
		}
		writeValue = resultSet.getString(2);
		if (resultSet.wasNull()) {
		    writeValue = null;
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ImageData_RW_MySQL" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	}

	if (readValue != null) {
	    imageValueRW[0] = getImageValue(readValue, data_type);
	} else {
	    imageValueRW[0] = null;
	}

	if (writeValue != null) {
	    imageValueRW[1] = getImageValue(writeValue, data_type);
	} else {
	    imageValueRW[1] = null;
	}

	return imageValueRW;
    }

    private Object[] extract_ImageData_RW_Oracle(final int snapID, final int snapAtt,
	    final int data_type) {
	final Object[] imageValueRW = new Object[2];

	String query = "";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	String select_field = "";
	select_field = select_field + GlobalConst.TAB_IM_RW_NUM[3] + GlobalConst.TAB_IM_RW_NUM[4];
	final String table_1 = getDbSchema() + "." + GlobalConst.TABS[5];
	final String clause_1 = GlobalConst.TAB_IM_RW_NUM[0] + "=?";
	final String clause_2 = GlobalConst.TAB_IM_RW_NUM[1] + "=?";
	query = "SELECT " + select_field + " FROM " + table_1 + " WHERE " + clause_1 + " AND "
		+ clause_2;

	String readString = null;
	String writeString = null;

	try {
	    preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement.setInt(1, snapID);
	    preparedStatement.setInt(2, snapAtt);

	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
		final Clob readClob = resultSet.getClob(1);
		if (resultSet.wasNull()) {
		    readString = null;
		} else {
		    readString = readClob.getSubString(1, (int) readClob.length());
		}
		final Clob writeClob = resultSet.getClob(2);
		if (resultSet.wasNull()) {
		    writeString = null;
		} else {
		    writeString = writeClob.getSubString(1, (int) writeClob.length());
		}
	    }
	} catch (final SQLException e) {
	    System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "DataBaseAPI.extract_ImageData_RO_Oracle" + "\r\n" + "\t Reason : \t "
		    + getDbSchema().toUpperCase().trim() + "_FAILURE" + "\r\n"
		    + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \r\n" + "\t\t Statement : " + query + "\r\n");
	    // throw e;
	} finally {
	    closeResultSet(resultSet);
	    closeStatement(preparedStatement);
	}

	if (readString != null) {
	    imageValueRW[0] = getImageValue(readString, data_type);
	} else {
	    imageValueRW[0] = null;
	}

	if (writeString != null) {
	    imageValueRW[1] = getImageValue(writeString, data_type);
	} else {
	    imageValueRW[1] = null;
	}

	return imageValueRW;
    }

    private Object[] getSpectrumValue(final String readStr, final String writeStr,
	    final int data_type, final boolean returnAsReadWrite) {
	int readSize = 0, writeSize = 0;
	StringTokenizer readTokenizer;
	String readString = null, writeString = null;
	if (readStr == null) {
	    readTokenizer = null;
	} else {
	    readString = readStr.replaceAll("\\[", "").replaceAll("\\]", "");
	    if ("".equals(readString) || "null".equals(readString)
		    || "NaN".equalsIgnoreCase(readString)) {
		readTokenizer = null;
	    } else {
		readTokenizer = new StringTokenizer(readString, GlobalConst.CLOB_SEPARATOR);
		readSize += readTokenizer.countTokens();
	    }
	}

	StringTokenizer writeTokenizer;
	if (writeStr == null) {
	    writeTokenizer = null;
	} else {
	    writeString = writeStr.replaceAll("\\[", "").replaceAll("\\]", "");
	    if ("".equals(writeString) || "null".equals(writeString)
		    || "NaN".equalsIgnoreCase(writeString)) {
		writeTokenizer = null;
	    } else {
		writeTokenizer = new StringTokenizer(writeString, GlobalConst.CLOB_SEPARATOR);
		writeSize += writeTokenizer.countTokens();
	    }
	}

	Double[] dvalueArr_read = null, dvalueArr_write = null;
	Byte[] cvalueArr_read = null, cvalueArr_write = null;
	Integer[] lvalueArr_read = null, lvalueArr_write = null;
	Short[] svalueArr_read = null, svalueArr_write = null;
	Boolean[] bvalueArr_read = null, bvalueArr_write = null;
	Float[] fvalueArr_read = null, fvalueArr_write = null;
	String[] stvalueArr_read = null, stvalueArr_write = null;
	switch (data_type) {
	case TangoConst.Tango_DEV_BOOLEAN:
	    bvalueArr_read = new Boolean[readSize];
	    break;
	case TangoConst.Tango_DEV_CHAR:
	case TangoConst.Tango_DEV_UCHAR:
	    cvalueArr_read = new Byte[readSize];
	    break;
	case TangoConst.Tango_DEV_STATE:
	case TangoConst.Tango_DEV_LONG:
	case TangoConst.Tango_DEV_ULONG:
	    lvalueArr_read = new Integer[readSize];
	    break;
	case TangoConst.Tango_DEV_SHORT:
	case TangoConst.Tango_DEV_USHORT:
	    svalueArr_read = new Short[readSize];
	    break;
	case TangoConst.Tango_DEV_FLOAT:
	    fvalueArr_read = new Float[readSize];
	    break;
	case TangoConst.Tango_DEV_STRING:
	    stvalueArr_read = new String[readSize];
	    break;
	case TangoConst.Tango_DEV_DOUBLE:
	default:
	    dvalueArr_read = new Double[readSize];
	}
	if (returnAsReadWrite) {
	    switch (data_type) {
	    case TangoConst.Tango_DEV_BOOLEAN:
		bvalueArr_write = new Boolean[writeSize];
		break;
	    case TangoConst.Tango_DEV_CHAR:
	    case TangoConst.Tango_DEV_UCHAR:
		cvalueArr_write = new Byte[writeSize];
		break;
	    case TangoConst.Tango_DEV_STATE:
	    case TangoConst.Tango_DEV_LONG:
	    case TangoConst.Tango_DEV_ULONG:
		lvalueArr_write = new Integer[writeSize];
		break;
	    case TangoConst.Tango_DEV_SHORT:
	    case TangoConst.Tango_DEV_USHORT:
		svalueArr_write = new Short[writeSize];
		break;
	    case TangoConst.Tango_DEV_FLOAT:
		fvalueArr_write = new Float[writeSize];
		break;
	    case TangoConst.Tango_DEV_STRING:
		stvalueArr_write = new String[writeSize];
		break;
	    case TangoConst.Tango_DEV_DOUBLE:
	    default:
		dvalueArr_write = new Double[writeSize];
	    }
	}
	int i = 0;

	if (readTokenizer != null) {
	    while (readTokenizer.hasMoreTokens()) {
		final String currentValRead = readTokenizer.nextToken();
		if (currentValRead == null || currentValRead.trim().equals("")) {
		    break;
		}
		switch (data_type) {
		case TangoConst.Tango_DEV_BOOLEAN:
		    try {
			if (currentValRead == null || "".equals(currentValRead)
				|| "null".equals(currentValRead)
				|| "NaN".equalsIgnoreCase(currentValRead)) {
			    bvalueArr_read[i] = null;
			} else {
			    bvalueArr_read[i] = new Boolean(Double.valueOf(currentValRead)
				    .intValue() != 0);
			}
		    } catch (final NumberFormatException n) {
			bvalueArr_read[i] = new Boolean("true".equalsIgnoreCase(currentValRead
				.trim()));
		    }
		    break;
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
		    try {
			if (currentValRead == null || "".equals(currentValRead)
				|| "null".equals(currentValRead)
				|| "NaN".equalsIgnoreCase(currentValRead)) {
			    cvalueArr_read[i] = null;
			} else {
			    cvalueArr_read[i] = Byte.valueOf(currentValRead);
			}
		    } catch (final NumberFormatException n) {
			cvalueArr_read[i] = new Byte(Double.valueOf(currentValRead).byteValue());
		    }
		    break;
		case TangoConst.Tango_DEV_STATE:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
		    try {
			if (currentValRead == null || "".equals(currentValRead)
				|| "null".equals(currentValRead)
				|| "NaN".equalsIgnoreCase(currentValRead)) {
			    lvalueArr_read[i] = null;
			} else {
			    lvalueArr_read[i] = Integer.valueOf(currentValRead);
			}
		    } catch (final NumberFormatException n) {
			lvalueArr_read[i] = new Integer(Double.valueOf(currentValRead).intValue());
		    }
		    break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
		    try {
			if (currentValRead == null || "".equals(currentValRead)
				|| "null".equals(currentValRead)
				|| "NaN".equalsIgnoreCase(currentValRead)) {
			    svalueArr_read[i] = null;
			} else {
			    svalueArr_read[i] = Short.valueOf(currentValRead);
			}
		    } catch (final NumberFormatException n) {
			svalueArr_read[i] = new Short(Double.valueOf(currentValRead).shortValue());
		    }
		    break;
		case TangoConst.Tango_DEV_FLOAT:
		    if (currentValRead == null || "".equals(currentValRead)
			    || "null".equals(currentValRead)
			    || "NaN".equalsIgnoreCase(currentValRead)) {
			fvalueArr_read[i] = null;
		    } else {
			fvalueArr_read[i] = Float.valueOf(currentValRead);
		    }
		    break;
		case TangoConst.Tango_DEV_STRING:
		    if (currentValRead == null || "".equals(currentValRead)
			    || "null".equals(currentValRead)
			    || "NaN".equalsIgnoreCase(currentValRead)) {
			stvalueArr_read[i] = null;
		    } else {
			stvalueArr_read[i] = StringFormater.formatStringToRead(new String(
				currentValRead));
		    }
		    break;
		case TangoConst.Tango_DEV_DOUBLE:
		default:
		    if (currentValRead == null || "".equals(currentValRead)
			    || "null".equals(currentValRead)
			    || "NaN".equalsIgnoreCase(currentValRead)) {
			dvalueArr_read[i] = null;
		    } else {
			dvalueArr_read[i] = Double.valueOf(currentValRead);
		    }
		}
		i++;
	    }
	}

	if (writeTokenizer != null) {
	    i = 0;
	    while (writeTokenizer.hasMoreTokens()) {
		final String currentValWrite = writeTokenizer.nextToken();
		if (currentValWrite == null || currentValWrite.trim().equals("")) {
		    break;
		}
		switch (data_type) {
		case TangoConst.Tango_DEV_BOOLEAN:
		    try {
			if (currentValWrite == null || "".equals(currentValWrite)
				|| "null".equals(currentValWrite)
				|| "NaN".equalsIgnoreCase(currentValWrite)) {
			    bvalueArr_write[i] = null;
			} else {
			    bvalueArr_write[i] = new Boolean(Double.valueOf(currentValWrite)
				    .intValue() != 0);
			}
		    } catch (final NumberFormatException n) {
			bvalueArr_write[i] = new Boolean("true".equalsIgnoreCase(currentValWrite
				.trim()));
		    }
		    break;
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
		    try {
			if (currentValWrite == null || "".equals(currentValWrite)
				|| "null".equals(currentValWrite)
				|| "NaN".equalsIgnoreCase(currentValWrite)) {
			    cvalueArr_write[i] = null;
			} else {
			    cvalueArr_write[i] = Byte.valueOf(currentValWrite);
			}
		    } catch (final NumberFormatException n) {
			cvalueArr_write[i] = new Byte(Double.valueOf(currentValWrite).byteValue());
		    }
		    break;
		case TangoConst.Tango_DEV_STATE:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
		    try {
			if (currentValWrite == null || "".equals(currentValWrite)
				|| "null".equals(currentValWrite)
				|| "NaN".equalsIgnoreCase(currentValWrite)) {
			    lvalueArr_write[i] = null;
			} else {
			    lvalueArr_write[i] = Integer.valueOf(currentValWrite);
			}
		    } catch (final NumberFormatException n) {
			lvalueArr_write[i] = new Integer(Double.valueOf(currentValWrite).intValue());
		    }
		    break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
		    try {
			if (currentValWrite == null || "".equals(currentValWrite)
				|| "null".equals(currentValWrite)
				|| "NaN".equalsIgnoreCase(currentValWrite)) {
			    svalueArr_write[i] = null;
			} else {
			    svalueArr_write[i] = Short.valueOf(currentValWrite);
			}
		    } catch (final NumberFormatException n) {
			svalueArr_write[i] = new Short(Double.valueOf(currentValWrite).shortValue());
		    }
		    break;
		case TangoConst.Tango_DEV_FLOAT:
		    if (currentValWrite == null || "".equals(currentValWrite)
			    || "null".equals(currentValWrite)
			    || "NaN".equalsIgnoreCase(currentValWrite)) {
			fvalueArr_write[i] = null;
		    } else {
			fvalueArr_write[i] = Float.valueOf(currentValWrite);
		    }
		    break;
		case TangoConst.Tango_DEV_STRING:
		    if (currentValWrite == null || "".equals(currentValWrite)
			    || "null".equals(currentValWrite)
			    || "NaN".equalsIgnoreCase(currentValWrite)) {
			stvalueArr_write[i] = null;
		    } else {
			stvalueArr_write[i] = StringFormater.formatStringToRead(new String(
				currentValWrite));
		    }
		    break;
		case TangoConst.Tango_DEV_DOUBLE:
		default:
		    if (currentValWrite == null || "".equals(currentValWrite)
			    || "null".equals(currentValWrite)
			    || "NaN".equalsIgnoreCase(currentValWrite)) {
			dvalueArr_write[i] = null;
		    } else {
			dvalueArr_write[i] = Double.valueOf(currentValWrite);
		    }
		}
		i++;
	    }
	}

	if (returnAsReadWrite) {
	    final Object[] result = new Object[2];
	    switch (data_type) {
	    case TangoConst.Tango_DEV_BOOLEAN:
		result[0] = bvalueArr_read;
		result[1] = bvalueArr_write;
		break;
	    case TangoConst.Tango_DEV_CHAR:
	    case TangoConst.Tango_DEV_UCHAR:
		result[0] = cvalueArr_read;
		result[1] = cvalueArr_write;
		break;
	    case TangoConst.Tango_DEV_STATE:
	    case TangoConst.Tango_DEV_LONG:
	    case TangoConst.Tango_DEV_ULONG:
		result[0] = lvalueArr_read;
		result[1] = lvalueArr_write;
		break;
	    case TangoConst.Tango_DEV_SHORT:
	    case TangoConst.Tango_DEV_USHORT:
		result[0] = svalueArr_read;
		result[1] = svalueArr_write;
		break;
	    case TangoConst.Tango_DEV_FLOAT:
		result[0] = fvalueArr_read;
		result[1] = fvalueArr_write;
		break;
	    case TangoConst.Tango_DEV_STRING:
		result[0] = stvalueArr_read;
		result[1] = stvalueArr_write;
		break;
	    case TangoConst.Tango_DEV_DOUBLE:
	    default:
		result[0] = dvalueArr_read;
		result[1] = dvalueArr_write;
	    }
	    return result;
	} else {
	    switch (data_type) {
	    case TangoConst.Tango_DEV_BOOLEAN:
		return bvalueArr_read;
	    case TangoConst.Tango_DEV_CHAR:
	    case TangoConst.Tango_DEV_UCHAR:
		return cvalueArr_read;
	    case TangoConst.Tango_DEV_STATE:
	    case TangoConst.Tango_DEV_LONG:
	    case TangoConst.Tango_DEV_ULONG:
		return lvalueArr_read;
	    case TangoConst.Tango_DEV_SHORT:
	    case TangoConst.Tango_DEV_USHORT:
		return svalueArr_read;
	    case TangoConst.Tango_DEV_FLOAT:
		return fvalueArr_read;
	    case TangoConst.Tango_DEV_STRING:
		return stvalueArr_read;
	    case TangoConst.Tango_DEV_DOUBLE:
	    default:
		return dvalueArr_read;
	    }
	}
    }

    private Object[][] getImageValue(final String dbValue, final int data_type) {
	// System.out.println("***getImageValue() -> " + dbValue);
	if (dbValue == null || "".equals(dbValue) || "null".equals(dbValue)) {
	    return null;
	}
	Object[] valArray = null;
	String value = new String(dbValue);
	value = value.replaceAll("\\[", "");
	value = value.replaceAll("\\]", "");

	StringTokenizer readTokenizer = null;
	int rowSize = 0, colSize = 0;

	readTokenizer = new StringTokenizer(value, GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS);
	rowSize = readTokenizer.countTokens();

	if (readTokenizer != null) {
	    valArray = new Object[rowSize];
	    int i = 0;
	    while (readTokenizer.hasMoreTokens()) {
		valArray[i++] = readTokenizer.nextToken().trim().split(
			GlobalConst.CLOB_SEPARATOR_IMAGE_COLS);
	    }
	    if (rowSize > 0) {
		colSize = ((String[]) valArray[0]).length;
	    }
	}

	// System.out.println("rowSize,Colsize:"+rowSize+","+colSize);
	Double[][] dvalueArr = null;
	Byte[][] cvalueArr = null;
	Integer[][] lvalueArr = null;
	Short[][] svalueArr = null;
	Boolean[][] bvalueArr = null;
	Float[][] fvalueArr = null;
	String[][] stvalueArr = null;
	switch (data_type) {
	case TangoConst.Tango_DEV_BOOLEAN:
	    bvalueArr = new Boolean[rowSize][colSize];
	    for (int i = 0; i < rowSize; i++) {
		for (int j = 0; j < colSize; j++) {
		    try {
			if (((String[]) valArray[i])[j] == null
				|| "".equals(((String[]) valArray[i])[j])
				|| "null".equals(((String[]) valArray[i])[j])
				|| "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			    bvalueArr[i][j] = null;
			} else {
			    bvalueArr[i][j] = new Boolean(Double.valueOf(
				    ((String[]) valArray[i])[j].trim()).intValue() != 0);
			}
		    } catch (final NumberFormatException n) {
			bvalueArr[i][j] = new Boolean("true"
				.equalsIgnoreCase(((String[]) valArray[i])[j].trim()));
		    }
		}
	    }
	    return bvalueArr;
	case TangoConst.Tango_DEV_CHAR:
	case TangoConst.Tango_DEV_UCHAR:
	    cvalueArr = new Byte[rowSize][colSize];
	    for (int i = 0; i < valArray.length; i++) {
		for (int j = 0; j < colSize; j++) {
		    try {
			if (((String[]) valArray[i])[j] == null
				|| "".equals(((String[]) valArray[i])[j])
				|| "null".equals(((String[]) valArray[i])[j])
				|| "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			    cvalueArr[i][j] = null;
			} else {
			    cvalueArr[i][j] = Byte.valueOf(((String[]) valArray[i])[j].trim());
			}
		    } catch (final NumberFormatException n) {
			cvalueArr[i][j] = new Byte(Double.valueOf(
				((String[]) valArray[i])[j].trim()).byteValue());
		    }
		}
	    }
	    return cvalueArr;
	case TangoConst.Tango_DEV_STATE:
	case TangoConst.Tango_DEV_LONG:
	case TangoConst.Tango_DEV_ULONG:
	    lvalueArr = new Integer[rowSize][colSize];
	    for (int i = 0; i < valArray.length; i++) {
		for (int j = 0; j < colSize; j++) {
		    try {
			if (((String[]) valArray[i])[j] == null
				|| "".equals(((String[]) valArray[i])[j])
				|| "null".equals(((String[]) valArray[i])[j])
				|| "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			    lvalueArr[i][j] = null;
			} else {
			    lvalueArr[i][j] = Integer.valueOf(((String[]) valArray[i])[j].trim());
			}
		    } catch (final NumberFormatException n) {
			lvalueArr[i][j] = new Integer(Double.valueOf(
				((String[]) valArray[i])[j].trim()).intValue());
		    }
		}
	    }
	    return lvalueArr;
	case TangoConst.Tango_DEV_SHORT:
	case TangoConst.Tango_DEV_USHORT:
	    svalueArr = new Short[rowSize][colSize];
	    for (int i = 0; i < valArray.length; i++) {
		for (int j = 0; j < colSize; j++) {
		    try {
			if (((String[]) valArray[i])[j] == null
				|| "".equals(((String[]) valArray[i])[j])
				|| "null".equals(((String[]) valArray[i])[j])
				|| "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			    svalueArr[i][j] = null;
			} else {
			    svalueArr[i][j] = Short.valueOf(((String[]) valArray[i])[j].trim());
			}
		    } catch (final NumberFormatException n) {
			svalueArr[i][j] = new Short(Double.valueOf(
				((String[]) valArray[i])[j].trim()).shortValue());
		    }
		}
	    }
	    return svalueArr;
	case TangoConst.Tango_DEV_FLOAT:
	    fvalueArr = new Float[rowSize][colSize];
	    for (int i = 0; i < valArray.length; i++) {
		for (int j = 0; j < colSize; j++) {
		    try {
			if (((String[]) valArray[i])[j] == null
				|| "".equals(((String[]) valArray[i])[j])
				|| "null".equals(((String[]) valArray[i])[j])
				|| "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			    fvalueArr[i][j] = null;
			} else {
			    fvalueArr[i][j] = Float.valueOf(((String[]) valArray[i])[j].trim());
			}
		    } catch (final NumberFormatException n) {
			fvalueArr[i][j] = new Float(Double.valueOf(
				((String[]) valArray[i])[j].trim()).floatValue());
		    }
		}
	    }
	    return fvalueArr;
	case TangoConst.Tango_DEV_STRING:
	    stvalueArr = new String[rowSize][colSize];
	    for (int i = 0; i < valArray.length; i++) {
		for (int j = 0; j < colSize; j++) {
		    if (((String[]) valArray[i])[j] == null
			    || "".equals(((String[]) valArray[i])[j])
			    || "null".equals(((String[]) valArray[i])[j])
			    || "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			stvalueArr[i][j] = null;
		    } else {
			stvalueArr[i][j] = StringFormater.formatStringToRead(new String(
				((String[]) valArray[i])[j].trim()));
		    }
		}
	    }
	    return stvalueArr;
	case TangoConst.Tango_DEV_DOUBLE:
	default:
	    dvalueArr = new Double[rowSize][colSize];
	    for (int i = 0; i < valArray.length; i++) {
		for (int j = 0; j < colSize; j++) {
		    if (((String[]) valArray[i])[j] == null
			    || "".equals(((String[]) valArray[i])[j])
			    || "null".equals(((String[]) valArray[i])[j])
			    || "NaN".equalsIgnoreCase(((String[]) valArray[i])[j])) {
			dvalueArr[i][j] = null;
		    } else {
			dvalueArr[i][j] = Double.valueOf(((String[]) valArray[i])[j].trim());
		    }
		}
	    }
	    return dvalueArr;
	}

    }

    /*****************************************************************************
     * 
     * 
     * Generals methods used to update SNAP (CREATE / ALTER) (Insert)
     * 
     ****************************************************************************/

    /**
     * This method registers a given attribute into the database for snapshots
     * This methos does not take care of id parameter of the given attribute as
     * this parameter is managed in the database side (autoincrement).
     * 
     * @param snapAttributeHeavy
     *            the attribute to register
     * @throws SnapshotingException
     */
    public void registerAttribute(final SnapAttributeHeavy snapAttributeHeavy)
	    throws SnapshotingException {
	if (db_type == ConfigConst.BD_MYSQL) {
	    PreparedStatement preparedStatement = null;
	    Connection conn = null;
	    final String tableName = getDbSchema() + "." + GlobalConst.TABS[0];

	    // Create and execute the SQL query string
	    // Build the query string
	    String insert_fields = "";
	    for (int i = 1; i < GlobalConst.TAB_DEF.length - 1; i++) {
		insert_fields = insert_fields + GlobalConst.TAB_DEF[i] + ", ";
	    }
	    insert_fields = insert_fields + GlobalConst.TAB_DEF[GlobalConst.TAB_DEF.length - 1];

	    String insert_values = "";
	    for (int i = 1; i < GlobalConst.TAB_DEF.length - 1; i++) {
		insert_values = insert_values + "?" + ", ";
	    }
	    insert_values = insert_values + "?";

	    final String query = "INSERT INTO " + tableName + " (" + insert_fields + ") "
		    + " VALUES(" + insert_values + " ) ";

	    try {

		if (m_dataSource == null) {
		    preparedStatement = dbconn.prepareStatement(query);
		} else {
		    conn = m_dataSource.getConnection();
		    preparedStatement = conn.prepareStatement(query);
		}
		// preparedStatement.setInt(1,
		// snapAttributeHeavy.getAttribute_id());
		preparedStatement.setDate(1, new java.sql.Date(snapAttributeHeavy
			.getRegistration_time().getTime()));
		preparedStatement.setString(2, snapAttributeHeavy.getAttribute_complete_name());
		preparedStatement.setString(3, snapAttributeHeavy.getAttribute_device_name());
		preparedStatement.setString(4, snapAttributeHeavy.getDomain());
		preparedStatement.setString(5, snapAttributeHeavy.getFamily());
		preparedStatement.setString(6, snapAttributeHeavy.getMember());
		preparedStatement.setString(7, snapAttributeHeavy.getAttribute_name());
		preparedStatement.setInt(8, snapAttributeHeavy.getData_type());
		preparedStatement.setInt(9, snapAttributeHeavy.getData_format());
		preparedStatement.setInt(10, snapAttributeHeavy.getWritable());
		preparedStatement.setInt(11, snapAttributeHeavy.getMax_dim_x());
		preparedStatement.setInt(12, snapAttributeHeavy.getMax_dim_y());
		preparedStatement.setInt(13, snapAttributeHeavy.getLevel());
		preparedStatement.setString(14, snapAttributeHeavy.getCtrl_sys());
		preparedStatement.setInt(15, snapAttributeHeavy.getArchivable());
		preparedStatement.setInt(16, snapAttributeHeavy.getSubstitute());

		preparedStatement.executeUpdate();
		// preparedStatement.close();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.registerAttribute() method...";
		final String desc = "";
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    } finally {
		closeStatement(preparedStatement);
		closeConnection(conn);
	    }
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    CallableStatement callableStatement = null;
	    final String procName = "RSA";
	    final String query = "{call " + getDbSchema() + "." + procName
		    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	    try {
		callableStatement = dbconn.prepareCall(query); // number of
		// parameters :
		// 16
		callableStatement.setTimestamp(1, snapAttributeHeavy.getRegistration_time());
		callableStatement.setString(2, snapAttributeHeavy.getAttribute_complete_name());
		callableStatement.setString(3, snapAttributeHeavy.getAttribute_device_name());
		callableStatement.setString(4, snapAttributeHeavy.getDomain());
		callableStatement.setString(5, snapAttributeHeavy.getFamily());
		callableStatement.setString(6, snapAttributeHeavy.getMember());
		callableStatement.setString(7, snapAttributeHeavy.getAttribute_name());
		callableStatement.setInt(8, snapAttributeHeavy.getData_type());
		callableStatement.setInt(9, snapAttributeHeavy.getData_format());
		callableStatement.setInt(10, snapAttributeHeavy.getWritable());
		callableStatement.setInt(11, snapAttributeHeavy.getMax_dim_x());
		callableStatement.setInt(12, snapAttributeHeavy.getMax_dim_y());
		callableStatement.setInt(13, snapAttributeHeavy.getLevel());
		callableStatement.setString(14, snapAttributeHeavy.getCtrl_sys());
		callableStatement.setInt(15, snapAttributeHeavy.getArchivable());
		callableStatement.setInt(16, snapAttributeHeavy.getSubstitute());
		callableStatement.executeUpdate();
		callableStatement.close();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.registerAttribute() method...";
		final String desc = "";
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    } finally {
		closeStatement(callableStatement);
	    }
	}
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Returns an array containing the differents
     * definition informations for the given attribute
     * 
     * @param att_name
     *            The attribute's name
     * @return An array containing the differents definition informations for
     *         the given attribute
     * @throws SnapshotingException
     *             **************************************************************
     *             ********
     */
    public Vector getAttDefinitionData(final String att_name) throws SnapshotingException {
	final Vector definitionsList = new Vector();
	Connection conn = null;
	ResultSet rset = null;
	// PreparedStatement preparedStatement;
	Statement statement = null;
	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	String query = "";
	final String table_name = getDbSchema() + "." + GlobalConst.TABS[0];
	String select_field_spec = "";
	// String clause_1 = ConfigConst.TAB_DEF[2] + "= ?";
	final String clause_1 = GlobalConst.TAB_DEF[2] + "= " + "'" + att_name.trim() + "'";
	if (db_type == ConfigConst.BD_MYSQL) {
	    select_field_spec = " * ";
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    for (int i = 0; i < GlobalConst.TAB_DEF.length - 1; i++) {
		select_field_spec = select_field_spec + "to_char(" + GlobalConst.TAB_DEF[i] + ")"
			+ ", ";
	    }
	    select_field_spec = select_field_spec + "to_char("
		    + GlobalConst.TAB_DEF[GlobalConst.TAB_DEF.length - 1] + ")";
	}
	query = "SELECT " + select_field_spec + " FROM " + table_name + " WHERE " + clause_1;
	try {
	    if (m_dataSource == null) {
		statement = dbconn.createStatement();
	    } else {
		conn = m_dataSource.getConnection();
		statement = conn.createStatement();
	    }
	    rset = statement.executeQuery(query);
	    // Gets the result of the query
	    while (rset.next()) {
		for (int i = 0; i < GlobalConst.TAB_DEF.length; i++) {
		    final String info = GlobalConst.TAB_DEF[i] + "::" + rset.getString(i + 1);
		    definitionsList.addElement(info);
		}
	    }
	    statement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getAttDefinitionData() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(statement);
	    closeConnection(conn);
	}
	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the names list
	return definitionsList;
    }

    /**
     * Renvoi les diff�rentes informations de d�finition de l�attribut pass� en
     * param�tre, qui verifie les conditions donnees. Author : Laure Garda
     * 
     * @param att_name
     * @param clause
     * @return
     * @throws SnapshotingException
     */
    public ArrayList getAttDefinitionData(final String att_name, final String clause)
	    throws SnapshotingException {
	final ArrayList definitionsList = new ArrayList(512);
	Connection conn = null;
	ResultSet rset = null;
	// PreparedStatement preparedStatement;
	Statement statement = null;
	// First connect with the database
	if (getAutoConnect()) {
	    connect();
	}
	// Create and execute the SQL query string
	String query = "";
	final String table_name = getDbSchema() + "." + GlobalConst.TABS[0];
	String select_field_spec = "";
	final String clause_1 = GlobalConst.TAB_DEF[2] + " = " + "'" + att_name.trim() + "'";

	select_field_spec = " * ";
	/*
	 * if ( db_type == ConfigConst.BD_MYSQL ) { select_field_spec = " * "; }
	 * else if ( db_type == ConfigConst.BD_ORACLE ) { for ( int i = 0 ; i <
	 * GlobalConst.TAB_DEF.length - 1 ; i++ ) { select_field_spec =
	 * select_field_spec + "to_char(" + GlobalConst.TAB_DEF[ i ] + ")" +
	 * ", "; } select_field_spec = select_field_spec + "to_char(" +
	 * GlobalConst.TAB_DEF[ GlobalConst.TAB_DEF.length - 1 ] + ")"; } CLA
	 * 05/04/07 corrected a bug: didn't work for Oracle
	 */

	if (clause.equals("")) {
	    query = "SELECT " + select_field_spec + " FROM " + table_name + " WHERE " + clause_1;
	} else {
	    query = "SELECT " + select_field_spec + " FROM " + table_name + clause + " AND "
		    + clause_1;
	}

	try {
	    if (m_dataSource == null) {
		statement = dbconn.createStatement();
	    } else {
		conn = m_dataSource.getConnection();
		statement = conn.createStatement();
	    }
	    rset = statement.executeQuery(query);
	    // Gets the result of the query
	    while (rset.next()) {
		final SnapAttributeHeavy snapAttributeHeavy = new SnapAttributeHeavy();
		snapAttributeHeavy.setAttribute_id(rset.getInt(1));
		snapAttributeHeavy.setRegistration_time(rset.getTimestamp(2));
		snapAttributeHeavy.setAttribute_complete_name(rset.getString(3));
		snapAttributeHeavy.setAttribute_device_name(rset.getString(4));
		snapAttributeHeavy.setDomain(rset.getString(5));
		snapAttributeHeavy.setFamily(rset.getString(6));
		snapAttributeHeavy.setMember(rset.getString(7));
		snapAttributeHeavy.setAttribute_name(rset.getString(8));
		snapAttributeHeavy.setData_type(rset.getInt(9));
		snapAttributeHeavy.setData_format(rset.getInt(10));
		snapAttributeHeavy.setWritable(rset.getInt(11));
		snapAttributeHeavy.setMax_dim_x(rset.getInt(12));
		snapAttributeHeavy.setMax_dim_y(rset.getInt(13));
		snapAttributeHeavy.setLevel(rset.getInt(14));
		snapAttributeHeavy.setCtrl_sys(rset.getString(15)); // facility
		// =
		// ctrl_sys??
		snapAttributeHeavy.setArchivable(rset.getInt(16));
		snapAttributeHeavy.setSubstitute(rset.getInt(17));
		definitionsList.add(snapAttributeHeavy);
	    }
	    statement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getAttDefinitionData() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(statement);
	    closeConnection(conn);
	}

	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
	// Returns the names list
	// System.out.println("query : " + query);
	return definitionsList;
    }

    public int get_context_last_id(final SnapContext snapContext) throws SnapshotingException {
	int contextID = -1;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	Connection conn = null;
	final String tableName = getDbSchema() + "." + GlobalConst.TABS[1];

	// Create and execute the SQL query string
	// Build the query string
	String query;
	final String select_fields = GlobalConst.TAB_CONTEXT[0];

	final String where_fields = GlobalConst.TAB_CONTEXT[1] + " = ?" + " AND "
		+ GlobalConst.TAB_CONTEXT[2] + " LIKE ?" + " AND " + GlobalConst.TAB_CONTEXT[3]
		+ " LIKE ?" + " AND " + GlobalConst.TAB_CONTEXT[4] + " LIKE ?" + " AND "
		+ GlobalConst.TAB_CONTEXT[5] + " LIKE ?";

	query = "SELECT MAX(" + select_fields + ")" + " FROM " + tableName + " WHERE "
		+ where_fields;

	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    // preparedStatement.setInt(1, snapContext.getId());
	    preparedStatement.setDate(1, snapContext.getCreation_date());
	    preparedStatement.setString(2, snapContext.getName());
	    preparedStatement.setString(3, snapContext.getAuthor_name());
	    preparedStatement.setString(4, snapContext.getReason());
	    preparedStatement.setString(5, snapContext.getDescription());

	    rset = preparedStatement.executeQuery();
	    if (rset.next()) {
		contextID = rset.getInt(1);
		preparedStatement.close();
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.get_context_last_id() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}

	return contextID;
    }

    /**
     * This method registers a new context in the database for snapshots.
     * 
     * @param snapContext
     *            the new context to register into the database for snapshots.
     * @return the context identifier (int) associated to the new registered
     *         context.
     * @throws SnapshotingException
     */
    public int create_context(final SnapContext snapContext) throws SnapshotingException {
	System.out.println("DataBaseAPI.create_context");
	int contextID = -1;
	if (db_type == ConfigConst.BD_MYSQL) {
	    Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    final String tableName = getDbSchema() + "." + GlobalConst.TABS[1];

	    // Create and execute the SQL query string
	    // Build the query string
	    String query;
	    final String insert_fields = // ConfigConst.TAB_CONTEXT[0] + ", " +
	    GlobalConst.TAB_CONTEXT[1] + ", " + GlobalConst.TAB_CONTEXT[2] + ", "
		    + GlobalConst.TAB_CONTEXT[3] + ", " + GlobalConst.TAB_CONTEXT[4] + ", "
		    + GlobalConst.TAB_CONTEXT[5];
	    final String insert_values = "?,?,?,?,?";

	    query = "INSERT INTO " + tableName + " (" + insert_fields + ")" + "VALUES ("
		    + insert_values + ")";
	    /*
	     * query_lock = "LOCK TABLES " + tableName + " READ, " + tableName +
	     * " WRITE"; query_unlock = "UNLOCK TABLES";
	     */
	    try {
		/*
		 * preparedStatement = dbconn.prepareStatement(query_lock);
		 * preparedStatement.executeUpdate();
		 */

		if (m_dataSource == null) {
		    preparedStatement = dbconn.prepareStatement(query);
		} else {
		    conn = m_dataSource.getConnection();
		    preparedStatement = conn.prepareStatement(query);
		}
		// preparedStatement.setInt(1, snapContext.getId());
		preparedStatement.setDate(1, snapContext.getCreation_date());
		preparedStatement.setString(2, snapContext.getName());
		preparedStatement.setString(3, snapContext.getAuthor_name());
		preparedStatement.setString(4, snapContext.getReason());
		preparedStatement.setString(5, snapContext.getDescription());
		preparedStatement.executeUpdate();

		/*
		 * preparedStatement = dbconn.prepareStatement(query_unlock);
		 * preparedStatement.executeUpdate();
		 */

		// preparedStatement.close();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.create_context() method...";
		final String desc = "";
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    } finally {
		closeStatement(preparedStatement);
		closeConnection(conn);
	    }

	    // todo r�cup�rer le last id
	    contextID = get_context_last_id(snapContext);
	    snapContext.setId(contextID);
	    // contextID = snapContext.getId();
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    CallableStatement callableStatement;
	    final String procName = "register_context";
	    final String query = "{? = call " + getDbSchema() + "." + procName + "(?, ?, ?, ?, ?)}";

	    try {
		callableStatement = dbconn.prepareCall(query);
		callableStatement.registerOutParameter(1, Types.INTEGER);

		callableStatement.setDate(2, snapContext.getCreation_date());
		callableStatement.setString(3, snapContext.getName());
		callableStatement.setString(4, snapContext.getAuthor_name());
		callableStatement.setString(5, snapContext.getReason());
		callableStatement.setString(6, snapContext.getDescription());
		callableStatement.executeQuery();
		// Gets the result of the query
		contextID = callableStatement.getInt(1);
		snapContext.setId(contextID);
		callableStatement.close();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.create_context() method...";
		final String desc = "";
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    }
	}
	setContextAssociatedAttributes(snapContext);
	return contextID;
    }

    /**
     * This method returns the list of attributes associated to a context
     * 
     * @param id_context
     *            The given context's identifier
     * @return The list of attributes associated to the given context
     * @throws SnapshotingException
     * @see SnapAttributeLight
     */
    public ArrayList getContextAssociatedAttributes(final int id_context)
	    throws SnapshotingException {
	final ArrayList attibutesList = new ArrayList(32);
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	Connection conn = null;

	// Create and execute the SQL query string
	String query = "";
	final String table_name_1 = getDbSchema() + "." + GlobalConst.TABS[0];
	final String table_name_2 = getDbSchema() + "." + GlobalConst.TABS[1];
	final String table_name_3 = getDbSchema() + "." + GlobalConst.TABS[2];
	final String tables = table_name_1 + ", " + table_name_2 + ", " + table_name_3;
	final String select_fields = GlobalConst.TAB_DEF[2] + ", " + // full_name
		GlobalConst.TAB_DEF[0] + ", " + // attribute_id
		GlobalConst.TAB_DEF[8] + ", " + // data_type
		GlobalConst.TAB_DEF[9] + ", " + // data_format
		GlobalConst.TAB_DEF[10]; // writable
	final String clause_1 = GlobalConst.TABS[0] + "." + GlobalConst.TAB_DEF[0] + " = "
		+ GlobalConst.TABS[2] + "." + GlobalConst.TAB_LIST[1];
	final String clause_2 = GlobalConst.TABS[2] + "." + GlobalConst.TAB_LIST[0] + " = "
		+ GlobalConst.TABS[1] + "." + GlobalConst.TAB_CONTEXT[0];
	final String clause_3 = GlobalConst.TABS[1] + "." + GlobalConst.TAB_CONTEXT[0] + " = ?";
	// the SQL request is : select DISTINCT full_name from snap.ast where
	// ast.id = list.id_att and list.id_context = context.id_context and
	// context.name = 'nom';
	query = "SELECT DISTINCT " + select_fields + " FROM " + tables + " WHERE " + clause_1
		+ " AND " + clause_2 + " AND " + clause_3;
	try {
	    if (m_dataSource != null) {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    } else {
		preparedStatement = dbconn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, id_context);
	    rset = preparedStatement.executeQuery();
	    // Gets the result of the query
	    while (rset.next()) {
		final SnapAttributeLight snapAttributeLight = new SnapAttributeLight();
		snapAttributeLight.setAttribute_complete_name(rset
			.getString(GlobalConst.TAB_DEF[2]));
		snapAttributeLight.setAttribute_id(rset.getInt(GlobalConst.TAB_DEF[0]));
		snapAttributeLight.setData_type(rset.getInt(GlobalConst.TAB_DEF[8]));
		snapAttributeLight.setData_format(rset.getInt(GlobalConst.TAB_DEF[9]));
		snapAttributeLight.setWritable(rset.getInt(GlobalConst.TAB_DEF[10]));
		attibutesList.add(snapAttributeLight);
	    }
	    // preparedStatement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getContextAssociatedAttributes() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
	// Close the connection with the database
	// Returns the list
	attibutesList.trimToSize();
	return attibutesList;
    }

    /**
     * Registers the link between an attribute and a given context.
     * 
     * @param snapContext
     * @throws SnapshotingException
     */
    public void setContextAssociatedAttributes(final SnapContext snapContext)
	    throws SnapshotingException {
	final int id_context = snapContext.getId();
	final ArrayList attList = snapContext.getAttributeList();
	PreparedStatement preparedStatement = null;
	Connection conn = null;
	final String tableName = getDbSchema() + "." + GlobalConst.TABS[2];

	String query;
	final String insert_fields = GlobalConst.TAB_LIST[0] + ", " + GlobalConst.TAB_LIST[1];
	query = "INSERT INTO " + tableName + " (" + insert_fields + ")" + " VALUES (?,?)";

	try {
	    if (m_dataSource != null) {
		conn = m_dataSource.getConnection();
	    }

	    for (int i = 0; i < attList.size(); i++) {
		final String att_full_name = (String) attList.get(i);
		final int id_att = getAttID(att_full_name);

		try {
		    if (conn == null) {
			preparedStatement = dbconn.prepareStatement(query);
		    } else {
			preparedStatement = conn.prepareStatement(query);
		    }
		    preparedStatement.setInt(1, id_context);
		    preparedStatement.setInt(2, id_att);
		    preparedStatement.executeUpdate();
		    // preparedStatement.close();
		} catch (final SQLException e) {
		    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.QUERY_FAILURE;
		    final String reason = "Failed while executing DataBaseAPI.setContextAssociatedAttributes() method...";
		    final String desc = "";
		    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
		} finally {
		    closeStatement(preparedStatement);
		}
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.setContextAssociatedAttributes() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} catch (final SnapshotingException e) {
	    throw e;
	} finally {
	    closeConnection(conn);
	}
    }

    /**
     * This method registers a new snapshots and retrieves its identifier
     * 
     * @param contextID
     *            the context identifier to wich the new registered snapshots
     *            belongs to.
     * @param timestamp
     *            the registration timestamp
     * @return the identifier for the new registered snapshot
     * @throws SnapshotingException
     * @see #getMaxContextID()
     * @see #registerSnap(int id_context)
     * @see #getSnapID(int id_context)
     * @see #updateSnapContextID(int id_snap, int initial_context_value)
     */
    public SnapShot createNewSnap(final int contextID, final Timestamp timestamp)
	    throws SnapshotingException {
	SnapShot snapShot = new SnapShot();
	if (db_type == ConfigConst.BD_MYSQL) {
	    snapShot = createNewSnapMySQL(contextID, timestamp);
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    snapShot = createNewSnapOracle(contextID, timestamp);
	}
	return snapShot;
    }

    /**
     * This method registers a new snapshots and retrieves its identifier
     * 
     * @param contextID
     *            the context identifier to wich the new registered snapshots
     *            belongs to.
     * @param timestamp
     *            the registration timestamp
     * @return the identifier for the new registered snapshot
     * @throws SnapshotingException
     * @see #getMaxContextID()
     * @see #registerSnap(int id_context)
     * @see #getSnapID(int id_context)
     * @see #updateSnapContextID(int id_snap, int initial_context_value)
     */
    private SnapShot createNewSnapMySQL(final int contextID, final Timestamp timestamp)
	    throws SnapshotingException {
	final SnapShot snapShot = new SnapShot();
	// Here we use a special pattern to be sure of the unicity of the
	// snapID...
	int snapID = -1;

	// int maxContextID = getMaxContextID();
	final int maxContextID = getMaxContextIDMySql();
	/*
	 * -- M = max -- R = Nombre_aleatoire( compris entre 1 et 100000 ) + M
	 * -- Ins�rons l'enregistrement, avec R � la place de id_context
	 */
	final Random rand = new Random(); // constructeur
	final int alea = rand.nextInt(100000); // g�n�ration

	final int pseudoContextRecord = maxContextID + alea;
	registerSnap(pseudoContextRecord);

	/*--  Nous r�cup�rons notre enregistrement --- > ID:*/
	snapID = getSnapID(pseudoContextRecord);

	// -- Pla�ons la valeur de l'index dans une variable ID, Et mettons �
	// jour la valeur de id_context :
	updateSnapContextID(snapID, contextID);
	snapShot.setId_context(contextID);
	snapShot.setId_snap(snapID);
	snapShot.setSnap_date(timestamp);
	snapShot.setAttribute_List(getContextAssociatedAttributes(contextID));
	return snapShot;
    }

    /**
     * This method registers a new snapshots and retrieves its identifier
     * 
     * @param contextID
     *            the context identifier to wich the new registered snapshots
     *            belongs to.
     * @param timestamp
     *            the registration timestamp
     * @return the identifier for the new registered snapshot
     * @throws SnapshotingException
     * @see #getMaxContextID()
     * @see #registerSnap(int id_context)
     * @see #getSnapID(int id_context)
     * @see #updateSnapContextID(int id_snap, int initial_context_value)
     */
    private SnapShot createNewSnapOracle(final int contextID, final Timestamp timestamp)
	    throws SnapshotingException {
	final SnapShot snapShot = new SnapShot();
	int snapID = -1;
	if (contextID > getMaxContextID()) {
	    return null;
	}
	CallableStatement cstmt_register_snapshot = null;
	final String query = "{? = call SNAP.register_snapshot(?, ?)}";

	try {
	    cstmt_register_snapshot = dbconn.prepareCall(query);
	    cstmt_register_snapshot.registerOutParameter(1, Types.INTEGER);
	    cstmt_register_snapshot.setInt(2, contextID);
	    cstmt_register_snapshot.setTimestamp(3, timestamp);
	    cstmt_register_snapshot.executeQuery();
	    // Gets the result of the query
	    snapID = cstmt_register_snapshot.getInt(1);
	    cstmt_register_snapshot.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.createNewSnapOracle() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(cstmt_register_snapshot);
	}

	snapShot.setId_context(contextID);
	snapShot.setId_snap(snapID);
	snapShot.setSnap_date(timestamp);
	snapShot.setAttribute_List(getContextAssociatedAttributes(contextID));
	return snapShot;
    }

    /**
     * This method is used to update the ContextID for a snap record
     * 
     * @param id_snap
     * @param initial_context_value
     * @throws SnapshotingException
     * @see #getMaxContextID()
     * @see #registerSnap(int id_context)
     * @see #getSnapID(int id_context)
     * @see #createNewSnap(int contextID, Timestamp timestamp)
     */
    private void updateSnapContextID(final int id_snap, final int initial_context_value)
	    throws SnapshotingException {
	PreparedStatement preparedStatement = null;
	final String table = getDbSchema() + "." + GlobalConst.TABS[3];
	final String setField = GlobalConst.TAB_SNAP[1];
	final String refField = GlobalConst.TAB_SNAP[0];
	final String query = "UPDATE " + table + " SET " + setField + "=? " + "WHERE " + refField
		+ "=?";
	Connection conn = null;

	try {
	    conn = m_dataSource.getConnection();

	    // preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement = conn.prepareStatement(query);
	    preparedStatement.setInt(1, initial_context_value);
	    preparedStatement.setInt(2, id_snap);
	    preparedStatement.executeUpdate();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.updateSnapContextID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
    }

    /**
     * Remplace le commentaire d'un snapshot donne par celui passe en argument.
     * Author : Laure Garda
     * 
     * @param id_snap
     * @param new_comment
     * @throws SnapshotingException
     */
    public void updateSnapComment(final int id_snap, final String new_comment)
	    throws SnapshotingException {
	PreparedStatement preparedStatement = null;
	Connection conn = null;
	final String table = getDbSchema() + "." + GlobalConst.TABS[3];
	final String setField = GlobalConst.TAB_SNAP[3];
	final String refField = GlobalConst.TAB_SNAP[0];
	final String query = "UPDATE " + table + " SET " + setField + " = '" + new_comment
		+ "' WHERE " + refField + " = ?";
	try {
	    if (m_dataSource == null) {
		preparedStatement = dbconn.prepareStatement(query);
	    } else {
		conn = m_dataSource.getConnection();
		preparedStatement = conn.prepareStatement(query);
	    }

	    preparedStatement.setInt(1, id_snap);
	    preparedStatement.executeUpdate();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.updateSnapComment() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
    }

    /**
     * This methos is used to retrieve the snapID when registered with a
     * temporary generated and unique contextID
     * 
     * @param id_context
     * @return the newly created snap identifier
     * @throws SnapshotingException
     * @see #getMaxContextID()
     * @see #registerSnap(int id_context)
     * @see #updateSnapContextID(int id_snap, int initial_context_value)
     * @see #createNewSnap(int contextID, Timestamp timestamp)
     */
    private int getSnapID(final int id_context) throws SnapshotingException {
	int idSnap = -1;
	final String table = getDbSchema() + "." + GlobalConst.TABS[3];
	final String selectField = GlobalConst.TAB_SNAP[0];
	final String clause_1 = GlobalConst.TAB_SNAP[1] + "=?";
	final String query = "SELECT " + selectField + " FROM " + table + " WHERE " + clause_1;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	Connection conn = null;
	try {
	    conn = m_dataSource.getConnection();

	    // preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement = conn.prepareStatement(query);
	    preparedStatement.setInt(1, id_context);
	    rset = preparedStatement.executeQuery();
	    // Gets the result of the query
	    if (rset.next()) {
		idSnap = rset.getInt(1);
	    }

	    return idSnap;
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getSnapID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
    }

    /**
     * This method is used to generate a temporary and unique contextID
     * 
     * @return The biggest context identifier number
     * @throws SnapshotingException
     * @see #registerSnap(int id_context)
     * @see #getSnapID(int id_context)
     * @see #updateSnapContextID(int id_snap, int initial_context_value)
     * @see #createNewSnap(int contextID, Timestamp timestamp)
     */
    private int getMaxContextID() throws SnapshotingException {
	int max = -1;
	Statement statement = null;
	ResultSet rset = null;
	final String table = getDbSchema() + "." + GlobalConst.TABS[1];
	final String selectField = GlobalConst.TAB_SNAP[1];
	final String query = "SELECT MAX(" + selectField + ") AS max FROM " + table;

	try {
	    dbconn.prepareStatement(query);
	    statement = dbconn.createStatement();
	    rset = statement.executeQuery(query);
	    // Gets the result of the query
	    if (rset.next()) {
		max = rset.getInt(1);
	    }
	    statement.close();
	    return max;
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getMaxContextID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(statement);
	}
    }

    private int getMaxContextIDMySql() throws SnapshotingException {
	int max = -1;
	Statement statement = null;
	ResultSet rset = null;
	Connection conn = null;
	final String table = getDbSchema() + "." + GlobalConst.TABS[3];
	final String selectField = GlobalConst.TAB_SNAP[1];
	final String query = "SELECT MAX(" + selectField + ") AS max FROM " + table;

	try {
	    conn = m_dataSource.getConnection();

	    conn.prepareStatement(query);
	    statement = conn.createStatement();
	    rset = statement.executeQuery(query);
	    // Gets the result of the query
	    if (rset.next()) {
		max = rset.getInt(1);
	    }
	    statement.close();
	    return max;
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.getMaxContextID() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeResultSet(rset);
	    closeStatement(statement);
	    closeConnection(conn);

	}
    }

    /**
     * This methods registers a new snapShot in the database, but with a wrong
     * context identifier
     * 
     * @param id_context
     * @throws SnapshotingException
     * @see #getMaxContextID()
     * @see #getSnapID(int id_context)
     * @see #updateSnapContextID(int id_snap, int initial_context_value)
     * @see #createNewSnap(int contextID, Timestamp timestamp)
     */
    private Timestamp registerSnap(final int id_context) throws SnapshotingException {
	final java.sql.Timestamp time = new java.sql.Timestamp(new java.util.Date().getTime());
	PreparedStatement preparedStatement = null;
	final String table = getDbSchema() + "." + GlobalConst.TABS[3];
	final String insert_fields = GlobalConst.TAB_SNAP[1] + ", " + GlobalConst.TAB_SNAP[2];
	final String insert_values = "?, ?";
	final String query = "INSERT INTO " + table + " (" + insert_fields + ") VALUES("
		+ insert_values + ")";
	Connection conn = null;

	try {
	    conn = m_dataSource.getConnection();
	    // preparedStatement = dbconn.prepareStatement(query);
	    preparedStatement = conn.prepareStatement(query);
	    preparedStatement.setInt(1, id_context);
	    preparedStatement.setTimestamp(2, time);
	    preparedStatement.executeUpdate();
	    return time;
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.registerSnap() method...";
	    final String desc = "" + "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(preparedStatement);
	    closeConnection(conn);
	}
    }

    // todo Miscenelaous INSERTS

    public void insert_ScalarData(final ScalarEvent scalarEvent) throws SnapshotingException {
	PreparedStatement preparedStatement;
	final StringBuffer tableName = new StringBuffer();
	tableName.append(getDbSchema());
	tableName.append(".");

	final StringBuffer insert_fields = new StringBuffer();
	final StringBuffer insert_values = new StringBuffer();
	final StringBuffer query = new StringBuffer();

	final Object value = scalarEvent.getValue();
	switch (scalarEvent.getWritable()) {
	case AttrWriteType._READ:
	    tableName
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TABS[8]
			    : GlobalConst.TABS[10]);
	    insert_fields
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RO_NUM[0]
			    + ", "
			    + GlobalConst.TAB_SC_RO_NUM[1]
			    + ", "
			    + GlobalConst.TAB_SC_RO_NUM[2]
			    : GlobalConst.TAB_SC_RO_STR[0] + ", " + GlobalConst.TAB_SC_RO_STR[1]
				    + ", " + GlobalConst.TAB_SC_RO_STR[2]);
	    insert_values.append("?, ?, ?");
	    break;
	case AttrWriteType._READ_WITH_WRITE:
	    tableName
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TABS[9]
			    : GlobalConst.TABS[11]);
	    insert_fields
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RW_NUM[0]
			    + ", "
			    + GlobalConst.TAB_SC_RW_NUM[1]
			    + ", "
			    + GlobalConst.TAB_SC_RW_NUM[2] + ", " + GlobalConst.TAB_SC_RW_NUM[3]
			    : GlobalConst.TAB_SC_RW_STR[0] + ", " + GlobalConst.TAB_SC_RW_STR[1]
				    + ", " + GlobalConst.TAB_SC_RW_STR[2] + ", "
				    + GlobalConst.TAB_SC_RW_STR[3]);
	    insert_values.append("?, ?, ?, ?");
	    break;
	case AttrWriteType._WRITE:
	    tableName
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TABS[8]
			    : GlobalConst.TABS[10]);

	    insert_fields
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_WO_NUM[0]
			    + ", "
			    + GlobalConst.TAB_SC_WO_NUM[1]
			    + ", "
			    + GlobalConst.TAB_SC_WO_NUM[2]
			    : GlobalConst.TAB_SC_WO_STR[0] + ", " + GlobalConst.TAB_SC_WO_STR[1]
				    + ", " + GlobalConst.TAB_SC_WO_STR[2]);
	    insert_values.append("?, ?, ?");
	    break;
	case AttrWriteType._READ_WRITE:
	    tableName
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TABS[9]
			    : GlobalConst.TABS[11]);

	    insert_fields
		    .append(scalarEvent.getData_type() != TangoConst.Tango_DEV_STRING ? GlobalConst.TAB_SC_RW_NUM[0]
			    + ", "
			    + GlobalConst.TAB_SC_RW_NUM[1]
			    + ", "
			    + GlobalConst.TAB_SC_RW_NUM[2] + ", " + GlobalConst.TAB_SC_RW_NUM[3]
			    : GlobalConst.TAB_SC_RW_STR[0] + ", " + GlobalConst.TAB_SC_RW_STR[1]
				    + ", " + GlobalConst.TAB_SC_RW_STR[2] + ", "
				    + GlobalConst.TAB_SC_RW_STR[3]);
	    insert_values.append("?, ?, ?, ?");
	    break;
	}
	query.append("INSERT INTO ").append(tableName).append(" (").append(insert_fields).append(
		")").append(" VALUES (").append(insert_values).append(")");
	// System.out.println("DataBaseApi/insert_ScalarData/query/"+query.toString());
	try {
	    preparedStatement = dbconn.prepareStatement(query.toString());

	    try {

		preparedStatement.setInt(1, scalarEvent.getId_snap());
		preparedStatement.setInt(2, scalarEvent.getId_att());
		if (value != null) {
		    if (scalarEvent.getWritable() == AttrWriteType._READ
			    || scalarEvent.getWritable() == AttrWriteType._WRITE) {
			switch (scalarEvent.getData_type()) {
			case TangoConst.Tango_DEV_STRING:
			    preparedStatement.setString(3, (String) value);
			    break;
			case TangoConst.Tango_DEV_STATE:
			    preparedStatement.setInt(3, ((DevState) value).value());
			    break;
			case TangoConst.Tango_DEV_UCHAR:
			    preparedStatement.setByte(3, ((Byte) value).byteValue());
			    break;
			case TangoConst.Tango_DEV_LONG:
			    preparedStatement.setInt(3, ((Integer) value).intValue());
			    break;
			case TangoConst.Tango_DEV_ULONG:
			    preparedStatement.setInt(3, ((Integer) value).intValue());
			    break;
			case TangoConst.Tango_DEV_BOOLEAN:
			    preparedStatement.setBoolean(3, ((Boolean) value).booleanValue());
			    break;
			case TangoConst.Tango_DEV_SHORT:
			    preparedStatement.setShort(3, ((Short) value).shortValue());
			    break;
			case TangoConst.Tango_DEV_FLOAT:
			    preparedStatement.setFloat(3, ((Float) value).floatValue());
			    break;
			case TangoConst.Tango_DEV_DOUBLE:
			    preparedStatement.setDouble(3, ((Double) value).doubleValue());
			    break;
			default:
			    preparedStatement.setDouble(3, ((Double) value).doubleValue());
			    break;
			}
		    } else { // if (writable == AttrWriteType._READ_WITH_WRITE
			// || writable == AttrWriteType._READ_WRITE)
			switch (scalarEvent.getData_type()) {
			case TangoConst.Tango_DEV_STRING:
			    if (((String[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.VARCHAR);
			    } else {
				preparedStatement.setString(3, ((String[]) value)[0]);
			    }
			    if (((String[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.VARCHAR);
			    } else {
				preparedStatement.setString(4, ((String[]) value)[1]);
			    }
			    break;
			case TangoConst.Tango_DEV_STATE:
			    if (((Integer[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setInt(3, ((Integer[]) value)[0].intValue());
			    }
			    if (((Integer[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setInt(4, ((Integer[]) value)[1].intValue());
			    }
			    break;
			case TangoConst.Tango_DEV_UCHAR:
			    if (((Byte[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setByte(3, ((Byte[]) value)[0].byteValue());
			    }
			    if (((Byte[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setByte(4, ((Integer[]) value)[1].byteValue());
			    }
			    break;
			case TangoConst.Tango_DEV_LONG:
			    if (((Integer[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setInt(3, ((Integer[]) value)[0].intValue());
			    }
			    if (((Integer[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setInt(4, ((Integer[]) value)[1].intValue());
			    }
			    break;
			case TangoConst.Tango_DEV_ULONG:
			    if (((Integer[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setInt(3, ((Integer[]) value)[0].intValue());
			    }
			    if (((Integer[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setInt(4, ((Integer[]) value)[1].intValue());
			    }
			    break;
			case TangoConst.Tango_DEV_BOOLEAN:
			    if (((Boolean[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.BOOLEAN);
			    } else {
				preparedStatement.setBoolean(3, ((Boolean[]) value)[0]
					.booleanValue());
			    }
			    if (((Boolean[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.BOOLEAN);
			    } else {
				preparedStatement.setBoolean(4, ((Boolean[]) value)[1]
					.booleanValue());
			    }
			    break;
			case TangoConst.Tango_DEV_SHORT:
			    if (((Short[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setShort(3, ((Short[]) value)[0].shortValue());
			    }
			    if (((Short[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setShort(4, ((Short[]) value)[1].shortValue());
			    }
			    break;
			case TangoConst.Tango_DEV_USHORT:
			    if (((Short[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setShort(3, ((Short[]) value)[0].shortValue());
			    }
			    if (((Short[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setShort(4, ((Short[]) value)[1].shortValue());
			    }
			    break;
			case TangoConst.Tango_DEV_FLOAT:
			    if (((Float[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setFloat(3, ((Float[]) value)[0].floatValue());
			    }
			    if (((Float[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setFloat(4, ((Float[]) value)[1].floatValue());
			    }
			    break;
			case TangoConst.Tango_DEV_DOUBLE:
			    if (((Double[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setDouble(3, ((Double[]) value)[0].doubleValue());
			    }
			    if (((Double[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setDouble(4, ((Double[]) value)[1].doubleValue());
			    }
			    break;
			default:
			    if (((Double[]) value)[0] == null) {
				preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setDouble(3, ((Double[]) value)[0].doubleValue());
			    }
			    if (((Float[]) value)[1] == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			    } else {
				preparedStatement.setDouble(4, ((Double[]) value)[1].doubleValue());
			    }
			    break;
			}
		    }

		} else {
		    if (scalarEvent.getWritable() == AttrWriteType._READ
			    || scalarEvent.getWritable() == AttrWriteType._WRITE) {
			preparedStatement.setNull(3, java.sql.Types.NUMERIC);
		    } else {
			preparedStatement.setNull(3, java.sql.Types.NUMERIC);
			preparedStatement.setNull(4, java.sql.Types.NUMERIC);
		    }

		}

		preparedStatement.executeUpdate();
	    } catch (final SQLException e) {
		preparedStatement.close();
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.insert_ScalarData() method...";
		final String desc = "Attribute name : " + scalarEvent.getAttribute_complete_name();
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    } finally {
		closeStatement(preparedStatement);
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX
		    + " : "
		    + (db_type == ConfigConst.BD_MYSQL ? GlobalConst.COMM_FAILURE_MYSQL
			    : GlobalConst.COMM_FAILURE_ORACLE);
	    final String reason = "Failed while executing DataBaseAPI.insert_ScalarData() method...";
	    final String desc = "Attribute name : " + scalarEvent.getAttribute_complete_name();
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	}

    }

    /**
     * Inserts a read only type scalar data intyo the snapshot database.
     * 
     * @param snapScalarEvent_RO
     * @throws SnapshotingException
     */
    public void insert_ScalarData_RO(final SnapScalarEvent_RO snapScalarEvent_RO)
	    throws SnapshotingException {
	PreparedStatement preparedStatement = null;
	final StringBuffer tableName = new StringBuffer().append(getDbSchema()).append(".").append(
		GlobalConst.TABS[8]);
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_SC_RO_NUM[0])
		.append(", ").append(GlobalConst.TAB_SC_RO_NUM[1]).append(", ").append(
			GlobalConst.TAB_SC_RO_NUM[2]);
	final StringBuffer insert_values = new StringBuffer("?, ?, ?");
	final StringBuffer query = new StringBuffer().append("INSERT INTO ").append(tableName)
		.append(" (").append(insert_fields).append(")").append(" VALUES (").append(
			insert_values).append(")");

	try {
	    preparedStatement = dbconn.prepareStatement(query.toString());
	    preparedStatement.setInt(1, snapScalarEvent_RO.getId_snap());
	    preparedStatement.setInt(2, snapScalarEvent_RO.getId_att());
	    if (snapScalarEvent_RO.getScalarValueRO() != null
		    && !snapScalarEvent_RO.getScalarValueRO().isNaN()) {
		preparedStatement.setDouble(3, snapScalarEvent_RO.getScalarValueRO().doubleValue());
	    } else {
		preparedStatement.setNull(3, java.sql.Types.NUMERIC);
	    }
	    preparedStatement.executeUpdate();
	    // preparedStatement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.insert_ScalarData_RO() method...";
	    final String desc = "Query : " + query;
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(preparedStatement);
	}
    }

    /**
     * Inserts a write only type scalar data intyo the snapshot database.
     * 
     * @param snapScalarEvent_WO
     * @throws SnapshotingException
     */
    public void insert_ScalarData_WO(final SnapScalarEvent_WO snapScalarEvent_WO)
	    throws SnapshotingException {
	PreparedStatement preparedStatement = null;
	final String tableName = getDbSchema() + "." + GlobalConst.TABS[8];
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_SC_WO_NUM[0])
		.append(", ").append(GlobalConst.TAB_SC_WO_NUM[1]).append(", ").append(
			GlobalConst.TAB_SC_WO_NUM[2]);
	final StringBuffer insert_values = new StringBuffer("?, ?, ?");
	final StringBuffer query = new StringBuffer().append("INSERT INTO ").append(tableName)
		.append(" (").append(insert_fields).append(")").append(" VALUES (").append(
			insert_values).append(")");

	try {
	    preparedStatement = dbconn.prepareStatement(query.toString());
	    preparedStatement.setInt(1, snapScalarEvent_WO.getId_snap());
	    preparedStatement.setInt(2, snapScalarEvent_WO.getId_att());
	    if (!Double.isNaN(snapScalarEvent_WO.getScalarValueWO())) {
		preparedStatement.setDouble(3, snapScalarEvent_WO.getScalarValueWO());
	    } else {
		preparedStatement.setNull(3, java.sql.Types.NUMERIC);
	    }
	    preparedStatement.executeUpdate();
	    // preparedStatement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.insert_ScalarData_WO() method...";
	    final String desc = "Query : " + query;
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(preparedStatement);
	}
    }

    /**
     * Inserts a read/write type scalar data intyo the snapshot database.
     * 
     * @param snapScalarEvent_RW
     * @throws SnapshotingException
     */
    public void insert_ScalarData_RW(final SnapScalarEvent_RW snapScalarEvent_RW)
	    throws SnapshotingException {
	PreparedStatement preparedStatement = null;
	final StringBuffer tableName = new StringBuffer().append(getDbSchema()).append(".").append(
		GlobalConst.TABS[9]);
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_SC_RW_NUM[0])
		.append(", ").append(GlobalConst.TAB_SC_RW_NUM[1]).append(", ").append(
			GlobalConst.TAB_SC_RW_NUM[2]).append(", ").append(
			GlobalConst.TAB_SC_RW_NUM[3]);
	final StringBuffer insert_values = new StringBuffer("?, ?, ?, ?");
	final StringBuffer query = new StringBuffer().append("INSERT INTO ").append(tableName)
		.append(" (").append(insert_fields).append(")").append(" VALUES (").append(
			insert_values).append(")");

	try {
	    preparedStatement = dbconn.prepareStatement(query.toString());
	    preparedStatement.setInt(1, snapScalarEvent_RW.getId_snap());
	    preparedStatement.setInt(2, snapScalarEvent_RW.getId_att());
	    if (snapScalarEvent_RW.getScalarValueRW()[0] != null
		    && !snapScalarEvent_RW.getScalarValueRW()[0].isNaN()) {
		preparedStatement.setDouble(3, snapScalarEvent_RW.getScalarValueRW()[0]
			.doubleValue());
	    } else {
		preparedStatement.setNull(3, java.sql.Types.NUMERIC);
	    }
	    if (snapScalarEvent_RW.getScalarValueRW()[1] != null
		    && !snapScalarEvent_RW.getScalarValueRW()[1].isNaN()) {
		preparedStatement.setDouble(4, snapScalarEvent_RW.getScalarValueRW()[1]
			.doubleValue());
	    } else {
		preparedStatement.setNull(4, java.sql.Types.NUMERIC);
	    }
	    preparedStatement.executeUpdate();
	    // preparedStatement.close();
	} catch (final SQLException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.QUERY_FAILURE;
	    final String reason = "Failed while executing DataBaseAPI.insert_ScalarData_RW() method...";
	    final String desc = "Query : " + query;
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	} finally {
	    closeStatement(preparedStatement);
	}

    }

    /**
     * Inserts a read only type spectrum data intyo the snapshot database.
     * 
     * @param snapSpectrumEvent_RO
     * @throws SnapshotingException
     */
    public void insert_SpectrumData_RO(final SnapSpectrumEvent_RO snapSpectrumEvent_RO)
	    throws SnapshotingException {
	int data_type = -1;
	Double[] dvalue = null;
	Float[] fvalue = null;
	Integer[] lvalue = null;
	Short[] svalue = null;
	Byte[] cvalue = null;
	Boolean[] bvalue = null;
	String[] stvalue = null;
	Statement statement;
	if (snapSpectrumEvent_RO.getValue() instanceof Double[]) {
	    dvalue = (Double[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_DOUBLE;
	} else if (snapSpectrumEvent_RO.getValue() instanceof Float[]) {
	    fvalue = (Float[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_FLOAT;
	} else if (snapSpectrumEvent_RO.getValue() instanceof Integer[]) {
	    lvalue = (Integer[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_LONG;
	} else if (snapSpectrumEvent_RO.getValue() instanceof Short[]) {
	    svalue = (Short[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_SHORT;
	} else if (snapSpectrumEvent_RO.getValue() instanceof Byte[]) {
	    cvalue = (Byte[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_CHAR;
	} else if (snapSpectrumEvent_RO.getValue() instanceof Boolean[]) {
	    bvalue = (Boolean[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_BOOLEAN;
	} else if (snapSpectrumEvent_RO.getValue() instanceof String[]) {
	    stvalue = (String[]) snapSpectrumEvent_RO.getValue();
	    data_type = TangoConst.Tango_DEV_STRING;
	}

	final int idSnap = snapSpectrumEvent_RO.getId_snap();
	final int idAtt = snapSpectrumEvent_RO.getId_att();
	final int dimX = snapSpectrumEvent_RO.getDim_x();
	final StringBuffer tableName = new StringBuffer().append(getDbSchema()).append(".").append(
		GlobalConst.TABS[6]);
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_SP_RO_NUM[0])
		.append(", ").append(GlobalConst.TAB_SP_RO_NUM[1]).append(", ").append(
			GlobalConst.TAB_SP_RO_NUM[2]).append(", ").append(
			GlobalConst.TAB_SP_RO_NUM[3]);
	StringBuffer query = new StringBuffer();
	final StringBuffer valueStr = new StringBuffer();
	if (db_type == ConfigConst.BD_MYSQL) {
	    valueStr.append("'[");
	    switch (data_type) {
	    case TangoConst.Tango_DEV_DOUBLE:
		for (int i = 0; i < dvalue.length - 1; i++) {
		    valueStr.append(dvalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (dvalue.length > 0) {
		    valueStr.append(dvalue[dvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_FLOAT:
		for (int i = 0; i < fvalue.length - 1; i++) {
		    valueStr.append(fvalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (fvalue.length > 0) {
		    valueStr.append(fvalue[fvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_LONG:
		for (int i = 0; i < lvalue.length - 1; i++) {
		    valueStr.append(lvalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (lvalue.length > 0) {
		    valueStr.append(lvalue[lvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_SHORT:
		for (int i = 0; i < svalue.length - 1; i++) {
		    valueStr.append(svalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (svalue.length > 0) {
		    valueStr.append(svalue[svalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_CHAR:
		for (int i = 0; i < cvalue.length - 1; i++) {
		    valueStr.append(cvalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (cvalue.length > 0) {
		    valueStr.append(cvalue[cvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_BOOLEAN:
		for (int i = 0; i < bvalue.length - 1; i++) {
		    valueStr.append(bvalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (bvalue.length > 0) {
		    valueStr.append(bvalue[bvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_STRING:
		for (int i = 0; i < stvalue.length - 1; i++) {
		    valueStr.append(stvalue[i]).append(GlobalConst.CLOB_SEPARATOR).append(" ");
		}
		if (stvalue.length > 0) {
		    valueStr.append(stvalue[stvalue.length - 1]);
		}
		break;
	    }
	    valueStr.append("]'");
	    System.out.println("*********valueStr=" + valueStr.toString());
	    query.append("INSERT INTO ").append(tableName).append(" (").append(insert_fields)
		    .append(")").append(" VALUES (").append(idSnap).append(",").append(idAtt)
		    .append(",").append(dimX).append(",").append(valueStr).append(")").toString();
	    System.out.println("*********query=" + query.toString());

	    try {
		statement = dbconn.createStatement();
		statement.executeUpdate(query.toString());
		statement.close();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.insert_SpectrumData_RO() method...";
		final String desc = "Query : " + query;
		e.printStackTrace();
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    }
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    CallableStatement cstmt_ins_spectrum_ro;
	    query = new StringBuffer().append("{call ").append(getDbSchema()).append(
		    ".sn_sp_1val (?, ?, ?, ?)}");

	    switch (data_type) {
	    case TangoConst.Tango_DEV_DOUBLE:
		for (int i = 0; i < dvalue.length - 1; i++) {
		    valueStr.append(dvalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (dvalue.length > 0) {
		    valueStr.append(dvalue[dvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_FLOAT:
		for (int i = 0; i < fvalue.length - 1; i++) {
		    valueStr.append(fvalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (fvalue.length > 0) {
		    valueStr.append(fvalue[fvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_LONG:
		for (int i = 0; i < lvalue.length - 1; i++) {
		    valueStr.append(lvalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (lvalue.length > 0) {
		    valueStr.append(lvalue[lvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_SHORT:
		for (int i = 0; i < svalue.length - 1; i++) {
		    valueStr.append(svalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (svalue.length > 0) {
		    valueStr.append(svalue[svalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_CHAR:
		for (int i = 0; i < cvalue.length - 1; i++) {
		    valueStr.append(cvalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (cvalue.length > 0) {
		    valueStr.append(cvalue[cvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_BOOLEAN:
		for (int i = 0; i < bvalue.length - 1; i++) {
		    valueStr.append(bvalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (bvalue.length > 0) {
		    valueStr.append(bvalue[bvalue.length - 1]);
		}
		break;

	    case TangoConst.Tango_DEV_STRING:
		for (int i = 0; i < stvalue.length - 1; i++) {
		    valueStr.append(stvalue[i]).append(GlobalConst.CLOB_SEPARATOR);
		}
		if (stvalue.length > 0) {
		    valueStr.append(stvalue[stvalue.length - 1]);
		}
		break;
	    }
	    try {

		cstmt_ins_spectrum_ro = dbconn.prepareCall(query.toString());
		cstmt_ins_spectrum_ro.setInt(1, idSnap);
		cstmt_ins_spectrum_ro.setString(2, snapSpectrumEvent_RO
			.getAttribute_complete_name());
		cstmt_ins_spectrum_ro.setInt(3, snapSpectrumEvent_RO.getDim_x());
		cstmt_ins_spectrum_ro.setString(4, valueStr.toString());

		cstmt_ins_spectrum_ro.executeUpdate();
		cstmt_ins_spectrum_ro.close();

	    } catch (final SQLException e) {
		String message = "";
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.INSERT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.insert_SpectrumData_RO() method..."
			+ "\r\n\t\t Query : "
			+ query
			+ "\r\n\t\t Param 1 (Attribute name) : "
			+ snapSpectrumEvent_RO.getAttribute_complete_name()
			+ "\r\n\t\t Param 2 (Id Snap)      : "
			+ idSnap
			+ "\r\n\t\t Param 3 (Dimension)      : "
			+ snapSpectrumEvent_RO.getDim_x()
			+ "\r\n\t\t Param 4 (Value)          : "
			+ valueStr.toString()
			+ "\r\n\t\t Code d'erreur : "
			+ e.getErrorCode()
			+ "\r\n\t\t Message : "
			+ e.getMessage()
			+ "\r\n\t\t SQL state : "
			+ e.getSQLState()
			+ "\r\n\t\t Stack : ";
		e.printStackTrace();

		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);
	    }
	}

    }

    /**
     * Inserts a read only type spectrum data intyo the snapshot database.
     * 
     * @param snapSpectrumEvent_rw
     * @throws SnapshotingException
     */
    public void insert_SpectrumData_RW(final SnapSpectrumEvent_RW snapSpectrumEvent_rw)
	    throws SnapshotingException {
	int data_type = -1;
	Double[] dvalueRead = null, dvalueWrite = null;
	Float[] fvalueRead = null, fvalueWrite = null;
	Integer[] lvalueRead = null, lvalueWrite = null;
	Short[] svalueRead = null, svalueWrite = null;
	Byte[] cvalueRead = null, cvalueWrite = null;
	Boolean[] bvalueRead = null, bvalueWrite = null;
	String[] stvalueRead = null, stvalueWrite = null;
	if (snapSpectrumEvent_rw.getValue() instanceof Double[]) {
	    dvalueRead = (Double[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    dvalueWrite = (Double[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_DOUBLE;
	} else if (snapSpectrumEvent_rw.getValue() instanceof Float[]) {
	    fvalueRead = (Float[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    fvalueWrite = (Float[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_FLOAT;
	} else if (snapSpectrumEvent_rw.getValue() instanceof Integer[]) {
	    lvalueRead = (Integer[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    lvalueWrite = (Integer[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_LONG;
	} else if (snapSpectrumEvent_rw.getValue() instanceof Short[]) {
	    svalueRead = (Short[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    svalueWrite = (Short[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_SHORT;
	} else if (snapSpectrumEvent_rw.getValue() instanceof Byte[]) {
	    cvalueRead = (Byte[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    cvalueWrite = (Byte[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_CHAR;
	} else if (snapSpectrumEvent_rw.getValue() instanceof Boolean[]) {
	    bvalueRead = (Boolean[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    bvalueWrite = (Boolean[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_BOOLEAN;
	} else if (snapSpectrumEvent_rw.getValue() instanceof String[]) {
	    stvalueRead = (String[]) snapSpectrumEvent_rw.getSpectrumValueRWRead();
	    stvalueWrite = (String[]) snapSpectrumEvent_rw.getSpectrumValueRWWrite();
	    data_type = TangoConst.Tango_DEV_STRING;
	}
	Statement statement;

	final int idSnap = snapSpectrumEvent_rw.getId_snap();
	final int idAtt = snapSpectrumEvent_rw.getId_att();
	final int dimX = snapSpectrumEvent_rw.getDim_x();

	// System.out.println (
	// "CLA/DatabaseApi/insert_SpectrumData_RW/valueRead.length/"+valueRead.length+"/valueWrite.length/"+valueWrite.length+"/dimX/"+dimX
	// );
	// CLA/DatabaseApi/insert_SpectrumData_RW/valueRead.length/128/valueWrite.length/128/dimX/256
	final StringBuffer tableName = new StringBuffer().append(getDbSchema()).append(".").append(
		GlobalConst.TABS[7]);
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_SP_RW_NUM[0])
		.append(", ").append(GlobalConst.TAB_SP_RW_NUM[1]).append(", ").append(
			GlobalConst.TAB_SP_RW_NUM[2]).append(", ").append(
			GlobalConst.TAB_SP_RW_NUM[3]).append(", ").append(
			GlobalConst.TAB_SP_RW_NUM[4]);
	StringBuffer query = new StringBuffer();
	final StringBuffer valueReadStr = new StringBuffer();
	final StringBuffer valueWriteStr = new StringBuffer();
	if (db_type == ConfigConst.BD_MYSQL) {
	    valueReadStr.append("'[");
	    valueWriteStr.append("'[");
	    switch (data_type) {
	    case TangoConst.Tango_DEV_DOUBLE:
		for (int i = 0; i < dvalueRead.length - 1; i++) {
		    valueReadStr.append(dvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(dvalueRead[dvalueRead.length - 1]);
		for (int i = 0; i < dvalueWrite.length - 1; i++) {
		    valueWriteStr.append(dvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(dvalueWrite[dvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_FLOAT:
		for (int i = 0; i < fvalueRead.length - 1; i++) {
		    valueReadStr.append(fvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(fvalueRead[fvalueRead.length - 1]);
		for (int i = 0; i < fvalueWrite.length - 1; i++) {
		    valueWriteStr.append(fvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(fvalueWrite[fvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_LONG:
		for (int i = 0; i < lvalueRead.length - 1; i++) {
		    valueReadStr.append(lvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(lvalueRead[lvalueRead.length - 1]);
		for (int i = 0; i < lvalueWrite.length - 1; i++) {
		    valueWriteStr.append(lvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(lvalueWrite[lvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_SHORT:
		for (int i = 0; i < svalueRead.length - 1; i++) {
		    valueReadStr.append(svalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(svalueRead[svalueRead.length - 1]);
		for (int i = 0; i < svalueWrite.length - 1; i++) {
		    valueWriteStr.append(svalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(svalueWrite[svalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_CHAR:
		for (int i = 0; i < cvalueRead.length - 1; i++) {
		    valueReadStr.append(cvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(cvalueRead[cvalueRead.length - 1]);
		for (int i = 0; i < cvalueWrite.length - 1; i++) {
		    valueWriteStr.append(cvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(cvalueWrite[cvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_BOOLEAN:
		for (int i = 0; i < bvalueRead.length - 1; i++) {
		    valueReadStr.append(bvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(bvalueRead[bvalueRead.length - 1]);
		for (int i = 0; i < bvalueWrite.length - 1; i++) {
		    valueWriteStr.append(bvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(bvalueWrite[bvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_STRING:
		for (int i = 0; i < stvalueRead.length - 1; i++) {
		    valueReadStr.append(stvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(stvalueRead[stvalueRead.length - 1]);
		for (int i = 0; i < stvalueWrite.length - 1; i++) {
		    valueWriteStr.append(stvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR)
			    .append(" ");
		}
		valueWriteStr.append(stvalueWrite[stvalueWrite.length - 1]);
		break;
	    }

	    valueReadStr.append("]'");
	    valueWriteStr.append("]'");
	    query.append("INSERT INTO ").append(tableName).append(" (").append(insert_fields)
		    .append(")").append(" VALUES (").append(idSnap).append(",").append(idAtt)
		    .append(",").append(dimX).append(",").append(valueReadStr).append(",").append(
			    valueWriteStr).append(")").toString();

	    // System.out.println("******query : |"+query.toString()+"|");
	    try {
		// System.out.println("*******insert_SpectrumData_RW test0");
		statement = dbconn.createStatement();
		// System.out.println("*******insert_SpectrumData_RW test1");
		statement.executeUpdate(query.toString());
		// System.out.println("*******insert_SpectrumData_RW test2");
		statement.close();
		// System.out.println("*******insert_SpectrumData_RW test3");
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.insert_SpectrumData_RW() method...";
		final String desc = "Query : " + query;
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    }
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    CallableStatement cstmt_ins_spectrum_rw;
	    query = new StringBuffer().append("{call ").append(getDbSchema()).append(
		    ".sn_sp_2val (?, ?, ?, ?, ?)}");

	    switch (data_type) {
	    case TangoConst.Tango_DEV_DOUBLE:
		for (int i = 0; i < dvalueRead.length - 1; i++) {
		    valueReadStr.append(dvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(dvalueRead[dvalueRead.length - 1]);
		for (int i = 0; i < dvalueWrite.length - 1; i++) {
		    valueWriteStr.append(dvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(dvalueWrite[dvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_FLOAT:
		for (int i = 0; i < fvalueRead.length - 1; i++) {
		    valueReadStr.append(fvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(fvalueRead[fvalueRead.length - 1]);
		for (int i = 0; i < fvalueWrite.length - 1; i++) {
		    valueWriteStr.append(fvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(fvalueWrite[fvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_LONG:
		for (int i = 0; i < lvalueRead.length - 1; i++) {
		    valueReadStr.append(lvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(lvalueRead[lvalueRead.length - 1]);
		for (int i = 0; i < lvalueWrite.length - 1; i++) {
		    valueWriteStr.append(lvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(lvalueWrite[lvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_SHORT:
		for (int i = 0; i < svalueRead.length - 1; i++) {
		    valueReadStr.append(svalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(svalueRead[svalueRead.length - 1]);
		for (int i = 0; i < svalueWrite.length - 1; i++) {
		    valueWriteStr.append(svalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(svalueWrite[svalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_CHAR:
		for (int i = 0; i < cvalueRead.length - 1; i++) {
		    valueReadStr.append(cvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(cvalueRead[cvalueRead.length - 1]);
		for (int i = 0; i < cvalueWrite.length - 1; i++) {
		    valueWriteStr.append(cvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(cvalueWrite[cvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_BOOLEAN:
		for (int i = 0; i < bvalueRead.length - 1; i++) {
		    valueReadStr.append(bvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(bvalueRead[bvalueRead.length - 1]);
		for (int i = 0; i < bvalueWrite.length - 1; i++) {
		    valueWriteStr.append(bvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueWriteStr.append(bvalueWrite[bvalueWrite.length - 1]);
		break;

	    case TangoConst.Tango_DEV_STRING:
		for (int i = 0; i < stvalueRead.length - 1; i++) {
		    valueReadStr.append(stvalueRead[i]).append(GlobalConst.CLOB_SEPARATOR).append(
			    " ");
		}
		valueReadStr.append(stvalueRead[stvalueRead.length - 1]);
		for (int i = 0; i < stvalueWrite.length - 1; i++) {
		    valueWriteStr.append(stvalueWrite[i]).append(GlobalConst.CLOB_SEPARATOR)
			    .append(" ");
		}
		valueWriteStr.append(stvalueWrite[stvalueWrite.length - 1]);
		break;
	    }

	    try {

		cstmt_ins_spectrum_rw = dbconn.prepareCall(query.toString());
		cstmt_ins_spectrum_rw.setInt(1, idSnap);
		cstmt_ins_spectrum_rw.setString(2, snapSpectrumEvent_rw
			.getAttribute_complete_name());
		cstmt_ins_spectrum_rw.setInt(3, snapSpectrumEvent_rw.getDim_x());
		cstmt_ins_spectrum_rw.setString(4, valueReadStr.toString());
		cstmt_ins_spectrum_rw.setString(5, valueWriteStr.toString());

		cstmt_ins_spectrum_rw.executeUpdate();
		cstmt_ins_spectrum_rw.close();

	    } catch (final SQLException e) {
		String message = "";
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.INSERT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.insert_SpectrumData_RW() method..."
			+ "\r\n\t\t Query : "
			+ query
			+ "\r\n\t\t Param 1 (Attribute name) : "
			+ snapSpectrumEvent_rw.getAttribute_complete_name()
			+ "\r\n\t\t Param 2 (Id Snap)      : "
			+ idSnap
			+ "\r\n\t\t Param 3 (Dimension)      : "
			+ snapSpectrumEvent_rw.getDim_x()
			+ "\r\n\t\t Param 4 (valueRead)          : "
			+ valueReadStr.toString()
			+ "\r\n\t\t Param 5 (valueWrite)          : "
			+ valueWriteStr.toString()
			+ "\r\n\t\t Code d'erreur : "
			+ e.getErrorCode()
			+ "\r\n\t\t Message : "
			+ e.getMessage()
			+ "\r\n\t\t SQL state : "
			+ e.getSQLState()
			+ "\r\n\t\t Stack : ";
		e.printStackTrace();

		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);
	    }
	}

    }

    /**
     * Inserts a read only type image data intyo the snapshot database.
     * 
     * @param snapImageEvent_RO
     * @throws SnapshotingException
     */
    public void insert_ImageData_RO(final SnapImageEvent_RO snapImageEvent_RO)
	    throws SnapshotingException {
	// Runtime runtime = Runtime.getRuntime ();
	// long freeMemory = runtime.freeMemory ();
	// long maxMemory = runtime.maxMemory ();
	// long totalMemory = runtime.totalMemory ();
	// System.out.println (
	// "\n*************\nGIR 0/freeMemory/"+freeMemory+"/maxMemory/"+maxMemory+"/totalMemory/"+totalMemory+"/\n*************"
	// );

	final int dim_x = snapImageEvent_RO.getDim_x();
	final int dim_y = snapImageEvent_RO.getDim_y();
	// int dim_x = 10;
	// int dim_y = 10;
	// System.out.println("dim_x,dim_y:"+dim_x+","+dim_y);

	final StringBuffer tableName = new StringBuffer().append(getDbSchema()).append(".").append(
		GlobalConst.TABS[4]);

	final StringBuffer valueStr = new StringBuffer();

	Double[][] dvalue = null;

	if (snapImageEvent_RO.getValue() instanceof Double[][]) {
	    dvalue = (Double[][]) snapImageEvent_RO.getValue();
	}

	if (dvalue != null) {
	    for (int i = 0; i < dim_y; i++) {
		for (int j = 0; j < dim_x; j++) {
		    valueStr.append(dvalue[i][j]);
		    if (j < dim_x - 1) {
			valueStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_COLS).append(" ");
		    }
		}

		if (i < dim_y - 1) {
		    valueStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS).append(" ");
		}
	    }
	}
	final StringBuffer query = new StringBuffer();
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_IM_RO_NUM[0])
		.append(", ").append(GlobalConst.TAB_IM_RO_NUM[1]).append(", ").append(
			GlobalConst.TAB_IM_RO_NUM[2]).append(", ").append(
			GlobalConst.TAB_IM_RO_NUM[3]).append(", ").append(
			GlobalConst.TAB_IM_RO_NUM[4]);
	final StringBuffer insert_values = new StringBuffer("?, ?, ?, ?, ?");
	if (db_type == ConfigConst.BD_MYSQL) {

	    query.append("INSERT INTO ").append(tableName).append(" (").append(insert_fields)
		    .append(")").append(" VALUES (").append(insert_values).append(")");
	    // System.out.println("@@@@@@@@@@@@@@@@@@@@\n"+query.toString()+"\n@@@@@@@@@@@@@@@@@@@@");
	    // System.out.println("@@@@@@@@@@@@@@@@@@@@\n"+valueStr.toString()+"\n@@@@@@@@@@@@@@@@@@@@");
	    PreparedStatement preparedStatement = null;
	    try {
		preparedStatement = dbconn.prepareStatement(query.toString());
		preparedStatement.setInt(1, snapImageEvent_RO.getId_snap());
		preparedStatement.setInt(2, snapImageEvent_RO.getId_att());
		preparedStatement.setInt(3, dim_x);
		preparedStatement.setInt(4, dim_y);

	    } catch (final SQLException e1) {
		String message = "";
		if (e1.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e1.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.INSERT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.insert_ImageData_RO() method...";
		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e1);
	    }
	    try {
		if (dvalue != null) {
		    preparedStatement.setString(5, valueStr.toString());
		    // System.out.println("***********petit test : " +
		    // valueStr.toString().split(GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS).length);
		    // System.out.println("***********petit test 2 : " +
		    // valueStr.toString().split(GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS)[0].split(GlobalConst.CLOB_SEPARATOR_IMAGE_COLS).length);
		} else {
		    preparedStatement.setNull(5, java.sql.Types.BLOB);
		}
		preparedStatement.executeUpdate();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.insert_ImageData_RO() method...";
		final String desc = "Query : " + query;
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    } finally {
		closeStatement(preparedStatement);
	    }
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    CallableStatement cstmt_ins_image_ro = null;
	    query.append("{call ").append(tableName).append(" (").append(insert_fields)
		    .append(")}");

	    try {
		cstmt_ins_image_ro = dbconn.prepareCall(query.toString());
		cstmt_ins_image_ro.setInt(1, snapImageEvent_RO.getId_snap());
		cstmt_ins_image_ro.setString(2, snapImageEvent_RO.getAttribute_complete_name());
		cstmt_ins_image_ro.setInt(3, dim_x);
		cstmt_ins_image_ro.setInt(4, dim_y);
		if (dvalue != null) {
		    cstmt_ins_image_ro.setString(5, valueStr.toString());
		} else {
		    cstmt_ins_image_ro.setNull(5, java.sql.Types.CLOB);
		}

		cstmt_ins_image_ro.executeUpdate();
	    } catch (final SQLException e) {
		String message = "";
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.INSERT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.insert_ImageData_RO() method..."
			+ "\r\n\t\t Query : "
			+ query
			+ "\r\n\t\t Param 1 (Attribute name) : "
			+ snapImageEvent_RO.getAttribute_complete_name()
			+ "\r\n\t\t Param 2 (Id Snap)        : "
			+ snapImageEvent_RO
			+ "\r\n\t\t Param 3 (X Dimension)    : "
			+ snapImageEvent_RO.getDim_x()
			+ "\r\n\t\t Param 4 (Y Dimension)    : "
			+ snapImageEvent_RO.getDim_y()
			+ "\r\n\t\t Param 5 (Value)          : "
			+ (dvalue == null ? null : valueStr.toString())
			+ "\r\n\t\t Error Code               : "
			+ e.getErrorCode()
			+ "\r\n\t\t Message                  : "
			+ e.getMessage()
			+ "\r\n\t\t SQL state                : "
			+ e.getSQLState()
			+ "\r\n\t\t Stack                    : ";
		e.printStackTrace();

		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);
	    } finally {
		try {
		    if (cstmt_ins_image_ro != null) {
			cstmt_ins_image_ro.close();
		    }
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
    }

    /**
     * Inserts a read write type image data intyo the snapshot database.
     * 
     * @param snapImageEvent_RW
     * @throws SnapshotingException
     */
    public void insert_ImageData_RW(final SnapImageEvent_RW snapImageEvent_RW)
	    throws SnapshotingException {
	final int dim_x = snapImageEvent_RW.getDim_x();
	final int dim_y = snapImageEvent_RW.getDim_y();

	final int dim_x_write = snapImageEvent_RW.getDim_x_write();
	final int dim_y_write = snapImageEvent_RW.getDim_y_write();

	final StringBuffer tableName = new StringBuffer().append(getDbSchema()).append(".").append(
		GlobalConst.TABS[5]);

	final StringBuffer valueReadStr = new StringBuffer();
	final StringBuffer valueWriteStr = new StringBuffer();

	final Double[][] readValue = snapImageEvent_RW.getImageValueRWRead();
	final Double[][] writeValue = snapImageEvent_RW.getImageValueRWWrite();

	if (readValue != null) {
	    for (int i = 0; i < dim_y; i++) {
		for (int j = 0; j < dim_x; j++) {
		    valueReadStr.append(readValue[i][j]);
		    if (j < dim_x - 1) {
			valueReadStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_COLS).append(" ");
		    }
		}

		if (i < dim_y - 1) {
		    valueReadStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS).append(" ");
		}
	    }
	}

	if (writeValue != null) {
	    for (int i = 0; i < dim_y_write; i++) {
		for (int j = 0; j < dim_x_write; j++) {
		    valueWriteStr.append(writeValue[i][j]);
		    if (j < dim_x_write - 1) {
			valueWriteStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_COLS).append(" ");
		    }
		}

		if (i < dim_y_write - 1) {
		    valueWriteStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS).append(" ");
		}
	    }
	}
	final StringBuffer query = new StringBuffer();
	final StringBuffer insert_fields = new StringBuffer().append(GlobalConst.TAB_IM_RW_NUM[0])
		.append(", ").append(GlobalConst.TAB_IM_RW_NUM[1]).append(", ").append(
			GlobalConst.TAB_IM_RW_NUM[2]).append(", ").append(
			GlobalConst.TAB_IM_RW_NUM[3]).append(", ").append(
			GlobalConst.TAB_IM_RW_NUM[4]).append(", ").append(
			GlobalConst.TAB_IM_RW_NUM[5]);
	final StringBuffer insert_values = new StringBuffer("?, ?, ?, ?, ?");
	if (db_type == ConfigConst.BD_MYSQL) {

	    query.append("INSERT INTO ").append(tableName).append(" (").append(insert_fields)
		    .append(")").append(" VALUES (").append(insert_values).append(")");

	    PreparedStatement preparedStatement = null;
	    try {
		preparedStatement = dbconn.prepareStatement(query.toString());
		preparedStatement.setInt(1, snapImageEvent_RW.getId_snap());
		preparedStatement.setInt(2, snapImageEvent_RW.getId_att());
		preparedStatement.setInt(3, dim_x);
		preparedStatement.setInt(4, dim_y);

	    } catch (final SQLException e1) {
		String message = "";
		if (e1.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e1.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.INSERT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.insert_ImageData_RO() method...";
		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e1);
	    }
	    try {
		if (readValue != null) {
		    preparedStatement.setString(5, valueReadStr.toString());
		} else {
		    preparedStatement.setNull(5, java.sql.Types.BLOB);
		}

		if (writeValue != null) {
		    preparedStatement.setString(6, valueWriteStr.toString());
		} else {
		    preparedStatement.setNull(6, java.sql.Types.BLOB);
		}
		preparedStatement.executeUpdate();
	    } catch (final SQLException e) {
		final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			+ GlobalConst.QUERY_FAILURE;
		final String reason = "Failed while executing DataBaseAPI.insert_ImageData_RO() method...";
		final String desc = "Query : " + query;
		throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	    } finally {
		closeStatement(preparedStatement);
	    }
	} else if (db_type == ConfigConst.BD_ORACLE) {
	    CallableStatement cstmt_ins_image_rw = null;
	    query.append("{call ").append(tableName).append(" (").append(insert_fields)
		    .append(")}");

	    try {
		cstmt_ins_image_rw = dbconn.prepareCall(query.toString());
		cstmt_ins_image_rw.setInt(1, snapImageEvent_RW.getId_snap());
		cstmt_ins_image_rw.setString(2, snapImageEvent_RW.getAttribute_complete_name());
		cstmt_ins_image_rw.setInt(3, dim_x);
		cstmt_ins_image_rw.setInt(4, dim_y);
		if (readValue != null) {
		    cstmt_ins_image_rw.setString(5, valueReadStr.toString());
		} else {
		    cstmt_ins_image_rw.setNull(5, java.sql.Types.CLOB);
		}

		if (writeValue != null) {
		    cstmt_ins_image_rw.setString(6, valueWriteStr.toString());
		} else {
		    cstmt_ins_image_rw.setNull(6, java.sql.Types.CLOB);
		}

		cstmt_ins_image_rw.executeUpdate();
	    } catch (final SQLException e) {
		String message = "";
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.INSERT_FAILURE;
		final String desc = "Failed while executing DataBaseApi.insert_ImageData_RO() method..."
			+ "\r\n\t\t Query : "
			+ query
			+ "\r\n\t\t Param 1 (Attribute name) : "
			+ snapImageEvent_RW.getAttribute_complete_name()
			+ "\r\n\t\t Param 2 (Id Snap)        : "
			+ snapImageEvent_RW
			+ "\r\n\t\t Param 3 (X Dimension)    : "
			+ snapImageEvent_RW.getDim_x()
			+ "\r\n\t\t Param 4 (Y Dimension)    : "
			+ snapImageEvent_RW.getDim_y()
			+ "\r\n\t\t Param 5 (Read Value)     : "
			+ (readValue == null ? null : readValue.toString())
			+ "\r\n\t\t Param 6 (Write Value)    : "
			+ (writeValue == null ? null : writeValue.toString())
			+ "\r\n\t\t Error Code               : "
			+ e.getErrorCode()
			+ "\r\n\t\t Message                  : "
			+ e.getMessage()
			+ "\r\n\t\t SQL state                : "
			+ e.getSQLState()
			+ "\r\n\t\t Stack                    : ";
		e.printStackTrace();

		throw new SnapshotingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);
	    } finally {
		try {
		    if (cstmt_ins_image_rw != null) {
			cstmt_ins_image_rw.close();
		    }
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	// Close the connection with the database
	if (getAutoConnect()) {
	    close();
	}
    }

    /**
     * Method that writes a read only type image data into a file.
     * 
     * @param rootPath
     *            the root directory path into wich the file will be writen
     * @param snapImageEvent_ro
     *            the read only type image data
     * @return the full path to the file (directory + file name)
     * @throws SnapshotingException
     */
    public String writeInFile_SnapImageEvent_RO(final String rootPath,
	    final SnapImageEvent_RO snapImageEvent_ro) throws SnapshotingException {
	// String contextPath = root_dir + File.separator +
	// snapImageEvent_ro.getId_snap();
	final File snapDir = new File(rootPath);
	if (!snapDir.exists()) {
	    snapDir.mkdirs();
	}

	final String full_file_path = snapDir + File.separator + "snap_"
		+ snapImageEvent_ro.getId_snap() + "_" + snapImageEvent_ro.getId_att() + ".txt";
	try {
	    final FileWriter fileWriter = new FileWriter(full_file_path);
	    final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	    bufferedWriter.write(snapImageEvent_ro.getId_snap() + ",");
	    bufferedWriter.newLine();
	    bufferedWriter.write(snapImageEvent_ro.getId_att() + ",");
	    bufferedWriter.newLine();
	    bufferedWriter.write(snapImageEvent_ro.getDim_x() + ",");
	    bufferedWriter.newLine();
	    bufferedWriter.write(snapImageEvent_ro.getDim_y() + ",");
	    bufferedWriter.newLine();
	    bufferedWriter.flush();

	    final Double[][] value = snapImageEvent_ro.getImageValueRO();
	    for (final Double[] element : value) { // lignes
		for (int j = 0; j < element.length - 1; j++) { // colonnes
		    bufferedWriter.write(element[j] + "\t");
		}
		bufferedWriter.write(element[element.length - 1] + "");
		bufferedWriter.newLine();
		bufferedWriter.flush();
	    }
	    bufferedWriter.newLine();
	    bufferedWriter.write("%%%");
	    bufferedWriter.flush();
	    bufferedWriter.close();
	    fileWriter.close();
	} catch (final IOException e) {
	    final String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : "
		    + GlobalConst.WRITING_FILE_EXCEPTION;
	    final String reason = "Failed while executing DataBaseAPI.writeInFile_SnapImageEvent_RO() method...";
	    final String desc = "";
	    throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", e);
	}
	return full_file_path;
    }

    /*****************************************************************************
     * 
     * 
     * Miscellaneous others methods used by the API
     * 
     * 
     ****************************************************************************/

    /**
     * ************************************************************************
     * <b>Description : </b> Build a array of String with the given String
     * Vector
     * 
     * @param my_vector
     *            The given String Vector
     * @return a String type array that contains the differents vector's String
     *         type elements <br>
     *         ******************************************************************
     *         *******
     */
    private String[] toStringArray(final Vector my_vector) {
	String[] my_array;
	my_array = new String[my_vector.size()];
	for (int i = 0; i < my_vector.size(); i++) {
	    my_array[i] = (String) my_vector.elementAt(i);
	}
	return my_array;
    }

    /**
     * ************************************************************************
     * <b>Description : </b> Build a array of Double with the given Double
     * Vector
     * 
     * @param my_vector
     *            The given Double Vector
     * @return a Double type array that contains the differents vector's Double
     *         type elements <br>
     *         ******************************************************************
     *         *******
     */
    /*
     * private double[] toDoubleArray(Vector my_vector) { double[] my_array;
     * my_array = new double[ my_vector.size() ]; for ( int i = 0 ; i <
     * my_vector.size() ; i++ ) { my_array[ i ] = ( ( Double )
     * my_vector.elementAt(i) ).doubleValue(); } return my_array; }
     * 
     * private int[] toIntArray(Vector my_vector) { int[] my_array; my_array =
     * new int[ my_vector.size() ]; for ( int i = 0 ; i < my_vector.size() ; i++
     * ) { my_array[ i ] = ( ( Integer ) my_vector.elementAt(i) ).intValue(); }
     * return my_array; }
     * 
     * private Object[] toArray(Vector my_vector) { Object[] my_array; my_array
     * = new Object[ my_vector.size() ]; for ( int i = 0 ; i < my_vector.size()
     * ; i++ ) { my_array[ i ] = ( ( Object ) my_vector.elementAt(i) ); } return
     * my_array; }
     */

    /**
     * @return Returns the currentSpectrumDimX.
     */
    public int getCurrentSpectrumDimX() {
	return currentSpectrumDimX;
    }

    /**
     * @param currentSpectrumDimX
     *            The currentSpectrumDimX to set.
     */
    public void setCurrentSpectrumDimX(final int currentSpectrumDimX) {
	this.currentSpectrumDimX = currentSpectrumDimX;
    }
}
