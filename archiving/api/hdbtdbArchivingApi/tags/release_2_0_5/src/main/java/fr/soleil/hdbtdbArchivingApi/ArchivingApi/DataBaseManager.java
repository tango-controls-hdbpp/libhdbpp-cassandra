package fr.soleil.hdbtdbArchivingApi.ArchivingApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.AttributeExtractor;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.AttributeExtractorFactroy;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.IGenericMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.ModeFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class DataBaseManager {
	private IDBConnection dbConn;
	private IAdtAptAttributes attribute;
	private AttributeExtractor extractor;
	private IGenericMode mode;
	private int archivingType;

	public DataBaseManager(int db_type) {
		// TODO Auto-generated constructor stub
		archivingType = getArchivingType(db_type);
	}
	public DataBaseManager() {
		// TODO Auto-generated constructor stub
	}

	public IDBConnection getDbConn() throws ArchivingException {
		if(dbConn == null) 
			dbConn = ConnectionFactory.getInstance(archivingType);
		return dbConn;
	}


	public IDbUtils getDbUtil() throws ArchivingException {
		return getDbConn().getDbUtils();
	}

	public IAdtAptAttributes getAttribute() throws ArchivingException {
		if(attribute == null) attribute = AdtAptAttributesFactory.getInstance(getDbConn());
		return attribute;
	}

	public AttributeExtractor getExtractor() throws ArchivingException {
		if(extractor == null) extractor = AttributeExtractorFactroy.getAttributeExtractor(getDbConn(), getAttribute());
		return extractor;
	}

	public IGenericMode getMode() throws ArchivingException {
		if(mode == null) mode = ModeFactory.getInstance( getDbConn());
		return mode;
	}

	public void setDbConn(IDBConnection dbConn) {
		this.dbConn = dbConn;
		archivingType = dbConn.getDbType();

	}

	protected int getArchivingType(int db_type) {
		return archivingType;
	}



}
