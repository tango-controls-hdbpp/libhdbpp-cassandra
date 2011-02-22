//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/StringScalar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  StringScalar.
//						(Chinkumo Jean) - Feb. 28, 2006
//
// $Author: pierrejoseph $
//
// $Revision: 1.16 $
//
// $Log: StringScalar.java,v $
// Revision 1.16  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.15  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package HdbArchiver.Collector.scalar;

import java.util.HashMap;
import java.util.Hashtable;

import Common.Archiver.Collector.ModesCounters;
import HdbArchiver.HdbArchiver;
import HdbArchiver.Collector.HdbCollector;
import HdbArchiver.Collector.HdbModeHandler;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.events.ITangoArchiveListener;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.Device;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IStringScalar;
import fr.esrf.tangoatk.core.IStringScalarListener;
import fr.esrf.tangoatk.core.attribute.AttributeFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

//--------------------------------------------------------------------------//
//ELETTRA : Archiving Events 
//--------------------------------------------------------------------------//
public abstract class StringScalar extends HdbCollector implements
		IStringScalarListener, ITangoArchiveListener {
	/**
	 * This Hashtable is used to store the last value of an attribute. This
	 * value will be compared to the current one (cf. Modes)
	 */
	protected Hashtable<String, Object> lastValueHashtable;

	private HashMap<Device, TangoEventsAdapter> evtAdaptHMap;

	public StringScalar(HdbModeHandler _modeHandler) {
		super(_modeHandler);

		lastValueHashtable = new Hashtable<String, Object>();

		evtAdaptHMap = new HashMap<Device, TangoEventsAdapter>();
	}

	synchronized public void addSource(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		if (HdbArchiver.isUseEvents) {
			addSourceForEvents(attributeLightMode);
		} else {
			addSourceForPolling(attributeLightMode);
		}
	}

	synchronized public void removeSource(String attributeName)
			throws ArchivingException {
		Util.out2.println("StringScalar.removeSource");

		if (HdbArchiver.isUseEvents) {
			removeSourceForEvents(attributeName);
		}
		try {
			synchronized (attributeList) {
				/*
				 * while ( ( IStringScalar ) attributeList.get(attributeName) !=
				 * null ) {
				 */
				IStringScalar attribute = (IStringScalar) attributeList
						.get(attributeName);
				if (attribute != null) {
					attribute.removeStringScalarListener(this);
					attribute.removeErrorListener(this);
					attributeList.remove(attributeName);
					Util.out4.println("\t The attribute named " + attributeName
							+ " was fired from the Collector list...");
					// informs the mother class that one new attribute must be
					// removed
					removeAttribute(attributeName);

					if (attributeList.isEmpty()) {
						stopCollecting();
					}
				}
			}
		} catch (Exception e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed removing '" + attributeName + "' from sources";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing "
					+ this.getClass().getSimpleName()
					+ ".removeSource() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		}
	}

	private void addSourceForPolling(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		try {
			String att_name = attributeLightMode.getAttribute_complete_name();

			synchronized (attributeList) {
				while ((IStringScalar) attributeList.get(att_name) == null) {
					IStringScalar attribute = null;
					attribute = (IStringScalar) attributeList.add(att_name);

					attribute.addStringScalarListener(this);
					attribute.addErrorListener(this);
					Util.out4.println("\t The attribute named " + att_name
							+ " was hired to the Collector list...");
					// informs the mother class that one new attribute must be
					// managed
					addAttribute(att_name);

					if (attributeList.size() == 1) {
						startCollecting();
					}
				}
			}
		} catch (ConnectionException e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing "
					+ this.getClass().getSimpleName()
					+ ".addSource() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);

		} catch (Exception e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing "
					+ this.getClass().getSimpleName()
					+ ".addSource() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		}
	}

	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events
	// --------------------------------------------------------------------------//
	private void addSourceForEvents(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		/* Adapter for the Tango archive events */
		TangoEventsAdapter evtAdapt = null;
		IStringScalar attribute = null;

		try {
			/*
			 * Get the attribute from the AttributeFactory, so that it is not
			 * added to the attribute polled list. Remember that `attributeList'
			 * is an AttributePolledList().
			 */
			attribute = (IStringScalar) AttributeFactory.getInstance()
					.getAttribute(
							attributeLightMode.getAttribute_complete_name());
			if (attribute == null) {
				System.out
						.println("\033[1;31mStringScalar.java: the attribute \""
								+ attributeLightMode
										.getAttribute_complete_name()
								+ " is null (StringScalar.java): not adding it!");
				return;
			}
		} catch (DevFailed e) {
			System.out
					.println("\033[1;31mStringScalar.java: Failed to get the attribute from the AttributeFactory\033[0m");
			for (int err = 0; err < e.errors.length; err++)
				System.out.println("error: " + e.errors[err].desc);
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = "DevFailed exception while calling AttributeFactory.getInstance().getAttribute();";
			String desc = "Failed while executing Spectrum_RW.addSource() method...\nSpectrum_RW.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		} catch (ConnectionException e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing StringScalar.addSource() method...\nStateScalar.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		} /*
		 * end of first try/catch block, to retrieve the attribute from the
		 * AttributeFactory
		 */

		System.out.print("scalar attribute \"" + attribute.getNameSansDevice()
				+ "\"...\t");
		System.out.flush();

		/*
		 * If an attribute (name + device server) is already in the map, it
		 * means it has already been registered for the Tango Archive events. So
		 * it does not have to be added to the archive events once again, nor to
		 * the attributePolledList: we can return. The same goes for an
		 * attribute already present in the attributePolledList `attributeList'.
		 */
		/*
		 * if(HdbArchiver.useEvents.equalsIgnoreCase("no")) {
		 * System.out.print("[\033[1;35mEVENTS DISABLED\033[0m]. ");
		 * registerForScalarListener(attributeLightMode); } else {
		 */
		try {
			synchronized (evtAdaptHMap) {
				evtAdapt = evtAdaptHMap.get(attribute.getDevice());
				if (evtAdapt == null) {
					evtAdapt = new TangoEventsAdapter(attribute.getDevice());
					evtAdaptHMap.put(attribute.getDevice(), evtAdapt);
				} else
					System.out
							.println("\nThe adapter for the attribute is already configured");
			}
			String[] filter = new String[0];

			/*
			 * Try to register for the archive events: evtAdapt is the new
			 * adapter or the one already found in the map.
			 */
			evtAdapt.addTangoArchiveListener(this, attribute
					.getNameSansDevice(), filter);
			archive_listeners_counter++;
			System.out.println("[\033[1;32mEVENTS\033[0m].");
		} catch (DevFailed e) {
			/*
			 * If archive events are not enabled for the attribute, we will poll
			 * it
			 */
			for (int err = 0; err < e.errors.length; err++) {
				// // System.out.println("error registering: " +
				// e.errors[err].desc);
				if (e.errors[err].desc.contains("Already connected to event")) {
					System.out
							.println("Already connected to events for attribute");
					return;
				}
			}
			/* Register for polling */
			addSourceForPolling(attributeLightMode);
			/* unlock the attributeList */
		}
		// } /* function finishes */
	}

	private void removeSourceForEvents(String attributeName) {
		Util.out2.println("StringScalar.removeSource");
		System.out.print("state: removing source for \"" + attributeName
				+ "\"...\t");
		TangoEventsAdapter adapter;
		IStringScalar attribute;
		/*
		 * If useEvents is enabled, we should remove the eventListener, if not
		 * we can skip this piece of code
		 */
		// if (HdbArchiver.useEvents.equalsIgnoreCase("yes"))
		// {
		try {
			synchronized (evtAdaptHMap) {
				attribute = (IStringScalar) AttributeFactory.getInstance()
						.getAttribute(attributeName);
				/*
				 * Retrieve in the hash table the event adapter associated to
				 * the attributeName.
				 */
				if (attribute == null)
					System.out.println("\033[1;31mThe attribute "
							+ attributeName + " is null\033[0m");
				adapter = evtAdaptHMap.get(attribute.getDevice());

				if (adapter != null) {
					adapter.removeTangoArchiveListener(this, attribute
							.getNameSansDevice());
					archive_listeners_counter--;
					System.out.println("[\033[1;32mEVENTS\033[0m].");
					Util.out2.println(" (adapter: " + adapter.device_name()
							+ ")");
					/* Should be ok now to return */
					return;
				}
			} /* unlock event adapters map */
		} catch (ConnectionException e) /* getAttribute() failed */
		{
			Util.out2
					.println("\033[1;31mConnection exception while retrieving the attribute from the AttributeFactory!\033[0m");
		} catch (DevFailed f) {
			Util.out2.println("Error removing tangoArchiveListener");
			for (int err = 0; err < f.errors.length; err++)
				Util.out2.println("error: " + f.errors[err].desc);
		}
		// }
	}

	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events
	// --------------------------------------------------------------------------//

	public void stateChange(AttributeStateEvent e) {
	}

	public void processEventScalar(ScalarEvent scalarEvent, int try_number) {
		boolean timeCondition = super
				.isDataArchivableTimestampWise(scalarEvent);
		if (!timeCondition) {
			return;
		}

		try {
			boolean doArchive = false;
			if (this.isFirstValue) {
				doArchive = true;
				this.isFirstValue = false;
			} else {

				ModesCounters mc = getModeCounter(scalarEvent
						.getAttribute_complete_name());
				if (mc == null)
					this.m_logger.trace(ILogger.LEVEL_ERROR,
							"Attribute Counters unknown");
				else
					doArchive = doArchiveEvent(mc, scalarEvent.getData_type(),
							scalarEvent.getReadValue(),
							getLastValue(scalarEvent), scalarEvent
									.getAttribute_complete_name());
			}

			if (doArchive) {
				super.dbProxy.store(scalarEvent);
			}

			setLastValue(scalarEvent, scalarEvent.getReadValue());
		} catch (Exception e) {
			String message = "Problem storing StringScalar value";
			super.m_logger.trace(ILogger.LEVEL_ERROR, message);
			super.m_logger.trace(ILogger.LEVEL_ERROR, e);

			try_number--;
			if (try_number > 0) {
				Util.out2
						.println("NumberScalar.processEventScalar : \r\n\ttry "
								+ (try_number) + "failed...");
				processEventScalar(scalarEvent, try_number);
			}
		}
		checkGC();
	}

	protected void setLastValue(ScalarEvent scalarEvent, Object lastValue) {
		super.setLastTimestamp(scalarEvent);
		if (lastValue != null) {
			lastValueHashtable.put(scalarEvent.getAttribute_complete_name(),
					lastValue);
		}
	}

	protected Object getLastValue(ScalarEvent scalarEvent) {
		return lastValueHashtable.get(scalarEvent.getAttribute_complete_name());
	}

	protected void printException(String cause, int cause_value, String name,
			DevFailed devFailed) {
		String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
		String reason = "Failed while executing StringScalar.stringScalarChange()...";
		String desc = (cause_value != -1) ? cause + " (" + cause_value
				+ ") not supported !! [" + name + "]"
				: "Unable to retrieve the attribute data type";
		Util.out2.println((devFailed == null) ? (new ArchivingException(
				message, reason, ErrSeverity.PANIC, desc, "").toString())
				: (new ArchivingException(message, reason, ErrSeverity.PANIC,
						desc, "", devFailed)).toString());
	}

	protected abstract int getWritableValue();

	public void errorChange(ErrorEvent errorEvent) {
		int tryNumber = DEFAULT_TRY_NUMBER;

		String errorMess = this.getClass().getSimpleName()
				+ ".errorChange : Unable to read the attribute named "
				+ errorEvent.getSource().toString();
		Util.out3.println(errorMess);
		super.m_logger.trace(ILogger.LEVEL_ERROR, errorMess);

		try {
			processEventScalar(getNullValueScalarEvent(errorEvent,
					TangoConst.Tango_DEV_STRING, getWritableValue()), tryNumber);
		} catch (Exception e) {
			super.m_logger
					.trace(
							ILogger.LEVEL_ERROR,
							this.getClass().getSimpleName()
									+ ".errorChange : during processEventScalar creation execp : "
									+ e);
			e.printStackTrace();
		}
	}
}
