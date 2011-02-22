//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/BooleanScalar_WO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BooleanScalar_WO.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: BooleanScalar_WO.java,v $
// Revision 1.12  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.11  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.10  2007/06/11 12:21:04  pierrejoseph
// errorEvent method is managed by the mother class
//
// Revision 1.9  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.8  2006/07/18 08:04:02  ounsy
// moved the setAttribute_complete_name call in the try block
//
// Revision 1.7  2006/07/13 13:56:11  ounsy
// added logging in case of errors during XXXValueChanged
//
// Revision 1.6  2006/05/03 14:29:18  ounsy
// compatible with the AttributePolledList in "dispatch" mode
//
// Revision 1.5  2006/01/27 13:06:40  ounsy
// organised imports
//
// Revision 1.4  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.2  2005/09/26 08:01:20  chinkumo
// Minor changes !
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
import fr.esrf.tangoatk.core.BooleanScalarEvent;
import fr.esrf.tangoatk.core.IBooleanScalar;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class BooleanScalar_WO extends BooleanScalar {

    /**
	 *
	 */
    private static final long serialVersionUID = -4029883084366891708L;

    public BooleanScalar_WO(final HdbModeHandler modeHandler) {
	super(modeHandler);
    }

    @Override
    protected int getWritableValue() {
	return AttrWriteType._WRITE;
    }

    public void booleanScalarChange(final BooleanScalarEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	final ScalarEvent scalarEvent = new ScalarEvent();

	try {
	    scalarEvent.setAttribute_complete_name(((IBooleanScalar) event.getSource()).getName());

	    scalarEvent.setData_format(AttrDataFormat._SCALAR);
	    scalarEvent.setWritable(getWritableValue());
	    scalarEvent.setData_type(((IBooleanScalar) event.getSource()).getAttribute().getType());

	    scalarEvent.setTimeStamp(event.getTimeStamp());

	    scalarEvent.setValue(new Boolean(((IBooleanScalar) event.getSource()).getSetPoint()));

	    processEventScalar(scalarEvent, tryNumber);

	} catch (final DevFailed devFailed) {
	    System.err.println("BooleanScalar_WO.numberScalarChange : "
		    + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...");
	    printException(GlobalConst.DATA_TYPE_EXCEPTION, AttrDataFormat._SCALAR, scalarEvent
		    .getAttribute_complete_name(), devFailed);
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("BooleanScalar_WO.numberScalarChange : "
		    + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values. Event : " + event
		    + ", source " + event.getSource());
	    exE.printStackTrace();
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);

	    final String message = "Problem in BooleanScalar_WO/booleanScalarChange";
	    super.m_logger.trace(ILogger.LEVEL_ERROR, message);
	    super.m_logger.trace(ILogger.LEVEL_ERROR, exE);
	}
    }

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
    /**
     * @since events
     * @author giacomo Implements the boolean scalar archive events for the read
     *         only type
     */
    public void archive(final TangoArchiveEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	DeviceAttribute attrib = null;
	TangoArchive arch;
	DeviceProxy proxy;
	final ScalarEvent scalarEvent = new ScalarEvent();

	try {
	    attrib = event.getValue();
	} catch (final DevFailed f) {
	    System.out.println("Error getting archive event attribute value");
	    print_exception(
		    "\033[1;31mBooleanScalar_WO.archive.getValue() failed, caught DevFailed\033[0m",
		    f);
	    return;
	} catch (final Exception e) /* Shouldn't be reached */
	{
	    System.out
		    .println("BooleanScalar_WO.archive.getValue() failed, caught generic Exception, code failure");
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
			.println("\033[1;31mBooleanScalar_WO: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
		return;
	    }

	    scalarEvent.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
	    scalarEvent.setTimeStamp(attrib.getTime());
	    scalarEvent.setData_format(AttrDataFormat._SCALAR);
	    scalarEvent.setWritable(AttrWriteType._WRITE);
	    scalarEvent.setData_type(attrib.getType());

	    final Boolean bvalue = attrib.extractBoolean();
	    scalarEvent.setValue(bvalue);

	    System.out.println(((IBooleanScalar) event.getSource()).getName()
		    + "{boolean, WO} [\033[1;36mPOLLED\033[0m]: " + bvalue);
	    processEventScalar(scalarEvent, tryNumber);

	} catch (final DevFailed devFailed) {
	    print_exception("NumberScalar_WO.archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX
		    + "\r\n\t" + "Problem while reading "
		    + scalarEvent.getAttribute_complete_name() + " values...", devFailed);
	    final Object value = null;
	    scalarEvent.setValue(value);
	    processEventScalar(scalarEvent, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("BooleanScalar_WO.archive : " + GlobalConst.ARCHIVING_ERROR_PREFIX
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
