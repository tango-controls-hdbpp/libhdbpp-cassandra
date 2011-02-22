//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/NumberScalar_RO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar_RO.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: NumberScalar_RO.java,v $
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
// Revision 1.5  2006/03/13 14:51:10  ounsy
// Long as an int management
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
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.core.NumberScalarEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class NumberScalar_RO extends NumberScalar {

	private static final long serialVersionUID = 800010L;

	public NumberScalar_RO(HdbModeHandler modeHandler) {
		super(modeHandler);
	}

	protected int getWritableValue() {
		return AttrWriteType._READ;
	}

	public void numberScalarChange(NumberScalarEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		ScalarEvent scalarEvent = new ScalarEvent();

		try {
			scalarEvent.setAttribute_complete_name(event.getNumberSource()
					.getName());

			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(getWritableValue());
			scalarEvent.setData_type(event.getNumberSource().getAttribute()
					.getType());

			scalarEvent.setTimeStamp(event.getTimeStamp());
			// --------------------------------------------------------------------------//
			// ELETTRA : Archiving Events
			// --------------------------------------------------------------------------//
			/*
			 * scalarEvent.setValue(new Short((short) event.getValue() ));
			 * scalarEvent.setValue(new Integer((int) event.getValue() ));
			 * scalarEvent.setValue(new Double((double) event.getValue() ));
			 * scalarEvent.setValue(new Float((float) event.getValue() ));
			 */
			// --------------------------------------------------------------------------//
			// ELETTRA : Archiving Events
			// --------------------------------------------------------------------------//
			switch (scalarEvent.getData_type()) {
			case TangoConst.Tango_DEV_SHORT:
				scalarEvent.setValue(new Short((short) event.getNumberSource()
						.getNumberScalarValue()));
				break;
			case TangoConst.Tango_DEV_USHORT:
				scalarEvent.setValue(new Short((short) event.getNumberSource()
						.getNumberScalarValue()));
				break;
			case TangoConst.Tango_DEV_LONG:
				scalarEvent.setValue(new Integer((int) event.getNumberSource()
						.getNumberScalarValue()));
				break;
			case TangoConst.Tango_DEV_ULONG:
				scalarEvent.setValue(new Integer((int) event.getNumberSource()
						.getNumberScalarValue()));
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				scalarEvent.setValue(new Double(event.getNumberSource()
						.getNumberScalarValue()));
				break;
			case TangoConst.Tango_DEV_FLOAT:
				scalarEvent.setValue(new Float((float) event.getNumberSource()
						.getNumberScalarValue()));
				break;
			default:
				scalarEvent.setValue(new Double(event.getNumberSource()
						.getNumberScalarValue()));
				break;
			}
			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			System.err.println("NumberScalar_RO.numberScalarChange : "
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
			System.err.println("NumberScalar_RO.numberScalarChange : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name() + " values...");
			exE.printStackTrace();
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);

			String message = "Problem in NumberScalar_RO/numberScalarChange";
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
		boolean bvalue;
		double dvalue;
		float fvalue;
		int ivalue;
		short svalue;
		ScalarEvent scalarEvent = new ScalarEvent();

		try {
			attrib = event.getValue();
		} catch (DevFailed f) {
			System.out
					.println("NUMBER SCALAR READ ONLY: Error getting archive event value");
			System.out.println();
			print_exception(f);
			return;
		} catch (Exception e) /* Shouldn't be reached */
		{
			System.out
					.println("NumberScalar_RO.archive.getValue() failed, caught generic Exception, code failure");
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
						.println("\033[1;31mNumberScalar_RO.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}

			scalarEvent.setAttribute_complete_name(proxy.name() + "/"
					+ attrib.getName());
			scalarEvent.setTimeStamp(attrib.getTime());
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ);
			scalarEvent.setData_type(attrib.getType());

			System.out.print(proxy.name() + ": " + attrib.getName()
					+ "{scalar, RO} [\033[1;32mEVENT\033[0m]: ");

			switch (scalarEvent.getData_type()) {
			case TangoConst.Tango_DEV_SHORT:
				svalue = attrib.extractShort();
				scalarEvent.setValue(new Short(svalue));
				System.out.println("short: " + svalue);
				break;
			case TangoConst.Tango_DEV_USHORT:
				ivalue = attrib.extractUShort();
				scalarEvent.setValue(new Integer(ivalue));
				System.out.println("unsigned short: " + ivalue);
				break;
			case TangoConst.Tango_DEV_LONG:
				ivalue = attrib.extractLong();
				scalarEvent.setValue(new Integer(ivalue));
				System.out.println("long: " + ivalue);
				break;
			case TangoConst.Tango_DEV_ULONG:
				ivalue = attrib.extractLong();
				scalarEvent.setValue(new Integer(ivalue));
				System.out.println("unsigned long: " + ivalue);
				break;

			case TangoConst.Tango_DEV_FLOAT:
				fvalue = attrib.extractFloat();
				scalarEvent.setValue(new Float(fvalue));
				System.out.println("float: " + fvalue);
				break;
			case TangoConst.Tango_DEV_BOOLEAN:
				bvalue = attrib.extractBoolean();
				scalarEvent.setValue(new Boolean(bvalue));
				System.out.println("boolean here? :o ");
				break;
			/* The default case, double */
			case TangoConst.Tango_DEV_DOUBLE:
			default:
				dvalue = attrib.extractDouble();
				scalarEvent.setValue(new Double(dvalue));
				System.out.println("double: " + dvalue);
				break;
			}
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
			System.err.println("NumberScalar_RO.archive() : "
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
