// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/CloseSelectedVCAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenVCEditDialogAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: CloseSelectedVCAction.java,v $
// Revision 1.1 2005/11/29 18:27:07 chinkumo
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

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.view.OpenedVCComboBox;

public class CloseSelectedVCAction extends AbstractAction {

    private static final long            serialVersionUID = 2246255411910239583L;
    private static CloseSelectedVCAction instance         = null;

    /**
     * @return
     */
    public static CloseSelectedVCAction getInstance(String name) {
        if (instance == null) {
            instance = new CloseSelectedVCAction(name);
        }

        return instance;
    }

    public static CloseSelectedVCAction getInstance() {
        return instance;
    }

    /**
     * @param name
     */
    private CloseSelectedVCAction(String name) {
        super.putValue(Action.NAME, name);
        super.putValue(Action.SHORT_DESCRIPTION, name);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        OpenedVCComboBox.removeSelectedElement();
    }

}
