/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;

/**
 * @author AYADI
 * 
 */
public abstract class GenericMode implements IGenericMode {
    protected int arch_type;

    /**
	 *
	 */
    public GenericMode(final int type) {
	// TODO Auto-generated constructor stub
	arch_type = type;
    }

    protected abstract String getSelectField(String select_field);

    protected abstract void setSpec(Mode mode, ResultSet rset);

    protected abstract void setSpecificStatementForInsertMode(PreparedStatement preparedStatement,
	    AttributeLightMode attributeLightMode);

    protected abstract void updateSelectField(StringBuffer select_field);

    protected abstract void appendQuery(StringBuffer query, StringBuffer tableName,
	    StringBuffer select_field);

    /**
     * 
     * @return
     * @throws ArchivingException
     */
    public String[] getCurrentArchivedAtt() throws ArchivingException {
	return getAttribute(true);
    }

    /**
     * 
     * @param archivingOnly
     * @return
     * @throws ArchivingException
     */
    public String[] getAttribute(final boolean archivingOnly) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return null;
	}
	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;

	try {
	    final Vector<String> nameVect = new Vector<String>();
	    String[] nameArr = new String[5];

	    // First connect with the database
	    // if ( dbConn.isAutoConnect() )
	    // {
	    // dbConn.connect();
	    // }

	    // Create and execute the SQL query string
	    // Build the query string
	    final String select_field = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2];
	    final String table_1 = dbConn.getSchema() + "." + ConfigConst.ADT;
	    final String table_2 = dbConn.getSchema() + "." + ConfigConst.AMT;
	    final String tables = table_1 + ", " + table_2;
	    final String clause_1 = ConfigConst.AMT + "." + ConfigConst.stopDate + " IS NULL";
	    final String clause_2 = ConfigConst.AMT + "." + ConfigConst.ID + " = "
		    + ConfigConst.ADT + "." + ConfigConst.ID;

	    final String whereClause = archivingOnly ? "(" + clause_1 + " AND " + clause_2 + ")"
		    : clause_2;
	    String getAttributeDataQuery;
	    if (archivingOnly) {
		getAttributeDataQuery = "SELECT " + select_field + " FROM " + tables + " WHERE "
			+ whereClause;
	    } else {
		getAttributeDataQuery = "SELECT " + select_field + " FROM " + table_1;
	    }

	    conn = dbConn.getConnection();
	    stmt = conn.createStatement();
	    dbConn.setLastStatement(stmt);
	    System.out.println("CLA/DataBaseApi/getCurrentArchivedAtt/query|"
		    + getAttributeDataQuery + "|");
	    rset = stmt.executeQuery(getAttributeDataQuery);
	    while (rset.next()) {
		final String next = rset.getString(1);
		if (next != null) {
		    nameVect.addElement(next);
		}
	    }

	    nameArr = DbUtils.toStringArray(nameVect);

	    // Returns the names list
	    return nameArr;
	} catch (final SQLException e) {
	    e.printStackTrace();

	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed while executing DataBaseApi.getCurrentArchivedAtt() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.ERR, desc, this.getClass()
		    .getName(), e);
	} catch (final Throwable t) {
	    t.printStackTrace();

	    final SQLException sqle = new SQLException(t.toString());
	    sqle.setStackTrace(t.getStackTrace());

	    String message = "";
	    if (t.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || t.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed2 while executing GenericMode.getCurrentArchivedAtt() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.ERR, desc, this.getClass()
		    .getName(), sqle);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(stmt);
	    stmt = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
    }

    /**
     * <b>Description : </b> Gets the number of attributes that are being
     * archived in <I>HDB</I>
     * 
     * @return An integer which is the number of attributes being archived in
     *         <I>HDB</I>
     * @throws ArchivingException
     */
    public int getCurrentArchivedAttCount() throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return 0;
	}

	// todo test
	int activeSimpleSignalCount = 0;
	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;

	// First connect with the database
	// (if not already done)
	// if ( dbConn.isAutoConnect() )
	// dbConn.connect();

	// Create and execute the SQL query string
	final String select_field = "COUNT(*)";
	final String table = dbConn.getSchema() + "." + ConfigConst.AMT;
	final String clause_1 = ConfigConst.AMT + "." + ConfigConst.startDate + " IS NOT NULL";
	final String clause_2 = ConfigConst.AMT + "." + ConfigConst.stopDate + " IS NULL";
	final String getAttributeDataQuery = "SELECT DISTINCT(" + select_field + ")" + " FROM "
		+ table + " WHERE " + "(" + clause_1 + " AND " + clause_2 + ")";

	try {
	    conn = dbConn.getConnection();
	    stmt = conn.createStatement();
	    dbConn.setLastStatement(stmt);
	    rset = stmt.executeQuery(getAttributeDataQuery);
	    // Gets the result of the query
	    if (rset.next()) {
		activeSimpleSignalCount = rset.getInt(1);
	    }
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed while executing GenericMode.getCurrentArchivedAttCount() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(stmt);
	    stmt = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
	// Returns the number of active simple signals defined in HDB
	return activeSimpleSignalCount;
    }

    /**
     * <b>Description : </b> Gets the name of the device in charge of the
     * archiving of the given attribute.
     * 
     * @return the name of the device in charge of the archiving of the given
     *         attribute.
     * @throws ArchivingException
     */
    public String getDeviceInCharge(final String attribut_name) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return null;
	}

	String deviceInCharge = "";
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// Create and execute the SQL query string
	// Build the query string
	String getDeviceInChargeQuery = "";
	String select_field = "";
	select_field = select_field + ConfigConst.AMT + "." + ConfigConst.archiver;

	final String table_1 = dbConn.getSchema() + "." + ConfigConst.AMT;
	final String table_2 = dbConn.getSchema() + "." + ConfigConst.ADT;
	final String clause_1 = ConfigConst.AMT + "." + ConfigConst.ID + " = " + ConfigConst.ADT
		+ "." + ConfigConst.TAB_DEF[0];
	// String clause_2 = ConfigConst.TABS[ 0 ] + "." + ConfigConst.TAB_DEF[
	// 2 ] + " LIKE " + "?";
	final String clause_2 = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2] + "=" + "?";
	final String clause_3 = ConfigConst.AMT + "." + ConfigConst.stopDate + " IS NULL ";

	getDeviceInChargeQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
		+ " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")" + " AND "
		+ "(" + clause_3 + ")" + ") ";

	try {
	    // System.out.println (
	    // "CLA/GenericMode/getDeviceInCharge/query|"+getDeviceInChargeQuery+"|"
	    // );
	    conn = dbConn.getConnection();
	    preparedStatement = conn.prepareStatement(getDeviceInChargeQuery);
	    dbConn.setLastStatement(preparedStatement);
	    preparedStatement.setString(1, attribut_name.trim());
	    rset = preparedStatement.executeQuery();
	    while (rset.next()) {
		deviceInCharge = rset.getString(ConfigConst.archiver);
	    }
	} catch (final SQLException e) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + GlobalConst.EXTRAC_FAILURE;
	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed while executing GenericMode.getDeviceInCharge() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(preparedStatement);
	    preparedStatement = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
	// Returns the names list
	return deviceInCharge;
    }

    /**
     * <b>Description : </b>
     * 
     * @param att_name
     *            The name of the attribute
     * @return a boolean : "true" if the attribute of given name is currently
     *         archived, "false" otherwise.
     * @throws ArchivingException
     */
    public boolean isArchived(final String att_name) throws ArchivingException {
	return isArchived(att_name, null);
    }

    /**
     * <b>Description : </b>
     * 
     * @param att_name
     *            The name of the attribute
     * @param device_name
     *            The name of the device in charge
     * @return a boolean : "true" if the attribute named att_name is currently
     *         archived by the device named device_name, "false" otherwise.
     * @throws ArchivingException
     */
    public boolean isArchived(final String att_name, final String device_name)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return false;
	}

	boolean ret = false;
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// Build the query string
	String isArchivedQuery = "";
	final String select_field = ConfigConst.AMT_ID;
	final String table_1 = dbConn.getSchema() + "." + ConfigConst.AMT;
	final String table_2 = dbConn.getSchema() + "." + ConfigConst.ADT;
	final String clause_1 = ConfigConst.AMT + "." + ConfigConst.ID + " = " + ConfigConst.ADT
		+ "." + ConfigConst.ID;
	final String clause_2 = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
	final String clause_3 = ConfigConst.AMT_archiver + " = " + "?";
	final String clause_4 = ConfigConst.AMT_stopDate + " IS NULL ";

	isArchivedQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
		+ " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")";

	if (device_name != null) {
	    isArchivedQuery += " AND " + "(" + clause_3 + ")";
	}

	isArchivedQuery += " AND " + "(" + clause_4 + ")" + ") ";
	conn = dbConn.getConnection();
	if (conn != null) {
	    try {
		// System.out.println("isArchived - executing query " +
		// isArchivedQuery);
		preparedStatement = conn.prepareStatement(isArchivedQuery);
		dbConn.setLastStatement(preparedStatement);
		preparedStatement.setString(1, att_name.trim());
		if (device_name != null) {
		    preparedStatement.setString(2, device_name.trim());
		}
		rset = preparedStatement.executeQuery();
		if (rset.next()) {
		    ret = true;
		}
	    } catch (final SQLException e) {
		String message = "";
		if (e.getMessage().equalsIgnoreCase("Io exception: Broken pipe")
			|| e.getMessage().indexOf("Communication link failure") != -1) {
		    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.QUERY_FAILURE;
		final String desc = "Failed while executing GenericMode.isArchived(att_name,device_name) method...";
		throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);
	    } finally {
		ConnectionCommands.close(rset);
		rset = null;
		ConnectionCommands.close(preparedStatement);
		preparedStatement = null;
		dbConn.closeConnection(conn);
		conn = null;
	    }
	}
	// Close the connection with the database
	return ret;
    }

    /**
     * <b>Description : </b>
     * 
     * @param att_name
     *            The name of the attribute
     * @return a String : The name of the corresponding archiver if the
     *         attribute is beeing archived, an empty String otherwise
     * @throws ArchivingException
     */
    public String getArchiverForAttribute(final String att_name) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return null;
	}

	final Vector<String> archivVect = new Vector<String>();
	int res = 0;
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// Build the query string
	String isArchivedQuery = "";
	final String select_field = ConfigConst.AMT + "." + ConfigConst.archiver;
	final String table_1 = dbConn.getSchema() + "." + ConfigConst.AMT;
	final String table_2 = dbConn.getSchema() + "." + ConfigConst.ADT;
	final String clause_1 = ConfigConst.AMT + "." + ConfigConst.ID + " = " + ConfigConst.ADT
		+ "." + ConfigConst.ID;
	final String clause_2 = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
	final String clause_3 = ConfigConst.AMT_stopDate + " IS NULL ";

	isArchivedQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
		+ " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")" + " AND "
		+ "(" + clause_3 + ")" + ") ";
	try {
	    conn = dbConn.getConnection();
	    preparedStatement = conn.prepareStatement(isArchivedQuery);
	    dbConn.setLastStatement(preparedStatement);
	    preparedStatement.setString(1, att_name.trim());
	    rset = preparedStatement.executeQuery();
	    while (rset.next()) {
		archivVect.addElement(rset.getString(1));
		res++;
	    }
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase("Io exception: Broken pipe")
		    || e.getMessage().indexOf("Communication link failure") != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed while executing GenericMode.getArchiverForAttribute(att_name) method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(preparedStatement);
	    preparedStatement = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
	// Close the connection with the database
	if (res > 0) {
	    return archivVect.firstElement();
	} else {
	    return "";
	}
    }

    /**
     * <b>Description : </b>
     * 
     * @param att_name
     *            The name of the attribute
     * @return a String : The name of the corresponding archiver if the
     *         attribute is beeing archived, an empty String otherwise
     * @throws ArchivingException
     */
    public String getArchiverForAttributeEvenIfTheStopDateIsNotNull(final String att_name)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return null;
	}

	final Vector<String> archivVect = new Vector<String>();
	int res = 0;
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// Build the query string
	String isArchivedQuery = "";
	final String select_field = ConfigConst.AMT + "." + ConfigConst.archiver;
	final String table_1 = dbConn.getSchema() + "." + ConfigConst.AMT;
	final String table_2 = dbConn.getSchema() + "." + ConfigConst.ADT;
	final String clause_1 = ConfigConst.AMT_ID + " = " + ConfigConst.ADT + "." + ConfigConst.ID;
	final String clause_2 = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
	final String clause_3 = ConfigConst.AMT_stopDate + " IS NULL ";

	isArchivedQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
		+ " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")" + " AND "
		+ "(" + clause_3 + ")" + ") ";
	try {
	    conn = dbConn.getConnection();
	    preparedStatement = conn.prepareStatement(isArchivedQuery);
	    dbConn.setLastStatement(preparedStatement);
	    preparedStatement.setString(1, att_name.trim());
	    rset = preparedStatement.executeQuery();
	    while (rset.next()) {
		archivVect.addElement(rset.getString(1));
		res++;
	    }
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase("Io exception: Broken pipe")
		    || e.getMessage().indexOf("Communication link failure") != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed while executing GenericMode.getArchiverForAttribute(att_name) method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(preparedStatement);
	    preparedStatement = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
	// Close the connection with the database
	if (res > 0) {
	    return archivVect.firstElement();
	} else {
	    return "";
	}
    }

    /**
     * <b>Description : </b> Gets the current archiving mode for a given
     * attribute name.
     * 
     * @return An array of string containing all the current mode's informations
     *         for a given attibute's name
     * @throws ArchivingException
     */
    public Mode getCurrentArchivingMode(final String attribut_name) throws ArchivingException {
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	// boolean isArchived = false;
	final Mode mode = new Mode();
	if (dbConn != null) {
	    try {

		// Create and execute the SQL query string
		// Build the query string
		String select_field = ConfigConst.AMT_periodicMode + ", "
			+ ConfigConst.AMT_periodicModePeriod + ", " + ConfigConst.AMT_absoluteMode
			+ ", " + ConfigConst.AMT_absoluteModePeriod + ", "
			+ ConfigConst.AMT_absoluteModeInf + ", " + ConfigConst.AMT_absoluteModeSup
			+ ", " + ConfigConst.AMT_relativeMode + ", "
			+ ConfigConst.AMT_relativeModePeriod + ", "
			+ ConfigConst.AMT_relativeModeInf + ", " + ConfigConst.AMT_relativeModeSup
			+ ", " + ConfigConst.AMT_thresoldMode + ", "
			+ ConfigConst.AMT_thresoldModePeriod + ", "
			+ ConfigConst.AMT_thresoldModeInf + ", " + ConfigConst.AMT_thresoldModeSup
			+ ", " + ConfigConst.AMT_calcMode + ", " + ConfigConst.AMT_calcModePeriod
			+ ", " + ConfigConst.AMT_calcModeVal + ", " + ConfigConst.AMT_calcModeType
			+ ", " + ConfigConst.AMT_calcModeAlgo + ", " + ConfigConst.AMT_diffMode
			+ ", " + ConfigConst.AMT_diffModePeriod + ", " + ConfigConst.AMT_extMode;

		select_field = getSelectField(select_field);

		final String table_1 = dbConn.getSchema() + "." + ConfigConst.AMT;
		final String table_2 = dbConn.getSchema() + "." + ConfigConst.ADT;
		final String clause_1 = ConfigConst.AMT + "." + ConfigConst.ID + " = "
			+ ConfigConst.ADT + "." + ConfigConst.ID;
		// String clause_2 = ConfigConst.TABS[ 0 ] + "." +
		// ConfigConst.TAB_DEF[ 2 ] + " LIKE " + "?";
		final String clause_2 = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2] + " = "
			+ "?";
		final String clause_3 = ConfigConst.AMT + "." + ConfigConst.stopDate + " IS NULL ";

		final String getCurrentArchivingModeQuery = "SELECT " + select_field + " FROM "
			+ table_1 + ", " + table_2 + " WHERE (" + "(" + clause_1 + ")" + " AND "
			+ "(" + clause_2 + ")" + " AND " + "(" + clause_3 + ")" + ") ";

		// System.out.println (
		// "CLA/GenericMode/getCurrentArchivingMode/query|"+getCurrentArchivingModeQuery+"|"
		// );

		conn = dbConn.getConnection();
		preparedStatement = conn.prepareStatement(getCurrentArchivingModeQuery);
		dbConn.setLastStatement(preparedStatement);
		preparedStatement.setString(1, attribut_name.trim());
		rset = preparedStatement.executeQuery();
		while (rset.next()) {
		    // isArchived = true;
		    if (rset.getInt(ConfigConst.periodicMode) == 1) {
			final ModePeriode modePeriode = new ModePeriode(rset
				.getInt(ConfigConst.periodicModePeriod));
			mode.setModeP(modePeriode);
		    }
		    if (rset.getInt(ConfigConst.absoluteMode) == 1) {
			final ModeAbsolu modeAbsolu = new ModeAbsolu(rset
				.getInt(ConfigConst.absoluteModePeriod), rset
				.getDouble(ConfigConst.absoluteModeInf), rset
				.getDouble(ConfigConst.absoluteModeSup), false);
			mode.setModeA(modeAbsolu);
		    } else if (rset.getInt(ConfigConst.absoluteMode) == 2) {
			final ModeAbsolu modeAbsolu = new ModeAbsolu(rset
				.getInt(ConfigConst.absoluteModePeriod), rset
				.getDouble(ConfigConst.absoluteModeInf), rset
				.getDouble(ConfigConst.absoluteModeSup), true);
			mode.setModeA(modeAbsolu);
		    }
		    if (rset.getInt(ConfigConst.relativeMode) == 1) {
			final ModeRelatif modeRelatif = new ModeRelatif(rset
				.getInt(ConfigConst.relativeModePeriod), rset
				.getDouble(ConfigConst.relativeModeInf), rset
				.getDouble(ConfigConst.relativeModeSup), false);
			mode.setModeR(modeRelatif);
		    } else if (rset.getInt(ConfigConst.relativeMode) == 2) {
			final ModeRelatif modeRelatif = new ModeRelatif(rset
				.getInt(ConfigConst.relativeModePeriod), rset
				.getDouble(ConfigConst.relativeModeInf), rset
				.getDouble(ConfigConst.relativeModeSup), true);
			mode.setModeR(modeRelatif);
		    }
		    if (rset.getInt(ConfigConst.thresoldMode) == 1) {
			final ModeSeuil modeSeuil = new ModeSeuil(rset
				.getInt(ConfigConst.thresoldModePeriod), rset
				.getDouble(ConfigConst.thresoldModeInf), rset
				.getDouble(ConfigConst.thresoldModeSup));
			mode.setModeT(modeSeuil);
		    }
		    if (rset.getInt(ConfigConst.calcMode) == 1) {
			final ModeCalcul modeCalcul = new ModeCalcul(rset
				.getInt(ConfigConst.calcModePeriod), rset
				.getInt(ConfigConst.calcModeVal), rset
				.getInt(ConfigConst.calcModeType));
			mode.setModeC(modeCalcul);
			// Warning Field 18 is not used yet ...
		    }
		    if (rset.getInt(ConfigConst.diffMode) == 1) {
			final ModeDifference modeDifference = new ModeDifference(rset
				.getInt(ConfigConst.diffModePeriod));
			mode.setModeD(modeDifference);
		    }
		    if (rset.getInt(ConfigConst.extMode) == 1) {
			final ModeExterne modeExterne = new ModeExterne();
			mode.setModeE(modeExterne);
		    }
		    setSpec(mode, rset);
		}
	    } catch (final SQLException e) {
		e.printStackTrace();

		String message = "";
		if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.QUERY_FAILURE;
		final String desc = "Failed while executing GenericMode.getCurrentArchivingMode() method...";
		throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), e);
	    } catch (final Throwable t) {
		t.printStackTrace();

		final SQLException sqle = new SQLException(t.toString());
		sqle.setStackTrace(t.getStackTrace());

		String message = "";
		if (t.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
			|| t.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			    + GlobalConst.ADB_CONNECTION_FAILURE;
		} else {
		    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			    + GlobalConst.STATEMENT_FAILURE;
		}

		final String reason = GlobalConst.QUERY_FAILURE;
		final String desc = "Failed2 while executing GenericMode.getCurrentArchivingMode() method...";
		throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this
			.getClass().getName(), sqle);
	    } finally {
		ConnectionCommands.close(rset);
		rset = null;
		ConnectionCommands.close(preparedStatement);
		preparedStatement = null;
		dbConn.closeConnection(conn);
		conn = null;
	    }
	}

	return mode;
	// if (isArchived) {
	// return mode;
	// }
	// else {
	// mode = null;
	// throw new ArchivingException("Invalid attribute: " + attribut_name,
	// "Invalid attribute: " + attribut_name, ErrSeverity.WARN,
	// "No database connection or \"" + attribut_name
	// + "\" attribute not found in database", this.getClass().getName());
	// }
    }

    /**
     * This method retrieves a given archiver's current tasks and for a given
     * archiving type.
     * 
     * @param archiverName
     * @param historic
     * @return The current task for a given archiver and for a given archiving
     *         type.
     * @throws ArchivingException
     */
    public Vector<AttributeLightMode> getArchiverCurrentTasks(final String archiverName)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return null;
	}

	final Vector<AttributeLightMode> attributeListConfig = new Vector<AttributeLightMode>();
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet rset = null;
	// Create and execute the SQL query string
	// Build the query string
	String getArchiverCurrentTasksQuery = "";
	// ADT
	String select_field = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[2] + ", " + // fullname
		ConfigConst.ADT + "." + ConfigConst.TAB_DEF[8] + ", " + // data_type
		ConfigConst.ADT + "." + ConfigConst.TAB_DEF[9] + ", " + // data_format
		ConfigConst.ADT + "." + ConfigConst.TAB_DEF[10] + ", "; // writable
	// AMT
	select_field = select_field + ConfigConst.AMT_periodicMode + ", "
		+ ConfigConst.AMT_periodicModePeriod + ", " + ConfigConst.AMT_absoluteMode + ", "
		+ ConfigConst.AMT_absoluteModePeriod + ", " + ConfigConst.AMT_absoluteModeInf
		+ ", " + ConfigConst.AMT_absoluteModeSup + ", " + ConfigConst.AMT_relativeMode
		+ ", " + ConfigConst.AMT_relativeModePeriod + ", "
		+ ConfigConst.AMT_relativeModeInf + ", " + ConfigConst.AMT_relativeModeSup + ", "
		+ ConfigConst.AMT_thresoldMode + ", " + ConfigConst.AMT_thresoldModePeriod + ", "
		+ ConfigConst.AMT_thresoldModeInf + ", " + ConfigConst.AMT_thresoldModeSup + ", "
		+ ConfigConst.AMT_calcMode + ", " + ConfigConst.AMT_calcModePeriod + ", "
		+ ConfigConst.AMT_calcModeVal + ", " + ConfigConst.AMT_calcModeType + ", "
		+ ConfigConst.AMT_calcModeAlgo + ", " + ConfigConst.AMT_diffMode + ", "
		+ ConfigConst.AMT_diffModePeriod + ", " + ConfigConst.AMT_extMode;
	select_field = getSelectField(select_field);

	// adt.full_name, adt.data_type, adt.data_format, adt.writable, amt.*
	final String table_1 = dbConn.getSchema() + "." + ConfigConst.ADT;
	final String table_2 = dbConn.getSchema() + "." + ConfigConst.AMT;
	final String clause_1 = ConfigConst.ADT + "." + ConfigConst.TAB_DEF[0] + " = "
		+ ConfigConst.AMT + "." + ConfigConst.ID;
	// String clause_2 = ConfigConst.TABS[ 2 ] + "." + ConfigConst.TAB_MOD[
	// 1 ] + " LIKE " + "?";
	final String clause_2 = ConfigConst.AMT + "." + ConfigConst.archiver + " = " + "?";
	final String clause_3 = ConfigConst.AMT + "." + ConfigConst.stopDate + " IS NULL ";

	getArchiverCurrentTasksQuery = "SELECT " + select_field + " FROM " + table_1 + ", "
		+ table_2 + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")"
		+ " AND " + "(" + clause_3 + ")" + ") ";

	try {
	    conn = dbConn.getConnection();
	    preparedStatement = conn.prepareStatement(getArchiverCurrentTasksQuery);
	    // System.out.println("/historic/"+historic+"/getArchiverCurrentTasksQuery/"+getArchiverCurrentTasksQuery);
	    dbConn.setLastStatement(preparedStatement);
	    preparedStatement.setString(1, archiverName.trim());
	    rset = preparedStatement.executeQuery();
	    while (rset.next()) {
		boolean sd;
		final AttributeLightMode attributeLightMode = new AttributeLightMode();

		attributeLightMode.setAttribute_complete_name(rset.getString(1));
		attributeLightMode.setData_type(rset.getInt(2));
		attributeLightMode.setData_format(rset.getInt(3));
		attributeLightMode.setWritable(rset.getInt(4));
		attributeLightMode.setDevice_in_charge(archiverName);
		// Mode
		final Mode mode = new Mode();
		if (rset.getInt(ConfigConst.periodicMode) != 0) {
		    final ModePeriode modePeriode = new ModePeriode(rset
			    .getInt(ConfigConst.periodicModePeriod));
		    mode.setModeP(modePeriode);
		}
		if (rset.getInt(ConfigConst.absoluteMode) != 0) {
		    sd = rset.getInt(ConfigConst.absoluteMode) == 2;
		    final ModeAbsolu modeAbsolu = new ModeAbsolu(rset
			    .getInt(ConfigConst.absoluteModePeriod), rset
			    .getDouble(ConfigConst.absoluteModeInf), rset
			    .getDouble(ConfigConst.absoluteModeSup), sd);
		    mode.setModeA(modeAbsolu);
		}
		if (rset.getInt(ConfigConst.relativeMode) != 0) {
		    sd = rset.getInt(ConfigConst.relativeMode) == 2;
		    final ModeRelatif modeRelatif = new ModeRelatif(rset
			    .getInt(ConfigConst.relativeModePeriod), rset
			    .getDouble(ConfigConst.relativeModeInf), rset
			    .getDouble(ConfigConst.relativeModeSup), sd);
		    mode.setModeR(modeRelatif);
		}
		if (rset.getInt(ConfigConst.thresoldMode) != 0) {
		    final ModeSeuil modeSeuil = new ModeSeuil(rset
			    .getInt(ConfigConst.thresoldModePeriod), rset
			    .getDouble(ConfigConst.thresoldModeInf), rset
			    .getDouble(ConfigConst.thresoldModeSup));
		    mode.setModeT(modeSeuil);
		}
		if (rset.getInt(ConfigConst.calcMode) != 0) {
		    final ModeCalcul modeCalcul = new ModeCalcul(rset
			    .getInt(ConfigConst.calcModePeriod), rset
			    .getInt(ConfigConst.calcModeVal), rset.getInt(ConfigConst.calcModeType));
		    mode.setModeC(modeCalcul);
		}
		if (rset.getInt(ConfigConst.diffMode) != 0) {
		    final ModeDifference modeDifference = new ModeDifference(rset
			    .getInt(ConfigConst.diffModePeriod));
		    mode.setModeD(modeDifference);
		}
		if (rset.getInt(ConfigConst.extMode) != 0) {
		    final ModeExterne modeExterne = new ModeExterne();
		    mode.setModeE(modeExterne);
		}
		setSpec(mode, rset);
		attributeLightMode.setMode(mode);
		attributeListConfig.add(attributeLightMode);
	    }
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.QUERY_FAILURE;
	    final String desc = "Failed while executing GenericMode.getArchiverCurrentTasks() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(preparedStatement);
	    preparedStatement = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
	return attributeListConfig;
    }

    /**
     * <b>Description : </b> Inserts a record in the "Attribut Mode Table"
     * <I>(mySQL only)</I>. Each time that the archiving of an attribute is
     * triggered, this table is fielded.
     */
    public void insertModeRecord(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return;
	}

	Connection conn = null;
	PreparedStatement preparedStatement = null;
	final int id = AdtAptAttributesFactory.getInstance(arch_type).getIds().getBufferedAttID(
		attributeLightMode.getAttribute_complete_name());
	final StringBuffer tableName = new StringBuffer().append(dbConn.getSchema()).append(".")
		.append(ConfigConst.AMT);

	final StringBuffer select_field = new StringBuffer();
	select_field.append(ConfigConst.AMT_ID).append(", ");
	select_field.append(ConfigConst.AMT_archiver).append(", ");
	select_field.append(ConfigConst.AMT_startDate).append(", ");
	select_field.append(ConfigConst.AMT_periodicMode).append(", ");
	select_field.append(ConfigConst.AMT_periodicModePeriod).append(", ");
	select_field.append(ConfigConst.AMT_absoluteMode).append(", ");
	select_field.append(ConfigConst.AMT_absoluteModePeriod).append(", ");
	select_field.append(ConfigConst.AMT_absoluteModeInf).append(", ");
	select_field.append(ConfigConst.AMT_absoluteModeSup).append(", ");
	select_field.append(ConfigConst.AMT_relativeMode).append(", ");
	select_field.append(ConfigConst.AMT_relativeModePeriod).append(", ");
	select_field.append(ConfigConst.AMT_relativeModeInf).append(", ");
	select_field.append(ConfigConst.AMT_relativeModeSup).append(", ");
	select_field.append(ConfigConst.AMT_thresoldMode).append(", ");
	select_field.append(ConfigConst.AMT_thresoldModePeriod).append(", ");
	select_field.append(ConfigConst.AMT_thresoldModeInf).append(", ");
	select_field.append(ConfigConst.AMT_thresoldModeSup).append(", ");
	select_field.append(ConfigConst.AMT_calcMode).append(", ");
	select_field.append(ConfigConst.AMT_calcModePeriod).append(", ");
	select_field.append(ConfigConst.AMT_calcModeVal).append(", ");
	select_field.append(ConfigConst.AMT_calcModeType).append(", ");
	select_field.append(ConfigConst.AMT_calcModeAlgo).append(", ");
	select_field.append(ConfigConst.AMT_diffMode).append(", ");
	select_field.append(ConfigConst.AMT_diffModePeriod).append(", ");
	select_field.append(ConfigConst.AMT_extMode);

	updateSelectField(select_field);

	// Create and execute the SQL query string
	// Build the query string
	final StringBuffer query = new StringBuffer();
	appendQuery(query, tableName, select_field);
	// System.out.println("GenericMode.insertModeRecord : Query ... \r\n\t"
	// + query);

	try {
	    conn = dbConn.getConnection();
	    preparedStatement = conn.prepareStatement(query.toString());
	    dbConn.setLastStatement(preparedStatement);

	    preparedStatement.setInt(1, id);
	    preparedStatement.setString(2, attributeLightMode.getDevice_in_charge());
	    // preparedStatement.setTimestamp(3 ,
	    // attributeLightMode.getTrigger_time());
	    preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
	    // the field named "stop_date (3) is not included"

	    // Periodical Mode
	    if (attributeLightMode.getMode().getModeP() != null) {
		preparedStatement.setInt(4, 1);
		preparedStatement.setInt(5, attributeLightMode.getMode().getModeP().getPeriod());
	    } else {
		preparedStatement.setInt(4, 0);
		preparedStatement.setInt(5, 0);
	    }
	    // Absolute Mode
	    if (attributeLightMode.getMode().getModeA() != null) {
		if (attributeLightMode.getMode().getModeA().isSlow_drift()) {
		    preparedStatement.setInt(6, 2);
		} else {
		    preparedStatement.setInt(6, 1);
		}
		preparedStatement.setInt(7, attributeLightMode.getMode().getModeA().getPeriod());
		preparedStatement.setDouble(8, attributeLightMode.getMode().getModeA().getValInf());
		preparedStatement.setDouble(9, attributeLightMode.getMode().getModeA().getValSup());
	    } else {
		preparedStatement.setInt(6, 0);
		preparedStatement.setInt(7, 0);
		preparedStatement.setInt(8, 0);
		preparedStatement.setInt(9, 0);
	    }
	    // Relative Mode
	    if (attributeLightMode.getMode().getModeR() != null) {
		if (attributeLightMode.getMode().getModeR().isSlow_drift()) {
		    preparedStatement.setInt(10, 2);
		} else {
		    preparedStatement.setInt(10, 1);
		}
		preparedStatement.setInt(11, attributeLightMode.getMode().getModeR().getPeriod());
		preparedStatement.setDouble(12, attributeLightMode.getMode().getModeR()
			.getPercentInf());
		preparedStatement.setDouble(13, attributeLightMode.getMode().getModeR()
			.getPercentSup());
	    } else {
		preparedStatement.setInt(10, 0);
		preparedStatement.setInt(11, 0);
		preparedStatement.setInt(12, 0);
		preparedStatement.setInt(13, 0);
	    }
	    // Threshold Mode
	    if (attributeLightMode.getMode().getModeT() != null) {
		preparedStatement.setInt(14, 1);
		preparedStatement.setInt(15, attributeLightMode.getMode().getModeT().getPeriod());
		preparedStatement.setDouble(16, attributeLightMode.getMode().getModeT()
			.getThresholdInf());
		preparedStatement.setDouble(17, attributeLightMode.getMode().getModeT()
			.getThresholdSup());
	    } else {
		preparedStatement.setInt(14, 0);
		preparedStatement.setInt(15, 0);
		preparedStatement.setInt(16, 0);
		preparedStatement.setInt(17, 0);
	    }
	    // On Calculation Mode
	    if (attributeLightMode.getMode().getModeC() != null) {
		preparedStatement.setInt(18, 1);
		preparedStatement.setInt(19, attributeLightMode.getMode().getModeC().getPeriod());
		preparedStatement.setInt(20, attributeLightMode.getMode().getModeC().getRange());
		preparedStatement.setInt(21, attributeLightMode.getMode().getModeC()
			.getTypeCalcul());
		preparedStatement.setString(22, "");
	    } else {
		preparedStatement.setInt(18, 0);
		preparedStatement.setInt(19, 0);
		preparedStatement.setInt(20, 0);
		preparedStatement.setInt(21, 0);
		preparedStatement.setString(22, "");
	    }
	    // On Difference Mode
	    if (attributeLightMode.getMode().getModeD() != null) {
		preparedStatement.setInt(23, 1);
		preparedStatement.setInt(24, attributeLightMode.getMode().getModeD().getPeriod());
	    } else {
		preparedStatement.setInt(23, 0);
		preparedStatement.setInt(24, 0);
	    }
	    // On External Mode
	    if (attributeLightMode.getMode().getModeE() != null) {
		preparedStatement.setInt(25, 1);
	    } else {
		preparedStatement.setInt(25, 0);
	    }

	    // Specif Tdb

	    setSpecificStatementForInsertMode(preparedStatement, attributeLightMode);
	    preparedStatement.executeUpdate();
	} catch (final SQLException e) {
	    e.printStackTrace();

	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.UPDATE_FAILURE;
	    final String desc = "Failed while executing GenericMode.insertModeRecord() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(preparedStatement);
	    preparedStatement = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	}
    }

    /**
     * Method which updates a record in the "Attribute Mode Table" Each time
     * that the archiving of an attribute is stopped, one of this table's row is
     * fielded.
     */

    public void updateModeRecord(final String attribute_name) throws ArchivingException {
	// DbModeFactory.getInstance(arch_type).updateModeRecord(attribute_name);
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
	if (dbConn == null || att == null) {
	    return;
	}

	final int att_Id = att.getIds().getBufferedAttID(attribute_name);
	final Connection conn = dbConn.getConnection();
	PreparedStatement preparedStatement = null;
	// Create and execute the SQL query string
	// Build the query string
	final String set_clause = new StringBuffer().append(ConfigConst.stopDate).append(" = ?")
		.toString();
	final String updated_table = new StringBuffer().append(dbConn.getSchema()).append(".")
		.append(ConfigConst.AMT).toString();
	final String clause_1 = new StringBuffer().append(ConfigConst.AMT_ID).append(" = ").append(
		att_Id).toString();
	final String clause_2 = new StringBuffer().append(ConfigConst.AMT_stopDate).append(
		" IS NULL ").toString();
	final String update_query = new StringBuffer().append("UPDATE ").append(updated_table)
		.append(" SET ").append(set_clause).append(" WHERE ").append("(").append("(")
		.append(clause_1).append(")").append(" AND ").append("(").append(clause_2).append(
			")").append(")").toString();
	System.out.println("updateModeRecord - executing query " + update_query);
	try {

	    preparedStatement = conn.prepareStatement(update_query);
	    dbConn.setLastStatement(preparedStatement);
	    final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    preparedStatement.setTimestamp(1, timestamp);
	    preparedStatement.executeUpdate();
	} catch (final SQLException e) {
	    String message = "";
	    if (e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.UPDATE_FAILURE;
	    final String desc = "Failed while executing MySqlMode.updateModeRecord() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(preparedStatement);
	    dbConn.closeConnection(conn);
	}
    }

    public void updateModeRecordWithoutStop(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	if (dbConn == null) {
	    return;
	}
	final int id = AdtAptAttributesFactory.getInstance(arch_type).getIds().getBufferedAttID(
		attributeLightMode.getAttribute_complete_name());
	final StringBuffer tableName = new StringBuffer().append(dbConn.getSchema()).append(".")
		.append(ConfigConst.AMT);

	final StringBuffer query = new StringBuffer();
	query.append("UPDATE ").append(tableName).append(" SET ");
	query.append(ConfigConst.AMT_archiver).append("=?,");
	// select_field.append(ConfigConst.AMT).append(".").append(ConfigConst.TAB_MOD[2]).append(
	// "=?,");
	query.append(ConfigConst.AMT_periodicMode).append("=?,");
	query.append(ConfigConst.AMT_periodicModePeriod).append("=?,");
	query.append(ConfigConst.AMT_absoluteMode).append("=?,");
	query.append(ConfigConst.AMT_absoluteModePeriod).append("=?,");
	query.append(ConfigConst.AMT_absoluteModeInf).append("=?,");
	query.append(ConfigConst.AMT_absoluteModeSup).append("=?,");
	query.append(ConfigConst.AMT_relativeMode).append("=?,");
	query.append(ConfigConst.AMT_relativeModePeriod).append("=?,");
	query.append(ConfigConst.AMT_relativeModeInf).append("=?,");
	query.append(ConfigConst.AMT_relativeModeSup).append("=?,");
	query.append(ConfigConst.AMT_thresoldMode).append("=?,");
	query.append(ConfigConst.AMT_thresoldModePeriod).append("=?,");
	query.append(ConfigConst.AMT_thresoldModeInf).append("=?,");
	query.append(ConfigConst.AMT_thresoldModeSup).append("=?,");
	query.append(ConfigConst.AMT_calcMode).append("=?,");
	query.append(ConfigConst.AMT_calcModePeriod).append("=?,");
	query.append(ConfigConst.AMT_calcModeVal).append("=?,");
	query.append(ConfigConst.AMT_calcModeType).append("=?,");
	query.append(ConfigConst.AMT_calcModeAlgo).append("=?,");
	query.append(ConfigConst.AMT_diffMode).append("=?,");
	query.append(ConfigConst.AMT_diffModePeriod).append("=?,");
	query.append(ConfigConst.AMT_extMode).append(" = ? WHERE ");
	query.append(ConfigConst.AMT_ID).append(" = ? AND ");
	query.append(ConfigConst.AMT_stopDate).append(" is null");
	updateSelectField(query);

	// Create and execute the SQL query string
	// Build the query string
	PreparedStatement preparedStatement = null;
	Connection conn = null;
	try {
	    conn = dbConn.getConnection();
	    preparedStatement = conn.prepareStatement(query.toString());
	    dbConn.setLastStatement(preparedStatement);
	    int i = 1;
	    preparedStatement.setString(i++, attributeLightMode.getDevice_in_charge());

	    // Periodical Mode
	    if (attributeLightMode.getMode().getModeP() != null) {
		preparedStatement.setInt(i++, 1);
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeP().getPeriod());
	    } else {
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
	    }
	    // Absolute Mode
	    if (attributeLightMode.getMode().getModeA() != null) {
		if (attributeLightMode.getMode().getModeA().isSlow_drift()) {
		    preparedStatement.setInt(i++, 2);
		} else {
		    preparedStatement.setInt(i++, 1);
		}
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeA().getPeriod());
		preparedStatement.setDouble(i++, attributeLightMode.getMode().getModeA()
			.getValInf());
		preparedStatement.setDouble(i++, attributeLightMode.getMode().getModeA()
			.getValSup());
	    } else {
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
	    }
	    // Relative Mode
	    if (attributeLightMode.getMode().getModeR() != null) {
		if (attributeLightMode.getMode().getModeR().isSlow_drift()) {
		    preparedStatement.setInt(i++, 2);
		} else {
		    preparedStatement.setInt(i++, 1);
		}
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeR().getPeriod());
		preparedStatement.setDouble(i++, attributeLightMode.getMode().getModeR()
			.getPercentInf());
		preparedStatement.setDouble(i++, attributeLightMode.getMode().getModeR()
			.getPercentSup());
	    } else {
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
	    }
	    // Threshold Mode
	    if (attributeLightMode.getMode().getModeT() != null) {
		preparedStatement.setInt(i++, 1);
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeT().getPeriod());
		preparedStatement.setDouble(i++, attributeLightMode.getMode().getModeT()
			.getThresholdInf());
		preparedStatement.setDouble(i++, attributeLightMode.getMode().getModeT()
			.getThresholdSup());
	    } else {
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
	    }
	    // On Calculation Mode
	    if (attributeLightMode.getMode().getModeC() != null) {
		preparedStatement.setInt(i++, 1);
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeC().getPeriod());
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeC().getRange());
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeC()
			.getTypeCalcul());
		preparedStatement.setString(i++, "");
	    } else {
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
		preparedStatement.setString(i++, "");
	    }
	    // On Difference Mode
	    if (attributeLightMode.getMode().getModeD() != null) {
		preparedStatement.setInt(i++, 1);
		preparedStatement.setInt(i++, attributeLightMode.getMode().getModeD().getPeriod());
	    } else {
		preparedStatement.setInt(i++, 0);
		preparedStatement.setInt(i++, 0);
	    }
	    // On External Mode
	    if (attributeLightMode.getMode().getModeE() != null) {
		preparedStatement.setInt(i++, 1);
	    } else {
		preparedStatement.setInt(i++, 0);
	    }
	    // ID
	    preparedStatement.setInt(i++, id);

	    System.out.println("preparedStatement " + preparedStatement.toString());

	    // Specif Tdb

	    setSpecificStatementForInsertMode(preparedStatement, attributeLightMode);
	    preparedStatement.executeUpdate();
	} catch (final SQLException e) {
	    e.printStackTrace();

	    String message = "";
	    if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
		    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.ADB_CONNECTION_FAILURE;
	    } else {
		message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
			+ GlobalConst.STATEMENT_FAILURE;
	    }

	    final String reason = GlobalConst.UPDATE_FAILURE;
	    final String desc = "Failed while executing GenericMode.insertModeRecord() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(preparedStatement);
	    dbConn.closeConnection(conn);
	}
    }
}
