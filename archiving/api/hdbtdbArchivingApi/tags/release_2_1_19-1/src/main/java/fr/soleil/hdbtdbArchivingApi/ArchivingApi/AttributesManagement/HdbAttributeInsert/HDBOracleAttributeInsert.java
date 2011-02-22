/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import oracle.ucp.ConnectionHarvestingCallback;
import oracle.ucp.jdbc.HarvestableConnection;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.IUpdateJobs;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.UpdateJob;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

/**
 * @author AYADI
 *
 */
public class HDBOracleAttributeInsert extends HdbAttributeInsert implements
		ConnectionHarvestingCallback/* , IUpdateJobs */{

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public HDBOracleAttributeInsert(int type, IAdtAptAttributes at) {
		super(type, at);
		// TODO Auto-generated constructor stub
		// System.out
		// .println("SPJZ====> HDBOracleAttributeInsert object creation");
		// UpdateJob.getInstance().addUpdateJob(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.
	 * AttributeExtractor
	 * .HDBExtractor#insert_ImageData_RO_DataBase(java.lang.StringBuffer,
	 * java.lang.StringBuffer, java.lang.StringBuffer, int, int,
	 * java.sql.Timestamp, java.lang.Double[][], java.lang.StringBuffer)
	 */
	@Override
	protected void insert_ImageData_RO_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			int dim_y, Timestamp timeSt, Double[][] dvalue,
			StringBuffer valueStr, String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		Connection conn = null;
		CallableStatement cstmt_ins_spectrum_ro = null;
		query = new StringBuffer().append("{call ").append(dbConn.getSchema())
				.append(".ins_im_1val (?, ?, ?, ?, ?)}");

		// System.out.println ( "CLA/INSERTING IMAGE VALUE OF LENGTH/" +
		// valueStr.toString().length() );
		try {
			conn = dbConn.getConnection();
			cstmt_ins_spectrum_ro = conn.prepareCall(query.toString());
			dbConn.setLastStatement(cstmt_ins_spectrum_ro);
			cstmt_ins_spectrum_ro.setString(1, att_name);
			cstmt_ins_spectrum_ro.setTimestamp(2, timeSt);
			cstmt_ins_spectrum_ro.setInt(3, dim_x);
			cstmt_ins_spectrum_ro.setInt(4, dim_y);
			if (dvalue == null) {
				cstmt_ins_spectrum_ro.setNull(4, java.sql.Types.CLOB);
			} else
				cstmt_ins_spectrum_ro.setString(5, valueStr.toString());

			cstmt_ins_spectrum_ro.executeUpdate();

		} catch (SQLException e) {
			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing HDBOracleAttributeInsert.insert_ImageData_RO() method..."
					+ "\r\n\t\t Query : "
					+ query
					+ "\r\n\t\t Param 1 (Attribute name) : "
					+ att_name
					+ "\r\n\t\t Param 2 (Timestamp)      : "
					+ timeSt.toString()
					+ "\r\n\t\t Param 3 (X Dimension)    : "
					+ dim_x
					+ "\r\n\t\t Param 3 (Y Dimension)    : "
					+ dim_y
					+ "\r\n\t\t Param 4 (Value)          : "
					+ valueStr.toString()
					+ "\r\n\t\t Code d'erreur : "
					+ e.getErrorCode()
					+ "\r\n\t\t Message : "
					+ e.getMessage()
					+ "\r\n\t\t SQL state : "
					+ e.getSQLState()
					+ "\r\n\t\t Stack : ";
			e.printStackTrace();

			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(cstmt_ins_spectrum_ro);
			cstmt_ins_spectrum_ro = null;
			dbConn.closeConnection(conn);
			conn = null;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.
	 * AttributeExtractor
	 * .HDBExtractor#insert_SpectrumData_RO_DataBase(java.lang.StringBuffer,
	 * java.lang.StringBuffer, java.lang.StringBuffer, int, java.sql.Timestamp,
	 * java.lang.Double[], java.lang.StringBuffer, java.lang.String)
	 */
	@Override
	protected void insert_SpectrumData_RO_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalue, StringBuffer valueStr,
			String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		CallableStatement cstmt_ins_spectrum_ro = null;
		query = new StringBuffer().append("{call ").append(dbConn.getSchema())
				.append(".ins_sp_1val (?, ?, ?, ?)}");
		Connection conn = null;
		try {
			conn = dbConn.getConnection();
			cstmt_ins_spectrum_ro = conn.prepareCall(query.toString());
			dbConn.setLastStatement(cstmt_ins_spectrum_ro);
			cstmt_ins_spectrum_ro.setString(1, att_name);
			cstmt_ins_spectrum_ro.setTimestamp(2, timeSt);
			cstmt_ins_spectrum_ro.setInt(3, dim_x);
			if (valueStr == null) {
				cstmt_ins_spectrum_ro.setNull(4, java.sql.Types.CLOB);
			} else {
				cstmt_ins_spectrum_ro.setString(4, valueStr.toString());
			}

			try {
				cstmt_ins_spectrum_ro.executeUpdate();
			} catch (SQLException e) {
				ConnectionCommands.close(cstmt_ins_spectrum_ro);
				throw e;
			}
		} catch (SQLException e) {
			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;
			message += " ( attribut = " + att_name + ")";

			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing HDBOracleAttributeInsert.insert_SpectrumData_RO() method..."
					+ "\r\n\t\t Query : "
					+ query
					+ "\r\n\t\t Param 1 (Attribute name) : "
					+ att_name
					+ "\r\n\t\t Param 2 (Timestamp)      : "
					+ timeSt.toString()
					+ "\r\n\t\t Param 3 (Dimension)      : "
					+ dim_x
					+ "\r\n\t\t Param 4 (Value)          : "
					+ valueStr.toString()
					+ "\r\n\t\t Code d'erreur : "
					+ e.getErrorCode()
					+ "\r\n\t\t Message : "
					+ e.getMessage()
					+ "\r\n\t\t SQL state : "
					+ e.getSQLState()
					+ "\r\n\t\t Stack : ";
			e.printStackTrace();

			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(cstmt_ins_spectrum_ro);
			cstmt_ins_spectrum_ro = null;
			dbConn.closeConnection(conn);
			conn = null;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.
	 * AttributeExtractor
	 * .HDBExtractor#insert_SpectrumData_RW_DataBase(java.lang.StringBuffer,
	 * java.lang.StringBuffer, java.lang.StringBuffer, int, java.sql.Timestamp,
	 * java.lang.Double[], java.lang.Double[], java.lang.StringBuffer,
	 * java.lang.StringBuffer, java.lang.String)
	 */
	@Override
	protected void insert_SpectrumData_RW_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalueWrite, Double[] dvalueRead,
			StringBuffer valueWriteStr, StringBuffer valueReadStr,
			String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		CallableStatement cstmt_ins_spectrum_rw = null;
		Connection conn = null;
		query = new StringBuffer().append("{call ").append(dbConn.getSchema())
				.append(".ins_sp_2val (?, ?, ?, ?, ?)}");

		try {

			conn = dbConn.getConnection();
			cstmt_ins_spectrum_rw = conn.prepareCall(query.toString());
			dbConn.setLastStatement(cstmt_ins_spectrum_rw);
			cstmt_ins_spectrum_rw.setString(1, att_name);
			cstmt_ins_spectrum_rw.setTimestamp(2, timeSt);
			cstmt_ins_spectrum_rw.setInt(3, dim_x);
			if (valueReadStr == null) {
				cstmt_ins_spectrum_rw.setNull(4, java.sql.Types.CLOB);
			} else {
				cstmt_ins_spectrum_rw.setString(4, valueReadStr.toString());
			}
			if (valueWriteStr == null) {
				cstmt_ins_spectrum_rw.setNull(5, java.sql.Types.CLOB);
			} else {
				cstmt_ins_spectrum_rw.setString(5, valueWriteStr.toString());
			}

			try {
				cstmt_ins_spectrum_rw.executeUpdate();
				ConnectionCommands.close(cstmt_ins_spectrum_rw);

			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				throw e;
			}

		} catch (SQLException e) {
			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;
			message += " ( attribut = " + att_name + ")";
			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing HDBOracleAttributeInsert.insert_SpectrumData_RO() method..."
					+ "\r\n\t\t Query : "
					+ query
					+ "\r\n\t\t Param 1 (Attribute name) : "
					+ att_name
					+ "\r\n\t\t Param 2 (Timestamp)      : "
					+ timeSt.toString()
					+ "\r\n\t\t Param 3 (Dimension)      : "
					+ dim_x
					+ "\r\n\t\t Param 4 (Value Read)     : "
					+ valueReadStr.toString()
					+ "\r\n\t\t Param 5 (Value Write)    : "
					+ valueWriteStr.toString()
					+ "\r\n\t\t Code d'erreur : "
					+ e.getErrorCode()
					+ "\r\n\t\t Message : "
					+ e.getMessage()
					+ "\r\n\t\t SQL state : "
					+ e.getSQLState()
					+ "\r\n\t\t Stack : ";
			e.printStackTrace();

			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(cstmt_ins_spectrum_rw);
			cstmt_ins_spectrum_rw = null;
			dbConn.closeConnection(conn);
			conn = null;

		}

	}

	private Connection connection = null;

	private synchronized void createHarvestConnection()
			throws ArchivingException {

		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		try {
			if (connection == null || connection.isClosed()) {
				connection = null; // in case of isClosed
				connection = dbConn.getConnection();
				connection.setAutoCommit(false);

				// Callback pour commiter et fermer la connexion lorsque le
				// pool va la récupérera (harvest)
				((HarvestableConnection) connection)
						.registerConnectionHarvestingCallback(this);
			}
			// Travail avec la connexion en empêchant le pool de la
			// récupérer (harvest)
			((HarvestableConnection) connection)
					.setConnectionHarvestable(false);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void freeHarvestConnection() {
		if (connection != null) {
			try {

				((HarvestableConnection) connection)
						.setConnectionHarvestable(true);
				//connection.setAutoCommit(true); fait sur le getConnection par pr�caution
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			System.err
					.println("Becareful : Cnx cannot be free because it is already null");
	}

	@Override
	/*
	 * * Called by the connections pool
	 */
	public boolean cleanup() {
		System.out.println("Harvesting connection ...");
		Boolean result = false;

		if (connection != null) {

			// synchronized (connection) {

			// Commit connection
			tryToCommit(connection);

			// Close connection
			result = tryToClose(connection);

			// Reset connection
			connection = null;
			// }
		}
		return result;
		// return synchronizedUpdate();
	}

	// private Boolean synchronizedUpdate() {
	// Boolean result = false;
	//
	// if (connection != null) {
	//
	// // synchronized (connection) {
	//
	// // Commit connection
	// tryToCommit(connection);
	//
	// // Close connection
	// result = tryToClose(connection);
	//
	// // Reset connection
	// connection = null;
	// // }
	// }
	// return result;
	// }

	private void tryToCommit(Connection connection) {
		try {
			System.out.println("Commiting connection ...");
			connection.commit();
			System.out.println("Connection commited successfully.");
		} catch (SQLException e) {
			System.err.println("Error occured while commiting connection." + e);
		}
	}

	private Boolean tryToClose(Connection connection) {
		try {
			System.out.println("Closing connection ...");
			connection.close();
			System.out.println("Connection closed successfully.");
		} catch (SQLException e) {
			System.err.println("Error occured while closing connection. " + e);
			return false;
		}
		return true;
	}

	public void insert_ScalarData(ScalarEvent scalarEvent)
			throws ArchivingException {

		PreparedStatement preparedStatement = null;
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		String _attributeName = "";
		try {
			String _schema = dbConn.getSchema();
			StringBuffer _query;
			StringBuffer _tableName;
			StringBuffer _tableFields;
			String _selectFields = "";
			int _writable = scalarEvent.getWritable();

			_attributeName = scalarEvent.getAttribute_complete_name().trim();
			// Create and execute the SQL query string
			// Build the query string
			_selectFields = ((_writable == AttrWriteType._READ || _writable == AttrWriteType._WRITE) ? "?, ?"
					: "?, ?, ?");
			_tableFields = (_writable == AttrWriteType._READ || _writable == AttrWriteType._WRITE) ? new StringBuffer()
					.append(ConfigConst.TAB_SCALAR_RO[0]).append(", ").append(
							ConfigConst.TAB_SCALAR_RO[1])
					: new StringBuffer().append(ConfigConst.TAB_SCALAR_RW[0])
							.append(", ").append(ConfigConst.TAB_SCALAR_RW[1])
							.append(", ").append(ConfigConst.TAB_SCALAR_RW[2]);

			_tableName = new StringBuffer().append(_schema).append(".").append(
					dbUtils.getTableName(_attributeName));

			_query = new StringBuffer();
			_query.append("INSERT INTO ").append(_tableName).append(" (")
					.append(_tableFields).append(")").append(" VALUES").append(
							" (").append(_selectFields).append(")");

			// conn = dbConn.getConnection();
			createHarvestConnection();
			preparedStatement = connection.prepareStatement(_query.toString());
			// dbConn.setLastStatement(preparedStatement);
			Object _value = scalarEvent.getValue();
			int _dataType = scalarEvent.getData_type();
			if (_value != null) {
				if (_writable == AttrWriteType._READ
						|| _writable == AttrWriteType._WRITE) {
					// Fill the value field
					ConnectionCommands.prepareSmtForInsertScalar1ValNotNull(
							preparedStatement, _dataType, _value, 2);
				} else {
					ConnectionCommands.prepareSmtForInsertScalar2ValNotNull(
							preparedStatement, _dataType, _value, 2);
				}
			} else // if ( _value != null )
			{
				ConnectionCommands.prepareSmtForInsertScalarNullValue(
						preparedStatement, _writable, _dataType, 2);
			}

			Timestamp _timeSt = new Timestamp(scalarEvent.getTimeStamp());
			preparedStatement.setTimestamp(1, _timeSt);

			preparedStatement.executeUpdate();
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

			message += " ( attribut = " + _attributeName + ")";
			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing DataBaseApi.insertScalarDataROOracle() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} catch (ArchivingException e) {
			throw e;
		} finally {
			ConnectionCommands.close(preparedStatement);
			preparedStatement = null;
			freeHarvestConnection();
		}
	}
}