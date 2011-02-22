// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/CancelAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class CancelAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: CancelAction.java,v $
// Revision 1.7 2006/05/29 15:44:47 ounsy
// minor changes
//
// Revision 1.6 2006/04/05 13:40:03 ounsy
// optimization
//
// Revision 1.5 2006/01/24 12:50:05 ounsy
// Bug of the new VC replacing the former selected VC corrected
//
// Revision 1.4 2006/01/24 12:25:54 ounsy
// Bug of the new AC replacing the former selected AC corrected
//
// Revision 1.3 2005/12/15 10:38:17 ounsy
// minor changes
//
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
import javax.swing.JDialog;

import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public class CancelAction extends AbstractAction {

    private static final long serialVersionUID = 7970013079534371577L;
    private JDialog           toClose;

    /**
     * @param name
     */
    public CancelAction(String name, JDialog _toClose) {
        super(name);
        this.putValue(Action.NAME, name);
        this.toClose = _toClose;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CancelAction.performCancel(this.toClose);
    }

    public static void performCancel(JDialog _toClose) {
        // nothing to do in case of VCEditDialog : everything is kept clear by
        // ViewConfigurationBean

        // if (_toClose instanceof VCEditDialog) {
        // VCAttributesRecapTree recapTree = VCAttributesRecapTree
        // .getInstance();
        // VCAttributesPropertiesTree propTree = VCAttributesPropertiesTree
        // .getInstance();
        //
        // ViewConfiguration selectedVC = ViewConfiguration
        // .getSelectedViewConfiguration();
        // OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        //
        // if (selectedVC == null) {
        // selectedVC = new ViewConfiguration();
        // }
        //
        // selectedVC.push();
        // if (recapTree != null) recapTree.openExpandedPath();
        // if (propTree != null) propTree.openExpandedPath();
        //
        // if (((VCEditDialog) _toClose).isNewVC()) {
        // recapTree.expandAll(true);
        // propTree.expandAll(true);
        // }
        //
        // }
        if (_toClose instanceof ACEditDialog) {
            ArchivingConfiguration selectedAC = ArchivingConfiguration
                    .getSelectedArchivingConfiguration();
            if (selectedAC == null) {
                selectedAC = new ArchivingConfiguration();
            }

            selectedAC.pushLight();
            if (((ACEditDialog) _toClose).isNewAC()) {
                OpenedACComboBox.removeSelectedElement();
            }
        }

        _toClose.setVisible(false);
        System.gc();
    }
}
