package HdbArchivingWatcher;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;

import Common.Watcher.AbsArchivingWatcher;
import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoDs.Attribute;
import fr.esrf.TangoDs.DeviceClass;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.ArchivingWatch;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttributeSubName;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResult;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResultLine;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Domain;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ModeData;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchiversComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchivingAttributeComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.DomainsComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.lifecycle.LifeCycleManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.lifecycle.LifeCycleManagerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.delay.DelayManagerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.delay.IDelayManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

/**
 * Class Description: This device is in charge of controlling HDB archiving. It
 * does so by comparing the theoretical archiving (the attributes that have been
 * registered for archiving) with the effective archiving (are records being
 * added for the registered attributes). If the effective archiving matches the
 * theoretical archiving, the attribute is called "OK", otherwise "KO". The
 * tightness of the required matching has a default value, but can be
 * user-defined (through expert commands).
 * 
 * ArchivingWatcher controlls all N registered attributes during a full cycle,
 * which duration T is about 4 hours. During one cycle, n sub-controls ("steps"
 * henceforth) are done, for N/n attributes each time (n is chosen so that the
 * control state is refreshed often, and that the database isn't accessed too
 * frequently).
 * 
 * Commands suffixed by "Current" return control information related to the
 * current step, ie. information that is refreshed about every 10 seconds.
 * Commands suffixed by "LatestError" return control information related to the
 * latest full cycle having KO attributes, ie. information that is refreshed
 * about every 4 hours.
 * 
 * Once a cycle has KO attributes, the device's state becomes ALARM, and stays
 * the same even if later cycles have none. This is so that the operator is
 * warned of the archiving problem. After he is, he can "reset" the device's
 * state to normal.
 * 
 * @author $Author: pierrejoseph $
 * @version $Revision: 1.12 $
 */

// --------- Start of States Description ----------
/*
 * Device States Description: DevState.ON : Controlling. DevState.OFF : Standby.
 * DevState.FAULT : Problem to connect to the database or other severe device
 * conditions. DevState.INIT : Device was started, but no control step has been
 * completed yet. Please Wait. DevState.ALARM : Archiving problems have been
 * detected during this cycle or a previous cycle.
 */
// --------- End of States Description ----------

public class HdbArchivingWatcher extends AbsArchivingWatcher {
    // private int state;
    private final String m_version;
    // --------- Start of attributes data members ----------

    protected short[] attr_ArchivingHealth_read = new short[1];
    protected String[] attr_FormattedReport_read = new String[10000];

    // --------- End of attributes data members ----------

    // Add your own data members here
    private ControlResult controlResult;
    private final int readType = IDelayManager.READ_ROLLOVER;

    // private ControlResultLineComparator linesComparator;
    private ArchiversComparator archiversComparator;
    private DomainsComparator domainsComparator;
    // private ArchivingAttributeSubNamesComparator
    // archivingAttributeSubNamesComparator;
    private ArchivingAttributeComparator archivingAttributeComparator;

    // --------------------------------------

    // =========================================================
    /**
     * Constructor for simulated Time Device Server.
     * 
     * @param cl
     *            The DeviceClass object
     * @param s
     *            The Device name.
     * @param version
     *            The device version
     */
    HdbArchivingWatcher(final DeviceClass cl, final String s, final String version)
	    throws DevFailed {
	super(cl, s);
	m_version = version;
	init_device();
    }

    // =========================================================
    /**
     * Constructor for simulated Time Device Server.
     * 
     * @param cl
     *            The DeviceClass object
     * @param s
     *            The Device name.
     * @param d
     *            Device description.
     * @param version
     *            The device version
     */
    HdbArchivingWatcher(final DeviceClass cl, final String s, final String d, final String version)
	    throws DevFailed {
	super(cl, s, d);
	m_version = version;
	init_device();
    }

    @Override
    protected DbDatum getClassProperty(final String name) {
	final HdbArchivingWatcherClass ds_class = (HdbArchivingWatcherClass) super
		.get_device_class();
	return ds_class.get_class_property(name);
    }

    // =========================================================
    /**
     * Initialize the device.
     */
    // =========================================================
    @Override
    public void init_device() throws DevFailed {
	get_device_property();

	// Initialise variables to default values
	super.initArchivingDataWatch();

	LifeCycleManagerFactory.setWatcherToWarn(this);
	final LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
		.getImpl(LifeCycleManagerFactory.HDB_LIFE_CYCLE);
	final Thread watcherThread = lifeCycleManager.getAsThread();

	startLoggingFactory();

	// this.linesComparator = new ControlResultLineComparator ();
	archiversComparator = new ArchiversComparator();
	domainsComparator = new DomainsComparator();
	// this.archivingAttributeSubNamesComparator = new
	// ArchivingAttributeSubNamesComparator ();
	archivingAttributeComparator = new ArchivingAttributeComparator();

	watcherThread.start();

	super.initLatestFaultData();

	if (!ArchivingWatch.isDoStartOnInitDevice()) {
	    warnOff();
	}
    }

    // =========================================================
    /**
     * Method always executed before command execution.
     */
    // =========================================================
    @Override
    public void always_executed_hook() {
	// get_logger().info("In always_executed_hook method()");
    }

    // ===================================================================

    // ===================================================================
    /**
     * Method called by the read_attributes CORBA operation to read device
     * hardware
     * 
     * @param attr_list
     *            Vector of index in the attribute vector of attribute to be
     *            read
     * @throws DevFailed
     */
    // ===================================================================
    @Override
    public void read_attr_hardware(final Vector attr_list) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);
	controlResult = _controlResult == null ? null : _controlResult.cloneControlResult();
    }

    // ===================================================================
    /**
     * Method called by the read_attributes CORBA operation to set internal
     * attribute value.
     * 
     * @param attr
     *            reference to the Attribute object
     */
    // ===================================================================
    @Override
    public void read_attr(final Attribute attr) throws DevFailed {
	final String attr_name = attr.get_name();
	// get_logger().info("In read_attr for attribute " + attr_name);

	// Switch on attribute name
	if (attr_name.equals("ArchivingHealth")) {
	    // Add your own code here
	    final int code = controlResult == null ? ControlResult.NOT_READY : controlResult
		    .getCode();
	    attr.set_value((short) code);
	} else if (attr_name.equals("FormattedReport")) {
	    // Add your own code here
	    final String report = controlResult == null ? ControlResult.EMPTY_REPORT
		    : controlResult.getReport();

	    final String[] res = new String[1];
	    res[0] = report;
	    attr.set_value(res, 1);
	} else if (attr_name == "version") {
	    // Add your own code here
	    attr.set_value(m_version);
	}
	// ---------------------------------
    }

    // =========================================================
    /**
     * Execute command "GetReport" on device. Returns the complete formatted
     * report
     * 
     * @return The complete formatted report
     */
    // =========================================================
    private String get_report(final ControlResult _controlResult) throws DevFailed {
	final String empty = ControlResult.EMPTY_REPORT;
	if (_controlResult == null) {
	    return empty;
	}

	return _controlResult.getReport();
    }

    // =========================================================
    /**
     * Execute command "IsAttributeCorrectlyArchived" on device. Returns true if
     * archiving works correctly for this attribute
     * 
     * @param argin
     *            The complete name of the attribute
     * @return True if archiving works correctly for this attribute
     */
    // =========================================================
    private boolean is_attribute_correctly_archived(final String argin,
	    final ControlResult _controlResult) throws DevFailed {
	if (_controlResult == null) {
	    throw new DevFailed(HdbArchivingWatcher.getNotYetReadyError());
	}

	return _controlResult.isAttributeCorrectlyArchived(argin);
    }

    private static DevError[] getNotYetReadyError() {
	final ErrSeverity severity = ErrSeverity.WARN;
	final String reason = ControlResult.EMPTY_REPORT;
	final String desc = ControlResult.EMPTY_REPORT;
	final String origin = ControlResult.EMPTY_REPORT;

	final DevError devError = new DevError(reason, severity, desc, origin);
	final DevError[] ret = new DevError[1];
	ret[0] = devError;
	return ret;
    }

    // =========================================================
    /**
     * Execute command "Start" on device. Starts archiving control with default
     * parameters. Use start_absolute or start_relative to specify those
     * parameters.
     */
    // =========================================================
    public void start() throws DevFailed {
	get_logger().info("Entering start()");

	// ---Add your Own code to control device here ---
	final LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl();
	lifeCycleManager.startProcessing();
	// ---END

	get_logger().info("Exiting start()");
    }

    // =========================================================
    /**
     * Execute command "StartAbsolute" on device. Starts archiving control in
     * the specified absolute mode. In this mode, the period that is effectively
     * controlled is the theoretical period + a fixed duration (hence the name
     * 'absolute')
     * 
     * @param argin
     *            The absolute mode parameters: argin[0] = the duration amount,
     *            argin[1] = the duration nature (1=seconds, 2=minutes, 3=hours)
     */
    // =========================================================
    public void start_absolute(final int[] argin) throws DevFailed {
	get_logger().info("Entering start_absolute()");

	// ---Add your Own code to control device here ---
	if (argin == null || argin.length != 2) {
	    Tools.throwDevFailed(new IllegalArgumentException(
		    "Needs 2 int parameters: [ amount , type ]"));
	}

	final int amount = argin[0];
	final int type = argin[1];
	SaferPeriodCalculatorFactory.getAbsoluteImpl(amount, type);

	start();
	// ---END

	get_logger().info("Exiting start_absolute()");
    }

    // =========================================================
    /**
     * Execute command "StartRelative" on device. Starts archiving control in
     * the specified relative mode. In this mode, the period that is effectively
     * controlled is the theoretical period * a multipier (hence the name
     * 'relative')
     * 
     * @param argin
     *            The multiplier
     */
    // =========================================================
    public void start_relative(final double argin) throws DevFailed {
	get_logger().info("Entering start_absolute()");

	// ---Add your Own code to control device here ---
	if (argin <= 0) {
	    Tools.throwDevFailed(new IllegalArgumentException("The multiplier has to be > 0"));
	}

	SaferPeriodCalculatorFactory.getRelativeImpl(argin);

	start();
	// ---END

	get_logger().info("Exiting start_absolute()");
    }

    // =========================================================
    /**
     * Execute command "Stop" on device. Stops archiving control, puts device in
     * standby
     * 
     */
    // =========================================================
    public void stop() throws DevFailed {
	get_logger().info("Entering stop()");

	// ---Add your Own code to control device here ---
	final LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl();
	lifeCycleManager.stopProcessing();
	// this.warnOff ();
	// ---END

	get_logger().info("Exiting stop()");
    }

    // =========================================================
    /**
     * Execute command "GetErrorArchiversCurrent" on device. Lists the archivers
     * that had at least one attribute incorrectly archived during the current
     * control cycle. Partially refreshed every time an attribute is controlled
     * (at worst every 10 seconds)
     * 
     * @return The list of archivers that have at least one KO attribute.
     */
    // =========================================================
    public String[] get_error_archivers_current() throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);

	return get_error_archivers(_controlResult);
    }

    /**
     * @param result
     * @return
     */
    private String[] get_error_archivers(final ControlResult _controlResult) {
	final String[] empty = new String[0];
	if (_controlResult == null) {
	    return empty;
	}

	final Map _errorArchivers = _controlResult.getErrorArchivers();
	final String[] argout = new String[_errorArchivers.size()];

	final List list = new Vector();
	list.addAll(_errorArchivers.values());
	Collections.sort(list, archiversComparator);
	final Iterator it = list.iterator();

	int i = 0;
	while (it.hasNext()) {
	    final Archiver key = (Archiver) it.next();
	    argout[i] = key.getName();
	    i++;
	}

	return argout;
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForArchiver" on device. Lists KO attributes for
     * this archiver
     * 
     * @param argin
     *            The name of the archiver
     * @return The list of KO attributes for this archiver
     */
    // =========================================================
    private String[] get_errors_for_archiver(final String argin, final ControlResult _controlResult)
	    throws DevFailed {
	final String[] empty = new String[0];
	if (_controlResult == null) {
	    return empty;
	}

	final Hashtable _errorArchivers = _controlResult.getErrorArchivers();
	if (_errorArchivers == null) {
	    return empty;
	}

	final Archiver archiver = (Archiver) _errorArchivers.get(argin);
	if (archiver == null) {
	    return empty;
	}

	final Hashtable _errorAttributes = archiver.getKOAttributes();
	if (_errorAttributes == null) {
	    return empty;
	}

	final String[] argout = new String[_errorAttributes.size()];
	final List list = new Vector();
	list.addAll(_errorAttributes.values());
	Collections.sort(list, archivingAttributeComparator);
	final Iterator it = list.iterator();
	int i = 0;

	while (it.hasNext()) {
	    final ArchivingAttribute key = (ArchivingAttribute) it.next();
	    argout[i] = key.getCompleteName();
	    i++;
	}
	// -----------------END
	return argout;
    }

    // =========================================================
    /**
     * Execute command "Reset" on device. Notifies the device that the last
     * alarm (should it exist) was taken into account by the user.
     * 
     */
    // =========================================================
    // public synchronized void reset() throws DevFailed
    // {
    // get_logger().info("Entering reset()");
    // // ---Add your Own code to control device here ---
    // this.goBackToFormerState ();
    // get_logger().info("Exiting reset()");
    // }

    // =========================================================
    /**
     * Execute command "Reset" on device. This is used after a KO control cycle
     * has set the state to ALARM, to notify the device that the user has seen
     * the ALARM state. Resets the state to its former value.
     */
    // =========================================================
    public void reset() throws DevFailed {
	get_logger().info("Entering reset()");

	// ---Add your Own code to control device here ---
	if (get_state().value() != DevState._ALARM) {
	    // do nothing
	    return;
	}

	final LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl();
	final boolean isProcessing = lifeCycleManager.isProcessing();
	if (isProcessing) {
	    this.set_state((short) DevState._ON);
	} else {
	    this.set_state((short) DevState._OFF);
	}

	get_logger().info("Exiting reset()");
    }

    // =========================================================
    /**
     * Execute command "Restart" on device. Restarts the device.
     */
    // =========================================================
    public void restart() throws DevFailed {
	get_logger().info("Entering restart()");

	// complete stop
	final LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
		.getImpl(LifeCycleManagerFactory.HDB_LIFE_CYCLE);
	lifeCycleManager.stopProcessing();

	final Thread watcherThread = lifeCycleManager.getAsThread();
	try {
	    watcherThread.join(1000);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	    Tools.throwDevFailed(e);
	}

	// restarting
	init_device();

	final boolean isProcessing = lifeCycleManager.isProcessing();
	if (isProcessing) {
	    this.set_state((short) DevState._ON);
	} else {
	    this.set_state((short) DevState._OFF);
	}

	get_logger().info("Exiting restart()");
    }

    private String[] get_error_domains(final ControlResult _controlResult) throws DevFailed {
	final String[] empty = new String[0];
	if (_controlResult == null) {
	    return empty;
	}

	final Hashtable _errorDomains = _controlResult.getErrorDomains();
	final String[] argout = new String[_errorDomains.size()];

	final List list = new Vector();
	list.addAll(_errorDomains.values());
	Collections.sort(list, domainsComparator);
	final Iterator it = list.iterator();

	int i = 0;
	while (it.hasNext()) {
	    final Domain key = (Domain) it.next();
	    argout[i] = key.getName();
	    i++;
	}

	return argout;
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForDomain" on device. Lists KO attributes for
     * this domain
     * 
     * @param argin
     *            The domain name
     * @return The list of KO attributes for this domain.
     */
    // =========================================================
    private String[] get_errors_for_domain(final String argin, final ControlResult _controlResult)
	    throws DevFailed {
	final String[] empty = new String[0];
	if (_controlResult == null) {
	    return empty;
	}

	final Hashtable _errorDomains = _controlResult.getErrorDomains();
	if (_errorDomains == null) {
	    return empty;
	}

	final Domain domain = (Domain) _errorDomains.get(argin);
	if (domain == null) {
	    return empty;
	}

	final Hashtable _errorAttributes = domain.getKOAttributes();
	if (_errorAttributes == null) {
	    return empty;
	}

	final String[] argout = new String[_errorAttributes.size()];

	final List list = new Vector();
	list.addAll(_errorAttributes.values());
	Collections.sort(list, archivingAttributeComparator);
	final Iterator it = list.iterator();

	int i = 0;
	while (it.hasNext()) {
	    final ArchivingAttribute key = (ArchivingAttribute) it.next();
	    argout[i] = key.getCompleteName();
	    i++;
	}

	return argout;
    }

    // =========================================================
    /**
     * Execute command "SetMode" on device. Choose a mode. Must be one of those
     * predefined modes: READ_LATEST_COMPLETE_CYCLE = 10
     * READ_LATEST_COMPLETE_CYCLE_AUTO = 11 READ_LATEST_COMPLETE_STEP = 20
     * READ_LATEST_ADDITION = 30 READ_LATEST_BAD_CYCLE = 40
     * 
     * @param argin
     *            The mode
     */
    // =========================================================
    /*
     * public synchronized void set_mode(short argin) throws DevFailed {
     * get_logger().info("Entering set_mode()");
     * 
     * // ---Add your Own code to control device here --- IDelayManager
     * delayManager = DelayManagerFactory.getCurrentImpl (); try {
     * delayManager.setReadType ( argin ); this.readType = argin; } catch (
     * IllegalArgumentException iae ) { DevFailed devFailed = new DevFailed ();
     * devFailed.initCause ( iae ); throw iae; }
     * //------------------------------------------------
     * 
     * get_logger().info("Exiting set_mode()"); }
     */

    // =========================================================
    /**
     * Execute command "GetErrorsForAttribute" on device. Returns the list of KO
     * attributes for this attribute name
     * 
     * @param argin
     *            The NOT complete name of the attribute
     * @return The list of KO attributes for this attribute name
     */
    // =========================================================
    private String[] get_errors_for_attribute(final String argin, final ControlResult _controlResult)
	    throws DevFailed {
	final String[] empty = new String[0];
	if (_controlResult == null) {
	    return empty;
	}

	final Hashtable _errorAttributeSubNames = _controlResult.getErrorSubAttributes();
	if (_errorAttributeSubNames == null) {
	    return empty;
	}

	final ArchivingAttributeSubName attributeSubName = (ArchivingAttributeSubName) _errorAttributeSubNames
		.get(argin);
	if (attributeSubName == null) {
	    return empty;
	}

	final Hashtable _errorAttributes = attributeSubName.getKOAttributes();
	if (_errorAttributes == null) {
	    return empty;
	}

	final String[] argout = new String[_errorAttributes.size()];

	final List list = new Vector();
	list.addAll(_errorAttributes.values());
	Collections.sort(list, archivingAttributeComparator);
	final Iterator it = list.iterator();

	int i = 0;
	while (it.hasNext()) {
	    final ArchivingAttribute key = (ArchivingAttribute) it.next();
	    argout[i] = key.getCompleteName();
	    i++;
	}

	return argout;
    }

    // =========================================================
    /**
     * Execute command "GetReportCurrent" on device. Returns a formatted report
     * representing the current control cycle results. Partially refreshed every
     * time an attribute is controlled (at worst every 10 seconds)
     * 
     * @return The report
     */
    // =========================================================
    public String get_report_current() throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);
	// ControlResult _controlResult = delayManager.getControlResult (
	// IDelayManager.READ_ROLLOVER );

	return get_report(_controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetReportLatestError" on device. Returns a formatted
     * report representing the latest KO control cycle results. (a control cycle
     * becomes KO when it has at least one KO atribute, and until the user
     * cleans the state via the "reset" command or another cycle is KO)
     * 
     * @return The report
     */
    // =========================================================
    public String get_report_latest_error() throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return get_report(_controlResult);
    }

    // =========================================================
    /**
     * Execute command "IsAttributeCorrectlyArchivedCurrent" on device. Returns
     * whether the specified attribute was correctly archiving during the
     * current control cycle. Partially refreshed every time an attribute is
     * controlled (at worst every 10 seconds)
     * 
     * @param argin
     *            The attribute complete name
     * @return True if archiving works correctly for this attribute
     */
    // =========================================================
    public boolean is_attribute_correctly_archived_current(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);

	return is_attribute_correctly_archived(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "IsAttributeCorrectlyArchivedLatestError" on device.
     * Returns whether the specified attribute was correctly archiving during
     * the current control cycle. (a control cycle becomes KO when it has at
     * least one KO atribute, and until the user cleans the state via the
     * "reset" command or another cycle is KO)
     * 
     * @param argin
     *            The attribute complete name
     * @return True if archiving works correctly for this attribute
     */
    // =========================================================
    public boolean is_attribute_correctly_archived_latest_error(final String argin)
	    throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return is_attribute_correctly_archived(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorArchiversLatestError" on device. Lists the
     * archivers that had at least one attribute incorrectly archived during the
     * latest KO control cycle. (a control cycle becomes KO when it has at least
     * one KO atribute, and until the user cleans the state via the "reset"
     * command or another cycle is KO)
     * 
     * @return The list of archivers that had at least one KO attribute.
     */
    // =========================================================
    public String[] get_error_archivers_latest_error() throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return get_error_archivers(_controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForArchiverCurrent" on device. Lists the KO
     * attributes for a given archiver during the current control cycle.
     * Partially refreshed every time an attribute is controlled (at worst every
     * 10 seconds)
     * 
     * @param argin
     *            The name of the archiver
     * @return The list of KO attributes for this archiver
     */
    // =========================================================
    public String[] get_errors_for_archiver_current(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);

	return get_errors_for_archiver(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetAllArchivingAttributes" on device. Lists all HDB
     * archiving attributes (does a new request regardless of the watcher
     * cycle).
     * 
     * @return The list of all HDB archiving attributes.
     */
    // =========================================================
    public String[] get_all_archiving_attributes() throws DevFailed {
	String[] ret = null;
	try {
	    final Hashtable<String, ModeData> all = dbReader.getArchivedAttributes();

	    ret = all == null ? null : new String[all.size()];
	    if (ret == null) {
		return null;
	    }

	    final ControlResult cr = new ControlResult();
	    cr.setAllArchivingAttributes(all);
	    final ControlResultLine[] orderedLines = cr.sort();

	    for (int i = 0; i < all.size(); i++) {
		ret[i] = orderedLines[i].getAttribute().getCompleteName();
		final Mode mode = dbReader.getModeForAttribute(ret[i]);
		if (mode != null) {
		    ret[i] += ": " + mode.toStringWatcher();
		}
	    }
	} catch (final Exception df) {
	    logger.trace(ILogger.LEVEL_ERROR,
		    "get_all_archiving_attributes/error! VVVVVVVVVVVVVVVVV");
	    logger.trace(ILogger.LEVEL_ERROR, df);
	    logger.trace(ILogger.LEVEL_ERROR,
		    "get_all_archiving_attributes/error! ^^^^^^^^^^^^^^^^^");
	    Tools.throwDevFailed(df);
	}
	return ret;
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForArchiverLatestError" on device. Lists the KO
     * attributes for a given archiver during the latest KO control cycle. (a
     * control cycle becomes KO when it has at least one KO atribute, and until
     * the user cleans the state via the "reset" command or another cycle is KO)
     * 
     * @param argin
     *            The name of the archiver
     * @return The list of KO attributes for this archiver
     */
    // =========================================================
    public String[] get_errors_for_archiver_latest_error(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return get_errors_for_archiver(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorDomainsCurrent" on device. Lists the domains
     * that had at least one attribute incorrectly archived during the current
     * control cycle. Partially refreshed every time an attribute is controlled
     * (at worst every 10 seconds)
     * 
     * @return The list of archivers that have at least one KO attribute.
     */
    // =========================================================
    public String[] get_error_domains_current() throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);

	return get_error_domains(_controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorDomainsLatestError" on device. Lists the domains
     * that had at least one attribute incorrectly archived during the latest KO
     * control cycle. (a control cycle becomes KO when it has at least one KO
     * atribute, and until the user cleans the state via the "reset" command or
     * another cycle is KO)
     * 
     * @return The list of archivers that had at least one KO attribute.
     */
    // =========================================================
    public String[] get_error_domains_latest_error() throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return get_error_domains(_controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForDomainCurrent" on device. Lists the KO
     * attributes for a given domain during the current control cycle. Partially
     * refreshed every time an attribute is controlled (at worst every 10
     * seconds)
     * 
     * @param argin
     *            The name of the domain
     * @return The list of KO attributes for this domain
     */
    // =========================================================
    public String[] get_errors_for_domain_current(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);

	return get_errors_for_domain(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForDomainLatestError" on device. Lists the KO
     * attributes for a given domain during the current control cycle. (a
     * control cycle becomes KO when it has at least one KO atribute, and until
     * the user cleans the state via the "reset" command or another cycle is KO)
     * 
     * @param argin
     *            The name of the domain
     * @return The list of KO attributes for this domain
     */
    // =========================================================
    public String[] get_errors_for_domain_latest_error(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return get_errors_for_domain(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForAttributeCurrent" on device. Lists the KO
     * attributes with a given name during the current control cycle. Partially
     * refreshed every time an attribute is controlled (at worst every 10
     * seconds)
     * 
     * @param argin
     *            The attribute (NOT the complete name)
     * @return The list of KO attributes with this name
     */
    // =========================================================
    public String[] get_errors_for_attribute_current(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_ROLLOVER);

	return get_errors_for_attribute(argin, _controlResult);
    }

    // =========================================================
    /**
     * Execute command "GetErrorsForAttributeLatestError" on device. Lists the
     * KO attributes with a given name during the current control cycle. (a
     * control cycle becomes KO when it has at least one KO atribute, and until
     * the user cleans the state via the "reset" command or another cycle is KO)
     * 
     * @param argin
     *            The attribute (NOT the complete name)
     * @return The list of KO attributes with this name
     */
    // =========================================================
    public String[] get_errors_for_attribute_latest_error(final String argin) throws DevFailed {
	final IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
	final ControlResult _controlResult = delayManager
		.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

	return get_errors_for_attribute(argin, _controlResult);
    }

    /**
     * /**
     * 
     * @return Watcher Archiving Report
     */
    public void start_archiving_report() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
		return;
	    }
	    dbReader.startArchivingReport();
	} catch (final DevFailed e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * 
     * @return Watcher database Report
     */
    public String[] get_database_report() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    return dbReader.getDatabaseReport();
	} catch (final DevFailed e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 
     * @return Watcher attributes count
     */
    public int get_current_archiving_attributes_count() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    return dbReader.getDatabase().getMode().getCurrentArchivedAttCount();
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return 0;
    }

    /**
     * 
     * @return Watcher KO attributes count
     */
    public int get_ko_attributes_count() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    return dbReader.getDatabase().getDbUtil().getAttributesCountOkOrKo(false);
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return 0;
    }

    /**
     * 
     * @return Watcher OK attributes count
     */
    public int get_ok_attributes_count() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    return dbReader.getDatabase().getDbUtil().getAttributesCountOkOrKo(true);
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return 0;
    }

    /**
     * 
     * @return Watcher list of ko attributes
     */
    public String[] get_ko_attributes() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }

	    return dbReader.getDatabase().getDbUtil().getKoAttributes();
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * 
     * @return Watcher list of oracle databse partitions
     */
    public String[] get_partitions() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }

	    return dbReader.getDatabase().getDbUtil().getListOfPartitions();
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * 
     * @return Watcher list of job status
     */
    public String[] get_job_status() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    final String[] res = { "No Job Status" };
	    final String[] tmp = dbReader.getDatabase().getDbUtil().getListOfJobStatus();
	    if (tmp == null) {
		return res;
	    } else {
		return tmp;
	    }
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * 
     * @return Watcher list of oracle databse partitions
     */
    public String[] get_job_errors() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    final String[] res = { "No Job Errors" };
	    final String[] tmp = dbReader.getDatabase().getDbUtil().getListOfJobErrors();
	    if (tmp == null) {
		return res;
	    } else {
		return tmp;
	    }
	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * return in % the feedalive procedure progression level
     * 
     * @return
     */
    public int get_feedalive_progression_level() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    return dbReader.getDatabase().getDbUtil().getFeedAliveProgression();

	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return 0;
    }

    /**
     * get ko attributes by archiver device
     * 
     * @return
     */
    public String[] get_ko_attributes_count_by_device() throws DevFailed {
	// TODO Auto-generated method stub

	try {
	    if (dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
		Except.throw_exception("DATABASE_ERROR", "MySql Exception",
			"MySql is not supported for this command ");
	    }
	    return dbReader.getDatabase().getDbUtil().getKOAttrCountByDevice();

	} catch (final ArchivingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    // =========================================================
    /**
     * main part for the device server class
     */
    // =========================================================
    public static void main(final String[] argv) {
	try {
	    final Util tg = Util.init(argv, "HdbArchivingWatcher");
	    tg.server_init();

	    System.out.println("Ready to accept request");

	    tg.server_run();
	}

	catch (final OutOfMemoryError ex) {
	    System.err.println("Can't allocate memory !!!!");
	    System.err.println("Exiting");
	} catch (final UserException ex) {
	    Except.print_exception(ex);

	    System.err.println("Received a CORBA user exception");
	    System.err.println("Exiting");
	} catch (final SystemException ex) {
	    Except.print_exception(ex);

	    System.err.println("Received a CORBA system exception");
	    System.err.println("Exiting");
	}

	System.exit(-1);
    }

    private void startLoggingFactory() {
	// System.out.println (
	// "CLA/startLoggingFactory/this.device_name/"+this.device_name+"/hashCode/"+this.hashCode
	// ()+"/this.hasDiary/"+this.hasDiary );

	final ILogger _logger = LoggerFactory.getImpl(device_name, ArchivingWatch.getDiaryPath(),
		ArchivingWatch.isHasDiary());
	_logger.setTraceLevel(ArchivingWatch.getDiaryLogLevel());

	logger = _logger;
    }

    @Override
    public void delete_device() throws DevFailed {
	// TODO Auto-generated method stub

    }
}

// --------------------------------------------------------------------------
/*
 * end of $Source:
 * /cvsroot/tango-cs/tango/jserver/archiving/HdbArchivingWatcher/
 * HdbArchivingWatcher.java,v $
 */
