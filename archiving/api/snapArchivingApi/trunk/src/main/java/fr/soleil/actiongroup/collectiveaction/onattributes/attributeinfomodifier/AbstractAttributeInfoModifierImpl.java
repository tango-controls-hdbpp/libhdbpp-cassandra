package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;


/**
 * An abstract implementation 
 * @author CLAISSE 
 */
abstract class AbstractAttributeInfoModifierImpl implements AttributeInfoModifier 
{
    /**
     * The property's new value
     */
    protected String newProperty;
    
    /**
     * @param _newProperty The property's new value
     */
    protected AbstractAttributeInfoModifierImpl ( String _newProperty )
    {
        this.newProperty = _newProperty;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier#getAttributeInfoPropertyValue()
     */
    public String getAttributeInfoPropertyValue() 
    {
        return this.newProperty;
    }
}
