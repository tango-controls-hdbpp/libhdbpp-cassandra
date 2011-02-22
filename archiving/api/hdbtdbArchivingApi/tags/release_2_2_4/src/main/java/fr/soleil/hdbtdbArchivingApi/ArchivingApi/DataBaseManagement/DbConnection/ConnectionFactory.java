/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class ConnectionFactory {
	static IDBConnection hdbConn = null;
	static IDBConnection tdbConn = null;

	// True if connection and disconnection are made once
	// False if the connection are or made for every request,
	// private boolean autoConnect;

	public ConnectionFactory() {
		// TODO Auto-generated constructor stub
	}

	public static IDBConnection getInstance(int type) throws ArchivingException {
		int archiving_type = DbUtils.getHdbTdbType(type);
		if (archiving_type == ConfigConst.HDB) {
			if (hdbConn == null)
				hdbConn = getDbConnection(type);
			return hdbConn;
		} else if (archiving_type == ConfigConst.TDB) {
			if (tdbConn == null)
				tdbConn = getDbConnection(type);
			return tdbConn;
		}
		return null;

	}

	private static IDBConnection getDbConnection(int type) {
		// TODO Auto-generated method stub
		// int dbType = DbUtils.getDbType(type);

		switch (type) {
		case ConfigConst.HDB_MYSQL:
			return new HDBMySqlConnection(type);
		case ConfigConst.TDB_MYSQL:
			return new TDBMySqlConnection(type);
		case ConfigConst.HDB_ORACLE:
			return new HDBOracleConnection(type);
		case ConfigConst.TDB_ORACLE:
			return new TDBOracleConnection(type);
		}
		return null;

	}

	public static IDBConnection connect_auto(String host, String name,
			String schema, String user, String pass, int type,
			boolean racConnection, boolean harvestableCnx, boolean autoCommit)
			throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = getDbConn(type);
		if (dbConn == null) {

			ArchivingException archivingException = new ArchivingException();
			try {
				// try oracle
				dbConn = getInstance(DbUtils.getArchivingType(type,
						ConfigConst.BD_ORACLE), host, name, schema, user, pass);

				// Cnx Type definition
				dbConn.setIsRacConnection(racConnection);
				dbConn.setIsHarvestable(harvestableCnx);
				dbConn.setIsAutoCommit(autoCommit);

				dbConn.connect();
				System.out.println("ConnectionFactory.connect_auto (Oracle): "
						+ user + "@" + name + " has been done.");
			} catch (ArchivingException e) {
				initDbConn(type);
				String message = GlobalConst.ARCHIVING_ERROR_PREFIX
						+ " Database Connection FAILED";
				String reason = "Failed while executing ConnectionFactory.connect_auto() method (For Oracle)...";
				/*
				 * + "\nSystem properties : \nOracle Home : " +
				 * System.getProperty("ORACLE_HOME") + "\nPath : " +
				 * System.getProperty("PATH") + "\nLink Path : " +
				 * System.getProperty("LD_LIBRARY_PATH") + "\nTNS Admin Path : "
				 * + System.getProperty("TNS_ADMIN") ;
				 */

				String desc = "Oracle database Connection FAILED with the following parameters : \n\t\tuser = "
						+ user
						+ " password = "
						+ pass
						+ " rac = "
						+ racConnection
						+ " harvestable = "
						+ harvestableCnx
						+ " host = " + host + " name = " + name;
				archivingException.addStack(message, reason, ErrSeverity.PANIC,
						desc, "", e);
				// System.out.println(desc);

				try {
					dbConn = getInstance(DbUtils.getArchivingType(type,
							ConfigConst.BD_MYSQL), host, name, schema, user,
							pass);
					// try MySQL;
					dbConn.connect();
					System.out
							.println("ConnectionFactory.connect_auto (MySql): "
									+ dbConn.getUser() + "@" + dbConn.getName()
									+ " has been done.");
				} catch (ArchivingException e1) {

					reason = "Failed while executing ConnectionFactory.connect_auto() method (For Mysql) ...";
					desc = "MySql database Connection FAILED with the following parameters : \n\t\tuser = "
							+ user
							+ " password = "
							+ pass
							+ " host = "
							+ host
							+ " name = " + name;

					archivingException.addStack(message, reason,
							ErrSeverity.PANIC, desc, "", e1);
					// System.out.println(desc);
					throw archivingException;
				}
			}
		}
		return dbConn;

	}

	public static IDBConnection getInstance(int type, String host, String name,
			String schema, String user, String pass) throws ArchivingException {
		int archiving_type = DbUtils.getHdbTdbType(type);
		if ((archiving_type == ConfigConst.HDB) && (hdbConn == null)) {
			hdbConn = getDbConnection(type, host, name, schema, user, pass);
			return hdbConn;
		} else if ((archiving_type == ConfigConst.TDB) && (tdbConn == null)) {
			tdbConn = getDbConnection(type, host, name, schema, user, pass);
			return tdbConn;
		}
		return null;
	}

	private static IDBConnection getDbConnection(int type, String host,
			String name, String schema, String user, String pass) {
		// TODO Auto-generated method stub

		// int dbType = DbUtils.getDbType(type);

		switch (type) {
		case ConfigConst.HDB_MYSQL:
			return new HDBMySqlConnection(type, host, name, schema, user, pass);
		case ConfigConst.TDB_MYSQL:
			return new TDBMySqlConnection(type, host, name, schema, user, pass);
		case ConfigConst.HDB_ORACLE:
			return new HDBOracleConnection(type, host, name, schema, user, pass);
		case ConfigConst.TDB_ORACLE:
			return new TDBOracleConnection(type, host, name, schema, user, pass);
		}
		return null;

	}

	private static IDBConnection getDbConn(int type) {

		if (type == ConfigConst.HDB)
			return hdbConn;
		else
			return tdbConn;
	}

	private static void initDbConn(int type) {
		// TODO Auto-generated method stub
		if (type == ConfigConst.HDB)
			hdbConn = null;
		else
			tdbConn = null;

	}

}
