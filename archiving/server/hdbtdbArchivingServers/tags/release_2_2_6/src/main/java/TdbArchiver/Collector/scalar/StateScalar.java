/*	Synchrotron Soleil
 *
 *   File          :  StateScalar.java
 *
 *   Project       :  TdbArchiver_CVS
 *
 *   Description   :
 *
 *   Author        :  SOLEIL
 *
 *   Original      :  10 mars 2006
 *
 *   Revision:  					Author:
 *   Date: 							State:
 *
 *   Log: StateScalar.java,v
 *
 */
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
import fr.esrf.tangoatk.core.DevStateScalarEvent;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IDevStateScalar;
import fr.esrf.tangoatk.core.IDevStateScalarListener;
import fr.esrf.tangoatk.core.IEntity;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoStateTranslation;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

@SuppressWarnings("serial")
public class StateScalar extends TdbCollector implements IDevStateScalarListener {

    /**
     * This Hashtable is used to store the last value of an attribute. This
     * value will be compared to the current one (cf. Modes)
     */
    protected Map<String, Object> lastValueHashtable = new HashMap<String, Object>();

    public StateScalar(final TdbModeHandler _modeHandler, final String currentDsPath,
	    final String currentDbPath, final AttrWriteType writableType) {
	super(_modeHandler, currentDsPath, currentDbPath, writableType);
    }

    public void processEventScalar(final ScalarEvent scalarEvent, int try_number) {
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
		Util.out2.println("StateScalar.processEventScalar : \r\n\ttry " + try_number
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
	final String reason = "Failed while executing StateScalar.devStateScalarChange()...";
	final String desc = cause_value != -1 ? cause + " (" + cause_value + ") not supported !! ["
		+ name + "]" : "Unable to retrieve the attribute data type";
	Util.out2.println(devFailed == null ? new ArchivingException(message, reason,
		ErrSeverity.PANIC, desc, "").toString() : new ArchivingException(message, reason,
		ErrSeverity.PANIC, desc, "", devFailed).toString());
    }

    @Override
    public void addListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IDevStateScalar) {
	    ((IDevStateScalar) attribute).addDevStateScalarListener(this);
	    ((IDevStateScalar) attribute).addErrorListener(this);
	}
    }

    @Override
    public void removeListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IDevStateScalar) {
	    ((IDevStateScalar) attribute).removeDevStateScalarListener(this);
	    ((IDevStateScalar) attribute).removeErrorListener(this);
	}
    }

    public void stateChange(final AttributeStateEvent e) {
    }

    public void devStateScalarChange(final DevStateScalarEvent event) {
	if (event != null && event.getSource() != null) {
	    final ScalarEvent scalarEvent = new ScalarEvent();
	    scalarEvent.setAttribute_complete_name(((IDevStateScalar) event.getSource()).getName());
	    try {
		scalarEvent.setData_format(AttrDataFormat._SCALAR);
		scalarEvent.setWritable(getWritableValue().value());
		scalarEvent.setData_type(((IDevStateScalar) event.getSource()).getAttribute()
			.getType());

		scalarEvent.setTimeStamp(event.getTimeStamp());

		final String value = ((IDevStateScalar) event.getSource()).getValue();
		scalarEvent.setValue(TangoStateTranslation.getTangoDevState(value));
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

    @Override
    public void errorChange(final ErrorEvent errorEvent) {

	final String errorMess = this.getClass().getSimpleName()
		+ ".errorChange : Unable to read the attribute named "
		+ errorEvent.getSource().toString();
	Util.out3.println(errorMess);
	super.m_logger.trace(ILogger.LEVEL_ERROR, errorMess);

	try {
	    processEventScalar(getNullValueScalarEvent(errorEvent, TangoConst.Tango_DEV_STATE,
		    getWritableValue().value()), DEFAULT_TRY_NUMBER);
	} catch (final Exception e) {
	    super.m_logger.trace(ILogger.LEVEL_ERROR, this.getClass().getSimpleName()
		    + ".errorChange : during processEventScalar creation execp : " + e);
	    e.printStackTrace();
	}
    }
}
