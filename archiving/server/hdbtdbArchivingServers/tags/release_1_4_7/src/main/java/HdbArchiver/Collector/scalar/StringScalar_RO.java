//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/StringScalar_RO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  StringScalar_RO.
//						(Chinkumo Jean) - Feb 28, 2006
//
// $Author: pierrejoseph $
//
// $Revision: 1.9 $
//
// $Log: StringScalar_RO.java,v $
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
import fr.esrf.tangoatk.core.IStringScalar;
import fr.esrf.tangoatk.core.StringScalarEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class StringScalar_RO extends StringScalar
{
	private static final long serialVersionUID = 29138005L;
	
    public StringScalar_RO ( HdbModeHandler _modeHandler )
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
        
        try
        {
            scalarEvent.setAttribute_complete_name(( (IStringScalar) (event.getSource()) ).getName());
            
            scalarEvent.setData_format(AttrDataFormat._SCALAR);
            scalarEvent.setWritable(getWritableValue());
            scalarEvent.setData_type(( (IStringScalar) (event.getSource()) ).getAttribute().getType());

            scalarEvent.setTimeStamp(event.getTimeStamp());
//        	--------------------------------------------------------------------------//
//        	ELETTRA : Archiving Events 
//        	--------------------------------------------------------------------------//
/*             String value = new String( ( (IStringScalar) (event)).getStringValue());
 * 
 */
//        	--------------------------------------------------------------------------//
//        	ELETTRA : Archiving Events 
//        	--------------------------------------------------------------------------//
            String value = new String( ( (IStringScalar) (event.getSource()) ).getStringValue());
            scalarEvent.setValue( value );
            processEventScalar(scalarEvent , tryNumber);

        }
        catch ( DevFailed devFailed )
        {
            System.err.println("StringScalar_RO.stringScalarChange : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
            printException(GlobalConst.DATA_TYPE_EXCEPTION , AttrDataFormat._SCALAR , scalarEvent.getAttribute_complete_name() , devFailed);
            Object value = null;
            scalarEvent.setValue(value);
            processEventScalar(scalarEvent , tryNumber);
        }
        catch ( Exception exE )
        {
            System.err.println("StringScalar_RO.stringScalarChange : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
            exE.printStackTrace();
            Object value = null;
            scalarEvent.setValue(value);
            processEventScalar(scalarEvent , tryNumber);
            
            String message = "Problem in StringScalar_RO/stringScalarChange";
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
		/* The scalar event which must be filled in and finally archived. */
		ScalarEvent scalarEvent = new ScalarEvent();
		String svalue;
		
		try{
			attrib = event.getValue();
		}
		catch(DevFailed f)
		{
			print_exception("Error getting archive event value \033[1;31mStringScalar_RO.archive.getValue() failed, caught DevFailed\033[0m", 
					f);
		    return;
		}
		catch (Exception e) /* Shouldn't be reached */
		{
			System.out.println("StateScalar_RO.archive.getValue() failed, caught generic Exception, code failure");
			e.printStackTrace();
			return;
		}
		
		try{
			/* See NumberScalar_RW.java for comments */
			arch = (TangoArchive) event.getSource();
			proxy = arch.getEventSupplier(); /* The device that supplied the event */
			if(arch == null || proxy == null || attrib == null)
			{
				System.out.println("\033[1;31mStringScalar_RO.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}
			System.out.print(proxy.name() + ": " +attrib.getName() + 
				"{string scalar, RO} [\033[1;32mEVENT\033[0m]: ");
			
			scalarEvent.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
			scalarEvent.setTimeStamp(attrib.getTime() );
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ);
			scalarEvent.setData_type(attrib.getType());
			
			/* Extract the string  */
			svalue = new String (attrib.extractString() );
			/* The code goes on as in the stringScalarChange() below */
			System.out.println(svalue);
			
            scalarEvent.setValue( svalue ); /* complete the scalarEvent */
            processEventScalar(scalarEvent , tryNumber);
		} 
		catch ( DevFailed devFailed )
        {
            print_exception("StringScalar_RO.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX + 
            		"\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...",
            		devFailed);
            Object value = null;
            scalarEvent.setValue(value);
            processEventScalar(scalarEvent , tryNumber);
        }
        catch ( Exception exE )
        {
            System.err.println("StringScalar_RO.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + scalarEvent.getAttribute_complete_name() + " values...");
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
