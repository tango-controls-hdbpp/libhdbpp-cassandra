/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode.DbModeFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode.IDbMode;

/**
 * @author AYADI
 *
 */
public class ModeFactory {

	/**
	 * 
	 */
	public ModeFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static IGenericMode getInstance(IDBConnection dbConn){
		IDbMode mode = DbModeFactory.getInstance(DbUtils.getDbType(dbConn.getDbType()));
		switch(DbUtils.getHdbTdbType(dbConn.getDbType())){
		case ConfigConst.HDB: return new HdbMode(mode, dbConn);
		case ConfigConst.TDB: return new TdbMode(mode, dbConn);
		default: return null;
		}
	}

}
