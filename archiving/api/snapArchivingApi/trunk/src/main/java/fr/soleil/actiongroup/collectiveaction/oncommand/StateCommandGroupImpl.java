/*	Synchrotron Soleil 
 *  
 *   File          :  TangoGroupCLAWithStates.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  5 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: TangoGroupCLAWithStates.java,v 
 *
 */
 /*
 * Created on 5 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.oncommand;

import fr.soleil.actiongroup.collectiveaction.components.singleaction.IndividualAction;
import fr.soleil.actiongroup.collectiveaction.components.singleaction.atomic.ExecuteStateCommand;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;

/**
 * An action group that reads the State of all its targets.
 * @author CLAISSE 
 */
public class StateCommandGroupImpl extends CommandGroupImpl implements CommandGroup 
{
    /**
     * @param _proxies The targets of the State command
     */
    public StateCommandGroupImpl ( Target[] _proxies )
    {
        super ( _proxies );
        
        super.commandName = "State";
        super.commandParameters = null;
    }
    
    @Override
    protected IndividualAction getTask(int deviceIndex, ActionListener listener) 
    {
        return new ExecuteStateCommand ( listener , targets [ deviceIndex ] );
    }
}
