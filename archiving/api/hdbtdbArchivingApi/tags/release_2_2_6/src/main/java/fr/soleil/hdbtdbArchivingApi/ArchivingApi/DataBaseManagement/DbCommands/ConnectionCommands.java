package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OraclePreparedStatement;
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

    public static void close(final Statement stmt) {
	if (stmt != null) {
	    try {
		stmt.close();
	    } catch (final SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public static void close(final ResultSet rset) {
	if (rset != null) {
	    try {
		rset.close();
	    } catch (final SQLException e) {
		// ignore
		e.printStackTrace();
	    }
	}
    }

    public static void commit(final Connection conn, final IDbUtils dbUtils)
	    throws ArchivingException {
	try {
	    conn.commit();
	} catch (final SQLException e) {
	    final String message = dbUtils.getCommunicationFailureMessage(e);
	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.commit() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc,
		    ConnectionCommands.class.getName(), e);
	}

    }

    public static void minimumRequest(final IDBConnection dbConn) throws SQLException,
	    ArchivingException {
	final String tableName = dbConn.getSchema() + ".adt";
	final String query = "SELECT NULL FROM " + tableName + " WHERE ID=1";

	final Connection conn = dbConn.getConnection();
	final Statement stmt = conn.createStatement();
	ResultSet res = null;
	dbConn.setLastStatement(stmt);
	// ResultSet rset =
	try {
	    res = stmt.executeQuery(query);
	    close(res);
	    close(stmt);
	} catch (final SQLException e) {
	    throw e;
	} finally {
	    try {
		if (stmt != null) {
		    close(stmt);
		}
		if (conn != null) {
		    dbConn.closeConnection(conn);
		}
	    } catch (final Exception e) {
		dbConn.closeConnection(conn);
	    }
	}

    }

    public static void rollback(final Connection conn, final IDbUtils dbUtils)
	    throws ArchivingException {
	try {
	    conn.rollback();
	} catch (final SQLException e) {
	    final String message = dbUtils.getCommunicationFailureMessage(e);
	    final String reason = GlobalConst.STATEMENT_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.rollback() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc,
		    ConnectionCommands.class.getName(), e);
	}

    }

    public static void prepareSmtScalar(final PreparedStatement preparedStatement,
	    final int dataType, final int writeType, final Object value, final int pos)
	    throws SQLException, ArchivingException {
	if (value == null) {
	    if (dataType == TangoConst.Tango_DEV_STRING) {
		preparedStatement.setNull(pos, java.sql.Types.VARCHAR);
		if (writeType == AttrWriteType._READ_WRITE) {
		    preparedStatement.setNull(pos + 1, java.sql.Types.VARCHAR);
		}
	    } else {
		preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
		if (writeType == AttrWriteType._READ_WRITE) {
		    preparedStatement.setNull(pos + 1, java.sql.Types.NUMERIC);
		}
	    }
	}
	final Class<?> clazz = value.getClass();

	if (clazz.isArray()) { // read or write attribute
	    prepareSmt(preparedStatement, Array.get(value, 0), pos);
	    prepareSmt(preparedStatement, Array.get(value, 1), pos + 1);
	} else {
	    prepareSmt(preparedStatement, value, pos);
	}
    }

    private static void prepareSmt(final PreparedStatement preparedStatement, final Object value,
	    final int pos) throws SQLException, ArchivingException {
	final Class<?> clazz = value.getClass();
	if (clazz.equals(String.class)) {
	    preparedStatement.setString(pos, StringFormater.formatStringToWrite((String) value));
	} else if (clazz.equals(Byte.class)) {
	    preparedStatement.setByte(pos, ((Byte) value));
	} else if (clazz.equals(DevState.class)) {
	    preparedStatement.setInt(pos, ((DevState) value).value());
	} else if (clazz.equals(Integer.class)) {
	    preparedStatement.setInt(pos, ((Integer) value));
	} else if (clazz.equals(Boolean.class)) {
	    preparedStatement.setBoolean(pos, ((Boolean) value));
	} else if (clazz.equals(Short.class)) {
	    preparedStatement.setShort(pos, ((Short) value));
	} else if (clazz.equals(Float.class)) {
	    preparedStatement.setFloat(pos, ((Float) value));
	} else if (clazz.equals(Double.class)) {
	    if (Double.isNaN(((Double) value))) {
		preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
		// FIXME: remove this crappy code that refers to oracle API
		if (preparedStatement instanceof OraclePreparedStatement) {
		    ((OraclePreparedStatement) preparedStatement).setBinaryDouble(pos, Double.NaN);
		} else { // NaN not supported
		    preparedStatement.setNull(pos, java.sql.Types.NUMERIC);
		}
	    } else {
		preparedStatement.setDouble(pos, ((Double) value));
	    }
	} else {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
		    + GlobalConst.INSERT_FAILURE;
	    final String reason = GlobalConst.INSERT_FAILURE;
	    final String desc = "Failed while executing ConectionCommands.prepareSmtForInsertScalar1ValNotNull() method : Invalid DATA_TYPE ";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc,
		    ConnectionCommands.class.getName(), null);
	}

    }
}
