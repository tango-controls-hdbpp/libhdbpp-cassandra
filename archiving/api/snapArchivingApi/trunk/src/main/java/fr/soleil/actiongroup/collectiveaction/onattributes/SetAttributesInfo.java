package fr.soleil.actiongroup.collectiveaction.onattributes;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionWithMessages;
import fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier.AttributeInfoModifier;

/**
 * A group that set attributes info properties for all its members.
 * The property to modify needs to be the same for every attribute.
 * Uses an instance of IAttributeInfoModifier to perform the property change.
 * @author CLAISSE 
 */
public interface SetAttributesInfo extends CollectiveActionWithMessages
{
    /**
     * Sets the IAttributeInfoModifier to use to modify each attribute's info property
     * @param modifier
     */
    public void setAttributeInfoModifier ( AttributeInfoModifier modifier );
}
