/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class OracleMode implements IDbMode {

	/**
	 * 
	 */
	public OracleMode() {
		// TODO Auto-generated constructor stub
	}

	/**For Oracle
	 * Method which updates a record in the "Attribute Mode Table"
	 * Each time that the archiving of an attribute is stopped, one of this table's row is fielded.
	 */

	public void updateModeRecord(IDBConnection dbConn, String attribute_name, IAdtAptAttributes att) throws ArchivingException
	{
		PreparedStatement preparedStatement = null;
		// Create and execute the SQL query string
		// Build the query string
		String set_clause = new StringBuffer().append(ConfigConst.TAB_MOD[ 3 ]).append(" = ?").toString();
		String updated_table = new StringBuffer().append(dbConn.getSchema()).append(".").append(ConfigConst.TABS[ 2 ]).toString();
		String clause_1 = new StringBuffer().append(ConfigConst.TAB_MOD[ 0 ]).append(" = ").append("(SELECT ").append(ConfigConst.TAB_DEF[ 0 ]).
		append(" FROM ").append(dbConn.getSchema()).append(".").append(ConfigConst.TABS[ 0 ]).append(" WHERE ").append(ConfigConst.TAB_DEF[ 2 ]).append(" = ").append("?)").toString();
		String clause_2 = new StringBuffer().append(ConfigConst.TABS[ 2 ]).append(".").append(ConfigConst.TAB_MOD[ 3 ]).
		append(" IS NULL ").toString();
		String update_query = new StringBuffer().append("UPDATE ").append(updated_table).append(" SET ").append(set_clause).
		append(" WHERE ").append("(").append("(").append(clause_1).append(")").append(" AND ").append("(").append(clause_2).append(")").append(")").toString();

		Connection conn = dbConn.getConnection();
		try
		{
			preparedStatement = conn.prepareStatement(update_query);
			dbConn.setLastStatement(preparedStatement);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			preparedStatement.setTimestamp(1 , timestamp);
			preparedStatement.setString(2 , attribute_name);
			try
			{
				preparedStatement.executeUpdate();
				if(dbConn.getAutoCommit()){
					ConnectionCommands.commit(conn, dbConn.getDbUtils());
				}
			}
			catch ( SQLException e )
			{
				ConnectionCommands.rollback(conn, dbConn.getDbUtils());
				throw e;
			}
			catch ( ArchivingException ae )
			{
				throw ae;
			}
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.UPDATE_FAILURE;
			String desc = "Failed while executing DataBaseApi.updateModeRecord() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally{
			try {
				if(preparedStatement!=null)ConnectionCommands.close(preparedStatement);
				if(conn!=null)dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}


}
