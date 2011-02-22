package fr.soleil.bensikin.actions.roles;

import javax.swing.Action;

import fr.soleil.bensikin.actions.context.AddSelectedContextAttributeAlternateAction;
import fr.soleil.bensikin.actions.context.AddSelectedContextAttributesAction;
import fr.soleil.bensikin.actions.context.AlternateSelectionAction;
import fr.soleil.bensikin.actions.context.DomainsFilterAlternateAction;
import fr.soleil.bensikin.actions.context.MatchContextAttributesAction;
import fr.soleil.bensikin.actions.context.MatchPossibleContextAttributesAction;
import fr.soleil.bensikin.actions.context.NewContextAction;
import fr.soleil.bensikin.actions.context.RegisterContextAction;
import fr.soleil.bensikin.actions.context.RemoveSelectedContextAttributesAction;
import fr.soleil.bensikin.actions.context.ValidateAlternateSelectionAction;
import fr.soleil.bensikin.components.BensikinToolbar;
import fr.soleil.bensikin.containers.context.ContextAttributesPanelAlternate;
import fr.soleil.bensikin.containers.context.ContextDataPanel;


/**
 * 
 * @author CLAISSE 
 */
public class SnapshotsOnlyRightsManager implements IRightsManager 
{

    /**
     * 
     */
    public SnapshotsOnlyRightsManager() 
    {
        super();
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.actions.rights.IRightsManager#isGrantedToOperator(javax.swing.AbstractAction)
     */
    //public boolean isGrantedToOperator(BensikinAction action) 
    public boolean isGrantedToOperator(Action action)
    {
        //System.out.println ( "CLA/isGrantedToOperator/action/"+action.getClass().toString() );
                               
        if ( action instanceof AddSelectedContextAttributeAlternateAction ) 
        {
            return false;
        }
        if ( action instanceof AddSelectedContextAttributesAction ) 
        {
            return false;
        }
        if ( action instanceof AlternateSelectionAction ) 
        {
            return false;
        }
        if ( action instanceof DomainsFilterAlternateAction ) 
        {
            return false;
        }
        /*if ( action instanceof LaunchSnapshotAction ) 
        {
            return false;
        }*/
        if ( action instanceof MatchContextAttributesAction ) 
        {
            return false;
        }
        if ( action instanceof MatchPossibleContextAttributesAction ) 
        {
            return false;
        }
        if ( action instanceof NewContextAction ) 
        {
            return false;
        }
        if ( action instanceof RegisterContextAction ) 
        {
            return false;
        }
        if ( action instanceof RemoveSelectedContextAttributesAction ) 
        {
            return false;
        }
        if ( action instanceof ValidateAlternateSelectionAction ) 
        {
            return false;
        }
        /*if ( action instanceof EditCommentAction ) 
        {
            return false;
        }
        if ( action instanceof OpenEditCommentAction ) 
        {
            return false;
        }
        if ( action instanceof SelectAllOrNoneAction ) 
        {
            return false;
        }
        if ( action instanceof SetEquipmentsAction ) 
        {
            return false;
        }
        if ( action instanceof SnapSpectrumSetAction ) 
        {
            return false;
        }
        if ( action instanceof SnapSpectrumSetAllAction ) 
        {
            return false;
        }*/
        return true;
    }

    public void disableUselessFields() 
    {
        BensikinToolbar bensikinToolbar = BensikinToolbar.getInstance ();
        if ( bensikinToolbar != null )
        {
            bensikinToolbar.removeNewContextButton ();    
        }
        
        ContextDataPanel.disableInput ();  
        ContextAttributesPanelAlternate.disableInput ();  
    }

}
