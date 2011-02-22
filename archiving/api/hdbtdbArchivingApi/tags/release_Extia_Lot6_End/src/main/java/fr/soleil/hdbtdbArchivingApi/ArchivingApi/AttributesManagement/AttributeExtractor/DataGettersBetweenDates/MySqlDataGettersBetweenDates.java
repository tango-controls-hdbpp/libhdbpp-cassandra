/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

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
public class MySqlDataGettersBetweenDates extends DataGettersBetweenDates {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public MySqlDataGettersBetweenDates(int type) {
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
		String query = "";
		String fields = "";
		String whereClause = "";
		String groupByClause = "";
		String orderByClause = "";
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

		if (samplingType.hasSampling()) {

			if (!samplingType.hasAdditionalFiltering()) {
				String format = samplingType.getMySqlFormat();
				String groupingNormalisationType = SamplingType
						.getGroupingNormalisationType(ConfigConst.BD_MYSQL);

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
				String format = samplingType.getOneLevelHigherFormat(true);
				// System.out.println (
				// "getAttScalarDataBetweenDates/format|"+format+"|" );
				// String fullFormat = ( SamplingType.getSamplingType (
				// SamplingType.ALL ) ).getFormat ( db_type ==
				// ConfigConst.BD_MYSQL );
				String fullFormat = (SamplingType
						.getSamplingType(SamplingType.SECOND)).getFormat(true);

				String groupingNormalisationType = SamplingType
						.getGroupingNormalisationType(ConfigConst.BD_MYSQL);

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
						true, ConfigConst.TAB_SCALAR_RO[0]);
				orderByClause = " ORDER BY time";

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
			orderByClause = " ORDER BY time";

			query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "("
					+ whereClause + ")" + orderByClause;
		}

		return query;
	}

	@Override
	public Vector getAttSpectrumDataBetweenDates(String att_name,
			String time_0, String time_1, int data_type,
			SamplingType samplingType) throws ArchivingException {
		// TODO Auto-generated method stub
		// System.out.println ( "CLA/getAttSpectrumDataBetweenDatesMySql/" );
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
		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectField_2 = "";
		String selectField_3 = null;
		String selectFields = "";
		String dateClause = "";
		String tableName = "";
		String whereClause = "";
		String groupByClause = "";
		String orderByClause = "";
		tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);

		boolean isBothReadAndWrite = !(writable == AttrWriteType._READ || writable == AttrWriteType._WRITE);

		if (samplingType.hasSampling()) {
			if (!samplingType.hasAdditionalFiltering()) {
				String format = samplingType.getMySqlFormat();
				if (!isBothReadAndWrite) {
					selectField_0 = dbUtils.toDbTimeFieldString(
							ConfigConst.TAB_SPECTRUM_RO[0], format);
					selectField_1 = "AVG (" + ConfigConst.TAB_SPECTRUM_RO[1]
							+ ")";
					selectField_2 = "MIN(CAST("
							+ ConfigConst.TAB_SPECTRUM_RO[2] + " AS CHAR))";
				} else {
					selectField_0 = dbUtils.toDbTimeFieldString(
							ConfigConst.TAB_SPECTRUM_RW[0], format);
					selectField_1 = "AVG (" + ConfigConst.TAB_SPECTRUM_RW[1]
							+ ")";
					selectField_2 = "MIN(CAST("
							+ ConfigConst.TAB_SPECTRUM_RW[2] + " AS CHAR))";
					selectField_3 = "MIN(CAST("
							+ ConfigConst.TAB_SPECTRUM_RW[3] + " AS CHAR))";
				}

				whereClause = ConfigConst.TAB_SPECTRUM_RO[0] + " BETWEEN "
						+ dbUtils.toDbTimeString(time_0.trim()) + " AND "
						+ dbUtils.toDbTimeString(time_1.trim());
				groupByClause = " GROUP BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SPECTRUM_RO[0], format);
				orderByClause = " ORDER BY "
						+ dbUtils.toDbTimeFieldString(
								ConfigConst.TAB_SPECTRUM_RO[0], format);

				selectFields = selectField_0 + "," + selectField_1 + ","
						+ selectField_2;
				if (isBothReadAndWrite) {
					selectFields += "," + selectField_3;
				}
				getAttributeDataQuery = "SELECT " + selectFields + " FROM "
						+ tableName + " WHERE " + whereClause + groupByClause
						+ orderByClause;
			} else {
				String format = samplingType.getOneLevelHigherFormat(true);
				String fullFormat = (SamplingType
						.getSamplingType(SamplingType.SECOND)).getFormat(true);

				String minTime = "MIN(" + ConfigConst.TAB_SCALAR_RO[0] + ")";
				selectField_0 = dbUtils
						.toDbTimeFieldString(minTime, fullFormat);

				if (!isBothReadAndWrite) {
					selectField_1 = "AVG (" + ConfigConst.TAB_SPECTRUM_RO[1]
							+ ")";
					selectField_2 = "MIN(CAST("
							+ ConfigConst.TAB_SPECTRUM_RO[2] + " AS CHAR))";
				} else {
					selectField_1 = "AVG (" + ConfigConst.TAB_SPECTRUM_RW[1]
							+ ")";
					selectField_2 = "MIN(CAST("
							+ ConfigConst.TAB_SPECTRUM_RW[2] + " AS CHAR))";
					selectField_3 = "MIN(CAST("
							+ ConfigConst.TAB_SPECTRUM_RW[3] + " AS CHAR))";
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
						true, ConfigConst.TAB_SCALAR_RO[0]);
				orderByClause = " ORDER BY time";

				getAttributeDataQuery = "SELECT " + selectFields + " FROM "
						+ tableName + " WHERE " + whereClause + groupByClause
						+ orderByClause;
			}
		} else {
			if (!isBothReadAndWrite) {
				selectField_0 = ConfigConst.TAB_SPECTRUM_RO[0];
				selectField_1 = ConfigConst.TAB_SPECTRUM_RO[1];
				selectField_2 = ConfigConst.TAB_SPECTRUM_RO[2];
			} else {
				selectField_0 = ConfigConst.TAB_SPECTRUM_RW[0];
				selectField_1 = ConfigConst.TAB_SPECTRUM_RW[1];
				selectField_2 = ConfigConst.TAB_SPECTRUM_RW[2];
				selectField_3 = ConfigConst.TAB_SPECTRUM_RW[3];
			}
			selectFields = selectField_0 + ", " + selectField_1 + ", "
					+ selectField_2;
			if (isBothReadAndWrite) {
				selectFields += ", " + selectField_3;
			}

			dateClause = selectField_0 + " BETWEEN " + "'" + time_0.trim()
					+ "'" + " AND " + "'" + time_1.trim() + "'";
			getAttributeDataQuery = "SELECT " + selectFields + " FROM "
					+ tableName + " WHERE " + "(" + dateClause + ")"
					+ " ORDER BY time";
		}

		// System.out.println (
		// "CLA/getAttSpectrumDataBetweenDatesMySql/getAttributeDataQuery/"+getAttributeDataQuery
		// );

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getAttributeDataQuery);
			// System.out.println ( "CLA/getAttSpectrumDataBetweenDatesMySql/ 1"
			// );
			while (rset.next()) {
				SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
				SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
				// Timestamp
				try {
					spectrumEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset
							.getString(1)));
					spectrumEvent_rw.setTimeStamp(DateUtil.stringToMilli(rset
							.getString(1)));
				} catch (Exception e) {
				}

				// Dim
				int dim_x = 0;
				try {
					dim_x = rset.getInt(2);
				} catch (Exception e) {
				}
				spectrumEvent_ro.setDim_x(dim_x);
				spectrumEvent_rw.setDim_x(dim_x);
				// Value
				String valueReadSt = null;
				try {
					valueReadSt = rset.getString(3);
					if (rset.wasNull()) {
						valueReadSt = "null";
					}
				} catch (Exception e) {
				}
				String valueWriteSt = null;
				if (isBothReadAndWrite) {
					try {
						valueWriteSt = rset.getString(4);
						if (rset.wasNull()) {
							valueWriteSt = "null";
						}
					} catch (Exception e) {
					}
				}
				Object value = dbUtils.getSpectrumValue(valueReadSt,
						valueWriteSt, data_type);
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
			String desc = "Failed while executing MySqlDataGettersBetweenDates.getAttSpectrumDataBetweenDatesMySql() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(stmt);
			stmt = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				ConnectionCommands.close(rset);
//				ConnectionCommands.close(stmt);
//				dbConn.closeConnection(conn);
//			} catch (SQLException e) {
//				dbConn.closeConnection(conn);
//				e.printStackTrace();
//
//				String message = "";
//				if (e.getMessage().equalsIgnoreCase(
//						GlobalConst.COMM_FAILURE_ORACLE)
//						|| e.getMessage().indexOf(
//								GlobalConst.COMM_FAILURE_MYSQL) != -1)
//					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
//							+ GlobalConst.ADB_CONNECTION_FAILURE;
//				else
//					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
//							+ GlobalConst.STATEMENT_FAILURE;
//
//				String reason = GlobalConst.QUERY_FAILURE;
//				String desc = "Failed while executing MySqlDataGettersBetweenDates.getAttSpectrumDataBetweenDatesMySql() method...";
//				throw new ArchivingException(message, reason, ErrSeverity.WARN,
//						desc, this.getClass().getName(), e);
//			}
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

				String valueReadSt = rset.getString(4);
				if (rset.wasNull()) {
					valueReadSt = "null";
				}

				if (isBothReadAndWrite) {
					// TODO : images RW not supported
				} else {
					imageEvent_ro.setValue(dbUtils.getImageValue(valueReadSt,
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
