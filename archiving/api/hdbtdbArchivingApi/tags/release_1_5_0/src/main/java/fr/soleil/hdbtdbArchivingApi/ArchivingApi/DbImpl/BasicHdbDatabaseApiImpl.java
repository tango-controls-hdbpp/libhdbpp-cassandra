//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbImpl\\BasicHdbDatabaseApiImpl.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.IGetInterfaceDataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public abstract class BasicHdbDatabaseApiImpl extends DataBaseApiImpl implements IHdbDataBaseApi 
{
   
   /**
    * @roseuid 45EC1E61037C
    */
   public BasicHdbDatabaseApiImpl(IDataBaseApiTools dataBaseApiBasic, IGetInterfaceDataBaseApi getInterface) 
   {
	   super(dataBaseApiBasic, getInterface);
   }
   
   /**
    * @roseuid 45C9AEEE0236
    */
   public void insert_ScalarData(ScalarEvent scalarEvent) throws ArchivingException
   {
	   System.out.println("SPJZ==> BasicHdbDatabaseApiImpl insert_ScalarData");
	   insertScalar();
   }
   
   /**
    * @roseuid 45C9AEF00274
    */
   protected abstract void insertScalar(); 

}
