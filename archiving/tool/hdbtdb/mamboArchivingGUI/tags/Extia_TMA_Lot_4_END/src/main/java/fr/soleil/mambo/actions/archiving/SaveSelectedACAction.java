//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/SaveSelectedACAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SaveSelectedACAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.2 $
//
//$Log: SaveSelectedACAction.java,v $
//Revision 1.2  2005/11/29 18:27:07  chinkumo
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

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;

public class SaveSelectedACAction extends AbstractAction {
	private boolean isSaveAs;

	/**
	 * @param name
	 */
	public SaveSelectedACAction(String name, boolean _isSaveAs) {
		super.putValue(Action.NAME, name);
		super.putValue(Action.SHORT_DESCRIPTION, name);

		this.isSaveAs = _isSaveAs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		IArchivingConfigurationManager manager = ArchivingConfigurationManagerFactory
				.getCurrentImpl();
		ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
				.getSelectedArchivingConfiguration();

		if (selectedArchivingConfiguration == null) {
			return;
		}

		selectedArchivingConfiguration.save(manager, this.isSaveAs);
	}

}
