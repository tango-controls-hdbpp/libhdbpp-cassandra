//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbFactories\\SupportDataBaseApiAbstractFactory.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories;

import java.sql.SQLException;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbConnection.IDataBaseConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.IDataBaseApiTools;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.IExtractorDataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.IRecorderDataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

import java.sql.Connection;

public abstract class SupportDataBaseApiAbstractFactory extends DataBaseApiUtilities 
	implements IGetInterfaceDataBaseApi, IDataBaseApiTools 
{
	protected IRecorderDataBaseApi m_IRecorderInstance;
	protected IExtractorDataBaseApi m_IExtractorInstance;
	
	/**
	 * @param dataBaseApiTools
	 * @roseuid 45C9BD660101
	 */
	protected SupportDataBaseApiAbstractFactory(IDataBaseConnection dbConn) 
	{
		super(dbConn);
		
		createIExtractorDataBaseApi();
		createIRecorderDataBaseApi();    
	}

	/**
	 * @param dataBaseApiTools
	 * @roseuid 45C9BDFB0091
	 */
	protected abstract void createIExtractorDataBaseApi();

	/**
	 * @param dataBaseApiTools
	 * @roseuid 45C9BDFB0092
	 */
	protected abstract void createIRecorderDataBaseApi();

	/**
	 * @return fr.soleil.TangoArchiving.ArchivingApi.DbInterfaces.IRecorderDataBaseApi
	 * @roseuid 45EC27F6028F
	 */
	public IRecorderDataBaseApi getIRecorderDataBaseApi() 
	{
		return m_IRecorderInstance;    
	}

	/**
	 * @return fr.soleil.TangoArchiving.ArchivingApi.DbInterfaces.IExtractorDataBaseApi
	 * @roseuid 45EC27F60290
	 */
	public IExtractorDataBaseApi getlExtractorDataBaseApi() 
	{
		return m_IExtractorInstance;    
	}

	/**
	 * @param e
	 * @param methodeName
	 * @throws fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException
	 * @roseuid 45ED5DCC03E0
	 */
	public abstract void sqlExceptionTreatment(SQLException e, String methodeName) throws ArchivingException;

	/**
	 * @return java.sql.Connection
	 * @roseuid 45ED7E8501D5
	 */
	public Connection getDbConnection() 
	{
		return m_dbConnection.getDbConnection();
	}

	/**
	 * @return java.lang.String
	 * @roseuid 45ED7E8501E5
	 */
	public String getDbSchema() 
	{
		return m_dbConnection.getDbSchema();
	}
	
	public boolean isAutoCommit() 
	{
		return m_dbConnection.isAutoCommit();
	}
}
