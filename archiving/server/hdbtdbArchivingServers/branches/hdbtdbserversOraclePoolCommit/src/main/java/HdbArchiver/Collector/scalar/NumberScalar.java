//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/scalar/NumberScalar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar.
//						(Chinkumo Jean) - Mar 25, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.30 $
//
// $Log: NumberScalar.java,v $
// Revision 1.30  2007/10/18 15:30:44  pierrejoseph
// avoid a double creation of the evtAdaptHMap
//
// Revision 1.29  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.28  2007/09/28 14:49:23  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.27  2007/09/25 14:59:17  pierrejoseph
// 5251 : sometimes the device stayed in Running state sue to a blocking while in  the addAttribute method.
//
// Revision 1.26  2007/08/27 14:14:50  pierrejoseph
// Traces addition
//
// Revision 1.25  2007/06/14 07:09:37  pierrejoseph
// Exception addition in errorChange + retrieve the data_type of a Number Event
//
// Revision 1.24  2007/06/13 14:26:01  ounsy
// catch exception addition
//
// Revision 1.23  2007/06/13 13:13:23  pierrejoseph
// the mode counter are managed attribute by attribute in each collector
//
// Revision 1.22  2007/06/11 12:23:27  pierrejoseph
// These abstracts classes managed the errorChange method +
// call the doArchiveEvent method from the mother class to avoid dispatched the various catch exceptions at all collectors levels
//
// Revision 1.21  2007/04/24 14:29:27  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.20  2007/04/03 15:40:26  ounsy
// removed logs
//
// Revision 1.19  2007/03/27 15:17:43  ounsy
// corrected the processEventScalar method to use the isFirstValue boolean
//
// Revision 1.18  2007/03/14 09:17:51  ounsy
// added a call to scalarEvent.avoidUnderFlow () in processNumberScalarEvent
//
// Revision 1.17  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.16  2006/07/27 12:42:19  ounsy
// modified processEventScalar so that it calls setLastValue even if the current value doesn't have to be archived
//
// Revision 1.15  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.14  2006/07/21 14:43:08  ounsy
// minor changes
//
// Revision 1.13  2006/07/18 08:04:02  ounsy
// moved the setAttribute_complete_name call in the try block
//
// Revision 1.12  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.11  2006/06/13 13:28:19  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.10  2006/05/23 11:57:17  ounsy
// now checks the timeCondition condition before calling DbProxy.store
//
// Revision 1.9  2006/01/27 13:06:40  ounsy
// organised imports
//
// Revision 1.8  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.7.10.4  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.7.10.3  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.7.10.2  2005/09/26 08:01:20  chinkumo
// Minor changes !
//
// Revision 1.7.10.1  2005/09/09 10:13:26  chinkumo
// Since the collecting politic was simplified and improved this class was modified.
//
// Revision 1.7  2005/06/24 12:06:27  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.6  2005/06/14 10:30:27  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.5.2.1  2005/06/13 14:01:06  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.5  2005/04/08 15:40:39  chinkumo
// Code reformated.
//
// Revision 1.4  2005/03/07 16:26:10  chinkumo
// Minor change (tag renamed)
//
// Revision 1.3  2005/02/04 17:10:12  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:22  chinkumo
// First commit (new architecture).
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
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.Device;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.INumberScalar;
import fr.esrf.tangoatk.core.INumberScalarListener;
import fr.esrf.tangoatk.core.attribute.AAttribute;
import fr.esrf.tangoatk.core.attribute.AttributeFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

//--------------------------------------------------------------------------//
//ELETTRA : Archiving Events
//--------------------------------------------------------------------------//
public abstract class NumberScalar extends HdbCollector implements
		INumberScalarListener, ITangoArchiveListener {
	/**
	 *
	 */
	private static final long serialVersionUID = -2184635906279603102L;

	/**
	 * This Hashtable is used to store the last value of an attribute. This
	 * value will be compared to the current one (cf. Modes)
	 */
	protected Hashtable<String, Object> lastValueHashtable;

	private HashMap<Device, TangoEventsAdapter> evtAdaptHMap;

	public NumberScalar(HdbModeHandler modeHandler) {
		super(modeHandler);
		lastValueHashtable = new Hashtable<String, Object>();
		evtAdaptHMap = new HashMap<Device, TangoEventsAdapter>();
	}

	/* synchronized */public void addSource(
			AttributeLightMode attributeLightMode) throws ArchivingException {
		if (HdbArchiver.isUseEvents) {
			addSourceForEvents(attributeLightMode);
		} else {
			addSourceForPolling(attributeLightMode);
		}
	}

	/* synchronized */public void removeSource(String attributeName)
			throws ArchivingException {
		Util.out2.println("NumberScalar.removeSource");

		this.m_logger.trace(ILogger.LEVEL_INFO, "===> Entering "
				+ this.getClass().getSimpleName() + ".removeSource for "
				+ attributeName);

		if (HdbArchiver.isUseEvents) {
			removeSourceForEvents(attributeName);
		}

		try {
			synchronized (attributeList) {
				/*
				 * while ( ( INumberScalar ) attributeList.get(attributeName) !=
				 * null ) {
				 */
				INumberScalar attribute = (INumberScalar) attributeList
						.get(attributeName);
				if (attribute != null) {
					attribute.removeNumberScalarListener(this);
					attribute.removeErrorListener(this);
					attributeList.remove(attributeName);
					this.m_logger
							.trace(ILogger.LEVEL_INFO,
									"\t The attribute was fired from the Collector list...");
					// informs the mother class that one new attribute must be
					// removed
					removeAttribute(attributeName);

					if (attributeList.isEmpty()) {
						this.m_logger.trace(ILogger.LEVEL_INFO,
								"===> StopCollecting is requested ");
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
		} finally {
			this.m_logger.trace(ILogger.LEVEL_INFO, "===> Exiting "
					+ this.getClass().getSimpleName() + ".removeSource for "
					+ attributeName);
		}
	}

	private void addSourceForPolling(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		try {
			String att_name = attributeLightMode.getAttribute_complete_name();

			synchronized (attributeList) {
				// while ( ( INumberScalar ) attributeList.get(att_name) == null
				// )
				// {
				INumberScalar attribute = null;
				attribute = (INumberScalar) attributeList.add(att_name);

				attribute.addNumberScalarListener(this);
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
					+ "Unknown exception when adding '"
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
		INumberScalar attribute = null;

		try {
			/*
			 * Get the attribute from the AttributeFactory, so that it is not
			 * added to the attribute polled list. Remember that `attributeList'
			 * is an AttributePolledList().
			 */
			attribute = (INumberScalar) AttributeFactory.getInstance()
					.getAttribute(
							attributeLightMode.getAttribute_complete_name());
			if (attribute == null) {
				System.out
						.println("\033[1;31mNumberScalar.java: the attribute \""
								+ attributeLightMode
										.getAttribute_complete_name()
								+ " is null (NumberScalar.java): not adding it!");
				return;
			}
		} catch (DevFailed e) {
			System.out
					.println("\033[1;31mNumberScalar.java: Failed to get the attribute from the AttributeFactory\033[0m");
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
			String desc = "Failed while executing NumberScalar.addSource() method...\nNumberScalar.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		} /*
		 * end of first try/catch block, to retrieve the attribute from the
		 * AttributeFactory
		 */

		System.out.print("\033[1;42mregistering\033[0m the scalar attribute \""
				+ attribute.getNameSansDevice() + "\"...\t");
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
			System.out.println("[\033[1;42mEVENTS\033[0m].");
		} catch (DevFailed e) {
			/*
			 * If archive events are not enabled for the attribute, we will poll
			 * it
			 */
			for (int err = 0; err < e.errors.length; err++) {

				if (e.errors[err].desc.contains("Already connected to event")) {
					System.out
							.println("Already connected to events for attribute");
					return;
				} else if (e.errors[err].desc
						.contains("The polling (necessary to send events) for the attribute")) {
					System.out.print(" {\033[1;35mpoller not started\033[0m} ");
					break;
				} else if (e.errors[err].desc
						.contains("Archive event properties")) {
					System.out
							.print(" {\033[1;35marchive event properties not set\033[0m} ");
					break;
				} else
					System.out.println("error registering: "
							+ e.errors[err].desc);

			}
			/* Register for polling */
			addSourceForPolling(attributeLightMode);
			/* unlock the attributeList */
		}
		// } /* function finishes */
	}

	private void removeSourceForEvents(String attributeName) {
		System.out.print("number: \033[1;42mremoving\033[0m source for \""
				+ attributeName + "\"...\t");
		TangoEventsAdapter adapter;
		INumberScalar attribute;
		/*
		 * If useEvents is enabled, we should remove the eventListener, if not
		 * we can skip this piece of code
		 */
		try {
			synchronized (evtAdaptHMap) {
				attribute = (INumberScalar) AttributeFactory.getInstance()
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
					System.out.println("[\033[1;42mEVENTS\033[0m].");
					Util.out2.println(" (adapter: " + adapter.device_name()
							+ ")");
					/* Should be ok now to return */
					// return;
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
		boolean timeCondition = super
				.isDataArchivableTimestampWise(scalarEvent);
		// System.out.println (
		// "processEventScalar/getAttribute_complete_name/"+scalarEvent.getAttribute_complete_name()+"/timeCondition/"+timeCondition
		// );
		if (!timeCondition) {
			// System.out.println ( "processEventScalar/EXITING" );
			return;
		}

		try {
			scalarEvent.avoidUnderFlow();

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
				setLastValue(scalarEvent, scalarEvent.getReadValue());
			}

		} catch (Exception e) {
			String message = "Problem storing NumberScalar value";
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
		String reason = "Failed while executing NumberScalar.numberScalarChange()...";
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

		/* Must be change because the number scalar are not only double */
		try {
			processEventScalar(getNullValueScalarEvent(errorEvent,
					((AAttribute) errorEvent.getSource()).getTangoDataType(),
					getWritableValue()), tryNumber);
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
