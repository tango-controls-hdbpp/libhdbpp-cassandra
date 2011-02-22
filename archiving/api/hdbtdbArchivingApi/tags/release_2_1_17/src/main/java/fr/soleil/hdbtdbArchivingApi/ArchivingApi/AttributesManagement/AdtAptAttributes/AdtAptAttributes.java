/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeDomains;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeFamilies;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeIds;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeMembers;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeNames;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeProperties;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeHeavy;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLight;

/**
 * @author AYADI
 *
 */
public abstract class AdtAptAttributes implements IAdtAptAttributes {
	protected int arch_type;

	public AttributeFamilies families;
	public AttributeMembers members;
	public AttributeNames names;
	public AttributeDomains domains;
	public AttributeIds ids;
	public AttributeProperties properties;

	public AdtAptAttributes(int type) {

		families = new AttributeFamilies(type);
		members = new AttributeMembers(type);
		names = new AttributeNames(type);
		domains = new AttributeDomains(type);
		ids = new AttributeIds(type);
		properties = new AttributeProperties(type);
		this.arch_type = type;
	}

	abstract public void registerAttribute(AttributeHeavy attributeHeavy)
			throws ArchivingException;

	public abstract void CreateAttributeTableIfNotExist(String attributeName,
			AttributeHeavy attr) throws ArchivingException;

	abstract protected String getRequest(String att_name);

	/**
	 * This methods retreives some informations associated to the given
	 * attribute, builds an AttributeLight and returns it.
	 *
	 * @param att_name
	 *            attribute name
	 * @return an AttributeLight object (built with the retrieved informations).
	 * @throws ArchivingException
	 */
	public AttributeLight getAttributeLightInfo(String att_name)
			throws SQLException, ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;

		AttributeLight attributeLight = new AttributeLight(att_name);
		int attributeTypeInfo = 0;
		int attributeFormatInfo = 0;
		int attributeWritableInfo = 0;
		ResultSet rset = null;

		Connection conn = null;
		PreparedStatement ps_get_attributeLight_info = null;
		String table_name = dbConn.getSchema() + "." + ConfigConst.TABS[0];
		String field_fullName = ConfigConst.TAB_DEF[2];
		String field_type = ConfigConst.TAB_DEF[8];
		String field_format = ConfigConst.TAB_DEF[9];
		String field_writable = ConfigConst.TAB_DEF[10];

		// My statement
		String selectString = "SELECT " + field_type + ", " + field_format
				+ ", " + field_writable + " FROM " + table_name +
				// " WHERE " + field_fullName + " LIKE ?";
				" WHERE " + field_fullName + " = ?";
		conn = dbConn.getConnection();
		ps_get_attributeLight_info = conn.prepareStatement(selectString);
		dbConn.setLastStatement(ps_get_attributeLight_info);
		try {
			String field1 = att_name.trim();
			ps_get_attributeLight_info.setString(1, field1);
			rset = ps_get_attributeLight_info.executeQuery();
			// Gets the result of the query
			if (rset.next()) {
				attributeTypeInfo = rset.getInt(1);
				attributeFormatInfo = rset.getInt(2);
				attributeWritableInfo = rset.getInt(3);
			}
			attributeLight.setData_type(attributeTypeInfo);
			attributeLight.setData_format(attributeFormatInfo);
			attributeLight.setWritable(attributeWritableInfo);
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
			String desc = "Failed while executing AdtAptAttributes.getAttributeLightInfo() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(ps_get_attributeLight_info);
			ps_get_attributeLight_info = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (ps_get_attributeLight_info != null)
//					ConnectionCommands.close(ps_get_attributeLight_info);
//				if (conn != null)
//					dbConn.closeConnection(conn);
//			} catch (SQLException e) {
//				dbConn.closeConnection(conn);
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return attributeLight;
	}

	/**
	 * <b>Description : </b> Returns an array containing the differents
	 * definition informations for the given attribute
	 *
	 * @param att_name
	 *            The attribute's name
	 * @return An array containing the differents definition informations for
	 *         the given attribute
	 * @throws ArchivingException
	 */

	public Vector<String> getAttDefinitionData(String att_name)
			throws ArchivingException {
		Vector<String> definitionsList = new Vector<String>();
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return definitionsList;

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// First connect with the database
		// if ( dbConn.isAutoConnect() )
		// dbConn.connect();
		// Create and execute the SQL query string
		String sqlStr = "";
		sqlStr = getRequest(att_name);

		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(sqlStr);
			// Gets the result of the query
			while (rset.next())
				for (int i = 0; i < ConfigConst.TAB_DEF.length; i++) {
					String info = ConfigConst.TAB_DEF[i] + "::"
							+ rset.getString(i + 1);
					definitionsList.addElement(info);
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
			String desc = "Failed while executing AdtAptAttributes.getAttDefinitionData() method...";
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
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				dbConn.closeConnection(conn);
//				e.printStackTrace();
//			}
		}
		// Close the connection with the database
		// if ( dbConn.isAutoConnect() )
		// dbConn.close();
		// Returns the names list
		return definitionsList;

	}

	/**
	 * <b>Description : </b> Checks if the attribute of the given name, is
	 * already registered in <I>HDB</I> (and more particularly in the table of
	 * the definitions).
	 *
	 * @param att_name
	 *            The name of the attribute to check.
	 * @return boolean
	 * @throws ArchivingException
	 */
	public boolean isRegisteredADT(String att_name) throws ArchivingException {
		int id = ids.getAttID(att_name.trim());

		if (id != 0) {
			return true;
		} else
			return false;
	}

	/**
	 * Returns the tango format of a given attribute
	 *
	 * @param att_name
	 * @return An int that describe the format of the given attribute.
	 * @throws ArchivingException
	 */
	public int getAttDataFormat(String att_name) throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return 0;

		int dataFormat = 0;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String get_DataFormatQuery = "";
		String select_field = ConfigConst.TABS[0] + "."
				+ ConfigConst.TAB_DEF[9];
		String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
		// String clause_1 = ConfigConst.TABS[ 0 ] + "." + ConfigConst.TAB_DEF[
		// 2 ] + " LIKE " + "?";
		String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2]
				+ "=" + "?" + "";
		get_DataFormatQuery = "SELECT " + select_field + " FROM " + table_1
				+ " WHERE (" + clause_1 + ") ";

		try {
			conn = dbConn.getConnection();
			if (conn == null) {return dataFormat;}
			preparedStatement = conn.prepareStatement(get_DataFormatQuery);
			dbConn.setLastStatement(preparedStatement);
			preparedStatement.setString(1, att_name.trim());
			rset = preparedStatement.executeQuery();
			while (rset.next()) {
				dataFormat = rset.getInt(1);
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
			String desc = "Failed while executing AdtAptAttributes.getAttDataFormat() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(preparedStatement);
			preparedStatement = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (preparedStatement != null)
//					ConnectionCommands.close(preparedStatement);
//				if (conn != null)
//					dbConn.closeConnection(conn);
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
//				String desc = "Failed while executing AdtAptAttributes.getAttDataFormat() method...";
//				throw new ArchivingException(message, reason, ErrSeverity.WARN,
//						desc, this.getClass().getName(), e);
//			}
		}
		// Returns the names list
		return dataFormat;
	}

	/**
	 * Returns the tango type of a given attribute
	 *
	 * @param att_name
	 * @return An int containing that describe the type of the given attribute.
	 * @throws ArchivingException
	 */
	public int getAttDataType(String att_name) throws SQLException,
			ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return 0;

		Connection conn = null;
		if ((conn = dbConn.getConnection()) == null)
			return 0;
		int dataType = 0;

		PreparedStatement preparedStatement = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String get_DataTypeQuery = "";
		String select_field = "";
		select_field = select_field + ConfigConst.TABS[0] + "."
				+ ConfigConst.TAB_DEF[8];

		String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
		String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2]
				+ " = " + "?";

		get_DataTypeQuery = "SELECT " + select_field + " FROM " + table_1
				+ " WHERE (" + clause_1 + ") ";
		preparedStatement = conn.prepareStatement(get_DataTypeQuery);
		dbConn.setLastStatement(preparedStatement);
		try {
			preparedStatement.setString(1, att_name.trim());
			rset = preparedStatement.executeQuery();
			// if (dbConn.isCanceled()) return 0;
			while (rset.next()) {
				dataType = rset.getInt(1);
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
			String desc = "Failed while executing AdtAptAttributes.getAttDataType() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(preparedStatement);
			preparedStatement = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (preparedStatement != null)
//					ConnectionCommands.close(preparedStatement);
//				if (conn != null)
//					dbConn.closeConnection(conn);
//			} catch (SQLException e) {
//				dbConn.closeConnection(conn);
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		// Returns the names list
		return dataType;
	}

	/**
	 * Returns the tango writable parameter for a given attribute
	 *
	 * @param att_name
	 * @return An int that is the writable value of the given attribute.
	 * @throws ArchivingException
	 */
	public int getAttDataWritable(String att_name) throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return 0;

		int dataWritable = 0;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String get_DataWritableQuery = "";
		String select_field = ConfigConst.TABS[0] + "."
				+ ConfigConst.TAB_DEF[10];
		String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
		String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2]
				+ " = " + "?";
		get_DataWritableQuery = "SELECT " + select_field + " FROM " + table_1
				+ " WHERE (" + clause_1 + ") ";
		try {
			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(get_DataWritableQuery);
			dbConn.setLastStatement(preparedStatement);
			preparedStatement.setString(1, att_name.trim());
			rset = preparedStatement.executeQuery();
			while (rset.next()) {
				dataWritable = rset.getInt(1);
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
			String desc = "Failed while executing AdtAptAttributes.getAttDataWritable() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);

		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(preparedStatement);
			preparedStatement = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (preparedStatement != null)
//					ConnectionCommands.close(preparedStatement);
//				if (conn != null)
//					dbConn.closeConnection(conn);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				dbConn.closeConnection(conn);
//				e.printStackTrace();
//			}
		}
		// Returns the names list
		return dataWritable;
	}

	/**
	 * Returns the type, the format and the writable property of a given
	 * attribute
	 *
	 * @param att_name
	 * @return An array (int) containing the type, the format and the writable
	 *         property of the given attribute.
	 * @throws ArchivingException
	 */
	public int[] getAtt_TFW_Data(String att_name) throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;

		int[] TFW_Data = new int[3];

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String get_TFW_DataQuery = "";
		String select_field = "";
		select_field = select_field + ConfigConst.TABS[0] + "."
				+ ConfigConst.TAB_DEF[8] + ", " + ConfigConst.TABS[0] + "."
				+ ConfigConst.TAB_DEF[9] + ", " + ConfigConst.TABS[0] + "."
				+ ConfigConst.TAB_DEF[10];

		String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
		String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2]
				+ " = " + "?";

		get_TFW_DataQuery = "SELECT " + select_field + " FROM " + table_1
				+ " WHERE (" + clause_1 + ") ";
		try {
			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(get_TFW_DataQuery);
			dbConn.setLastStatement(preparedStatement);
			preparedStatement.setString(1, att_name.trim());
			rset = preparedStatement.executeQuery();
			while (rset.next()) {
				TFW_Data[0] = rset.getInt(ConfigConst.TAB_DEF[8]);
				TFW_Data[1] = rset.getInt(ConfigConst.TAB_DEF[9]);
				TFW_Data[2] = rset.getInt(ConfigConst.TAB_DEF[10]);
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
			String desc = "Failed while executing AdtAptAttributes.getAtt_TFW_Data() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
			ConnectionCommands.close(preparedStatement);
			preparedStatement = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				if (rset != null)
//					ConnectionCommands.close(rset);
//				if (preparedStatement != null)
//					ConnectionCommands.close(preparedStatement);
//				if (conn != null)
//					dbConn.closeConnection(conn);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				dbConn.closeConnection(conn);
//				e.printStackTrace();
//			}
		}
		// Returns the names list
		return TFW_Data;
	}

	/**
	 * <b>Description : </b> Retrieves the number of records for a given
	 * attribute.
	 *
	 * @param att_name
	 *            The attribute's name.
	 * @return The record's number (int)
	 * @throws ArchivingException
	 */
	public int getAttRecordCount(String att_name) throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return 0;

		int count = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		getAttributeDataQuery = "SELECT COUNT(*)"
				+ " FROM "
				+ dbConn.getSchema()
				+ "."
				+ DbUtilsFactory.getInstance(arch_type).getTableName(
						att_name.trim());
		try {
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getAttributeDataQuery);
			while (rset.next()) {
				count = rset.getInt(1);
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
			String desc = "Failed while executing AdtAptAttributes.getAttRecordCount() method...";
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
//				String desc = "Failed while executing AdtAptAttributes.getAttRecordCount() method...";
//				throw new ArchivingException(message, reason, ErrSeverity.WARN,
//						desc, this.getClass().getName(), e);
//			}
			// Close the connection with the database
			// if ( dbConn.isAutoConnect() )
			// {
			// dbConn.close();
			// }
		}

		return count;
	}

	public AttributeFamilies getFamilies() {
		return families;
	}

	public AttributeMembers getMembers() {
		return members;
	}

	public AttributeNames getNames() {
		return names;
	}

	public AttributeDomains getDomains() {
		return domains;
	}

	public AttributeIds getIds() {
		return ids;
	}

	public AttributeProperties getProperties() {
		return properties;
	}

}
