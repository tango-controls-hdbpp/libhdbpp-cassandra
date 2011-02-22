package fr.soleil.actiongroup.collectiveaction.onattributes;

import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponse;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.BuildAttributesGroupListenerImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.BuildGroupListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * An action group that read attributes of any types from its members.
 * It can use an IPluginAction implementation to call specific actions on each attribute.
 * @author CLAISSE 
 */
public class ReadGenericAttributesImpl extends ReadAttributesImpl implements ReadGenericAttributes
{
    private boolean attributesAreSet = false;
    private BuildGroupListener buildGroupResponseListener;
    
    /**
     * @param _targets The devices to read from
     */
    public ReadGenericAttributesImpl ( Target[] _targets ) 
    {
        super ( _targets , null );
        
        super.attributes = new String[_targets.length][];
    }
    
    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        buildGroupResponseListener = new BuildAttributesGroupListenerImpl ( super.getBasicListener () );
        return buildGroupResponseListener;
    }

    public synchronized CollectiveResponse getGroupResponse () 
    {
        if ( ! attributesAreSet )
        {
            throw new IllegalStateException ( "ReadGenericAttributesActionGroup/getGroupResponse/the attributes are not set! Call setAttributesToRead first" );
        }
        return buildGroupResponseListener.getGroupResponse ();
    }
    
    public synchronized void setAttributesToRead(String[] attributesToRead) 
    {
        if ( attributesToRead == null || attributesToRead.length == 0 )
        {
            return;
        }
        
        for ( int i = 0 ; i < super.attributes.length ; i ++ )
        {
            super.attributes [ i ] = attributesToRead;
        }
        
        this.attributesAreSet = true;
    }
}
