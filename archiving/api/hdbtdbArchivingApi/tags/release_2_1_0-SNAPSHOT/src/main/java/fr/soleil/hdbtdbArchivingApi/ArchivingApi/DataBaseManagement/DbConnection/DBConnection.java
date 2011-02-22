/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public abstract class DBConnection implements IDBConnection {
	///////////////////////////////////Attributes///////////////////////////////

	/**
	 * Connection object used
	 */
//	private Connection conn;

	/**
	 * JDBC driver used for the connection
	 */
	private String driver;
	/**
	 * Database Host adress
	 */
	private  String host;
	/**
	 * User's name for the connection
	 */
	private String user;
	/**
	 * User's password for the connection
	 */
	private String passwd;
	/**
	 * database'schema' used
	 */
	private String schema;
	/**
	 * database name
	 */
	private String name;
	/**
	 * Connection dbatabase type (<I>MySQL</I>, <I>Oracle</I>, ...)
	 */

	private boolean autoConnect;

	private   DataSource m_dataSource = null;
	public Statement lastStatement = null;
	protected Hashtable idsBuffer;
	private int dbType;
	//////////////////////////////////Constructors////////////////////////////

	/**
	 * Default constructor. Never used
	 */
	public DBConnection()
	{
		idsBuffer = new Hashtable ();
		
	}

	/**
	 * Constructor using database name.
	 * Not used directly
	 * @param host_name Identifier (name or IP adress) of the machine which supplies the service "data base <I>HDB</I>"
	 * @param db_name   database name
	 * @param db_schema Name of the database's schema used
	 */
	public DBConnection(String host_name , String db_name , String db_schema)
	{
		idsBuffer = new Hashtable ();

		setHost(host_name);
		setName(db_name);
		setSchema(db_schema);
	}


	/**
	 * Constructor using host name, user name, password and database name.
	 *
	 * @param host_name Identifier (name or IP adress) of the machine which supplies the service "data base <I>HDB</I>"
	 * @param db_name   Name of the data base used
	 * @param db_schema Name of the database's schema used
	 * @param user_name Name to use to connect
	 * @param password  Password to use to connect
	 */
	public DBConnection(String host_name , String db_name , String db_schema , String user_name , String password)
	{
		this(host_name , db_name , db_schema);        
		setUser(user_name);
		setPasswd(password);
	}

	////////////////////////////////////Methods///////////////////////////////////
	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#close()
	 */
	public void close() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(m_dataSource!=null)
			try {
				((OracleDataSource)m_dataSource).close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				String message  = DbUtilsFactory.getInstance(dbType).getCommunicationFailureMessage(e); 
				//String message = ConfigConst.ARCHIVING_ERROR_PREFIX + " : ";
				String reason = GlobalConst.STATEMENT_FAILURE;
				String desc = "Failed while executing DataBaseApi.close() method...";
				try {
					throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
				} catch (ArchivingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#closeConnection(com.mysql.jdbc.Connection)
	 */
	public void closeConnection(Connection conn) {
		// TODO Auto-generated method stub
		try {
			if(conn!=null ){
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#connect()
	 */
	public abstract void connect(boolean rac) throws ArchivingException;
	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#getConnection()
	 */
	public  Connection getConnection() {
		try {
			// TODO Auto-generated method stub
			return m_dataSource.getConnection();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#getConnectionObject()
	 */

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#getInfo()
	 */
	public String getInfo() {
		// TODO Auto-generated method stub
		String msg = null;
		// Then get info from the database
		Connection conn = null;
		try
		{
			conn = getConnection();
			DatabaseMetaData db_info = conn.getMetaData();

			msg = "\tUser : " + db_info.getUserName() + "\n\tdatabase name : " + name;
			msg = msg + "\n\tdatabase product : " + db_info.getDatabaseProductVersion();
			msg = msg + "\n\tURL : " + db_info.getURL();
			msg = msg + "\n\tDriver name : " + db_info.getDriverName();
			msg = msg + " Version " + db_info.getDriverVersion();
			msg = msg + "\n\tdatabase modes : ";
			if ( conn.getAutoCommit() )
				msg = msg + "AUTO COMMIT ; ";
			else
				msg = msg + "MANUAL COMMIT ; ";

			if ( conn.isReadOnly() )
				msg = msg + "READ ONLY ; ";
			else
				msg = msg + "READ-WRITE ; ";

			if ( db_info.usesLocalFiles() )
				msg = msg + "USES LOCAL FILES";
			else
				msg = msg + "DONT USE LOCAL FILES";

		}
		catch ( SQLException e )
		{
			closeConnection(conn);
			String message  = DbUtilsFactory.getInstance(dbType).getCommunicationFailureMessage(e); 
			String reason = GlobalConst.STATEMENT_FAILURE;
			String desc = "Failed while executing DataBaseApi.getInfo() method...";
			try {
				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			} catch (ArchivingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally{
			closeConnection(conn);
		}
		return msg;


	}
	/**
	 * <b>Description : </b>    	Build the url string and get a connection object
	 *
	 * @throws ArchivingException
	 */


	/**
	 * <b>Description</b>    	: Sets the auto commit mode to "true" or "false"
	 *
	 * @param value The mode value
	 * @throws ArchivingException
	 */
	public void setAutoCommit(boolean value, Connection conn) throws ArchivingException
	{
		// Set commit mode to manual

		try
		{
			conn.setAutoCommit(value);
		}
		catch ( SQLException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
			String reason = GlobalConst.STATEMENT_FAILURE;
			String desc = "Failed while executing DataBaseApi.setAutoCommit() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);


		}
		finally{
			if(conn!=null)
				closeConnection(conn);
		}
	}

	///////////////////////////Abstract Methods///////////////////////////////


	protected abstract void setDataBaseUrl(String url) ;

	protected abstract void loadDriver() ;
	abstract public boolean getAutoCommit();


	/////////////////////////////Getters/Setters////////////////////////////////////
	public void setDbType(int type){
		dbType = type;
	}
	public int getDbType(){
		return dbType;
	}

	public boolean isAutoConnect() {
		return autoConnect;
	}
	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}



	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDriver() {
		return driver;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	public void setSchema(String db_schema) {
		// TODO Auto-generated method stub
		this.schema = db_schema;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public String getSchema() {
		return this.schema;
	}

	public DataSource getM_dataSource() {
		return m_dataSource;
	}

	public void setM_dataSource(DataSource source) {
		m_dataSource = source;
	}

	public Statement getLastStatement() {
		return lastStatement;
	}

	public void setLastStatement(Statement lastStatement) {
		this.lastStatement = lastStatement;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Hashtable getIdsBuffer() {
		return idsBuffer;
	}

	public void setIdsBuffer(Hashtable idsBuffer) {
		this.idsBuffer = idsBuffer;
	}



}
