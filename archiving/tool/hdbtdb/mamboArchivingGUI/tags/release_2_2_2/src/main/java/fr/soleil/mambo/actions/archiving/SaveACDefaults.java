//+======================================================================
//$Source$
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  LoadACAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author$
//
//$Revision$
//
//$Log$
//Revision 1.1  2006/10/05 08:57:17  ounsy
//LoadACDefaults and SaveACDefaults moved to mambo.actions.archiving instead of mambo.actions
//
//Revision 1.2  2006/10/03 14:51:36  ounsy
//ACDefaultsFileFilter moved in mambo.components.archiving instead of mambo.components
//
//Revision 1.1  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import fr.soleil.mambo.components.archiving.ACDefaultsFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.options.manager.ACDefaultsManagerFactory;
import fr.soleil.mambo.options.manager.IACDefaultsManager;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.Messages;

public class SaveACDefaults extends AbstractAction {

	/**
	 * @param name
	 */
	public SaveACDefaults(String name) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		// System.out.println ("LoadACAction");
		IACDefaultsManager manager = ACDefaultsManagerFactory.getCurrentImpl();

		// open file chooser
		JFileChooser chooser = new JFileChooser();
		ACDefaultsFileFilter filter = new ACDefaultsFileFilter();
		chooser.addChoosableFileFilter(filter);
		chooser.setCurrentDirectory(new File(manager.getDefaultSaveLocation()));

		int returnVal = chooser.showSaveDialog(MamboFrame.getInstance());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String path = f.getAbsolutePath();

			if (f != null) {
				String extension = ACDefaultsFileFilter.getExtension(f);
				String expectedExtension = filter.getExtension();

				if (extension == null
						|| !extension.equalsIgnoreCase(expectedExtension)) {
					path += ".";
					path += expectedExtension;
				}
			}
			manager.setSaveLocation(path);
		} else {
			return;
		}

		ILogger logger = LoggerFactory.getCurrentImpl();

		try {
			ACOptions selectedACOptions = new ACOptions();
			selectedACOptions.fillFromOptionsDialog();
			manager.saveACDefaults(selectedACOptions);

			String msg = Messages
					.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_DEFAULTS_ACTION_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (FileNotFoundException fnfe) {
			String msg = Messages
					.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_DEFAULTS_ACTION_WARNING");
			logger.trace(ILogger.LEVEL_WARNING, msg);
			logger.trace(ILogger.LEVEL_WARNING, fnfe);
			return;
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_DEFAULTS_ACTION_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}
}
