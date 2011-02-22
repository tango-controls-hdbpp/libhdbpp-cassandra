package fr.soleil.actiongroup.collectiveaction.onproperties;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionWithMessages;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DbDatumWrapper;

/**
 * A group that sets a list of device attributes for all its targets.
 * @author CLAISSE 
 */
public interface SetPropertyGroup extends CollectiveActionWithMessages
{
    /**
     * Sets the properties to update.
     * @param properties The property name --> value mapping
     */
    public void setProperties ( Map <String,DbDatumWrapper> properties );
}
