//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/scalar/BooleanScalar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BooleanScalar.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.23 $
//
// $Log: BooleanScalar.java,v $
// Revision 1.23  2007/06/14 07:09:36  pierrejoseph
// Exception addition in errorChange + retrieve the data_type of a Number Event
//
// Revision 1.22  2007/06/11 12:16:33  pierrejoseph
// doArchive method has been added in the TdbCollector class with new catched exceptions that can be raised by the isDataArchivable method.
//
// Revision 1.21  2007/06/01 09:47:40  pierrejoseph
// Mantis 5232 : in the errorChange method with the scalarevent was created the data_type was missing so in case of error nothing was stored in the BD
//
// Revision 1.20  2007/05/25 12:03:36  pierrejoseph
// Pb mode counter on various mode in the same collector : one ModesCounters object by attribut stored in a hashtable of the ArchiverCollector object (in common part)
//
// Revision 1.19  2007/04/24 14:29:28  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.18  2007/04/03 15:40:26  ounsy
// removed logs
//
// Revision 1.17  2007/03/27 15:17:43  ounsy
// corrected the processEventScalar method to use the isFirstValue boolean
//
// Revision 1.16  2007/03/20 15:03:15  ounsy
// trying alternate version of processEventScalar
//
// Revision 1.15  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.14  2007/02/13 14:41:45  ounsy
// added diary entry in the case of unexpected exceptions in addSource
//
// Revision 1.13  2007/02/13 14:19:16  ounsy
// corrected a bug in addSource: an infinite nnumber of FileTools instances could potentially be created
//
// Revision 1.12  2006/10/19 12:26:01  ounsy
// modfiied the removeSource to take into account the new isAsuynchronous parameter
//
// Revision 1.11  2006/08/23 09:55:15  ounsy
// FileTools compatible with the new TDB file management
// + keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)
//
// Revision 1.10  2006/07/27 12:42:19  ounsy
// modified processEventScalar so that it calls setLastValue even if the current value doesn't have to be archived
//
// Revision 1.9  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.8  2006/07/21 14:40:50  ounsy
// modified processEventScalar: moved the time condition test to imitate Hdb
//
// Revision 1.7  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.6  2006/05/23 11:58:03  ounsy
// now checks the timeCondition condition before calling FileTools.processEvent
//
// Revision 1.5  2006/05/16 09:29:53  ounsy
// added what's necessary for the old files deletion mechanism
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

import java.util.HashMap;
import java.util.Map;

import Common.Archiver.Collector.ModesCounters;
import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.BooleanScalarEvent;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IBooleanScalar;
import fr.esrf.tangoatk.core.IBooleanScalarListener;
import fr.esrf.tangoatk.core.IEntity;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

@SuppressWarnings("serial")
public class BooleanScalar extends TdbCollector implements IBooleanScalarListener {

    /**
     * This Hashtable is used to store the last value of an attribute. This
     * value will be compared to the current one (cf. Modes)
     */
    protected Map<String, Object> lastValueHashtable = new HashMap<String, Object>();

    public BooleanScalar(final TdbModeHandler modeHandler, final String currentDsPath,
	    final String currentDbPath, final AttrWriteType writableType) {
	super(modeHandler, currentDsPath, currentDbPath, writableType);
    }

    @Override
    public void addListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IBooleanScalar) {
	    ((IBooleanScalar) attribute).addBooleanScalarListener(this);
	    ((IBooleanScalar) attribute).addErrorListener(this);
	}
    }

    @Override
    public void removeListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IBooleanScalar) {
	    ((IBooleanScalar) attribute).removeBooleanScalarListener(this);
	    ((IBooleanScalar) attribute).removeErrorListener(this);
	}
    }

    @Override
    public void booleanScalarChange(final BooleanScalarEvent event) {
	if (event != null && event.getSource() != null) {
	    final ScalarEvent scalarEvent = new ScalarEvent();
	    try {
		scalarEvent.setAttribute_complete_name(((IBooleanScalar) event.getSource())
			.getName());
		scalarEvent.setData_format(AttrDataFormat._SCALAR);
		scalarEvent.setData_type(((IBooleanScalar) event.getSource()).getAttribute()
			.getType());
		scalarEvent.setWritable(getWritableValue().value());
		scalarEvent.setTimeStamp(event.getTimeStamp());
		if (getWritableValue().equals(AttrWriteType.WRITE)) {
		    scalarEvent.setValue(new Boolean(((IBooleanScalar) event.getSource())
			    .getSetPoint()));
		} else if (getWritableValue().equals(AttrWriteType.READ_WRITE)) {
		    final Boolean[] bvalue = new Boolean[2];
		    bvalue[0] = ((IBooleanScalar) event.getSource()).getValue();
		    bvalue[1] = ((IBooleanScalar) event.getSource()).getSetPoint();
		    scalarEvent.setValue(bvalue);
		} else {
		    scalarEvent.setValue(new Boolean(((IBooleanScalar) event.getSource())
			    .getValue()));
		}

		processEventScalar(scalarEvent, DEFAULT_TRY_NUMBER);
	    } catch (final DevFailed devFailed) {
		printException(GlobalConst.DATA_TYPE_EXCEPTION, AttrDataFormat._SCALAR, scalarEvent
			.getAttribute_complete_name(), devFailed);
	    }
	} else {
	    final String message = "event is null or empty: " + event;
	    System.err.println(message);
	    m_logger.trace(ILogger.LEVEL_ERROR, message);
	}
    }

    private void processEventScalar(final ScalarEvent scalarEvent, int try_number) {
	final boolean timeCondition = super.isDataArchivableTimestampWise(scalarEvent);
	if (!timeCondition) {
	    return;
	}

	try {
	    boolean doArchive = false;
	    if (isFirstValue) {
		doArchive = true;
		isFirstValue = false;
	    } else {
		final ModesCounters mc = getModeCounter(scalarEvent.getAttribute_complete_name());
		if (mc == null) {
		    m_logger.trace(ILogger.LEVEL_ERROR, "Attribute Counters unknown");
		} else {
		    doArchive = doArchiveEvent(mc, scalarEvent.getData_type(), scalarEvent
			    .getReadValue(), getLastValue(scalarEvent), scalarEvent
			    .getAttribute_complete_name());
		}
	    }

	    if (doArchive) {
		filesNames.get(scalarEvent.getAttribute_complete_name()).processEventScalar(
			scalarEvent);
	    }

	    setLastValue(scalarEvent, scalarEvent.getReadValue());
	} catch (final Exception e) {
	    try_number--;
	    if (try_number > 0) {
		Util.out2.println("BooleanScalar.processEventScalar : \r\n\ttry " + try_number
			+ "failed...");
		processEventScalar(scalarEvent, try_number);
	    }
	}
    }

    protected void setLastValue(final ScalarEvent scalarEvent, final Object lastValue) {
	super.setLastTimestamp(scalarEvent);
	// if (lastValue != null) {
	lastValueHashtable.put(scalarEvent.getAttribute_complete_name(), lastValue);
	// }
    }

    protected Object getLastValue(final ScalarEvent scalarEvent) {
	return lastValueHashtable.get(scalarEvent.getAttribute_complete_name());
    }

    protected void printException(final String cause, final int cause_value, final String name,
	    final DevFailed devFailed) {
	final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
	final String reason = "Failed while executing BooleanScalar.booleanScalarChange()...";
	final String desc = cause_value != -1 ? cause + " (" + cause_value + ") not supported !! ["
		+ name + "]" : "Unable to retrieve the attribute data type";
	Util.out2.println(devFailed == null ? new ArchivingException(message, reason,
		ErrSeverity.PANIC, desc, "").toString() : new ArchivingException(message, reason,
		ErrSeverity.PANIC, desc, "", devFailed).toString());
    }

    @Override
    public void errorChange(final ErrorEvent errorEvent) {
	final int tryNumber = DEFAULT_TRY_NUMBER;

	final String errorMess = this.getClass().getSimpleName()
		+ ".errorChange : Unable to read the attribute named "
		+ errorEvent.getSource().toString();
	Util.out3.println(errorMess);
	m_logger.trace(ILogger.LEVEL_ERROR, errorMess);

	try {
	    processEventScalar(getNullValueScalarEvent(errorEvent, TangoConst.Tango_DEV_BOOLEAN,
		    getWritableValue().value()), tryNumber);
	} catch (final Exception e) {
	    super.m_logger.trace(ILogger.LEVEL_ERROR, this.getClass().getSimpleName()
		    + ".errorChange : during processEventScalar creation execp : " + e);
	    e.printStackTrace();
	}
    }

    @Override
    public void stateChange(final AttributeStateEvent e) {
	// TODO Auto-generated method stub

    }

}
