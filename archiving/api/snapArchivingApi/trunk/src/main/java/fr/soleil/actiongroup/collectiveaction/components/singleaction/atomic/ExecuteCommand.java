package fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.MinimalistActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * Executes a void (no return) command on a device. Notifies its listener on completion. 
 * @author CLAISSE 
 */
public class ExecuteCommand extends AtomicIndividualAction 
{
    /**
     * The command's name
     */
    private String commandName;
    
    /**
     * The command's parameters
     */
    private DeviceDataWrapper commandParameters;

    /**
     * @param _listener The listener to notify on command execution completion
     * @param _deviceToReadfrom The device to call the command on
     * @param _commandName The command's name
     * @param _commandParameters The command's parameters
     */
    public ExecuteCommand ( MinimalistActionListener _listener , Target _deviceToReadfrom , String _commandName , DeviceDataWrapper _commandParameters )
    {
        super ( _listener , _deviceToReadfrom );
        this.commandName = _commandName;
        this.commandParameters = _commandParameters;
    }

    @Override
    protected ActionResult executeAtomicAction() throws Throwable 
    {
        DeviceDataWrapper response = this.target.command_inout ( this.commandName , this.commandParameters );
        return new ActionResult ( response );
    }

}