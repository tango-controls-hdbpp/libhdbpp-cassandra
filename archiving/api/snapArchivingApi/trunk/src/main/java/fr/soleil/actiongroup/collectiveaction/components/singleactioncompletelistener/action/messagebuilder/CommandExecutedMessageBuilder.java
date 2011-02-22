package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder;

import java.util.Date;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

public class CommandExecutedMessageBuilder extends AbstractMessageBuilder implements MessageBuilder 
{
    private String commandName;
    
    public CommandExecutedMessageBuilder ( String _commandName )
    {
        this.commandName = _commandName;   
    }

    public String getSuccessMessage ( String targetName , ActionResult actionResult ) 
    {    
        //return null;
        String message = targetName + "->" + dateFormat.format(new Date()) + " : Command " + this.commandName + " result SUCCESS";
        return message;
    }
    
    public String getFailureMessage ( String targetName , ActionResult actionResult , Throwable e ) 
    {    
        String message = targetName + "->" + dateFormat.format(new Date()) + " : Command " + this.commandName + " result FAILURE";
        return message;
    }
}
