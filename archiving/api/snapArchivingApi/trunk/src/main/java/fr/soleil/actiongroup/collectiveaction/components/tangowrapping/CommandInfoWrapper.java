/*	Synchrotron Soleil 
 *  
 *   File          :  CommandInfoWrapper.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  9 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: CommandInfoWrapper.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.TangoApi.CommandInfo;

public class CommandInfoWrapper 
{
    private CommandInfo commandInfo;

    /**
     * @param commandInfo
     */
    public CommandInfoWrapper(CommandInfo commandInfo) 
    {
        this.commandInfo = commandInfo;
    }

    /**
     * @return the commandInfo
     */
    public CommandInfo getCommandInfo() 
    {
        return this.commandInfo;
    }
}
