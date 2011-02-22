package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action;

import java.util.Hashtable;
import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListenerDecorator;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

/**
 * A listener that's notified when the device properties updating operation for a given device is complete.
 * Stores the attribute updating operation's result message (success or failure) for each attribute.
 * @author CLAISSE 
 */
public class StoreMessageListener extends ActionListenerDecorator implements ActionListener 
{
    /**
     * A map which keys are the failed individual task's target, and which values are the correseponding error messages.
     */
    private Map<String, String> actionResultMessages;
    private MessageBuilder messagesBuilder;
    
    public StoreMessageListener ( ActionListener _decorator , MessageBuilder _messagesBuilder ) 
    {
        super ( _decorator );
        this.messagesBuilder = _messagesBuilder;
        this.actionResultMessages = new Hashtable<String, String> ();
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.AbstractActionCompleteListener#actionFailed(java.lang.String, java.lang.Object, java.lang.Throwable)
     */
    public void actionFailed(String targetName , ActionResult actionResult , Throwable e) 
    {
        super.actionFailed ( targetName , actionResult , e );
                
        String message = this.messagesBuilder.getFailureMessage ( targetName , actionResult , e );
        if ( message != null )
        {
            this.addActionResultMessage ( targetName , message );
        }
    }

    public void actionSucceeded(String targetName, ActionResult actionResult) 
    {
        String message = this.messagesBuilder.getSuccessMessage ( targetName , actionResult );
        if ( message != null )
        {
            this.addActionResultMessage ( targetName , message );
        }
    }
    
    /**
     * Stores a new error message.
     * @param targetName The failed individual task's target
     * @param actionResultMessage The failure message
     */
    protected void addActionResultMessage ( String targetName , String actionResultMessage )
    {
        synchronized ( this )
        {
            this.actionResultMessages.put ( targetName , actionResultMessage );
        }
        
    }
    
    /**
     * @return A map which keys are the failed individual task's target, and which values are the correseponding error messages.
     */
    public Map<String, String> getMessages() 
    {
        return actionResultMessages;
    }
}
