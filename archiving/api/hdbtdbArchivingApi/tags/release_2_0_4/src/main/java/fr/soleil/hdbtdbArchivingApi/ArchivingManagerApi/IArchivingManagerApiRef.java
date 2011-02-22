/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingMessConfig;

/**
 * @author AYADI
 *
 */
public interface IArchivingManagerApiRef {

	//public ArchivingManagerRefactoring getArchivingManagerInstance();
	public String getDbHost() throws ArchivingException;
	public String getDbName() throws ArchivingException;
	public String getDbUser() throws ArchivingException;
	public String getDbPassword() throws ArchivingException;
	public int getDbType() throws ArchivingException;
	boolean getFacility();
	public  int getArchiverListSize();
	
	public String getStatus(String attributeName , boolean historic) throws ArchivingException;
	public void ArchivingStart(ArchivingMessConfig archivingMessConfig) throws ArchivingException;
	public void ArchivingStop(String[] attributeNameList) throws ArchivingException;
	public void ArchivingStopConf(String[] attributeNameList) throws ArchivingException;
	
	public  Mode GetArchivingMode(String attributeName);
	public boolean isArchived(String attributeName) throws ArchivingException;
	public void ArchivingConfigure(String user , String password ) throws ArchivingException;
	public void ArchivingConfigure(String host , String name, String user , String password ) throws ArchivingException;
	public void ArchivingConfigureWithoutArchiverListInit(String host , String name, String user , String password) throws ArchivingException;
	public DataBaseManager getDataBase();
	public boolean is_db_connected();
	public void closeDatabase();
	abstract public  boolean isM_Facility() ;

	abstract public  void setM_Facility(boolean facility) ;

	abstract  public  String[] getM_ExportedArchiverList();

	abstract public  void setM_ExportedArchiverList(String[] exportedArchiverList);

	abstract public  String[] getM_NotExportedArchiverList();

	abstract public  void setM_NotExportedArchiverList(String[] notExportedArchiverList);

}
