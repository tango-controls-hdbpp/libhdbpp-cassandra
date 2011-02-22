package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;

/**
 * Modifies the "Max value" attribute info property
 * @author CLAISSE 
 */
class MaxValueModifier extends AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * @param newMaxValue
     */
    MaxValueModifier(String newMaxValue) 
    {
        super(newMaxValue);
    }

    public AttributeInfoWrapper modify ( AttributeInfoWrapper in ) 
    {
        in.setMaxValue ( super.newProperty );
        return in;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier#getAttributeInfoPropertyName()
     */
    public String getAttributeInfoPropertyName() 
    {
        return "MAX_VALUE";
    }
}
