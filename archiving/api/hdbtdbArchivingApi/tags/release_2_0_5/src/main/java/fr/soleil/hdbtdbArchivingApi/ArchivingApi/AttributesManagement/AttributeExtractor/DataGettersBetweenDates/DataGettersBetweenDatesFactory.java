package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;

public class DataGettersBetweenDatesFactory {

	public DataGettersBetweenDatesFactory() {
		// TODO Auto-generated constructor stub
	}
	public static DataGettersBetweenDates getDataGatters(IDBConnection con, IAdtAptAttributes at,int dbType){
		switch(dbType){
		case ConfigConst.BD_MYSQL: return new  MySqlDataGettersBetweenDates(con, at);
		case ConfigConst.BD_ORACLE:return new OracleDataGettersBetweenDates(con,  at);
		default: return null;
		}
	}

}
