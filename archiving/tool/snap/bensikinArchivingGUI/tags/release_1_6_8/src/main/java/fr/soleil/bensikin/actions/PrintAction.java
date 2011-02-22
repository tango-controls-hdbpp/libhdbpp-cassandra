//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/PrintAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  PrintAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.6 $
//
//$Log: PrintAction.java,v $
//Revision 1.6  2007/08/23 15:28:48  ounsy
//Print Context as tree, table or text (Mantis bug 3913)
//
//Revision 1.5  2007/08/23 12:57:22  ounsy
//minor changes
//
//Revision 1.4  2007/08/22 14:47:24  ounsy
//new print system
//
//Revision 1.3  2007/08/21 15:13:18  ounsy
//Print Snapshot as table or text (Mantis bug 3913)
//
//Revision 1.2  2006/04/10 08:46:41  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/11/29 18:25:08  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * An action that prints the current context's or snapshot's, String
 * representation to a printer.
 * 
 * @author CLAISSE
 */
public class PrintAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();
    private int type = NO_PRESET_TYPE;

    /**
     * The object to print will be set later
     */
    public static final int NO_PRESET_TYPE = -1;

    /**
     * The object to print is the current context
     */
    public static final int CONTEXT_TYPE = 0;

    /**
     * The object to print is the current snapshot
     */
    public static final int SNAPSHOT_TYPE = 1;

    /**
     * Standard action constructor that sets the action's name and icon, plus
     * its type (none, context, or snapshot).
     * 
     * @param name
     *            The action name
     * @param icon
     *            The action icon
     * @param _type
     *            Must be either NO_PRESET_TYPE, CONTEXT_TYPE, or SNAPSHOT_TYPE
     */
    public PrintAction(final String name, final Icon icon, final int _type) {
	super(name, icon);
	putValue(Action.NAME, name);
	type = _type;
	if (_type != NO_PRESET_TYPE && _type != CONTEXT_TYPE && _type != SNAPSHOT_TYPE) {
	    throw new IllegalArgumentException();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent arg0) {
	// System.out.println("PrintAction/actionPerformed");
	int printType = -1;
	if (type == NO_PRESET_TYPE) {
	    final String printLabel = Messages.getMessage("MENU_PRINT");
	    final String chooseLabel = Messages.getMessage("ACTION_PRINT_CHOOSE");
	    final String currentACLabel = Messages.getMessage("ACTION_PRINT_CURRENT_CONTEXT");
	    final String currentVCLabel = Messages.getMessage("ACTION_PRINT_CURRENT_SNAPSHOT");
	    final String[] possibleValues = { currentACLabel, currentVCLabel };
	    final String selectedValue = (String) JOptionPane.showInputDialog(null, chooseLabel,
		    printLabel, JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
		    possibleValues[0]);
	    if (selectedValue == null) {
		return;
	    }
	    if (selectedValue.equals(possibleValues[0])) {
		printType = CONTEXT_TYPE;
	    } else if (selectedValue.equals(possibleValues[1])) {
		printType = SNAPSHOT_TYPE;
	    } else {
		throw new IllegalStateException();
	    }
	} else {
	    printType = type;
	}

	String msg = "";

	switch (printType) {
	case CONTEXT_TYPE:
	    // ----------------Building the context to be printed
	    final ContextActionPanel contextActionPanel = ContextActionPanel.getInstance();
	    // ----------------Building the context to be printed
	    if (contextActionPanel == null) {
		msg = Messages.getLogMessage("PRINT_CONTEXT_ACTION_NULL");
		logger.trace(ILogger.LEVEL_WARNING, msg);
		return;
	    } else {
		contextActionPanel.openPrintDialog();
		return;
	    }
	case SNAPSHOT_TYPE:
	    final SnapshotDetailTabbedPane snapshotDetailTabbedPane = SnapshotDetailTabbedPane
		    .getInstance();
	    final SnapshotDetailTabbedPaneContent selectedContent = snapshotDetailTabbedPane
		    .getSelectedSnapshotDetailTabbedPaneContent();
	    if (selectedContent == null) {
		msg = Messages.getLogMessage("PRINT_SNAPSHOT_ACTION_NULL");
		logger.trace(ILogger.LEVEL_WARNING, msg);
		return;
	    } else {
		selectedContent.openPrintDialog();
		return;
	    }
	}

    }
}
