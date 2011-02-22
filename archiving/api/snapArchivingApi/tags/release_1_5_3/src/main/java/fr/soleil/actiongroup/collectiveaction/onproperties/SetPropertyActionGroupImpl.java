package fr.soleil.actiongroup.collectiveaction.onproperties;

import java.util.Hashtable;
import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.molecular.SetProperties;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.BasicActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.StoreMessageListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.DevicePropertyUpdatedMessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DbDatumWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * An action group that sets a list of device attributes for all its targets.
 * @author CLAISSE 
 */
public class SetPropertyActionGroupImpl extends CollectiveActionImpl implements SetPropertyGroup 
{
    /**
     * The property name --> value mapping for the properties to update
     */
    private Map<String,DbDatumWrapper> properties;
    
    private StoreMessageListener storeMessageActionListener;
    
    /**
     * @param _proxies The targets of the attributes updating
     */
    public SetPropertyActionGroupImpl ( Target[] _proxies )
    {
        super ( _proxies , null );
        this.properties = new Hashtable <String,DbDatumWrapper> ();
    }
    
    public void setProperties(Map<String, DbDatumWrapper> _properties) 
    {
        this.properties = _properties;
    }

    @Override
    protected IndividualAction getTask(int deviceIndex, ActionListener listener) 
    {
        return new SetProperties ( listener , this.targets [ deviceIndex ] , this.properties );
    }

    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        ActionListener basic = new BasicActionListener ( super.latch );
        MessageBuilder messagesBuilder = new DevicePropertyUpdatedMessageBuilder ();
        
        storeMessageActionListener = new StoreMessageListener ( basic , messagesBuilder ); 
        return storeMessageActionListener;
    }

    public Map<String, String> getMessages() 
    {
        return storeMessageActionListener.getMessages();
    }
}
