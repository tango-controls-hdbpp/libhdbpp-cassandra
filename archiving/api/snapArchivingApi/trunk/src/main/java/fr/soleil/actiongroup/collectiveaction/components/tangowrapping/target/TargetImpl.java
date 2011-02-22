/*	Synchrotron Soleil 
 *  
 *   File          :  Target.java
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
 *   Log: Target.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.AttributeInfo;
import fr.esrf.TangoApi.CommandInfo;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.AttributeInfoWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.CommandInfoWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DbDatumWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceDataWrapper;

public class TargetImpl implements Target 
{
    private DeviceProxy proxy;
    
    TargetImpl ( DeviceProxy _proxy )
    {
        this.proxy = _proxy;
    }

    public void set_timeout_millis ( int timeout ) throws DevFailed 
    {
        this.proxy.set_timeout_millis ( timeout );
    }

    public DeviceDataWrapper command_inout ( String commandName , DeviceDataWrapper commandParameters ) throws DevFailed 
    {
        DeviceData response;
        if ( commandParameters == null || commandParameters.getCommandArgument () == null )
        {
            response = this.proxy.command_inout ( commandName );    
        }
        else
        {
            response = this.proxy.command_inout ( commandName , commandParameters.getCommandArgument () );    
        }
        
        return new DeviceDataWrapper ( response );
    }

    public DeviceAttributeWrapper[] read_attribute ( String [] attributesToRead ) throws DevFailed 
    {
        DeviceAttribute[] response = this.proxy.read_attribute ( attributesToRead );
        DeviceAttributeWrapper[] wrappedResponse = this.wrapAttributes ( response );
        
        return wrappedResponse;
    }

    private DeviceAttributeWrapper[] wrapAttributes(DeviceAttribute[] response) 
    {
        if ( response == null )
        {
            return null;    
        }
        
        DeviceAttributeWrapper [] ret = new DeviceAttributeWrapper [ response.length ];
        for ( int i = 0 ; i < response.length ; i ++ )
        {
            ret [ i ] = new DeviceAttributeWrapper ( response [ i ] );
        }
        return ret;
    }

    public AttributeInfoWrapper get_attribute_info ( String attributeName ) throws DevFailed 
    {
        AttributeInfo tmpAttributeInfo = this.proxy.get_attribute_info ( attributeName );
        return new AttributeInfoWrapper ( tmpAttributeInfo );
    }

    public void set_attribute_info ( AttributeInfoWrapper wrapper ) throws DevFailed 
    {
        this.proxy.set_attribute_info ( new AttributeInfo[] { wrapper.getAttributeInfo () } );
    }
    
    public void put_property ( String propertyName , DbDatumWrapper propertyValueHolder ) throws DevFailed
    {
        if ( ! propertyName.equals ( "" ) )
        {
            DbDatum propertyValue = propertyValueHolder.getDbDatum ();
            this.proxy.put_property ( new DbDatum[] {propertyValue} );    
        }
        else
        {
            this.proxy.delete_property ( "" );    
        }
    }

    public String get_name () 
    {
        return this.proxy.get_name ();
    }

    public void write_attribute ( DeviceAttributeWrapper[] attributesToWrite , Double newValue ) throws DevFailed 
    {
        DeviceAttribute[] attrs = this.setNewValue ( attributesToWrite , newValue ); 
        this.proxy.write_attribute ( attrs );
    }
    
    private DeviceAttribute[] setNewValue ( DeviceAttributeWrapper[] attributesAnswer , Double newValue ) throws IllegalArgumentException, DevFailed
    {
        DeviceAttribute [] ret = new DeviceAttribute [ attributesAnswer.length ];
        for ( int i = 0 ; i < attributesAnswer.length ; i ++ )
        {
            ret [ i ] = attributesAnswer [ i ].getAttribute ();
            this.insertNewValue ( ret [ i ] , newValue );
        }
        return ret;    
    }

    private void insertNewValue(DeviceAttribute attribute, Double newValue) throws DevFailed 
    {
        switch ( attribute.getType () )
        {
            case TangoConst.Tango_DEV_SHORT : 
                attribute.insert ( newValue.shortValue () );
            break;

            case TangoConst.Tango_DEV_USHORT: 
                attribute.insert ( newValue.shortValue () );
            break;
            
            case TangoConst.Tango_DEV_CHAR:
                attribute.insert ( newValue.intValue () );
            break;

            case TangoConst.Tango_DEV_UCHAR: 
                attribute.insert_uc ( newValue.shortValue () );
            break;
                
            case TangoConst.Tango_DEV_LONG: 
                attribute.insert ( newValue.intValue () );
            break;

            case TangoConst.Tango_DEV_ULONG:
                attribute.insert ( newValue.intValue () );
            break;
           
            case TangoConst.Tango_DEV_FLOAT: 
                attribute.insert ( newValue.floatValue () );
            break;

            case TangoConst.Tango_DEV_DOUBLE:
                attribute.insert ( newValue );
            break;
                
            case TangoConst.Tango_DEV_BOOLEAN: 
                attribute.insert ( newValue == 1 );
            break;

            default:
                throw new IllegalArgumentException ( "WriteNumericValueListener/Unexpected attribute type/"+attribute.getType () );
        }    
    }

    public CommandInfoWrapper[] command_list_query() throws DevFailed 
    {
        CommandInfo[] response = this.proxy.command_list_query();
        return this.wrapCommandInfos ( response );
    }
    
    private CommandInfoWrapper[] wrapCommandInfos(CommandInfo[] response) 
    {
        if ( response == null )
        {
            return null;    
        }
        
        CommandInfoWrapper [] ret = new CommandInfoWrapper [ response.length ];
        for ( int i = 0 ; i < response.length ; i ++ )
        {
            ret [ i ] = new CommandInfoWrapper ( response [ i ] );
        }
        return ret;
    }
}
