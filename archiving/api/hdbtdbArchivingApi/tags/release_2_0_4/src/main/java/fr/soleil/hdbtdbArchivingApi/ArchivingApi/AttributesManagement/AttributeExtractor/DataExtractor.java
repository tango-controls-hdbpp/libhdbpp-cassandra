package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;

public class DataExtractor {
	protected IDBConnection dbConn;
	protected IAdtAptAttributes att;
	protected GenericExtractorMethods methods;
	public DataExtractor(IDBConnection con, IAdtAptAttributes at) {
		// TODO Auto-generated constructor stub
		this.dbConn = con;
		this.att = at;
		methods = new GenericExtractorMethods(att, dbConn.getDbUtils());
	}

}
