package fr.soleil.mambo.datasources.tango.alternate;

public class ThreadedBufferedTangoAlternateSelectionManager extends BufferedTangoAlternateSelectionManager implements ITangoAlternateSelectionManager, IPreBufferingEventSource
{   
    ThreadedBufferedTangoAlternateSelectionManager ( ITangoAlternateSelectionManager _tasm ) 
    {
        super ( _tasm );
    }

    protected void doPreBuffering ()
    {
        DoPreBufferingRunnable preBufferingRunnable = new DoPreBufferingRunnable ();
        Thread preBufferingThread = new Thread ( preBufferingRunnable , "MamboPreBufferingThread" );
        
        Thread.currentThread ().setPriority ( Thread.MAX_PRIORITY );
        preBufferingThread.setPriority ( Thread.MIN_PRIORITY );
        
        preBufferingThread.start (); 
    }
    
    private class DoPreBufferingRunnable implements Runnable
    {
        public synchronized void run() 
        {
            ThreadedBufferedTangoAlternateSelectionManager.super.doPreBuffering ();
        }
    }
    
}
