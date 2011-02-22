package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder;

import java.util.Date;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

public class AttributePropertyUpdatedMessageBuilder extends AbstractMessageBuilder implements MessageBuilder 
{
    public String getSuccessMessage ( String targetName , ActionResult actionResult ) 
    {    
        String message = dateFormat.format(new Date()) + " : Update property : SUCCESS";
        return message;
    }
    
    public String getFailureMessage ( String targetName , ActionResult actionResult , Throwable e ) 
    {    
        String message = dateFormat.format(new Date()) + " : Update property : FAILURE";
        return message;
    }
}
