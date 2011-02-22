//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbImpl\\db\\oracle\\OracleHdbDataBaseApiImpl.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.db.oracle;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.IGetInterfaceDataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.BasicHdbDatabaseApiImpl;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.IDataBaseApiTools;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class OracleHdbDataBaseApiImpl extends BasicHdbDatabaseApiImpl 
{

	/**
	 * @roseuid 45EC210300B9
	 */
	public OracleHdbDataBaseApiImpl(IDataBaseApiTools dataBaseApiTools, IGetInterfaceDataBaseApi getInterface) 
	{
		super(dataBaseApiTools, getInterface);
	}


	/**
	 * @roseuid 45C9AF1102C5
	 */
	protected void insertScalar() 
	{
		System.out.println("SPJZ==> OracleHdbDataBaseApiImpl.insertScalar");
	}

}
