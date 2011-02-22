package fr.soleil.bensikin.actions.roles;

import javax.swing.Action;

/**
* Defines User rights on actions 
*
* @author CLAISSE
*/
public interface IRightsManager
{
	/**
	 * Returns whether rights to an action are granted to an Operator user.
	 *
	 * @param AbstractAction          The action
	 * @return True if and only if rights to an action are granted to an Operator user
	 */
	public boolean isGrantedToOperator ( Action action );
    
    /**
     * Disables fields that the operator doesn't need to use
     */
    public void disableUselessFields ();
}
