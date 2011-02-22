//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/LogsOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LogsOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: LogsOptions.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/26 07:52:25  chinkumo
// Miscellaneous changes...
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
package fr.soleil.mambo.options.sub;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.components.OperatorsList;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsLogsTab;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;

public class LogsOptions extends ReadWriteOptionBook implements PushPullOptionBook {

    final static ILogger logger = GUILoggerFactory.getLogger();

    public static final String LEVEL = "LEVEL";

    public static final String KEY = "logs"; // for XML save and load

    /**
     * 
     */
    public LogsOptions() {
	super(KEY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog() {
	final OptionsLogsTab logsTab = OptionsLogsTab.getInstance();
	final String logLevel_s = logsTab.getLogLevel();

	// GUIUtilities.trace ( 9 ,
	// "SaveOptions/fillFromOptionsDialog/selectedActionCommand/"+selectedActionCommand+"/"
	// );

	content.put(LEVEL, logLevel_s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    public void push() {

	String level_s = (String) content.get(LEVEL);
	// System.out.println ( "LogsOptions/push/level_s/"+level_s+"/" );
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
