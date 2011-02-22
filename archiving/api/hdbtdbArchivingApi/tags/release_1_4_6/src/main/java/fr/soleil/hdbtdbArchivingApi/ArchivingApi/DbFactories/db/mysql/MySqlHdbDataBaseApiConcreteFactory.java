//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbFactories\\db\\mysql\\MySqlHdbDataBaseApiConcreteFactory.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.db.mysql;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbConnection.IDataBaseConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.DataBaseApiAbstractFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.db.mysql.MySqlHdbDataBaseApiImpl;



public class MySqlHdbDataBaseApiConcreteFactory extends DataBaseApiAbstractFactory 
{
	/**
	 * @param dbConn
	 * @roseuid 45C9E89A02A1
	 */
	public MySqlHdbDataBaseApiConcreteFactory(IDataBaseConnection dbConn) 
	{
		super(dbConn);
	}

	protected void createIDataBaseApi() 
	{
		m_DataBaseApiInstance = new MySqlHdbDataBaseApiImpl(m_SupportDataBaseApiInstance, m_SupportDataBaseApiInstance);
	}
	/**
	 * @roseuid 45CC4AE400A9
	 */
	protected void createSupportDataBaseApiAbstractFactory(IDataBaseConnection dbConn) 
	{
		m_SupportDataBaseApiInstance = new MySqlSupportDataBaseApiConcreteFactory(dbConn);
	}
}
