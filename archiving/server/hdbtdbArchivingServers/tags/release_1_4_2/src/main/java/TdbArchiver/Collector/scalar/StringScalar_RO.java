/*	Synchrotron Soleil 
 *  
 *   File          :  StringScalar_RO.java
 *  
 *   Project       :  TdbArchiver_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  28 févr. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: StringScalar_RO.java,v 
 *
 */
package TdbArchiver.Collector.scalar;

import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.tangoatk.core.IStringScalar;
import fr.esrf.tangoatk.core.StringScalarEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class StringScalar_RO extends StringScalar
{

    public StringScalar_RO ( TdbModeHandler _modeHandler )
    {
        super( _modeHandler );
    }

	protected int getWritableValue()
	{
		return AttrWriteType._READ;
	}
    
    public void stringScalarChange(StringScalarEvent event)
    {
        int tryNumber = DEFAULT_TRY_NUMBER;
        ScalarEvent scalarEvent = new ScalarEvent();
        scalarEvent.setAttribute_complete_name( ( (IStringScalar)event.getSource() ).getName() );

        try
        {
            scalarEvent.setData_format(AttrDataFormat._SCALAR);
            scalarEvent.setWritable(AttrWriteType._READ);
            scalarEvent.setData_type(( (IStringScalar)event.getSource() ).getAttribute().getType());

            scalarEvent.setTimeStamp(event.getTimeStamp());

            String value = new String( ( (IStringScalar) (event.getSource()) ).getStringValue());
            scalarEvent.setValue( value );
            processEventScalar(scalarEvent , tryNumber);

        }
        catch ( DevFailed devFailed )
        {
            printException(GlobalConst.DATA_TYPE_EXCEPTION , AttrDataFormat._SCALAR , scalarEvent.getAttribute_complete_name() , devFailed);
        }

    }

}
