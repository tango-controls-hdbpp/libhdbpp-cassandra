package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder;

import java.util.Date;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;

/**
 * A listener that's notified when the attributes to modify are ready for the values setting operation.
 * This implementation stores the write operation's result message (success or failure) for each attribute, but doesn't effectively perform the values setting operation.
 * It is its daughter class NumericAttributesWriteListener that does.
 * @author CLAISSE 
 */
public class NumericValueWrittenMessageBuilder extends AbstractMessageBuilder implements MessageBuilder 
{
    public String getFailureMessage(String targetName, ActionResult actionResult, Throwable e) 
    {
        Double value = actionResult.getNewValue ();
        return dateFormat.format(new Date()) + " : Write " + value + " : FAILED";
    }

    public String getSuccessMessage(String targetName, ActionResult actionResult) 
    {
        Double value = actionResult.getNewValue ();
        return dateFormat.format(new Date()) + " : Write " + value + " : SUCCESS";
    }
}
