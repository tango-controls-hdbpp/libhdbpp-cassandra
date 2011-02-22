package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;


/**
 * A listener that's notified when the numeric attributes reading operation for a given device is complete.
 * The values accumulated during the individual device reading operations can be accessed via getResult. 
 * @author CLAISSE 
 */
public interface FindSlowestDeviceListener extends ActionListener 
{   
    public String getSlowestDevice ();
    public long getSlowestTime ();
}
