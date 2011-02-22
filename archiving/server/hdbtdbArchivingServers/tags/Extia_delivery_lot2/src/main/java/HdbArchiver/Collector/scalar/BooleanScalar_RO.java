//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/BooleanScalar_RO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BooleanScalar_RO.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: BooleanScalar_RO.java,v $
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
// Revision 1.8  2006/07/21 14:43:08  ounsy
// minor changes
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

public class BooleanScalar_RO extends BooleanScalar {
	private static final long serialVersionUID = 10046L;

	public BooleanScalar_RO(HdbModeHandler modeHandler) {
		super(modeHandler);
	}

	protected int getWritableValue() {
		return AttrWriteType._READ;
	}

	public void booleanScalarChange(BooleanScalarEvent event) {
		ScalarEvent scalarEvent = new ScalarEvent();
		int tryNumber = DEFAULT_TRY_NUMBER;

		try {
			scalarEvent.setAttribute_complete_name(((IBooleanScalar) event
					.getSource()).getName());

			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(getWritableValue());
			scalarEvent.setData_type(((IBooleanScalar) event.getSource())
					.getAttribute().getType());
			// Null pointer ( IBooleanScalar ) event.getSource()
			// ).getAttribute() is null sometimes

			scalarEvent.setTimeStamp(event.getTimeStamp());

			// --------------------------------------------------------------------------//
			// ELETTRA : Archiving Events
			// --------------------------------------------------------------------------//
			/*
			 * In case of error the line can be modified as followed Boolean
			 * readvalue; readvalue = event.getValue(); System.out.println( ( (
			 * IBooleanScalar ) event.getSource() ).getName() +
			 * "{boolean, RO} [\033[1;36mPOLLED\033[0m]: " + readvalue);
			 * 
			 * scalarEvent.setValue(readvalue);
			 */
			// --------------------------------------------------------------------------//
			// ELETTRA : Archiving Events
			// --------------------------------------------------------------------------//
			scalarEvent.setValue(new Boolean(((IBooleanScalar) event
					.getSource()).getValue()));

			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			System.err.println("BooleanScalar_RO.numberScalarChange : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name() + " values...");
			printException(GlobalConst.DATA_TYPE_EXCEPTION,
					AttrDataFormat._SCALAR, scalarEvent
							.getAttribute_complete_name(), devFailed);
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);
		} catch (Exception exE) {
			System.err.println("BooleanScalar_RO.numberScalarChange : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name() + " values...");
			exE.printStackTrace();
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);

			String message = "Problem in BooleanScalar_RO/booleanScalarChange";
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
	public void archive(TangoArchiveEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		DeviceAttribute attrib = null;
		TangoArchive arch;
		DeviceProxy proxy;
		ScalarEvent scalarEvent = new ScalarEvent();
		Boolean bvalue;

		try {
			attrib = event.getValue();
		} catch (DevFailed f) {
			System.out.println("Error getting archive event attribute value");
			System.out.println();
			printException(
					GlobalConst.DATA_TYPE_EXCEPTION,
					AttrDataFormat._SCALAR,
					"\033[1;31mBooleanScalar_RO.archive.getValue() failed, caught DevFailed\033[0m",
					f);
			return;
		} catch (Exception e) /* Shouldn't be reached */
		{
			System.out
					.println("BooleanScalar_RO.archive.getValue() failed, caught generic Exception, code failure");
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
						.println("\033[1;31mBooleanScalar_RO: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}

			scalarEvent.setAttribute_complete_name(proxy.name() + "/"
					+ attrib.getName());
			scalarEvent.setTimeStamp(attrib.getTime());
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ);
			scalarEvent.setData_type(attrib.getType());

			bvalue = attrib.extractBoolean();
			scalarEvent.setValue(bvalue);

			System.out.println(proxy.name() + ": " + attrib.getName()
					+ "{boolean, RO} [\033[1;32mEVENT\033[0m]: " + bvalue);

			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			print_exception("NumberScalar_RO.archive() : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name() + " values...",
					devFailed);
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);
		} catch (Exception exE) {
			System.err.println("NumberScalar_RO.archive : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name() + " values...");
			exE.printStackTrace();
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);
		}

	}
	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events
	// --------------------------------------------------------------------------//
}
