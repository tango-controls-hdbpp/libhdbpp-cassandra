/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataExtractor;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.HdbTdbDbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

/**
 * @author AYADI
 * 
 */
public abstract class DataGettersBetweenDates extends DataExtractor {

    /**
     * @param con
     * @param ut
     * @param at
     */
    public DataGettersBetweenDates(final int type) {
	super(type);
    }

    abstract protected String getAttScalarDataRequest(SamplingType samplingType, boolean ro_fields,
	    String tableName, String time_0, String time_1);

    abstract protected Vector getAttSpectrumDataBetweenDates(String att_name, String time_0,
	    String time_1, int data_type, SamplingType samplingFactor) throws ArchivingException;

    abstract protected Vector getAttImageDataBetweenDatesRequest(Vector my_imageS, ResultSet rset,
	    int data_type, boolean isBothReadAndWrite);

    /**
     * <b>Description : </b> Retrieves data beetwen two dates, for a given
     * scalar attribute.
     * 
     * @param argin
     *            The attribute's name, the beginning date (DD-MM-YYYY
     *            HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
     * @param samplingFactor
     * @return The scalar data for the specified attribute <br>
     * @throws ArchivingException
     */
    public DbData getAttDataBetweenDates(final String[] argin, final SamplingType samplingFactor)
	    throws ArchivingException {
	final long t1 = System.currentTimeMillis();
	final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
	final IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
	if (dbUtils == null || att == null) {
	    return null;
	}

	final String att_name = argin[0].trim();
	final String time_0 = dbUtils.getTime(argin[1]);
	final String time_1 = dbUtils.getTime(argin[2]);

	DevVarDoubleStringArray dvdsa = null;
	Vector result;
	final HdbTdbDbData dbData = new HdbTdbDbData(att_name);
	final int[] tfw = att.getAtt_TFW_Data(att_name);
	dbData.setData_type(tfw[0]);
	dbData.setData_format(tfw[1]);
	dbData.setWritable(tfw[2]);

	switch (dbData.getData_format()) { // [0 - > SCALAR] (1 - > SPECTRUM] [2
	// - > IMAGE]
	case AttrDataFormat._SCALAR:
	    dvdsa = getAttScalarDataBetweenDates(att_name, time_0, time_1, dbData.getWritable(),
		    samplingFactor);
	    dbData.setData(dvdsa);
	    break;
	case AttrDataFormat._SPECTRUM:
	    result = getAttSpectrumDataBetweenDates(att_name, time_0, time_1,
		    dbData.getData_type(), samplingFactor);
	    dbData.setData(result);
	    break;
	case AttrDataFormat._IMAGE:
	    result = getAttImageDataBetweenDates(att_name, time_0, time_1, dbData.getData_type(),
		    samplingFactor);
	    dbData.setData(result);
	    break;
	default:
	    methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
	}
	return dbData;
    }

    /*
     * <b>Description : </b> Returns the number of data beetwen two dates and,
     * for a given scalar attribute.
     * 
     * @param argin The attribute's name, the beginning date (DD-MM-YYYY
     * HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
     * 
     * @return The number of data beetwen two dates and, for a given scalar
     * attribute.<br>
     * 
     * @throws ArchivingException
     */
    public int getAttDataBetweenDatesCount(final String[] argin) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	final IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
	if (dbConn == null || dbUtils == null) {
	    return 0;
	}

	final String getAttributeDataQuery = "SELECT " + "COUNT(*)" + " FROM " + dbConn.getSchema()
		+ "." + dbUtils.getTableName(argin[0].trim()) + " WHERE ("
		+ ConfigConst.TAB_SCALAR_RO[0] + " BETWEEN "
		+ dbUtils.toDbTimeString(argin[1].trim()) + " AND "
		+ dbUtils.toDbTimeString(argin[2].trim()) + ") ORDER BY time";

	return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);
    }

    /*
	 *
	 */
    private DevVarDoubleStringArray getAttScalarDataBetweenDates(final String att_name,
	    final String time_0, final String time_1, final int writable,
	    final SamplingType samplingType) throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	final IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
	final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
	if (dbConn == null || dbUtils == null || att == null) {
	    return null;
	}

	final List<String> timeVect = new LinkedList<String>();
	final List<Double> valueRVect = new LinkedList<Double>();
	final List<String> valueRVectString = new LinkedList<String>();
	final List<Double> valueWVect = new LinkedList<Double>();
	final List<String> valueWVectString = new LinkedList<String>();

	final boolean ro_fields = writable == AttrWriteType._READ
		|| writable == AttrWriteType._WRITE;
	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;
	final int data_type = att.getAtt_TFW_Data(att_name)[0];

	// Create and execute the SQL query string
	String query;
	// final String fields = "";
	final String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
	query = getAttScalarDataRequest(samplingType, ro_fields, tableName, time_0, time_1);

	try {
	    conn = dbConn.getConnection();
	    // stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
	    // ResultSet.CONCUR_READ_ONLY);
	    // stmt.setFetchSize(Integer.MIN_VALUE);
	    stmt = conn.createStatement();
//	    stmt.setFetchSize(1000);
	    dbConn.setLastStatement(stmt);
	    // System.out.println ( "CLA/getAttScalarDataBetweenDates/BEFORE" );
	    rset = stmt.executeQuery(query);

	    // System.out.println ( "CLA/getAttScalarDataBetweenDates/AFTER" );
	    if (ro_fields) {
		while (rset.next()) {
		    timeVect.add(DateUtil.stringToDisplayString(rset.getString(1)));

		    if (data_type == TangoConst.Tango_DEV_STRING) {
			String val = rset.getString(2);
			if (rset.wasNull()) {
			    valueRVectString.add("null");
			} else {
			    val = StringFormater.formatStringToRead(val);
			    valueRVectString.add(new String(val));
			}
		    } else if (data_type == TangoConst.Tango_DEV_BOOLEAN) {
			final String result = rset.getString(2);
			if (rset.wasNull()) {
			    valueRVect.add(new Double(GlobalConst.NAN_FOR_NULL));
			} else {
			    final boolean resultValue = "1".equalsIgnoreCase(result.trim())
				    || "true".equalsIgnoreCase(result.trim())
				    || "vrai".equalsIgnoreCase(result.trim());
			    valueRVect.add(new Double((resultValue ? 1 : 0)));
			}
		    } else {
			final double dvalue = rset.getDouble(2);
			if (rset.wasNull()) {
			    valueRVect.add(new Double(GlobalConst.NAN_FOR_NULL));
			} else {
			    valueRVect.add(new Double(dvalue));
			}
		    }
		}
	    } else {
		while (rset.next()) {
		    timeVect.add(DateUtil.stringToDisplayString(rset.getString(1)));

		    if (data_type == TangoConst.Tango_DEV_STRING) {
			String result1 = rset.getString(2);
			if (rset.wasNull()) {
			    result1 = "null";
			}
			String result2 = rset.getString(3);
			if (rset.wasNull()) {
			    result2 = "null";
			}
			valueRVectString.add(StringFormater.formatStringToRead(result1));
			valueWVectString.add(StringFormater.formatStringToRead(result2));
		    } else if (data_type == TangoConst.Tango_DEV_BOOLEAN) {
			final String result1 = rset.getString(2);
			if (rset.wasNull()) {
			    valueRVect.add(new Double(GlobalConst.NAN_FOR_NULL));
			} else {
			    final boolean resultValueRead = "1".equalsIgnoreCase(result1.trim())
				    || "true".equalsIgnoreCase(result1.trim())
				    || "vrai".equalsIgnoreCase(result1.trim());
			    valueRVect.add(new Double((resultValueRead ? 1 : 0)));
			}
			final String result2 = rset.getString(3);
			if (rset.wasNull()) {
			    valueWVect.add(new Double(GlobalConst.NAN_FOR_NULL));
			} else {
			    final boolean resultValueWrite = "1".equalsIgnoreCase(result2.trim())
				    || "true".equalsIgnoreCase(result2.trim())
				    || "vrai".equalsIgnoreCase(result2.trim());
			    valueWVect.add(new Double((resultValueWrite ? 1 : 0)));
			}
		    } else {
			final double result1 = rset.getDouble(2);
			if (rset.wasNull()) {
			    valueRVect.add(new Double(GlobalConst.NAN_FOR_NULL));
			} else {
			    valueRVect.add(new Double(result1));
			}
			final double result2 = rset.getDouble(3);
			if (rset.wasNull()) {
			    valueWVect.add(new Double(GlobalConst.NAN_FOR_NULL));
			} else {
			    valueWVect.add(new Double(result2));
			}
		    }
		}
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
	    final String desc = "Failed while executing DataGettersBetweenDates.getAttDataBetweenDates() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    ConnectionCommands.close(stmt);
	    dbConn.closeConnection(conn);

	}

	DevVarDoubleStringArray dvdsa;
	// String[] timeArr;
	// double[] valueRWArr;
	if (data_type == TangoConst.Tango_DEV_STRING) {
	    // in case of Tango_DEV_STRING, time is coded in double, value in
	    // String
	    final String[] timeArr = timeVect.toArray(new String[timeVect.size()]);
	    final double[] timeDouble = new double[timeArr.length];
	    for (int i = 0; i < timeArr.length; i++) {
		timeDouble[i] = DateUtil.stringToMilli(timeArr[i]);
	    }
	    // timeArr = DbUtils.toStringArray(valueRVect, valueWVect);
	    valueRVectString.addAll(valueRVectString);
	    final String[] val = valueRVectString.toArray(new String[valueRVectString.size()]);
	    dvdsa = new DevVarDoubleStringArray(timeDouble, val);
	} else {
	    final String[] timeArr = timeVect.toArray(new String[timeVect.size()]);
	    valueRVect.addAll(valueWVect);
	    final Double[] valueRWArr = valueRVect.toArray(new Double[valueRVect.size()]);
	    dvdsa = new DevVarDoubleStringArray(ArrayUtils.toPrimitive(valueRWArr), timeArr);
	}

	return dvdsa;
    }

    /*
	 *
	 */
    public Vector getAttImageDataBetweenDates(final String[] argin) throws ArchivingException {
	final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
	if (att == null) {
	    return null;
	}
	final String att_name = argin[0].trim();
	final String time_0 = argin[1].trim();
	final String time_1 = argin[2].trim();
	final int[] tfw = att.getAtt_TFW_Data(att_name);
	Vector my_imageS = new Vector();
	final int data_format = att.getAttDataFormat(att_name);
	final String message = "", reason = "", desc = "";
	switch (data_format) { // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
	case AttrDataFormat._SCALAR:
	    methods.makeDataException(data_format, "Image", "Scalar");
	case AttrDataFormat._SPECTRUM:
	    methods.makeDataException(data_format, "Image", "Spectrum");
	case AttrDataFormat._IMAGE:
	    my_imageS = getAttImageDataBetweenDates(att_name, time_0, time_1, tfw[0], SamplingType
		    .getSamplingType(SamplingType.ALL));
	    break;
	}
	return my_imageS;
    }

    /**
     * 
     * @param att_name
     * @param time_0
     * @param time_1
     * @param data_type
     * @param samplingType
     * @return
     * @throws ArchivingException
     */
    private Vector getAttImageDataBetweenDates(final String att_name, final String time_0,
	    final String time_1, final int data_type, final SamplingType samplingType)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	final IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
	final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
	if (dbConn == null || dbUtils == null) {
	    return null;
	}

	// TODO Auto-generated method stub
	// System.out.println ( "CLA/getAttSpectrumDataBetweenDatesOracle/" );

	Vector my_imageS = new Vector();
	final int writable = att.getAttDataWritable(att_name);
	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;
	// Create and execute the SQL query string
	// Build the query string
	String query = "";
	String selectField_0 = "";
	String selectField_1 = "";
	String selectField_2 = "";
	String selectField_3 = "";
	String selectField_4 = null;
	String selectFields = "";
	String dateClause = "";
	String tableName = "";
	tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name) + " T";
	final boolean isBothReadAndWrite = !(writable == AttrWriteType._READ || writable == AttrWriteType._WRITE);
	if (samplingType.hasSampling()) {
	    final String format = samplingType.getMySqlFormat();
	    if (!isBothReadAndWrite) {
		selectField_0 = dbUtils.toDbTimeFieldString(
			"T" + "." + ConfigConst.TAB_IMAGE_RO[0], format);
		selectField_1 = "AVG (" + "T" + "." + ConfigConst.TAB_IMAGE_RO[1] + ")";
		selectField_2 = "AVG (" + "T" + "." + ConfigConst.TAB_IMAGE_RO[2] + ")";
		selectField_3 = "to_clob ( MIN ( to_char (" + "T" + "."
			+ ConfigConst.TAB_IMAGE_RO[3] + ") ) )";
	    } else {
		selectField_0 = dbUtils.toDbTimeFieldString(
			"T" + "." + ConfigConst.TAB_IMAGE_RW[0], format);
		selectField_1 = "AVG (" + "T" + "." + ConfigConst.TAB_IMAGE_RW[1] + ")";
		selectField_2 = "AVG (" + "T" + "." + ConfigConst.TAB_IMAGE_RW[2] + ")";
		selectField_3 = "to_clob ( MIN ( to_char (" + "T" + "."
			+ ConfigConst.TAB_IMAGE_RW[3] + ") ) )";
		selectField_4 = "to_clob ( MIN ( to_char (" + "T" + "."
			+ ConfigConst.TAB_IMAGE_RW[4] + ") ) )";
	    }

	    final String whereClause = ConfigConst.TAB_IMAGE_RO[0] + " BETWEEN "
		    + dbUtils.toDbTimeString(time_0.trim()) + " AND "
		    + dbUtils.toDbTimeString(time_1.trim());
	    final String groupByClause = " GROUP BY "
		    + dbUtils.toDbTimeFieldString(ConfigConst.TAB_IMAGE_RO[0], format);
	    final String orderByClause = " ORDER BY "
		    + dbUtils.toDbTimeFieldString(ConfigConst.TAB_IMAGE_RO[0], format);

	    selectFields = selectField_0 + ", " + selectField_1 + ", " + selectField_2 + ", "
		    + selectField_3;
	    if (isBothReadAndWrite) {
		selectFields += ", " + selectField_4;
	    }

	    query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + whereClause
		    + groupByClause + orderByClause;
	} else {
	    if (!isBothReadAndWrite) {
		selectField_0 = "T" + "." + ConfigConst.TAB_IMAGE_RO[0];
		selectField_1 = "T" + "." + ConfigConst.TAB_IMAGE_RO[1];
		selectField_2 = "T" + "." + ConfigConst.TAB_IMAGE_RO[2];
		selectField_3 = "T" + "." + ConfigConst.TAB_IMAGE_RO[3];
	    } else {
		selectField_0 = "T" + "." + ConfigConst.TAB_IMAGE_RW[0];
		selectField_1 = "T" + "." + ConfigConst.TAB_IMAGE_RW[1];
		selectField_2 = "T" + "." + ConfigConst.TAB_IMAGE_RW[2];
		selectField_3 = "T" + "." + ConfigConst.TAB_IMAGE_RW[3];
		selectField_4 = "T" + "." + ConfigConst.TAB_IMAGE_RW[4];
	    }
	    selectFields = selectField_0 + ", " + selectField_1 + ", " + selectField_2 + ", "
		    + selectField_3;
	    if (isBothReadAndWrite) {
		selectFields += ", " + selectField_4;
	    }

	    dateClause = selectField_0 + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim())
		    + " AND " + dbUtils.toDbTimeString(time_1.trim());
	    query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + dateClause
		    + ")" + " ORDER BY time";
	}

	try {
	    conn = dbConn.getConnection();
	    stmt = conn.createStatement();
	    dbConn.setLastStatement(stmt);
	    rset = stmt.executeQuery(query);
	    my_imageS = getAttImageDataBetweenDatesRequest(my_imageS, rset, data_type,
		    isBothReadAndWrite);
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
	    final String desc = "Failed while executing DataGettersBetweenDates.getAttImageDataBetweenDatesOracle() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(stmt);
	    stmt = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	    // try {
	    // ConnectionCommands.close(rset);
	    // ConnectionCommands.close(stmt);
	    // dbConn.closeConnection(conn);
	    // } catch (SQLException e) {
	    // dbConn.closeConnection(conn);
	    // e.printStackTrace();
	    //
	    // String message = "";
	    // if (e.getMessage().equalsIgnoreCase(
	    // GlobalConst.COMM_FAILURE_ORACLE)
	    // || e.getMessage().indexOf(
	    // GlobalConst.COMM_FAILURE_MYSQL) != -1)
	    // message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
	    // + GlobalConst.ADB_CONNECTION_FAILURE;
	    // else
	    // message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
	    // + GlobalConst.STATEMENT_FAILURE;
	    //
	    // String reason = GlobalConst.QUERY_FAILURE;
	    // String desc =
	    // "Failed while executing DataGettersBetweenDates.getAttImageDataBetweenDatesOracle() method...";
	    // throw new ArchivingException(message, reason, ErrSeverity.WARN,
	    // desc, this.getClass().getName(), e);
	    // }
	}
	return my_imageS;

    }

    /**
     * 
     * @param att_name
     * @param time
     * @return
     * @throws ArchivingException
     */
    public double[][] getAttImageDataForDate(final String att_name, final String time)
	    throws ArchivingException {
	final IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
	final IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
	if (dbConn == null || dbUtils == null) {
	    return null;
	}

	Connection conn = null;
	Statement stmt = null;
	ResultSet rset = null;
	// Create and execute the SQL query string
	// Build the query string
	String query = "";
	String selectField_0 = "";
	String selectFields = "";
	String dateClause = "";
	String tableName = "";
	tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);

	selectField_0 = ConfigConst.TAB_IMAGE_RO[3];
	final String dateField = ConfigConst.TAB_IMAGE_RO[0];
	selectFields = selectField_0;

	dateClause = dateField + " = " + "'" + time.trim() + "'";
	query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + dateClause
		+ ")" + " ORDER BY time";
	// System.out.println (
	// "CLA/DataGettersBetweenDates/getAttImageDataForDate/query/"+query+"/"
	// );

	double[][] dvalueArr = null;

	try {
	    conn = dbConn.getConnection();
	    stmt = conn.createStatement();
	    dbConn.setLastStatement(stmt);
	    rset = stmt.executeQuery(query);

	    if (!rset.next()) {
		return null;
	    }

	    // Value
	    String valueReadSt = null;
	    StringTokenizer stringTokenizerReadRows = null;
	    if (rset.getObject(1) != null) {
		valueReadSt = rset.getString(1);
		if (rset.wasNull()) {
		    valueReadSt = "null";
		}
		stringTokenizerReadRows = new StringTokenizer(valueReadSt,
			GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS);
	    }

	    int rowIndex = 0;
	    int numberOfRows = 0;
	    int numberOfCols = 0;

	    if (stringTokenizerReadRows != null) {
		numberOfRows = stringTokenizerReadRows.countTokens();
		dvalueArr = new double[numberOfRows][];

		while (stringTokenizerReadRows.hasMoreTokens()) {
		    final String currentRowRead = stringTokenizerReadRows.nextToken();
		    if (currentRowRead == null || currentRowRead.trim().equals("")) {
			break;
		    }

		    final StringTokenizer stringTokenizerReadCols = new StringTokenizer(
			    currentRowRead, GlobalConst.CLOB_SEPARATOR_IMAGE_COLS);
		    numberOfCols = stringTokenizerReadCols.countTokens();
		    final double[] currentRow = new double[numberOfCols];
		    int colIndex = 0;

		    while (stringTokenizerReadCols.hasMoreTokens()) {
			String currentValRead = stringTokenizerReadCols.nextToken();
			if (currentValRead == null) {
			    break;
			}
			currentValRead = currentValRead.trim();
			if (currentValRead.equals("")) {
			    break;
			}

			currentRow[colIndex] = Double.parseDouble(currentValRead);
			colIndex++;
		    }

		    dvalueArr[rowIndex] = currentRow;
		    rowIndex++;
		}
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
	    final String desc = "Failed while executing DataGettersBetweenDates.getAttSpectrumDataBetweenDatesMySql() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
		    .getName(), e);
	} finally {
	    ConnectionCommands.close(rset);
	    rset = null;
	    ConnectionCommands.close(stmt);
	    stmt = null;
	    dbConn.closeConnection(conn);
	    conn = null;
	    // try {
	    // ConnectionCommands.close(rset);
	    // ConnectionCommands.close(stmt);
	    // dbConn.closeConnection(conn);
	    // } catch (SQLException e) {
	    // dbConn.closeConnection(conn);
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    // }
	}
	return dvalueArr;
    }

}
