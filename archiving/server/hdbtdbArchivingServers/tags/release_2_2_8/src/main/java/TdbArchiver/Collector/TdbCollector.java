//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/TdbCollector.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TdbCollector.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.32 $
//
// $Log: TdbCollector.java,v $
// Revision 1.32  2007/06/13 13:12:03  pierrejoseph
// modeHandler is stored in a common archiver part
//
// Revision 1.31  2007/06/11 12:16:33  pierrejoseph
// doArchive method has been added in the TdbCollector class with new catched exceptions that can be raised by the isDataArchivable method.
//
// Revision 1.30  2007/06/07 09:54:55  pierrejoseph
// Hastable specification
//
// Revision 1.29  2007/06/01 09:45:53  pierrejoseph
// Attribute ArchiverCollectro.logger has been renamed in m_logger
//
// Revision 1.28  2007/05/25 12:03:36  pierrejoseph
// Pb mode counter on various mode in the same collector : one ModesCounters object by attribut stored in a hashtable of the ArchiverCollector object (in common part)
//
// Revision 1.27  2007/04/03 15:15:13  ounsy
// corrected a deadlock problem
//
// Revision 1.26  2007/03/27 15:17:06  ounsy
// added a isFirstValue attribute
//
// Revision 1.25  2007/03/26 14:36:13  ounsy
// trying a new method to avoid double records.
//
// Revision 1.24  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.23  2007/02/26 09:51:51  ounsy
// the cleaning is never done directly by the archivers themselves anymore
// (even in MySQL)
//
// Revision 1.22  2007/01/09 15:25:28  ounsy
// set the most frequent traces to DEBUG level
//
// Revision 1.21  2006/11/15 16:29:42  ounsy
// refresher with synchronized period
//
// Revision 1.20  2006/10/19 12:24:37  ounsy
// modified exportFile2Db() to take a new parameter isAsynchronous
//
// Revision 1.19  2006/08/08 13:41:14  ounsy
// no more cleaning thread with oracle : the database auto cleans
//
// Revision 1.18  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.17  2006/07/25 13:39:14  ounsy
// correcting bad merge
//
// Revision 1.16  2006/07/25 09:47:29  ounsy
// added a loadAssessment() method
//
// Revision 1.15  2006/07/21 14:42:14  ounsy
// replaced the lastTimestampHashtable with lastTimestampStacksHashtable, to keep track of more than one timestamp in the past
//
// Revision 1.14  2006/07/18 08:01:28  ounsy
// exportFile2Db() now returns the table name
//
// Revision 1.13  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.12  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.11  2006/06/07 12:57:02  ounsy
// minor changes
//
// Revision 1.10  2006/06/02 08:40:57  ounsy
// now keep a reference to the Warnable that instantiated it so this API class can warn the required TdbArchiver device
//
// Revision 1.9  2006/05/23 11:56:04  ounsy
// added a lastTimestamp Hashtable in the same vein as the lastValue one in HdbModeHandler to avoid getting twice the same timestamp in a row
//
// Revision 1.8  2006/05/16 09:29:30  ounsy
// added what's necessary for the old files deletion mechanism
//
// Revision 1.7  2006/05/12 09:20:17  ounsy
// list concurrent modification bug correction
//
// Revision 1.6  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.5.10.4  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.5.10.3  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.5.10.2  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.5.10.1  2005/09/09 10:17:59  chinkumo
// Minor changes.
//
// Revision 1.5  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.6.1  2005/06/13 13:47:46  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/02/07 09:37:15  chinkumo
// To avoid side effects when including events in ATK, AttributeList object is replaced with the AttributePolledList object.
//
// Revision 1.3  2005/02/04 17:10:40  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/31 15:09:05  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:24  chinkumo
// First commit (new architecture).
//
// Revision 1.5  2004/09/30 15:19:08  chinkumo
// The sleeping time for the thread wich deletes records was moved from 30 minutes to 10.
//
// Revision 1.4  2004/09/14 07:33:51  chinkumo
// Some unused 'import' were removed.
// Some error messages were re-written to fit the 'error policy' recently decided.
//
// A new class was included inside 'TdbCollector' class.
// This class was named 'KeepingThread'. This class represent a thread wich the job is to delete, periodicaly, database's old records.
//
// Revision 1.3  2004/09/01 15:32:25  chinkumo
// Heading was updated.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package TdbArchiver.Collector;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import Common.Archiver.Collector.ArchiverCollector;
import TdbArchiver.Collector.Tools.FileTools;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributePolledList;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IAttribute;
import fr.esrf.tangoatk.core.IEntity;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.LimitedStack;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Warnable;

public abstract class TdbCollector extends ArchiverCollector {
    /**
     * This list is the list used by the refresher of the Collector
     */
    protected AttributePolledList attributeList;
    /**
     * This field represent the refreshing state of the Collector
     */
    protected boolean refreshing;
    /**
     * A file is associated to each archived attribute. We them must keep a the
     * association (attribute name / corresponding file )
     */
    protected Hashtable<String, FileTools> filesNames;
    /**
     * This parameter specify the number of time a Collector retry the archiving
     * of an attribute event
     */
    // protected static int tryNumber = 1;
    protected final static int DEFAULT_TRY_NUMBER = 2;

    protected KeepingThread keepingThread;

    protected long keepingPeriod;

    // protected Hashtable lastTimestampHashtable;
    protected static Hashtable<String, LimitedStack> lastTimestampStacksHashtable;
    protected boolean isFirstValue;
    protected Warnable warnable;

    protected DbProxy dbProxy;

    protected String m_currentDbPath = "";

    protected String m_currentDsPath = "";

    private final AttrWriteType writableType;

    public TdbCollector(final TdbModeHandler _modeHandler, final String currentDsPath,
	    final String currentDbPath, final AttrWriteType writableType) {
	super(_modeHandler);
	this.writableType = writableType;
	attributeList = new AttributePolledList();

	filesNames = new Hashtable<String, FileTools>();

	lastTimestampStacksHashtable = getLastTimestampsInstance();
	isFirstValue = true;
	m_currentDbPath = currentDbPath;
	m_currentDsPath = currentDsPath;
    }

    private static Hashtable<String, LimitedStack> getLastTimestampsInstance() {
	if (lastTimestampStacksHashtable == null) {
	    lastTimestampStacksHashtable = new Hashtable<String, LimitedStack>();
	}
	return lastTimestampStacksHashtable;
    }

    protected AttrWriteType getWritableValue() {
	return writableType;
    }

    /**
     * Returns a boolean to know wheather the attribute list is empty or not
     * 
     * @return A boolean to know wheather the attribute list is empty or not
     */
    public boolean hasEmptyList() {
	if (attributeList != null) {
	    return attributeList.isEmpty();
	} else {
	    return true;
	}
    }

    /**
     * Adds an attribute to the list of the attributes for which is responsible
     * this TdbCollector.
     * 
     * @param attributeLightMode
     * @throws ArchivingException
     */
    public void addSource(final AttributeLightMode attributeLightMode, final int attributePerFile)
	    throws ArchivingException {
	try {
	    synchronized (attributeList) {
		final String att_name = attributeLightMode.getAttribute_complete_name();
		final IEntity attribute = attributeList.add(att_name);
		addListeners(attribute);
		Util.out4.println("\t The attribute named " + att_name
			+ " was hired to the Collector list...");

		// informs the mother class that one new attribute must be
		// managed
		addAttribute(att_name);

		final String table_name = dbProxy.getDataBase().getDbUtil().getTableName(att_name);
		if (filesNames.get(att_name) == null) {
		    final FileTools myFile = new FileTools(attributeLightMode
			    .getAttribute_complete_name(), table_name, attributeLightMode
			    .getData_format(), attributeLightMode.getWritable(), attributeLightMode
			    .getMode().getTdbSpec().getExportPeriod(), super.m_logger, dbProxy,
			    m_currentDsPath, m_currentDbPath);
		    myFile.setAttributePerFile(attributePerFile);
		    filesNames.put(att_name, myFile);
		}
		// Verify that the recording file exists

		startCollecting();

		if (attributeList.get(att_name) == null) {
		    super.m_logger.trace(ILogger.LEVEL_WARNING,
			    "addSource/The first add test failed for attribute|" + att_name);
		}
	    }
	} catch (final ConnectionException e) {
	    super.m_logger.trace(ILogger.LEVEL_WARNING, e);

	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '"
		    + attributeLightMode.getAttribute_complete_name() + "' as source";
	    final String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	    final String desc = "Failed while executing BooleanScalar.addSource() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
	} catch (final Exception e) {
	    super.m_logger.trace(ILogger.LEVEL_WARNING, "Unexpected exception during addSource:"
		    + attributeLightMode.getAttribute_complete_name());
	    super.m_logger.trace(ILogger.LEVEL_WARNING, e);
	}
    }

    public abstract void addListeners(IEntity attribute) throws ArchivingException;

    /**
     * Removes an attribute from the list of the attributes for which is
     * responsible this TdbCollector.
     * 
     * @param attributeName
     * @throws ArchivingException
     */
    public void removeSource(final String attributeName, final boolean closeFile)
	    throws ArchivingException {
	Util.out2.println("BooleanScalar.removeSource");
	try {
	    synchronized (attributeList) {
		/*
		 * while ( ( IBooleanScalar ) attributeList.get(attributeName)
		 * != null ) {
		 */
		final IEntity attribute = attributeList.get(attributeName);
		if (attribute != null) {
		    removeListeners(attribute);

		    attributeList.remove(attributeName);

		    // informs the mother class that one new attribute must be
		    // removed
		    removeAttribute(attributeName);

		    Util.out4.println("\t The attribute named " + attributeName
			    + " was fired from the Collector list...");
		    if (closeFile) {
			filesNames.get(attributeName).stopFileGeneration();
			filesNames.remove(attributeName);
		    }
		    if (attributeList.isEmpty()) {
			stopCollecting();
		    }
		}
	    }
	} catch (final Exception e) {
	    final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed removing '"
		    + attributeName + "' from sources";
	    final String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	    final String desc = "Failed while executing BooleanScalar.removeSource() method...";
	    throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, "", e);
	}
    }

    public abstract void removeListeners(IEntity attribute) throws ArchivingException;

    /**
     * Adds an attribute to the list of the attributes for which is responsible
     * this TdbCollector.
     * 
     * @param attributeName
     *            The name of the attibute of witch data are expected.
     * @throws IOException
     */
    public String exportFile2Db(final String attributeName) throws IOException {
	System.out.println("TdbCollector/exportFile2Db/START");
	return filesNames.get(attributeName).switchFile();
    }

    /**
     * Triggers the collecting action of this TdbCollector.
     */
    public void startCollecting() {
	synchronized (attributeList) {
	    if (!attributeList.isEmpty()) {
		if (attributeList.size() == 1) {
		    attributeList.setRefreshInterval(m_modeHandler.getRefreshInterval());
		    attributeList.setSynchronizedPeriod(true);
		    attributeList.startRefresher();
		    refreshing = true;
		    if (keepingThread != null) {
			keepingThread.start();
		    }
		} else {
		    // force a refresh for newly added attributes
		    attributeList.refresh();
		}
	    }
	}
    }

    /**
     * Stops the collecting action of this TdbCollector.
     */
    public void stopCollecting() {
	Util.out4.println("TdbCollector.stopCollecting");
	try {
	    synchronized (attributeList) {
		if (attributeList.isEmpty()) {
		    attributeList.stopRefresher();
		    if (keepingThread != null) {
			keepingThread.destroy();
			keepingThread = null;
		    }
		    refreshing = false;
		}
	    }
	} catch (final Exception e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "TdbCollector.stopCollecting" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    e.printStackTrace();
	}
    }

    public void errorChange(final ErrorEvent errorEvent) {
	final long temp = System.currentTimeMillis();
	Util.out4.println("\n" + this.getClass().toString() + " (" + temp + ")"
		+ "\n\t Source \t : " + errorEvent.getSource().toString() + "(" + temp + ")"
		+ "\n\t TimeS \t : " + errorEvent.getTimeStamp() + "(" + temp + ")");

    }

    public String assessment() {
	final StringBuffer ass = new StringBuffer();
	ass.append("Collector Reference : " + toString() + "\r\n");
	ass.append("Activity (refreshing) : " + isRefreshing() + "\r\n");
	ass.append("Mode : " + "\r\n" + m_modeHandler.getMode().toString() + "\r\n");
	ass.append("Attribute list (" + attributeList.getSize() + "): " + "\r\n");
	final Enumeration myAttList = attributeList.elements();
	int i = 1;
	while (myAttList.hasMoreElements()) {
	    final IAttribute iNumberScalar = (IAttribute) myAttList.nextElement();
	    ass.append("\t" + i++ + "\t" + iNumberScalar.getName() + "\r\n");
	}
	return ass.toString();
    }

    // public static synchronized void checkGC() {
    // if (TdbArchiver.gc_counter < TdbArchiver.gc_counter_limit) { // 1x /
    // // hour
    // // if
    // // period
    // // time
    // // is 1
    // // second
    // TdbArchiver.gc_counter++;
    // } else {
    // Util.out4
    // .println("TdbCollector.checkGC : Launching the garbage collector...");
    // TdbArchiver.gc_counter = 0;
    // // new CleanerThread().start();
    // }
    // }

    public boolean isRefreshing() {
	return refreshing;
    }

    private class KeepingThread extends Thread {
	long keeping_time = m_modeHandler.getMode().getTdbSpec().getKeepingPeriod();
	long sleeping_time = 10 * 60 * 1000; // Deletes records every 10 minutes
	String state = "NOT RUNNING";

	public KeepingThread() {
	    super("KeepingThread");
	}

	public void activate(final boolean b) {
	    if (b) {
		state = "RUNNING";
	    } else {
		state = "NOT RUNNING";
	    }
	}

	/**
	 * This method is called by the system to give a Thread a chance to
	 * clean up before it actually exits.
	 */
	@Override
	public void destroy() {
	    System.out.println("KeepingThread.destroy");
	    activate(false);
	    interrupt();
	}

	@Override
	public void run() {
	    System.out.println("KeepingThread.run");
	    state = "RUNNING";
	    while (state.equals("RUNNING")) {
		try {
		    sleep(sleeping_time);
		} catch (final InterruptedException e) {
		    activate(false);
		    /*
		     * System.err.println( "Reason :         Unknown !!!" +
		     * "\r\n" +
		     * "Description : The thread does not want to sleep !!" +
		     * "\r\n" + "Origin :            KeepingThread.run");
		     * e.printStackTrace();
		     */
		}
		Util.out4.println("KeepingThread.run : Getting attribute list...");
		final AttributePolledList tmpAttributeList = attributeList;
		final String[] m_attributeList = new String[tmpAttributeList.size()];
		for (int i = 0; i < m_attributeList.length; i++) {
		    m_attributeList[i] = ((IEntity) tmpAttributeList.getElementAt(i)).getName();
		}
		try {
		    Util.out4
			    .println("KeepingThread.run : Sending the attribute list to the database...");
		    dbProxy.deleteOldRecords(keeping_time, m_attributeList);
		} catch (final ArchivingException e) {
		    Util.out2.println(e.toString());
		}
	    }
	}
    }

    protected void setLastTimestamp(final ArchivingEvent scalarEvent) {
	if (scalarEvent != null) {
	    final String name = scalarEvent.getAttribute_complete_name();
	    final Long longTimeStamp = new Long(scalarEvent.getTimeStamp());

	    final LimitedStack lastTimestampStack = getLastTimestampStack(name);
	    lastTimestampStack.push(longTimeStamp);
	}
    }

    private LimitedStack getLastTimestampStack(final String name) {
	LimitedStack lastTimestampStack = lastTimestampStacksHashtable.get(name);

	if (lastTimestampStack == null) {
	    lastTimestampStack = new LimitedStack();
	    lastTimestampStacksHashtable.put(name, lastTimestampStack);
	}

	return lastTimestampStack;
    }

    protected boolean isDataArchivableTimestampWise(final ArchivingEvent scalarEvent) {
	final String name = scalarEvent.getAttribute_complete_name();

	final long newTime = scalarEvent.getTimeStamp();
	if (newTime == 0) {
	    final String message = "TdbCollector/isDataArchivableTimestampWise/Received a zero timestamp ("
		    + newTime
		    + " = "
		    + new Timestamp(newTime)
		    + ") for attribute|"
		    + name
		    + "|tableName|" + scalarEvent.getTable_name() + "|";
	    m_logger.trace(ILogger.LEVEL_WARNING, message);
	    warnable.trace(message, Warnable.LOG_LEVEL_WARN);

	    return false;
	}

	final LimitedStack lastTimestampStack = lastTimestampStacksHashtable.get(name);
	if (lastTimestampStack == null) {
	    // System.out.println ( "isDataArchivableTimestampWise/name/2");
	    return true;
	}

	final boolean isAlreadyRegisteredDate = lastTimestampStack.containsDate(newTime, m_logger);
	// System.out.println (
	// "isDataArchivableTimestampWise/isAlreadyRegisteredDate/"+isAlreadyRegisteredDate);
	if (isAlreadyRegisteredDate) {
	    final String message = "TdbCollector/isDataArchivableTimestampWise/FALSE for attribute|"
		    + name + "|and timestamp|" + new Timestamp(newTime) + "|";
	    m_logger.trace(ILogger.LEVEL_DEBUG, message);
	    Util.out1.println(message);

	    return false;
	}
	final boolean isValidRegisteredDate = lastTimestampStack.validateDate(newTime, m_logger);
	// System.out.println (
	// "isDataArchivableTimestampWise/isAlreadyRegisteredDate/"+isAlreadyRegisteredDate);

	if (!isValidRegisteredDate) {
	    final String message = "TdbCollector/isDataArchivableTimestampWise/Invalid Timestamp for attribute|"
		    + name + "|and timestamp|" + new Timestamp(newTime) + "|";
	    m_logger.trace(ILogger.LEVEL_DEBUG, message);
	    Util.out1.println(message);

	    return false;
	}

	// System.out.println ( "isDataArchivableTimestampWise/name/3");
	return true;
    }

    public void setWarnable(final Warnable _warnable) {
	warnable = _warnable;
    }

    public short[] loadAssessment() {
	final short[] ret = new short[3];
	synchronized (attributeList) {
	    final Enumeration myAttList = attributeList.elements();
	    // int i = 1;
	    while (myAttList.hasMoreElements()) {
		final IAttribute nextAttr = (IAttribute) myAttList.nextElement();
		final int X = nextAttr.getMaxXDimension();
		final int Y = nextAttr.getMaxYDimension();

		short type = 0;
		if (X > 1) {
		    type++;
		}
		if (Y > 1) {
		    type++;
		}

		ret[type]++;
	    }
	}

	return ret;
    }

    public void setDbProxy(final DbProxy _dbProxy) {
	dbProxy = _dbProxy;
    }

    public List<String> getAttributeList() {
	final List<String> attributeListResult = new ArrayList<String>();
	final Enumeration<?> myAttList = attributeList.elements();
	while (myAttList.hasMoreElements()) {
	    final IAttribute attr = (IAttribute) myAttList.nextElement();
	    attributeListResult.add(attr.getName());
	}
	return attributeListResult;
    }
}