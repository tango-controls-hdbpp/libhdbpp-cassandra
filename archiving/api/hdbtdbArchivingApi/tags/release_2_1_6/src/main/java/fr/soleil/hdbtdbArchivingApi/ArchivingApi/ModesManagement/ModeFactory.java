/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;

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
	
	public static IGenericMode getInstance(int arch_type){
		switch(DbUtils.getHdbTdbType(arch_type)){
		case ConfigConst.HDB: return new HdbMode( arch_type);
		case ConfigConst.TDB: return new TdbMode( arch_type);
		default: return null;
		}
	}

}
