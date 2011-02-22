//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/StringScalar_RW.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  StringScalar_RW.
//						(Chinkumo Jean) - Feb. 28, 2006
//
// $Author: pierrejoseph $
//
// $Revision: 1.9 $
//
// $Log: StringScalar_RW.java,v $
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
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class StringScalar_RW extends StringScalar {

    /**
	 *
	 */
    private static final long serialVersionUID = -6664045409944983610L;

    public StringScalar_RW(final HdbModeHandler _modeHandler) {
	super(_modeHandler);
    }

    @Override
    protected int getWritableValue() {
	return AttrWriteType._READ_WRITE;
    }

    public void stringScalarChange(final StringScalarEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	final ScalarEvent scalarEvent = new ScalarEvent();
	String[] svalue;

	try {
	    scalarEvent.setAttribute_complete_name(((IStringScalar) event.getSource()).getName());

	    scalarEvent.setData_format(AttrDataFormat._SCALAR);
	    scalarEvent.setWritable(getWritableValue());
	    scalarEvent.setData_type(((IStringScalar) event.getSource()).getAttribute().getType());

	    scalarEvent.setTimeStamp(event.getTimeStamp());
	    // --------------------------------------------------------------------------//
	    // ELETTRA : Archiving Events
	    // --------------------------------------------------------------------------//
	    /*
	     * svalue[0] = event.getValue();
	     */
	    // --------------------------------------------------------------------------//
	    // ELETTRA : Archiving Events
	    // --------------------------------------------------------------------------//
	    svalue = new String[2];
	    svalue[0] = ((IStringScalar) event.getSource()).getStringValue();
	    svalue[1] = ((IStringScalar) event.getSource()).getStringSetPoint();
	    scalarEvent.setValue(svalue);
	    processEventScalar(scalarEvent, tryNumber);

	} catch (final DevFailed devFailed) {
	    System.err.println("StringScalar_RW.stringScalarChange : "
		    + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...");
	    printException(GlobalConst.DATA_TYPE_EXCEPTION, AttrDataFormat._SCALAR, scalarEvent
		    .getAttribute_complete_name(), devFailed);
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("StringScalar_RW.stringScalarChange : "
		    + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values. Event : " + event
		    + ", source " + event.getSource());
	    exE.printStackTrace();
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);

	    final String message = "Problem in StringScalar_RW/stringScalarChange";
	    super.m_logger.trace(ILogger.LEVEL_ERROR, message);
	    super.m_logger.trace(ILogger.LEVEL_ERROR, exE);
	}
    }

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
    /* Invoked on the reception of a tango archive event. */
    public void archive(final TangoArchiveEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	DeviceAttribute attrib = null;
	TangoArchive arch;
	DeviceProxy proxy;
	/* The scalar event which must be filled in and finally archived. */
	final ScalarEvent scalarEvent = new ScalarEvent();
	String svalue[];

	try {
	    attrib = event.getValue();
	} catch (final DevFailed f) {
	    print_exception(
		    "Error getting archive event value \033[1;31mStringScalar_RW.archive.getValue() failed, caught DevFailed\033[0m",
		    f);
	    return;
	} catch (final Exception e) /* Shouldn't be reached */
	{
	    System.out
		    .println("StringScalar_RW.archive.getValue() failed, caught generic Exception, code failure");
	    e.printStackTrace();
	    return;
	}

	try {
	    /*
	     * To correctly archive the attribute, we have to know its complete
	     * name. To acquire this information, we must o back to the
	     * TangoArchive object (which contains the DeviceProxy).
	     */
	    arch = (TangoArchive) event.getSource();
	    proxy = arch.getEventSupplier(); /*
					      * The device that supplied the
					      * event
					      */
	    if (arch == null || proxy == null || attrib == null) {
		System.out
			.println("\033[1;31mStringScalar_RW.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
		return;
	    }
	    System.out.println("\033[1;35marchive() [String Scalar RW]: [EVENT]: \033[1;32m"
		    + proxy.name() + "/" + attrib.getName() + "\033[0m");

	    scalarEvent.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
	    scalarEvent.setTimeStamp(attrib.getTime());
	    scalarEvent.setData_format(AttrDataFormat._SCALAR);
	    scalarEvent.setWritable(AttrWriteType._READ_WRITE);
	    scalarEvent.setData_type(attrib.getType());

	    /* Extract the string (get value and set value) */
	    svalue = attrib.extractStringArray();
	    /* The code goes on as in the stringScalarChange() below */
	    for (int i = 0; i < svalue.length; i++) {
		final String value = StringFormater.formatStringToWrite(svalue[i]);
		svalue[i] = value;

	    }
	    System.out.print(proxy.name() + ": " + attrib.getName()
		    + "{string scalar, RW} [\033[1;32mEVENT\033[0m]: ");

	    System.out.println("\033[1;35m(R): " + svalue[0] + "\033[0m, (Set):\033[1;36m "
		    + svalue[1] + "\033[0m");
	    scalarEvent.setValue(svalue); /* complete the scalarEvent */

	    processEventScalar(scalarEvent, tryNumber);
	} catch (final DevFailed devFailed) {
	    print_exception("StringScalar_RW.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX
		    + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...", devFailed);
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("StringScalar_RW.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX
		    + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...");
	    exE.printStackTrace();
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	}
    }
    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
}
