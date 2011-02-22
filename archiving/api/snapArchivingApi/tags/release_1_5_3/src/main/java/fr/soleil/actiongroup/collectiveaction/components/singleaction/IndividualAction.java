package fr.soleil.actiongroup.collectiveaction.components.singleaction;

/**
 * A runnable with a maximum execution time
 * @author CLAISSE 
 */
public interface IndividualAction extends Runnable 
{
    /**
     * Set an individual action's timeout
     * @param timeout The action's timeout
     */
    public void setTimeout ( int timeout );
}
