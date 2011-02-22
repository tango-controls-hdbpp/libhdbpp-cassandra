/*  Synchrotron Soleil 
 *  
 *   File          :  TangoGroupCLA.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  8 déc. 06 
 *  
 *   Revision:                      Author:  
 *   Date:                          State:  
 *  
 *   Log: TangoGroupCLA.java,v 
 *
 */
 /*
 * Created on 8 déc. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes;

import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.StoreMessageListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.UsePluginListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.ActionCompleteMessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.Plugin;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PluginContext;

/**
 * An action group that read attributes of any types from its members.
 * It can use an IPluginAction implementation to call specific actions on each attribute.
 * @author CLAISSE 
 */
public class UsePluginImpl extends ReadAttributesImpl implements UsePlugin
{
    /**
     * The action to perform on each attribute 
     */
    private Plugin pluginAction;
    private PluginContext context;
    
    /**
     * @param _targets The devices to read from
     * @param _attributes The list of attributes to read for each device
     * @param _pluginAction The action to perform on each attribute 
     */
    public UsePluginImpl ( Target[] _targets, String[][] _attributes , Plugin _pluginAction ) 
    {
        super ( _targets , _attributes );
        this.pluginAction = _pluginAction;
    }
    
    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        MessageBuilder messagesBuilder = new ActionCompleteMessageBuilder ();
        ActionListener loggingListener = new StoreMessageListener ( super.getBasicListener () , messagesBuilder );
        
        return new UsePluginListener ( loggingListener , this.pluginAction , this.context );
    }

    public synchronized Map<String, String> getMessages() 
    {
        return null;//TO DO?
    }

    public synchronized void setPluginContext(PluginContext _context) 
    {
        this.context = _context;
    }
}
