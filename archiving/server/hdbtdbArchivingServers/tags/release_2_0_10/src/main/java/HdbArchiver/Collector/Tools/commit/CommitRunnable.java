package HdbArchiver.Collector.Tools.commit;

import java.sql.Connection;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class CommitRunnable implements Runnable
{
    private long commitPeriod;
    private DataBaseManager dbApi;
    
    public CommitRunnable ( long _commitPeriod , DataBaseManager _dbApi )
    {
        this.commitPeriod = _commitPeriod * 1000 * 60;
        this.dbApi = _dbApi;
    }
    
    public void run () 
    {
        while ( true )
        {
            long commitTime = this.commit ();
            long sleepDuration = Math.max ( this.commitPeriod - commitTime , 0 );
            this.doSleep ( sleepDuration );
        }
    }
    
    private long commit() 
    {
        long before = System.currentTimeMillis ();
        Connection conn = null;
        //System.out.println ( "CommitRunnable/commit/"+new Timestamp (before) );
        try 
        {
        	conn = this.dbApi.getDbConn().getConnection();
           ConnectionCommands.commit (conn, this.dbApi.getDbUtil());
        } 
        catch (ArchivingException e) 
        {
            e.printStackTrace();
        }
        finally{
        	if(conn!=null)
				try {
					this.dbApi.getDbConn().closeConnection(conn);
				} catch (ArchivingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        long after = System.currentTimeMillis ();
        return after - before;
    }

    private void doSleep ( long sleepDuration )
    {
        try 
        {
            Thread.sleep ( sleepDuration );
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
}