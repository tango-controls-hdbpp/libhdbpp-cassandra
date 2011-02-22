/*  Synchrotron Soleil 
 *  
 *   File          :  ITangoGroup.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  8 déc. 06 
 *  
 *   Revision:                      Author:  
 *   Date:                          State:  
 *  
 *   Log: ITangoGroup.java,v 
 *
 */
 /*
 * Created on 8 déc. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes;

import fr.soleil.actiongroup.collectiveaction.CollectiveActionWithMessages;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PluginContext;


/**
 * A group that read attributes of any types from its members
 * @author CLAISSE 
 */
public interface UsePlugin extends CollectiveActionWithMessages
{
    public void setPluginContext(PluginContext context);   
}
