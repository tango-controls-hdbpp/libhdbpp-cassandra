package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;

/**
 * Modifies the "Min alarm" attribute info property
 * @author CLAISSE 
 */
class MinAlarmModifier extends AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * @param newMinAlarm
     */
    MinAlarmModifier(String newMinAlarm) 
    {
        super(newMinAlarm);
    }

    public AttributeInfoWrapper modify ( AttributeInfoWrapper in ) 
    {
        in.setMinAlarm ( super.newProperty );
        return in;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier#getAttributeInfoPropertyName()
     */
    public String getAttributeInfoPropertyName() 
    {
        return "MIN_ALARM";
    }
}
