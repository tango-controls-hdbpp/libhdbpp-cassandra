/*	Synchrotron Soleil 
 *  
 *   File          :  TangoConstWrapper.java
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
 *   Log: TangoConstWrapper.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.TangoDs.TangoConst;

public class TangoConstWrapper 
{
    public static final int Tango_DEV_DOUBLE = TangoConst.Tango_DEV_DOUBLE;
    public static final int Tango_DEV_SHORT = TangoConst.Tango_DEV_SHORT;;
    public static final int Tango_DEV_USHORT = TangoConst.Tango_DEV_USHORT;;
    public static final int Tango_DEV_LONG = TangoConst.Tango_DEV_LONG;;
    public static final int Tango_DEV_ULONG = TangoConst.Tango_DEV_ULONG;
    public static final int Tango_DEV_FLOAT = TangoConst.Tango_DEV_FLOAT;
    public static final int Tango_DEV_BOOLEAN = TangoConst.Tango_DEV_BOOLEAN;
    public static final int Tango_DEV_STRING = TangoConst.Tango_DEV_STRING;
    public static final int Tango_DEV_STATE = TangoConst.Tango_DEV_STATE;
    
    public static final int AttrDataFormat_SCALAR = AttrDataFormat._SCALAR;
    public static final int AttrDataFormat_SPECTRUM = AttrDataFormat._SPECTRUM;
    public static final int AttrDataFormat_IMAGE = AttrDataFormat._IMAGE;
    
    public static final int AttrWriteType_READ = AttrWriteType._READ;
    public static final int AttrWriteType_WRITE = AttrWriteType._WRITE;
    public static final int AttrWriteType_READ_WRITE = AttrWriteType._READ_WRITE;
    public static final int AttrWriteType_READ_WITH_WRITE = AttrWriteType._READ_WITH_WRITE;
    
    public static final String TIMEOUT = "org.omg.CORBA.TIMEOUT";
}
