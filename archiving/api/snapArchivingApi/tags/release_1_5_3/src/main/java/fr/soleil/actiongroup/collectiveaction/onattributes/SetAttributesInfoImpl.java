/*	Synchrotron Soleil 
 *  
 *   File          :  TangoGroupCLAForSettingAttributeInfo.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  15 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: TangoGroupCLAForSettingAttributeInfo.java,v 
 *
 */
 /*
 * Created on 15 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.molecular.SetAttributesInfos;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.StoreMessageListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.AttributePropertyUpdatedMessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;
import fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier.AttributeInfoModifier;

/**
 * An action group that set attributes info properties for all its members.
 * The property to modify needs to be the same for every attribute.
 * Uses an instance of IAttributeInfoModifier to perform the property change.
 * @author CLAISSE 
 */
public class SetAttributesInfoImpl extends CollectiveActionImpl implements SetAttributesInfo 
{
    /**
     * The attribute info property modifier
     */
    private AttributeInfoModifier modifier; 
    
    private StoreMessageListener storeMessageActionListener;
    
    /**
     * @param _targets The target devices
     * @param _attributes The attributes to modify
     */
    public SetAttributesInfoImpl ( Target[] _targets , String [][] _attributes ) 
    {
        super ( _targets , _attributes );
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.info.ISetAttributesInfoGroup#setAttributeInfoModifier(fr.soleil.core.groupactions.apis.group.attributes.write.info.modifiers.IAttributeInfoModifier)
     */
    public synchronized void setAttributeInfoModifier ( AttributeInfoModifier _modifier ) 
    {
        this.modifier = _modifier;
    }
    
    @Override
    protected IndividualAction getTask ( int deviceIndex , ActionListener listener ) 
    {
        return new SetAttributesInfos ( listener , this.targets [ deviceIndex ] , modifier , this.attributes [ deviceIndex ] );
    }

    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        MessageBuilder messagesBuilder = new AttributePropertyUpdatedMessageBuilder ();
        storeMessageActionListener = new StoreMessageListener ( super.getBasicListener () , messagesBuilder ); 
        return storeMessageActionListener;
    }
    
    public synchronized Map<String, String> getMessages() 
    {
        return storeMessageActionListener.getMessages();
    }
}
