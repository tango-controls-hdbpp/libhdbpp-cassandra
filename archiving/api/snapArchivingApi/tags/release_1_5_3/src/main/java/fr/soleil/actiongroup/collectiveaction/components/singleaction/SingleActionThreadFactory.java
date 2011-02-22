package fr.soleil.actiongroup.collectiveaction.components.singleaction;

import java.util.concurrent.ThreadFactory;

/**
 * Creates daemon threads.
 * The priority is Thread.MAX_PRIORITY
 * @author CLAISSE 
 */
public class SingleActionThreadFactory implements ThreadFactory 
{
    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    public Thread newThread(Runnable r) 
    {
        Thread ret = new Thread ( r );
        ret.setDaemon ( true );
        ret.setPriority ( Thread.MAX_PRIORITY );
        return ret;
    }
}
