//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/scalar/BooleanScalar_WO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BooleanScalar_WO.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.7 $
//
// $Log: BooleanScalar_WO.java,v $
// Revision 1.7  2007/06/01 09:47:40  pierrejoseph
// Mantis 5232 : in the errorChange method with the scalarevent was created the data_type was missing so in case of error nothing was stored in the BD
//
// Revision 1.6  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.5  2006/05/03 14:29:30  ounsy
// compatible with the AttributePolledList in "dispatch" mode
//
// Revision 1.4  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.2  2005/09/26 08:01:54  chinkumo
// Minor changes !
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
import fr.esrf.tangoatk.core.BooleanScalarEvent;
import fr.esrf.tangoatk.core.IBooleanScalar;
import fr.esrf.tangoatk.core.IBooleanScalarListener;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class BooleanScalar_WO extends BooleanScalar implements
		IBooleanScalarListener {
	public BooleanScalar_WO(TdbModeHandler modeHandler) {
		super(modeHandler);
	}

	protected int getWritableValue() {
		return AttrWriteType._WRITE;
	}

	public void booleanScalarChange(BooleanScalarEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		ScalarEvent scalarEvent = new ScalarEvent();
		scalarEvent.setAttribute_complete_name(((IBooleanScalar) event
				.getSource()).getName());

		try {
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._WRITE);
			scalarEvent.setData_type(((IBooleanScalar) event.getSource())
					.getAttribute().getType());

			scalarEvent.setTimeStamp(event.getTimeStamp());

			scalarEvent.setValue(new Boolean(((IBooleanScalar) event
					.getSource()).getSetPoint()));

			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			printException(GlobalConst.DATA_TYPE_EXCEPTION,
					AttrDataFormat._SCALAR, scalarEvent
							.getAttribute_complete_name(), devFailed);
		}
	}
}
