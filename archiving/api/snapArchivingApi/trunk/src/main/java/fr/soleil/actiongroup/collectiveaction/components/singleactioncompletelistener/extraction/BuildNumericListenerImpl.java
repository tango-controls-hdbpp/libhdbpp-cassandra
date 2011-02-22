package fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction;

import java.util.Map;

import fr.esrf.Tango.DevFailed;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListener;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.ActionListenerDecorator;
import fr.soleil.actiongroup.collectiveaction.components.singleactioncompletelistener.extraction.qualities.IAttributeQualityRegister;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.ActionResult;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DevFailedWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.Tools;

/**
 * A listener for the event "the numeric attributes reading operation from a device is complete".
 * If the operation succeeded, the numeric values are stored. 
 * @author CLAISSE 
 */
public class BuildNumericListenerImpl extends ActionListenerDecorator implements BuildNumericListener 
{
    /**
     * The object that stores the numeric values
     */
    private double [][] result;
    
    /**
     * Maps the devices names to their position in the result object.
     */
    private Map<String, Integer> deviceNameToIndexMap;
    
    /**
     * The attributes qualities manager
     */
    protected IAttributeQualityRegister attributeQualityRegister;
    
    public BuildNumericListenerImpl ( ActionListener _decorator , Map<String, Integer> _deviceNameToIndexMap , IAttributeQualityRegister _attributeQualityRegister ) 
    {
        super ( _decorator );
        
        this.deviceNameToIndexMap = _deviceNameToIndexMap;
        this.result = new double [ _deviceNameToIndexMap.size () ][];
        this.attributeQualityRegister = _attributeQualityRegister;
    }
    
    public void actionSucceeded ( String deviceName , ActionResult readResult )
    {
        //System.out.println("BuildNumericListenerImpl/actionSucceeded/deviceName|"+deviceName);
        DeviceAttributeWrapper[] attributesAnswer = readResult.getAttributesValue ();
        
        int idx = deviceNameToIndexMap.get ( deviceName );
        double [] res = null;
        
        try 
        {
            res = this.toDoubleTab ( deviceName , attributesAnswer );
            
            synchronized ( this )
            {
                result [ idx ] = res;    
            }
            
            super.actionSucceeded ( deviceName , readResult );
        } 
        catch ( Throwable e ) 
        {
            super.actionFailed ( deviceName , readResult , e );
        }
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.core.CLA.listener.AbstractDeviceReaderListenerImpl#getResult()
     */
    public synchronized double[][] getResult()
    {
        return result;
    }
    
    /**
     * Extracts the numeric values from raw numeric attributes. 
     * Delegates attributes quality management (or lack thereof) to mother class AbstractAttributesReadListener
     * @param deviceName The device that was read from 
     * @param deviceAttributes The result of the read operation
     * @return The corresponding numeric values 
     * @throws DevFailed The numeric values extraction failed
     */
    private synchronized double[] toDoubleTab(String deviceName, DeviceAttributeWrapper [] deviceAttributes) throws DevFailedWrapper 
    {
        double[] ret = new double[deviceAttributes.length];
        for ( int i = 0 ; i < deviceAttributes.length ; i ++ )
        {
            ret [ i ] = Tools.toDouble ( deviceAttributes [ i ].getAttribute () );
            //System.out.println("BuildNumericListenerImpl/toDoubleTab/deviceName|"+deviceName+"/ret [ i ]/"+ret [ i ]);
            this.registerAttributeQuality ( deviceName , deviceAttributes [ i ] );
        }
        
        return ret;
    }

    /**
     * @param deviceName The target of the attributes reading task
     * @param attribute A particular attribute within the attributes to read set
     */
    public synchronized void registerAttributeQuality ( String deviceName, DeviceAttributeWrapper attribute )
    {
        this.attributeQualityRegister.registerAttributeQuality ( deviceName , attribute );
    }
    
    public Map<String, String> getMessages() 
    {
        return null; //TO DO?
    }
}
