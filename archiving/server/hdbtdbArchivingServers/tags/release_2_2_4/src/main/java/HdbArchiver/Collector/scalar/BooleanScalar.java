//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/BooleanScalar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BooleanScalar.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.22 $
//
// $Log: BooleanScalar.java,v $
// Revision 1.22  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.21  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.20  2007/09/25 14:59:17  pierrejoseph
// 5251 : sometimes the device stayed in Running state sue to a blocking while in  the addAttribute method.
//
// Revision 1.19  2007/06/14 07:09:37  pierrejoseph
// Exception addition in errorChange + retrieve the data_type of a Number Event
//
// Revision 1.18  2007/06/13 13:13:23  pierrejoseph
// the mode counter are managed attribute by attribute in each collector
//
// Revision 1.17  2007/06/11 12:23:27  pierrejoseph
// These abstracts classes managed the errorChange method +
// call the doArchiveEvent method from the mother class to avoid dispatched the various catch exceptions at all collectors levels
//
// Revision 1.16  2007/04/24 14:29:27  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.15  2007/04/03 15:40:25  ounsy
// removed logs
//
// Revision 1.14  2007/03/27 15:17:43  ounsy
// corrected the processEventScalar method to use the isFirstValue boolean
//
// Revision 1.13  2007/03/20 10:47:38  ounsy
// trying alternate version of processEventScalar
//
// Revision 1.12  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.11  2006/07/27 12:42:19  ounsy
// modified processEventScalar so that it calls setLastValue even if the current value doesn't have to be archived
//
// Revision 1.10  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.9  2006/07/18 08:04:03  ounsy
// moved the setAttribute_complete_name call in the try block
//
// Revision 1.8  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.7  2006/06/13 13:28:20  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.6  2006/05/23 11:57:17  ounsy
// now checks the timeCondition condition before calling DbProxy.store
//
// Revision 1.5  2006/01/27 13:06:40  ounsy
// organised imports
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
import fr.esrf.tangoatk.core.IBooleanScalar;
import fr.esrf.tangoatk.core.IBooleanScalarListener;
import fr.esrf.tangoatk.core.attribute.AttributeFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public abstract class BooleanScalar extends HdbCollector implements
		IBooleanScalarListener, ITangoArchiveListener {
	/**
	 *
	 */
	private static final long serialVersionUID = -6610853472664968184L;

	/**
	 * This Hashtable is used to store the last value of an attribute. This
	 * value will be compared to the current one (cf. Modes)
	 */
	protected Hashtable<String, Object> lastValueHashtable;

	private HashMap<Device, TangoEventsAdapter> evtAdaptHMap;

	public BooleanScalar(HdbModeHandler hdbModeHandler) {
		super(hdbModeHandler);

		lastValueHashtable = new Hashtable<String, Object>();

		evtAdaptHMap = new HashMap<Device, TangoEventsAdapter>();
	}

	/*synchronized*/ public void addSource(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		if (HdbArchiver.isUseEvents) {
			addSourceForEvents(attributeLightMode);
		} else {
			addSourceForPolling(attributeLightMode);
		}
	}

	/*synchronized*/ public void removeSource(String attributeName)
			throws ArchivingException {
		Util.out2.println("BooleanScalar.removeSource");

		if (HdbArchiver.isUseEvents) {
			removeSourceForEvents(attributeName);
		}

		try {
			synchronized (attributeList) {
				/*
				 * while ( ( IBooleanScalar ) attributeList.get(attributeName)
				 * != null ) {
				 */
				IBooleanScalar attribute = (IBooleanScalar) attributeList
						.get(attributeName);
				if (attribute != null) {
					attribute.removeBooleanScalarListener(this);
					attribute.removeErrorListener(this);
					attributeList.remove(attributeName);

					// informs the mother class that one new attribute must be
					// removed
					removeAttribute(attributeName);

					Util.out4.println("\t The attribute named " + attributeName
							+ " was fired from the Collector list...");
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
			synchronized (attributeList) {
				String att_name = attributeLightMode
						.getAttribute_complete_name();

				// while ( ( IBooleanScalar ) attributeList.get(att_name) ==
				// null )
				// {
				IBooleanScalar attribute = null;

				attribute = (IBooleanScalar) attributeList.add(att_name);

				attribute.addBooleanScalarListener(this);
				attribute.addErrorListener(this);
				Util.out4.println("\t The attribute named " + att_name
						+ " was hired to the Collector list...");

				// informs the mother class that one new attribute must be
				// managed
				addAttribute(att_name);

				if (attributeList.size() == 1) {
					startCollecting();
				}
				// }
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
		IBooleanScalar attribute = null;

		try {
			/*
			 * Get the attribute from the AttributeFactory, so that it is not
			 * added to the attribute polled list. Remember that `attributeList'
			 * is an AttributePolledList().
			 */
			attribute = (IBooleanScalar) AttributeFactory.getInstance()
					.getAttribute(
							attributeLightMode.getAttribute_complete_name());
			if (attribute == null) {
				System.out
						.println("\033[1;31mBooleanScalar.java: the attribute \""
								+ attributeLightMode
										.getAttribute_complete_name()
								+ " is null (BooleanScalar.java): not adding it!");
				return;
			}
		} catch (DevFailed e) {
			System.out
					.println("\033[1;31mBooleanScalar.java: Failed to get the attribute from the AttributeFactory\033[0m");
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
			String desc = "Failed while executing NumberSpectrum_RW.addSource() method...\nBooleanScalar.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		} /*
		 * end of first try/catch block, to retrieve the attribute from the
		 * AttributeFactory
		 */

		System.out.print("boolean attribute \"" + attribute.getNameSansDevice()
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
		Util.out2.println("NumberSpectrum_RW.removeSource");
		System.out.print("boolean: removing source for \"" + attributeName
				+ "\"...\t");
		TangoEventsAdapter adapter;
		IBooleanScalar attribute;

		try {
			synchronized (evtAdaptHMap) {
				attribute = (IBooleanScalar) AttributeFactory.getInstance()
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
	}

	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events
	// --------------------------------------------------------------------------//

	public void stateChange(AttributeStateEvent event) {
	}

	public void processEventScalar(ScalarEvent scalarEvent, int try_number) {
		// System.out.println(this);
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
			String message = "Problem storing BooleanScalar value";
			super.m_logger.trace(ILogger.LEVEL_ERROR, message);
			super.m_logger.trace(ILogger.LEVEL_ERROR, e);

			try_number--;
			if (try_number > 0) {
				Util.out2
						.println("BooleanScalar.processEventScalar : \r\n\ttry "
								+ (try_number) + "failed...");
				processEventScalar(scalarEvent, try_number);
			}
		}
		//checkGC();
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
		String reason = "Failed while executing BooleanScalar.booleanScalarChange()...";
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
		m_logger.trace(ILogger.LEVEL_ERROR, errorMess);

		try {
			processEventScalar(getNullValueScalarEvent(errorEvent,
					TangoConst.Tango_DEV_BOOLEAN, getWritableValue()),
					tryNumber);
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
