/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public interface ITdbDataExport {
	public  void  exportToDB_Data(String remoteDir , String fileName , String tableName, int writable) throws ArchivingException;
	public void ExportData2Tdb(String attributeName, String endDate) throws ArchivingException;
	public void forceDatabaseToImportFile(String tableName) throws ArchivingException;
}
