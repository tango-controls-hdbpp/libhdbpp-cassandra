package fr.soleil.actiongroup.collectiveaction.onattributes;

import java.util.Hashtable;
import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic.ReadAttributes;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.StoreMessageListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.WriteNumericValueListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.NumericValueWrittenMessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * An action group that set its member attributes to a new value.
 * Those attributes need to be numeric or boolean (true=1, false=anything else).
 * @author CLAISSE 
 */
public class WriteNumericAttributesImpl extends CollectiveActionImpl implements WriteNumericAttributes 
{
    /**
     * The attributes new value
     */
    private double newValue;
    
    private StoreMessageListener storeMessageActionListener;
    
    /**
     * @param _targets The target devices
     * @param _attributes The attributes to modify
     */
    public WriteNumericAttributesImpl ( Target[] _targets , String [][] _attributes )
    {
        super ( _targets , _attributes );
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.write.numeric.IWriteNumericAttributesGroup#setNewValue(double)
     */
    public synchronized void setNewValue ( double _newValue ) 
    {
        this.newValue = _newValue;
    }
    
    @Override
    protected IndividualAction getTask ( int deviceIndex , ActionListener listener ) 
    {
        return new ReadAttributes ( listener , this.targets [ deviceIndex ] , this.attributes [ deviceIndex ] );
    }

    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        MessageBuilder messagesBuilder = new NumericValueWrittenMessageBuilder ();
        storeMessageActionListener = new StoreMessageListener ( super.getBasicListener () , messagesBuilder );
        
        Map<String, Target> deviceNameToIndexMap = this.buildDevicesMap ();
        return new WriteNumericValueListener ( storeMessageActionListener , this.newValue , deviceNameToIndexMap );
    }

    private Map<String, Target> buildDevicesMap() 
    {   
        int n = this.targets.length;
        Map<String, Target> ret = new Hashtable <String, Target> ( n );
        for ( int i = 0 ; i < n ; i ++ )
        {
            ret.put ( this.targets [ i ].get_name () , this.targets [ i ] );
        }
        
        return ret;
    }

    public synchronized Map<String, String> getMessages() 
    {
        return storeMessageActionListener.getMessages();
    }  
}
