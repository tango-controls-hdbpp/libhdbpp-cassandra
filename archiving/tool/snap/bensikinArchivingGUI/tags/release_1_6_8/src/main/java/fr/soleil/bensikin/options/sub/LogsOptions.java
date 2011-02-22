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

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsLogsTab;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * The logs options of the application. Contains: -the desired log level
 * 
 * @author CLAISSE
 */
public class LogsOptions extends ReadWriteOptionBook implements PushPullOptionBook {

    final static ILogger logger = GUILoggerFactory.getLogger();
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
	final OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
	final String logLevel_s = logsTab.getLogLevel();

	content.put(LEVEL, logLevel_s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    public void push() {

	String level_s = content.get(LEVEL);
	int lvl;
	if (level_s != null) {
	    lvl = logger.getTraceLevel(level_s);
	} else {
	    lvl = ILogger.LEVEL_DEBUG;
	    level_s = ILogger.DEBUG;
	}
	logger.setTraceLevel(lvl);

	final OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
	final OperatorsList comboBox = logsTab.getComboBox();
	comboBox.setSelectedItem(level_s);
    }

}
