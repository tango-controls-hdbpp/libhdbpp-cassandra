// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/logs/DefaultLogger.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DefaultLogger.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.1  2010/11/15 08:21:00  abeilleg
// *** empty log message ***
//
// Revision 1.1  2010/11/10 12:54:00  abeilleg
// first import
//
// Revision 1.1  2010/11/10 07:52:09  abeilleg
// use common archiving api logging
//
// Revision 1.3 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:41 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.archiving.gui.logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import fr.soleil.archiving.gui.messages.MessagesPanel;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.AbstractLogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * The default implementation.
 * 
 * @author CLAISSE
 */
public class GUILogger extends AbstractLogger {

    /**
     * Builds a DefaultLogger :
     * <UL>
     * <LI>By default the log level is set to LEVEL_DEBUG
     * <LI>Initializes the diary writer
     * </UL>
     */
    public GUILogger() {
	super();
	super.setTraceLevel(ILogger.LEVEL_DEBUG);
	try {
	    initDiaryWriter(System.getProperty(GUIUtilities.WORKING_DIR), "");
	} catch (final IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void initDiaryWriter(final String path, final String archiver) throws IOException {

	String absp = path + "/logs";
	final File f = new File(absp);
	if (!f.canWrite()) {
	    f.mkdir();
	}

	final java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
	final String date = today.toString();
	absp += "/diary_" + date + ".log";

	writer = new PrintWriter(new FileWriter(absp, true));
    }

    @Override
    protected String getDecoratedLog(String msg, final int level) {
	switch (level) {
	case ILogger.LEVEL_CRITIC:
	    msg = ILogger.CRITIC + ": " + msg;
	    break;

	case ILogger.LEVEL_ERROR:
	    msg = ILogger.ERROR + ": " + msg;
	    break;

	case ILogger.LEVEL_WARNING:
	    msg = ILogger.WARNING + ": " + msg;
	    break;

	case ILogger.LEVEL_INFO:
	    msg = ILogger.INFO + ": " + msg;
	    break;

	case ILogger.LEVEL_DEBUG:
	    msg = ILogger.DEBUG + ": " + msg;
	    break;

	default:
	    break;
	}
	return msg;
    }

    @Override
    protected void addToDiary(final int level, final Object o) throws Exception {
	try {
	    if (o instanceof String) {
		String msg = (String) o;
		msg = getDecoratedDiaryEntry(level, msg);
		GUIUtilities.write2(writer, msg, true);
		System.out.println(msg);
	    } else if (o instanceof Exception) {
		final Exception e = (Exception) o;
		e.printStackTrace();
		GUIUtilities.write2(writer, "    " + e.toString(), true);
		log(ILogger.ERROR + " - " + e.toString());
		System.out.println(e.toString());
		final StackTraceElement[] stack = e.getStackTrace();
		for (final StackTraceElement element : stack) {
		    GUIUtilities.write2(writer, "        at " + element.toString(), true);
		    // log(element.toString());
		    // System.out.println(element.toString());
		}
	    } else {
		throw new IllegalArgumentException("DefaultLogger/addToDiary/other/"
			+ o.getClass().toString() + "/");
	    }
	} catch (final Exception e) {
	    System.err.println("error adding to diary");
	    // we catch the exception and only trace it so that we don't get a
	    // failure message on an action
	    // if the action result logs fail.
	    e.printStackTrace();
	}
    }

    /**
     * "Decorates" a diary message, that is adds informations/tags to it
     * depending on its level.
     * 
     * @param msg
     *            The message to decorate
     * @return The decorated message
     */
    private String getDecoratedDiaryEntry(final int level, final String msg) {
	final String criticity = levels.get(level);
	final java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

	String ret = now.toString();
	ret += " ";
	ret += criticity;
	ret += " --> ";
	ret += msg;

	return ret;
    }

    @Override
    protected void log(final String msg) {
	final MessagesPanel messagesPanel = MessagesPanel.getInstance();
	final String[] lines = msg.split("\n");
	for (final String line : lines) {
	    messagesPanel.getMessagesArea().addTrace(line);
	}

    }

    @Override
    public void close() {
	if (writer != null) {
	    writer.close();
	}
    }

    @Override
    protected void changeDiaryFileIfNecessary() throws IOException {
	// TODO Auto-generated method stub

    }
}
