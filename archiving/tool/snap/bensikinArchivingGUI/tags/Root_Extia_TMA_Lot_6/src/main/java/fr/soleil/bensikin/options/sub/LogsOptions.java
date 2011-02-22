//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/LogsOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LogsOptions.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: LogsOptions.java,v $
// Revision 1.4  2005/11/29 18:25:13  chinkumo
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
package fr.soleil.bensikin.options.sub;

import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsLogsTab;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;

/**
 * The logs options of the application. Contains: -the desired log level
 * 
 * @author CLAISSE
 */
public class LogsOptions extends ReadWriteOptionBook implements
		PushPullOptionBook {
	/**
	 * The log level property name
	 */
	public static final String LEVEL = "LEVEL";

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "logs";

	/**
	 * Default constructor
	 */
	public LogsOptions() {
		super(XML_TAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog() {
		OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
		String logLevel_s = logsTab.getLogLevel();

		this.content.put(LEVEL, logLevel_s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push() {
		ILogger logger = LoggerFactory.getCurrentImpl();

		String level_s = (String) this.content.get(LEVEL);
		int lvl;
		if (level_s != null) {
			lvl = logger.getTraceLevel(level_s);
		} else {
			lvl = ILogger.LEVEL_DEBUG;
			level_s = ILogger.DEBUG;
		}
		logger.setTraceLevel(lvl);

		OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
		OperatorsList comboBox = logsTab.getComboBox();
		comboBox.setSelectedItem(level_s);
	}

}
