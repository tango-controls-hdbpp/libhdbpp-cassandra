// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/ResetAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ResetAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ResetAction.java,v $
// Revision 1.3 2006/04/05 13:40:04 ounsy
// optimization
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
import javax.swing.Icon;

import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public class ResetAction extends AbstractAction {

    private static final long serialVersionUID = -240509498918466405L;

    /**
     * @param name
     */
    public ResetAction(final String name, final Icon icon) {
	super(name, icon);
	putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
	final ArchivingConfiguration newAC = new ArchivingConfiguration();
	ArchivingConfiguration.setCurrentArchivingConfiguration(newAC);
	ArchivingConfiguration.setSelectedArchivingConfiguration(newAC);
	newAC.push();

	final OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
	openedVCComboBox.empty();

	final OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
	openedACComboBox.empty();
    }
}
