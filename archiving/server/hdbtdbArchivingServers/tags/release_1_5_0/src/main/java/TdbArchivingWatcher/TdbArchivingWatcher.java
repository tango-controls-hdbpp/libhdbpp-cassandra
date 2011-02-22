package TdbArchivingWatcher;


import java.sql.Timestamp;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;

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
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.ArchivingWatch;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.DBReaderFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.IDBReader;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttributeSubName;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResult;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ControlResultLine;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.Domain;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchiversComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchivingAttributeComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchivingAttributeSubNamesComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ControlResultLineComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.DomainsComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.lifecycle.LifeCycleManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.lifecycle.LifeCycleManagerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.ISaferPeriodCalculator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.delay.DelayManagerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.delay.IDelayManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;


/**
 *	Class Description:
 * This device is in charge of controlling HDB archiving. It does so by comparing the theoretical archiving (the attributes that have been registered for archiving) with the effective archiving (are records being added for the registered attributes).
 * If the effective archiving matches the theoretical archiving, the attribute is called "OK", otherwise "KO".
 * The tightness of the required matching has a default value, but can be user-defined (through expert commands).
 *   
 * ArchivingWatcher controlls all N registered attributes during a full cycle, which duration T is about 4 hours. 
 * During one cycle, n sub-controls ("steps" henceforth) are done, for N/n attributes each time (n is chosen so that the control state is refreshed often, and that the database isn't accessed too frequently).
 *
 * Commands suffixed by "Current" return control information related to the current step, ie. information that is refreshed about every 10 seconds.
 * Commands suffixed by "LatestError" return control information related to the latest full cycle having KO attributes, ie. information that is refreshed about every 4 hours.
 *
 * Once a cycle has KO attributes, the device's state becomes ALARM, and stays the same even if later cycles have none. This is so that the operator is warned of the archiving problem. After he is, he can "reset" the device's state to normal.
 *
 * @author	$Author: pierrejoseph $
 * @version	$Revision: 1.12 $
 */

//--------- Start of States Description ----------
/*
 *	Device States Description:
 *	DevState.ON :	Controlling.
 *	DevState.OFF :	Standby.
 *	DevState.FAULT :	Problem to connect to the database or other severe device conditions.
 *	DevState.INIT :	Device was started, but no control step has been completed yet. Please Wait.
 *	DevState.ALARM :	Archiving problems have been detected during this cycle or a previous cycle.
 */
//--------- End of States Description ----------


public class TdbArchivingWatcher extends DeviceImpl/*WithShutdownRunnable*/ implements TangoConst,Warnable
{
	//private	int	state;
	private String m_version; 
	//--------- Start of attributes data members ----------

	protected short[]	attr_ArchivingHealth_read = new short[1];
	protected String[]	attr_FormattedReport_read = new String[10000];
    
    private ControlResultLineComparator linesComparator;
    private ArchiversComparator archiversComparator;
    private DomainsComparator domainsComparator;
    private ArchivingAttributeSubNamesComparator archivingAttributeSubNamesComparator;
    private ArchivingAttributeComparator archivingAttributeComparator;

//--------- End of attributes data members ----------


	//--------- Start of properties data members ----------

	/**
	 * The minimum number of attributes in a control step.
	 * Not used in the current device implementation.
	 * <b>Default value : </b>10
	 */
	short	minPacketSize;
	/**
	 * The maximum number of attributes in a control step.
	 * Not used in the current device implementation.
	 * <b>Default value : </b>100
	 */
	short	maxPacketSize;
	/**
	 * Used to tell the device if it should diagnose KO archivers (archivers that have at least 1 KO attribute).
	 * If set to true, the archivers part of the generated control report will contain specific archiver information such as scalar load etc..<br>
	 * If set to false, those details will remain blank.<br>
	 * Setting the property to true will increase the database load.
	 * <b>Default value : </b>false
	 */
	boolean	doArchiverDiagnosis;
	/**
	 * Used to tell the device if it should ask the archiver to retry archiving KO attributes.
	 * If set to true, the device automatically calls the HDBArchiver command "retryForAttributes" for KO attributes.<br>
	 * If set to false, it doesn't.<br>
	 * <b>Default value : </b>false
	 */
	boolean	doRetry;
	/**
	 * Describes the device start-up sequence.
	 * If set to true, the device automatically starts controlling on initialisation; with default parameters (as if the "start" command was called)<br>
	 * If set to false, the device waits in standby after being initialised.<br>
	 * <b>Default value : </b>false
	 */
	boolean	doStartOnInitDevice;
	/**
	 * The duration of a full control cycle in seconds.
	 * <b>Default value : </b>14400 (4 hours)
	 */
	int	macroPeriod;

	/**
	 * Computer identifier on wich is settled the database DB. The identifier can be the computer name or its IP address. <br>
	 * <b>Default value : </b> hdb
	 */
	String hdbHost;
	/**
	 * Database name.<br>
	 * <b>Default value : </b> hdb
	 */
	String hdbName;

	/**
	 * Computer identifier on wich is settled the database TDB. The identifier can be the computer name or its IP address. <br>
	 * <b>Default value : </b> tdb
	 */
	String tdbHost;
	/**
	 * Database name.<br>
	 * <b>Default value : </b> tdb
	 */
	String tdbName;
	

	
	/**
	 * User identifier (name) used to connect the database HDB. <br>
	 * <b>Default value : </b> hdb
	 */
	String	hdbUser;
	/**
	 * User identifier (name) used to connect the database TDB.<br>
	 * <b>Default value : </b> tdb
	 */
	String	tdbUser;
	/**
	 * Password used to connect the database HDB. <br>
	 * <b>Default value : </b> hdb
	 */
	String	hdbPwd;
	/**
	 * Password used to connect the database TDB.<br>
	 * <b>Default value : </b> tdb
	 */
	String	tdbPwd;
	
	/**
	 * Describes the default safety period calculation method, which will be used if the default start command is called.<br>
	 * <b>Default value : </b> absolute/minutes/15
	 */
	String defaultSafetyPeriod;
    
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

//--------- End of properties data members ----------


	//	Add your own data members here
	private ControlResult controlResult;
	private int readType = IDelayManager.READ_ROLLOVER;
    
    private String latestFaultMessage = "";
    private int faultsCounter = 0;
    
    private IDBReader dbReader;
    private ILogger logger;
	//--------------------------------------



//=========================================================
/**
 *	Constructor for simulated Time Device Server.
 *
 *	@param	cl	The DeviceClass object
 *	@param	s	The Device name.
     * @param   version The device version
  */
TdbArchivingWatcher(DeviceClass cl, String s, String version) throws DevFailed
	{
		super(cl,s);
		m_version = version;
		init_device();
	}
//=========================================================
/**
 *	Constructor for simulated Time Device Server.
 *
 *	@param	cl	The DeviceClass object
 *	@param	s	The Device name.
 *	@param	d	Device description.
 *  @param   version The device version
 */
TdbArchivingWatcher(DeviceClass cl, String s, String d, String version) throws DevFailed
	{
		super(cl,s,d);
		m_version = version;
		init_device();
	}


//=========================================================
/**
 *	Initialize the device.
 */
//=========================================================
	public void init_device() throws DevFailed
	{
		get_device_property();
		
        //	Initialise variables to default values
		
		ArchivingWatch.setHDBHost ( this.hdbHost );
		ArchivingWatch.setHDBName ( this.hdbName );
		ArchivingWatch.setTDBHost ( this.tdbHost );
		ArchivingWatch.setTDBName ( this.tdbName );

		ArchivingWatch.setHDBuser ( this.hdbUser );
		ArchivingWatch.setHDBpassword ( this.hdbPwd );
		ArchivingWatch.setTDBuser ( this.tdbUser );
		ArchivingWatch.setTDBpassword ( this.tdbPwd );
		ArchivingWatch.setMacroPeriod ( this.macroPeriod );
		ArchivingWatch.setDoArchiverDiagnosis ( this.doArchiverDiagnosis );
		ArchivingWatch.setDoRetry ( this.doRetry );
		ArchivingWatch.setDoStartOnInitDevice ( this.doStartOnInitDevice );		
		ArchivingWatch.setDefaultSafetyPeriod ( this.defaultSafetyPeriod );
        ArchivingWatch.setHasDiary ( this.hasDiary );
        ArchivingWatch.setDiaryPath ( this.diaryPath );
        ArchivingWatch.setDiaryLogLevel ( this.diaryLogLevel );
        
        LifeCycleManagerFactory.setWatcherToWarn ( this );
		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getImpl( LifeCycleManagerFactory.TDB_LIFE_CYCLE );
		Thread watcherThread = lifeCycleManager.getAsThread ();
        
        this.startLoggingFactory ();
        
        this.linesComparator = new ControlResultLineComparator ();
        this.archiversComparator = new ArchiversComparator ();
        this.domainsComparator = new DomainsComparator ();
        this.archivingAttributeSubNamesComparator = new ArchivingAttributeSubNamesComparator ();
        this.archivingAttributeComparator = new ArchivingAttributeComparator ();
        
        watcherThread.start ();
        
        if ( ! this.doStartOnInitDevice )
        {
            this.warnOff ();
        }
	}

//===================================================================
/**
 *	Read the device properties from database.
 */
//===================================================================			
	public void get_device_property() throws DevFailed
	{
		//	Initialize your default values here.
		//------------------------------------------


		//	Read device properties from database.(Automatic code generation)
		//-------------------------------------------------------------
		if (Util._UseDb==false)
			return;
		String[]	propnames = {
				"MinPacketSize",
				"MaxPacketSize",
				"DoArchiverDiagnosis",
				"DoRetry",
				"DoStartOnInitDevice",
				"MacroPeriod",
				"HdbHost",
				"HdbName",
				"TdbHost",
				"TdbName",
				"HdbUser",
				"TdbUser",
				"HdbPwd",
				"TdbPwd",
				"DefaultSafetyPeriod",
                "HasDiary",
                "DiaryPath",
                "DiaryLogLevel",
			};

		//	Call database and extract values
		//--------------------------------------------
		DbDatum[]	dev_prop = get_db_device().get_property(propnames);
		TdbArchivingWatcherClass	ds_class = (TdbArchivingWatcherClass)get_device_class();
		int	i = -1;
		//	Extract MinPacketSize value
		if (dev_prop[++i].is_empty()==false)		minPacketSize = dev_prop[i].extractShort();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	minPacketSize = cl_prop.extractShort();
		}

		//	Extract MaxPacketSize value
		if (dev_prop[++i].is_empty()==false)		maxPacketSize = dev_prop[i].extractShort();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	maxPacketSize = cl_prop.extractShort();
		}

		//	Extract DoArchiverDiagnosis value
		if (dev_prop[++i].is_empty()==false)		doArchiverDiagnosis = dev_prop[i].extractBoolean();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	doArchiverDiagnosis = cl_prop.extractBoolean();
		}
		
		//Extract DoRetry value
		if (dev_prop[++i].is_empty()==false)		doRetry = dev_prop[i].extractBoolean();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	doRetry = cl_prop.extractBoolean();
		}
		
		//Extract doStartOnInitDevice value
		if (dev_prop[++i].is_empty()==false)		doStartOnInitDevice = dev_prop[i].extractBoolean();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	doStartOnInitDevice = cl_prop.extractBoolean();
		}

		
		//	Extract MacroPeriod value
		if (dev_prop[++i].is_empty()==false)		macroPeriod = dev_prop[i].extractLong();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	macroPeriod = cl_prop.extractLong();
		}
        //  Extract HDBHost value
        if ( dev_prop[ ++i ].is_empty() == false )
            hdbHost = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) hdbHost = cl_prop.extractString();
        }
        
        //  Extract HDbName value
        if ( dev_prop[ ++i ].is_empty() == false )
            hdbName = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) hdbName = cl_prop.extractString();
        }

        //  Extract TDBHost value
        if ( dev_prop[ ++i ].is_empty() == false )
            tdbHost = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) tdbHost = cl_prop.extractString();
        }
        
        //  Extract TDbName value
        if ( dev_prop[ ++i ].is_empty() == false )
            tdbName = dev_prop[ i ].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[ i ].name);
            if ( cl_prop.is_empty() == false ) tdbName = cl_prop.extractString();
        }


		
		//	Extract HdbUser value
		if (dev_prop[++i].is_empty()==false)		hdbUser = dev_prop[i].extractString();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	hdbUser = cl_prop.extractString();
		}

		//	Extract TdbUser value
		if (dev_prop[++i].is_empty()==false)		tdbUser = dev_prop[i].extractString();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	tdbUser = cl_prop.extractString();
		}

		//	Extract HdbPwd value
		if (dev_prop[++i].is_empty()==false)		hdbPwd = dev_prop[i].extractString();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	hdbPwd = cl_prop.extractString();
		}

		//	Extract TdbPwd value
		if (dev_prop[++i].is_empty()==false)		tdbPwd = dev_prop[i].extractString();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	tdbPwd = cl_prop.extractString();
		}
		
		//  Extract DefaultSafetyPeriod value
		if (dev_prop[++i].is_empty()==false)		defaultSafetyPeriod = dev_prop[i].extractString();
		else
		{
			//	Try to get value from class property
			DbDatum	cl_prop = ds_class.get_class_property(dev_prop[i].name);
			if (cl_prop.is_empty()==false)	defaultSafetyPeriod = cl_prop.extractString();
		}
		
		//  Extract hasDiary value
        if (dev_prop[++i].is_empty()==false)        hasDiary = dev_prop[i].extractBoolean();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty()==false)  hasDiary = cl_prop.extractBoolean();
        }
        
        //  Extract diaryPath value
        if (dev_prop[++i].is_empty()==false)        diaryPath = dev_prop[i].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty()==false)  diaryPath = cl_prop.extractString();
        }
        
        //  Extract diaryLogLevel value
        if (dev_prop[++i].is_empty()==false)        diaryLogLevel = dev_prop[i].extractString();
        else
        {
            //  Try to get value from class property
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty()==false)  diaryLogLevel = cl_prop.extractString();
        }
		//	End of Automatic code generation
		//-------------------------------------------------------------

	}
//=========================================================
/**
 *	Method always executed before command execution.
 */
//=========================================================
	public void always_executed_hook()
	{	
		//get_logger().info("In always_executed_hook method()");
	}

//===================================================================

//===================================================================
/**
 *	Method called by the read_attributes CORBA operation to
 *	read device hardware
 *
 *	@param	attr_list	Vector of index in the attribute vector
 *		of attribute to be read
 * @throws DevFailed
 */
//===================================================================			
	public synchronized void read_attr_hardware(Vector attr_list) throws DevFailed
	{
		if ( this.get_state().value() != DevState._FAULT )
        {
            IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
            ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
            this.controlResult = _controlResult == null ? null :_controlResult.cloneControlResult ();
        }
        else
        {
            this.controlResult = new ControlResult ();
        }
	}
//===================================================================
/**
 *	Method called by the read_attributes CORBA operation to
 *	set internal attribute value.
 *
 *	@param	attr	reference to the Attribute object
 */
//===================================================================			
	public synchronized void read_attr(Attribute attr) throws DevFailed
	{
		String attr_name = attr.get_name();
		get_logger().info("In read_attr for attribute " + attr_name);

		//	Switch on attribute name
		if (attr_name.equals ( "ArchivingHealth") )
		{
			//	Add your own code here
		    int code = this.controlResult == null ? ControlResult.NOT_READY : this.controlResult.getCode ();
		    attr.set_value ( (short) code );
		}
		else if (attr_name.equals ( "FormattedReport") )
		{
			//	Add your own code here
		    String report = this.controlResult == null ? ControlResult.EMPTY_REPORT : this.controlResult.getReport ();
		    
		    String [] res = new String [ 1 ];
		    res [ 0 ] = report;
		    attr.set_value ( res , 1 );
		}
		else if ( attr_name == "version" )
        {
            //  Add your own code here
            attr.set_value(m_version);
        }
		//---------------------------------
	}


//=========================================================
/**
 *	Execute command "GetReport" on device.
 *	Returns the complete formatted report
 *
 * @return	The complete formatted report
 */
//=========================================================
	private String get_report ( ControlResult _controlResult ) throws DevFailed
	{
	    String empty = ControlResult.EMPTY_REPORT;
	    if ( _controlResult == null )
		{
	        return empty;   
		}
	    
	    return _controlResult.getReport ();
	}



//=========================================================
/**
 *	Execute command "IsAttributeCorrectlyArchived" on device.
 *	Returns true if archiving works correctly for this attribute
 *
 * @param	argin	The complete name of the attribute
 * @return	True if archiving works correctly for this attribute
 */
//=========================================================
	private boolean is_attribute_correctly_archived(String argin , ControlResult _controlResult ) throws DevFailed
	{
	    if ( _controlResult == null )
		{
	        throw new DevFailed ( TdbArchivingWatcher.getNotYetReadyError () );	    
		}
	    
	    return  _controlResult.isAttributeCorrectlyArchived ( argin );
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




//=========================================================
/**
 *	Execute command "Start" on device.
 * Starts archiving control with default parameters.
 * Use start_absolute or start_relative to specify those parameters.
 */
//=========================================================
	public synchronized void start() throws DevFailed
	{
		get_logger().info("Entering start()");

		// ---Add your Own code to control device here ---
		LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl ();
		lifeCycleManager.startProcessing ();
        //---END
        
		get_logger().info("Exiting start()");
	}

    //=========================================================
	/**
	 *	Execute command "StartAbsolute" on device.
	 * Starts archiving control in the specified absolute mode. 
	 * In this mode, the period that is effectively controlled is the theoretical period + a fixed duration (hence the name 'absolute')
	 * @param	argin	The absolute mode parameters: argin[0] = the duration amount, argin[1] = the duration nature (1=seconds, 2=minutes, 3=hours) 
	 */
    //=========================================================
	public synchronized void start_absolute ( int[] argin ) throws DevFailed
	{
		get_logger().info("Entering start_absolute()");

		// ---Add your Own code to control device here ---
		if ( argin == null || argin.length != 2 )
		{
		    Tools.throwDevFailed ( new IllegalArgumentException ( "Needs 2 int parameters: [ amount , type ]" ) );
		}
		
		int amount = argin [ 0 ];
		int type =  argin [ 1 ];
		SaferPeriodCalculatorFactory.getAbsoluteImpl ( amount , type );
		
		this.start ();
        //---END
        
		get_logger().info("Exiting start_absolute()");
	}
	
    //=========================================================
	/**
	 *	Execute command "StartRelative" on device.
	 * Starts archiving control in the specified relative mode. 
	 * In this mode, the period that is effectively controlled is the theoretical period * a multipier (hence the name 'relative')
	 * @param	argin	The multiplier 
	 */
    //=========================================================
	public synchronized void start_relative ( double argin ) throws DevFailed
	{
		get_logger().info("Entering start_absolute()");

		// ---Add your Own code to control device here ---
		if ( argin <= 0 )
		{
		    Tools.throwDevFailed ( new IllegalArgumentException ( "The multiplier has to be > 0" ) );
		}
		
		SaferPeriodCalculatorFactory.getRelativeImpl ( argin );
		
		this.start ();
        //---END
        
		get_logger().info("Exiting start_absolute()");
	}
	
//=========================================================
    /**
     *	Execute command "Stop" on device.
     *	Stops archiving control, puts device in standby
     *
     */
//=========================================================
public synchronized void stop() throws DevFailed
{
	get_logger().info("Entering stop()");

	// ---Add your Own code to control device here ---
	LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl ();
	lifeCycleManager.stopProcessing ();
	//this.warnOff ();
	//---END

	get_logger().info("Exiting stop()");
}

	
	/**
	 * Overrides set_state so that formerState is stored when the device state changes
	 * @param _in
	 */
	public synchronized void set_state ( DevState in )
	{
	    this.set_state ( (short) in.value () );
	}
	
	/**
	 * Overrides set_state so that formerState is stored when the device state changes
	 * @param _state
	 */
	public synchronized void set_state ( short _state )
	{
        /*int currentState = this.get_state().value();
        if ( _state != currentState )
        {
            this.formerState = currentState;
        }*/
        super.set_state ( DevState.from_int ( _state ) );
	    
	    String _status = this.formatStatus ( _state );
	    this.setStatus ( _status );
	}
	
	/**
     * @param i
     */
    private String formatStatus ( int _state ) 
    {
        ISaferPeriodCalculator saferPeriodCalculator = SaferPeriodCalculatorFactory.getCurrentImpl ();
        String currentModeDescription = saferPeriodCalculator == null ? "No description available" : saferPeriodCalculator.getDescription ();
        
        String _status;
        switch ( _state )
        {
        	case DevState._ALARM : 
        	    _status = "Archiving problems have been detected.";
        	break;
        	
        	case DevState._FAULT :
        	    _status = "This device isn't working properly.";
        	break;    
        	
        	case DevState._INIT :
        	    _status = "No control step has been completed yet. Please wait.";
        	break;
        	
        	case DevState._OFF :
        	    _status = "This device is waiting.";
        	break;    
        	
        	case DevState._ON :
        	    _status = "This device is running normally.";
        	break;    
        	
        	default :
        	    _status = "Unknown";
        	break;
        }
        
        String ret = _status + Tools.CRLF + "Current mode: " + currentModeDescription;
        return ret;
    }
    
    /*private void goBackToFormerState ()
	{
        System.out.println ( "CLA/ArchivingWatcher/goBackToFormerState!!!!!!!!!!!!!!!" );
        //if ( !this.get_state().get )
        
        if ( this.formerState != DevState._UNKNOWN )
        {
            System.out.println ( "CLA/ArchivingWatcher/goBackToFormerState/this.formerState/"+this.formerState+"/DevState._ALARM/"+DevState._ALARM+"/DevState._ON/"+DevState._ON+"/DevState._OFF/"+DevState._OFF);
            this.set_state ( (short) this.formerState );    
        }
        else
        {
            System.out.println ( "CLA/ArchivingWatcher/goBackToFormerState/ON");
            this.set_state ( DevState.ON );    
        }   
	}*/




//=========================================================
/**
 *	Execute command "GetErrorArchiversCurrent" on device.
 *	Lists the archivers that had at least one attribute incorrectly archived during the current control cycle.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @return	The list of archivers that have at least one KO attribute.
 */
//=========================================================
public synchronized String[] get_error_archivers_current() throws DevFailed
{
    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
	ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
	
	return this.get_error_archivers ( _controlResult );
}


/**
 * @param result
 * @return
 */
private String[] get_error_archivers(ControlResult _controlResult) 
{
    String [] empty = new String [ 0 ];
    if ( _controlResult == null )
    {
        return empty;   
    }
    
    Map _errorArchivers = _controlResult.getErrorArchivers ();
    String[] argout = new String [ _errorArchivers.size () ];
    
    List list = new Vector ();
    list.addAll ( _errorArchivers.values () );
    Collections.sort(list, this.archiversComparator );
    Iterator it = list.iterator ();
    
    int i = 0;
    while ( it.hasNext () )
    {
        Archiver key = (Archiver) it.next ();
        argout [ i ] = key.getName ();
        i++;
    }
    
    return argout;
}
//=========================================================
/**
 *	Execute command "GetErrorsForArchiver" on device.
 *	Lists KO attributes for this archiver
 *
 * @param	argin	The name of the archiver
 * @return	The list of KO attributes for this archiver
 */
//=========================================================
private String[] get_errors_for_archiver ( String argin , ControlResult _controlResult ) throws DevFailed
{
    String [] empty = new String [ 0 ];
    if ( _controlResult == null )
    {
        return empty;   
    }
    
    Hashtable _errorArchivers = _controlResult.getErrorArchivers ();
    if ( _errorArchivers == null )
    {
        return empty;
    }
    
    Archiver archiver = (Archiver) _errorArchivers.get ( argin );
    if ( archiver == null )
    {
        return empty;
    }
    
    Hashtable _errorAttributes = archiver.getKOAttributes ();
    if ( _errorAttributes == null )
    {
        return empty;
    }
    
    String [] argout = new String [ _errorAttributes.size () ];
    List list = new Vector ();
    list.addAll ( _errorAttributes.values () );
    Collections.sort(list, this.archivingAttributeComparator );
    Iterator it = list.iterator ();
    int i = 0;
    
    while ( it.hasNext () )
    {
        ArchivingAttribute key = (ArchivingAttribute) it.next ();
        argout [ i ] = key.getCompleteName();
        i++;
    }
    //-----------------END
    return argout;
}



//=========================================================
/**
 *	Execute command "Reset" on device.
 *	Notifies the device that the last alarm (should it exist) was taken into account by the user.
 *
 */
//=========================================================
//	public synchronized void reset() throws DevFailed
//	{
//		get_logger().info("Entering reset()");
//
//		// ---Add your Own code to control device here ---
//		this.goBackToFormerState ();
//		
//		get_logger().info("Exiting reset()");
//	}



//=========================================================
/**
 *	Execute command "Reset" on device.
 * This is used after a KO control cycle has set the state to ALARM, to notify the device that the user has seen the ALARM state.
 * Resets the state to its former value.
 */
//=========================================================
	public synchronized void reset() throws DevFailed
	{
		get_logger().info("Entering reset()");

		// ---Add your Own code to control device here ---
        if ( this.get_state ().value () != DevState._ALARM )
        {
            //do nothing
            return;
        }
        
        LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl ();
        boolean isProcessing = lifeCycleManager.isProcessing ();
        if ( isProcessing )
        {
            this.set_state ( (short) DevState._ON );    
        }
        else
        {
            this.set_state ( (short) DevState._OFF );    
        }
        
		get_logger().info("Exiting reset()");
	}

//  =========================================================
    /**
     *  Execute command "Restart" on device.
     * Restarts the device.
     */
//  =========================================================
    public synchronized void restart () throws DevFailed
    {
        get_logger().info("Entering restart()");

        //complete stop
        LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getImpl( LifeCycleManagerFactory.TDB_LIFE_CYCLE );
        lifeCycleManager.stopProcessing ();
        
        Thread watcherThread = lifeCycleManager.getAsThread ();
        try 
        {
            watcherThread.join ( 1000 );
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
            Tools.throwDevFailed ( e );
        }

        //restarting
        this.init_device ();
        this.latestFaultMessage = "";
        this.faultsCounter = 0;
        
        boolean isProcessing = lifeCycleManager.isProcessing ();
        if ( isProcessing )
        {
            this.set_state ( (short) DevState._ON );    
        }
        else
        {
            this.set_state ( (short) DevState._OFF );    
        }
        
        get_logger().info("Exiting restart()");
    }
    

    private String[] get_error_domains( ControlResult _controlResult ) throws DevFailed
    {
        String [] empty = new String [ 0 ];
        if ( _controlResult == null )
        {
            return empty;   
        }
        
        Hashtable _errorDomains = _controlResult.getErrorDomains ();
        String [] argout = new String [ _errorDomains.size () ];
        
        List list = new Vector ();
        list.addAll ( _errorDomains.values () );
        Collections.sort(list, this.domainsComparator );
        Iterator it = list.iterator ();
        
        int i = 0;
        while ( it.hasNext () )
        {
            Domain key = (Domain) it.next ();
            argout [ i ] = key.getName ();
            i++;
        }

        return argout;
    }






//=========================================================
/**
 *	Execute command "GetErrorsForDomain" on device.
 *	Lists KO attributes for this domain
 *
 * @param	argin	The domain name
 * @return	The list of KO attributes for this domain.
 */
//=========================================================
    private String[] get_errors_for_domain ( String argin , ControlResult _controlResult ) throws DevFailed
    {
        String [] empty = new String [ 0 ];
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
        
        List list = new Vector ();
        list.addAll ( _errorAttributes.values () );
        Collections.sort(list, this.archivingAttributeComparator );
        Iterator it = list.iterator ();
        
        int i = 0;
        while ( it.hasNext () )
        {
            ArchivingAttribute key = (ArchivingAttribute) it.next ();
            argout [ i ] = key.getCompleteName();
            i++;
        }
        
        return argout;
    }
//=========================================================
/**
 *	Execute command "SetMode" on device.
 *	Choose a mode. Must be one of those predefined modes:
 *	READ_LATEST_COMPLETE_CYCLE = 10
 *	READ_LATEST_COMPLETE_CYCLE_AUTO = 11
 *	READ_LATEST_COMPLETE_STEP = 20
 *	READ_LATEST_ADDITION = 30
 *	READ_LATEST_BAD_CYCLE = 40
 *
 * @param	argin	The mode
 */
//=========================================================
/*public synchronized void set_mode(short argin) throws DevFailed
	{
		get_logger().info("Entering set_mode()");

		// ---Add your Own code to control device here ---
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		try
		{
		    delayManager.setReadType ( argin );
		    this.readType = argin;
		}
		catch ( IllegalArgumentException iae )
		{
		    DevFailed devFailed = new DevFailed ();
		    devFailed.initCause ( iae );
		    throw iae;
		}
		//------------------------------------------------
		
		get_logger().info("Exiting set_mode()");
	}*/


//=========================================================
/**
 *	Execute command "GetErrorsForAttribute" on device.
 *	Returns the list of KO attributes for this attribute name
 *
 * @param	argin	The NOT complete name of the attribute
 * @return	The list of KO attributes for this attribute name
 */
//=========================================================
    private String[] get_errors_for_attribute ( String argin , ControlResult _controlResult ) throws DevFailed
    {
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
        
        List list = new Vector ();
        list.addAll ( _errorAttributes.values () );
        Collections.sort(list, this.archivingAttributeComparator );
        Iterator it = list.iterator ();
        
        int i = 0;
        while ( it.hasNext () )
        {
            ArchivingAttribute key = (ArchivingAttribute) it.next ();
            argout [ i ] = key.getCompleteName();
            i++;
        }

        return argout;
    }

//=========================================================
/**
 *	Execute command "GetReportCurrent" on device.
 * Returns a formatted report representing the current control cycle results.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @return	The report
 */
//=========================================================
public synchronized String get_report_current() throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
	    //ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
		
		return this.get_report ( _controlResult );
	}


//=========================================================
/**
 *	Execute command "GetReportLatestError" on device.
 * Returns a formatted report representing the latest KO control cycle results.
 * (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
 * @return	The report
 */
//=========================================================
public synchronized String get_report_latest_error() throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
		
		return this.get_report ( _controlResult );
	}

//=========================================================
/**
 *	Execute command "IsAttributeCorrectlyArchivedCurrent" on device.
 * Returns whether the specified attribute was correctly archiving during the current control cycle.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @param	argin	The attribute complete name
 * @return	True if archiving works correctly for this attribute
 */
//=========================================================
public synchronized boolean is_attribute_correctly_archived_current(String argin) throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
		
		return this.is_attribute_correctly_archived ( argin , _controlResult );
	}

//=========================================================
/**
 *	Execute command "IsAttributeCorrectlyArchivedLatestError" on device.
 * Returns whether the specified attribute was correctly archiving during the current control cycle.
 * (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
 * @param	argin	The attribute complete name
 * @return	True if archiving works correctly for this attribute
 */
//=========================================================
public synchronized boolean is_attribute_correctly_archived_latest_error(String argin) throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
		
		return this.is_attribute_correctly_archived ( argin , _controlResult );
	}

//=========================================================
/**
 *	Execute command "GetErrorArchiversLatestError" on device.
 *	Lists the archivers that had at least one attribute incorrectly archived during the latest KO control cycle.
 *  (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
 * @return	The list of archivers that had at least one KO attribute.
 */
//=========================================================
public synchronized String[] get_error_archivers_latest_error() throws DevFailed
{
	IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
	ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
	
	return this.get_error_archivers ( _controlResult );
}


//=========================================================
/**
 *	Execute command "GetErrorsForArchiverCurrent" on device.
 * Lists the KO attributes for a given archiver during the current control cycle.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @param	argin	The name of the archiver
 * @return	The list of KO attributes for this archiver
 */
//=========================================================
public synchronized String[] get_errors_for_archiver_current(String argin) throws DevFailed
	{
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
		
		return this.get_errors_for_archiver ( argin , _controlResult );
	}


    //=========================================================
    /**
     *	Execute command "GetErrorsForArchiverLatestError" on device.
     * Lists the KO attributes for a given archiver during the latest KO control cycle.
     * (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
     * @param	argin	The name of the archiver
     * @return	The list of KO attributes for this archiver
     */
    //=========================================================
	public synchronized String[] get_errors_for_archiver_latest_error(String argin) throws DevFailed
	{
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
	
		return this.get_errors_for_archiver ( argin , _controlResult );
	}

//  =========================================================
    /**
     * Execute command "GetAllArchivingAttributes" on device.
     * Lists all TDB archiving attributes (does a new request regardless of the watcher cycle).
     * @return  The list of all TDB archiving attributes.
     */
//=========================================================
    public synchronized String [] get_all_archiving_attributes() throws DevFailed
    {
        String [] ret = null;
        
        try
        {
            Hashtable all = this.dbReader.getArchivedAttributes ();
            if ( all != null )
            {
                ret = new String [ all.size () ];
            }
            else
            {
                return null;
            }
            
            ControlResult cr = new ControlResult ();
            cr.setAllArchivingAttributes ( all );
            ControlResultLine [] orderedLines = cr.sort ();
            
            for ( int i = 0 ; i < all.size () ; i ++ )
            {
                ret [ i ] = orderedLines [ i ].getAttribute ().getCompleteName ();
                Mode mode = this.dbReader.getModeForAttribute ( ret [ i ] );
                if ( mode != null )
                {
                    ret [ i ] += ": " + mode.toStringWatcher ();    
                }
            }
        }
        catch ( Exception e )
        {
            this.logger.trace ( ILogger.LEVEL_ERROR , "get_all_archiving_attributes/error! VVVVVVVVVVVVVVVVV" );
            this.logger.trace ( ILogger.LEVEL_ERROR , e);
            this.logger.trace ( ILogger.LEVEL_ERROR , "get_all_archiving_attributes/error! ^^^^^^^^^^^^^^^^^" );
            Tools.throwDevFailed ( e );
        }
        
        return ret;
    }
    
//=========================================================
/**
 *	Execute command "GetErrorDomainsCurrent" on device.
 *	Lists the domains that had at least one attribute incorrectly archived during the current control cycle.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @return	The list of archivers that have at least one KO attribute.
 */
//=========================================================
public synchronized String[] get_error_domains_current() throws DevFailed
	{
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
		
		return this.get_error_domains ( _controlResult );
	}


//=========================================================
/**
 *	Execute command "GetErrorDomainsLatestError" on device.
 *	Lists the domains that had at least one attribute incorrectly archived during the latest KO control cycle.
 *  (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
 * @return	The list of archivers that had at least one KO attribute.
 */
//=========================================================
public synchronized String[] get_error_domains_latest_error() throws DevFailed
	{
		IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
		
		return this.get_error_domains ( _controlResult );
	}


//=========================================================
/**
 *	Execute command "GetErrorsForDomainCurrent" on device.
 * Lists the KO attributes for a given domain during the current control cycle.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @param	argin	The name of the domain
 * @return	The list of KO attributes for this domain
 */
//=========================================================
public synchronized String[] get_errors_for_domain_current(String argin) throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
		
		return this.get_errors_for_domain ( argin , _controlResult );
	}

//=========================================================
/**
 *	Execute command "GetErrorsForDomainLatestError" on device.
 * Lists the KO attributes for a given domain during the current control cycle.
 * (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
 * @param	argin	The name of the domain
 * @return	The list of KO attributes for this domain
 */
//=========================================================
public synchronized String[] get_errors_for_domain_latest_error(String argin) throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
		
		return this.get_errors_for_domain ( argin , _controlResult );
	}


//=========================================================
/**
 *	Execute command "GetErrorsForAttributeCurrent" on device.
 * Lists the KO attributes with a given name during the current control cycle.
 * Partially refreshed every time an attribute is controlled (at worst every 10 seconds)
 * @param	argin	The attribute (NOT the complete name)
 * @return	The list of KO attributes with this name
 */
//=========================================================
public synchronized String[] get_errors_for_attribute_current(String argin) throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_ROLLOVER );
		
		return this.get_errors_for_attribute ( argin , _controlResult );
	}


//=========================================================
/**
 *	Execute command "GetErrorsForAttributeLatestError" on device.
 * Lists the KO attributes with a given name during the current control cycle.
 * (a control cycle becomes KO when it has at least one KO atribute, and until the user cleans the state via the "reset" command or another cycle is KO)
 * @param	argin	The attribute (NOT the complete name)
 * @return	The list of KO attributes with this name
 */
//=========================================================
public synchronized String[] get_errors_for_attribute_latest_error(String argin) throws DevFailed
	{
	    IDelayManager delayManager = DelayManagerFactory.getCurrentImpl ();
		ControlResult _controlResult = delayManager.getControlResult ( IDelayManager.READ_LATEST_BAD_CYCLE );
		
		return this.get_errors_for_attribute ( argin , _controlResult );
	}


//=========================================================
/**
 *	main part for the device server class
 */
//=========================================================
	public static void main(String[] argv)
	{
		try
		{
			Util tg = Util.init(argv,"TdbArchivingWatcher");
			tg.server_init();

            System.out.println("Ready to accept request");

			tg.server_run();
		}

		catch (OutOfMemoryError ex)
		{
			System.err.println("Can't allocate memory !!!!");
			System.err.println("Exiting");
		}
		catch (UserException ex)
		{
			Except.print_exception(ex);
			
			System.err.println("Received a CORBA user exception");
			System.err.println("Exiting");
		}
		catch (SystemException ex)
		{
			Except.print_exception(ex);
			
			System.err.println("Received a CORBA system exception");
			System.err.println("Exiting");
		}
		
		System.exit(-1);		
	}
/* (non-Javadoc)
 * @see archwatch.strategy.delay.Warnable#warn()
 */
public synchronized void warnOn() 
{
    //this.formerState = DevState._ON;
    
    switch ( this.get_state().value() )
    {
    	case DevState._ALARM:
    	    //do nothing
    	return;
    }
    
    this.dbReader = DBReaderFactory.getCurrentImpl ();
    this.set_state ( (short) DevState._ON );    
    
    String message = "At :" + this.now ();
    message += Tools.CRLF;
    message += this.get_status ();
    
    this.setStatus ( message );
}
/* (non-Javadoc)
 * @see archwatch.strategy.delay.WarnAble#warnFault()
 */
public synchronized void warnFault ( DevFailed t ) 
{
    System.out.println ( "CLA/ArchivingWatcher/warnFault" );
    
    this.set_state ( (short) DevState._FAULT );

    String faultMessage = "At :" + this.now ();
    faultMessage += Tools.CRLF;
    faultMessage += Tools.getCompleteMessage ( t );
    this.setLatestFaultMessage ( faultMessage );
    
    String message = this.get_status ();
    message += Tools.CRLF;
    message += faultMessage;
    
    this.setStatus ( message );
}

/* (non-Javadoc)
 * @see archwatch.tools.WarnAble#warnAlarm()
 */
public synchronized void warnAlarm() 
{
    this.set_state ( (short) DevState._ALARM );      
    
    String message = "At :" + this.now ();
    message += Tools.CRLF;
    message += this.get_status ();
    
    this.setStatus ( message );
}

public synchronized void warnOff() 
{
    //this.formerState = DevState._OFF;
    
    switch ( this.get_state().value() )
    {
    	case DevState._ALARM:
            //do nothing            
    	return;
    	
    	case DevState._FAULT:
    	    //do nothing
    	return;
    }
    
    this.set_state ( (short) DevState._OFF );      
    
    String message = "At :" + this.now ();
    message += Tools.CRLF;
    message += this.get_status ();
    
    this.setStatus ( message );
}

public synchronized void warnInit() 
{
    switch ( this.get_state().value() )
    {
    	case DevState._ALARM:
    	    //do nothing
    	return;
    }
    
    this.set_state ( (short) DevState._INIT );      
    
    String message = "At :" + this.now ();
    message += Tools.CRLF;
    message += this.get_status ();
    
    this.setStatus ( message );
}
/* (non-Javadoc)
 * @see archwatch.tools.Warnable#setStatus(java.lang.String)
 */
public synchronized void setStatus(String status) 
{
    super.set_status ( status );    
}

public String get_status () 
{
    String ret = super.get_status ();
    
    if ( this.get_state().value() != DevState._FAULT )
    {
        ret += this.getLatestFaultMessage ();
    }
    
    return ret;
}


/* (non-Javadoc)
 * @see fr.soleil.TangoArchiving.ArchivingWatchApi.devicelink.Warnable#trace(java.lang.String, int)
 */
public synchronized void trace ( String msg, int level ) throws DevFailed 
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
    	
    	default :
    	    Tools.throwDevFailed ( new IllegalArgumentException ( "Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got " + level + "instead." ) );
    }
}

public synchronized void trace ( String msg , Throwable t , int level ) throws DevFailed 
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
    	
    	default :
    	    Tools.throwDevFailed ( new IllegalArgumentException ( "Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got " + level + "instead." ) ); 
    }
}

private String now ()
{
    Timestamp now = new Timestamp ( System.currentTimeMillis () );
    return now + "";
}
/**
 * @return Returns the latestFaultMessage.
 */
private String getLatestFaultMessage() 
{
    if ( this.latestFaultMessage == null || this.latestFaultMessage.equals ( "" ) )
    {
        return "";
    }
    else
    {
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
/**
 * @param latestFaultMessage The latestFaultMessage to set.
 */
private void setLatestFaultMessage(String latestFaultMessage) 
{
    this.latestFaultMessage = latestFaultMessage;
    this.faultsCounter ++;
    this.addToRecentFaults ( latestFaultMessage );
}

private void addToRecentFaults(String latestFaultMessage) 
{
    //this.recentFaults.add ( latestFaultMessage );    
}

public void logIntoDiary ( int level , Object o ) 
{
    /*if (  this.logger != null )
    {
        this.logger.trace ( level , o );
    }*/
    this.logger.trace ( level , o );
}

private void startLoggingFactory() 
{
    //System.out.println ( "CLA/startLoggingFactory/this.device_name/"+this.device_name+"/hashCode/"+this.hashCode ()+"/this.hasDiary/"+this.hasDiary );
    
    ILogger _logger = LoggerFactory.getImpl ( LoggerFactory.DEFAULT_TYPE , this.device_name , this.diaryPath , this.hasDiary );
    _logger.setTraceLevel ( this.diaryLogLevel );
    
    this.logger = _logger;
}

}
	

//--------------------------------------------------------------------------
/* end of $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchivingWatcher/TdbArchivingWatcher.java,v $ */
