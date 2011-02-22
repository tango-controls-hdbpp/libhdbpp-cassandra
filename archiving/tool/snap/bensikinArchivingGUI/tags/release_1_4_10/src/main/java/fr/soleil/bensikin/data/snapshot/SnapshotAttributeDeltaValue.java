//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotAttributeDeltaValue.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotAttributeDeltaValue.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.14 $
//
// $Log: SnapshotAttributeDeltaValue.java,v $
// Revision 1.14  2007/08/23 12:59:23  ounsy
// toUserFriendlyString() available in SnapshotAttributeValue
//
// Revision 1.13  2007/06/29 09:18:58  ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.12  2007/06/28 12:51:00  ounsy
// spectrum and image values represented as arrays
//
// Revision 1.11  2006/10/31 16:54:08  ounsy
// milliseconds and null values management
//
// Revision 1.10  2006/04/13 15:20:22  ounsy
// setting value to null when necessary
//
// Revision 1.9  2006/04/13 12:37:33  ounsy
// new spectrum types support
//
// Revision 1.8  2006/03/29 10:23:00  ounsy
// corrected a bug in the delta calculation for shorts
//
// Revision 1.7  2006/03/20 15:51:04  ounsy
// added the case of Snapshot delta value for spectrums which
// read and write parts are the same length.
//
// Revision 1.6  2005/12/14 16:36:13  ounsy
// added methods necessary for direct clipboard edition
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:38  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.snapshot;

import fr.esrf.TangoDs.TangoConst;

/**
 * Represents the difference between an attribute effective read value and its write value.
 *
 * @author CLAISSE
 */
public class SnapshotAttributeDeltaValue extends SnapshotAttributeValue
{

    /**
     * Builds a SnapshotAttributeDeltaValue from the difference between a read value and a write value.
     *
     * @param writeValue The write value
     * @param readValue  The read value
     */
    public SnapshotAttributeDeltaValue ( SnapshotAttributeWriteValue writeValue , SnapshotAttributeReadValue readValue )
    {
        super( writeValue.getDataFormat() , writeValue.getDataType() , null );
        
        Object deltaValue = getDeltaValue( writeValue , readValue );
        if ( deltaValue == null )
        {
            this.setNotApplicable ( true );
        }
        
        this.setValue( deltaValue );
        
    }

    /**
     * Builds a SnapshotAttributeDeltaValue directly, given its Object value and the format of this Object.
     *
     * @param _dataFormat The Tango type of _value
     * @param _value      The Object value
     */
    public SnapshotAttributeDeltaValue ( int _dataFormat , Object _value )
    {
        super( _dataFormat , 0 , _value );
    }

    /**
     * Returns the difference between the values of a SnapshotAttributeWriteValue and a SnapshotAttributeReadValue
     *
     * @param writeValue The write value
     * @param readValue  The read value
     * @return 29 juin 2005
     */
    private Object getDeltaValue ( SnapshotAttributeWriteValue writeValue , SnapshotAttributeReadValue readValue )
    {
        switch ( this.dataFormat )
        {
            case SCALAR_DATA_FORMAT:
                return getScalarDeltaValue( writeValue , readValue );

            case SPECTRUM_DATA_FORMAT:
                return getSpectrumDeltaValue( writeValue , readValue );

            case IMAGE_DATA_FORMAT:
                return getImageDeltaValue( writeValue , readValue );

            default:
                return null;
        }
    }

    /**
     * Returns the difference between the values of a SnapshotAttributeWriteValue and a SnapshotAttributeReadValue,
     * provided they are scalar.
     *
     * @param writeValue The write value
     * @param readValue  The read value
     * @return 29 juin 2005
     */
    private Object getScalarDeltaValue ( SnapshotAttributeWriteValue writeValue , SnapshotAttributeReadValue readValue )
    {
        if ( writeValue == null || readValue == null )
        {
            return null;
        }

        switch ( this.getDataType() )
        {
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                try
                {
                    Short writeDouble = ( Short ) writeValue.getValue();
                    Short readDouble = ( Short ) readValue.getValue();
                    if ( writeDouble == null || readDouble == null )
                    {
                        return null;
                    }
                    return new Short( ( short ) ( readDouble.shortValue() - writeDouble.shortValue() ) );
                }
                catch ( ClassCastException e )
                {
                    //happens when the read/write values are String loaded from a file
                    String writeDouble_s = "" + writeValue.getValue();
                    String readDouble_s = "" + readValue.getValue();
                    if ( writeDouble_s == null || writeDouble_s.equals( "" ) || readDouble_s == null || readDouble_s.equals( "" ) )
                    {
                        return null;
                    }
                    /*short readDouble = Short.parseShort( readDouble_s );
                    short writeDouble = Short.parseShort( writeDouble_s );*/
                    double readDouble = Double.parseDouble( readDouble_s );
                    double writeDouble = Double.parseDouble( writeDouble_s );

                    return new Short( ( short ) ( readDouble - writeDouble ) );
                }
                //--------------------
            case TangoConst.Tango_DEV_DOUBLE:
                try
                {
                    Double writeDouble = ( Double ) writeValue.getValue();
                    Double readDouble = ( Double ) readValue.getValue();
                    if ( writeDouble == null || readDouble == null )
                    {
                        return null;
                    }
                    return new Double( readDouble.doubleValue() - writeDouble.doubleValue() );
                }
                catch ( ClassCastException e )
                {
                    //happens when the read/write values are String loaded from a file
                    String writeDouble_s = "" + writeValue.getValue();
                    String readDouble_s = "" + readValue.getValue();
                    if ( writeDouble_s == null || writeDouble_s.equals( "" ) || readDouble_s == null || readDouble_s.equals( "" ) )
                    {
                        return null;
                    }
                    double readDouble = Double.parseDouble( readDouble_s );
                    double writeDouble = Double.parseDouble( writeDouble_s );

                    return new Double( readDouble - writeDouble );
                }
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                try
                {
                    Integer writeLong = ( Integer ) writeValue.getValue();
                    Integer readLong = ( Integer ) readValue.getValue();
                    if ( writeLong == null || readLong == null )
                    {
                        return null;
                    }
                    return new Integer( readLong.intValue() - writeLong.intValue() );
                }
                catch ( ClassCastException e )
                {
                    //happens when the read/write values are String loaded from a file
                    String writeLong_s = "" + writeValue.getValue();
                    String readLong_s = "" + readValue.getValue();
                    if ( writeLong_s == null || writeLong_s.equals( "" ) || readLong_s == null || readLong_s.equals( "" ) )
                    {
                        return null;
                    }
                    double readDouble = Double.parseDouble( readLong_s );
                    double writeDouble = Double.parseDouble( writeLong_s );

                    return new Integer( (int) ( readDouble - writeDouble ) );
                }
            case TangoConst.Tango_DEV_FLOAT:
                try
                {
                    Float writeFloat = ( Float ) writeValue.getValue();
                    Float readFloat = ( Float ) readValue.getValue();
                    if ( writeFloat == null || readFloat == null )
                    {
                        return null;
                    }
                    return new Float( readFloat.longValue() - writeFloat.longValue() );
                }
                catch ( ClassCastException e )
                {
                    //happens when the read/write values are String loaded from a file
                    String writeFloat_s = "" + writeValue.getValue();
                    String readFloat_s = "" + readValue.getValue();
                    if ( writeFloat_s == null || writeFloat_s.equals( "" ) || readFloat_s == null || readFloat_s.equals( "" ) )
                    {
                        return null;
                    }
                    float readFloat = Float.parseFloat( readFloat_s );
                    float writeFloat = Float.parseFloat( writeFloat_s );

                    return new Float( readFloat - writeFloat );
                }

            default:
                return null;
        }
    }

    /**
     * @param writeValue The write value
     * @param readValue  The read value
     * @return 29 juin 2005
     */
    private Object getImageDeltaValue ( SnapshotAttributeWriteValue writeValue , SnapshotAttributeReadValue readValue )
    {
        return null;
    }

    /**
     * @param writeValue The write value
     * @param readValue  The read value
     * @return 29 juin 2005
     */
    private Object getSpectrumDeltaValue ( SnapshotAttributeWriteValue writeValue , SnapshotAttributeReadValue readValue )
    {
        if ( writeValue == null || readValue == null )
        {
            return null;
        }

        Object readValueTab = readValue.getSpectrumValue ();
        Object writeValueTab = writeValue.getSpectrumValue ();
        int readLength = 0;
        int writeLength = 0;
        Byte[] readChar = null, writeChar = null, diffChar = null;
        Integer[] readLong = null, writeLong = null, diffLong = null;
        Short[] readShort = null, writeShort = null, diffShort = null;
        Float[] readFloat = null, writeFloat = null, diffFloat = null;
        Double[] readDouble = null, writeDouble = null, diffDouble = null;
        switch ( dataType )
        {

            case TangoConst.Tango_DEV_DOUBLE:
                if (readValueTab != null && !"Nan".equals(readValueTab))
                {
                    readDouble = (Double[])readValueTab;
                    readLength = readDouble.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab))
                {
                    writeDouble = (Double[])writeValueTab;
                    writeLength = writeDouble.length;
                }
                break;

            case TangoConst.Tango_DEV_FLOAT:
                if (readValueTab != null && !"Nan".equals(readValueTab))
                {
                    readFloat = (Float[])readValueTab;
                    readLength = readFloat.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab))
                {
                    writeFloat = (Float[])writeValueTab;
                    writeLength = writeFloat.length;
                }
                break;

            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                if (readValueTab != null && !"Nan".equals(readValueTab))
                {
                    readShort = (Short[])readValueTab;
                    readLength = readShort.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab))
                {
                    writeShort = (Short[])writeValueTab;
                    writeLength = writeShort.length;
                }
                break;

            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                if (readValueTab != null && !"Nan".equals(readValueTab))
                {
                    readChar = (Byte[])readValueTab;
                    readLength = readChar.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab))
                {
                    writeChar = (Byte[])writeValueTab;
                    writeLength = writeChar.length;
                }
                break;

            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                if (readValueTab != null && !"Nan".equals(readValueTab))
                {
                    readLong = (Integer[])readValueTab;
                    readLength = readLong.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab))
                {
                    writeLong = (Integer[])writeValueTab;
                    writeLength = writeLong.length;
                }
                break;

            default :
                // nothing to do

        } // end switch (dataType)
        if ( readValueTab == null || readLength == 0)
        {
            return null;
        }   
        if ( writeValueTab == null || writeLength == 0)
        {
            return null;
        }

        if (readLength != writeLength )
        {
            return null;
        }

        Object[] ret = null;
        switch ( dataType )
        {

            case TangoConst.Tango_DEV_DOUBLE:
                diffDouble = new Double[readLength];
                for (int i = 0; i < diffDouble.length; i++)
                {
                    diffDouble[i] = new Double(readDouble[i].doubleValue() - writeDouble[i].doubleValue());
                }
                ret = diffDouble;
                break;

            case TangoConst.Tango_DEV_FLOAT:
                diffFloat = new Float[readLength];
                for (int i = 0; i < diffFloat.length; i++)
                {
                    diffFloat[i] = new Float(readFloat[i].floatValue() - writeFloat[i].floatValue());
                }
                ret = diffFloat;
                break;

            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                diffShort = new Short[readLength];
                for (int i = 0; i < diffShort.length; i++)
                {
                    diffShort[i] = new Short ( (short)(readShort[i].shortValue() - writeShort[i].shortValue()) );
                }
                ret = diffShort;
                break;

            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                diffChar = new Byte[readLength];
                for (int i = 0; i < diffChar.length; i++)
                {
                    diffChar[i] = new Byte ( (byte)(readChar[i].byteValue() - writeChar[i].byteValue()) );
                }
                ret = diffChar;
                break;

            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                diffLong = new Integer[readLength];
                for (int i = 0; i < diffLong.length; i++)
                {
                    diffLong[i] = new Integer(readLong[i].intValue() - writeLong[i].intValue());
                }
                ret = diffLong;
                break;

            default :
                // nothing to do

        } // end switch (dataType)

        this.setSpectrumValue ( ret );
        return ret;
    }

    /**
     * Returns a SnapshotAttributeDeltaValue calculated from the values of writeValue and readValue when possible.
     * When impossible, returns a "Not applicable" SnapshotAttributeDeltaValue.
     *
     * @param writeValue The write value
     * @param readValue  The read value
     * @return A SnapshotAttributeDeltaValue calculated from the values of writeValue and readValue when possible
     */
    public static SnapshotAttributeDeltaValue getInstance ( SnapshotAttributeWriteValue writeValue , SnapshotAttributeReadValue readValue )
    {
        switch ( writeValue.getDataType() )
        {
            case TangoConst.Tango_DEV_FLOAT:
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_DOUBLE:
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                return new SnapshotAttributeDeltaValue( writeValue , readValue );

            default:
                return new SnapshotAttributeDeltaValue( SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT , null );
        }
    }

}
