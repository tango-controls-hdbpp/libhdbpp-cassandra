/*	Synchrotron Soleil
 *
 *   File          :  ArchivingWatch.java
 *
 *   Project       :  archiving_watcher
 *
 *   Description   :
 *
 *   Author        :  CLAISSE
 *
 *   Original      :  28 nov. 2005
 *
 *   Revision:  					Author:
 *   Date: 							State:
 *
 *   Log: ArchivingWatch.java,v
 *
 */
 /*
 * Created on 28 nov. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.device.IDbProxy;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.lifecycle.LifeCycleManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.lifecycle.LifeCycleManagerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;


/**
 * The Main class in standalone mode, it is also used to store device properties in device mode.
 * @author CLAISSE
 */
public class ArchivingWatch
{

    private static String HDBHost;
    private static String HDBName;
    private static String TDBHost;
    private static String TDBName;

	private static String HDBuser;
    private static String HDBpassword;
    private static String TDBuser;
    private static String TDBpassword;

    private static long macroPeriod;
    private static boolean doArchiverDiagnosis;
    private static boolean doRetry;
    private static boolean doStartOnInitDevice;
    private static String defaultSafetyPeriod;

    private static String diaryPath;
    private static boolean hasDiary;
    private static String diaryLogLevel;

    private static boolean HDBRac;
    private static boolean TDBRac;
    private static short minNumberOfKoForAlarmState;

	private static IDbProxy dbProxy;

    /**
     * @param args The launch parameters in standalone mode
     */
    public static void main ( String[] args )
    {
        if ( args == null || args.length != 7 )
        {
            Tools.trace( "Incorrect arguments. Correct syntax: java ArchivingWatch HDBuser HDBpassword TDBuser TDBpassword sleepDuration(s) doArchiverDiagnosis(true/false) doRetry(true/false)" , Warnable.LOG_LEVEL_FATAL );
            System.exit( 1 );
        }

        HDBuser = args[ 0 ];
        HDBpassword = args[ 1 ];
        TDBuser = args[ 2 ];
        TDBpassword = args[ 3 ];

        String macroPeriod_s = args[ 4 ];
        String doArchiverDiagnosis_s = args[ 5 ];
        //String doRetry_s = args[ 6 ];

        try
        {
            macroPeriod = Long.parseLong ( macroPeriod_s );
            macroPeriod *= 1000;
        }
        catch ( Exception e )
        {
            Tools.trace( "Incorrect macroPeriod parameter. Must be a duration in seconds" , Warnable.LOG_LEVEL_FATAL );
            System.exit( 1 );
        }

        LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getImpl( LifeCycleManagerFactory.HDB_LIFE_CYCLE );
        lifeCycleManager.applicationWillStart( doRetry );

        boolean doArchiverDiagnosis = Tools.stringToBoolean ( doArchiverDiagnosis_s );
        //boolean doRetry = Tools.stringToBoolean ( doRetry_s );

        try
        {
            lifeCycleManager.startProcessing ();
            lifeCycleManager.process ( macroPeriod , doArchiverDiagnosis,(short) 100 );
        }
        catch ( Throwable t )
        {
            t.printStackTrace ();
            lifeCycleManager.applicationWillClose ( null );
        }
    }
    /**
     * @return Returns the hDBpassword.
     */
    public static String getHDBpassword() {
        return HDBpassword;
    }
    /**
     * @param bpassword The hDBpassword to set.
     */
    public static void setHDBpassword(String bpassword) {
        HDBpassword = bpassword;
    }
    /**
     * @return Returns the hDBuser.
     */
    public static String getHDBuser() {
        return HDBuser;
    }
    /**
     * @param buser The hDBuser to set.
     */
    public static void setHDBuser(String buser) {
        HDBuser = buser;
    }
    /**
     * @return Returns the tDBpassword.
     */
    public static String getTDBpassword() {
        return TDBpassword;
    }
    /**
     * @param bpassword The tDBpassword to set.
     */
    public static void setTDBpassword(String bpassword) {
        TDBpassword = bpassword;
    }
    /**
     * @return Returns the tDBuser.
     */
    public static String getTDBuser() {
        return TDBuser;
    }
    /**
     * @param buser The tDBuser to set.
     */
    public static void setTDBuser(String buser) {
        TDBuser = buser;
    }

    /**
     * @param b
     */
    public static void setDoArchiverDiagnosis(boolean b)
    {
        doArchiverDiagnosis = b;
    }

    /**
     * @param i macroPeriod in seconds
     */
    public static void setMacroPeriod(int i)
    {
        macroPeriod = ( (long) i ) * 1000;
    }
    /**
     * @return Returns the doArchiverDiagnosis.
     */
    public static boolean isDoArchiverDiagnosis() {
        return doArchiverDiagnosis;
    }
    /**
     * @return Returns the macroPeriod.
     */
    public static long getMacroPeriod() {
        return macroPeriod;
    }


    /**
     * @return Returns the dbProxy.
     */
    public static IDbProxy getDbProxy() {
        return dbProxy;
    }
    /**
     * @param dbProxy The dbProxy to set.
     */
    public static void setDbProxy(IDbProxy dbProxy) {
        ArchivingWatch.dbProxy = dbProxy;
    }


    /**
     * @return Returns the defaultSafetyPeriod.
     */
    public static String isDefaultSafetyPeriod() {
        return defaultSafetyPeriod;
    }
    /**
     * @param defaultSafetyPeriod The defaultSafetyPeriod to set.
     */
    public static void setDefaultSafetyPeriod(String _defaultSafetyPeriod) {
        ArchivingWatch.defaultSafetyPeriod = _defaultSafetyPeriod;

        SaferPeriodCalculatorFactory.setUserDefinedDefaultMode ( _defaultSafetyPeriod );
    }
    /**
     * @return Returns the doRetry.
     */
    public static boolean isDoRetry() {
        return doRetry;
    }
    /**
     * @param doRetry The doRetry to set.
     */
    public static void setDoRetry(boolean doRetry) {
        ArchivingWatch.doRetry = doRetry;
    }
    /**
     * @return Returns the doStartOnInitDevice.
     */
    public static boolean isDoStartOnInitDevice() {
        return doStartOnInitDevice;
    }
    /**
     * @param doStartOnInitDevice The doStartOnInitDevice to set.
     */
    public static void setDoStartOnInitDevice(boolean doStartOnInitDevice) {
        ArchivingWatch.doStartOnInitDevice = doStartOnInitDevice;
    }
    /**
     * @return Returns the diaryLogLevel.
     */
    public static String getDiaryLogLevel() {
        return diaryLogLevel;
    }
    /**
     * @param diaryLogLevel The diaryLogLevel to set.
     */
    public static void setDiaryLogLevel(String diaryLogLevel) {
        ArchivingWatch.diaryLogLevel = diaryLogLevel;
    }
    /**
     * @return Returns the diaryPath.
     */
    public static String getDiaryPath() {
        return diaryPath;
    }
    /**
     * @param diaryPath The diaryPath to set.
     */
    public static void setDiaryPath(String diaryPath) {
        ArchivingWatch.diaryPath = diaryPath;
    }
    /**
     * @return Returns the hasDiary.
     */
    public static boolean isHasDiary() {
        return hasDiary;
    }
    /**
     * @param hasDiary The hasDiary to set.
     */
    public static void setHasDiary(boolean hasDiary) {
        ArchivingWatch.hasDiary = hasDiary;
    }
	public static String getTDBHost() {
		return TDBHost;
	}
	public static void setTDBHost(String host) {
		TDBHost = host==null?"":host;
	}
	public static String getTDBName() {
		return TDBName;
	}
	public static void setTDBName(String name) {
		TDBName = name==null?"":name;
	}
	public static String getHDBHost() {
		return HDBHost;
	}
	public static void setHDBHost(String host) {
		HDBHost = host==null?"":host;
	}
	public static String getHDBName() {
		return HDBName;
	}
	public static void setHDBName(String name) {
		HDBName = name==null?"":name;
	}
	public static boolean isHDBRac() {
		return HDBRac;
	}
	public static void setHDBRac(boolean rac) {
		HDBRac = rac;
	}
	public static boolean isTDBRac() {
		return TDBRac;
	}
	public static void setTDBRac(boolean rac) {
		TDBRac = rac;
	}

    public static short getMinNumberOfKoForAlarmState() {
		return minNumberOfKoForAlarmState;
	}
	public static void setMinNumberOfKoForAlarmState(
			short minNumberOfKoForAlarmState) {
		ArchivingWatch.minNumberOfKoForAlarmState = minNumberOfKoForAlarmState;
	}
}
