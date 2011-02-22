/*	Synchrotron Soleil 
 *  
 *   File          :  CommitThread.java
 *  
 *   Project       :  archiving
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  23 nov. 06 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: CommitThread.java,v 
 *
 */
 /*
 * Created on 23 nov. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package HdbArchiver.Collector.Tools.commit;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseApi;

public class CommitThread extends Thread
{
    public CommitThread ( long _commitPeriod , DataBaseApi _dbApi )
    {
        super ( new CommitRunnable ( _commitPeriod , _dbApi ) );
        super.setPriority ( Thread.MIN_PRIORITY );
    }
    
}
