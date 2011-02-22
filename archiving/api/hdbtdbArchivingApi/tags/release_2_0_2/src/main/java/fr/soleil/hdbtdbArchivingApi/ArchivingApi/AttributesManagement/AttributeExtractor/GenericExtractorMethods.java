package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;

public class GenericExtractorMethods {
	private IAdtAptAttributes att;
	private IDbUtils dbUtils;
	public GenericExtractorMethods(IAdtAptAttributes at, IDbUtils ut) {
		// TODO Auto-generated constructor stub
		this.att = at;
		this.dbUtils = ut;
	}
	public  void makeDataException(int  format, String type1, String type2) throws ArchivingException
	{
		// TODO Auto-generated method stub
		String message = "" , reason = "" , desc = "";
		message = "Failed retrieving data ! ";
		reason = "The attribute should be "+ type1;
		desc = "The attribute format is  not " + type1+ " : " + format + " ("+type2+") !!";
		throw new ArchivingException(message , reason , null , desc , this.getClass().getName());

	}

	public int getDataCountFromQuery(String query, IDBConnection dbConn)throws ArchivingException
	{
		int valuesCount = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		// MySQL and Oracle querry are the same in this case : no test

		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(query);
			while ( rset.next() )
			{
				valuesCount = rset.getInt(1);
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
			String desc = "Failed while executing GenericExtractorMethods.getAttDataCount() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Returns the number of corresponding records
		return valuesCount;
	}

	public DevVarDoubleStringArray getAttScalarDataCondition(String att_name , String query , boolean ro_fields, IDBConnection dbConn) throws ArchivingException
	{
		DevVarDoubleStringArray dvdsa;
		Vector timeVect = new Vector();
		Vector valueRVect = new Vector();
		Vector valueWVect = new Vector();
		String[] timeArr = new String[ 5 ];
		double[] valueRWArr = new double[ 5 ];
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		// Create and execute the SQL query string

		// My statement
		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(query);
			if ( ro_fields )
			{
				while ( rset.next() )
				{
					timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
					double result = rset.getDouble(2);
					if (rset.wasNull())
					{
						valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
					}
					else
					{
						valueRVect.addElement(new Double(result));
					}
				}
			}
			else
			{
				while ( rset.next() )
				{
					timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
					double result1 = rset.getDouble(2);
					if (rset.wasNull())
					{
						valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
					}
					else
					{
						valueRVect.addElement(new Double(result1));
					}
					double result2 = rset.getDouble(3);
					if (rset.wasNull())
					{
						valueWVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
					}
					else
					{
						valueWVect.addElement(new Double(result2));
					}
				}
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
			String desc = "Failed while executing GenericExtractorMethods.getAttDataInfThan() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		timeArr = DbUtils.toStringArray(timeVect);
		valueRWArr = DbUtils.toDoubleArray(valueRVect , valueWVect);
		dvdsa = new DevVarDoubleStringArray(valueRWArr , timeArr);
		return dvdsa;
	}

	/**
	 * 
	 * @param att_name
	 * @param writable
	 * @param cmd
	 * @return
	 * @throws ArchivingException
	 */
	public double getAttScalarDataMinMaxAvg(String att_name , int writable, String cmd, IDBConnection dbConn) throws ArchivingException
	{
		double min = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String selectField = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name.trim(),att);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE )
		{
			selectField = ConfigConst.TAB_SCALAR_RO[ 1 ];
		}
		else
		{ // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			selectField = ConfigConst.TAB_SCALAR_RW[ 1 ];
		}
		String getAttributeDataQuery = "";
		getAttributeDataQuery = "SELECT "+ cmd + "(" + selectField + ")" + " FROM " + tableName;

		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getAttributeDataQuery);
			while ( rset.next() )
			{
				min = rset.getDouble(1);
				if (rset.wasNull())
				{
					min = Double.NaN;
				}
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
			String desc = "Failed while executing GenericExtractorMethods.getAttDataMin() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return min;
	}
	/*
	 * 
	 */
	public double getAttScalarDataMinMaxAvgBetweenDates(String att_name , String time_0 , String time_1 , int writable, String cmd, IDBConnection dbConn) throws ArchivingException
	{
		double max = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String dateClause = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name,att);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE )
		{
			selectField_0 = ConfigConst.TAB_SCALAR_RO[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		}
		else
		{ // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			selectField_0 = ConfigConst.TAB_SCALAR_RW[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];
		}
		dateClause = selectField_0 + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());

		getAttributeDataQuery =
			"SELECT " + cmd + "(" + selectField_1 + ")" + " FROM " + tableName + " WHERE " + "(" + dateClause + ")" + " ORDER BY time";

		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(getAttributeDataQuery);
			while ( rset.next() )
			{
				max = rset.getDouble(1);
				if (rset.wasNull())
				{
					max = Double.NaN;
				}
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
			String desc = "Failed while executing GenericExtractorMethods.getAttDataMaxBetweenDates() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Returns the names list
		return max;
	}
	/*
	 * 
	 */
	public void buildAttributeScalarTab(String att_id , int data_type, String[] tab, IDBConnection dbConn) throws ArchivingException
	{
		Connection conn = null;
		Statement stmt = null;
		// Create and execute the SQL query string
		// Build the query string
		String create_query = "";
		String type = "double";
		if (data_type == TangoConst.Tango_DEV_STRING || data_type == TangoConst.Tango_DEV_BOOLEAN )
		{
			type = "varchar(255)";
		}


		create_query = "CREATE TABLE `" + att_id + "` (" +

		"`" + tab[ 0 ] + "` " + "datetime NOT NULL default '0000-00-00 00:00:00', " +
		"`" + tab[ 1 ] + "` " + type + " default NULL, ";
		if(tab.length == 3)
			create_query +=   "`" + tab[ 2 ] + "` " + type + " default NULL)";

		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement( stmt);
			stmt.executeUpdate(create_query.toString().trim());
			
			
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.TAB_BUILD_FAILURE;
			String desc = "Failed while executing GenericExtractorMethods.buildAttributeScalarTab_RW() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally{
			
			try {
				if(stmt!=null)ConnectionCommands.close(stmt);
				if(conn!=null)dbConn.closeConnection(conn);

			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	}
	/*
	 * 
	 */
	public DevVarDoubleStringArray getAttScalarConditionBetweenDates(String query, boolean ro_fields, IDBConnection dbConn) throws ArchivingException
	{
		DevVarDoubleStringArray dvdsa;
		Vector timeVect = new Vector();
		Vector valueRVect = new Vector();
		Vector valueWVect = new Vector();
		String[] timeArr = new String[ 5 ];
		double[] valueRWArr = new double[ 5 ];

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;


		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			rset = stmt.executeQuery(query);
			if ( ro_fields )
			{
				while ( rset.next() )
				{
					timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
					double result = rset.getDouble(2);
					if (rset.wasNull())
					{
						valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
					}
					else
					{
						valueRVect.addElement(new Double(result));
					}
				}
			}
			else
			{
				while ( rset.next() )
				{
					timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
					double result1 = rset.getDouble(2);
					if (rset.wasNull())
					{
						valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
					}
					else
					{
						valueRVect.addElement(new Double(result1));
					}
					double result2 = rset.getDouble(3);
					if (rset.wasNull())
					{
						valueWVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
					}
					else
					{
						valueWVect.addElement(new Double(result2));
					}
				}
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
			String desc = "Failed while executing GenericExtractorMethods.getAttDataInfThanBetweenDates() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				dbConn.closeConnection(conn);
				e.printStackTrace();
			}
		}
		timeArr = DbUtils.toStringArray(timeVect);
		valueRWArr = DbUtils.toDoubleArray(valueRVect , valueWVect);
		dvdsa = new DevVarDoubleStringArray(valueRWArr , timeArr);
		return dvdsa;
	}
	/*
	 * 
	 */
	public void buildAttributeSpectrumTab(String att_id, String[] tab, int data_type, IDBConnection dbConn)
	throws ArchivingException {
//		TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
//		Create and execute the SQL query string
//		Build the query string
		String create_query = "";

		create_query = "CREATE TABLE `" + att_id + "` (" +
		"`" + tab[ 0 ] + "` " + "DATETIME NOT NULL default '0000-00-00 00:00:00', " +
		"`" + tab[ 1 ] + "` " + "SMALLINT NOT NULL, " +
		"`" + tab[ 2 ] + "` " + "BLOB default NULL, ";
		if(tab.length == 4) 
			create_query += "`" + tab[ 3 ] + "` " + "BLOB default NULL)";

		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			stmt.executeUpdate(create_query.toString().trim());
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.TAB_BUILD_FAILURE;
			String desc = "Failed while executing GenericExtractorMethods.buildAttributeSpectrumTab_RW() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close ( stmt );
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
	 * <b>Description : </b> Creates the table of an image read/write type attribute <I>(mySQL only)</I>.
	 *
	 * @param att_id The ID of the associated attribute
	 */
	public void buildAttributeImageTab(String att_id , String[] tab, int data_type, IDBConnection dbConn) throws ArchivingException
	{
        Connection conn = null;
		Statement stmt = null;
		// Create and execute the SQL query string
		// Build the query string
		String create_query = "";

			create_query = "CREATE TABLE `" + att_id + "` (" +
			               "`" + tab[ 0 ] + "` " + "DATETIME NOT NULL default '0000-00-00 00:00:00', " +
			               "`" + tab[ 1 ] + "` " + "SMALLINT NOT NULL, " +
			               "`" + tab[ 2 ] + "` " + "SMALLINT NOT NULL, " +
			               "`" + tab[ 3 ] + "` " + "LONGBLOB default NULL, " ;
		if(tab.length == 5 )
		    create_query += "`" + tab[ 4 ] + "` " + "LONGBLOB default NULL)";
		
		try
		{
			conn = dbConn.getConnection();
			stmt = conn.createStatement();
			dbConn.setLastStatement(stmt);
			stmt.executeUpdate(create_query.toString().trim());
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.TAB_BUILD_FAILURE;
			String desc = "Failed while executing GenericExtractorMethods.buildAttributeImageTab_RW() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
		{
			try {
				ConnectionCommands.close (stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				dbConn.closeConnection(conn);
				e.printStackTrace();
			}
		}
	}

}
