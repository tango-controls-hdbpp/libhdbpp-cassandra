// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/logs/DefaultLogger.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DefaultLogger.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: DefaultLogger.java,v $
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
package fr.soleil.bensikin.logs;

import java.io.IOException;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.containers.messages.MessagesPanel;
import fr.soleil.bensikin.tools.GUIUtilities;

/**
 * The default implementation.
 * 
 * @author CLAISSE
 */
public class DefaultLogger extends AbstractLogger {

    // private final ArrayList<String> messageBuffer;

    /**
     * Builds a DefaultLogger :
     * <UL>
     * <LI>By default the log level is set to LEVEL_DEBUG
     * <LI>Initializes the diary writer
     * </UL>
     */
    public DefaultLogger() {
	super();
	super.setTraceLevel(ILogger.LEVEL_DEBUG);
	// messageBuffer = new ArrayList<String>();

	final String pathToResources = Bensikin.getPathToResources();
	try {
	    super.initDiaryWriter(pathToResources);
	} catch (final IOException e) {
	    e.printStackTrace();
	}
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
	    } else if (o instanceof Exception) {
		final Exception e = (Exception) o;
		e.printStackTrace();

		GUIUtilities.write2(writer, "    " + e.toString(), true);
		final StackTraceElement[] stack = e.getStackTrace();
		for (final StackTraceElement element : stack) {
		    GUIUtilities.write2(writer, "        at " + element.toString(), true);
		}
	    } else {
		throw new IllegalArgumentException("DefaultLogger/addToDiary/other/"
			+ o.getClass().toString() + "/");
	    }
	} catch (final Exception e) {
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
	final String criticity = (String) levels.get(new Integer(level));
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
	messagesPanel.getMessagesArea().addTrace(msg);
	// if (messagesPanel == null) {
	// synchronized (messageBuffer) {
	// messageBuffer.add(msg);
	// }
	// } else {
	// synchronized (messageBuffer) {
	// final int size = messageBuffer.size();
	// if (size > 0) {
	// for (int i = 0; i < size; i++) {
	// // if (messageBuffer.get(i).startsWith("ERROR")) {
	// // messagesPanel.getMessagesArea().setFont(
	// // new Font("Serif", Font.BOLD, 16));
	// // } else {
	// // messagesPanel.getMessagesArea().setFont(
	// // new Font("Serif", Font.PLAIN, 12));
	// // }
	// messagesPanel.getMessagesArea().addTrace(messageBuffer.get(i));
	// // messagesPanel.getMessagesArea().append(messageBuffer.get(i));
	// // messagesPanel.getMessagesArea().append(GUIUtilities.CRLF);
	//
	// }
	// messageBuffer.clear();
	// }
	// }
	// messagesPanel.getMessagesArea().addTrace(msg);
	// // messagesPanel.getMessagesArea().append(GUIUtilities.CRLF);
	// // BensikinMainPanel.getInstance().scrollDownToLatestMessage();
	// }
	// messagesPanel = null;
    }
}
