package TdbArchiver;

import java.sql.Connection;
import java.util.StringTokenizer;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.TDBDataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.TdbMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TdbCleaner 
{
    private String tdbUser;
    private String tdbPwd;
    //private int keepingPeriod;
    
    private DataBaseManager dataBaseApi;
    private IArchivingManagerApiRef manager;
    
    private static final short FAIL = 1;
    private static final short SUCCESS = 2;
    private static final short NOTHING_TO_DO = 3;
    
    private static final short MINUTES = 1;
    private static final short HOURS = 2;
    private static final short DAYS = 3;
    
    public TdbCleaner(String[] args) 
    {
        this.tdbUser = args [ 0 ];
        this.tdbPwd = args [ 1 ];
        //this.keepingPeriod = Integer.parseInt(args [ 2 ]);
    }

    /**
     * @param args
     * @throws ArchivingException 
     */
    public static void main(String[] args) throws ArchivingException 
    {
        if ( args == null || args.length != 2 )
        {
            end ( FAIL , "Bad arguments. Usage: java TdbCleaner tdbUser tdbPwd");// keepingPeriod" );
        }
        
        TdbCleaner tdbCleaner = new TdbCleaner ( args );
        tdbCleaner.verify ();
        tdbCleaner.process ();
    }

    private void verify () 
    {
        try 
        {
            this.manager = ArchivingManagerApiRefFactory.getInstance(ConfigConst.TDB);
            this.manager.ArchivingConfigure ( this.tdbUser , this.tdbPwd );
            this.dataBaseApi = this.manager.getDataBase (  );
        } 
        catch (ArchivingException e) 
        {
            e.printStackTrace();
            end ( FAIL , "TdbCleaner/verify/can't connect to the TDB database" );
        }
    }

    private void process () throws ArchivingException 
    {
        //long time = (long)(this.keepingPeriod) * 3600 * 1000;
        
        String[] attributeList = null;
        try
        {
            attributeList = ((TdbMode)this.dataBaseApi.getMode()).getArchivedAtt ();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            end ( FAIL , "TdbCleaner/process/can't load the list of attributes" );
        }
        
        if ( attributeList == null || attributeList.length == 0 )
        {
            end ( NOTHING_TO_DO , "TdbCleaner/process/no attributes to clean" );
        }
        /*for ( int i = 0 ; i < attributeList.length ; i ++ )
        {
            System.out.println("TdbCleaner/next attribute|"+attributeList[i]);
        }*/
        
        try 
        {
        	long time = computeRetentionPeriod();
        	System.out.println("Retention = " + time);
            ((TDBDataBaseManager)this.dataBaseApi).getTdbDeleteAttribute().deleteOldRecords ( time, attributeList );
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            end ( FAIL , "TdbCleaner/process/can't delete the old records" );
        }
        
        if ( ! this.dataBaseApi.getDbConn().getAutoCommit () )
        {
        	Connection conn = null;
            try 
            {
            	conn =  this.dataBaseApi.getDbConn().getConnection();
                ConnectionCommands.commit(conn, DbUtilsFactory.getInstance(this.dataBaseApi.getType()));
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                end ( FAIL , "TdbCleaner/process/can't commit!" );
            }
            finally{
            	if(conn!=null) 
            		this.dataBaseApi.getDbConn().closeConnection(conn);
            }
        }
  
    }

    private static void end(int code, String message) 
    {
        System.out.println ( message );
        switch ( code )
        {
            case FAIL: 
                System.exit ( 1 );
            break;
            
            case SUCCESS:
            case NOTHING_TO_DO:
                System.exit ( 0 );
            break;
            
            default:
                System.out.println ( "Incorrect exit code" );
                System.exit ( 1 );
        }
    }

    /** 
     * Compute the retention period  : the default value is 3 days.
     * @return the retention period 
     */
    protected long computeRetentionPeriod() 
    {	
    short timeUnity = DAYS;
    int timePeriod = 3;
    boolean useDefault = false;
    
    	try 
    	{
    		// Read the retention period property
    		String retentionValue = GetConf.readStringInDB("TdbArchiver", "RetentionPeriod");
    		
    		// Parse it
    		StringTokenizer st = new StringTokenizer ( retentionValue , "/" );
    		String type_s = st.nextToken ();
    		
    		if(type_s != "")
    		{
    			if("hours".equals(type_s.trim())){
    				timeUnity = HOURS;
    			}
    			else if ("minutes".equals(type_s.trim())){
    				timeUnity = MINUTES;
    			}
    			else timeUnity = DAYS;
    			    			
    			// time extraction
    			timePeriod = Integer.parseInt ( st.nextToken () );
    			if(timePeriod <= 0) {
    				System.out.println("Invalid period : default value is used (3 days)");
    				useDefault = true;
    			}
    		}
    	}
    	catch(ArchivingException e)
    	{
    		System.out.println("Properties is not used due to exception : default value is used (3 days)");
    		// Nothing to do use default value
    		useDefault = true;
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception raised " + e + " default value is used (3 days)");
       		useDefault = true;
    	}
    	
    	if(useDefault){
    	   	timeUnity = DAYS;
        	timePeriod = 3;
    	}
    	
    	switch(timeUnity)
    	{
    		case MINUTES:
    			return (long)(timePeriod) * (long)(60 * 1000);
    		case HOURS:
    			return (long)(timePeriod) * (long)(3600 * 1000);
    		default:
    			return (long)(timePeriod) * (long)(24 * 3600 * 1000);
    	}
    }
}
