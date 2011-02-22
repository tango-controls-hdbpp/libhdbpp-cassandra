/*	Synchrotron Soleil 
 *  
 *   File          :  IPreBufferingEventListener.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  20 sept. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IPreBufferingEventListener.java,v 
 *
 */
 /*
 * Created on 20 sept. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.datasources.tango;

public interface IPreBufferingEventListener 
{
    public void allDone ();
    public void stepDone ( int step , int totalSteps );
    public String getName ();
}
