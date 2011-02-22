//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/Messages.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Messages.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.9 $
//
// $Log: Messages.java,v $
// Revision 1.9  2007/12/12 17:48:59  pierrejoseph
// no more mambo.properties in the classpath
//
// Revision 1.8  2007/11/20 16:03:27  pierrejoseph
// The exception has been suppressed when the mambo.properpies file has not been found
//
// Revision 1.7  2007/10/30 17:44:21  soleilarc
// Author: XP
// Mantis bug ID: 6961
//
// Revision 1.6  2007/10/22 15:41:12  soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment: In the method initResourceBundle, surround the call to getBundle for the data myProperties with a try/catch block, so that Mambo can run even if the file mambo.properties is missing.
//
// Revision 1.5  2007/10/18 12:41:36  soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment: Add a ResourceBundle data myProperties and load the properties from the file mambo.properties.
//
// Revision 1.3  2006/11/16 11:53:15  ounsy
// correction mantis 2580
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.tools;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
	private final static String resourcesFileName = "fr.soleil.mambo.resources.messages.resources";
	private final static String logsFileName = "fr.soleil.mambo.resources.messages.logs";
	private final static String buildFileName = "fr.soleil.mambo.resources.application";

	// private final static String propertiesFileName = "mambo";

	private static ResourceBundle myResources;
	private static ResourceBundle myLogResources;
	private static ResourceBundle myAppProperties;

	/**
	 * @param currentLocale
	 * @throws Exception
	 *             8 juil. 2005
	 */
	public static void initResourceBundle(Locale currentLocale)
			throws Exception {
		myResources = ResourceBundle
				.getBundle(resourcesFileName, currentLocale);
		myLogResources = ResourceBundle.getBundle(logsFileName, currentLocale);
		myAppProperties = ResourceBundle
				.getBundle(buildFileName, currentLocale);

		/*
		 * try { myProperties = ResourceBundle.getBundle(propertiesFileName); }
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * System.out.println(
		 * "the properties file has not been found, default behaviour will be applied"
		 * ); //e.printStackTrace(); }
		 */
	}

	/**
	 * @param string
	 * @return 29 juin 2005
	 */
	public static String getMessage(String key) {
		try {
			String ret = myResources.getString(key);
			return ret;
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * @param string
	 * @return 29 juin 2005
	 */
	public static String getLogMessage(String key) {
		try {
			String ret = myLogResources.getString(key);
			return ret;
		} catch (Exception e) {
			return key;
		}
	}

	public static String getAppMessage(String key) {
		try {
			String ret = myAppProperties.getString(key);
			return ret;
		} catch (Exception e) {
			return key;
		}
	}
	/*
	 * public static String getProperty ( String key ) { try { String ret =
	 * myProperties.getString( key ); return ret; } catch ( Exception e ) {
	 * return key; } }
	 */
}
