/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
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
	public OracleDbUtils(int type) {
		// TODO Auto-generated constructor stub
		super(type);
	}

	@Override
	public String getFormat(SamplingType samplingType) {
		return samplingType.getOracleFormat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#
	 * toDbTimeFieldString(java.lang.String)
	 */
	@Override
	public String toDbTimeFieldString(String timeField) {
		// TODO Auto-generated method stub
		return "to_char(" + timeField + " ,'DD-MM-YYYY HH24:MI:SS.FF')";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#
	 * toDbTimeFieldString(java.lang.String, java.lang.String)
	 */
	@Override
	public String toDbTimeFieldString(String timeField, String format) {
		// TODO Auto-generated method stub
		return "to_char(" + timeField + " ,'" + format + "')";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils#
	 * toDbTimeString(java.lang.String)
	 */
	@Override
	public String toDbTimeString(String timeField) {
		// TODO Auto-generated method stub
		return "\'" + timeField + "" + "\'";
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
	protected String getFormattedTimeField(String maxOrMin) {
		// TODO Auto-generated method stub
		return toDbTimeFieldString(maxOrMin + "("
				+ ConfigConst.TAB_SCALAR_RO[0] + ")");
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

//	@Override
//	protected String getQueryOfGetTimeValueNullOrNotOfLastInsert(
//			String tableName) {
//		// TODO Auto-generated method stub
//		String field = toDbTimeFieldString("MAX("
//				+ ConfigConst.TAB_SCALAR_RW[0] + ")");
//		String query = "select " + field + " from " + tableName;
////		if ((partition == null) && (this.arch_type == ConfigConst.HDB_ORACLE))
////			try {
////				partition = getPartition();
////			} catch (ArchivingException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		query += (partition != null) ? " PARTITION (" + partition + " )" : "";
//
//		return query;
//	}

	/**
	 *
	 * @param dbconn
	 * @return String Partition
	 * @throws ArchivingException
	 */
	private String getPartition() throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		String res = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		String table = "dual";
		String field = "\'Y\'||to_char(sysdate,\'YY\')||\'M\'||to_char(sysdate,\'MM\')";
		String query = "SELECT " + field + " FROM " + table;
		System.out.println("getPartition|query :  " + query);

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query);
			rset.next();
			res = rset.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();

			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
					|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.QUERY_FAILURE;
			String desc = "Failed while executing DataBaseApi.getPartition() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} catch (Exception e) {
			return null;
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(stmt);
			stmt = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				ConnectionCommands.close(rset);
//				ConnectionCommands.close(stmt);
//				dbConn.closeConnection(conn);
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//
//				String message = "";
//				if (e.getMessage().equalsIgnoreCase(
//						GlobalConst.COMM_FAILURE_ORACLE)
//						|| e.getMessage().indexOf(
//								GlobalConst.COMM_FAILURE_MYSQL) != -1)
//					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
//							+ GlobalConst.ADB_CONNECTION_FAILURE;
//				else
//					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
//							+ GlobalConst.STATEMENT_FAILURE;
//
//				String reason = GlobalConst.QUERY_FAILURE;
//				String desc = "Failed while executing DataBaseApi.getPartition() method...";
//				throw new ArchivingException(message, reason, ErrSeverity.WARN,
//						desc, this.getClass().getName(), e);
//			}

		}
		return res;
	}

	@Override
	public String getCommunicationFailureMessage(SQLException e) {
		// TODO Auto-generated method stub
		String message = "";
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
				|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
			message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.ADB_CONNECTION_FAILURE;
		else
			message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.STATEMENT_FAILURE;

		return message;
	}

	public String[] getListOfPartitions() throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Vector<String> partitions = new Vector<String>();
		String select_field = "*";
		String table = dbConn.getSchema() + "." + ConfigConst.TABS[4];
		String clause_1 = " ROWNUM < 5 ";
		String getPartitionQuery = "SELECT " + select_field + " FROM " + table
				+ " WHERE " + "(" + clause_1 + ")";
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getPartitionQuery);
			// Gets the result of the query
			partitions.add("---------- List Of Partitions : ");
			partitions.add("NBR  Partition_Name");
			while (rset.next()) {
				partitions.add(String.valueOf(rset.getInt(1)) + "  "
						+ rset.getString(2));
			}
		} catch (SQLException e) {
			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
					|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.QUERY_FAILURE;
			String desc = "Failed while executing DbUtils.getListOfPartitions() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(stmt);
			stmt = null;
			dbConn.closeConnection(conn);
			conn = null;
//
//			try {
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (stmt != null)
//					ConnectionCommands.close(stmt);
//				if (conn != null)
//					dbConn.closeConnection(conn);
//			} catch (SQLException e) {
//				dbConn.closeConnection(conn);
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		// Returns the number of active simple signals defined in HDB
		return partitions.size() > 2 ? toStringArray(partitions) : null;

	}

}
