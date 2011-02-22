package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

public class DataGettersFactory {

	public DataGettersFactory() {
		// TODO Auto-generated constructor stub
	}
	public static DataGetters getDataGatters(int dbType){
		switch(dbType){
		case ConfigConst.BD_MYSQL: return new  MySqlDataGetters(dbType);
		case ConfigConst.BD_ORACLE:return new OracleDataGetters(dbType);
		default: return null;
		}
	}

}
