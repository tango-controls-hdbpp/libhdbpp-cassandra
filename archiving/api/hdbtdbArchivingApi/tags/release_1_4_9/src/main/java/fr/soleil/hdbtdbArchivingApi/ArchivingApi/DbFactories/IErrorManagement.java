//Source file: D:\\eclipse\\eclipse\\workspace\\DevArchivage\\javaapi\\fr\\soleil\\TangoArchiving\\ArchivingApi\\DbInterfaces\\IErrorManagement.java

package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories;

import java.sql.SQLException;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;


public interface IErrorManagement 
{
   
   /**
    * @roseuid 45CC9BCC0061
    */
   public void sqlExceptionTreatment(SQLException e, String methodeName) throws  ArchivingException;
}
