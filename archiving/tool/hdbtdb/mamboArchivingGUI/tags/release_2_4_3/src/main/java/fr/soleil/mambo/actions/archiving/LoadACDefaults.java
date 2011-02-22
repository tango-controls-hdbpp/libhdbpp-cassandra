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

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.components.archiving.ACDefaultsFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.manager.ACDefaultsManagerFactory;
import fr.soleil.mambo.options.manager.IACDefaultsManager;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.Messages;

public class LoadACDefaults extends AbstractAction {

    final static ILogger logger = GUILoggerFactory.getLogger();
    private final boolean isDefault;

    /**
     * @param name
     */
    public LoadACDefaults(final String name, final boolean _isDefault) {
	super.putValue(Action.NAME, name);
	super.putValue(Action.SHORT_DESCRIPTION, name);

	isDefault = _isDefault;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent actionEvent) {
	// System.out.println ("LoadACAction");
	final IACDefaultsManager manager = ACDefaultsManagerFactory.getCurrentImpl();
	manager.setDefault(isDefault);

	if (!isDefault) {
	    // open file chooser
	    final JFileChooser chooser = new JFileChooser();
	    final ACDefaultsFileFilter filter = new ACDefaultsFileFilter();
	    chooser.addChoosableFileFilter(filter);
	    chooser.setCurrentDirectory(new File(manager.getDefaultSaveLocation()));

	    final int returnVal = chooser.showOpenDialog(MamboFrame.getInstance());
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		final File f = chooser.getSelectedFile();
		String path = f.getAbsolutePath();

		if (f != null) {
		    final String extension = ACDefaultsFileFilter.getExtension(f);
		    final String expectedExtension = filter.getExtension();

		    if (extension == null || !extension.equalsIgnoreCase(expectedExtension)) {
			path += ".";
			path += expectedExtension;
		    }
		}
		manager.setSaveLocation(path);
	    } else {
		return;
	    }
	} else {
	    manager.setSaveLocation(null);
	}

	try {
	    final ACOptions selectedACOptions = manager.loadACDefaults();

	    final Options options = Options.getInstance();
	    options.setAcOptions(selectedACOptions);
	    Options.setOptionsInstance(options);
	    selectedACOptions.push();

	    final String msg = Messages
		    .getLogMessage("LOAD_ARCHIVING_CONFIGURATION_DEFAULTS_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final FileNotFoundException fnfe) {
	    final String msg = Messages
		    .getLogMessage("LOAD_ARCHIVING_CONFIGURATION_DEFAULTS_ACTION_WARNING");
	    logger.trace(ILogger.LEVEL_WARNING, msg);
	    logger.trace(ILogger.LEVEL_WARNING, fnfe);
	    return;
	} catch (final Exception e) {
	    final String msg = Messages
		    .getLogMessage("LOAD_ARCHIVING_CONFIGURATION_DEFAULTS_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}
    }
}
