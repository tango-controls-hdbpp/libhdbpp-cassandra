/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import java.sql.SQLException;
import java.sql.Timestamp;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

/**
 * @author AYADI
 *
 */
public interface IDbUtils {
	public String getTableName(String att_name, IAdtAptAttributes att) throws ArchivingException;
	public String getFormat(SamplingType samplingType) ;
	public  String toDbTimeString(String timeField);
	public  String toDbTimeFieldString(String timeField);
	public String toDbTimeFieldString(String timeField,String format);
	public String getTime(String string) ;
	public String getTableName(int index);
	public Object[] getSpectrumValue(String readString, String writeString, int data_type);
	public Object[][] getImageValue(String dbValue, int data_type);
	public Timestamp getTimeOfLastInsert(String completeName, boolean max, IDBConnection dbConn, IAdtAptAttributes att) throws ArchivingException;
	public String[] getTimeValueNullOrNotOfLastInsert(String completeName, int dataFormat, int writable, String partition, IDBConnection dbConn, IAdtAptAttributes att) throws ArchivingException;
	public boolean isLastDataNull(String att_name, IDBConnection dbConn, IAdtAptAttributes att ) throws ArchivingException;
	public  String getCommunicationFailureMessage(SQLException e);
}
