package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListenerDecorator;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.Plugin;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PluginContext;

/**
 * A listener for the event "the attributes (not necessarily numeric) reading operation from a device is complete".
 * If the operation succeeded, a "plugin action" uses the read result.
 * What this plugin action does (if it does anything) depends on the implementation of IPluginAction.
 * @author CLAISSE 
 */
public class UsePluginListener extends ActionListenerDecorator implements ActionListener 
{
    private Plugin action;
    private PluginContext context;
    
    public UsePluginListener ( ActionListener _decorator , Plugin _action, PluginContext _context ) 
    {
        super(_decorator);
        this.action = _action;
        this.context = _context;
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.IActionCompleteListener#actionSucceeded(java.lang.String, java.lang.Object)
     */
    public void actionSucceeded ( String targetName , ActionResult actionResult )
    {
        try
        {
            DeviceAttributeWrapper[] attributesAnswer = actionResult.getAttributesValue();
            for ( int i = 0 ; i < attributesAnswer.length ; i ++ )
            {
                this.action.execute ( this.context , targetName , attributesAnswer [ i ] );
            }
        }
        catch ( Exception e )
        {
            super.actionFailed ( targetName , actionResult , e );
        }
        
        super.actionSucceeded ( targetName , actionResult );
    }
}
