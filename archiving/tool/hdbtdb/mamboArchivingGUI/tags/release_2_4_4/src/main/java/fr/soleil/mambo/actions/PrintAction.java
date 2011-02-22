// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/PrintAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class PrintAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: PrintAction.java,v $
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.DTPrinter;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.tools.Messages;

public class PrintAction extends AbstractAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = -1331977605831085632L;

    private int type = NO_PRESET_TYPE;

    public static final int NO_PRESET_TYPE = -1;
    public static final int AC_TYPE = 0;
    public static final int VC_TYPE = 1;

    /**
     * @param name
     */
    public PrintAction(final String name, final Icon icon, final int _type) {
	super(name, icon);
	putValue(Action.NAME, name);
	type = _type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent arg0) {
	// System.out.println ( "PrintAction/actionPerformed" );
	int printType = -1;

	if (type == NO_PRESET_TYPE) {
	    final String printLabel = Messages.getMessage("MENU_PRINT");
	    final String chooseLabel = Messages.getMessage("ACTION_PRINT_CHOOSE");
	    final String currentACLabel = Messages.getMessage("ACTION_PRINT_CURRENT_AC");
	    final String currentVCLabel = Messages.getMessage("ACTION_PRINT_CURRENT_VC");

	    final String[] possibleValues = { currentACLabel, currentVCLabel };
	    final String selectedValue = (String) JOptionPane.showInputDialog(null, chooseLabel,
		    printLabel, JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
		    possibleValues[0]);

	    if (selectedValue == null) {
		return;
	    }

	    if (selectedValue.equals(possibleValues[0])) {
		printType = AC_TYPE;
	    } else if (selectedValue.equals(possibleValues[1])) {
		printType = VC_TYPE;
	    } else {
		throw new IllegalStateException();
	    }
	} else {
	    printType = type;
	}

	String content = "";
	String title = "";
	String msg = "";

	try {
	    switch (printType) {
	    case AC_TYPE:
		final ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
			.getSelectedArchivingConfiguration();
		if (selectedArchivingConfiguration != null) {
		    content = selectedArchivingConfiguration.toString();
		    title = Messages.getMessage("ACTION_PRINT_CURRENT_AC");
		} else {
		    msg = Messages.getLogMessage("PRINT_ARCHIVING_CONFIGURATION_ACTION_NULL");
		    logger.trace(ILogger.LEVEL_WARNING, msg);
		    return;
		}

		msg = Messages.getLogMessage("PRINT_ARCHIVING_CONFIGURATION_ACTION_OK");
		break;

	    case VC_TYPE:
		final ViewConfiguration selectedViewConfiguration = ViewConfigurationBeanManager
			.getInstance().getSelectedConfiguration();
		if (selectedViewConfiguration != null) {
		    content = selectedViewConfiguration.toString();
		    title = Messages.getMessage("ACTION_PRINT_CURRENT_VC");
		} else {
		    msg = Messages.getLogMessage("PRINT_VIEW_CONFIGURATION_ACTION_NULL");
		    logger.trace(ILogger.LEVEL_WARNING, msg);
		    return;
		}

		msg = Messages.getLogMessage("PRINT_VIEW_CONFIGURATION_ACTION_OK");
		break;
	    }

	    DTPrinter.printText(title, content);
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    switch (printType) {
	    case AC_TYPE:
		msg = Messages.getLogMessage("PRINT_ARCHIVING_CONFIGURATION_ACTION_KO");
		break;

	    case VC_TYPE:
		msg = Messages.getLogMessage("PRINT_VIEW_CONFIGURATION_ACTION_KO");
		break;
	    }

	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}

    }
}
