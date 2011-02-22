//+======================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Diary/DefaultLogger.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DefaultLogger.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: DefaultLogger.java,v $
// Revision 1.2  2006/07/18 15:17:34  ounsy
// added a file-changing mechanism to change log files when the day changes
//
// Revision 1.1  2006/06/16 09:24:03  ounsy
// moved from the TdbArchiving project
//
// Revision 1.3  2006/06/15 15:15:30  ounsy
// padded the dates with "0" if they're too short
//
// Revision 1.2  2006/06/13 13:30:04  ounsy
// minor changes
//
// Revision 1.1  2006/06/08 08:34:49  ounsy
// creation
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.commonarchivingapi.ArchivingTools.Diary;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DateHeure;

public class DefaultLogger extends AbstractLogger {
    private DateHeure timeOfLastLog;
    private final String archiver;
    private final String path;

    public DefaultLogger(final String _archiver, final String _path) {
	super();
	System.out.println("default logger");
	archiver = _archiver;
	path = _path;
	super.setTraceLevel(ILogger.LEVEL_DEBUG);

	try {
	    super.initDiaryWriter(_path, _archiver);
	} catch (final IOException e) {
	    e.printStackTrace();
	}

	final DateHeure now = new DateHeure();
	setTimeOfLastLog(now);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.logs.AbstractLogger#getDecoratedLog(java.lang.String, int)
     */
    @Override
    protected String getDecoratedLog(String msg, final int level) {
	switch (level) {
	case ILogger.LEVEL_CRITIC:
	    msg = "CRITIC: " + msg;
	    break;

	case ILogger.LEVEL_ERROR:
	    msg = "ERROR: " + msg;
	    break;

	case ILogger.LEVEL_WARNING:
	    msg = "WARNING: " + msg;
	    break;

	case ILogger.LEVEL_INFO:
	    msg = "INFO: " + msg;
	    break;

	case ILogger.LEVEL_DEBUG:
	    msg = "DEBUG: " + msg;
	    break;

	default:

	    break;
	}

	return msg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.logs.AbstractLogger#addToDiary(int, java.lang.Object)
     */
    @Override
    protected void addToDiary(final int level, final Object o) {
	if (o instanceof String) {
	    String msg = (String) o;
	    msg = getDecoratedDiaryEntry(level, msg);
	    write(writer, msg, true);
	} else if (o instanceof Exception) {
	    final Exception e = (Exception) o;
	    e.printStackTrace();

	    write(writer, "    " + e.toString(), true);
	    final StackTraceElement[] stack = e.getStackTrace();
	    for (final StackTraceElement element : stack) {
		write(writer, "        at " + element.toString(), true);
	    }
	} else {
	    // System.out.println (
	    // "DefaultLogger/addToDiary/other/"+o.getClass().toString()+"/"
	    // );
	}
    }

    /**
     * @param msg
     * @return 6 juil. 2005
     */
    private String getDecoratedDiaryEntry(final int level, final String msg) {
	final String time = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss::SS").format(new Date());
	final StringBuffer buff = new StringBuffer();
	buff.append(time);
	buff.append(" - ");
	buff.append(levels.get(level));
	buff.append(" - ");
	buff.append(msg);

	return buff.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.logs.AbstractLogger#log(java.lang.String)
     */
    @Override
    protected void log(final String msg) {
	System.out.println(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.logs.ILogger#close()
     */
    @Override
    public void close() {
	if (writer != null) {
	    writer.close();
	}
    }

    /**
     * @param pw
     * @param s
     * @param hasNewLine
     * @throws Exception
     *             8 juil. 2005
     */
    public static void write(final PrintWriter pw, final String s, final boolean hasNewLine) {
	if (pw != null) {
	    if (hasNewLine) {
		pw.println(s);
	    } else {
		pw.print(s);
	    }
	    pw.flush();
	} else {
	    System.err.println("file is null - " + s);
	}
    }

    @Override
    protected void changeDiaryFileIfNecessary() throws IOException {
	final DateHeure timeOfLastLog = getTimeOfLastLog();
	final DateHeure now = new DateHeure();

	if (needsNewFile(timeOfLastLog, now)) {
	    close();
	    super.initDiaryWriter(path, archiver);
	}

	setTimeOfLastLog(now);
    }

    private boolean needsNewFile(final DateHeure timeOfLastLog2, final DateHeure now) {
	final String separator = "|";

	/*
	 * String timeOfLastLogToTheMinute = timeOfLastLog2.getJour() +
	 * separator + timeOfLastLog2.getHeure () + separator +
	 * timeOfLastLog2.getMinute (); String nowToTheMinute = now.getJour() +
	 * separator + now.getHeure () + separator + now.getMinute (); boolean
	 * areTheSameMinute = nowToTheMinute.equals( timeOfLastLogToTheMinute );
	 * 
	 * System.out.println (
	 * "DefaultLogger/needsNewFile/now/"+now+"/timeOfLastLog/"
	 * +timeOfLastLog+
	 * "/timeOfLastLogToTheMinute/"+timeOfLastLogToTheMinute+
	 * "/nowToTheMinute/"
	 * +nowToTheMinute+"/areTheSameMinute/"+areTheSameMinute );
	 * 
	 * return ! areTheSameMinute;
	 */

	final String timeOfLastLogToTheDay = timeOfLastLog2.getAnnee() + separator
		+ timeOfLastLog2.getMois() + separator + timeOfLastLog2.getJour();
	final String nowToTheDay = now.getAnnee() + separator + now.getMois() + separator
		+ now.getJour();
	final boolean areTheSameDay = nowToTheDay.equals(timeOfLastLogToTheDay);

	// System.out.println (
	// "DefaultLogger/needsNewFile/now/"+now+"/timeOfLastLog/"+timeOfLastLog+"/timeOfLastLogToTheDay/"+timeOfLastLogToTheDay+"/nowToTheDay/"+nowToTheDay+"/areTheSameDay/"+areTheSameDay
	// );

	return !areTheSameDay;
    }

    /**
     * @return Returns the timeOfLastLog.
     */
    public DateHeure getTimeOfLastLog() {
	return timeOfLastLog;
    }

    /**
     * @param timeOfLastLog
     *            The timeOfLastLog to set.
     */
    public void setTimeOfLastLog(final DateHeure timeOfLastLog) {
	this.timeOfLastLog = timeOfLastLog;
    }

}
