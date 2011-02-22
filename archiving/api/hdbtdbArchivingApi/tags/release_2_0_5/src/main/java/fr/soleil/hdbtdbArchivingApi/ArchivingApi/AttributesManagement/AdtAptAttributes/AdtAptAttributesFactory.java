package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;

public class AdtAptAttributesFactory {


	public AdtAptAttributesFactory(){

	}
	/**
	 * 
	 * @param dbType
	 * @param manager
	 * @return
	 */
	public static IAdtAptAttributes getInstance(IDBConnection dbConn){
		
		switch(dbConn.getDbType()){
		case ConfigConst.TDB_MYSQL: 
			return  new TDBMySqlAdtAptAttributes(dbConn);
		case ConfigConst.TDB_ORACLE: 
			return  new TDBOracleAdtAptAttributes(dbConn);
		
		case ConfigConst.HDB_MYSQL: 
			return  new MySqlAdtAptAttributes(dbConn);
		case ConfigConst.HDB_ORACLE: 
			return  new OracleAdtAptAttributes(dbConn);
		default: 
			return null;

		}
	}
}
