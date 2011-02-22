//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/logs/AbstractLogger.java,v $
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
// Revision 1.4  2006/09/26 15:07:07  ounsy
// minor changes
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
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
package fr.soleil.mambo.logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

public abstract class AbstractLogger implements ILogger {
	protected int traceLevel;
	protected PrintWriter writer;
	protected Hashtable levels;
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
	 * @param msg
	 * @param level
	 * @return 8 juil. 2005
	 */
	protected abstract String getDecoratedLog(String msg, int level);

	/**
	 * @param level
	 * @param o
	 * @throws Exception
	 *             8 juil. 2005
	 */
	protected abstract void addToDiary(int level, Object o) throws Exception;

	/**
	 * @param msg
	 *            8 juil. 2005
	 */
	protected abstract void log(String msg);

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.logs.ILogger#close()
	 */
	public abstract void close();

}
