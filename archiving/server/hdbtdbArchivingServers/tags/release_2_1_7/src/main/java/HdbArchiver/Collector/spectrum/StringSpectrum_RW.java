//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  StringSpectrum_RW.
//						(Chinkumo Jean) - March 24, 2004
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.13  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.12  2007/09/28 14:49:22  pierrejoseph
// Merged between Polling and Events code
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package HdbArchiver.Collector.spectrum;

import java.util.HashMap;

import HdbArchiver.HdbArchiver;
import HdbArchiver.Collector.HdbCollector;
import HdbArchiver.Collector.HdbModeHandler;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.events.ITangoArchiveListener;
import fr.esrf.TangoApi.events.TangoArchive;
import fr.esrf.TangoApi.events.TangoArchiveEvent;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.Device;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IStringSpectrum;
import fr.esrf.tangoatk.core.IStringSpectrumListener;
import fr.esrf.tangoatk.core.StringSpectrumEvent;
import fr.esrf.tangoatk.core.attribute.AttributeFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

public class StringSpectrum_RW extends HdbCollector implements
		IStringSpectrumListener, ITangoArchiveListener {
	private static final long serialVersionUID = 4016L;

	private HashMap<Device, TangoEventsAdapter> evtAdaptHMap;

	public StringSpectrum_RW(HdbModeHandler hdbModeHandler) {
		super(hdbModeHandler);
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
		Util.out2.println("StringSpectrum_RW.removeSource");

		if (HdbArchiver.isUseEvents) {
			removeSourceForEvents(attributeName);
		}

		try {
			synchronized (attributeList) {
				/*
				 * while ( ( IStringSpectrum ) attributeList.get(attributeName)
				 * != null ) {
				 */
				IStringSpectrum attribute = (IStringSpectrum) attributeList
						.get(attributeName);
				if (attribute != null) {

					attribute.removeListener(this);
					attribute.removeErrorListener(this);

					this.attributeList.remove(attributeName);
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
			String desc = "Failed while executing Spectrum_RO.removeSource() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		}
	}

	private void addSourceForPolling(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		try {
			synchronized (attributeList) {
				// while ( ( IStringSpectrum )
				// attributeList.get(attributeLightMode.getAttribute_complete_name())
				// == null )
				// {
				IStringSpectrum attribute = (IStringSpectrum) attributeList
						.add(attributeLightMode.getAttribute_complete_name());
				attribute.addListener(this);
				attribute.addErrorListener(this);
				Util.out4.println("\t The attribute named "
						+ attributeLightMode.getAttribute_complete_name()
						+ " was hired to the Collector list...");
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
			String desc = "Failed while executing Spectrum_RO.addSource() method...";
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
		IStringSpectrum attribute = null;

		try {
			/*
			 * Get the attribute from the AttributeFactory, so that it is not
			 * added to the attribute polled list. Remember that `attributeList'
			 * is an AttributePolledList().
			 */
			attribute = (IStringSpectrum) AttributeFactory.getInstance()
					.getAttribute(
							attributeLightMode.getAttribute_complete_name());
			if (attribute == null) {
				System.out
						.println("\033[1;31mStringSpectrum_RW.java: the attribute \""
								+ attributeLightMode
										.getAttribute_complete_name()
								+ " is null (StringStringSpectrum_RW.java): not adding it!");
				return;
			}
		} catch (DevFailed e) {
			System.out
					.println("\033[1;31mStringStringSpectrum_RW.java: Failed to get the attribute from the AttributeFactory\033[0m");
			for (int err = 0; err < e.errors.length; err++)
				System.out.println("error: " + e.errors[err].desc);
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = "DevFailed exception while calling AttributeFactory.getInstance().getAttribute();";
			String desc = "Failed while executing Spectrum_RO.addSource() method...\nStringSpectrum_RW.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		} catch (ConnectionException e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing NumberSpectrum_RO.addSource() method...\nStringStringSpectrum_RW.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);
		} /*
		 * end of first try/catch block, to retrieve the attribute from the
		 * AttributeFactory
		 */

		System.out.print("STRING SPECTRUM [RO] attribute \""
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
		 * System.out.print("[\033[1;35mEVENTS DISABLED\033[0m].");
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
		Util.out2.println("NumberSpectrum_RO.removeSource");
		System.out.print("spectrum [RO]: removing source for \""
				+ attributeName + "\"...\t");
		TangoEventsAdapter adapter;
		IStringSpectrum attribute;
		/*
		 * If useEvents is enabled, we should remove the eventListener, if not
		 * we can skip this piece of code
		 */
		// if (HdbArchiver.useEvents.equalsIgnoreCase("yes"))
		// {
		try {
			synchronized (evtAdaptHMap) {
				attribute = (IStringSpectrum) AttributeFactory.getInstance()
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

	/**
	 * @since events
	 * @author giacomo S. elettra This is called when a Tango archive event is
	 *         sent from the server (the attribute has changed)
	 */
	public void archive(TangoArchiveEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		DeviceAttribute attrib = null;
		TangoArchive arch;
		DeviceProxy proxy;
		SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();

		try {
			attrib = event.getValue();
		} catch (DevFailed f) {
			System.out
					.println("StringSpectrum_RW.java: archive(): Error getting archive event attribute value");
			f.printStackTrace();
			return;
		} catch (Exception e) /* Shouldn't be reached */
		{
			System.out
					.println("StringSpectrum_RW.java.archive.getValue() failed, caught generic Exception, code failure");
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
						.println("\033[1;31mBooleanStringSpectrum_RW.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}
			int dim = attrib.getDimX(); // ??
			String[] svalue = attrib.extractStringArray();
			dim = svalue.length;
			spectrumEvent_rw.setValue(svalue);
			System.out.println(proxy.name() + ": " + attrib.getName()
					+ "{string spectrum, RO} [\033[1;32mEVENT\033[0m]"
					+ " (dim: " + attrib.getDimX());
			spectrumEvent_rw.setAttribute_complete_name(proxy.name() + "/"
					+ attrib.getName());
			spectrumEvent_rw.setTimeStamp(attrib.getTime());
			spectrumEvent_rw.setData_type(attrib.getType());
			spectrumEvent_rw.setWritable(AttrWriteType._READ_WRITE);
			spectrumEvent_rw.setDim_x(dim);
			/* Process the just built spectrum event */
			processEventSpectrum(spectrumEvent_rw, tryNumber);

		} catch (DevFailed devFailed) {
			System.err.println("BooleanStringSpectrum_RW.java: archive() : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ spectrumEvent_rw.getAttribute_complete_name()
					+ " values...");
			// printException(GlobalConst.DATA_TYPE_EXCEPTION ,
			// AttrDataFormat._SCALAR ,
			// spectrumEvent_rw.getAttribute_complete_name() , devFailed);

			Object value = null;
			spectrumEvent_rw.setValue(value);
			processEventSpectrum(spectrumEvent_rw, tryNumber);
		} catch (Exception exE) {
			System.err.println("Boolean.StringSpectrum_RW.java: archive : "
					+ GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t"
					+ "Problem while reading "
					+ spectrumEvent_rw.getAttribute_complete_name()
					+ " values...");
			exE.printStackTrace();
			Object value = null;
			spectrumEvent_rw.setValue(value);
			processEventSpectrum(spectrumEvent_rw, tryNumber);
		}

	}

	// --------------------------------------------------------------------------//
	// ELETTRA : Archiving Events
	// --------------------------------------------------------------------------//
	public void errorChange(ErrorEvent errorEvent) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		Util.out3.println("StringSpectrum_RW.errorChange : "
				+ "Unable to read the attribute named "
				+ errorEvent.getSource().toString());
		String[] value = null;
		SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
		spectrumEvent_rw.setAttribute_complete_name(errorEvent.getSource()
				.toString());
		spectrumEvent_rw.setTimeStamp(errorEvent.getTimeStamp());
		spectrumEvent_rw.setValue(value);
		processEventSpectrum(spectrumEvent_rw, tryNumber);
	}

	public void stringSpectrumChange(StringSpectrumEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		String[] value = event.getValue();
		// System.out.println (
		// "CLA/Spectrum_RW/spectrumChange/value.length/"+value.length+"/" );
		SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
		spectrumEvent_rw.setAttribute_complete_name(((IStringSpectrum) event
				.getSource()).getName());
		spectrumEvent_rw.setDim_x(((IStringSpectrum) event.getSource())
				.getXDimension());
		spectrumEvent_rw.setTimeStamp(event.getTimeStamp());
		spectrumEvent_rw.setValue(value);
		processEventSpectrum(spectrumEvent_rw, tryNumber);
	}

	public void stateChange(AttributeStateEvent event) {
	}

	public void processEventSpectrum(SpectrumEvent_RW snapSpectrumEvent_RW,
			int try_number) {
		boolean timeCondition = super
				.isDataArchivableTimestampWise(snapSpectrumEvent_RW);
		if (!timeCondition) {
			return;
		}

		try // spectrum values can only have periodic modes, we don't need their
			// lastValue
		{
			super.dbProxy.store(snapSpectrumEvent_RW);
			super.setLastTimestamp(snapSpectrumEvent_RW);
		} catch (ArchivingException e) {
			String message = "Problem (ArchivingException) storing StringSpectrum_RW value";
			super.m_logger.trace(ILogger.LEVEL_ERROR, message);
			super.m_logger.trace(ILogger.LEVEL_ERROR, e);

			Util.out4
					.println("StringSpectrumEvent_RW.processEventSpectrum/ArchivingException");

			try_number--;
			if (try_number > 0) {
				Util.out2
						.println("StringSpectrumEvent_RW.processEventSpectrum : \r\n\ttry "
								+ (try_number) + "failed...");
				processEventSpectrum(snapSpectrumEvent_RW, try_number);
			}
		}
		checkGC();
	}
}
