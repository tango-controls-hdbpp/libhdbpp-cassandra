package fr.soleil.actiongroup.collectiveaction.onattributes;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionWithMessages;

/**
 * A group that set its member attributes to a new value.
 * Those attributes need to be numeric or boolean (true=1, false=anything else).
 * @author CLAISSE 
 */
public interface WriteNumericAttributes extends CollectiveActionWithMessages
{
    /**
     * Sets the new value
     * @param newValue The new value
     */
    public void setNewValue ( double newValue );
}
