//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/logs/AbstractLogger.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AbstractLogger.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: AbstractLogger.java,v $
// Revision 1.4  2006/06/28 12:51:51  ounsy
// minor changes
//
// Revision 1.3  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 * Partially implements ILogger:
 * <UL>
 * <LI>The trace level setting and getting
 * <LI>The application diary initialization (uses a daily diary like tomcat) and
 * closing
 * <LI>The repartition of a message between the diary and the GUI messages
 * display area
 * </UL>
 * but leaves the detail implementation of diary/GUI logging up to its
 * daughters.
 * 
 * @author CLAISSE
 */
public abstract class AbstractLogger implements ILogger {
	/**
	 * The criticity threshold
	 */
	protected int traceLevel;

	/**
	 * The writer used for the application's diary
	 */
	protected PrintWriter writer;

	/**
	 * The Hashtable mapping criticity levels-->criticity labels
	 */
	protected Hashtable levels;

	/**
	 * The Hashtable mapping criticity labels-->criticity levels
	 */
	protected Hashtable reverseLevels;

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#getTraceLevel()
	 */
	public int getTraceLevel() {
		return traceLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#setTraceLevel(int)
	 */
	public void setTraceLevel(int level) {
		System.out.println("Trace level set to " + level + "/");
		this.traceLevel = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#initDiaryWriter(java.lang.String)
	 */
	public void initDiaryWriter(String path) throws IOException {
		levels = new Hashtable(5);
		levels.put(new Integer(LEVEL_CRITIC), CRITIC);
		levels.put(new Integer(LEVEL_ERROR), ERROR);
		levels.put(new Integer(LEVEL_WARNING), WARNING);
		levels.put(new Integer(LEVEL_INFO), INFO);
		levels.put(new Integer(LEVEL_DEBUG), DEBUG);

		reverseLevels = new Hashtable(5);
		reverseLevels.put(CRITIC, new Integer(LEVEL_CRITIC));
		reverseLevels.put(ERROR, new Integer(LEVEL_ERROR));
		reverseLevels.put(WARNING, new Integer(LEVEL_WARNING));
		reverseLevels.put(INFO, new Integer(LEVEL_INFO));
		reverseLevels.put(DEBUG, new Integer(LEVEL_DEBUG));

		String absp = path + "/logs";
		File f = new File(absp);
		if (!f.canWrite()) {
			// boolean b =
			f.mkdir();
		}

		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
		String date = today.toString();
		absp += "/diary_" + date + ".log";

		writer = new PrintWriter(new FileWriter(absp, true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#getTraceLevel(java.lang.String)
	 */
	public int getTraceLevel(String level_s) {
		Integer lev = (Integer) reverseLevels.get(level_s);
		if (lev == null) {
			return LEVEL_DEBUG;
		}

		try {
			int ret = lev.intValue();
			return ret;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return LEVEL_DEBUG;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#trace(int, java.lang.Object)
	 */
	public void trace(int level, Object o) {
		if (level <= this.traceLevel) {
			try {
				this.addToDiary(level, o);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (o instanceof String) {
				String msg = (String) o;
				msg = this.getDecoratedLog(msg, level);
				this.log(msg);
			}
		}
	}

	/**
	 * "Decorates" a GUI log message, that is adds informations/tags to it
	 * depending on its level.
	 * 
	 * @param msg
	 *            The message to log
	 * @param level
	 *            The log level
	 * @return The decorated message
	 */
	protected abstract String getDecoratedLog(String msg, int level);

	/**
	 * Logs to the daily diary.
	 * 
	 * @param level
	 *            The criticity of the message to log
	 * @param o
	 *            The object to log
	 * @throws Exception
	 */
	protected abstract void addToDiary(int level, Object o) throws Exception;

	/**
	 * Logs a message to the GUI messages display area.
	 * 
	 * @param msg
	 *            The message to log
	 */
	protected abstract void log(String msg);

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#close()
	 */
	public void close() {
		if (this.writer != null) {
			this.writer.close();
		}
	}

}
