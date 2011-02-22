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

import fr.soleil.mambo.components.archiving.LimitedACStack;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;

public class SaveAllToDiskAction extends AbstractAction {

	private static final long serialVersionUID = -5027970339358737447L;

	private int type = BOTH_TYPE;

	public static final int BOTH_TYPE = -1;
	public static final int AC_TYPE = 0;
	public static final int VC_TYPE = 1;

	/**
	 * @param name
	 */
	public SaveAllToDiskAction(String name, ImageIcon icon, int _type) {
		super(name, icon);
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);

		this.type = _type;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (this.type != VC_TYPE)// save ACs
		{
			IArchivingConfigurationManager archivingManager = ArchivingConfigurationManagerFactory
					.getCurrentImpl();
			ILogger logger = LoggerFactory.getCurrentImpl();

			OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
			LimitedACStack openedACs = openedACComboBox.getACElements();
			if (openedACs != null) {
				boolean hasError = openedACs.save(archivingManager, logger);
				if (!hasError) {
					String msg = Messages
							.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_ACTION_OK");
					logger.trace(ILogger.LEVEL_DEBUG, msg);
				} else {
					String msg = Messages
							.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_ACTION_KO");
					logger.trace(ILogger.LEVEL_ERROR, msg);
				}
			}
		}
		if (this.type != AC_TYPE)// save VCs
		{
			IViewConfigurationManager viewManager = ViewConfigurationManagerFactory
					.getCurrentImpl();
			ILogger logger = LoggerFactory.getCurrentImpl();

			OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
			LimitedVCStack openedVCs = openedVCComboBox.getVCElements();
			if (openedVCs != null) {
				boolean hasError = openedVCs.save(viewManager, logger);
				if (!hasError) {
					String msg = Messages
							.getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_OK");
					logger.trace(ILogger.LEVEL_DEBUG, msg);
				} else {
					String msg = Messages
							.getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_KO");
					logger.trace(ILogger.LEVEL_ERROR, msg);
				}
			}
		}

	}

}
