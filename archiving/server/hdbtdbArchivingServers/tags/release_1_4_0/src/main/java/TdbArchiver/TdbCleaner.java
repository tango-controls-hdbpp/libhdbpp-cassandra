package TdbArchiver;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TdbCleaner 
{
    private String tdbUser;
    private String tdbPwd;
    private int keepingPeriod;
    
    private DataBaseApi dataBaseApi;
    private ArchivingManagerApi manager;
    
    private static final short FAIL = 1;
    private static final short SUCCESS = 2;
    private static final short NOTHING_TO_DO = 3;
    
    public TdbCleaner(String[] args) 
    {
        this.tdbUser = args [ 0 ];
        this.tdbPwd = args [ 1 ];
        this.keepingPeriod = Integer.parseInt(args [ 2 ]);
    }

    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        if ( args == null || args.length != 3 )
        {
            end ( FAIL , "Bad arguments. Usage: java TdbCleaner tdbUser tdbPwd keepingPeriod" );
        }
        
        TdbCleaner tdbCleaner = new TdbCleaner ( args );
        tdbCleaner.verify ();
        tdbCleaner.process ();
    }

    private void verify () 
    {
        try 
        {
            this.manager = new ArchivingManagerApi ();
            this.manager.ArchivingConfigure ( this.tdbUser , this.tdbPwd , false );
            this.dataBaseApi = this.manager.getDataBase ( false );
        } 
        catch (ArchivingException e) 
        {
            e.printStackTrace();
            end ( FAIL , "TdbCleaner/verify/can't connect to the TDB database" );
        }
    }

    private void process () 
    {
        long time = this.keepingPeriod * 3600 * 1000;
        
        String[] attributeList = null;
        try
        {
            attributeList = this.dataBaseApi.getCurrentArchivedAtt (false);
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
            this.dataBaseApi.deleteOldRecords ( time, attributeList );
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            end ( FAIL , "TdbCleaner/process/can't delete the old records" );
        }
        
        if ( ! this.dataBaseApi.getAutoCommit () )
        {
            try 
            {
                this.dataBaseApi.commit();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                end ( FAIL , "TdbCleaner/process/can't commit!" );
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

}
