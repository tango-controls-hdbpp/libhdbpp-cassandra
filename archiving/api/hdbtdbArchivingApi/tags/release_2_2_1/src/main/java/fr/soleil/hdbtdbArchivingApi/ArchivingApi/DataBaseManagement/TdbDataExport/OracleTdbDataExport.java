/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.ucp.ConnectionHarvestingCallback;
import oracle.ucp.jdbc.HarvestableConnection;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class OracleTdbDataExport extends TdbDataExport implements
		ConnectionHarvestingCallback {
	public OracleTdbDataExport(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	private Connection connectionHarvestExportData = null;

	@Override
	/*
	 * * Called by the connections pool
	 */
	public synchronized boolean cleanup() {

		if (connectionHarvestExportData != null) {

			try {
				this.logger.trace(ILogger.LEVEL_INFO, "Closing connection ...");
				connectionHarvestExportData.close();
				this.logger.trace(ILogger.LEVEL_INFO,
						"Connection closed successfully.");
			} catch (SQLException e) {
				this.logger.trace(ILogger.LEVEL_ERROR,
						"HARVEST :Error occured while closing connection. " + e);
				return false;
			}

			// Reset connection
			connectionHarvestExportData = null;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbDataExport
	 * .IDataExport#exportToDB_Image(java.lang.String, java.lang.String,
	 * java.lang.String, int)
	 */
	public synchronized void exportToDB_Data(String remoteDir, String fileName,
			String tableName, int writable) throws ArchivingException {

		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;

		CallableStatement callableStatement = null;
		StringBuffer query = new StringBuffer().append("{call ").append(
				dbConn.getSchema()).append(".crcontrolfile(?, ?, ?)}");

		try {
			if (connectionHarvestExportData == null
					|| connectionHarvestExportData.isClosed()) {
				// connection = null; // in case of isClosed
				connectionHarvestExportData = dbConn.getConnection();

				// Callback pour fermer la connexion lorsque le
				// pool va la recuper (harvest)
				((HarvestableConnection) connectionHarvestExportData)
						.registerConnectionHarvestingCallback(this);
				this.logger.trace(ILogger.LEVEL_INFO,
						"Get New connection for filename = " + fileName);
			}
			// Travail avec la connexion en empechant le pool de la
			// recuperer (harvest)
			((HarvestableConnection) connectionHarvestExportData)
					.setConnectionHarvestable(false);

			callableStatement = connectionHarvestExportData.prepareCall(query
					.toString());
			dbConn.setLastStatement(callableStatement);
			callableStatement.setString(1, remoteDir);
			callableStatement.setString(2, fileName);
			callableStatement.setString(3, tableName);

			/*System.out
					.println("OracleTdbDataExport/exportToDB_Data BEFORE PS crcontrolfile: "
							+ new Timestamp(System.currentTimeMillis())
								.toString());*/
			callableStatement.executeUpdate();
			/*System.out
					.println("OracleTdbDataExport/exportToDB_Data AFTER PS crcontrolfile: "
							+ new Timestamp(System.currentTimeMillis())
									.toString());*/
		} catch (SQLException e) {
			e.printStackTrace();
			this.logger.trace(ILogger.LEVEL_ERROR, "HARVEST : Export problem " + e);
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
			ConnectionCommands.close(callableStatement);
			callableStatement = null;
			if (connectionHarvestExportData != null) {
				try {
					((HarvestableConnection) connectionHarvestExportData)
							.setConnectionHarvestable(true);
				} catch (SQLException e) {
					this.logger.trace(ILogger.LEVEL_ERROR,
							"HARVEST :setConnectionHarvestable true impossible");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				this.logger
						.trace(ILogger.LEVEL_ERROR,
								"HARVEST :Becareful : Cnx cannot be free because it is already null");
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
		Connection conn = null;
		try {
			conn = dbConn.getConnection();
			callableStatement = conn.prepareCall(query.toString());
			dbConn.setLastStatement(callableStatement);
			callableStatement.setString(1, tableName);

			/*System.out
					.println("CLA/OracleTdbDataExport/forceDatabaseToImportFile BEFORE PS force_load_data: "
							+ new Timestamp(System.currentTimeMillis())
										.toString());*/
			callableStatement.executeUpdate();
			ConnectionCommands.close(callableStatement);
			dbConn.closeConnection(conn);
			/*System.out
					.println("CLA/OracleTdbDataExport/forceDatabaseToImportFile AFTER PS force_load_data: "
							+ new Timestamp(System.currentTimeMillis())
									.toString());*/
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
			ConnectionCommands.close(callableStatement);
			callableStatement = null;
			dbConn.closeConnection(conn);
			conn = null;
		}
	}

}
