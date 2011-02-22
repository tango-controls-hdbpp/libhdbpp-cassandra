/*	Synchrotron Soleil 
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
 *   Revision:  					Author:  
 *   Date: 							State:  
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

import java.util.Map;

import fr.esrf.Tango.DevFailed;
import fr.soleil.actiongroup.collectiveaction.CollectiveActionWithMessages;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevFailedWrapper;


/**
 * A group that read numeric attributes from its members
 * @author CLAISSE 
 */
public interface ReadNumericAttributes extends CollectiveActionWithMessages
{
    /**
     * Reads attributes from a group of Tango devices
     * @return The attributes sorted by device
     * @throws DevFailed The read failed for at least one attribute
     */
    public double[][] getNumericAttributesSortedByDevice () throws DevFailedWrapper;
    
    /**
     * Reads attributes from a group of Tango devices
     * @return The attributes sorted by attribute complete name
     * @throws  
     * @throws DevFailed The read failed for at least one attribute
     */
    public Map<String, Double> getNumericAttributesSortedByAttribute () throws DevFailedWrapper;

    /**
     * Returns the name of the slowest to answer device.
     * @return The name of the slowest to answer device
     */
    public String getLastSlowestDevice();
}
