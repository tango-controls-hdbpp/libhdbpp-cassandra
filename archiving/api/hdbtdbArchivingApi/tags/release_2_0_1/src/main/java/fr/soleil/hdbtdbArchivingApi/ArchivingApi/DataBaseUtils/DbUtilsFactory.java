/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

/**
 * @author AYADI
 *
 */
public class DbUtilsFactory {

	/**
	 * 
	 */
	public DbUtilsFactory() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static IDbUtils getInstance(int db_Type){
		switch(db_Type){
		case ConfigConst.BD_MYSQL:
			return new MySqlDbUtils();
		case ConfigConst.BD_ORACLE:
			return new OracleDbUtils();
		
		default: 
			return null;
		
		}
		

	}
}
