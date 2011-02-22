//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DefaultLogger.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.5  2006/10/17 14:33:34  ounsy
// minor changes
//
// Revision 1.4  2006/09/27 09:44:18  ounsy
// modified the log() method
//
// Revision 1.3  2006/09/26 15:07:08  ounsy
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

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.AbstractLogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.MamboMainPanel;
import fr.soleil.mambo.containers.messages.MessagesPanel;
import fr.soleil.mambo.tools.GUIUtilities;

public class MamboLogger extends AbstractLogger {
    // private static MessagesArea messagesArea = null;

    public MamboLogger() {
	super();
	super.setTraceLevel(ILogger.LEVEL_DEBUG);

	final String pathToResources = Mambo.getPathToResources();

	try {
	    initDiaryWriter(pathToResources, "");
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
	    }
	} catch (final Exception e) {
	    // we catch the exception and only trace it so that we don't get a
	    // failure message on an action
	    // if the action result logs fail.
	    e.printStackTrace();
	}
    }

    /**
     * @param msg
     * @return 6 juil. 2005
     */
    private String getDecoratedDiaryEntry(final int level, final String msg) {
	final String criticity = levels.get(new Integer(level));
	final java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

	String ret = now.toString();
	ret += " ";
	ret += criticity;
	ret += " --> ";
	ret += msg;

	return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.logs.AbstractLogger#log(java.lang.String)
     */
    @Override
    protected void log(final String msg) {
	// MamboMainPanel panel =
	MamboMainPanel.getInstance();
	MessagesPanel.log(msg + System.getProperty("line.separator"));
    }

    /*
     * protected void log ( String msg ) { if ( messagesArea == null ) {
     * messagesArea = MessagesPanel.getInstance().getMessagesArea(); }
     * 
     * messagesArea.append( msg + System.getProperty( "line.separator" ) );
     * 
     * MamboMainPanel panel = MamboMainPanel.getInstance();
     * panel.scrollDownToLatestMessage(); }
     */

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

    @Override
    protected void changeDiaryFileIfNecessary() throws IOException {
	// TODO Auto-generated method stub

    }

}
