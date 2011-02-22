package TdbArchivingWatcher;

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

public class TdbArchivingWatcher extends AbsArchivingWatcher {
	// private int state;
	private String m_version;
	// --------- Start of attributes data members ----------

	protected short[] attr_ArchivingHealth_read = new short[1];
	protected String[] attr_FormattedReport_read = new String[10000];

	// private ControlResultLineComparator linesComparator;
	private ArchiversComparator archiversComparator;
	private DomainsComparator domainsComparator;
	// private ArchivingAttributeSubNamesComparator
	// archivingAttributeSubNamesComparator;
	private ArchivingAttributeComparator archivingAttributeComparator;

	// --------- End of attributes data members ----------

	// Add your own data members here
	private ControlResult controlResult;
	private int readType = IDelayManager.READ_ROLLOVER;

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
	TdbArchivingWatcher(DeviceClass cl, String s, String version)
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
	TdbArchivingWatcher(DeviceClass cl, String s, String d, String version)
			throws DevFailed {
		super(cl, s, d);
		m_version = version;
		init_device();
	}

	//
	protected DbDatum getClassProperty(String name) {
		TdbArchivingWatcherClass ds_class = (TdbArchivingWatcherClass) super
				.get_device_class();
		return ds_class.get_class_property(name);
	}

	// =========================================================
	/**
	 * Initialize the device.
	 */
	// =========================================================
	public void init_device() throws DevFailed {
		get_device_property();

		// Initialise variables to default values
		super.initArchivingDataWatch();

		LifeCycleManagerFactory.setWatcherToWarn(this);
		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getImpl(LifeCycleManagerFactory.TDB_LIFE_CYCLE);
		Thread watcherThread = lifeCycleManager.getAsThread();

		this.startLoggingFactory();

		// this.linesComparator = new ControlResultLineComparator ();
		this.archiversComparator = new ArchiversComparator();
		this.domainsComparator = new DomainsComparator();
		// this.archivingAttributeSubNamesComparator = new
		// ArchivingAttributeSubNamesComparator ();
		this.archivingAttributeComparator = new ArchivingAttributeComparator();

		watcherThread.start();

		if (!ArchivingWatch.isDoStartOnInitDevice())// this.doStartOnInitDevice
													// )
		{
			this.warnOff();
		}
	}

	// =========================================================
	/**
	 * Method always executed before command execution.
	 */
	// =========================================================
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
	public void read_attr_hardware(Vector attr_list)
			throws DevFailed {
		if (this.get_state().value() != DevState._FAULT) {
			IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
			ControlResult _controlResult = delayManager
					.getControlResult(IDelayManager.READ_ROLLOVER);
			this.controlResult = _controlResult == null ? null : _controlResult
					.cloneControlResult();
		} else {
			this.controlResult = new ControlResult();
		}
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
	public void read_attr(Attribute attr) throws DevFailed {
		String attr_name = attr.get_name();

		// Switch on attribute name
		if (attr_name.equals("ArchivingHealth")) {
			// Add your own code here
			int code = this.controlResult == null ? ControlResult.NOT_READY
					: this.controlResult.getCode();
			attr.set_value((short) code);
		} else if (attr_name.equals("FormattedReport")) {
			// Add your own code here
			String report = this.controlResult == null ? ControlResult.EMPTY_REPORT
					: this.controlResult.getReport();

			String[] res = new String[1];
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
	private String get_report(ControlResult _controlResult) throws DevFailed {
		String empty = ControlResult.EMPTY_REPORT;
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
	private boolean is_attribute_correctly_archived(String argin,
			ControlResult _controlResult) throws DevFailed {
		if (_controlResult == null) {
			throw new DevFailed(TdbArchivingWatcher.getNotYetReadyError());
		}

		return _controlResult.isAttributeCorrectlyArchived(argin);
	}

	private static DevError[] getNotYetReadyError() {
		ErrSeverity severity = ErrSeverity.WARN;
		String reason = ControlResult.EMPTY_REPORT;
		String desc = ControlResult.EMPTY_REPORT;
		String origin = ControlResult.EMPTY_REPORT;

		DevError devError = new DevError(reason, severity, desc, origin);
		DevError[] ret = new DevError[1];
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
		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getCurrentImpl();
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
	public void start_absolute(int[] argin) throws DevFailed {
		get_logger().info("Entering start_absolute()");

		// ---Add your Own code to control device here ---
		if (argin == null || argin.length != 2) {
			Tools.throwDevFailed(new IllegalArgumentException(
					"Needs 2 int parameters: [ amount , type ]"));
		}

		int amount = argin[0];
		int type = argin[1];
		SaferPeriodCalculatorFactory.getAbsoluteImpl(amount, type);

		this.start();
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
	public void start_relative(double argin) throws DevFailed {
		get_logger().info("Entering start_absolute()");

		// ---Add your Own code to control device here ---
		if (argin <= 0) {
			Tools.throwDevFailed(new IllegalArgumentException(
					"The multiplier has to be > 0"));
		}

		SaferPeriodCalculatorFactory.getRelativeImpl(argin);

		this.start();
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
		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getCurrentImpl();
		lifeCycleManager.stopProcessing();
		// this.warnOff ();
		// ---END

		get_logger().info("Exiting stop()");
	}

	/*
	 * private void goBackToFormerState () { System.out.println (
	 * "CLA/ArchivingWatcher/goBackToFormerState!!!!!!!!!!!!!!!" ); //if (
	 * !this.get_state().get )
	 *
	 * if ( this.formerState != DevState._UNKNOWN ) { System.out.println (
	 * "CLA/ArchivingWatcher/goBackToFormerState/this.formerState/"
	 * +this.formerState
	 * +"/DevState._ALARM/"+DevState._ALARM+"/DevState._ON/"+DevState
	 * ._ON+"/DevState._OFF/"+DevState._OFF); this.set_state ( (short)
	 * this.formerState ); } else { System.out.println (
	 * "CLA/ArchivingWatcher/goBackToFormerState/ON"); this.set_state (
	 * DevState.ON ); } }
	 */

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
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);

		return this.get_error_archivers(_controlResult);
	}

	/**
	 * @param result
	 * @return
	 */
	private String[] get_error_archivers(ControlResult _controlResult) {
		String[] empty = new String[0];
		if (_controlResult == null) {
			return empty;
		}

		Map _errorArchivers = _controlResult.getErrorArchivers();
		String[] argout = new String[_errorArchivers.size()];

		List list = new Vector();
		list.addAll(_errorArchivers.values());
		Collections.sort(list, this.archiversComparator);
		Iterator it = list.iterator();

		int i = 0;
		while (it.hasNext()) {
			Archiver key = (Archiver) it.next();
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
	private String[] get_errors_for_archiver(String argin,
			ControlResult _controlResult) throws DevFailed {
		String[] empty = new String[0];
		if (_controlResult == null) {
			return empty;
		}

		Hashtable _errorArchivers = _controlResult.getErrorArchivers();
		if (_errorArchivers == null) {
			return empty;
		}

		Archiver archiver = (Archiver) _errorArchivers.get(argin);
		if (archiver == null) {
			return empty;
		}

		Hashtable _errorAttributes = archiver.getKOAttributes();
		if (_errorAttributes == null) {
			return empty;
		}

		String[] argout = new String[_errorAttributes.size()];
		List list = new Vector();
		list.addAll(_errorAttributes.values());
		Collections.sort(list, this.archivingAttributeComparator);
		Iterator it = list.iterator();
		int i = 0;

		while (it.hasNext()) {
			ArchivingAttribute key = (ArchivingAttribute) it.next();
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
		if (this.get_state().value() != DevState._ALARM) {
			// do nothing
			return;
		}

		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getCurrentImpl();
		boolean isProcessing = lifeCycleManager.isProcessing();
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
		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
				.getImpl(LifeCycleManagerFactory.TDB_LIFE_CYCLE);
		lifeCycleManager.stopProcessing();

		Thread watcherThread = lifeCycleManager.getAsThread();
		try {
			watcherThread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Tools.throwDevFailed(e);
		}

		// restarting
		this.init_device();

		boolean isProcessing = lifeCycleManager.isProcessing();
		if (isProcessing) {
			this.set_state((short) DevState._ON);
		} else {
			this.set_state((short) DevState._OFF);
		}

		get_logger().info("Exiting restart()");
	}

	private String[] get_error_domains(ControlResult _controlResult)
			throws DevFailed {
		String[] empty = new String[0];
		if (_controlResult == null) {
			return empty;
		}

		Hashtable _errorDomains = _controlResult.getErrorDomains();
		String[] argout = new String[_errorDomains.size()];

		List list = new Vector();
		list.addAll(_errorDomains.values());
		Collections.sort(list, this.domainsComparator);
		Iterator it = list.iterator();

		int i = 0;
		while (it.hasNext()) {
			Domain key = (Domain) it.next();
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
	private String[] get_errors_for_domain(String argin,
			ControlResult _controlResult) throws DevFailed {
		String[] empty = new String[0];
		if (_controlResult == null) {
			return empty;
		}

		Hashtable _errorDomains = _controlResult.getErrorDomains();
		if (_errorDomains == null) {
			return empty;
		}

		Domain domain = (Domain) _errorDomains.get(argin);
		if (domain == null) {
			return empty;
		}

		Hashtable _errorAttributes = domain.getKOAttributes();
		if (_errorAttributes == null) {
			return empty;
		}

		String[] argout = new String[_errorAttributes.size()];

		List list = new Vector();
		list.addAll(_errorAttributes.values());
		Collections.sort(list, this.archivingAttributeComparator);
		Iterator it = list.iterator();

		int i = 0;
		while (it.hasNext()) {
			ArchivingAttribute key = (ArchivingAttribute) it.next();
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
	private String[] get_errors_for_attribute(String argin,
			ControlResult _controlResult) throws DevFailed {
		String[] empty = new String[0];
		if (_controlResult == null) {
			return empty;
		}

		Hashtable _errorAttributeSubNames = _controlResult
				.getErrorSubAttributes();
		if (_errorAttributeSubNames == null) {
			return empty;
		}

		ArchivingAttributeSubName attributeSubName = (ArchivingAttributeSubName) _errorAttributeSubNames
				.get(argin);
		if (attributeSubName == null) {
			return empty;
		}

		Hashtable _errorAttributes = attributeSubName.getKOAttributes();
		if (_errorAttributes == null) {
			return empty;
		}

		String[] argout = new String[_errorAttributes.size()];

		List list = new Vector();
		list.addAll(_errorAttributes.values());
		Collections.sort(list, this.archivingAttributeComparator);
		Iterator it = list.iterator();

		int i = 0;
		while (it.hasNext()) {
			ArchivingAttribute key = (ArchivingAttribute) it.next();
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
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);
		// ControlResult _controlResult = delayManager.getControlResult (
		// IDelayManager.READ_ROLLOVER );

		return this.get_report(_controlResult);
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
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.get_report(_controlResult);
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
	public boolean is_attribute_correctly_archived_current(
			String argin) throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);

		return this.is_attribute_correctly_archived(argin, _controlResult);
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
	public boolean is_attribute_correctly_archived_latest_error(
			String argin) throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.is_attribute_correctly_archived(argin, _controlResult);
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
	public String[] get_error_archivers_latest_error()
			throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.get_error_archivers(_controlResult);
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
	public String[] get_errors_for_archiver_current(String argin)
			throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);

		return this.get_errors_for_archiver(argin, _controlResult);
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
	public String[] get_errors_for_archiver_latest_error(
			String argin) throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.get_errors_for_archiver(argin, _controlResult);
	}

	// =========================================================
	/**
	 * Execute command "GetAllArchivingAttributes" on device. Lists all TDB
	 * archiving attributes (does a new request regardless of the watcher
	 * cycle).
	 *
	 * @return The list of all TDB archiving attributes.
	 */
	// =========================================================
	public String[] get_all_archiving_attributes()
			throws DevFailed {
		String[] ret = null;

		try {
			Hashtable<String, ModeData> all = this.dbReader.getArchivedAttributes();
			if (all != null) {
				ret = new String[all.size()];
			} else {
				return null;
			}

			ControlResult cr = new ControlResult();
			cr.setAllArchivingAttributes(all);
			ControlResultLine[] orderedLines = cr.sort();

			for (int i = 0; i < all.size(); i++) {
				ret[i] = orderedLines[i].getAttribute().getCompleteName();
				Mode mode = this.dbReader.getModeForAttribute(ret[i]);
				if (mode != null) {
					ret[i] += ": " + mode.toStringWatcher();
				}
			}
		} catch (Exception e) {
			this.logger.trace(ILogger.LEVEL_ERROR,
					"get_all_archiving_attributes/error! VVVVVVVVVVVVVVVVV");
			this.logger.trace(ILogger.LEVEL_ERROR, e);
			this.logger.trace(ILogger.LEVEL_ERROR,
					"get_all_archiving_attributes/error! ^^^^^^^^^^^^^^^^^");
			Tools.throwDevFailed(e);
		}

		return ret;
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
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);

		return this.get_error_domains(_controlResult);
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
	public String[] get_error_domains_latest_error()
			throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.get_error_domains(_controlResult);
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
	public String[] get_errors_for_domain_current(String argin)
			throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);

		return this.get_errors_for_domain(argin, _controlResult);
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
	public String[] get_errors_for_domain_latest_error(String argin)
			throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.get_errors_for_domain(argin, _controlResult);
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
	public String[] get_errors_for_attribute_current(String argin)
			throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_ROLLOVER);

		return this.get_errors_for_attribute(argin, _controlResult);
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
	public String[] get_errors_for_attribute_latest_error(
			String argin) throws DevFailed {
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl();
		ControlResult _controlResult = delayManager
				.getControlResult(IDelayManager.READ_LATEST_BAD_CYCLE);

		return this.get_errors_for_attribute(argin, _controlResult);
	}

	// =========================================================
	/**
	 * main part for the device server class
	 */
	// =========================================================
	public static void main(String[] argv) {
		try {
			Util tg = Util.init(argv, "TdbArchivingWatcher");
			tg.server_init();

			System.out.println("Ready to accept request");

			tg.server_run();
		}

		catch (OutOfMemoryError ex) {
			System.err.println("Can't allocate memory !!!!");
			System.err.println("Exiting");
		} catch (UserException ex) {
			Except.print_exception(ex);

			System.err.println("Received a CORBA user exception");
			System.err.println("Exiting");
		} catch (SystemException ex) {
			Except.print_exception(ex);

			System.err.println("Received a CORBA system exception");
			System.err.println("Exiting");
		}

		System.exit(-1);
	}

	/**
	 *
	 * @return Watcher Archiving Report
	 */
	public void start_archiving_report() throws DevFailed {
		// TODO Auto-generated method stub
		try {
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
				return;
			}
			this.dbReader.startArchivingReport();
		} catch (DevFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 *
	 * @return Watcher database Report
	 */
	public String[] get_database_report() {
		// TODO Auto-generated method stub
		try {
			if (this.dbReader.getDatabase().getType() == ConfigConst.HDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}
			return this.dbReader.getDatabaseReport();
		} catch (DevFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @return Watcher attributes count
	 */
	public int get_current_archiving_attributes_count()
			throws DevFailed {
		// TODO Auto-generated method stub

		try {
			return this.dbReader.getDatabase().getMode()
					.getCurrentArchivedAttCount();
		} catch (ArchivingException e) {
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
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}
			return this.dbReader.getDatabase().getDbUtil()
					.getAttributesCountOkOrKo(false);
		} catch (ArchivingException e) {
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
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}
			return this.dbReader.getDatabase().getDbUtil()
					.getAttributesCountOkOrKo(true);
		} catch (ArchivingException e) {
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
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}

			return this.dbReader.getDatabase().getDbUtil().getKoAttributes();
		} catch (ArchivingException e) {
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
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}

			return this.dbReader.getDatabase().getDbUtil()
					.getListOfPartitions();
		} catch (ArchivingException e) {
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
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}
			String[] res = { "No Job Status" };

			String[] tmp = this.dbReader.getDatabase().getDbUtil()
					.getListOfJobStatus();
			if (tmp == null)
				return res;
			else
				return tmp;
		} catch (ArchivingException e) {
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
		//  Auto-generated method stub

		try {
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}
			String[] res = { "No Job Errors" };
			String[] tmp = this.dbReader.getDatabase().getDbUtil()
					.getListOfJobErrors();
			if (tmp == null)
				return res;
			else
				return tmp;
		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * get ko attributes by archiver device
	 *
	 * @return
	 */
	public String[] get_ko_attributes_count_by_device()
			throws DevFailed {
		// TODO Auto-generated method stub

		try {
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}

			return this.dbReader.getDatabase().getDbUtil()
					.getKOAttrCountByDevice();

		} catch (ArchivingException e) {
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
			if (this.dbReader.getDatabase().getType() == ConfigConst.TDB_MYSQL) {
				Except.throw_exception("DATABASE_ERROR", "MySql Exception",
						"MySql is not supported for this command ");
			}

			return this.dbReader.getDatabase().getDbUtil()
					.getFeedAliveProgression();

		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	// =========================================================

	private void startLoggingFactory() {
		// System.out.println (
		// "CLA/startLoggingFactory/this.device_name/"+this.device_name+"/hashCode/"+this.hashCode
		// ()+"/this.hasDiary/"+this.hasDiary );

		ILogger _logger = LoggerFactory.getImpl(LoggerFactory.DEFAULT_TYPE,
				this.device_name, ArchivingWatch.getDiaryPath(), ArchivingWatch
						.isHasDiary());
		_logger.setTraceLevel(ArchivingWatch.getDiaryLogLevel());

		this.logger = _logger;
	}

	@Override
	public void delete_device() throws DevFailed {
		// TODO Auto-generated method stub

	}

}

// --------------------------------------------------------------------------
/*
 * end of $Source:
 * /cvsroot/tango-cs/tango/jserver/archiving/TdbArchivingWatcher/
 * TdbArchivingWatcher.java,v $
 */
