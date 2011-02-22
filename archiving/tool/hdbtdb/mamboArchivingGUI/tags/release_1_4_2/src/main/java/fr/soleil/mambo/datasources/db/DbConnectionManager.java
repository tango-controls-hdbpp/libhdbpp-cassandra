package fr.soleil.mambo.datasources.db;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;

public class DbConnectionManager 
{
    //protected static DataBaseApi database;
    protected static boolean isReady = false;
    protected static ArchivingManagerApi manager;//static because archiving and extracting have to use the same connection
    
    public DbConnectionManager()
    {
        if ( manager == null )
        {
            manager = new ArchivingManagerApi ();
        }
        
        System.out.println ( "DbConnectionManager/new/manager/"+manager );
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
            
            this.manager = new ArchivingManagerApi ();
            
            // If HDB is not available it is not useful to connect to this DB
            if ( Mambo.isHdbAvailable() == true ) {
            	
                String HDBuser = Mambo.getHDBuser();
                String HDBpassword = Mambo.getHDBpassword();
                
            	try
            	{
            		if(Mambo.canChooseArchivers())
            		{
            			this.manager.ArchivingConfigure( HDBuser , HDBpassword ,true );                  
            		}
            		else 
            		{
            			this.manager.ArchivingConfigureWithoutArchiverListInit( HDBuser , HDBpassword ,true );
            		}

            	}
            	catch ( ArchivingException ae )
            	{
            		System.out.println ( "DbConnectionManager/openConnection/failed to connect to HDB!" );
            		ae.printStackTrace ();

            		logger.trace ( ILogger.LEVEL_ERROR, "Failed to connect to HDB" );
            		logger.trace ( ILogger.LEVEL_ERROR, ae );

            		hdbFailure = ae;
            	}
            }
            
            try
            {
                String TDBuser = Mambo.getTDBuser();
                String TDBpassword = Mambo.getTDBpassword();
                
               	if(Mambo.canChooseArchivers())
                {
               	    this.manager.ArchivingConfigure( TDBuser , TDBpassword ,false );
                }
               	else
                {
                    this.manager.ArchivingConfigureWithoutArchiverListInit( TDBuser , TDBpassword ,false );
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
    public DataBaseApi getDataBaseApi ( boolean _historic ) throws ArchivingException
    {
        openConnection();
        DataBaseApi database = this.manager.getDataBase( _historic );

        return database;
    }
}
