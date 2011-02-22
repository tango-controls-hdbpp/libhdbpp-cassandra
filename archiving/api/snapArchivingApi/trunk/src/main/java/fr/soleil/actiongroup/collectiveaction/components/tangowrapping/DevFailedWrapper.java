package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.Tango.DevFailed;

public class DevFailedWrapper extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DevFailed devFailed;
    
    public DevFailedWrapper ()
    {

    }
    
    public DevFailedWrapper ( DevFailed _devFailed )
    {
        this.devFailed = _devFailed;
    }
    
    public DevFailed getDevFailed ()
    {
        return this.devFailed;
    }

    public void initCause(Exception cause) 
    {
        super.initCause(cause);
        //this.devFailed.initCause(cause);
    }

    public static DevErrorWrapper[] wrapErrors(Throwable e) 
    {
        DevErrorWrapper [] wrapped = null;
        if ( e instanceof DevFailed )
        {
            wrapped = DevErrorWrapper.fillWrapper ( ( (DevFailed) e ).errors );
        }
        return wrapped;
    }
}
