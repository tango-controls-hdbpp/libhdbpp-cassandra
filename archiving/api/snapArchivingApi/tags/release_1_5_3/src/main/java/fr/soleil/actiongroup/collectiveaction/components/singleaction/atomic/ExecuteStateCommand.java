package fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.MinimalistActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * Executes a void (no return) command on a device. Notifies its listener on completion. 
 * @author CLAISSE 
 */
public class ExecuteStateCommand extends ExecuteCommand 
{
    /**
     * @param _listener The listener to notify on command execution completion
     * @param _deviceToReadfrom The device to call the command on
     * @param _commandName The command's name
     * @param _commandParameters The command's parameters
     */
    public ExecuteStateCommand ( MinimalistActionListener _listener , Target _deviceToReadfrom )
    {
        super ( _listener , _deviceToReadfrom , "State" , null);
    }
}