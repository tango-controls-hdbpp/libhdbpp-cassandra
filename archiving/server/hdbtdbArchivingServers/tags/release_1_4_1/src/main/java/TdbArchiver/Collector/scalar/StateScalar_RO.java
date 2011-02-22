/*	Synchrotron Soleil 
 *  
 *   File          :  StateScalar_RO.java
 *  
 *   Project       :  TdbArchiver_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  10 mars 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: StateScalar_RO.java,v 
 *
 */
package TdbArchiver.Collector.scalar;

import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.tangoatk.core.DevStateScalarEvent;
import fr.esrf.tangoatk.core.IDevStateScalar;
import fr.esrf.tangoatk.core.IDevice;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class StateScalar_RO extends StateScalar
{

    public StateScalar_RO ( TdbModeHandler _modeHandler )
    {
        super( _modeHandler );
    }

    public void devStateScalarChange ( DevStateScalarEvent event )
    {
        int tryNumber = DEFAULT_TRY_NUMBER;
         ScalarEvent scalarEvent = new ScalarEvent();
            scalarEvent.setAttribute_complete_name( ((IDevStateScalar)event.getSource()).getName() );

            try
            {
                scalarEvent.setData_format(AttrDataFormat._SCALAR);
                scalarEvent.setWritable(AttrWriteType._READ);
                scalarEvent.setData_type(( ( IDevStateScalar ) event.getSource() ).getAttribute().getType());

                scalarEvent.setTimeStamp(event.getTimeStamp());

                String value = ( ( IDevStateScalar ) event.getSource() ).getValue();
                if (value.equals(IDevice.ON))
                {
                    scalarEvent.setValue(DevState.ON);
                }
                else if (value.equals(IDevice.OFF))
                {
                    scalarEvent.setValue(DevState.OFF);
                }
                else if (value.equals(IDevice.CLOSE))
                {
                    scalarEvent.setValue(DevState.CLOSE);
                }
                else if (value.equals(IDevice.OPEN))
                {
                    scalarEvent.setValue(DevState.OPEN);
                }
                else if (value.equals(IDevice.INSERT))
                {
                    scalarEvent.setValue(DevState.INSERT);
                }
                else if (value.equals(IDevice.EXTRACT))
                {
                    scalarEvent.setValue(DevState.EXTRACT);
                }
                else if (value.equals(IDevice.MOVING))
                {
                    scalarEvent.setValue(DevState.MOVING);
                }
                else if (value.equals(IDevice.STANDBY))
                {
                    scalarEvent.setValue(DevState.STANDBY);
                }
                else if (value.equals(IDevice.FAULT))
                {
                    scalarEvent.setValue(DevState.FAULT);
                }
                else if (value.equals(IDevice.INIT))
                {
                    scalarEvent.setValue(DevState.INIT);
                }
                else if (value.equals(IDevice.RUNNING))
                {
                    scalarEvent.setValue(DevState.RUNNING);
                }
                else if (value.equals(IDevice.ALARM))
                {
                    scalarEvent.setValue(DevState.ALARM);
                }
                else if (value.equals(IDevice.DISABLE))
                {
                    scalarEvent.setValue(DevState.DISABLE);
                }
                else
                {
                    scalarEvent.setValue(DevState.UNKNOWN);
                }

                processEventScalar(scalarEvent , tryNumber);

            }
            catch ( DevFailed devFailed )
            {
                printException(GlobalConst.DATA_TYPE_EXCEPTION , AttrDataFormat._SCALAR , scalarEvent.getAttribute_complete_name() , devFailed);
            }
    }
    
    protected int getWritableValue()
    {
    	return AttrWriteType._READ;
    }
  /*  public void errorChange(ErrorEvent errorEvent)
    {
        int tryNumber = DEFAULT_TRY_NUMBER;
        Util.out3.println("StateScalar_RO.errorChange : " + "Unable to read the attribute named " + ( (IDevStateScalar) errorEvent.getSource() ).getName());
        Object value = null;
        ScalarEvent scalarEvent = new ScalarEvent();
        scalarEvent.setAttribute_complete_name( ((IDevStateScalar)errorEvent.getSource()).getName() );
        scalarEvent.setWritable(AttrWriteType._READ);
        scalarEvent.setTimeStamp(errorEvent.getTimeStamp());
        scalarEvent.setValue(value);
        processEventScalar(scalarEvent , tryNumber);
    }
*/
}
