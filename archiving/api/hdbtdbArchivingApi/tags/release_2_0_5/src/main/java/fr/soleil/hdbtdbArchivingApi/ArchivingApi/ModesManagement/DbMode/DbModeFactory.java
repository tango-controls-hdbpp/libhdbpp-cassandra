/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

/**
 * @author AYADI
 *
 */
public class DbModeFactory {

	/**
	 * 
	 */
	public DbModeFactory() {
		// TODO Auto-generated constructor stub
	}
	public static IDbMode getInstance(int type){
		switch (type) {
		case ConfigConst.BD_MYSQL: return new MySqlMode();
		case ConfigConst.BD_ORACLE: return new OracleMode();	
		default:
			return null;
		}
	}
}
