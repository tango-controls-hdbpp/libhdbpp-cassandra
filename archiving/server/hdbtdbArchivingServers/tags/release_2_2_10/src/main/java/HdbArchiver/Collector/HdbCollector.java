//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/HdbCollector.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HdbCollector.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.29 $
//
// $Log: HdbCollector.java,v $
// Revision 1.29  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.28  2007/09/28 14:49:22  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.27  2007/06/13 13:13:23  pierrejoseph
// the mode counter are managed attribute by attribute in each collector
//
// Revision 1.26  2007/06/11 12:18:26  pierrejoseph
// Now it inherits from the ArchiverCollector object.
// doArchive method has been added in the TdbCollector class with new catched exceptions that can be raised by the isDataArchivable method.
//
// Revision 1.25  2007/04/03 15:40:54  ounsy
// corrected the deadlock
//
// Revision 1.24  2007/03/27 15:17:06  ounsy
// added a isFirstValue attribute
//
// Revision 1.23  2007/03/26 14:36:13  ounsy
// trying a new method to avoid double records.
//
// Revision 1.22  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.21  2007/01/09 15:25:28  ounsy
// set the most frequent traces to DEBUG level
//
// Revision 1.20  2006/11/15 16:29:42  ounsy
// refresher with synchronized period
//
// Revision 1.19  2006/08/23 09:41:57  ounsy
// minor changes
//
// Revision 1.18  2006/07/27 12:38:43  ounsy
// changed the logs with the correct HdbCollector name instead of TdbCollector
//
// Revision 1.17  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.16  2006/07/25 13:39:14  ounsy
// correcting bad merge
//
// Revision 1.15  2006/07/25 09:46:43  ounsy
// added a loadAssessment() method
//
// Revision 1.14  2006/07/24 14:51:15  ounsy
// corrected a bug in the assessment() method
//
// Revision 1.13  2006/07/21 14:42:14  ounsy
// replaced the lastTimestampHashtable with lastTimestampStacksHashtable, to keep track of more than one timestamp in the past
//
// Revision 1.12  2006/07/18 07:51:03  ounsy
// added a log message in case the received timestamp is 0
//
// Revision 1.11  2006/06/30 08:27:07  ounsy
// modified isDataArchivableTimestampWise () so that event with a zero timestamp aren't archived
//
// Revision 1.10  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.9  2006/06/13 13:28:20  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.8  2006/05/23 11:55:45  ounsy
// added a lastTimestamp Hashtable in the same vein as the lastValue one in HdbModeHandler to avoid getting twice the same timestamp in a row
//
// Revision 1.7  2006/05/12 09:20:17  ounsy
// list concurrent modification bug correction
//
// Revision 1.6  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.5.12.3  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.5.12.2  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.5.12.1  2005/09/09 10:08:42  chinkumo
// Minor changes.
//
// Revision 1.5  2005/06/14 10:30:27  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.4.1  2005/06/13 14:10:45  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/02/07 09:36:48  chinkumo
// To avoid side effects when including events in ATK, AttributeList object is replaced with the AttributePolledList object.
//
// Revision 1.3  2005/02/04 17:10:15  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:13  chinkumo
// First commit (new architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package HdbArchiver.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import Common.Archiver.Collector.ArchiverCollector;
import HdbArchiver.HdbArchiver;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributePolledList;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IAttribute;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.LimitedStack;

public abstract class HdbCollector extends ArchiverCollector {
    /**
     * This list is the list used by the refresher of the Collector
     */
    protected AttributePolledList attributeList = new AttributePolledList();
    /**
     * This field represent the refreshing rate of the Collector
     */
    protected boolean refreshing;

    protected boolean isFirstValue;

    /**
     * This parameter specify the number of time a Collector retry the archiving
     * of an attribute event
     */
    // protected int tryNumber = 1;
    protected final static int DEFAULT_TRY_NUMBER = 2;
    /**
     * An HdbModeHandler is associated to each HdbCollector to handle the
     * archiving mode
     */
    // private HdbModeHandler m_modeHandler;
    // protected Hashtable lastTimestampHashtable;
    protected static Hashtable<String, LimitedStack> lastTimestampStacksHashtable;
    protected DbProxy dbProxy;

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
    protected int archive_listeners_counter = 0;

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//

    public HdbCollector(final HdbModeHandler _modeHandler) {
	super(_modeHandler);
	lastTimestampStacksHashtable = getLastTimestampsInstance();
	isFirstValue = true;
    }

    private static Hashtable<String, LimitedStack> getLastTimestampsInstance() {
	if (lastTimestampStacksHashtable == null) {
	    lastTimestampStacksHashtable = new Hashtable<String, LimitedStack>();
	}
	return lastTimestampStacksHashtable;

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
     * this HdbCollector. If possible registers for the archive events
     * 
     * @param attributeLightMode
     */

    public void addSource(final AttributeLightMode attributeLightMode) throws ArchivingException {
	if (HdbArchiver.isUseEvents) {
	    addSourceForEvents(attributeLightMode);
	} else {
	    addSourceForPolling(attributeLightMode);
	}
    }

    protected abstract void addSourceForPolling(final AttributeLightMode attributeLightMode)
	    throws ArchivingException;

    protected abstract void addSourceForEvents(final AttributeLightMode attributeLightMode)
	    throws ArchivingException;

    /**
     * Removes an attribute from the list of the attributes for which is
     * responsible this HdbCollector. If the attribute was registered for the
     * archive events, we unsubscribe from it.
     * 
     * @param attributeName
     * @throws ArchivingException
     */
    public abstract void removeSource(String attributeName) throws ArchivingException;

    /**
     * Triggers the collecting action of this HdbCollector.
     */
    public void startCollecting() {
	synchronized (attributeList) {
	    if (!attributeList.isEmpty()) {
		if (attributeList.size() == 1) {
		    attributeList.setRefreshInterval(m_modeHandler.getRefreshInterval());
		    attributeList.setSynchronizedPeriod(true);
		    attributeList.startRefresher();
		    refreshing = true;
		} else {
		    // force a refresh for newly added attributes
		    attributeList.refresh();
		}
	    }
	}
    }

    /**
     * Stops the collecting action of this HdbCollector.
     */
    public void stopCollecting() {
	Util.out4.println("HdbCollector.stopCollecting");
	m_logger.trace(ILogger.LEVEL_INFO, "HdbCollector.stopCollecting");
	try {
	    synchronized (attributeList) {
		if (attributeList.isEmpty()) {
		    attributeList.stopRefresher();
		    refreshing = false;
		}
	    }
	} catch (final Exception e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "HdbCollector.stopCollecting" + "\r\n" + "\t Reason : \t " + "UNKNOWN_ERROR"
		    + "\r\n" + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \t " + "" + "\r\n");
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
	synchronized (attributeList) {
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
	}
	return ass.toString();
    }

    public boolean isRefreshing() {
	return refreshing;
    }

    protected void setLastTimestamp(final ArchivingEvent scalarEvent) {
	if (scalarEvent != null) {
	    final String name = scalarEvent.getAttribute_complete_name();
	    final Long longTimeStamp = new Long(scalarEvent.getTimeStamp());

	    // lastTimestampHashtable.put ( name ,longTimeStamp );
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

    protected boolean isDataArchivableTimestampWise(final ArchivingEvent archivingEvent) {
	final String name = archivingEvent.getAttribute_complete_name();

	final long newTime = archivingEvent.getTimeStamp();
	if (newTime == 0) {
	    final String message = "HdbCollector/isDataArchivableTimestampWise/Received a zero timestamp ("
		    + newTime
		    + " = "
		    + new Timestamp(newTime)
		    + ") for attribute|"
		    + name
		    + "|tableName|" + archivingEvent.getTable_name() + "|";
	    m_logger.trace(ILogger.LEVEL_WARNING, message);
	    Util.out1.println(message);

	    return false;
	}

	final LimitedStack lastTimestampStack = lastTimestampStacksHashtable.get(name);
	if (lastTimestampStack == null) {
	    return true;
	}

	final boolean isAlreadyRegisteredDate = lastTimestampStack.containsDate(newTime, m_logger);
	final boolean isValidDate = lastTimestampStack.validateDate(newTime, m_logger);
	if (isAlreadyRegisteredDate || !isValidDate) {
	    final String message = "HdbCollector/isDataArchivableTimestampWise/FALSE for attribute|"
		    + name + "|and timestamp|" + new Timestamp(newTime) + "|";
	    m_logger.trace(ILogger.LEVEL_DEBUG, message);
	    Util.out1.println(message);

	    return false;
	}

	return true;
    }

    public short[] loadAssessment() {
	final short[] ret = new short[3];
	synchronized (attributeList) {
	    final Enumeration myAttList = attributeList.elements();
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

    public List<String> getAttributeList() {
	final List<String> attributeListResult = new ArrayList<String>();
	final Enumeration<?> myAttList = attributeList.elements();
	while (myAttList.hasMoreElements()) {
	    final IAttribute attr = (IAttribute) myAttList.nextElement();
	    attributeListResult.add(attr.getName());
	}
	return attributeListResult;
    }

    public void setDbProxy(final DbProxy _dbProxy) {
	dbProxy = _dbProxy;
    }

    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//
    /**
     * 
     * @return true if there are archive listeners still alive.
     * @since events
     * @author Giacomo Strangolino
     */
    protected boolean areThereArchiveListeners() {
	if (archive_listeners_counter > 0) {
	    return true;
	}
	return false;
    }

    /* Print the exceptions */
    public void print_exception(final DevFailed df) {
	int err;
	System.err.println("\n-----------------------------------------------");
	System.err.println("\033[1;31mError\033[0m");
	for (err = 0; err < df.errors.length; err++) {
	    System.err.println("\033[4mDescription\033[0m: " + df.errors[err].desc);
	    System.err.println("\033[4mOrigin\033[0m: " + df.errors[err].origin);
	    System.err.println("\033[4mReason\033[0m: " + df.errors[err].reason);
	}
	System.err.println("-----------------------------------------------\n");
    }

    /* Prints the exception putting a message on the first line */
    public void print_exception(final String mess, final DevFailed df) {
	System.err.print("\n-> \033[4m" + mess + "\033[0m");
	print_exception(df);
    }
    // --------------------------------------------------------------------------//
    // ELETTRA : Archiving Events
    // --------------------------------------------------------------------------//

}
