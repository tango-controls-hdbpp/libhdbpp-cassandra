/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public class AttributeProperties extends AttributesData {

	/**
	 * @param m
	 */
	public AttributeProperties(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Description : </b> Returns an array containing the differents
	 * properties informations for the given attribute
	 * 
	 * @param att_name
	 *            The attribute's name
	 * @return An array containing the differents properties informations for
	 *         the given attribute
	 * @throws ArchivingException
	 */
	public Vector<String> getAttPropertiesData(String att_name)
			throws ArchivingException {

		Vector<String> properties = new Vector<String>();
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return properties;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String select_field = ConfigConst.TABS[1] + "." + "*";
		String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
		String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[1];
		String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[0]
				+ " = " + ConfigConst.TABS[1] + "." + ConfigConst.TAB_PROP[0];
		String clause_2 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2]
				+ " = " + "?";

		String sqlStr = "SELECT " + select_field + " FROM " + table_1 + ", "
				+ table_2 + " WHERE (" + "(" + clause_1 + ")" + " AND " + "("
				+ clause_2 + ")" + ") ";

		try {
			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(sqlStr);
			dbConn.setLastStatement(preparedStatement);
			preparedStatement.setString(1, att_name.trim());
			rset = preparedStatement.executeQuery();
			// Gets the result of the query
			while (rset.next())
				for (int i = 0; i < ConfigConst.TAB_PROP.length; i++) {
					String info = ConfigConst.TAB_PROP[i] + "::"
							+ rset.getString(i + 1);
					properties.addElement(info);
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
			String desc = "Failed while executing AttributeProperties.getAttPropertiesData() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (preparedStatement != null)
					ConnectionCommands.close(preparedStatement);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				dbConn.closeConnection(conn);
				e.printStackTrace();
			}
		}
		// Returns the names list
		return properties;
	}

	/**
	 * This methos returns the display format for the attribute of the given
	 * name
	 * 
	 * @param att_name
	 *            : the attribut's name
	 * @return the display format for the attribute of the given name
	 * @throws ArchivingException
	 */
	public String getDisplayFormat(String att_name) throws ArchivingException {
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return null;

		String displayFormat = "";
		ResultSet rset = null;
		Connection conn = null;
		PreparedStatement ps_get_att_id = null;
		// My statement
		String selectString = "SELECT " + ConfigConst.TAB_PROP[7] + " FROM "
				+ dbConn.getSchema() + "." + ConfigConst.TABS[0] + ", "
				+ dbConn.getSchema() + "." + ConfigConst.TABS[1] + " WHERE "
				+ ConfigConst.TAB_DEF[2] + " = ?" + " AND "
				+ ConfigConst.TABS[1] + "." + ConfigConst.TAB_PROP[0] + "="
				+ ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[0];
		try {
			conn = dbConn.getConnection();
			ps_get_att_id = conn.prepareStatement(selectString);
			dbConn.setLastStatement(ps_get_att_id);
			String field1 = att_name.trim();
			ps_get_att_id.setString(1, field1);
			rset = ps_get_att_id.executeQuery();
			// Gets the result of the query
			if (rset.next())
				displayFormat = rset.getString(1);

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
			String desc = "Failed while executing AttributeProperties.getDisplayFormat() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);
		} finally {
			try {
				if (rset != null)
					ConnectionCommands.close(rset);
				if (ps_get_att_id != null)
					ConnectionCommands.close(ps_get_att_id);
				if (conn != null)
					dbConn.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				dbConn.closeConnection(conn);
				e.printStackTrace();
			}
		}
		// Returns the total number of signals defined in HDB
		return displayFormat;
	}

}
