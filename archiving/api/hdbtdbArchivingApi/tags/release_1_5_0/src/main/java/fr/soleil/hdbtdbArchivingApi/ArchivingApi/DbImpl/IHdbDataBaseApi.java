//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbInterfaces\\IHdbDataBaseApi.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public interface IHdbDataBaseApi extends IDataBaseApi 
{
   
   /**
    * @param scalarEvent
    * @throws fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException
    * @roseuid 45E710AB0018
    */
   public void insert_ScalarData(ScalarEvent scalarEvent) throws ArchivingException;
}
