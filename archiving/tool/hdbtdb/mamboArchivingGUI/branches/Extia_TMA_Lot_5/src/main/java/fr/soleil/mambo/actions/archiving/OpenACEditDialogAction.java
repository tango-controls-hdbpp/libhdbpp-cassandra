// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/OpenACEditDialogAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenACEditDialogAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.14 $
//
// $Log: OpenACEditDialogAction.java,v $
// Revision 1.14 2007/08/24 12:51:43 ounsy
// WaitingDialog should allways close now
//
// Revision 1.13 2006/12/06 12:34:56 ounsy
// better error and waiting dialog management
//
// Revision 1.12 2006/10/11 15:00:27 ounsy
// minor changes
//
// Revision 1.11 2006/08/29 14:04:07 ounsy
// waiting dialog becomes static
//
// Revision 1.10 2006/07/18 10:21:56 ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.9 2006/05/19 13:40:08 ounsy
// waiting dialog added
//
// Revision 1.8 2006/04/05 13:41:27 ounsy
// small bug correction : does not reduce max stack in case of cancel after new
//
// Revision 1.7 2006/03/20 10:33:56 ounsy
// if the user closes the dialog with the little cross,
// the AC edit dialog isn't opened
//
// Revision 1.6 2006/02/24 12:14:57 ounsy
// modified so that a isHistoric choice popup is used
//
// Revision 1.5 2006/01/24 12:50:31 ounsy
// minor changes
//
// Revision 1.4 2006/01/24 12:26:14 ounsy
// Bug of the new AC replacing the former selected AC corrected
//
// Revision 1.3 2005/12/15 10:44:12 ounsy
// minor changes
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
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditChooseDialog;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;

public class OpenACEditDialogAction extends AbstractAction {

    private static final long serialVersionUID = 6912982657890240618L;
    private boolean           isNew;
    private boolean           isAlternateSelectionMode;
    private boolean           isHistoric;

    /**
     * @param name
     */
    public OpenACEditDialogAction(String name, boolean _isNew,
            boolean _isAlternateSelectionMode) {
        super.putValue(Action.NAME, name);
        super.putValue(Action.SHORT_DESCRIPTION, name);

        this.isNew = _isNew;
        this.isAlternateSelectionMode = _isAlternateSelectionMode;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.gc();
        ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
                .getSelectedArchivingConfiguration();
        boolean needsNewAc = this.isNew
                || selectedArchivingConfiguration == null;
        boolean needsStackCleaning = false;

        WaitingDialog.openInstance();

        // 1st, test whether it is an historical AC
        if (needsNewAc) {
            ACEditChooseDialog.getInstance().setVisible(true);
            if (!ACEditChooseDialog.getInstance().isValidated()) {
                WaitingDialog.closeInstance();
                return;
            }
            this.isHistoric = ACEditChooseDialog.getInstance().isHistoric();
        }
        else {
            this.isHistoric = selectedArchivingConfiguration.isHistoric();
        }
        ACEditDialog dialog = ACEditDialog.getInstance(
                this.isAlternateSelectionMode, this.isHistoric);
        dialog.setNewAC(needsNewAc);

        try {
            if (needsNewAc) {
                needsStackCleaning = (OpenedACComboBox.getInstance()
                        .getStackSize() == OpenedACComboBox.getInstance()
                        .getMaxSize());
                if (needsStackCleaning) {
                    OpenedACComboBox.getInstance().setMaxSize(
                            OpenedACComboBox.getInstance().getMaxSize() + 1);
                }

                ArchivingConfiguration newAC = new ArchivingConfiguration();
                newAC.setHistoric(this.isHistoric);
                newAC.push();

                // AttributesPropertiesPanel.resetInstance ();
                // AttributesPropertiesTab.resetInstance ();
                // ACEditDialog.resetInstance ();

                AttributesPropertiesPanel panel = AttributesPropertiesPanel
                        .getInstance(this.isHistoric);
                panel.doUnselectAllModes();

                Options options = Options.getInstance();
                ACOptions ACoptions = options.getAcOptions();
                ACoptions.applyDefaults();
            }
            else {
                selectedArchivingConfiguration.pushLight();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }

        dialog.resetTabbedPane();
        WaitingDialog.closeInstance();
        dialog.setVisible(true);
        if (needsStackCleaning) {
            OpenedACComboBox.getInstance().setMaxSize(
                    OpenedACComboBox.getInstance().getMaxSize() - 1);
        }
        System.gc();
    }

    /**
     * @return Returns the isAlternateSelectionMode.
     */
    public boolean isAlternateSelectionMode() {
        return isAlternateSelectionMode;
    }

    /**
     * @param isAlternateSelectionMode
     *            The isAlternateSelectionMode to set.
     */
    public void setAlternateSelectionMode(boolean isAlternateSelectionMode) {
        this.isAlternateSelectionMode = isAlternateSelectionMode;
    }
}
