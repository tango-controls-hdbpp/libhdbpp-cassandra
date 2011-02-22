package fr.soleil.mambo.datasources.db;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;

public class DbConnectionManager 
{
    //protected static DataBaseApi database;
    protected static boolean isReady = false;
    protected static IArchivingManagerApiRef  hdbManager, tdbManager;
    
    public DbConnectionManager()
    {
        //System.out.println ( "DbConnectionManager/new/manager/"+manager );
    }
    
    public void openConnection () throws ArchivingException
    {
        if ( ! isReady )
        {
             ILogger logger = LoggerFactory.getCurrentImpl ();
            
            //ArchivingManagerApi.ArchivingConfigure( HDBuser , HDBpassword , TDBuser , TDBpassword );
            //CLA 30/10/06 AN EXCEPTION IS NO LONGER THROWN, AS BOTH DATABASES ARE INITIALIZED INDEPENDANTLY
            ArchivingException hdbFailure = null;
            ArchivingException tdbFailure = null;
            
            
            // If HDB is not available it is not useful to connect to this DB
            if ( Mambo.isHdbAvailable() == true ) {
            	this.hdbManager = ArchivingManagerApiRefFactory.getInstance(ConfigConst.HDB);
            	
            	String HDBuser = Mambo.getHDBuser();
                String HDBpassword = Mambo.getHDBpassword();
                String HDBHost = System.getProperty("HDB_HOST")== null?"":System.getProperty("HDB_HOST");
                String HDBName = System.getProperty("HDB_NAME")== null?"":System.getProperty("HDB_NAME");
                
            	try
            	{
            		if(Mambo.canChooseArchivers())
            		{
            			
            			this.hdbManager.ArchivingConfigure( HDBuser , HDBpassword  );
            			
            		}
            		else 
            		{
            			this.hdbManager.ArchivingConfigureWithoutArchiverListInit(HDBHost, HDBName, HDBuser , HDBpassword  );
            		}
            	}
            	catch ( ArchivingException ae )
            	{
            		hdbManager = null;
            		System.out.println ( "DbConnectionManager/openConnection/failed to connect to HDB!" );
            		ae.printStackTrace ();

            		logger.trace ( ILogger.LEVEL_ERROR, "Failed to connect to HDB" );
            		logger.trace ( ILogger.LEVEL_ERROR, ae );

            		hdbFailure = ae;
            	}
            }
            
            try
            {
            	this.tdbManager = ArchivingManagerApiRefFactory.getInstance(ConfigConst.TDB);
                String TDBuser = Mambo.getTDBuser();
                String TDBpassword = Mambo.getTDBpassword();
                String TDBHost = System.getProperty("TDB_HOST")== null?"":System.getProperty("TDB_HOST");
                String TDBName = System.getProperty("TDB_NAME")== null?"":System.getProperty("TDB_NAME");

               	if(Mambo.canChooseArchivers())
                {
               	    this.tdbManager.ArchivingConfigure( TDBuser , TDBpassword  );
                }
               	else
                {
                    this.tdbManager.ArchivingConfigureWithoutArchiverListInit(TDBHost, TDBName, TDBuser , TDBpassword  );
                }
            }
            catch ( ArchivingException ae )
            {
                System.out.println ( "DbConnectionManager/openConnection/failed to connect to TDB!" );
                ae.printStackTrace ();
                
                logger.trace ( ILogger.LEVEL_ERROR, "Failed to connect to TDB" );
                logger.trace ( ILogger.LEVEL_ERROR, ae );
                
                tdbFailure = ae;
            }
            
            if ( hdbFailure != null && tdbFailure != null )
            {
                throw hdbFailure;
            }
            
            isReady = true;
        }
    }
    
    /* (non-Javadoc)
     * @see mambo.datasources.db.IAttrDBManager#getDataBaseApi(boolean)
     */
    public DataBaseManager getDataBaseApi ( boolean _historic ) throws ArchivingException
    {
    	DataBaseManager database = null;
        try
        {
        	openConnection();
            database = getArchivingManagerApiInstance(_historic).getDataBase( );
        }
        catch(Exception e ){
        	
        }

        return database;
    }
    public IArchivingManagerApiRef getArchivingManagerApiInstance(boolean historic){
    	return historic? this.hdbManager:this.tdbManager;
    }
}
