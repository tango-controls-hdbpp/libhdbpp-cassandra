//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/scalar/NumberScalar_WO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar_WO.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.7 $
//
// $Log: NumberScalar_WO.java,v $
// Revision 1.7  2007/06/01 09:47:40  pierrejoseph
// Mantis 5232 : in the errorChange method with the scalarevent was created the data_type was missing so in case of error nothing was stored in the BD
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

public class NumberScalar_WO extends NumberScalar implements
		INumberScalarListener {
	public NumberScalar_WO(TdbModeHandler _modeHandler, String currentDsPath,
			String currentDbPath) {
		super(_modeHandler, currentDsPath, currentDbPath);
	}

	protected int getWritableValue() {
		return AttrWriteType._WRITE;
	}

	public void numberScalarChange(NumberScalarEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		ScalarEvent scalarEvent = new ScalarEvent();
		scalarEvent.setAttribute_complete_name(event.getNumberSource()
				.getName());

		try {
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._WRITE);
			scalarEvent.setData_type(event.getNumberSource().getAttribute()
					.getType());

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
				scalarEvent.setValue(new Double(event.getNumberSource()
						.getNumberScalarSetPoint()));
				break;
			case TangoConst.Tango_DEV_FLOAT:
				scalarEvent.setValue(new Float((float) event.getNumberSource()
						.getNumberScalarSetPoint()));
				break;
			default:
				scalarEvent.setValue(new Double(event.getNumberSource()
						.getNumberScalarSetPoint()));
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
