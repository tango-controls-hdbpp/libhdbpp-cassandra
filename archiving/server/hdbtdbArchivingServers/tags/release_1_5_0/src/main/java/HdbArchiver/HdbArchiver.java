//+============================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/HdbArchiver.java,v $
//
// project :     Tango Device Server
//
// Description: java source code for the HdbArchiver class and its commands.
//              This class is derived from DeviceImpl class.
//              It represents the CORBA servant obbject which
//              will be accessed from the network. All commands which
//              can be executed on the HdbArchiver are implemented
//              in this file.
//
// $Author: pierrejoseph $
//
// $Revision: 1.89 $
//
// $Log: HdbArchiver.java,v $
// Revision 1.89  2007/10/03 15:23:46  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.88.2.1  2007/09/28 14:48:08  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.88  2007/08/27 14:14:50  pierrejoseph
// Traces addition
//
// Revision 1.87  2007/06/15 07:46:47  pierrejoseph
// add traces in trigger to locate the collector freezing
//
// Revision 1.86  2007/06/07 09:54:21  pierrejoseph
// Traces addition to locate when the device stays in the RUNNING state
//
// Revision 1.85  2007/05/11 13:58:34  pierrejoseph
// Attribute addition : release version
//
// Revision 1.84  2007/05/10 16:23:45  chinkumo
// Archiving Version 1.2.0  for Tango 5.5.8
//
// Revision 1.83  2007/04/05 10:06:04  chinkumo
// New Release 5.5.7
//
// Revision 1.82  2007/03/27 09:17:52  ounsy
// move the AMT updating upwards in stop_archive_att so that the archiving can be stopped even if no collector was created (as can happen for reserved attributes of formerly-dedicated archivers).
//
// Revision 1.81  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.80  2007/02/26 16:14:24  ounsy
// archiving devices now inherits just from DeviceImpl instead of
// DeviceImplWithShutdownRunnable (they're nonlonger unexported onn shutdown)
//
// Revision 1.79  2007/02/26 08:31:47  chinkumo
// For Tango 5.5.6
//
// Revision 1.78  2007/02/19 13:01:45  pierrejoseph
// Add a trace in case of insertion error in amt table.
//
// Revision 1.77  2007/02/19 09:06:26  chinkumo
// Release 19 - 02 - 2007 for Tango 5.5.6
//
// Revision 1.76  2007/01/11 07:35:52  chinkumo
// Release / 2007-01-11
//
// Revision 1.75  2007/01/09 16:44:58  ounsy
// commands that lock the device into the RUNNING state now set it back to ON even if they encountered exceptions. in this case an entry is logged into the archiver's diary.
//
// Revision 1.74  2007/01/09 15:16:04  ounsy
// put the Giacomo version back for XDBCollectorFactory.removeAllForAttribute
//
// Revision 1.73  2007/01/05 12:56:35  pierrejoseph
// Modification of the ArchivingMessConfig object creation and the trigger_archive_conf method argin has lost its first value.
//
// Revision 1.72  2007/01/04 08:48:02  chinkumo
// Production of the dated version 2007-01-04
//
// Revision 1.71  2006/12/08 07:45:14  chinkumo
// date of the version : 2006-12-08
//
// Revision 1.70  2006/11/24 14:54:17  ounsy
// we re-use the old removeAllForAttribute
//
// Revision 1.69  2006/11/24 14:04:16  ounsy
// the diary entry in case of missing collector is now done by he archiver
//
// Revision 1.68  2006/11/24 13:19:35  ounsy
// TdbCollectorFactory.get(attrLightMode) has a new parameter "doCreateCollectorIfMissing". A missing collector will only be created if this is true, otherwise a message in logged into the archiver's diary and an exception launched
//
// Revision 1.67  2006/11/20 09:23:51  ounsy
// minor changes
//
// Revision 1.65  2006/11/13 15:57:37  ounsy
// all java devices now inherit from UnexportOnShutdownDeviceImpl instead of from DeviceImpl
//
// Revision 1.64  2006/10/30 14:34:49  ounsy
// minor change
//
// Revision 1.63  2006/10/18 14:50:45  ounsy
// release date updated
//
// Revision 1.62  2006/10/17 14:50:09  ounsy
// release date updated
//
// Revision 1.61  2006/10/13 15:00:20  ounsy
// release date updated
//
// Revision 1.60  2006/10/11 08:44:15  ounsy
// release date updated
//
// Revision 1.59  2006/10/11 08:31:19  ounsy
// modified the trigger_archive_conf commands to accept the same parameters as the archiving manager
//
// Revision 1.58  2006/10/09 13:55:34  ounsy
// removed logs
//
// Revision 1.57  2006/10/05 15:42:04  ounsy
// added archivingMessConfig.filter in trigger_archive_conf
//
// Revision 1.56  2006/09/26 15:51:42  ounsy
// minor changes
//
// Revision 1.55  2006/09/15 08:56:54  ounsy
// release date updated
//
// Revision 1.54  2006/09/13 09:49:43  ounsy
// corrected a bug in getDeviceproperty
//
// Revision 1.53  2006/09/12 13:01:42  ounsy
// methods that put the archiver in the RUNNING state now do it from the start and only go to the ON state at the end of the method
//
// Revision 1.52  2006/09/08 14:17:20  ounsy
// added missing descriptions of properties
//
// Revision 1.51  2006/08/29 15:14:42  ounsy
// release date updated
//
// Revision 1.50  2006/08/29 08:44:51  ounsy
// corrected a bug in getControlResult()
//
// Revision 1.49  2006/08/23 09:41:01  ounsy
// minor changes
//
// Revision 1.48  2006/08/10 09:28:28  ounsy
// release date updated
//
// Revision 1.47  2006/08/08 12:22:39  ounsy
// release date updated
//
// Revision 1.46  2006/07/31 08:01:23  ounsy
// release date updated
//
// Revision 1.45  2006/07/31 08:00:55  ounsy
// release date updated
//
// Revision 1.44  2006/07/28 13:50:40  ounsy
// corrected the method name in logRetry
//
// Revision 1.43  2006/07/28 09:33:39  ounsy
// added diary logging on retryForXXXmethods
//
// Revision 1.42  2006/07/26 08:35:22  ounsy
// initDevice synchronized + minor changes
//
// Revision 1.41  2006/07/25 16:24:17  ounsy
// HdbCollectorFactory no more static
//
// Revision 1.40  2006/07/25 13:39:14  ounsy
// correcting bad merge
//
// Revision 1.39  2006/07/25 13:22:32  ounsy
// added a "version" attribute
//
// Revision 1.38  2006/07/25 09:45:25  ounsy
// changed the XXX_charge management
//
// Revision 1.37  2006/07/21 14:39:02  ounsy
// minor changes
//
// Revision 1.36  2006/07/18 15:11:55  ounsy
// added a diaryLogLevel property
//
// Revision 1.35  2006/07/12 14:00:24  ounsy
// added the synchronized keyword on all trigger_XXX and stop_XXX methods
//
// Revision 1.34  2006/07/06 09:14:18  ounsy
// modified the retryForXXX methods so that they retry in a separate thread to void timeouts
//
// Revision 1.33  2006/06/29 08:45:10  ounsy
// added a hasDiary property (default false) that has to be set to true for the logging to do anything
//
// Revision 1.32  2006/06/27 08:35:05  ounsy
// Corrected a grave bug in logging by removing the "synchronized" tag from the trace methods, that was causing deadlocks
//
// Revision 1.31  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.30  2006/06/13 13:28:19  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.29  2006/06/07 12:55:56  ounsy
// added period, lastInsert and lastInsertRequest to the data of each line representing a KO attribute
//
// Revision 1.28  2006/06/02 08:39:12  ounsy
// minor changes
//
// Revision 1.27  2006/05/23 07:39:34  ounsy
// +added a hasThreadedStartup which if true does the AMT inserts in a separate thread
// -removed TDB-specific useless attributes
//
// Revision 1.26  2006/05/17 15:02:09  ounsy
// made the logs more coherent
//
// Revision 1.25  2006/04/11 09:11:49  ounsy
// double archiving protection
//
// Revision 1.24  2006/03/29 10:20:51  ounsy
// Added content to the error message:
// each atribute with an invalid mode has a specific log
//
// Revision 1.23  2006/03/27 13:51:10  ounsy
// catched the ArchivingException on checkMode so that if an attribute's mode
// is invalid, the other attributes can still be archived
//
// Revision 1.22  2006/03/14 13:09:56  ounsy
// removed useless logs
//
// Revision 1.21  2006/03/08 16:26:24  ounsy
// added the global POGO class comments for the devices:
// -HDBArchiver
// -TDBArchiver
// -ArchivingWatcher
//
// Revision 1.20  2006/03/08 14:36:21  ounsy
// added pogo comments
//
// Revision 1.19  2006/03/01 15:45:33  ounsy
// if the safetyPeriod  property is missing, the default (absolute+15mn) mode is chosen
//
// Revision 1.18  2006/02/28 10:38:31  ounsy
// lastInsertRequest added for HDBArchiver internal controls
//
// Revision 1.17  2006/02/27 12:17:47  ounsy
// the HdbArchiver internal diagnosis function's safety period calculation is now defined
//  by the safetyPeriod property
//  (for example safetyPeriod=absolute/minutes/15 or safetyPeriod=relative/2)
//
// Revision 1.16  2006/02/24 12:07:30  ounsy
// removed useless logs
//
// Revision 1.15  2006/02/15 13:11:31  ounsy
// organized imports
//
// Revision 1.14  2006/02/15 13:01:25  ounsy
// added HdbArchiver internal diagnosis commands
//
// Revision 1.13  2006/02/15 11:08:59  chinkumo
// Javadoc comment update.
//
// Revision 1.12  2006/02/08 15:00:05  ounsy
// added comments for the new commands
//
// Revision 1.11  2006/02/06 13:00:46  ounsy
// new commands RetryForAttribute and RetryForAttributes
//
// Revision 1.10  2006/01/23 09:11:17  ounsy
// minor changes (reorganized imports + methods set deprecated)
//
// Revision 1.9  2006/01/13 14:28:28  chinkumo
// Changes made to avoid multi lines in AMT table when archivers are rebooted.
//
// Revision 1.8  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.7.10.6  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.7.10.5  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.7.10.4  2005/09/26 08:01:20  chinkumo
// Minor changes !
//
// Revision 1.7.10.3  2005/09/16 08:08:46  chinkumo
// Minor changes.
//
// Revision 1.7.10.2  2005/09/14 14:25:01  chinkumo
// 'trigger_archive_conf' methods were changed to allow the management of ArchivingMessConfig objects.
//
// Revision 1.7.10.1  2005/09/09 10:16:41  chinkumo
// Since the checkAttributeSupport was implemented, this class was modified.
// This enhances the support of attributes types.
//
// Revision 1.7  2005/06/24 12:06:27  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.
// This change was reported here.
//
// Revision 1.6  2005/06/15 14:00:37  chinkumo
// The device was regenerated in Tango V5.
//
// Revision 1.5  2005/06/14 10:30:28  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.4.2  2005/06/13 14:04:27  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4.4.1  2005/05/03 16:30:56  chinkumo
// Some constants in the class 'fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst' were renamed. Changes reported here.
// Some unused references removed.
//
// Revision 1.4  2005/02/04 17:10:16  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.3  2005/01/31 17:12:16  chinkumo
// Minors changes made into the logging messages of the method named 'stop_archive_att'.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:24  chinkumo
// First commit (new architecture).
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


package HdbArchiver;


import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;

import HdbArchiver.Collector.DbProxy;
import HdbArchiver.Collector.HdbCollector;
import HdbArchiver.Collector.HdbCollectorFactory;
import fr.esrf.Tango.DevError;
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
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingMessConfig;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeSupport;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DBTools;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttributeSubName;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResult;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Domain;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.ISaferPeriodCalculator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;



/**
 * Class Description:
 * This device is in charge of archiving Tango attributes to the Historic Database, as known as HDB.
 * On startup, it looks up in database the attributes it is in charge of.
 * A collector thread retrieves attribute values, with the associated timestamp.
 * For each attribute, the values are then inserted into HDB every [base period], and if so required, when its value meets certain user-defined criterions.
 *
 * @author  $Author: pierrejoseph $
 * @version $Revision: 1.89 $
 */

//--------- Start of States Description ----------
/*
 *  Device States Description:
 *  DevState.RUNNING :  The device is not avalaible because it is executing a command
 *  DevState.ON :   The device is ready to use
 *  DevState.INIT : The device has been initialized but not ready to use
 *  DevState.FAULT :    The device has been initialized but the connection to the database crashed.
 */
//--------- End of States Description ----------


public class HdbArchiver extends DeviceImpl/*WithShutdownRunnable*/ implements TangoConst,Warnable
{
    protected int state;
    private DbProxy dbProxy;

    //--------- Start of attributes data members ----------

    protected short attr_scalar_charge_read;
    protected short attr_spectrum_charge_read;
    protected short attr_image_charge_read;
    
    private String m_version; 

//--------- End of attributes data members ----------

	//--------- Start of properties data members ----------

	/**
	 * Computer identifier on wich is settled the database HDB. The identifier can be the computer name or its IP address. <br>
	 * <b>Default value : </b> Class value
	 */
	String dbHost;
	/**
	 * Database name.<br>
	 * <b>Default value : </b> hdb
	 */
	String dbName;
 
    /**
     * User identifier (name) used to connect the database HDB. <br>
     * <b>Default value : </b> Class Value
     */
    String dbUser;
    /**
     * Password used to connect the database HDB. <br>
     * <b>Default value : </b> hdb
     */
    String dbPassword;
    
    /**
     * Describes how tightly effective archiving periods have to match theoretical archiving periods, for an attribute to be declared "OK" (ie. archiving correctly)
     * Can be either:
     * <UL>
     * <LI> absolute/[hours minutes seconds]/[amount]: the effective period = the theoretical period + the specified amount
     * <LI> relative/[multiplier]: the effective period = the theoretical period * the specified multiplier
     * </UL>     
     * <b>Default value : </b>absolute/minutes/15
     */
    String safetyPeriod;
    
    /**
     * Property used to describe the startup behaviour of the device. <br>
     * If set to true, the inserts in AMT are done in another thread so that the device can be exported earlier. <br>
     * If set to false, the inserts in AMT are done in the main thread sequentially.<br>
     * <b>Default value : </b>false
     */
    boolean hasThreadedStartup;
    
    /**
     * Defines whether the archiver is a "dedicated archiver".
     * A "dedicated archiver" is an archiver specialized in archiving a specific list of attributes, and none others.
     * If the value is false, the related attribute "reservedAttributes" won't be used. 
     * <b>Default value : </b>false
     */
    boolean isDedicated;
    
    /**
     * Defines whether the archiver will log to a diary.
     * <b>Default value : </b>false
     */
    boolean hasDiary;
    
    /**
     * The criticity threshold of the archiver's logging.
     * Only messages with a criticity higher than this attribute will be logged to the diary.
     * Possible values are:
     * <UL>
     *  <LI>DEBUG
     *  <LI>INFO
     *  <LI>WARNING
     *  <LI>ERROR
     *  <LI>CRITIC
     * </UL>
     * <b>Default value : </b>DEBUG   
     */
    String diaryLogLevel;
    
    /**
     * The path of the diary logs. Don't include a filename, diary files are named automatically. 
     */
    String diaryPath;
    
    /**
     * The list of attributes that will always be archived on this archiver no matter the load-balancing situation.
     * Be aware that:
     *  - this list will be ignored if the related attribute "isDedicated" is false or missing
     *  - one attribute shouldn't be on the reservedAttributes list of several archivers
     */
    String [] reservedAttributes;

//--------- End of properties data members ----------


    //  Add your own data members here
    protected HdbCollectorFactory collectorFactory;
    public static boolean dbConnected = false;
    public static int gc_counter;
    public static int gc_counter_limit = 10000;

    public static String myDbHost;
    public static String myDbName;

    public static String myDbUser;
    public static String myDbPassword;
    public static String mySafetyPeriod;
    public static boolean myHasThreadedStartup;
    public static boolean myIsDedicated;
    public static String [] myReservedAttributes;
    public static String myDiaryPath;
    public static boolean myHasDiary;
    public static String myDiaryLogLevel;
    
    public static final short NA = 10;
    public static final short SUCCEEDED = 20;
    public static final short FAILED = 30;
     
    private ILogger logger;
    //--------------------------------------

    //--------------------------------------------------------------------------//
    // ELETTRA : Archiving Events 
    //--------------------------------------------------------------------------//
    /* Archiving related properties */
    public static Boolean isUseEvents = false;
    public static String eventDBUpdatePolicy = "amt";
    //--------------------------------------------------------------------------//
    // ELETTRA : Archiving Events 
    //--------------------------------------------------------------------------//
    
//=========================================================
    /**
     * Constructor for simulated Time Device Server.
     *
     * @param   cl  The DeviceClass object
     * @param   s   The Device name.
     * @param   version The device version
     */
//=========================================================
    HdbArchiver(DeviceClass cl , String s, String version) throws DevFailed
    {
        super(cl , s);
        m_version = version;
        init_device();
    }
//=========================================================
    /**
     * Constructor for simulated Time Device Server.
     *
     * @param   cl  The DeviceClass object
     * @param   s   The Device name.
     * @param   d   Device description.
     * @param   version The device version
     */
//=========================================================
    HdbArchiver(DeviceClass cl , String s , String d, String version) throws DevFailed
    {
        super(cl , s , d);
        m_version = version;
        init_device();
    }


//=========================================================
    /**
     * Initialize the device.
     */
//=========================================================
    public synchronized void init_device() throws DevFailed
    {
        set_state(DevState.RUNNING);
        set_status(device_name + " : " + "DevState.RUNNING");
        
        System.out.println("HdbArchiver() create " + device_name);

        collectorFactory = new HdbCollectorFactory();
        //  Initialise variables to default values
        //-------------------------------------------
        get_device_property();
        myDbHost = dbHost;
        myDbName = dbName;
        myDbUser = dbUser;
        myDbPassword = dbPassword;
        myHasThreadedStartup = hasThreadedStartup;
        myIsDedicated = isDedicated;
        myReservedAttributes = reservedAttributes;
        myDiaryPath = diaryPath;
        myHasDiary = hasDiary;        
        myDiaryLogLevel = diaryLogLevel;

        attr_image_charge_read = 0;
        attr_spectrum_charge_read = 0;
        attr_scalar_charge_read = 0;
        gc_counter = 0;

        get_logger().info("dbHost = " + dbHost);
        get_logger().info("dbName = " + dbName);

        get_logger().info("dbUser = " + dbUser);
        get_logger().info("dbPassword = " + dbPassword);
        get_logger().info("hasThreadedStartup = " + hasThreadedStartup);
        get_logger().info("isDedicated = " + isDedicated);
        get_logger().info("reservedAttributes = " + traceStrings(reservedAttributes));
        get_logger().info("diaryPath = " + diaryPath);
        get_logger().info("hasDiary = " + hasDiary);
        get_logger().info("diaryLogLevel = " + diaryLogLevel);
       
        DBTools.setWarnable ( this );
        
        //collectorFactory.checkInstance();

        this.dbProxy = new DbProxy ();
        if ( !( this.dbProxy.is_db_connected() ) )
        {
            set_state(DevState.FAULT);
            set_status(device_name + " : " + "DevState.FAULT" + "\r\n" +
                       "Historical database connection : " + "FAULT" + " (may be broken...)" + "\r\n");
            get_logger().error("ERROR : Database unconnected !!");
        }
        else
        {
            set_state(DevState.RUNNING);
            set_status(device_name + " : " + "DevState.RUNNING" + "\r\n" +
                       "Historical database connection : " + "OK");
            try
            {
                setControlMode ( mySafetyPeriod );
            }
            catch ( Throwable t )
            {
                DBTools.throwDevFailed ( t );
            }
            
            try
            {
            	this.startLoggingFactory ();
            	
                Vector myCurrentTasks = this.dbProxy.getArchiverCurrentTasks(device_name);
                if(myCurrentTasks.size() > 0)
                {
                	ArchivingMessConfig archivingMessConfig = ArchivingMessConfig.basicObjectCreation();
                	for ( int i = 0 ; i < myCurrentTasks.size() ; i++ )
                	{
                		archivingMessConfig.add(( AttributeLightMode ) myCurrentTasks.elementAt(i));
                	}
                
                              
                	boolean forceThreadedMode = false;
                	this.startArchiving ( archivingMessConfig , forceThreadedMode );
                }
            }
            catch ( ArchivingException e )
            {
                get_logger().error(e.toString());
                Util.out2.println(e.toString());
            }
            catch ( DevFailed devFailed )
            {
                String message = DBTools.getCompleteMessage ( devFailed );
                get_logger().error ( message );
            }
        }
    }
    
    /**
     * @param archivingMessConfig
     * @param forceThreadedMode
     * @throws DevFailed
     */
    private void startArchiving(ArchivingMessConfig archivingMessConfig , boolean forceThreadedMode) throws DevFailed 
    {
        StartArchivingRunnable startArchivingRunnable = new StartArchivingRunnable ( archivingMessConfig );
        Thread startArchivingThread = new Thread ( startArchivingRunnable , "HDBStartArchivingThread" ); 
        
        get_logger ().info ( "startArchiving/hasThreadedStartup/"+hasThreadedStartup );
        if ( this.hasThreadedStartup || forceThreadedMode )
        {
            startArchivingThread.start ();    
        }
        else
        {
            startArchivingThread.run ();   
        }
    }

    private class StartArchivingRunnable implements Runnable
    {
        private ArchivingMessConfig archivingMessConfig;

        public StartArchivingRunnable(ArchivingMessConfig _archivingMessConfig)
        {
            this.archivingMessConfig = _archivingMessConfig;
        }

        public synchronized void run() 
        {
            try 
            {
                trigger_archive_conf ( archivingMessConfig.toArray () );
            } 
            catch ( DevFailed devFailed )
            {
                String completeMessage = DBTools.getCompleteMessage ( devFailed );
                get_logger().error( completeMessage );
            }  
        }
    }
    
    /**
     * @param safetyPeriod2
     */
    private void setControlMode ( String _safetyPeriod ) throws DevFailed
    {
        get_logger ().info ( "setControlMode/safetyPeriod/"+_safetyPeriod );
        ISaferPeriodCalculator saferPeriodCalculator = SaferPeriodCalculatorFactory.getImpl ( _safetyPeriod );
        get_logger ().info ( "setControlMode/success/desription/"+saferPeriodCalculator.getDescription() );
    }
    //===================================================================
    /**
     * Read the device properties from database.
     */
//===================================================================
    public void get_device_property() throws DevFailed
    {
        //  Initialize your default values here.
        //------------------------------------------
        dbUser = ConfigConst.HDB_ARCHIVER_USER;
        dbPassword = ConfigConst.HDB_ARCHIVER_PASSWORD;
        hasThreadedStartup = false;
        isDedicated = false;
        reservedAttributes = null;
        diaryPath = null;
        hasDiary = false;
        diaryLogLevel = ILogger.DEBUG;

        //  Read device properties from database.(Automatic code generation)
        //-------------------------------------------------------------
        if ( Util._UseDb == false )
            return;
        String[] propnames = {
       		"DbHost",
       		"DbName",
            "DbUser",
            "DbPassword",
            "SafetyPeriod",
            "HasThreadedStartup",
            "IsDedicated",
            "ReservedAttributes",
            "DiaryPath",
            "HasDiary",
            "DiaryLogLevel",
            //--------------------------------------------------------------------------//
            // ELETTRA : Archiving Events 
            //--------------------------------------------------------------------------//
			"UseEvents",
            "EventsDBUpdatePolicy"
		    //--------------------------------------------------------------------------//
		    // ELETTRA : Archiving Events 
		    //--------------------------------------------------------------------------//
        };

        //  Call database and extract values
        //--------------------------------------------
        DbDatum[] dev_prop = get_db_device().get_property(propnames);
        HdbArchiverClass ds_class = ( HdbArchiverClass ) get_device_class();
        int i = -1;
        //  Extract DbHost value
        if ( dev_prop[ ++i ].is_empty() == false )
            dbHost = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) dbHost = cl_prop.extractString();
        }
        
        //  Extract DbName value
        if ( dev_prop[ ++i ].is_empty() == false )
            dbName = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) dbName = cl_prop.extractString();
        }
    
        
        //  Extract DbUser value
        if ( dev_prop[ ++i ].is_empty() == false )
            dbUser = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) dbUser = cl_prop.extractString();
        }

        //  Extract DbPassword value
        if ( dev_prop[ ++i ].is_empty() == false )
            dbPassword = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) dbPassword = cl_prop.extractString();
        }
        
        //Extract SafetyPeriod value
        if ( dev_prop[ ++i ].is_empty() == false )
            mySafetyPeriod = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) mySafetyPeriod = cl_prop.extractString();
        }

        //  Extract HasThreadedStartup value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
            hasThreadedStartup = dev_prop[ i ].extractBoolean();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
                hasThreadedStartup = cl_prop.extractBoolean();
            }
        }
        
        // Extract IsDedicated value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
            isDedicated = dev_prop[ i ].extractBoolean();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
                isDedicated = cl_prop.extractBoolean();
            }
        }
        
        // Extract ReservedAttributes value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
            reservedAttributes = dev_prop[ i ].extractStringArray();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
                reservedAttributes = cl_prop.extractStringArray();
            }
        }
         
        // Extract DiaryPath value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
            diaryPath = dev_prop[ i ].extractString ();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
                diaryPath = cl_prop.extractString ();
            }
        }
        
        // Extract HasDiary value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
            hasDiary = dev_prop[ i ].extractBoolean();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
                hasDiary = cl_prop.extractBoolean();
            }
        }
        
        // Extract DiaryLogLevel value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
            diaryLogLevel = dev_prop[ i ].extractString();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
                diaryLogLevel = cl_prop.extractString();
            }
        }
        //  End of Automatic code generation
        //-------------------------------------------------------------
        
        //--------------------------------------------------------------------------//
        // ELETTRA : Archiving Events 
        //--------------------------------------------------------------------------//
//      Extract UseEvents value
        System.out.print("Seeing if events are enabled...");
        if ( dev_prop[ ++i ].is_empty() == false )
        {
        	System.out.print(" in the device properties (" + dev_prop[ i ].name + ")... ");
            isUseEvents = dev_prop[ i ].extractBoolean();
        }
        else
        {
            //  Try to get value from class property
        	System.out.print(" (empty)\nLooking in class properties (" + dev_prop[ i ].name + ")... ");
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
            	isUseEvents = cl_prop.extractBoolean();
            }
            else
            	System.out.print("(empty) ");
        }
        System.out.println(isUseEvents);
        
        // Extract EventsDBUpdatePolicy value
        if ( dev_prop[ ++i ].is_empty() == false )
        {
        	eventDBUpdatePolicy = dev_prop[ i ].extractString();
        }
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false )
            {
            	eventDBUpdatePolicy = cl_prop.extractString();
            }
        }
        System.out.println("The database priority is set to " + eventDBUpdatePolicy);
        
        //--------------------------------------------------------------------------//
        // ELETTRA : Archiving Events 
        //--------------------------------------------------------------------------//

    }
//=========================================================
    /**
     * Method always executed before command execution.
     */
//=========================================================
    public void always_executed_hook()
    {
        get_logger().info("In always_executed_hook method()");
    }

//===================================================================
    /**
     * Method called by the read_attributes CORBA operation to
     * read device hardware
     *
     * @param   attr_list   Vector of index in the attribute vector
     * of attribute to be read
     */
//===================================================================
    public void read_attr_hardware(Vector attr_list)
    {
        get_logger().info("In read_attr_hardware for " + attr_list.size() + " attribute(s)");

        //  Switch on attribute name
        //---------------------------------
    }
//===================================================================
    /**
     * Method called by the read_attributes CORBA operation to
     * set internal attribute value.
     *
     * @param   attr    reference to the Attribute object
     */
//===================================================================
    public void read_attr(Attribute attr) throws DevFailed
    {
        String attr_name = attr.get_name();
        get_logger().info("In read_attr for attribute " + attr_name);

        //  Switch on attribute name
        //---------------------------------
        if ( attr_name == "scalar_charge" )
        {
            //  Add your own code here
            attr.set_value(this.attr_scalar_charge_read);
        }
        else if ( attr_name == "spectrum_charge" )
        {
            //  Add your own code here
            attr.set_value(attr_spectrum_charge_read);
        }
        else if ( attr_name == "image_charge" )
        {
            //  Add your own code here
            attr.set_value(attr_image_charge_read);
        }
        else if ( attr_name == "version" )
        {
            //  Add your own code here
            attr.set_value(m_version);
        }
    }


//=========================================================
    /**
     * Execute command "TriggerArchiveConf" on device.
     * This command is invoked when the archiving of a group of attributes is triggered.
     * The group of attributes is therefore encapsulated in an ArchivingMessConfig type object.
     * The use of this command suppose that the database is ready to host those attributes's values.
     * That means registrations were made, the associated tables built, ...
     *
     * @param   argin   The group of attributes to archive
     * @see ArchivingMessConfig
     * @see AttributeLightMode
     */
//=========================================================
    public synchronized void trigger_archive_conf(String[] argin) throws DevFailed
    {
        set_state(DevState.RUNNING);
        set_status(device_name + " : " + "DevState.RUNNING");
        
        get_logger().info("Entering trigger_archive_conf()");
        this.logger.trace ( ILogger.LEVEL_INFO , "===> Entering trigger_archive_conf()");
        
        ArchivingException archivingException = new ArchivingException();
        ArchivingMessConfig archivingMessConfig = ArchivingMessConfig.creationWithFullInformation(argin);
        archivingMessConfig.filter ( this.isDedicated , this.reservedAttributes , this.logger );
        for ( Enumeration my_EnumAttributes = archivingMessConfig.getAttributeListKeys() ; my_EnumAttributes.hasMoreElements() ; )
        {
            String m_attribute = String.valueOf(my_EnumAttributes.nextElement());
            this.logger.trace ( ILogger.LEVEL_INFO , "===> Attribute Name = " + m_attribute);
            
            AttributeLightMode attributeLightMode = ( AttributeLightMode ) archivingMessConfig.getAttribute(m_attribute);
            attributeLightMode.setTrigger_time(new Timestamp(System.currentTimeMillis()));
            boolean db_update = false , att_support = false;
            String att_complete_name = attributeLightMode.getAttribute_complete_name();
         
            try
            {
    
                att_support = AttributeSupport.checkAttributeSupport(att_complete_name , attributeLightMode.getData_type() , attributeLightMode.getData_format() , attributeLightMode.getWritable());
         
                if ( att_support )
                {
                    try
                    {
                        attributeLightMode.getMode().checkMode(true);
                    }
                    catch ( ArchivingException ae )
                    {
                        //next attribute
                        String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                        String reason = "Failed while executing HdbArchiver.trigger_archive_conf() method...";
                        String desc = "Invalid mode for attribute " + att_complete_name;
                        archivingException.addStack(message , reason , ErrSeverity.PANIC , desc , "" , ae);
                        this.logger.trace ( ILogger.LEVEL_ERROR , reason + " " + desc);
                        continue;
                    }
                    
                    attributeLightMode.setDevice_in_charge(device_name);
                    // Garanti l'unicit� de lignes "NULL" dans l'AMT !!
                    
                    //if ( this.dbProxy.isArchived(attributeLightMode.getAttribute_complete_name()) )
                        //this.dbProxy.updateModeRecord(attributeLightMode.getAttribute_complete_name());
                    
                    //--------------------------------------------------------------------------//
                    // ELETTRA : Archiving Events 
                    //--------------------------------------------------------------------------//
//////////////////////////////new method //////////
					/* Can be put in the hdbArchiver.java.
					 * We can call it updateAMT()
					 */
					
					/* if(events_enabled() )
					 * {	
					 * 	if(overwriteAMTFromTangoDB() )
					 * 		{	
					 * 				getDataFromTangoDB();	
					 * 				attributeLightMode.setPeriod();
					 *              attributeLightMode.setAbsChange();
					 *              attributeLightMode.setRelativeChange();
					 * 		}
					 * }
					 */
                    
                    // SPZJ : Is it really the good place ?
                    //--------------------------------------------------------------------------//
                    // ELETTRA : Archiving Events 
                    //--------------------------------------------------------------------------//
                    
                    // if the attribute is already archived, ignore it
                    if ( this.dbProxy.isArchived(att_complete_name) )
                    {
                        if ( this.dbProxy.isArchived(att_complete_name,device_name ) )
                        {
                            try
                            {                                
                                //collectorFactory.removeAllForAttribute( attributeLightMode.getAttribute_complete_name() );
                            	this.logger.trace ( ILogger.LEVEL_INFO , "===> Entering removeAllForAttribute");
                                collectorFactory.removeAllForAttribute( attributeLightMode );
                                this.logger.trace ( ILogger.LEVEL_INFO , "===> Exiting removeAllForAttribute");
                                this.dbProxy.updateModeRecord(att_complete_name );
                            }
                            catch (ArchivingException ae)
                            {
                            	this.logger.trace ( ILogger.LEVEL_ERROR , "Failed while executing HdbArchiver.trigger_archive_conf() method : removeAllForAttribute or updateModeRecord");
                                ae.printStackTrace();
                                continue;
                            }
                        }
                        else 
                        {
                            continue;
                        }
                    }
                    
                    try
                    {
                        this.dbProxy.insertModeRecord(attributeLightMode);
                    }
                    catch(Exception e)
                    {
                        this.logger.trace ( ILogger.LEVEL_ERROR , "############################################################" );
                        this.logger.trace ( ILogger.LEVEL_ERROR , "Insertion failed in AMT for the att : " + att_complete_name + " with following exception" );
                        this.logger.trace ( ILogger.LEVEL_ERROR , e );
                        this.logger.trace ( ILogger.LEVEL_ERROR , "############################################################" );
                        
                        // if the attribute is already archived, ignore it
                        continue;
                    }
                    
                    Integer period = new Integer ( attributeLightMode.getMode ().getModeP ().getPeriod () );
                    this.dbProxy.setPeriodForAttribute ( att_complete_name , period );
                    
                    //--------------------------------------------------------------------------//
                    // ELETTRA : Archiving Events 
                    //--------------------------------------------------------------------------//
                    
                    /* here we could call another new method to update the tango db */
					/* if(events_enabled() )
					 * {	
					 * 	if(overwriteTangoDbFromAMT() )
					 * 		{	
					 * 				// attributeLightMode contains the properies from amt
					 * 				updateTangoDbFromAmt(attributeLightMode);	
					 * 						
					 * 		}
					 * }
					 *
					 */
////////////        end new method /////////////////
                    //--------------------------------------------------------------------------//
                    // ELETTRA : Archiving Events 
                    //--------------------------------------------------------------------------//
                    
                    db_update = true;
                    HdbCollector m_collector = null;
                    this.logger.trace ( ILogger.LEVEL_INFO , "===> Entering get collector");
                    m_collector = collectorFactory.get(attributeLightMode,this.logger,true,this.dbProxy);
                    this.logger.trace ( ILogger.LEVEL_INFO , "===> Exiting get collector + Entering addSource ");
                    m_collector.addSource(attributeLightMode);
                    this.logger.trace ( ILogger.LEVEL_INFO , "===> Exiting addSource ");
                }
            }
            catch ( ArchivingException e )
            {
                e.printStackTrace();
                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                String reason = "Failed while executing HdbArchiver.trigger_archive_conf() method...";
                String desc = ( att_support ) ?
                              ( db_update ) ?
                              "Unable to find a collector for the attribute named " + att_complete_name + " ! " + "\r\n" + "Mode parameters should be checked..." :
                              "Unable to register the attribute named " + att_complete_name + " in the mode table !" :
                              "Attribute named " + att_complete_name + " have not supported type, format or writable property!" ;
                archivingException.addStack(message , reason , ErrSeverity.PANIC , desc , "" , e);
                
                this.logger.trace ( ILogger.LEVEL_ERROR , reason + " " + desc);
            }
            catch ( Exception e )
            {
                this.logger.trace ( ILogger.LEVEL_ERROR , "ATTENTION=====>Failed while executing HdbArchiver.trigger_archive_conf() method see exception " + e);
                e.printStackTrace();
            }
        }
        
        this.computeLoads ();
        
        set_state(DevState.ON);
        set_status(device_name + " : " + "DevState.ON");
        
        if ( !archivingException.getMessage().equals("") )
        {
            this.logWarningAboutRiskySettingOfStateBackToOn ( "TriggerArchiveConf" , archivingException );
            throw archivingException.toTangoException();
        }
        
        this.logger.trace ( ILogger.LEVEL_INFO , "===> Exiting trigger_archive_conf()");
        get_logger().info("Exiting trigger_archive_conf()");
    }


    private void logWarningAboutRiskySettingOfStateBackToOn ( String methodName , Exception e ) 
    {
        String openingLine = "Setting the state back to ON despite encountering exceptions in " + methodName + " VVVVVVVVVVV";
        String closingLine = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
        this.logger.trace ( ILogger.LEVEL_WARNING , openingLine );
        this.logger.trace ( ILogger.LEVEL_WARNING , e );
        this.logger.trace ( ILogger.LEVEL_WARNING , closingLine );
    }
    private void delay() 
    {
        System.out.println ("CLA/delay/BEFORE/"+now());
        long duration = 1; //millis
        duration *= 1000; //seconds
        duration *= 10;
        try {
            Thread.sleep ( duration );
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println ("CLA/delay/AFTER/"+now());    
    }
    private Timestamp now() 
    {
        return new Timestamp ( System.currentTimeMillis () );
    }
    //=========================================================
    /**
     * Execute command "TriggerArchiveAtt" on device.
     * This command is invoked when the archiving of an attribute is triggered.
     * The attribute to archive is therefore encapsulated in a fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLight type object.
     * The use of this command suppose that the HDB database is ready to host that attribute's values.
     * That means the registration was made, the associated table built, ...
     *
     * @param   argin   The attribute to archive
     * @see ArchivingMessConfig
     * @see AttributeLightMode
     * @deprecated use trigger_archive_conf(String[] argin) instead
     */
//=========================================================
    public synchronized void trigger_archive_att(String[] argin) throws DevFailed
    {
        set_state(DevState.RUNNING);
        set_status(device_name + " : " + "DevState.RUNNING");
        
        get_logger().info("Entering trigger_archive_att()");

        ArchivingException archivingException = new ArchivingException();
        AttributeLightMode attributeLightMode = AttributeLightMode.creationWithFullInformation(argin);
        HdbCollector collector = null;
        boolean att_support = false;
        boolean mode_valid = false;
        //boolean att_alive = false;
        try
        {
            att_support = AttributeSupport.checkAttributeSupport(attributeLightMode.getAttribute_complete_name() , attributeLightMode.getData_type() , attributeLightMode.getData_format() , attributeLightMode.getWritable());
            if ( att_support )
            {
                attributeLightMode.getMode().checkMode(true);
                mode_valid = true;
                collector = collectorFactory.get(attributeLightMode,this.logger,true, this.dbProxy);
                collector.addSource(attributeLightMode);
            }
        }
        catch ( ArchivingException e )
        {
            get_logger().error(e.toString());
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
            String reason = "Failed while executing HdbArchiver.trigger_archive_att() method...";
            String desc = "Unable to archive the attribute named " + attributeLightMode.getAttribute_complete_name();
            if ( !att_support )
                desc = desc + "\r\n" + "Type, format or writable property should be checked!";
            else if ( !mode_valid )
                desc = desc + "\r\n" + "Mode parameters should be checked...";
            else
            {
                desc = desc + "\r\n" + "The attribute is unreachable... may be OFF or DOWN";
                // Correction des donn�es de la base : Cas o� les �quipements ne tournent pas au red�marrage de l'archiver.
                try
                {
                    this.dbProxy.updateModeRecord(attributeLightMode);
                }
                catch ( ArchivingException e1 )
                {
                    e1.printStackTrace();
                }
            }
            
            this.logWarningAboutRiskySettingOfStateBackToOn ( "TriggerArchiveAtt" , e );
            
            archivingException.addStack(message , reason , ErrSeverity.PANIC , desc , "" , e);
            throw e.toTangoException();
        }
        finally
        {
            this.computeLoads ();

            set_state(DevState.ON);
            set_status(device_name + " : " + "DevState.ON");    
        }
        
        get_logger().info("Exiting trigger_archive_att()");
    }


//=========================================================
    /**
     * Execute command "TriggerArchiveAttCheck" on device.
     * This command is invoked when the archiving of an attribute is triggered.
     * Before archiving, this command checks that HDB database is ready to host the given attribute values.
     * If not registrations is done and the associated table is built.
     *
     * @param   argin   The name of the attribute to archive
     * @deprecated use trigger_archive_conf(String[] argin) instead
     */
//=========================================================
    public synchronized void trigger_archive_att_check(String argin) throws DevFailed
    {
        set_state(DevState.RUNNING);
        set_status(device_name + " : " + "DevState.RUNNING");
        
        get_logger().info("Entering trigger_archive_att_check()");
        
        get_logger().info("Exiting trigger_archive_att_check()");
        
        set_state(DevState.ON);
        set_status(device_name + " : " + "DevState.ON");
    }


//=========================================================
    /**
     * Execute command "StopArchiveConf" on device.
     * This command is invoked when stopping the archiving of a group of attributes.
     *
     * @param   argin   The group of attributes
     */
//=========================================================
    public synchronized void stop_archive_conf(String[] argin) throws DevFailed
    {
        set_state(DevState.RUNNING);
        set_status(device_name + " : " + "DevState.RUNNING");
        
        get_logger().info("Entering stop_archive_conf()");
        this.logger.trace ( ILogger.LEVEL_INFO , "===> Entering stop_archive_conf()");
        
        Vector myConf = ArchivingManagerApi.stoppingVector(argin);
        for ( int i = 0 ; i < myConf.size() ; i++ )
        {
            stop_archive_att(( ( AttributeLightMode ) myConf.elementAt(i) ).toArray());
        }
        
        set_state(DevState.ON);
        set_status(device_name + " : " + "DevState.ON");
        
        get_logger().info("Exiting stop_archive_conf()");
        this.logger.trace ( ILogger.LEVEL_INFO , "===> Exiting stop_archive_conf()");
    }

    //=========================================================
    /**
     * Execute command "RetryForAttribute" on device.
     * Tries to start archiving for a given attribute, specified by its complete name.
     * @param attributeToRetry The complete name of the attribute
     * @return  A return code, can be either: 10 (the archiver isn't in charge of the specified attribute) 20 (the retry succeeded) or 30 (the retry failed)
     */
    //=========================================================
    public synchronized short retry_for_attribute ( String attributeToRetry )
    {
        if ( attributeToRetry == null || attributeToRetry.equals ( "" ) )
        {
            return NA;
        }
        try 
        {
            Vector myCurrentTasks = this.dbProxy.getArchiverCurrentTasks(device_name);
            
            ArchivingMessConfig archivingMessConfig = ArchivingMessConfig.basicObjectCreation();
            
            boolean configIsNotEmpty = false;
            for ( int i = 0 ; i < myCurrentTasks.size() ; i++ )
            {
                AttributeLightMode attributeLightMode = ( AttributeLightMode ) myCurrentTasks.elementAt(i);
                String attrName = attributeLightMode.getAttribute_complete_name ();
                if ( attrName.equals ( attributeToRetry ) )
                {
                    archivingMessConfig.add(attributeLightMode);
                    configIsNotEmpty = true;
                    break;
                }
            }
            
            if ( configIsNotEmpty )
            {
                this.logRetry ( archivingMessConfig , "retry_for_attribute" );
                
                boolean forceThreadedMode = true;
                this.startArchiving ( archivingMessConfig , forceThreadedMode );
                
                return SUCCEEDED;
            }
            else
            {
                return NA;
            }
        } 
        catch (DevFailed e) 
        { 
            String message = DBTools.getCompleteMessage ( e );
            get_logger().warn ( "Failed during retry_for_attribute ( " + attributeToRetry + " ):");
            get_logger().warn ( message );
            
            return FAILED;
        }
        catch (Throwable t) 
        {
            DevFailed devFailed = new DevFailed ();
            devFailed.initCause ( t );
            devFailed.setStackTrace ( t.getStackTrace () );
            
            String message = DBTools.getCompleteMessage ( devFailed );
            get_logger().warn ( "Failed during retry_for_attribute ( " + attributeToRetry + " ):");
            get_logger().warn ( message );
            
            return FAILED;
        }
    }
    
    //=========================================================
    /**
     * Execute command "RetryForAttributes" on device.
     * Tries to start archiving for a given list of attributes, specified by their complete names.
     * @param attributesToRetry The complete names of the attributes
     * @return  A return code, can be either: 10 (the archiver isn't in charge of any of the specified attributes) 20 (the retry succeeded) or 30 (the retry failed)
     */
    //=========================================================
    public synchronized short retry_for_attributes ( String [] attributesToRetry )
    {
        if ( attributesToRetry == null || attributesToRetry.length == 0 )
        {
            return NA;
        }
        StringBuffer argsBuffer = new StringBuffer ();
        
        try 
        {
            Vector myCurrentTasks = this.dbProxy.getArchiverCurrentTasks(device_name);
            Hashtable modes = new Hashtable ( myCurrentTasks.size() );
            
            for ( int i = 0 ; i < myCurrentTasks.size() ; i++ )
            {
                AttributeLightMode attributeLightMode = (AttributeLightMode) myCurrentTasks.elementAt(i);
                String attrName = attributeLightMode.getAttribute_complete_name ();
                modes.put ( attrName , attributeLightMode );
            }
            if ( modes.size () == 0 )
            {
                return NA;
            }
            
            boolean configIsNotEmpty = false;  
            ArchivingMessConfig archivingMessConfig = ArchivingMessConfig.basicObjectCreation();
            for ( int j = 0 ; j < attributesToRetry.length ; j++ )
            {
                String currentName = attributesToRetry [ j ];
                argsBuffer.append ( currentName );
                if ( j < attributesToRetry.length - 1 )
                {
                    argsBuffer.append ( " , " );
                }
                
                AttributeLightMode attributeLightMode = (AttributeLightMode) modes.get ( currentName );
                if ( attributeLightMode != null )
                {
                    archivingMessConfig.add(attributeLightMode);
                    configIsNotEmpty = true;
                }
            }
            
            if ( configIsNotEmpty )
            {
                this.logRetry ( archivingMessConfig , "retry_for_attributes" );
                
                boolean forceThreadedMode = true;
                this.startArchiving ( archivingMessConfig , forceThreadedMode );
                
                return SUCCEEDED;
            }
            else
            {
                return NA;
            }
        } 
        catch (DevFailed e) 
        { 
            String message = DBTools.getCompleteMessage ( e );
            get_logger().warn ( "Failed during retry_for_attributes ( " + argsBuffer.toString () + " ):");
            get_logger().warn ( message );
            
            return FAILED;
        }
        catch (Throwable t) 
        {
            DevFailed devFailed = new DevFailed ();
            devFailed.initCause ( t );
            devFailed.setStackTrace ( t.getStackTrace () );
            
            String message = DBTools.getCompleteMessage ( devFailed );
            get_logger().warn ( "Failed during retry_for_attributes ( " + argsBuffer.toString () + " ):");
            get_logger().warn ( message );
            
            return FAILED;
        }
    }
    
    //=========================================================
    /**
     * Execute command "RetryForKoAttributes" on device.
     * Looks up the complete list of attributes that:
     * <UL>
     *  <LI>the archiver is supposed to be taking care of
     *  <LI>aren't correctly archiving, according to the archiver's internal controls 
     * </UL>
     * , then starts archiving on all of them. 
     * @return  A return code, can be either: 10 (the archiver isn't in charge of any attribute, or they are all correctly archiving) 20 (the retry succeeded) or 30 (the retry failed) 
     */
    //=========================================================
    public synchronized short retry_for_ko_attributes ()
    {
        try
        {
            Vector [] listOfControlledAttributes = this.getControlledAttributes ();
            Vector listOfKOAttributes = listOfControlledAttributes [ 0 ];
            String [] ret = new String [ listOfKOAttributes.size () ];
            for ( int i = 0 ; i < listOfKOAttributes.size() ; i++ )
            {
                ret [ i ] = (String) listOfKOAttributes.elementAt ( i );                
            }
            
            this.logger.trace ( ILogger.LEVEL_INFO , "retry_for_ko_attributes/calling retry_for_attributes" );
            
            return this.retry_for_attributes ( ret );
        }
        catch (DevFailed e) 
        { 
            String message = DBTools.getCompleteMessage ( e );
            get_logger().warn ( "Failed during retry_for_ko_attributes ():");
            get_logger().warn ( message );
            
            return FAILED;
        }
        catch (Throwable t) 
        {
            DevFailed devFailed = new DevFailed ();
            devFailed.initCause ( t );
            devFailed.setStackTrace ( t.getStackTrace () );
            
            String message = DBTools.getCompleteMessage ( devFailed );
            get_logger().warn ( "Failed during retry_for_ko_attributes ():");
            get_logger().warn ( message );
            
            return FAILED;
        }
    }
    
    private ControlResult getControlResult () throws DevFailed, ArchivingException
    {
        try 
        {
            ControlResult cr = new ControlResult ();
            Vector [] listOfControlledAttributes = this.getControlledAttributes ();
            
            cr.setLines ( listOfControlledAttributes );
            
            System.out.println ( cr.getReport () );
            
            return cr;
        } 
        catch (DevFailed e) 
        {
            e.printStackTrace ();
            String message = DBTools.getCompleteMessage ( e );
            get_logger().error ( message );
            throw e;            
        } 
        catch (Throwable t) 
        {
            t.printStackTrace ();
            
            String message = t.getMessage();
            get_logger().error ( message );
            
            DevFailed toThrow = DBTools.getAsDevFailed ( t );
            throw ( toThrow );
        }
    }
    
    //=========================================================
    /**
     * Execute command "IsAttributeCorrectlyArchivedCurrent" on device.
     * Returns whether a given attribute, specified by its complete name, is correctly archiving, based on the archiver's internal controls.  
     * @param   argin   The complete name of the attribute
     * @return  True if this attribute is archiving correctly
     */
    //=========================================================
    public synchronized boolean is_attribute_correctly_archived_internal(String argin) throws DevFailed
    {
        ControlResult _controlResult = null;
        try 
        {
            _controlResult = this.getControlResult ();
        } 
        catch (DevFailed e) 
        {
            String message = DBTools.getCompleteMessage ( e );
            get_logger().error ( message );
            
            throw e;
        }
        catch (Throwable t) 
        {
            t.printStackTrace ();
            DBTools.throwDevFailed ( t );
        }
        
        if ( _controlResult == null )
        {
            throw new DevFailed ( getNotYetReadyError () );     
        }
        
        return  _controlResult.isAttributeCorrectlyArchived ( argin );
    }
    
    //=========================================================
    /**
     *  Execute command "getErrorsForDomainInternal" on device.
     *  Returns the list of all KO attributes (attributes that aren't correctly archiving) for a given domain name, based on the archiver's internal controls.    
     * @param   argin   The domain name
     * @return  The list of all KO attributes for this domain
     */
    //=========================================================
    public String[] get_errors_for_domain_internal ( String argin) throws DevFailed
    {
        String [] empty = new String [ 0 ];
        ControlResult _controlResult = null;
        //System.out.println ( "CLA--------------------------1" );
        try 
        {
            _controlResult = this.getControlResult ();
        } 
        catch (DevFailed e) 
        {
            String message = DBTools.getCompleteMessage ( e );
            get_logger().error ( message );
            throw e;
        }
        catch (Throwable t) 
        {
            t.printStackTrace ();
            DBTools.throwDevFailed ( t );
        }
        
        if ( _controlResult == null )
        {
            return empty;   
        }
        
        Hashtable _errorDomains = _controlResult.getErrorDomains ();
        if ( _errorDomains == null )
        {
            return empty;
        }
        
        Domain domain = (Domain) _errorDomains.get ( argin );
        if ( domain == null )
        {
            return empty;
        }
        
        Hashtable _errorAttributes = domain.getKOAttributes ();
        if ( _errorAttributes == null )
        {
            return empty;
        }
        
        String [] argout = new String [ _errorAttributes.size () ];
        Enumeration keys = _errorAttributes.keys ();
        int i = 0;
        
        while ( keys.hasMoreElements () )
        {
            String key = (String) keys.nextElement ();
            ArchivingAttribute attribute = (ArchivingAttribute) _errorAttributes.get ( key );
            
            String line = this.formatLine ( attribute );
            
            argout [ i ] = line;
            i++;
        }
        
        return argout;
    }
    
    private String formatLine(ArchivingAttribute attribute) 
    {
        StringBuffer buff = new StringBuffer ();
        
        String completeName = attribute.getCompleteName ();
        int period = attribute.getPeriod();
        Timestamp lastInsert = attribute.getLastInsert ();
        Timestamp lastInsertRequest = attribute.getLastInsertRequest ();
        
        buff.append ( completeName );
        buff.append ( "          period: " + period ); 
        buff.append ( "     lastInsert: " + lastInsert );
        buff.append ( "     lastInsertRequest: " + lastInsertRequest );
        
        String ret = buff.toString ();
        return ret;
    }
    //=========================================================
    /**
     *  Execute command "getErrorDomainsInternal" on device.
     *  Returns the list of all domains that have at least one attribute not correctly archiving, based on the archiver's internal controls.    
     * @return  The list of all domains that have at least one attribute not correctly archiving
     */
    //=========================================================
    public String[] get_error_domains_internal() throws DevFailed
    {
        ControlResult _controlResult = null;
        try 
        {
            _controlResult = this.getControlResult ();
        } 
        catch (DevFailed e) 
        {
            String message = DBTools.getCompleteMessage ( e );
            get_logger().error ( message );
            throw e;
        }
        catch (Throwable t) 
        {
            t.printStackTrace ();
            DBTools.throwDevFailed ( t );
        }
        
        String [] empty = new String [ 0 ];
        if ( _controlResult == null )
        {
            return empty;   
        }
        
        Hashtable _errorDomains = _controlResult.getErrorDomains ();
        String [] argout = new String [ _errorDomains.size () ];
        Enumeration keys = _errorDomains.keys ();
        int i = 0;
        
        while ( keys.hasMoreElements () )
        {
            String key = (String) keys.nextElement ();
            //Archiver archiver = (Archiver) _errorArchivers.get ( key );
            argout [ i ] = key;
            i++;
        }

        return argout;
    }
    
    //=========================================================
    /**
     *  Execute command "getErrorsForAttributeInternal" on device.
     *  Returns the list of all KO attributes (attributes that aren't correctly archiving) for a given attribute name, based on the archiver's internal controls.    
     * @param   argin   The attribute name
     * @return  The list of all KO attributes sharing this name
     */
    //=========================================================
    public String []  get_errors_for_attribute_internal(String argin) throws DevFailed
    {
        ControlResult _controlResult = null;
        //System.out.println ( "CLA--------------------------1" );
        try 
        {
            _controlResult = this.getControlResult ();
        } 
        catch (DevFailed e) 
        {
            String message = DBTools.getCompleteMessage ( e );
            get_logger().error ( message );
            throw e;
        }
        catch (Throwable t) 
        {
            t.printStackTrace ();
            DBTools.throwDevFailed ( t );
        } 
        
        String [] empty = new String [ 0 ];
        if ( _controlResult == null )
        {
            return empty;   
        }
        
        Hashtable _errorAttributeSubNames = _controlResult.getErrorSubAttributes ();
        if ( _errorAttributeSubNames == null )
        {
            return empty;
        }
        
        ArchivingAttributeSubName attributeSubName = (ArchivingAttributeSubName) _errorAttributeSubNames.get ( argin );
        if ( attributeSubName == null )
        {
            return empty;
        }
        
        Hashtable _errorAttributes = attributeSubName.getKOAttributes ();
        if ( _errorAttributes == null )
        {
            return empty;
        }
        
        String[] argout = new String [ _errorAttributes.size () ];
        Enumeration keys = _errorAttributes.keys ();
        int i = 0;
        
        while ( keys.hasMoreElements () )
        {
            String key = (String) keys.nextElement ();
            ArchivingAttribute attribute = (ArchivingAttribute) _errorAttributes.get ( key );
            
            String line = this.formatLine ( attribute );
            
            argout [ i ] = line;
            i++;
        }

        return argout;
    }
    
    /**
     * @return
     * @throws ArchivingException
     * @throws DevFailed
     */
    private Vector [] getControlledAttributes() throws ArchivingException, DevFailed 
    {
        try
        {
            Vector myCurrentTasks = this.dbProxy.getArchiverCurrentTasks(device_name);
            Vector koAttrs = new Vector ();
            Vector okAttrs = new Vector ();
            
            for ( int i = 0 ; i < myCurrentTasks.size() ; i++ )
            {
                AttributeLightMode mode = (AttributeLightMode) myCurrentTasks.elementAt ( i );
                String completeName = mode.getAttribute_complete_name ();
        
                boolean isOK = this.dbProxy.hasBeenArchiving ( completeName );
                
                if ( ! isOK )
                {
                    koAttrs.add ( completeName );    
                }
                else
                {
                    okAttrs.add ( completeName );
                }
            }
            
            Vector [] ret = new Vector [ 2 ];
            ret [ 0 ] = koAttrs;
            ret [ 1 ] = okAttrs;
            return ret;
        }
        catch ( Throwable t )
        {
            t.printStackTrace ();
            
        }
        return null;
    }
    
    //=========================================================
    /**
     * Execute command "RetryForAll" on device.
     * Looks up the complete list of attributes the archiver is supposed to be taking care of, then starts archiving on all of them. 
     * Those that are already archiving aren't impacted.
     * @return  A return code, can be either: 10 (the archiver isn't in charge of any attribute) 20 (the retry succeeded) or 30 (the retry failed) 
     */
    //=========================================================
    public synchronized short retry_for_all ()
    {
        try
        {
            Vector myCurrentTasks = this.dbProxy.getArchiverCurrentTasks(device_name);
            
            ArchivingMessConfig archivingMessConfig = ArchivingMessConfig.basicObjectCreation();
            for ( int i = 0 ; i < myCurrentTasks.size() ; i++ )
            {
                archivingMessConfig.add(( AttributeLightMode ) myCurrentTasks.elementAt(i));
            }
            
            if ( myCurrentTasks.size() > 0 )
            {
                this.logRetry ( archivingMessConfig , "retry_for_all" );
                
                boolean forceThreadedMode = true;
                this.startArchiving ( archivingMessConfig , forceThreadedMode );
                
                return SUCCEEDED;
            }
            else
            {
                return NA;
            }
        }
        catch (DevFailed e) 
        { 
            String message = DBTools.getCompleteMessage ( e );
            get_logger().warn ( "Failed during retry_for_all ():");
            get_logger().warn ( message );
            
            return FAILED;
        }
        catch (Throwable t) 
        {
            DevFailed devFailed = new DevFailed ();
            devFailed.initCause ( t );
            devFailed.setStackTrace ( t.getStackTrace () );
            
            String message = DBTools.getCompleteMessage ( devFailed );
            get_logger().warn ( "Failed during retry_for_all ():");
            get_logger().warn ( message );
            
            return FAILED;
        }
    }
    
    
    private void logRetry(ArchivingMessConfig archivingMessConfig, String methodName) 
    {
        String retryContent = archivingMessConfig.toString();
        String openingLine = methodName + "/retry for the following attributes VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV";
        String closingLine = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
        
        this.logger.trace ( ILogger.LEVEL_INFO , openingLine );
        this.logger.trace ( ILogger.LEVEL_INFO , retryContent );
        this.logger.trace ( ILogger.LEVEL_INFO , closingLine );
    }
    
    private synchronized void computeLoads() 
    {
        short [] loads = collectorFactory.factoryLoadAssessment();
        
        this.attr_scalar_charge_read = loads [ 0 ];
        this.attr_spectrum_charge_read = loads [ 1 ]; 
        this.attr_image_charge_read = loads [ 2 ]; 
    }
    //=========================================================
    /**
     * Execute command "StopArchiveAtt" on device.
     * Command that stops the archiving of an attibute.
     * This command need an AttributeLightMode type object.
     * An AttributeLightMode type object encapsulate all the informations needed to found the Collector in charge of the archiving of the specified attribute
     * The informations needed are the type, the format, the writable property and the archiving mode
     *
     * @param   argin   the attribute on witch archiving must be stopped
     */
//=========================================================
    public synchronized void stop_archive_att(String[] argin) throws DevFailed
    {
        set_state(DevState.RUNNING);
        set_status(device_name + " : " + "DevState.RUNNING");
        get_logger().info("Entering stop_archive_att()");
        this.logger.trace ( ILogger.LEVEL_INFO , "===> Entering stop_archive_att()");
        
        AttributeLightMode attributeLightMode = AttributeLightMode.creationWithFullInformation(argin);
        HdbCollector hdbCollector = null;
        try
        {
        	this.logger.trace ( ILogger.LEVEL_INFO , "===> Attribute Name = " + attributeLightMode.getAttribute_complete_name());
        	
            this.dbProxy.updateModeRecord(attributeLightMode);
            
            hdbCollector = collectorFactory.get(attributeLightMode,this.logger,false, this.dbProxy);
            if ( hdbCollector != null )
            {
                hdbCollector.removeSource(attributeLightMode.getAttribute_complete_name() );
                if ( !hdbCollector.isRefreshing() )
                {
                    collectorFactory.destroy(attributeLightMode);
                }
            }
            else// should only happen when attempting to call stop_archive_att on a non-longer-reserved attribute of a formerly (ie. at the time the archiving was started) dedicated archiver.
            {
                String msg = "HdbArchiver/stop_archive_att/getAttribute_complete_name|"+attributeLightMode.getAttribute_complete_name()+"| The collector is missing. should only happen when attempting to call stop_archive_att on a non-longer-reserved attribute of a formerly (ie. at the time the archiving was started) dedicated archiver.";
                logger.trace ( ILogger.LEVEL_ERROR , msg );
                
                /*String message = "Attempt to get a missing collector!";
                String reason = "Missing collector for attribute: "+attributeLightMode.getAttribute_complete_name();
                String desc = "Failed while executing HdbArchiver.stop_archive_att";
                throw new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "");*/
            }            
        }
        catch ( ArchivingException e )
        {
            this.logWarningAboutRiskySettingOfStateBackToOn ( "StopArchiveAtt" , e );
            
            e.printStackTrace();
            
            get_logger().error(e.toString());
            Util.out2.println(e.toString());
            throw e.toTangoException();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            this.computeLoads ();
            
            set_state(DevState.ON);
            set_status(device_name + " : " + "DevState.ON");    
            this.logger.trace ( ILogger.LEVEL_INFO , "===> Exiting stop_archive_att()");
        }
        
        get_logger().info("Exiting stop_archive_att()");
        
    }


    private void throwDevFailed() throws DevFailed 
    {
        throw new DevFailed ();        
    }
    //=========================================================
    /**
     * Execute command "StateDetailed" on device.
     * This command returns a detailed state of the device.
     *
     * @return  The detailed state
     */
//=========================================================
    public String state_detailed() throws DevFailed
    {
        String argout = new String();
        get_logger().info("Entering state_detailed()");

        // ---Add your Own code to control device here ---
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            stringBuffer.append(collectorFactory.factoryAssessment());
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        argout = stringBuffer.toString();
        get_logger().info("Exiting state_detailed()");
        return argout;
    }

//=========================================================
    /**
     * main part for the device server class
     */
//=========================================================
    public static void main(String[] argv)
    {
        try
        {
            Util tg = Util.init(argv , "HdbArchiver");
            //UtilCLA tg = (UtilCLA) UtilCLA.init(argv , "HdbArchiver");
            //UtilCLA tgCLA = UtilCLA.castToUtilCLA ( tg );
            
            tg.server_init();

            System.out.println("Ready to accept request");

            //tgCLA.server_run();
            tg.server_run();
        }
        catch ( OutOfMemoryError ex )
        {
            System.err.println("Can't allocate memory !!!!");
            System.err.println("Exiting");
        }
        catch ( UserException ex )
        {
            Except.print_exception(ex);

            System.err.println("Received a CORBA user exception");
            System.err.println("Exiting");
        }
        catch ( SystemException ex )
        {
            Except.print_exception(ex);

            System.err.println("Received a CORBA system exception");
            System.err.println("Exiting");
        }

       
        System.exit(-1);
    }
    
    private static DevError [] getNotYetReadyError ()
    {
        ErrSeverity severity = ErrSeverity.WARN ; 
        String reason = ControlResult.EMPTY_REPORT;  
        String desc = ControlResult.EMPTY_REPORT;
        String origin = ControlResult.EMPTY_REPORT;
        
        DevError devError = new DevError ( reason , severity , desc , origin );
        DevError [] ret = new DevError [ 1 ];
        ret [ 0 ] = devError;
        return ret;
    }
    
    /* (non-Javadoc)
     * @see fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable#trace(java.lang.String, int)
     */
    public void trace ( String msg, int level )// throws DevFailed 
    {
        switch ( level )
        {
            case Warnable.LOG_LEVEL_DEBUG :
                get_logger ().debug ( msg );        
            break;
            
            case Warnable.LOG_LEVEL_INFO :
                get_logger ().info ( msg );   
            break;
            
            case Warnable.LOG_LEVEL_WARN :
                get_logger ().warn ( msg );
            break;
            
            case Warnable.LOG_LEVEL_ERROR :
                get_logger ().error ( msg );
            break;
            
            case Warnable.LOG_LEVEL_FATAL :
                get_logger ().fatal ( msg );
            break;
            
            /*default :
                HDBTools.throwDevFailed ( new IllegalArgumentException ( "Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got " + level + "instead." ) );*/
        }
    }

    public void trace ( String msg , Throwable t , int level )// throws DevFailed 
    {
        switch ( level )
        {
            case Warnable.LOG_LEVEL_DEBUG :
                get_logger ().debug ( msg , t );        
            break;
            
            case Warnable.LOG_LEVEL_INFO :
                get_logger ().info ( msg , t );   
            break;
            
            case Warnable.LOG_LEVEL_WARN :
                get_logger ().warn ( msg , t );
            break;
            
            case Warnable.LOG_LEVEL_ERROR :
                get_logger ().error ( msg , t );
            break;
            
            case Warnable.LOG_LEVEL_FATAL :
                get_logger ().fatal ( msg , t );
            break;
            
            /*default :
                HDBTools.throwDevFailed ( new IllegalArgumentException ( "Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got " + level + "instead." ) );*/ 
        }
    }

    /*private String now ()
    {
        Timestamp now = new Timestamp ( System.currentTimeMillis () );
        return now + "";
    }*/
    
    private void startLoggingFactory() 
    {
        ILogger _logger = LoggerFactory.getImpl ( LoggerFactory.DEFAULT_TYPE , this.device_name , this.diaryPath , this.hasDiary );
        _logger.setTraceLevel ( this.diaryLogLevel );
        
        this.logger = _logger;
    }
    
    private String traceStrings(String[] reservedAttributes2) 
    {
        if ( reservedAttributes2 == null || reservedAttributes2.length == 0 )
        {
            return "";
        }
        
        String ret = "";
        for ( int i = 0 ; i < reservedAttributes2.length ; i ++ )
        {
            ret += reservedAttributes2 [ i ];
            if ( i < reservedAttributes2.length - 1 )
            {
                ret += ", ";
            }
        }
        return ret;
    }
}


//--------------------------------------------------------------------------
/* end of $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/HdbArchiver.java,v $ */
