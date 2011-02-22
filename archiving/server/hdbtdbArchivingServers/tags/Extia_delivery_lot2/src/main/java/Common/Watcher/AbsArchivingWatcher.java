package Common.Watcher;

import java.sql.Timestamp;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoDs.DeviceClass;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.ArchivingWatch;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.DBReaderFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.IDBReader;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.ISaferPeriodCalculator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

public abstract class AbsArchivingWatcher extends DeviceImpl implements
		TangoConst, Warnable {

	// --------- Start of properties data members ----------

	/**
	 * The minimum number of attributes in a control step. Not used in the
	 * current device implementation. <b>Default value : </b>10
	 */
	private short minPacketSize = 10;
	/**
	 * The maximum number of attributes in a control step. Not used in the
	 * current device implementation. <b>Default value : </b>100
	 */
	private short maxPacketSize = 100;
	/**
	 * Used to tell the device if it should diagnose KO archivers (archivers
	 * that have at least 1 KO attribute). If set to true, the archivers part of
	 * the generated control report will contain specific archiver information
	 * such as scalar load etc..<br>
	 * If set to false, those details will remain blank.<br>
	 * Setting the property to true will increase the database load. <b>Default
	 * value : </b>false
	 */
	private boolean doArchiverDiagnosis = false;
	/**
	 * Used to tell the device if it should ask the archiver to retry archiving
	 * KO attributes. If set to true, the device automatically calls the
	 * HDBArchiver command "retryForAttributes" for KO attributes.<br>
	 * If set to false, it doesn't.<br>
	 * <b>Default value : </b>false
	 */
	private boolean doRetry = false;
	/**
	 * Describes the device start-up sequence. If set to true, the device
	 * automatically starts controlling on initialisation; with default
	 * parameters (as if the "start" command was called)<br>
	 * If set to false, the device waits in standby after being initialised.<br>
	 * <b>Default value : </b>false
	 */
	private boolean doStartOnInitDevice = false;
	/**
	 * The duration of a full control cycle in seconds. <b>Default value :
	 * </b>14400 (4 hours)
	 */
	private int macroPeriod = 14400;

	/**
	 * Computer identifier on wich is settled the database DB. The identifier
	 * can be the computer name or its IP address. <br>
	 * <b>Default value : </b> hdb
	 */
	private String hdbHost;
	/**
	 * Database name.<br>
	 * <b>Default value : </b> hdb
	 */
	private String hdbName;

	/**
	 * Computer identifier on wich is settled the database TDB. The identifier
	 * can be the computer name or its IP address. <br>
	 * <b>Default value : </b> tdb
	 */
	private String tdbHost;

	/**
	 * Database name.<br>
	 * <b>Default value : </b> tdb
	 */
	private String tdbName;

	/**
	 * User identifier (name) used to connect the database HDB. <br>
	 * <b>Default value : </b> hdb
	 */
	private String hdbUser;
	/**
	 * User identifier (name) used to connect the database TDB.<br>
	 * <b>Default value : </b> tdb
	 */
	private String tdbUser;
	/**
	 * Password used to connect the database HDB. <br>
	 * <b>Default value : </b> hdb
	 */
	private String hdbPwd;
	/**
	 * Password used to connect the database TDB.<br>
	 * <b>Default value : </b> tdb
	 */
	private String tdbPwd;

	/**
	 * Describes the default safety period calculation method, which will be
	 * used if the default start command is called.<br>
	 * <b>Default value : </b> absolute/minutes/15
	 */
	private String defaultSafetyPeriod = "absolute/minutes/15";

	/**
	 * Defines whether the archiver will log to a diary. <b>Default value :
	 * </b>false
	 */
	private boolean hasDiary = false;

	/**
	 * The criticity threshold of the archiver's logging. Only messages with a
	 * criticity higher than this attribute will be logged to the diary.
	 * Possible values are:
	 * <UL>
	 * <LI>DEBUG
	 * <LI>INFO
	 * <LI>WARNING
	 * <LI>ERROR
	 * <LI>CRITIC
	 * </UL>
	 * <b>Default value : </b>DEBUG
	 */
	private String diaryLogLevel = "DEBUG";

	/**
	 * The path of the diary logs. Don't include a filename, diary files are
	 * named automatically.
	 */
	private String diaryPath;
	/**
	 * true if the ORACLE RAC connection is activated. This information is
	 * appended to all device's (or attributes) name. false otherwise.<br>
	 * <b>Default value : </b> false
	 */
	private boolean hdbRacConnection = false;
	private boolean tdbRacConnection = false;

	/**
	 * It defines the minimum number of KO attributes which put the device in
	 * ALARM state <b>Default value : </b> 100
	 */
	private short minNumberOfKoForAlarmState = 100;
	// --------- End of properties data members ----------

	// --------- Start of attributes data members ----------

	protected IDBReader dbReader;
	private String latestFaultMessage = "";
	private int faultsCounter = 0;

	// Add your own data members here
	protected ILogger logger;

	// --------- End of attributes data members ----------

	public AbsArchivingWatcher(DeviceClass arg0, String arg1) throws DevFailed {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public AbsArchivingWatcher(DeviceClass arg0, String arg1, String arg2)
			throws DevFailed {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void init_device() throws DevFailed;

	// A l'avenir faire une interface avec la WatcherClass pour pouvoir utiliser
	// la méthode
	// get_class_property(string) directement
	protected abstract DbDatum getClassProperty(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see archwatch.tools.Warnable#setStatus(java.lang.String)
	 */
	@Override
	public synchronized void setStatus(String status) {
		super.set_status(status);
	}

	@Override
	public void logIntoDiary(int level, Object o) {
		/*
		 * if ( this.logger != null ) { this.logger.trace ( level , o ); }
		 */
		this.logger.trace(level, o);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.soleil.TangoArchiving.ArchivingWatchApi.devicelink.Warnable#trace(
	 * java.lang.String, int)
	 */
	public synchronized void trace(String msg, int level) throws DevFailed {
		switch (level) {
		case Warnable.LOG_LEVEL_DEBUG:
			get_logger().debug(msg);
			break;

		case Warnable.LOG_LEVEL_INFO:
			get_logger().info(msg);
			break;

		case Warnable.LOG_LEVEL_WARN:
			get_logger().warn(msg);
			break;

		case Warnable.LOG_LEVEL_ERROR:
			get_logger().error(msg);
			break;

		case Warnable.LOG_LEVEL_FATAL:
			get_logger().fatal(msg);
			break;

		default:
			Tools
					.throwDevFailed(new IllegalArgumentException(
							"Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got "
									+ level + "instead."));
		}

	}

	@Override
	public synchronized void trace(String msg, Throwable t, int level)
			throws DevFailed {
		switch (level) {
		case Warnable.LOG_LEVEL_DEBUG:
			get_logger().debug(msg, t);
			break;

		case Warnable.LOG_LEVEL_INFO:
			get_logger().info(msg, t);
			break;

		case Warnable.LOG_LEVEL_WARN:
			get_logger().warn(msg, t);
			break;

		case Warnable.LOG_LEVEL_ERROR:
			get_logger().error(msg, t);
			break;

		case Warnable.LOG_LEVEL_FATAL:
			get_logger().fatal(msg, t);
			break;

		default:
			Tools
					.throwDevFailed(new IllegalArgumentException(
							"Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got "
									+ level + "instead."));
		}

	}

	/**
	 * Overrides set_state so that formerState is stored when the device state
	 * changes
	 * 
	 * @param _in
	 */
	public synchronized void set_state(DevState in) {
		this.set_state((short) in.value());
	}

	/**
	 * Overrides set_state so that formerState is stored when the device state
	 * changes
	 * 
	 * @param _state
	 */
	public synchronized void set_state(short _state) {
		super.set_state(DevState.from_int(_state));

		String _status = this.formatStatus(_state);
		this.setStatus(_status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see archwatch.tools.WarnAble#warnAlarm()
	 */
	@Override
	public synchronized void warnAlarm() {
		if (this.get_state().value() == DevState._ALARM)
			return;

		this.set_state((short) DevState._ALARM);

		String message = "At :" + this.now();
		message += Tools.CRLF;
		message += this.get_status();

		this.setStatus(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see archwatch.strategy.delay.WarnAble#warnFault()
	 */
	@Override
	public synchronized void warnFault(DevFailed t) {
		System.out.println("CLA/ArchivingWatcher/warnFault");

		this.set_state((short) DevState._FAULT);

		String faultMessage = "At :" + this.now();
		faultMessage += Tools.CRLF;
		faultMessage += Tools.getCompleteMessage(t);
		this.setLatestFaultMessage(faultMessage);

		String message = this.get_status();
		message += Tools.CRLF;
		message += faultMessage;

		this.setStatus(message);
	}

	@Override
	public synchronized void warnInit() {
		switch (this.get_state().value()) {
		case DevState._ALARM:
			// do nothing
			return;
		}

		this.set_state((short) DevState._INIT);

		String message = "At :" + this.now();
		message += Tools.CRLF;
		message += this.get_status();

		this.setStatus(message);
	}

	@Override
	public synchronized void warnOff() {
		// this.formerState = DevState._OFF;

		switch (this.get_state().value()) {
		case DevState._ALARM:
			// do nothing
			return;

		case DevState._FAULT:
			// do nothing
			return;
		}

		this.set_state((short) DevState._OFF);

		String message = "At :" + this.now();
		message += Tools.CRLF;
		message += this.get_status();

		this.setStatus(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see archwatch.strategy.delay.Warnable#warn()
	 */
	@Override
	public synchronized void warnOn() {
		// this.formerState = DevState._ON;

		/*
		 * switch ( this.get_state().value() ) { case DevState._ALARM: //do
		 * nothing return; }
		 */

		if (this.get_state().value() == DevState._ON)
			return;

		this.dbReader = DBReaderFactory.getCurrentImpl();
		this.set_state((short) DevState._ON);

		String message = "At :" + this.now();
		message += Tools.CRLF;
		message += this.get_status();

		this.setStatus(message);
	}

	public String get_status() {
		String ret = super.get_status();

		if (this.get_state().value() != DevState._FAULT) {
			ret += this.getLatestFaultMessage();
		}

		return ret;
	}

	// =========================================================
	protected void initLatestFaultData() {
		this.latestFaultMessage = "";
		this.faultsCounter = 0;
	}

	// ===================================================================
	/**
	 * Read the device properties from database.
	 */
	// ===================================================================
	protected void get_device_property() throws DevFailed {
		// Initialize your default values here.
		// ------------------------------------------

		// Read device properties from database.(Automatic code generation)
		// -------------------------------------------------------------
		if (Util._UseDb == false)
			return;
		String[] propnames = { "MinPacketSize", "MaxPacketSize",
				"DoArchiverDiagnosis", "DoRetry", "DoStartOnInitDevice",
				"MacroPeriod", "HdbHost", "HdbName", "TdbHost", "TdbName",
				"HdbUser", "TdbUser", "HdbPwd", "TdbPwd",
				"DefaultSafetyPeriod", "HasDiary", "DiaryPath",
				"DiaryLogLevel", "HdbRacConnection", "TdbRacConnection",
				"MinNumberOfKoForAlarm" };

		// Call database and extract values
		// --------------------------------------------
		DbDatum[] dev_prop = get_db_device().get_property(propnames);
		int i = -1;
		// Extract MinPacketSize value
		if (dev_prop[++i].is_empty() == false)
			minPacketSize = dev_prop[i].extractShort();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				minPacketSize = cl_prop.extractShort();
		}

		// Extract MaxPacketSize value
		if (dev_prop[++i].is_empty() == false)
			maxPacketSize = dev_prop[i].extractShort();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				maxPacketSize = cl_prop.extractShort();
		}

		// Extract DoArchiverDiagnosis value
		if (dev_prop[++i].is_empty() == false)
			doArchiverDiagnosis = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				doArchiverDiagnosis = cl_prop.extractBoolean();
		}

		// Extract DoRetry value
		if (dev_prop[++i].is_empty() == false)
			doRetry = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				doRetry = cl_prop.extractBoolean();
		}

		// Extract doStartOnInitDevice value
		if (dev_prop[++i].is_empty() == false)
			doStartOnInitDevice = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				doStartOnInitDevice = cl_prop.extractBoolean();
		}

		// Extract MacroPeriod value
		if (dev_prop[++i].is_empty() == false)
			macroPeriod = dev_prop[i].extractLong();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				macroPeriod = cl_prop.extractLong();
		}
		// Extract HDBHost value
		if (dev_prop[++i].is_empty() == false)
			hdbHost = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				hdbHost = cl_prop.extractString();
		}

		// Extract HDbName value
		if (dev_prop[++i].is_empty() == false)
			hdbName = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				hdbName = cl_prop.extractString();
		}

		// Extract TDBHost value
		if (dev_prop[++i].is_empty() == false)
			tdbHost = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				tdbHost = cl_prop.extractString();
		}

		// Extract TDbName value
		if (dev_prop[++i].is_empty() == false)
			tdbName = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				tdbName = cl_prop.extractString();
		}

		// Extract HdbUser value
		if (dev_prop[++i].is_empty() == false)
			hdbUser = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				hdbUser = cl_prop.extractString();
		}

		// Extract TdbUser value
		if (dev_prop[++i].is_empty() == false)
			tdbUser = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				tdbUser = cl_prop.extractString();
		}

		// Extract HdbPwd value
		if (dev_prop[++i].is_empty() == false)
			hdbPwd = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				hdbPwd = cl_prop.extractString();
		}

		// Extract TdbPwd value
		if (dev_prop[++i].is_empty() == false)
			tdbPwd = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				tdbPwd = cl_prop.extractString();
		}

		// Extract DefaultSafetyPeriod value
		if (dev_prop[++i].is_empty() == false)
			defaultSafetyPeriod = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				defaultSafetyPeriod = cl_prop.extractString();
		}

		// Extract hasDiary value
		if (dev_prop[++i].is_empty() == false)
			hasDiary = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				hasDiary = cl_prop.extractBoolean();
		}

		// Extract diaryPath value
		if (dev_prop[++i].is_empty() == false)
			diaryPath = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				diaryPath = cl_prop.extractString();
		}

		// Extract diaryLogLevel value
		if (dev_prop[++i].is_empty() == false)
			diaryLogLevel = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				diaryLogLevel = cl_prop.extractString();
		}

		// Extract HdbRacConnection value
		if (dev_prop[++i].is_empty() == false)
			hdbRacConnection = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				hdbRacConnection = cl_prop.extractBoolean();
		}

		// Extract TdbRacConnection value
		if (dev_prop[++i].is_empty() == false)
			tdbRacConnection = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				tdbRacConnection = cl_prop.extractBoolean();
		}

		// Extract minNumberOfKoForAlarmState value
		if (dev_prop[++i].is_empty() == false)
			minNumberOfKoForAlarmState = dev_prop[i].extractShort();
		else {
			// Try to get value from class property
			DbDatum cl_prop = getClassProperty(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				minNumberOfKoForAlarmState = cl_prop.extractShort();
		}

		get_logger().info("minPacketSize = " + minPacketSize);
		get_logger().info("maxPacketSize = " + maxPacketSize);
		get_logger().info("doArchiverDiagnosis = " + doArchiverDiagnosis);
		get_logger().info("doRetry = " + doRetry);
		get_logger().info("doStartOnInitDevice = " + doStartOnInitDevice);
		get_logger().info("macroPeriod = " + macroPeriod);
		get_logger().info("hdbHost = " + hdbHost);
		get_logger().info("hdbName = " + hdbName);
		get_logger().info("hdbUser = " + hdbUser);
		get_logger().info("hdbPwd = " + hdbPwd);
		get_logger().info("tdbHost = " + tdbHost);
		get_logger().info("tdbName = " + tdbName);
		get_logger().info("tdbUser = " + tdbUser);
		get_logger().info("tdbPwd = " + tdbPwd);
		get_logger().info("hasDiary = " + hasDiary);
		get_logger().info("diaryPath = " + diaryPath);
		get_logger().info("diaryLogLevel = " + diaryLogLevel);
		get_logger().info("hdbRacConnection = " + hdbRacConnection);
		get_logger().info("tdbRacConnection = " + tdbRacConnection);
		get_logger().info(
				"MinNumberOfKoForAlarmState = " + minNumberOfKoForAlarmState);

		//
		// End of Automatic code generation
		// -------------------------------------------------------------

	}

	protected void initArchivingDataWatch() {
		ArchivingWatch.setHDBHost(this.hdbHost);
		ArchivingWatch.setHDBName(this.hdbName);
		ArchivingWatch.setTDBHost(this.tdbHost);
		ArchivingWatch.setTDBName(this.tdbName);

		ArchivingWatch.setHDBuser(this.hdbUser);
		ArchivingWatch.setHDBpassword(this.hdbPwd);
		ArchivingWatch.setTDBuser(this.tdbUser);
		ArchivingWatch.setTDBpassword(this.tdbPwd);
		ArchivingWatch.setMacroPeriod(this.macroPeriod);
		ArchivingWatch.setDoArchiverDiagnosis(this.doArchiverDiagnosis);
		ArchivingWatch.setDoRetry(this.doRetry);
		ArchivingWatch.setDoStartOnInitDevice(this.doStartOnInitDevice);
		ArchivingWatch.setDefaultSafetyPeriod(this.defaultSafetyPeriod);
		ArchivingWatch.setHasDiary(this.hasDiary);
		ArchivingWatch.setDiaryPath(this.diaryPath);
		ArchivingWatch.setDiaryLogLevel(this.diaryLogLevel);
		ArchivingWatch.setHDBRac(this.hdbRacConnection);
		ArchivingWatch.setTDBRac(this.tdbRacConnection);
		ArchivingWatch
				.setMinNumberOfKoForAlarmState(this.minNumberOfKoForAlarmState);
	}

	// =========================================================
	private String now() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return now + "";
	}

	/**
	 * @param i
	 */
	private String formatStatus(int _state) {
		ISaferPeriodCalculator saferPeriodCalculator = SaferPeriodCalculatorFactory
				.getCurrentImpl();
		String currentModeDescription = saferPeriodCalculator == null ? "No description available"
				: saferPeriodCalculator.getDescription();

		String _status;
		switch (_state) {
		case DevState._ALARM:
			_status = "Archiving problems have been detected.";
			break;

		case DevState._FAULT:
			_status = "This device isn't working properly.";
			break;

		case DevState._INIT:
			_status = "No control step has been completed yet. Please wait.";
			break;

		case DevState._OFF:
			_status = "This device is waiting.";
			break;

		case DevState._ON:
			_status = "This device is running normally.";
			break;

		default:
			_status = "Unknown";
			break;
		}

		String ret = _status + Tools.CRLF + "Current mode: "
				+ currentModeDescription;
		return ret;
	}

	/**
	 * @param latestFaultMessage
	 *            The latestFaultMessage to set.
	 */
	private void setLatestFaultMessage(String latestFaultMessage) {
		this.latestFaultMessage = latestFaultMessage;
		this.faultsCounter++;
	}

	/**
	 * @return Returns the latestFaultMessage.
	 */
	private String getLatestFaultMessage() {
		if (this.latestFaultMessage == null
				|| this.latestFaultMessage.equals("")) {
			return "";
		} else {
			String ret = Tools.CRLF;
			ret += Tools.CRLF;
			ret += "Latest fault: ";
			ret += Tools.CRLF;
			ret += this.latestFaultMessage;
			ret += Tools.CRLF;
			ret += "Number of times the device has been in fault state: ";
			ret += faultsCounter;

			return ret;
		}
	}

}
