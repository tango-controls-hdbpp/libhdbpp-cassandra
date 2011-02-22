//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/logs/DefaultLogger.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DefaultLogger.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: DefaultLogger.java,v $
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

import java.io.IOException;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.MamboMainPanel;
import fr.soleil.mambo.containers.messages.MessagesPanel;
import fr.soleil.mambo.tools.GUIUtilities;


public class DefaultLogger extends AbstractLogger
{
    //private static MessagesArea messagesArea = null;

    public DefaultLogger ()
    {
        super();
        super.setTraceLevel( ILogger.LEVEL_DEBUG );

        String pathToResources = Mambo.getPathToResources();

        try
        {
            super.initDiaryWriter( pathToResources );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see bensikin.logs.AbstractLogger#getDecoratedLog(java.lang.String, int)
     */
    protected String getDecoratedLog ( String msg , int level )
    {
        switch ( level )
        {
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

            default :

                break;
        }

        return msg;
    }

    /* (non-Javadoc)
     * @see bensikin.logs.AbstractLogger#addToDiary(int, java.lang.Object)
     */
    protected void addToDiary ( int level , Object o ) throws Exception
    {
        try
        {
            if ( o instanceof String )
            {
                String msg = ( String ) o;
                msg = this.getDecoratedDiaryEntry( level , msg );
                GUIUtilities.write2( this.writer , msg , true );
            }
            else if ( o instanceof Exception )
            {
                Exception e = ( Exception ) o;
                e.printStackTrace();

                GUIUtilities.write2( this.writer , "    " + e.toString() , true );
                StackTraceElement[] stack = e.getStackTrace();
                for ( int i = 0 ; i < stack.length ; i++ )
                {
                    GUIUtilities.write2( this.writer , "        at " + stack[ i ].toString() , true );
                }
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
     * @param msg
     * @return 6 juil. 2005
     */
    private String getDecoratedDiaryEntry ( int level , String msg )
    {
        String criticity = ( String ) levels.get( new Integer( level ) );
        java.sql.Timestamp now = new java.sql.Timestamp( System.currentTimeMillis() );

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
    protected void log ( String msg )
    {
        //MamboMainPanel panel = 
        MamboMainPanel.getInstance();
        MessagesPanel.log ( msg + System.getProperty( "line.separator" ) );
    }
    /*protected void log ( String msg )
    {
        if ( messagesArea == null )
        {
            messagesArea = MessagesPanel.getInstance().getMessagesArea();
        }

        messagesArea.append( msg + System.getProperty( "line.separator" ) );

        MamboMainPanel panel = MamboMainPanel.getInstance();
        panel.scrollDownToLatestMessage();
    }*/

    /* (non-Javadoc)
     * @see bensikin.logs.ILogger#close()
     */
    public void close ()
    {
        if ( this.writer != null )
        {
            this.writer.close();
        }
    }

}
