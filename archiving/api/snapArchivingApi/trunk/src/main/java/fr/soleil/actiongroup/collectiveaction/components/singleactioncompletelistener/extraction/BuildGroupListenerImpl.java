package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponse;
import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponseImpl;
import fr.soleil.actiongroup.collectiveaction.components.response.IndividualResponse;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListenerDecorator;

/**
 * A listener for the event "the attributes (not necessarily numeric) reading operation from a device is complete".
 * If the operation succeeded, a "plugin action" uses the read result.
 * What this plugin action does (if it does anything) depends on the implementation of IPluginAction.
 * @author CLAISSE 
 */
public class BuildGroupListenerImpl extends ActionListenerDecorator implements BuildGroupListener 
{
    private CollectiveResponse response;
    
    public BuildGroupListenerImpl ( ActionListener _decorator ) 
    {
        super ( _decorator );
        this.response = new CollectiveResponseImpl ();
    }

    public synchronized CollectiveResponse getGroupResponse() 
    {
        return this.response;
    }
    
    protected synchronized void addIndividualResponse ( IndividualResponse individualResponse )
    {
        response.addIndividualResponse ( individualResponse );
    }
}
