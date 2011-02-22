package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;

public class HdbAttributeInsertFactroy {

	public HdbAttributeInsertFactroy() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * 
	 */
	public static IHdbAttributeInsert getInstance(int arch_type,
			IAdtAptAttributes attr) {
		int type = DbUtils.getDbType(arch_type);
		switch (type) {
		case ConfigConst.HDB_MYSQL:
			return new HDBMySqlAttributeInsert(arch_type, attr);

		case ConfigConst.HDB_ORACLE:
			return new HDBOracleAttributeInsert(arch_type, attr);

		}
		return null;

	}
}
