//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/DbProxy.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DbProxy.
//						(Chinkumo Jean) - 22 janv. 2005
//
// $Author: ounsy $
//
// $Revision: 1.27 $
//
// $Log: DbProxy.java,v $
// Revision 1.27  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.26  2006/12/06 15:07:51  ounsy
// corrected a bug in initCommitThread
//
// Revision 1.25  2006/11/27 16:08:14  ounsy
// The current date is now obtained from System.currentTimeMillis instead of a DB call to SYSDATE
//
// Revision 1.24  2006/11/23 09:56:21  ounsy
// added a commit thread
//
// Revision 1.23  2006/11/20 09:25:49  ounsy
// minor changes
//
// Revision 1.22  2006/10/30 14:32:24  ounsy
// commented the old hasBeenArchiving method and renamed the new hasBeenArchiving2 method to just hasBeenArchiving to avoid confusions
//
// Revision 1.21  2006/08/24 13:52:42  ounsy
// minor changes
//
// Revision 1.20  2006/07/24 09:54:52  ounsy
// the store(ScalarEvent) method now uses insertScalarData_withId instead of insertScalarData
//
// Revision 1.19  2006/07/21 14:44:55  ounsy
// minor changes
//
// Revision 1.18  2006/07/19 08:37:36  ounsy
// catch and rethrow ArchivingException
//
// Revision 1.17  2006/07/18 15:19:32  ounsy
// minor changes
//
// Revision 1.16  2006/06/07 12:55:08  ounsy
// corrected a small bug that displayed a 1Jan1970 date instead of Null for lastInsertRequest in case of missing data
//
// Revision 1.15  2006/05/17 15:02:41  ounsy
// corrected a bug in getLastInsertRequest ()
//
// Revision 1.14  2006/04/11 09:12:06  ounsy
// added some attribute checking methods
//
// Revision 1.13  2006/02/28 10:38:31  ounsy
// lastInsertRequest added for HDBArchiver internal controls
//
// Revision 1.12  2006/02/27 12:17:57  ounsy
// the HdbArchiver internal diagnosis function's safety period calculation is now defined
//  by the safetyPeriod property
//  (for example safetyPeriod=absolute/minutes/15 or safetyPeriod=relative/2)
//
// Revision 1.11  2006/02/15 15:55:43  chinkumo
// Javadoc updated.
//
// Revision 1.10  2006/02/15 12:49:06  ounsy
// added support for HdbArchiver internal support
//
// Revision 1.9  2006/02/06 13:03:29  ounsy
// added a store (SpectrumEventRW) method
//
// Revision 1.8  2006/01/26 10:50:03  ounsy
// commit bug corrected
//
// Revision 1.7  2006/01/23 09:12:18  ounsy
// commit forced in "insertModeRecord" method
//
// Revision 1.6  2006/01/13 14:28:27  chinkumo
// Changes made to avoid multi lines in AMT table when archivers are rebooted.
//
// Revision 1.5  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.4.10.4  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.4.10.3  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.4.10.2  2005/09/26 08:01:20  chinkumo
// Minor changes !
//
// Revision 1.4.10.1  2005/09/09 10:07:50  chinkumo
// The method store is more generic (scalars).
//
// Revision 1.4  2005/06/24 12:06:27  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.3  2005/06/14 10:30:27  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.2.4.1  2005/06/13 14:17:45  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.2  2005/02/04 17:10:15  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.1  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package HdbArchiver.Collector;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.Database;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.HDBDataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DBTools;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.ArchivingWatch;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.device.IDbProxy;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.ISaferPeriodCalculator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;

public class DbProxy implements IDbProxy {
    private Hashtable<String, Integer> periods;
    private Hashtable<String, Long> counters;
    private Hashtable<String, Long> lastInserts;
    private Hashtable<String, Long> lastInsertRequests;

    private IArchivingManagerApiRef manager;

    private ILogger logger;

    public DbProxy(final ILogger logger, final String myDbHost, final String myDbName,
	    final String myDbUser, final String myDbPassword, final boolean myRacConnection) {
	try {
	    manager = ArchivingManagerApiRefFactory.getInstance(ConfigConst.HDB);
	    manager.ArchivingConfigureWithoutArchiverListInit(myDbHost, myDbName, myDbUser,
		    myDbPassword, myRacConnection, false, false);

	    periods = new Hashtable<String, Integer>();
	    counters = new Hashtable<String, Long>();
	    lastInserts = new Hashtable<String, Long>();
	    lastInsertRequests = new Hashtable<String, Long>();

	    // this.initCommitThread();

	    this.logger = logger;
	    ((HDBDataBaseManager) manager.getDataBase()).getHdbAttributeInsert().setLogger(
		    this.logger);

	    ArchivingWatch.setDbProxy(this);
	} catch (final ArchivingException e) {
	    System.err.println(e.toString());
	}
    }

    public boolean is_db_connected() {
	final boolean ret = manager.is_db_connected();
	return ret;
    }

    /**
     * This method gives to the Archiver the event for filing. *
     * 
     * @param scalarEvent
     *            event to be archieved.
     * @throws ArchivingException
     */
    public void store(final ScalarEvent scalarEvent) throws ArchivingException {
	// System.out.println("archive " +
	// scalarEvent.getAttribute_complete_name());
	try {
	    updateCountersForAttributePreInsert(scalarEvent);
	    // ArchivingManagerApi.getDataBase(true).insert_ScalarData(scalarEvent);
	    ((HDBDataBaseManager) manager.getDataBase()).getHdbAttributeInsert().insert_ScalarData(
		    scalarEvent);
	    updateCountersForAttributePostInsert(scalarEvent);
	} catch (final ArchivingException e) {
	    logger.trace(ILogger.LEVEL_ERROR, "Error during scalar insertion " + scalarEvent);
	    logger.trace(ILogger.LEVEL_ERROR, e.toString());
	    throw e;
	}
    }

    /**
     * @param scalarEvent
     */
    private void updateCountersForAttributePreInsert(final ArchivingEvent archivingEvent) {
	final String completeName = archivingEvent.getAttribute_complete_name();
	lastInsertRequests.put(completeName, Long.valueOf(archivingEvent.getTimeStamp()));
    }

    /**
     * 
     * @param archivingEvent
     */
    private void updateCountersForAttributePostInsert(final ArchivingEvent archivingEvent) {
	final String completeName = archivingEvent.getAttribute_complete_name();

	final Long currentCounter_I = counters.get(completeName);
	long currentCounter = currentCounter_I == null ? 0 : currentCounter_I.longValue();
	currentCounter++;
	counters.put(completeName, Long.valueOf(currentCounter));

	lastInserts.put(completeName, Long.valueOf(archivingEvent.getTimeStamp()));
    }

    public void store(final SpectrumEvent_RO spectrumEvent_ro) throws ArchivingException {
	Util.out4.println("DbProxy.store");
	try {
	    updateCountersForAttributePreInsert(spectrumEvent_ro);
	    ((HDBDataBaseManager) manager.getDataBase()).getHdbAttributeInsert()
		    .insert_SpectrumData_RO(spectrumEvent_ro);
	    updateCountersForAttributePostInsert(spectrumEvent_ro);
	} catch (final ArchivingException e) {
	    Util.out2.println(e.toString());
	    throw e;
	}
    }

    public void store(final SpectrumEvent_RW spectrumEvent_rw) throws ArchivingException {
	Util.out4.println("DbProxy.store SpectrumEvent_RW");
	try {
	    updateCountersForAttributePreInsert(spectrumEvent_rw);
	    ((HDBDataBaseManager) manager.getDataBase()).getHdbAttributeInsert()
		    .insert_SpectrumData_RW(spectrumEvent_rw);
	    updateCountersForAttributePostInsert(spectrumEvent_rw);
	} catch (final ArchivingException e) {
	    Util.out4.println("DbProxy.store/ArchivingException");
	    Util.out2.println(e.toString());
	    throw e;
	}
    }

    public Timestamp getLastInsertRequest(final String completeName) throws DevFailed {
	Timestamp ret = null;

	try {
	    final Long value = lastInsertRequests.get(completeName);
	    final long val = value == null ? -1 : value.longValue();
	    ret = val == -1 ? null : new Timestamp(val);
	} catch (final Exception e) {
	    DBTools.throwDevFailed(e);
	}
	return ret;
    }

    public boolean hasBeenArchiving(final String completeName) throws DevFailed {
	final Long lastInsert_L = lastInserts.get(completeName);
	if (lastInsert_L == null) {
	    return false;
	}
	final long lastInsert = lastInsert_L.longValue();

	final Integer period_I = periods.get(completeName);
	if (period_I == null) {
	    final ErrSeverity severity = ErrSeverity.PANIC;
	    final String reason = "UNKNOWN ATTRIBUTE";
	    final String desc = "UNKNOWN ATTRIBUTE";
	    final String origin = "UNKNOWN ATTRIBUTE";

	    final DevError devError = new DevError(reason, severity, desc, origin);
	    final DevError[] ret = new DevError[1];
	    ret[0] = devError;

	    throw new DevFailed(ret);
	}
	final int period = period_I.intValue();

	ISaferPeriodCalculator saferPeriodCalculator = SaferPeriodCalculatorFactory
		.getCurrentImpl();
	if (saferPeriodCalculator == null) {
	    saferPeriodCalculator = SaferPeriodCalculatorFactory
		    .getImpl(SaferPeriodCalculatorFactory.ABSOLUTE);
	}
	final int saferPeriod = saferPeriodCalculator.getSaferPeriod(period);

	// DataBaseApi database = ArchivingManagerApi.getDataBase( true );
	final long now = System.currentTimeMillis();
	final long threshold = now - saferPeriod;

	final boolean ret = lastInsert >= threshold;
	return ret;
    }

    public void store(final ImageEvent_RO imageEvent_RO) throws SQLException, IOException,
	    ArchivingException {
	Util.out4.println("DbProxy.store");
	try {
	    updateCountersForAttributePreInsert(imageEvent_RO);
	    ((HDBDataBaseManager) manager.getDataBase()).getHdbAttributeInsert()
		    .insert_ImageData_RO(imageEvent_RO);
	    updateCountersForAttributePostInsert(imageEvent_RO);
	} catch (final SQLException e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t " + "DbProxy.store" + "\r\n"
		    + "\t Reason : \t " + "UNKNOWN_ERROR" + "\r\n" + "\t Description : \t "
		    + e.getMessage() + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    throw e;
	} catch (final IOException e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t " + "DbProxy.store" + "\r\n"
		    + "\t Reason : \t " + "UNKNOWN_ERROR" + "\r\n" + "\t Description : \t "
		    + e.getMessage() + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    throw e;
	} catch (final ArchivingException e) {
	    Util.out2.println(e.toString());
	    throw e;
	}
    }

    public void insertModeRecord(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	manager.getDataBase().getMode().insertModeRecord(attributeLightMode);
    }

    public boolean isArchived(final String att_name) throws ArchivingException {
	return manager.getDataBase().getMode().isArchived(att_name);

    }

    public boolean isArchived(final String att_name, final String device_name)
	    throws ArchivingException {
	return manager.getDataBase().getMode().isArchived(att_name, device_name);

    }

    public void updateModeRecord(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {

	manager.getDataBase().getMode().updateModeRecord(
		attributeLightMode.getAttribute_complete_name());
    }

    public void updateModeRecord(final String att_name) throws ArchivingException {
	manager.getDataBase().getMode().updateModeRecord(att_name);
    }

    public void updateModeRecordWithoutStop(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	manager.getDataBase().getMode().updateModeRecordWithoutStop(attributeLightMode);
    }

    public Vector<AttributeLightMode> getArchiverCurrentTasks(final String archiverName)
	    throws ArchivingException {
	Util.out4.println("DbProxy.getArchiverCurrentTasks");
	// Vector<AttributeLightMode> archiverCurrentTasks = new
	// Vector<AttributeLightMode>();
	final boolean facility = manager.isM_Facility();

	try {
	    final Vector<AttributeLightMode> archiverCurrentTasks = manager.getDataBase().getMode()
		    .getArchiverCurrentTasks(
			    (facility ? "//" + new Database().get_tango_host() + "/" : "")
				    + archiverName);
	    System.out.println("Current Tasks - " + archiverCurrentTasks.size() + ": ");
	    for (int i = 0; i < archiverCurrentTasks.size(); i++) {
		final AttributeLightMode attributeLightMode = archiverCurrentTasks.elementAt(i);
		// System.out.println("*******************     " + (i + 1)
		// + "     *******************");
		System.out.println(attributeLightMode);
	    }
	    return archiverCurrentTasks;
	} catch (final DevFailed devFailed) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
		    + GlobalConst.DBT_UNREACH_EXCEPTION;
	    final String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	    final String desc = "Failed while executing DbProxy.getArchiverCurrentTasks() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "", devFailed);
	}
    }

    /**
     * @param completeName
     * @param period
     */
    public void setPeriodForAttribute(final String completeName, final Integer period) {
	periods.put(completeName, period);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.device.IDbProxy
     * #getLastInsert(java.lang.String)
     */
    public Timestamp getLastInsert(final String completeName) throws DevFailed {
	Timestamp ret = null;
	final Long lastInsert_L = lastInserts.get(completeName);
	if (lastInsert_L != null) {
	    ret = new Timestamp(lastInsert_L.longValue());
	}
	return ret;
    }
}
