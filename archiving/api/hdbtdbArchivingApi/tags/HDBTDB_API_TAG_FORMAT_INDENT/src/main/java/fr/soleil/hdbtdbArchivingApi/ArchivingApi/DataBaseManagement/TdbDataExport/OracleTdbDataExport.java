/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public class OracleTdbDataExport extends TdbDataExport {
	public OracleTdbDataExport(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbDataExport
	 * .IDataExport#exportToDB_Image(java.lang.String, java.lang.String,
	 * java.lang.String, int)
	 */
	public void exportToDB_Data(String remoteDir, String fileName,
			String tableName, int writable) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;

		Connection conn = null;
		CallableStatement callableStatement = null;
		StringBuffer query = new StringBuffer().append("{call ").append(
				dbConn.getSchema()).append(".crcontrolfile(?, ?, ?)}");

		try {
			conn = dbConn.getConnection();
			callableStatement = conn.prepareCall(query.toString());
			dbConn.setLastStatement(callableStatement);
			callableStatement.setString(1, remoteDir);
			callableStatement.setString(2, fileName);
			callableStatement.setString(3, tableName);

			System.out
					.println("OracleTdbDataExport/exportToDB_Data BEFORE PS crcontrolfile: "
							+ new Timestamp(System.currentTimeMillis())
									.toString());
			callableStatement.executeUpdate();
			System.out
					.println("OracleTdbDataExport/exportToDB_Data AFTER PS crcontrolfile: "
							+ new Timestamp(System.currentTimeMillis())
									.toString());
		} catch (SQLException e) {
			e.printStackTrace();

			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.EXPORTING_FILE_EXCEPTION;
			String desc = "Failed while executing OracleTdbDataExport.exportToDBByOracleJob() method..."
					+ "\r\n\t\t Query : "
					+ query
					+ "\r\n\t\t Param 1 (remote directory) : "
					+ remoteDir
					+ "\r\n\t\t Param 2 (file name)        : "
					+ fileName
					+ "\r\n\t\t Param 3 (table name)       : "
					+ tableName
					+ "\r\n\t\t Code d'erreur : "
					+ e.getErrorCode()
					+ "\r\n\t\t Message : "
					+ e.getMessage()
					+ "\r\n\t\t SQL state : "
					+ e.getSQLState()
					+ "\r\n\t\t Stack : ";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {

				ConnectionCommands.close(callableStatement);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param tableName
	 * @throws ArchivingException
	 */
	public void forceDatabaseToImportFile(String tableName)
			throws ArchivingException {
		// System.out.println("DataBaseApi/forceDatabaseToImportFile/START");
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;

		CallableStatement callableStatement = null;
		// StringBuffer query = new
		// StringBuffer().append("{call ").append(getDbSchema()).append(".force_import_from_file(?)}");
		StringBuffer query = new StringBuffer().append("{call ").append(
				dbConn.getSchema()).append(".force_load_data(?)}");
		Connection conn = dbConn.getConnection();
		try {
			callableStatement = conn.prepareCall(query.toString());
			dbConn.setLastStatement(callableStatement);
			callableStatement.setString(1, tableName);

			try {
				System.out
						.println("CLA/OracleTdbDataExport/forceDatabaseToImportFile BEFORE PS force_load_data: "
								+ new Timestamp(System.currentTimeMillis())
										.toString());
				callableStatement.executeUpdate();
				ConnectionCommands.close(callableStatement);
				dbConn.closeConnection(conn);
				System.out
						.println("CLA/OracleTdbDataExport/forceDatabaseToImportFile AFTER PS force_load_data: "
								+ new Timestamp(System.currentTimeMillis())
										.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				ConnectionCommands.close(callableStatement);
				dbConn.closeConnection(conn);
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();

			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.EXPORTING_FILE_EXCEPTION;
			String desc = "Failed while executing OracleTdbDataExport.exportToDBByOracleJob() method..."
					+ "\r\n\t\t Query : "
					+ query
					+ "\r\n\t\t Param 1 (table name)       : "
					+ tableName
					+ "\r\n\t\t Code d'erreur : "
					+ e.getErrorCode()
					+ "\r\n\t\t Message : "
					+ e.getMessage()
					+ "\r\n\t\t SQL state : "
					+ e.getSQLState()
					+ "\r\n\t\t Stack : ";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {

				ConnectionCommands.close(callableStatement);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
