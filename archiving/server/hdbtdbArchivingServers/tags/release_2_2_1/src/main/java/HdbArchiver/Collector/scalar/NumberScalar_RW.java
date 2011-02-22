//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/NumberScalar_RW.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar_RW.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.13 $
//
// $Log: NumberScalar_RW.java,v $
// Revision 1.13  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.12  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.11  2007/06/11 12:21:04  pierrejoseph
// errorEvent method is managed by the mother class
//
// Revision 1.10  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
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

//--------------------------------------------------------------------------//
//ELETTRA : Archiving Events
//--------------------------------------------------------------------------//
public class NumberScalar_RW extends NumberScalar {


	/**
	 *
	 */
	private static final long serialVersionUID = -5039552396416573704L;

	public NumberScalar_RW(HdbModeHandler modeHandler) {
		super(modeHandler);
	}

	protected int getWritableValue() {
		return AttrWriteType._READ_WRITE;
	}

	public void numberScalarChange(NumberScalarEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		ScalarEvent scalarEvent = new ScalarEvent();

		Double[] dvalue;
		Float[] fvalue;
		Integer[] ivalue;
		Short[] svalue;
		try {
			scalarEvent.setAttribute_complete_name(event.getNumberSource()
					.getName());

			scalarEvent.setTimeStamp(event.getTimeStamp());
			/*
			 * long fixed = 1153484775366L; long l; double seuil= 0.7; if (
			 * Math.random () < seuil ) { System.out.println (
			 * "NumberScalar_RW/numberScalarChange/fixed/"+new Timestamp (fixed)
			 * ); l =fixed; } else { l =event.getTimeStamp(); }
			 * scalarEvent.setTimeStamp( l );
			 */

			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(getWritableValue());
			scalarEvent.setData_type(event.getNumberSource().getAttribute()
					.getType());
			// --------------------------------------------------------------------------//
			// ELETTRA : Archiving Events
			// --------------------------------------------------------------------------//
			/*
			 * svalue[0] = new Short((short)event.getValue()); ivalue[0] = new
			 * Integer((int)event.getValue()); dvalue[0] = new
			 * Double(event.getValue()); fvalue[0] = new
			 * Float((float)event.getValue());
			 */
			// --------------------------------------------------------------------------//
			// ELETTRA : Archiving Events
			// --------------------------------------------------------------------------//
			switch (scalarEvent.getData_type()) {
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				svalue = new Short[2];
				svalue[0] = new Short((short) event.getNumberSource()
						.getNumberScalarValue());
				svalue[1] = new Short((short) event.getNumberSource()
						.getNumberScalarSetPoint());
				scalarEvent.setValue(svalue);
				break;
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				ivalue = new Integer[2];
				ivalue[0] = new Integer((int) event.getNumberSource()
						.getNumberScalarValue());
				ivalue[1] = new Integer((int) event.getNumberSource()
						.getNumberScalarSetPoint());
				scalarEvent.setValue(ivalue);
				break;
			case TangoConst.Tango_DEV_FLOAT:
				fvalue = new Float[2];
				fvalue[0] = new Float((float) event.getNumberSource()
						.getNumberScalarValue());
				fvalue[1] = new Float((float) event.getNumberSource()
						.getNumberScalarSetPoint());
				scalarEvent.setValue(fvalue);
				break;
			case TangoConst.Tango_DEV_DOUBLE:
			default:
				dvalue = new Double[2];
				dvalue[0] = new Double(event.getNumberSource()
						.getNumberScalarValue());
				dvalue[1] = new Double(event.getNumberSource()
						.getNumberScalarSetPoint());
				scalarEvent.setValue(dvalue);
				break;
			}
			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			System.err.println("NumberScalar_RW.numberScalarChange : "
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
			System.err.println("NumberScalar_RW.numberScalarChange : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name() + " values...");
			exE.printStackTrace();
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);

			String message = "Problem in NumberScalar_RW/numberScalarChange";
			super.m_logger.trace(ILogger.LEVEL_ERROR, message);
			super.m_logger.trace(ILogger.LEVEL_ERROR, exE);
		}

	}

	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events Begin
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
		Boolean[] bvalue = { false, false };
		Double[] dvalue = { 0.0, 0.0 };
		Float[] fvalue = { (float) 0, (float) 0 };
		Integer[] ivalue = { 0, 0 };
		Short[] svalue = { 0, 0 };
		ScalarEvent scalarEvent = new ScalarEvent();

		try {
			attrib = event.getValue();
		} catch (DevFailed f) {
			System.out
					.println("NumberScalar_RW.java: Error getting archive event value for the event \033[1;31m"
							+ event.toString() + "\033[0m");
			System.out.println();
			print_exception(f);
			return;
		} catch (Exception e) /* Shouldn't be reached */
		{
			System.out
					.println("NumberScalar_RW.archive.getValue() failed, caught generic Exception, code failure");
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
						.println("\033[1;31mNumberScalar_RW.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}

			System.out.print(proxy.name() + ": " + attrib.getName()
					+ "{scalar, RW} [\033[1;32mEVENT\033[0m]: ");

			scalarEvent.setAttribute_complete_name(proxy.name() + "/"
					+ attrib.getName());
			scalarEvent.setTimeStamp(attrib.getTime());
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ_WRITE);
			scalarEvent.setData_type(attrib.getType());

			switch (scalarEvent.getData_type()) {
			case TangoConst.Tango_DEV_SHORT:
				short[] shvalue = attrib.extractShortArray();
				svalue[0] = (Short) shvalue[0];
				svalue[1] = (Short) shvalue[1];
				scalarEvent.setValue(svalue);
				System.out.println("short (R): " + svalue[0] + " (W): "
						+ svalue[1]);
				break;
			case TangoConst.Tango_DEV_USHORT:
				/*
				 * extractUShortArray() returns an int[]. Then it cannot be
				 * casted to Short[]. If we do this, a cast exception is thrown.
				 * We probably could convert int to short and then short to
				 * Short... But anyway we lose the `unsigned' information
				 */
				short ushvalue[] = attrib.extractShortArray();
				svalue[0] = (Short) ushvalue[0];
				svalue[1] = (Short) ushvalue[1];
				scalarEvent.setValue(svalue);
				System.out.println("unsigned short (R): " + svalue[0]
						+ " (W): " + svalue[1]);
				break;
			case TangoConst.Tango_DEV_LONG:
				int[] intvalue = attrib.extractLongArray();
				ivalue[0] = (Integer) intvalue[0];
				ivalue[1] = (Integer) intvalue[1];
				scalarEvent.setValue(ivalue);
				System.out.println("long (R): " + ivalue[0] + " (W): "
						+ ivalue[1]);
				break;
			case TangoConst.Tango_DEV_ULONG:
				int[] intulongvalue = attrib.extractLongArray();
				ivalue[0] = (Integer) intulongvalue[0];
				ivalue[1] = (Integer) intulongvalue[1];
				scalarEvent.setValue(ivalue);
				System.out.println("unsigned long (R): " + ivalue[0] + " (W): "
						+ ivalue[1]);
				break;

			case TangoConst.Tango_DEV_FLOAT:
				float[] fval = attrib.extractFloatArray();
				fvalue[0] = (Float) fval[0];
				fvalue[1] = (Float) fval[1];
				scalarEvent.setValue(fvalue);
				System.out.println("float (R): " + fvalue[0] + " (W): "
						+ fvalue[1]);
				break;
			case TangoConst.Tango_DEV_BOOLEAN:
				boolean[] bovalue = attrib.extractBooleanArray();
				bvalue[0] = (Boolean) bovalue[0];
				bvalue[1] = (Boolean) bovalue[1];
				scalarEvent.setValue(bvalue);
				System.out.println("boolean (Here?!?! :o ) (R): " + bvalue[0]
						+ " (W): " + bvalue[1]);
				break;

			case TangoConst.Tango_DEV_DOUBLE:
			default:
				double dovalue[] = attrib.extractDoubleArray();
				dvalue[0] = (Double) dovalue[0];
				dvalue[1] = (Double) dovalue[1];
				scalarEvent.setValue(dvalue);
				System.out.println("double (R): " + dvalue[0] + " (W): "
						+ dvalue[1]);
				break;
			}
			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			print_exception("NumberScalar_RW.archive() : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name()
					+ " values (DevFailed)...", devFailed);
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);
		} catch (Exception exE) {
			System.err.println("NumberScalar_RW.archive : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ scalarEvent.getAttribute_complete_name()
					+ " values (Exception)...");
			exE.printStackTrace();
			Object value = null;
			scalarEvent.setValue(value);
			processEventScalar(scalarEvent, tryNumber);
		}

	}
	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events End
	// --------------------------------------------------------------------------//
}
