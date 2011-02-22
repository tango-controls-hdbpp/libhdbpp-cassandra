package fr.soleil.hdbtdbArchivingApi.ArchivingApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert.HdbAttributeInsertFactroy;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert.IHdbAttributeInsert;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class HDBDataBaseManager extends DataBaseManager {
	private IHdbAttributeInsert attributeInsert;

	public HDBDataBaseManager(int db_type) {
		super(db_type);
		// TODO Auto-generated constructor stub
	}
	/*
	 * 
	 */
	public IHdbAttributeInsert getHdbAttributeInsert() throws ArchivingException {
		if(attributeInsert == null) attributeInsert = HdbAttributeInsertFactroy.getInstance(archivingType, getAttribute());
		return attributeInsert;
	}
	/*
	 * 
	 */
	public HDBDataBaseManager() {
		// TODO Auto-generated constructor stub
	}
	/*
	 * (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager#getArchivingType(int)
	 */
	public int getArchivingType(int db_type) {
		if(db_type == ConfigConst.BD_MYSQL)
			return ConfigConst.HDB_MYSQL;
			else return ConfigConst.HDB_MYSQL;
		
	}

}
