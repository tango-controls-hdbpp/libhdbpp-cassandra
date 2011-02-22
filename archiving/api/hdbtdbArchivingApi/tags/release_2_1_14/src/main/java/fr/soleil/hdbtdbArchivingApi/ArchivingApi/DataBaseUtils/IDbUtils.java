/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import java.sql.SQLException;
import java.sql.Timestamp;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

/**
 * @author AYADI
 * 
 */
public interface IDbUtils {
	public String getTableName(String att_name) throws ArchivingException;

	public String getFormat(SamplingType samplingType);

	public String toDbTimeString(String timeField);

	public String toDbTimeFieldString(String timeField);

	public String toDbTimeFieldString(String timeField, String format);

	public String getTime(String string);

	public String getTableName(int index);

	public Object[] getSpectrumValue(String readString, String writeString,
			int data_type);

	public Object[][] getImageValue(String dbValue, int data_type);

	public Timestamp getTimeOfLastInsert(String completeName, boolean max)
			throws ArchivingException;

	public String[] getTimeValueNullOrNotOfLastInsert(String completeName,
			int dataFormat, int writable, String partition)
			throws ArchivingException;

	public void startWatcherReport() throws ArchivingException;

	public int getAttributesCountOkOrKo(boolean isOKStatus)
			throws ArchivingException;

	public String[] getKoAttributes() throws ArchivingException;

	public String[] getListOfPartitions() throws ArchivingException;

	public String[] getListOfJobStatus() throws ArchivingException;

	public String[] getListOfJobErrors() throws ArchivingException;

	public String[] getKOAttrCountByDevice() throws ArchivingException;

	public int getFeedAliveProgression() throws ArchivingException;

	public boolean isLastDataNull(String att_name) throws ArchivingException;

	public String getCommunicationFailureMessage(SQLException e);

}
