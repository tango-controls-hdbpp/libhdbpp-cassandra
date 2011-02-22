package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

import oracle.jdbc.pool.OracleDataSource;

public class TDBOraclePoolConnection implements IPoolConnection{
	private  static OracleDataSource dataSource;
	public TDBOraclePoolConnection() {
		// TODO Auto-generated constructor stub
	}

	public DataSource getPoolInstance(String url, String user, String pwd) throws ArchivingException
	{
		// TODO Auto-generated method stub
		if(dataSource==null){
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url , user , pwd);
				dataSource = new OracleDataSource();
				dataSource.setURL(url);
				dataSource.setUser(user);
				dataSource.setPassword(pwd);
				// caching parms
				Double a = new Double(125.00114);

				dataSource.setConnectionCachingEnabled(true);
				dataSource.setConnectionCacheName("TDB_"+user);
				Properties cacheProps = new Properties();
				cacheProps.setProperty("MinLimit", "1");
				cacheProps.setProperty("MaxLimit", "8");
				cacheProps.setProperty("InactivityTimeout","7200"); //seconds // A baser sur l'export period
				cacheProps.setProperty("AbandonedConnectionTimeout","900"); //seconds
				cacheProps.setProperty("MaxStatementLimit", "5"); //for one connection
				cacheProps.setProperty("InitialLimit", "1");
				cacheProps.setProperty("ConnectionWaitTimeout", "1");
				cacheProps.setProperty("ValidateConnection", "true");

				dataSource.setConnectionCacheProperties(cacheProps);
				if(conn != null)  conn.close();
			}
			catch (SQLException e) {
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "CONNECTION FAILED !";
					String reason = "Failed while executing TDBOraclePoolConnection.getPoolInstance() method...";
					String desc = "Failed while connecting to the Oracle archiving database";
					throw new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , e);

			}
		}
		return dataSource;
	}


}
