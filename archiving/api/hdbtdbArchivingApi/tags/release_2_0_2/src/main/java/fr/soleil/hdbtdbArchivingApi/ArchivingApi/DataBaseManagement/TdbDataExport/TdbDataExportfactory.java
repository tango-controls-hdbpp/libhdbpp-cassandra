/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;

/**
 * @author AYADI
 *
 */
public class TdbDataExportfactory {
	
	/**
	 * 
	 */
	public TdbDataExportfactory() {
		// TODO Auto-generated constructor stub
	}
	/***
	 * 
	 * @param dbType
	 * @return
	 */
	public static ITdbDataExport getInstance(IDBConnection conn){
		
		switch(conn.getDbType()){
		case ConfigConst.TDB_MYSQL: 
			return  new MySqlTdbDataExport(conn);
		
		case ConfigConst.TDB_ORACLE: 
			return  new OracleTdbDataExport(conn);
		
		}
		return null;
	}
}
