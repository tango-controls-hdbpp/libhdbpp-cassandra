package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;

/**
 * Modifies the "Min value" attribute info property
 * @author CLAISSE 
 */
class MinValueModifier extends AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * @param newMinValue
     */
    MinValueModifier(String newMinValue) 
    {
        super(newMinValue);
    }

    public AttributeInfoWrapper modify ( AttributeInfoWrapper in ) 
    {
        in.setMinValue ( super.newProperty );
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
