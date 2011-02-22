package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;

/**
 * Modifies the "Unit" attribute info property
 * @author CLAISSE 
 */
class UnitModifier extends AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * @param newUnit
     */
    UnitModifier(String newUnit) 
    {
        super(newUnit);
    }

    public AttributeInfoWrapper modify ( AttributeInfoWrapper in ) 
    {
        in.setUnit ( super.newProperty );
        return in;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier#getAttributeInfoPropertyName()
     */
    public String getAttributeInfoPropertyName() 
    {
        return "UNIT";
    }
}
