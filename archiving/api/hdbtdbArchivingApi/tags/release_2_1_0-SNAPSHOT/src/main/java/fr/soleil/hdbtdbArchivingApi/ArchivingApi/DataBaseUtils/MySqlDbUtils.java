/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import java.sql.SQLException;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

/**
 * @author AYADI
 *
 */
public class MySqlDbUtils extends DbUtils {

	/**
	 * 
	 */
	public MySqlDbUtils(int type) {
		// TODO Auto-generated constructor stub
		super(type);
	}
	@Override
	public String getFormat(SamplingType samplingType) {
		return samplingType.getMySqlFormat();
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#getRequest()
	 */
	@Override
	protected String getRequest() {
		// TODO Auto-generated method stub
		return "SELECT SYSDATE()";
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#toDbTimeFieldString(java.lang.String)
	 */
	@Override
	public String toDbTimeFieldString(String timeField) {
		// TODO Auto-generated method stub
		return " `" + timeField + "`" ;
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#toDbTimeFieldString(java.lang.String, java.lang.String)
	 */
	@Override
	public String toDbTimeFieldString(String timeField, String format) {
		// TODO Auto-generated method stub
		return "DATE_FORMAT(" + timeField + " ,'" + format + "')";
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#toDbTimeString(java.lang.String)
	 */
	@Override
	public String toDbTimeString(String timeField) {
		// TODO Auto-generated method stub
		return " \"" + timeField + "\"";
	}

	@Override
	public String getTableName(int index) {
		// TODO Auto-generated method stub
		String tableName = ConfigConst.TAB_PREF;
			if ( index < 10 )
			{
				tableName = tableName + "0000" + index;
			}
			else if ( index < 100 )
			{
				tableName = tableName + "000" + index;
			}
			else if ( index < 1000 )
			{
				tableName = tableName + "00" + index;
			}
			else
			{ // if (index < 10000) {
				tableName = tableName + "0" + index;
			}

		return tableName;
	}
	@Override
	public String getCommunicationFailureMessage(SQLException e) {
		// TODO Auto-generated method stub
		String message = "";
		if (e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
			message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
		else
			message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
		return message;

	}

	@Override
	protected String getField(String maxOrMin) {
		// TODO Auto-generated method stub
		return maxOrMin+"("+ConfigConst.TAB_SCALAR_RO[ 0 ]+")";  
	}

	@Override
	public String getTime(String string) {
		// TODO Auto-generated method stub
		return string;
	}
	@Override
	protected String getQueryOfGetTimeValueNullOrNotOfLastInsert(String tableName, String partition) {
		// TODO Auto-generated method stub
	    String field = ConfigConst.TAB_SCALAR_RW[0];            
	        	
        String query = "select " + field + " from " + tableName;

		return query;
	}
	public String[] getListOfPartitions()
			throws ArchivingException {
		// TODO Auto-generated method stub
		return null;
	}

}
