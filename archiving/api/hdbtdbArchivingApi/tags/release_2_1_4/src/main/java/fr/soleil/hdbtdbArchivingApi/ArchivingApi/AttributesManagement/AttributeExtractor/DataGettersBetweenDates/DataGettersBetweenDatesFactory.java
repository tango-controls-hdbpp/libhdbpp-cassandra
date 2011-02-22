package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;

public class DataGettersBetweenDatesFactory {

	public DataGettersBetweenDatesFactory() {
		// TODO Auto-generated constructor stub
	}
	public static DataGettersBetweenDates getDataGatters(int arch_type){
		switch(DbUtils.getDbType(arch_type)){
		case ConfigConst.BD_MYSQL: return new  MySqlDataGettersBetweenDates(arch_type);
		case ConfigConst.BD_ORACLE:return new OracleDataGettersBetweenDates(arch_type);
		default: return null;
		}
	}

}
