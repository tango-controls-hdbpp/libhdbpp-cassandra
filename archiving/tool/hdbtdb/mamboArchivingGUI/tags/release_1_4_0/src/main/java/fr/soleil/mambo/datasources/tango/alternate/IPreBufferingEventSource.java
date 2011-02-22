/*	Synchrotron Soleil 
 *  
 *   File          :  IPreBufferingEventSource.java
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
 *   Log: IPreBufferingEventSource.java,v 
 *
 */
 /*
 * Created on 20 sept. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.tango.alternate;

public interface IPreBufferingEventSource 
{
    public void register ( IPreBufferingEventListener source );
    public void unregister ( IPreBufferingEventListener source );
    public void notifyAllDone ();
    public void notifyStepDone ( int step , int totalSteps ); 
}
