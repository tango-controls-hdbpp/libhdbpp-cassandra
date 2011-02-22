/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class HDBMySqlAttributeInsert extends HdbAttributeInsert {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public HDBMySqlAttributeInsert(int type, IAdtAptAttributes at) {
		super(type, at);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.HDBExtractor#insert_ImageData_RO_DataBase()
	 */
	@Override
	protected void insert_ImageData_RO_DataBase(StringBuffer query, StringBuffer tableName, StringBuffer tableFields, int dim_x, int dim_y, Timestamp timeSt,  Double[][] dvalue, StringBuffer valueStr, String att_name)
	throws  ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return;
		// TODO Auto-generated method stub
			Connection conn = null;
			PreparedStatement preparedStatement = null;
			query.append("INSERT INTO ").append(tableName).append(" (").append(tableFields).append(")").append(" VALUES (?, ?, ?, ?)");
			
			try
			{
				conn = dbConn.getConnection();
				preparedStatement = conn.prepareStatement(query.toString());
				dbConn.setLastStatement(preparedStatement);
				preparedStatement.setTimestamp(1 , timeSt);
				preparedStatement.setInt(2 , dim_x);
				preparedStatement.setInt(3 , dim_y);
                if (dvalue == null)
                {
                    preparedStatement.setNull(4 , java.sql.Types.BLOB);
                }
                else preparedStatement.setString(4 , valueStr.toString());
			    preparedStatement.executeUpdate();
				//System.out.println ( "DataBaseApi/insert_ImageData_RO/DONE!!" );
			}
			catch ( SQLException e )
			{
				String message = "";
				if ( e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.INSERT_FAILURE;
				String desc = "Failed while executing DataBaseApi.insert_ImageData_RO() method...";
				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			}
			finally
			{
				try {
					ConnectionCommands.close(preparedStatement);
					dbConn.closeConnection(conn);
				} catch (SQLException e) {
					dbConn.closeConnection(conn);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	@Override
	protected void insert_SpectrumData_RO_DataBase(StringBuffer query, StringBuffer tableName, StringBuffer tableFields, int dim_x, Timestamp timeSt,  Double[] dvalue, StringBuffer valueStr, String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return;
			Connection conn = null;
			PreparedStatement preparedStatement = null;
			query.append("INSERT INTO ").append(tableName).append(" (").append(tableFields).append(")").append(" VALUES (?, ?, ?)");

			try
			{
				conn = dbConn.getConnection();
				preparedStatement = conn.prepareStatement(query.toString());
				dbConn.setLastStatement(preparedStatement);
				preparedStatement.setTimestamp(1 , timeSt);
				preparedStatement.setInt(2 , dim_x);
                if (valueStr == null)
                {
                    preparedStatement.setNull(3 , java.sql.Types.BLOB);
                }
                else
                {
                    preparedStatement.setString(3 , valueStr.toString());
                }
                preparedStatement.executeUpdate();
			}
			catch ( SQLException e )
			{
				String message = "";
				if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.INSERT_FAILURE;
				String desc = "Failed while executing HDBMySqlAttributeInsert.insert_SpectrumData_RO() method...";
				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			}
			finally
			{
				try {
					ConnectionCommands.close(preparedStatement);
					dbConn.closeConnection(conn);
				} catch (SQLException e) {
					dbConn.closeConnection(conn);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	

	}

	@Override
	protected void insert_SpectrumData_RW_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalueWrite, Double[] dvalueRead,
			StringBuffer valueWriteStr, StringBuffer valueReadStr,
			String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		query.append("INSERT INTO ").append(tableName).append(" (").append(tableFields).append(")").append(" VALUES (?, ?, ?, ?)");
		//System.out.println ( "query/"+query.toString() );

		try
		{
			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(query.toString());
			dbConn.setLastStatement(preparedStatement);
			preparedStatement.setTimestamp(1 , timeSt);
			preparedStatement.setInt(2 , dim_x);
			if (valueReadStr == null)
			{
				preparedStatement.setNull(3, java.sql.Types.BLOB);
			}
			else
			{
				preparedStatement.setString(3 , valueReadStr.toString());
			}
			if (valueWriteStr == null)
			{
				preparedStatement.setNull(4, java.sql.Types.BLOB);
			}
			else
			{
				preparedStatement.setString(4 , valueWriteStr.toString());
			}
			preparedStatement.executeUpdate();
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing HDBMySqlAttributeInsert.insert_SpectrumData_RO() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close(preparedStatement);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	

}
