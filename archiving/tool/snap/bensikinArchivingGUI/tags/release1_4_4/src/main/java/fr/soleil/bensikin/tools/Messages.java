//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/tools/Messages.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Messages.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: Messages.java,v $
// Revision 1.2  2006/11/16 11:53:15  ounsy
// correction mantis 2580
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:32  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.tools;

import java.util.Locale;
import java.util.ResourceBundle;

import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;


/**
 * The class in charge of internationalization.
 * Uses 2 resource bundles, one for the application's labels, and a smaller one for all log messages and error
 * messages.
 *
 * @author CLAISSE
 */
public class Messages
{
	private static ResourceBundle myResources;
	private static ResourceBundle myLogResources;
	private static ResourceBundle myAppProperties;

	/**
	 * Initializes the resource bundles used for labels and log messages
	 *
	 * @param currentLocale The locale used by the application
	 * @throws Exception
	 */
	public static void initResourceBundle(Locale currentLocale) throws Exception
	{
		ILogger logger = LoggerFactory.getCurrentImpl();
		try
		{
			myResources = ResourceBundle.getBundle("fr.soleil.bensikin.resources.messages.resources" , currentLocale);
			myLogResources = ResourceBundle.getBundle("fr.soleil.bensikin.resources.messages.logs" , currentLocale);
			myAppProperties = ResourceBundle.getBundle("fr.soleil.bensikin.resources.application" , currentLocale);
		}
		catch ( Exception e )
		{
			//of course if the resources loading failed we can't look up the failure message in the ressources..
			String msg = "Failed to load resources";
			logger.trace(ILogger.LEVEL_CRITIC , msg);
			logger.trace(ILogger.LEVEL_CRITIC , e);

			throw e;
		}
	}

	/**
	 * @param key The message key
	 * @return The label for this key
	 */
	public static String getMessage(String key)
	{
        try
        {
            String ret = myResources.getString(key);
            return ret;
        }
        catch ( Exception e )
        {
            return key;
        }
	}

	/**
	 * @param key The log message key
	 * @return The log message for this key
	 */
	public static String getLogMessage(String key)
	{
		try
		{
			String ret = myLogResources.getString(key);
			return ret;
		}
		catch ( Exception e )
		{
			return key;
		}
	}
	
	  public static String getAppMessage ( String key )
	    {
	        try
	        {
	            String ret = myAppProperties.getString( key );
	            return ret;
	        }
	        catch ( Exception e )
	        {
	            return key;
	        }
	    }
}
