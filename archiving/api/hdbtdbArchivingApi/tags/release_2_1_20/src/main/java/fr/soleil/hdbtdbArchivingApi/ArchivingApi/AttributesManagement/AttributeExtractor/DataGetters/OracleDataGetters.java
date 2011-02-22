/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

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
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

/**
 * @author AYADI
 *
 */
public class OracleDataGetters extends DataGetters {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public OracleDataGetters(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.
	 * AttributeExtractor
	 * .DataGetters#treatStatementResultForGetSpectData(java.sql.ResultSet,
	 * boolean, int, java.util.Vector)
	 */
	@Override
	protected Vector treatStatementResultForGetSpectData(ResultSet rset,
			boolean isBothReadAndWrite, int data_type, Vector my_spectrumS) {
		// TODO Auto-generated method stub
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbUtils == null)
			return null;

		try {
			while (rset.next()) {
				// System.out.println (
				// "CLA/getAttSpectrumData2/--------------7" );
				SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
				SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
				// Timestamp
				String rawDate = rset.getString(1);
				long milliDate;

				try {
					milliDate = DateUtil.stringToMilli(rawDate);
				} catch (Exception e) {
					e.printStackTrace();

					String _reason = "FAILED TO PARSE DATE|" + rawDate + "|";
					String message = _reason;
					String _desc = "Failed while executing OracleDataGetters.getAttSpectrumData() method...";
					throw new ArchivingException(message, _reason,
							ErrSeverity.WARN, _desc, this.getClass().getName(),
							e);
				}

				spectrumEvent_ro.setTimeStamp(milliDate);
				spectrumEvent_rw.setTimeStamp(milliDate);

				// Dim
				int dim_x = 0;
				try {
					dim_x = rset.getInt(2);
				} catch (Exception e) {
				}
				spectrumEvent_ro.setDim_x(dim_x);
				spectrumEvent_rw.setDim_x(dim_x);
				// Value
				Clob readClob = null;
				String readString = null;

				try {
					readClob = rset.getClob(3);
					if (rset.wasNull()) {
						readString = "null";
					} else {
						readString = readClob.getSubString(1, (int) readClob
								.length());
					}
				} catch (Exception e) {
				}

				Clob writeClob = null;
				String writeString = null;
				if (isBothReadAndWrite) {
					try {
						writeClob = rset.getClob(4);
						if (rset.wasNull()) {
							writeString = "null";
						} else {
							writeString = writeClob.getSubString(1,
									(int) writeClob.length());
						}
					} catch (Exception e) {
					}
				}

				Object value = dbUtils.getSpectrumValue(readString,
						writeString, data_type);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return my_spectrumS;
	}

	@Override
	protected Vector treatStatementResultForGetImageData(ResultSet rset,
			boolean isBothReadAndWrite, int data_type, Vector my_ImageS) {
		// TODO Auto-generated method stub
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbUtils == null)
			return null;

		try {
			while (rset.next()) {
				// System.out.println (
				// "CLA/getAttSpectrumData2/--------------7" );
				ImageEvent_RO imageEvent_ro = new ImageEvent_RO();
				// SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
				// Timestamp
				try {
					imageEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset
							.getString(1)));
				} catch (Exception e) {
				}
				// spectrumEvent_rw.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));

				// Dim
				int dim_x = rset.getInt(2);
				int dim_y = rset.getInt(3);
				// System.out.println (
				// "CLA/getAttSpectrumData/dim_x/"+dim_x+"/" );
				imageEvent_ro.setDim_x(dim_x);
				imageEvent_ro.setDim_y(dim_y);
				// spectrumEvent_rw.setDim_x(dim_x);
				// Value
				String readString;
				Clob readClob = rset.getClob(4);
				if (rset.wasNull()) {
					readString = "null";
				} else {
					readString = readClob.getSubString(1, (int) readClob
							.length());
				}

				if (isBothReadAndWrite) {
					// TODO: not supported yet
				} else {
					imageEvent_ro.setValue(dbUtils.getImageValue(readString,
							data_type));
					my_ImageS.add(imageEvent_ro);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return my_ImageS;
	}

	@Override
	protected String getDbScalarLast_nRequest(String tableName,
			String att_name, int number, boolean ro_fields, String fields)
			throws ArchivingException {
		// TODO Auto-generated method stub
		String orderField = (ro_fields ? ConfigConst.TAB_SCALAR_RO[0]
				: ConfigConst.TAB_SCALAR_RW[0]);

		return "SELECT " + fields + " FROM " + "(" + "SELECT * FROM "
				+ tableName + " ORDER BY " + orderField + " DESC" + ")"
				+ " WHERE rownum <= " + number + " ORDER BY  " + orderField
				+ " ASC";

	}

	@Override
	public Vector getAttSpectrumDataLast_n(String att_name, int number,
			int writable) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbConn == null || att == null || dbUtils == null)
			return null;

		Vector tmpSpectrumVect = new Vector();

		int data_type = att.getAtt_TFW_Data(att_name)[0];
		boolean ro_fields = (writable == AttrWriteType._READ || writable == AttrWriteType._WRITE);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string

		String tableName = dbConn.getSchema() + "."
				+ dbUtils.getTableName(att_name);
		String selectField_0, selectField_1, selectField_2, selectField_3 = null, selectFields, orderField;
		if (ro_fields) {

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
		orderField = (ro_fields ? ConfigConst.TAB_SPECTRUM_RO[0]
				: ConfigConst.TAB_SPECTRUM_RW[0]);
		String whereClause = "rownum" + " <= " + number + " ORDER BY  "
				+ orderField + " ASC";

		String query1 = "SELECT * FROM " + tableName + " ORDER BY "
				+ selectField_0 + " DESC";
		tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name)
				+ " T";
		if (ro_fields) {
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
		if (!ro_fields) {
			selectFields += ", " + selectField_3;
		}

		String query = "SELECT " + selectFields + " FROM (" + query1
				+ ") T WHERE " + whereClause;
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(query);
			while (rset.next()) {
				// System.out.println (
				// "CLA/getAttSpectrumData2/--------------7" );
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
				Clob readClob = null;
				String readString = null;

				try {
					readClob = rset.getClob(3);
					if (rset.wasNull()) {
						readString = "null";
					} else {
						readString = readClob.getSubString(1, (int) readClob
								.length());
					}
				} catch (Exception e) {
				}

				Clob writeClob = null;
				String writeString = null;
				if (!ro_fields) {
					try {
						writeClob = rset.getClob(4);
						if (rset.wasNull()) {
							writeString = "null";
						} else {
							writeString = writeClob.getSubString(1,
									(int) writeClob.length());
						}
					} catch (Exception e) {
					}
				}

				Object value = dbUtils.getSpectrumValue(readString,
						writeString, data_type);
				if (!ro_fields) {
					spectrumEvent_rw.setValue(value);
					tmpSpectrumVect.add(spectrumEvent_rw);
				} else {
					spectrumEvent_ro.setValue(value);
					tmpSpectrumVect.add(spectrumEvent_ro);
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
			String desc = "Failed while executing OracleDataGetters.getAttSpectrumDataLast_n() method..."
					+ "\r\n" + query;
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
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (stmt != null)
//					ConnectionCommands.close(stmt);
//				if (conn != null)
//					dbConn.closeConnection(conn);
//
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				dbConn.closeConnection(conn);
//				e.printStackTrace();
//			}

		}
		return tmpSpectrumVect;

	}

	@Override
	public void buildAttributeTab(String tableName, int data_type,
			int data_format, int writable) throws ArchivingException {
		// TODO Auto-generated method stub

	}

}
