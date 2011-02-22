package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class ConnectionCommands {
	/**
	 *
	 */
	public ConnectionCommands() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands
	 * .IConnectionCommands#allow()
	 */
	public static void allow(Connection dbConn) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands
	 * .IConnectionCommands#close(java.sql.Statement)
	 */
	public static void close(Statement stmt){
		// TODO Auto-generated method stub
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands
	 * .IConnectionCommands#close(java.sql.ResultSet)
	 */
	public static void close(ResultSet rset){
		// TODO Auto-generated method stub
		if (rset != null) {
			try {
				rset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands
	 * .IConnectionCommands#commit()
	 */
	public static void commit(Connection conn, IDbUtils dbUtils)
			throws ArchivingException {
		// TODO Auto-generated method stub
		try {
			conn.commit();
		} catch (SQLException e) {
			String message = dbUtils.getCommunicationFailureMessage(e);
			String reason = GlobalConst.STATEMENT_FAILURE;
			String desc = "Failed while executing DataBaseApi.commit() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, ConnectionCommands.class.getName(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands
	 * .IConnectionCommands#minimumRequest()
	 */
	public static void minimumRequest(IDBConnection dbConn) throws SQLException {
		// TODO Auto-generated method stub
		String tableName = dbConn.getSchema() + ".adt";
		String query = "SELECT NULL FROM " + tableName + " WHERE ID=1";

		Connection conn = dbConn.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet res = null;
		dbConn.setLastStatement(stmt);
		// ResultSet rset =
		try {
			res = stmt.executeQuery(query);
			close(res);
			close(stmt);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stmt != null)
					close(stmt);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (Exception e) {
				dbConn.closeConnection(conn);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands
	 * .IConnectionCommands#rollback()
	 */
	public static void rollback(Connection conn, IDbUtils dbUtils)
			throws ArchivingException {
		// TODO Auto-generated method stub
		try {

			conn.rollback();
		} catch (SQLException e) {
			String message = dbUtils.getCommunicationFailureMessage(e);
			String reason = GlobalConst.STATEMENT_FAILURE;
			String desc = "Failed while executing DataBaseApi.rollback() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, ConnectionCommands.class.getName(), e);
		}

	}

	/**
	 * Fill the PreparedStatement object with a null value - For Insert request
	 *
	 * @param preparedStatement
	 * @param writable
	 * @param dataType
	 * @param pos
	 *            position of the value in the Insert request
	 * @throws SQLException
	 */
	public static void prepareSmtForInsertScalarNullValue(
			PreparedStatement preparedStatement, int writable, int dataType,
			int pos) throws SQLException {
		switch (dataType) {
		case TangoConst.Tango_DEV_STRING:
			preparedStatement.setNull(pos, java.sql.Types.VARCHAR);
			if (!(writable == AttrWriteType._READ || writable == AttrWriteType._WRITE))
				preparedStatement.setNull(pos + 1, java.sql.Types.VARCHAR);
			break;
		case TangoConst.Tango_DEV_STATE:
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
		case TangoConst.Tango_DEV_BOOLEAN:
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
		case TangoConst.Tango_DEV_FLOAT:
		case TangoConst.Tango_DEV_DOUBLE:
		default:
			preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			if (!(writable == AttrWriteType._READ || writable == AttrWriteType._WRITE))
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
		}
	}

	/**
	 * Fill the PreparedStatement object with a value not null - For Insert
	 * request - Read or Write Only
	 *
	 * @param preparedStatement
	 * @param dataType
	 * @param value
	 * @param pos
	 * @throws SQLException
	 * @throws ArchivingException
	 */
	public static void prepareSmtForInsertScalar1ValNotNull(
			PreparedStatement preparedStatement, int dataType, Object value,
			int pos) throws SQLException, ArchivingException {
		switch (dataType) {
		case TangoConst.Tango_DEV_STRING:
			preparedStatement.setString(pos, StringFormater
					.formatStringToWrite((String) value));
			break;
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			preparedStatement.setByte(pos, ((Byte) value).byteValue());
			break;
		case TangoConst.Tango_DEV_STATE:
			preparedStatement.setInt(pos, ((DevState) value).value());
			break;
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
			preparedStatement.setInt(pos, ((Integer) value).intValue());
			break;
		case TangoConst.Tango_DEV_BOOLEAN:
			preparedStatement.setBoolean(pos, ((Boolean) value).booleanValue());
			break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			preparedStatement.setShort(pos, ((Short) value).shortValue());
			break;
		case TangoConst.Tango_DEV_FLOAT:
			preparedStatement.setFloat(pos, ((Float) value).floatValue());
			break;
		case TangoConst.Tango_DEV_DOUBLE:
			if (Double.isNaN(((Double) value).doubleValue())) {
				// XXX : Should not replace NaN by null
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement
						.setDouble(pos, ((Double) value).doubleValue());
			}
			break;
		default:
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.INSERT_FAILURE;
			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing ConectionCommands.prepareSmtForInsertScalar1ValNotNull() method : Invalid DATA_TYPE ";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, ConnectionCommands.class.getName(), null);
		}
	}

	/**
	 * Fill the PreparedStatement object with a value not null - For Insert
	 * request - Read and Write
	 *
	 * @param preparedStatement
	 * @param dataType
	 * @param value
	 * @param pos
	 * @throws SQLException
	 * @throws ArchivingException
	 */
	public static void prepareSmtForInsertScalar2ValNotNull(
			PreparedStatement preparedStatement, int dataType, Object value,
			int pos) throws SQLException, ArchivingException {
		switch (dataType) {
		case TangoConst.Tango_DEV_STRING:
			if (((String[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.VARCHAR);
			} else {
				preparedStatement.setString(pos, StringFormater
						.formatStringToWrite(((String[]) value)[0]));
			}
			if (((String[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.VARCHAR);
			} else {
				preparedStatement.setString(pos + 1, StringFormater
						.formatStringToWrite(((String[]) value)[1]));
			}
			break;

		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			if (((Byte[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setByte(pos, ((Byte[]) value)[0].byteValue());
			}
			if (((Byte[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setByte(pos + 1, ((Byte[]) value)[1]
						.byteValue());
			}
			break;
		case TangoConst.Tango_DEV_STATE:
			if (((Integer[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement
						.setInt(pos, ((Integer[]) value)[0].intValue());
			}
			if (((Integer[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setInt(pos + 1, ((Integer[]) value)[1]
						.intValue());
			}
			break;
		case TangoConst.Tango_DEV_ULONG:
		case TangoConst.Tango_DEV_LONG:
			if (((Integer[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement
						.setInt(pos, ((Integer[]) value)[0].intValue());
			}
			if (((Integer[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setInt(pos + 1, ((Integer[]) value)[1]
						.intValue());
			}
			break;
		case TangoConst.Tango_DEV_BOOLEAN:
			if (((Boolean[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.BOOLEAN);
			} else {
				preparedStatement.setBoolean(pos, ((Boolean[]) value)[0]
						.booleanValue());
			}
			if (((Boolean[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.BOOLEAN);
			} else {
				preparedStatement.setBoolean(pos + 1, ((Boolean[]) value)[1]
						.booleanValue());
			}
			break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			if (((Short[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setShort(pos, ((Short[]) value)[0]
						.shortValue());
			}
			if (((Short[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setShort(pos + 1, ((Short[]) value)[1]
						.shortValue());
			}
			break;
		case TangoConst.Tango_DEV_FLOAT:
			if (((Float[]) value)[0] == null) {
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setFloat(pos, ((Float[]) value)[0]
						.floatValue());
			}
			if (((Float[]) value)[1] == null) {
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setFloat(pos + 1, ((Float[]) value)[1]
						.floatValue());
			}
			break;
		case TangoConst.Tango_DEV_DOUBLE:
			if (((Double[]) value)[0] == null || ((Double[]) value)[0].isNaN()) {
				// XXX : Should not replace NaN by null
				preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setDouble(pos, ((Double[]) value)[0]
						.doubleValue());
			}
			if (((Double[]) value)[1] == null || ((Double[]) value)[1].isNaN()) {
				// XXX : Should not replace NaN by null
				preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
			} else {
				preparedStatement.setDouble(pos + 1, ((Double[]) value)[1]
						.doubleValue());
			}
			break;
		default:
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.INSERT_FAILURE;
			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing ConnectionCommands.prepareSmtForInsertScalar2ValNotNull() method : Invalid DATA_TYPE ";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, ConnectionCommands.class.getName(), null);
		}
	}

}
