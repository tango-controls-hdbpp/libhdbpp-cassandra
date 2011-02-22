package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;

public class HdbAttributeInsertFactroy {

	public HdbAttributeInsertFactroy() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * 
	 */
	public static IHdbAttributeInsert getInstance(IDBConnection conn, IAdtAptAttributes attr){
		switch(conn.getDbType()){
		case ConfigConst.HDB_MYSQL: 
			return  new HDBMySqlAttributeInsert(conn, attr);
		
		case ConfigConst.HDB_ORACLE: 
			return  new HDBOracleAttributeInsert(conn, attr);
		
		}
		return null;

	}
}
