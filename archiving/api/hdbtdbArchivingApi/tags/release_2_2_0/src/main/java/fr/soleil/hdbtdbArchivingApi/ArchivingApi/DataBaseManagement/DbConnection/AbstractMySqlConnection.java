/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public abstract class AbstractMySqlConnection extends DBConnection {

	public AbstractMySqlConnection(int type) {
		super();
		setDbType(type);
		setDriver(ConfigConst.DRIVER_MYSQL);
	}

	public AbstractMySqlConnection(int type, String host, String name,
			String schema, String user, String pass) {
		super(host, name, schema, user, pass);
		setDbType(type);
		setDriver(ConfigConst.DRIVER_MYSQL);

	}

//	/**
//	 * <b>Description : </b> Gets the current database's autocommit <br>
//	 *
//	 * @return True if database is in autocommit mode
//	 */
//	public boolean getAutoCommit() {
//		return ConfigConst.AUTO_COMMIT_MYSQL;
//	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.DBConnection
	// * #loadDriver()
	// */
	// @Override
	// protected void loadDriver() {
	// // TODO Auto-generated method stub
	// try {
	// Class.forName("org.gjt.mm.mysql.Driver");
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseConnection.DBConnection
	// * #setDataBaseUrl(java.lang.String)
	// */
	// @Override
	// protected void setDataBaseUrl(String url) {
	// // TODO Auto-generated method stub
	// url = ConfigConst.DRIVER_MYSQL + "://" + getHost() + "/" + getName(); //
	// D�finition
	// // du
	// // pilote
	// // MySQL
	// System.out
	// .println("DataBaseApi/buildUrlAndConnect/BD_MYSQL/url|" + url);
	//
	// }

	/**
	 * <b>Description : </b> Allows to connect to the <I>HDB</I> database when
	 * of <I>mySQL</I> type. replace connect_mysql
	 *
	 * @throws ArchivingException
	 */
	public void connect() throws ArchivingException {
		// Load the driver
		Connection conn = null;
		String user;
		String passwd;
		try {
			Class.forName("org.gjt.mm.mysql.Driver"); // D�finition du pilote
			// MySQL
			String url = ConfigConst.DRIVER_MYSQL + "://" + getHost() + "/"
					+ getName(); // D�finition du pilote MySQL

			System.out.println("AbstractMySqlConnection/connect/url|" + url);
			// Used to test the Cnx, otherwise the cnx pb will be visible at the
			// first GetConnection method call
			user = getUser();
			passwd = getPasswd();
			conn = DriverManager.getConnection(url, user, passwd);

			setDriver(ConfigConst.DRIVER_MYSQL);
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setURL(url);
			dataSource.setUser(user);
			dataSource.setPassword(passwd);

			// Taille du pool device property a ajouter
			dataSource.setMetadataCacheSize(50);
			setM_dataSource(dataSource);

		} catch (ClassNotFoundException e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.DRIVER_MISSING;
			String reason = "Failed while executing MySqlConnection.connect() method...";
			String desc = "No MySQL driver available..., please check !";
			throw new ArchivingException(message, reason, ErrSeverity.PANIC,
					desc, "", e);
		} catch (Exception e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.ADB_CONNECTION_FAILURE;
			String reason = "Failed while executing MySqlConnection.connect() method...";
			String desc = (e.getMessage()
					.indexOf(GlobalConst.NO_HOST_EXCEPTION) != -1) ? "The 'host' property ("
					+ getHost() + ") might be wrong... please check it..."
					: "The loggin parameters (host, database name,  user, password) seem to be wrong...";
			throw new ArchivingException(message, reason, ErrSeverity.PANIC,
					desc, "", e);

		} finally {
			closeConnection(conn);
			conn = null;
		}
	}

}
