/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

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
	public static ITdbDataExport getInstance(int arch_type){
		
		switch(arch_type){
		case ConfigConst.TDB_MYSQL: 
			return  new MySqlTdbDataExport(arch_type);
		
		case ConfigConst.TDB_ORACLE: 
			return  new OracleTdbDataExport(arch_type);
		
		}
		return null;
	}
}
