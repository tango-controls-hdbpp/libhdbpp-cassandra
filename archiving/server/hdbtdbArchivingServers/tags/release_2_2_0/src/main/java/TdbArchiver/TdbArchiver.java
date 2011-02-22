//+============================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/TdbArchiver.java,v $
//
// project :     Tango Device Server
//
// Description: java source code for the TdbArchiver class and its commands.
//              This class is derived from DeviceImpl class.
//              It represents the CORBA servant obbject which
//              will be accessed from the network. All commands which
//              can be executed on the TdbArchiver are implemented
//              in this file.
//
// $Author: pierrejoseph $
//
// $Revision: 1.85 $
//
// $Log: TdbArchiver.java,v $
// Revision 1.85  2007/08/27 14:14:35  pierrejoseph
// Traces addition : the logger object is stored in the TdbCollectorFactory
//
// Revision 1.84  2007/06/15 14:27:41  pierrejoseph
// add traces in trigger to locate the collector freezing
//
// Revision 1.83  2007/06/04 11:51:24  pierrejoseph
// logger info addition in trigger_archive_conf
//
// Revision 1.82  2007/06/04 10:36:53  pierrejoseph
// logger info addition
//
// Revision 1.81  2007/05/25 12:06:54  pierrejoseph
// system.out suppressions
// update trigger_archive_conf if the insertModeRecord raised an exception (aligned on Hdb behaviour)
//
// Revision 1.80  2007/05/11 13:58:55  pierrejoseph
// Attribute addition : release version
//
// Revision 1.79  2007/05/10 16:23:45  chinkumo
// Archiving Version 1.2.0  for Tango 5.5.8
//
// Revision 1.78  2007/04/24 14:29:28  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.77  2007/04/05 10:06:03  chinkumo
// New Release 5.5.7
//
// Revision 1.76  2007/03/27 09:17:52  ounsy
// move the AMT updating upwards in stop_archive_att so that the archiving can be stopped even if no collector was created (as can happen for reserved attributes of formerly-dedicated archivers).
//
// Revision 1.75  2007/03/05 16:25:19  ounsy
// non-static DataBase
//
// Revision 1.74  2007/02/27 15:32:21  ounsy
// added a bit of test code in read_attr
//
// Revision 1.73  2007/02/26 16:14:24  ounsy
// archiving devices now inherits just from DeviceImpl instead of
// DeviceImplWithShutdownRunnable (they're nonlonger unexported onn shutdown)
//
// Revision 1.72  2007/02/26 08:31:47  chinkumo
// For Tango 5.5.6
//
// Revision 1.71  2007/02/19 13:01:45  pierrejoseph
// Add a trace in case of insertion error in amt table.
//
// Revision 1.70  2007/02/19 09:06:26  chinkumo
// Release 19 - 02 - 2007 for Tango 5.5.6
//
// Revision 1.69  2007/02/01 13:52:41  pierrejoseph
// minor changes
//
// Revision 1.68  2007/01/11 07:36:07  chinkumo
// Release / 2007-01-11
//
// Revision 1.67  2007/01/09 16:44:57  ounsy
// commands that lock the device into the RUNNING state now set it back to ON even if they encountered exceptions. in this case an entry is logged into the archiver's diary.
//
// Revision 1.66  2007/01/09 15:16:04  ounsy
// put the Giacomo version back for XDBCollectorFactory.removeAllForAttribute
//
// Revision 1.65  2007/01/05 12:56:35  pierrejoseph
// Modification of the ArchivingMessConfig object creation and the trigger_archive_conf method argin has lost its first value.
//
// Revision 1.64  2007/01/04 08:48:02  chinkumo
// Production of the dated version 2007-01-04
//
// Revision 1.63  2006/12/08 07:45:13  chinkumo
// date of the version : 2006-12-08
//
// Revision 1.62  2006/11/24 14:54:16  ounsy
// we re-use the old removeAllForAttribute
//
// Revision 1.61  2006/11/24 14:04:16  ounsy
// the diary entry in case of missing collector is now done by he archiver
//
// Revision 1.60  2006/11/24 13:19:35  ounsy
// TdbCollectorFactory.get(attrLightMode) has a new parameter "doCreateCollectorIfMissing". A missing collector will only be created if this is true, otherwise a message in logged into the archiver's diary and an exception launched
//
// Revision 1.59  2006/11/20 09:24:30  ounsy
// minor changes
//
// Revision 1.58  2006/11/16 11:11:18  pierrejoseph
// Version : 2006-11-16
//
// Revision 1.57  2006/11/15 15:48:38  ounsy
// added the Giacomo correction of removeAllForAttribute
//
// Revision 1.56  2006/11/13 15:57:36  ounsy
// all java devices now inherit from UnexportOnShutdownDeviceImpl instead of from DeviceImpl
//
// Revision 1.55  2006/10/19 12:22:52  ounsy
// modified export_data2_db() to take a new parameter isAsynchronous= false
//
// Revision 1.52  2006/10/13 15:00:20  ounsy
// release date updated
//
// Revision 1.51  2006/10/12 15:08:46  ounsy
// removed a test on whether the archiver is "archiving/tdbarchiver/45" in trigger_archive_conf
//
// Revision 1.50  2006/10/11 08:44:15  ounsy
// release date updated
//
// Revision 1.49  2006/10/11 08:31:50  ounsy
// modified the trigger_archive_conf commands to accept the same parameters as the archiving manager
//
// Revision 1.48  2006/10/09 13:55:34  ounsy
// removed logs
//
// Revision 1.47  2006/10/05 15:42:20  ounsy
// added archivingMessConfig.filter in trigger_archive_conf
//
// Revision 1.46  2006/09/18 09:05:01  ounsy
// minor chages
//
// Revision 1.45  2006/09/15 08:56:54  ounsy
// release date updated
//
// Revision 1.44  2006/09/13 09:49:43  ounsy
// corrected a bug in getDeviceproperty
//
// Revision 1.43  2006/09/12 13:01:42  ounsy
// methods that put the archiver in the RUNNING state now do it from the start and only go to the ON state at the end of the method
//
// Revision 1.42  2006/09/08 14:17:45  ounsy
// added missing descriptions of properties, removed obsolete properties,
//
// Revision 1.41  2006/08/29 15:15:12  ounsy
// release date updated
//
// Revision 1.40  2006/08/10 09:28:28  ounsy
// release date updated
//
// Revision 1.39  2006/08/08 12:22:39  ounsy
// release date updated
//
// Revision 1.38  2006/07/31 08:01:23  ounsy
// release date updated
//
// Revision 1.37  2006/07/31 08:00:55  ounsy
// release date updated
//
// Revision 1.36  2006/07/28 13:50:40  ounsy
// corrected the method name in logRetry
//
// Revision 1.35  2006/07/28 09:33:39  ounsy
// added diary logging on retryForXXXmethods
//
// Revision 1.34  2006/07/26 08:44:19  ounsy
// TdbCollectorFactory no more static
//
// Revision 1.33  2006/07/26 08:35:43  ounsy
// initDevice synchronized
//
// Revision 1.32  2006/07/25 13:22:32  ounsy
// added a "version" attribute
//
// Revision 1.31  2006/07/25 09:47:16  ounsy
// changed the XXX_charge management
//
// Revision 1.30  2006/07/21 14:39:02  ounsy
// minor changes
//
// Revision 1.29  2006/07/18 15:11:55  ounsy
// added a diaryLogLevel property
//
// Revision 1.28  2006/07/18 08:02:33  ounsy
// export_data2_db() now returns the table name
//
// Revision 1.27  2006/06/29 08:45:10  ounsy
// added a hasDiary property (default false) that has to be set to true for the logging to do anything
//
// Revision 1.26  2006/06/27 08:35:05  ounsy
// Corrected a grave bug in logging by removing the "synchronized" tag from the trace methods, that was causing deadlocks
//
// Revision 1.25  2006/06/16 09:25:32  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.24  2006/06/13 13:30:04  ounsy
// minor changes
//
// Revision 1.23  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.22  2006/06/07 12:57:02  ounsy
// minor changes
//
// Revision 1.21  2006/06/02 08:40:00  ounsy
// now exends Warnable so that API classes can warn the required TdbArchiver device
//
// Revision 1.20  2006/05/30 12:37:00  ounsy
// added a cleanOldFiles property
//
// Revision 1.19  2006/05/23 12:01:20  ounsy
// +added a hasThreadedStartup which if true does the AMT inserts in a separate thread
//
// Revision 1.18  2006/04/11 09:12:55  ounsy
// double archiving protection
//
// Revision 1.17  2006/03/29 10:20:51  ounsy
// Added content to the error message:
// each atribute with an invalid mode has a specific log
//
// Revision 1.16  2006/03/27 13:51:09  ounsy
// catched the ArchivingException on checkMode so that if an attribute's mode
// is invalid, the other attributes can still be archived
//
// Revision 1.15  2006/03/08 16:26:24  ounsy
// added the global POGO class comments for the devices:
// -HDBArchiver
// -TDBArchiver
// -ArchivingWatcher
//
// Revision 1.14  2006/03/08 14:36:21  ounsy
// added pogo comments
//
// Revision 1.13  2006/02/24 12:08:39  ounsy
// corrected a  bug where an incorrect Periodic mode wasn't reported
//
// Revision 1.12  2006/02/15 12:55:37  ounsy
// added retry_for_attribute and retry_for_attributes commands
//
// Revision 1.11  2006/02/15 11:10:11  chinkumo
// Javadoc comment update.
//
// Revision 1.10  2006/01/27 13:07:20  ounsy
// organised imports
//
// Revision 1.9  2006/01/13 14:28:35  chinkumo
// Changes made to avoid multi lines in AMT table when archivers are rebooted.
//
// Revision 1.8  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.7.8.5  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.7.8.4  2005/11/15 13:45:39  chinkumo
// ...
//
// Revision 1.7.8.3  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.7.8.2  2005/09/16 08:08:42  chinkumo
// Minor changes.
//
// Revision 1.7.8.1  2005/09/14 14:25:06  chinkumo
// 'trigger_archive_conf' methods were changed to allow the management of ArchivingMessConfig objects.
//
// Revision 1.7  2005/06/24 12:06:38  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.6  2005/06/15 14:03:00  chinkumo
// The device was regenerated in Tango V5.
//
// Revision 1.5  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.6.3  2005/06/13 13:42:24  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4.6.2  2005/05/11 16:02:46  chinkumo
// Minor changes made (some variables were renamed).
//
// Revision 1.4.6.1  2005/05/03 16:24:37  chinkumo
// Some constants in the class 'fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst' were renamed. Changes reported here.
// Some unuse references removed.
//
// Revision 1.4  2005/02/04 17:10:41  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.3  2005/01/31 17:17:03  chinkumo
// Minors change made into the set_status message of the method named 'stop_archive_att' (same message as on the historical side).
//
// Revision 1.2  2005/01/31 15:09:05  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:22  chinkumo
// First commit (new architecture).
//
// Revision 1.6  2004/10/07 14:31:21  chinkumo
// Since the facility of the archiver is taken into account when registrated into the AMT table, the ini_device() method was changed.
//
// Revision 1.5  2004/09/01 16:04:51  chinkumo
// Minor changes were made.
//
// Revision 1.4  2004/08/16 16:31:31  chinkumo
// Changes made to fit with the modification added to the file java/fr/soleil/TangoArchiving/HdbApi/HdbDataBase.java.
// (The way to connect to the database)
//
// Revision 1.3  2004/07/23 14:30:04  chinkumo
// Some javadoc comments changed.
//
// Revision 1.2  2004/07/22 15:36:15  chinkumo
// Changes made
// - to make the property 'DbHost' and 'DbName' be class property.
// - to make the property 'DbUser', 'DbPassword', 'DsPath' and 'DbPath' be device property.
//
// - to enable device able restart jobs after a crash.
//
// - to take note of renamed classes.
//
// Revision 1.1  2004/07/12 12:43:42  chinkumo
// First commit for this device.
// (This device as been renamed from 'HdbFastArchiver' to 'TdbArchiver').
//
// Revision 1.14  2004/07/07 16:50:57  chinkumo
// Initialization of default values in the get_device_property() method added again.
// They were firstly removed because of a possible dbt side effect.
// The property is invisible from Jive but seems to be be read from somewhere out of the dserver source code... (dbt ??)
//
// Revision 1.13  2004/07/07 12:10:56  ho
// Remove the comment before dbPath and dsPath in order to have default values
//
// Revision 1.12  2004/07/02 15:42:11  chinkumo
// Initialization of default values in the get_device_property() method removed.
//
// Revision 1.11  2004/07/02 15:14:30  chinkumo
// No more used property data members removed
//
// Revision 1.10  2004/06/30 14:52:42  chinkumo
// Changes made to destroy collectors that have empty attribute list to collect data from.
//
// Revision 1.9  2004/06/30 10:00:59  chinkumo
// Status informations changed.
//
// Revision 1.8  2004/06/29 16:02:46  chinkumo
// A command to force the export of data to the temporay database added.
//
// Revision 1.7  2004/06/25 08:01:46  ho
// Add Data base information
//
// Revision 1.6  2004/06/18 14:03:26  chinkumo
// Informations to enable the connection to the database are no more 'device properties'.
// They are 'class properties'.
// Thoses informations are : DbHost, DbName, DbUser, DbPassword.
//
// Revision 1.5  2004/06/17 07:51:00  chinkumo
// DServer status's initialization added
//
// Revision 1.4  2004/06/14 15:23:29  chinkumo
// Minors logging messages changes
//
// Revision 1.3  2004/06/14 12:33:37  chinkumo
// A property named DbName has been defined to define the name of the database.
//
// Revision 1.2  2004/06/14 10:25:39  chinkumo
// Names of properties updated.
//
// Revision 1.1  2004/06/11 14:13:42  chinkumo
// The first team version.
//
//
// copyleft :   European Synchrotron Radiation Facility
//              BP 220, Grenoble 38043
//              FRANCE
//
//-============================================================================
//
//          This file is generated by POGO
//  (Program Obviously used to Generate tango Object)
//
//         (c) - Software Engineering Group - ESRF
//=============================================================================

package TdbArchiver;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;

import TdbArchiver.Collector.DbProxy;
import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbCollectorFactory;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoDs.Attribute;
import fr.esrf.TangoDs.DeviceClass;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingMessConfig;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeSupport;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DBTools;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Warnable;

/**
 * Class Description: This device is in charge of archiving Tango attributes to
 * the Temporary Database, as known as TDB. On startup, it looks up in database
 * the attributes it is in charge of. A collector thread retrieves attribute
 * values, with the associated timestamp. For each attribute, the values are
 * then inserted into TDB every [base period], and if so required, when its
 * value meets certain user-defined criterions. The typical TDB archiving is
 * higher frequency than HDB archiving. Thus, records are not directly inserted
 * into TDB. Instead, they are written to a temp file, which is periodically
 * imported by TDB.
 *
 * @author $Author: pierrejoseph $
 * @version $Revision: 1.85 $
 */

// --------- Start of States Description ----------
/*
 * Device States Description: DevState.RUNNING : The device is not avalaible
 * because it is executing a command DevState.ON : The device is ready to use
 * DevState.INIT : The device has been initialized but not ready to use
 * DevState.FAULT : The device has been initialized but the connection to the
 * database crashed.
 */
// --------- End of States Description ----------

public class TdbArchiver extends DeviceImpl
		/* WithShutdownRunnable */implements TangoConst, Warnable {
	protected int state;
	private DbProxy dbProxy;

	// --------- Start of attributes data members ----------

	protected short attr_scalar_charge_read;
	protected short attr_spectrum_charge_read;
	protected short attr_image_charge_read;

	protected String m_version;

	// --------- End of attributes data members ----------

	// --------- Start of properties data members ----------
	/**
	 * Computer identifier on wich is settled the database HDB. The identifier
	 * can be the computer name or its IP address. <br>
	 * <b>Default value : </b> Class value
	 */
	String dbHost;
	/**
	 * Database name.<br>
	 * <b>Default value : </b> hdb
	 */
	String dbName;

	/**
	 * User identifier (name) used to connect the database TDB.<br>
	 * <b>Default value : </b> tdb
	 */
	String dbUser;
	/**
	 * Password used to connect the database TDB.<br>
	 * <b>Default value : </b> tdb
	 */
	String dbPassword;
	/**
	 * Path to the working directory. <br>
	 * This directory will be used to temporarily store data. <br>
	 * Data will finally be stored into the database. <br>
	 * <b>Default value : </b> C:\tango\TdbArchiver
	 */
	String dsPath;
	/**
	 * Path used by the database service to access temporary data through the
	 * network.<br>
	 * <p/>
	 * <b>Default value : </b> C:\tango\TdbArchiver
	 */
	String dbPath;

	/**
	 * Property used to describe the startup behaviour of the device. <br>
	 * If set to true, the inserts in AMT are done in another thread so that the
	 * device can be exported earlier. <br>
	 * If set to false, the inserts in AMT are done in the main thread
	 * sequentially.<br>
	 * <b>Default value : </b>false
	 */
	boolean hasThreadedStartup;

	/**
	 * Defines whether the archiver will log to a diary. <b>Default value :
	 * </b>false
	 */
	boolean hasDiary;
	/**
	 * true if the ORACLE RAC connection is activated. This information is
	 * appended to all device's (or attributes) name. false otherwise.<br>
	 * <b>Default value : </b> false
	 */
	boolean RacConnection;

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
	String diaryLogLevel;

	/**
	 * The path of the diary logs. Don't include a filename, diary files are
	 * named automatically. <b>Default value : </b> dsPath
	 */
	String diaryPath;

	/**
	 * Defines whether the archiver is a "dedicated archiver". A
	 * "dedicated archiver" is an archiver specialized in archiving a specific
	 * list of attributes, and none others. If the value is false, the related
	 * attribute "reservedAttributes" won't be used. <b>Default value :
	 * </b>false
	 */
	boolean isDedicated;

	/**
	 * The list of attributes that will always be archived on this archiver no
	 * matter the load-balancing situation. Be aware that: - this list will be
	 * ignored if the related attribute "isDedicated" is false or missing - one
	 * attribute shouldn't be on the reservedAttributes list of several
	 * archivers
	 */
	String[] reservedAttributes;
	// --------- End of properties data members ----------

	// Add your own data members here
	protected TdbCollectorFactory collectorFactory;
	public static boolean dbConnected = false;
	public static int gc_counter;
	public static int gc_counter_limit = 10000;

	public static String myDbHost;
	public static String myDbName;
	public static String myDbUser;
	public static String myDbPassword;
	//public static String myDsPath;
	//public static String myDbPath;
	public static boolean myCleaner;
	public static boolean myHasThreadedStartup;
	public static boolean myCleanOldFiles;
	public static boolean myHasTriggeredExport;
	public static boolean myHasDiary;
	public static String myDiaryLogLevel;
	public static String myDiaryPath;
	public static boolean myIsDedicated;
	public static String[] myReservedAttributes;
	public static boolean myRacConnection;

	public static final short NA = 10;
	public static final short SUCCEEDED = 20;
	public static final short FAILED = 30;

	private ILogger logger;

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
	// =========================================================
	TdbArchiver(DeviceClass cl, String s, String version) throws DevFailed {
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
	// =========================================================
	TdbArchiver(DeviceClass cl, String s, String d, String version)
			throws DevFailed {
		super(cl, s, d);
		m_version = version;
		init_device();
	}

	// =========================================================
	/**
	 * Initialize the device.
	 */
	// =========================================================
	public synchronized void init_device() throws DevFailed {
		// System.out.println("init_device/0");
		set_state(DevState.RUNNING);
		set_status(device_name + " : " + "DevState.RUNNING");

		System.out.println("TdbArchiver() create " + device_name);
		// System.out.println("init_device/1");
		// Initialise variables to default values
		// -------------------------------------------
		get_device_property();

		myDbHost = dbHost;
		myDbName = dbName;

		myDbUser = dbUser;
		myDbPassword = dbPassword;
		//myDsPath = dsPath;
		//myDbPath = dbPath;
		// myCleaner = cleaner;
		myHasThreadedStartup = hasThreadedStartup;
		/*
		 * myCleanOldFiles = cleanOldFiles; myHasTriggeredExport =
		 * hasTriggeredExport;
		 */
		myHasDiary = hasDiary;
		myDiaryLogLevel = diaryLogLevel;
		myDiaryPath = diaryPath;
		myIsDedicated = isDedicated;
		myReservedAttributes = reservedAttributes;
		myRacConnection = RacConnection;
		attr_image_charge_read = 0;
		attr_spectrum_charge_read = 0;
		attr_scalar_charge_read = 0;
		gc_counter = 0;

		get_logger().info("dbHost = " + dbHost);
		get_logger().info("dbName = " + dbName);

		get_logger().info("dbUser = " + dbUser);
		get_logger().info("dbPassword = " + dbPassword);
		get_logger().info("Rac Connection  = " + RacConnection);
		get_logger().info("dsPath = " + dsPath);
		get_logger().info("dbPath = " + dbPath);
		get_logger().info("hasThreadedStartup = " + hasThreadedStartup);
		/*
		 * get_logger().info("cleanOldFiles = " + cleanOldFiles);
		 * get_logger().info("hasNewExport = " + hasTriggeredExport);
		 */
		get_logger().info("hasDiary = " + hasDiary);
		get_logger().info("diaryLogLevel = " + diaryLogLevel);
		get_logger().info("diaryPath = " + diaryPath);
		get_logger().info("isDedicated = " + isDedicated);
		get_logger().info(
				"reservedAttributes = " + traceStrings(reservedAttributes));
		// System.out.println("init_device/2");
		// TdbCollectorFactory.checkInstance();

		this.startLoggingFactory();
		//collectorFactory = new TdbCollectorFactory(logger);
		collectorFactory = new TdbCollectorFactory(logger,dsPath,dbPath);

		this.dbProxy = new DbProxy(this.logger);
		if (!(this.dbProxy.is_db_connected())) {
			set_state(DevState.FAULT);
			set_status(device_name + " : " + "DevState.FAULT" + "\r\n"
					+ "Temporary database connection : " + "FAULT"
					+ " (may be broken...)" + "\r\n");
			get_logger().error("ERROR : Database unconnected !!");
		} else {
			// System.out.println("init_device/3");
			// CLA 18/05/06: shouldn't be ON yet
			set_state(DevState.RUNNING);
			set_status(device_name + " : " + "RUNNING.ON" + "\r\n"
					+ "Temporary database connection : " + "OK" + "\r\n"
					+ "DsPath : " + "\t" + dsPath + "\r\n" + "DbPath : " + "\t"
					+ dbPath);
			try {
				// System.out.println("init_device/4");

				Vector myCurrentTasks = this.dbProxy
						.getArchiverCurrentTasks(device_name);
				// System.out.println("init_device/5");
				if (myCurrentTasks.size() > 0) {
					ArchivingMessConfig archivingMessConfig = ArchivingMessConfig
							.basicObjectCreation();
					for (int i = 0; i < myCurrentTasks.size(); i++) {
						archivingMessConfig
								.add((AttributeLightMode) myCurrentTasks
										.elementAt(i));
					}

					boolean forceThreadedMode = false;
					this.startArchiving(archivingMessConfig, forceThreadedMode);
				}
				// System.out.println("init_device/6");
			}
			/*
			 * catch ( ArchivingException e ) {
			 * get_logger().error(e.toString());
			 * Util.out2.println(e.toString()); } catch ( DevFailed devFailed )
			 * { String message = DBTools.getCompleteMessage ( devFailed );
			 * get_logger().error ( message ); }
			 */
			catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.println("init_device/7");
		}
	}

	private String traceStrings(String[] reservedAttributes2) {
		if (reservedAttributes2 == null || reservedAttributes2.length == 0) {
			return "";
		}

		String ret = "";
		for (int i = 0; i < reservedAttributes2.length; i++) {
			ret += reservedAttributes2[i];
			if (i < reservedAttributes2.length - 1) {
				ret += ", ";
			}
		}
		return ret;
	}

	private void startLoggingFactory() {
		// System.out.println (
		// "CLA/startLoggingFactory/this.device_name/"+this.device_name+"/hashCode/"+this.hashCode
		// () );
		// ILogger _logger = LoggerFactory.getImpl ( LoggerFactory.DEFAULT_TYPE
		// , this.device_name , this.dsPath , this.hasDiary );
		ILogger _logger = LoggerFactory.getImpl(LoggerFactory.DEFAULT_TYPE,
				this.device_name, this.diaryPath, this.hasDiary);
		_logger.setTraceLevel(this.diaryLogLevel);
		this.logger = _logger;
	}

	private void startArchiving(ArchivingMessConfig archivingMessConfig,
			boolean forceThreadedMode) throws DevFailed {
		StartArchivingRunnable startArchivingRunnable = new StartArchivingRunnable(
				archivingMessConfig);
		Thread startArchivingThread = new Thread(startArchivingRunnable,
				"TDBStartArchivingThread");

		get_logger().info(
				"startArchiving/hasThreadedStartup/" + hasThreadedStartup);
		if (this.hasThreadedStartup || forceThreadedMode) {
			startArchivingThread.start();
		} else {
			startArchivingThread.run();
		}
	}

	private class StartArchivingRunnable implements Runnable {
		private ArchivingMessConfig archivingMessConfig;

		public StartArchivingRunnable(ArchivingMessConfig _archivingMessConfig) {
			this.archivingMessConfig = _archivingMessConfig;
		}

		public synchronized void run() {
			try {
				trigger_archive_conf(archivingMessConfig.toArray());
			} catch (DevFailed devFailed) {
				String completeMessage = DBTools.getCompleteMessage(devFailed);
				get_logger().error(completeMessage);
			}
		}
	}

	// ===================================================================
	/**
	 * Read the device properties from database.
	 */
	// ===================================================================
	public void get_device_property() throws DevFailed {
		// Initialize your default values here.
		// ------------------------------------------
		dbUser = ConfigConst.TDB_ARCHIVER_USER;
		dbPassword = ConfigConst.TDB_ARCHIVER_PASSWORD;
		dsPath = "C:\\tango\\TdbArchiver";
		dbPath = "C:\\tango\\TdbArchiver";
		// cleaner = false;
		hasThreadedStartup = false;
		/*
		 * cleanOldFiles = false; hasTriggeredExport = false;
		 */
		hasDiary = false;
		diaryLogLevel = ILogger.DEBUG;
		diaryPath = null;
		isDedicated = false;
		reservedAttributes = null;

		// Read device properties from database.(Automatic code generation)
		// -------------------------------------------------------------
		if (Util._UseDb == false)
			return;
		String[] propnames = { "DbHost", "DbName", "DbUser", "DbPassword",
				"RacConnection", "DsPath",
				"DbPath",
				// "Cleaner",
				"HasThreadedStartup",
				// "CleanOldFiles",
				// "HasNewExport",
				"HasDiary", "DiaryLogLevel", "DiaryPath", "IsDedicated",
				"ReservedAttributes" };

		// Call database and extract values
		// --------------------------------------------
		DbDatum[] dev_prop = get_db_device().get_property(propnames);
		TdbArchiverClass ds_class = (TdbArchiverClass) get_device_class();
		int i = -1;

		// Extract DbHost value
		if (dev_prop[++i].is_empty() == false)
			dbHost = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				dbHost = cl_prop.extractString();
		}

		// Extract DbName value
		if (dev_prop[++i].is_empty() == false)
			dbName = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				dbName = cl_prop.extractString();
		}

		// Extract DbUser value
		if (dev_prop[++i].is_empty() == false)
			dbUser = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				dbUser = cl_prop.extractString();
		}

		// Extract DbPassword value
		if (dev_prop[++i].is_empty() == false)
			dbPassword = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				dbPassword = cl_prop.extractString();
		}
		// Extract RacConnection value
		if (dev_prop[++i].is_empty() == false)
			RacConnection = dev_prop[i].extractBoolean();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				RacConnection = cl_prop.extractBoolean();
		}

		// Extract DsPath value
		if (dev_prop[++i].is_empty() == false)
			dsPath = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				dsPath = cl_prop.extractString();
		}

		// Extract DbPath value
		if (dev_prop[++i].is_empty() == false)
			dbPath = dev_prop[i].extractString();
		else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false)
				dbPath = cl_prop.extractString();
		}

		/*
		 * // Extract Cleaner value if ( dev_prop[ ++i ].is_empty() == false )
		 * cleaner = dev_prop[ i ].extractBoolean(); else { // Try to get value
		 * from class property DbDatum cl_prop =
		 * ds_class.get_class_property(dev_prop[ i ].name); if (
		 * cl_prop.is_empty() == false ) cleaner = cl_prop.extractBoolean(); }
		 */

		// Extract HasThreadedStartup value
		if (dev_prop[++i].is_empty() == false) {
			hasThreadedStartup = dev_prop[i].extractBoolean();
		} else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false) {
				hasThreadedStartup = cl_prop.extractBoolean();
			}
		}

		/*
		 * // Extract CleanOldFiles value if ( dev_prop[ ++i ].is_empty() ==
		 * false ) { cleanOldFiles = dev_prop[ i ].extractBoolean(); } else { //
		 * Try to get value from class property DbDatum cl_prop =
		 * ds_class.get_class_property(dev_prop[ i ].name); if (
		 * cl_prop.is_empty() == false ) { cleanOldFiles =
		 * cl_prop.extractBoolean(); } }
		 *
		 * // Extract hasNewExport value if ( dev_prop[ ++i ].is_empty() ==
		 * false ) { hasTriggeredExport = dev_prop[ i ].extractBoolean(); } else
		 * { // Try to get value from class property DbDatum cl_prop =
		 * ds_class.get_class_property(dev_prop[ i ].name); if (
		 * cl_prop.is_empty() == false ) { hasTriggeredExport =
		 * cl_prop.extractBoolean(); } }
		 */

		// Extract hasDiary value
		if (dev_prop[++i].is_empty() == false) {
			hasDiary = dev_prop[i].extractBoolean();
		} else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false) {
				hasDiary = cl_prop.extractBoolean();
			}
		}

		// Extract DiaryLogLevel value
		if (dev_prop[++i].is_empty() == false) {
			diaryLogLevel = dev_prop[i].extractString();
		} else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false) {
				diaryLogLevel = cl_prop.extractString();
			}
		}

		// Extract DiaryPath value
		if (dev_prop[++i].is_empty() == false) {
			diaryPath = dev_prop[i].extractString();
		} else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false) {
				diaryPath = cl_prop.extractString();
			}
		}
		if (diaryPath == null || diaryPath.equals("")) {
			diaryPath = this.dsPath;
		}

		// Extract IsDedicated value
		if (dev_prop[++i].is_empty() == false) {
			isDedicated = dev_prop[i].extractBoolean();
			System.out.println("TdbArchiver/get_device_property/2/"
					+ this.get_name() + "/isDedicated/" + isDedicated);
		} else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false) {
				isDedicated = cl_prop.extractBoolean();
			}
		}

		// Extract ReservedAttributes value
		if (dev_prop[++i].is_empty() == false) {
			reservedAttributes = dev_prop[i].extractStringArray();
		} else {
			// Try to get value from class property
			DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty() == false) {
				reservedAttributes = cl_prop.extractStringArray();
			}
		}
		// End of Automatic code generation
		// -------------------------------------------------------------

	}

	// =========================================================
	/**
	 * Method always executed before command execution.
	 */
	// =========================================================
	public void always_executed_hook() {
		get_logger().info("In always_executed_hook method()");
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
		// ---------------------------------
		if (attr_name == "scalar_charge") {
			// Add your own code here
			attr.set_value(attr_scalar_charge_read);
			// attr.set_value(Double.NaN);
			// throw new DevFailed();
		} else if (attr_name == "spectrum_charge") {
			// Add your own code here
			attr.set_value(attr_spectrum_charge_read);
		} else if (attr_name == "image_charge") {
			// Add your own code here
			attr.set_value(attr_image_charge_read);
		} else if (attr_name == "version") {
			// Add your own code here
			attr.set_value(m_version);
		}
	}

	// =========================================================
	/**
	 * Execute command "TriggerArchiveConf" on device. This command is invoked
	 * when the archiving (intermediate archiving) of a group of attributes is
	 * triggered. The group of attributes is therefore encapsulated in an
	 * ArchivingMessConfig type object. The use of this command suppose that the
	 * database is ready to host those attributes's values. That means
	 * registrations were made, the associated tables built, ...
	 *
	 * @param argin
	 *            The group of attributes to archive
	 * @see ArchivingMessConfig
	 * @see AttributeLightMode
	 */
	// =========================================================
	public synchronized void trigger_archive_conf(String[] argin)
			throws DevFailed {
		// ---Add your Own code to control device here ---
		set_state(DevState.RUNNING);
		set_status(device_name + " : " + "DevState.RUNNING");
		this.logger.trace(ILogger.LEVEL_INFO,
				"===> Entering trigger_archive_conf()");

		ArchivingException archivingException = new ArchivingException();
		ArchivingMessConfig archivingMessConfig = ArchivingMessConfig
				.creationWithFullInformation(argin);
		archivingMessConfig.filter(this.isDedicated, this.reservedAttributes,
				this.logger);

		boolean hasToThrow = false;

		for (Enumeration my_EnumAttributes = archivingMessConfig
				.getAttributeListKeys(); my_EnumAttributes.hasMoreElements();) {
			String m_attribute = String
					.valueOf(my_EnumAttributes.nextElement());
			this.logger.trace(ILogger.LEVEL_INFO, "===> Attribute Name = "
					+ m_attribute);

			AttributeLightMode attributeLightMode = (AttributeLightMode) archivingMessConfig
					.getAttribute(m_attribute);
			attributeLightMode.setTrigger_time(new Timestamp(System
					.currentTimeMillis()));
			boolean db_update = false, att_support = false;
			String att_complete_name = attributeLightMode
					.getAttribute_complete_name();

			try {
				att_support = AttributeSupport.checkAttributeSupport(
						att_complete_name, attributeLightMode.getData_type(),
						attributeLightMode.getData_format(), attributeLightMode
								.getWritable());

				if (att_support) {
					try {
						attributeLightMode.getMode().checkMode(false);
					} catch (ArchivingException ae) {
						// next attribute
						String message = GlobalConst.ARCHIVING_ERROR_PREFIX
								+ " : ";
						String reason = "Failed while executing TdbArchiver.trigger_archive_conf() method...";
						String desc = "Invalid mode for attribute "
								+ att_complete_name;
						archivingException.addStack(message, reason,
								ErrSeverity.PANIC, desc, "", ae);
						this.logger.trace(ILogger.LEVEL_ERROR, reason + " "
								+ desc);

						continue;
					}
					if (attributeLightMode.getMode().getTdbSpec() != null) {
						attributeLightMode.getMode().getTdbSpec()
								.setExportPeriod(getExportPeriod());
					}
					attributeLightMode.setDevice_in_charge(device_name);
					// Garanti l'unicit� de lignes "NULL" dans l'AMT !!
					if (this.dbProxy.isArchived(att_complete_name)) {
						if (this.dbProxy.isArchived(att_complete_name,
								device_name)) {
							try {
								// collectorFactory.removeAllForAttribute(attributeLightMode.getAttribute_complete_name());
								// this.logger.trace ( ILogger.LEVEL_INFO ,
								// "===> Entering removeAllForAttribute");
								collectorFactory
										.removeAllForAttribute(attributeLightMode);
								// this.logger.trace ( ILogger.LEVEL_INFO ,
								// "===> Exiting removeAllForAttribute");
							} catch (ArchivingException ae) {
								this.logger
										.trace(
												ILogger.LEVEL_ERROR,
												"Failed while executing TdbArchiver.trigger_archive_conf() method : removeAllForAttribute");
								ae.printStackTrace();
								continue;
							}

							this.dbProxy.updateModeRecord(att_complete_name);
						} else
							continue;
					}

					try {
						// this.dbProxy.getDataBase().getAttribute().CreateAttributeTableIfNotExist(att_complete_name,
						// this.dbProxy.getDataBase().getExtractor().getDataGetters(),
						// this.dbProxy.getDataBase().getDbUtil());
						this.dbProxy.insertModeRecord(attributeLightMode);
					} catch (ArchivingException e) {
						this.logger
								.trace(ILogger.LEVEL_ERROR,
										"############################################################");
						this.logger.trace(ILogger.LEVEL_ERROR,
								"Insertion failed in AMT for the att : "
										+ att_complete_name
										+ " with following exception");
						this.logger.trace(ILogger.LEVEL_ERROR, e);
						this.logger
								.trace(ILogger.LEVEL_ERROR,
										"############################################################");

						// if the attribute is already archived, ignore it
						continue;
					}

					db_update = true;
					TdbCollector m_collector = null;
					this.logger.trace(ILogger.LEVEL_INFO,
							"===> Entering get collector");
					m_collector = collectorFactory.get(attributeLightMode,
							true, this.dbProxy);
					this.logger.trace(ILogger.LEVEL_INFO,
							"===> Exiting get collector + Entering addSource ");
					m_collector.addSource(attributeLightMode);
					this.logger.trace(ILogger.LEVEL_INFO,
							"===> Exiting addSource ");
					m_collector.setWarnable(this);
				}
			} catch (ArchivingException e) {
				e.printStackTrace();
				String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
				String reason = "Failed while executing TdbArchiver.trigger_archive_conf() method...";
				String desc = (att_support) ? (db_update) ? "Unable to find a collector for the attribute named "
						+ att_complete_name
						+ " ! "
						+ "\r\n"
						+ "Mode parameters should be checked..."
						: "Unable to register the attribute named "
								+ att_complete_name + " in the mode table !"
						: "Attribute named "
								+ att_complete_name
								+ " have not supported type, format or writable property!";
				archivingException.addStack(message, reason, ErrSeverity.PANIC,
						desc, "", e);
				hasToThrow = true;

				this.logger.trace(ILogger.LEVEL_ERROR, reason + " " + desc);
			} catch (Exception e) {
				this.logger
						.trace(
								ILogger.LEVEL_ERROR,
								"ATTENTION=====Failed while executing TdbArchiver.trigger_archive_conf() method see exception "
										+ e);
				e.printStackTrace();
			}
		}

		this.computeLoads();

		set_state(DevState.ON);
		set_status(device_name + " : " + "DevState.ON");

		if (hasToThrow) {
			this.logWarningAboutRiskySettingOfStateBackToOn(
					"TriggerArchiveConf", archivingException);
			throw archivingException.toTangoException();
		}

		this.logger.trace(ILogger.LEVEL_INFO,
				"===> Exiting trigger_archive_conf()");
		get_logger().info("Exiting trigger_archive_conf()");
	}

	/**
	 *
	 * @return the export period from the TdbArchiver Class property
	 */
	private long getExportPeriod() {
		// TODO Auto-generated method stub
		try {
			return (int) GetConf.readLongInDB("TdbArchiver",
					GetConf.EXPORT_PERIOD_PROPERTY);
		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GetConf.m_DEFAULT_EXPORT_PERIOD_VALUE;
	}

	private void logWarningAboutRiskySettingOfStateBackToOn(String methodName,
			Exception e) {
		String openingLine = "Setting the state back to ON despite encountering exceptions in "
				+ methodName + " VVVVVVVVVVV";
		String closingLine = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
		this.logger.trace(ILogger.LEVEL_WARNING, openingLine);
		this.logger.trace(ILogger.LEVEL_WARNING, e);
		this.logger.trace(ILogger.LEVEL_WARNING, closingLine);
	}

	// =========================================================
	/**
	 * Execute command "TriggerArchiveAtt" on device. This command is invoked
	 * when the archiving (intermediate archiving) of an attribute is triggered.
	 * The attribute to archive is therefore encapsulated in a
	 * fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLight type
	 * object. The use of this command suppose that the TDB database is ready to
	 * host that attribute's values. That means the registration was made, the
	 * associated table built, ...
	 *
	 * @param argin
	 *            The attribute to archive
	 * @see ArchivingMessConfig
	 * @see AttributeLightMode
	 * @deprecated use trigger_archive_conf(String[] argin) instead
	 */
	// =========================================================
	public synchronized void trigger_archive_att(String[] argin)
			throws DevFailed {
		set_state(DevState.RUNNING);
		set_status(device_name + " : " + "DevState.RUNNING");

		get_logger().info("Entering trigger_archive_att()");

		ArchivingException archivingException = new ArchivingException();
		AttributeLightMode attributeLightMode = AttributeLightMode
				.creationWithFullInformation(argin);
		TdbCollector collector = null;
		boolean att_support = false;
		try {
			att_support = AttributeSupport
					.checkAttributeSupport(attributeLightMode
							.getAttribute_complete_name(), attributeLightMode
							.getData_type(), attributeLightMode
							.getData_format(), attributeLightMode.getWritable());
			if (att_support) {
				attributeLightMode.getMode().checkMode(false);
				collector = collectorFactory.get(attributeLightMode, true,
						this.dbProxy);
				collector.addSource(attributeLightMode);
			}
		} catch (ArchivingException e) {
			get_logger().error(e.toString());
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
			String reason = "Failed while executing TdbArchiver.trigger_archive_att() method...";
			String desc = (att_support) ? "Unable to archive the attribute named "
					+ attributeLightMode.getAttribute_complete_name()
					+ "\r\n"
					+ "Mode parameters should be checked..."
					: "Attribute named "
							+ attributeLightMode.getAttribute_complete_name()
							+ " have not supported type, format or writable property!";

			this.logWarningAboutRiskySettingOfStateBackToOn(
					"TriggerArchiveAtt", e);

			archivingException.addStack(message, reason, ErrSeverity.PANIC,
					desc, "", e);
			throw e.toTangoException();
		} finally {
			this.computeLoads();

			set_state(DevState.ON);
			set_status(device_name + " : " + "DevState.ON");
		}

		get_logger().info("Exiting trigger_archive_att()");
	}

	// =========================================================
	/**
	 * Execute command "TriggerArchiveAttCheck" on device. This command is
	 * invoked when the archiving (intermediate archiving) of an attribute is
	 * triggered. Before archiving, this command checks that TDB database is
	 * ready to host the given attribute values. If not registrations is done
	 * and the associated table is built.
	 *
	 * @param argin
	 *            The name of the attribute to archive
	 * @deprecated use trigger_archive_conf(String[] argin) instead
	 */
	// =========================================================
	public synchronized void trigger_archive_att_check(String argin)
			throws DevFailed {
		set_state(DevState.RUNNING);
		set_status(device_name + " : " + "DevState.RUNNING");

		get_logger().info("Entering trigger_archive_att_check()");
		get_logger().info("Exiting trigger_archive_att_check()");

		set_state(DevState.ON);
		set_status(device_name + " : " + "DevState.ON");
	}

	// =========================================================
	/**
	 * Execute command "StopArchiveConf" on device. This command is invoked when
	 * stopping the archiving of a group of attributes.
	 *
	 * @param argin
	 *            The group of attributes
	 */
	// =========================================================
	public synchronized void stop_archive_conf(String[] argin) throws DevFailed {
		set_state(DevState.RUNNING);
		set_status(device_name + " : " + "DevState.RUNNING");

		get_logger().info("Entering stop_archive_conf()");

		Vector myConf = ArchivingManagerApiRef.stoppingVector(argin);
		for (int i = 0; i < myConf.size(); i++) {
			stop_archive_att(((AttributeLightMode) myConf.elementAt(i))
					.toArray());
		}

		get_logger().info("Exiting stop_archive_conf()");

		set_state(DevState.ON);
		set_status(device_name + " : " + "DevState.ON");
	}

	// =========================================================
	/**
	 * Execute command "StopArchiveAtt" on device. This command need an
	 * AttributeLightMode type object. An AttributeLightMode type object
	 * encapsulate all the informations needed to found the Collector in charge
	 * of the archiving of the specified attribute The informations needed are
	 * the type, the format, the writable property and the archiving mode
	 *
	 * @param argin
	 *            the attribute on witch archiving must be stopped
	 */
	// =========================================================
	public synchronized void stop_archive_att(String[] argin) throws DevFailed {
		set_state(DevState.RUNNING);
		set_status(device_name + " : " + "DevState.RUNNING");

		get_logger().info("Entering stop_archive_att()");

		AttributeLightMode attributeLightMode = AttributeLightMode
				.creationWithFullInformation(argin);
		TdbCollector tdbCollector = null;
		try {
			this.dbProxy.updateModeRecord(attributeLightMode);

			tdbCollector = collectorFactory.get(attributeLightMode, false,
					this.dbProxy);
			if (tdbCollector != null) {
				tdbCollector.removeSource(attributeLightMode
						.getAttribute_complete_name());
				if (!tdbCollector.isRefreshing()) {
					collectorFactory.destroy(attributeLightMode);
				}
			} else// should only happen when attempting to call stop_archive_att
					// on a non-longer-reserved attribute of a formerly (ie. at
					// the time the archiving was started) dedicated archiver.
			{
				String msg = "TdbArchiver/stop_archive_att/getAttribute_complete_name|"
						+ attributeLightMode.getAttribute_complete_name()
						+ "| The collector is missing. should only happen when attempting to call stop_archive_att on a non-longer-reserved attribute of a formerly (ie. at the time the archiving was started) dedicated archiver.";
				logger.trace(ILogger.LEVEL_ERROR, msg);

				/*
				 * String message = "Attempt to get a missing collector!";
				 * String reason =
				 * "Missing collector for attribute: "+attributeLightMode
				 * .getAttribute_complete_name(); String desc =
				 * "Failed while executing TdbArchiver.stop_archive_att"; throw
				 * new ArchivingException(message , reason , ErrSeverity.PANIC ,
				 * desc , "");
				 */
			}
		} catch (ArchivingException e) {
			this
					.logWarningAboutRiskySettingOfStateBackToOn(
							"StopArchiveAtt", e);

			get_logger().error(e.toString());
			Util.out2.println(e.toString());
			throw e.toTangoException();
		} finally {
			this.computeLoads();

			set_state(DevState.ON);
			set_status(device_name + " : " + "DevState.ON");
		}

		get_logger().info("Exiting stop_archive_att()");
	}

	// =========================================================
	/**
	 * Execute command "ExportData2Db" on device. This command need an
	 * AttributeLightMode type object. An AttributeLightMode type object
	 * encapsulate all the informations needed to found the Collector in charge
	 * of the archiving of the specified attribute The informations needed are
	 * the type, the format, the writable property and the archiving mode
	 *
	 * @param argin
	 *            the attribute from witch data are expected.
	 */
	// =========================================================
	public String export_data2_db(String[] argin) throws DevFailed {
		// System.out.println("TdbArchiver/export_data2_db/START");
		get_logger().info("Entering export_data2_db()");
		/*
		 * for ( int i = 0 ; i < argin.length ; i ++ ) { System.out.println (
		 * "export_data2_db/i|"+i+"|argin[i]|"+argin[i]+"|" ); }
		 */
		// ---Add your Own code to control device here ---
		AttributeLightMode attributeLightMode = AttributeLightMode
				.creationWithFullInformation(argin);
		String tableName = "";

		try {
			TdbCollector collector = collectorFactory.get(attributeLightMode,
					false, this.dbProxy);
			if (collector != null) {
				tableName = collector.exportFile2Db(attributeLightMode
						.getAttribute_complete_name(), false);
			} else {
				String msg = "TdbArchiver/export_data2_db/getAttribute_complete_name|"
						+ attributeLightMode.getAttribute_complete_name()
						+ "| The collector is missing!";
				logger.trace(ILogger.LEVEL_CRITIC, msg);

				String message = "Attempt to get a missing collector!";
				String reason = "Missing collector for attribute: "
						+ attributeLightMode.getAttribute_complete_name();
				String desc = "Failed while executing TdbArchiver.export_data2_db";
				throw new ArchivingException(message, reason,
						ErrSeverity.PANIC, desc, "");
			}
		} catch (ArchivingException e) {
			get_logger().error(e.toString());
			Util.out2.println(e.toString());
			throw e.toTangoException();
		}
		get_logger().info("Exiting export_data2_db()");

		return tableName;
	}

	// =========================================================
	/**
	 * Execute command "StateDetailed" on device. This command returns a
	 * detailed state of the device.
	 *
	 * @return The detailed state
	 */
	// =========================================================
	public String state_detailed() throws DevFailed {
		String argout = new String();
		get_logger().info("Entering state_detailed()");

		// ---Add your Own code to control device here ---
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(collectorFactory.factoryAssessment());

		argout = stringBuffer.toString();
		get_logger().info("Exiting state_detailed()");
		return argout;
	}

	// =========================================================
	/**
	 * main part for the device server class
	 */
	// =========================================================
	public static void main(String[] argv) {
		try {
			Util tg = Util.init(argv, "TdbArchiver");
			tg.server_init();

			System.out.println("Ready to accept request");

			tg.server_run();
		} catch (OutOfMemoryError ex) {
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

	// ========================================================
	/**
	 * Execute command "RetryForAttribute" on device. Tries to start archiving
	 * for a given attribute, specified by its complete name.
	 *
	 * @param attributeToRetry
	 *            The complete name of the attribute
	 * @return A return code, can be either: 10 (the archiver isn't in charge of
	 *         the specified attribute) 20 (the retry succeeded) or 30 (the
	 *         retry failed)
	 */
	// =========================================================
	public synchronized short retry_for_attribute(String attributeToRetry) {
		if (attributeToRetry == null || attributeToRetry.equals("")) {
			return NA;
		}
		try {
			Vector myCurrentTasks = this.dbProxy
					.getArchiverCurrentTasks(device_name);
			ArchivingMessConfig archivingMessConfig = ArchivingMessConfig
					.basicObjectCreation();

			boolean configIsNotEmpty = false;
			for (int i = 0; i < myCurrentTasks.size(); i++) {
				AttributeLightMode attributeLightMode = (AttributeLightMode) myCurrentTasks
						.elementAt(i);
				String attrName = attributeLightMode
						.getAttribute_complete_name();

				if (attrName.equals(attributeToRetry)) {
					archivingMessConfig.add(attributeLightMode);
					configIsNotEmpty = true;
					break;
				}
			}

			if (configIsNotEmpty) {
				this.logRetry(archivingMessConfig, "retry_for_attribute");

				boolean forceThreadedMode = true;
				this.startArchiving(archivingMessConfig, forceThreadedMode);

				return SUCCEEDED;
			} else {
				return NA;
			}
		} catch (DevFailed devFailed) {
			String message = DBTools.getCompleteMessage(devFailed);
			get_logger().error(message);

			return FAILED;
		} catch (Throwable t) {
			DevFailed devFailed = new DevFailed();
			devFailed.initCause(t);
			String message = DBTools.getCompleteMessage(devFailed);
			get_logger().error(message);

			return FAILED;
		}
	}

	// =========================================================
	/**
	 * Execute command "RetryForAttributes" on device. Tries to start archiving
	 * for a given list of attributes, specified by their complete names.
	 *
	 * @param attributesToRetry
	 *            The complete names of the attributes
	 * @return A return code, can be either: 10 (the archiver isn't in charge of
	 *         any of the specified attributes) 20 (the retry succeeded) or 30
	 *         (the retry failed)
	 */
	// =========================================================
	public synchronized short retry_for_attributes(String[] attributesToRetry) {
		if (attributesToRetry == null || attributesToRetry.length == 0) {
			return NA;
		}

		try {
			Vector myCurrentTasks = this.dbProxy
					.getArchiverCurrentTasks(device_name);
			Hashtable modes = new Hashtable(myCurrentTasks.size());

			for (int i = 0; i < myCurrentTasks.size(); i++) {
				AttributeLightMode attributeLightMode = (AttributeLightMode) myCurrentTasks
						.elementAt(i);
				String attrName = attributeLightMode
						.getAttribute_complete_name();
				modes.put(attrName, attributeLightMode);
			}
			if (modes.size() == 0) {
				return NA;
			}

			boolean configIsNotEmpty = false;
			ArchivingMessConfig archivingMessConfig = ArchivingMessConfig
					.basicObjectCreation();
			for (int j = 0; j < attributesToRetry.length; j++) {
				String currentName = attributesToRetry[j];
				AttributeLightMode attributeLightMode = (AttributeLightMode) modes
						.get(currentName);
				if (attributeLightMode != null) {
					archivingMessConfig.add(attributeLightMode);
					configIsNotEmpty = true;
				}
			}

			if (configIsNotEmpty) {
				this.logRetry(archivingMessConfig, "retry_for_attributes");

				boolean forceThreadedMode = true;
				this.startArchiving(archivingMessConfig, forceThreadedMode);

				return SUCCEEDED;
			} else {
				return NA;
			}
		} catch (DevFailed devFailed) {
			String message = DBTools.getCompleteMessage(devFailed);
			get_logger().error(message);

			return FAILED;
		} catch (Throwable t) {
			DevFailed devFailed = new DevFailed();
			devFailed.initCause(t);
			String message = DBTools.getCompleteMessage(devFailed);
			get_logger().error(message);

			return FAILED;
		}

	}

	public void trace(String msg, int level) // throws DevFailed
	{
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

		/*
		 * default : Tools.throwDevFailed ( new IllegalArgumentException (
		 * "Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got "
		 * + level + "instead." ) );
		 */
		}
	}

	public void trace(String msg, Throwable t, int level)// throws DevFailed
	{
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

		/*
		 * default : Tools.throwDevFailed ( new IllegalArgumentException (
		 * "Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got "
		 * + level + "instead." ) );
		 */
		}
	}

	private void logRetry(ArchivingMessConfig archivingMessConfig,
			String methodName) {
		String retryContent = archivingMessConfig.toString();
		String openingLine = methodName
				+ "/retry for the following attributes VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV";
		String closingLine = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";

		this.logger.trace(ILogger.LEVEL_INFO, openingLine);
		this.logger.trace(ILogger.LEVEL_INFO, retryContent);
		this.logger.trace(ILogger.LEVEL_INFO, closingLine);
	}

	private synchronized void computeLoads() {
		short[] loads = collectorFactory.factoryLoadAssessment();

		this.attr_scalar_charge_read = loads[0];
		this.attr_spectrum_charge_read = loads[1];
		this.attr_image_charge_read = loads[2];
	}

	@Override
	public void delete_device() throws DevFailed {
		// TODO Auto-generated method stub

	}

}

// --------------------------------------------------------------------------
/*
 * end of $Source:
 * /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/TdbArchiver.java,v $
 */