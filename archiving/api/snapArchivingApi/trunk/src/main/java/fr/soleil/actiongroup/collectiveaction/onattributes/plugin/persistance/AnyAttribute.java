/*	Synchrotron Soleil 
 *  
 *   File          :  AnyAttribute.java
 *  
 *   Project       :  TangoParser
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  17 janv. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: AnyAttribute.java,v 
 *
 */
 /*
 * Created on 17 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.onattributes.plugin.persistance;

import fr.esrf.Tango.DevState;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.TangoConstWrapper;

/**
 * A data transfer object that holds the data extracted from a read attribute.
 * @author CLAISSE 
 */
public class AnyAttribute 
{
    private static final String SPECTRUM_SEPARATOR = ",";

    /**
     * The attribute's complete name
     */
    private String completeName;
    
    /**
     * The attribute's X dimension (1 for scalars)
     */
    private int dimX;
    
    /**
     * The attribute's Y dimension (1 for scalars and spectrums)
     */
    private int dimY;
    
    /**
     * The attribute's type
     */
    private int type;
    
    /**
     * The attribute's format (scalar, spectrum, or image)
     */
    private int format;
    
    /**
     * The attribute's read/write property (read-only, write-only, or read/write)
     */
    private int writable;
    
    /**
     * The attribute's double value
     */
    private double [] rawValueDouble;
    
    /**
     * The attribute's int value
     */
    private int [] rawValueInt;
    
    /**
     * The attribute's short value
     */
    private short [] rawValueShort;
    
    /**
     * The attribute's String value
     */
    private String [] rawValueString;
    
    /**
     * The attribute's long value
     */
    private int [] rawValueLong;
    
    /**
     * The attribute's float value
     */
    private float [] rawValueFloat;
    
    /**
     * The attribute's boolean value
     */
    private boolean [] rawValueBoolean;
    
    /**
     * The attribute's State value
     */
    private DevState rawValueState;

    private int numberOfComponents;

    private double[] convertedNumericValuesTable;

    private String[] convertedStringValuesTable;

    private boolean hasBothReadAndWrite;

    private boolean hasBeenAggreggated;
    private String aggreggateAll;
    private String aggreggateRead;
    private String aggreggateWrite; 
    
    public AnyAttribute(String deviceName, DeviceAttributeWrapper attribute) throws Exception 
    {
        this.initParameters ( deviceName , attribute );
        this.numberOfComponents = this.extractValue ( attribute );
        this.initFormatAndWritable ();
        this.buildPersistableValue ();
    }
    
    private void initFormatAndWritable() 
    {
        int format = TangoConstWrapper.AttrDataFormat_SCALAR;
        if ( this.dimX > 1 )
        {
            format = TangoConstWrapper.AttrDataFormat_SPECTRUM;
        }
        if ( this.dimY > 1 )
        {
            format = TangoConstWrapper.AttrDataFormat_IMAGE;
        }
        this.setFormat ( format );
        
        this.hasBothReadAndWrite = numberOfComponents > dimX;
        //WARNING comment distinguer le cas WO???
        int writable = hasBothReadAndWrite ? TangoConstWrapper.AttrWriteType_READ_WRITE : TangoConstWrapper.AttrWriteType_READ;
        this.setWritable ( writable );
    }

    private int extractValue(DeviceAttributeWrapper attribute) throws Exception 
    {
        int _numberOfComponents = 0;
        switch ( this.getType() )
        {
            case TangoConst.Tango_DEV_BOOLEAN:
                this.rawValueBoolean = attribute.extractBooleanArray ();
                _numberOfComponents = rawValueBoolean == null ? 0 : rawValueBoolean.length; 
            break;
            
            case TangoConst.Tango_DEV_DOUBLE:
                this.rawValueDouble = attribute.extractDoubleArray ();
                _numberOfComponents = rawValueDouble == null ? 0 : rawValueDouble.length;
            break;
            
            case TangoConst.Tango_DEV_FLOAT:
                this.rawValueFloat = attribute.extractFloatArray ();
                _numberOfComponents = rawValueFloat == null ? 0 : rawValueFloat.length;
            break;
            
            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_ULONG:
                this.rawValueLong = attribute.extractLongArray ();
                _numberOfComponents = rawValueLong == null ? 0 : rawValueLong.length;
            break;
            
            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_USHORT:
                this.rawValueShort = attribute.extractShortArray ();
                _numberOfComponents = rawValueShort == null ? 0 : rawValueShort.length;
            break;
            
            case TangoConst.Tango_DEV_STATE:
                this.rawValueState = attribute.extractState();
                _numberOfComponents = 1;
            break;
            
            case TangoConst.Tango_DEV_STRING:
                this.rawValueString = attribute.extractStringArray ();
                _numberOfComponents = rawValueString == null ? 0 : rawValueString.length;
            break;
        }
        return _numberOfComponents;
    }
    
    private void buildPersistableValue() 
    {
        if ( this.numberOfComponents == 0 )
        {
            return;
        }
        this.convertedStringValuesTable = new String [ this.numberOfComponents ];
        this.convertedNumericValuesTable = new double [ this.numberOfComponents ];
        
        for ( int i = 0 ; i < this.numberOfComponents ; i ++ )
        {
            switch ( this.getType() )
            {
                    case TangoConst.Tango_DEV_BOOLEAN:
                        this.convertedNumericValuesTable [ i ] = this.rawValueBoolean [ i ] ? 1 : 0; //for scalars which are stored as 0/1
                        this.convertedStringValuesTable [ i ] = this.rawValueBoolean [ i ] + ""; //for spectrums which are stored as true,false,false,...
                        //System.out.println("");
                    break;
                    
                    case TangoConst.Tango_DEV_DOUBLE:
                        this.convertedNumericValuesTable [ i ] = this.rawValueDouble [ i ];
                        this.convertedStringValuesTable [ i ] = this.rawValueDouble [ i ]+"";
                    break;
                    
                    case TangoConst.Tango_DEV_FLOAT:
                        this.convertedNumericValuesTable [ i ] = this.rawValueFloat [ i ];
                        this.convertedStringValuesTable [ i ] = this.rawValueFloat [ i ]+"";
                    break;
                    
                    case TangoConst.Tango_DEV_LONG:
                    case TangoConst.Tango_DEV_ULONG:
                        this.convertedNumericValuesTable [ i ] = this.rawValueLong [ i ];
                        this.convertedStringValuesTable [ i ] = this.rawValueLong [ i ]+"";
                    break;
                    
                    case TangoConst.Tango_DEV_SHORT:
                    case TangoConst.Tango_DEV_USHORT:
                        this.convertedNumericValuesTable [ i ] = this.rawValueShort [ i ];
                        this.convertedStringValuesTable [ i ] = this.rawValueShort [ i ]+"";
                    break;
                    
                    case TangoConst.Tango_DEV_STATE:
                        this.convertedNumericValuesTable [ i ] = this.rawValueState.value();
                        this.convertedStringValuesTable [ i ] = this.rawValueState+"";
                    break;
                    
                    case TangoConst.Tango_DEV_STRING:
                        this.convertedStringValuesTable [ i ] = this.rawValueString [ i ];
                    break;
            }
        }
    }

    private void initParameters(String deviceName, DeviceAttributeWrapper attribute) throws Exception 
    {
        String name = attribute.getName ();
        String completeName = deviceName + "/" + name;
        int dimX = attribute.getDimX ();
        int dimY = attribute.getDimY ();
        int type = attribute.getType ();
        
        this.setCompleteName ( completeName );
        this.setDimX ( dimX );
        this.setDimY ( dimY );
        this.setType ( type );
    }
    /**
     * @return the completeName
     */
    public String getCompleteName() {
        return this.completeName;
    }
    /**
     * @param completeName the completeName to set
     */
    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }
    /**
     * @return the dimX
     */
    public int getDimX() {
        return this.dimX;
    }
    /**
     * @param dimX the dimX to set
     */
    public void setDimX(int dimX) {
        this.dimX = dimX;
    }
    /**
     * @return the dimY
     */
    public int getDimY() {
        return this.dimY;
    }
    /**
     * @param dimY the dimY to set
     */
    public void setDimY(int dimY) {
        this.dimY = dimY;
    }
    /**
     * @return the type
     */
    public int getType() {
        return this.type;
    }
    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * @return the format
     */
    public int getFormat() {
        return this.format;
    }
    /**
     * @param format the format to set
     */
    public void setFormat(int format) {
        this.format = format;
    }
    /**
     * @return the writable
     */
    public int getWritable() {
        return this.writable;
    }
    /**
     * @param writable the writable to set
     */
    public void setWritable(int writable) {
        this.writable = writable;
    }
    
    public double [] getConvertedNumericValuesTable ()
    {
        return this.convertedNumericValuesTable;
    }
    
    public String [] getConvertedStringValuesTable ()
    {
        return this.convertedStringValuesTable;
    }
    
    public String getConvertedStringAggreggatedValues ( boolean wantsReadValue )
    {
       //System.out.println("AnyAttribute/getConvertedStringAggreggatedValues/START");
       if ( ! this.hasBeenAggreggated )
       {
           //System.out.println("AnyAttribute/getConvertedStringAggreggatedValues/1");
           this.aggreggate ();
       }
        
       if ( ! this.hasBothReadAndWrite )
       {
           //System.out.println("AnyAttribute/getConvertedStringAggreggatedValues/2");
           return this.aggreggateAll;
       }
       else
       {
           if ( wantsReadValue )
           {
               //System.out.println("AnyAttribute/getConvertedStringAggreggatedValues/3");
               return aggreggateRead;
           }
           else
           {
               //System.out.println("AnyAttribute/getConvertedStringAggreggatedValues/4");
               return aggreggateWrite;
           }
       }
    }

    private void aggreggate() 
    {
        //System.out.println("AnyAttribute/aggreggate/START");
        if ( ! this.hasBothReadAndWrite )
        {
            //System.out.println("AnyAttribute/aggreggateAll/");
            this.aggreggateAll = this.aggreggateAll ();
        }
        else
        {
            //System.out.println("AnyAttribute/aggreggateRead/");
            this.aggreggateRead = this.aggreggateRead ();
        
            //System.out.println("AnyAttribute/aggreggateWrite/");
            this.aggreggateWrite = this.aggreggateWrite ();
            //System.out.println("AnyAttribute/AFTER aggreggateWrite/aggreggateWrite|"+ this.aggreggateWrite+"|" );
        }
        this.hasBeenAggreggated = true;    
    }

    private String aggreggateWrite() 
    {
        try
        {
            if ( this.convertedStringValuesTable == null || this.convertedStringValuesTable.length == 0 )
            {
                return null;
            }
            StringBuffer buff = new StringBuffer ();
            for ( int i = this.dimX ; i < this.convertedStringValuesTable.length ; i ++ )
            {
                buff.append ( this.convertedStringValuesTable [ i ] );
                if ( i < this.convertedStringValuesTable.length - 1 )
                {
                    buff.append ( SPECTRUM_SEPARATOR );
                }
            }
            return buff.toString();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    private String aggreggateRead() 
    {
        //System.out.println("AnyAttribute/aggreggateRead/START");
        if ( this.convertedStringValuesTable == null || this.convertedStringValuesTable.length == 0 )
        {
            return null;
        }
        StringBuffer buff = new StringBuffer ();
        for ( int i = 0 ; i < this.dimX ; i ++ )
        {
            buff.append ( this.convertedStringValuesTable [ i ] );
            if ( i < this.dimX - 1 )
            {
                buff.append ( SPECTRUM_SEPARATOR );
            }
        }
        return buff.toString ();
    }

    private String aggreggateAll() 
    {
        if ( this.convertedStringValuesTable == null || this.convertedStringValuesTable.length == 0 )
        {
            return null;
        }
        StringBuffer buff = new StringBuffer ();
        for ( int i = 0 ; i < this.convertedStringValuesTable.length ; i ++ )
        {
            buff.append ( this.convertedStringValuesTable [ i ] );
            if ( i < this.convertedStringValuesTable.length - 1 )
            {
                buff.append ( SPECTRUM_SEPARATOR );
            }
        }
        return buff.toString ();
    }
}
