/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

/**
 * @author AYADI
 *
 */
public class OracleDataGettersBetweenDates extends DataGettersBetweenDates {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public OracleDataGettersBetweenDates(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.
	 * AttributeExtractor
	 * .DataGettersBetweenDates.DataGettersBetweenDates#getAttScalarDataRequest
	 * (fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType, boolean,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected String getAttScalarDataRequest(SamplingType samplingType,
			boolean ro_fields, String tableName, String time_0, String time_1) {
		// TODO Auto-generated method stub
		IDBConnection dbConn = null;
		try {
			dbConn = ConnectionFactory.getInstance(arch_type);
		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbConn == null || dbUtils == null)
			return null;

		String query = "";
		String fields = "";
		String whereClause = "";
		String groupByClause = "";
		String orderByClause = "";
		if (samplingType.hasSampling()) {
			if (!samplingType.hasAdditionalFiltering()) {
				String format = samplingType.getOracleFormat();
				String groupingNormalisationType = SamplingType
						.getGroupingNormalisationType(ConfigConst.BD_ORACLE);

				fields = ro_fields ? (dbUtils.toDbTimeFieldString(
						ConfigConst.TAB_SCALAR_RO[0], format)
						+ ", " + groupingNormalisationType + "(" + ConfigConst.TAB_SCALAR_RO[1])
						+ ")"
						: (dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SCALAR_RW[0], format)
								+ ", "
								+ groupingNormalisationType
								+ "("
								+ ConfigConst.TAB_SCALAR_RW[1]
								+ ") , "
								+ groupingNormalisationType + "(" + ConfigConst.TAB_SCALAR_RW[2])
								+ ")";
				// System.out.println (
				// "CLA/getAttScalarDataBetweenDates/fields1|"+fields+"|" );
				whereClause = ConfigConst.TAB_SCALAR_RO[0] + " BETWEEN "
						+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
						+ dbUtils.toDbTimeString(time_1.trim());
				groupByClause = " GROUP BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SCALAR_RO[0], format);
				orderByClause = " ORDER BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SCALAR_RO[0], format);

				query = "SELECT " + fields + " FROM " + tableName + " WHERE "
						+ whereClause + groupByClause + orderByClause;
			} else {
				String format = samplingType.getOneLevelHigherFormat(false);
				// System.out.println (
				// "getAttScalarDataBetweenDates/format|"+format+"|" );
				// String fullFormat = ( SamplingType.getSamplingType (
				// SamplingType.ALL ) ).getFormat ( db_type ==
				// ConfigConst.BD_MYSQL );
				String fullFormat = (SamplingType
						.getSamplingType(SamplingType.SECOND)).getFormat(false);

				String groupingNormalisationType = SamplingType
						.getGroupingNormalisationType(ConfigConst.BD_ORACLE);

				String minTime = "MIN(" + ConfigConst.TAB_SCALAR_RO[0] + ")";
				String convertedMinTime = dbUtils.toDbTimeFieldString(minTime,
						fullFormat);

				fields = ro_fields ? convertedMinTime + ", "
						+ groupingNormalisationType + "("
						+ ConfigConst.TAB_SCALAR_RO[1] + ")" : convertedMinTime
						+ ", " + groupingNormalisationType + "("
						+ ConfigConst.TAB_SCALAR_RW[1] + ") , "
						+ groupingNormalisationType + "("
						+ ConfigConst.TAB_SCALAR_RW[2] + ")";
				// System.out.println (
				// "CLA/getAttScalarDataBetweenDates/fields2|"+fields+"|" );
				whereClause = ConfigConst.TAB_SCALAR_RO[0] + " BETWEEN "
						+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
						+ dbUtils.toDbTimeString(time_1.trim());

				groupByClause = " GROUP BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SCALAR_RO[0], format);
				// System.out.println (
				// "getAttScalarDataBetweenDates/groupByClause|"+groupByClause+"|"
				// );

				groupByClause += samplingType.getAdditionalFilteringClause(
						false, ConfigConst.TAB_SCALAR_RO[0]);
				orderByClause = " ORDER BY " + "MIN("
						+ ConfigConst.TAB_SCALAR_RW[0] + ")";

				query = "SELECT " + fields + " FROM " + tableName + " WHERE "
						+ whereClause + groupByClause + orderByClause;
			}
		} else {
			fields = ro_fields ? (dbUtils
					.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[0])
					+ ", " + ConfigConst.TAB_SCALAR_RO[1])
					: (dbUtils
							.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[0])
							+ ", " + ConfigConst.TAB_SCALAR_RW[1] + ", " + ConfigConst.TAB_SCALAR_RW[2]);

			// System.out.println (
			// "CLA/getAttScalarDataBetweenDates/fields3|"+fields+"|" );
			whereClause = ConfigConst.TAB_SCALAR_RO[0] + " BETWEEN "
					+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
					+ dbUtils.toDbTimeString(time_1.trim());

			query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "("
					+ whereClause + ")" + " ORDER BY time";
		}
		return query;

	}

	@Override
	public Vector getAttSpectrumDataBetweenDates(String att_name,
			String time_0, String time_1, int data_type,
			SamplingType samplingType) throws ArchivingException {
		// TODO Auto-generated method stub
		// System.out.println ( "CLA/getAttSpectrumDataBetweenDatesOracle/" );
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if (dbConn == null || dbUtils == null || att == null)
			return null;

		Vector my_spectrumS = new Vector();
		int writable = att.getAttDataWritable(att_name);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String query = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectField_2 = "";
		String selectField_3 = null;
		String selectFields = "";
		String dateClause = "";
		String whereClause = "";
		String groupByClause = "";
		String orderByClause = "";
		String tableName = "";
		tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name)
				+ " T";
		boolean isBothReadAndWrite = !(writable == AttrWriteType._READ || writable == AttrWriteType._WRITE);

		if (samplingType.hasSampling()) {
			String groupingNormalisationType = "MIN"; // can't average a
														// spectrum
			if (!samplingType.hasAdditionalFiltering()) {
				String format = samplingType.getOracleFormat();
				if (!isBothReadAndWrite) {
					selectField_0 = dbUtils.toDbTimeFieldString("T" + "."
							+ ConfigConst.TAB_SPECTRUM_RO[0], format);
					selectField_1 = "AVG (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RO[1] + ")";
					selectField_2 = "to_clob ( " + groupingNormalisationType
							+ " ( to_char (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RO[2] + ") ) )";
				} else {
					selectField_0 = dbUtils.toDbTimeFieldString("T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[0], format);
					selectField_1 = "AVG (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[1] + ")";
					selectField_2 = "to_clob ( " + groupingNormalisationType
							+ " ( to_char (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[2] + ") ) )";
					selectField_3 = "to_clob ( " + groupingNormalisationType
							+ " ( to_char (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[3] + ") ) )";
				}

				selectFields = selectField_0 + ", " + selectField_1 + ", "
						+ selectField_2;
				if (isBothReadAndWrite) {
					selectFields += ", " + selectField_3;
				}

				whereClause = ConfigConst.TAB_SPECTRUM_RO[0] + " BETWEEN "
						+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
						+ dbUtils.toDbTimeString(time_1.trim());
				orderByClause = " ORDER BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SPECTRUM_RO[0], format);
				groupByClause = " GROUP BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SPECTRUM_RO[0], format);

				query = "SELECT " + selectFields + " FROM " + tableName
						+ " WHERE " + whereClause + groupByClause
						+ orderByClause;
			} else {
				String format = samplingType.getOneLevelHigherFormat(false);
				String fullFormat = (SamplingType
						.getSamplingType(SamplingType.SECOND)).getFormat(false);

				String minTime = "MIN(" + ConfigConst.TAB_SCALAR_RO[0] + ")";
				selectField_0 = dbUtils
						.toDbTimeFieldString(minTime, fullFormat);

				if (!isBothReadAndWrite) {
					selectField_1 = "AVG (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RO[1] + ")";
					selectField_2 = "to_clob ( " + groupingNormalisationType
							+ " ( to_char (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RO[2] + ") ) )";
				} else {
					selectField_1 = "AVG (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[1] + ")";
					selectField_2 = "to_clob ( " + groupingNormalisationType
							+ " ( to_char (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[2] + ") ) )";
					selectField_3 = "to_clob ( " + groupingNormalisationType
							+ " ( to_char (" + "T" + "."
							+ ConfigConst.TAB_SPECTRUM_RW[3] + ") ) )";
				}

				selectFields = selectField_0 + ", " + selectField_1 + ", "
						+ selectField_2;
				if (isBothReadAndWrite) {
					selectFields += ", " + selectField_3;
				}

				whereClause = ConfigConst.TAB_SPECTRUM_RO[0] + " BETWEEN "
						+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
						+ dbUtils.toDbTimeString(time_1.trim());
				groupByClause = " GROUP BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SCALAR_RO[0], format);
				groupByClause += samplingType.getAdditionalFilteringClause(
						false, ConfigConst.TAB_SCALAR_RO[0]);
				orderByClause = " ORDER BY " + "MIN("
						+ ConfigConst.TAB_SCALAR_RW[0] + ")";

				query = "SELECT " + selectFields + " FROM " + tableName
						+ " WHERE " + whereClause + groupByClause
						+ orderByClause;
			}
		} else {
			if (!isBothReadAndWrite) {
				selectField_0 = "T" + "." + ConfigConst.TAB_SPECTRUM_RO[0];
				selectField_1 = "T" + "." + ConfigConst.TAB_SPECTRUM_RO[1];
				selectField_2 = "T" + "." + ConfigConst.TAB_SPECTRUM_RO[2];
			} else {
				selectField_0 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[0];
				selectField_1 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[1];
				selectField_2 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[2];
				selectField_3 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[3];
			}
			selectFields = dbUtils.toDbTimeFieldString(selectField_0) + ", "
					+ selectField_1 + ", " + selectField_2;
			if (isBothReadAndWrite) {
				selectFields += ", " + selectField_3;
			}

			dateClause = selectField_0 + " BETWEEN "
					+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
					+ dbUtils.toDbTimeString(time_1.trim());
			query = "SELECT " + selectFields + " FROM " + tableName + " WHERE "
					+ "(" + dateClause + ")" + " ORDER BY time";
		}

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(query);
			while (rset.next()) {
				// System.out.println (
				// "GIR/getAttSpectrumDataBetweenDatesOracle/rset.next(): i="+i
				// );
				SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
				SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
				// Timestamp
				try {
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get TimeStamp: i="+i
					// );
					spectrumEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset
							.getString(1)));
					spectrumEvent_rw.setTimeStamp(DateUtil.stringToMilli(rset
							.getString(1)));
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get TimeStamp OK: i="+i
					// );
				} catch (Exception e) {
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get TimeStamp KO: i="+i
					// );
				}

				// Dim
				int dim_x = 0;
				try {
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get dim x: i="+i
					// );
					dim_x = rset.getInt(2);
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get dim x OK: i="+i
					// );
				} catch (Exception e) {
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get dim x KO: i="+i
					// );
				}
				spectrumEvent_ro.setDim_x(dim_x);
				spectrumEvent_rw.setDim_x(dim_x);
				// Value
				Clob readClob = null;
				String readString = null;

				try {
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get read CLOB: i="+i
					// );
					readClob = rset.getClob(3);
					if (rset.wasNull()) {
						readString = "null";
					} else {
						readString = readClob.getSubString(1, (int) readClob
								.length());
					}
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get read CLOB OK - get substring OK: i="+i
					// );
				} catch (Exception e) {
					// System.out.println (
					// "GIR/getAttSpectrumDataBetweenDatesOracle/get read CLOB KO: i="+i
					// );
					// if (dbConn.isCanceled()) return null;
				}

				Clob writeClob = null;
				String writeString = null;
				if (isBothReadAndWrite) {
					try {
						// System.out.println (
						// "GIR/getAttSpectrumDataBetweenDatesOracle/get write CLOB: i="+i
						// );
						writeClob = rset.getClob(4);
						if (rset.wasNull()) {
							writeString = "null";
						} else {
							writeString = writeClob.getSubString(1,
									(int) writeClob.length());
						}
						// System.out.println (
						// "GIR/getAttSpectrumDataBetweenDatesOracle/get write CLOB OK - get substring OK: i="+i
						// );
					} catch (Exception e) {
						// System.out.println (
						// "GIR/getAttSpectrumDataBetweenDatesOracle/get write CLOB KO: i="+i
						// );
					}
				}

				// System.out.println (
				// "GIR/getAttSpectrumDataBetweenDatesOracle/get spectrum value: i="+i
				// );
				Object value = dbUtils.getSpectrumValue(readString,
						writeString, data_type);
				// System.out.println (
				// "GIR/getAttSpectrumDataBetweenDatesOracle/get spectrum value OK: i="+i
				// );
				if (isBothReadAndWrite) {
					spectrumEvent_rw.setValue(value);
					my_spectrumS.add(spectrumEvent_rw);
				} else {
					spectrumEvent_ro.setValue(value);
					my_spectrumS.add(spectrumEvent_ro);
				}
				spectrumEvent_ro = null;
				spectrumEvent_rw = null;
				value = null;
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
			String desc = "Failed while executing OracleDataGettersBetweenDates.getAttSpectrumDataBetweenDatesOracle() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(stmt);
			stmt = null;
			dbConn.closeConnection(conn);
			conn = null;
			// Close the connection with the database
			// if ( dbConn.isAutoConnect() )
			// {
			// dbConn.close();
			// }
		}
		return my_spectrumS;

	}

	@Override
	protected Vector getAttImageDataBetweenDatesRequest(Vector my_imageS,
			ResultSet rset, int data_type, boolean isBothReadAndWrite) {
		// TODO Auto-generated method stub
		try {
			IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
			while (rset.next()) {
				ImageEvent_RO imageEvent_ro = new ImageEvent_RO();

				try {
					imageEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset
							.getString(1)));
				} catch (Exception e) {
				}

				int dim_x = rset.getInt(2);
				int dim_y = rset.getInt(3);
				imageEvent_ro.setDim_x(dim_x);
				imageEvent_ro.setDim_y(dim_y);

				Clob readClob = null;
				String readString = null;
				if (rset.getObject(3) != null) {
					readClob = rset.getClob(3);
					if (rset.wasNull()) {
						readString = "null";
					} else {
						readString = readClob.getSubString(1, (int) readClob
								.length());
					}
				}

				if (isBothReadAndWrite) {
					// TODO : images RW not supported
				} else {
					imageEvent_ro.setValue(dbUtils.getImageValue(readString,
							data_type));
					my_imageS.add(imageEvent_ro);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return my_imageS;
	}

}
