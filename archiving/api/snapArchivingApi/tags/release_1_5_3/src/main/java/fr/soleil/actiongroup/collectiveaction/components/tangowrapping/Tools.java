package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.CommunicationFailed;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoDs.TangoConst;

public class Tools 
{
    public static boolean isDueToATimeout ( Throwable e )
    {
        boolean ret = false;
        
        if ( e instanceof CommunicationFailed )
        {
            DevError[] errs = ( (CommunicationFailed) e ).errors;
            if ( errs != null )
            {
                for ( int i = 0 ; i < errs.length ; i++ )
                {
                    if ( errs [ i ].reason.indexOf ( TangoConstWrapper.TIMEOUT ) != -1 )
                    {
                        ret = true;
                        break;
                    }
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Extracts the numeric value from a raw numeric attribute
     * @param deviceAttribute
     * @return The numeric value
     * @throws DevFailed The numeric values extraction failed
     */
    public static double toDouble(DeviceAttribute deviceAttribute) throws DevFailedWrapper 
    {
        try
        {
            switch (deviceAttribute.getType()) 
            {
                case TangoConst.Tango_DEV_DOUBLE:
                    return (deviceAttribute.extractDoubleArray())[0];
                case TangoConst.Tango_DEV_SHORT:
                case TangoConst.Tango_DEV_USHORT:
                    return (deviceAttribute.extractShortArray())[0];
                case TangoConst.Tango_DEV_LONG:
                case TangoConst.Tango_DEV_ULONG:
                    return (deviceAttribute.extractLongArray())[0];
                case TangoConst.Tango_DEV_FLOAT:
                    return (deviceAttribute.extractFloatArray())[0];
                case TangoConst.Tango_DEV_BOOLEAN:
                    return (deviceAttribute.extractBooleanArray())[0] ? 1. : 0.;
                default:
                    return 0.;
            }
        }
        catch ( DevFailed df )
        {
            throw new DevFailedWrapper ( df );
        }
    }
}
