package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;

public class DataGettersFactory {

	public DataGettersFactory() {
		// TODO Auto-generated constructor stub
	}

	public static DataGetters getDataGatters(int type) {
		switch (DbUtils.getDbType(type)) {
		case ConfigConst.BD_MYSQL:
			return new MySqlDataGetters(type);
		case ConfigConst.BD_ORACLE:
			return new OracleDataGetters(type);
		default:
			return null;
		}
	}

}
