package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.response.IndividualCommandResponse;
import fr.soleil.actiongroup.collectiveaction.components.response.IndividualCommandResponseImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevErrorWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevFailedWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.Tools;

/**
 * A listener that is notified when a void group command has been completed for an individual device.
 * Adds a result message even if the command succeeded.
 * @author CLAISSE 
 */
public class BuildCommandGroupListenerImpl extends BuildGroupListenerImpl implements BuildGroupListener 
{
    public BuildCommandGroupListenerImpl ( ActionListener _decorator ) 
    {
        super ( _decorator );
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.IActionCompleteListener#actionSucceeded(java.lang.String, java.lang.Object)
     */
    public void actionSucceeded ( String deviceName , ActionResult readResult )
    {
        IndividualCommandResponse individualResponse = new IndividualCommandResponseImpl ( deviceName );
        individualResponse.setData ( readResult.getCommandValue () );
        this.addIndividualResponse ( individualResponse );
        
        super.actionSucceeded ( deviceName , readResult );
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.listener.AbstractActionCompleteListener#actionFailed(java.lang.String, java.lang.Object, java.lang.Throwable)
     */
    public void actionFailed(String deviceName , ActionResult actionResult , Throwable e) 
    {
        super.actionFailed ( deviceName , actionResult , e );
        
        IndividualCommandResponse individualResponse = new IndividualCommandResponseImpl ( deviceName );
        individualResponse.set_failed ( true );
        
        DevErrorWrapper [] wrapped = DevFailedWrapper.wrapErrors ( e );
        individualResponse.set_err_stack ( wrapped );
        individualResponse.set_timeout ( Tools.isDueToATimeout ( e ) );
        
        this.addIndividualResponse ( individualResponse );
    }
}