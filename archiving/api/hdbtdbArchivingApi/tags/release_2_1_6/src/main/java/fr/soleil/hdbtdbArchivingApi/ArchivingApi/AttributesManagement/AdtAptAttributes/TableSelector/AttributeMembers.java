package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class AttributeMembers extends AttributesData {

	public AttributeMembers(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Description : </b> Gets all the registered members
	 *
	 * @return array of strings
	 * @throws ArchivingException
	 */
	public String[] get_members() throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return null;

		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 6 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE " + ConfigConst.TAB_DEF[ 6 ] + " IS NOT NULL";
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	/**
	 * <b>Description : </b> Returns the number of distinct members.
	 *
	 * @return the number of distinct members.
	 * @throws ArchivingException
	 */
	public int get_membersCount() throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return 0;

		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT COUNT(DISTINCT " + ConfigConst.TAB_DEF[ 6 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE " + ConfigConst.TAB_DEF[ 6 ] + " IS NOT NULL";
		return getInt_AttributeData(sqlStr);
	}

	/**
	 * <b>Description : </b> Gets all the registered members for the given domain and family
	 *
	 * @param domain_name the given domain
	 * @param family_name the given family
	 * @return array of strings
	 * @throws ArchivingException
	 */
	public String[] get_members(String domain_name , String family_name) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return null;

 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 6 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE (";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + domain_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 5 ] + " LIKE " + "'" + family_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 6 ] + " IS NOT NULL" + ")";

		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	public String[] get_members_by_criterion(String domain_name , String family_name , String member_regexp) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return null;

 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 6 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE (";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + domain_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 5 ] + " LIKE " + "'" + family_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 6 ] + " LIKE " + "'" + member_regexp.replace('*' , '%') + "'";
		sqlStr = sqlStr + " ) ";
        //CLA 25/07/06
        sqlStr = sqlStr + " ORDER BY " + ConfigConst.TAB_DEF[ 6 ]; 
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	/**
	 * <b>Description : </b> Returns the number of distinct registered members for the given domain and family.
	 *
	 * @param domain_name the given domain
	 * @param family_name the given family
	 * @return the number of distinct registered members for the given domain and family.
	 * @throws ArchivingException
	 */
	public int get_membersCount(String domain_name , String family_name) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return 0;

 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT COUNT(DISTINCT " + ConfigConst.TAB_DEF[ 6 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE (";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + domain_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 5 ] + " LIKE " + "'" + family_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 6 ] + " IS NOT NULL" + ")";

		// Returns the corresponding number
		return getInt_AttributeData(sqlStr);
	}

}
