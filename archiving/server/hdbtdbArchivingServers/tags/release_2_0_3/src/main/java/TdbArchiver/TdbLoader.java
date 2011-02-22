/*	Synchrotron Soleil 
 *  
 *   File          :  TdbLoader.java
 *  
 *   Project       :  archiving
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  19 oct. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: TdbLoader.java,v 
 *
 */
 /*
 * Created on 19 oct. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package TdbArchiver;

import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

import TdbArchiver.Collector.DbProxy;
import TdbArchiver.Collector.Tools.FileTools;
import TdbArchiver.Collector.Tools.FileTools.ExportTask;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.DoNothingLogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.TDBDataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.ITDBAdtAptAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TdbLoader 
{
    private String tdbUser;
    private String tdbPwd;
    
    private String workingPath;
    private String dbPath;
    private File workingDirectory;
    private File [] filesToLoad;
    private int numberOfFilesToLoad;
    private DataBaseManager dataBaseApi;
    private IArchivingManagerApiRef manager;
    
    private static final short FAIL = 1;
    private static final short SUCCESS = 2;
    private static final short NOTHING_TO_DO = 3;
    
    public TdbLoader(String[] args) 
    {
        this.workingPath = args [ 0 ];
        this.dbPath = args [ 1 ];
        this.tdbUser = args [ 2 ];
        this.tdbPwd = args [ 3 ];
    }

    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        if ( args == null || args.length != 4 )
        {
            TdbLoader.end ( TdbLoader.FAIL , "Bad arguments. Usage: java TdbLoader workingPath dbPath tdbUser tdbPwd" );
        }
        
        TdbLoader tdbLoader = new TdbLoader ( args );
        tdbLoader.verify ();
        tdbLoader.process ();
    }

    private void verify () 
    {
        this.workingDirectory = new File ( this.workingPath );
        if ( ! this.workingDirectory.isDirectory () )
        {
            TdbLoader.end ( TdbLoader.FAIL , "TdbLoader/verify/The working path isn't a directory|" + this.workingPath + "|" );
        }
        if ( ! this.workingDirectory.isDirectory () )
        {
            TdbLoader.end ( TdbLoader.FAIL , "TdbLoader/verify/The working path can't be read|" + this.workingPath + "|" );
        }
        
        FilenameFilter filter = new TmpFilesFilter ();
        this.filesToLoad = this.workingDirectory.listFiles ( filter );
        if ( this.filesToLoad == null || this.filesToLoad.length == 0 )
        {
            TdbLoader.end ( TdbLoader.NOTHING_TO_DO , "TdbLoader/verify/No .dat files to load in the working directory|" + this.workingPath + "|" );            
        }
        
        this.numberOfFilesToLoad = this.filesToLoad.length;
        
        try 
        {
        	this.manager = ArchivingManagerApiRefFactory.getInstance(ConfigConst.TDB);
            this.manager.ArchivingConfigure ( this.tdbUser , this.tdbPwd );
            this.dataBaseApi = this.manager.getDataBase (  );
        } 
        catch (ArchivingException e) 
        {
            e.printStackTrace();
            TdbLoader.end ( TdbLoader.FAIL , "TdbLoader/verify/can't connect to the TDB database with tdbUser|" + this.tdbUser + "|tdbPwd|" + this.tdbPwd );
        }
    }

    private void process () 
    {
        for ( int i = 0 ; i < this.numberOfFilesToLoad ; i ++ )
        {
            processFile ( this.filesToLoad [ i ] );
            this.filesToLoad [ i ].delete ();
        }
    }

    private void processFile(File file) 
    {
        String fileName = file.getName ();
        System.out.println ( "TdbLoader/processFile/processing file|" + fileName + "|" );
        String tableName = this.getTableName ( fileName );
        
        try
        {
            int [] tfw = this.getTypeFormatWritable ( tableName );
            int dataFormat = tfw [ 1 ];
            int writable = tfw [ 2 ];
            long windowsDuration = 0;
            ILogger logger = new DoNothingLogger ();
            
            DbProxy dbProxy = new DbProxy ();
            FileTools fileTools = new FileTools ( tableName , dataFormat  , writable , windowsDuration , logger , false , dbProxy );
            FileTools.set_remoteFilePath ( this.dbPath );
            
            ExportTask exportTask = fileTools.new ExportTask ( fileName , false );
            exportTask.execute ();    
            
            ((TDBDataBaseManager)this.manager.getDataBase()).getTdbExport().forceDatabaseToImportFile ( tableName );
        }
        catch ( ArchivingException e )
        {
            failFile ( fileName , e );
        }
    }

    private void failFile(String nextFilePath, ArchivingException e) 
    {
        System.out.println ( "Failed to load file |" + nextFilePath + "|" );
        e.printStackTrace ();
    }

    private int[] getTypeFormatWritable(String tableName) throws ArchivingException 
    {
        StringTokenizer st = new StringTokenizer ( tableName , "_" );
        st.nextToken ();
        String id_s = st.nextToken ();
        int id = Integer.parseInt ( id_s );
        //System.out.println ( "getTypeFormatWritable/id|" + id + "|" );
        
        return ((ITDBAdtAptAttribute)this.dataBaseApi.getAttribute()).getAtt_TFW_Data_By_Id ( id );
    }

    private String getTableName(String nextFilePath) 
    {
        StringTokenizer st = new StringTokenizer ( nextFilePath , "-" );
        //att_20-1161185627648.dat
        return st.nextToken ();
    }

    private class TmpFilesFilter implements FilenameFilter 
    {
        public boolean accept(File dir, String name) 
        {
            return name.endsWith ( ".dat" );
        }
    }
    
    private static void end(int code, String message) 
    {
        System.out.println ( message );
        switch ( code )
        {
            case TdbLoader.FAIL: 
                System.exit ( 1 );
            break;
            
            case TdbLoader.SUCCESS:
            case TdbLoader.NOTHING_TO_DO:
                System.exit ( 0 );
            break;
            
            default:
                System.out.println ( "Incorrect exit code" );
                System.exit ( 1 );
        }
    }

}
