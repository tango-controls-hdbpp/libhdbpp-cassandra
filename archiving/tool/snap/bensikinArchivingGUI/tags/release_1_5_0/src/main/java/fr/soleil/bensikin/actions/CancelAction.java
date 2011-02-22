// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/CancelAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class CancelAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: CancelAction.java,v $
// Revision 1.2 2006/04/10 08:46:41 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.1 2005/11/29 18:25:08 chinkumo
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
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.containers.sub.dialogs.CancelableDialog;

/**
 * An action representing a cancel button: closes the popup it depend upon.
 * 
 * @author CLAISSE
 */
public class CancelAction extends BensikinAction {

    private static final long serialVersionUID = 125538339053955856L;
    private CancelableDialog  toClose;

    /**
     * Standard action constructor that sets the action's name, plus initializes
     * the reference to the popup to close on <code>actionPerformed</code>
     * 
     * @param name
     *            The name of the action
     * @param _toClose
     *            the JDialog to close
     */
    public CancelAction(String name, CancelableDialog _toClose) {
        super(name);
        this.putValue(Action.NAME, name);
        this.toClose = _toClose;
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent actionEvent) {
        toClose.setVisible(false);
        toClose.cancel();
    }

}
