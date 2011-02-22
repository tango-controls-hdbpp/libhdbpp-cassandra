package fr.soleil.actiongroup.collectiveaction.onattributes;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities.AttributeQualityRegister;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttrQualityWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * An action group that reads the value and quality of numeric attributes from its members.
 * Uses as implementation of IAttributeQualityRegister an instance of AttributeQualityRegister, which stores the read attributes qualities
 * @author CLAISSE 
 */
public class ReadNumericAttributesAndQualitiesImpl extends ReadNumericAttributesImpl implements ReadNumericAttributesAndQualities
{    
    /**   
     * @param _targets The devices to read from
     * @param _attributes The list of attributes to read for each device
     */
    public ReadNumericAttributesAndQualitiesImpl(Target[] _targets, String[][] _attributes) 
    {
        super ( _targets , _attributes );
        super.attributeQualityRegister = new AttributeQualityRegister ();
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.attributes.read.quality.IAttributeQualityReader#getQualities()
     */
    public synchronized Map<String, AttrQualityWrapper> getQualities() 
    {
        return this.attributeQualityRegister.getQualities ();
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.attributes.read.quality.IAttributeQualityReader#getQuality(java.lang.String)
     */
    public synchronized AttrQualityWrapper getQuality(String attributeName) 
    {
        return this.attributeQualityRegister.getQuality ( attributeName );
    }
}
