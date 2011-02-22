package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class AttributeFamilies extends AttributesData{

	public AttributeFamilies(IDBConnection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <b>Description : </b> Gets all the registered families.
	 *
	 * @return All the registered families (array of strings).
	 * @throws ArchivingException
	 */
	public String[] get_families() throws ArchivingException
	{
 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 5 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE " + ConfigConst.TAB_DEF[ 5 ] + " IS NOT NULL";
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	/**
	 * <b>Description : </b> Returns the number of distinct registered families.
	 *
	 * @return the number of distinct registered families.
	 * @throws ArchivingException
	 */
	public int get_familiesCount() throws ArchivingException
	{
		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT COUNT(DISTINCT " + ConfigConst.TAB_DEF[ 5 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE " + ConfigConst.TAB_DEF[ 5 ] + " IS NOT NULL";
		return getInt_AttributeData(sqlStr);
	}

	/**
	 * <b>Description : </b> Gets all the registered families for the given domain
	 *
	 * @param domain_name the given domain
	 * @return array of strings
	 * @throws ArchivingException
	 */
	public String[] get_families(String domain_name) throws ArchivingException
	{
		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 5 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + domain_name.replace('*' , '%') + "'";
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	public String[] get_families_by_criterion(String domain_name , String family_regexp) throws ArchivingException
	{
		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 5 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + domain_name.replace('*' , '%') + "'";
		sqlStr = sqlStr + " AND ";
		sqlStr = sqlStr + ConfigConst.TAB_DEF[ 5 ] + " LIKE " + "'" + family_regexp.replace('*' , '%') + "'";
		//  CLA 25/07/06
        sqlStr = sqlStr + " ORDER BY " + ConfigConst.TAB_DEF[ 5 ];
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}

	/**
	 * <b>Description : </b> Returns the number of distinct registered families for a given domain.
	 *
	 * @param domain_name the given domain
	 * @return the number of distinct registered families for a given domain.
	 * @throws ArchivingException
	 */
	public int get_familiesCount(String domain_name) throws ArchivingException
	{
		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT COUNT(DISTINCT " + ConfigConst.TAB_DEF[ 5 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		sqlStr = sqlStr + " WHERE ";
		if ( domain_name.trim().equals("*") )
		{
			sqlStr = sqlStr + "(" + ConfigConst.TAB_DEF[ 4 ] + " LIKE '%' OR " + ConfigConst.TAB_DEF[ 4 ] + " IS NULL)";
		}
		else
		{
			sqlStr = sqlStr + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + domain_name.replace('*' , '%') + "'";
		}

		return getInt_AttributeData(sqlStr);
	}

}
