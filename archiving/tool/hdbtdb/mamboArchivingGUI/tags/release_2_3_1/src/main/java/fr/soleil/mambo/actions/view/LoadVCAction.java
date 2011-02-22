// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/LoadVCAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class LoadVCAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: LoadVCAction.java,v $
// Revision 1.3 2006/05/05 14:39:43 ounsy
// added the same optimisation on loading VCs as for ACs
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.components.view.VCFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;

public class LoadVCAction extends AbstractAction {

    private static final long serialVersionUID = -7672739086989273080L;

    private boolean           isDefault;

    /**
     * @param name
     */
    public LoadVCAction(String name, boolean _isDefault) {
        super.putValue(Action.NAME, name);
        super.putValue(Action.SHORT_DESCRIPTION, name);

        this.isDefault = _isDefault;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory
                .getCurrentImpl();
        if (!this.isDefault) {
            // open file chooser
            JFileChooser chooser = new JFileChooser();
            VCFileFilter VCfilter = new VCFileFilter();
            chooser.addChoosableFileFilter(VCfilter);
            chooser.setCurrentDirectory(new File(manager
                    .getDefaultSaveLocation()));

            int returnVal = chooser.showOpenDialog(MamboFrame.getInstance());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                String path = f.getAbsolutePath();

                if (f != null) {
                    String extension = ConfigurationFileFilter.getExtension(f);
                    String expectedExtension = VCfilter.getExtension();

                    if (extension == null
                            || !extension.equalsIgnoreCase(expectedExtension)) {
                        path += ".";
                        path += expectedExtension;
                    }
                }
                manager.setNonDefaultSaveLocation(path);
            }
            else {
                return;
            }
        }
        else {
            manager.setNonDefaultSaveLocation(null);
        }

        ViewConfiguration existing = null;
        String path = manager.getNonDefaultSaveLocation();
        for (ViewConfiguration vc : OpenedVCComboBox.getInstance()
                .getVCElements()) {
            if ((path != null) && path.equals(vc.getPath())) {
                existing = vc;
                break;
            }
        }
        if (existing == null) {
            loadVCOnceThePathIsSet(false);
        }
        else {
            // If the VC is already open, we ask to duplicate it or not
            int ok = JOptionPane.showConfirmDialog(MamboFrame.getInstance(),
                    Messages.getMessage("DIALOGS_FILE_ALREADY_OPENED"),
                    Messages.getMessage("DIALOGS_FILE_ALREADY_OPENED_TITLE"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                loadVCOnceThePathIsSet(true);
            }
            else {
                ViewConfigurationBeanManager.getInstance()
                        .setSelectedConfiguration(existing);
            }
        }
    }

    /**
     * 
     */
    private void loadVCOnceThePathIsSet(boolean modified) {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory
                .getCurrentImpl();
        ILogger logger = LoggerFactory.getCurrentImpl();

        try {
            ViewConfiguration selectedViewConfiguration = manager
                    .loadViewConfiguration();
            selectedViewConfiguration.setModified(modified);
            if (modified) {
                selectedViewConfiguration.setPath(null);
            }
            else {
                selectedViewConfiguration.setPath(manager.getSaveLocation());
            }
            // We set the last update date as now, in order to duplicate the vc
            // if we open it twice
            if (selectedViewConfiguration.getData() != null) {
                selectedViewConfiguration.getData().setLastUpdateDate(
                        new Timestamp(System.currentTimeMillis()));
            }

            // selectedViewConfiguration.push();
            OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
            if (openedVCComboBox != null) {
                openedVCComboBox.selectElement(selectedViewConfiguration);
            }

            String msg = Messages
                    .getLogMessage("LOAD_VIEW_CONFIGURATION_ACTION_OK");
            logger.trace(ILogger.LEVEL_DEBUG, msg);
        }
        catch (FileNotFoundException fnfe) {
            String msg = Messages
                    .getLogMessage("LOAD_VIEW_CONFIGURATION_ACTION_WARNING");
            logger.trace(ILogger.LEVEL_WARNING, msg);
            logger.trace(ILogger.LEVEL_WARNING, fnfe);
            // e.printStackTrace();
            return;
        }
        catch (Exception e) {
            String msg = Messages
                    .getLogMessage("LOAD_VIEW_CONFIGURATION_ACTION_KO");
            logger.trace(ILogger.LEVEL_ERROR, msg);
            logger.trace(ILogger.LEVEL_ERROR, e);
            // e.printStackTrace();
            return;
        }
    }

}
