/*	Synchrotron Soleil 
 *  
 *   File          :  AttributeInfoModifierFactory.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  16 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: AttributeInfoModifierFactory.java,v 
 *
 */
 /*
 * Created on 16 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier;

/**
 * A factory used to create various kinds of IAttributeInfoModifier instances
 * @author CLAISSE 
 */
public class AttributeInfoModifierFactory 
{
    /**
     * A modifier that modifies the "Format" attribute info property
     */
    public static final short FORMAT = 1;
    /**
     * A modifier that modifies the "Label" attribute info property
     */
    public static final short LABEL = 2;
    /**
     * A modifier that modifies the "Min alarm" attribute info property
     */
    public static final short MIN_ALARM = 3;
    /**
     * A modifier that modifies the "Max alarm" attribute info property
     */
    public static final short MAX_ALARM = 4;
    /**
     * A modifier that modifies the "Min value" attribute info property
     */
    public static final short MIN_VALUE = 5;
    /**
     * A modifier that modifies the "Max value" attribute info property
     */
    public static final short MAX_VALUE = 6;
    /**
     * A modifier that modifies the "Unit" attribute info property
     */
    public static final short UNIT = 7;
    
    /**
     * @param value The new format
     * @return A Modifier that sets the attribute's "Format" property to value
     */
    public static AttributeInfoModifier getFormatModifier(String value) 
    {
        return new FormatModifier ( value );
    }
    
    /**
     * @param value The new label
     * @return A Modifier that sets the attribute's "Label" property to value
     */
    public static AttributeInfoModifier getLabelModifier(String value) 
    {
        return new LabelModifier ( value );
    }
    
    /**
     * @param value The new format
     * @return A Modifier that sets the attribute's "Min alarm" property to value
     */
    public static AttributeInfoModifier getMinAlarmModifier(String value) 
    {
        return new MinAlarmModifier ( value );
    }
    
    /**
     * @param value The new format
     * @return A Modifier that sets the attribute's "Max alarm" property to value
     */
    public static AttributeInfoModifier getMaxAlarmModifier(String value) 
    {
        return new MaxAlarmModifier ( value );
    }
    
    /**
     * @param value The new format
     * @return A Modifier that sets the attribute's "Min value" property to value
     */
    public static AttributeInfoModifier getMinValueModifier(String value) 
    {
        return new MinValueModifier ( value );
    }
    
    /**
     * @param value The new format
     * @return A Modifier that sets the attribute's "Max value" property to value
     */
    public static AttributeInfoModifier getMaxValueModifier(String value) 
    {
        return new MaxValueModifier ( value );
    }
    
    /**
     * @param value The new format
     * @return A Modifier that sets the attribute's "Unit" property to value
     */
    public static AttributeInfoModifier getUnitModifier(String value) 
    {
        return new UnitModifier ( value );
    }
}
