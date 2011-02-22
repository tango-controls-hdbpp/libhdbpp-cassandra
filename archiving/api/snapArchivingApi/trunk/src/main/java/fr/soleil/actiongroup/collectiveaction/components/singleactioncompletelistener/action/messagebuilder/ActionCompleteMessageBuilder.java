package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

public class ActionCompleteMessageBuilder extends AbstractMessageBuilder implements MessageBuilder 
{
    public String getSuccessMessage ( String targetName , ActionResult actionResult ) 
    {    
        return null;
    }
    
    public String getFailureMessage ( String targetName , ActionResult actionResult , Throwable e ) 
    {    
        return e.getMessage ();
    }
}
