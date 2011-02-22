package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

public class PoolConnectionFactory {

	public static IPoolConnection getInstance(int arch_type) {
		switch (arch_type) {
		case ConfigConst.HDB_ORACLE:
			return new HDBOraclePoolConnection();
		case ConfigConst.TDB_ORACLE:
			return new TDBOraclePoolConnection();
		case ConfigConst.HDB_MYSQL:
			return new HDBMySqlPoolConnection();
		case ConfigConst.TDB_MYSQL:
			return new TDBMySqlPoolConnection();
		default:
			return null;
		}

	}
}
