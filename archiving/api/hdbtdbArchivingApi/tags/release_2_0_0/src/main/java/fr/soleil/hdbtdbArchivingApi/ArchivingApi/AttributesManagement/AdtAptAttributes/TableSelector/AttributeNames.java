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
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class AttributeNames  extends AttributesData{

	public AttributeNames(IDBConnection  c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	
	
    public String[] get_attributes_complete_names() throws ArchivingException
    {
        // Create and execute the SQL query string
        String sqlStr;
        sqlStr = "SELECT  " + ConfigConst.TAB_DEF[ 2 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
                 //+" ORDER BY " + ConfigConst.TAB_DEF[ 4 ];
        
        return  DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
    }

	/**
	 * <b>Description : </b>    	Gets all the attributes's names for a given  domain, family, member<br>
	 *
	 * @param domain The given domain used to retrieve the names
	 * @param family The given family used to retrieve the names
	 * @param member The given member used to retrieve the names
	 * @return array of name strings
	 * @throws ArchivingException
	 */
	public String[] getAttributes(String domain , String family , String member) throws ArchivingException
	{
         String sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 2 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
        " WHERE " + ConfigConst.TAB_DEF[ 4 ] + " = " + "'" + domain + "'" +
        " AND " + ConfigConst.TAB_DEF[ 5 ] + " = " + "'" + family + "'" +
        " AND " + ConfigConst.TAB_DEF[ 6 ] + " = " + "'" + member + "'";
 		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}
	/**
	 * <b>Description : </b>    	Gets the list of attributes registered in <I>HDB</I>
	 *
	 * @return an array of String containing the attributes names <br>
	 * @throws ArchivingException
	 */
	public Vector<String> getAttributes() throws ArchivingException
	{
 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 2 ] +
		         " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];

		// Returns the names list
		return getStringVector_AttributeData(sqlStr);
	}

	/**
	 * <b>Description : </b>    	Gets the list of attributes registered in the historical (resp. temporary) database, that belong to the specified facility.
	 *
	 * @return an array of String containing the attributes names <br>
	 * @throws ArchivingException
	 */
	public Vector<String> getAttributes(String facility) throws ArchivingException
	{
			System.out.println("DataBaseApi.getAttributes : " + facility);
			Vector<String> nameList = new Vector<String>();
			Connection conn = null;
			PreparedStatement preparedStatement = null;
			ResultSet rset = null;

		// Create and execute the SQL query string

		String select_field = ConfigConst.TABS[ 0 ] + "." + ConfigConst.TAB_DEF[ 2 ];
		String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		String clause_1 = ConfigConst.TABS[ 0 ] + "." + ConfigConst.TAB_DEF[ 14 ] + " LIKE ?";
		String getAttributeDataQuery = "SELECT DISTINCT(" + select_field + ")" + " FROM " + table_1 + " WHERE " + "(" + clause_1 + ")";

		try
		{
			conn = dbConn.getConnection();
			preparedStatement = conn.prepareStatement(getAttributeDataQuery);
			dbConn.setLastStatement(preparedStatement) ;
			preparedStatement.setString(1 , facility.trim());
			rset = preparedStatement.executeQuery();
			// Gets the result of the query
			while ( rset.next() )
				nameList.addElement(rset.getString(1));
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.QUERY_FAILURE;
			String desc = "Failed while executing DataBaseApi.getAttributes() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally
        {
        	try {
				if(rset !=null)ConnectionCommands.close(rset);
				if(preparedStatement!=null)ConnectionCommands.close(preparedStatement);
				if(conn!=null)dbConn.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				dbConn.closeConnection(conn);
				e.printStackTrace();
			}
        }
		// Close the connection with the database
//		if ( dbConn.isAutoConnect() )
//			dbConn.close();
		// Returns the names list
		return nameList;
	}


	/**
	 * <b>Description : </b>    	Returns the number of attributes defined in <I>HDB</I>
	 *
	 * @return The total number of attributes defined in <I>HDB</I>
	 * @throws ArchivingException
	 */
	public int getAttributesCount() throws ArchivingException
	{
 		// Build the query string
		String getAttributeCountQuery;
		getAttributeCountQuery = "SELECT count(*) FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];

		// Returns the total number of signals defined in HDB
		return getInt_AttributeData(getAttributeCountQuery);
	}

	public String[] getAttributes_by_criterion(String domain , String family , String member , String att_regexp) throws ArchivingException
	{
 		// Create and execute the SQL query string
        String sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 2 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
        " WHERE " + ConfigConst.TAB_DEF[ 4 ] + " = " + "'" + domain + "'" +
        " AND " + ConfigConst.TAB_DEF[ 5 ] + " = " + "'" + family + "'" +
        " AND " + ConfigConst.TAB_DEF[ 6 ] + " = " + "'" + member + "'" 
        ;
        //CLA 25/07/06
        sqlStr += " ORDER BY " + ConfigConst.TAB_DEF[ 2 ];

        //System.out.println ( "DataBaseApi/getAttributes_by_criterion/sqlStr|"+sqlStr+"|" );
        
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	/**
	 * <b>Description : </b>    	Returns the number of registered the attributes for a given  domain, family, member<br>
	 *
	 * @param domain The given domain used to retrieve the names
	 * @param family The given family used to retrieve the names
	 * @param member The given member used to retrieve the names
	 * @return array of name strings
	 * @throws ArchivingException
	 */
	public int getAttributesCount(String domain , String family , String member) throws ArchivingException
	{
		// Create and execute the SQL query string
		String sqlStr = "SELECT COUNT(DISTINCT " + ConfigConst.TAB_DEF[ 2 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
		                " WHERE " + ConfigConst.TAB_DEF[ 4 ] + "='" + domain + "'" +
		                " AND " + ConfigConst.TAB_DEF[ 5 ] + "='" + family + "'" +
		                " AND " + ConfigConst.TAB_DEF[ 6 ] + "='" + member + "'";

		return getInt_AttributeData(sqlStr);
	}

	/**
	 * <b>Description : </b>    	Gets the total number of attributes defined in <I>HDB</I> with the given type
	 * (0: DevShort - 1: DevLong - 2: DevDouble - 3: DevString) <br>
	 *
	 * @param data_type
	 * @return The total number of attributes for the specified type
	 * @throws ArchivingException
	 */
	public int getAttributesCountT(int data_type) throws ArchivingException
	{
 
		String getAttributeCountQuery;
		getAttributeCountQuery = "SELECT count(*) FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
		                         " WHERE " + ConfigConst.TAB_DEF[ 8 ] + "=" + data_type;
		// Execute the SQL query string


		// Returns the total number of signals defined in HDB
		return getInt_AttributeData(getAttributeCountQuery);
	}

	/**
	 * <b>Description : </b>    	Gets the total number of attributes defined in <I>HDB</I> with the given format
	 * (0 -> scalar | 1 -> spectrum | 2 -> image) <br>
	 *
	 * @param data_format
	 * @return The total number of attributes for the specified type
	 * @throws ArchivingException
	 */
	public int getAttributesCountF(int data_format) throws ArchivingException
	{
 		// Build the query string
		String getAttributeCountQuery;
		getAttributeCountQuery = "SELECT count(*) FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
		                         " WHERE " + ConfigConst.TAB_DEF[ 9 ] + "=" + data_format;

	// Returns the total number of signals defined in HDB
		return getInt_AttributeData(getAttributeCountQuery);
	}


	/**
	 * <b>Description : </b>    	Gets the list of <I>HDB</I> registered attributes for the given format (0 -> scalar | 1 -> spectrum | 2 -> image)
	 *
	 * @return An array  containing the attributes names <br>
	 * @throws ArchivingException
	 */
	public Vector<String> getAttributesNamesF(int data_format) throws ArchivingException
	{
 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 2 ] +
		         " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
		         " WHERE " + ConfigConst.TAB_DEF[ 9 ] + "=" + data_format;
		// Returns the names list
		return getStringVector_AttributeData(sqlStr);
	}

	/**
	 * <b>Description : </b>    	Gets the list of registered <I>HDB</I> attributes for the given type (2 -> Tango::DevShort | 3 -> Tango::DevLong | 5 -> Tango::DevDouble and 8 -> Tango::DevString)
	 *
	 * @return An array  containing the attributes names <br>
	 * @throws ArchivingException
	 */
	public Vector<String> getAttributesNamesT(int data_type) throws ArchivingException
	{
		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 2 ] +
		         " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ] +
		         " WHERE " + ConfigConst.TAB_DEF[ 8 ] + "=" + data_type;

		// Returns the names list
		return getStringVector_AttributeData(sqlStr);
	}

}
