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
	public static IDbUtils getInstance(int arch_type){
		int db_Type = DbUtils.getDbType(arch_type);
		switch(db_Type){
		case ConfigConst.BD_MYSQL:
			return new MySqlDbUtils(arch_type);
		case ConfigConst.BD_ORACLE:
			return new OracleDbUtils(arch_type);
		
		default: 
			return null;
		
		}
		

	}
}
