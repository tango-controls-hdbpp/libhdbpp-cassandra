package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;


/**
 * A listener that's notified when the numeric attributes reading operation for a given device is complete.
 * The values accumulated during the individual device reading operations can be accessed via getResult. 
 * @author CLAISSE 
 */
public interface BuildNumericListener extends ActionListener 
{   
    /**
     * Returns the complete result.
     * This method is to be called when all reading operations are complete 
     * @return The complete result
     */
    public double[][] getResult();
}
