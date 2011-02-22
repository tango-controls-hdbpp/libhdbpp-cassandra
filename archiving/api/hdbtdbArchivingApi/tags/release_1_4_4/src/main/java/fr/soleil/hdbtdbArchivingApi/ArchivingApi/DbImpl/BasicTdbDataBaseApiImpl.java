//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbImpl\\BasicTdbDataBaseApiImpl.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.IGetInterfaceDataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;


public abstract class BasicTdbDataBaseApiImpl extends DataBaseApiImpl implements ITdbDataBaseApi
{

	// Constructor
	/**
	 * @roseuid 45EC1DE60359
	 */
	public BasicTdbDataBaseApiImpl(IDataBaseApiTools dataBaseApiBasic, IGetInterfaceDataBaseApi getInterface) 
	{
		super(dataBaseApiBasic, getInterface);
	}

	// Abstract Methods
	protected abstract void exportToDB(String remoteDir, String fileName, String tableName, int writable);
	
	
	/**
	 * @roseuid 45C9AC020260
	 */
	public void exportToDB_Scalar(String remoteDir, String fileName, String tableName, int writable) throws ArchivingException
	{
		exportToDB(remoteDir,fileName,tableName,writable);
	}
}
