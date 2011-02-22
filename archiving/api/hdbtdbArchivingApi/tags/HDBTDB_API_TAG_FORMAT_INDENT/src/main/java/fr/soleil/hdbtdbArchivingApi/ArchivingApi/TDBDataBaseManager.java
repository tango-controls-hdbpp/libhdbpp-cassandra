package fr.soleil.hdbtdbArchivingApi.ArchivingApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.TdbAttributeDelete.TdbAttributeDelete;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport.ITdbDataExport;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport.TdbDataExportfactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TDBDataBaseManager extends DataBaseManager {
	private ITdbDataExport tdbExport;
	private TdbAttributeDelete tdbAttrDelete;

	public TDBDataBaseManager(int db_type) {
		super(db_type);
		// TODO Auto-generated constructor stub
	}

	public TDBDataBaseManager() {
		// TODO Auto-generated constructor stub
	}

	public ITdbDataExport getTdbExport() throws ArchivingException {
		if (tdbExport == null)
			tdbExport = TdbDataExportfactory.getInstance(archivingType);
		return tdbExport;
	}

	public TdbAttributeDelete getTdbDeleteAttribute() throws ArchivingException {
		if (tdbAttrDelete == null)
			tdbAttrDelete = new TdbAttributeDelete(archivingType);
		return tdbAttrDelete;
	}

	// public int getArchivingType( int db_type) {
	// if(db_type == ConfigConst.BD_MYSQL)
	// return ConfigConst.TDB_MYSQL;
	// else return ConfigConst.TDB_ORACLE;
	// }

}
