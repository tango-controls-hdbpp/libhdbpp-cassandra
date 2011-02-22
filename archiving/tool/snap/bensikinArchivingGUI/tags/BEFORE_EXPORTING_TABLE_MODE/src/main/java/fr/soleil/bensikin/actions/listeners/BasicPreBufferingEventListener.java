package fr.soleil.bensikin.actions.listeners;

import fr.soleil.bensikin.containers.context.ContextAttributesPanelAlternate;
import fr.soleil.bensikin.datasources.tango.IPreBufferingEventListener;

public class BasicPreBufferingEventListener implements IPreBufferingEventListener 
{
    public BasicPreBufferingEventListener() 
    {
        super();
    }

    public void allDone() 
    {
        System.out.println ( "PreBufferingEventListener/allDone" );
        //ILogger logger = LoggerFactory.getCurrentImpl ();
        //String msg = "Buffering of table selection complete";
        //logger.trace ( ILogger.LEVEL_INFO , msg );
    }

    public void stepDone(int step, int totalSteps) 
    {
        System.out.println ( "PreBufferingEventListener/stepDone/step/"+step+"/totalSteps/"+totalSteps );
        ContextAttributesPanelAlternate.setBufferingStatus ( step , totalSteps );
    }

    public String getName() 
    {
        return "Mambo PreBufferingEventListener";
    }
}
