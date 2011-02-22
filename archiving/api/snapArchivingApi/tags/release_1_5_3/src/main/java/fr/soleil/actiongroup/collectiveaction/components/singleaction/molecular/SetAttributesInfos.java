package fr.soleil.actiongroup.collectiveaction.components.singleaction.molecular;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.AbstractIndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;
import fr.soleil.actiongroup.collectiveaction.onattributes.attributeinfomodifier.AttributeInfoModifier;

/**
 * Modifies the attribute info properties of a device's attributes. Notifies its listener on completion. 
 * @author CLAISSE 
 */
public class SetAttributesInfos extends AbstractIndividualAction 
{
    /**
     * The attribute info modifier
     */
    private AttributeInfoModifier modifier;
    /**
     * The list of attributes to modify
     */
    private String[] attributesToModify;
    
    private ActionResult res;
    
    /**
     * @param _listener The listener to notify on completion
     * @param _target The device to read from
     * @param _modifier The attribute info modifier
     * @param _attributesToModify The list of attributes to modify (all these attributes need to have the attribute info property defined by _modifier)
     */
    public SetAttributesInfos ( ActionListener _listener , Target _target , AttributeInfoModifier _modifier , String[] _attributesToModify )
    {
        super ( _listener , _target );
        this.modifier = _modifier;
        this.attributesToModify = _attributesToModify;
        
        this.res = new ActionResult ( this.modifier.getAttributeInfoPropertyName (), this.modifier.getAttributeInfoPropertyValue () );
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() 
    {
        for (String nextAtt : this.attributesToModify )
        {
            try 
            {
                AttributeInfoWrapper wrapper = this.target.get_attribute_info ( nextAtt );
                this.modifier.modify ( wrapper );
                this.target.set_attribute_info ( wrapper );
                
                this.listener.actionSucceeded ( target.get_name () + "/" + nextAtt , this.res );
            } 
            catch (Throwable t) 
            {
                this.listener.actionFailed ( target.get_name () + "/" + nextAtt , this.res , t );
            }
        }
                
        this.listener.actionCompleted ();
    }
}