package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.response.IndividualAttributesResponse;
import fr.soleil.actiongroup.collectiveaction.components.response.IndividualAttributesResponseImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevErrorWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevFailedWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.Tools;

/**
 * A listener for the event "the attributes (not necessarily numeric) reading operation from a device is complete".
 * If the operation succeeded, a "plugin action" uses the read result.
 * What this plugin action does (if it does anything) depends on the implementation of IPluginAction.
 * @author CLAISSE 
 */
public class BuildAttributesGroupListenerImpl extends BuildGroupListenerImpl implements BuildGroupListener 
{
    public BuildAttributesGroupListenerImpl ( ActionListener _decorator ) 
    {
        super ( _decorator );
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.IActionCompleteListener#actionSucceeded(java.lang.String, java.lang.Object)
     */
    public void actionSucceeded ( String deviceName , ActionResult readResult )
    {
        DeviceAttributeWrapper[] attributesAnswer = readResult.getAttributesValue ();
        
        IndividualAttributesResponse individualResponse = new IndividualAttributesResponseImpl ( deviceName );
        individualResponse.setAttributes( attributesAnswer );
        this.addIndividualResponse ( individualResponse );
        
        super.actionSucceeded ( deviceName, readResult );
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.AbstractActionCompleteListener#actionFailed(java.lang.String, java.lang.Object, java.lang.Throwable)
     */
    public void actionFailed(String deviceName , ActionResult actionResult , Throwable e) 
    {
        super.actionFailed ( deviceName , actionResult , e );
        
        IndividualAttributesResponse individualResponse = new IndividualAttributesResponseImpl ( deviceName );
        individualResponse.set_failed ( true );
        
        DevErrorWrapper [] wrapped = DevFailedWrapper.wrapErrors ( e );
        individualResponse.set_err_stack ( wrapped );
        individualResponse.set_timeout ( Tools.isDueToATimeout ( e ) );
        
        this.addIndividualResponse ( individualResponse );
    }
}
