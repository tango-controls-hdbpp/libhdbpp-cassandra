/*	Synchrotron Soleil
 *
 *   File          :  StringScalar_RW.java
 *
 *   Project       :  TdbArchiver_CVS
 *
 *   Description   :
 *
 *   Author        :  SOLEIL
 *
 *   Original      :  28 févr. 2006
 *
 *   Revision:  					Author:
 *   Date: 							State:
 *
 *   Log: StringScalar_RW.java,v
 *
 */
package TdbArchiver.Collector.scalar;

import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.tangoatk.core.IStringScalar;
import fr.esrf.tangoatk.core.StringScalarEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public class StringScalar_RW extends StringScalar {

	/**
	 *
	 */
	private static final long serialVersionUID = 1089032138466606244L;

	public StringScalar_RW(TdbModeHandler _modeHandler, String currentDsPath,
			String currentDbPath) {
		super(_modeHandler, currentDsPath, currentDbPath);
	}

	protected int getWritableValue() {
		return AttrWriteType._READ_WRITE;
	}

	public void stringScalarChange(StringScalarEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		ScalarEvent scalarEvent = new ScalarEvent();
		scalarEvent.setAttribute_complete_name(((IStringScalar) event
				.getSource()).getName());
		String[] svalue;

		try {
			scalarEvent.setData_format(AttrDataFormat._SCALAR);
			scalarEvent.setWritable(AttrWriteType._READ_WRITE);
			scalarEvent.setData_type(((IStringScalar) event.getSource())
					.getAttribute().getType());

			scalarEvent.setTimeStamp(event.getTimeStamp());

			svalue = new String[2];
			svalue[0] = ((IStringScalar) (event.getSource())).getStringValue();
			svalue[1] = ((IStringScalar) (event.getSource()))
					.getStringSetPoint();
			scalarEvent.setValue(svalue);
			processEventScalar(scalarEvent, tryNumber);

		} catch (DevFailed devFailed) {
			printException(GlobalConst.DATA_TYPE_EXCEPTION,
					AttrDataFormat._SCALAR, scalarEvent
							.getAttribute_complete_name(), devFailed);
		}

	}

}
