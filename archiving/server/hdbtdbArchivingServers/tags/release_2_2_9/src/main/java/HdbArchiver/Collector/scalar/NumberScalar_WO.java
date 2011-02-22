//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/NumberScalar_WO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar_WO.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.11 $
//
// $Log: NumberScalar_WO.java,v $
// Revision 1.11  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.10  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.9  2007/06/11 12:21:04  pierrejoseph
// errorEvent method is managed by the mother class
//
// Revision 1.8  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.7  2006/07/18 08:04:02  ounsy
// moved the setAttribute_complete_name call in the try block
//
// Revision 1.6  2006/07/13 13:56:11  ounsy
// added logging in case of errors during XXXValueChanged
//
// Revision 1.5  2006/05/03 14:29:18  ounsy
// compatible with the AttributePolledList in "dispatch" mode
//
// Revision 1.4  2006/03/13 14:51:10  ounsy
// Long as an int management
//
// Revision 1.3  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.2  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.1  2005/09/09 10:03:56  chinkumo
// First commit !
// (Dynamic attribut release)
//
//
// copyleft :	Synchrotron SOLEIL
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
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.core.NumberScalarEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class NumberScalar_WO extends NumberScalar {

    /**
	 *
	 */
    private static final long serialVersionUID = 3795129767870099591L;

    public NumberScalar_WO(final HdbModeHandler modeHandler) {
	super(modeHandler);
    }

    @Override
    protected int getWritableValue() {
	return AttrWriteType._WRITE;
    }

    public void numberScalarChange(final NumberScalarEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	final ScalarEvent scalarEvent = new ScalarEvent();

	try {
	    scalarEvent.setAttribute_complete_name(event.getNumberSource().getName());

	    scalarEvent.setData_format(AttrDataFormat._SCALAR);
	    scalarEvent.setWritable(getWritableValue());
	    scalarEvent.setData_type(event.getNumberSource().getAttribute().getType());

	    scalarEvent.setTimeStamp(event.getTimeStamp());

	    switch (scalarEvent.getData_type()) {
	    case TangoConst.Tango_DEV_SHORT:
		scalarEvent.setValue(new Short((short) event.getNumberSource()
			.getNumberScalarSetPoint()));
		break;
	    case TangoConst.Tango_DEV_USHORT:
		scalarEvent.setValue(new Short((short) event.getNumberSource()
			.getNumberScalarSetPoint()));
		break;
	    case TangoConst.Tango_DEV_LONG:
		scalarEvent.setValue(new Integer((int) event.getNumberSource()
			.getNumberScalarSetPoint()));
		break;
	    case TangoConst.Tango_DEV_ULONG:
		scalarEvent.setValue(new Integer((int) event.getNumberSource()
			.getNumberScalarSetPoint()));
		break;
	    case TangoConst.Tango_DEV_DOUBLE:
		scalarEvent.setValue(new Double(event.getNumberSource().getNumberScalarSetPoint()));
		break;
	    case TangoConst.Tango_DEV_FLOAT:
		scalarEvent.setValue(new Float((float) event.getNumberSource()
			.getNumberScalarSetPoint()));
		break;
	    default:
		scalarEvent.setValue(new Double(event.getNumberSource().getNumberScalarSetPoint()));
		break;
	    }
	    processEventScalar(scalarEvent, tryNumber);

	} catch (final DevFailed devFailed) {
	    System.err.println("NumberScalar_WO.numberScalarChange : "
		    + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...");
	    printException(GlobalConst.DATA_TYPE_EXCEPTION, AttrDataFormat._SCALAR, scalarEvent
		    .getAttribute_complete_name(), devFailed);
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("NumberScalar_WO.numberScalarChange : "
		    + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values. Event : " + event
		    + ", source " + event.getSource());
	    exE.printStackTrace();
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);

	    final String message = "Problem in NumberScalar_RW/numberScalarChange";
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

	try {
	    attrib = event.getValue();
	} catch (final DevFailed f) {
	    System.out.println("Error getting archive event value");
	    System.out.println();
	    print_exception(
		    "\033[1;31mNumberScalar_WO.archive.getValue() failed, caught DevFailed\033[0m",
		    f);
	    return;
	} catch (final Exception e) /* Shouldn't be reached */
	{
	    System.out
		    .println("NumberScalar_WO.archive.getValue() failed, caught generic Exception, code failure");
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
			.println("\033[1;31mNumberScalar_WO.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
		return;
	    }

	    System.out.print(proxy.name() + ": " + attrib.getName()
		    + "{scalar, WO} [\033[1;32mEVENT\033[0m]: ");

	    scalarEvent.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
	    scalarEvent.setTimeStamp(attrib.getTime());
	    scalarEvent.setData_format(AttrDataFormat._SCALAR);
	    scalarEvent.setWritable(AttrWriteType._WRITE);
	    scalarEvent.setData_type(attrib.getType());

	    switch (scalarEvent.getData_type()) {
	    case TangoConst.Tango_DEV_SHORT:
		Short sval = new Short(attrib.extractShort());
		scalarEvent.setValue(sval);
		System.out.println(sval);
		break;
	    case TangoConst.Tango_DEV_USHORT:
		sval = new Short((short) attrib.extractUShort());
		scalarEvent.setValue(sval);
		System.out.println(sval);
		break;
	    case TangoConst.Tango_DEV_LONG:
		/* extractLong() returns an int */
		final int longval = attrib.extractLong();
		scalarEvent.setValue(new Integer(longval));
		System.out.println(longval);
		break;
	    case TangoConst.Tango_DEV_ULONG:
		final int longintval = attrib.extractLong();
		scalarEvent.setValue(new Integer(longintval));
		System.out.println(longintval);
		break;
	    case TangoConst.Tango_DEV_DOUBLE:
		final Double dval = new Double(attrib.extractDouble());
		scalarEvent.setValue(new Double(dval));
		System.out.println(dval);
		break;
	    case TangoConst.Tango_DEV_FLOAT:
		final Float floatval = new Float(attrib.extractFloat());
		scalarEvent.setValue(floatval);
		System.out.println(floatval);
		break;
	    case TangoConst.Tango_DEV_BOOLEAN:
		scalarEvent.setValue(new Boolean(attrib.extractBoolean()));
		break;
	    default:
		scalarEvent.setValue(new Double(attrib.extractDouble()));
		break;
	    }
	    processEventScalar(scalarEvent, tryNumber);

	} catch (final DevFailed devFailed) {
	    System.err.println("NumberScalar_WO.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX
		    + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...");
	    printException(GlobalConst.DATA_TYPE_EXCEPTION, AttrDataFormat._SCALAR, scalarEvent
		    .getAttribute_complete_name(), devFailed);
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("NumberScalar_WO.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX
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
