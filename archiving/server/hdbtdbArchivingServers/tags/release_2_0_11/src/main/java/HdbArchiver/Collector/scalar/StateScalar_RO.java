//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/StateScalar_RO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  StateScalar_RO.
//						(Chinkumo Jean) - March 8, 2006
//
// $Author: pierrejoseph $
//
// $Revision: 1.9 $
//
// $Log: StateScalar_RO.java,v $
// Revision 1.9  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.8  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package HdbArchiver.Collector.scalar;

import HdbArchiver.Collector.HdbModeHandler;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.events.TangoArchive;
import fr.esrf.TangoApi.events.TangoArchiveEvent;
import fr.esrf.tangoatk.core.DevStateScalarEvent;
import fr.esrf.tangoatk.core.IDevStateScalar;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoStateTranslation;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;
//--------------------------------------------------------------------------//
//ELETTRA : Archiving Events 
//--------------------------------------------------------------------------//
public class StateScalar_RO extends StateScalar
{
	private static final long serialVersionUID = 92112001L;
	
    public StateScalar_RO ( HdbModeHandler _modeHandler )
    {
        super( _modeHandler );
    }

    protected int getWritableValue()
    {
    	return AttrWriteType._READ;
    }
    
    public void devStateScalarChange ( DevStateScalarEvent event )
    {
        int tryNumber = DEFAULT_TRY_NUMBER;
        ScalarEvent scalarEvent = new ScalarEvent();
        
        try
        {
            scalarEvent.setAttribute_complete_name( ((IDevStateScalar)event.getSource()).getName() );
            
            scalarEvent.setData_format(AttrDataFormat._SCALAR);
            scalarEvent.setWritable(getWritableValue());
            scalarEvent.setData_type(( ( IDevStateScalar ) event.getSource() ).getAttribute().getType());

            scalarEvent.setTimeStamp(event.getTimeStamp());
//        	--------------------------------------------------------------------------//
//        	ELETTRA : Archiving Events 
//        	--------------------------------------------------------------------------//
/*
 * String value = ( ( IDevStateScalar ) event).getValue();
 */
//        	--------------------------------------------------------------------------//
//        	ELETTRA : Archiving Events 
//        	--------------------------------------------------------------------------//
            String value = ( ( IDevStateScalar ) event.getSource() ).getValue();
            scalarEvent.setValue(TangoStateTranslation.getTangoDevState(value));
            processEventScalar(scalarEvent , tryNumber);

        }
        catch ( DevFailed devFailed )
        {
            System.err.println("StateScalar_RO.devStateScalarChange : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
            printException(GlobalConst.DATA_TYPE_EXCEPTION , AttrDataFormat._SCALAR , scalarEvent.getAttribute_complete_name() , devFailed);
            Object value = null;
            scalarEvent.setValue(value);
            processEventScalar(scalarEvent , tryNumber);
        }
        catch ( Exception exE )
        {
            System.err.println("StateScalar_RO.devStateScalarChange : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
            exE.printStackTrace();
            Object value = null;
            scalarEvent.setValue(value);
            processEventScalar(scalarEvent , tryNumber);
            
            String message = "Problem in StateScalar_RO/devStateScalarChange";
            super.m_logger.trace ( ILogger.LEVEL_ERROR , message );
            super.m_logger.trace ( ILogger.LEVEL_ERROR , exE );
        }
    }

//	--------------------------------------------------------------------------//
//	ELETTRA : Archiving Events 
//	--------------------------------------------------------------------------//
    public void archive(TangoArchiveEvent event)
	{
    	int tryNumber = DEFAULT_TRY_NUMBER;
		DeviceAttribute attrib = null;
		TangoArchive arch;
		DeviceProxy proxy;
		
		ScalarEvent scalarEvent = new ScalarEvent();
		
		try{
			attrib = event.getValue();
		}
		catch(DevFailed f)
		{
			System.out.println("Error getting archive event value");
			System.out.println();
			printException(GlobalConst.DATA_TYPE_EXCEPTION , AttrDataFormat._SCALAR,
					"\033[1;31mStateScalar_RO.archive.getValue() failed, caught DevFailed\033[0m", f);
		    return;
		}
		catch (Exception e) /* Shouldn't be reached */
		{
			System.out.println("StateScalar_RO.archive.getValue() failed, caught generic Exception, code failure");
			e.printStackTrace();
			return;
		}
		
		try{
			/* To correctly archive the attribute, we have to know its complete name.
			 * To acquire this information, we must o back to the TangoArchive object
			 * (which contains the DeviceProxy).
			 */
			arch = (TangoArchive) event.getSource();
			proxy = arch.getEventSupplier(); /* The device that supplied the event */
			if(arch == null || proxy == null || attrib == null)
			{
				System.out.println("\033[1;31mStateScalar_RO: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}
			System.out.println(proxy.name() + ": " +attrib.getName() + 
				"{state scalar, RO} [\033[1;32mEVENT\033[0m]: ");
		
			scalarEvent.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
			scalarEvent.setTimeStamp(attrib.getTime() );
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ);
			scalarEvent.setData_type(attrib.getType());
			
			scalarEvent.setValue(attrib.extractState() );
			processEventScalar(scalarEvent , tryNumber);
			System.out.println(attrib.extractState());

		}
		catch ( DevFailed devFailed )
		{
			System.err.println("NumberScalar_RO.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
			printException(GlobalConst.DATA_TYPE_EXCEPTION , AttrDataFormat._SCALAR , scalarEvent.getAttribute_complete_name() , devFailed);
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent , tryNumber);
		}
		catch ( Exception exE )
		{
			System.err.println("NumberScalar_RO.archive : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
			exE.printStackTrace();
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent , tryNumber);
		}

	} 
//	--------------------------------------------------------------------------//
//	ELETTRA : Archiving Events 
//	--------------------------------------------------------------------------//
}
