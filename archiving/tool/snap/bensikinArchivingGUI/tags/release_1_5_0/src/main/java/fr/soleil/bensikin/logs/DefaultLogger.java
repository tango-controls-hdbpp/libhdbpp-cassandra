//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/logs/DefaultLogger.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DefaultLogger.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: DefaultLogger.java,v $
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

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.components.messages.MessagesArea;
import fr.soleil.bensikin.containers.BensikinMainPanel;
import fr.soleil.bensikin.containers.messages.MessagesPanel;
import fr.soleil.bensikin.tools.GUIUtilities;


/**
 * The default implementation.
 *
 * @author CLAISSE
 */
public class DefaultLogger extends AbstractLogger
{
	private static MessagesArea messagesArea = null;

	/**
	 * Builds a DefaultLogger :
	 * <UL>
	 * <LI> By default the log level is set to LEVEL_DEBUG
	 * <LI> Initializes the diary writer
	 * </UL>
	 */
	public DefaultLogger()
	{
		super();
		super.setTraceLevel(ILogger.LEVEL_DEBUG);

		String pathToResources = Bensikin.getPathToResources();
		try
		{
			super.initDiaryWriter(pathToResources);
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see bensikin.logs.AbstractLogger#getDecoratedLog(java.lang.String, int)
	 */
	protected String getDecoratedLog(String msg , int level)
	{
		switch ( level )
		{
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

			default :

				break;
		}

		return msg;
	}

	/* (non-Javadoc)
	 * @see bensikin.logs.AbstractLogger#addToDiary(int, java.lang.Object)
	 */
	protected void addToDiary(int level , Object o) throws Exception
	{
		try
		{
			if ( o instanceof String )
			{
				String msg = ( String ) o;
				msg = this.getDecoratedDiaryEntry(level , msg);
				GUIUtilities.write2(this.writer , msg , true);
			}
			else if ( o instanceof Exception )
			{
				Exception e = ( Exception ) o;
				e.printStackTrace();

				GUIUtilities.write2(this.writer , "    " + e.toString() , true);
				StackTraceElement[] stack = e.getStackTrace();
				for ( int i = 0 ; i < stack.length ; i++ )
				{
					GUIUtilities.write2(this.writer , "        at " + stack[ i ].toString() , true);
				}
			}
			else
			{
				throw new IllegalArgumentException("DefaultLogger/addToDiary/other/" + o.getClass().toString() + "/");
			}
		}
		catch ( Exception e )
		{
			//we catch the exception and only trace it so that we don't get a failure message on an action
			//if the action result logs fail.
			e.printStackTrace();
		}
	}

	/**
	 * "Decorates" a diary message, that is adds informations/tags to it depending on its level.
	 *
	 * @param msg The message to decorate
	 * @return The decorated message
	 */
	private String getDecoratedDiaryEntry(int level , String msg)
	{
		String criticity = ( String ) levels.get(new Integer(level));
		java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

		String ret = now.toString();
		ret += " ";
		ret += criticity;
		ret += " --> ";
		ret += msg;

		return ret;
	}

	/* (non-Javadoc)
	 * @see bensikin.logs.AbstractLogger#log(java.lang.String)
	 */
	protected void log(String msg)
	{
		if ( messagesArea == null )
		{
			messagesArea = MessagesPanel.getInstance().getMessagesArea();
		}

		messagesArea.append(msg + GUIUtilities.CRLF);

		BensikinMainPanel panel = BensikinMainPanel.getInstance();
		panel.scrollDownToLatestMessage();
	}
}
