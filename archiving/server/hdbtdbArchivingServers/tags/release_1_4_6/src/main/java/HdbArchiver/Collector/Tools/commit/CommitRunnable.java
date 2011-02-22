package HdbArchiver.Collector.Tools.commit;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class CommitRunnable implements Runnable
{
    private long commitPeriod;
    private DataBaseApi dbApi;
    
    public CommitRunnable ( long _commitPeriod , DataBaseApi _dbApi )
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
        //System.out.println ( "CommitRunnable/commit/"+new Timestamp (before) );
        try 
        {
            this.dbApi.commit ();
        } 
        catch (ArchivingException e) 
        {
            e.printStackTrace();
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