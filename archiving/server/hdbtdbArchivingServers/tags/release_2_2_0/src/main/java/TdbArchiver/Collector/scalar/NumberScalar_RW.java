//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/scalar/NumberScalar_RW.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar_RW.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.8 $
//
// $Log: NumberScalar_RW.java,v $
// Revision 1.8  2007/06/01 09:47:40  pierrejoseph
// Mantis 5232 : in the errorChange method with the scalarevent was created the data_type was missing so in case of error nothing was stored in the BD
//
// Revision 1.7  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
//
// Revision 1.6  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.5  2006/05/03 14:29:30  ounsy
// compatible with the AttributePolledList in "dispatch" mode
//
// Revision 1.4  2006/03/13 14:54:12  ounsy
// Long as an int management
//
// Revision 1.3  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.2  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.1  2005/09/09 10:04:03  chinkumo
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

package TdbArchiver.Collector.scalar;

import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.core.INumberScalarListener;
import fr.esrf.tangoatk.core.NumberScalarEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class NumberScalar_RW extends NumberScalar implements
		INumberScalarListener {
	public NumberScalar_RW(TdbModeHandler _modeHandler, String currentDsPath,
			String currentDbPath) {
		super(_modeHandler, currentDsPath, currentDbPath);
	}

	protected int getWritableValue() {
		return AttrWriteType._READ_WRITE;
	}

	public void numberScalarChange(NumberScalarEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		ScalarEvent scalarEvent = new ScalarEvent();
		scalarEvent.setAttribute_complete_name(event.getNumberSource()
				.getName());
		Double[] dvalue;
		Float[] fvalue;
		Integer[] ivalue;
		Short[] svalue;
		try {
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ_WRITE);
			scalarEvent.setData_type(event.getNumberSource().getAttribute()
					.getType());

			scalarEvent.setTimeStamp(event.getTimeStamp());

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
			printException(GlobalConst.DATA_TYPE_EXCEPTION,
					AttrDataFormat._SCALAR, scalarEvent
							.getAttribute_complete_name(), devFailed);
		}

	}
}
