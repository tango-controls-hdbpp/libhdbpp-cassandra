/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public class HDBOracleAttributeInsert extends HdbAttributeInsert /*
								  * implements
								  * ConnectionHarvestingCallback
								  */{

    private static final short DEFAULT_COMMIT_COUNTER = 10;
    private short insertionCounter = 0;
    private short maxInsertionCounter = DEFAULT_COMMIT_COUNTER;
    private static final String COMMIT_PERIOD_IN_COUNTER = "CommitCounter";

    private Connection connectionForInsertScalar;
    private IDBConnection dbConn;
    /**
     * Memorize statement to avoid reopen it at each insert. Avoid cursor leak
     * in DB
     */
    private final Map<String, PreparedStatement> statementScalarList = new HashMap<String, PreparedStatement>();

    // private final Map<String, CallableStatement> statementSpectrumList = new
    // HashMap<String, CallableStatement>();

    /**
     * @param con
     * @param ut
     * @param at
     */
    public HDBOracleAttributeInsert(final int type, final IAdtAptAttributes at) {
	super(type, at);
	// UpdateHarvestingConnections.getInstance().addUpdateHarvestingConnection(this);
	try {
	    maxInsertionCounter = GetConf
		    .readShortInDB("HdbArchiver", COMMIT_PERIOD_IN_COUNTER, DEFAULT_COMMIT_COUNTER);
	    // this.logger.trace(ILogger.LEVEL_INFO, "CommitPeriodInMinute = "
	    // + _commitInCounter);
	} catch (final ArchivingException e) {
	    maxInsertionCounter = DEFAULT_COMMIT_COUNTER;
	    // this.logger.trace(ILogger.LEVEL_ERROR,
	    // "ArchivingException has been raised during "
	    // + GetConf.COMMIT_PERIOD_IN_MINUTE
	    // + " property reading. Default value is used");
	}

    }

    @Override
    protected synchronized void insertScalarDataInDB(final StringBuffer _query, final String _attributeName,
	    final int _writable, final Timestamp _timeSt, final Object _value, final int _dataType)
	    throws ArchivingException {
	PreparedStatement preparedStatement = null;
	try {

	    if (connectionForInsertScalar == null || connectionForInsertScalar.isClosed()) {
		openConnection();
	    }
	    if (insertionCounter >= maxInsertionCounter) {
		closeConnection();
		openConnection();
	    }
	    final String query = _query.toString();
	    if (!statementScalarList.containsKey(query)) {
		preparedStatement = connectionForInsertScalar.prepareStatement(query);
		statementScalarList.put(query, preparedStatement);
	    } else {
		preparedStatement = statementScalarList.get(query);
	    }
	    ConnectionCommands.prepareSmtScalar(preparedStatement, _dataType, _writable, _value, 2);
	    preparedStatement.setTimestamp(1, _timeSt);
	    preparedStatement.executeUpdate();
	    insertionCounter++;
	} catch (final SQLException e) {
	    closeConnection();
	    final String message = "SqlException in HDBOracleAttributeInsert.insertScalarData";
	    logger.trace(ILogger.LEVEL_ERROR, message + ":" + e);
	    final String reason = GlobalConst.INSERT_FAILURE;
	    final String desc = e.getLocalizedMessage();
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
	}
    }

    private void openConnection() throws ArchivingException, SQLException {
	if (dbConn == null) {
	    dbConn = ConnectionFactory.getInstance(arch_type);
	}
	connectionForInsertScalar = dbConn.getConnection();
	logger.trace(ILogger.LEVEL_DEBUG, "new connection");
	connectionForInsertScalar.setAutoCommit(false);
    }

    private void closeConnection() {
	insertionCounter = 0;
	try {
	    if (connectionForInsertScalar != null && !connectionForInsertScalar.isClosed()) {
		try {
		    connectionForInsertScalar.commit();
		    logger.trace(ILogger.LEVEL_DEBUG, "commit OK");
		} catch (final SQLException e) {
		    logger.trace(ILogger.LEVEL_ERROR, "commit failed " + e);
		}

		for (final PreparedStatement smt : statementScalarList.values()) {
		    try {
			smt.close();
		    } catch (final SQLException e) {
			logger.trace(ILogger.LEVEL_ERROR, "closing statement failed: " + e);
		    }
		}
		statementScalarList.clear();
		dbConn.closeConnection(connectionForInsertScalar);
		logger.trace(ILogger.LEVEL_DEBUG, "connection closed");
	    }
	} catch (final SQLException e) {
	    logger.trace(ILogger.LEVEL_ERROR, "closing connection failed: " + e);
	}
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
    protected void insert_ImageData_RO_DataBase(StringBuffer query, final StringBuffer tableName,
	    final StringBuffer tableFields, final int dim_x, final int dim_y, final Timestamp timeSt,
	    final Double[][] dvalue, final StringBuffer valueStr, final String att_name) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return;
	}
	Connection conn = null;
	CallableStatement cstmt_ins_spectrum_ro = null;
	query = new StringBuffer().append("{call ").append(dbConn.getSchema()).append(".ins_im_1val (?, ?, ?, ?, ?)}");

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
	    } else {
		cstmt_ins_spectrum_ro.setString(5, valueStr.toString());
	    }

	    cstmt_ins_spectrum_ro.executeUpdate();

	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.INSERT_FAILURE;
	    final String desc = "Failed while executing HDBOracleAttributeInsert.insert_ImageData_RO() method..."
		    + "\r\n\t\t Query : " + query + "\r\n\t\t Param 1 (Attribute name) : " + att_name
		    + "\r\n\t\t Param 2 (Timestamp)      : " + timeSt.toString()
		    + "\r\n\t\t Param 3 (X Dimension)    : " + dim_x + "\r\n\t\t Param 3 (Y Dimension)    : " + dim_y
		    + "\r\n\t\t Param 4 (Value)          : " + valueStr.toString() + "\r\n\t\t Code d'erreur : "
		    + e.getErrorCode() + "\r\n\t\t Message : " + e.getMessage() + "\r\n\t\t SQL state : "
		    + e.getSQLState() + "\r\n\t\t Stack : ";
	    e.printStackTrace();

	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
	} finally {
	    ConnectionCommands.close(cstmt_ins_spectrum_ro);
	    dbConn.closeConnection(conn);
	}

    }

    @Override
    protected void insert_SpectrumData_RO_DataBase(StringBuffer query, final StringBuffer tableName,
	    final StringBuffer tableFields, final int dim_x, final Timestamp timeSt, final Double[] dvalue,
	    final StringBuffer valueStr, final String att_name) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return;
	}
	CallableStatement cstmt_ins_spectrum_ro = null;
	query = new StringBuffer().append("{call ").append(dbConn.getSchema()).append(".ins_sp_1val (?, ?, ?, ?)}");
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

	    cstmt_ins_spectrum_ro.executeUpdate();

	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
	    }
	    message += " ( attribut = " + att_name + ")";

	    final String reason = GlobalConst.INSERT_FAILURE;
	    final String desc = "Failed while executing HDBOracleAttributeInsert.insert_SpectrumData_RO() method..."
		    + "\r\n\t\t Query : " + query + "\r\n\t\t Param 1 (Attribute name) : " + att_name
		    + "\r\n\t\t Param 2 (Timestamp)      : " + timeSt.toString()
		    + "\r\n\t\t Param 3 (Dimension)      : " + dim_x + "\r\n\t\t Param 4 (Value)          : "
		    + valueStr.toString() + "\r\n\t\t Code d'erreur : " + e.getErrorCode() + "\r\n\t\t Message : "
		    + e.getMessage() + "\r\n\t\t SQL state : " + e.getSQLState() + "\r\n\t\t Stack : ";
	    e.printStackTrace();

	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
	} finally {
	    ConnectionCommands.close(cstmt_ins_spectrum_ro);
	    dbConn.closeConnection(conn);
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
    protected void insert_SpectrumData_RW_DataBase(StringBuffer query, final StringBuffer tableName,
	    final StringBuffer tableFields, final int dim_x, final Timestamp timeSt, final Double[] dvalueWrite,
	    final Double[] dvalueRead, final StringBuffer valueWriteStr, final StringBuffer valueReadStr,
	    final String att_name) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return;
	}
	CallableStatement cstmt_ins_spectrum_rw = null;
	Connection conn = null;
	query = new StringBuffer().append("{call ").append(dbConn.getSchema()).append(".ins_sp_2val (?, ?, ?, ?, ?)}");

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

	    cstmt_ins_spectrum_rw.executeUpdate();

	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
	    }
	    message += " ( attribut = " + att_name + ")";
	    final String reason = GlobalConst.INSERT_FAILURE;
	    final String desc = "Failed while executing HDBOracleAttributeInsert.insert_SpectrumData_RO() method..."
		    + "\r\n\t\t Query : " + query + "\r\n\t\t Param 1 (Attribute name) : " + att_name
		    + "\r\n\t\t Param 2 (Timestamp)      : " + timeSt.toString()
		    + "\r\n\t\t Param 3 (Dimension)      : " + dim_x + "\r\n\t\t Param 4 (Value Read)     : "
		    + valueReadStr.toString() + "\r\n\t\t Param 5 (Value Write)    : " + valueWriteStr.toString()
		    + "\r\n\t\t Code d'erreur : " + e.getErrorCode() + "\r\n\t\t Message : " + e.getMessage()
		    + "\r\n\t\t SQL state : " + e.getSQLState() + "\r\n\t\t Stack : ";
	    e.printStackTrace();

	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
	} finally {
	    ConnectionCommands.close(cstmt_ins_spectrum_rw);
	    dbConn.closeConnection(conn);
	}

    }

}