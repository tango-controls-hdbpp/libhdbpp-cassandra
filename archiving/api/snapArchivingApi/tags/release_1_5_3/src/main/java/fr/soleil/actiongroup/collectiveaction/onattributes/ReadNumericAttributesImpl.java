/*	Synchrotron Soleil 
 *  
 *   File          :  TangoGroupCLA.java
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
 *   Log: TangoGroupCLA.java,v 
 *
 */
 /*
 * Created on 8 déc. 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes;

import java.util.Hashtable;
import java.util.Map;

import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.StoreMessageListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.ActionCompleteMessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.action.messagebuilder.MessageBuilder;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.BuildNumericListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.BuildNumericListenerImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.FindSlowestDeviceListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.FindSlowestDeviceListenerImpl;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities.DoNothingAttributeQualityRegister;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities.IAttributeQualityRegister;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevFailedWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;


/**
 * An action group that read attributes of any types from its members.
 * It uses an instance of IAttributeQualityRegister to record the attribute's qualities while reading them
 * @author CLAISSE 
 */
public class ReadNumericAttributesImpl extends ReadAttributesImpl implements ReadNumericAttributes
{
    /**
     * Records the attributes qualities 
     */
    protected IAttributeQualityRegister attributeQualityRegister;
    protected StoreMessageListener storeMessageActionListener;
    protected BuildNumericListener numericResponseListener;
    protected FindSlowestDeviceListener findSlowestDeviceActionListener;
    
    /**
     * Instantiates a new ReadNumericAttributesActionGroup. 
     * Uses an inactive implementation of IAttributeQualityRegister (DoNothingAttributeQualityRegister).
     * @param _targets The devices to read from
     * @param _attributes The list of attributes to read for each device
     */
    public ReadNumericAttributesImpl(Target[] _targets, String[][] _attributes ) 
    {
        super ( _targets , _attributes );
        this.attributeQualityRegister = new DoNothingAttributeQualityRegister ();
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.CLA.ITangoGroup#readAttributes()
     */
    public synchronized double[][] getNumericAttributesSortedByDevice() throws DevFailedWrapper
    {
        if ( super.taskCompletionListener.hasBeenNotifiedOfFailedActions () )
        {
            Exception exception = new Exception ( "Failed to read attributes" );

            DevFailedWrapper devFailed = new DevFailedWrapper ();
            devFailed.initCause ( exception );
            devFailed.setStackTrace ( exception.getStackTrace () );
            throw devFailed;
        }
        else
        {    
            return numericResponseListener.getResult ();
        }
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.CLA.ITangoGroup#getLastSlowestDevice()
     */
    public synchronized String getLastSlowestDevice() 
    {    
        String device = findSlowestDeviceActionListener.getSlowestDevice ();
        long time = findSlowestDeviceActionListener.getSlowestTime ();
        return device + " in " + time + " ms";  
    }

    /* (non-Javadoc)
     * @see fr.soleil.core.groupactions.apis.group.attributes.read.numeric.IReadNumericAttributesGroup#getNumericAttributesSortedByAttribute()
     */
    public synchronized Map<String, Double> getNumericAttributesSortedByAttribute() throws DevFailedWrapper 
    {
        double[][] attributesSortedByDevice = this.getNumericAttributesSortedByDevice ();
        if ( attributesSortedByDevice == null || attributesSortedByDevice.length == 0 )
        {
            return null;
        }
        
        Map<String, Double> res = new Hashtable<String, Double> ();
        for ( int i = 0 ; i < this.attributes.length ; i ++ )
        {
            if ( attributesSortedByDevice[i]== null )
            {
                System.out.println("NULL !!!! 2/this.targets[i].get_name()/"+this.targets[i].get_name());
            }
            
            for ( int j = 0 ; j < this.attributes[i].length ; j ++ )
            {
                res.put ( this.targets[i].get_name() + "/" + this.attributes[i][j] , attributesSortedByDevice[i][j] );
            }  
        }
        return res;
    }

    @Override
    protected ActionListener getTaskCompletionListener() 
    {
        MessageBuilder messagesBuilder = new ActionCompleteMessageBuilder ();
        storeMessageActionListener = new StoreMessageListener ( super.getBasicListener () , messagesBuilder );
        
        Map<String, Integer> deviceNameToIndexMap = this.buildDevicesPositionMap ();
        numericResponseListener = new BuildNumericListenerImpl ( storeMessageActionListener , deviceNameToIndexMap , this.attributeQualityRegister );
        
        findSlowestDeviceActionListener = new FindSlowestDeviceListenerImpl ( numericResponseListener );
        return findSlowestDeviceActionListener;
        
    }

    private Map<String, Integer> buildDevicesPositionMap() 
    {
        int n = this.targets.length;
        Map<String, Integer> ret = new Hashtable <String, Integer> ( n );
        for ( int i = 0 ; i < n ; i ++ )
        {
            ret.put ( this.targets [ i ].get_name () , i );
        }
        
        return ret;
    }

    public synchronized Map<String, String> getMessages() 
    {
        return this.storeMessageActionListener.getMessages();
    }
}
