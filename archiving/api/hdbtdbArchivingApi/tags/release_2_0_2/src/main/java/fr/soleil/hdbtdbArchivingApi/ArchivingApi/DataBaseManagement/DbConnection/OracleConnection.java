/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection.IPoolConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class OracleConnection extends DBConnection {
	public OracleConnection(int type, IDbUtils ut, IPoolConnection pc){
		super(pc);
		dbUtils = ut;
		setDriver(ConfigConst.DRIVER_ORACLE);
		setDbType(type);
	}
	public OracleConnection( int type, IDbUtils ut, String host , String name , String schema , String user , String pass, IPoolConnection pc){
		super(host, name, schema, user, pass, pc);
		dbUtils = ut;
		setDriver(ConfigConst.DRIVER_ORACLE);
		setDbType(type);


	}

	   /**
	  * <b>Description : </b>  Gets the current database's autocommit <br>
	  *
	  * @return True if database is in autocommit mode
	  */
	 public boolean getAutoCommit()
	 {
	             return ConfigConst.AUTO_COMMIT_ORACLE;
	 }

	/**
	 * <b>Description : </b> Allows to connect to the <I>HDB</I> database when of <I>Oracle</I> type.
	 *
	 * @throws ArchivingException
	 */
	public void connect() throws ArchivingException
	{
		// Load the driver
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");  //pilote Oracle
			String url = ConfigConst.DRIVER_ORACLE + ":@" + getHost() + ":" + ConfigConst.ORACLE_PORT + ":" + getName(); //pilote Oracle
			setDriver(ConfigConst.DRIVER_ORACLE);
			setM_dataSource(poolConnection.getPoolInstance(url, getUser(), getPasswd()));
		}
		catch ( ClassNotFoundException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.DRIVER_MISSING;
			String reason = "Failed while executing OracleConnection.connect_oracle() method...";
			String desc = "No Oracle driver available..., please check !";
			throw new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , e);
		}
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.IDBConnection#getConnection()
	 */
	public  Connection getConnection() {
		try {
			Connection conn =  getM_dataSource().getConnection();
			alterSession(conn);			
		// Display pooling cache info
//			OracleConnectionCacheManager occm =
//		          OracleConnectionCacheManager.getConnectionCacheManagerInstance();
//				
//				System.out.println("------------------------------------------------------------------");
//				System.out.println("Cache name: " + ((OracleDataSource)getM_dataSource()).getConnectionCacheName());
//				System.out.println("Active connections : " + occm.getNumberOfActiveConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
//				System.out.println("Connections available: " + occm.getNumberOfAvailableConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
//				System.out.println("------------------------------------------------------------------");
				return conn;
				
//			} 
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch(ArchivingException e){
			return null;
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
				if(getM_dataSource()!=null){
//				OracleConnectionCacheManager occm =
//			          OracleConnectionCacheManager.getConnectionCacheManagerInstance();
//				System.out.println("------------------------------------------------------------------");
//				System.out.println("Cache name: " + ((OracleDataSource)getM_dataSource()).getConnectionCacheName());
//				System.out.println("Active connections : " + occm.getNumberOfActiveConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
//				System.out.println("Connections available: " + occm.getNumberOfAvailableConnections(((OracleDataSource)getM_dataSource()).getConnectionCacheName()));
//				System.out.println("------------------------------------------------------------------");
				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * This method is used when connecting an Oracle database. It tunes the connection to the database.
	 *
	 * @throws ArchivingException
	 */
	private void alterSession(Connection conn) throws ArchivingException
	{
		if(conn == null) return;
		Statement stmt = null;
		String sqlStr1 , sqlStr2 , sqlStr3;
		sqlStr1 = "alter session set NLS_NUMERIC_CHARACTERS = \". \"";
		sqlStr2 = "alter session set NLS_TIMESTAMP_FORMAT = 'DD-MM-YYYY HH24:MI:SS.FF'";
		sqlStr3 = "alter session set NLS_DATE_FORMAT = 'DD-MM-YYYY HH24:MI:SS'";
		try
		{
//			conn = getConnection();
			stmt = conn.createStatement();
            lastStatement = stmt;
			stmt.executeQuery(sqlStr1);
			stmt.executeQuery(sqlStr2);
			stmt.executeQuery(sqlStr3);
		}
		catch ( SQLException e )
		{
			String message  = dbUtils.getCommunicationFailureMessage(e); 

			String reason = GlobalConst.STATEMENT_FAILURE;
			String desc = "Failed while executing OracleConnection.alterSession() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				
				ConnectionCommands.close(stmt);
//				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	
	
	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.DBConnection#loadDriver()
	 */
	@Override
	protected void loadDriver() {
		// TODO Auto-generated method stub
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  //pilote Oracle

	}


	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.DBConnection#setDataBaseUrl(java.lang.String)
	 */
	@Override
	protected void setDataBaseUrl(String url) {
		// TODO Auto-generated method stub
		url = ConfigConst.DRIVER_ORACLE + ":@" + getHost() + ":" + ConfigConst.ORACLE_PORT + ":" + getName(); //pilote Oracle
        System.out.println("DBConnection/buildUrlAndConnect/BD_ORACLE/url|"+url);

	}

}
