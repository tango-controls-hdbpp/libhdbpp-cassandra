// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/SaveSelectedVCAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SaveSelectedVCAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: SaveSelectedVCAction.java,v $
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

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;

public class SaveSelectedVCAction extends AbstractAction {

	private static final long serialVersionUID = -3086842926392495083L;
	private boolean isSaveAs;

	/**
	 * @param name
	 */
	public SaveSelectedVCAction(String name, boolean _isSaveAs) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);

		this.isSaveAs = _isSaveAs;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		ViewConfigurationBeanManager.getInstance().saveVC(
				ViewConfigurationBeanManager.getInstance()
						.getSelectedConfiguration(), isSaveAs);
	}

}
