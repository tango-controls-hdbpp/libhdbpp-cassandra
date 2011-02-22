package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class AttributeDomains extends AttributesData{

	public AttributeDomains(int type) {
		super(type);
	}
	/**
	 * <b>Description : </b> Gets all the registered domains.
	 *
	 * @return all the registered domains (array of strings)
	 * @throws ArchivingException
	 */
	public String[] get_domains() throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return null;

 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 4 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		//System.out.println("sqlStr = " + sqlStr);
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
		
		
	}
	/**
	 * 
	 * @param dom_regexp
	 * @return
	 * @throws ArchivingException
	 */
	public String[] get_domains_by_criterion(String dom_regexp) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return null;

 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[ 4 ] + " FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ]
		         + " WHERE " + ConfigConst.TAB_DEF[ 4 ] + " LIKE " + "'" + dom_regexp.replace('*' , '%') + "'"
                 //CLA 25/07/06
                 +" ORDER BY " + ConfigConst.TAB_DEF[ 4 ]
                 ;
		return DbUtils.toStringArray(getStringVector_AttributeData(sqlStr));
	}
	/**
	 * <b>Description : </b> Returns the number of distinct registered domains.
	 *
	 * @return the number of distinct registered domains.
	 * @throws ArchivingException
	 */
	public int get_domainsCount() throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if(dbConn == null)
			return 0;

 		// Create and execute the SQL query string
		String sqlStr;
		sqlStr = "SELECT COUNT(DISTINCT " + ConfigConst.TAB_DEF[ 4 ] + ") FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[ 0 ];
		//System.out.println("sqlStr = " + sqlStr);
		return getInt_AttributeData(sqlStr);
	}

}
