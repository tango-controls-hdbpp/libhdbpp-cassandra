// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/VCCustomTabbedPaneListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCCustomTabbedPaneListener.
// (Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: VCCustomTabbedPaneListener.java,v $
// Revision 1.3 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.2 2006/05/19 15:03:36 ounsy
// minor changes
//
// Revision 1.1 2005/11/29 18:27:07 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.view.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fr.soleil.mambo.actions.view.VCBackAction;
import fr.soleil.mambo.actions.view.VCFinishAction;
import fr.soleil.mambo.actions.view.VCNextAction;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;

public class VCCustomTabbedPaneListener implements PropertyChangeListener {

	public static final String SELECTED_PAGE = "SELECTED_PAGE";
	private VCEditDialog editDialog;

	public VCCustomTabbedPaneListener(VCEditDialog editDialog) {
		super();
		this.editDialog = editDialog;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String prop = event.getPropertyName();

		if (SELECTED_PAGE.equals(prop)) {
			Integer newValue_o = (Integer) event.getNewValue();

			int newValue = newValue_o.intValue();

			VCBackAction backAction = editDialog.getBackAction();
			VCNextAction nextAction = editDialog.getNextAction();
			VCFinishAction finishAction = editDialog.getFinishAction();

			switch (newValue) {
			case 0:
				backAction.setEnabled(false);
				nextAction.setEnabled(true);
				finishAction.setEnabled(false);
				break;

			case 1:
				backAction.setEnabled(true);
				nextAction.setEnabled(true);
				finishAction.setEnabled(false);
				break;

			case 2:
				backAction.setEnabled(true);
				nextAction.setEnabled(true);
				finishAction.setEnabled(true);
				break;

			case 3:
				backAction.setEnabled(true);
				nextAction.setEnabled(false);
				finishAction.setEnabled(true);
				break;
			}
		}
	}

}
