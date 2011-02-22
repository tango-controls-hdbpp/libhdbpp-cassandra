/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class ConnectionFactory {
	static IDBConnection hdbConn, tdbConn;
	
	// True if connection and disconnection are made once
	// False if the connection are or made for every request,
	//private boolean autoConnect;


	public ConnectionFactory() {
		// TODO Auto-generated constructor stub
	}
	public static IDBConnection getInstance(int type ) throws ArchivingException{
		int archiving_type = DbUtils.getHdbTdbType(type);
		if(archiving_type == ConfigConst.HDB){
			if(hdbConn==null)
				hdbConn = getDbConnection(type);
			return hdbConn;
		}
		else if(archiving_type == ConfigConst.TDB){
			if(tdbConn==null)
				tdbConn = getDbConnection(type);
			return tdbConn;
		}
		return null;
		
	}
	

	private static IDBConnection getDbConnection(int type) {
		// TODO Auto-generated method stub
		int dbType = DbUtils.getDbType(type);
		
		switch(dbType){
		case ConfigConst.BD_MYSQL:
			return  new MySqlConnection(type);
		case ConfigConst.BD_ORACLE:
			return  new OracleConnection(type);
		}
		return null;

	}
	/*
	 * 
	 */
	public  static IDBConnection connect_auto(int type) throws ArchivingException
	{
		// TODO Auto-generated method stub
		ArchivingException archivingException1 = null , archivingException2 = null;
		IDBConnection dbConn = getDbConn(type);
		if(dbConn == null){

		try
		{
			//  try oracle
			dbConn = getInstance(DbUtils.getArchivingType(type, ConfigConst.BD_ORACLE));
			dbConn.connect(false);
			System.out.println("ConnectionFactory.connect_auto : " + dbConn.getUser() + "@" + dbConn.getHost() + " has been done.");
			
		}
		catch ( ArchivingException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "CONNECTION FAILED !";
			String reason = "Failed while executing ConnectionFactory.connect_auto() method...";
			String desc = "Failed while connecting to the Oracle archiving database";
			archivingException1 = new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , e);
		}
		try
		{
			dbConn =getInstance(DbUtils.getArchivingType(type, ConfigConst.BD_MYSQL));
			// try MySQL;
			dbConn.connect(false);
			System.out.println("ConnectionFactory.connect_auto : " + dbConn.getUser() + "@" + dbConn.getHost() + " has been done.");
		}
		catch ( ArchivingException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "CONNECTION FAILED !";
			String reason = "Failed while executing ConnectionFactory.connect_auto() method...";
			String desc = "Failed while connecting to the MySQL archiving database";
			archivingException2 = new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , e);

			String _desc = "Failed while connecting to the archiving database";

			ArchivingException archivingException = new ArchivingException(message , reason , ErrSeverity.PANIC , _desc , "" , archivingException1);
			archivingException.addStack(message , reason , ErrSeverity.PANIC , _desc , "" , archivingException2);
			throw archivingException;
		}
		}
		return dbConn;
	}

	/*
	 * 
	 */
	public  static IDBConnection connect_auto(String host, String name, String schema, String user, String pass, int type, boolean rac) throws ArchivingException
	{
		
		
		// TODO Auto-generated method stub
		ArchivingException archivingException1 = null , mysqlArchivingException = null;
		IDBConnection dbConn = getDbConn(type);
		if(dbConn == null){
		try
		{
			//  try oracle
			
			dbConn = getInstance(DbUtils.getArchivingType(type, ConfigConst.BD_ORACLE), host, name, schema, user, pass);
			dbConn.connect(rac);
			System.out.println("ConnectionFactory.connect_auto : " + dbConn.getUser() + "@" + dbConn.getName() + " has been done.");
			
		}
		catch ( ArchivingException e )
		{
			initDbConn(type);
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + /*" : \nuser :" + dbConn.getUser() +"\npassword : " + dbConn.getPasswd()
			  + "\nrac : " + rac + "\nhost : " + dbConn.getHost()+ "\n name : "  + dbConn.getName() +*/ " CONNECTION Oracle FAILED !";
			String reason = "Failed while executing ConnectionFactory.connect_auto() method..." ;/*+
				"\nSystem properties : \nOracle Home : " + System.getProperty("ORACLE_HOME") + 
				"\nPath : " + System.getProperty("PATH") + 
				"\nLink Path : " + System.getProperty("LD_LIBRARY_PATH") +
				"\nTNS Admin Path : " + System.getProperty("TNS_ADMIN") ;*/
			String desc = "Failed while connecting to the Oracle archiving database";
			archivingException1 = new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , e);
			
			try
			{
				dbConn =getInstance(DbUtils.getArchivingType(type, ConfigConst.BD_MYSQL), host, name, schema, user, pass);
				// try MySQL;
				dbConn.connect(false);
				System.out.println("ConnectionFactory.connect_auto : " + dbConn.getUser() + "@" + dbConn.getName() + " has been done.");
			}
			catch ( ArchivingException e1 )
			{
				
				String mysqlMessage = GlobalConst.ARCHIVING_ERROR_PREFIX + /*" : \nuser :" + dbConn.getUser() +"\npassword : " + dbConn.getPasswd()
				  + "\nrac : " + rac + "\nhost : " + dbConn.getHost()+ "\n name : "  + " : " +*/ "CONNECTION My SQL FAILED !";
				String mysqlReason = "Failed while executing ConnectionFactory.connect_auto() method...";
				String mysqlDesc = "Failed while connecting to the MySQL archiving database";
				mysqlArchivingException = new ArchivingException(mysqlMessage , mysqlReason , ErrSeverity.PANIC , mysqlDesc , "" , e1);

				String _desc = "Failed while connecting to the archiving database";

				ArchivingException archivingException = new ArchivingException(mysqlMessage , mysqlReason , ErrSeverity.PANIC , _desc , "" , archivingException1);
				archivingException.addStack(mysqlMessage , mysqlReason , ErrSeverity.PANIC , _desc , "" , mysqlArchivingException);
				throw archivingException;
			}

		}
		}
		return dbConn;

	}
	

	public static IDBConnection getInstance(int type , String host,
			String name, String schema, String user, String pass) throws ArchivingException{
		int archiving_type = DbUtils.getHdbTdbType(type);
		if((archiving_type == ConfigConst.HDB) && (hdbConn==null)){
			hdbConn = getDbConnection(type, host, name, schema, user, pass);
			return hdbConn;
		}else if((archiving_type == ConfigConst.TDB) && (tdbConn==null)){
			tdbConn = getDbConnection(type, host, name, schema, user, pass);
			return tdbConn;
		}
		return null;
	}
	

	private static IDBConnection getDbConnection(int type, String host,
			String name, String schema, String user, String pass) {
		// TODO Auto-generated method stub
		
		int dbType = DbUtils.getDbType(type);
		

		switch(dbType){
		case ConfigConst.BD_MYSQL:
			return  new MySqlConnection(type, host, name, schema, user, pass);
		case ConfigConst.BD_ORACLE:
			return  new OracleConnection(type, host, name, schema, user, pass);
		}
		return null;

	}
	
	private static IDBConnection getDbConn(int type){
		
		if(type == ConfigConst.HDB)
			return hdbConn;
		else return tdbConn;
	}
	private static void initDbConn(int type) {
		// TODO Auto-generated method stub
		if(type == ConfigConst.HDB)
			hdbConn = null;
		else  tdbConn = null;

	}

}
