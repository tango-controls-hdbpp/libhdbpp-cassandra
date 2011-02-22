package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.ucp.jdbc.ValidConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class HDBOracleConnection extends AbstractOracleConnection {

	public HDBOracleConnection(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public HDBOracleConnection(int type, String host, String name,
			String schema, String user, String pass) {
		super(type, host, name, schema, user, pass);

	}

	@Override
	protected String getClassDeviceName() {
		// TODO Auto-generated method stub
		return ConfigConst.HDB_CLASS_DEVICE;
	}

	@Override
	protected String getDbName() {
		// TODO Auto-generated method stub
		return "HDB";
	}

	public Connection getConnection() {

		if (poolDS == null) {
			return null;
		}
		boolean retry = false;
		int nbTry = 1;
		Connection conn = null;

		do {
			try {
				conn = poolDS.getConnection();
				try {
					conn.setAutoCommit(true); // Nécessaire car le havestable modifie la nature des connexions
					alterSession(conn);
				} catch (ArchivingException e) {
					e.printStackTrace();
				}
				retry = false;
			} catch (SQLException e) {
				System.err.println("ALARM : GetConnection ERROR");
				e.printStackTrace();

				if (conn != null) {
					ValidConnection vconn = (ValidConnection) conn;
					try {
						if (!vconn.isValid()) {
							System.err.println("SetInvalid a connexion");
							vconn.setInvalid();
							closeConnection(conn);
							conn = null;
							retry = true;
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						nbTry++;
					}
				}
				else {
					retry = true;
					nbTry++;
				}
			}
		} while (retry && nbTry <= 3);

		if (nbTry > 3) {
			return null;
		}

		return conn;
	}
}
