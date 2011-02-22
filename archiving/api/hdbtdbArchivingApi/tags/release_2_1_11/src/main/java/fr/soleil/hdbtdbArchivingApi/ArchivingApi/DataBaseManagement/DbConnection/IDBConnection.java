/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public interface IDBConnection {

	public void connect(boolean rac) throws ArchivingException;

	public Connection getConnection();

	// public Connection getConnectionObject();
	public String getInfo();

	public void close();

	public void closeConnection(Connection conn);

	public int getDbType();

	public void setDbType(int type);

	public String getName();

	public void setName(String name);

	public String getSchema();

	public void setSchema(String schema);

	public String getHost();

	public void setHost(String host);

	public String getDriver();

	public String getUser();

	public void setUser(String user);

	public String getPasswd();

	public void setPasswd(String pwd);

	public boolean getAutoCommit();

	public Statement getLastStatement();

	public void setLastStatement(Statement lastStatement);

	// public Connection getConn();
	public boolean isAutoConnect();

	public void setAutoConnect(boolean autoConnect);

	public Hashtable getIdsBuffer();

}
