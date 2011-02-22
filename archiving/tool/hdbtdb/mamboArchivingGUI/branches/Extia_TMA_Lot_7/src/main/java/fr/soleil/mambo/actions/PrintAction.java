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

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.DTPrinter;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;

public class PrintAction extends AbstractAction {

	private static final long serialVersionUID = -1331977605831085632L;

	private int type = NO_PRESET_TYPE;

	public static final int NO_PRESET_TYPE = -1;
	public static final int AC_TYPE = 0;
	public static final int VC_TYPE = 1;

	/**
	 * @param name
	 */
	public PrintAction(String name, Icon icon, int _type) {
		super(name, icon);
		this.putValue(Action.NAME, name);
		this.type = _type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// System.out.println ( "PrintAction/actionPerformed" );
		int printType = -1;

		if (this.type == NO_PRESET_TYPE) {
			String printLabel = Messages.getMessage("MENU_PRINT");
			String chooseLabel = Messages.getMessage("ACTION_PRINT_CHOOSE");
			String currentACLabel = Messages
					.getMessage("ACTION_PRINT_CURRENT_AC");
			String currentVCLabel = Messages
					.getMessage("ACTION_PRINT_CURRENT_VC");

			String[] possibleValues = { currentACLabel, currentVCLabel };
			String selectedValue = (String) JOptionPane.showInputDialog(null,
					chooseLabel, printLabel, JOptionPane.INFORMATION_MESSAGE,
					null, possibleValues, possibleValues[0]);

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
			printType = this.type;
		}

		String content = "";
		String title = "";
		String msg = "";

		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			switch (printType) {
			case AC_TYPE:
				ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
						.getSelectedArchivingConfiguration();
				if (selectedArchivingConfiguration != null) {
					content = selectedArchivingConfiguration.toString();
					title = Messages.getMessage("ACTION_PRINT_CURRENT_AC");
				} else {
					msg = Messages
							.getLogMessage("PRINT_ARCHIVING_CONFIGURATION_ACTION_NULL");
					logger.trace(ILogger.LEVEL_WARNING, msg);
					return;
				}

				msg = Messages
						.getLogMessage("PRINT_ARCHIVING_CONFIGURATION_ACTION_OK");
				break;

			case VC_TYPE:
				ViewConfiguration selectedViewConfiguration = ViewConfigurationBeanManager
						.getInstance().getSelectedConfiguration();
				if (selectedViewConfiguration != null) {
					content = selectedViewConfiguration.toString();
					title = Messages.getMessage("ACTION_PRINT_CURRENT_VC");
				} else {
					msg = Messages
							.getLogMessage("PRINT_VIEW_CONFIGURATION_ACTION_NULL");
					logger.trace(ILogger.LEVEL_WARNING, msg);
					return;
				}

				msg = Messages
						.getLogMessage("PRINT_VIEW_CONFIGURATION_ACTION_OK");
				break;
			}

			DTPrinter.printText(title, content);
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (Exception e) {
			switch (printType) {
			case AC_TYPE:
				msg = Messages
						.getLogMessage("PRINT_ARCHIVING_CONFIGURATION_ACTION_KO");
				break;

			case VC_TYPE:
				msg = Messages
						.getLogMessage("PRINT_VIEW_CONFIGURATION_ACTION_KO");
				break;
			}

			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}

	}
}
