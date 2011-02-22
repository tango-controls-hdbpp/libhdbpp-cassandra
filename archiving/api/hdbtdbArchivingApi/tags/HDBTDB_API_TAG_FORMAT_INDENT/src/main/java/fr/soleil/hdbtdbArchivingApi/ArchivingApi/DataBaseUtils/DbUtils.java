package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.Vector;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

public abstract class DbUtils implements IDbUtils {
	protected int arch_type;

	public DbUtils(int type) {
		this.arch_type = type;
	}

	abstract public String getFormat(SamplingType samplingType);

	abstract public String toDbTimeString(String timeField);

	abstract public String toDbTimeFieldString(String timeField);

	abstract public String toDbTimeFieldString(String timeField, String format);

	abstract public String getTime(String string);

	abstract public String getTableName(int index);

	abstract protected String getRequest();

	abstract protected String getField(String maxOrMin);

	abstract protected String getQueryOfGetTimeValueNullOrNotOfLastInsert(
			String tableName, String partition);

	public abstract String getCommunicationFailureMessage(SQLException e);

	/*
	 *
	 */
	public Timestamp getTimeOfLastInsert(String completeName, boolean max)
			throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Timestamp ret = null;

		String maxOrMin = max ? "MAX" : "MIN";

		String field = getField(maxOrMin);

		// System.out.println (
		// "CLA/DataBaseApi/getTimeOfLastInsert/field|"+field+"|" );
		String tableName;
		try {
			tableName = dbConn.getSchema() + "." + getTableName(completeName);
		} catch (ArchivingException e) {
			return null;
		}
		String query = "select " + field + " from " + tableName;
		System.out.println("CLA/DataBaseApi/getTimeOfLastInsert/query|" + query
				+ "|");

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query);
			rset.next();
			String rawDate = rset.getString(1);
			if (rawDate == null) {
				return null;
			}

			long stringToMilli = DateUtil.stringToMilli(rawDate);
			ret = new Timestamp(stringToMilli);
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
			String desc = "Failed while executing DataBaseApi.getTimeOfLastInsert() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				e.printStackTrace();

				String message = "";
				if (e.getMessage().equalsIgnoreCase(
						GlobalConst.COMM_FAILURE_ORACLE)
						|| e.getMessage().indexOf(
								GlobalConst.COMM_FAILURE_MYSQL) != -1)
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.QUERY_FAILURE;
				String desc = "Failed while executing DataBaseApi.getTimeOfLastInsert() method...";
				throw new ArchivingException(message, reason, ErrSeverity.WARN,
						desc, this.getClass().getName(), e);
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param manager
	 * @return
	 * @throws ArchivingException
	 * @throws SQLException
	 */
	public void deleteOldRecords(long keepedPeriod, String[] attributeList)
			throws ArchivingException, SQLException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if (dbConn == null || att == null)
			return;
		long current_date = System.currentTimeMillis();
		long keeped_date = current_date - keepedPeriod;
		long time = keeped_date;
		// long time = current_date;
		Timestamp timeSt = new Timestamp(time);

		Connection conn = null;
		PreparedStatement ps_delete = null;
		try {
			conn = dbConn.getConnection();

			for (int i = 0; i < attributeList.length; i++) {

				try {
					String name = attributeList[i];
					String tableName = dbConn.getSchema() + "."
							+ getTableName(name);
					String tableField = ConfigConst.TAB_SCALAR_RO[0];

					String deleteString = "DELETE FROM  " + tableName
							+ " WHERE " + tableField + " <= ?";
					String truncateString = "TRUNCATE TABLE  " + tableName;
					boolean everythingIsOld = false;

					Timestamp lastInsert = getTimeOfLastInsert(name, true);

					if (lastInsert != null) {
						everythingIsOld = lastInsert.getTime()
								- timeSt.getTime() < 0;
					}
					System.out.println("DataBaseApi/deleteOldRecords/name|"
							+ name + "|lastInsert|" + lastInsert
							+ "|threshold|" + timeSt + "|everythingIsOld|"
							+ everythingIsOld);

					String query = everythingIsOld ? truncateString
							: deleteString;
					System.out.println("DataBaseApi/deleteOldRecords/query|"
							+ query);

					ps_delete = conn.prepareStatement(query);
					dbConn.setLastStatement(ps_delete);

					if (!everythingIsOld) {
						ps_delete.setTimestamp(1, timeSt);
					}

					ps_delete.executeUpdate();
					ConnectionCommands.close(ps_delete);
				} catch (SQLException e) {
					ConnectionCommands.close(ps_delete);
					e.printStackTrace();
					System.out
							.println("SQLException received (go to the next element) : "
									+ e);
					continue;
				} catch (ArchivingException e) {
					ConnectionCommands.close(ps_delete);
					e.printStackTrace();
					System.out
							.println("ArchivingException received (go to the next element) : "
									+ e);
					continue;
				} catch (Exception e) {
					ConnectionCommands.close(ps_delete);
					e.printStackTrace();
					System.out
							.println("Unknown Exception received (go to the next element) : "
									+ e);
					continue;
				}
			}
		} finally {
			ConnectionCommands.close(ps_delete);
			dbConn.closeConnection(conn);
		}
	}

	/*
	 *
	 */
	public Timestamp now() throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		String sqlStr = getRequest();
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			// System.out.println ( "CLA/DataBaseApi/now/query|"+sqlStr+"|" );

			rset = stmt.executeQuery(sqlStr);
			rset.next();
			// Gets the result of the query

			Timestamp date = rset.getTimestamp(1);
			// System.out.println("date = " + date);

			return date;
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
			String desc = "Failed while executing DbUtils.now() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				e.printStackTrace();

				String message = "";
				if (e.getMessage().equalsIgnoreCase(
						GlobalConst.COMM_FAILURE_ORACLE)
						|| e.getMessage().indexOf(
								GlobalConst.COMM_FAILURE_MYSQL) != -1)
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.QUERY_FAILURE;
				String desc = "Failed while executing DbUtils.now() method...";
				throw new ArchivingException(message, reason, ErrSeverity.WARN,
						desc, this.getClass().getName(), e);
			}
			// Close the connection with the database
			// if ( dbConn.isAutoConnect() )
			// {
			// dbConn.close();
			// }
		}

	}

	// --------------------------------- Archiving Watcher Report
	// ---------------------------------
	/**
	 * Return a Watcher Report: 1/archived attributes number 2/number of KO and
	 * OK attributes 3/List of KO attribute's name 4/List Of partitions for
	 * Oracle database 5/List of job's Status 6/List of job's errors
	 */
	// --------------------------------- Archiving Watcher Report
	// ---------------------------------
	/**
	 * Start watcher report in real time
	 */
	public void startWatcherReport() throws ArchivingException {

		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		Connection conn = null;
		CallableStatement stmt = null;
		String startAdminReportQuery = "CALL " + dbConn.getSchema()
				+ ".FEEDALIVE()";
		try {
			conn = dbConn.getConnection();
			stmt = conn.prepareCall(startAdminReportQuery);
			dbConn.setLastStatement(stmt);
			stmt.execute();

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
			String desc = "Failed while executing DbUtils.startWatcherReport() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * return % of procedure call progression
	 */
	public int getFeedAliveProgression() throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		String table = dbConn.getSchema() + "." + ConfigConst.TABS[3];
		String getProgressionLevelQuery = "SELECT TRUNC ( COUNT("
				+ ConfigConst.TAB_ISALIVED[2] + ")/" + "(SELECT COUNT(*) FROM "
				+ ConfigConst.TABS[2] + " WHERE " + ConfigConst.TAB_MOD[3]
				+ " IS NULL)" + ") FROM " + table;
		// System.out.println("SPJZ ============> getFeedAliveProgression = " +
		// getProgressionLevelQuery);
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getProgressionLevelQuery);
			// Gets the result of the query
			if (rset.next())
				return rset.getInt(1);
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
			String desc = "Failed while executing DbUtils.getFeedAliveProgression() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return 0;

	}

	/**
	 * return the number of KO and OK attributes
	 */
	public int getAttributesCountOkOrKo(boolean isOKStatus)
			throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		int attributesCount = 0;
		String status = isOKStatus ? "\'OK\'" : "\'KO\'";
		String select_field = "COUNT(*)";
		String table = dbConn.getSchema() + "." + ConfigConst.TABS[3];
		String clause_1 = ConfigConst.TABS[3] + "."
				+ ConfigConst.TAB_ISALIVED[0] + " = " + status;
		String getAttributesCountDataQuery = "SELECT DISTINCT(" + select_field
				+ ")" + " FROM " + table + " WHERE " + "(" + clause_1 + ")";
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getAttributesCountDataQuery);
			// Gets the result of the query
			if (rset.next())
				attributesCount = rset.getInt(1);
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
			String desc = "Failed while executing DbUtils.getAttributesCountOkOrKo() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return attributesCount;

	}

	public String[] getKOAttrCountByDevice() throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		String tableName = dbConn.getSchema() + "." + ConfigConst.TABS[7];
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		String getKOAttributesByDeviceQuery = "SELECT * FROM " + tableName;
		Vector<String> res = new Vector<String>();
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getKOAttributesByDeviceQuery);
			// Gets the result of the query
			res.add("\n---------- Ko attributes count by device: ");
			res.add("Archiver  NBR");
			while (rset.next()) {
				String elt = rset.getString(1) + "  "
						+ String.valueOf(rset.getInt(2));
				res.add(elt);
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
			String desc = "Failed while executing DbUtils.getKOAttrCountByDevice() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Returns the number of active simple signals defined in HDB
		return res.size() > 2 ? toStringArray(res) : null;

	}

	/**
	 * return the number of KO and OK attributes
	 */
	public String[] getKoAttributes() throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Vector<String> koAttributes = new Vector<String>();
		String select_field = "*";
		String table = dbConn.getSchema() + "." + ConfigConst.TABS[3];
		String clause_1 = ConfigConst.TABS[3] + "."
				+ ConfigConst.TAB_ISALIVED[0] + " = \'KO\'";
		String orderbyClause = ConfigConst.TABS[3] + "."
				+ ConfigConst.TAB_ISALIVED[4];
		String getKOAttributesQuery = "SELECT " + select_field + " FROM "
				+ table + " WHERE " + "(" + clause_1 + ")" + " ORDER BY "
				+ orderbyClause;
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getKOAttributesQuery);
			// Gets the result of the query
			koAttributes.add("\n---------- List of Ko attributes: ");
			koAttributes.add("Status  Full_Name  ID  Archiver  maxTime");
			while (rset.next()) {
				String elt = rset.getString(1) + "  " + rset.getString(2)
						+ "  " + String.valueOf(rset.getInt(3)) + "  "
						+ rset.getString(4) + "  ";
				elt += rset.getDate(5) == null ? "null" : rset.getDate(5);
				koAttributes.add(elt);
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
			String desc = "Failed while executing DbUtils.getKoAttributes() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Returns the number of active simple signals defined in HDB
		return koAttributes.size() > 2 ? toStringArray(koAttributes) : null;

	}

	/**
	 * return the list of job's status
	 */
	public String[] getListOfJobStatus() throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Vector<String> jobStatusVector = new Vector<String>();

		String select_field = ConfigConst.TAB_LOG_JOB[2] + ", "
				+ ConfigConst.TAB_LOG_JOB[3] + " ,"
				+ ConfigConst.TAB_LOG_JOB[7] + ", " + "to_char("
				+ ConfigConst.TAB_LOG_JOB[1] + ",\'YYYY-MM-DD HH24:MI:SS\')"
				+ " time";

		String table = ConfigConst.TABS[5];

		String clause_1 = ConfigConst.TAB_LOG_JOB[1] + " IN (SELECT MAX("
				+ ConfigConst.TAB_LOG_JOB[1] + ") FROM " + table + " GROUP BY "
				+ ConfigConst.TAB_LOG_JOB[3] + ")";
		String clause_2 = ConfigConst.TAB_LOG_JOB[2] + " IN (\'SYSTEM\', \'"
				+ dbConn.getSchema().toUpperCase()
				+ "\', \'ADMINISTRATOR\') ORDER BY 2,1";
		String getListOfJobStatusQuery = "SELECT " + select_field + " FROM "
				+ table + " WHERE " + clause_1 + " AND " + clause_2;

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getListOfJobStatusQuery);

			// Gets the result of the query
			jobStatusVector.add("\n---------- List of job status: ");
			jobStatusVector.add("OWNER  JOB_NAME  STATUS  LOG_DATE");
			while (rset.next()) {
				String elt = rset.getString(1) + "  " + rset.getString(2)
						+ "  " + rset.getString(3) + "  ";
				elt += rset.getTimestamp(4) == null ? "null" : rset
						.getTimestamp(4).toString();
				jobStatusVector.add(elt);
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
			String desc = "Failed while executing DbUtils.getListOfJobStatus() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Returns the number of active simple signals defined in HDB
		return jobStatusVector.size() > 2 ? toStringArray(jobStatusVector)
				: null;

	}

	/**
	 * return the list of job's status
	 */
	public String[] getListOfJobErrors() throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Vector<String> jobErrorsVector = new Vector<String>();

		String select_field = "to_char(" + ConfigConst.TAB_RUN_DETAILS[1]
				+ ",\'YYYY-MM-DD HH24:MI:SS\')" + " time " + ", "
				+ ConfigConst.TAB_RUN_DETAILS[3] + ", "
				+ ConfigConst.TAB_RUN_DETAILS[5] + " ,"
				+ ConfigConst.TAB_RUN_DETAILS[14];

		String table = ConfigConst.TABS[6];

		String clause_1 = ConfigConst.TAB_RUN_DETAILS[14] + " IS NOT NULL ";
		String clause_2 = ConfigConst.TAB_RUN_DETAILS[1] + " > SYSDATE - 5"
				+ "ORDER BY 1";

		String getJobErrorsQuery = "SELECT " + select_field + " FROM " + table
				+ " WHERE " + clause_1 + " AND " + clause_2;

		try {
			conn = dbConn.getConnection();
			getJobErrorsQuery = conn.nativeSQL(getJobErrorsQuery);
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getJobErrorsQuery);
			// Gets the result of the query
			jobErrorsVector.add("\n---------- List of job Errors : ");
			jobErrorsVector.add("LOG_DATE  JOB_NAME  STATUS  ADDITIONAL_INFO");
			while (rset.next()) {
				jobErrorsVector.add(rset.getTimestamp(1).toString() + "  "
						+ rset.getString(2) + "  " + rset.getString(3) + "  "
						+ rset.getString(4));
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
			String desc = "Failed while executing DbUtils.getListOfJobErrors() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (stmt != null)
					ConnectionCommands.close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Returns the number of active simple signals defined in HDB
		return jobErrorsVector.size() > 2 ? toStringArray(jobErrorsVector)
				: null;

	}

	// ---------------------------------------------------------------------------------------

	/**
	 * <b>Description : </b> Build a array of Double with the given Double
	 * Vector
	 * 
	 * @param my_vector
	 *            The given Double Vector
	 * @return a Double type array that contains the differents vector's Double
	 *         type elements <br>
	 */
	public static double[] toDoubleArray(Vector<Double> my_vector) {
		double[] my_array;
		my_array = new double[my_vector.size()];
		for (int i = 0; i < my_vector.size(); i++) {
			my_array[i] = ((Double) my_vector.elementAt(i)).doubleValue();
		}
		return my_array;
	}

	/**
	 * <b>Description : </b> Build a array of Double with the two given Double
	 * Vectors
	 * 
	 * @param my_vector1
	 *            The first given Double Vector
	 * @param my_vector2
	 *            The second given Double Vector
	 * @return a Double type array that contains the first and the second Double
	 *         elements <br>
	 */
	public static double[] toDoubleArray(Vector<Double> my_vector1,
			Vector<Double> my_vector2) {
		for (int i = 0; i < my_vector2.size(); i++) {
			my_vector1.addElement((Double) my_vector2.elementAt(i));
		}
		return toDoubleArray(my_vector1);
	}

	/**
	 * <b>Description : </b> Build a array of String with the given String
	 * Vector
	 * 
	 * @param my_vector
	 *            The given String Vector
	 * @return a String type array that contains the differents vector's String
	 *         type elements <br>
	 */
	public static String[] toStringArray(Vector<String> my_vector) {
		String[] my_array;
		my_array = new String[my_vector.size()];
		for (int i = 0; i < my_vector.size(); i++) {
			my_array[i] = (String) my_vector.elementAt(i);
		}
		return my_array;
	}

	/**
	 * <b>Description : </b> Build a array of String with the two String Double
	 * Vectors
	 * 
	 * @param my_vector1
	 *            The first given String Vector
	 * @param my_vector2
	 *            The second given String Vector
	 * @return a String type array that contains the first and the second String
	 *         elements <br>
	 */
	public static String[] toStringArray(Vector<String> my_vector1,
			Vector<String> my_vector2) {
		for (int i = 0; i < my_vector2.size(); i++) {
			my_vector1.addElement((String) my_vector2.elementAt(i));
		}
		return toStringArray(my_vector1);
	}

	/**
	 * This method returns a String which contains the value columnn name
	 * 
	 * @param completeName
	 *            : attribute full_name
	 * @param dataFormat
	 *            : AttrDataFormat._SCALAR or AttrDataFormat._SPECTRUM ot
	 *            AttrDataFormat._IMAGE
	 * @param writable
	 *            AttrWriteType._READ_WRITE or AttrWriteType._READ or
	 *            AttrWriteType._WRITE
	 * @return corresponding column name
	 */
	public static String getValueColumnName(int dataFormat, int writable) {

		if (dataFormat == AttrDataFormat._SCALAR) {
			if (writable == AttrWriteType._READ_WRITE)
				return ConfigConst.TAB_SCALAR_RW[1];
			else if (writable == AttrWriteType._READ)
				return ConfigConst.TAB_SCALAR_RO[1];
			else
				return ConfigConst.TAB_SCALAR_WO[1];
		} else if (dataFormat == AttrDataFormat._SPECTRUM) {
			if (writable == AttrWriteType._READ_WRITE)
				return ConfigConst.TAB_SPECTRUM_RW[2];
			else
				return ConfigConst.TAB_SPECTRUM_RO[2];
		} else {
			if (writable == AttrWriteType._READ_WRITE)
				return ConfigConst.TAB_IMAGE_RW[3];
			else
				return ConfigConst.TAB_IMAGE_RO[3];
		}
	}

	/**
	 * 
	 * @param attDirPath
	 * @param prefix
	 * @param nb_file_max
	 * @return
	 */
	public int getRelativePathIndex(String attDirPath, String prefix,
			int nb_file_max) {
		File attDirectory = new File(attDirPath);
		// Le repertoire est il vide ?
		if (!attDirectory.exists()) {
			// Si 'oui', je le cree et je renvoi 1
			attDirectory.mkdirs();
			return 1;
		} else {
			// Si 'non' je choisi le reperoire courant (d'index maximum) : je
			// compte pour celui le nombre de sous repertoire
			int current_index_dir = attDirectory.listFiles().length;
			String currentPath = attDirPath + File.separator + prefix
					+ current_index_dir;
			File attCurrentDirectory = new File(currentPath);
			// Si le nombre de fichier du repertoire courant est inferieur au
			// nombre max... je retourne le nombre courant
			if (attCurrentDirectory.listFiles().length < nb_file_max) {
				return current_index_dir;
			} else {
				return current_index_dir + 1;
			}
		}
	}

	/**
	 * This method returns the name of the table associated (table in wich will
	 * host its archived values) to the given attribute
	 * 
	 * @param att_name
	 *            the attribute's name (cf. ADT in HDB).
	 * @return the name of the table associated (table in wich will host its
	 *         archived values) to the given attribute.
	 */
	public String getTableName(String att_name) throws ArchivingException {
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		String table_name = "";
		// table_name = getTableName(getAttID(att_name));
		int id = att.getIds().getBufferedAttID(att_name);
		if (id <= 0) {
			throw new ArchivingException("Invalid attribute: " + att_name);
		}
		table_name = getTableName(id);
		return table_name;
	}

	/*
	 *
	 */

	public Object[] getSpectrumValue(String readString, String writeString,
			int data_type) {
		int size = 0;
		StringTokenizer readTokenizer;
		if (readString == null || "".equals(readString)
				|| "null".equals(readString)) {
			readTokenizer = null;
		} else {
			readTokenizer = new StringTokenizer(readString,
					GlobalConst.CLOB_SEPARATOR);
			size += readTokenizer.countTokens();
		}

		StringTokenizer writeTokenizer;
		if (writeString == null || "".equals(writeString)
				|| "null".equals(writeString)) {
			writeTokenizer = null;
		} else {
			writeTokenizer = new StringTokenizer(writeString,
					GlobalConst.CLOB_SEPARATOR);
			size += writeTokenizer.countTokens();
		}

		Double[] dvalueArr = null;
		Byte[] cvalueArr = null;
		Integer[] lvalueArr = null;
		Short[] svalueArr = null;
		Boolean[] bvalueArr = null;
		Float[] fvalueArr = null;
		String[] stvalueArr = null;
		switch (data_type) {
		case TangoConst.Tango_DEV_BOOLEAN:
			bvalueArr = new Boolean[size];
			break;
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			cvalueArr = new Byte[size];
			break;
		case TangoConst.Tango_DEV_STATE:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
			lvalueArr = new Integer[size];
			break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			svalueArr = new Short[size];
			break;
		case TangoConst.Tango_DEV_FLOAT:
			fvalueArr = new Float[size];
			break;
		case TangoConst.Tango_DEV_STRING:
			stvalueArr = new String[size];
			break;
		case TangoConst.Tango_DEV_DOUBLE:
		default:
			dvalueArr = new Double[size];
		}
		int i = 0;

		if (readTokenizer != null) {
			while (readTokenizer.hasMoreTokens()) {
				String currentValRead = readTokenizer.nextToken();
				if (currentValRead == null || currentValRead.trim().equals("")) {
					break;
				}
				switch (data_type) {
				case TangoConst.Tango_DEV_BOOLEAN:
					try {
						if (currentValRead == null || "".equals(currentValRead)
								|| "null".equals(currentValRead)
								|| "NaN".equalsIgnoreCase(currentValRead)) {
							bvalueArr[i] = null;
						} else {
							bvalueArr[i] = new Boolean(Double.valueOf(
									currentValRead).intValue() != 0);
						}
					} catch (NumberFormatException n) {
						bvalueArr[i] = new Boolean("true"
								.equalsIgnoreCase(currentValRead.trim()));
					}
					break;
				case TangoConst.Tango_DEV_CHAR:
				case TangoConst.Tango_DEV_UCHAR:
					try {
						if (currentValRead == null || "".equals(currentValRead)
								|| "null".equals(currentValRead)
								|| "NaN".equalsIgnoreCase(currentValRead)) {
							cvalueArr[i] = null;
						} else {
							cvalueArr[i] = Byte.valueOf(currentValRead);
						}
					} catch (NumberFormatException n) {
						cvalueArr[i] = new Byte(Double.valueOf(currentValRead)
								.byteValue());
					}
					break;
				case TangoConst.Tango_DEV_STATE:
				case TangoConst.Tango_DEV_LONG:
				case TangoConst.Tango_DEV_ULONG:
					try {
						if (currentValRead == null || "".equals(currentValRead)
								|| "null".equals(currentValRead)
								|| "NaN".equalsIgnoreCase(currentValRead)) {
							lvalueArr[i] = null;
						} else {
							lvalueArr[i] = Integer.valueOf(currentValRead);
						}
					} catch (NumberFormatException n) {
						lvalueArr[i] = new Integer(Double.valueOf(
								currentValRead).intValue());
					}
					break;
				case TangoConst.Tango_DEV_SHORT:
				case TangoConst.Tango_DEV_USHORT:
					try {
						if (currentValRead == null || "".equals(currentValRead)
								|| "null".equals(currentValRead)
								|| "NaN".equalsIgnoreCase(currentValRead)) {
							svalueArr[i] = null;
						} else {
							svalueArr[i] = Short.valueOf(currentValRead);
						}
					} catch (NumberFormatException n) {
						svalueArr[i] = new Short(Double.valueOf(currentValRead)
								.shortValue());
					}
					break;
				case TangoConst.Tango_DEV_FLOAT:
					if (currentValRead == null || "".equals(currentValRead)
							|| "null".equals(currentValRead)
							|| "NaN".equalsIgnoreCase(currentValRead)) {
						fvalueArr[i] = null;
					} else {
						fvalueArr[i] = Float.valueOf(currentValRead);
					}
					break;
				case TangoConst.Tango_DEV_STRING:
					if (currentValRead == null || "".equals(currentValRead)
							|| "null".equals(currentValRead)
							|| "NaN".equalsIgnoreCase(currentValRead)) {
						stvalueArr[i] = null;
					} else {
						stvalueArr[i] = StringFormater
								.formatStringToRead(new String(currentValRead));
					}
					break;
				case TangoConst.Tango_DEV_DOUBLE:
				default:
					if (currentValRead == null || "".equals(currentValRead)
							|| "null".equals(currentValRead)
							|| "NaN".equalsIgnoreCase(currentValRead)) {
						dvalueArr[i] = null;
					} else {
						dvalueArr[i] = Double.valueOf(currentValRead);
					}
				}
				i++;
			}
		}

		if (writeTokenizer != null) {
			while (writeTokenizer.hasMoreTokens()) {
				String currentValWrite = writeTokenizer.nextToken();
				if (currentValWrite == null
						|| currentValWrite.trim().equals("")) {
					break;
				}
				switch (data_type) {
				case TangoConst.Tango_DEV_BOOLEAN:
					try {
						if (currentValWrite == null
								|| "".equals(currentValWrite)
								|| "null".equals(currentValWrite)
								|| "NaN".equalsIgnoreCase(currentValWrite)) {
							bvalueArr[i] = null;
						} else {
							bvalueArr[i] = new Boolean(Double.valueOf(
									currentValWrite).intValue() != 0);
						}
					} catch (NumberFormatException n) {
						bvalueArr[i] = new Boolean("true"
								.equalsIgnoreCase(currentValWrite.trim()));
					}
					break;
				case TangoConst.Tango_DEV_CHAR:
				case TangoConst.Tango_DEV_UCHAR:
					try {
						if (currentValWrite == null
								|| "".equals(currentValWrite)
								|| "null".equals(currentValWrite)
								|| "NaN".equalsIgnoreCase(currentValWrite)) {
							cvalueArr[i] = null;
						} else {
							cvalueArr[i] = Byte.valueOf(currentValWrite);
						}
					} catch (NumberFormatException n) {
						cvalueArr[i] = new Byte(Double.valueOf(currentValWrite)
								.byteValue());
					}
					break;
				case TangoConst.Tango_DEV_STATE:
				case TangoConst.Tango_DEV_LONG:
				case TangoConst.Tango_DEV_ULONG:
					try {
						if (currentValWrite == null
								|| "".equals(currentValWrite)
								|| "null".equals(currentValWrite)
								|| "NaN".equalsIgnoreCase(currentValWrite)) {
							lvalueArr[i] = null;
						} else {
							lvalueArr[i] = Integer.valueOf(currentValWrite);
						}
					} catch (NumberFormatException n) {
						lvalueArr[i] = new Integer(Double.valueOf(
								currentValWrite).intValue());
					}
					break;
				case TangoConst.Tango_DEV_SHORT:
				case TangoConst.Tango_DEV_USHORT:
					try {
						if (currentValWrite == null
								|| "".equals(currentValWrite)
								|| "null".equals(currentValWrite)
								|| "NaN".equalsIgnoreCase(currentValWrite)) {
							svalueArr[i] = null;
						} else {
							svalueArr[i] = Short.valueOf(currentValWrite);
						}
					} catch (NumberFormatException n) {
						svalueArr[i] = new Short(Double
								.valueOf(currentValWrite).shortValue());
					}
					break;
				case TangoConst.Tango_DEV_FLOAT:
					if (currentValWrite == null || "".equals(currentValWrite)
							|| "null".equals(currentValWrite)
							|| "NaN".equalsIgnoreCase(currentValWrite)) {
						fvalueArr[i] = null;
					} else {
						fvalueArr[i] = Float.valueOf(currentValWrite);
					}
					break;
				case TangoConst.Tango_DEV_STRING:
					if (currentValWrite == null || "".equals(currentValWrite)
							|| "null".equals(currentValWrite)
							|| "NaN".equalsIgnoreCase(currentValWrite)) {
						stvalueArr[i] = null;
					} else {
						stvalueArr[i] = StringFormater
								.formatStringToRead(new String(currentValWrite));
					}
					break;
				case TangoConst.Tango_DEV_DOUBLE:
				default:
					if (currentValWrite == null || "".equals(currentValWrite)
							|| "null".equals(currentValWrite)
							|| "NaN".equalsIgnoreCase(currentValWrite)) {
						dvalueArr[i] = null;
					} else {
						dvalueArr[i] = Double.valueOf(currentValWrite);
					}
				}
				i++;
			}
		}

		if (readTokenizer == null && writeTokenizer == null) {
			return null;
		}

		switch (data_type) {
		case TangoConst.Tango_DEV_BOOLEAN:
			return bvalueArr;
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			return cvalueArr;
		case TangoConst.Tango_DEV_STATE:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
			return lvalueArr;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			return svalueArr;
		case TangoConst.Tango_DEV_FLOAT:
			return fvalueArr;
		case TangoConst.Tango_DEV_STRING:
			return stvalueArr;
		case TangoConst.Tango_DEV_DOUBLE:
		default:
			return dvalueArr;
		}
	}

	/**
	 * 
	 * @param dbValue
	 * @param data_type
	 * @return
	 */
	public Object[][] getImageValue(String dbValue, int data_type) {
		if (dbValue == null || "".equals(dbValue) || "null".equals(dbValue)) {
			return null;
		}
		Object[] valArray = null;
		String value = new String(dbValue);
		value = value.replaceAll("\\[", "");
		value = value.replaceAll("\\]", "");

		StringTokenizer readTokenizer = null;
		int rowSize = 0, colSize = 0;

		readTokenizer = new StringTokenizer(value,
				GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS);
		rowSize = readTokenizer.countTokens();

		if (readTokenizer != null) {
			valArray = new Object[rowSize];
			int i = 0;
			while (readTokenizer.hasMoreTokens()) {
				valArray[i++] = readTokenizer.nextToken().trim().split(
						GlobalConst.CLOB_SEPARATOR_IMAGE_COLS);
			}
			if (rowSize > 0) {
				colSize = ((String[]) valArray[0]).length;
			}
		} else
			return null;

		// System.out.println("rowSize,Colsize:"+rowSize+","+colSize);
		Double[][] dvalueArr = null;
		Byte[][] cvalueArr = null;
		Integer[][] lvalueArr = null;
		Short[][] svalueArr = null;
		Boolean[][] bvalueArr = null;
		Float[][] fvalueArr = null;
		String[][] stvalueArr = null;
		switch (data_type) {
		case TangoConst.Tango_DEV_BOOLEAN:
			bvalueArr = new Boolean[rowSize][colSize];
			for (int i = 0; i < rowSize; i++) {
				for (int j = 0; j < colSize; j++) {
					try {
						if (((String[]) valArray[i])[j] == null
								|| "".equals(((String[]) valArray[i])[j])
								|| "null".equals(((String[]) valArray[i])[j])
								|| "NaN"
										.equalsIgnoreCase(((String[]) valArray[i])[j])) {
							bvalueArr[i][j] = null;
						} else {
							bvalueArr[i][j] = new Boolean(Double.valueOf(
									((String[]) valArray[i])[j].trim())
									.intValue() != 0);
						}
					} catch (NumberFormatException n) {
						bvalueArr[i][j] = new Boolean("true"
								.equalsIgnoreCase(((String[]) valArray[i])[j]
										.trim()));
					}
				}
			}
			return bvalueArr;
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			cvalueArr = new Byte[rowSize][colSize];
			for (int i = 0; i < valArray.length; i++) {
				for (int j = 0; j < colSize; j++) {
					try {
						if (((String[]) valArray[i])[j] == null
								|| "".equals(((String[]) valArray[i])[j])
								|| "null".equals(((String[]) valArray[i])[j])
								|| "NaN"
										.equalsIgnoreCase(((String[]) valArray[i])[j])) {
							cvalueArr[i][j] = null;
						} else {
							cvalueArr[i][j] = Byte
									.valueOf(((String[]) valArray[i])[j].trim());
						}
					} catch (NumberFormatException n) {
						cvalueArr[i][j] = new Byte(Double.valueOf(
								((String[]) valArray[i])[j].trim()).byteValue());
					}
				}
			}
			return cvalueArr;
		case TangoConst.Tango_DEV_STATE:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
			lvalueArr = new Integer[rowSize][colSize];
			for (int i = 0; i < valArray.length; i++) {
				for (int j = 0; j < colSize; j++) {
					try {
						if (((String[]) valArray[i])[j] == null
								|| "".equals(((String[]) valArray[i])[j])
								|| "null".equals(((String[]) valArray[i])[j])
								|| "NaN"
										.equalsIgnoreCase(((String[]) valArray[i])[j])) {
							lvalueArr[i][j] = null;
						} else {
							lvalueArr[i][j] = Integer
									.valueOf(((String[]) valArray[i])[j].trim());
						}
					} catch (NumberFormatException n) {
						lvalueArr[i][j] = new Integer(Double.valueOf(
								((String[]) valArray[i])[j].trim()).intValue());
					}
				}
			}
			return lvalueArr;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			svalueArr = new Short[rowSize][colSize];
			for (int i = 0; i < valArray.length; i++) {
				for (int j = 0; j < colSize; j++) {
					try {
						if (((String[]) valArray[i])[j] == null
								|| "".equals(((String[]) valArray[i])[j])
								|| "null".equals(((String[]) valArray[i])[j])
								|| "NaN"
										.equalsIgnoreCase(((String[]) valArray[i])[j])) {
							svalueArr[i][j] = null;
						} else {
							svalueArr[i][j] = Short
									.valueOf(((String[]) valArray[i])[j].trim());
						}
					} catch (NumberFormatException n) {
						svalueArr[i][j] = new Short(Double.valueOf(
								((String[]) valArray[i])[j].trim())
								.shortValue());
					}
				}
			}
			return svalueArr;
		case TangoConst.Tango_DEV_FLOAT:
			fvalueArr = new Float[rowSize][colSize];
			for (int i = 0; i < valArray.length; i++) {
				for (int j = 0; j < colSize; j++) {
					try {
						if (((String[]) valArray[i])[j] == null
								|| "".equals(((String[]) valArray[i])[j])
								|| "null".equals(((String[]) valArray[i])[j])
								|| "NaN"
										.equalsIgnoreCase(((String[]) valArray[i])[j])) {
							fvalueArr[i][j] = null;
						} else {
							fvalueArr[i][j] = Float
									.valueOf(((String[]) valArray[i])[j].trim());
						}
					} catch (NumberFormatException n) {
						fvalueArr[i][j] = new Float(Double.valueOf(
								((String[]) valArray[i])[j].trim())
								.floatValue());
					}
				}
			}
			return fvalueArr;
		case TangoConst.Tango_DEV_STRING:
			stvalueArr = new String[rowSize][colSize];
			for (int i = 0; i < valArray.length; i++) {
				for (int j = 0; j < colSize; j++) {
					if (((String[]) valArray[i])[j] == null
							|| "".equals(((String[]) valArray[i])[j])
							|| "null".equals(((String[]) valArray[i])[j])
							|| "NaN"
									.equalsIgnoreCase(((String[]) valArray[i])[j])) {
						stvalueArr[i][j] = null;
					} else {
						stvalueArr[i][j] = StringFormater
								.formatStringToRead(new String(
										((String[]) valArray[i])[j].trim()));
					}
				}
			}
			return stvalueArr;
		case TangoConst.Tango_DEV_DOUBLE:
		default:
			dvalueArr = new Double[rowSize][colSize];
			for (int i = 0; i < valArray.length; i++) {
				for (int j = 0; j < colSize; j++) {
					if (((String[]) valArray[i])[j] == null
							|| "".equals(((String[]) valArray[i])[j])
							|| "null".equals(((String[]) valArray[i])[j])
							|| "NaN"
									.equalsIgnoreCase(((String[]) valArray[i])[j])) {
						dvalueArr[i][j] = null;
					} else {
						dvalueArr[i][j] = Double
								.valueOf(((String[]) valArray[i])[j].trim());
					}
				}
			}
			return dvalueArr;
		}

	}

	/*
	 *
	 */
	public boolean isLastDataNull(String att_name) throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if (dbConn == null || att == null)
			return false;
		int[] tfw = att.getAtt_TFW_Data(att_name);
		int format = tfw[1];
		int writable = tfw[2];
		boolean ro_fields = (writable == AttrWriteType._READ || writable == AttrWriteType._WRITE);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// int data_type = tfw [0];

		// Create and execute the SQL query string
		// Build the query string
		String tableName = dbConn.getSchema() + "." + getTableName(att_name);
		String fields = ro_fields ? (toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[0])
				+ ", " + ConfigConst.TAB_SCALAR_RO[1])
				: (toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[0]) + ", "
						+ ConfigConst.TAB_SCALAR_RW[1] + ", " + ConfigConst.TAB_SCALAR_RW[2]);

		// String orderField = ( ro_fields ? ConfigConst.TAB_SCALAR_RO[ 0 ] :
		// ConfigConst.TAB_SCALAR_RW[ 0 ] );

		String query = "SELECT " + fields + " FROM " + tableName /* + " LIMIT 1" */;

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(query);
			rset.next();
			boolean ret;

			if (ro_fields) {
				if (format == AttrDataFormat._SCALAR) {
					ret = (rset.getString(2) == null || rset.getString(2)
							.equals(""));
					// System.out.println (
					// "CLA/hasDataLast_n/completeName|"+att_name+"|rset.getString(2)|"+rset.getString(2)+"|ret|"+ret
					// );
				} else {
					ret = (rset.getClob(2) == null || rset.getClob(2)
							.equals(""));
					// System.out.println (
					// "CLA/hasDataLast_n/completeName|"+att_name+"|rset.getClob(2)|"+rset.getClob(2)+"|ret|"+ret
					// );
				}
			} else {
				if (format == AttrDataFormat._SCALAR) {
					ret = (rset.getString(2) == null || rset.getString(2)
							.equals(""));
					ret = ret
							&& (rset.getString(3) == null || rset.getString(3)
									.equals(""));
					// System.out.println (
					// "CLA/hasDataLast_n/completeName|"+att_name+"|rset.getString(2)|"+rset.getString(2)+"|rset.getString(3)|"+rset.getString(3)+"|ret|"+ret
					// );
				} else {
					ret = (rset.getClob(2) == null || rset.getClob(2)
							.equals(""));
					ret = ret
							&& (rset.getClob(3) == null || rset.getClob(3)
									.equals(""));
					// System.out.println (
					// "CLA/hasDataLast_n/completeName|"+att_name+"|rset.getClob(2)|"+rset.getClob(2)+"|rset.getClob(3)|"+rset.getClob(3)+"|ret|"+ret
					// );
				}
			}

			return ret;
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
			String desc = "Failed while executing DbUtils.hasDataLast_n() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				e.printStackTrace();

				String message = "";
				if (e.getMessage().equalsIgnoreCase(
						GlobalConst.COMM_FAILURE_ORACLE)
						|| e.getMessage().indexOf(
								GlobalConst.COMM_FAILURE_MYSQL) != -1)
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.QUERY_FAILURE;
				String desc = "Failed while executing DbUtils.hasDataLast_n() method...";
				throw new ArchivingException(message, reason, ErrSeverity.WARN,
						desc, this.getClass().getName(), e);
			}
		}
	}

	/**
	 * This method returns a String array which contains as first element the
	 * time value and the second element indicates if the corresponding value is
	 * null or not
	 * 
	 * @param completeName
	 *            : attribute full_name
	 * @param dataFormat
	 *            : AttrDataFormat._SCALAR or AttrDataFormat._SPECTRUM ot
	 *            AttrDataFormat._IMAGE
	 * @param writable
	 *            AttrWriteType._READ_WRITE or AttrWriteType._READ or
	 *            AttrWriteType._WRITE
	 * @return couple of string with the maximum time value and the value equal
	 *         to null or notnull
	 * @throws ArchivingException
	 */
	public String[] getTimeValueNullOrNotOfLastInsert(String completeName,
			int dataFormat, int writable, String partition)
			throws ArchivingException {

		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if (dbConn == null || att == null)
			return null;

		String tableName;
		try {
			tableName = dbConn.getSchema() + "." + getTableName(completeName);
		} catch (ArchivingException e) {
			return null;
		}
		String query = getQueryOfGetTimeValueNullOrNotOfLastInsert(tableName,
				partition);

		System.out
				.println("SPJZ/DataBaseApi/getTimeValueNullOrNotOfLastInsert/query|"
						+ query + "|\n full_name = " + completeName);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		String ret[] = null;

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query);

			if (rset.next()) // Otherwise it indicates that the ResultSet is
								// empty
			{
				String rawDate = rset.getString(1);
				if (rset.wasNull())
					return null;
				if (rawDate == null || "".equals(rawDate)
						|| "null".equals(rawDate)) {
					return null;
				}

				ret = new String[3];

				ret[0] = rawDate;

				ret[1] = "notnull";

				ret[2] = partition;
			}

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
			String desc = "Failed while executing DataBaseApi.getTimeValueNullOrNotOfLastInsert() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();

				String message = "";
				if (e.getMessage().equalsIgnoreCase(
						GlobalConst.COMM_FAILURE_ORACLE)
						|| e.getMessage().indexOf(
								GlobalConst.COMM_FAILURE_MYSQL) != -1)
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
							+ GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.QUERY_FAILURE;
				String desc = "Failed while executing DataBaseApi.getTimeValueNullOrNotOfLastInsert() method...";
				throw new ArchivingException(message, reason, ErrSeverity.WARN,
						desc, this.getClass().getName(), e);
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static int getHdbTdbType(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case ConfigConst.TDB_MYSQL:
		case ConfigConst.TDB_ORACLE:
			return ConfigConst.TDB;
		case ConfigConst.HDB_MYSQL:
		case ConfigConst.HDB_ORACLE:
			return ConfigConst.HDB;
		default:
			return -1;
		}
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static int getDbType(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case ConfigConst.TDB_MYSQL:
		case ConfigConst.HDB_MYSQL:
			return ConfigConst.BD_MYSQL;
		case ConfigConst.TDB_ORACLE:
		case ConfigConst.HDB_ORACLE:
			return ConfigConst.BD_ORACLE;
		default:
			return -1;
		}
	}

	public static int getArchivingType(int type, int bd) {
		// TODO Auto-generated method stub
		if (type == ConfigConst.HDB)
			return (bd == ConfigConst.BD_MYSQL) ? ConfigConst.HDB_MYSQL
					: ConfigConst.HDB_ORACLE;
		else
			return (bd == ConfigConst.BD_MYSQL) ? ConfigConst.TDB_MYSQL
					: ConfigConst.TDB_ORACLE;
	}

	public static String getPoolName(int arch_type, String user) {
		// TODO Auto-generated method stub
		if (arch_type == ConfigConst.HDB)
			return "HDB_" + user;
		else
			return "TDB_" + user;
	}

}
