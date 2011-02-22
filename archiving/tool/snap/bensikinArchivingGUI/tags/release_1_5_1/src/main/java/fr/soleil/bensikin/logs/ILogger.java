//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/logs/ILogger.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ILogger.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: ILogger.java,v $
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

import java.io.IOException;

/**
 * Defines everything that has to with logging in Bensikin.
 * Logs are divided in two categories:
 * <UL>
 * <LI> Messages that are logged into the application diary for future debugging and control
 * <LI> Confirmation and error messages displayed to the user upon the realisation of an action
 * </UL>
 * All messages have a criticity level; the most critic messages have the lowest criticity index.
 * A message will be logged if and only if its criticity is under the criticity threshold of the logger.
 *
 * @author CLAISSE
 */
public interface ILogger
{
	/**
	 * Log only critic messages
	 */
	public static final int LEVEL_CRITIC = 1;
	/**
	 * Log critic and error messages
	 */
	public static final int LEVEL_ERROR = 3;
	/**
	 * Log critic, error, and warning messages
	 */
	public static final int LEVEL_WARNING = 5;
	/**
	 * Log critic, error, warning, and info messages
	 */
	public static final int LEVEL_INFO = 7;
	/**
	 * Log all messages
	 */
	public static final int LEVEL_DEBUG = 9;

	/**
	 * Prefix for critic messages
	 */
	public static final String CRITIC = "CRITIC";
	/**
	 * Prefix for error messages
	 */
	public static final String ERROR = "ERROR";
	/**
	 * Prefix for warning messages
	 */
	public static final String WARNING = "WARNING";
	/**
	 * Prefix for info messages
	 */
	public static final String INFO = "INFO";
	/**
	 * Prefix for debug messages
	 */
	public static final String DEBUG = "DEBUG";

	/**
	 * Returns the current criticity threshold, under which a message is logged.
	 *
	 * @return The current criticity threshold, under which a message is logged
	 */
	public int getTraceLevel();

	/**
	 * Sets the new criticity threshold, under which a message will be logged
	 *
	 * @param level The new criticity threshold, under which a message will be logged
	 */
	public void setTraceLevel(int level);

	/**
	 * Logs a message, if its criticity is under the current criticity threshold; otherwise does nothing.
	 *
	 * @param level The message's criticity
	 * @param o     The Object to log (can be Exception, String, ...)
	 */
	public void trace(int level , Object o);

	/**
	 * Inits the resource used for the daily application diary.
	 *
	 * @param path The path where the diary will be created
	 * @throws IOException
	 */
	public void initDiaryWriter(String path) throws IOException;

	/**
	 * Closes the resource used for the daily application diary.
	 */
	public void close();

	/**
	 * Returns the criticity level associated with a given log prefix.
	 *
	 * @param level_s The log prefix
	 * @return The criticity level associated with a given log prefix
	 */
	public int getTraceLevel(String level_s);
}
