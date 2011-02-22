/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

/**
 * @author AYADI
 *
 */
public class OracleDbUtils extends DbUtils {

	/**
	 * 
	 */
	public OracleDbUtils() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getFormat(SamplingType samplingType) {
		return samplingType.getOracleFormat();
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#toDbTimeFieldString(java.lang.String)
	 */
	@Override
	public String toDbTimeFieldString(String timeField) {
		// TODO Auto-generated method stub
		return "to_char(" + timeField + " ,'DD-MM-YYYY HH24:MI:SS.FF')" ;
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#toDbTimeFieldString(java.lang.String, java.lang.String)
	 */
	@Override
	public String toDbTimeFieldString(String timeField, String format) {
		// TODO Auto-generated method stub
		return "to_char(" + timeField + " ,'" + format + "')";  
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#toDbTimeString(java.lang.String)
	 */
	@Override
	public String toDbTimeString(String timeField) {
		// TODO Auto-generated method stub
		return "\'" + timeField + "" + "\'" ; 
	}

	@Override
	protected String getRequest() {
		// TODO Auto-generated method stub
		return "SELECT SYSDATE FROM DUAL";

		}

	@Override
	public String getTableName(int index) {
		// TODO Auto-generated method stub
		return ConfigConst.TAB_PREF + index;
	}

	@Override
	protected String getField(String maxOrMin) {
		// TODO Auto-generated method stub
		return toDbTimeFieldString ( maxOrMin+"("+ConfigConst.TAB_SCALAR_RO[ 0 ]+")" ); 
	}

	@Override
	public String getTime(String string) {
		// TODO Auto-generated method stub
		try {
			return DateUtil.stringToDisplayString(string);
		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}

	@Override
	protected String getQueryOfGetTimeValueNullOrNotOfLastInsert(String tableName, IDBConnection dbconn, String partition) {
		// TODO Auto-generated method stub
		String field = toDbTimeFieldString ( "MAX(" + ConfigConst.TAB_SCALAR_RW[0]  + ")");
		String query = "select " + field + " from " + tableName;
	 	  if((partition == null) && (dbconn.getDbType() == ConfigConst.HDB_ORACLE))
			try {
				partition = getPartition(dbconn);
			} catch (ArchivingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	  query += (partition != null)? " PARTITION (" + partition + " )" : "";

		return query;    
	}

	   /**
     * 
     * @param dbconn 
	 * @return String Partition
     * @throws ArchivingException 
     */
    private String getPartition(IDBConnection dbconn) throws ArchivingException {
		// TODO Auto-generated method stub
    	 String res = null;
       	Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
       
        
		String table = "dual";
		String field = "\'Y\'||to_char(sysdate,\'YY\')||\'M\'||to_char(sysdate,\'MM\')";
		String query = "SELECT "  +  field + " FROM " + table;
		System.out.println("getPartition|query :  "+ query);
		
	      try
	        {
	        	conn = dbconn.getConnection();
	            stmt = conn.createStatement();
	            
	            rset = stmt.executeQuery(query);
	            rset.next ();
	            res = rset.getString ( 1 );
	            
	        }
	        catch ( SQLException e )
	        {
	            e.printStackTrace();
	            
	            String message = "";
	            if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
	                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
	            else
	                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

	            String reason = GlobalConst.QUERY_FAILURE;
	            String desc = "Failed while executing DataBaseApi.getPartition() method...";
	            throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
	        }
	        catch ( Exception e )
	        {
	            return null;
	        }
	        finally
	        {
	            try 
	            {
	                ConnectionCommands.close ( rset );
	                ConnectionCommands.close ( stmt );
	                dbconn.closeConnection(conn);
	                
	            } 
	            catch (SQLException e) 
	            {
	                e.printStackTrace();
	                
	                String message = "";
	                if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
	                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
	                else
	                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

	                String reason = GlobalConst.QUERY_FAILURE;
	                String desc = "Failed while executing DataBaseApi.getPartition() method...";
	                throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
	            }
	        
	        }
    	return res;
	}

	
	@Override
	public String getCommunicationFailureMessage(SQLException e) {
		// TODO Auto-generated method stub
		String message = "";
		if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
			message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
		else
			message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

		return message;
	}

}
