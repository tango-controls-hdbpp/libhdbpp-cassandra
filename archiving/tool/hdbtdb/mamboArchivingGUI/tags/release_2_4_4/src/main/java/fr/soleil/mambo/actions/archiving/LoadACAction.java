//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/LoadACAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LoadACAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: LoadACAction.java,v $
// Revision 1.3  2006/04/10 08:58:20  ounsy
// optimisation on loading an AC
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
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
import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.archiving.ACFileFilter;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.tools.Messages;

public class LoadACAction extends AbstractAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private final boolean isDefault;

    /**
     * @param name
     */
    public LoadACAction(final String name, final boolean _isDefault) {
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
	final IArchivingConfigurationManager manager = ArchivingConfigurationManagerFactory
		.getCurrentImpl();
	if (!isDefault) {
	    // open file chooser
	    final JFileChooser chooser = new JFileChooser();
	    final ACFileFilter ACfilter = new ACFileFilter();
	    chooser.addChoosableFileFilter(ACfilter);
	    chooser.setCurrentDirectory(new File(manager.getDefaultSaveLocation()));

	    final int returnVal = chooser.showOpenDialog(MamboFrame.getInstance());
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		/*
		 * String path = chooser.getSelectedFile ().getPath ();
		 * manager.setSaveLocation ( path );
		 */
		final File f = chooser.getSelectedFile();
		String path = f.getAbsolutePath();

		if (f != null) {
		    final String extension = ConfigurationFileFilter.getExtension(f);
		    final String expectedExtension = ACfilter.getExtension();

		    if (extension == null || !extension.equalsIgnoreCase(expectedExtension)) {
			path += ".";
			path += expectedExtension;
		    }
		}
		manager.setNonDefaultSaveLocation(path);
	    } else {
		return;
	    }
	} else {
	    manager.setNonDefaultSaveLocation(null);
	}

	loadACOnceThePathIsSet();
    }

    /**
     * 
     */
    private void loadACOnceThePathIsSet() {
	final IArchivingConfigurationManager manager = ArchivingConfigurationManagerFactory
		.getCurrentImpl();

	try {
	    final ArchivingConfiguration selectedArchivingConfiguration = manager
		    .loadArchivingConfiguration();
	    selectedArchivingConfiguration.setModified(false);
	    selectedArchivingConfiguration.setPath(manager.getSaveLocation());

	    // selectedArchivingConfiguration.push();
	    final OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
	    if (openedACComboBox != null) {
		openedACComboBox.selectElement(selectedArchivingConfiguration);
	    }

	    final String msg = Messages.getLogMessage("LOAD_ARCHIVING_CONFIGURATION_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final FileNotFoundException fnfe) {
	    final String msg = Messages
		    .getLogMessage("LOAD_ARCHIVING_CONFIGURATION_ACTION_WARNING");
	    logger.trace(ILogger.LEVEL_WARNING, msg);
	    logger.trace(ILogger.LEVEL_WARNING, fnfe);
	    // e.printStackTrace();
	    return;
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("LOAD_ARCHIVING_CONFIGURATION_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    // e.printStackTrace();
	    return;
	}
    }
}
