//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberSpectrum_RW.
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
import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.events.ITangoArchiveListener;
import fr.esrf.TangoApi.events.TangoArchive;
import fr.esrf.TangoApi.events.TangoArchiveEvent;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.Device;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.INumberSpectrum;
import fr.esrf.tangoatk.core.ISpectrumListener;
import fr.esrf.tangoatk.core.NumberSpectrumEvent;
import fr.esrf.tangoatk.core.attribute.AttributeFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

public class NumberSpectrum_RW extends HdbCollector implements ISpectrumListener,
	ITangoArchiveListener {

    /**
	 *
	 */
    private static final long serialVersionUID = 6326850167925075893L;
    private final HashMap<Device, TangoEventsAdapter> evtAdaptHMap;

    public NumberSpectrum_RW(final HdbModeHandler hdbModeHandler) {
	super(hdbModeHandler);
	evtAdaptHMap = new HashMap<Device, TangoEventsAdapter>();
    }

    @Override
    public void removeSource(final String attributeName) throws ArchivingException {
	Util.out2.println("NumberSpectrum_RW.removeSource");

	if (HdbArchiver.isUseEvents) {
	    removeSourceForEvents(attributeName);
	}

	Util.out2.println("Spectrum_RO.removeSource");
	try {
	    synchronized (attributeList) {
		/*
		 * while ( ( INumberSpectrum ) attributeList.get(attributeName)
		 * != null ) {
		 */
		final INumberSpectrum attribute = (INumberSpectrum) attributeList
			.get(attributeName);
		if (attribute != null) {

		    attribute.removeSpectrumListener(this);
		    attribute.removeErrorListener(this);

		    attributeList.remove(attributeName);
		    Util.out4.println("\t The attribute named " + attributeName
			    + " was fired from the Collector list...");
		    if (attributeList.isEmpty()) {
			stopCollecting();
		    }
		}
	    }
	} catch (final Exception e) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed removing '"
		    + attributeName + "' from sources";
	    final String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	    final String desc = "Failed while executing Spectrum_RO.removeSource() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
	}
    }

    @Override
    protected void addSourceForPolling(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	try {
	    synchronized (attributeList) {
		// while ( ( INumberSpectrum )
		// attributeList.get(attributeLightMode.getAttribute_complete_name())
		// == null )
		// {
		final INumberSpectrum attribute = (INumberSpectrum) attributeList
			.add(attributeLightMode.getAttribute_complete_name());
		attribute.addSpectrumListener(this);
		attribute.addErrorListener(this);
		Util.out4.println("\t The attribute named "
			+ attributeLightMode.getAttribute_complete_name()
			+ " was hired to the Collector list...");
		startCollecting();
	    }
	} catch (final ConnectionException e) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '"
		    + attributeLightMode.getAttribute_complete_name() + "' as source";
	    final String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	    final String desc = "Failed while executing Spectrum_RO.addSource() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
	}
    }

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
    @Override
    protected void addSourceForEvents(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	/* Adapter for the Tango archive events */
	TangoEventsAdapter evtAdapt = null;
	INumberSpectrum attribute = null;

	try {
	    /*
	     * Get the attribute from the AttributeFactory, so that it is not
	     * added to the attribute polled list. Remember that `attributeList'
	     * is an AttributePolledList().
	     */
	    attribute = (INumberSpectrum) AttributeFactory.getInstance().getAttribute(
		    attributeLightMode.getAttribute_complete_name());
	    if (attribute == null) {
		System.out.println("\033[1;31mNumberSpectrum_RW.java: the attribute \""
			+ attributeLightMode.getAttribute_complete_name()
			+ " is null (NumberSpectrum_RW.java): not adding it!");
		return;
	    }
	} catch (final DevFailed e) {
	    System.out
		    .println("\033[1;31mNumberSpectrum_RW.java: Failed to get the attribute from the AttributeFactory\033[0m");
	    for (final DevError error : e.errors) {
		System.out.println("error: " + error.desc);
	    }
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '"
		    + attributeLightMode.getAttribute_complete_name() + "' as source";
	    final String reason = "DevFailed exception while calling AttributeFactory.getInstance().getAttribute();";
	    final String desc = "Failed while executing Spectrum_RW.addSource() method...\nSpectrum_RW.java: Failed to get the attribute from the AttributeFactory";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
	} catch (final ConnectionException e) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '"
		    + attributeLightMode.getAttribute_complete_name() + "' as source";
	    final String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	    final String desc = "Failed while executing NumberSpectrum_RW.addSource() method...\nNumberSpectrum_RW.java: Failed to get the attribute from the AttributeFactory";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
	} /*
	   * end of first try/catch block, to retrieve the attribute from the
	   * AttributeFactory
	   */

	System.out
		.print("\033[0;44mregistering\033[0m the \033[4mSPECTRUM\033[0m [RO] attribute \""
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
	 * System.out.print("[\033[1;45mEVENTS DISABLED\033[0m].");
	 * registerForScalarListener(attributeLightMode); } else {
	 */
	try {
	    synchronized (evtAdaptHMap) {
		evtAdapt = evtAdaptHMap.get(attribute.getDevice());
		if (evtAdapt == null) {
		    evtAdapt = new TangoEventsAdapter(attribute.getDevice());
		    evtAdaptHMap.put(attribute.getDevice(), evtAdapt);
		} else {
		    System.out.println("\nThe adapter for the attribute is already configured");
		}
	    }
	    final String[] filter = new String[0];

	    /*
	     * Try to register for the archive events: evtAdapt is the new
	     * adapter or the one already found in the map.
	     */
	    evtAdapt.addTangoArchiveListener(this, attribute.getNameSansDevice(), filter);
	    archive_listeners_counter++;
	    System.out.println("[\033[0;42mEVENTS\033[0m].");
	} catch (final DevFailed e) {
	    /*
	     * If archive events are not enabled for the attribute, we will poll
	     * it
	     */
	    for (final DevError error : e.errors) {

		if (error.desc.contains("Already connected to event")) {
		    System.out.println("Already connected to events for attribute");
		    return;
		} else if (error.desc
			.contains("The polling (necessary to send events) for the attribute")) {
		    System.out.print(" {\033[1;35mpoller not started\033[0m} ");
		    break;
		} else if (error.desc.contains("Archive event properties")) {
		    System.out.print(" {\033[1;35marchive event properties not set\033[0m} ");
		    break;
		} else {
		    System.out.println("error registering: " + error.desc);
		}

	    }
	    /* Register for polling */
	    addSourceForPolling(attributeLightMode);
	    /* unlock the attributeList */
	}
	// } /* function finishes */
    }

    private void removeSourceForEvents(final String attributeName) {
	Util.out2.println("NumberSpectrum_RW.removeSource");
	System.out.print("\033[0;44mremoving\033[0m source for \033[4mspectrum\033[0m [RW]:\""
		+ attributeName + "\"...\t");
	TangoEventsAdapter adapter;
	INumberSpectrum attribute;
	/*
	 * If useEvents is enabled, we should remove the eventListener, if not
	 * we can skip this piece of code
	 */
	// if (HdbArchiver.useEvents.equalsIgnoreCase("yes"))
	// {
	try {
	    synchronized (evtAdaptHMap) {
		attribute = (INumberSpectrum) AttributeFactory.getInstance().getAttribute(
			attributeName);
		/*
		 * Retrieve in the hash table the event adapter associated to
		 * the attributeName.
		 */
		if (attribute == null) {
		    System.out.println("\033[1;31mThe attribute " + attributeName
			    + " is null\033[0m");
		}
		adapter = evtAdaptHMap.get(attribute.getDevice());

		if (adapter != null) {
		    adapter.removeTangoArchiveListener(this, attribute.getNameSansDevice());
		    archive_listeners_counter--;
		    System.out.println("[\033[0;42mEVENTS\033[0m].");
		    Util.out2.println(" (adapter: " + adapter.device_name() + ")");
		    /* Should be ok now to return */
		    return;
		}
	    } /* unlock event adapters map */
	} catch (final ConnectionException e) /* getAttribute() failed */
	{
	    Util.out2
		    .println("\033[1;31mConnection exception while retrieving the attribute from the AttributeFactory!\033[0m");
	} catch (final DevFailed f) {
	    print_exception("Error removing tangoArchiveListener for the attribute "
		    + attributeName, f);
	}
	// }
    }

    /**
     * @since events
     * @author giacomo S. elettra This is called when a Tango archive event is
     *         sent from the server (the attribute has changed)
     */
    public void archive(final TangoArchiveEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	DeviceAttribute attrib = null;
	TangoArchive arch;
	DeviceProxy proxy;
	final SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();

	try {
	    attrib = event.getValue();
	} catch (final DevFailed f) {
	    System.out
		    .println("Spectrum_RW.java: archive(): Error getting archive event attribute value");
	    f.printStackTrace();
	    return;
	} catch (final Exception e) /* Shouldn't be reached */
	{
	    System.out
		    .println("Spectrum_RW.java.archive.getValue() failed, caught generic Exception, code failure");
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
			.println("\033[1;31mSpectrum_RW.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
		return;
	    }
	    final int dim = attrib.getDimX();
	    final Integer[] ivalue = new Integer[dim];
	    int i;
	    switch (attrib.getType()) {
	    case TangoConst.Tango_DEV_SHORT:
		final Short[] value = new Short[dim];
		short[] tval = new short[dim];
		tval = attrib.extractShortArray();
		for (i = 0; i < dim; i++) {
		    value[i] = tval[i];
		}
		spectrumEvent_rw.setValue(value);
		break;

	    case TangoConst.Tango_DEV_USHORT:
		final int[] ivaluetmp = attrib.extractUShortArray();
		for (i = 0; i < dim; i++) {
		    ivalue[i] = ivaluetmp[i];
		}
		spectrumEvent_rw.setValue(ivalue);
		break;

	    case TangoConst.Tango_DEV_LONG:
	    case TangoConst.Tango_DEV_ULONG:
		final int[] lvalue = attrib.extractLongArray();
		for (i = 0; i < dim; i++) {
		    ivalue[i] = lvalue[i];
		}
		spectrumEvent_rw.setValue(ivalue);
		break;

	    case TangoConst.Tango_DEV_FLOAT:
		final float[] fvaluetmp = attrib.extractFloatArray();
		final Float fvalue[] = new Float[dim];
		for (i = 0; i < dim; i++) {
		    fvalue[i] = fvaluetmp[i];
		}
		spectrumEvent_rw.setValue(fvalue);
		break;

	    case TangoConst.Tango_DEV_BOOLEAN:
		final boolean[] bvaluetmp = attrib.extractBooleanArray();
		final Boolean[] bvalue = new Boolean[dim];
		for (i = 0; i < dim; i++) {
		    bvalue[i] = bvaluetmp[i];
		}
		spectrumEvent_rw.setValue(bvalue);
		System.out
			.println("\033[1;31mboolean spectrum here? In NumberSpectrum_RW!!??\033[0m");
		break;

	    case TangoConst.Tango_DEV_DOUBLE:
	    default:
		final double[] dvaluetmp = attrib.extractDoubleArray();
		final Double dvalue[] = new Double[dim];
		for (i = 0; i < dim; i++) {
		    dvalue[i] = dvaluetmp[i];
		}
		spectrumEvent_rw.setValue(dvalue);
		break;

	    }
	    System.out.println(proxy.name() + ": " + attrib.getName()
		    + " {\033[4mspectrum\033[0m, RW} [\033[1;32mEVENT\033[0m]" + " (dim: "
		    + attrib.getDimX() + ")");
	    spectrumEvent_rw.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
	    spectrumEvent_rw.setTimeStamp(attrib.getTime());
	    spectrumEvent_rw.setData_type(attrib.getType());
	    spectrumEvent_rw.setWritable(AttrWriteType._READ_WRITE);
	    spectrumEvent_rw.setDim_x(dim);
	    /* Process the just built spectrum event */
	    processEventSpectrum(spectrumEvent_rw, tryNumber);

	} catch (final DevFailed devFailed) {
	    print_exception("Spectrum_RW.java: archive() : " + GlobalConst.ARCHIVING_ERROR_PREFIX
		    + "\r\n\t" + "Problem while reading "
		    + spectrumEvent_rw.getAttribute_complete_name() + " values...", devFailed);
	    final Object value = null;
	    spectrumEvent_rw.setValue(value);
	    processEventSpectrum(spectrumEvent_rw, tryNumber);
	} catch (final Exception exE) {
	    System.err.println("Spectrum_RW.java: archive : " + GlobalConst.ARCHIVING_ERROR_PREFIX
		    + "\r\n\t" + "Problem while reading "
		    + spectrumEvent_rw.getAttribute_complete_name() + " values...");
	    exE.printStackTrace();
	    final Object value = null;
	    spectrumEvent_rw.setValue(value);
	    processEventSpectrum(spectrumEvent_rw, tryNumber);
	}

    }

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
    @Override
    public void errorChange(final ErrorEvent errorEvent) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	Util.out3.println("Spectrum_RO.errorChange : " + "Unable to read the attribute named "
		+ errorEvent.getSource().toString());
	final Double[] value = null;
	final SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
	spectrumEvent_rw.setAttribute_complete_name(errorEvent.getSource().toString());
	spectrumEvent_rw.setTimeStamp(errorEvent.getTimeStamp());
	spectrumEvent_rw.setValue(value);
	processEventSpectrum(spectrumEvent_rw, tryNumber);
    }

    public void spectrumChange(final NumberSpectrumEvent event) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	final double[] spectrumvalue = event.getValue();
	Double[] value;
	if (spectrumvalue == null) {
	    value = null;
	} else {
	    value = new Double[spectrumvalue.length];
	    for (int i = 0; i < spectrumvalue.length; i++) {
		value[i] = new Double(spectrumvalue[i]);
	    }
	}
	// System.out.println (
	// "CLA/NumberSpectrum_RW/spectrumChange/value.length/"+value.length+"/"
	// );
	final SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
	spectrumEvent_rw
		.setAttribute_complete_name(((INumberSpectrum) event.getSource()).getName());
	spectrumEvent_rw.setDim_x(((INumberSpectrum) event.getSource()).getXDimension());
	spectrumEvent_rw.setTimeStamp(event.getTimeStamp());
	spectrumEvent_rw.setValue(value);
	processEventSpectrum(spectrumEvent_rw, tryNumber);
    }

    public void stateChange(final AttributeStateEvent event) {
    }

    public void processEventSpectrum(final SpectrumEvent_RW snapSpectrumEvent_RW, int try_number) {
	final boolean timeCondition = super.isDataArchivableTimestampWise(snapSpectrumEvent_RW);
	if (!timeCondition) {
	    return;
	}

	try // spectrum values can only have periodic modes, we don't need their
	// lastValue
	{
	    super.dbProxy.store(snapSpectrumEvent_RW);
	    super.setLastTimestamp(snapSpectrumEvent_RW);
	} catch (final ArchivingException e) {
	    final String message = "Problem (ArchivingException) storing NumberSpectrum_RW value";
	    super.m_logger.trace(ILogger.LEVEL_ERROR, message);
	    super.m_logger.trace(ILogger.LEVEL_ERROR, e);

	    Util.out4.println("SpectrumEvent_RW.processEventSpectrum/ArchivingException");

	    try_number--;
	    if (try_number > 0) {
		Util.out2.println("SpectrumEvent_RW.processEventSpectrum : \r\n\ttry " + try_number
			+ "failed...");
		processEventSpectrum(snapSpectrumEvent_RW, try_number);
	    }
	}
	// checkGC();
    }
}
