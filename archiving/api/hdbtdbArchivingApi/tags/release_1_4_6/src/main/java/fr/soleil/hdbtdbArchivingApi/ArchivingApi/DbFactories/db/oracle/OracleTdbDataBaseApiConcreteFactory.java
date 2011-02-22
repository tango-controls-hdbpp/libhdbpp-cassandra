//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbFactories\\db\\oracle\\OracleTdbDataBaseApiConcreteFactory.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.db.oracle;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbConnection.IDataBaseConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.DataBaseApiAbstractFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.db.oracle.OracleTdbDataBaseApiImpl;

public class OracleTdbDataBaseApiConcreteFactory extends DataBaseApiAbstractFactory 
{

	/**
	 * @param dbConn
	 * @roseuid 45EC2B9E020D
	 */
	public OracleTdbDataBaseApiConcreteFactory(IDataBaseConnection dbConn) 
	{
		super(dbConn);
	}

	/**
	 * @roseuid 45ED83BF021F
	 */
	protected void createIDataBaseApi() 
	{
		m_DataBaseApiInstance = new OracleTdbDataBaseApiImpl(m_SupportDataBaseApiInstance, m_SupportDataBaseApiInstance);
	}

	/**
	 * @param dbConn
	 * @roseuid 45CC96A20071
	 */
	protected void createSupportDataBaseApiAbstractFactory(IDataBaseConnection dbConn) 
	{
		m_SupportDataBaseApiInstance = new OracleSupportDataBaseApiConcreteFactory(dbConn);
	}
}
