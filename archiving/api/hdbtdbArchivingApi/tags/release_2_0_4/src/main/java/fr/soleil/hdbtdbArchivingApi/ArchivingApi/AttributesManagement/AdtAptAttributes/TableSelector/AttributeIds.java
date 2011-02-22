package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class AttributeIds extends AttributesData {

	public AttributeIds(IDBConnection  c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * <b>Description : </b>    	Gets for a specified attribute its ID as defined in HDB
	 *
	 * @param att_name The attribute's name
	 * @return The <I>HDB</I>'s ID that caracterize the given attribute
	 * @throws ArchivingException
	 */
	public int getAttID(String att_name) throws ArchivingException
	{
		int attributesID = 0;
		ResultSet rset = null;
		Connection conn = null;
		PreparedStatement ps_get_att_id = null;
		// My statement
		String selectString = "SELECT " + ConfigConst.TAB_DEF[ 0 ] +
		                      " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
                              " WHERE " + ConfigConst.TAB_DEF[ 2 ] + " = ?";
		try
		{
			conn = dbConn.getConnection();
			ps_get_att_id = conn.prepareStatement(selectString);
			dbConn.setLastStatement( ps_get_att_id);
			String field1 = att_name.trim();
			ps_get_att_id.setString(1 , field1);
			rset = ps_get_att_id.executeQuery();
			// Gets the result of the query
			if ( rset.next() )
				attributesID = rset.getInt(1);
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.QUERY_FAILURE;
			String desc = "Failed while executing AttributesIds.getAttID() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				if(rset !=null)ConnectionCommands.close(rset);
				if(ps_get_att_id!=null)ConnectionCommands.close(ps_get_att_id);
				if(conn!=null)dbConn.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				dbConn.closeConnection(conn);
				e.printStackTrace();
			}
		}
		// Returns the total number of signals defined in HDB
		return attributesID;
	}

	/**
	 * <b>Description : </b> Returns the new id for the attribute being referenced in <I>HDB</I>.<I>(mySQL only)</I>
	 *
	 * @return the new id for the attribute being referenced in <I>HDB</I>
	 * @throws ArchivingException
	 */
	public int getMaxID() throws ArchivingException
	{
        Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		int new_ID = 10000;
		int res = 10000;
		// First connect with the database
//		if ( dbConn.isAutoConnect() )
//			dbConn.connect();
		// Create and execute the SQL query string
		// Build the query string

		String getMaxIdQuery;
		getMaxIdQuery = "SELECT MAX(" + ConfigConst.TAB_DEF[ 0 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];

		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getMaxIdQuery);
			// Gets the result of the query
			if ( rset.next() )
			{
				res = rset.getInt("max(ID)");
			}
			if ( res < new_ID )
			{
				//System.out.println("La table/base est vide");
				res = new_ID;
			}
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.QUERY_FAILURE;
			String desc = "Failed while executing AttributesIds.getMaxID() method...";
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
		return res;
	}
	/**
	 * 
	 * @param name
	 * @return
	 * @throws ArchivingException
	 */
	public int getBufferedAttID(String name) throws ArchivingException 
    {
        Integer idI = (Integer) dbConn.getIdsBuffer().get ( name );
        int id;
        
        if ( idI == null )
        {
            id = getAttID ( name );
            //System.out.println ( "CLA/getBufferedAttID/BUFFERING for attr|"+name+"|id|"+id );
            
            idI = new Integer ( id );
            dbConn.getIdsBuffer().put ( name , idI );
        }
        else
        {
            id = idI.intValue ();
            //System.out.println ( "CLA/getBufferedAttID/USING BUFFER for attr|"+name+"|id|"+id );
        }
        
        return id;
    }

}
