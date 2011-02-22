/*	Synchrotron Soleil 
 *  
 *   File          :  MessagesBuilder.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  7 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: MessagesBuilder.java,v 
 *
 */
 /*
 * Created on 7 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder;

import java.text.SimpleDateFormat;

public abstract class AbstractMessageBuilder implements MessageBuilder 
{
    /**
     * The error messages date format
     */
    protected static final SimpleDateFormat dateFormat  = new SimpleDateFormat ("dd-MM-yyyy HH:mm:ss");
}
