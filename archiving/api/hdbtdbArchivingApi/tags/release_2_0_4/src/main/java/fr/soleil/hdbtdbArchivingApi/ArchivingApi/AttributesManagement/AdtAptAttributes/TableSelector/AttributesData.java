package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class AttributesData {
	protected  IDBConnection dbConn;
	
	public AttributesData(IDBConnection c) {
		// TODO Auto-generated constructor stub
		dbConn = c;
	}
	public Vector<String> getStringVector_AttributeData(String sqlStr)throws ArchivingException
	{
			Vector<String> argout_vect = new Vector<String>();
			Connection conn = null;
			Statement stmt = null;
			ResultSet rset = null;
			// First connect with the database
//			if ( dbConn.isAutoConnect() )
//				dbConn.connect();
			try
			{
				conn = dbConn.getConnection();
				stmt = conn.createStatement();
				dbConn.setLastStatement(stmt);

				rset = stmt.executeQuery(sqlStr);
				// Gets the result of the query
				while ( rset.next() )
					argout_vect.addElement(rset.getString(1));
			}
			catch ( SQLException e )
			{
				String message = "";
				if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.QUERY_FAILURE;
				String desc = "Failed while executing AttributesData.get_domains() method...";
				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			}
			finally
			{
				try {
					if(rset !=null)ConnectionCommands.close(rset);
					if(stmt!=null)ConnectionCommands.close(stmt);
					if(conn!=null)dbConn.closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					dbConn.closeConnection(conn);
					e.printStackTrace();
				}
			}
			// Close the connection with the database
//			if ( dbConn.isAutoConnect() )
//				dbConn.close();
			// Returns the families list
			
			return argout_vect;

	}
	
	public int getInt_AttributeData(String sqlStr)throws ArchivingException
	{
			int counter = 0;
			Connection conn = null;
			Statement stmt = null;
			ResultSet rset = null;
			// First connect with the database
//			if ( dbConn.isAutoConnect() )
//				dbConn.connect();
			try
			{
				conn = dbConn.getConnection();
				stmt = conn.createStatement();
				dbConn.setLastStatement(stmt);

				rset = stmt.executeQuery(sqlStr);
				// Gets the result of the query
				while ( rset.next() )
					counter = rset.getInt(1);
			}
			catch ( SQLException e )
			{
				String message = "";
				if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.QUERY_FAILURE;
				String desc = "Failed while executing AttributesData.get_domainsCount() method...";
				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			}
			finally
			{
				try {
					if(rset !=null)ConnectionCommands.close(rset);
					if(stmt!=null)ConnectionCommands.close(stmt);
					if(conn!=null)dbConn.closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					dbConn.closeConnection(conn);
					e.printStackTrace();
				}
			}
			// Close the connection with the database
//			if ( dbConn.isAutoConnect() )
//				dbConn.close();
			// Returns the corresponding number
			return counter;


	}

}
