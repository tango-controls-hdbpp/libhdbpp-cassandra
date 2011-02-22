package fr.soleil.actiongroup.collectiveaction.onattributes;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic.ReadAttributes;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;


/**
 * A group that read attributes from its members
 * @author CLAISSE 
 */
public abstract class ReadAttributesImpl extends CollectiveActionImpl
{
    /**   
     * @param _proxies The devices to read from
     * @param _attributes The list of attributes to read for each device
     */
    public ReadAttributesImpl(Target[] _proxies, String[][] _attributes ) 
    {
        super ( _proxies , _attributes );
    }
    
    @Override
    protected IndividualAction getTask ( int deviceIndex , ActionListener listener ) 
    {
        return new ReadAttributes ( listener , targets [ deviceIndex ] , attributes [ deviceIndex ] );
    }
}
