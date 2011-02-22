/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class TDBAdtAptMethods {
	protected int arch_type;

	/**
	 *
	 */
	public TDBAdtAptMethods(int type) {
		// TODO Auto-generated constructor stub
		this.arch_type = type;
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
	public int[] getAtt_TFW_Data_By_Id(int id) throws ArchivingException {
		int[] TFW_Data = new int[3];
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if (dbConn == null)
			return TFW_Data;

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
		String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[0]
				+ " = " + "?";

		get_TFW_DataQuery = "SELECT " + select_field + " FROM " + table_1
				+ " WHERE (" + clause_1 + ") ";
		// System.out.println ( "getAtt_TFW_Data_By_Id/query/"+get_TFW_DataQuery
		// );
		try {
			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(get_TFW_DataQuery);
			dbConn.setLastStatement(preparedStatement);
			preparedStatement.setInt(1, id);
			// preparedStatement.setString(1 , id.trim());
			rset = preparedStatement.executeQuery();
			while (rset.next()) {
				TFW_Data[0] = rset.getInt(ConfigConst.TAB_DEF[8]);
				TFW_Data[1] = rset.getInt(ConfigConst.TAB_DEF[9]);
				TFW_Data[2] = rset.getInt(ConfigConst.TAB_DEF[10]);
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
			String desc = "Failed while executing TDBAdtAptMethods.getAtt_TFW_Data_By_Id() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, att.getClass().getName(), e);
		} finally {
			ConnectionCommands.close(rset);
			rset = null;
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
		return TFW_Data;
	}

}
