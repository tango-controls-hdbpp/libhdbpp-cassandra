package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;

/**
 * Modifies the "Max alarm" attribute info property
 * @author CLAISSE 
 */
class MaxAlarmModifier extends AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * @param newMaxAlarm
     */
    MaxAlarmModifier(String newMaxAlarm) 
    {
        super(newMaxAlarm);
    }

    public AttributeInfoWrapper modify ( AttributeInfoWrapper in ) 
    {
        in.setMaxAlarm ( super.newProperty );
        return in;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier#getAttributeInfoPropertyName()
     */
    public String getAttributeInfoPropertyName() 
    {
        return "MAX_ALARM";
    }
}
