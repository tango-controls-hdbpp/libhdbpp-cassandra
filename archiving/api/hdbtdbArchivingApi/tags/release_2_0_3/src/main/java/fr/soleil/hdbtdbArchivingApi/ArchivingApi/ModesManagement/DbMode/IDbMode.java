/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public interface IDbMode {
	
	public void updateModeRecord(IDBConnection dbConn, String attribute_name, IAdtAptAttributes adtAptAttributes) throws ArchivingException;
}
