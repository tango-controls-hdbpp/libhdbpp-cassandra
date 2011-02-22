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
	public static IAdtAptAttributes getInstance(int arch_type){
		
		switch(arch_type){
		case ConfigConst.TDB_MYSQL: 
			return  new TDBMySqlAdtAptAttributes(arch_type);
		case ConfigConst.TDB_ORACLE: 
			return  new TDBOracleAdtAptAttributes(arch_type);
		
		case ConfigConst.HDB_MYSQL: 
			return  new MySqlAdtAptAttributes(arch_type);
		case ConfigConst.HDB_ORACLE: 
			return  new OracleAdtAptAttributes(arch_type);
		default: 
			return null;

		}
	}
}
