package fr.soleil.actiongroup.collectiveaction.onattributes;

import fr.soleil.actiongroup.collectiveaction.CollectiveAction;
import fr.soleil.actiongroup.collectiveaction.components.response.CollectiveResponse;


/**
 * A group that read attributes of any types from its members
 * @author CLAISSE 
 */
public interface ReadGenericAttributes extends CollectiveAction
{
    public CollectiveResponse getGroupResponse ();
    
    public void setAttributesToRead ( String [] attributesToRead );
}
