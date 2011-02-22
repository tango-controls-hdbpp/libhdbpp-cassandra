package fr.soleil.actiongroup.collectiveaction.oncommand;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionWithMessages;
import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponse;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;

/**
 * A group that calls a void command on all its targets.
 * @author CLAISSE 
 */
public interface CommandGroup extends CollectiveActionWithMessages
{
    /**
     * Sets up the command that will be executed
     * @param commandName The command's name
     * @param commandParameters The command's parameters
     */
    public void setCommand ( String commandName , DeviceDataWrapper commandParameters );
    
    public CollectiveResponse getGroupResponse ();
}
