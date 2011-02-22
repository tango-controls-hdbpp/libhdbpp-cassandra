package fr.soleil.hdbtdbArchivingApi.ArchivingApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.AttributeExtractor;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.AttributeExtractorFactroy;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.IGenericMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.ModeFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class DataBaseManager {
//	private IDBConnection dbConn;
	protected int archivingType;

	public DataBaseManager(int db_type) {
		// TODO Auto-generated constructor stub
		archivingType = getArchivingType(db_type);
	}
	public DataBaseManager() {
		// TODO Auto-generated constructor stub
	}

	public IDBConnection getDbConn() throws ArchivingException {
		 
			return ConnectionFactory.getInstance(archivingType);
		
	}


	public IDbUtils getDbUtil() throws ArchivingException {
		return DbUtilsFactory.getInstance(archivingType);
	}

	public IAdtAptAttributes getAttribute() throws ArchivingException {
		return  AdtAptAttributesFactory.getInstance(archivingType);
		
	}

	public AttributeExtractor getExtractor() throws ArchivingException {
		return AttributeExtractorFactroy.getAttributeExtractor(archivingType);
		
	}

	public IGenericMode getMode() throws ArchivingException {
		return  ModeFactory.getInstance(archivingType);
		
	}

	public void setDbConn(IDBConnection dbConn) {
		archivingType = dbConn.getDbType();

	}

	protected int getArchivingType(int db_type) {
		return archivingType;
	}

   public int getType(){
	   return archivingType;
   }

}
