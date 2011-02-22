package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TDBMySqlPoolConnection implements IPoolConnection {
	private  static DataSource dataSource; 

	public TDBMySqlPoolConnection() {
		// TODO Auto-generated constructor stub
	}

	public DataSource getPoolInstance(String url, String user, String pwd) throws ArchivingException {
		// TODO Auto-generated method stub
		if(dataSource == null ){
			Connection conn = null;
			try{
				conn = DriverManager.getConnection(url , user , pwd);
				ObjectPool connectionPool = new GenericObjectPool(null);
				ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url , user , pwd);
				PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
				dataSource = new PoolingDataSource(connectionPool);
				if(conn != null)
					conn.close();
			}
			catch(SQLException e) {
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String mysqlMessage = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "CONNECTION FAILED !";
					String mysqlReason = "Failed while executing ConnectionFactory.connect_auto() method...";
					String mysqlDesc = "Failed while connecting to the MySQL archiving database";
					throw new ArchivingException(mysqlMessage , mysqlReason , ErrSeverity.PANIC , mysqlDesc , "" );

			}

		}

		return dataSource;
	}

}
