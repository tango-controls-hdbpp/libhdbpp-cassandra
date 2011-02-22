// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/SaveAllToDiskAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SaveAllToDiskAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: SaveAllToDiskAction.java,v $
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
import javax.swing.ImageIcon;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.components.archiving.LimitedACStack;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.tools.Messages;

public class SaveAllToDiskAction extends AbstractAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = -5027970339358737447L;

    private int type = BOTH_TYPE;

    public static final int BOTH_TYPE = -1;
    public static final int AC_TYPE = 0;
    public static final int VC_TYPE = 1;

    /**
     * @param name
     */
    public SaveAllToDiskAction(final String name, final ImageIcon icon, final int _type) {
	super(name, icon);
	super.putValue(Action.NAME, name);
	super.putValue(Action.SHORT_DESCRIPTION, name);

	type = _type;
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
	if (type != VC_TYPE)// save ACs
	{
	    final IArchivingConfigurationManager archivingManager = ArchivingConfigurationManagerFactory
		    .getCurrentImpl();

	    final OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
	    final LimitedACStack openedACs = openedACComboBox.getACElements();
	    if (openedACs != null) {
		final boolean hasError = openedACs.save(archivingManager, logger);
		if (!hasError) {
		    final String msg = Messages
			    .getLogMessage("SAVE_ARCHIVING_CONFIGURATION_ACTION_OK");
		    logger.trace(ILogger.LEVEL_DEBUG, msg);
		} else {
		    final String msg = Messages
			    .getLogMessage("SAVE_ARCHIVING_CONFIGURATION_ACTION_KO");
		    logger.trace(ILogger.LEVEL_ERROR, msg);
		}
	    }
	}
	if (type != AC_TYPE)// save VCs
	{
	    final OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
	    final LimitedVCStack openedVCs = openedVCComboBox.getVCElements();
	    if (openedVCs != null) {
		final boolean hasError = openedVCs.save(logger);
		if (!hasError) {
		    final String msg = Messages.getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_OK");
		    logger.trace(ILogger.LEVEL_DEBUG, msg);
		} else {
		    final String msg = Messages.getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_KO");
		    logger.trace(ILogger.LEVEL_ERROR, msg);
		}
	    }
	}

    }

}
