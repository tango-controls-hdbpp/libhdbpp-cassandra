package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

public abstract class HdbAttributeInsert implements IHdbAttributeInsert {
	// protected IDBConnection dbConn;
	int arch_type;
	protected IAdtAptAttributes att;

	public HdbAttributeInsert(int type, IAdtAptAttributes at) {
		// TODO Auto-generated constructor stub
		// this.dbConn = con;
		arch_type = type;
		this.att = at;

	}

	abstract protected void insert_SpectrumData_RO_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalue, StringBuffer valueStr,
			String att_name) throws ArchivingException;

	abstract protected void insert_SpectrumData_RW_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalueWrite, Double[] dvalueRead,
			StringBuffer valueWriteStr, StringBuffer valueReadStr,
			String att_name) throws ArchivingException;

	abstract protected void insert_ImageData_RO_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			int dim_y, Timestamp timeSt, Double[][] dvalue,
			StringBuffer valueStr, String att_name) throws ArchivingException;

	/**
	 * 
	 * @param scalarEvent
	 * @throws ArchivingException
	 */

	public void insert_ScalarData(ScalarEvent scalarEvent)
			throws ArchivingException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		try {
			if (dbConn == null)
				return;
			String _attributeName = scalarEvent.getAttribute_complete_name()
					.trim();
			StringBuffer _query;
			StringBuffer _tableName;
			StringBuffer _tableFields;
			String _selectFields = "";
			int _writable = scalarEvent.getWritable();

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

			_tableName = new StringBuffer().append(dbConn.getSchema()).append(
					".").append(dbUtils.getTableName(_attributeName));

			_query = new StringBuffer();
			_query.append("INSERT INTO ").append(_tableName).append(" (")
					.append(_tableFields).append(")").append(" VALUES").append(
							" (").append(_selectFields).append(")");

			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(_query.toString());
			dbConn.setLastStatement(preparedStatement);
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

			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing DataBaseApi.insertScalarDataROOracle() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} catch (ArchivingException e) {
			throw e;
		} finally {
			try {
				ConnectionCommands.close(preparedStatement);
				dbConn.closeConnection(conn);
			} catch (Exception e) {
				dbConn.closeConnection(conn);
			}
		}
	}

	/**
	 * <b>Description : </b> Inserts a spectrum type attribute's data
	 * 
	 * @param hdbSpectrumEvent_RO
	 *            an object that contains the attribute's name, the timestamp's
	 *            value and the value.
	 */
	public void insert_SpectrumData_RO(SpectrumEvent_RO hdbSpectrumEvent_RO)
			throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		String att_name = hdbSpectrumEvent_RO.getAttribute_complete_name()
				.trim();
		StringBuffer tableName = new StringBuffer().append(dbConn.getSchema())
				.append(".").append(dbUtils.getTableName(att_name));
		long time = hdbSpectrumEvent_RO.getTimeStamp();
		Timestamp timeSt = new Timestamp(time);
		int dim_x = hdbSpectrumEvent_RO.getDim_x();
		int data_type = -1;
		Double[] dvalue = null;
		Float[] fvalue = null;
		Integer[] lvalue = null;
		Short[] svalue = null;
		Byte[] cvalue = null;
		Boolean[] bvalue = null;
		String[] stvalue = null;
		if (hdbSpectrumEvent_RO.getValue() instanceof Double[]) {
			dvalue = (Double[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_DOUBLE;
		} else if (hdbSpectrumEvent_RO.getValue() instanceof Float[]) {
			fvalue = (Float[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_FLOAT;
		} else if (hdbSpectrumEvent_RO.getValue() instanceof Integer[]) {
			lvalue = (Integer[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_LONG;
		} else if (hdbSpectrumEvent_RO.getValue() instanceof Short[]) {
			svalue = (Short[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_SHORT;
		} else if (hdbSpectrumEvent_RO.getValue() instanceof Byte[]) {
			cvalue = (Byte[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_CHAR;
		} else if (hdbSpectrumEvent_RO.getValue() instanceof Boolean[]) {
			bvalue = (Boolean[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_BOOLEAN;
		} else if (hdbSpectrumEvent_RO.getValue() instanceof String[]) {
			stvalue = (String[]) hdbSpectrumEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_STRING;
		}
		StringBuffer valueStr = new StringBuffer();
		StringBuffer query = new StringBuffer();

		// First connect with the database
		// if ( dbConn.isAutoConnect() )
		// dbConn.connect();

		// Create and execute the SQL query string
		// Build the query string
		StringBuffer tableFields = new StringBuffer().append(
				ConfigConst.TAB_SPECTRUM_RO[0]).append(", ").append(
				ConfigConst.TAB_SPECTRUM_RO[1]).append(", ").append(
				ConfigConst.TAB_SPECTRUM_RO[2]);
		switch (data_type) {
		case TangoConst.Tango_DEV_DOUBLE:
			if (dvalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < dvalue.length - 1; i++) {
					valueStr.append(dvalue[i]).append(
							GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (dvalue.length > 0) {
					valueStr.append(dvalue[dvalue.length - 1]);
				}
			}
			break;

		case TangoConst.Tango_DEV_FLOAT:
			if (fvalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < fvalue.length - 1; i++) {
					valueStr.append(fvalue[i]).append(
							GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (fvalue.length > 0) {
					valueStr.append(fvalue[fvalue.length - 1]);
				}
			}
			break;

		case TangoConst.Tango_DEV_LONG:
			if (lvalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < lvalue.length - 1; i++) {
					valueStr.append(lvalue[i]).append(
							GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (lvalue.length > 0) {
					valueStr.append(lvalue[lvalue.length - 1]);
				}
			}
			break;

		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			if (svalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < svalue.length - 1; i++) {
					valueStr.append(svalue[i]).append(
							GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (svalue.length > 0) {
					valueStr.append(svalue[svalue.length - 1]);
				}
			}
			break;

		case TangoConst.Tango_DEV_CHAR:
			if (cvalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < cvalue.length - 1; i++) {
					valueStr.append(cvalue[i]).append(
							GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (cvalue.length > 0) {
					valueStr.append(cvalue[cvalue.length - 1]);
				}
			}
			break;

		case TangoConst.Tango_DEV_BOOLEAN:
			if (bvalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < bvalue.length - 1; i++) {
					valueStr.append(bvalue[i]).append(
							GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (bvalue.length > 0) {
					valueStr.append(bvalue[bvalue.length - 1]);
				}
			}
			break;

		case TangoConst.Tango_DEV_STRING:
			if (stvalue == null)
				valueStr = null;
			else {
				for (int i = 0; i < stvalue.length - 1; i++) {
					valueStr.append(
							StringFormater.formatStringToWrite(stvalue[i]))
							.append(GlobalConst.CLOB_SEPARATOR).append(" ");
				}
				if (stvalue.length > 0) {
					valueStr.append(StringFormater
							.formatStringToWrite(stvalue[stvalue.length - 1]));
				}
			}
			break;
		}
		insert_SpectrumData_RO_DataBase(query, tableName, tableFields, dim_x,
				timeSt, dvalue, valueStr, att_name);

		// Close the connection with the database
		// if ( dbConn.isAutoConnect() )
		// dbConn.close();
	}

	/**
	 * 
	 * @param hdbSpectrumEvent_RW
	 * @throws ArchivingException
	 */
	public void insert_SpectrumData_RW(SpectrumEvent_RW hdbSpectrumEvent_RW)
			throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		String att_name = hdbSpectrumEvent_RW.getAttribute_complete_name()
				.trim();
		StringBuffer tableName = new StringBuffer().append(dbConn.getSchema())
				.append(".").append(dbUtils.getTableName(att_name));
		long time = hdbSpectrumEvent_RW.getTimeStamp();
		Timestamp timeSt = new Timestamp(time);
		int dim_x = hdbSpectrumEvent_RW.getDim_x();
		// System.out.println ( "CLA/insert_SpectrumData_RW/dim_x/"+dim_x );
		int data_type = -1;
		Double[] dvalueRead = null, dvalueWrite = null;
		Float[] fvalueRead = null, fvalueWrite = null;
		Integer[] lvalueRead = null, lvalueWrite = null;
		Short[] svalueRead = null, svalueWrite = null;
		Byte[] cvalueRead = null, cvalueWrite = null;
		Boolean[] bvalueRead = null, bvalueWrite = null;
		String[] stvalueRead = null, stvalueWrite = null;

		if (hdbSpectrumEvent_RW.getValue() instanceof Double[]) {
			dvalueRead = (Double[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWRead();
			dvalueWrite = (Double[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_DOUBLE;

		} else if (hdbSpectrumEvent_RW.getValue() instanceof Float[]) {
			fvalueRead = (Float[]) hdbSpectrumEvent_RW.getSpectrumValueRWRead();
			fvalueWrite = (Float[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_FLOAT;

		} else if (hdbSpectrumEvent_RW.getValue() instanceof Integer[]) {
			lvalueRead = (Integer[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWRead();
			lvalueWrite = (Integer[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_LONG;

		} else if (hdbSpectrumEvent_RW.getValue() instanceof Short[]) {
			svalueRead = (Short[]) hdbSpectrumEvent_RW.getSpectrumValueRWRead();
			svalueWrite = (Short[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_SHORT;

		} else if (hdbSpectrumEvent_RW.getValue() instanceof Byte[]) {
			cvalueRead = (Byte[]) hdbSpectrumEvent_RW.getSpectrumValueRWRead();
			cvalueWrite = (Byte[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_CHAR;

		} else if (hdbSpectrumEvent_RW.getValue() instanceof Boolean[]) {
			bvalueRead = (Boolean[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWRead();
			bvalueWrite = (Boolean[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_BOOLEAN;

		} else if (hdbSpectrumEvent_RW.getValue() instanceof String[]) {
			stvalueRead = (String[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWRead();
			stvalueWrite = (String[]) hdbSpectrumEvent_RW
					.getSpectrumValueRWWrite();
			data_type = TangoConst.Tango_DEV_STRING;

		}

		StringBuffer valueReadStr = new StringBuffer();
		StringBuffer valueWriteStr = new StringBuffer();
		StringBuffer query = new StringBuffer();

		switch (data_type) {
		case TangoConst.Tango_DEV_DOUBLE:
			if (dvalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < dvalueRead.length; i++) {
					valueReadStr.append(dvalueRead[i]);
					if (i < dvalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (dvalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < dvalueWrite.length; i++) {
					valueWriteStr.append(dvalueWrite[i]);
					if (i < dvalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}
			break;

		case TangoConst.Tango_DEV_FLOAT:
			if (fvalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < fvalueRead.length; i++) {
					valueReadStr.append(fvalueRead[i]);
					if (i < fvalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (fvalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < fvalueWrite.length; i++) {
					valueWriteStr.append(fvalueWrite[i]);
					if (i < fvalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}
			break;

		case TangoConst.Tango_DEV_LONG:
			if (lvalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < lvalueRead.length; i++) {
					valueReadStr.append(lvalueRead[i]);
					if (i < lvalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (lvalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < lvalueWrite.length; i++) {
					valueWriteStr.append(lvalueWrite[i]);
					if (i < lvalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}
			break;

		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			if (svalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < svalueRead.length; i++) {
					valueReadStr.append(svalueRead[i]);
					if (i < svalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (svalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < svalueWrite.length; i++) {
					valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
							.append(" ");
					if (i < svalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}
			break;

		case TangoConst.Tango_DEV_CHAR:
			if (cvalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < cvalueRead.length; i++) {
					valueReadStr.append(cvalueRead[i]);
					if (i < cvalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (cvalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < cvalueWrite.length; i++) {
					valueWriteStr.append(cvalueWrite[i]);
					if (i < cvalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}

			break;

		case TangoConst.Tango_DEV_BOOLEAN:
			if (bvalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < bvalueRead.length; i++) {
					valueReadStr.append(bvalueRead[i]);
					if (i < bvalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (bvalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < bvalueWrite.length; i++) {
					valueWriteStr.append(bvalueWrite[i]);
					if (i < bvalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}

			break;

		case TangoConst.Tango_DEV_STRING:
			if (stvalueRead == null)
				valueReadStr = null;
			else {
				for (int i = 0; i < stvalueRead.length; i++) {
					valueReadStr.append(StringFormater
							.formatStringToWrite(stvalueRead[i]));
					if (i < stvalueRead.length - 1) {
						valueReadStr.append(GlobalConst.CLOB_SEPARATOR).append(
								" ");
					}
				}
			}

			if (stvalueWrite == null)
				valueWriteStr = null;
			else {
				for (int i = 0; i < stvalueWrite.length; i++) {
					valueWriteStr.append(stvalueWrite[i]);
					if (i < stvalueWrite.length - 1) {
						valueWriteStr.append(GlobalConst.CLOB_SEPARATOR)
								.append(" ");
					}
				}
			}
			break;
		}

		// First connect with the database
		// if ( dbConn.isAutoConnect() )
		// dbConn.connect();

		// Create and execute the SQL query string
		// Build the query string
		StringBuffer tableFields = new StringBuffer().append(
				ConfigConst.TAB_SPECTRUM_RW[0]).append(", ").append(
				ConfigConst.TAB_SPECTRUM_RW[1]).append(", ").append(
				ConfigConst.TAB_SPECTRUM_RW[2]).append(", ").append(
				ConfigConst.TAB_SPECTRUM_RW[3]);
		insert_SpectrumData_RW_DataBase(query, tableName, tableFields, dim_x,
				timeSt, dvalueWrite, dvalueRead, valueWriteStr, valueReadStr,
				att_name);

		// Close the connection with the database
		// if ( dbConn.isAutoConnect() )
		// dbConn.close();
	}

	/**
	 * <b>Description : </b> Inserts an image type attribute's data
	 * 
	 * @param imageEvent_RO
	 *            an object that contains the attribute's name, the timestamp's
	 *            value and the value.
	 * @throws ArchivingException
	 */
	public void insert_ImageData_RO(ImageEvent_RO imageEvent_RO)
			throws SQLException, IOException, ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbConn == null)
			return;
		String att_name = imageEvent_RO.getAttribute_complete_name().trim();
		StringBuffer tableName = new StringBuffer().append(dbConn.getSchema())
				.append(".").append(dbUtils.getTableName(att_name));
		long time = imageEvent_RO.getTimeStamp();
		Timestamp timeSt = new Timestamp(time);

		int dim_x = imageEvent_RO.getDim_x();
		int dim_y = imageEvent_RO.getDim_y();

		int data_type = -1;

		// System.out.println("DataBaseApi.insert_ImageData_RO/dim_x/"+dim_x+"/dim_y/"+dim_y);

		Double[][] dvalue = null;

		if (imageEvent_RO.getValue() instanceof Double[][]) {
			dvalue = (Double[][]) imageEvent_RO.getValue();
			data_type = TangoConst.Tango_DEV_DOUBLE;
		} else {
			return;
		}

		StringBuffer valueStr = new StringBuffer();
		StringBuffer query = new StringBuffer();

		// First connect with the database
		// if ( dbConn.isAutoConnect() )
		// {
		// dbConn.connect();
		// }

		// Create and execute the SQL query string
		// Build the query string
		StringBuffer tableFields = new StringBuffer().append(
				ConfigConst.TAB_IMAGE_RO[0]).append(", ").append(
				ConfigConst.TAB_IMAGE_RO[1]).append(", ").append(
				ConfigConst.TAB_IMAGE_RO[2]).append(", ").append(
				ConfigConst.TAB_IMAGE_RO[3]);
		if (dvalue != null) {
			switch (data_type) {
			case TangoConst.Tango_DEV_DOUBLE:
				for (int i = 0; i < dim_y; i++) {
					for (int j = 0; j < dim_x; j++) {
						valueStr.append(dvalue[i][j]);
						if (j < dim_x - 1) {
							valueStr.append(
									GlobalConst.CLOB_SEPARATOR_IMAGE_COLS)
									.append(" ");
						}
					}

					if (i < dim_y - 1) {
						valueStr.append(GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS)
								.append(" ");
					}
				}
				break;
			}
		}

		insert_ImageData_RO_DataBase(query, tableName, tableFields, dim_x,
				dim_y, timeSt, dvalue, valueStr, att_name);
		// Close the connection with the database
		// if ( dbConn.isAutoConnect() )
		// {
		// dbConn.close();
		// }
	}

}
