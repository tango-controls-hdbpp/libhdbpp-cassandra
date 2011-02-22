package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;

public class DataGettersFactory {

	public DataGettersFactory() {
		// TODO Auto-generated constructor stub
	}
	public static DataGetters getDataGatters(IDBConnection con, IAdtAptAttributes at,int dbType){
		switch(dbType){
		case ConfigConst.BD_MYSQL: return new  MySqlDataGetters(con, at);
		case ConfigConst.BD_ORACLE:return new OracleDataGetters(con, at);
		default: return null;
		}
	}

}
