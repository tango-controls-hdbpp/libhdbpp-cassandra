package fr.soleil.actiongroup.collectiveaction.oncommand;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionImpl;
import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponse;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic.ExecuteCommand;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.StoreMessageListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.CommandExecutedMessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.BuildCommandGroupListenerImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.BuildGroupListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * An action group that calls a void command on all its targets.
 * @author CLAISSE 
 */
public class CommandGroupImpl extends CollectiveActionImpl implements CommandGroup
{
    /**
     * The command's name
     */
    protected String commandName;
    /**
     * The command's parameters
     */
    protected DeviceDataWrapper commandParameters;
    
    private StoreMessageListener storeMessageActionListener;
    private BuildGroupListener buildGroupResponseListener;
    
    /**
     * @param _proxies The targets of the group void command
     */
    public CommandGroupImpl ( Target[] _proxies )
    {
        super ( _proxies , null  );
    }
    
    public void setCommand ( String _commandName , DeviceDataWrapper _commandParameters ) 
    {
        this.commandName = _commandName;
        this.commandParameters = _commandParameters;
    }

    @Override
    protected IndividualAction getTask(int deviceIndex, ActionListener listener) 
    {
        return new ExecuteCommand ( listener , this.targets [ deviceIndex ] , this.commandName , this.commandParameters );
    }

    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        MessageBuilder messagesBuilder = new CommandExecutedMessageBuilder ( this.commandName );
        storeMessageActionListener = new StoreMessageListener ( super.getBasicListener () , messagesBuilder );
        
        buildGroupResponseListener = new BuildCommandGroupListenerImpl ( storeMessageActionListener );
        return buildGroupResponseListener;
    }
    
    public CollectiveResponse getGroupResponse ()
    {
        if(buildGroupResponseListener == null)
            return null;
            
        return buildGroupResponseListener.getGroupResponse ();
    }

    public Map<String, String> getMessages() 
    {
        return storeMessageActionListener.getMessages();
    }
}
